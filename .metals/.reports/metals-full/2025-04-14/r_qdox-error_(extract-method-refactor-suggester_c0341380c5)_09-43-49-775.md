error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9155.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9155.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9155.java
text:
```scala
a@@ssertEquals("Pjava/lang/Comparable<TE;>;",comparable.getSignature());

/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Adrian Colyer			Initial implementation
 * ******************************************************************/
package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.Repository;
import org.aspectj.apache.bcel.classfile.GenericSignatureParser;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.Signature;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariable;
import org.aspectj.weaver.UnresolvedType;

import junit.framework.TestCase;

/**
 * @author colyer
 *
 */
public class BcelGenericSignatureToTypeXTestCase extends TestCase {

	public void testEnumFromHell() {
		BcelWorld world = new BcelWorld();
		JavaClass javaLangEnum = Repository.lookupClass("java/lang/Enum");
		Signature.ClassSignature cSig = javaLangEnum.getGenericClassTypeSignature();
		UnresolvedType superclass = 
			BcelGenericSignatureToTypeXConverter.classTypeSignature2TypeX(
					cSig.superclassSignature,
					cSig.formalTypeParameters,
					world
					);
		assertEquals("Ljava/lang/Object;",superclass.getSignature());
		assertEquals("2 superinterfaces",2,cSig.superInterfaceSignatures.length);
		UnresolvedType comparable = 
			BcelGenericSignatureToTypeXConverter.classTypeSignature2TypeX(
					cSig.superInterfaceSignatures[0],
					cSig.formalTypeParameters,
					world
					);			
		assertEquals("Pjava/lang/Comparable<Pjava/lang/Enum<Ljava/lang/Object;>;>;",comparable.getSignature());
		UnresolvedType serializable = 
			BcelGenericSignatureToTypeXConverter.classTypeSignature2TypeX(
					cSig.superInterfaceSignatures[1],
					cSig.formalTypeParameters,
					world
					);
		assertEquals("Ljava/io/Serializable;",serializable.getSignature());
	}
	
	public void testColonColon() {
		BcelWorld world = new BcelWorld();
		Signature.ClassSignature cSig = new GenericSignatureParser().parseAsClassSignature("<T::Ljava/io/Serializable;>Ljava/lang/Object;Ljava/lang/Comparable<TT;>;");
		UnresolvedType resolved = BcelGenericSignatureToTypeXConverter.classTypeSignature2TypeX(
				cSig.superclassSignature,
				cSig.formalTypeParameters,
				world);
		assertEquals("Ljava/lang/Object;",resolved.getSignature());
		UnresolvedType resolvedInt = BcelGenericSignatureToTypeXConverter.classTypeSignature2TypeX(
				cSig.superInterfaceSignatures[0],
				cSig.formalTypeParameters,
				world);
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9155.java