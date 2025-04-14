error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9016.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9016.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[63,1]

error in qdox parser
file content:
```java
offset: 2807
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9016.java
text:
```scala
public final class PoolTcpConnector extends BaseInterceptor implements LogAware {

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


p@@ackage org.apache.tomcat.service;

import org.apache.tomcat.util.*;
import org.apache.tomcat.core.*;
import org.apache.tomcat.net.*;
import org.apache.tomcat.logging.*;
import java.io.*;
import java.net.*;
import java.util.*;

//import org.apache.tomcat.server.HttpServer;

/* Similar with MPM module in Apache2.0. Handles all the details related with
   "tcp server" functionality - thread management, accept policy, etc.
   It should do nothing more - as soon as it get a socket ( and all socket options
   are set, etc), it just handle the stream to ConnectionHandler.processConnection. (costin)
*/



/**
 * Connector for a TCP-based connector using the API in tomcat.service.
 * You need to set a "connection.handler" property with the class name of
 * the TCP connection handler
 *
 * @author costin@eng.sun.com
 * @author Gal Shachor [shachor@il.ibm.com]
 */
public final class PoolTcpConnector extends BaseInterceptor implements ServerConnector, LogAware {
    // Attributes we accept ( to support the old model of
    // configuration, will be deprecated )
    public static final String VHOST_PORT="vhost_port";
    public static final String VHOST_NAME="vhost_name";
    public static final String VHOST_ADDRESS="vhost_address";
    public static final String SOCKET_FACTORY="socketFactory";


    public static final String INET = "inet";
    public static final String PORT = "port";
    public static final String HANDLER = "handler";

    /*
     * Threading and mod_mpm style properties.
     */
    public static final String THREAD_POOL = "thread_pool";
    public static final String MAX_THREADS = "max_threads";
    public static final String MAX_SPARE_THREADS = "max_spare_threads";
    public static final String MIN_SPARE_THREADS = "min_spare_threads";
    public static final String BACKLOG = "backlog";

    // XXX define ConnectorException
    // XXX replace strings with sm.get...
    // XXX replace static strings with constants
    String handlerClassName;
    PoolTcpEndpoint ep;
    TcpConnectionHandler con;
    
    Hashtable attributes = new Hashtable();
    Object cm;

    private String vhost;
    private InetAddress address;
    private int port;

    private int backlog = -1;
    private boolean usePools = true;
    private int maxThreads = -1;
    private int maxSpareThreads = -1;
    private int minSpareThreads = -1;

    private ServerSocketFactory socketFactory;
    private ServerSocket serverSocket;

    boolean running = true;
    int debug=0;
    
    public PoolTcpConnector() {
    	ep = new PoolTcpEndpoint();
    }

    // -------------------- Start/stop --------------------

    /** Called when the ContextManger is started
     */
    public void engineInit(ContextManager cm) throws TomcatException {
	this.cm=cm;
	try {
	    start();
	} catch( Exception ex ) {
	    throw new TomcatException( ex );
	}
    }

    public void engineShutdown(ContextManager cm) throws TomcatException {
	try {
	    stop();
	} catch( Exception ex ) {
	    throw new TomcatException( ex );
	}
    }

    
    public void start() throws Exception {
    	if(con==null)
    	    throw new Exception( "Invalid ConnectionHandler");

	con.setServer( cm );
	con.setAttribute("context.manager",cm ); // old mechanism

	// Pass properties to the handler
	Enumeration attE=attributes.keys();
	while( attE.hasMoreElements() ) {
	    String key=(String)attE.nextElement();
	    Object v=attributes.get( key );
	    con.setAttribute( key, v );
	}

    	ep.setPort(port);
	ep.setAddress( address );
    	ep.setPoolOn(usePools);
    	if(backlog > 0) {
    	    ep.setBacklog(backlog);
    	}
    	if(maxThreads > 0) {
    	    ep.setMaxThreads(maxThreads);
    	}
    	if(maxSpareThreads > 0) {
    	    ep.setMaxSpareThreads(maxSpareThreads);
    	}
    	if(minSpareThreads > 0) {
    	    ep.setMinSpareThreads(minSpareThreads);
    	}

	if(socketFactory != null) {
	    ep.setServerSocketFactory( socketFactory );
	    // Pass properties to the socket factory
	    attE=attributes.keys();
	    while( attE.hasMoreElements() ) {
		String key=(String)attE.nextElement();
		Object v=attributes.get( key );
		socketFactory.setAttribute( key, v );
	    }
	}
	ep.setConnectionHandler( con );
	ep.startEndpoint();

	String classN=con.getClass().getName();
	int lidot=classN.lastIndexOf( "." );
	if( lidot >0 ) classN=classN.substring( lidot + 1 );

	loghelper.log("Starting " + classN + " on " + port);
    }

    public void stop() throws Exception {
    	ep.stopEndpoint();
    }

