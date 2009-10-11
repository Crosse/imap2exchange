<%@ page language="java" contentType="text/html" %>

<table class="txt">
  <tr>
    <td<%=request.getRequestURI().endsWith("index.jsp") ? " class=\"txt-bold\"" : ""%>>
      <a href="index.jsp">Home</a>
    </td>
  </tr>
  <tr>
    <td<%=request.getRequestURI().endsWith("add.jsp") ? " class=\"txt-bold\"" : ""%>>
      <a href="add.jsp">Add Conversions</a>
    </td>
  </tr>
  <tr>
    <td<%=request.getRequestURI().endsWith("status.jsp") ? " class=\"txt-bold\"" : ""%>>
      <a href="status.jsp">Active Conversions</a>
    </td>
  </tr>
  <tr>
    <td<%=request.getRequestURI().endsWith("completed.jsp") ? " class=\"txt-bold\"" : ""%>>
      <a href="completed.jsp">Completed Conversions</a>
    </td>
  </tr>
  <tr>
    <td<%=request.getRequestURI().endsWith("memory.jsp") ? " class=\"txt-bold\"" : ""%>>
      <a href="memory.jsp">Memory Graph</a>
    </td>
  </tr>
<!--
  <tr>
    <td<%=request.getRequestURI().endsWith("poFinder.jsp") ? " class=\"txt-bold\"" : ""%>>
      <a href="poFinder.jsp">Find Central Po</a>
    </td>
  </tr>
-->
  <tr>
    <td<%=request.getRequestURI().endsWith("info.jsp") ? " class=\"txt-bold\"" : ""%>>
      <a href="info.jsp">Server Config Info</a>
    </td>
  </tr>
</table>
