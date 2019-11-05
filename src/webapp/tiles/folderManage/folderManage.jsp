<%@page contentType="text/html" import="edu.ufl.osg.webmail.util.Util,
                                        java.util.Map,
                                        java.util.HashMap,
                                        javax.mail.Folder"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<table class="headerTable" cellpadding="3" cellspacing="0" width="100%">
  <tr class="header">
    <th colspan="6"><bean:message key="folderManage.title"/></th>
  </tr>
  <tr class="subheader">
    <td><bean:message key="folderManage.table.folder"/></td>
    <td width="10%"><bean:message key="folderManage.table.numMessages"/></td>
    <td width="10%"><bean:message key="folderManage.table.subscribed"/></td>
    <td width="10%"><bean:message key="folderManage.table.modify"/></td>
    <td width="10%"><bean:message key="folderManage.table.delete"/></td>
    <td width="30%">&nbsp;</td>
  </tr>
  <c:forEach items="${folderBeanList}" var="folder" varStatus="folderStatus">
<%
    Folder folder = (Folder)pageContext.getAttribute("folder");
    Map folderParams = new HashMap();
    folderParams.put("folder", folder.getFullName());
    pageContext.setAttribute("folderParams", folderParams);
%>
<c:choose>
 <c:when test="${folderStatus.index % 2 == 0}">
  <tr>
 </c:when>
 <c:otherwise>
  <tr class="altrow">
 </c:otherwise>
</c:choose>
       <td><html:link forward="folder" paramId="folder" paramName="folder" paramProperty="fullName">
	   <bean:write name="folder" property="fullName" filter="true"/></html:link>
       </td>
       <td align="right">
<% // TODO Figure out how to predict when this will throw an exception.
    try {
%>
<bean:write name="folder" property="messageCount"/>
<%
    } catch (Exception e) {
        // Ignore the error
    }
%>
       </td>
       <td align="center">
       <c:choose>
        <c:when test="${folder.subscribed}">
         <bean:message key="folderManage.table.yes"/>
        </c:when>
        <c:otherwise>
         <bean:message key="folderManage.table.no"/>
        </c:otherwise>
       </c:choose>
       </td>
       <td><html:link forward="folderManageModify" name="folderParams" scope="page">
	   <bean:message key="folderManage.table.modify"/></html:link>
       </td>
       <td>
<%
    if (!Util.isReservedFolder(folder.getFullName())) {
%>
	   <html:link forward="deleteFolder" name="folderParams" scope="page">
	   <bean:message key="folderManage.table.delete"/></html:link>
<%
    }
%>
       </td>
       <td width="30%">&nbsp;</td>
     </tr>
  </c:forEach>

</table>