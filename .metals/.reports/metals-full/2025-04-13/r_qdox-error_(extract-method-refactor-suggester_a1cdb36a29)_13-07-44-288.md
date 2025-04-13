error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8097.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8097.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8097.java
text:
```scala
public final P@@ointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {

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


package org.aspectj.weaver.patterns;

import java.io.IOException;

import org.aspectj.util.TypeSafeEnum;
import org.aspectj.weaver.*;

// PTWIMPL New kind added to this class, can be (de)serialized
public abstract class PerClause extends Pointcut {
	protected ResolvedType inAspect;

	public static PerClause readPerClause(VersionedDataInputStream s, ISourceContext context) throws IOException {
		Kind kind = Kind.read(s);
		if (kind == SINGLETON) return PerSingleton.readPerClause(s, context);
		else if (kind == PERCFLOW) return PerCflow.readPerClause(s, context);
		else if (kind == PEROBJECT) return PerObject.readPerClause(s, context);
		else if (kind == FROMSUPER) return PerFromSuper.readPerClause(s, context);
		else if (kind == PERTYPEWITHIN) return PerTypeWithin.readPerClause(s,context);
			
		throw new BCException("unknown kind: " + kind);
	}

    public final Pointcut concretize1(ResolvedType inAspect, IntMap bindings) {
    	throw new RuntimeException("unimplemented: wrong concretize");
    }

	public abstract PerClause concretize(ResolvedType inAspect);
	
	public abstract PerClause.Kind getKind();
	
	public abstract String toDeclarationString();
	
	public static class Kind extends TypeSafeEnum {
        public Kind(String name, int key) { super(name, key); }
        
        public static Kind read(VersionedDataInputStream s) throws IOException {
            int key = s.readByte();
            switch(key) {
                case 1: return SINGLETON;
                case 2: return PERCFLOW;
                case 3: return PEROBJECT;
                case 4: return FROMSUPER;
                case 5: return PERTYPEWITHIN;
            }
            throw new BCException("weird kind " + key);
        }
    }

	public void resolveBindingsFromRTTI() {
    	throw new UnsupportedOperationException("Can't resolve per-clauses at runtime");
    }

	public static final Kind SINGLETON = new Kind("issingleton", 1);
	public static final Kind PERCFLOW  = new Kind("percflow", 2);
	public static final Kind PEROBJECT  = new Kind("perobject", 3);
	public static final Kind FROMSUPER  = new Kind("fromsuper", 4);
	public static final Kind PERTYPEWITHIN = new Kind("pertypewithin",5);

    public static class KindAnnotationPrefix extends TypeSafeEnum {
        private KindAnnotationPrefix(String name, int key) {
            super(name, key);
        }

        public String extractPointcut(String perClause) {
            int from = getName().length();
            int to = perClause.length()-1;
            if (!perClause.startsWith(getName())
 !perClause.endsWith(")")
 from > perClause.length()) {
                throw new RuntimeException("cannot read perclause " + perClause);
            }

            return perClause.substring(from, to);
        }

        public static final KindAnnotationPrefix PERCFLOW = new KindAnnotationPrefix("percflow(", 1);
        public static final KindAnnotationPrefix PERCFLOWBELOW = new KindAnnotationPrefix("percflowbelow(", 2);
        public static final KindAnnotationPrefix PERTHIS = new KindAnnotationPrefix("perthis(", 3);
        public static final KindAnnotationPrefix PERTARGET = new KindAnnotationPrefix("pertarget(", 4);
        public static final KindAnnotationPrefix PERTYPEWITHIN = new KindAnnotationPrefix("pertypewithin(", 5);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8097.java