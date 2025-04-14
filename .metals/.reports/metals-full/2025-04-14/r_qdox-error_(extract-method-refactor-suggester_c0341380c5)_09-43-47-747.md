error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2043.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2043.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2043.java
text:
```scala
public O@@bject accept(PatternNodeVisitor visitor, Object data) {

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

import org.aspectj.weaver.VersionedDataInputStream;

public class NamePattern extends PatternNode {
	char[] pattern;
	int starCount = 0;

	public static final NamePattern ELLIPSIS = new NamePattern("");
	public static final NamePattern ANY = new NamePattern("*");

	public NamePattern(String name) {
		this(name.toCharArray());
	}
	
	public NamePattern(char[] pattern) {
		this.pattern = pattern;
		
		for (int i=0, len=pattern.length; i<len; i++) {
			if (pattern[i] == '*') starCount++;
		}
	}
	
	public boolean matches(char[] a2) {
		char[] a1 = pattern;
		int len1 = a1.length;
		int len2 = a2.length;
		if (starCount == 0) {
			if (len1 != len2) return false;
			for (int i=0; i<len1; i++) {
				if (a1[i] != a2[i]) return false;
			}
			return true;
		} else if (starCount == 1) {
			// just '*' matches anything
			if (len1 == 1) return true;
			if (len1 > len2+1) return false;
			
			int i2=0;
			for (int i1=0; i1<len1; i1++) {
				char c1 = a1[i1];
				if (c1 == '*') {
					i2 = len2 - (len1-(i1+1));
				} else if (c1 != a2[i2++]) {
					return false;
				}
			}
			return true;
		} else {
//            String pattern = new String(a1);
//            String target = new String(a2);
//            System.err.print("match(\"" + pattern + "\", \"" + target + "\") -> ");
            boolean b = outOfStar(a1, a2, 0, 0, len1 - starCount, len2, starCount);
//            System.err.println(b);
            return b;
		}
    }
    private static boolean outOfStar(final char[] pattern, final char[] target, 
                                              int     pi,            int     ti, 
                                              int     pLeft,         int     tLeft,
                                       final int     starsLeft) {
        if (pLeft > tLeft) return false;
        while (true) {
            // invariant: if (tLeft > 0) then (ti < target.length && pi < pattern.length) 
            if (tLeft == 0) return true;
            if (pLeft == 0) {
                return (starsLeft > 0);  
            }
            if (pattern[pi] == '*') {
                return inStar(pattern, target, pi+1, ti, pLeft, tLeft, starsLeft-1);
            }
            if (target[ti] != pattern[pi]) {
                return false;
            }
            pi++; ti++; pLeft--; tLeft--;
        }
    }    
    private static boolean inStar(final char[] pattern, final char[] target, 
                                           int    pi,             int    ti, 
                                     final int    pLeft,          int    tLeft,
                                            int    starsLeft) {
        // invariant: pLeft > 0, so we know we'll run out of stars and find a real char in pattern
        char patternChar = pattern[pi];
        while (patternChar == '*') {
            starsLeft--;
            patternChar = pattern[++pi];
        }
        while (true) {
            // invariant: if (tLeft > 0) then (ti < target.length)
            if (pLeft > tLeft) return false;
            if (target[ti] == patternChar) {
                if (outOfStar(pattern, target, pi+1, ti+1, pLeft-1, tLeft-1, starsLeft)) return true;
            }
            ti++; tLeft--;
        }
    }    
       
	public boolean matches(String other) {
		return matches(other.toCharArray());
	}
	
	public String toString() {
		return new String(pattern);
	}
	
	public boolean equals(Object other) {
		if (other instanceof NamePattern) {
			NamePattern otherPat = (NamePattern)other;
			return otherPat.starCount == this.starCount &&
			     new String(otherPat.pattern).equals(new String(this.pattern));
		}
		return false;
	}
    public int hashCode() {
        return new String(pattern).hashCode();
    }
    
    
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(new String(pattern));
	}
	
	public static NamePattern read(VersionedDataInputStream in) throws IOException {
		String s = in.readUTF();
		if (s.length() == 0) return ELLIPSIS;
		return new NamePattern(s);
	}
	/**
	 * Method maybeGetSimpleName.
	 * @return String
	 */
	public String maybeGetSimpleName() {
		if (starCount == 0 && pattern.length > 0) return new String(pattern);
		return null;
	}

	/**
	 * Method isAny.
	 * @return boolean
	 */
	public boolean isAny() {
		return starCount == 1 && pattern.length == 1;
	}

    public Object accept(PointcutVisitor visitor, Object data) {
        return visitor.visit(this, data);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2043.java