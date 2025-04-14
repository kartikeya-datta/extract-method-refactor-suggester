error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2042.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2042.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2042.java
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
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.weaver.VersionedDataInputStream;

public class ModifiersPattern extends PatternNode {
	private int requiredModifiers;
	private int forbiddenModifiers;
	
	public static final ModifiersPattern ANY = new ModifiersPattern(0, 0);
	
	public ModifiersPattern(int requiredModifiers, int forbiddenModifiers) {
		this.requiredModifiers = requiredModifiers;
		this.forbiddenModifiers = forbiddenModifiers;
	}

	public String toString() {
		if (this == ANY) return "";
		
		String ret = Modifier.toString(requiredModifiers);
		if (forbiddenModifiers == 0) return ret;
		else return ret + " !" + Modifier.toString(forbiddenModifiers);
	}
    
	public boolean equals(Object other) {
		if (!(other instanceof ModifiersPattern)) return false;
		ModifiersPattern o = (ModifiersPattern)other;
		return o.requiredModifiers == this.requiredModifiers &&
				o.forbiddenModifiers == this.forbiddenModifiers;
	}
    public int hashCode() {
        int result = 17;
        result = 37*result + requiredModifiers;
        result = 37*result + forbiddenModifiers;
        return result;
    }	
    
	public boolean matches(int modifiers) {
		return ((modifiers & requiredModifiers) == requiredModifiers) &&
		        ((modifiers & forbiddenModifiers) == 0);
	}
	

	public static ModifiersPattern read(VersionedDataInputStream s) throws IOException {
		int requiredModifiers = s.readShort();
		int forbiddenModifiers = s.readShort();
		if (requiredModifiers == 0 && forbiddenModifiers == 0) return ANY;
		return new ModifiersPattern(requiredModifiers, forbiddenModifiers);
	}

	/**
	 * @see org.aspectj.weaver.patterns.PatternNode#write(DataOutputStream)
	 */
	public void write(DataOutputStream s) throws IOException {
		//s.writeByte(MODIFIERS_PATTERN);
		s.writeShort(requiredModifiers);
		s.writeShort(forbiddenModifiers);
	}
	
	
	private static Map modifierFlags = null;

	public static int getModifierFlag(String name) {
		if (modifierFlags == null) {
			modifierFlags = new HashMap();
			int flag = 1;
			while (flag <= Modifier.STRICT) {
				String flagName = Modifier.toString(flag);
				modifierFlags.put(flagName, new Integer(flag));
				flag = flag << 1;
			}
		}
		Integer flag = (Integer)modifierFlags.get(name);
		if (flag == null) return -1;
		return flag.intValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2042.java