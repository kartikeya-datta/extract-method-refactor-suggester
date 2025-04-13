error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/781.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/781.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/781.java
text:
```scala
i@@f (exactType.isMissing()) continue;

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
import java.util.List;
import java.util.Map;

import org.aspectj.bridge.IMessage;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;

public class DeclarePrecedence extends Declare {
	private TypePatternList patterns;
	

	public DeclarePrecedence(List patterns) {
		this(new TypePatternList(patterns));
	}
	
	private DeclarePrecedence(TypePatternList patterns) {
		this.patterns = patterns;
	}
	
	public Object accept(PatternNodeVisitor visitor, Object data) {
		return visitor.visit(this,data);
	}
	
	public Declare parameterizeWith(Map typeVariableBindingMap) {
		DeclarePrecedence ret = new DeclarePrecedence(this.patterns.parameterizeWith(typeVariableBindingMap));
		ret.copyLocationFrom(this);
		return ret;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("declare precedence: ");
		buf.append(patterns);
		buf.append(";");
		return buf.toString();
	}
	
	public boolean equals(Object other) { 
		if (!(other instanceof DeclarePrecedence)) return false;
		DeclarePrecedence o = (DeclarePrecedence)other;
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
	
	public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		Declare ret = new DeclarePrecedence(TypePatternList.read(s, context));
		ret.readLocation(context, s);
		return ret;
	}
	
    public void resolve(IScope scope) {
    	patterns = patterns.resolveBindings(scope, Bindings.NONE, false, false); 
    	boolean seenStar = false;
    	
    	for (int i=0; i < patterns.size(); i++) {
    		TypePattern pi = patterns.get(i);
    		if (pi.isStar()) {
    			if (seenStar) {
    				scope.getWorld().showMessage(IMessage.ERROR,
    						WeaverMessages.format(WeaverMessages.TWO_STARS_IN_PRECEDENCE),
							pi.getSourceLocation(), null);    				
    			}
    			seenStar = true;
    			continue;
    		}
    		ResolvedType exactType = pi.getExactType().resolve(scope.getWorld());
    		if (exactType == ResolvedType.MISSING) continue;
    		
    		// Cannot do a dec prec specifying a non-aspect types unless suffixed with a '+'
    		if (!exactType.isAspect() && !pi.isIncludeSubtypes() && !exactType.isTypeVariableReference()) {
    			scope.getWorld().showMessage(IMessage.ERROR,
    					WeaverMessages.format(WeaverMessages.CLASSES_IN_PRECEDENCE,exactType.getName()),
						pi.getSourceLocation(),null);
    		}
    		
    		for (int j=0; j < patterns.size(); j++) {
    			if (j == i) continue;
    			TypePattern pj = patterns.get(j);
    			if (pj.isStar()) continue;
    			if (pj.matchesStatically(exactType)) {
    				scope.getWorld().showMessage(IMessage.ERROR,
    						WeaverMessages.format(WeaverMessages.TWO_PATTERN_MATCHES_IN_PRECEDENCE,exactType.getName()), 
							pi.getSourceLocation(), pj.getSourceLocation());
    			}
    		}
    	}    	
    }

	public TypePatternList getPatterns() {
		return patterns;
	}

	private int matchingIndex(ResolvedType a) {
		int knownMatch = -1;
		int starMatch = -1;
		for (int i=0, len=patterns.size(); i < len; i++) {
			TypePattern p = patterns.get(i);
			if (p.isStar()) {
				starMatch = i;
			} else if (p.matchesStatically(a)) {
				if (knownMatch != -1) {
					a.getWorld().showMessage(IMessage.ERROR,
							WeaverMessages.format(WeaverMessages.MULTIPLE_MATCHES_IN_PRECEDENCE,a,patterns.get(knownMatch),p),
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
	

	public int compare(ResolvedType aspect1, ResolvedType aspect2) {
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

	public String getNameSuffix() {
		return "precedence";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/781.java