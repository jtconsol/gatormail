<%@ page import="javax.mail.Folder,
                 edu.ufl.osg.webmail.util.Util"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<%
    Folder folder = (Folder)request.getAttribute("folder");
    folder = Util.getFolder(folder);
%>
<table width="100%" border="0" cellpadding="4" cellspacing="0" class="folderMessageList">
 <tr class="darkBlueRow">
  <th>
   <bean:message key="folder.view.folder.info" arg0="<%= folder.getFullName() %>" arg1="<%= String.valueOf(folder.getMessageCount()) %>" arg2="<%= String.valueOf(folder.getUnreadMessageCount()) %>" arg3="<%= String.valueOf(folder.getNewMessageCount()) %>" />
  </th>
 </tr>
</table>
<%
    Util.releaseFolder(folder);
%>