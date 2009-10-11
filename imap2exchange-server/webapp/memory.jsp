<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="edu.yale.its.tp.java.monitor.*" %>
<%@ page session="false" %>
<%
  response.addHeader("Cache-Control","no-cache");               
  response.addHeader("Cache-Control","no-store");               
  response.addHeader("Cache-Control","must-revalidate");     
  response.addHeader("Pragma","no-cache");
%>
<jsp:include page="header.jsp" />
<% 
   HttpSession session = request.getSession();

   // Get the parameters
   String action   = request.getParameter("action");
   String rate     = request.getParameter("rate");
   String duration = request.getParameter("duration");
   String type     = request.getParameter("type");

   // Set the defaults
   if(action == null) action = "";
   if(rate == null) rate = "1";
   if(duration == null) duration = "-1";
   if(type == null) type = "heap";


   JStatChart chart = (JStatChart)session.getAttribute(JStatChartServlet.JSTAT_CHART_SESSION_ID);
   if(chart == null) {
       chart = new JStatChart();
       chart.setPlotMaxHeap(true);
       chart.setTitle(new SimpleDateFormat("MM/d/yy").format(new Date()) + " Memory Stats");
       session.setAttribute(JStatChartServlet.JSTAT_CHART_SESSION_ID, chart);
   } 

   boolean started = chart.isStarted();	  
   if (action.equals("start")){
       chart.stop();
       chart.createChart();
       chart.start();
       chart.setRate(Integer.parseInt(rate)*1000);
       chart.setDuration(Integer.parseInt(duration)*60*1000);
       chart.setOnlyHeap(type.equals("heap"));
       started = true;
   } else if (action.equals("stop")){
       chart.stop();
       started = false;
   }
%>

<script type="text/javascript">
<!--
<%=started ? ("setInterval('reloadImage()', " + rate + "*1000);") : ""%>

function reloadImage(){
  img = document.getElementById('memoryChartPNG');
  img.src = 'memoryChart.png?' + Math.random();
}
-->
</script>

     <form name="settings" action="memory.jsp">
        <input type="hidden" name="action" value="<%=started ? "stop" : "start"%>"/>
        <table align="left" border="0">
          <tr>
            <td align="center">
              <img id="memoryChartPNG" src="memoryChart.png" alt="Realtime Memory Stats" />
            </td>
          </tr>
          <tr><td>&nbsp;</td></tr>
          <tr><td align="center">
              <table border="0">
                <tr>
                   <td align="center">Rate</td><td align="center">Duration</td><td align="center">Type</td>
                </tr>
                <tr>
                   <td align="center">
                      <select name="rate" <%=started ? "disabled" : ""%> >
                        <option value="1" <%=rate.equals("1") ? "selected" : ""%> >1s</option>
                        <option value="2" <%=rate.equals("2") ? "selected" : ""%> >2s</option>
                        <option value="5" <%=rate.equals("5") ? "selected" : ""%> >5s</option>
                        <option value="10" <%=rate.equals("10") ? "selected" : ""%> >10s</option>
                        <option value="30" <%=rate.equals("30") ? "selected" : ""%> >30s</option>
                      </select>
                   </td>
                   <td align="center">
                      <select name="duration" <%=started ? "disabled" : ""%> >
                        <option value="-1" <%=duration.equals("-1") ? "selected" : ""%> >forever</option>
                        <option value="5" <%=duration.equals("5") ? "selected" : ""%> >5 mins</option>
                        <option value="30" <%=duration.equals("30") ? "selected" : ""%> >30 mins</option>
                        <option value="60" <%=duration.equals("60") ? "selected" : ""%> >1 hour</option>
                      </select>
                   </td>
                   <td align="center">
                      <select name="type" <%=started ? "disabled" : ""%> >
                        <option value="heap" <%=type.equals("heap") ? "selected" : ""%> >Heap Only</option>
                        <option value="all" <%=type.equals("all") ? "selected" : ""%> >All Memory</option>
                      </select>
                   </td>
                 </tr>
              </table>
          </td></tr>
          <tr><td>&nbsp;</td></tr>
          <tr>
            <td align="center">
              <input type="submit" value="Start" <%=started ? "disabled" : ""%> />
              <input type="submit" value="Stop" <%=started ? "" : "disabled" %> /> 
            </td>
          </tr>
        </table>
     </form>

<jsp:include page="footer.jsp" />
