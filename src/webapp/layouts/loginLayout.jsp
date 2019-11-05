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
 <link rel="stylesheet" type="text/css" href="<html:rewrite forward="CSS"/>"/>
</head>
<body>

<center>
 <div class="header">
  <tiles:get name="header"/>
 </div>

 <div class="body">
  <tiles:get name="body"/>
 </div>

 <div class="footer">
  <tiles:get name="footer"/>
 </div>
</center>

</body>
</html:html>
