error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9927.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9927.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9927.java
text:
```scala
@version $@@Revision: 1.2 $

/*
 * JBoss, the OpenSource EJB server
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.web.tomcat.security;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.catalina.net.ServerSocketFactory;

import org.jboss.security.SecurityDomain;
import org.jboss.security.ssl.DomainServerSocketFactory;

/** An implementation of the catalina ServerSocketFactory for use with https
 secure transport.
 
 @see org.apache.catalina.net.ServerSocketFactory
 
 @author Scott.Stark@jboss.org
 @version $Revision: 1.1 $
 */
public class SSLServerSocketFactory
   implements ServerSocketFactory
{
   private DomainServerSocketFactory socketFactory;

   public SSLServerSocketFactory()
   {
   }
   public SSLServerSocketFactory(SecurityDomain securityDomain)
      throws IOException
   {
      socketFactory = new DomainServerSocketFactory(securityDomain);
   }

   public void setSecurityDomainName(String jndiName)
      throws NamingException, IOException
   {
      InitialContext iniCtx = new InitialContext();
      SecurityDomain securityDomain = (SecurityDomain) iniCtx.lookup(jndiName);
      socketFactory = new DomainServerSocketFactory(securityDomain);
   }

   public ServerSocket createSocket(int port) throws IOException
   {
      return createSocket(port, 50, null);
   }
   public ServerSocket createSocket(int port, int backlog)
      throws IOException
   {
      return createSocket(port, backlog, null);
   }
   /**
    * Returns a server socket which uses only the specified network
    * interface on the local host, is bound to a the specified port,
    * and uses the specified connection backlog.  The socket is configured
    * with the socket options (such as accept timeout) given to this factory.
    *
    * @param port the port to listen to
    * @param backlog how many connections are queued
    * @param ifAddress the network interface address to use
    *
    * @exception IOException for networking errors
    */
   public ServerSocket createSocket(int port, int backlog, InetAddress ifAddress)
      throws IOException
   {
      return socketFactory.createServerSocket(port, backlog, ifAddress);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9927.java