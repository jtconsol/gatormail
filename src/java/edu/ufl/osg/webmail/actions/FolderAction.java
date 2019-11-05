/*
 * This file is part of GatorMail, a servlet based webmail.
 * Copyright (C) 2002, 2003 William A. McArthur, Jr.
 * Copyright (C) 2003 The Open Systems Group / University of Florida
 *
 * GatorMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GatorMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GatorMail; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package edu.ufl.osg.webmail.actions;

import edu.ufl.osg.webmail.Constants;
import edu.ufl.osg.webmail.User;
import edu.ufl.osg.webmail.prefs.PreferencesProvider;
import edu.ufl.osg.webmail.forms.FolderForm;
import edu.ufl.osg.webmail.util.Util;
import edu.ufl.osg.webmail.util.Util.DateSort;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.sun.mail.imap.IMAPFolder;


/**
 * Sets up Folder/Message listing view.
 *
 * @author sandymac
 * @author drakee
 * @version $Revision: 1.5 $
 */
public class FolderAction extends Action {
    private static final Logger logger = Logger.getLogger(FolderAction.class.getName());

    private static final List filterChoices;

    static {
        final List list = new ArrayList();
        list.add("none");
        list.add("subjectAndSender");
        list.add("subject");
        list.add("sender");
        list.add("toRecipient");
        list.add("allRecipients");
        list.add("new");
        list.add("seen");
        list.add("replied");
        list.add("hasAttachment");
        list.add("noAttachment");
        list.add("addressBook");
        list.add("notAddressBook");
        list.add("flaggedJunk");
        list.add("notFlaggedJunk");
        filterChoices = Collections.unmodifiableList(list);
    }

    /**
     * Sets up the request enviroment for the folder view. The current folder
     * and a List of messages are put in the request attributes. If there is no
     * folder parameter of the request URL forward the user to their INBOX.
     *
     * @param mapping  The ActionMapping used to select this
     *                 instance
     * @param form     The optional ActionForm bean for this request
     *                 (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return An ActionForward instance to either their
     *         inbox or the view.
     * @throws Exception if the application business logic throws an
     *                   exception
     */
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        logger.debug("=== FolderAction.execute() begin ===");
        ActionsUtil.checkSession(request);

        final FolderForm folderForm = (FolderForm) form;

        // Get the current Folder
        final Folder folder = ActionsUtil.fetchFolder(folderForm, request);
        request.setAttribute("folder", folder);

        if (folder == null) {
            Util.releaseFolder(folder); // clean up
            return mapping.findForward("inbox");
        }

        final HttpSession session = request.getSession();
        final User user = Util.getUser(session);
        final PreferencesProvider pp = (PreferencesProvider) getServlet().getServletContext().getAttribute(Constants.PREFERENCES_PROVIDER);
        final Properties prefs = pp.getPreferences(user, session);

        // Get valid Messages in the folder
        final List messageList;
        final List addressBookList = Util.getAddressList(request.getSession(true));
        final Message[] messages;

        final SearchTerm searchTerms = buildSearchFilter(folderForm.getFilterType(), folderForm.getFilter(), addressBookList, prefs);
        if (searchTerms != null) {
            messages = folder.search(searchTerms);
            prefetchMessageFlags(folder, messages);
            messageList = ActionsUtil.buildMessageList(messages);

            request.setAttribute("folderListFilterType", folderForm.getFilterType());
            request.setAttribute("folderListFilter", folderForm.getFilter());

        } else {
            final Message[] msgs = folder.getMessages();
            prefetchMessageFlags(folder, msgs);
            messageList = ActionsUtil.buildMessageList(msgs);
        }


        flagMessagesWithAttachments(messageList);

        prefetchMessageEnvelopeUID(messageList, folder);

        if (Boolean.valueOf(prefs.getProperty("folder.list.threading", "false")).booleanValue()) {
            prefetchThreadingMessageHeaders(messageList, folder);
        }
        if (Integer.valueOf(prefs.getProperty("message.junk.threashold", "8")).intValue() > 0) {
            prefetchJunkMessageHeaders(messageList, folder);
        }

        // XXX Sort on user's preferences
        Collections.sort(messageList, new DateSort());
        Util.releaseFolder(folder); // clean up

        // Populate the request attributes
        request.setAttribute("messages", messageList);

        final Folder inbox = Util.getFolder(session, "INBOX");

        // Populate Quota info
        final List quotaList = ActionsUtil.getQuotaList(inbox);
        request.setAttribute("quotaList", quotaList);
        Util.releaseFolder(inbox); // clean up

        // Populate the folderNameList
        final List folderList = ActionsUtil.getSusbscribedFolders(Util.getMailStore(session));
        request.setAttribute("folderBeanList", folderList);

