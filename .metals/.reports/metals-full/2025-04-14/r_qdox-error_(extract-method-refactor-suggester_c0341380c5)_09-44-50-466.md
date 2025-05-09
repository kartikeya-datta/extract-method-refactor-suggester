error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3398.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3398.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[68,1]

error in qdox parser
file content:
```java
offset: 2847
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3398.java
text:
```scala
class AJP12RequestAdapter extends Request {

/*
 *
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


/*
  Based on Ajp11ConnectionHandler and Ajp12 implementation of JServ
*/

p@@ackage org.apache.tomcat.service.connector;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.tomcat.core.*;
import org.apache.tomcat.util.*;
import org.apache.tomcat.logging.*;
import org.apache.tomcat.service.http.*;
import org.apache.tomcat.service.http.HttpResponseAdapter;
import org.apache.tomcat.service.http.HttpRequestAdapter;
import org.apache.tomcat.service.*;
import javax.servlet.*;
import javax.servlet.http.*;

/* Deprecated - must be rewriten to the connector model.
 */
public class Ajp12ConnectionHandler implements  TcpConnectionHandler {
    static StringManager sm = StringManager.getManager("org.apache.tomcat.resources");

    ContextManager contextM;

    Logger.Helper loghelper = new Logger.Helper("tc_log", this);
    
    public Ajp12ConnectionHandler() {
    }

    public Object[] init() {
	Object thData[]=new Object[2];
	AJP12RequestAdapter reqA=new AJP12RequestAdapter();
	AJP12ResponseAdapter resA=new AJP12ResponseAdapter();
	contextM.initRequest( reqA, resA );
	thData[0]=reqA;
	thData[1]=resA;

	return  thData;
    }

    public void setAttribute(String name, Object value ) {
	if("context.manager".equals(name) ) {
	    contextM=(ContextManager)value;
	}
    }

    public void setServer( Object contextM ) {
	this.contextM=(ContextManager )contextM;
    }

    public void processConnection(TcpConnection connection, Object[] thData) {
        try {
	    // XXX - Add workarounds for the fact that the underlying
	    // serverSocket.accept() call can now time out.  This whole
	    // architecture needs some serious review.
	    if (connection == null)
		return;
	    Socket socket=connection.getSocket();
	    if (socket == null)
		return;

	    socket.setSoLinger( true, 100);
	    //	    socket.setSoTimeout( 1000); // or what ?

	    AJP12RequestAdapter reqA=null;
	    AJP12ResponseAdapter resA=null;
	    
	    if( thData != null ) {
		reqA=(AJP12RequestAdapter)thData[0];
		resA=(AJP12ResponseAdapter)thData[1];
		if( reqA!=null ) reqA.recycle();
		if( resA!=null ) resA.recycle();
	    }

	    if( reqA==null || resA==null ) {
		reqA = new AJP12RequestAdapter();
		resA=new AJP12ResponseAdapter();
		contextM.initRequest( reqA, resA );
	    }

	    InputStream in=socket.getInputStream();
	    OutputStream out=socket.getOutputStream();

	    reqA.setSocket( socket);
	    resA.setOutputStream(out);

	    reqA.readNextRequest();
	    if( reqA.isPing )
		return;
	    if( reqA.shutdown )
		return;
	    if (resA.getStatus() >= 400) {
		resA.finish();
		socket.close();
		return;
	    }

	    contextM.service( reqA, resA );
	    //resA.finish(); // is part of contextM !
	    socket.close();
	} catch (Exception e) {
	    log("HANDLER THREAD PROBLEM", e);
	}
    }

    void log(String s, Throwable e) {
	loghelper.log(s,e);
    }
    
}

class AJP12RequestAdapter extends RequestImpl {
    static StringManager sm = StringManager.getManager("org.apache.tomcat.resources");
    Socket socket;
    InputStream sin;
    BufferedInputStream ajpin;
    boolean shutdown=false;
    boolean isPing=false;
    boolean doLog;

    // Debug only - use only to debug this component
    void d( String s ) {
	System.out.print("Ajp12RequestAdapter: ");
	System.out.println( s );
    }
    
