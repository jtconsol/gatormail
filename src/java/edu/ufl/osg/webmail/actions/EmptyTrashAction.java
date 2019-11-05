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
import edu.ufl.osg.webmail.beans.ResultBean;
import edu.ufl.osg.webmail.util.Util;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Permanently deletes everything in the trash folder.
 *
 * @author drakee
 * @version $Revision: 1.2 $
 */
public class EmptyTrashAction extends Action {
    private static final Logger logger = Logger.getLogger(EmptyTrashAction.class.getName());

    /**
     * Sets up the request enviroment for the folder view. The current folder
     * and a List of messages are put in the request attributes. If there is no
     * folder parameter of the request URL forward the user to their INBOX.
     *
     * @param  mapping             The ActionMapping used to select this
     *                             instance
     * @param  form                The optional ActionForm bean for this request
     *                             (if any)
     * @param  request             The HTTP request we are processing
     * @param  response            The HTTP response we are creating
     * @return                     An ActionForward instance to either their
     *                             inbox or the view.
     * @throws Exception           if the application business logic throws an
     *                             exception
     */
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        logger.debug("=== EmptyTrashAction.execute() begin ===");
        ActionsUtil.checkSession(request);

        Folder trashFolder = null;
        try {
        trashFolder = Util.getFolder(request.getSession(), Constants.TRASH_FOLDER_FULLNAME);
        if (trashFolder != null) {
            final Message[] messages = trashFolder.getMessages();
            // make sure all items in trash are marked for delete
            for (int i = 0; i < messages.length; i++) {
                messages[i].setFlag(Flag.DELETED, true);
                //messages[i].saveChanges();
                logger.debug("marked message " + i + " for deletion.");
            }
            // empty trash
            trashFolder.expunge();
            logger.debug("trash folder expunged.");

            // set success message
            final ResultBean result = new ResultBean();
            if (messages.length == 1) {
                result.setMessage(Util.getFromBundle("emptyTrash.result.single.success"));
            } else {
                result.setMessage(Util.getFromBundle("emptyTrash.result.multiple.success"), String.valueOf(messages.length));
            }
            request.setAttribute(Constants.RESULT, result);
        }
        } finally {
            Util.releaseFolder(trashFolder);
        }

        logger.debug("=== EmptyTrashAction.execute() end ===");
        return mapping.findForward("success");
    }
}
