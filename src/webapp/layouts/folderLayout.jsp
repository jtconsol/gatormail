<%@page contentType="text/html"%>
<%@taglib uri="/tags/struts-tiles" prefix="tiles"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<html:html locale="true" xhtml="true">
<head>
 <title>
  <bean:message key="site.title"/>
  <bean:message key="folder.title"/>
  <tiles:getAsString name="folderName"/>
 </title>
 <html:base/>
 <link rel="start" href="<html:rewrite forward="INBOX"/>" title="INBOX"/>
 <link rel="help" href="<html:rewrite forward="help"/>" title="GatorMail Help"/>
 <link rel="stylesheet" type="text/css" href="<html:rewrite forward="CSS"/>"/>
</head>
<body>

 <div class="header">
  <tiles:get name="header"/>
 </div>

 <div class="body">
  <div class="topNavBar">
   <tiles:get name="navBar"/>
  </div>

  <table width="100%" border="0" cellpadding="0" cellspacing="0">
   <tr>
    <%-- Left side content --%>
    <td valign="top" class="leftNavBar" width="170" rowspan="100">
     <tiles:get name="folderList"/>
<% //TODO: Fix this.
try {
//java.lang.reflect.Method quotaMethod = com.sun.mail.imap.IMAPFolder.class.getMethod("getQuota", null);
java.lang.reflect.Method quotaMethod = request.getAttribute("folder").getClass().getMethod("getQuota", null);

%>
     <hr align="center" width="90%">
     <tiles:get name="emptyTrash"/>
     <tiles:get name="folderQuota"/>
<%
} catch (NoSuchMethodException nsme) {
// do nothing
}
%>
    </td>

    <%-- Right side content --%>
    <td valign="top">
     <table cellpadding="0" cellspacing="0" width="100%">
      <tr>
       <td>
        <tiles:get name="results"/>
       </td>
      </tr>

      <tr>
       <td>
        <tiles:get name="messageListHeader"/>
       </td>
      </tr>

      <tr>
       <td>
        <tiles:get name="messageListView"/>
       </td>
      </tr>

      <tr>
       <td valign="top">
        <tiles:get name="messageList"/>
       </td>
      </tr>
     </table>
    </td>
   </tr>
  </table>

 </div>

 <div class="footer">
  <tiles:get name="footer"/>
 </div>

</body>
</html:html>