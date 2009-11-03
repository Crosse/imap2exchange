<%@ page language="java" contentType="text/html" %>
<%@ page import="edu.yale.its.tp.email.conversion.*" %>
<%@ page import="java.util.*" %>
<%
  response.addHeader("Cache-Control","no-cache");               
  response.addHeader("Cache-Control","no-store");               
  response.addHeader("Cache-Control","must-revalidate");     
  response.addHeader("Pragma","no-cache");
%>

<jsp:include page="header.jsp" />
<script type="text/javascript">
<!--

function makeHttpRequest(url, callback_function, return_xml)
{
   var http_request = false;

   if (window.XMLHttpRequest) { // Mozilla, Safari,...
       http_request = new XMLHttpRequest();
       if (http_request.overrideMimeType) {
           http_request.overrideMimeType('text/xml');
       }
   } else if (window.ActiveXObject) { // IE
       try {
           http_request = new ActiveXObject("Msxml2.XMLHTTP");
       } catch (e) {
           try {
               http_request = new ActiveXObject("Microsoft.XMLHTTP");
           } catch (e) {}
       }
   }

   if (!http_request) {
       alert('Unfortunately you browser does not support this feature.');
       return false;
   }
   http_request.onreadystatechange = function() {
       if (http_request.readyState == 4) {
           if (return_xml) {
               eval(callback_function + 'http_request.responseXML)');
           } else {
               eval(callback_function + 'http_request.responseText)');
           }
       }
   }
   http_request.open('GET', url, true);
   http_request.send(null);
}

function trim(str){
  return str.replace(/^\s+|\s+$/g, '') ;
}

function addConvsFromTextArea(){
  var rawText = document.getElementById('convs').value;
  makeRequests(rawText);
}

function makeRequests(rawText){
  clearResultsDiv();
  var reqText = "";
  var normText = rawText.replace(/ /g, '\n');
  var convs = normText.split("\n");
  for(i in convs){
    var conv = trim(convs[i]);
    if(conv != ''){
      reqText = 'addConversion?mailbox=' + conv + '&time=' + new Date();
      makeHttpRequest(reqText, 'processRequest("' + conv + '", ');
    } 
  }
}

function processRequest(conv, response){
  appendToResultsDiv("<table><tr><td>" + conv + ": " + response.split("\n")[0] + "</td></tr></table>");
}

function clearResultsDiv() {

  if (document.all) {
    document.all['resultsDiv'].innerHTML = "";
  } else {
    var rd = document.getElementById("resultsDiv");
    rd.innerHTML = "";
  }
}

function appendToResultsDiv(txt) {
  if (document.all) {
    document.all['resultsDiv'].innerHTML = document.all['resultsDiv'].innerHTML + txt;
  } else {
    var rd = document.getElementById("resultsDiv");
    rd.innerHTML = rd.innerHTML + txt;  
  }
}

function changeTarget(){
  //'upload_target' is the name of the iframe
  document.getElementById('file_upload_form').target = 'upload_target';
}

function processResults(mailboxes){
  makeRequests(trim(mailboxes));
}

-->
</script>

<form id="file_upload_form" method="post" enctype="multipart/form-data" action="echo.jsp" onsubmit="changeTarget()">
    <span class="hdr">Add Conversions</span><p />
<span class="txt">
        Enter the eIDs of the users to migrate in the box below.<br />
        imap2exchange will automatically try to find the correct mail store for the user.<br />
</span>
    <table>
      <tr>
      </tr>
      <tr>
        <td valign="top">
          <textarea rows="30" cols="50" id="convs"></textarea><br />
          <input type="button" value="Add Conversions" onclick="addConvsFromTextArea()" />
        </td>
        <td valign="top">
          <div id="resultsDiv" class="txt"> </div>
        </td>
      </tr>
      <tr>
        <td>&nbsp; </td>
      </tr>
  <!--
      <tr>
          <td><span class="txt">Or upload a file with the same format.<br /></span></td>
      </tr>
      <tr>
        <td>&nbsp; </td>
      </tr>
      <tr>
        <td>
          <input name="file" id="file" size="27" type="file" /><br />
          <input type="submit" name="action" value="Upload" /><br />
          <iframe id="upload_target" name="upload_target" src="" style="width:0;height:0;border:0px solid #fff;" ></iframe>
        </td>
      </tr>
      -->
  </table>

</form>

<br />
<br />

<jsp:include page="footer.jsp" />
