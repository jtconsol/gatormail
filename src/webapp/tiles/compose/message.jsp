<%@page contentType="text/html" import="java.util.List,
                                        edu.ufl.osg.webmail.util.Util,
                                        edu.ufl.osg.webmail.Constants"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<%@taglib uri="/tags/webmail" prefix="wm"%>

<html:form method="post" action="modifyCompose" enctype="multipart/form-data" focus="to">
<html:hidden property="composeKey"/>

<script language="JavaScript" type="text/javascript">
<!--
function populateAddress(field) {
    var contacts = document.composeForm.contacts;
    var length = contacts.length;

    while( length-- ) {
        if (contacts.options[length].selected) {
            if (field.value == "") {
                field.value += contacts.options[length].value;

            } else if (contacts.options[length].value != ""
                    && field.value.search(" " + contacts.options[length].value + ",") == -1
                    && field.value.search("^" + contacts.options[length].value) == -1
                    && field.value.search(" " + contacts.options[length].value + "$") == -1) {
                field.value += ", " + contacts.options[length].value;
            }
        }
    }

   return false;
}
//-->
</script>
<table width="100%" cellpadding="2" cellspacing="0">
 <tr class="lightBlueRow">
  <td width="15%" align="right" class="composeHeaderTitle">&nbsp;</td>
  <td colspan="4" class="darkBlueRow">
    <table>
     <tr>
      <td class="darkBlueRow">
       <html:submit property="action" styleClass="button" accesskey="s">
        <bean:message key="button.send"/>
       </html:submit>
      </td>
      <td class="darkBlueRow" colspan="2">&nbsp;</td>
     </tr>
    </table>
  </td>
 </tr>
  <tr class="lightBlueRow">
    <td width="15%" align="right" class="composeHeaderTitle"><bean:message key="message.to"/>:</td>
    <td colspan="3">
     <html:text property="to" size="63" style="width : 99%" tabindex="10"/>
    </td>
    <td valign="top" rowspan="7" width="200">
      <table align="center" width="100%">
        <tr>
          <td align="center">
           <noscript>
             <div style="color : red;"><bean:message key="addressbook.control.noscript"/></div>
           </noscript>
           <bean:message key="addressbook.control.message"/>
          </td>
        </tr>
        <tr>
          <td align="center">
            <input type="button" class="button" value="To" onclick="populateAddress(this.form.to)" style="xfont-size: smaller;" accesskey="t">
            <input type="button" class="button" value="CC" onclick="populateAddress(this.form.cc)" style="xfont-size: smaller;" accesskey="c">
            <input type="button" class="button" value="BCC" onclick="populateAddress(this.form.bcc)" style="xfont-size: smaller;" accesskey="b">
          </td>
        </tr>
        <tr>
          <td align="center">
            <select name="contacts" multiple="multiple" size="27" style="width : 200px;" onDblClick="populateAddress(this.form.to)">
<%-- TODO: Show empty address book message. --%>
            <c:choose>
             <c:when test="${!empty addressList}">
              <c:forEach items="${addressList}" var="iAddress">
               <option value="${iAddress.address}" title=""${iAddress.address}">
                <c:out value="${iAddress.personal}"/>
               </option>
              </c:forEach>
             </c:when>
             <c:otherwise>
               <option value=""><bean:message key="addressbook.control.empty1"/></option>
               <option value=""><bean:message key="addressbook.control.empty2"/></option>
               <option value=""><bean:message key="addressbook.control.empty3"/></option>
               <option value=""><bean:message key="addressbook.control.empty4"/></option>
             </c:otherwise>
            </c:choose>
            </select>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr class="lightBlueRow">
    <td width="15%" align="right" class="composeHeaderTitle"><bean:message key="message.cc"/>:</td>
    <td colspan="3">
     <html:text property="cc" size="63" style="width : 99%" tabindex="20"/>
    </td>
  </tr>
  <tr class="lightBlueRow">
    <td width="15%" align="right" class="composeHeaderTitle"><bean:message key="message.bcc"/>:</td>
    <td colspan="3">
     <html:text property="bcc" size="63" style="width : 99%" tabindex="30"/>
    </td>
  </tr>
  <tr class="lightBlueRow">
    <td width="15%" align="right" class="composeHeaderTitle"><bean:message key="message.subject"/>:</td>
    <td colspan="3">
     <html:text property="subject" size="63" style="width : 99%" tabindex="40"/>
    </td>
  </tr>
  <tr class="lightBlueRow">
    <td width="15%" align="right" class="composeHeaderTitle"><bean:message key="compose.attachment.upload"/>:</td>
    <td colspan="3">
       <html:file property="attachment" accesskey="f"/>
       <html:submit property="action" styleClass="button"><bean:message key="button.attachment.upload"/></html:submit>
    </td>
  </tr>
 <!-- show attachments -->
 <bean:define id="composeKey" name="composeForm" property="composeKey" type="java.lang.String"/>
  <tr class="lightBlueRow">
   <td width="15%" align="right" class="composeHeaderTitle">&nbsp;</td>
   <td colspan="3">
<%
  List attachList = Util.getAttachList(composeKey, session);
  if (attachList != null && attachList.size() > 0) {
     pageContext.setAttribute("attachList", attachList);
%>
    <table width="575" class="messageHeader" cellpadding="3">
      <tr class="darkBlueRow">
       <th><bean:message key="compose.name"/></th>
       <th><bean:message key="compose.type"/></th>
       <th><bean:message key="compose.size"/></th>
       <th>
        <html:submit property="action" styleClass="button">
         <bean:message key="button.attachment.delete"/>
        </html:submit>
       </th>
      </tr>
      <c:forEach items="${attachList}" var="attachObj">
       <tr>
        <td><c:out value="${attachObj.fileName}"/></td>
        <td><c:out value="${attachObj.displayContentType}"/></td>
        <td><wm:write name="attachObj" property="size" filter="false"
	      formatKey="general.size.format" formatClassKey="general.size.formatter"/>
        </td>
        <td>
	     <input type="checkbox" name="<%=Constants.DELETE_ATTACHMENT%>"
	     value="<c:out value="${attachObj.tempName}"/>" value="off"/>
        </td>
       </tr>
      </c:forEach>
    </table>
<!--   </td>
  </tr>
-->
 <%
    }
 %>
 <!-- end show attachments ->
 <tr class="lightBlueRow">
  <td width="15%" align="right" class="composeHeaderTitle">&nbsp;</td>
  <td colspan="3">
-->
    <table>
     <tr>
      <td><html:checkbox property="copyToSent" titleKey="compose.copyToSent" accesskey="c"/></td>
      <td><bean:message key="compose.copyToSent"/></td>
     </tr>
    </table>
  </td>
 </tr>

 <tr class="lightBlueRow">
  <td width="15%" align="right" class="composeHeaderTitle">&nbsp;</td>
  <td colspan="3">
    <html:textarea property="body"
          cols="<%= String.valueOf(Constants.COMPOSE_BODY_WIDTH) %>" rows="20" style="width : 99%" tabindex="50"/>
  </td>
 </tr>
 <tr class="lightBlueRow">
  <td width="15%" align="right" class="composeHeaderTitle">&nbsp;</td>
  <td colspan="4" class="darkBlueRow">
    <table>
     <tr>
      <td class="darkBlueRow">
       <html:submit property="action" styleClass="button">
        <bean:message key="button.send"/>
       </html:submit>
      </td>
      <td class="darkBlueRow" colspan="2">&nbsp;</td>
     </tr>
    </table>
  </td>
 </tr>
 </table>
</html:form>
