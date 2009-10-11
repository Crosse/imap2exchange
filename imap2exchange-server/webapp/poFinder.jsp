<%@ page import="java.net.*" %>
<%
  response.addHeader("Cache-Control","no-cache");               
  response.addHeader("Cache-Control","no-store");               
  response.addHeader("Cache-Control","must-revalidate");     
  response.addHeader("Pragma","no-cache");
%>

<jsp:include page="header.jsp" />

<span class="sub-hdr">Find Central Post Office.</span><br />
<span class="txt">
This is a utility to find the default Central PO for a netid.<br />
If a user has more than one PO, only the one registered to <br />
${netid}.mail.yale.edu will be returned.  <br />
<br />
<%
  String msg = null;
  String uid = request.getParameter("netid");
  
  if(uid != null && !uid.equals("")){
      String mailbox = uid + ".mail.yale.edu";
      try{
          InetAddress addr = InetAddress.getByAddress(InetAddress.getByName(mailbox).getAddress());
          String po = addr.getHostName();
          if(po != null && po != ""){
              msg = "Mailbox is: " + uid + "@" + po;
          }
      } catch (UnknownHostException e){  
          msg = "Invalid netid or this netid does not have a central email account.";
      }
  }
%>
<form action="poFinder.jsp">
        netid: <input type="text" name="netid"/>
        <input type="submit"/>
</form>
<br />
        <%=msg == null ? "" : msg%>
<jsp:include page="footer.jsp" />