    public int doRead() throws IOException {
	return ajpin.read();
    }

    public  int doRead( byte b[], int off, int len ) throws IOException {
	return ajpin.read( b,off,len);
    }

    public AJP12RequestAdapter() {
    }

    public void setContextManager(ContextManager cm ) {
	contextM=cm;
	doLog=contextM.getDebug() > 10;
    }

    public void setSocket( Socket s ) throws IOException {
	this.socket = s;
	sin = s.getInputStream();
	ajpin = new BufferedInputStream(sin);
    }
    
    public AJP12RequestAdapter(ContextManager cm, Socket s) throws IOException {
	this.socket = s;
	this.contextM=cm;
	sin = s.getInputStream();
	ajpin = new BufferedInputStream(sin);
	doLog=contextM.getDebug() > 10;
    }

    protected void readNextRequest() throws IOException {
	String dummy,token1,token2;
	int marker;
	int signal;
//      Hashtable env_vars=new Hashtable();

	try {
	    boolean more=true;
            while (more) {
		marker = ajpin.read();
		switch (marker) {
		case 0:       //NOP marker useful for testing if stream is OK
		    break;
		    
		case 1: //beginning of request
		    method = readString(ajpin, null);              //Method
		    
		    contextPath = readString(ajpin, null);               //Zone
		    // GS, the following commented line causes the Apache + Jserv + Tomcat
		    // combination to hang with a 404!!!
		    // if("ROOT".equals( contextPath ) ) contextPath="";
		    if("ROOT".equalsIgnoreCase( contextPath ) ) contextPath=null;
		    if( doLog ) d("AJP: CP=" + contextPath);
		    
		    if( contextPath!= null )
			context=contextM.getContext( contextPath );
		    if( doLog ) d("AJP: context=" + context );
		    
		    servletName = readString(ajpin, null);         //Servlet
		    if( doLog ) d("AJP: servlet=" + servletName );
		    
		    serverName = readString(ajpin, null);            //Server hostname
		    if( doLog ) d("AJP: serverName=" + serverName );
		    
		    dummy = readString(ajpin, null);               //Apache document root
		    
		    pathInfo = readString(ajpin, null);               //Apache parsed path-info
		    if( doLog ) d("AJP: PI=" + pathInfo );
		    
		    // XXX Bug in mod_jserv !!!!!
		    pathTranslated = readString(ajpin, null);               //Apache parsed path-translated
		    if( doLog ) d("AJP: PT=" + pathTranslated );
		    
		    queryString = readString(ajpin, null);         //query string
		    if( doLog ) d("AJP: QS=" + queryString );
		    
		    remoteAddr = readString(ajpin, "");            //remote address
		    if( doLog ) d("AJP: RA=" + remoteAddr );
		    
		    remoteHost = readString(ajpin, "");            //remote host
		    if( doLog ) d("AJP: RH=" + remoteHost );
		    
		    remoteUser = readString(ajpin, null);                 //remote user
		    if( doLog ) d("AJP: RU=" + remoteUser);
		    
		    authType = readString(ajpin, null);                 //auth type
		    if( doLog ) d("AJP: AT=" + authType);
		    
		    dummy = readString(ajpin, null);                 //remote port
		    
		    method = readString(ajpin, null);                //request method
		    if( doLog ) d("AJP: Meth=" + method );
		    
		    requestURI = readString(ajpin, "");             //request uri
		    if( doLog ) d("AJP: URI: " + requestURI + " CP:" + contextPath + " LP: " + lookupPath);

		    // XXX don't set lookup path - problems with URL rewriting.
		    // need to be fixed.
		    //		if(contextPath!=null && contextPath.length() >0 )
		    //		    lookupPath=requestURI.substring( contextPath.length() + 1 );
		    if( doLog ) d("AJP: URI: " + requestURI + " CP:" + contextPath + " LP: " + lookupPath);
		    
		    dummy = readString(ajpin, null);                   //script filename
		    //		System.out.println("AJP: Script filen=" + dummy);
		    
		    dummy = readString(ajpin, null);                   //script name
		    //		System.out.println("AJP: Script name=" + dummy);

		    serverName = readString(ajpin, "");                //server name
		    if( doLog ) d("AJP: serverName=" + serverName );
		    try {
			serverPort = Integer.parseInt(readString(ajpin, "80")); //server port
		    } catch (Exception any) {
			serverPort = 80;
		    }

		    dummy = readString(ajpin, "");                     //server protocol
		    //		System.out.println("AJP: Server proto=" + dummy);
		    dummy = readString(ajpin, "");                     //server signature
		    //		System.out.println("AJP: Server sign=" + dummy);
		    dummy = readString(ajpin, "");                     //server software
		    //		System.out.println("AJP: Server softw=" + dummy);
		    jvmRoute = readString(ajpin, "");                     //JSERV ROUTE
		    if(jvmRoute.length() == 0) {
			jvmRoute = null;
		    }
		    if( doLog ) d("AJP: Server jvmRoute=" + jvmRoute);


                    /**
                     * The two following lines are commented out because we don't
                     * want to depend on unreleased versions of the jserv module.
                     *                                            - costin
                     * The two following lines are uncommented because JServ 1.1 final
                     * is far behind.
                     * NB: you need a recent mod_jserv to use the latest protocol version.
                     * Theses env vars are simply ignored. (just here for compatibility)
                     *                                            - jluc
                     */
                     dummy = readString(ajpin, "");
                     dummy = readString(ajpin, "");
		    // XXX all dummy fields will be used after core is changed to make use
		    // of them!

		    break;


                    /**
                     * Marker = 5 will be used by mod_jserv to send environment vars
                     * as key+value (dynamically configurable).
                     * can be considered as "reserved", and safely ignored by other connectors.
                     * env_vars is (above in this  code) commented out for performance issues.
                     * so theses env vars are simply ignored. (just here for compatibility)
                     * but it is where mod_jserv would place SSL_* env vars (by exemple)
                     * See the new parameter for mod_jserv (version > 1.1):
                     * ApJServEnvVar localname remotename
                     *                                            - jluc
                     */
                case 5: // Environment vars
                    token1 = readString(ajpin, null);
                    token2 = readString(ajpin, "");
                    /*
                     * Env variables should go into the request attributes
                     * table. 
					 *
					 * Also, if one of the request attributes is HTTPS=on
                     * assume that there is an SSL connection.
					 */
                    attributes.put(token1, token2);
                    if(token1.equals("HTTPS") && token2.equals("on")) {
                        setScheme("https");
                    }
                    break;

		case 3: // Header
		    token1 = readString(ajpin, null);
		    token2 = readString(ajpin, "");
		    headers.putHeader(token1.toLowerCase(), token2);
		    break;

		case 254: // Signal
		    signal = ajpin.read();

		    if (signal == 0) { // PING implemented as signal
			try {
			    // close the socket connection after we send reply
			    socket.getOutputStream().write(0); // PING reply
			    sin.close();
			} catch (IOException ignored) {
			    contextM.log("Exception closing, ignored",  ignored);
			}
                        isPing = true;
                        return;
		    } else {
			try {
			    // close the socket connection before handling any signal
			    // but get the addresses first so they are not corrupted
			    InetAddress serverAddr = socket.getLocalAddress();
			    InetAddress clientAddr = socket.getInetAddress();
			    sin.close();
			    if ( (signal== 15) &&
				 isSameAddress(serverAddr, clientAddr) ) {
				// Shutdown - probably apache was stoped with apachectl stop
				contextM.stop();
				// same behavior as in past, because it seems that
				// stopping everything doesn't work - need to figure
				// out what happens with the threads ( XXX )
				System.exit(0);

				shutdown=true;
				return;
			    }
			} catch (Exception ignored) {
			    contextM.log("Ignored exception processing signal " +
			      signal, ignored);
			}
		    }
		    return;
		    
		case 4:
		case 255:
		    more=false;
		    break;
		    
		case -1:
		    throw new java.io.IOException("Stream closed prematurely");
		    
		    
		default:
		    throw new java.io.IOException("Stream broken");
		    
		    
		} // switch
            } // while
	} catch (IOException ioe) {
	    throw ioe;
        } catch (Exception e) {
	    contextM.log("Uncaught exception handling request", e);
        }
	
	// REQUEST_URI includes query string
	int indexQ=requestURI.indexOf("?");
	int rLen=requestURI.length();
	if ( (indexQ >-1) && ( indexQ  < rLen) ) {
	    if(doLog) d("Orig QS " + queryString );
	    queryString = requestURI.substring(indexQ + 1, requestURI.length());
	    if(doLog) d("New QS " + queryString );
	    requestURI = requestURI.substring(0, indexQ);
	} 
	
	if( doLog ) d("Request: " + requestURI );
	if( doLog ) d("Query: " + queryString );
	// System.out.println("ENV: " + env_vars );
	// 	System.out.println("HEADERS: " + headers_in );
	// 	System.out.println("PARAMETERS: " + parameters );
	

	//processCookies();

	contentLength = headers.getIntHeader("content-length");
	contentType = headers.getHeader("content-type");

	// XXX
	// detect for real whether or not we have more requests
	// coming

	// XXX
	// Support persistent connection in AJP21
	//moreRequests = false;
    }

