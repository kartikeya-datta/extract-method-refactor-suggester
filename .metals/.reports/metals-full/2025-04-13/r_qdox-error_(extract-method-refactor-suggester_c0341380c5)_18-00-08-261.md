error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9072.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9072.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,48]

error in qdox parser
file content:
```java
offset: 48
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9072.java
text:
```scala
"org.apache.tomcat.modules.config.WorkDirSetup",@@

package org.apache.tomcat.startup;

import java.net.*;
import java.io.*;

import org.apache.tomcat.core.*;
import org.apache.tomcat.request.*;
import org.apache.tomcat.modules.server.*;
import org.apache.tomcat.modules.session.*;
import org.apache.tomcat.context.*;
import org.apache.tomcat.util.log.*;
import java.security.*;
import java.util.*;

/**
 *
 *  Wrapper around ContextManager. Use this class to embed tomcat in your
 *  application if you want to use "API"-based configuration ( instead
 *  or in addition to server.xml ).
 *
 *  The order is important:
 * 
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
public class EmbededTomcat { 
    // the "real" server
    protected ContextManager contextM = new ContextManager();

    // your application
    protected Object application;

    // null == not set up
    protected Vector requestInt=null;
    protected Vector connectors=new Vector();

    // configurable properties
    protected int debug=0;
    
    public EmbededTomcat() {
    }

    // -------------------- Properties - set before start

    public ContextManager getContextManager() {
	return contextM;
    }
    
    /** Set debugging - must be called before anything else
     */
    public void setDebug( int debug ) {
	this.debug=debug;
	contextM.setDebug( debug );
    }


    // -------------------- Application Modules --------------------

    /** This is an adapter object that provides callbacks into the
     *  application.
     */
    public void addApplicationAdapter( BaseInterceptor adapter )
	throws TomcatException
    {
	if(requestInt==null)  initDefaultInterceptors();
	addInterceptor(adapter);
    }

    /** Keep a reference to the application in which we are embeded
     */
    public void setApplication( Object app ) {
	application=app;
    }

    /** Keep a reference to the application in which we are embeded
     */
    public Object getApplication() {
	return application;
    }

    // -------------------- Helpers for http connectors --------------------
    
    /** Add a HTTP listener.
     */
    public void addEndpoint( int port, InetAddress addr , String hostname)
	throws TomcatException
    {
	if(debug>0) log( "addConnector " + port + " " + addr +
			 " " + hostname );

	Http10Interceptor sc=new Http10Interceptor();
	sc.setPort( port ) ;
	if( addr != null ) sc.setAddress( addr );
	if( hostname != null ) sc.setHostName( hostname );
	
	contextM.addInterceptor(  sc );
    }

    /** Add AJP12 listener.
     */
    public void addAjpEndpoint( int port, InetAddress addr , String hostname)
	throws TomcatException
    {
	if(debug>0) log( "addAjp12Connector " + port + " " + addr +
			 " " + hostname );

	Ajp12Interceptor sc=new Ajp12Interceptor();
	sc.setPort( port ) ;
	if( addr != null ) sc.setAddress( addr );
	if( hostname != null ) sc.setHostName( hostname );
	
	contextM.addInterceptor(  sc );
    }

    /** Add a secure HTTP listener.
     */
    public void addSecureEndpoint( int port, InetAddress addr, String hostname,
				    String keyFile, String keyPass )
	throws TomcatException
    {
	if(debug>0) log( "addSecureConnector " + port + " " + addr + " " +
			 hostname );
	
	Http10Interceptor sc=new Http10Interceptor();
	sc.setPort( port ) ;
	if( addr != null ) sc.setAddress(  addr );
	if( hostname != null ) sc.setHostName( hostname );
	
	sc.setSocketFactory("org.apache.tomcat.util.net.SSLSocketFactory");
	sc.setSecure(true);

	contextM.addInterceptor(  sc );
    }

    // -------------------- Context add/remove --------------------

    boolean initialized=false;
    
    /** Add and init a context. Must be called after all modules are added.
     */
    public Context addContext(  String ctxPath, URL docRoot, String hosts[] )
	throws TomcatException
    {
	if(debug>0) log( "add context \"" + hosts[0] + ":" + ctxPath + "\" " +
			 docRoot );
	if( ! initialized ) {
	    initContextManager();
	}
	
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
	    ctx.setDocBase( docRoot.getFile());
	    if( hosts!=null && hosts.length>0 ) {
		ctx.setHost( hosts[0] );
		for( int i=1; i>hosts.length; i++) {
		    ctx.addHostAlias( hosts[i]);
		}
	    }

	    contextM.addContext( ctx );
	    return ctx;
	} catch( Exception ex ) {
	    log("exception adding context " + ctxPath + "/" + docRoot, ex);
	}
	return null;
    }

    /** Find the context mounted at /cpath for a virtual host.
     */
    public Context getContext( String host, String cpath )
    {
	// We don't support virtual hosts in embeded tomcat
	// ( it's not difficult, but can be done later )
	Enumeration ctxE=contextM.getContexts();
	while( ctxE.hasMoreElements() ) {
	    Context ctx=(Context)ctxE.nextElement();
	    // XXX check host too !
	    if( ctx.getPath().equals( cpath )) {
		// find if the host matches
		if( ctx.getHost()==null ) 
		    return ctx;
		if( host==null )
		    return ctx;
		if( ctx.getHost().equals( host ))
		    return ctx;
		Enumeration aliases=ctx.getHostAliases();
		while( aliases.hasMoreElements()){
		    if( host.equals( (String)aliases.nextElement()))
			return ctx;
		}
	    }
	}
	return null;
    }

    public void start() throws TomcatException {
	contextM.start();
    }

    public void stop() throws TomcatException {
	contextM.stop();
    }

    // -------------------- Private methods
    public void addInterceptor( BaseInterceptor ri ) {
	if( requestInt == null ) requestInt=new Vector();
	requestInt.addElement( ri );
	ri.setDebug( debug );
    }

    protected void initContextManager()
	throws TomcatException 
    {
	if(requestInt==null)  initDefaultInterceptors();
	
	for( int i=0; i< requestInt.size() ; i++ ) {
	    contextM.addInterceptor( (BaseInterceptor)
				     requestInt.elementAt( i ) );
	}

	try {
	    contextM.init();
	} catch( Exception ex ) {
	    log("exception initializing ContextManager", ex);
	}
	if(debug>0) log( "ContextManager initialized" );
	initialized=true;
    }

    // no AutoSetup !

    protected String moduleSet1[] = {
	"org.apache.tomcat.context.DefaultCMSetter",
	"org.apache.tomcat.facade.WebXmlReader",
	"org.apache.tomcat.context.PolicyInterceptor",
	"org.apache.tomcat.context.LoaderInterceptor12",
	"org.apache.tomcat.context.ErrorHandler",
	"org.apache.tomcat.context.WorkDirInterceptor",
	"org.apache.tomcat.modules.session.SessionId",
	"org.apache.tomcat.request.SimpleMapper1",
	"org.apache.tomcat.request.InvokerInterceptor",
	"org.apache.tomcat.facade.JspInterceptor",
	"org.apache.tomcat.request.StaticInterceptor",
	"org.apache.tomcat.modules.session.SimpleSessionStore",
	"org.apache.tomcat.facade.LoadOnStartupInterceptor",
	"org.apache.tomcat.facade.Servlet22Interceptor",
	"org.apache.tomcat.request.AccessInterceptor",
	"org.apache.tomcat.request.CredentialsInterceptor",
	"org.apache.tomcat.request.Jdk12Interceptor"
    };
    
    protected String moduleSet2[] = {
	"org.apache.tomcat.modules.config.ServerXmlInterceptor",
    };
    
    protected void initDefaultInterceptors() {
	addModules( moduleSet1 );
    }

    protected void addModules(String set[] ) {
	for( int i=0; i<set.length; i++ ) {
	    addInterceptor( createModule( set[i] ));
	}
    }

    // -------------------- Utils --------------------
    public void log( String s ) {
	contextM.log( s );
    }
    public void log( String s, Throwable t ) {
	contextM.log( s, t );
    }

    private BaseInterceptor createModule( String classN ) {
	try {
	    Class c=Class.forName( classN );
	    return (BaseInterceptor)c.newInstance();
	} catch( Exception ex ) {
	    ex.printStackTrace();
	    return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9072.java