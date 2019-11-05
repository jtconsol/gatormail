<%@page contentType="text/css"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%--
May be useful: http://www.infinitespaces.com/bmah/

TODO: This is a mess. Could split this into a few files or just clean it all up.
    Also finish the docs in the jsp comment below.
--%>

body {
    color: #333333;
    background-color : #ffffff;
    font-weight: normal;
    font-family: Arial, Helvetica, sans-serif;
<%  // This is so IE doesn't get horizontal scroll bars in the compose view.
    String us = request.getHeader("User-Agent").toLowerCase();
    if (us.indexOf("msie") != -1) {
%>
    margin : 0px;
    padding : 0px;
<%
    }
%>
}

.topNavBar {
    margin : 0px;
    padding : 0px;
    font-size: 11px;
    font-weight: bold;
}

.leftNavBar {
    background-color: #e6e6e6;
    font-size: 11px;
    font-weight: bold;
}

a:link {
    font-family: Arial, Helvetica, sans-serif;
    color: #0021a5;
}

a:visited {
    font-family: Arial, Helvetica, sans-serif;
    color: #ff4a00;
}

/* Page header */
.header .version { /* Version text next to the logo */
    font-family: Arial, Helvetica, sans-serif;
    font-size: 18px;
    color: #CCCCCC;
}

.header .loggedInAs {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 11px;
    color: #CCCCCC;
}

.header .username {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 14px;
}


.topNav { /* The text across the nav bar*/
    font-family: Arial, Helvetica, sans-serif;
    font-size: 13px;
    font-weight: bold;
    color: #0021a5;
}

/* Copyright text at bottom of pages */
.copyright {
    text-align : center;
    font-size : smaller;
}

.footer {
    color: #666666;
    font-size: 11px;
    font-weight: normal;
}

.button {
    border : outset 2px;
    padding : 1px 3px;
}

/* message displaying result of action */
font.result {
    color : green;
    font-weight : bolder;
}

font.warning {
    color : red;
}

<%--
Available CSS classes:
    loginForm: loginErrors, {username,password}{Prompt,Input,Errors}, {submit,reset}Button
    messageList: message{New,NewSelected,Seen,SeenSelected}
    messageHeader:
--%>
/* error messages */
ul.errors {
    color: red;
}

li.error {
    font-weight: bolder;
}

font.error {
    color: red;
    font-weight: bolder;
}

/* login form */
table.loginForm {
    border-spacing: 15pt;
    empty-cells: hide;
}

td.submitButton, td.resetButton {
    text-align: center;
}


/* Common table properties */

.darkBlueRow {
    color : #FFFFFF;
    background-color : #0021a5;
    text-align : left;
    font-size : 14px;
    font-style : normal;
    font-weight : normal;
}

.darkBlueRow a { /* Make links on the dark blue be white so the user can see them. */
    color : #FFFFFF;
}

.lightBlueRow {
    color: #000000;
    background-color: #ccd3ed;
    font-size: 12px;
    font-weight: normal;
}

/* Folder Message Listings */
.folderMessageListHeader {
    color : #000000;
    background-color : #e6e6e6;
    text-align : left;
    font-size : 17px;
    font-weight : normal;
}

.messageUnread {
    background-color: #ffffff;
    font-size: 14px;
    font-style: normal;
    font-weight: 900;
}

.messageUnreadSelected {
    background-color: #ffffcc;
    font-size: 14px;
    font-style: normal;
    font-weight: 900;
}

.messageUnreadThreaded {
    background-color: #d8ffe4;
    font-size: 14px;
    font-style: normal;
    font-weight: 900;
}

.messageUnreadSelectedThreaded {
    background-color: #ebffd8;
    font-size: 14px;
    font-style: normal;
    font-weight: 900;
}


.messageSeen {
    background-color: #ffffff;
    color : #666666;
    font-size : 14px;
    font-weight : normal;
}

.messageSeenSelected {
    background-color: #ffffcc;
    color : #666666;
    font-size : 14px;
    font-weight : normal;
}

