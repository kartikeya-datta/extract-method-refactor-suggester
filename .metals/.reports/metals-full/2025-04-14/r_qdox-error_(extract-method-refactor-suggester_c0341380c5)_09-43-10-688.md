error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10037.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10037.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10037.java
text:
```scala
private A@@spectJModel model = null;

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 * 
 * ******************************************************************/

package org.aspectj.ajde;

import java.util.Iterator;

import org.aspectj.ajdt.internal.core.builder.AsmNodeFormatter;
import org.aspectj.asm.*;
import org.aspectj.asm.IProgramElement.Kind;


// TODO: add tests for java kinds
public class AsmDeclarationsTest extends AjdeTestCase {

	private StructureModel model = null;
	private static final String CONFIG_FILE_PATH = "../examples/coverage/coverage.lst";
	private static final int DEC_MESSAGE_LENGTH = AsmNodeFormatter.MAX_MESSAGE_LENGTH;

	public AsmDeclarationsTest(String name) {
		super(name);  
	}

	public void testRoot() {
		IProgramElement root = (IProgramElement)model.getRoot();
		assertNotNull(root);
		assertEquals(root.getName(), "coverage.lst");	
	}
	
	public void testFileInPackageAndDefaultPackage() {
		IProgramElement root = model.getRoot();
		assertEquals(root.getName(), "coverage.lst");	
		IProgramElement pkg = (IProgramElement)root.getChildren().get(1);
		assertEquals(pkg.getName(), "pkg");	
		assertEquals(((IProgramElement)pkg.getChildren().get(0)).getName(), "InPackage.java"); 	
		assertEquals(((IProgramElement)root.getChildren().get(0)).getName(), "ModelCoverage.java"); 
	}  

	public void testDeclares() {
		IProgramElement node = (IProgramElement)model.getRoot();
		assertNotNull(node);
	
		IProgramElement aspect = AsmManager.getDefault().getModel().findNodeForType(null, "DeclareCoverage");
		assertNotNull(aspect);
		
		String decErrMessage = "declare error: \"Illegal construct..\"";
		IProgramElement decErrNode = model.findNode(aspect, IProgramElement.Kind.DECLARE_ERROR, decErrMessage);
		assertNotNull(decErrNode);
		assertEquals(decErrNode.getName(), decErrMessage);
		
		String decWarnMessage = "declare warning: \"Illegal construct..\"";
		IProgramElement decWarnNode = model.findNode(aspect, IProgramElement.Kind.DECLARE_WARNING, decWarnMessage);
		assertNotNull(decWarnNode);
		assertEquals(decWarnNode.getName(), decWarnMessage);	
		
		String decParentsMessage = "declare parents: Point";
		IProgramElement decParentsNode = model.findNode(aspect, IProgramElement.Kind.DECLARE_PARENTS, decParentsMessage);
		assertNotNull(decParentsNode);		
		assertEquals(decParentsNode.getName(), decParentsMessage);	
			
		String decParentsPtnMessage = "declare parents: Point+";
		IProgramElement decParentsPtnNode = model.findNode(aspect, IProgramElement.Kind.DECLARE_PARENTS, decParentsPtnMessage);
		assertNotNull(decParentsPtnNode);		
		assertEquals(decParentsPtnNode.getName(), decParentsPtnMessage);			

		String decParentsTPMessage = "declare parents: <type pattern>";
		IProgramElement decParentsTPNode = model.findNode(aspect, IProgramElement.Kind.DECLARE_PARENTS, decParentsTPMessage);
		assertNotNull(decParentsTPNode);		
		assertEquals(decParentsTPNode.getName(), decParentsTPMessage);
		
		String decSoftMessage = "declare soft: SizeException";
		IProgramElement decSoftNode = model.findNode(aspect, IProgramElement.Kind.DECLARE_SOFT, decSoftMessage);
		assertNotNull(decSoftNode);		
		assertEquals(decSoftNode.getName(), decSoftMessage);		

		String decPrecMessage = "declare precedence: AdviceCoverage, InterTypeDecCoverage, <type pattern>";
		IProgramElement decPrecNode = model.findNode(aspect, IProgramElement.Kind.DECLARE_PRECEDENCE, decPrecMessage);
		assertNotNull(decPrecNode);		
		assertEquals(decPrecNode.getName(), decPrecMessage);	
	} 

	public void testInterTypeMemberDeclares() {
		IProgramElement node = (IProgramElement)model.getRoot();
		assertNotNull(node);
	
		IProgramElement aspect = AsmManager.getDefault().getModel().findNodeForType(null, "InterTypeDecCoverage");
		assertNotNull(aspect);
		
		String fieldMsg = "Point.xxx";
		IProgramElement fieldNode = model.findNode(aspect, IProgramElement.Kind.INTER_TYPE_FIELD, fieldMsg);
		assertNotNull(fieldNode);		
		assertEquals(fieldNode.getName(), fieldMsg);

		String methodMsg = "Point.check(int, Line)";
		IProgramElement methodNode = model.findNode(aspect, IProgramElement.Kind.INTER_TYPE_METHOD, methodMsg);
		assertNotNull(methodNode);		
		assertEquals(methodNode.getName(), methodMsg);

		// TODO: enable
//		String constructorMsg = "Point.new(int, int, int)";
//		ProgramElementNode constructorNode = model.findNode(aspect, ProgramElementNode.Kind.INTER_TYPE_CONSTRUCTOR, constructorMsg);
//		assertNotNull(constructorNode);		
//		assertEquals(constructorNode.getName(), constructorMsg);
	}

	public void testPointcuts() {
		IProgramElement node = (IProgramElement)model.getRoot();
		assertNotNull(node);
	
		IProgramElement aspect = AsmManager.getDefault().getModel().findNodeForType(null, "AdviceNamingCoverage");
		assertNotNull(aspect);		
	
		String ptct = "named()";
		IProgramElement ptctNode = model.findNode(aspect, IProgramElement.Kind.POINTCUT, ptct);
		assertNotNull(ptctNode);		
		assertEquals(ptctNode.getName(), ptct);		

		String params = "namedWithArgs(int, int)";
		IProgramElement paramsNode = model.findNode(aspect, IProgramElement.Kind.POINTCUT, params);
		assertNotNull(paramsNode);		
		assertEquals(paramsNode.getName(), params);	


	}

	public void testAbstract() {
		IProgramElement node = (IProgramElement)model.getRoot();
		assertNotNull(node);
	
		IProgramElement aspect = AsmManager.getDefault().getModel().findNodeForType(null, "AbstractAspect");
		assertNotNull(aspect);	
		
		String abst = "abPtct()";
		IProgramElement abstNode = model.findNode(aspect, IProgramElement.Kind.POINTCUT, abst);
		assertNotNull(abstNode);		
		assertEquals(abstNode.getName(), abst);			
	}

	public void testAdvice() {
		IProgramElement node = (IProgramElement)model.getRoot();
		assertNotNull(node);
	
		IProgramElement aspect = AsmManager.getDefault().getModel().findNodeForType(null, "AdviceNamingCoverage");
		assertNotNull(aspect);	

		String anon = "before(): <anonymous pointcut>";
		IProgramElement anonNode = model.findNode(aspect, IProgramElement.Kind.ADVICE, anon);
		assertNotNull(anonNode);		
		assertEquals(anonNode.getName(), anon);			

		String named = "before(): named..";
		IProgramElement namedNode = model.findNode(aspect, IProgramElement.Kind.ADVICE, named);
		assertNotNull(namedNode);		
		assertEquals(namedNode.getName(), named);		

		String namedWithOneArg = "around(int): namedWithOneArg..";
		IProgramElement namedWithOneArgNode = model.findNode(aspect, IProgramElement.Kind.ADVICE, namedWithOneArg);
		assertNotNull(namedWithOneArgNode);		
		assertEquals(namedWithOneArgNode.getName(), namedWithOneArg);		

		String afterReturning = "afterReturning(int, int): namedWithArgs..";
		IProgramElement afterReturningNode = model.findNode(aspect, IProgramElement.Kind.ADVICE, afterReturning);
		assertNotNull(afterReturningNode);		
		assertEquals(afterReturningNode.getName(), afterReturning);

		String around = "around(int): namedWithOneArg..";
		IProgramElement aroundNode = model.findNode(aspect, IProgramElement.Kind.ADVICE, around);
		assertNotNull(aroundNode);		
		assertEquals(aroundNode.getName(), around);

		String compAnon = "before(int): <anonymous pointcut>..";
		IProgramElement compAnonNode = model.findNode(aspect, IProgramElement.Kind.ADVICE, compAnon);
		assertNotNull(compAnonNode);		
		assertEquals(compAnonNode.getName(), compAnon);

		String compNamed = "before(int): named()..";
		IProgramElement compNamedNode = model.findNode(aspect, IProgramElement.Kind.ADVICE, compNamed);
		assertNotNull(compNamedNode);		
		assertEquals(compNamedNode.getName(), compNamed);
	}

	protected void setUp() throws Exception {
		super.setUp("examples");
		assertTrue("build success", doSynchronousBuild(CONFIG_FILE_PATH));	
		model =	AsmManager.getDefault().getModel();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10037.java