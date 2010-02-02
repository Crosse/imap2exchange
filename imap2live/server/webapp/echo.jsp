<%@ page language="java" contentType="text/html" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%
  response.addHeader("Cache-Control","no-cache");               
  response.addHeader("Cache-Control","no-store");               
  response.addHeader("Cache-Control","must-revalidate");     
  response.addHeader("Pragma","no-cache");
%>
<html>
  <body onload="parent.processResults(document.body.innerHTML)">
<%
if(!ServletFileUpload.isMultipartContent(request)){
  out.println("No file uploaded in reqeust.");
} else {
  FileItemFactory factory = new DiskFileItemFactory();
  ServletFileUpload upload = new ServletFileUpload(factory);
  List<FileItem> items = upload.parseRequest(request);
  for(FileItem item : items){
    if(!item.isFormField()){
      out.println(item.getString());
    }
  }
}
%>
  </body>
</html>