    /**
     * Return <code>true</code> if the specified client and server addresses
     * are the same.  This method works around a bug in the IBM 1.1.8 JVM on
     * Linux, where the address bytes are returned reversed in some
     * circumstances.
     *
     * @param server The server's InetAddress
     * @param client The client's InetAddress
     */
    private boolean isSameAddress(InetAddress server, InetAddress client) {

	// Compare the byte array versions of the two addresses
	byte serverAddr[] = server.getAddress();
	byte clientAddr[] = client.getAddress();
	if (serverAddr.length != clientAddr.length)
	    return (false);
	boolean match = true;
	for (int i = 0; i < serverAddr.length; i++) {
	    if (serverAddr[i] != clientAddr[i]) {
		match = false;
		break;
	    }
	}
	if (match)
	    return (true);

	// Compare the reversed form of the two addresses
	for (int i = 0; i < serverAddr.length; i++) {
	    if (serverAddr[i] != clientAddr[(serverAddr.length-1)-i])
		return (false);
	}
	return (true);

    }

    
    public int readWord(InputStream in ) throws java.io.IOException {
        int b1 = in.read();
        if( b1 == -1)
            return -1;

        int b2 = in.read();
        if ( b2==-1)
            return -1;

        return ((int)((b1 << 8) | b2)) & 0xffff;
    }

