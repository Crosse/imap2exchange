<%@ page language="java" contentType="text/html" %>
<%@ page import="edu.yale.its.tp.email.conversion.*" %>
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
<table>
   <tr>
     <td>
	   <span class="hdr">Completed Conversions</span><br />
	 </td> 
   </tr>
<%
  SimpleDateFormat dtFmt = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
  ThreadAppender ta = (ThreadAppender)Logger.getLogger("edu.yale").getAppender("T");  
  File logDir = new File(ta.getOutputFolder());
  File[] logs = logDir.listFiles(new FilenameFilter() {
                                  public boolean accept(File dir, String name) {
                                      if(name.contains("@")
                                          && name.endsWith("yale.edu")) return true;
                                      return false;
                                  }
                              });
                       
  Arrays.sort(logs, new Comparator() {
    public int compare(Object o1, Object o2) {
      File f1 = (File) o1; File f2 = (File) o2;
      return (int) (f2.lastModified() - f1.lastModified());
    }
  });                              
  for(File log : logs){
%>
  <tr class="txt">
    <td>
      <a href="viewLog.jsp?convId=<%=log.getName()%>"><%=dtFmt.format(log.lastModified())%> - <%=log.getName()%></a>
    </td>
  </tr>

</span>
<% } %>
</table>
<jsp:include page="footer.jsp" />
