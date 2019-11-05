<%@page contentType="text/html" import="javax.mail.Folder,
                                        edu.ufl.osg.webmail.util.Util,
                                        java.util.List,
                                        java.util.Arrays,
                                        java.util.Iterator,
                                        edu.ufl.osg.webmail.Constants"%>
<%@taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib uri="/tags/struts-bean" prefix="bean"%>
<%--
TODO: This needs to be made more compact. Also, we may want to list the number
of new messages.
--%>
<%
    try {

    Folder currentRootFolder = (Folder)request.getAttribute("currentRootFolder");
    if (currentRootFolder == null) {
        currentRootFolder = Util.getFolder(session, "INBOX");
    }
    currentRootFolder = Util.getFolder(currentRootFolder);
    request.setAttribute("currentRootFolder", currentRootFolder);
    String fullName = currentRootFolder.getFullName();
    String imageName = "/";
    if (currentRootFolder.getFullName().equals("INBOX")) {
        imageName += "inbox";
    } else if (currentRootFolder.getFullName().equals(Constants.SENT_FOLDER_FULLNAME)) {
        imageName += "sent";
    } else if (currentRootFolder.getFullName().equals(Constants.TRASH_FOLDER_FULLNAME)) {
        imageName += "trash";
        // TODO When we support the concept of a Draft folder.
        //    } else if (currentRootFolder.getFullName().equals(Constants.DRAFT_FOLDER_FULLNAME)) {
        //        imageName += "draft";
    } else {
        imageName += "folder";
    }
    // I'd rather do this by seeing if Folder objects are equal but that doesn't work. Fsck'ing JavaMail
    if (currentRootFolder.getFullName().equals(((Folder)request.getAttribute("folder")).getFullName())) {
        imageName += "-opened";
    } else {
        imageName += "-closed";
    }

    int unreadMessageCount = 0;
    if (currentRootFolder.isOpen()) {
        unreadMessageCount = currentRootFolder.getUnreadMessageCount();
        if (unreadMessageCount != 0) {
            imageName += "-unread";
        }
    }
    imageName += ".gif";

    if (currentRootFolder.isSubscribed()) {
%>
<div class="folderListFolder">
  <html:link forward="folder" paramId="folder" paramName="currentRootFolder" paramProperty="fullName" title="<%= fullName %>">
   <html:img page="<%= imageName %>" alt="Folder:" border="0" align="absmiddle" hspace="5"/><bean:write name="currentRootFolder" property="name"/>
<%
    if (unreadMessageCount != 0) {
        out.print("(" + unreadMessageCount + ")");
    }
%>      
  </html:link>
<%
    } else {
%>
<div class="folderListFolder">
  <span title="Unsubscribed Folder">
   <html:img page="<%= imageName %>" alt="Folder:" border="0" align="absmiddle" hspace="5"/><bean:write name="currentRootFolder" property="name"/>
<%
        if (unreadMessageCount != 0) {
            out.print("(" + unreadMessageCount + ")");
        }
%>
  </span>
<%
    }

    List subFolderList;
    subFolderList = Arrays.asList(currentRootFolder.listSubscribed());
    Util.releaseFolder(currentRootFolder);

    Iterator subFolderIterator = subFolderList.iterator();
    while (subFolderIterator.hasNext()) {
        Folder f = (Folder)subFolderIterator.next();
        //f = Util.getFolder(session, f); // This is less efficent ... maybe not?
        request.setAttribute("currentRootFolder", f);

        // TODO Tiles doesn't work for some reason, should figure out why
%><jsp:include page="/tiles/folder/folderList.jsp" flush="true"/><%
        Util.releaseFolder(f);
    }
%>
</div>
<%
    request.removeAttribute("currentRootFolder");

    } catch (Throwable t) {
        t.printStackTrace();
        throw t;
    }
%>
