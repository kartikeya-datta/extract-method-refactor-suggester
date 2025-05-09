error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3800.java
text:
```scala
final S@@tring PATH = "mumble" + File.separator + "rt.jar";

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

package org.aspectj.ajdt.ajc;

import java.io.*;
import java.util.*;

import junit.framework.TestCase;

import org.aspectj.ajdt.internal.core.builder.*;
import org.aspectj.bridge.CountingMessageHandler;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.MessageWriter;
import org.aspectj.testing.util.TestUtil;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

/**
 * Some black-box test is happening here.
 */
public class BuildArgParserTestCase extends TestCase {

	private static final String TEST_DIR = AjdtAjcTests.TESTDATA_PATH + File.separator + "ajc" + File.separator;
	private MessageWriter messageWriter = new MessageWriter(new PrintWriter(System.out), false);

	public BuildArgParserTestCase(String name) {
		super(name);
	}
	
	private AjBuildConfig genBuildConfig(String[] args, IMessageHandler handler) {
		return new BuildArgParser(handler).genBuildConfig(args);
	}

	public void testDefaultClasspathAndTargetCombo() throws Exception {
		String ENTRY = "1.jar" + File.pathSeparator + "2.jar";
		final String classpath = System.getProperty("java.class.path");
		try {
            System.setProperty("java.class.path", ENTRY); // see finally below
            BuildArgParser parser = new BuildArgParser(messageWriter);
    		AjBuildConfig config = parser.genBuildConfig(new String[] { });
            /*String err = */parser.getOtherMessages(true);       
            //!!!assertTrue(err, null == err);
            assertTrue(
    			config.getClasspath().toString(),
    			config.getClasspath().contains("1.jar"));
    		assertTrue(
    			config.getClasspath().toString(),
    			config.getClasspath().contains("2.jar"));
    
    		config = genBuildConfig(new String[] { "-1.3" }, messageWriter);
    		// these errors are deffered to the compiler now
            //err = parser.getOtherMessages(true);       
            //!!!assertTrue(err, null == err);
    		assertTrue(
    			config.getClasspath().toString(),
    			config.getClasspath().contains("1.jar"));
    		assertTrue(
    			config.getClasspath().toString(),
    			config.getClasspath().contains("2.jar"));
    
			parser = new BuildArgParser(messageWriter);
    		config = parser.genBuildConfig(new String[] { "-1.3" });
            /*err = */parser.getOtherMessages(true);       
            //!!!assertTrue(err, null == err);
    		assertTrue(
    			config.getClasspath().toString(),
    			config.getClasspath().contains("1.jar"));
    		assertTrue(
    			config.getClasspath().toString(),
    			config.getClasspath().contains("2.jar"));
    			
    		config = genBuildConfig(new String[] { 
    			"-classpath", ENTRY, "-1.4" }, messageWriter);
			//			these errors are deffered to the compiler now
            //err = parser.getOtherMessages(true);       
            //assertTrue("expected errors for missing jars", null != err);
    		List cp = config.getClasspath();
    		boolean jar1Found = false;
    		boolean jar2Found = false;
    		for (Iterator iter = cp.iterator(); iter.hasNext();) {
                String element = (String) iter.next();
                if (element.indexOf("1.jar") != -1) jar1Found = true;
                if (element.indexOf("2.jar") != -1) jar2Found = true;
            }
    		assertTrue(
    			config.getClasspath().toString(),
    			jar1Found);
    		assertTrue(
    			config.getClasspath().toString(),
    			jar2Found);
    			
        } finally {
            // do finally to avoid messing up classpath for other tests
            System.setProperty("java.class.path", classpath);
            String setPath = System.getProperty("java.class.path");
            String m = "other tests will fail - classpath not reset";
            assertEquals(m, classpath, setPath); 
        }
	}
	
	public void testPathResolutionFromConfigArgs() {
		String FILE_PATH =   "@" + TEST_DIR + "configWithClasspathExtdirsBootCPArgs.lst";
		AjBuildConfig config = genBuildConfig(new String[] { FILE_PATH }, messageWriter);
		List classpath = config.getClasspath();
		// should have three entries, resolved relative to location of .lst file
		assertEquals("Three entries in classpath",3,classpath.size());
		Iterator cpIter = classpath.iterator();
		try {
		    assertEquals("Should be relative to TESTDIR",new File(TEST_DIR+File.separator+"xyz").getCanonicalPath(),cpIter.next());
		    assertEquals("Should be relative to TESTDIR",new File(TEST_DIR+File.separator+"myextdir" + File.separator + "dummy.jar").getCanonicalPath(),cpIter.next());
		    assertEquals("Should be relative to TESTDIR",new File(TEST_DIR+File.separator+"abc.jar").getCanonicalPath(),cpIter.next());
			List files = config.getFiles();
			assertEquals("Two source files",2,files.size());
			Iterator fIter = files.iterator();
			assertEquals("Should be relative to TESTDIR",new File(TEST_DIR+File.separator+"Abc.java").getCanonicalFile(),fIter.next());
			assertEquals("Should be relative to TESTDIR",new File(TEST_DIR+File.separator+"xyz"+File.separator+"Def.aj").getCanonicalFile(),fIter.next());
		    
		} catch (IOException ex) {
		    fail("Test case failure attempting to create canonical path: " + ex);
		}
		
	}
	
	public void testAjOptions() throws InvalidInputException {
		AjBuildConfig config = genBuildConfig(new String[] {  "-Xlint" }, messageWriter);
 	
		assertTrue(
			"default options",
			config.getLintMode().equals(AjBuildConfig.AJLINT_DEFAULT));			
	}

	public void testAspectpath() throws InvalidInputException {
		final String SOURCE_JAR = AjdtAjcTests.TESTDATA_PATH + "/testclasses.jar";
		final String SOURCE_JARS = AjdtAjcTests.TESTDATA_PATH + "/testclasses.jar" + File.pathSeparator 
			+ "../weaver/testdata/tracing.jar" + File.pathSeparator 
			+ "../weaver/testdata/dummyAspect.jar";
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-aspectpath", SOURCE_JAR }, 
			messageWriter);
		
		assertTrue(((File)config.getAspectpath().get(0)).getName(), ((File)config.getAspectpath().get(0)).getName().equals("testclasses.jar"));

		config = genBuildConfig(new String[] { 
			"-aspectpath", SOURCE_JARS }, 
			messageWriter);
		assertTrue("size", + config.getAspectpath().size() == 3);
	}

	public void testInJars() throws InvalidInputException {
		final String SOURCE_JAR = AjdtAjcTests.TESTDATA_PATH + "/testclasses.jar";
		final String SOURCE_JARS = AjdtAjcTests.TESTDATA_PATH + "/testclasses.jar" + File.pathSeparator 
			+ "../weaver/testdata/tracing.jar" + File.pathSeparator 
			+ "../weaver/testdata/dummyAspect.jar";
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-injars", SOURCE_JAR }, 
			messageWriter);
		//XXX don't let this remain in both places in beta1			
//		assertTrue(
//			"" + config.getAjOptions().get(AjCompilerOptions.OPTION_InJARs),  
//			config.getAjOptions().get(AjCompilerOptions.OPTION_InJARs).equals(CompilerOptions.PRESERVE));
		assertTrue(((File)config.getInJars().get(0)).getName(), ((File)config.getInJars().get(0)).getName().equals("testclasses.jar"));

		config = genBuildConfig(new String[] { 
			"-injars", SOURCE_JARS }, 
			messageWriter);
		assertTrue("size", + config.getInJars().size() == 3);
	}

	public void testBadInJars() throws InvalidInputException {
		final String SOURCE_JARS = AjdtAjcTests.TESTDATA_PATH + "/testclasses.jar" + File.pathSeparator + "b.far" + File.pathSeparator + "c.jar";
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-injars", SOURCE_JARS }, 
			messageWriter);
		assertTrue("size: " + config.getInJars().size(), config.getInJars().size() == 1);
	}

	public void testBadPathToSourceFiles() {
		CountingMessageHandler countingHandler = new CountingMessageHandler(messageWriter);
		/*AjBuildConfig config = */genBuildConfig(new String[]{ "inventedDir/doesntexist/*.java"},countingHandler);
		assertTrue("Expected an error for the invalid path.",countingHandler.numMessages(IMessage.ERROR,false)==1);	
	}

	public void testMultipleSourceRoots() throws InvalidInputException, IOException {
		final String SRCROOT_1 = AjdtAjcTests.TESTDATA_PATH + "/src1/p1";
		final String SRCROOT_2 = AjdtAjcTests.TESTDATA_PATH + "/ajc";
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-sourceroots", SRCROOT_1 + File.pathSeparator + SRCROOT_2 }, 
			messageWriter);
		
		assertEquals(getCanonicalPath(new File(SRCROOT_1)), ((File)config.getSourceRoots().get(0)).getAbsolutePath());
		
		Collection expectedFiles = Arrays.asList(new File[] {
			new File(SRCROOT_1+File.separator+"A.java").getCanonicalFile(),
			new File(SRCROOT_1+File.separator+"Foo.java").getCanonicalFile(),
			new File(SRCROOT_2+File.separator+"A.java").getCanonicalFile(),
			new File(SRCROOT_2+File.separator+"B.java").getCanonicalFile(),
			new File(SRCROOT_2+File.separator+"X.aj").getCanonicalFile(),
			new File(SRCROOT_2+File.separator+"Y.aj").getCanonicalFile(),
			new File(SRCROOT_2+File.separator+"pkg"+File.separator+"Hello.java").getCanonicalFile(),
		});
  
		//System.out.println(config.getFiles());

		TestUtil.assertSetEquals(expectedFiles, config.getFiles());
	}

	/**
	 * @param file
	 * @return String
	 */
	private String getCanonicalPath(File file) {
		String ret = "";
		try {
			ret = file.getCanonicalPath();
		} catch (IOException ioEx) {
			fail("Unable to canonicalize " + file + " : " + ioEx);
		}
		return ret;
	}

	public void testSourceRootDir() throws InvalidInputException, IOException {
		final String SRCROOT = AjdtAjcTests.TESTDATA_PATH + "/ajc";
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-sourceroots", SRCROOT }, 
			messageWriter);

		assertEquals(getCanonicalPath(new File(SRCROOT)), ((File)config.getSourceRoots().get(0)).getAbsolutePath());
		
		Collection expectedFiles = Arrays.asList(new File[] {
			new File(SRCROOT+File.separator+"A.java").getCanonicalFile(),
			new File(SRCROOT+File.separator+"B.java").getCanonicalFile(),
			new File(SRCROOT+File.separator+"X.aj").getCanonicalFile(),
			new File(SRCROOT+File.separator+"Y.aj").getCanonicalFile(),
			new File(SRCROOT+File.separator+"pkg"+File.separator+"Hello.java").getCanonicalFile(),
		});

		//System.out.println(config.getFiles());

		TestUtil.assertSetEquals(expectedFiles, config.getFiles());
	}

	public void testBadSourceRootDir() throws InvalidInputException {
		AjBuildConfig config = genBuildConfig(new String[] {   
			"-sourceroots", 
			AjdtAjcTests.TESTDATA_PATH + "/mumbleDoesNotExist" + File.pathSeparator 
            + AjdtAjcTests.TESTDATA_PATH + "/ajc" }, 
			messageWriter);

		assertTrue(config.getSourceRoots().toString(), config.getSourceRoots().size() == 1);

		config = genBuildConfig(new String[] { 
			"-sourceroots" }, 
			messageWriter);

		assertTrue("" + config.getSourceRoots(), config.getSourceRoots().size() == 0);
			
	}

	//??? we've decided not to make this an error
	public void testSourceRootDirWithFiles() throws InvalidInputException, IOException {
		final String SRCROOT = AjdtAjcTests.TESTDATA_PATH + "/ajc/pkg";
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-sourceroots", SRCROOT,  AjdtAjcTests.TESTDATA_PATH + "/src1/A.java"}, 
			messageWriter);

		assertEquals(getCanonicalPath(new File(SRCROOT)), ((File)config.getSourceRoots().get(0)).getAbsolutePath());
		
		Collection expectedFiles = Arrays.asList(new File[] {
			new File(SRCROOT+File.separator+"Hello.java").getCanonicalFile(),
			new File(AjdtAjcTests.TESTDATA_PATH +File.separator+"src1"+File.separator+"A.java").getCanonicalFile(),
		});

		TestUtil.assertSetEquals(expectedFiles, config.getFiles());
		
	}

	public void testExtDirs() throws Exception {
		final String DIR = AjdtAjcTests.TESTDATA_PATH;
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-extdirs", DIR }, 
			messageWriter);
		assertTrue(config.getClasspath().toString(), config.getClasspath().contains(
			new File(DIR + File.separator + "testclasses.jar").getCanonicalPath()
		));
	}

	public void testBootclasspath() throws InvalidInputException {
		final String PATH = "mumble/rt.jar";
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-bootclasspath", PATH }, 
			messageWriter);		
		assertTrue(config.getClasspath().toString(), ((String)config.getClasspath().get(0)).indexOf(PATH) != -1); 

		config = genBuildConfig(new String[] { 
			}, 
			messageWriter);		
		assertTrue(config.getClasspath().toString(), !config.getClasspath().get(0).equals(PATH)); 
	}

	public void testOutputJar() throws InvalidInputException {
		final String OUT_JAR = AjdtAjcTests.TESTDATA_PATH + "/testclasses.jar";
		
		AjBuildConfig config = genBuildConfig(new String[] { 
			"-outjar", OUT_JAR }, 
			messageWriter);

		//XXX don't let this remain in both places in beta1
//		assertTrue(
//			"will generate: " + config.getAjOptions().get(AjCompilerOptions.OPTION_OutJAR),  
//			config.getAjOptions().get(AjCompilerOptions.OPTION_OutJAR).equals(CompilerOptions.GENERATE));
		assertEquals(
			getCanonicalPath(new File(OUT_JAR)),config.getOutputJar().getAbsolutePath()); 
	
		File nonExistingJar = new File(AjdtAjcTests.TESTDATA_PATH + "/mumbleDoesNotExist.jar");
		config = genBuildConfig(new String[] { 
			"-outjar", nonExistingJar.getAbsolutePath() }, 
			messageWriter);
		assertEquals(
			getCanonicalPath(nonExistingJar), 
			config.getOutputJar().getAbsolutePath());	

		nonExistingJar.delete();
	}
	
	//XXX shouldn't need -1.4 to get this to pass
	public void testCombinedOptions() throws InvalidInputException {
		AjBuildConfig config = genBuildConfig(new String[] {  "-Xlint:error", "-target", "1.4"}, messageWriter);
		assertTrue(
				"target set",  
				config.getOptions().targetJDK == CompilerOptions.JDK1_4); 

		assertTrue(
			"Xlint option set",
			config.getLintMode().equals(AjBuildConfig.AJLINT_ERROR));			
	}
	
	public void testOutputDirectorySetting() throws InvalidInputException {
		AjBuildConfig config = genBuildConfig(new String[] {  "-d", TEST_DIR }, messageWriter);
		
		assertTrue(
			new File(config.getOutputDir().getPath()).getAbsolutePath() + " ?= " + 
			new File(TEST_DIR).getAbsolutePath(),
			config.getOutputDir().getAbsolutePath().equals((new File(TEST_DIR)).getAbsolutePath()));	
	}

	public void testClasspathSetting() throws InvalidInputException {
		String ENTRY = "1.jar" + File.pathSeparator + "2.jar";
		AjBuildConfig config = genBuildConfig(new String[] {  "-classpath", ENTRY }, messageWriter);
		
   		List cp = config.getClasspath();
		boolean jar1Found = false;
		boolean jar2Found = false;
		for (Iterator iter = cp.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            if (element.indexOf("1.jar") != -1) jar1Found = true;
            if (element.indexOf("2.jar") != -1) jar2Found = true;
        }
		assertTrue(
			config.getClasspath().toString(),
			jar1Found);
		assertTrue(
			config.getClasspath().toString(),
			jar2Found);
	}

	public void testArgInConfigFile() throws InvalidInputException {
		String FILE_PATH =   "@" + TEST_DIR + "configWithArgs.lst";
		String OUT_PATH = "bin";
		AjBuildConfig config = genBuildConfig(new String[] { FILE_PATH }, messageWriter);
		
        assertNotNull(config);
        File outputDir = config.getOutputDir();
        assertNotNull(outputDir);        
		assertEquals(outputDir.getPath(), OUT_PATH);
	}

	public void testNonExistentConfigFile() throws IOException {
		String FILE_PATH =   "@" + TEST_DIR + "../bug-40257/d1/test.lst";
		AjBuildConfig config = genBuildConfig(new String[] { FILE_PATH }, messageWriter);

		String a = new File(TEST_DIR + "../bug-40257/d1/A.java").getCanonicalPath();
		String b = new File(TEST_DIR + "../bug-40257/d1/d2/B.java").getCanonicalPath();
		String c = new File(TEST_DIR + "../bug-40257/d3/C.java").getCanonicalPath();
		List pathList = new ArrayList();
		for (Iterator it = config.getFiles().iterator(); it.hasNext(); ) {
			pathList.add(((File)it.next()).getCanonicalPath());
		}
		assertTrue(pathList.contains(a));
		assertTrue(pathList.contains(b));
		assertTrue(pathList.contains(c));
			
	}

	public void testXlint() throws InvalidInputException {
//		AjdtCommand command = new AjdtCommand();
		AjBuildConfig config = genBuildConfig(new String[] {"-Xlint"}, messageWriter);
		assertTrue("", config.getLintMode().equals(AjBuildConfig.AJLINT_DEFAULT));
		config = genBuildConfig(new String[] {"-Xlint:warn"}, messageWriter);
		assertTrue("", config.getLintMode().equals(AjBuildConfig.AJLINT_WARN));
		config = genBuildConfig(new String[] {"-Xlint:error"}, messageWriter);
		assertTrue("", config.getLintMode().equals(AjBuildConfig.AJLINT_ERROR));
		config = genBuildConfig(new String[] {"-Xlint:ignore"}, messageWriter);
		assertTrue("", config.getLintMode().equals(AjBuildConfig.AJLINT_IGNORE));
	}

	public void testXlintfile() throws InvalidInputException {
		String lintFile = AjdtAjcTests.TESTDATA_PATH + "/lintspec.properties"; 
//		String badLintFile = "lint.props";
		AjBuildConfig config = genBuildConfig(new String[] {"-Xlintfile", lintFile}, messageWriter);
		assertTrue(new File(lintFile).exists());
		assertEquals(getCanonicalPath(new File(lintFile)),config.getLintSpecFile().getAbsolutePath());	
	}

	public void testOptions() throws InvalidInputException {
//		AjdtCommand command = new AjdtCommand();
		String TARGET = "1.4";
		AjBuildConfig config = genBuildConfig(new String[] {"-target", TARGET, "-source", TARGET}, messageWriter);
		assertTrue(
			"target set",  
			config.getOptions().targetJDK == CompilerOptions.JDK1_4);
		assertTrue(
			"source set",  
			config.getOptions().sourceLevel == CompilerOptions.JDK1_4);
	}
	
	public void testLstFileExpansion() throws IOException, FileNotFoundException, InvalidInputException {
		String FILE_PATH =  TEST_DIR + "config.lst";
		String SOURCE_PATH_1 = "A.java";
		String SOURCE_PATH_2 = "B.java";

//        File f = new File(FILE_PATH);
		
		AjBuildConfig config = genBuildConfig(new String[] { "@" + FILE_PATH }, messageWriter);
		List resultList = config.getFiles();
		
		assertTrue("correct number of files", resultList.size() == 2);	
		assertTrue(resultList.toString() + new File(TEST_DIR + SOURCE_PATH_1).getCanonicalFile(),
			resultList.contains(new File(TEST_DIR + SOURCE_PATH_1).getCanonicalFile()));
		assertTrue(resultList.toString() + SOURCE_PATH_2,
			resultList.contains(new File(TEST_DIR + SOURCE_PATH_2).getCanonicalFile()));			
	}
	

	//??? do we need to remove this limitation
//	public void testArgInConfigFileAndRelativizingPathParam() throws InvalidInputException {
//		String FILE_PATH =   "@" + TEST_DIR + "configWithArgs.lst";
//		String OUT_PATH = TEST_DIR + "bin";
//		AjBuildConfig config = genBuildConfig(new String[] { FILE_PATH });
//		
//		assertTrue(
//			config.getOutputDir().getPath() + " ?= " + OUT_PATH,
//			config.getOutputDir().getAbsolutePath().equals((new File(OUT_PATH)).getAbsolutePath()));	
//	}
	
	public void testAjFileInclusion() throws InvalidInputException {
		genBuildConfig(new String[] { TEST_DIR + "X.aj", TEST_DIR + "Y.aj"}, messageWriter);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3800.java