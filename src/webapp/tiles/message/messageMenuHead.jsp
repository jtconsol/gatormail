<%@page contentType="text/html" import="javax.mail.Message,
                                        javax.mail.Folder,
                                        java.util.Map,
                                        java.util.HashMap,
                                        edu.ufl.osg.webmail.util.Util,
                                        edu.ufl.osg.webmail.Constants"%>
<%@taglib uri="/tags/struts-tiles" prefix="tiles"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<%@taglib uri="/tags/webmail" prefix="wm"%>
<%--
TODO: Make to and other email's need to be run through the formatter like from is.
The look of the message header needs to be reworked, probably should be in a tile.
--%>
<%
    Message message = (Message)request.getAttribute("message");

    Folder messageFolder = message.getFolder();
    request.setAttribute("messageFolder", messageFolder);


    if (messageFolder != null) {
        messageFolder = Util.getFolder(messageFolder);

        Map messageParams = new HashMap();
        Util.addMessageParams(message, messageParams);
        request.setAttribute("messageParams", messageParams);
%>
 <table class="messageHeader" width="100%" cellspacing="0" cellpadding="2" border="0">
   <tr>
     <th class="leftNavBar" width="100">&nbsp;</th>

     <th class="darkBlueRow" width="50%">
       <tiles:insert page="/tiles/message/messageMenu.jsp" flush="true"/>
     </th>

     <th align="right" class="darkBlueRow">
       <html:form method="post" action="modifyMessage">
<%
    messageParams = (Map)request.getAttribute("messageParams");
    if (messageParams.get("uid") != null) {
        String uid = messageParams.get("uid").toString();
%><html:hidden property="uid" value="<%=uid%>"/><%
    } else if (messageParams.get("messageNumber") != null) {
        String messageNumber = messageParams.get("messageNumber").toString();
%><html:hidden property="messageNumber" value="<%=messageNumber%>"/><%
    }
%>
     <html:hidden property="folder" value="<%=messageFolder.getFullName()%>"/>
     <tiles:insert page="/tiles/common/moveCopy.jsp" flush="true"/>
     </html:form>
     </th>
   </tr>
 </table>
<%
        Util.releaseFolder(messageFolder);
} // if (messageFolder != null)
%>
