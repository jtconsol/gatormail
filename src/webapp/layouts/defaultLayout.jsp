<%@page contentType="text/html"%>
<%@taglib uri="/tags/struts-tiles" prefix="tiles"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<html:html locale="true" xhtml="true">
<head>
 <title>
  <bean:message key="site.title"/>
  <tiles:getAsString name="title"/>
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


 <table class="body" width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
   <td class="topNavBar">
     <tiles:get name="navBar"/>
   </td>
  </tr>
  <tr>
   <td>
     <tiles:get name="results"/>
   </td>
  </tr>
  <tr>
   <td>
     <tiles:get name="body"/>
   </td>
  </tr>
 </table>

 <div class="footer">
  <tiles:get name="footer"/>
 </div>

</body>
</html:html>