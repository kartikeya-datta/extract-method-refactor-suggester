error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12751.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12751.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12751.java
text:
```scala
r@@eturn FuzzyBoolean.MAYBE;  // could still match at runtime

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * ******************************************************************/
package org.aspectj.weaver.patterns;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.World;

/**
 * @author colyer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnnotationPatternList extends PatternNode {

	private AnnotationTypePattern[] typePatterns;
	int ellipsisCount = 0;
	
	public static final AnnotationPatternList EMPTY =
		new AnnotationPatternList(new AnnotationTypePattern[] {});
	
	public static final AnnotationPatternList ANY =
	    new AnnotationPatternList(new AnnotationTypePattern[] {AnnotationTypePattern.ELLIPSIS});
	
	public AnnotationPatternList() {
		typePatterns = new AnnotationTypePattern[0];
		ellipsisCount = 0;
	}

	public AnnotationPatternList(AnnotationTypePattern[] arguments) {
		this.typePatterns = arguments;
		for (int i=0; i<arguments.length; i++) {
			if (arguments[i] == AnnotationTypePattern.ELLIPSIS) ellipsisCount++;
		}
	}
	
	public AnnotationPatternList(List l) {
		this((AnnotationTypePattern[]) l.toArray(new AnnotationTypePattern[l.size()]));
	}

	public void resolve(World inWorld) {
		for (int i = 0; i < typePatterns.length; i++) {
			typePatterns[i].resolve(inWorld);
		}
	}
	
	public FuzzyBoolean matches(ResolvedTypeX[] someArgs) {
		// do some quick length tests first
		int numArgsMatchedByEllipsis = (someArgs.length + ellipsisCount) - typePatterns.length;
		if (numArgsMatchedByEllipsis < 0) return FuzzyBoolean.NO;
		if ((numArgsMatchedByEllipsis > 0) && (ellipsisCount == 0)) {
			return FuzzyBoolean.NO;
		}
		// now work through the args and the patterns, skipping at ellipsis
    	FuzzyBoolean ret = FuzzyBoolean.YES;
    	int argsIndex = 0;
    	for (int i = 0; i < typePatterns.length; i++) {
			if (typePatterns[i] == AnnotationTypePattern.ELLIPSIS) {
				// match ellipsisMatchCount args
				argsIndex += numArgsMatchedByEllipsis;
			} else if (typePatterns[i] == AnnotationTypePattern.ANY) {
				argsIndex++;
			} else {
				// match the argument type at argsIndex with the ExactAnnotationTypePattern
				// we know it is exact because nothing else is allowed in args
				ExactAnnotationTypePattern ap = (ExactAnnotationTypePattern)typePatterns[i];
				FuzzyBoolean matches = ap.matches(someArgs[argsIndex]);
				if (matches == FuzzyBoolean.NO) {
					return FuzzyBoolean.NO;
				} else {
					argsIndex++;
					ret = ret.and(matches);
				}
			}
		}   	
    	return ret;
	}
	
	public int size() { return typePatterns.length; }
	
	public AnnotationTypePattern get(int index) {
		return typePatterns[index];
	}

	public AnnotationPatternList resolveBindings(IScope scope, Bindings bindings, boolean allowBinding) {
		for (int i=0; i<typePatterns.length; i++) {
			AnnotationTypePattern p = typePatterns[i];
			if (p != null) {
				typePatterns[i] = typePatterns[i].resolveBindings(scope, bindings, allowBinding);
			}
		}
		return this;
	}
	
	public AnnotationPatternList resolveReferences(IntMap bindings) {
		int len = typePatterns.length;
		AnnotationTypePattern[] ret = new AnnotationTypePattern[len];
		for (int i=0; i < len; i++) {
			ret[i] = typePatterns[i].remapAdviceFormals(bindings);
		}
		return new AnnotationPatternList(ret);
	}

    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	buf.append("(");
    	for (int i=0, len=typePatterns.length; i < len; i++) {
    		AnnotationTypePattern type = typePatterns[i];
    		if (i > 0) buf.append(", ");
    		if (type == AnnotationTypePattern.ELLIPSIS) {
    			buf.append("..");
    		} else {
    			buf.append(type.toString());
    		}
    	}
    	buf.append(")");
    	return buf.toString();
    }
    
	public boolean equals(Object other) {
		if (!(other instanceof AnnotationPatternList)) return false;
		AnnotationPatternList o = (AnnotationPatternList)other;
		int len = o.typePatterns.length;
		if (len != this.typePatterns.length) return false;
		for (int i=0; i<len; i++) {
			if (!this.typePatterns[i].equals(o.typePatterns[i])) return false;
		}
		return true;
	}
	
    public int hashCode() {
        int result = 41;
        for (int i = 0, len = typePatterns.length; i < len; i++) {
            result = 37*result + typePatterns[i].hashCode();
        }
        return result;
    }
    
	public static AnnotationPatternList read(DataInputStream s, ISourceContext context) throws IOException  {
		short len = s.readShort();
		AnnotationTypePattern[] arguments = new AnnotationTypePattern[len];
		for (int i=0; i<len; i++) {
			arguments[i] = AnnotationTypePattern.read(s, context);
		}
		AnnotationPatternList ret = new AnnotationPatternList(arguments);
		ret.readLocation(context, s);
		return ret;
	}


	public void write(DataOutputStream s) throws IOException {
		s.writeShort(typePatterns.length);
		for (int i=0; i<typePatterns.length; i++) {
			typePatterns[i].write(s);
		}
		writeLocation(s);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12751.java