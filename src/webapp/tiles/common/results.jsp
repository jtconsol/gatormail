<%@page contentType="text/html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<table style="background-color: #e6e6e6;" width="100%" cellspacing="0">
 <tr>
  <td>
<html:errors/>
<c:if test="${!empty result}">
<p align="center">
  <font class="result">
    <bean:write name="result" property="message" filter="true" scope="request"/>
  </font>
</p>
</c:if>
  </td>
 </tr>
</table>