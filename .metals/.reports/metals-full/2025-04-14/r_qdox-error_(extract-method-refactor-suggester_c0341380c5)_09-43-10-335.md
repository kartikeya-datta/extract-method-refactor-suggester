error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1458.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1458.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1458.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

package org.apache.jmeter.engine;

import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.jmeter.samplers.RemoteListenerWrapper;
import org.apache.jmeter.samplers.RemoteSampleListener;
import org.apache.jmeter.samplers.RemoteSampleListenerImpl;
import org.apache.jmeter.samplers.RemoteSampleListenerWrapper;
import org.apache.jmeter.samplers.RemoteTestListenerWrapper;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.ListedHashTree;
import org.apache.jmeter.util.ListedHashTreeVisitor;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 * @author mstover
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class ConvertListeners implements ListedHashTreeVisitor {

	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.engine");
	/**
	 * @see ListedHashTreeVisitor#addNode(Object, ListedHashTree)
	 */
	public void addNode(Object node, ListedHashTree subTree) {
		if(node instanceof ThreadGroup)
			{
				log.info("num threads = "+((ThreadGroup)node).getNumThreads());
			}
		Iterator iter = subTree.list().iterator();
		while(iter.hasNext())
		{
			Object item = iter.next();
			if(item instanceof ThreadGroup)
			{
				log.info("num threads = "+((ThreadGroup)item).getNumThreads());
			}
			if(item instanceof Remoteable && (item instanceof TestListener || item instanceof SampleListener))
			{
				try {
					RemoteSampleListener rtl = new RemoteSampleListenerImpl(item);
					if(item instanceof TestListener && item instanceof SampleListener)
					{
						RemoteListenerWrapper wrap = new RemoteListenerWrapper(rtl);
						subTree.replace(item,wrap);
					}
					else if(item instanceof TestListener)
					{
						RemoteTestListenerWrapper wrap = new RemoteTestListenerWrapper(rtl);
						subTree.replace(item,wrap);
					}
					else
					{
						RemoteSampleListenerWrapper wrap = new RemoteSampleListenerWrapper(rtl);
						subTree.replace(item,wrap);
					}
				} catch(RemoteException e) {
					log.error("",e);
				}
			}
		}
	}

	/**
	 * @see ListedHashTreeVisitor#subtractNode()
	 */
	public void subtractNode() {
	}

	/**
	 * @see ListedHashTreeVisitor#processPath()
	 */
	public void processPath() {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1458.java