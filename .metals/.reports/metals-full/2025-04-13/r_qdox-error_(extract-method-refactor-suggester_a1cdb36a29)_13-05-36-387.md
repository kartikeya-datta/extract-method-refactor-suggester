error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/192.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/192.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/192.java
text:
```scala
public v@@oid updateSelectedGUI()

package org.columba.core.command;

import java.util.Vector;

import junit.framework.TestCase;

import org.columba.core.gui.FrameController;
import org.columba.core.logging.ColumbaLogger;

/**
 * @author Timo Stich (tstich@users.sourceforge.net)
 * 
 */
public class DefaultProcessorTest extends TestCase {

	private DefaultProcessor processor;

	/**
	 * Constructor for DefaultProcessorTest.
	 * @param arg0
	 */
	public DefaultProcessorTest(String arg0) {
		super(arg0);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		new ColumbaLogger();
		
		processor = new DefaultProcessor();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddOp_PriorityOrdering() {
		TestCommand command1 = new TestCommand( null, null );
		command1.setPriority(Command.NORMAL_PRIORITY);
		processor.addOp(command1,Command.FIRST_EXECUTION);
				
		TestCommand command2 = new TestCommand( null, null );
		command2.setPriority(Command.NORMAL_PRIORITY);
		processor.addOp(command2,Command.FIRST_EXECUTION);

		TestCommand command3 = new TestCommand( null, null );
		command3.setPriority(Command.REALTIME_PRIORITY);
		processor.addOp(command3,Command.FIRST_EXECUTION);

		TestCommand command4 = new TestCommand( null, null );
		command4.setPriority(Command.DAEMON_PRIORITY);
		processor.addOp(command4,Command.FIRST_EXECUTION);

		TestCommand command5 = new TestCommand( null, null );
		command5.setPriority(Command.NORMAL_PRIORITY);
		processor.addOp(command5,Command.FIRST_EXECUTION);

		Vector result = processor.getOperationQueue();

		assertTrue( ((OperationItem)result.elementAt(0)).operation == command3);
		assertTrue( ((OperationItem)result.elementAt(1)).operation == command1);
		assertTrue( ((OperationItem)result.elementAt(2)).operation == command2);
		assertTrue( ((OperationItem)result.elementAt(3)).operation == command5);		
		assertTrue( ((OperationItem)result.elementAt(4)).operation == command4);		
	}

	public void testAddOp_PriorityOrderingWithSynchronized() {
		TestCommand command1 = new TestCommand( null, null );
		command1.setPriority(Command.NORMAL_PRIORITY);
		processor.addOp(command1,Command.FIRST_EXECUTION);
				
		TestCommand command2 = new TestCommand( null, null );
		command2.setPriority(Command.NORMAL_PRIORITY);
		command2.setSynchronize(true);
		processor.addOp(command2,Command.FIRST_EXECUTION);

		TestCommand command3 = new TestCommand( null, null );
		command3.setPriority(Command.REALTIME_PRIORITY);
		command3.setSynchronize(true);
		processor.addOp(command3,Command.FIRST_EXECUTION);

		TestCommand command4 = new TestCommand( null, null );
		command4.setPriority(Command.DAEMON_PRIORITY);
		command4.setSynchronize(true);
		processor.addOp(command4,Command.FIRST_EXECUTION);

		TestCommand command5 = new TestCommand( null, null );
		command5.setPriority(Command.NORMAL_PRIORITY);
		processor.addOp(command5,Command.FIRST_EXECUTION);

		Vector result = processor.getOperationQueue();

		assertTrue( ((OperationItem)result.elementAt(0)).operation == command1);
		assertTrue( ((OperationItem)result.elementAt(1)).operation == command2);
		assertTrue( ((OperationItem)result.elementAt(2)).operation == command3);
		assertTrue( ((OperationItem)result.elementAt(3)).operation == command4);		
		assertTrue( ((OperationItem)result.elementAt(4)).operation == command5);		
	}


}

class TestCommand extends Command {
	
	public TestCommand( FrameController controller, DefaultCommandReference[] arguments ) {
		super( controller, arguments );		
	}

	public void updateGUI()
	{}

	public void execute( Worker worker ) throws Exception {
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/192.java