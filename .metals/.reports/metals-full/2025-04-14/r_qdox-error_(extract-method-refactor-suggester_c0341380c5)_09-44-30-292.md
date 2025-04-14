error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8578.java
text:
```scala
i@@nt debug=21;

package org.apache.tomcat.startup;

import java.net.*;
import java.io.*;

import org.apache.tomcat.core.*;
import org.apache.tomcat.request.*;
import org.apache.tomcat.service.*;
import org.apache.tomcat.service.http.*;
import org.apache.tomcat.session.StandardSessionInterceptor;
import org.apache.tomcat.context.*;
import java.security.*;
import javax.servlet.*;
import java.util.*;

/**
 *  Use this class to embed tomcat in your application.
 *  The order is important:
 *  1. set properties like workDir and debug
 *  2. add all interceptors including your application-specific
 *  3. add the endpoints 
 *  4. add at least the root context ( you can add more if you want )
 *  5. call start(). The web service will be operational.
 *  6. You can add/remove contexts
 *  7. stop().
 *  
 *  You can add more contexts after start, but interceptors and  
 *  endpoints must be set before the first context and root must be
 *  set before start().
 *
 *  All file paths _must_ be absolute. ( right now if the path is relative it
 *  will be made absolute using tomcat.home as base. This behavior is very
 *  "expensive" as code complexity and will be deprecated ).
 * 
 * @author costin@eng.sun.com
 */
public class EmbededTomcat { // extends WebService
    ContextManager contextM = null;
    Object application;
    // null == not set up
    Vector requestInt=null;
    Vector contextInt=null;
    /** Right now we assume all web apps use the same
	servlet API version. This will change after we
	finish the FacadeManager implementation
    */
    FacadeManager facadeM=null;
    Vector connectors=new Vector();

    String workDir;
    
    // configurable properties
    int debug=0;
    
    public EmbededTomcat() {
    }

    // -------------------- Properties - set before start
    
    /** Set debugging - must be called before anything else
     */
    public void setDebug( int debug ) {
	this.debug=debug;
    }

    /** This is an adapter object that provides callbacks into the
     *  application.
     *  For tomcat, it will be a RequestInterceptor.
     * 	See the top level documentation
     */
    public void addApplicationAdapter( Object adapter ) {
	if(requestInt==null)  initDefaultInterceptors();

	// In our case the adapter must be RequestInterceptor.
	if ( adapter instanceof RequestInterceptor ) {
	    addRequestInterceptor( (RequestInterceptor)adapter);
	}
    }

    public void setApplication( Object app ) {
	application=app;
    }

    /** Keep a reference to the application in which we are embeded
     */
    public Object getApplication() {
	return application;
    }
    
    public void setWorkDir( String dir ) {
	workDir=dir;
    }
    
    // -------------------- Endpoints --------------------
    
    /** Add a web service on the specified address. You must add all the
     *  endpoints before calling start().
     */
    public void addEndpoint( int port, InetAddress addr , String hostname) {
	if(debug>0) log( "addConnector " + port + " " + addr +
			 " " + hostname );

	PoolTcpConnector sc=new PoolTcpConnector();
	sc.setServer( contextM );
	sc.setDebug( debug );
	sc.setAttribute( "vhost_port" , new Integer( port ) );
	if( addr != null ) sc.setAttribute( "vhost_address", addr );
	if( hostname != null ) sc.setAttribute( "vhost_name", hostname );
	
	sc.setTcpConnectionHandler( new HttpConnectionHandler());
	
	contextM.addServerConnector(  sc );
    }

    /** Add a secure web service.
     */
    public void addSecureEndpoint( int port, InetAddress addr, String hostname,
				    String keyFile, String keyPass )
    {
	if(debug>0) log( "addSecureConnector " + port + " " + addr + " " +
			 hostname );

	PoolTcpConnector sc=new PoolTcpConnector();
	sc.setServer( contextM );
	sc.setAttribute( "vhost_port" , new Integer( port ) );
	if( addr != null ) sc.setAttribute( "vhost_address", addr );
	if( hostname != null ) sc.setAttribute( "vhost_name", hostname );
	
	sc.setTcpConnectionHandler( new HttpConnectionHandler());
	// XXX add the secure socket
	
	contextM.addServerConnector(  sc );
    }

    // -------------------- Context add/remove --------------------
    
    /** Add and init a context
     */
    public ServletContext addContext( String ctxPath, URL docRoot ) {
	if(debug>0) log( "add context \"" + ctxPath + "\" " + docRoot );
	if( contextM == null )
	    initContextManager();
	
	// tomcat supports only file-based contexts
	if( ! "file".equals( docRoot.getProtocol()) ) {
	    log( "addContext() invalid docRoot: " + docRoot );
	    throw new RuntimeException("Invalid docRoot " + docRoot );
	}

	try {
	    Context ctx=new Context();
	    ctx.setDebug( debug );
	    ctx.setContextManager( contextM );
	    ctx.setPath( ctxPath );
	    // XXX if virtual host set it.
	    ctx.setDocBase( docRoot.getFile());
	    contextM.addContext( ctx );
	    if( facadeM == null ) facadeM=ctx.getFacadeManager();
	    return ctx.getFacade();
	} catch( Exception ex ) {
	    ex.printStackTrace();
	}
	return null;
    }

    /** Remove a context
     */
    public void removeContext( ServletContext sctx ) {
	if(debug>-1) log( "remove context " + sctx );
	try {
	    if( facadeM==null ) {
		System.out.println("XXX ERROR: no facade manager");
		return;
	    }
	    Context ctx=facadeM.getRealContext( sctx );
	    contextM.initContext( ctx );
	} catch( Exception ex ) {
	    ex.printStackTrace();
	}
	// XXX todo
	// XXX Make sure we remove the HttpSecurityHandler:
	// 	HttpSecurityHandler.removeInstance(ctx);	
    }


    /** The application may want to add an application-specific path
	to the context.
    */
    public void addClassPath( ServletContext ctx, String cpath ) {
	if(debug>-1) log( "addClassPath " + ctx.getRealPath("") + " " +
			  cpath );
	// XXX This functionality can be achieved by setting it in the parent
	// class loader ( i.e. the loader that is used to load tomcat ).

	// It shouldn't be needed if the web app is self-contained,
    }

    /** Find the context mounted at /cpath.
	Right now virtual hosts are not supported in
	embeded tomcat.
    */
    public ServletContext getServletContext( String host,
					     String cpath )
    {
	// We don't support virtual hosts in embeded tomcat
	// ( it's not difficult, but can be done later )
	Context ctx=contextM.getContext( cpath );
	if( ctx==null ) return null;
	return ctx.getFacade();
    }

    /** This will make the context available.
     */
    public void initContext( ServletContext sctx ) {
	try {
	    if( facadeM==null ) {
		System.out.println("XXX ERROR: no facade manager");
		return;
	    }
	    Context ctx=facadeM.getRealContext( sctx );
	    contextM.initContext( ctx );
	} catch( Exception ex ) {
	    ex.printStackTrace();
	}
    }

    public void destroyContext( ServletContext ctx ) {

    }

    // -------------------- Start/stop
    
    public void start() {
	try {
	    contextM.start();
	} catch( Exception ex ) {
	    ex.printStackTrace();
	}
	if(debug>0) log( "Started" );
    }

    public void stop() {
	// XXX not implemented
    }
    
    // -------------------- Private methods
    public void addRequestInterceptor( RequestInterceptor ri ) {
	if( requestInt == null ) requestInt=new Vector();
	requestInt.addElement( ri );
	if( ri instanceof BaseInterceptor )
	    ((BaseInterceptor)ri).setDebug( debug );
    }
    public void addContextInterceptor( ContextInterceptor ci ) {
	if( contextInt == null ) contextInt=new Vector();
	contextInt.addElement( ci );
	if( ci instanceof BaseInterceptor )
	    ((BaseInterceptor)ci).setDebug( debug );
    }

    private void initContextManager() {
	if(requestInt==null)  initDefaultInterceptors();
	contextM=new ContextManager();
	debug=0;
	contextM.setDebug( debug );
	
	for( int i=0; i< contextInt.size() ; i++ ) {
	    contextM.addContextInterceptor( (ContextInterceptor)
					    contextInt.elementAt( i ) );
	}

	for( int i=0; i< requestInt.size() ; i++ ) {
	    contextM.addRequestInterceptor( (RequestInterceptor)
					    requestInt.elementAt( i ) );
	}

	contextM.setWorkDir( workDir );

	try {
	    contextM.init();
	} catch( Exception ex ) {
	    ex.printStackTrace();
	}
	if(debug>0) log( "ContextManager initialized" );
    }
    
    private void initDefaultInterceptors() {
	// Explicitely set up all the interceptors we need.
	// The order is important ( like in apache hooks, it's a chain !)
	
	// no AutoSetup !
	
	// set workdir, engine header, auth Servlet, error servlet, loader
	PolicyInterceptor polI=new PolicyInterceptor();
	addContextInterceptor( polI );

	LoaderInterceptor loadI=new LoaderInterceptor();
	addContextInterceptor( loadI );

	DefaultCMSetter defaultCMI=new DefaultCMSetter();
	addContextInterceptor( defaultCMI );

	WebXmlReader webXmlI=new WebXmlReader();
	webXmlI.setValidate( false );
	addContextInterceptor( webXmlI );
	
	LoadOnStartupInterceptor loadOnSI=new LoadOnStartupInterceptor();
	addContextInterceptor( loadOnSI );

	// Debug
	// 	LogEvents logEventsI=new LogEvents();
	// 	addRequestInterceptor( logEventsI );

	SessionInterceptor sessI=new SessionInterceptor();
	addRequestInterceptor( sessI );

	SimpleMapper1 mapI=new SimpleMapper1();
	//	mapI.setDebug(10);
	addRequestInterceptor( mapI );
	
	addRequestInterceptor( new StandardSessionInterceptor());
	
	// access control ( find if a resource have constraints )
	AccessInterceptor accessI=new AccessInterceptor();
	addRequestInterceptor( accessI );

	// set context class loader
	Jdk12Interceptor jdk12I=new Jdk12Interceptor();
	addRequestInterceptor( jdk12I );
    }
    

    // -------------------- Utils --------------------
    private void log( String s ) {
	System.out.println("WebAdapter: " + s );
    }

    /** Sample - you can use it to tomcat
     */
    public static void main( String args[] ) {
	try {
	    EmbededTomcat tc=new EmbededTomcat();
	    tc.setWorkDir( "/home/costin/src/jakarta/build/tomcat/work");
	    ServletContext sctx;
	    sctx=tc.addContext("", new URL
		( "file:/home/costin/src/jakarta/build/tomcat/webapps/ROOT"));
	    tc.initContext( sctx );
	    sctx=tc.addContext("/examples", new URL
		("file:/home/costin/src/jakarta/build/tomcat/webapps/examples"));
	    tc.initContext( sctx );
	    tc.addEndpoint( 8080, null, null);
	    tc.start();
	} catch (Throwable t ) {
	    t.printStackTrace();
	}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8578.java