    // -------------------- Tcp-server specific methods --------------------
    public void setTcpConnectionHandler( TcpConnectionHandler handler) {
    	this.con=handler;
    }

    public TcpConnectionHandler getTcpConnectionHandler() {
	    return con;
    }

    // -------------------- Bean-setters for TcpConnector --------------------
    public void setServer( Object ctx ) {
	this.cm=ctx;
	if (cm instanceof LogAware) {
	    loghelper.setProxy(((LogAware)cm).getLoggerHelper());
	}
    }

    public void setDebug( int i ) {
	debug=i;
    }
    
    public void setPort( int port ) {
    	this.port=port;
    }

    public void setPort(  String portS ) {
	this.port=string2Int( portS );
    }

    public int getPort() {
    	return port;
    }

    public InetAddress getAddress() {
	return address;
    }

    /** Generic configure system - this allows Connector
     * 	configuration using name/value.
     *
     *  The "prefered" configuration is to call setters,
     * 	and tomcat using server.xml will do that, but
     *	this allows (minimal) integration with simpler
     *	systems.
     *
     *  Only a minimal number of properties can be set
     *  this way. This mechanism may be deprecated
     *  after we improve the startup system.
     *
     *  Supported attributes:
     *  "vhost_port" - port ( will act as a virtual host )
     *  "vhost_name" - virtual host name 
     *  "vhost_address" - virtual host binding address
     *  "socketFactory" - socket factory - for ssl.
     *  XXX add the others
     * 
     *  Note that the attributes are passed to the Endpoint.
     */
    public void setAttribute( String prop, Object value) {
	if( debug > 0 ) 
	    loghelper.log( "setAttribute( " + prop + " , " + value + ")");

	try {
	if( value instanceof String ) {
	    String valueS=(String)value;
	    
	    if( PORT.equals(prop) ) {
		setPort( valueS );
	    } else if(HANDLER.equals(prop)) {
		con=string2ConnectionHandler( valueS );
	    } else if(THREAD_POOL.equals(prop)) {
		usePools = ! valueS.equalsIgnoreCase("off");
	    } else if(INET.equals(prop)) {
		address=string2Inet( valueS );
	    } else if( MAX_THREADS.equals(prop)) {
		maxThreads = string2Int(valueS);
	    } else if( MAX_SPARE_THREADS.equals(prop)) {
		maxSpareThreads = string2Int(valueS);
	    } else if( MIN_SPARE_THREADS.equals(prop)) {
		minSpareThreads = string2Int(valueS);
	    } else if(VHOST_NAME.equals(prop) ) {
		vhost=valueS;
	    } else if( BACKLOG.equals(prop)) {
		backlog = string2Int(valueS);
	    } else if(VHOST_PORT.equals(prop) ) {
		port= string2Int( valueS );
	    } else if(SOCKET_FACTORY.equals(prop)) {
		socketFactory= string2SocketFactory( valueS );
	    } else if(VHOST_ADDRESS.equals(prop)) {
		address= string2Inet(valueS);
	    } else {
		if( valueS!=null)
		    attributes.put( prop, valueS );
	    }
	} else {
	    // Objects - avoids String-based "serialization" 
	    if(VHOST_PORT.equals(prop) ) {
		port=((Integer)value).intValue();
	    } else if(VHOST_ADDRESS.equals(prop)) {
		address=(InetAddress)value;
	    } else if(SOCKET_FACTORY.equals(prop)) {
		socketFactory=(ServerSocketFactory)value;
	    } else {
		if( value!=null)
		    attributes.put( prop, value );
	    }
	}
	}
	catch (Exception e) {
	    loghelper.log("setAttribute: " +prop + "=" + value, e, Logger.ERROR);
	}
    }

    public void setProperty( String prop, String value) {
	setAttribute( prop, value );
    }

    // ----- Logging -----
    private Logger.Helper loghelper = new Logger.Helper("tc_log", "PoolTcpConnector");
        
    /**
     * Set a logger explicitly. Note that setLogger(null) will not
     * necessarily redirect log output to System.out; if there is a
     * "tc_log" logger it will default back to using it. 
     **/
    public void setLogger( Logger logger ) {
	loghelper.setLogger(logger);
    }

    public Logger.Helper getLoggerHelper() {
	return loghelper;
    }
    
    // -------------------- Implementation methods --------------------


    // now they just throw exceptions, which are caught and logged by
    // the caller

    private static TcpConnectionHandler string2ConnectionHandler( String val) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
	Class chC=Class.forName( val );
	return (TcpConnectionHandler)chC.newInstance();
    }

    private static ServerSocketFactory string2SocketFactory( String val) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
	Class chC=Class.forName( val );
	return (ServerSocketFactory)chC.newInstance();
    }

    private static InetAddress string2Inet( String val) throws UnknownHostException {
	return InetAddress.getByName( val );
    }
    
    private static int string2Int( String val) {
	return Integer.parseInt(val);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9016.java