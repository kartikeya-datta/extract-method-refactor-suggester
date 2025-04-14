error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4790.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4790.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4790.java
text:
```scala
t@@ype = type.resolveBindings(scope, bindings, false, false);

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
 * ******************************************************************/


package org.aspectj.weaver.patterns;

import java.io.*;

import org.aspectj.weaver.*;
import org.aspectj.weaver.ast.*;
import org.aspectj.util.*;

public class WithinPointcut extends Pointcut {
	TypePattern type;
	
	public WithinPointcut(TypePattern type) {
		this.type = type;
	}
    
	public FuzzyBoolean match(Shadow shadow) {
		TypeX enclosingType = shadow.getEnclosingType();
		while (enclosingType != null) {
			if (type.matchesStatically(shadow.getIWorld().resolve(enclosingType))) {
				return FuzzyBoolean.YES;
			}
			enclosingType = enclosingType.getDeclaringType();
		}
		return FuzzyBoolean.NO;
	}

	public void write(DataOutputStream s) throws IOException {
		s.writeByte(Pointcut.WITHIN);
		type.write(s);
		writeLocation(s);
	}
	public static Pointcut read(DataInputStream s, ISourceContext context) throws IOException {
		TypePattern type = TypePattern.read(s, context);
		WithinPointcut ret = new WithinPointcut(type);
		ret.readLocation(context, s);
		return ret;
	}

	public void resolveBindings(IScope scope, Bindings bindings) {
		type = type.resolveBindings(scope, bindings, false);
	}

	public void postRead(ResolvedTypeX enclosingType) {
		type.postRead(enclosingType);
	}

	public boolean equals(Object other) {
		if (!(other instanceof WithinPointcut)) return false;
		WithinPointcut o = (WithinPointcut)other;
		return o.type.equals(this.type);
	}
    public int hashCode() {
        int result = 43;
        result = 37*result + type.hashCode();
        return result;
    }

	public String toString() {
		return "within(" + type + ")";
	}

	public Test findResidue(Shadow shadow, ExposedState state) {
		return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
	}
	
	
	public Pointcut concretize1(ResolvedTypeX inAspect, IntMap bindings) {
		return this; //??? no pointers out of here so we're okay
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4790.java