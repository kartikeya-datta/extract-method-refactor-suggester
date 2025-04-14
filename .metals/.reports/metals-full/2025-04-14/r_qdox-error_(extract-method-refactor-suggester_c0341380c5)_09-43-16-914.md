error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11128.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11128.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11128.java
text:
```scala
public v@@oid addMethod(EclipseFactory world , ResolvedMember member) {

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/

 
 package org.aspectj.ajdt.internal.compiler.lookup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.TypeX;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;

public class HelperInterfaceBinding extends SourceTypeBinding {
	private TypeX typeX;
	SourceTypeBinding enclosingType;
	List methods = new ArrayList();
	
	public HelperInterfaceBinding(SourceTypeBinding enclosingType, TypeX typeX) {
		super();
		this.fPackage = enclosingType.fPackage;
		//this.fileName = scope.referenceCompilationUnit().getFileName();
		this.modifiers = AccPublic | AccInterface;
		this.sourceName = enclosingType.scope.referenceContext.name;
		this.enclosingType = enclosingType;
		this.typeX = typeX;
		
		this.scope = enclosingType.scope;
	}

	public HelperInterfaceBinding(
		char[][] compoundName,
		PackageBinding fPackage,
		ClassScope scope) {
		super(compoundName, fPackage, scope);
	}
	
	public char[] getFileName() {
		return enclosingType.getFileName();
	}

	public TypeX getTypeX() {
		return typeX;
	}
	
	public void addMethod(EclipseWorld world , ResolvedMember member) {
		MethodBinding binding = world.makeMethodBinding(member);
		this.methods.add(binding);
	}
	
	public FieldBinding[] fields() { return new FieldBinding[0]; }
	
	public MethodBinding[] methods() { return new MethodBinding[0]; }
	

	public char[] constantPoolName() {
		String sig = typeX.getSignature();
		return sig.substring(1, sig.length()-1).toCharArray();
	}

	public void generateClass(CompilationResult result, ClassFile enclosingClassFile) {
		ClassFile classFile = new ClassFile(this, enclosingClassFile, false);
		classFile.addFieldInfos();

		classFile.setForMethodInfos();
		for (Iterator i = methods.iterator(); i.hasNext(); ) {
			MethodBinding b = (MethodBinding)i.next();
			generateMethod(classFile, b);
		}

		classFile.addAttributes();
			
		result.record(this.constantPoolName(), classFile);
	}
	
	
	private void generateMethod(ClassFile classFile, MethodBinding binding) {
		classFile.generateMethodInfoHeader(binding);
		int methodAttributeOffset = classFile.contentsOffset;
		int attributeNumber = classFile.generateMethodInfoAttribute(binding);
		classFile.completeMethodInfo(methodAttributeOffset, attributeNumber);
	}
	
	

	public ReferenceBinding[] superInterfaces() {
		return new ReferenceBinding[0];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11128.java