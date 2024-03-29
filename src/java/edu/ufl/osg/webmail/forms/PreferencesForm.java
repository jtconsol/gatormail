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

package edu.ufl.osg.webmail.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Form bean for the user's preferences.
 *
 * @author sandymac
 * @since Aug 28, 2003 1:13:03 PM
 * @version $Revision: 1.4 $
 */
public class PreferencesForm extends ActionForm {
    private static final Logger logger = Logger.getLogger(PreferencesForm.class.getName());

    private String action;

    private String username;
    private String replyTo;
    private String signature;
    private String imageUrl;
    private String maxInactiveMultiplier;
    private Boolean threading;
    private Boolean hideHeader;
    private String junkThreashold;

    public void reset(final ActionMapping actionMapping, final HttpServletRequest request) {
        super.reset(actionMapping, request);
        setAction(null);
        setSignature(null);
        setReplyTo(null);
        setUsername(null);
        setMaxInactiveMultiplier(null);
        setThreading(Boolean.FALSE);
        setHideHeader(Boolean.FALSE);
        setJunkThreashold(null);
    }

    public ActionErrors validate(final ActionMapping actionMapping, final HttpServletRequest request) {
        ActionErrors errors = super.validate(actionMapping, request);
        if (errors == null) {
            errors = new ActionErrors();
        }

        final String replyTo = getReplyTo();
        if (replyTo != null && replyTo.length() > 0) {
            try {
                new InternetAddress(replyTo).validate();
            } catch (AddressException ae) {
                errors.add("replyTo", new ActionError("preferences.replyTo.invalid", ae.getMessage()));
            }
        }

        final String imageUrl = getImageUrl();
        if (imageUrl != null && imageUrl.length() > 0) {
            try {
                new URL(imageUrl);
            } catch (MalformedURLException mue) {
                logger.error("imageUrl", mue);
                errors.add("imageUrl", new ActionError("preferences.imageUrl.invalid", mue.getMessage()));
            }
        }

        if (getMaxInactiveMultiplier() != null) {
            try {
                final float maxInactiveMultiplier = Float.parseFloat(getMaxInactiveMultiplier());
                if (maxInactiveMultiplier < 0.1 || 10 < maxInactiveMultiplier) {
                    errors.add("maxInactiveMultiplier", new ActionError("preferences.maxInactiveMultiplier.invalid"));
                }
            } catch (NumberFormatException nfe) {
                errors.add("maxInactiveMultiplier", new ActionError("preferences.maxInactiveMultiplier.invalid"));
            }
        }

        if (getJunkThreashold() != null) {
            try {
                final int junkThreahsold = Integer.parseInt(getJunkThreashold());
                if (junkThreahsold < 0 || 15 < junkThreahsold) {
                    errors.add("junkThreahsold", new ActionError("preferences.junkThreahsold.invalid"));
                }
            } catch (NumberFormatException nfe) {
                errors.add("junkThreahsold", new ActionError("preferences.junkThreahsold.invalid"));
            }
        }

        return errors;
    }

    private static String zeroLengthToNull(final String string) {
        if (string != null && string.length() == 0) {
            return null;
        } else {
            return string;
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username != null ? username.trim() : username;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(final String replyTo) {
        this.replyTo = replyTo != null ? zeroLengthToNull(replyTo.trim()) : replyTo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(final String signature) {
        this.signature = signature;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl != null ? zeroLengthToNull(imageUrl.trim()) : imageUrl;
    }

    public String getMaxInactiveMultiplier() {
        return maxInactiveMultiplier;
    }

    public void setMaxInactiveMultiplier(final String maxInactiveMultiplier) {
        this.maxInactiveMultiplier = maxInactiveMultiplier;
    }

    public Boolean getThreading() {
        return threading;
    }

    public void setThreading(final Boolean threading) {
        this.threading = threading;
    }

    public Boolean getHideHeader() {
        return hideHeader;
    }

    public void setHideHeader(final Boolean hideHeader) {
        this.hideHeader = hideHeader;
    }

    public String getJunkThreashold() {
        return junkThreashold;
    }

    public void setJunkThreashold(String junkThreashold) {
        this.junkThreashold = junkThreashold;
    }
}
