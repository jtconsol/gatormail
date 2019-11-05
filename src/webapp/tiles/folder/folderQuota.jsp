<%@page contentType="text/html" import="edu.ufl.osg.webmail.util.Util,
                                        javax.mail.Folder,
                                        com.sun.mail.imap.IMAPFolder,
                                        edu.ufl.osg.webmail.beans.QuotaResourceBean"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<%
    Folder folder = (Folder)request.getAttribute("folder");
    folder = Util.getFolder(folder);
    if (!(folder instanceof IMAPFolder)) {
        out.println("<!-- This folder does not support quotas. -->");
        return;
    }

    Util.releaseFolder(folder);

    if (request.getAttribute("quotaList") == null) {
        out.println("<!-- No quota list was supplied. -->");
        return;
    }
%>
<table class="folderQuota" width="150" align="center" cellpadding="0" cellspacing="0">
 <tr>
  <th class="folderQuotaTitle"><bean:message key="folder.quota.title"/></th>
 </tr>

<c:forEach items="${quotaList}" var="quotaResources">
 <tr>
  <td>

 <c:forEach items="${quotaResources}" var="resource">
<%
    QuotaResourceBean resource = (QuotaResourceBean)pageContext.getAttribute("resource");
    long usage = (long)((double)resource.getUsage() / (double)resource.getLimit() * 100d);
%>
   <table class="folderQuotaResource" border="0" cellspacing="0">
    <tr>
     <th colspan="2"><bean:write name="resource" property="name"/></th>
    </tr>
    <tr>
     <td class="folderQuotaResourceUsed" width="<%= (int)(Math.max(1, usage) * 1.5) %>" title="<%= usage + "% used" %>">&nbsp;</td>
     <td class="folderQuotaResourceFree" width="<%= (int)(Math.min(99, 100 - usage) * 1.5) %>" title="<%= (100 - usage) + "% free" %>">&nbsp;</td>
    </tr>
    <tr>
     <td colspan="2" class="folderQuotaMessage">
      <bean:message key="folder.quota.message" arg0="<%= Long.toString(resource.getUsage()) %>" arg1="<%= Long.toString(resource.getLimit()) %>"/>
     </td>
    </tr>
   </table>
 </c:forEach>

  </td>
 </tr>
</c:forEach>

</table>