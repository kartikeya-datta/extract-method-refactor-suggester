error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/345.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/345.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/345.java
text:
```scala
public F@@uzzyBoolean fastMatch(FastMatchInfo type) {

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;

/**
 * This is a kind of KindedPointcut.  This belongs either in 
 * a hierarchy with it or in a new place to share code
 * with other potential future statement-level pointcuts like
 * synchronized and throws
 */
public class HandlerPointcut extends Pointcut {
	TypePattern exceptionType;

	public HandlerPointcut(TypePattern exceptionType) {
		this.exceptionType = exceptionType;
	}

    public FuzzyBoolean fastMatch(ResolvedTypeX type) {
    	//??? should be able to do better by finding all referenced types in type
		return FuzzyBoolean.MAYBE;
	}
	
	public FuzzyBoolean match(Shadow shadow) {
		if (shadow.getKind() != Shadow.ExceptionHandler) return FuzzyBoolean.NO;
		
		// we know we have exactly one parameter since we're checking an exception handler
		return exceptionType.matches(
				shadow.getSignature().getParameterTypes()[0].resolve(shadow.getIWorld()), 
				TypePattern.STATIC);
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof HandlerPointcut)) return false;
		HandlerPointcut o = (HandlerPointcut)other;
		return o.exceptionType.equals(this.exceptionType);	}
    
    public int hashCode() {
        int result = 17;
        result = 37*result + exceptionType.hashCode();
        return result;
    }
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("handler(");
		buf.append(exceptionType.toString());
		buf.append(")");
		return buf.toString();
	}
	

	public void write(DataOutputStream s) throws IOException {
		s.writeByte(Pointcut.HANDLER);
		exceptionType.write(s);
		writeLocation(s);
	}
	
	public static Pointcut read(DataInputStream s, ISourceContext context) throws IOException {
		HandlerPointcut ret = new HandlerPointcut(TypePattern.read(s, context));
		ret.readLocation(context, s);
		return ret;
	}

	// XXX note: there is no namebinding in any kinded pointcut.
	// still might want to do something for better error messages
	// We want to do something here to make sure we don't sidestep the parameter
	// list in capturing type identifiers.
	public void resolveBindings(IScope scope, Bindings bindings) {
		exceptionType = exceptionType.resolveBindings(scope, bindings, false, false);
		//XXX add error if exact binding and not an exception
	}
	public Test findResidue(Shadow shadow, ExposedState state) {
		return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
	}
	
	public Pointcut concretize1(ResolvedTypeX inAspect, IntMap bindings) {
		return new HandlerPointcut(exceptionType);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/345.java