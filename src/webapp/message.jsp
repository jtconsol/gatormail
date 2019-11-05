<%@ page import="javax.mail.Message,
                 edu.ufl.osg.webmail.util.Util"%>
<%@page contentType="text/html; charset=iso-8859-1"%>
<%@taglib uri="/tags/struts-tiles" prefix="tiles"%>
<%
    Util.getFolder(((Message)request.getAttribute("message")).getFolder());
%>
<tiles:insert definition="messageLayout">
  <tiles:put name="messageName"><%= ((Message)request.getAttribute("message")).getSubject() %></tiles:put>
</tiles:insert>
<%
    Util.releaseFolder(((Message)request.getAttribute("message")).getFolder());
%>