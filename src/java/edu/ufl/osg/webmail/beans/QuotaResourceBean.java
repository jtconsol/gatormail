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

package edu.ufl.osg.webmail.beans;

/**
 * Holds Quota Resource information, for JSP containers that can't see
 * JavaMail >= 1.3.
 *
 * @author sandymac
 * @version $Revision: 1.2 $
 */
public class QuotaResourceBean {

    private String name;
    private long usage;
    private long limit;

    public QuotaResourceBean() {
    }

    public QuotaResourceBean(final String name, final long usage, final long limit) {
        this.name = name;
        this.usage = usage;
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getUsage() {
        return usage;
    }

    public void setUsage(final long usage) {
        this.usage = usage;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(final long limit) {
        this.limit = limit;
    }

    public String toString() {
        return name + ": usage = " + usage + ", limit = " + limit;
    }
}
