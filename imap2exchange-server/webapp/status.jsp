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
<%
        SimpleDateFormat dtFmt = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

        /** Get the Exchange Conversion Manager from the Application context */
        ServletContext context = request.getSession().getServletContext();
        ExchangeConversionManager manager = (ExchangeConversionManager)context.getAttribute("EXCHANGE_CONVERSION_MANAGER");
        if(manager == null){
                manager = ExchangeConversionManager.getInstance();
                if(manager == null){
                        throw new ServletException("Exchange Conversion Manager not initialized by Spring Container.");
                }
                context.setAttribute("EXCHANGE_CONVERSION_MANAGER", manager);
        }
        List<ExchangeConversion> queued = manager.getQueued();
        List<ExchangeConversion> running = manager.getRunning();
%>

<!-- Queued Conversions --> 
<span class="sub-hdr">
Queued 
</span> 
<table border="1" rules="groups" cellpadding="2" class="txt"> 
  <colgroup align="center">
  <colgroup align="center">
    <thead>
      <tr>
        <th>Position</th>
        <th>uid@Po</th>
      </tr> 
    </thead>
    <tbody>
<% 
   int i=1; 
   if(queued.size() == 0){ %>
    <tr align="center" class="txt"><td>empty</td><td>-</td></tr>       
<% } else {
       for(ExchangeConversion conv : queued){ 
%>
        <tr>
                <td align="center"><%=i++ %></td>
                <td><%=conv.getId() %></td>
        </tr>
<%     } 
   } %>
  </tbody>
</table>

<!-- Running Conversions -->
<br />
<span class="sub-hdr">
Running
</span> 
<table border="1" rules="groups" cellpadding="2" class="txt"> 
  <colgroup align="center">
  <colgroup align="center">
    <thead>
      <tr>
        <th>Started</th>
        <th>uid@Po</th>
      </tr>
    </thead>
    <tbody>
<% 
   i=1;
   if(running.size() == 0){ %>
    <tr align="center">
      <td>empty</td>
      <td>-</td>
    </tr>       
<% } else {
     for(int j=running.size(); j>0; j--){ 
         ExchangeConversion conv = running.get(j-1);
       User user = conv.getUser();
%>
        <tr>
                <td><%=dtFmt.format(new Date(conv.getStart()))%></td>
                <td><%=conv.getId() %></td>
        </tr>
<%    }
   } %>
  </tbody>
</table>
<jsp:include page="footer.jsp" />
