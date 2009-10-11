<%@ page language="java" contentType="text/html" %>
<%@ page import="edu.yale.its.tp.email.conversion.log.*" %>
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

<% 
  String convId = request.getParameter("convId");
  ThreadAppender ta = (ThreadAppender)Logger.getLogger("edu.yale").getAppender("T");  
  String logFile = ta.getOutputFolder() + convId;
  boolean logExists = new File(logFile).isFile();
%>
<script type="text/javascript">
<!--
function toggleVisibility(id) {
  var e = document.getElementById(id);
  if(e.style.display != 'block')
    e.style.display = 'block';
  else
    e.style.display = 'none';
}
//-->
</script>

<table class="txt">
  <tr>
<%
if(!logExists){
%>
    <td><span class="hdr">Conversion Logs for <%=convId%> were not found.</span></td>
<%  
} else {
%>
    <td><span class="hdr">Conversion Logs for <%=convId%></span></td>
  </tr> 
<%  String line;
    BufferedReader in = new BufferedReader(new FileReader(logFile));
    boolean started = false;
    String status = "Success";
    String startTime = null;
    StringBuilder sb = null;
    while((line = in.readLine()) != null){
    
        if(!started){
            int idx = line.indexOf("Conversion Starting:");
            if(idx != -1){
                    started = true;
                    startTime = line.substring(idx + 24 + convId.length());
                    sb = new StringBuilder();
                    status = "Success";
                    sb.append(line).append("\n");
                }
        } else {
            if(line.indexOf("Conversion Completed:") != -1){
                started = false;
                sb.append(line).append("\n");
                if(status.equals("Success")) status = "<span class=\"txt-green\">" + status + "</span>";
                else if(status.equals("Warning")) status = "<span class=\"txt-orange\">" + status + "</span>";
                else if(status.equals("Error")) status = "<span class=\"txt-red\">" + status + "</span>";
%>             <tr> 
                 <td> 
                   <a href="javascript:toggleVisibility('<%=startTime%>')"><%=startTime%> - <%=convId%> - <%=status%></a><br />
                   <div id="<%=startTime%>" class="hidden-code">
                     <pre><%=sb.toString()%></pre> 
                   </div>
                 </td>
               </tr> 
<%          } else {
                if(status.equals("Success") && line.indexOf(convId + "] WARN") != -1){
                   status = "Warning";
                } else if((status.equals("Success") || status.equals("Warning"))
                  && line.indexOf(convId + "] ERROR") != -1){
                  status = "Error";
                }
                sb.append(line).append("\n");
            }
        }
    }
}
%>
</table>


<jsp:include page="footer.jsp" />
