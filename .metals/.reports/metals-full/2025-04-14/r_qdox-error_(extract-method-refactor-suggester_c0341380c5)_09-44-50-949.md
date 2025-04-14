error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6286.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6286.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6286.java
text:
```scala
S@@tring expectedSignature = "java.lang.Object[] java.util.Collection.toArray(java.lang.Object[])";

package org.aspectj.weaver;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegateTest;

public class TestJava5ReflectionBasedReferenceTypeDelegate extends ReflectionBasedReferenceTypeDelegateTest {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("TestJava5ReflectionBasedReferenceTypeDelegate");
		suite.addTestSuite(TestJava5ReflectionBasedReferenceTypeDelegate.class);
		return suite;
	}
	
	
	/**
	 * Let's play about with a generic type and ensure we can work with it in a reflective world.
	 */
	public void testResolveGeneric() {
		UnresolvedType collectionType = UnresolvedType.forName("java.util.Collection");
		world.resolve(collectionType).getRawType().resolve(world);
		ResolvedMember[] methods = world.resolve(collectionType).getDeclaredMethods();
		int i = findMethod("toArray", 1, methods);
		assertTrue("Couldn't find 'toArray' in the set of methods? "+methods,i != -1);
		String expectedSignature = "T[] java.util.Collection.toArray(T[])";
		assertTrue("Expected signature of '"+expectedSignature+"' but it was '"+methods[i],methods[i].toString().equals(expectedSignature));
	}

	/**
	 * Can we resolve the dreaded Enum type...
	 */
	public void testResolveEnum() {
		ResolvedType enumType = world.resolve("java.lang.Enum");
		assertTrue("Should be the raw type but is "+enumType.typeKind,enumType.isRawType());
		ResolvedType theGenericEnumType = enumType.getGenericType();
		assertTrue("Should have a type variable ",theGenericEnumType.getTypeVariables().length>0);
		TypeVariable tv = theGenericEnumType.getTypeVariables()[0];
		String expected = "TypeVar E extends java.lang.Enum<E>";
		assertTrue("Type variable should be '"+expected+"' but is '"+tv+"'",tv.toString().equals(expected));
	}
	
	public void testResolveClass() {
		world.resolve("java.lang.Class").getGenericType();		
	}
	
    public void testGenericInterfaceSuperclass_ReflectionWorldResolution() {
        
        UnresolvedType javaUtilMap = UnresolvedType.forName("java.util.Map");
        
        ReferenceType rawType = (ReferenceType) world.resolve(javaUtilMap);
        assertTrue("Should be the raw type ?!? "+rawType.getTypekind(),rawType.isRawType());
        
        ReferenceType genericType = (ReferenceType)rawType.getGenericType();
        assertTrue("Should be the generic type ?!? "+genericType.getTypekind(),genericType.isGenericType());
        
        ResolvedType rt = rawType.getSuperclass();
        assertTrue("Superclass for Map raw type should be Object but was "+rt,rt.equals(UnresolvedType.OBJECT));     
        
        ResolvedType rt2 = genericType.getSuperclass();
        assertTrue("Superclass for Map generic type should be Object but was "+rt2,rt2.equals(UnresolvedType.OBJECT));       
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6286.java