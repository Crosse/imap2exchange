<%@ page language="java" contentType="text/html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--
/**
 * 
 * Copyright (c) 2000-2003 Yale University. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 * DISCLAIMED. IN NO EVENT SHALL YALE UNIVERSITY OR ITS EMPLOYEES BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED, THE COSTS OF
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED IN ADVANCE OF THE POSSIBILITY OF SUCH
 * DAMAGE. 
 * 
 * Redistribution and use of this software in source or binary forms,
 * with or without modification, are permitted, provided that the
 * following conditions are met:
 * 
 * 1. Any redistribution must include the above copyright notice and
 * disclaimer and this list of conditions in any related documentation
 * and, if feasible, in the redistributed software.
 * 
 * 2. Any redistribution must include the acknowledgment, "This product
 * includes software developed by Yale University," in any related
 * documentation and, if feasible, in the redistributed software.
 * 
 * 3. The names "Yale" and "Yale University" must not be used to endorse
 * or promote products derived from this software.
 * 
 *
 */
-->
<%
  response.addHeader("Cache-Control","no-cache");               
  response.addHeader("Cache-Control","no-store");               
  response.addHeader("Cache-Control","must-revalidate");     
  response.addHeader("Pragma","no-cache");
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <title>imap2exchange Conversion Utility</title>
  </head> 

  <body>
    <table id="fullheight" width="100%" align="left">
      <tr>
        <td width="170" valign="top">
          <jsp:include page="menu.jsp" />
        </td>
        <td valign="top">
