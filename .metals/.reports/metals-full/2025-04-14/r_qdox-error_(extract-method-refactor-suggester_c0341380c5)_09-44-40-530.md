error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8683.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8683.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8683.java
text:
```scala
r@@eqA.scheme().setString( "https" );

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


package org.apache.tomcat.service.http;

import org.apache.tomcat.service.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.tomcat.core.*;
import org.apache.tomcat.util.*;
import org.apache.tomcat.util.net.*;
import org.apache.tomcat.util.net.ServerSocketFactory;
import org.apache.tomcat.util.log.*;

/** Standalone http.
 *
 *  Connector properties:
 *  - secure - will load a SSL socket factory and act as https server
 *
 *  Properties passed to the net layer:
 *  - timeout
 *  - backlog
 *  - address
 *  - port
 * Thread pool properties:
 *  - minSpareThreads
 *  - maxSpareThreads
 *  - maxThreads
 *  - poolOn
 * Properties for HTTPS:
 *  - keystore - certificates - default to ~/.keystore
 *  - keypass - password
 *  - clientauth - true if the server should authenticate the client using certs
 */
public class HttpInterceptor extends BaseInterceptor  implements  TcpConnectionHandler {
    boolean enabled=true;

    // Tcp stuff
    PoolTcpEndpoint ep;
    private ServerSocketFactory socketFactory;

    // properties
    int port;
    InetAddress address;
    String keystore;
    String keypass;
    
    
    boolean secure=false;
    ContextManager contextM;
    
    public HttpInterceptor() {
	super();
	ep=new PoolTcpEndpoint();
    }

    // -------------------- Properties --------------------

    public void setPort( int port ) {
    	this.port=port;
    }

    
    public int getPort() {
    	return port;
    }

    public InetAddress getAddress() {
	return address;
    }

    

    // -------------------- Tomcat plug in --------------------

    // Tcp Endoint start/stop
    public void engineInit(ContextManager cm) throws TomcatException {
	this.contextM=cm;
	ep.setConnectionHandler( this );
	try {
	    ep.setPort( port );
	    if( socketFactory!=null )
		ep.setServerSocketFactory( socketFactory );
	    ep.startEndpoint();
	    log( "Starting on " + port );
	} catch( Exception ex ) {
	    throw new TomcatException( ex );
	}
    }

    public void engineShutdown(ContextManager cm) throws TomcatException {
	try {
	    ep.stopEndpoint();
	} catch( Exception ex ) {
	    throw new TomcatException( ex );
	}
    }



    // -------------------- Attributes --------------------
    public void setKeystore( String k ) {
	keystore=k;
    }
    public String getKeystore() {
	return keystore;
    }
    
    public void setSecure( boolean b ) {
	enabled=false;
	secure=false;
	if( b == true ) {
	    if( keystore!=null && ! new File( keystore ).exists() ) {
		log("Can't find keystore " + keystore );
		return;
	    }
	    try {
		Class c1=Class.forName( "javax.net.ssl.SSLServerSocketFactory");
	    } catch( Exception ex ) {
		log( "Can't find JSSE, HTTPS will not be enabled");
		return;
	    }
	    try {
		Class chC=Class.forName( "org.apache.tomcat.util.net.SSLSocketFactory" );
		socketFactory=(ServerSocketFactory)chC.newInstance();
		if( keystore!=null)
		    socketFactory.setAttribute( "keystore", keystore);
	    } catch(Exception ex ) {
		log( "Error loading SSL socket factory ", ex);
		return;
	    }
	}
    	secure=b;
	enabled=true;
    }

    // -------------------- Handler implementation --------------------
    public void setServer(Object o) {
	//XXX deprecated
    }
    public void setAttribute(String s, Object o) {
	//XXX deprecated
    }
    
    public Object[] init() {
	Object thData[]=new Object[3];
	HttpRequestAdapter reqA=new HttpRequestAdapter();
	HttpResponseAdapter resA=new HttpResponseAdapter();
	contextM.initRequest( reqA, resA );
	thData[0]=reqA;
	thData[1]=resA;
	thData[2]=null;
	return  thData;
    }

    public void processConnection(TcpConnection connection, Object thData[]) {
	Socket socket=null;
	HttpRequestAdapter reqA=null;
	HttpResponseAdapter resA=null;

	try {
	    // XXX - Add workarounds for the fact that the underlying
	    // serverSocket.accept() call can now time out.  This whole
	    // architecture needs some serious review.
	    if (connection == null)
		return;

	    socket=connection.getSocket();
	    if (socket == null)
		return;

	    InputStream in=socket.getInputStream();
	    OutputStream out=socket.getOutputStream();
	    if( thData != null ) {
		reqA=(HttpRequestAdapter)thData[0];
		resA=(HttpResponseAdapter)thData[1];
		if( reqA!=null ) reqA.recycle();
		if( resA!=null ) resA.recycle();
	    }

	    if( reqA==null || resA==null ) {	
		log("No thread data ??");
		reqA=new HttpRequestAdapter();
		resA=new HttpResponseAdapter();
		contextM.initRequest( reqA, resA );
	    }
	    
	    reqA.setSocket( socket );
	    resA.setOutputStream( out );

	    reqA.readNextRequest(resA);
	    if( secure ) {
		reqA.setScheme( "https" );
	    }
	    
	    contextM.service( reqA, resA );

	    try {
               InputStream is = socket.getInputStream();
               int available = is.available ();
	       
               // XXX on JDK 1.3 just socket.shutdownInput () which
               // was added just to deal with such issues.

               // skip any unread (bogus) bytes
               if (available > 1) {
                   is.skip (available);
               }
	    }catch(NullPointerException npe) {
		// do nothing - we are just cleaning up, this is
		// a workaround for Netscape \n\r in POST - it is supposed
		// to be ignored
	    }
	}
	catch(java.net.SocketException e) {
	    // SocketExceptions are normal
	    log( "SocketException reading request, ignored", null, Logger.INFORMATION);
	    log( "SocketException reading request:", e, Logger.DEBUG);
	}
	catch (java.io.IOException e) {
	    // IOExceptions are normal 
	    log( "IOException reading request, ignored", null, Logger.INFORMATION);
	    log( "IOException reading request:", e, Logger.DEBUG);
	}
	// Future developers: if you discover any other
	// rare-but-nonfatal exceptions, catch them here, and log as
	// above.
	catch (Throwable e) {
	    // any other exception or error is odd. Here we log it
	    // with "ERROR" level, so it will show up even on
	    // less-than-verbose logs.
	    log( "Error reading request, ignored", e, Logger.ERROR);
	} 
	finally {
	    // recycle kernel sockets ASAP
	    try { if (socket != null) socket.close (); }
	    catch (IOException e) { /* ignore */ }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8683.java