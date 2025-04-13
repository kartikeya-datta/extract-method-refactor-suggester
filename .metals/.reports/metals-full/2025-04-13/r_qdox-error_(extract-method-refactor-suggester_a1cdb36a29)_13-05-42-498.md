error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2027.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2027.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2027.java
text:
```scala
F@@ile directoryFile = new File("src/test");

package org.tigris.scarab.test;

import java.io.File;

import junit.framework.TestCase;

import org.apache.torque.om.NumberKey;
import org.apache.turbine.TurbineConfig;
import org.tigris.scarab.om.ActivitySet;
import org.tigris.scarab.om.ActivitySetManager;
import org.tigris.scarab.om.ActivitySetTypePeer;
import org.tigris.scarab.om.Attachment;
import org.tigris.scarab.om.Attribute;
import org.tigris.scarab.om.AttributeManager;
import org.tigris.scarab.om.Issue;
import org.tigris.scarab.om.IssueManager;
import org.tigris.scarab.om.IssueType;
import org.tigris.scarab.om.IssueTypeManager;
import org.tigris.scarab.om.Module;
import org.tigris.scarab.om.ModuleManager;
import org.tigris.scarab.om.ScarabUser;
import org.tigris.scarab.om.ScarabUserManager;
/**
 * This test case is to verify whether exceptions in Velocity actions are
 * properly bubbled up when action.event.bubbleexception=true.  Or, if
 * action.event.bubbleexception=false, then the exceptions should be
 * logged and sunk.
 *
 * @author     <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 */
public class BaseTestCase2 extends TestCase {
	private static TurbineConfig tc = null;
	private static Module module = null;
	private static IssueType defaultIssueType = null;
	protected static int nbrDfltModules = 7;
	protected static int nbrDfltIssueTypes = 5;
	private ScarabUser user0 = null;
	private ScarabUser user1 = null;
	private ScarabUser user2 = null;
	private Issue issue0 = null;
	private Attribute platformAttribute = null;
	private Attribute assignAttribute = null;
	private Attribute ccAttribute = null;

	private static boolean initialized = false;

	public BaseTestCase2() {
	}

	public BaseTestCase2(String name) throws Exception {
		super(name);
	}
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {

		if (!initialized) {
			initTurbine();
			initScarab();
			initialized=true;
		}
	}
	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		if (tc != null) {
			//  tc.dispose();
		}
	}

	/**
	 * Grab Module #5 for testing. This is the same as what the web
	 * application does and this is setup in ScarabPage.tempWorkAround()
	 */
	private void initScarab() throws Exception {
		module = ModuleManager.getInstance(new NumberKey(5), false);
		defaultIssueType =
			IssueTypeManager.getInstance(new NumberKey(1), false);
	}
	
	private void initTurbine() throws Exception {
		File directoryFile = new File("./src/test");
		String directory = directoryFile.getAbsolutePath();

		tc =
			new TurbineConfig(directory, "TestTurbineResources.properties");
		tc.init();
		
	}

	protected ScarabUser getUser1() throws Exception {
		if (user1 == null) {
			user1 = ScarabUserManager.getInstance(new NumberKey(1), false);
		}
		return user1;
	}

	protected Issue getIssue0() throws Exception {
		if (issue0 == null) {
			issue0 = IssueManager.getInstance(new NumberKey(1), false);
		}
		return issue0;
	}

	protected Attribute getPlatformAttribute() throws Exception {
		if (platformAttribute == null) {
			platformAttribute = AttributeManager.getInstance(new NumberKey(5));
		}
		return platformAttribute;
	}

	protected ActivitySet getEditActivitySet() throws Exception {
		Attachment attach = new Attachment();
		attach.setTextFields(
			getUser1(),
			getIssue0(),
			Attachment.MODIFICATION__PK);
		attach.setName("commenttest");
		attach.save();

		ActivitySet trans =
			ActivitySetManager.getInstance(
				ActivitySetTypePeer.EDIT_ISSUE__PK,
				getUser1(),
				attach);
		trans.save();
		return trans;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2027.java