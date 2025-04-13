error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5898.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5898.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5898.java
text:
```scala
b@@uf.append((getDeclaringType()==null?"<nullDeclaringType>":getDeclaringType().getName()));

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

import org.aspectj.weaver.patterns.Pointcut;


public class ResolvedPointcutDefinition extends ResolvedMemberImpl {
	private Pointcut pointcut;

    public ResolvedPointcutDefinition(
        UnresolvedType declaringType,
        int modifiers,
        String name,
        UnresolvedType[] parameterTypes,
        Pointcut pointcut)
    {
        this(declaringType, modifiers, name, parameterTypes, ResolvedType.VOID, pointcut);
    }

    /**
     * An instance which can be given a specific returnType, used f.e. in if() pointcut for @AJ
     * 
     * @param declaringType
     * @param modifiers
     * @param name
     * @param parameterTypes
     * @param returnType
     * @param pointcut
     */
    public ResolvedPointcutDefinition(
		UnresolvedType declaringType,
		int modifiers,
		String name,
		UnresolvedType[] parameterTypes,
        UnresolvedType returnType,
		Pointcut pointcut)
    {
		super(
			POINTCUT,
			declaringType,
			modifiers,
			returnType,
			name,
			parameterTypes);
		this.pointcut = pointcut;
		//XXXpointcut.assertState(Pointcut.RESOLVED);
		checkedExceptions = UnresolvedType.NONE;
	}
	
	// ----

	public void write(DataOutputStream s) throws IOException {
		getDeclaringType().write(s);
		s.writeInt(getModifiers());
		s.writeUTF(getName());
		UnresolvedType.writeArray(getParameterTypes(), s);
		pointcut.write(s);
	}
	
	public static ResolvedPointcutDefinition read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		ResolvedPointcutDefinition rpd =
		  new ResolvedPointcutDefinition(
			UnresolvedType.read(s),
			s.readInt(),
			s.readUTF(),
			UnresolvedType.readArray(s),
			Pointcut.read(s, context));
		rpd.setSourceContext(context); // whilst we have a source context, let's remember it
		return rpd;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("pointcut ");
		buf.append(getDeclaringType().getName());
		buf.append(".");
		buf.append(getName());
		buf.append("(");
		for (int i=0; i < getParameterTypes().length; i++) {
			if (i > 0) buf.append(", ");
			buf.append(getParameterTypes()[i].toString());
		}
		buf.append(")");
		//buf.append(pointcut);
		
		return buf.toString();
	}
	
	public Pointcut getPointcut() {
		return pointcut;
	}
	
	public boolean isAjSynthetic() {
		return true;
	}
	
	/**
	 * Called when asking a parameterized super-aspect for its pointcuts.
	 */
	public ResolvedMemberImpl parameterizedWith(UnresolvedType[] typeParameters, ResolvedType newDeclaringType, boolean isParameterized) {
		return this;
	}
	
	// for testing
	public static final ResolvedPointcutDefinition DUMMY =
	    new ResolvedPointcutDefinition(UnresolvedType.OBJECT, 0, "missing", 
	    				UnresolvedType.NONE, Pointcut.makeMatchesNothing(Pointcut.RESOLVED));

	public void setPointcut(Pointcut pointcut) {
		this.pointcut = pointcut;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5898.java