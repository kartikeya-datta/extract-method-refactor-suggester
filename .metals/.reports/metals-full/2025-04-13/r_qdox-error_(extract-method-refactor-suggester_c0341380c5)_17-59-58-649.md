error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4778.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4778.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4778.java
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
import java.util.Arrays;

import org.aspectj.weaver.bcel.*;
import org.aspectj.util.*;

import junit.framework.TestCase;
import org.aspectj.weaver.*;

/**
 * @author hugunin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TypePatternListTestCase extends TestCase {		
	/**
	 * Constructor for PatternTestCase.
	 * @param name
	 */
	public TypePatternListTestCase(String name) {
		super(name);
	}
	
	World world;
	
	//XXX when instanceof matching works add tests for that here
	
	
	public void testMatch() {
		world = new BcelWorld();
        
        checkStaticMatch("()", new String[] {}, FuzzyBoolean.YES);
        checkStaticMatch("()", new String[] {"java.lang.Object"}, FuzzyBoolean.NO);
       
        checkStaticMatch("(java.lang.Object)", new String[] {"java.lang.Object"}, FuzzyBoolean.YES);

        checkStaticMatch("(java.lang.String)", new String[] {"java.lang.Object"}, FuzzyBoolean.NO);

        checkStaticMatch("(java.lang.Object)", new String[] {"java.lang.String"}, FuzzyBoolean.NO);

        checkStaticMatch("()", new String[] {"java.lang.Object"}, FuzzyBoolean.NO);
        
        checkStaticMatch("(..)", new String[] {}, FuzzyBoolean.YES);
        checkStaticMatch("(..)", new String[] {"int", "char"}, FuzzyBoolean.YES);


        checkStaticMatch("(int,..,int)", new String[] {"int", "int"}, FuzzyBoolean.YES);

        checkStaticMatch("(int,..)", new String[] {}, FuzzyBoolean.NO);
        checkStaticMatch("(int,..)", new String[] {"int"}, FuzzyBoolean.YES);
         
        checkStaticMatch("(..,int,..)", new String[] {"int"}, FuzzyBoolean.YES);

        // these checks are taken from new/ExpandedDotPattern.java
        stupidCheck("( ..,  ..,  ..)", new boolean[] { true,  true,  true, true,  true });
        stupidCheck("( ..,  .., int)", new boolean[] { false, true,  true,  true,  true });
        stupidCheck("( .., int,  ..)", new boolean[] { false, true,  true,  true,  true });
        stupidCheck("( .., int, int)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("(int,  ..,  ..)", new boolean[] { false, true,  true,  true,  true });
        stupidCheck("(int,  .., int)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("(int, int,  ..)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("(int, int, int)", new boolean[] { false, false, false, true,  false });
        
        stupidCheck("( ..,  ..,  ..,  ..)", new boolean[] { true,  true,  true,  true,  true });
        stupidCheck("( ..,  ..,  .., int)", new boolean[] { false, true,  true,  true,  true });
        stupidCheck("( ..,  .., int,  ..)", new boolean[] { false, true,  true,  true,  true });
        stupidCheck("( ..,  .., int, int)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("( .., int,  ..,  ..)", new boolean[] { false, true,  true,  true,  true });
        stupidCheck("( .., int,  .., int)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("( .., int, int,  ..)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("( .., int, int, int)", new boolean[] { false, false, false, true,  true });
               
        stupidCheck("(int,  ..,  ..,  ..)", new boolean[] { false, true,  true,  true,  true });
        stupidCheck("(int,  ..,  .., int)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("(int,  .., int,  ..)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("(int,  .., int, int)", new boolean[] { false, false, false, true,  true });
        stupidCheck("(int, int,  ..,  ..)", new boolean[] { false, false, true,  true,  true });
        stupidCheck("(int, int,  .., int)", new boolean[] { false, false, false, true,  true });
        stupidCheck("(int, int, int,  ..)", new boolean[] { false, false, false, true,  true });
        stupidCheck("(int, int, int, int)", new boolean[] { false, false, false, false, true });	
	}

	private TypePatternList makeArgumentsPattern(String pattern) {
		return new PatternParser(pattern).parseArgumentsPattern();
	}

	private void checkStaticMatch(String pattern, String[] names, 
                            FuzzyBoolean shouldMatchStatically) {
        // We're only doing TypePattern.STATIC matching here because my intent was
        // to test the wildcarding, and we don't do DYNAMIC matching on wildcarded things.                        
                                
		TypePatternList p = makeArgumentsPattern(pattern);
        ResolvedTypeX[] types = new ResolvedTypeX[names.length];
        for (int i = 0; i < names.length; i++) {
            types[i] = world.resolve(names[i]);
        }
        
        p.resolveBindings(makeTestScope(), Bindings.NONE, false, false);
		//System.out.println("type: " + type);
		FuzzyBoolean result = p.matches(types, TypePattern.STATIC);
		String msg = "matches statically " + pattern + " to " + Arrays.asList(types);
        assertEquals(msg, shouldMatchStatically, result);       
	}
	
	private TestScope makeTestScope() {
		TestScope scope = new TestScope(CollectionUtil.NO_STRINGS, CollectionUtil.NO_STRINGS, world);
		return scope;
	}
	    
    public void stupidCheck(String pattern, boolean[] matches) {
        TypePatternList p = makeArgumentsPattern(pattern);
        p.resolveBindings(makeTestScope(), Bindings.NONE, false, false);
        
        int len = matches.length;
        
        for (int j = 0; j < len; j++) {
            
            ResolvedTypeX[] types = new ResolvedTypeX[j];
            for (int i = 0; i < j; i++) {
                types[i] = world.resolve("int");
            }
       
            FuzzyBoolean result = p.matches(types, TypePattern.STATIC);
            String msg = "matches statically " + pattern + " to " + Arrays.asList(types);
            assertEquals(msg, FuzzyBoolean.fromBoolean(matches[j]), result);   
        }
    }     
    
	public void testSerialization() throws IOException {
		String[] patterns = new String[] {
            "( ..,  ..,  .., int)", 
            "( ..,  .., int,  ..)", 
            "( ..,  .., int, int)", 
            "( .., int,  ..,  ..)", 
            "( .., int,  .., int)", 
            "( .., int, int,  ..)", 
            "( .., int, int, int)", 
            
            "(int,  ..,  ..,  ..)", 
            "(int,  ..,  .., int)", 
            "(int,  .., int,  ..)", 
            "(int,  .., int, int)", 
            "(int, int,  ..,  ..)", 
            "(int, int,  .., int)", 
            "(int, int, int,  ..)", 
            "(int, int, int, int)"
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
		TypePatternList p = makeArgumentsPattern(string);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bo);
		p.write(out);
		out.close();
		
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		DataInputStream in = new DataInputStream(bi);
		TypePatternList newP = TypePatternList.read(in, null);
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4778.java