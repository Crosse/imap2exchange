<%@ page language="java" contentType="text/html" %>
<%@ page import="edu.yale.its.tp.email.conversion.*" %>
<%@ page import="edu.yale.its.tp.email.conversion.log.*" %>
<%@ page import="edu.yale.its.tp.email.conversion.util.*" %>
<%@ page import="org.apache.log4j.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.io.*" %>
<%
  response.addHeader("Cache-Control","no-cache");               
  response.addHeader("Cache-Control","no-store");               
  response.addHeader("Cache-Control","must-revalidate");     
  response.addHeader("Pragma","no-cache");
%>

<jsp:include page="header.jsp" />
<script src="js/sorttable.js"></script>
<script src="js/tablefilter.js"></script>
<span class="sub-hdr">Completed Conversions</span><br /><br />
<script language="javascript" type="text/javascript">
//<![CDATA[
function toggleFilterOnOff(){
  if(TF_HasGrid("completedTable")){
        TF_RemoveFilterGrid("completedTable");
  } else {
	setFilterGrid("completedTable");
  }
}
//]]>
</script>
<span class="txt">
<a href="javascript:toggleFilterOnOff()">Filters</a>
</span>
<table border="1" rules="groups" class="sortable" id="completedTable"> 
  <colgroup align="center">
  <colgroup align="center">
  <colgroup align="center">
  <colgroup align="center">
  <colgroup align="center">
  <colgroup align="center">
    <thead>
      <tr class="txt">
        <th>Started</th>
        <th>uid@Po</th>
        <th>Success</th>
        <th>Duration</th>
        <th>Msgs Copied</th>
        <th>Size Copied</th>
      </tr>
    </thead>
    </tbody>
<%
  ThreadAppender ta = (ThreadAppender)Logger.getLogger("edu.yale").getAppender("T");  
  File completedLog = new File(ta.getOutputFolder() + "/completed.log");
  BufferedReader in = new BufferedReader(new FileReader(completedLog));
  String line = null;
  while((line = in.readLine()) != null){
    String[] tokens = line.split("\t");
%>
    <tr align="center" class="txt">
      <td><%=tokens[0]%></td>
      <td><a href="viewLog.jsp?convId=<%=tokens[1] %>"><%=tokens[1] %></a></td>
      <td><%if(tokens[2].equals("success")) out.print("<span class=\"txt-green\">Success</span>");
            if(tokens[2].equals("warning")) out.print("<span class=\"txt-orange\">Warning</span>");
            if(tokens[2].equals("error")) out.print("<span class=\"txt-red\">Error</span>");%></td>
      <td><%=tokens[3]%></td>
      <td><%=tokens[4]%></td>
      <td><%=tokens[5]%></td>
    </tr>
<%
  }
%>
  </tbody>
</table>

<jsp:include page="footer.jsp" />
