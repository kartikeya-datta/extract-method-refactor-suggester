error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8550.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8550.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8550.java
text:
```scala
F@@ile moduleDir = new File(Util.path("..", module));

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC),
 *               2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/

package org.aspectj.internal.build;

import org.apache.tools.ant.*;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline.Argument;
import org.aspectj.internal.tools.ant.taskdefs.*;
import org.aspectj.internal.tools.ant.taskdefs.BuildModule;
import org.aspectj.internal.tools.build.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * Test our integrated taskdef build.
 * This responds to two environment variables:
 * (1) run.build.tests must be defined before
 *   tests that build the tree (and hence take minutes)
 *   will run;
 * (2) build.config takes the same form as it does for the
 *   builder task, e.g., "useEclipseCompiles" will avoid
 *   recompiling with Javac and adopt classes in the 
 *   {module}/bin directories.
 */
public class BuildModuleTest extends TestCase {

    private static boolean printInfoMessages = false;
    private static boolean printedMessage;
    // skip those requiring ajdoc, which requires tools.jar
    // also skip those requiring java5 unless manually set up
    private static final String[] SKIPS 
        = {"aspectjtools", "ajdoc", "aspectj5rt"};
    private static final String SKIP_MESSAGE = 
        "BuildModuleTest: Define \"run.build.tests\" as a system "
        + "property to run tests to build ";
    private static final String BUILD_CONFIG;
    static {
        String config = null;
        try {
            config = System.getProperty("build.config");
        } catch (Throwable t) {
            // ignore
        }
        BUILD_CONFIG = config;
        if (printInfoMessages) {
            System.out.println("BuildModuleTest build.config: " + config);
        }
    }
    
    ArrayList tempFiles = new ArrayList();
    private File jarDir;
    boolean building;  // must be enabled for tests to run
    
	public BuildModuleTest(String name) {
		super(name);
        building = (null != System.getProperty("run.build.tests"));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
        for (Iterator iter = tempFiles.iterator(); iter.hasNext();) {
			File file = (File) iter.next();
            if (!Util.delete(file)) {
                File[] list = file.listFiles();
                if (!Util.isEmpty(list)) {                
                    StringBuffer sb = new StringBuffer();
                    sb.append("warning: BuildModuleTest unable to delete ");
                    sb.append(file.toString());
                    sb.append("\n"); // XXX platform
                    for (int i = 0; i < list.length; i++) {
                        sb.append("  ");
                        sb.append(list[i].toString());
                        sb.append("\n"); // XXX platform
                    }
                    System.err.println(sb.toString());
                }
            }
		}
	}
    
    public void testBuild() {
        checkBuild("build", 
            Checklics.class.getName(), 
            new String[0], // help message
            true); // ant needed
    }
    
    public void testAsm() {
        checkBuild("asm");
    }
    
    public void testRuntime() {
        checkBuild("runtime");
    }

    public void testAspectj5rt() {
        checkBuild("aspectj5rt", 
                "org.aspectj.lang.annotation.Main",
                new String[] {}); // compiler version
    }

    public void testAjbrowser() {
        checkBuild("ajbrowser", 
            "org.aspectj.tools.ajbrowser.Main",
            new String[] {"-noExit", "-version"}); // compiler version
    }

    public void testAjdt() {
        checkBuild("org.aspectj.ajdt.core", 
           "org.aspectj.tools.ajc.Main",
            new String[] { "-noExit", "-version" });
    }
    public void testTestingDrivers() {
        checkBuild("testing-drivers", 
            "org.aspectj.testing.drivers.Harness", 
            new String[] {"-help"});
    }
    
    // ajdoc relies on tools.jar
    public void testAspectjtools() {
        if (!shouldBuild("aspectjtools")) {
            return;
        }
        File baseDir = new File("..");
        File tempBuildDir = new File(baseDir, "aj-build");
        File distDir = new File(tempBuildDir, "dist");
        File jarDir = new File(tempBuildDir, "jars");
        assertTrue(distDir.canWrite() || distDir.mkdirs());
        File productDir = new File(baseDir.getPath() + "/build/products/tools");
        assertTrue(""+productDir, productDir.canRead());
        checkBuildProduct(productDir, baseDir, distDir, jarDir);
    }


    void checkBuildProduct(File productDir, File baseDir, File distDir, File jarDir) {
        if (!shouldBuild(productDir.getPath())) {
            return;
        }
        assertTrue(null != productDir);
        assertTrue(productDir.canRead());

        checkJavac();

        BuildModule task = new BuildModule();
        Project project = new Project();
        task.setProject(project);
        assertTrue(jarDir.canWrite() || jarDir.mkdirs());
        tempFiles.add(jarDir);
        task.setJardir(new Path(project, jarDir.getAbsolutePath()));
        task.setProductdir(new Path(project, productDir.getAbsolutePath()));
        task.setBasedir(new Path(project, baseDir.getAbsolutePath()));
        task.setDistdir(new Path(project, distDir.getAbsolutePath()));
        task.setFailonerror(true);
        if (null != BUILD_CONFIG) {
            task.setBuildConfig(BUILD_CONFIG);
        }
        //task.setVerbose(true);
        task.setCreateinstaller(true);
        task.execute();
        // now run installer and do product tests?
    }

    File getAntJar() {
        return new File("../lib/ant/lib/ant.jar");
    }
    
    File getJarDir() {
        if (null == jarDir) {
            jarDir = new File("tempJarDir");
            tempFiles.add(jarDir);
        }
        if (!jarDir.exists()) {
            assertTrue(jarDir.mkdirs());
        }
        return jarDir;
    }
    
    BuildModule getTask(String module) {
        BuildModule task = new BuildModule();
        Project project = new Project();
        task.setProject(project);
        File jarDir = getJarDir();
        assertTrue(jarDir.canWrite() || jarDir.mkdirs());
        tempFiles.add(jarDir);
        File moduleDir = new File("../" + module);
        assertTrue(moduleDir.canRead());
        task.setModuledir(new Path(project, moduleDir.getAbsolutePath()));
        task.setJardir(new Path(project, jarDir.getAbsolutePath()));
        task.setFailonerror(true);
        if (null != BUILD_CONFIG) {
            task.setBuildConfig(BUILD_CONFIG);
        }
        return task;
    }
    
    void checkBuild(String module) { 
        checkBuild(module, null, null, false);
    }
    
    void checkBuild(String module, 
        String classname, 
        String[] args) {
        checkBuild(module, classname, args, false);
    }

    boolean shouldBuild(String target) {
        if (!building && !printedMessage) {
            System.err.println(SKIP_MESSAGE + target + " (this is the only warning)");
            printedMessage = true;
        }
        for (int i = 0; i < SKIPS.length; i++) {
            if (SKIPS[i].equals(target)) {
                if (printInfoMessages) {
                    System.err.println(target + " skipped build test [" + getClass().getName() + ".shouldBuild(..)]");                
                }
                return false;
            }
        }
        return building;
    }
    void checkBuild(String module, 
        String classname, 
        String[] args,
        boolean addAnt) {
        if (!shouldBuild(module)) {
            return;
        }
        assertTrue(null != module);
        checkJavac();
        
        // run without assembly
        BuildModule task = getTask(module);
        File jar = new File(getJarDir(), module + ".jar");
        task.setAssembleall(false);
        task.execute();
        assertTrue("cannot read " + jar, jar.canRead());
        assertTrue("cannot delete " + jar, jar.delete());

        // run with assembly
        task = getTask(module);
        task.setAssembleall(true);
        task.execute();
        jar = new File(getJarDir(), module + "-all.jar");
        assertTrue("cannot read " + jar, jar.canRead());
        // verify if possible
        if (null == classname) {
            return;
        }
        
        Java java = new Java();
        Project project = task.getProject();
        java.setProject(project);
        java.setFailonerror(true);
        Path cp = new Path(project);
        cp.append(new Path(project, jar.getAbsolutePath()));
        if (addAnt) {
            cp.append(new Path(project, getAntJar().getAbsolutePath()));
        }
        java.setClasspath(cp);
        java.setClassname(classname);
        if (null != args) {
            for (int i = 0; i < args.length; i++) {
                Argument arg = java.createArg();
                arg.setValue(args[i]);
            }
        }
        try {
            java.execute();
        } catch (BuildException e) {
            e.printStackTrace(System.err);
            assertTrue("BuildException running " + classname, false);
        }
    }

    void checkJavac() {
        boolean result = false;
        try {
            result = (null != Class.forName("sun.tools.javac.Main"));
        } catch (Throwable t) {
            // ignore
        }
        if (! result) {
            assertTrue("add tools.jar to the classpath for Ant's use of javac", false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8550.java