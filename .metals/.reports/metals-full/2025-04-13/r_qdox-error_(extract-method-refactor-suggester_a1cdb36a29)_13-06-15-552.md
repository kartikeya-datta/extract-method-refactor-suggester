error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3314.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3314.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3314.java
text:
```scala
public static R@@esolvedTypeMunger readConstructor(VersionedDataInputStream s, ISourceContext context) throws IOException {

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import org.aspectj.bridge.IMessage;

public class NewConstructorTypeMunger extends ResolvedTypeMunger {
	private ResolvedMember syntheticConstructor;
	private ResolvedMember explicitConstructor;


	public NewConstructorTypeMunger(
		ResolvedMember signature,
		ResolvedMember syntheticConstructor,
		ResolvedMember explicitConstructor,
		Set superMethodsCalled)
	{
		super(Constructor, signature);
		this.syntheticConstructor = syntheticConstructor;
		this.explicitConstructor = explicitConstructor;
		this.setSuperMethodsCalled(superMethodsCalled);

	}
	
	// doesnt seem required....
//	public ResolvedMember getDispatchMethod(UnresolvedType aspectType) {
//		return AjcMemberMaker.interMethodBody(signature, aspectType);
//	}

	public void write(DataOutputStream s) throws IOException {
		kind.write(s);
		signature.write(s);
		syntheticConstructor.write(s);
		explicitConstructor.write(s);
		writeSuperMethodsCalled(s);
		if (ResolvedTypeMunger.persistSourceLocation) writeSourceLocation(s);
	}
	
	public static ResolvedTypeMunger readConstructor(DataInputStream s, ISourceContext context) throws IOException {
		ResolvedTypeMunger munger = new NewConstructorTypeMunger(
				ResolvedMember.readResolvedMember(s, context),
				ResolvedMember.readResolvedMember(s, context),
				ResolvedMember.readResolvedMember(s, context),
				readSuperMethodsCalled(s));
		if (ResolvedTypeMunger.persistSourceLocation) munger.setSourceLocation(readSourceLocation(s));
		return munger;
	}

	public ResolvedMember getExplicitConstructor() {
		return explicitConstructor;
	}

	public ResolvedMember getSyntheticConstructor() {
		return syntheticConstructor;
	}

	public void setExplicitConstructor(ResolvedMember explicitConstructor) {
		this.explicitConstructor = explicitConstructor;
	}
	
	public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedType aspectType) {
		ResolvedMember ret = getSyntheticConstructor();
		if (ResolvedType.matches(ret, member)) return getSignature();
		return super.getMatchingSyntheticMember(member, aspectType);
	}
	
	public void check(World world) {
		if (getSignature().getDeclaringType().resolve(world).isAspect()) {
			world.showMessage(IMessage.ERROR, 
					WeaverMessages.format(WeaverMessages.ITD_CONS_ON_ASPECT),
					getSignature().getSourceLocation(), null);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3314.java