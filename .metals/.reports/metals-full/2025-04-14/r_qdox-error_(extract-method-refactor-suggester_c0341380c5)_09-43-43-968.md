error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7607.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7607.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7607.java
text:
```scala
b@@uf.append("declare precedence: ");

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

import java.io.*;
import java.util.List;

import org.aspectj.bridge.IMessage;
import org.aspectj.weaver.*;
import org.aspectj.weaver.ResolvedTypeX;

public class DeclareDominates extends Declare {
	private TypePatternList patterns;
	

	public DeclareDominates(List patterns) {
		this(new TypePatternList(patterns));
	}
	
	private DeclareDominates(TypePatternList patterns) {
		this.patterns = patterns;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("declare dominates: ");
		buf.append(patterns);
		buf.append(";");
		return buf.toString();
	}
	
	public boolean equals(Object other) { 
		if (!(other instanceof DeclareDominates)) return false;
		DeclareDominates o = (DeclareDominates)other;
		return o.patterns.equals(patterns);
	}
    
    public int hashCode() {
    	return patterns.hashCode();
    }


	public void write(DataOutputStream s) throws IOException {
		s.writeByte(Declare.DOMINATES);
		patterns.write(s);
		writeLocation(s);
	}

	public static Declare read(DataInputStream s, ISourceContext context) throws IOException {
		Declare ret = new DeclareDominates(TypePatternList.read(s, context));
		ret.readLocation(context, s);
		return ret;
	}
	
    public void resolve(IScope scope) {
    	patterns = patterns.resolveBindings(scope, Bindings.NONE, false, false); 
    	
    	for (int i=0; i < patterns.size(); i++) {
    		TypePattern pi = patterns.get(i);
    		if (pi.isStar()) continue;
    		ResolvedTypeX exactType = pi.getExactType().resolve(scope.getWorld());
    		if (exactType == ResolvedTypeX.MISSING) continue;
    		for (int j=0; j < patterns.size(); j++) {
    			if (j == i) continue;
    			TypePattern pj = patterns.get(j);
    			if (pj.isStar()) continue;
    			if (pj.matchesStatically(exactType)) {
    				scope.getWorld().showMessage(IMessage.ERROR,
    					"circularity in declare dominates, '" + exactType.getName() + 
    						"' matches two patterns", pi.getSourceLocation(), pj.getSourceLocation());
    			}
    		}
    	}    	
    }

	public TypePatternList getPatterns() {
		return patterns;
	}

	private int matchingIndex(ResolvedTypeX a) {
		int knownMatch = -1;
		int starMatch = -1;
		for (int i=0, len=patterns.size(); i < len; i++) {
			TypePattern p = patterns.get(i);
			if (p.isStar()) {
				starMatch = i;
			} else if (p.matchesStatically(a)) {
				if (knownMatch != -1) {
					a.getWorld().showMessage(IMessage.ERROR, "multiple matches for " + a + 
							", matches both " + patterns.get(knownMatch) + " and " + p,
							patterns.get(knownMatch).getSourceLocation(), p.getSourceLocation());
					return -1;
				} else {
					knownMatch = i;
				}
			}
		}
		if (knownMatch == -1) return starMatch;
		else return knownMatch;
	}
	

	public int compare(ResolvedTypeX aspect1, ResolvedTypeX aspect2) {
		int index1 = matchingIndex(aspect1);
		int index2 = matchingIndex(aspect2);
		
		//System.out.println("a1: " + aspect1 + ", " + aspect2 + " = " + index1 + ", " + index2);
		
		if (index1 == -1 || index2 == -1) return 0;
		
		if (index1 == index2) return 0;
		else if (index1 > index2) return -1;
		else return +1;
	}
	
	public boolean isAdviceLike() {
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7607.java