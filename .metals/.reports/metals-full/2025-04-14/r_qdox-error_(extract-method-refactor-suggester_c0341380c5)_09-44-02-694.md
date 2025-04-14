error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13632.java
text:
```scala
a@@rgs.add("-XterminateAfterCompilation");

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

package org.aspectj.ajdt.internal.compiler.batch;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.aspectj.ajdt.ajc.AjdtAjcTests;
import org.aspectj.tools.ajc.AjcTests;

public class BcweaverJarMaker {

	private static String cp = "../lib/test/aspectjrt.jar;../lib/test/testing-client.jar" + File.pathSeparator + System.getProperty("aspectjrt.path");
	
	public BcweaverJarMaker() {
		super();
	}

	public static void main(String[] args) throws IOException {
		makeJar0();
		makeJar1();
		makeJar1a();
		makeJar2();
		
		makeJarObviousNothing();
		makeJarHardNothing();
		
		
		makeTestJars();

		makeURLWeavingClassLoaderJars();
		
		makeDuplicateManifestTestJars();

		makeOutjarTestJars();
		makeAspectPathTestJars();
		makeAjc11TestJars();
	}
	
	public static void makeJar0() throws IOException {
		List args = new ArrayList();
		args.add("-outjar");
		args.add("../weaver/testdata/tracing.jar");

		args.add("-classpath");
		args.add(AjcTests.aspectjrtClasspath());
		
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/Trace.java");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/MyTrace.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}
	
	public static void makeJar1() throws IOException {
		List args = new ArrayList();
		args.add("-outjar");
		args.add("../weaver/testdata/megatrace.jar");

		args.add("-classpath");
        args.add(AjcTests.aspectjrtClasspath());
		
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/trace/MegaTrace.java");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/trace/ExecTrace.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}
	
	
	public static void makeJarObviousNothing() throws IOException {
		List args = new ArrayList();
		args.add("-outjar");
		args.add("../weaver/testdata/megatrace0easy.jar");

		args.add("-classpath");
        args.add(AjcTests.aspectjrtClasspath());
		
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/trace/MegaTrace.java");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/trace/ObviousTraceNothing.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}
	
	public static void makeJarHardNothing() throws IOException {
		List args = new ArrayList();
		args.add("-outjar");
		args.add("../weaver/testdata/megatrace0hard.jar");

		args.add("-classpath");
        args.add(AjcTests.aspectjrtClasspath());
		
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/trace/MegaTrace.java");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/trace/HardTraceNothing.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}
	
	
	public static void makeJar1a() throws IOException {
		List args = new ArrayList();
		args.add("-outjar");
		args.add("../weaver/testdata/megatraceNoweave.jar");

		args.add("-noweave");

		args.add("-classpath");
        args.add(AjcTests.aspectjrtClasspath());
		
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/trace/MegaTrace.java");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/trace/ExecTrace.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}
	
	
	public static void makeJar2() throws IOException {
		List args = new ArrayList();
		args.add("-outjar");
		args.add("../weaver/testdata/dummyAspect.jar");

		args.add("-classpath");
        args.add(AjcTests.aspectjrtClasspath());
		
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/DummyAspect.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}	
	
	public static void makeTestJars() throws IOException {
		List args = new ArrayList();

		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar" +
			File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../tests/new/options11/aspectlib1.jar");		
		args.add("../tests/new/options11/library1/*.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
		
		args = new ArrayList();

		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../tests/new/options11/aspectlib2.jar");		
		args.add("../tests/new/options11/library2/*.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
		
		args = new ArrayList();

		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar"  +
			File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../tests/new/options11/injar.jar");		
		args.add("../tests/new/options11/injar/*.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
		
		args = new ArrayList();

		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar"  +
			File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../tests/bugs/serialVersionUID/injar.jar");		
		args.add("../tests/bugs/serialVersionUID/Test.java");
		args.add("../tests/bugs/serialVersionUID/Util.java");
		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
		
		// For PR55341
		args = new ArrayList();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar"  +
			File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../tests/bugs/StringToString/helloworld.jar");		
		args.add("../tests/bugs/StringToString/HW.java");		
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		buildShowWeaveInfoTestingJars();		
	}
	
	public static void makeURLWeavingClassLoaderJars() throws IOException {
		List args = new ArrayList();

		/*
		 * Vanilla classes
		 */
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../weaver/testdata/ltw-classes.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWHelloWorld.java");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/ltw/LTWPackageTest.java");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/* 
		 * Woven classes
		 */
		args = new ArrayList();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar;../weaver/testdata/ltw-classes.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-XnotReweavable");
		args.add("-outjar");
		args.add("../weaver/testdata/ltw-woven.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWHelloWorld.java");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWAspect.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * Advice
		 */
		args = new ArrayList();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar;../weaver/testdata/ltw-classes.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../weaver/testdata/ltw-aspects.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWAspect.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * Declare warning advice
		 */
		args = new ArrayList();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar;../weaver/testdata/ltw-classes.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../weaver/testdata/ltw-dwaspects.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWDeclareWarning.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * Declare error advice
		 */
		args = new ArrayList();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar;../weaver/testdata/ltw-classes.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../weaver/testdata/ltw-deaspects.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWDeclareError.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * Around closure advice
		 */
		args = new ArrayList();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar;../weaver/testdata/ltw-classes.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../weaver/testdata/ltw-acaspects.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWAroundClosure.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * ITD
		 */
		args = new ArrayList();
		args.add("-Xlint:ignore"); 
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar;../weaver/testdata/ltw-classes.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../weaver/testdata/ltw-itdaspects.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWInterfaceITD.aj");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWFieldITD.aj");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWMethodITD.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * perXXX()
		 */
		args = new ArrayList();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar;../weaver/testdata/ltw-classes.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../weaver/testdata/ltw-peraspects.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/LTWPerthis.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}	
	
	private static void buildJarWithClasspath(String outjar,String input,String deps,boolean nodebug) {
		System.out.println("  Building "+outjar);
		List args = new ArrayList();
		if (nodebug) args.add("-g:none");		
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar"  +
				 File.pathSeparator + System.getProperty("aspectjrt.path") +
				 (deps!=null?File.pathSeparator + "../ajde/testdata/WeaveInfoMessagesTest/"+deps:""));
		args.add("-outjar");
		args.add("../ajde/testdata/WeaveInfoMessagesTest/"+outjar);		
		args.add("../ajde/testdata/WeaveInfoMessagesTest/"+input);
		
		System.err.println(args);
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);	
	}
	
	private static void buildShowWeaveInfoTestingJars() {
		System.out.println("For binary weave info message testing (ShowWeaveMessagesTestCase.java)");
		buildJarWithClasspath("Simple.jar","Simple.java",null,false);
		// Build with javac and jar
		// buildJarWithClasspath("Simple_nodebug.jar","Simple.java",null,true);
		buildJarWithClasspath("AspectAdvice.jar","AspectAdvice.aj",null,false);
		buildJarWithClasspath("AspectAdvice_nodebug.jar","AspectAdvice.aj","Simple.jar",true);
		buildJarWithClasspath("AspectDeclare.jar","AspectDeclare.aj","Simple.jar",false);
		buildJarWithClasspath("AspectDeclare_nodebug.jar","AspectDeclare.aj","Simple.jar",true);
		buildJarWithClasspath("AspectITD.jar","AspectITD.aj","Simple.jar",false);
		buildJarWithClasspath("AspectITD_nodebug.jar","AspectITD.aj","Simple.jar",true);
		buildJarWithClasspath("AspectDeclareSoft.jar","AspectDeclareSoft.aj","Simple.jar",false);
		buildJarWithClasspath("AspectDeclareSoft_nodebug.jar","AspectDeclareSoft.aj","Simple.jar",true);
	}	
	
	public static void makeDuplicateManifestTestJars() throws IOException {
		List args = new ArrayList();

		/*
		 * injar
		 */
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../ajde/testdata/DuplicateManifestTest/injar.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/Hello.java");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * aspectjar
		 */
		args = new ArrayList();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("../ajde/testdata/DuplicateManifestTest/aspectjar.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/Trace.java");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/TraceHello.java");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}	
	
	public static void makeAspectPathTestJars() throws IOException {
		List args = new ArrayList();

		args.clear();
		args.add("-classpath"); 
		args.add(cp);
		args.add("-outjar");
		args.add("../tests/bugs/perCflowAndJar/lib.jar");
		args.add("../tests/bugs/perCflowAndJar/PerCFlowCompileFromJar.java");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}
	
	public static void makeAjc11TestJars() throws IOException {
		List args = new ArrayList();

		args.clear();
		args.add("-classpath"); 
		args.add(cp);
		args.add("-outjar");
		args.add("../tests/bugs/cflowAndJar/lib.jar");
		args.add("../tests/bugs/cflowAndJar/AbstractAspect.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
	}
	
	public static void makeOutjarTestJars() throws IOException {
		List args = new ArrayList();

		/*
		 * parent
		 */
		args.clear();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar" +
		   File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("./testdata/OutjarTest/parent.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/OutjarTest/src/jar1/Parent.java");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * child
		 */
		args.clear();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar"
			+ File.pathSeparator + System.getProperty("aspectjrt.path")
			+ File.pathSeparator + "./testdata/OutjarTest/parent.jar");
		args.add("-outjar");
		args.add("./testdata/OutjarTest/child.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/OutjarTest/src/jar2/Child.java");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * aspects
		 */
		args.clear();
		args.add("-classpath"); 
		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar"
			+ File.pathSeparator + System.getProperty("aspectjrt.path"));
		args.add("-outjar");
		args.add("./testdata/OutjarTest/aspects.jar");
		args.add(AjdtAjcTests.TESTDATA_PATH + "/OutjarTest/src/jar3/Aspect.aj");
		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);

		/*
		 * aspectjar
		 */
//		args = new ArrayList();
//		args.add("-classpath"); 
//		args.add("../lib/test/aspectjrt.jar;../lib/test/testing-client.jar" +
//		   File.pathSeparator + System.getProperty("aspectjrt.path"));
//		args.add("-outjar");
//		args.add("../ajde/testdata/DuplicateManifestTest/aspectjar.jar");
//		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/Trace.java");
//		args.add(AjdtAjcTests.TESTDATA_PATH + "/src1/TraceHello.java");
//		CommandTestCase.runCompiler(args, CommandTestCase.NO_ERRORS);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13632.java