<%@page contentType="text/html; charset=iso-8859-1"
        import="edu.ufl.osg.webmail.Constants"
%>
<%@taglib uri="/tags/struts-tiles" prefix="tiles"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<tiles:insert definition="errorLayout">
 <tiles:put name="title">
  <bean:message key="errorCopy.title.reserved" arg0="<%=Constants.TRASH_FOLDER%>"/>
 </tiles:put>
 <tiles:put name="body" value="/tiles/error/errorCopyToTrash.jsp"/>
</tiles:insert>