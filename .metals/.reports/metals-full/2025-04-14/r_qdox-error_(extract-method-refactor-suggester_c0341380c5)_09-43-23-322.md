error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4776.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4776.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4776.java
text:
```scala
V@@ersionedDataInputStream in = new VersionedDataInputStream(bi);

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
import java.lang.reflect.*;

import org.aspectj.weaver.bcel.*;

import junit.framework.TestCase;
import org.aspectj.weaver.*;

public class ModifiersPatternTestCase extends TestCase {		
	/**
	 * Constructor for PatternTestCase.
	 * @param name
	 */
	public ModifiersPatternTestCase(String name) {
		super(name);
	}
	
	World world;
	
	public void testMatch() {
		world = new BcelWorld();
		
		int[] publicMatches = new int[] {
			Modifier.PUBLIC,
			Modifier.PUBLIC | Modifier.STATIC,
			Modifier.PUBLIC | Modifier.STATIC | Modifier.STRICT | Modifier.FINAL,
		};
		
		int[] publicFailures = new int[] {
			Modifier.PRIVATE,
			0,
			Modifier.STATIC | Modifier.STRICT | Modifier.FINAL,
		};
		
		int[] publicStaticMatches = new int[] {
			Modifier.PUBLIC | Modifier.STATIC,
			Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL | Modifier.STRICT,
		};
		
		int[] publicStaticFailures = new int[] {
			0,
			Modifier.PUBLIC,
			Modifier.STATIC,
		};
		
		int[] trickMatches = new int[] {
			Modifier.PRIVATE,
			Modifier.PRIVATE | Modifier.ABSTRACT,
			Modifier.PRIVATE | Modifier.FINAL,
		};
		
		int[] trickFailures = new int[] {
			Modifier.PUBLIC,
			Modifier.PRIVATE | Modifier.STATIC,
			Modifier.PRIVATE | Modifier.STRICT,
		};
		
		
		int[] none = new int[0];
		
		checkMatch("", publicMatches, none);
		checkMatch("", publicFailures, none);
		checkMatch("!public", publicFailures, publicMatches);
		checkMatch("public", publicMatches, publicFailures);
		checkMatch("public static", none, publicFailures);
		checkMatch("public static", publicStaticMatches, publicStaticFailures);
		
		checkMatch("private !static !strictfp", trickMatches, trickFailures);
		checkMatch("private !static !strictfp", none, publicMatches);
		checkMatch("private !static !strictfp", none, publicStaticMatches);
	}

	private ModifiersPattern makeModifiersPattern(String pattern) {
		return new PatternParser(pattern).parseModifiersPattern();
	}

	private void checkMatch(String pattern, int[] shouldMatch, int[] shouldFail) {
		ModifiersPattern p = makeModifiersPattern(pattern);
		checkMatch(p, shouldMatch, true);
		checkMatch(p, shouldFail, false);
	}
	
	private void checkMatch(ModifiersPattern p, int[] matches, boolean shouldMatch) {
		for (int i=0; i<matches.length; i++) {
			boolean result = p.matches(matches[i]);
			String msg = "matches " + p + " to " + Modifier.toString(matches[i]) + " expected ";
			if (shouldMatch) {
				assertTrue(msg + shouldMatch, result);
			} else {
				assertTrue(msg + shouldMatch, !result);
			}
		}
	}
		
	public void testSerialization() throws IOException {
		String[] patterns = new String[] {
			"", "!public", "public", "public static",
			"private !static !strictfp",
		};
		
		for (int i=0, len=patterns.length; i < len; i++) {
			checkSerialization(patterns[i]);
		}
	}

	/**
	 * Method checkSerialization.
	 * @param string
	 */
	private void checkSerialization(String string) throws IOException {
		ModifiersPattern p = makeModifiersPattern(string);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bo);
		p.write(out);
		out.close();
		
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		DataInputStream in = new DataInputStream(bi);
		ModifiersPattern newP = ModifiersPattern.read(in);
		
		assertEquals("write/read", p, newP);	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4776.java