    // UTF8 is a strict superset of ASCII.
    final static String CHARSET = "UTF8";

    public String readString(InputStream in, String def) throws java.io.IOException {
        int len = readWord(in);

        if( len == 0xffff)
            return def;

        if( len == -1)
            throw new java.io.IOException("Stream broken");

        byte[] b = new byte[len];
        int p = 0;
        int r;
        while(p<len) {
            r = in.read(b,p, len - p);
            if( r< 0) {
                throw new java.io.IOException("Stream broken, couldn't demarshal string :"+len+":"+p);
            }
            p = p+r;
        }
        return new String(b, CHARSET);
    }

}


// Ajp use Status: instead of Status
class AJP12ResponseAdapter extends HttpResponseAdapter {
    /** Override setStatus
     */
    protected void sendStatus( int status, String message)  throws IOException {
	printHead("Status: " );
	printHead( String.valueOf( status ));
	printHead( " " );
	printHead( message );
	printHead("\r\n");

	// We set it here because we extend HttpResponseAdapter, and this is the
	// method that is different. 
	
	// Servlet Engine header will be set per/adapter - smarter adapters will
	// not send it every time ( have it in C side ), and we may also want
	// to add informations about the adapter used 
	if( request.getContext() != null)
	    setHeader("Servlet-Engine", request.getContext().getEngineHeader());

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3398.java