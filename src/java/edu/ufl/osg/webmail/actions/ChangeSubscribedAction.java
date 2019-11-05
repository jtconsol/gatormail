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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import javax.mail.Folder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * Subscribes and unsubscribes a folder.
 *
 * @author drakee
 * @version $Revision: 1.2 $
 */
public class ChangeSubscribedAction extends LookupDispatchAction {
    private static final Logger logger = Logger.getLogger(ChangeSubscribedAction.class.getName());
    private Map map = new HashMap();

    public ChangeSubscribedAction() {
        map.put("button.subscribeFolder", "subscribeFolder");
        map.put("button.unsubscribeFolder", "unsubscribeFolder");
    }

    protected Map getKeyMethodMap() {
        return map;
    }

    /**
     * Makes folder subscribed.
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
    public ActionForward subscribeFolder(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        logger.debug("=== ChangeSubscribedAction.subscribeFolder() begin ...");
        ActionsUtil.checkSession(request);

        Folder folder = null;
        try {
            folder = ActionsUtil.fetchFolder(form, request);

            // make folder subscribed
            folder.setSubscribed(true);

        } finally {
            Util.releaseFolder(folder); // clean up
        }

        // set success message for the upcoming view
        request.setAttribute(Constants.RESULT, new ResultBean(Util.getFromBundle("changeSubscription.result.subscribed.success")));

        logger.debug("=== ChangeSubscribedAction.subscribeFolder() end ===");
        return mapping.findForward("success");
    }


    /**
     * Makes folder unsubscribed.
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
    public ActionForward unsubscribeFolder(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        logger.debug("=== ChangeSubscribedAction.unsubscribeFolder() begin ...");
        ActionsUtil.checkSession(request);

        Folder folder = null;
        try {
            folder = ActionsUtil.fetchFolder(form, request);

            // make sure user isn't trying to unsubscribe the INBOX
            if (folder.getFullName().equals("INBOX")) {

                final ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.changeSubscription.unsubscribe.inbox"));
                saveErrors(request, errors);
                return mapping.findForward("fail");
            }

            // make folder subscribed or unsubscribed
            folder.setSubscribed(false);

        } finally {
            Util.releaseFolder(folder); // clean up
        }

        // set success message for the upcoming view
        request.setAttribute(Constants.RESULT, new ResultBean(Util.getFromBundle("changeSubscription.result.unsubscribed.success")));

        logger.debug("=== ChangeSubscribedAction.unsubscribeFolder() end ===");
        return mapping.findForward("success");
    }
}
