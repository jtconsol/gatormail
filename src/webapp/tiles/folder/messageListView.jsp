<%@page contentType="text/html" import="java.util.List,
                                        edu.ufl.osg.webmail.actions.FolderAction"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<%
    // TODO: Move the CSS in the form below into the style sheet.
    
    List filterChoices = FolderAction.getFilterChoices();
    pageContext.setAttribute("filterChoices", filterChoices);
%>
<table width="100%" border="0" cellpadding="4" cellspacing="0" class="folderMessageList">
 <tr class="lightBlueRow">
  <td alight="left">
   <html:form action="folder" style="padding : 0px; margin : 0px; border : 0px;">
    <bean:define id="folderName" name="folder" property="fullName" type="java.lang.String"/>
    <html:hidden property="folder" value="<%=folderName%>"/>
    <html:select property="filterType" titleKey="folder.filter.filterType.title" onchange="checkFilter(this.options[this.selectedIndex].text);">
     <c:forEach items="${filterChoices}" var="choice">
<%
    String choice = (String)pageContext.getAttribute("choice");
    String filterKey = "folder.filter." + choice;
%>
      <html:option value="<%= choice %>"><bean:message key="<%= filterKey %>"/></html:option>
     </c:forEach>
    </html:select>

    <html:text property="filter" size="20" titleKey="folder.filter.filter.title"/>

    <html:submit property="action" styleClass="button" titleKey="folder.filter.action.title">
     <bean:message key="button.filter"/>
    </html:submit>
   </html:form>
   <script language="JavaScript" type="text/javascript">
<!--
function checkFilter(filterType) {
   if(filterType.search(".*:$") != -1)
   {
	document.folderForm.filter.disabled = false;
	document.folderForm.filter.focus();
   }
   else
   {
	document.folderForm.filter.disabled = true;
   }
}

checkFilter(document.folderForm.filterType.options[document.folderForm.filterType.selectedIndex].text);
//-->
   </script>
  </td>
  <td align="right">&nbsp;</td>
 </tr>
</table>