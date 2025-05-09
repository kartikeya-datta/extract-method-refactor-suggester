error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/872.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/872.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/872.java
text:
```scala
public S@@ervletWrapper getHandlerForPath( Context ctx, String path ) {

/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */ 


package org.apache.tomcat.core;

import org.apache.tomcat.core.*;
import org.apache.tomcat.net.*;
import org.apache.tomcat.context.*;
import org.apache.tomcat.loader.*;
import org.apache.tomcat.request.*;
import org.apache.tomcat.util.*;
import org.apache.tomcat.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.tomcat.service.PoolTcpConnector;

//import java.security.*;

/* XXX The main function of CM is to serve as an entry point into
   tomcat and manage a list of resources that are part of the servlet
   processing. ( manage == keep a list and provide centralized access ).

   It also have helper functions for common callbacks - but we need to
   review and change that.
*/
/*
 * It is possible to extend and override some of the methods ( this is not
 * "final" ), but this is an extreme case and shouldn't be used - if you want
 * to extend the server you should use interceptors.
 * Another extreme case is having more than one ContextManager instances.
 * Each will corespond to a separate servlet engine/container that will work
 * independent of each other in the same VM ( each having possible multiple
 * virtual hosts, etc). Both uses are not forbiden, but shouldn't be used
 * unless there is real need for that - and if that happen we should 
 * add interfaces to express the use cases.
 */


/**
 * ContextManager is the entry point and "controler" of the servlet execution.
 * It maintains a list of WebApplications and a list of global event interceptors
 * that are set up to handle the actual execution.
 * 
 * The ContextManager is a helper that will direct the request processing flow
 * from its arrival from the server/protocl adapter ( in service() ).
 * It is also responsible for controlling the request processing steps, from
 * request parsing and mapping, auth, autorization, pre/post service, actual
 * invocation and logging.
 * 
 * It will also store properties that are global to the servlet container - 
 * like root directory, install dir, work dir. 
 *
 *
 * @author James Duncan Davidson [duncan@eng.sun.com]
 * @author James Todd [gonzo@eng.sun.com]
 * @author Harish Prabandham
 * @author costin@eng.sun.com
 */
public class ContextManager { 

    /**
     * The string constants for this ContextManager.
     */
    private StringManager sm = StringManager.getManager("org.apache.tomcat.core");

    /** Global interceptors - all requests that will be served by this
	engine will pass those filters
    */
    private Vector requestInterceptors = new Vector();
    private Vector contextInterceptors = new Vector();
    
    // cache - faster access to interceptors, using [] instead of Vector
    ContextInterceptor cInterceptors[];
    RequestInterceptor rInterceptors[];

    /**
     * The default security permissions to use
     */
    private Object permissions;

    /** Adapters for the incoming protocol
     */
    Vector connectors=new Vector();

    /** Contexts managed by this server
     */
    private Vector contextsV=new Vector();

    int debug=0;

    // Global properties for this tomcat instance:
    
    /** Private workspace for this server
     */
    String workDir;

    /** The base directory where this instance runs.
     *  It can be different from the install directory to
     *  allow one install per system and multiple users
     */
    String home;

    /** The directory where tomcat is installed
     */  
    String installDir;

    /** Default work dir, relative to home
     */
    public static final String DEFAULT_WORK_DIR="work";
    
    /**
     * Construct a new ContextManager instance with default values.
     */
    public ContextManager() {
    }

    // -------------------- setable properties: tomcat directories  ---
    /** 
     *  The home of the tomcat instance - you can have multiple
     *  users running tomcat, with a shared install directory.
     *  Every instance will have its own logs, webapps directory
     *  and local config, all relative to this directory.
     */
    public void setHome(String home) {
	this.home=FileUtil.getCanonicalPath( home ); 
	logInt( "Setting home to " + this.home );
    }
    
    /** 
     *  The home of the tomcat instance - you can have multiple
     *  users running tomcat, with a shared install directory.
     *  Every instance will have its own logs, webapps directory
     *  and local config, all relative to this directory.
     *
     *  If no home is configured we'll try the install dir
     *  XXX clean up the order and process of guessing - maybe we can
     *  just throw error instead of guessing wrong.
     */
    public String getHome() {
	if( debug > 20 ) {
	    // we want to know all places that need this property
	    // and find how it's computed - for embeding tc.
	    logInt( "getHome " + home + " " + installDir + " " +
		 System.getProperty("tomcat.home") + " " +
		 FileUtil.getCanonicalPath( "." ));
	    /*DEBUG*/ try {throw new Exception(); } catch(Exception ex)
		{ex.printStackTrace();}
	}
	
	if(home!=null) return home;

	// If none defined, assume tomcat.home is used as base.
	if( installDir != null )
	    home=FileUtil.getCanonicalPath( installDir );

	if(home!=null) return home;

	// try at least the system property
	home=FileUtil.getCanonicalPath( System.getProperty("tomcat.home") );	
	if(home!=null) return home;
	
	home=FileUtil.getCanonicalPath( "." );
	// try current dir - we should throw an exception
	return home;
    }

    /** Get installation directory, where libraries and default files
     *	are located.  If path specified is relative, 
     *  evaluate it relative to the current working directory.
     */
    public String getInstallDir() {
	if( debug > 20 ) {
	    // we want to know all places that need this property
	    // and find how it's computed - for embeding tc.
	    logInt( "getInstallDir " + installDir + " " +
		 System.getProperty("tomcat.home"));
	    /*DEBUG*/ try {throw new Exception(); } catch(Exception ex) {
		ex.printStackTrace();}
	}

	if(installDir!= null) return installDir;
	
	installDir=System.getProperty("tomcat.home");
	if(installDir!= null) return installDir;

	// If the property is not set ( for example JNI worker ) assume
	// at least home is set up corectly.
	installDir=getHome();
	return installDir;
    }

    /** Set installation directory, where libraries and default files
     *	are located.  If path specified is relative, 
     *  evaluate it relative to the current working directory.
     */
    public void setInstallDir( String tH ) {
	installDir=tH;
    }
    
    /**
     * WorkDir property - where all working files will be created
     */ 
    public void setWorkDir( String wd ) {
	if(debug>0) logInt("set work dir " + wd);
	// make it absolute
	File f=new File( wd );
	if( ! f.isAbsolute() ) {
	    File wdF=getAbsolute( f );
	    wd= wdF.getAbsolutePath();
	}

	this.workDir=wd;
    }

    /**
     * WorkDir property - where all working files will be created
     */ 
    public String getWorkDir() {
	if( workDir==null)
	    workDir=getHome() + File.separator + DEFAULT_WORK_DIR;
	return workDir;
    }
    
    /**
     * Get the default Security Permissions for this server
     */
    public Object getPermissions() {
	return permissions;
    }

    /**
     * Add a Permission to the default Permissions
     */
    public void setPermissions(Object permissions) {
	this.permissions = permissions;
    }

    
    // -------------------- Support functions --------------------

    /**
     *  Set default settings ( interceptors, connectors, loader, manager )
     *  It is called from init if no connector is set up  - note that we
     *  try to avoid any "magic" - you either set up everything ( using
     *  server.xml or alternatives) or you don't set up and then defaults
     *  will be used.
     * 
     *  Set interceptors or call setDefaults before adding contexts.
     *
     *  This is mostly used to allow "0 config" case ( you just want the
     *  reasonable defaults and nothing else ).
     */
    public void setDefaults() {
	if(connectors.size()==0) {
	    if(debug>5) logInt("Setting default adapter");
	    PoolTcpConnector sc=new PoolTcpConnector();
	    sc.setTcpConnectionHandler( new
		org.apache.tomcat.service.http.HttpConnectionHandler());
	    addServerConnector(  sc );
	}
	
	if( contextInterceptors.size()==0) {
	    if(debug>5) logInt("Setting default context interceptors");
	    addContextInterceptor(new LogEvents());
	    addContextInterceptor(new AutoSetup());
	    //	    addContextInterceptor(new PolicyInterceptor());
	    addContextInterceptor(new LoaderInterceptor());
	    addContextInterceptor(new DefaultCMSetter());
	    addContextInterceptor(new WorkDirInterceptor());
	    addContextInterceptor( new WebXmlReader());
	    addContextInterceptor(new LoadOnStartupInterceptor());
	}
	
	if( requestInterceptors.size()==0) {
	    if(debug>5) logInt("Setting default request interceptors");
	    addRequestInterceptor(new SessionInterceptor());
	    SimpleMapper1 smap=new SimpleMapper1();
	    smap.setContextManager( this );
	    addRequestInterceptor(smap);

	    addRequestInterceptor(new
		org.apache.tomcat.session.StandardSessionInterceptor());
	}
    }
     
    /** Init() is called after the context manager is set up
     *  and configured. It will init all internal components
     *  to be ready for start.
     *
     *  There is a difference between Context and Adapters - the
     *  adapter has start/stop, the Context has init/shutdown(destroy
     *  may be a better name ? ). ( Initializing is different from starting.)
     */
    public void init()  throws TomcatException {
	//	logInt( "Tomcat install = " + getInstallDir());
	// logInt( "Tomcat home = " + home);
	if(debug>0 ) logInt( "Tomcat classpath = " +
			     System.getProperty( "java.class.path" ));

	setAccount( ACC_INIT_START, System.currentTimeMillis());
	
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].engineInit( this );
	}

    	// init contexts
	// XXX init must be called whith no context inside !!!
	Enumeration enum = getContexts();
	while (enum.hasMoreElements()) {
	    Context context = (Context)enum.nextElement();
	    try {
		initContext( context );
	    } catch (TomcatException ex ) {
		if( context!=null ) {
		    logInt( "ERROR initializing " + context.toString() );
		    removeContext( context  );	    
		    Throwable ex1=ex.getRootCause();
		    if( ex1!=null ) ex.printStackTrace();
		}
	    }
	}
	setAccount( ACC_INIT_END, System.currentTimeMillis() );
    }

    /** Will shutdown all contexts
     */
    public void shutdown() throws TomcatException {
	Enumeration enum = getContexts();
	while (enum.hasMoreElements()) {
	    removeContext((Context)enum.nextElement());
	}

	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].engineShutdown( this );
	}
    }
    
    /**
     * Initializes this context to be able to accept requests. This action
     * will cause the context to load it's configuration information
     * from the webapp directory in the docbase.
     *
     * <p>This method must be called
     * before any requests are handled by this context. It will be called
     * after the context was added, typically when the engine starts
     * or after the admin added a new context.
     */
    public void initContext( Context ctx ) throws TomcatException {
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].contextInit( ctx );
	}
    }

    /** Stop the context and release all resources.
     */
    public void shutdownContext( Context ctx ) throws TomcatException {
	// XXX This is here by accident, it should be moved as part
	// of a normal context interceptor that will handle all standard
	// start/stop actions
	
	// shut down and servlets
	Enumeration enum = ctx.getServletNames();
	while (enum.hasMoreElements()) {
	    String key = (String)enum.nextElement();
	    ServletWrapper wrapper = ctx.getServletByName( key );
	    ctx.removeServletByName( key );
	    try {
		wrapper.destroy();
	    } catch(Exception ex ) {
		ctx.log( "Error in destroy ", ex);
	    }
	}
	
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].contextShutdown( ctx );
	}
    }
    
    /** Will start the connectors and begin serving requests.
     *  It must be called after init.
     */
    public void start() throws Exception {// XXX TomcatException {
	Enumeration connE=getConnectors();
	while( connE.hasMoreElements() ) {
	    ((ServerConnector)connE.nextElement()).start();
	}
    }

    /** Will stop all connectors
     */
    public void stop() throws Exception {// XXX TomcatException {
	if(debug>0) logInt("Stopping context manager ");
	Enumeration connE=getConnectors();
	while( connE.hasMoreElements() ) {
	    ((ServerConnector)connE.nextElement()).stop();
	}
	shutdown();
    }

    // -------------------- Contexts -------------------- 
    /** Return the list of contexts managed by this server
     */
    public Enumeration getContexts() {
	return contextsV.elements();
    }
    
    /**
     * Adds a new Context to the set managed by this ContextManager.
     *
     * @param ctx context to be added.
     */
    public void addContext( Context ctx ) throws TomcatException {
	// Make sure context knows about its manager.
	ctx.setContextManager( this );

	// If the context already exist - the interceptors need
	// to deal with that ( either replace or throw an exception ).

	// The mapping alghoritm may use more than path and host -
	// if not now, then in future.

	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].addContext( this, ctx );
	}

	String vhost=ctx.getHost();
	logInt("Adding context " +  ctx.toString());

	// XXX temporary workaround for the old SimpleMapper -
	// This code will be removed as soon as the new mapper is stable.
	if( vhost ==null ) // the old mapper will support only "default" server
	    contexts.put( ctx.getPath(), ctx );
	contextsV.addElement( ctx );
    }
    
    /** Shut down and removes a context from service
     */
    public void removeContext( Context context ) throws TomcatException {
	if( context==null ) return;
	
	logInt( "Removing context " + context.toString());

	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].removeContext( this, context );
	}

	shutdownContext( context );
	contextsV.removeElement(context);
    }

    void doReload( Request req, Context context ) throws TomcatException {
	if( context==null ) return;
	
	if( debug>0 ) logInt( "Reloading context " + context.toString());

	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].reload(  req, context );
	}
    }


    /** Notify interceptors that a new container was added.
     */
    public void addContainer( Container container )
    	throws TomcatException
    {
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].addContainer( container);
	}
    }

    /** Notify interceptors that a container was removed.
     */
    public void removeContainer( Container container )
	throws TomcatException
    {
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].removeContainer( container);
	}
    }

    // -------------------- Connectors and Interceptors --------------------

    /**
     * Add the specified server connector to the those attached to this server.
     *
     * @param con The new server connector
     */
    public synchronized void addServerConnector( ServerConnector con ) {
	if(debug>0) logInt("Add connector javaClass=\"" +
			   con.getClass().getName() + "\"");
	con.setServer( this );
	connectors.addElement( con );
    }

    public Enumeration getConnectors() {
	return connectors.elements();
    }
    
    public void addRequestInterceptor( RequestInterceptor ri ) {
	if(debug>0) logInt("Add requestInterceptor javaClass=\"" +
			   ri.getClass().getName() + "\" ");
	requestInterceptors.addElement( ri );
	if( ri instanceof ContextInterceptor )
	    contextInterceptors.addElement( ri );
    }

    /** Return all the interceptors associated with a request.
	That includes global ( context manager ) interceptors,
	webapp ( Context ) interceptors and possibly interceptors
	associated with containers ( urls inside the web app ).
	
	For performance reasons we use arrays and cache the result inside
	containers.

	XXX Todo: container-level interceptors are not supported.
	Dynamic add of interceptors is not supported.
    */
    public RequestInterceptor[] getRequestInterceptors( Request req ) {
	// 	Container ct=req.getContext().getContainer();
	// 	return ct.getRequestInterceptors();

	// just global interceptors
	return getRequestInterceptors();
    }

    /** Return the context interceptors as an array.
	For performance reasons we use an array instead of
	returning the vector - the interceptors will not change at
	runtime and array access is faster and easier than vector
	access
    */
    public RequestInterceptor[] getRequestInterceptors() {
	if( rInterceptors == null ||
	    rInterceptors.length != requestInterceptors.size())
	{
	    rInterceptors=new RequestInterceptor[requestInterceptors.size()];
	    for( int i=0; i<rInterceptors.length; i++ ) {
		rInterceptors[i]=(RequestInterceptor)
		    requestInterceptors.elementAt(i);
	    }
	}
	return rInterceptors;
    }

    public void addContextInterceptor( ContextInterceptor ci) {
	if(debug>0) logInt("Add contextInterceptor javaClass=\"" +
			   ci.getClass().getName() + "\" ");
	contextInterceptors.addElement( ci );
    }


    /** Return the context interceptors as an array.
	For performance reasons we use an array instead of
	returning the vector - the interceptors will not change at
	runtime and array access is faster and easier than vector
	access
    */
    public ContextInterceptor[] getContextInterceptors() {
	if( contextInterceptors.size() == 0 ) {
	    setDefaults();
	}
	if( cInterceptors == null ||
	    cInterceptors.length != contextInterceptors.size())
	{
	    cInterceptors=new ContextInterceptor[contextInterceptors.size()];
	    for( int i=0; i<cInterceptors.length; i++ ) {
		cInterceptors[i]=(ContextInterceptor)contextInterceptors.
		    elementAt(i);
	    }
	}
	return cInterceptors;
    }
    // -------------------- Request processing / subRequest ------------------
    // -------------------- Main request processing methods ------------------ 

    /** Prepare the req/resp pair for use in tomcat.
     *  Call it after you create the request/response objects
     */
    public void initRequest( Request req, Response resp ) {
	// used to be done in service(), but there is no need to do it
	// every time. 
	// We may add other special calls here.
	// XXX Maybe make it a callback? 
	resp.setRequest( req );
	req.setResponse( resp );
	req.setContextManager( this );
    }

    /** This is the entry point in tomcat - the connectors ( or any other
     *  component able to generate Request/Response implementations ) will
     *  call this method to get it processed.
     *  XXX make sure the alghoritm is right, deal with response codes
     */
    public void service( Request req, Response res ) {
	internalService( req, res );
	// clean up
	try {
	    res.finish();
	    req.recycle();
	    res.recycle();
	} catch( Throwable ex ) {
	    handleError( req, res, ex );
	}
	return;
    }

    // Request processing steps and behavior
    private void internalService( Request req, Response res ) {
	try {
	    /* assert req/res are set up
	       corectly - have cm, and one-one relation
	    */
	    // wront request - parsing error
	    int status=res.getStatus();

	    if( status >= 400 ) {
		if( debug > 0)
		    log( "Error reading request " + req + " " + status);
		handleStatus( req, res, status );
		return;
	    }

	    status= processRequest( req );
	    if( status != 0 ) {
		if( debug > 0)
		    log("Error mapping the request " + req + " " + status);
		handleStatus( req, res, status );
		return;
	    }

	    if( req.getWrapper() == null ) {
		status=404;
		if( debug > 0)
		    log("No handler for request " + req + " " + status);
		handleStatus( req, res, status );
		return;
	    }
	    
	    String roles[]=req.getRequiredRoles();
	    if(roles != null )
		status=doAuthorize( req, res, roles );
	    if( status > 200 ) {
		if( debug > 0)
		    log("Authorize error " + req + " " + status);
		handleStatus( req, res, status );
		return;
	    }

	    req.getWrapper().service(req, res);

	} catch (Throwable t) {
	    handleError( req, res, t );
	}
    }

    /** Will find the ServletWrapper for a servlet, assuming we already have
     *  the Context. This is also used by Dispatcher and getResource -
     *  where the Context is already known. 
     */
    public int processRequest( Request req ) {
	if(debug>9) logInt("ProcessRequest: "+req.toString());
	int status=0;
	
	for( int i=0; i< requestInterceptors.size(); i++ ) {
	    status=((RequestInterceptor)requestInterceptors.elementAt(i)).
		contextMap( req );
	    if( status!=0 ) return status;
	}

	for( int i=0; i< requestInterceptors.size(); i++ ) {
	    status=((RequestInterceptor)requestInterceptors.elementAt(i)).
		requestMap( req );
	    if( status!=0 ) return status;
	}

	if(debug>9) logInt("After processing: "+req.toString());
	
	return 0;
    }

    /** Call all authentication callbacks. If any of them is able to
	identify the user it will set the principal in req.
     */
    public int doAuthenticate( Request req, Response res ) {
	int status=0;
	RequestInterceptor reqI[]= getRequestInterceptors(req);
	
	for( int i=0; i< reqI.length; i++ ) {
	    status=reqI[i].authenticate( req, res );
	    if ( status != 0 ) {
		if( debug>0) logInt( "Authenticate status " + status );
		return status;
	    }
	}
	return 0;
    }

    /** Call all authorization callbacks. The "require valid user" attributes
	are probably set during the mapping stage ( for efficiency), but it
	can be done here too.
     */
    public int doAuthorize( Request req, Response res, String roles[] ) {
	int status=0;
	RequestInterceptor reqI[]= getRequestInterceptors(req);
	
	for( int i=0; i< reqI.length; i++ ) {
	    status = reqI[i].authorize( req, res, roles );
	    if ( status != 0 ) {
		if( debug>0) logInt( "Authorize status " + status );
		return status;
	    }
	}
	return 0;
    }

    /** Call beforeBody callbacks. Before body allows you do do various
	actions before the first byte of the response is sent. After all
	those callbacks are called tomcat may send the status and headers
    */
    int doBeforeBody( Request req, Response res ) {
	RequestInterceptor reqI[]= getRequestInterceptors(req);
	
	for( int i=0; i< reqI.length; i++ ) {
	    reqI[i].beforeBody( req, res );
	}
	return 0;
    }

    /** Call beforeCommit callbacks. This allows interceptors to manipulate the
	buffer before it gets sent.
	XXX Add a standard way to access the body. The method was not used too
	much, we need a review and maybe change in parameters.
    */
    int doBeforeCommit( Request req, Response res ) {
	RequestInterceptor reqI[]= getRequestInterceptors(req);
	
	for( int i=0; i< reqI.length; i++ ) {
	    reqI[i].beforeCommit( req, res );
	}
	return 0;
    }
    
    int doPreService( Request req, Response res ) {
	RequestInterceptor reqI[]= getRequestInterceptors(req);
	
	for( int i=0; i< reqI.length; i++ ) {
	    reqI[i].preService( req, res );
	}
	return 0;
    }
    
    int doPostService( Request req, Response res ) {
	RequestInterceptor reqI[]= getRequestInterceptors(req);
	
	for( int i=0; i< reqI.length; i++ ) {
	    reqI[i].postService( req, res );
	}
	return 0;
    }
    
    int doNewSessionRequest( Request req, Response res ) {
	RequestInterceptor reqI[]= getRequestInterceptors(req);
	
	for( int i=0; i< reqI.length; i++ ) {
	    reqI[i].newSessionRequest( req, res );
	}
	return 0;
    }

    /** Call afterBody callbacks. It is called after the servlet finished
	sending the response ( either closeing the stream or ending ). You
	can deal with connection reuse or do other actions
    */
    int doAfterBody( Request req, Response res ) {
	RequestInterceptor reqI[]= getRequestInterceptors(req);
	
	for( int i=0; i< reqI.length; i++ ) {
	    reqI[i].afterBody( req, res );
	}
	return 0;
    }

    // -------------------- Sub-Request mechanism --------------------

    /** Create a new sub-request in a given context, set the context "hint"
     *  This is a particular case of sub-request that can't get out of
     *  a context ( and we know the context before - so no need to compute it
     *  again)
     *
     *  Note that session and all stuff will still be computed.
     */
    public Request createRequest( Context ctx, String urlPath ) {
	// assert urlPath!=null

	// deal with paths with parameters in it
	String contextPath=ctx.getPath();
	String origPath=urlPath;

	// append context path
	if( !"".equals(contextPath) && !"/".equals(contextPath)) {
	    if( urlPath.startsWith("/" ) )
		urlPath=contextPath + urlPath;
	    else
		urlPath=contextPath + "/" + urlPath;
	} else {
	    // root context
	    if( !urlPath.startsWith("/" ) )
		urlPath= "/" + urlPath;
	}

	if( debug >4 ) logInt("createRequest " + origPath + " " + urlPath  );
	Request req= createRequest( urlPath );
	String host=ctx.getHost();
	if( host != null) req.setServerName( host );
	return req;
    }

    /** Create a new sub-request, deal with query string
     */
    public Request createRequest( String urlPath ) {
	String queryString=null;
	int i = urlPath.indexOf("?");
	int len=urlPath.length();
	if (i>-1) {
	    if(i<len)
		queryString =urlPath.substring(i + 1, urlPath.length());
	    urlPath = urlPath.substring(0, i);
	}

	/** Creates an "internal" request
	 */
	RequestImpl lr = new RequestImpl();
	//	RequestAdapterImpl reqA=new RequestAdapterImpl();
	//lr.setRequestAdapter( reqA);
	lr.setRequestURI( urlPath );
	lr.setQueryString( queryString );
	//	lr.processQueryString();

	//	lr.setContext( ctx );
	
	// XXX set query string too 
	return lr;
    }

    // -------------------- Error handling --------------------

    /** Called for error-codes
     */
    public void handleStatus( Request req, Response res, int code ) {
	String errorPath=null;
	Handler errorServlet=null;
	
	res.reset();

	if( code==0 )
	    code=res.getStatus();
	else
	    res.setStatus(code);

	Context ctx = req.getContext();
	if(ctx==null) ctx=getContext("");

	ctx.log( "Status Handler: " + code + " req=" + req);

	errorPath = ctx.getErrorPage( code );
	if( errorPath != null ) {
	    errorServlet=getHandlerForPath( ctx, errorPath );
	}
	if( debug>-1 )
	    ctx.log( "Handler " + errorServlet + " " + errorPath);
	
	if( errorServlet==null )
	    errorServlet=ctx.getServletByName( "tomcat.statusHandler");

	req.setAttribute("javax.servlet.error.status_code",new Integer( code));

	req.setAttribute("tomcat.servlet.error.request", req);

	errorServlet.service( req, res );
    }

    // XXX XXX Security - we should log the message, but nothing
    // should show up  to the user - it gives up information
    // about the internal system !
    // Developers can/should use the logs !!!
    
    /** General error handling mechanism. It will try to find an error handler
     * or use the default handler.
     */
    void handleError( Request req, Response res , Throwable t  ) {
	Context ctx = req.getContext();
	if(ctx==null) {
	    ctx=getContext("");
	}

	/** The exception must be available to the user.
	    Note that it is _WRONG_ to send the trace back to
	    the client. AFAIK the trace is the _best_ debugger.
	*/
	if( t instanceof IllegalStateException ) {
	    ctx.log("IllegalStateException in: " + req  + " " +
		    t.getMessage() );
	} else if( t instanceof org.apache.jasper.JasperException ) {
	    ctx.log("JasperException: " + req + " "  + t.getMessage());
	} else if( t instanceof IOException ) {
	    if( ((IOException)t).getMessage().equals("Broken pipe"))
		return;
	    ctx.log("IOException in: " + req + " "  + t.getMessage());
	} else {
	    ctx.log("Exception in: " + req , t );
	}

	if(null!=req.getAttribute("tomcat.servlet.error.defaultHandler")){
	    // we are in handleRequest for the "default" error handler
	    System.out.println("ERROR: can't find default error handler "+
			       "or error in default error page");
	    t.printStackTrace();
	} 

	
	String errorPath=null;
	Handler errorServlet=null;

	// Scan the exception's inheritance tree looking for a rule
	// that this type of exception should be forwarded
	Class clazz = t.getClass();
	while (errorPath == null && clazz != null) {
	    String name = clazz.getName();
	    errorPath = ctx.getErrorPage(name);
	    clazz = clazz.getSuperclass();
	}
	
	if( errorPath != null ) {
	    errorServlet=getHandlerForPath( ctx, errorPath );
	}

	if( errorLoop( ctx, req ) || errorServlet==null) ;
	    errorServlet = ctx.getServletByName("tomcat.exceptionHandler");
	
	req.setAttribute("javax.servlet.error.exception_type", t.getClass());
	req.setAttribute("javax.servlet.error.message", t.getMessage());
	req.setAttribute("tomcat.servlet.error.throwable", t);
	req.setAttribute("tomcat.servlet.error.request", req);

	errorServlet.service( req, res );
    }

    public Handler getHandlerForPath( Context ctx, String path ) {
	if( ! path.startsWith( "/" ) ) {
	    return ctx.getServletByName( path );
	}
	RequestImpl req1=new RequestImpl();
	ResponseImpl res1=new ResponseImpl();
	initRequest( req1, res1 );
	
	req1.setRequestURI( ctx.getPath() + path );
	processRequest( req1 );
	return req1.getWrapper();
    }
    
    /** Handle the case of error handler generating an error or special status
     */
    private boolean errorLoop( Context ctx, Request req ) {
	if( req.getAttribute("javax.servlet.error.status_code") != null
 req.getAttribute("javax.servlet.error.exception_type")!=null) {

	    if( ctx.getDebug() > 0 )
		ctx.log( "Error: exception inside exception servlet " +
			 req.getAttribute("javax.servlet.error.status_code") +
			 " " + req.
			 getAttribute("javax.servlet.error.exception_type"));
	    
	    return true;
	}
	return false;
    }
    

    // -------------------- Support for notes --------------------
        
    /** Note id counters. Synchronized access is not necesarily needed
     *  ( the initialization is in one thread ), but anyway we do it
     */
    private  int noteId[]=new int[3];

    /** Maximum number of notes supported
     */
    public static final int MAX_NOTES=32;
    public static final int RESERVED=3; 

    public static final int SERVER_NOTE=0;
    public static final int CONTAINER_NOTE=1;
    public static final int REQUEST_NOTE=2;

    public static final int REQ_RE_NOTE=0;
    
    String noteName[][]=new String[3][MAX_NOTES];

    /** used to allow interceptors to set specific per/request, per/container
     * and per/CM informations.
     *
     * This will allow us to remove all "specialized" methods in
     * Request and Container/Context, without losing the functionality.
     * Remember - Interceptors are not supposed to have internal state
     * and minimal configuration, all setup is part of the "core", under
     *  central control.
     *  We use indexed notes instead of attributes for performance -
     * this is internal to tomcat and most of the time in critical path
     */
    
    /** Create a new note id. Interceptors will get an Id at init time for
     *  all notes that it needs. 
     *
     *  Throws exception if too many notes are set ( shouldn't happen in
     *  normal use ).
     *  @param noteType The note will be associated with the server,
     *   container or request.
     *  @param name the name of the note.
     */
    public synchronized int getNoteId( int noteType, String name )
	throws TomcatException
    {
	// find if we already have a note with this name
	// ( this is in init(), not critical )
	for( int i=0; i< noteId[noteType] ; i++ ) {
	    if( name.equals( noteName[noteType][i] ) )
		return i;
	}
	
	if( noteId[noteType] >= MAX_NOTES )
	    throw new TomcatException( "Too many notes ");

	// make sure the note id is > RESERVED
	if( noteId[noteType] < RESERVED ) noteId[noteType]=RESERVED;

	noteName[noteType][ noteId[noteType] ]=name;
	return noteId[noteType]++;
    }

    public String getNoteName( int noteType, int noteId ) {
	return noteName[noteType][noteId];
    }
    
    // -------------------- Per-server notes -------------------- 
    Object notes[]=new Object[MAX_NOTES];

    public void setNote( int pos, Object value ) {
	notes[pos]=value;
    }

    public Object getNote( int pos ) {
	return notes[pos];
    }

    // -------------------- Logging and debug --------------------
    boolean firstLog = true;
    LogHelper loghelper = new LogHelper("tc_log", "ContextManager");
    
    // Not used, except in server.xml, and usage is unclear -- should
    // we kill it? Looks very obsolete.
    public void addLogger(Logger l) {
	// Will use this later once I feel more sure what I want to do here.
	// -akv
	// firstLog=false;
	//	if("tc_log".equals( logger.getName()) cmLog=logger;
	String path=l.getPath();
	if( path!=null ) {
	    File f=new File( path );
	    if( ! f.isAbsolute() ) {
		// Make it relative to home !
		File wd= getAbsolute( f );
		l.setPath( wd.getAbsolutePath() );
	    }
	    // create the files, ready to log.
	} 
	l.open();
    }


    public void setDebug( int level ) {
	if( level != 0 ) System.out.println( "Setting level to " + level);
	debug=level;
    }

    public int getDebug() {
	return debug;
    }
    
    public final void log(String msg) {
	loghelper.log(msg);
    }

    private final void logInt(String msg) {
	loghelper.log(msg);
    }

    public final void doLog(String msg) {
	loghelper.log(msg);
    }
    
    public final void doLog(String msg, Throwable t) {
	loghelper.log(msg, t);
    }

    public final void doLog(String msg, Throwable t, int level) {
	loghelper.log(msg, t, level);
    }

    // -------------------- Accounting --------------------
    // XXX Can be implemented as note !

    public static final int ACC_INIT_START=0;
    public static final int ACC_INIT_END=0;
    
    public static final int ACCOUNTS=7;
    long accTable[]=new long[ACCOUNTS];

    public void setAccount( int pos, long value ) {
	accTable[pos]=value;
    }

    public long getAccount( int pos ) {
	return accTable[pos];
    }

    // -------------------- DEPRECATED --------------------
    // XXX host and port are used only to construct a unique
    // work-dir for contexts, using them and the path
    // Since nobody sets them - I guess we can just drop them
    // anyway.
    // XXX ask and find if there is any other use!
    public static final String DEFAULT_HOSTNAME="localhost";
    public static final int DEFAULT_PORT=8080;
    String hostname;
    int port;

    /**
     * Sets the port number on which this server listens.
     *
     * @param port The new port number
     * @deprecated 
     */
    public void setPort(int port) {
	/*DEBUG*/ try {throw new Exception(); } catch(Exception ex) {ex.printStackTrace();}
	this.port=port;
    }

    /**
     * Gets the port number on which this server listens.
     * @deprecated 
     */
    public int getPort() {
	//	/*DEBUG*/ try {throw new Exception(); } catch(Exception ex) {ex.printStackTrace();}
	if(port==0) port=DEFAULT_PORT;
	return port;
    }

    /**
     * Sets the virtual host name of this server.
     *
     * @param host The new virtual host name
     * @deprecated 
     */
    public void setHostName( String host) {
	/*DEBUG*/ try {throw new Exception(); } catch(Exception ex) {ex.printStackTrace();}
	this.hostname=host;
    }

    /**
     * Gets the virtual host name of this server.
     * @deprecated 
     */
    public String getHostName() {
	//	/*DEBUG*/ try {throw new Exception(); } catch(Exception ex) {ex.printStackTrace();}
	if(hostname==null)
	    hostname=DEFAULT_HOSTNAME;
	return hostname;
    }
    // -------------------- DEPRECATED --------------------
    
    /**
     * The set of Contexts associated with this ContextManager,
     * keyed by context paths.
     * @deprecated - the server shouldn't make any assumptions about
     *  the key.
     */
    private Hashtable contexts = new Hashtable();

    /**
     * Get the names of all the contexts in this server.
     * @deprecated Path is not "unique key".
     */
    public Enumeration getContextNames() {
	/*DEBUG*/ try {throw new Exception(); } catch(Exception ex) {ex.printStackTrace();}
        return contexts.keys();
    }

    /**
     * Gets a context by it's name, or <code>null</code> if there is
     * no such context.
     *
     * @param name Name of the requested context
     * @deprecated Use an external iterator to find the context that
     *  matches your conditions.
     *
     */
    public Context getContext(String name) {
	// System.out.println("Using deprecated getContext");
	//	/*DEBUG*/ try {throw new Exception(); } catch(Exception ex) {ex.printStackTrace();}
	return (Context)contexts.get(name);
    }
    
    /**
     * Shut down and removes a context from service.
     *
     * @param name Name of the Context to be removed
     * @deprecated Use removeContext( Context ).
     */
    public void removeContext(String name) throws TomcatException {
	Context context = (Context)contexts.get(name);
	log( "Removing context " + context.toString());

	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    cI[i].removeContext( this, context );
	}

	if(context != null) {
	    shutdownContext( context );
	    contexts.remove(name);
	}
    }

    public void doPreServletInit(Context ctx, ServletWrapper sw)
	throws TomcatException
    {
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    try {
		cI[i].preServletInit( ctx, sw );
	    } catch( TomcatException ex) {
		ex.printStackTrace();
	    }
	}
    }

    public void doPostServletInit(Context ctx, ServletWrapper sw)
	throws TomcatException
    {
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    try {
		cI[i].postServletInit( ctx, sw );
	    } catch( TomcatException ex) {
		ex.printStackTrace();
	    }
	}
    }

    public void doPreServletDestroy(Context ctx, ServletWrapper sw)
	throws TomcatException
    {
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    try {
		cI[i].preServletDestroy( ctx, sw );
	    } catch( TomcatException ex) {
		ex.printStackTrace();
	    }
	}
    }

    public void doPostServletDestroy(Context ctx, ServletWrapper sw)
	throws TomcatException
    {
	ContextInterceptor cI[]=getContextInterceptors();
	for( int i=0; i< cI.length; i++ ) {
	    try {
		cI[i].postServletDestroy( ctx, sw );
	    } catch( TomcatException ex) {
		ex.printStackTrace();
	    }
	}
    }

    /** @deprecated
     */
    public void setTomcatHome( String s ) {
	setInstallDir( s );
    }

    /** @deprecated
     */
    public String getTomcatHome() {
	return getInstallDir();
    }

    /** Convert a relative name to absolute by using the "home" property
     */
    public File getAbsolute(File f) {
        if (!f.isAbsolute()) {
            // evaluate repository path relative to the context's home
	    // directory
	    return new File(getHome(), f.getPath());
        }
        return f;
    }
    
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/872.java