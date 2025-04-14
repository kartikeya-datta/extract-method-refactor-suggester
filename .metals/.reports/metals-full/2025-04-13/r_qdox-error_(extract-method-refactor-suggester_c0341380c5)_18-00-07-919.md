error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11130.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11130.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11130.java
text:
```scala
W@@orld world = inAspect.world.getWorld();

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aspectj.ajdt.internal.compiler.ast.AspectDeclaration;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.Lint;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.World;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.IPrivilegedHandler;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;



public class PrivilegedHandler implements IPrivilegedHandler {
	private AspectDeclaration inAspect;
	private Map accessors = new HashMap();

	public PrivilegedHandler(AspectDeclaration inAspect) {
		this.inAspect = inAspect;
	}

	public FieldBinding getPrivilegedAccessField(FieldBinding baseField, AstNode location) {
		ResolvedMember key = inAspect.world.makeResolvedMember(baseField);
		if (accessors.containsKey(key)) return (FieldBinding)accessors.get(key);
		FieldBinding ret = new PrivilegedFieldBinding(inAspect, baseField);
		checkWeaveAccess(key.getDeclaringType(), location);
		accessors.put(key, ret);
		return ret;
	}

	public MethodBinding getPrivilegedAccessMethod(MethodBinding baseMethod, AstNode location) {
		ResolvedMember key = inAspect.world.makeResolvedMember(baseMethod);
		if (accessors.containsKey(key)) return (MethodBinding)accessors.get(key);
		
		MethodBinding ret;
		if (baseMethod.isConstructor()) {
			ret = baseMethod;
		} else {
			ret = inAspect.world.makeMethodBinding(
			AjcMemberMaker.privilegedAccessMethodForMethod(inAspect.typeX, key)
			);
		}
		checkWeaveAccess(key.getDeclaringType(), location);
		//new PrivilegedMethodBinding(inAspect, baseMethod);
		accessors.put(key, ret);
		return ret;
	}
	
	public void notePrivilegedTypeAccess(ReferenceBinding type, AstNode location) {
		ResolvedMember key =
			new ResolvedMember(Member.STATIC_INITIALIZATION,
				inAspect.world.fromEclipse(type), 0, ResolvedTypeX.VOID, "", TypeX.NONE);
		
		checkWeaveAccess(key.getDeclaringType(), location);
		accessors.put(key, key);
	}

	private void checkWeaveAccess(TypeX typeX, AstNode location) {
		World world = inAspect.world;
		Lint.Kind check = world.getLint().typeNotExposedToWeaver;
		if (check.isEnabled()) {
			if (!world.resolve(typeX).isExposedToWeaver()) {
				ISourceLocation loc = null;
				if (location != null) {
					loc = new EclipseSourceLocation(inAspect.compilationResult, 
							location.sourceStart, location.sourceEnd);
				}
				check.signal(typeX.getName() + " (needed for privileged access)",
							loc);
			}
		}
	}
	
	public ResolvedMember[] getMembers() {
		Collection m = accessors.keySet();
		int len = m.size();
		ResolvedMember[] ret = new ResolvedMember[len];
		int index = 0;
		for (Iterator i = m.iterator(); i.hasNext(); ) {
			ret[index++] = (ResolvedMember)i.next();
		}
		return ret;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11130.java