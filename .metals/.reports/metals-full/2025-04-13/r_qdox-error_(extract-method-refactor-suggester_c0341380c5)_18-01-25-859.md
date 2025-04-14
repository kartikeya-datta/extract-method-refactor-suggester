error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8523.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8523.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,48]

error in qdox parser
file content:
```java
offset: 48
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8523.java
text:
```scala
"org.jboss.jmx.connector.rmi.RMIAdaptorService",@@

/*
* JBoss, the OpenSource J2EE webOS
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.jmx.connector.rmi;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;
import javax.naming.InitialContext; 

/**
* Test Program for the JMX Connector over RMI for the server-side.
*
* It creates a local MBeanServer, loads the RMI Connector and registered
* it as a MBean. At the end it will bind it to the local JNDI server
* (us your own or download the ??.jar and ??.properties from jBoss).
* Afterwards you can download connector.jar from jBoss and test the
* connection.
*
*   @see <related>
*   @author Andreas Schaefer (andreas.schaefer@madplanet.com)
**/
public class TestServer {
	// Constants -----------------------------------------------------> 
	// Attributes ----------------------------------------------------> 
	// Static --------------------------------------------------------
	public static void main(String[] args)
		throws Exception
	{
		// Start server - Main does not have the proper permissions
		AccessController.doPrivileged(
			new PrivilegedAction() {
				public Object run() {
					new TestServer();
					return null;
				}
			}
		);
	}
	
	public TestServer() {
		try {
			System.out.println( "Start local MBeanServer" );
			MBeanServer lServer = MBeanServerFactory.createMBeanServer();

/*
			System.out.println( "Load and register the Logger Service" );
			ObjectName lLoggerName = new ObjectName( lServer.getDefaultDomain(), "service", "Log" );
			lLoggerName = lServer.createMBean(
				"org.jboss.logging.Logger",
				lLoggerName
			).getObjectName();
         
			System.out.println( "Load and register the Log4J Service" );
			ObjectName lLog4JName = new ObjectName( lServer.getDefaultDomain(), "service", "Log4J" );
			lLog4JName = lServer.createMBean(
				"org.jboss.logging.Log4jService",
				lLog4JName
			).getObjectName();
			System.out.println( "Init and Start the Log4J Service" );
//			lServer.invoke( lLog4JName, "init", new Object[] {}, new String[] {} );
			lServer.invoke( lLog4JName, "start", new Object[] {}, new String[] {} );
*/

			System.out.println( "Load and register the Naming Service" );
			ObjectName lNamingName = new ObjectName( lServer.getDefaultDomain(), "service", "naming" );
			lServer.createMBean(
				"org.jboss.naming.NamingService",
				lNamingName
			);
			System.out.println( "Start the Naming Service" );
			try {
			lServer.invoke( lNamingName, "init", new Object[] {}, new String[] {} );
			}
			catch( MBeanException me ) {
				System.err.println( "TestServer.main(), caught: " + me +
					", target: " + me.getTargetException() );
				me.printStackTrace();
				me.getTargetException().printStackTrace();
			}
			catch( Exception e ) {
				e.printStackTrace();
			}
			lServer.invoke( lNamingName, "start", new Object[] {}, new String[] {} );
			System.out.println( "Load and register the JMX RMI-Connector" );
			ObjectName lConnectorName = new ObjectName( lServer.getDefaultDomain(), "service", "RMIConnector" );
			lServer.createMBean(
				"org.jboss.jmx.connector.rmi.RMIConnectorService",
				lConnectorName
			);
			System.out.println( "Start the Connector" );
			lServer.invoke( lConnectorName, "init", new Object[] {}, new String[] {} );
			lServer.invoke( lConnectorName, "start", new Object[] {}, new String[] {} );
			System.out.println( "Now open a new Terminal or Command Prompt and start the connector.jar test client" );
		}
		catch( RuntimeMBeanException rme ) {
			System.err.println( "TestServer.main(), caught: " + rme +
				", target: " + rme.getTargetException() );
			rme.printStackTrace();
		}
		catch( MBeanException me ) {
			System.err.println( "TestServer.main(), caught: " + me +
				", target: " + me.getTargetException() );
			me.printStackTrace();
		}
		catch( RuntimeErrorException rte ) {
			System.err.println( "TestServer.main(), caught: " + rte +
				", target: " + rte.getTargetError() );
			rte.printStackTrace();
		}
		catch( ReflectionException re ) {
			System.err.println( "TestServer.main(), caught: " + re +
				", target: " + re.getTargetException() );
			re.printStackTrace();
		}
		catch( Exception e ) {
			System.err.println( "TestServer.main(), caught: " + e );
			e.printStackTrace();
		}
	}
	
	// Constructors --------------------------------------------------> 
	// Public --------------------------------------------------------
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8523.java