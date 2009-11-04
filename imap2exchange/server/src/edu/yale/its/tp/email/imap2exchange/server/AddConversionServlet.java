package edu.yale.its.tp.email.imap2exchange.server;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.imap.*;

/**
 * <pre>
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
 * </pre>
 *
 */
public class AddConversionServlet extends HttpServlet {

    static final long serialVersionUID = AddConversionServlet.class.getName().hashCode();

    static final String EXCHANGE_CONVERSION_MANAGER = "exchange.conversion.manager";
    static final String OK = "OK";

    static final String MAILBOX = "mailbox";
    static final String UID = "uid";
    static final String PO = "po";

    @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.doPost(request, response);
        }

    @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            // DO NOT CACHE THIS PAGE!!!
            response.addHeader("Cache-Control","no-cache");               
            response.addHeader("Cache-Control","no-store");               
            response.addHeader("Cache-Control","must-revalidate");     

            ServletOutputStream out = response.getOutputStream();
            ServletContext context = request.getSession().getServletContext();
            ExchangeConversionManager manager = (ExchangeConversionManager)context.getAttribute(EXCHANGE_CONVERSION_MANAGER);
            if(manager == null){
                manager = ExchangeConversionManager.getInstance();
                if(manager == null){
                    throw new ServletException("Exchange Conversion Manager not initialized by Spring Container.");
                }
                context.setAttribute(EXCHANGE_CONVERSION_MANAGER, manager);
            }

            User user = null;
            try{
                user = getUser(request, response);
                //validatePo(user.getSourceImapPo());
                checkQueuedAndRunningConversions(manager, user);
                ExchangeConversion conv = ExchangeConversionFactory.getInstance().makeExchangeConversion(user);
                conv.setFinalRun(isFinalConversion(request)); 
                manager.addConversion(conv);
                out.println(OK);
            } catch (ServletException se){
                out.println(se.getMessage());
            }

        }

    public boolean isFinalConversion(HttpServletRequest request)throws ServletException, IOException {
        String finalRun = request.getParameter("finalRun");
        return finalRun != null && Boolean.parseBoolean(finalRun);
    }


    public User getUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String mailbox = request.getParameter(MAILBOX);
        String uid = request.getParameter(UID);
        String po = request.getParameter(PO);

        if(mailbox != null
                && !mailbox.trim().equals("")){
            String[] parts = mailbox.split("@");
            if(parts.length > 2){
                throw new ServletException("Invalid Mailbox: [" + mailbox + "]");
            }
            uid = parts[0];
            if (parts.length == 2) {
                po = parts[1];
            } else {
                // We'll get the Po from eDir anyway.
                po = "";
            }
        }

        if(uid == null || uid.trim().equals("")){
            throw new ServletException("Invalid Uid [" + uid + "]");
        }

        return UserFactory.getInstance().createUser(uid.trim(), po.trim());

    }

    protected void validatePo(String po) throws ServletException {
        if(!ImapServerFactory.getInstance().contains(po)){
            throw new ServletException("Invalid Po [" + po + "]");
        }
    }

    protected void checkQueuedAndRunningConversions(ExchangeConversionManager manager, User user) throws ServletException {
        List<ExchangeConversion> queued = manager.getQueued();
        List<ExchangeConversion> running = manager.getRunning();
        if(containsUser(queued, user)){
            throw new ServletException("User [" + user.getUid() + "] is in queue.");
        }
        if(containsUser(running, user)){
            throw new ServletException("User [" + user.getUid() + "] is currently running.");
        }
    }

    protected boolean containsUser(List<ExchangeConversion> convs, User user){
        for(ExchangeConversion conv : convs){
            if(conv.getUser().getUid().equals(user.getUid())) return true;
        }
        return false;
    }

}
