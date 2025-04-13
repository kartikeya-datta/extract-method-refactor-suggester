error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/782.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/782.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/782.java
text:
```scala
i@@f (!excType.isMissing()) {

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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.aspectj.bridge.IMessage;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;

public class DeclareSoft extends Declare {
	private TypePattern exception;
	private Pointcut pointcut;

	public DeclareSoft(TypePattern exception, Pointcut pointcut) {
		this.exception = exception;
		this.pointcut = pointcut;
	}
	
	public Object accept(PatternNodeVisitor visitor, Object data) {
		return visitor.visit(this,data);
	}
	
	public Declare parameterizeWith(Map typeVariableBindingMap) {
		DeclareSoft ret = 
			new DeclareSoft(
					exception.parameterizeWith(typeVariableBindingMap),
					pointcut.parameterizeWith(typeVariableBindingMap));
		ret.copyLocationFrom(this);
		return ret;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("declare soft: ");
		buf.append(exception);
		buf.append(": ");
		buf.append(pointcut);
		buf.append(";");
		return buf.toString();
	}
	
	public boolean equals(Object other) { 
		if (!(other instanceof DeclareSoft)) return false;
		DeclareSoft o = (DeclareSoft)other;
		return
			o.pointcut.equals(pointcut) &&
			o.exception.equals(exception);
	}
    
    public int hashCode() {
        int result = 19;
        result = 37*result + pointcut.hashCode();
        result = 37*result + exception.hashCode();
        return result;
    }


	public void write(DataOutputStream s) throws IOException {
		s.writeByte(Declare.SOFT);
		exception.write(s);
		pointcut.write(s);
		writeLocation(s);
	}

	public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		Declare ret = new DeclareSoft(
			TypePattern.read(s, context),
			Pointcut.read(s, context)
		);
		ret.readLocation(context, s);
		return ret;
	}

	public Pointcut getPointcut() {
		return pointcut;
	}
	
	public TypePattern getException() {
		return exception;
	}

    public void resolve(IScope scope) {
    	exception = exception.resolveBindings(scope, null, false, true);
    	ResolvedType excType = exception.getExactType().resolve(scope.getWorld());
    	if (excType != ResolvedType.MISSING) {
    		if (excType.isTypeVariableReference()) {
    			// a declare soft in a generic abstract aspect, we need to check the upper bound
    			excType = excType.getUpperBound().resolve(scope.getWorld());
    		}
    		if (!scope.getWorld().getCoreType(UnresolvedType.THROWABLE).isAssignableFrom(excType)) {
    			scope.getWorld().showMessage(IMessage.ERROR,
    					WeaverMessages.format(WeaverMessages.NOT_THROWABLE,excType.getName()),
    					exception.getSourceLocation(), null);
    			pointcut = Pointcut.makeMatchesNothing(Pointcut.RESOLVED);
    			return;
    		}
	        // ENH 42743 suggests that we don't soften runtime exceptions.
			if (scope.getWorld().getCoreType(UnresolvedType.RUNTIME_EXCEPTION).isAssignableFrom(excType)) {
			    scope.getWorld().getLint().runtimeExceptionNotSoftened.signal(
			      		new String[]{excType.getName()},
			      		exception.getSourceLocation(),null);
				pointcut = Pointcut.makeMatchesNothing(Pointcut.RESOLVED);
				return;
			}			
    	}
    	
    	pointcut = pointcut.resolve(scope); 	
    }
    
    public boolean isAdviceLike() {
		return true;
	}
	
	public String getNameSuffix() {
		return "soft";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/782.java