.messageSeenThreaded {
    background-color: #d8ffe4;
    color : #666666;
    font-size : 14px;
    font-weight : normal;
}

.messageSeenSelectedThreaded {
    background-color: #ebffd8;
    color : #666666;
    font-size : 14px;
    font-weight : normal;
}

.folderEmptyFolder {
    color : red;
    text-align : center;
    font-size : larger;
}

/* Folder List */

.folderListFolder { /* First level folders shouldn't be indented */

}

.folderListFolder .folderListFolder { /* Second+ level folders should be indented */
    padding-left : 20px;
}

/* Folder Quota */
.folderQuota {
    border : solid 2px;
    background-color : #ffffff;
}

.folderQuotaTitle {
    color : #FFFFFF;
    background-color : #0021a5;
    font-size : 14px;
    font-style : normal;
    font-weight : normal;
}

.folderQuotaResource {
}

.folderQuotaResourceUsed {
    background-color: #ff9900;
    border-style : solid;
    border-width : 1px;
    border-right-width : 0px;
}

.folderQuotaResourceFree {
    background-color: #ffffff;
    border-style : solid;
    border-width : 1px;
    border-left-width : 1px;
}

.folderQuotaMessage {
    color: #666666;
    text-align : center;
    font-size : 11px;
    font-weight : normal;
}

/* Compose view */
.composeHeaderTitle {
	font-size: 14px;
	font-weight: bold;
	color: #666666;
	background-color: #e6e6e6;
}


/* generic table with nice header */
.headerTable .header {
    color : #ffffff;
    background-color : #0021a5;
    text-align : left;
    font-size : 14px;
    font-style : normal;
    font-weight : normal;
}

.headerTable .subheader {
    color: #000000;
    background-color: #ccd3ed;
    font-size: 12px;
    font-weight: normal;
}

.headerTable tr {
    background-color: #ffffff;
}

.headerTable tr.altrow {
    background-color : #e6e6e6;
}

/* ========= Address Book ========== */
.addressBook .leftNavBar {
    text-align : right;
	font-size: 14px;
	font-weight: bold;
}

/* ========= Message view ========== */

/* message header */
.messageHeader th.leftNavBar , .messageBody th.leftNavBar{ /* A lot of values also come from .leftNavBar */
    color: #666666;
    text-align : right;
    font-size: 14px;
}

.messageNavBar {
    margin : 0px;
    padding : 0px;
    font-size: 11px;
    font-weight: bold;
}

.messageHeader .darkBlueRow { /* This is so the move/copy form is right aligned. */
    text-align : right;
}

.messageHeader td.lightBlueRow {
    background-color: #ccd3ed;
    font-size: 12px;
    font-weight: normal;
}

.XmessageHeader .darkBlueRow th {
    color : #FFFFFF;
    background-color : #0021a5;
    text-align : left;
    font-size : 14px;
    font-style : normal;
    font-weight : normal;
}

.messageHeader td {
    background-color: #ffffff;
}

.messageBody {
}

.messageBody td {
    padding : 5px;
}

.messageBody > div {
}

.messageMessageRfc822, .messageContentOther {
    margin-bottom : 1em;
}

/* Text plain inline forwarding colorization */
.messageInlineForwardBlock {
    padding-left : 5px;
}

<%
for (int i=1; i < 25; i+=3) {
    if (i > 3) {
        out.print(", ");
    }
    for (int j=0; j < i; j++) {
        out.print(".messageInlineForwardBlock ");
    }
}
%> {
    border-left : solid navy;
    color : navy;
}

<%
for (int i=2; i < 25; i+=3) {
    if (i > 3) {
        out.print(", ");
    }
    for (int j=0; j < i; j++) {
        out.print(".messageInlineForwardBlock ");
    }
}
%> {
    border-left : solid maroon;
    color : maroon;
}

<%
for (int i=3; i < 25; i+=3) {
    if (i > 3) {
        out.print(", ");
    }
    for (int j=0; j < i; j++) {
        out.print(".messageInlineForwardBlock ");
    }
}
%> {
    border-left : solid green;
    color : green;
}
