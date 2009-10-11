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
This is a utility to  copy email from a <br />
set of defined imap sources to the defined <br />
exchange account.  This utility does not <br />
provision the exchange account, and relies on <br />
it existing prior to the conversion of mail.<br />
<br />
Good luck.<br />


<jsp:include page="footer.jsp" />
