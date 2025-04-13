error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3943.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3943.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3943.java
text:
```scala
S@@tringTokenizer tok = new StringTokenizer(protocolS,"|");

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


package org.apache.tomcat.util.net;


import java.net.*;
import java.util.*;
import java.io.IOException;
import org.apache.tomcat.util.compat.Jdk11Compat;

/**
 */

public final class StreamHandlerFactory implements URLStreamHandlerFactory {

    /** System property for protocol handlers.
     */
    public static final String SYS_PROTOCOLS = "java.protocol.handler.pkgs";
    private String protocolString = null;
    private Hashtable protocols = new Hashtable();
    private Jdk11Compat jdk11Compat = Jdk11Compat.getJdkCompat();

    public StreamHandlerFactory() {
	loadProtocols();
    }

    /** Create a <code>URLStreamHandler</code> for this protocol.
     *  This factory differs from the default in that
     *  <ul>
     *  <li>We load classes from the <code>ContextClassLoader</code></li>
     *  <li>If no handler is defined, we return a connection-less
     *       <code>URLStreamHandler</code> that allows parsing</li>
     *  </ul>
     */
    public  URLStreamHandler createURLStreamHandler(String protoS) {
	if(protocolString != System.getProperty(SYS_PROTOCOLS))
	    loadProtocols();
	ClassLoader acl = jdk11Compat.getContextClassLoader();
	if(acl == null)
	    acl = getClass().getClassLoader();
	Class phldrC = null;
	String phldrCN = (String)protocols.get(protoS);
	if(phldrCN != null) {
	    try {
		phldrC = acl.loadClass(phldrCN);
	    } catch(ClassNotFoundException cnfex){
	    }
	}
	if(phldrC == null){
	    phldrCN = "sun.net.www.protocol." + protoS + ".Handler";
	    try {
		phldrC = acl.loadClass(phldrCN);
	    } catch(ClassNotFoundException cnfex) {
	    }
	}
	if(phldrC == null) {
	    phldrC = DummyStreamHandler.class;
	}
	URLStreamHandler handler = null;
	try {
	    handler = (URLStreamHandler)phldrC.newInstance();
	} catch(Exception ex) {
	}
	return handler;
    }

    /** Pre-parse the defined protocols.
     *  This follows the rules specified in 
     *  <code>java.net.URL(String,String,int,String)</code>.
     */
    private synchronized void loadProtocols() {
	if(protocolString == System.getProperty(SYS_PROTOCOLS))	
	    return;
	String protocolS = System.getProperty(SYS_PROTOCOLS);
	StringTokenizer tok = new StringTokenizer(protocolString,"|");
	protocols.clear();
	while(tok.hasMoreTokens()) {
	    String protStr = tok.nextToken();
	    int hpos = protStr.lastIndexOf(".Handler");
	    if(hpos >= 0) {
		protStr = protStr.substring(0,hpos);
		int npos = protStr.lastIndexOf('.');
		String prot = protStr.substring(hpos+1);
		String protC = protStr.substring(0,hpos);
		protocols.put(prot,protC);
	    }
	}
	protocolString = protocolS;
    }
    /** A connection-less <code>URLStreamHandler</code> to allow parsing-only URLs.
     */
    public class DummyStreamHandler extends URLStreamHandler {
	DummyStreamHandler() {
	}
	protected URLConnection openConnection(java.net.URL xx) throws IOException{
	    throw new ConnectException("Connections are not supported.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3943.java