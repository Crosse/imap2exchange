package edu.jmu.email.conversion.jmu;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;
import org.apache.log4j.Logger;
import edu.yale.its.tp.email.conversion.trust.AllTrustingSocketFactory;

/**
 * 
 * <pre>
 * Copyright (c) 2000-2003 James Madison University. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 * DISCLAIMED. IN NO EVENT SHALL JAMES MADISON UNIVERSITY OR ITS EMPLOYEES BE
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
 * includes software developed by James Madison University," in any related
 * documentation and, if feasible, in the redistributed software.
 * 
 * 3. The names "JMU" and "James Madison University" must not be used to endorse
 * or promote products derived from this software.
 * </pre>
 *

 *
 */
public class JmuLdap {

    private static Logger logger = Logger.getLogger(JmuLdap.class);

    private static final String SECURE_PORT = "636"; 

    private static JmuLdap instance;

    public JmuLdap(){
        instance = this;
    }

    static public JmuLdap getInstance(){
        return instance;
    }

    Hashtable<String, Object> env;
    String uri;
    String port;
    String base;
    String uid;
    String pwd;

    public void logConfig(){
        if(logger.isDebugEnabled()){
            logger.debug("========================================");
            logger.debug("           LDAP Configuration");
            logger.debug("========================================");
            logger.debug("uri=" + uri);
            logger.debug("port=" + port);
            logger.debug("base=" + base);
            logger.debug("========================================");
        }
    }

    void initEnv(){
        env = new Hashtable<String, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + uri + ":" + port + "/" + base);
        if(port.equals(SECURE_PORT)){
            env.put(Context.SECURITY_PROTOCOL, "ssl");
            env.put("java.naming.ldap.factory.socket", AllTrustingSocketFactory.class.getName());
        }
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, uid);
        env.put(Context.SECURITY_CREDENTIALS, pwd);
        env.put("com.sun.jndi.ldap.connect.pool", "true");
    }

    public DirContext getLdap(){
        DirContext directory;
        if(env == null)	initEnv();
        try{
            directory = new InitialDirContext(env);
        } catch (NamingException ne) {
            throw new RuntimeException("Error Connecting to LDAP Server: " + uri + ":" + port, ne);
        }
        return directory;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