        logger.debug("=== FolderAction.execute() end ===");
        return mapping.findForward("success");
    }

    private static void prefetchMessageFlags(final Folder folder, final Message[] msgs) throws MessagingException {
        // Prefetch the flags for all messages.
        final FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.FLAGS);
        folder.fetch(msgs, fp);
    }

    /**
     * Prefetches the {@link FetchProfile.Item#ENVELOPE} and {@link
     * UIDFolder.FetchProfileItem#UID} for each {@link Message}. All messages in
     * <code>messageList</code> must be from <code>folder</code>.
     *
     * @param messageList the list of {@link Message} objects.
     * @param folder      the folder for which to prefetch from.
     * @throws MessagingException  if there is a problem with {@link
     *                             Folder#fetch}.
     * @throws ArrayStoreException if the <code>messageList</code> cannot be
     *                             converted to a <code>{@link
     *                             Message}[]</code>.
     */
    private static void prefetchMessageEnvelopeUID(final List messageList, final Folder folder) throws MessagingException, ArrayStoreException {
        // Prefetch the date headers for all messages.
        final FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(UIDFolder.FetchProfileItem.UID);
        final Message[] msgs = (Message[]) messageList.toArray(new Message[]{});
        folder.fetch(msgs, fp);
    }

    /**
     * Prefetches the <code>References</code> and <code>In-Reply-To</code> {@link IMAPFolder.FetchProfileItem#HEADERS}
     * for each {@link Message}. All messages in <code>messageList</code> must be from <code>folder</code>.
     *
     * @param messageList the list of {@link Message} objects.
     * @param folder      the folder for which to prefetch from.
     * @throws MessagingException  if there is a problem with {@link
     *                             Folder#fetch}.
     * @throws ArrayStoreException if the <code>messageList</code> cannot be
     *                             converted to a <code>{@link
     *                             Message}[]</code>.
     */
    private static void prefetchThreadingMessageHeaders(final List messageList, final Folder folder) throws MessagingException, ArrayStoreException {
        // Prefetch the threading headers for all messages.
        final FetchProfile fp = new FetchProfile();
        //fp.add(IMAPFolder.FetchProfileItem.HEADERS);
        fp.add("References");
        fp.add("In-Reply-To");
        final Message[] msgs = (Message[]) messageList.toArray(new Message[]{});
        folder.fetch(msgs, fp);
    }

    /**
     * Prefetches the <code>X-Spam-Leve</code> {@link IMAPFolder.FetchProfileItem#HEADERS} for each
     * {@link Message}. All messages in <code>messageList</code> must be from <code>folder</code>.
     *
     * @param messageList the list of {@link Message} objects.
     * @param folder      the folder for which to prefetch from.
     * @throws MessagingException  if there is a problem with {@link
     *                             Folder#fetch}.
     * @throws ArrayStoreException if the <code>messageList</code> cannot be
     *                             converted to a <code>{@link
     *                             Message}[]</code>.
     */
    private static void prefetchJunkMessageHeaders(final List messageList, final Folder folder) throws MessagingException, ArrayStoreException {
        // Prefetch the spam headers for all messages.
        final FetchProfile fp = new FetchProfile();
        //fp.add(IMAPFolder.FetchProfileItem.HEADERS);
        fp.add("X-Spam-Level");
        //fp.add("X-Spam-Status");
        final Message[] msgs = (Message[]) messageList.toArray(new Message[]{});
        folder.fetch(msgs, fp);
    }

    private static void flagMessagesWithAttachments(final List messages) {
        final Iterator iter = messages.iterator();
        int msgsFlagged = 0;
        while (iter.hasNext()) {
            try {
                if (flagMessageWithAttachments((Message) iter.next())) {
                    msgsFlagged++;
                }
            } catch (Exception e) {
                logger.warn("Problem figuring out attachment state of message", e);
            }
            // Break out after the first 50 messages so we don't spend all day flagging messages.
            // The next time they view that folder it will flag another 50 messages if they need it.
            if (msgsFlagged > 50) {
                break;
            }
        }
    }

    /**
     * @param message The message to look for atachments in.
     * @return true if the message was flagged.
     * @throws MessagingException JavaMail problem
     */
    private static boolean flagMessageWithAttachments(final Message message) throws MessagingException {
        boolean hasAttachment = false;
        final boolean seen = message.isSet(Flags.Flag.SEEN);
        Flags flags = message.getFlags();

        if (flags.contains(Constants.MESSAGE_FLAG_HAS_ATTACHMENT) || flags.contains(Constants.MESSAGE_FLAG_NO_ATTACHMENT)) {
            return false;
        }

        // No pre-existing flags

        if (message.isMimeType("text/*")) {
            hasAttachment = false;

        } else if (message.isMimeType("multipart/*")) {
            try {
                final MimeMultipart mmp = (MimeMultipart) message.getContent();
                hasAttachment = Util.hasAttachment(mmp);
            } catch (Exception e) {
                // Do nothing
            }
        }

        if (hasAttachment) {
            flags = new Flags(Constants.MESSAGE_FLAG_HAS_ATTACHMENT);
            message.setFlags(flags, true);
        } else {
            flags = new Flags(Constants.MESSAGE_FLAG_NO_ATTACHMENT);
            message.setFlags(flags, true);
        }

        // Reset the seen state if we need to.
        if (!seen) {
            message.setFlag(Flags.Flag.SEEN, false);
        }

        return true;
    }

    private static SearchTerm buildSearchFilter(final String type, final String filter, final List addressBookList, final Properties prefs) {
        SearchTerm searchTerm = null;
        if (filter == null || type == null) {
            return null;
        }
        if (type.equals("none")) {
            return null;

        } else if (type.equals("subjectAndSender")) {
            final FromStringTerm fst = new FromStringTerm(filter);
            final SubjectTerm ist = new SubjectTerm(filter);
            searchTerm = new OrTerm(fst, ist);

        } else if (type.equals("subject")) {
            searchTerm = new SubjectTerm(filter);

        } else if (type.equals("sender")) {
            searchTerm = new FromStringTerm(filter);

        } else if (type.equals("toRecipient")) {
            searchTerm = new RecipientStringTerm(Message.RecipientType.TO, filter);

        } else if (type.equals("allRecipients")) {
            final SearchTerm[] orTerms = new SearchTerm[3];
            orTerms[0] = new RecipientStringTerm(Message.RecipientType.TO, filter);
            orTerms[1] = new RecipientStringTerm(Message.RecipientType.CC, filter);
            orTerms[2] = new RecipientStringTerm(Message.RecipientType.BCC, filter);
            searchTerm = new OrTerm(orTerms);

        } else if (type.equals("new")) {
            final Flags f = new Flags();
            f.add(Flags.Flag.SEEN);
            searchTerm = new FlagTerm(f, false);

        } else if (type.equals("seen")) {
            final Flags f = new Flags();
            f.add(Flags.Flag.SEEN);
            searchTerm = new FlagTerm(f, true);

        } else if (type.equals("replied")) {
            final Flags f = new Flags();
            f.add(Flags.Flag.ANSWERED);
            searchTerm = new FlagTerm(f, true);

        } else if (type.equals("hasAttachment")) {
            // TODO: Rewrite this using FlagTerm
            searchTerm = new AttachmentTerm();

        } else if (type.equals("noAttachment")) {
            // TODO: Rewrite this using FlagTerm
            searchTerm = new NotTerm(new AttachmentTerm());

        } else if (type.equals("addressBook")) {
            if (addressBookList.size() != 0) {
                final FromEmailTerm[] fromAddresses = new FromEmailTerm[addressBookList.size()];
                for (int i = 0; i < fromAddresses.length; i++) {
                    fromAddresses[i] = new FromEmailTerm((InternetAddress) addressBookList.get(i));
                }
                searchTerm = new OrTerm(fromAddresses);
            }


        } else if (type.equals("notAddressBook")) {
            if (addressBookList.size() != 0) {
                final FromEmailTerm[] fromAddresses = new FromEmailTerm[addressBookList.size()];
                for (int i = 0; i < fromAddresses.length; i++) {
                    fromAddresses[i] = new FromEmailTerm((InternetAddress) addressBookList.get(i));
                }
                searchTerm = new NotTerm(new OrTerm(fromAddresses));
            }

        } else if (type.equals("flaggedJunk")) {
            final String junkPattern = buildJunkPattern(prefs);
            if (junkPattern != null) {
                searchTerm = new HeaderTerm("X-Spam-Level", junkPattern);
            }


        } else if (type.equals("notFlaggedJunk")) {
            final String junkPattern = buildJunkPattern(prefs);
            if (junkPattern != null) {
                searchTerm = new NotTerm(new HeaderTerm("X-Spam-Level", junkPattern));
            }

        }
        return searchTerm;
    }

    private static String buildJunkPattern(final Properties prefs) {
        final int junkThreashold = Integer.valueOf(prefs.getProperty("message.junk.threashold", "8")).intValue();
        final String junkPattern;
        if (junkThreashold > 0) {
            junkPattern = "***************************************".substring(0, junkThreashold);
        } else {
            junkPattern = null;
        }
        return junkPattern;
    }

    private static class AttachmentTerm extends SearchTerm {
        public boolean match(final Message message) {
            try {
                return Util.hasAttachment(message);
            } catch (MessagingException e) {
                return false;
            }
        }

    }

    /**
     * From an address in the users address book.
     */
    private static class FromEmailTerm extends AddressTerm {
        private FromEmailTerm(final InternetAddress address) {
            super(address);
        }

        public boolean match(final Message message) {
            final Address[] address;
            try {
                address = message.getFrom();
            } catch (Exception _ex) {
                return false;
            }
            if (address == null) {
                return false;
            }
            for (int i = 0; i < address.length; i++) {
                if (address[i] instanceof InternetAddress) {
                    final InternetAddress iAddr = (InternetAddress) address[i];
                    if (iAddr.getAddress().toLowerCase().equals(((InternetAddress) getAddress()).getAddress().toLowerCase())) {
                        return true;
                    }

                }

            }

            return false;
        }

    }

    public static List getFilterChoices() {
        return Collections.unmodifiableList(filterChoices);
    }
}
