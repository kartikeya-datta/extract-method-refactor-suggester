error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4301.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4301.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4301.java
text:
```scala
T@@ypeX listOfStringType = TypeX.forParameterizedTypes(TypeX.forName("java/util/List"), new TypeX[] {stringType});

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


package org.aspectj.weaver;

import junit.framework.TestCase;

import org.aspectj.testing.util.TestUtil;

/**
 * This is a test case for all the portions of TypeX that don't require a world.
 */
public class TypeXTestCase extends TestCase {

    public TypeXTestCase(String name) {
        super(name);
    }

    public void testUnresolvedTypes() {
        // basic equality
        String[] testNames = 
            new String[] {"int", "long", "int[]", "boolean[][]", 
                           "java.lang.String", "java.lang.String[]", "void" };
        String[] testSigs = 
            new String[] {"I", "J", "[I", "[[Z", 
                            "Ljava/lang/String;", "[Ljava/lang/String;", "V" };
        
        String[] componentNames = 
            new String[] {null, null, "int", "boolean[]",
                            null, "java.lang.String", null };

        int[] sizes = new int[] {1, 2, 1, 1, 1, 1, 0};
        
        boolean[] isPrimitive = 
            new boolean[] { true, true, false, false, false, false, true };
                                  
        nameSignatureTest(testNames, testSigs);
        arrayTest(TypeX.forNames(testNames), componentNames);
        arrayTest(TypeX.forSignatures(testSigs), componentNames);

        sizeTest(TypeX.forNames(testNames), sizes);
        sizeTest(TypeX.forSignatures(testSigs), sizes);
        
        isPrimitiveTest(TypeX.forSignatures(testSigs), isPrimitive);        
    }
    
    public void testNameAndSigWithInners() {
    	TypeX t = TypeX.forName("java.util.Map$Entry");
    	assertEquals(t.getName(), "java.util.Map$Entry");
    	assertEquals(t.getSignature(), "Ljava/util/Map$Entry;");
    	assertEquals(t.getOutermostType(), TypeX.forName("java.util.Map"));
    	assertEquals(TypeX.forName("java.util.Map").getOutermostType(), TypeX.forName("java.util.Map"));
    }
	
	public void testNameAndSigWithParameters() {
		TypeX t = TypeX.forName("java.util.List<java.lang.String>");
		assertEquals(t.getName(),"java.util.List<java.lang.String>");
		assertEquals(t.getSignature(),"Ljava/util/List<Ljava/lang/String;>;");
		t = new TypeX("Ljava/util/List<Ljava/lang/String;>;");
		assertEquals(t.getName(),"java.util.List<java.lang.String>");
		assertEquals(t.getSignature(),"Ljava/util/List<Ljava/lang/String;>;");
		t = TypeX.forName("java.util.Map<java.util.String,java.util.List<java.lang.Integer>>");
		assertEquals(t.getName(),"java.util.Map<java.util.String,java.util.List<java.lang.Integer>>");
		assertEquals(t.getSignature(),"Ljava/util/Map<Ljava/util/String;Ljava/util/List<Ljava/lang/Integer;>;>;");
		t = new TypeX("Ljava/util/Map<Ljava/util/String;Ljava/util/List<Ljava/lang/Integer;>;>;");
		assertEquals(t.getName(),"java.util.Map<java.util.String,java.util.List<java.lang.Integer>>");
		assertEquals(t.getSignature(),"Ljava/util/Map<Ljava/util/String;Ljava/util/List<Ljava/lang/Integer;>;>;");
	}
	
	/**
	 * Verify TypeX signature processing creates the right kind of TypeX's from a signature.
	 * 
	 * For example, calling TypeX.dump() for 
	 *   "Ljava/util/Map<Ljava/util/List<Ljava/lang/String;>;Ljava/lang/String;>;"
	 * results in:
	 *   TypeX:  signature=Ljava/util/Map<Ljava/util/List<Ljava/lang/String;>;Ljava/lang/String;>; parameterized=true #params=2
     *     TypeX:  signature=Ljava/util/List<Ljava/lang/String;>; parameterized=true #params=1
     *       TypeX:  signature=Ljava/lang/String; parameterized=false #params=0
     *     TypeX:  signature=Ljava/lang/String; parameterized=false #params=0
	 */
	public void testTypexGenericSignatureProcessing() {
		TypeX tx = null;
		
		tx = new TypeX("Ljava/util/Set<Ljava/lang/String;>;");
		checkTX(tx,true,1);
		
		tx = new TypeX("Ljava/util/Set<Ljava/util/List<Ljava/lang/String;>;>;");
		checkTX(tx,true,1);
		
		tx = new TypeX("Ljava/util/Map<Ljava/util/List<Ljava/lang/String;>;Ljava/lang/String;>;");
		checkTX(tx,true,2);
		checkTX(tx.getTypeParameters()[0],true,1);
		checkTX(tx.getTypeParameters()[1],false,0);
//		System.err.println(tx.dump());
	}
	
	public void testTypeXForParameterizedTypes() {
		TypeX stringType = TypeX.forName("java/lang/String");
		TypeX listOfStringType = TypeX.forParameterizedTypes("java/util/List", new TypeX[] {stringType});
		assertEquals("1 type param",1,listOfStringType.typeParameters.length);
		assertEquals(stringType,listOfStringType.typeParameters[0]);
		assertTrue(listOfStringType.isParameterized());
		assertFalse(listOfStringType.isGeneric());
	}
	
	private void checkTX(TypeX tx,boolean shouldBeParameterized,int numberOfTypeParameters) {
		assertTrue("Expected parameterization flag to be "+shouldBeParameterized,tx.isParameterized()==shouldBeParameterized);
		if (numberOfTypeParameters==0) {
			TypeX[] params = tx.getTypeParameters();
			assertTrue("Expected 0 type parameters but found "+params.length,params==null || params.length==0);
	    } else {
				assertTrue("Expected #type parameters to be "+numberOfTypeParameters,tx.getTypeParameters().length==numberOfTypeParameters);
	    }
	}
	

    private void isPrimitiveTest(TypeX[] types, boolean[] isPrimitives) {
        for (int i = 0, len = types.length; i < len; i++) {
            TypeX type = types[i];
            boolean b = isPrimitives[i];
            assertEquals(type + " is primitive: ", b, type.isPrimitive());
        }           
    }

    private void sizeTest(TypeX[] types, int[] sizes) {
        for (int i = 0, len = types.length; i < len; i++) {
            TypeX type = types[i];
            int size = sizes[i];
            assertEquals("size of " + type + ": ", size, type.getSize());
        }           
    }

    private void arrayTest(TypeX[] types, String[] components) {
        for (int i = 0, len = types.length; i < len; i++) {
            TypeX type = types[i];
            String component = components[i];
            assertEquals(type + " is array: ", component != null, type.isArray());
            if (component != null) 
                assertEquals(type + " componentType: ", component, 
                    type.getComponentType().getName());
        }                
    }

    private void nameSignatureTest(String[] ns, String[] ss) {
        for (int i = 0, len = ns.length; i < len; i++) {
            String n = ns[i];
            String s = ss[i];
            TypeX tn = TypeX.forName(n);
            TypeX ts = TypeX.forSignature(s);

            assertEquals("forName(n).getName()", n, 
                tn.getName());
            assertEquals("forSignature(s).getSignature()", s, 
                ts.getSignature());
            assertEquals("forName(n).getSignature()", s, 
                tn.getSignature());
            assertEquals("forSignature(n).getName()", n, 
                ts.getName());
                
            TestUtil.assertCommutativeEquals(tn, tn, true);
            TestUtil.assertCommutativeEquals(ts, ts, true);
            TestUtil.assertCommutativeEquals(tn, ts, true);
            
            for (int j = 0; j < len; j++) {
                if (i == j) continue;
                TypeX tn1 = TypeX.forName(ns[j]);
                TypeX ts1 = TypeX.forSignature(ss[j]); 
                TestUtil.assertCommutativeEquals(tn, tn1, false);
                TestUtil.assertCommutativeEquals(ts, tn1, false);
                TestUtil.assertCommutativeEquals(tn, ts1, false);
                TestUtil.assertCommutativeEquals(ts, ts1, false);
            }                               
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4301.java