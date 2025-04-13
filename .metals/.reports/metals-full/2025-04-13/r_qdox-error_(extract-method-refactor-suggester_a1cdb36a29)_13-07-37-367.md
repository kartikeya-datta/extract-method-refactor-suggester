error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8186.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8186.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8186.java
text:
```scala
B@@aseInterceptor ci[]=cm.getContextInterceptors();

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
package org.apache.tomcat.task;

import org.apache.tomcat.core.*;
import org.apache.tomcat.util.*;
import org.apache.tomcat.helper.*;
import org.apache.tomcat.util.xml.*;
import org.apache.tomcat.logging.*;
import java.io.*;
import java.net.*;
import java.util.*;

// Used to stop tomcat
import org.apache.tomcat.service.PoolTcpConnector;
import org.apache.tomcat.service.connector.Ajp12ConnectionHandler;
import org.apache.tomcat.modules.server.Ajp12Interceptor;

/**
 * This task will stop tomcat
 *
 * @author Costin Manolache
 */
public class StopTomcat { 
    static final String DEFAULT_CONFIG="conf/server.xml";
    private static StringManager sm =
	StringManager.getManager("org.apache.tomcat.resources");

    String configFile;
    String tomcatHome;
    Logger.Helper loghelper = new Logger.Helper("tc_log", "StopTomcat");
    
    public StopTomcat() 
    {
    }

    // -------------------- Parameters --------------------

    public void setConfig( String s ) {
	configFile=s;
    }

    /** -f option
     */
    public void setF( String s ) {
	configFile=s;
    }

    public void setH( String s ) {
	tomcatHome=s;
	System.getProperties().put("tomcat.home", s);
    }

    public void setHome( String s ) {
	tomcatHome=s;
	System.getProperties().put("tomcat.home", s);
    }

    // -------------------- Ant execute --------------------
    public void execute() throws Exception {
	System.out.println(sm.getString("tomcat.stop"));
	try {
	    stopTomcat(); // stop serving
	}
	catch (TomcatException te) {
	    if (te.getRootCause() instanceof java.net.ConnectException)
		System.out.println(sm.getString("tomcat.connectexception"));
	    else
		throw te;
	}
	return;
    }

    // -------------------- Implementation --------------------
    
    /** Stop tomcat using the configured cm
     *  The manager is set up using the same configuration file, so
     *  it will have the same port as the original instance ( no need
     *  for a "log" file).
     *  It uses the Ajp12 connector, which has a built-in "stop" method,
     *  that will change when we add real callbacks ( it's equivalent
     *  with the previous RMI method from almost all points of view )
     */
    void stopTomcat() throws TomcatException {
	XmlMapper xh=new XmlMapper();
	xh.setDebug( 0 );
	ContextManager cm=new ContextManager();
	
	ServerXmlHelper sxml=new ServerXmlHelper();

	sxml.setConnectorHelper( xh );
	// load server.xml
	File f = null;
	if (configFile != null)
	    f=new File(configFile);

	String tchome=sxml.getTomcatInstall();
	f=new File(tchome, DEFAULT_CONFIG);
	cm.setInstallDir( tchome);

	try {
	    xh.readXml(f,cm);
	} catch( Exception ex ) {
	    throw new TomcatException("Fatal exception reading " + f, ex);
	}
	
	execute( cm );     
    }
    
    
    /** This particular implementation will search for an AJP12
	connector ( that have a special stop command ).
    */
    public void execute(ContextManager cm)
	throws TomcatException 
    {
	// Find Ajp12 connector
	int portInt=8007;
	InetAddress address=null;
	ContextInterceptor ci[]=cm.getContextInterceptors();
	for( int i=0; i<ci.length; i++ ) {
	    Object con=ci[i];
	    if( con instanceof  Ajp12ConnectionHandler ) {
		PoolTcpConnector tcpCon=(PoolTcpConnector) con;
		portInt=tcpCon.getPort();
		address=tcpCon.getAddress();
	    }
	    if( con instanceof  Ajp12Interceptor ) {
		Ajp12Interceptor tcpCon=(Ajp12Interceptor) con;
		portInt=tcpCon.getPort();
		address=tcpCon.getAddress();
	    }
	}

	// use Ajp12 to stop the server...
	try {
	    if (address == null)
		address = InetAddress.getLocalHost();
	    Socket socket = new Socket(address, portInt);
	    OutputStream os=socket.getOutputStream();
	    byte stopMessage[]=new byte[2];
	    stopMessage[0]=(byte)254;
	    stopMessage[1]=(byte)15;
	    os.write( stopMessage );
	    socket.close();
	} catch(Exception ex ) {
	    throw new TomcatException("Error stopping Tomcat with Ajp12 on " + address + ":" + portInt, ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8186.java