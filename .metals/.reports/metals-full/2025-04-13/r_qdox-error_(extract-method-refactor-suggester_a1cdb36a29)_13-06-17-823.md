error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3823.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3823.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3823.java
text:
```scala
S@@tring testLstFile = AjdeTests.testDataPath("StructureModelRegressionTest/example.lst");

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


package org.aspectj.ajde; 

import java.io.File;
import java.util.List;

import junit.framework.TestSuite;

import org.aspectj.asm.*;

public class StructureModelRegressionTest extends AjdeTestCase {

	public StructureModelRegressionTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(StructureModelRegressionTest.class);
	}

	public static TestSuite suite() {
		TestSuite result = new TestSuite();
		result.addTestSuite(StructureModelRegressionTest.class);	
		return result;
	}

	public void test() {
		String testLstFile = AjdeTests.TESTDATA_PATH + "/StructureModelRegressionTest/example.lst";
        File f = new File(testLstFile);
        assertTrue(testLstFile, f.canRead());
        // TODO: enable when model is verified.
//        assertTrue("saved model: " + testLstFile, verifyAgainstSavedModel(testLstFile));    
	}

	public boolean verifyAgainstSavedModel(String lstFile) {
		File modelFile = new File(genStructureModelExternFilePath(lstFile));
		IHierarchy model = getModelForFile(lstFile);
		
		if (modelFile.exists()) {
			Ajde.getDefault().getStructureModelManager().readStructureModel(lstFile);
			IHierarchy savedModel = Ajde.getDefault().getStructureModelManager().getHierarchy();
			// AMC This test will not pass as written until IProgramElement defines
			// equals. The equals loic is commented out in the IProgramElement
			// class - adding it back in could have unforeseen system-wide
			// consequences, so I've defined a IProgramElementsEqual( ) helper
			// method here instead.
			IProgramElement rootNode = model.getRoot();
			IProgramElement savedRootNode = savedModel.getRoot();
			return IProgramElementsEqual( rootNode, savedRootNode );
		} else {
			Ajde.getDefault().getStructureModelManager().writeStructureModel(lstFile);
			return true;
		}
		//return true;
	}

	private boolean IProgramElementsEqual( IProgramElement s1, IProgramElement s2 ) {
	  final boolean equal = true;
	  	if ( s1 == s2 ) return equal;
	  	if ( null == s1 || null == s2 ) return !equal;

		if (!s1.getName( ).equals(s2.getName())) return !equal;
		if (!s1.getKind( ).equals(s2.getKind())) return !equal;
		
		// check child nodes
		List s1Kids = s1.getChildren();
		List s2Kids = s2.getChildren();
		
		if ( s1Kids != null && s2Kids != null ) {
			if (s1Kids == null || s2Kids == null) return !equal;			
			if (s1Kids.size() != s2Kids.size() ) return !equal;
			for ( int k=0; k<s1Kids.size(); k++ ) {
				IProgramElement k1 = (IProgramElement) s1Kids.get(k);
				IProgramElement k2 = (IProgramElement) s2Kids.get(k);	
				if (!IProgramElementsEqual( k1, k2 )) return !equal;
			}
		}
	  return equal;		
	}

	private IHierarchy getModelForFile(String lstFile) {
		Ajde.getDefault().getConfigurationManager().setActiveConfigFile(lstFile);
		Ajde.getDefault().getBuildManager().build(); // was buildStructure...
		while(!testerBuildListener.getBuildFinished()) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException ie) { } 
		}
		return Ajde.getDefault().getStructureModelManager().getHierarchy();	
	}

	protected void setUp() throws Exception {
		super.setUp("StructureModelRegressionTest");
		Ajde.getDefault().getStructureModelManager().setShouldSaveModel(false);
	}
	
	public void testModelExists() {
		assertTrue(Ajde.getDefault().getStructureModelManager().getHierarchy() != null);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3823.java