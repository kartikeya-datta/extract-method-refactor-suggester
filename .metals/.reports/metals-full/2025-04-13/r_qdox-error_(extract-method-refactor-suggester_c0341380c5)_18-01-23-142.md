error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5482.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5482.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5482.java
text:
```scala
R@@esolvedMember parameterizedSignature = getSignature().parameterizedWith(target.getTypeParameters(),genericType,target.isParameterizedType(),typeVariableAliases);

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


package org.aspectj.weaver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.aspectj.bridge.ISourceLocation;

public class NewMethodTypeMunger extends ResolvedTypeMunger {
	public NewMethodTypeMunger(
		ResolvedMember signature,
		Set superMethodsCalled,
		List typeVariableAliases) {
		super(Method, signature);
		this.typeVariableAliases = typeVariableAliases;
		this.setSuperMethodsCalled(superMethodsCalled);
	}
	
	public ResolvedMember getInterMethodBody(UnresolvedType aspectType) {
		return AjcMemberMaker.interMethodBody(signature, aspectType);
	}
	
	public ResolvedMember getInterMethodDispatcher(UnresolvedType aspectType) {
		return AjcMemberMaker.interMethodDispatcher(signature, aspectType);
	}

	public void write(DataOutputStream s) throws IOException {
		kind.write(s);
		signature.write(s);
		writeSuperMethodsCalled(s);
		writeSourceLocation(s);
		writeOutTypeAliases(s);
	}
	
	public static ResolvedTypeMunger readMethod(VersionedDataInputStream s, ISourceContext context) throws IOException {
		
		ISourceLocation sloc = null;		
		ResolvedMemberImpl rmImpl = ResolvedMemberImpl.readResolvedMember(s, context);
		Set superMethodsCalled = readSuperMethodsCalled(s);
		sloc = readSourceLocation(s);
		List typeVarAliases = readInTypeAliases(s);
		
		ResolvedTypeMunger munger = new NewMethodTypeMunger(rmImpl,superMethodsCalled,typeVarAliases);
		if (sloc!=null) munger.setSourceLocation(sloc);
		return munger;
	}
	
	public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedType aspectType) {	
		ResolvedMember ret = AjcMemberMaker.interMethodDispatcher(getSignature(), aspectType);
		if (ResolvedType.matches(ret, member)) return getSignature();
		return super.getMatchingSyntheticMember(member, aspectType);
	}
	
	/**
     * ResolvedTypeMunger.parameterizedFor(ResolvedType)
     */
	public ResolvedTypeMunger parameterizedFor(ResolvedType target) {
		ResolvedType genericType = target;
		if (target.isRawType() || target.isParameterizedType()) genericType = genericType.getGenericType();
		ResolvedMember parameterizedSignature = getSignature().parameterizedWith(target.getTypeParameters(),genericType,target.isParameterizedType());
		return new NewMethodTypeMunger(parameterizedSignature,getSuperMethodsCalled(),typeVariableAliases);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5482.java