error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9706.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9706.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9706.java
text:
```scala
v@@erifyClassSignature("Basic","Ljava/lang/Object;PJ<Ljava/lang/Double;>;PI<Ljava/lang/Double;>;");

package org.aspectj.systemtest.ajc150;

import java.io.File;

import junit.framework.Test;

import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.Signature;
import org.aspectj.apache.bcel.util.ClassPath;
import org.aspectj.apache.bcel.util.SyntheticRepository;
import org.aspectj.testing.XMLBasedAjcTestCase;

public class GenericsTests extends XMLBasedAjcTestCase {

	/*==========================================
	 * Generics test plan for pointcuts.
	 * 
	 * handler  PASS
	 *   - does not permit type var spec
	 *   - does not permit generic type (fail with type not found)
	 *   - does not permit parameterized types
	 * if PASS
	 *   - does not permit type vars
	 * cflow PASS
	 *   - does not permit type vars
	 * cflowbelow PASS
	 *   - does not permit type vars
	 * @this PASS
	 *   - does not permit type vars PASS
	 *   - does not permit parameterized type PASS
	 * @target PASS
	 *   - does not permit type vars PASS
	 *   - does not permit parameterized type PASS
	 * @args PASS
     *   - does not permit type vars PASS
	 *   - does not permit parameterized type PASS
	 * 	 @annotation PASS
     *   - does not permit type vars PASS
	 *   - does not permit parameterized type PASS
	 *   @within, @within code - as above PASS
	 * annotation type pattern with generic and parameterized types  PASS
	 *   - just make sure that annotation interfaces can never be generic first! VERIFIED
	 * 	  - @Foo<T>  should fail  PASS
	 *   - @Foo<String> should fail PASS
	 *   - @(Foo || Bar<T>) should fail  DEFERRED (not critical)
	 * staticinitialization
	 *   - error on parameterized type PASS
	 *   - permit parameterized type + PASS
	 *   - matching with parameterized type + 
	 *   - wrong number of parameters in parameterized type  PASS
	 *   - generic type with one type parameter
	 *   - generic type with n type parameters
	 *   - generic type with bounds [extends, extends + i/f's]
	 *   - generic type with wrong number of type params
	 *   - wildcards in bounds
	 * within
	 *   - as above, but allows parameterized type
	 *   - wildcards in type parameters
	 * this 
	 *   - no type vars
	 *   - parameterized types
	 *        - implements
	 *        - instanceof
	 * target
	 *   - as this
	 * args
	 *   - as this/target, plus...
	 *   - known static match
	 *   - known static match fail
	 *   - maybe match with unchecked warning
	 * get & set
	 *   - parameterized type
	 *   - generic type
	 *   - return type is type variable
	 *   - return type is parameterized
	 * initialization, preinitialization
	 *   - type variables as type params
	 *   - no join points for parameterized types
	 * execution, withincode
	 *    - wait till we get there!
	 * call
	 *   - wait till we get there!
	 */
	
	public static Test suite() {
		return XMLBasedAjcTestCase.loadSuite(GenericsTests.class);
	}

	protected File getSpecFile() {
		return new File("../tests/src/org/aspectj/systemtest/ajc150/ajc150.xml");
	}
	
	public void testITDReturningParameterizedType() {
		runTest("ITD with parameterized type");
	}
	
	public void testPR91267_1() {
		runTest("NPE using generic methods in aspects 1");
	}

	public void testPR91267_2() {
		runTest("NPE using generic methods in aspects 2");
	}
	
	public void testPR91053() {
		runTest("Generics problem with Set");
	}
	
	public void testPR87282() {
		runTest("Compilation error on generic member introduction");
	}
	
	public void testPR88606() {
		runTest("Parameterized types on introduced fields not correctly recognized");
	}

    public void testPR97763() {
	    runTest("ITD method with generic arg");
    }

    public void testGenericsBang_pr95993() {
	    runTest("NPE at ClassScope.java:660 when compiling generic class");
    }    
	
	// generic aspects
	public void testPR96220_GenericAspects1() {
		runTest("generic aspects - 1");
	}
	
	public void testPR96220_GenericAspects2() {
		runTest("generic aspects - 2");
	}
	
	public void testPR96220_GenericAspects3() {
		runTest("generic aspects - 3");
	}
	
	// Developers notebook
	// ITD of generic members
	
	public void testItdNonStaticMethod() {
		runTest("Parsing generic ITDs - 1");
	}
	public void testItdStaticMethod() {
		runTest("Parsing generic ITDs - 2");
	}
	public void testItdCtor() {
		runTest("Parsing generic ITDs - 3");
	}
	public void testItdComplexMethod() {
		runTest("Parsing generic ITDs - 4");
	}
		
	public void testItdNonStaticMember() {
		runTest("itd of non static member");
	}
	
	public void testItdStaticMember() {
		runTest("itd of static member");
	}

//	public void testItdOnGenericType() {
//		runTest("ITDs on generic type");
//	}
//	
//	public void testItdUsingTypeParameter() {
//		runTest("itd using type parameter");
//	}
//	
//	public void testItdIncorrectlyUsingTypeParameter() {
//		runTest("itd incorrectly using type parameter");
//	}

	// ----------------------------------------------------------------------------------------
	// generic declare parents tests
	// ----------------------------------------------------------------------------------------
	
	public void testPR96220_GenericDecp() {
		runTest("generic decp - simple");
		verifyClassSignature("Basic","Ljava/lang/Object;LJ<Ljava/lang/Double;>;LI<Ljava/lang/Double;>;");
	}
	
	// Both the existing type decl and the one adding via decp are parameterized
	public void testGenericDecpMultipleVariantsOfAParameterizedType1() {
		runTest("generic decp - implementing two variants #1");
	}

	// Existing type decl is raw and the one added via decp is parameterized
	public void testGenericDecpMultipleVariantsOfAParameterizedType2() {
		runTest("generic decp - implementing two variants #2");
	}

	// Existing type decl is parameterized and the one added via decp is raw
	public void testGenericDecpMultipleVariantsOfAParameterizedType3() {
		runTest("generic decp - implementing two variants #3");
	}

	// decp is parameterized but it does match the one already on the type
	public void testGenericDecpMultipleVariantsOfAParameterizedType4() {
		runTest("generic decp - implementing two variants #4");
	}
	
	// same as above four tests for binary weaving
	public void testGenericDecpMultipleVariantsOfAParameterizedType1_binaryWeaving() {
		runTest("generic decp binary - implementing two variants #1");
	}
	
	public void testGenericDecpMultipleVariantsOfAParameterizedType2_binaryWeaving() {
		runTest("generic decp binary - implementing two variants #2");
	}

	// Existing type decl is parameterized and the one added via decp is raw
	public void testGenericDecpMultipleVariantsOfAParameterizedType3_binaryWeaving() {
		runTest("generic decp binary - implementing two variants #3");
	}

	// decp is parameterized but it does match the one already on the type
	public void testGenericDecpMultipleVariantsOfAParameterizedType4_binaryWeaving() {
		runTest("generic decp binary - implementing two variants #4");
	}
	
//	
//	public void testGenericDecpIncorrectNumberOfTypeParams() {
//		runTest("generic decp - incorrect number of type parameters");
//	}
//	
//	public void testGenericDecpSpecifyingBounds() {
//		runTest("generic decp - specifying bounds");
//	}
//	
//	public void testGenericDecpViolatingBounds() {
//		runTest("generic decp - specifying bounds but breaking them");
//	}
	
	// need separate compilation test to verify signatures are ok
//
//	public void testIllegalGenericDecp() {
//		runTest("illegal generic decp");
//	}
//
//	public void testPR95992_TypeResolvingProblemWithGenerics() {
//		runTest("Problems resolving type name inside generic class");
//	}
	
	// missing tests in here:
	
	// 1. public ITDs and separate compilation - are the signatures correct for the new public members?
	// 2. ITDF

	// -- Pointcut tests...

	public void testHandlerWithGenerics() {
		runTest("handler pcd and generics / type vars");
	}
	
	public void testPointcutsThatDontAllowTypeVars() {
		runTest("pointcuts that dont allow type vars");
	}
	
	public void testParameterizedTypesInAtPCDs() {
		runTest("annotation pcds with parameterized types");
	}

	// comment out due to temporary failing
//	public void testAnnotationPatternsWithParameterizedTypes() {
//		runTest("annotation patterns with parameterized types");
//	}
	
	public void testStaticInitializationWithParameterizedTypes() {
		runTest("staticinitialization and parameterized types");
	}

	// temporary
//	public void testStaticInitializationMatchingWithParameterizedTypes() {
//		runTest("staticinitialization and parameterized type matching");
//	}

	// temporary
//	public void testStaticInitializationWithGenericTypes() {
//		runTest("staticinitialization with generic types");
//	}
//	
//	public void testStaticInitializationWithGenericTypesAdvanced() {
//		runTest("staticinitialization with generic types - advanced");		
//	}
	
	public void testExecutionWithRawType() {
		runTest("execution pcd with raw type matching");
	}
	
	public void testExecutionWithRawSignature() {
		runTest("execution pcd with raw signature matching");
	}
	
	public void testExecutionWithGenericDeclaringTypeAndErasedParameterTypes() {
		runTest("execution pcd with generic declaring type and erased parameter types");
	}
	
// not passing yet...
//	public void testExecutionWithGenericSignature() {
//		runTest("execution pcd with generic signature matching");
//	}
	
	// --- helpers
		
	// Check the signature attribute on a class is correct
	private void verifyClassSignature(String classname,String sig) {
		try {
			ClassPath cp = 
				new ClassPath(ajc.getSandboxDirectory() + File.pathSeparator + System.getProperty("java.class.path"));
		    SyntheticRepository sRepos =  SyntheticRepository.getInstance(cp);
			JavaClass clazz = sRepos.loadClass(classname);
			Signature sigAttr = null;
			Attribute[] attrs = clazz.getAttributes();
			for (int i = 0; i < attrs.length; i++) {
				Attribute attribute = attrs[i];
				if (attribute.getName().equals("Signature")) sigAttr = (Signature)attribute;
			}
			assertTrue("Failed to find signature attribute for class "+classname,sigAttr!=null);
			assertTrue("Expected signature to be '"+sig+"' but was '"+sigAttr.getSignature()+"'",
					sigAttr.getSignature().equals(sig));
		} catch (ClassNotFoundException e) {
			fail("Couldn't find class "+classname+" in the sandbox directory.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9706.java