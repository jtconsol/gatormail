<%@page contentType="text/html" import="java.util.*,javax.mail.*,com.sun.mail.imap.*"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>

<p>
<table border="0" width="100%">
  <tr>
    <td align="center">
    <html:form action="emptyTrash">
    <bean:define id="folderName" name="folder" property="fullName" type="java.lang.String"/>
    <html:submit property="action" styleClass="button"><bean:message key="button.emptyTrash"/></html:submit>
    </html:form>
    </td>
  </tr>
</table>
</p>
