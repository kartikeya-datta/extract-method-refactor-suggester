error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17522.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17522.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17522.java
text:
```scala
n@@ftm.setDeclaredSignature(getSignature());

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

public class NewFieldTypeMunger extends ResolvedTypeMunger {

	public NewFieldTypeMunger(ResolvedMember signature, Set superMethodsCalled, List typeVariableAliases) {
		super(Field, signature);
		this.typeVariableAliases = typeVariableAliases;
		signature.setAnnotatedElsewhere(true);
		this.setSuperMethodsCalled(superMethodsCalled);
	}

	public ResolvedMember getInitMethod(UnresolvedType aspectType) {
		return AjcMemberMaker.interFieldInitializer(signature, aspectType);
	}

	public void write(DataOutputStream s) throws IOException {
		kind.write(s);
		signature.write(s);
		writeSuperMethodsCalled(s);
		writeSourceLocation(s);
		writeOutTypeAliases(s);
	}

	public static ResolvedTypeMunger readField(VersionedDataInputStream s, ISourceContext context) throws IOException {
		ISourceLocation sloc = null;
		ResolvedMember fieldSignature = ResolvedMemberImpl.readResolvedMember(s, context);
		Set superMethodsCalled        = readSuperMethodsCalled(s);
		sloc                          = readSourceLocation(s);
		List aliases                  = readInTypeAliases(s);
		ResolvedTypeMunger munger = new NewFieldTypeMunger(fieldSignature,superMethodsCalled,aliases);
		if (sloc!=null) munger.setSourceLocation(sloc);
		return munger;
	}
	
	public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedType aspectType) {
		//??? might give a field where a method is expected	
		ResolvedType onType = aspectType.getWorld().resolve(getSignature().getDeclaringType());
		if (onType.isRawType()) onType = onType.getGenericType();
		
		ResolvedMember ret = AjcMemberMaker.interFieldGetDispatcher(getSignature(), aspectType);
		if (ResolvedType.matches(ret, member)) return getSignature();
		ret = AjcMemberMaker.interFieldSetDispatcher(getSignature(), aspectType);
		if (ResolvedType.matches(ret, member)) return getSignature();
		ret = AjcMemberMaker.interFieldInterfaceGetter(getSignature(), onType, aspectType);
		if (ResolvedType.matches(ret, member)) return getSignature();
		ret = AjcMemberMaker.interFieldInterfaceSetter(getSignature(), onType, aspectType);
		if (ResolvedType.matches(ret, member)) return getSignature();
		return super.getMatchingSyntheticMember(member, aspectType);
	}

	/**
     * see ResolvedTypeMunger.parameterizedFor(ResolvedType)
     */
	public ResolvedTypeMunger parameterizedFor(ResolvedType target) {
		ResolvedType genericType = target;
		if (target.isRawType() || target.isParameterizedType()) genericType = genericType.getGenericType();
		ResolvedMember parameterizedSignature = null;
		// If we are parameterizing it for a generic type, we just need to 'swap the letters' from the ones used 
		// in the original ITD declaration to the ones used in the actual target type declaration.
		if (target.isGenericType()) {
			TypeVariable vars[] = target.getTypeVariables();
			UnresolvedTypeVariableReferenceType[] varRefs = new UnresolvedTypeVariableReferenceType[vars.length];
			for (int i = 0; i < vars.length; i++) {
				varRefs[i] = new UnresolvedTypeVariableReferenceType(vars[i]);
			}
			parameterizedSignature = getSignature().parameterizedWith(varRefs,genericType,true,typeVariableAliases);
		} else {
		  // For raw and 'normal' parameterized targets  (e.g. Interface, Interface<String>)
		  parameterizedSignature = getSignature().parameterizedWith(target.getTypeParameters(),genericType,target.isParameterizedType(),typeVariableAliases);
		}
		NewFieldTypeMunger nftm = new NewFieldTypeMunger(parameterizedSignature,getSuperMethodsCalled(),typeVariableAliases);
	    nftm.setOriginalSignature(getSignature());
	    return nftm;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17522.java