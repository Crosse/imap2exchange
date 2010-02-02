<%
  response.addHeader("Cache-Control","no-cache");               
  response.addHeader("Cache-Control","no-store");               
  response.addHeader("Cache-Control","must-revalidate");     
  response.addHeader("Pragma","no-cache");
%>
<jsp:include page="header.jsp" />

<span class="hdr">Welcome to the imap2exchange Conversion Utility</span>
<br />
<br />
This utility will copy a user's email from the Mirapoint email system<br />
into their new Exchange mailbox.  This utility does <b>not</b> provision the<br />
exchange account&mdash;this must be done prior to running this utility<br />
for a user.
<br />

<jsp:include page="footer.jsp" />
