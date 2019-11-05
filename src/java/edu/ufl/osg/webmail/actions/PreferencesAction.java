/*
 * This file is part of GatorMail, a servlet based webmail.
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
import edu.ufl.osg.webmail.forms.PreferencesForm;
import edu.ufl.osg.webmail.prefs.PreferencesProvider;
import edu.ufl.osg.webmail.util.Util;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Properties;

/**
 * Loads and saves the peferences.
 *
 * @author sandymac
 * @since  Aug 28, 2003 1:28:15 PM
 * @version $Revision: 1.4 $
 */
public final class PreferencesAction extends Action {
    private static final Logger logger = Logger.getLogger(PreferencesAction.class.getName());

    public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        logger.debug("=== PreferencesAction.execute() begin ===");
        ActionsUtil.checkSession(request);
        final HttpSession session = request.getSession();
        final PreferencesForm prefsForm = (PreferencesForm)form;
        final User user = Util.getUser(session);

        final PreferencesProvider pp = (PreferencesProvider)getServlet().getServletContext().getAttribute(Constants.PREFERENCES_PROVIDER);
        final Properties prefs = pp.getPreferences(user, session);


        final String imageUrl = prefsForm.getImageUrl();
        if (prefsForm.getAction() == null) {
            // Populate form bean from perferences.
            prefsForm.setUsername(prefs.getProperty("user.name"));
            prefsForm.setReplyTo(prefs.getProperty("compose.replyTo"));
            prefsForm.setSignature(prefs.getProperty("compose.signature"));
            prefsForm.setImageUrl(prefs.getProperty("compose.X-Image-Url"));
            prefsForm.setMaxInactiveMultiplier(prefs.getProperty("session.maxInactive.multiplier"));
            prefsForm.setJunkThreashold(prefs.getProperty("message.junk.threashold", "8"));
            if (prefs.getProperty("folder.list.threading") != null) {
                prefsForm.setThreading(Boolean.valueOf(prefs.getProperty("folder.list.threading")));
            }
            if (prefs.getProperty("view.header.hide") != null) {
                prefsForm.setHideHeader(Boolean.valueOf(prefs.getProperty("view.header.hide")));
            }

        } else {
            // Update preferences from the form bean.
            final String username = prefsForm.getUsername();
            if (username == null && prefs.getProperty("user.name") != null) {
                prefs.remove("user.name");
            } else if (username != null && !username.equals(prefs.getProperty("user.name"))) {
                prefs.setProperty("user.name", username);
            }

            final String replyTo = prefsForm.getReplyTo();
            if (replyTo == null && prefs.getProperty("compose.replyTo") != null) {
                prefs.remove("compose.replyTo");
            } else if (replyTo != null && !replyTo.equals(prefs.getProperty("compose.replyTo"))) {
                prefs.setProperty("compose.replyTo", replyTo);
            }

            final String signature = prefsForm.getSignature();
            if (signature == null && prefs.getProperty("compose.signature") != null) {
                prefs.remove("compose.signature");
            } else if (signature != null && !signature.equals(prefs.getProperty("compose.signature"))) {
                prefs.setProperty("compose.signature", signature);
            }

            if (imageUrl == null && prefs.getProperty("compose.X-Image-Url") != null) {
                prefs.remove("compose.X-Image-Url");
            } else if (imageUrl != null && !imageUrl.equals(prefs.getProperty("compose.X-Image-Url"))) {
                prefs.setProperty("compose.X-Image-Url", imageUrl);
            }

            final String maxInactiveMultiplier = prefsForm.getMaxInactiveMultiplier();
            if (maxInactiveMultiplier == null
                    && prefs.getProperty("session.maxInactive.multiplier") != null) {
                prefs.remove("session.maxInactive.multiplier");
            } else if (maxInactiveMultiplier != null && Float.parseFloat(maxInactiveMultiplier) == 1) {
                prefs.remove("session.maxInactive.multiplier");
            } else if (maxInactiveMultiplier != null && !maxInactiveMultiplier.equals(prefs.getProperty("session.maxInactive.multiplier"))) {
                prefs.setProperty("session.maxInactive.multiplier", maxInactiveMultiplier);
            }

            final String junkThreashold = prefsForm.getJunkThreashold();
            if (junkThreashold == null
                    && prefs.getProperty("message.junk.threashold") != null) {
                prefs.remove("message.junk.threashold");
            } else if (junkThreashold != null && !junkThreashold.equals(prefs.getProperty("message.junk.threashold"))) {
                prefs.setProperty("message.junk.threashold", junkThreashold);
            }

            final Boolean threading = prefsForm.getThreading();
            if (threading == null && prefs.getProperty("folder.list.threading") != null) {
                prefs.remove("folder.list.threading");
            } else if (threading != null
                    && !threading.equals(
                            Boolean.valueOf(
                                    (prefs.getProperty("folder.list.threading") != null) ? prefs.getProperty("folder.list.threading") : "false"
                            )
                    )
            ) {
                prefs.setProperty("folder.list.threading", threading.toString());
            }

            final Boolean hideHeader = prefsForm.getHideHeader();
            if (hideHeader == null && prefs.getProperty("view.header.hide") != null) {
                prefs.remove("view.header.hide");
            } else if (hideHeader != null
                    && !hideHeader.equals(
                            Boolean.valueOf(
                                    (prefs.getProperty("view.header.hide") != null) ? prefs.getProperty("view.header.hide") : "false"
                            )
                    )
            ) {
                prefs.setProperty("view.header.hide", hideHeader.toString());
            }
        }

        if (imageUrl != null) {
            request.setAttribute("X-Image-Url", imageUrl);
        } else {
            request.setAttribute("X-Image-Url", prefs.getProperty("compose.X-Image-Url"));
        }

        logger.debug("=== PreferencesAction.execute() end ===");
        return mapping.findForward("success");
    }
}
