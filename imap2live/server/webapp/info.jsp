<%@ page language="java" contentType="text/html" %>
<%@ page import="edu.yale.its.tp.email.conversion.*" %>
<%@ page import="edu.yale.its.tp.email.conversion.exchange.*" %>
<%@ page import="edu.yale.its.tp.email.conversion.yale.*" %>
<%@ page import="edu.jmu.email.conversion.jmu.*" %>
<%@ page import="edu.yale.its.tp.email.conversion.imap.*" %>
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

<span class="hdr">Server Configuration Info Page</span>
<pre>
# Exchange Server Information for WS
###################################################
<%
YaleExchangeServerPortFactory esFactory = (YaleExchangeServerPortFactory)ExchangeServerPortFactory.getInstance();
%><i>exchangeServer.uri</i>=<%=esFactory.getUri()%>
<i>exchangeServer.adDomain</i>=<%=esFactory.getAdDomain()%>
<i>exchangeServer.uid</i>=<%=esFactory.getUid()%>

# Max number of conversion theads to run at 
# once.  I recommend no more than 5 in a server
# that can allocate only 2Gb of memory
# cached threads need to be the max
# number of user you plan to start per
# conversion session.
###################################################
<i>exchangeConversionManager.maxRunningThreads</i>=<%=ExchangeConversionManager.getInstance().getMaxRunningThreads()%>
<i>exchangeConversionManager.maxCachedThreads</i>=<%=ExchangeConversionManager.getInstance().getMaxCachedThreads()%>

# AD info to find users 
# UPN and default email Address.
###################################################
<i>ad.uri</i>=<%=JmuAD.getInstance().getUri()%>
<i>ad.port</i>=<%=JmuAD.getInstance().getPort()%>
<i>ad.uid</i>=<%=JmuAD.getInstance().getUid()%>
<i>ad.base</i>=<%=JmuAD.getInstance().getBase()%>

# Imap Servers
###################################################
<% List<ImapServer> servers = ImapServerFactory.getInstance().getImapServers();
   Collections.sort(servers, new Comparator(){
                                 public int compare(Object o1, Object o2){
                                     ImapServer s1 = (ImapServer)o1;
                                     ImapServer s2 = (ImapServer)o2;
                                     return s1.getUri().compareTo(s2.getUri());
                                 }
                              });

  for(ImapServer server : servers){
  String uri = server.getUri();
%><i><%=uri%>.uri</i>=<%=uri%>
<i><%=uri%>.port</i>=<%=server.getPort()%>
<i><%=uri%>.protocol</i>=<%=server.getProtocol()%>
<i><%=uri%>.adminUid</i>=<%=server.getAdminUid()%>
<i><%=uri%>.sasl</i>=<%=server.isSasl()%>

<%
  }
%>

# JMU Specific settings
###################################################
<i>jmuSite.mailDomain</i>=<%=JmuSite.getInstance().getMailDomain()%>
</pre>

<jsp:include page="footer.jsp" />
