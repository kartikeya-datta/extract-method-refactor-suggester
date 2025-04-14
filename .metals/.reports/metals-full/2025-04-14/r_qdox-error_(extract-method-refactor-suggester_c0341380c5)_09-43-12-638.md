error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10038.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10038.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10038.java
text:
```scala
A@@spectJModel model = Ajde.getDefault().getStructureModelManager().getModel();

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
 *     AMC 21.01.2003 fixed for new source location in eclipse.org
 * ******************************************************************/

package org.aspectj.ajde;

import java.io.*;
import java.util.Iterator;

import junit.framework.TestSuite;

import org.aspectj.asm.*;

/**
 * @author Mik Kersten
 */
public class StructureModelTest extends AjdeTestCase {
	
	private final String CONFIG_FILE_PATH = "../examples/figures-coverage/all.lst";

	public StructureModelTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(StructureModelTest.class);
	}

	public static TestSuite suite() {
		TestSuite result = new TestSuite();
		result.addTestSuite(StructureModelTest.class);	
		return result;
	}

//  XXX this should work
//	public void testFieldInitializerCorrespondence() throws IOException {
//		File testFile = createFile("testdata/examples/figures-coverage/figures/Figure.java");	
//		IProgramElement node = Ajde.getDefault().getStructureModelManager().getStructureModel().findNodeForSourceLine(
//			testFile.getCanonicalPath(), 28);
//		assertTrue("find result", node != null) ;	
//		ProgramElementNode pNode = (ProgramElementNode)node;
//		ProgramElementNode foundNode = null;
//		final List list = pNode.getRelations();
//        assertNotNull("pNode.getRelations()", list);
//		for (Iterator it = list.iterator(); it.hasNext(); ) {
//			RelationNode relation = (RelationNode)it.next();
//			
//			if (relation.getRelation().equals(AdviceAssociation.FIELD_ACCESS_RELATION)) {
//				for (Iterator it2 = relation.getChildren().iterator(); it2.hasNext(); ) {
//					LinkNode linkNode = (LinkNode)it2.next();
//					if (linkNode.getProgramElementNode().getName().equals("this.currVal = 0")) {
//						foundNode = linkNode.getProgramElementNode();	
//					}
//				}
//			}
//		}
//		
//		assertTrue("find associated node", foundNode != null) ;
//		
//		File pointFile = createFile("testdata/examples/figures-coverage/figures/primitives/planar/Point.java");	
//		IProgramElement fieldNode = Ajde.getDefault().getStructureModelManager().getStructureModel().findNodeForSourceLine(
//			pointFile.getCanonicalPath(), 12);		
//		assertTrue("find result", fieldNode != null);
//		
//		assertTrue("matches", foundNode.getParent() == fieldNode.getParent());
//	}


	public void testRootForSourceFile() throws IOException {
		File testFile = openFile("figures-coverage/figures/Figure.java");	
		IProgramElement node = Ajde.getDefault().getStructureModelManager().getModel().findRootNodeForSourceFile(
			testFile.getCanonicalPath());
		assertTrue("find result", node != null) ;	
		IProgramElement pNode = (IProgramElement)node;
		String child = ((IProgramElement)pNode.getChildren().get(0)).getName();
        assertTrue("expected Figure got child " + child, child.equals("Figure"));
	}

	public void testPointcutName() throws IOException {
		File testFile = openFile("figures-coverage/figures/Main.java");	
		IProgramElement node = Ajde.getDefault().getStructureModelManager().getModel().findRootNodeForSourceFile(
			testFile.getCanonicalPath());
		assertTrue("find result", node != null) ;	
		IProgramElement pNode = (IProgramElement)((IProgramElement)node).getChildren().get(1);
		IProgramElement pointcut = (IProgramElement)pNode.getChildren().get(0);
		assertTrue("kind", pointcut.getKind().equals(IProgramElement.Kind.POINTCUT));
		assertTrue("found node: " + pointcut.getName(), pointcut.getName().equals("testptct()"));
	}

	public void testDeclare() {
		
		
	}

	public void testFileNodeFind() throws IOException {
		File testFile = openFile("figures-coverage/figures/Main.java");
		IProgramElement node = Ajde.getDefault().getStructureModelManager().getModel().findNodeForSourceLine(
			testFile.getCanonicalPath(), 1);
		assertTrue("find result", node != null) ;	
		assertEquals("find result has children", 2, node.getChildren().size()) ;	
		IProgramElement pNode = (IProgramElement)node;
		assertTrue("found node: " + pNode.getName(), pNode.getKind().equals(IProgramElement.Kind.FILE_JAVA));
	}
  
  	/**
  	 * @todo	add negative test to make sure things that aren't runnable aren't annotated
  	 */ 
	public void testMainClassNodeInfo() throws IOException {
        StructureModel model = Ajde.getDefault().getStructureModelManager().getModel();
        assertTrue("model exists", model != null);
		assertTrue("root exists", model.getRoot() != null);
		File testFile = openFile("figures-coverage/figures/Main.java");
		IProgramElement node = model.findNodeForSourceLine(testFile.getCanonicalPath(), 11);	
		assertTrue("find result", node != null);	
		IProgramElement pNode = (IProgramElement)((IProgramElement)node).getParent();
        if (null == pNode) {
            assertTrue("null parent of " + node, false);
        }
		assertTrue("found node: " + pNode.getName(), pNode.isRunnable());
	}  
	
	/**
	 * Integrity could be checked somewhere in the API.
	 */ 
	public void testModelIntegrity() {
		IProgramElement modelRoot = Ajde.getDefault().getStructureModelManager().getModel().getRoot();
		assertTrue("root exists", modelRoot != null);	
		
		try {
			testModelIntegrityHelper(modelRoot);
		} catch (Exception e) {
			assertTrue(e.toString(), false);	
		}
	}

	private void testModelIntegrityHelper(IProgramElement node) throws Exception {
		for (Iterator it = node.getChildren().iterator(); it.hasNext(); ) {
			IProgramElement child = (IProgramElement)it.next();
			if (node == child.getParent()) {
				testModelIntegrityHelper(child);
			} else {
				throw new Exception("parent-child check failed for child: " + child.toString());
			}
		}		
	}
  
  	public void testNoChildIsNull() {
  		HierarchyWalker walker = new HierarchyWalker() {
  		    public void preProcess(IProgramElement node) {
  		    	if (node.getChildren() == null) return;
  		    	for (Iterator it = node.getChildren().iterator(); it.hasNext(); ) {
  		    		if (it.next() == null) throw new NullPointerException("null child on node: " + node.getName());	
  		    	}
  		    }
  		};
  		Ajde.getDefault().getStructureModelManager().getModel().getRoot().walk(walker);
  	}  
  
	protected void setUp() throws Exception {
		super.setUp("examples");
		doSynchronousBuild(CONFIG_FILE_PATH);	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10038.java