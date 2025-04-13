error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8687.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8687.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8687.java
text:
```scala
S@@tring rootFile=(String)attributes.get("rootfile");

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

package org.apache.tomcat.util.net;

import java.io.*;
import java.net.*;

import COM.claymoresystems.ptls.*;
import COM.claymoresystems.cert.*;
import COM.claymoresystems.sslg.*;

/**
 * SSL server socket factory--wraps PureTLS
 *
 * @author Eric Rescorla
 *
 * some sections of this file cribbed from SSLSocketFactory
 * (the JSSE socket factory)
 *
 */
 
public class PureTLSSocketFactory
    extends org.apache.tomcat.util.net.ServerSocketFactory
{
    static String defaultProtocol = "TLS";
    static boolean defaultClientAuth = false;
    static String defaultKeyStoreFile = "server.pem";
    static String defaultKeyPass = "password";    
    static String defaultRootFile = "root.pem";
    static String defaultRandomFile = "random.pem";
    
    private SSLContext context=null;
    
    public PureTLSSocketFactory() {
    }

    public ServerSocket createSocket(int port)
	throws IOException
    {
	init();
	return new SSLServerSocket(context,port);
    }

    public ServerSocket createSocket(int port, int backlog)
	throws IOException
    {
	init();
	ServerSocket tmp;
	
	try {
	    tmp=new SSLServerSocket(context,port,backlog);
	}
	catch (IOException e){
	    throw e;
	}
	return tmp;
    }

    public ServerSocket createSocket(int port, int backlog,
				     InetAddress ifAddress)
	throws IOException
    {
	init();
	return new SSLServerSocket(context,port,backlog,ifAddress);
    }

    private void init()
	throws IOException
    {
	if(context!=null)
	    return;
	
	boolean clientAuth=defaultClientAuth;

	try {
	    String keyStoreFile=(String)attributes.get("keystore");
	    if(keyStoreFile==null) keyStoreFile=defaultKeyStoreFile;
	    
	    String keyPass=(String)attributes.get("keypass");
	    if(keyPass==null) keyPass=defaultKeyPass;
	    
	    String rootFile=(String)attributes.get("randomfile");
	    if(rootFile==null) rootFile=defaultRootFile;

	    String randomFile=(String)attributes.get("randomfile");
	    if(randomFile==null) randomFile=defaultRandomFile;
	    
	    String protocol=(String)attributes.get("protocol");
	    if(protocol==null) protocol=defaultProtocol;

	    String clientAuthStr=(String)attributes.get("clientauth");
	    if(clientAuthStr != null){
		if(clientAuthStr.equals("true")){
		    clientAuth=true;
		} else if(clientAuthStr.equals("false")) {
		    clientAuth=false;
		} else {
		    throw new IOException("Invalid value '" +
					  clientAuthStr + 
					  "' for 'clientauth' parameter:");
		}
	    }

	    SSLContext tmpContext=new SSLContext();
	    if(clientAuth){
		tmpContext.loadRootCertificates(rootFile);
	    }
	    tmpContext.loadEAYKeyFile(keyStoreFile,keyPass);
	    tmpContext.useRandomnessFile(randomFile,keyPass);
	    
	    SSLPolicyInt policy=new SSLPolicyInt();
	    policy.requireClientAuth(clientAuth);
	    policy.handshakeOnConnect(false);
	    policy.waitOnClose(false);
	    tmpContext.setPolicy(policy);
	    context=tmpContext;
	} catch (Exception e){
	    throw new IOException(e.getMessage());
	}
    }

    public Socket acceptSocket(ServerSocket socket)
	throws IOException
    {
	try {
	    Socket sock=socket.accept();
	    return sock;
	} catch (SSLException e){
            throw new SocketException("SSL handshake error" + e.toString());
	}
    }

    public void handshake(Socket sock)
	 throws IOException
    {
	((SSLSocket)sock).handshake();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8687.java