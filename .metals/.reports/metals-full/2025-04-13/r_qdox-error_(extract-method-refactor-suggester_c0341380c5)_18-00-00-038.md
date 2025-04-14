error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1067.java
text:
```scala
i@@f (typePattern.matchesStatically(types[i])) return true;

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

import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.World;


public class ThrowsPattern extends PatternNode {
	private TypePatternList required;
	private TypePatternList forbidden;
	
	public static final ThrowsPattern ANY =
		new ThrowsPattern(TypePatternList.EMPTY, TypePatternList.EMPTY);
	
	public ThrowsPattern(TypePatternList required, TypePatternList forbidden) {
		this.required = required;
		this.forbidden = forbidden;
	}

	public String toString() {
		if (this == ANY) return "";
		
		
		String ret = "throws " + required.toString();
		if (forbidden.size() > 0) {
			ret = ret + " !(" + forbidden.toString() + ")";
		}
		return ret;
	}
    
	public boolean equals(Object other) {
		if (!(other instanceof ThrowsPattern)) return false;
		ThrowsPattern o = (ThrowsPattern)other;
		return o.required.equals(this.required) &&
				o.forbidden.equals(this.forbidden);
	}
    public int hashCode() {
        int result = 17;
        result = 37*result + required.hashCode();
        result = 37*result + forbidden.hashCode();
        return result;
    }	
    
    public ThrowsPattern resolveBindings(IScope scope, Bindings bindings) {
    	if (this == ANY) return this;
    	required = required.resolveBindings(scope, bindings, false, false);
    	forbidden = forbidden.resolveBindings(scope, bindings, false, false);
    	return this;
    }
    
	public boolean matches(TypeX[] tys, World world) {
		if (this == ANY) return true;
		
		//System.out.println("matching: " + this + " with " + Arrays.asList(tys));
		
		ResolvedTypeX[] types = world.resolve(tys);
		int len = types.length;
		for (int j=0, lenj = required.size(); j < lenj; j++) {
			if (! matchesAny(required.get(j), types)) {
				return false;
			}
		}
		for (int j=0, lenj = forbidden.size(); j < lenj; j++) {
			if (matchesAny(forbidden.get(j), types)) {
				return false;
			}
		}
		return true;
	}

	private boolean matchesAny(
		TypePattern typePattern,
		ResolvedTypeX[] types) 
	{
		for (int i = types.length - 1; i >= 0; i--) {
			if (typePattern.matchesExactly(types[i])) return true;	
		}
		return false;
	}

	public static ThrowsPattern read(DataInputStream s, ISourceContext context) throws IOException {
		TypePatternList required = TypePatternList.read(s, context);
		TypePatternList forbidden = TypePatternList.read(s, context);
		if (required.size() == 0 && forbidden.size() == 0) return ANY;
		ThrowsPattern ret = new ThrowsPattern(required, forbidden);
		//XXXret.readLocation(context, s);
		return ret;
	}

	public void write(DataOutputStream s) throws IOException {
		required.write(s);
		forbidden.write(s);
		//XXXwriteLocation(s);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1067.java