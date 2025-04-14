error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4748.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4748.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4748.java
text:
```scala
a@@ssertTrue(message.getContainedMessage().getMessage().indexOf("skipping missing, empty or corrupt inpath entry") != -1);

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.ajde.internal; 

import java.io.*;
import java.util.*;

import junit.framework.TestSuite;

import org.aspectj.ajde.*;
import org.aspectj.ajde.NullIdeTaskListManager.SourceLineTask;
import org.aspectj.ajde.ui.BuildConfigModel;
//import org.aspectj.ajde.ui.internal.AjcBuildOptions;
//import org.aspectj.bridge.Message;

public class LstBuildConfigManagerTest extends AjdeTestCase {
	
//	private AjcBuildOptions buildOptions = null;
	private BuildConfigManager buildConfigManager = new LstBuildConfigManager();
//	private LstBuildConfigFileUpdater fileUpdater = new LstBuildConfigFileUpdater();

	public LstBuildConfigManagerTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(LstBuildConfigManagerTest.class);
	}

	public static TestSuite suite() {
		TestSuite result = new TestSuite();
		result.addTestSuite(LstBuildConfigManagerTest.class);	
		return result;
	}

	public void testConfigParserErrorMessages() {
		doSynchronousBuild("dir-entry.lst");
		List messages = NullIdeManager.getIdeManager().getCompilationSourceLineTasks();
		NullIdeTaskListManager.SourceLineTask message = (NullIdeTaskListManager.SourceLineTask)messages.get(0);
		
		assertEquals(message.getContainedMessage().getSourceLocation().getSourceFile().getAbsolutePath(), openFile("dir-entry.lst").getAbsolutePath());

		doSynchronousBuild("bad-injar.lst");
		messages = NullIdeManager.getIdeManager().getCompilationSourceLineTasks();
		message = (NullIdeTaskListManager.SourceLineTask)messages.get(0);
		assertTrue(message.getContainedMessage().getMessage().indexOf("bad inpath") != -1);
	}

	public void testErrorMessages() throws IOException {
		doSynchronousBuild("invalid-entry.lst");
		assertTrue("compile failed", testerBuildListener.getBuildSucceeded());
		  
		List messages = NullIdeManager.getIdeManager().getCompilationSourceLineTasks();
		SourceLineTask message = (SourceLineTask)messages.get(0);	
		assertTrue(message.getContainedMessage().getMessage(), message.getContainedMessage().getMessage().indexOf("aaa.bbb") != -1);		
	
	}

	public void testNonExistentConfigFile() throws IOException {
		File file = openFile("mumbleDoesNotExist.lst");
		assertTrue("valid non-existing file", !file.exists());
		BuildConfigModel model = buildConfigManager.buildModel(file.getCanonicalPath());
		assertTrue("root: " + model.getRoot(), model.getRoot() != null);		
	}

	public void testFileRelativePathSameDir() throws IOException {
		File file = openFile("file-relPath-sameDir.lst");
		buildConfigManager.buildModel(file.getCanonicalPath());
		assertTrue("single file", true);
	}  
	
//	private void verifyFile(String configFile, String fileContents) {
//		StringTokenizer st = new StringTokenizer(fileContents, ";");
//		BuildConfigModel model1 = buildConfigManager.buildModel(configFile);
//		File testFile = new File(configFile + "-test.lst");
//		model1.setSourceFile(testFile.getPath());
//		buildConfigManager.writeModel(model1);
//		List newList = fileUpdater.readConfigFile(testFile.getPath());
//		testFile.delete();
//		
//		assertTrue("contents: " + newList, verifyLists(st, newList));
//	}
//	
//	private boolean verifyLists(StringTokenizer st, List list) {
//		Iterator it = list.iterator();
//		while (st.hasMoreElements()) {
//			String s1 = (String)st.nextElement();
//			String s2 = (String)it.next();
//			if (!s1.equals(s2)) return false;
//		}
//		if (it.hasNext()) {
//			return false;
//		} else {
//			return true;
//		}
//	}
	
	protected void setUp() throws Exception {
		super.setUp("LstBuildConfigManagerTest");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
//	private static final String WILDCARDS_FILE = "C:/Dev/aspectj/tests/ajde/examples/figures-coverage/test-config.lst";
//	private static final String BAD_PATHS_FILE = "C:/Dev/aspectj/tests/ajde/examples/figures-coverage/test-error.lst";
//	private static final String INCLUDES_FILE = "C:/Dev/aspectj/tests/ajde/examples/spacewar/spacewar/demo.lst";

//	private static final String WILDCARDS_FILE_CONTENTS;
//	private static final String BAD_PATHS_FILE_CONTENTS;
//	private static final String INCLUDES_FILE_CONTENTS;
	
	static {
//		WILDCARDS_FILE_CONTENTS = 
//			"figures/Debug.java;" +
//			"figures/Figure.java;" +
//			"figures/FigureElement.java;" +
//			"figures/Main.java;" +
//			"figures/composites/Line.java;" +
//			"figures/composites/Square.java;" +
//			"figures/primitives/planar/Point.java;" +
//			"figures/primitives/solid/SolidPoint.java;";

//		BAD_PATHS_FILE_CONTENTS = WILDCARDS_FILE_CONTENTS;
//
//        // TODO-path
//		INCLUDES_FILE_CONTENTS =
//			"../coordination/Condition.java;" +
//			"../coordination/CoordinationAction.java;" +
//			"../coordination/Coordinator.java;" +
//			"../coordination/Exclusion.java;" +
//			"../coordination/MethodState.java;" +
//			"../coordination/Mutex.java;" +
//			"../coordination/Selfex.java;" +
//			"../coordination/TimeoutException.java;" +
//			"Bullet.java;" +
//			"Display.java;" +
//			"Display1.java;" +
//			"Display2.java;" +
//			"EnergyPacket.java;" +
//			"EnergyPacketProducer.java;" +
//			"EnsureShipIsAlive.java;" +
//			"Game.java;" +
//			"GameSynchronization.java;" +
//			"Pilot.java;" +
//			"Player.java;" +
//			"Registry.java;" +
//			"RegistrySynchronization.java;" +
//			"Robot.java;" +
//			"SWFrame.java;" +
//			"Ship.java;" +
//			"SpaceObject.java;" +
//			"Timer.java;";
	}
}


//public void testWildcards() {
//	verifyFile(WILDCARDS_FILE, WILDCARDS_FILE_CONTENTS);
//}
//
//public void testIncludes() {
//	verifyFile(INCLUDES_FILE, INCLUDES_FILE_CONTENTS);
//}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4748.java