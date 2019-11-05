<%@page contentType="text/html; charset=iso-8859-1"%>
<%@taglib uri="/tags/struts-tiles" prefix="tiles"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<tiles:insert definition="folderManageLayout">
  <tiles:put name="title">
    <bean:message key="folderManageModify.title"/>
  </tiles:put>
  <tiles:put name="body" value="/tiles/folderManage/folderManageModify.jsp"/>
</tiles:insert>
