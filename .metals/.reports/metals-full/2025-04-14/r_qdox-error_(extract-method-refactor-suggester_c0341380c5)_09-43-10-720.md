error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16474.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16474.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16474.java
text:
```scala
p@@roject.setBasedir(System.getProperty("root"));

/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.types;

import junit.framework.TestCase;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 * JUnit 3 testcases for org.apache.tools.ant.CommandlineJava
 *
 */
public class CommandlineJavaTest extends TestCase {

    private String cloneVm;

    public CommandlineJavaTest(String name) {
        super(name);
    }

    private Project project;

    public void setUp() {
        project = new Project();
        project.setBasedir(".");
        project.setProperty("build.sysclasspath", "ignore");
        cloneVm = System.getProperty("build.clonevm");
        if (cloneVm != null) {
            System.setProperty("build.clonevm", "false");
        }
    }

    public void tearDown() {
        if (cloneVm != null) {
            System.setProperty("build.clonevm", cloneVm);
        }
    }

    public void testGetCommandline() throws Exception {
        CommandlineJava c = new CommandlineJava();
        c.createArgument().setValue("org.apache.tools.ant.CommandlineJavaTest");
        c.setClassname("junit.textui.TestRunner");
        c.createVmArgument().setValue("-Djava.compiler=NONE");
        String[] s = c.getCommandline();
        assertEquals("no classpath", 4, s.length);
        /*
         * After changing CommandlineJava to search for the java
         * executable, I don't know, how to tests the value returned
         * here without using the same logic as applied in the class
         * itself.
         *
         * assertTrue("no classpath", "java", s[0]);
         */
        assertEquals("no classpath", "-Djava.compiler=NONE", s[1]);
        assertEquals("no classpath", "junit.textui.TestRunner", s[2]);
        assertEquals("no classpath",
                     "org.apache.tools.ant.CommandlineJavaTest", s[3]);
        try {
            CommandlineJava c2 = (CommandlineJava) c.clone();
        } catch (NullPointerException ex) {
            fail("cloning should work without classpath specified");
        }

        c.createClasspath(project).setLocation(project.resolveFile("build.xml"));
        c.createClasspath(project).setLocation(project.resolveFile(
            System.getProperty("ant.home")+"/lib/ant.jar"));
        s = c.getCommandline();
        assertEquals("with classpath", 6, s.length);
        //        assertEquals("with classpath", "java", s[0]);
        assertEquals("with classpath", "-Djava.compiler=NONE", s[1]);
        assertEquals("with classpath", "-classpath", s[2]);
        assertTrue("build.xml contained",
               s[3].indexOf("build.xml"+java.io.File.pathSeparator) >= 0);
        assertTrue("ant.jar contained", s[3].endsWith("ant.jar"));
        assertEquals("with classpath", "junit.textui.TestRunner", s[4]);
        assertEquals("with classpath",
                     "org.apache.tools.ant.CommandlineJavaTest", s[5]);
    }

    public void testJarOption() throws Exception {
        CommandlineJava c = new CommandlineJava();
        c.createArgument().setValue("arg1");
        c.setJar("myfile.jar");
        c.createVmArgument().setValue("-classic");
        c.createVmArgument().setValue("-Dx=y");
        String[] s = c.getCommandline();
        assertEquals("-classic", s[1]);
        assertEquals("-Dx=y", s[2]);
        assertEquals("-jar", s[3]);
        assertEquals("myfile.jar", s[4]);
        assertEquals("arg1", s[5]);
    }

    public void testSysproperties() {
        String currentClasspath = System.getProperty("java.class.path");
        assertNotNull(currentClasspath);
        assertNull(System.getProperty("key"));
        CommandlineJava c = new CommandlineJava();
        Environment.Variable v = new Environment.Variable();
        v.setKey("key");
        v.setValue("value");
        c.addSysproperty(v);

        project.setProperty("key2", "value2");
        PropertySet ps = new PropertySet();
        ps.setProject(project);
        ps.appendName("key2");
        c.addSyspropertyset(ps);

        try {
            c.setSystemProperties();
            String newClasspath = System.getProperty("java.class.path");
            assertNotNull(newClasspath);
            assertEquals(currentClasspath, newClasspath);
            assertNotNull(System.getProperty("key"));
            assertEquals("value", System.getProperty("key"));
            assertTrue(System.getProperties().containsKey("java.class.path"));
            assertNotNull(System.getProperty("key2"));
            assertEquals("value2", System.getProperty("key2"));
        } finally {
            c.restoreSystemProperties();
        }
        assertNull(System.getProperty("key"));
        assertNull(System.getProperty("key2"));
    }

    public void testAssertions() throws Exception {
        if (JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_2)
 JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_3)) {
            return;
        }

        CommandlineJava c = new CommandlineJava();
        c.createArgument().setValue("org.apache.tools.ant.CommandlineJavaTest");
        c.setClassname("junit.textui.TestRunner");
        c.createVmArgument().setValue("-Djava.compiler=NONE");
        Assertions a = new Assertions();
        a.setProject(project);
        Assertions.EnabledAssertion ea = new Assertions.EnabledAssertion();
        ea.setClass("junit.textui.TestRunner");
        a.addEnable(ea);
        c.setAssertions(a);

        String[] expected = new String[] {
            null,
            "-Djava.compiler=NONE",
            "-ea:junit.textui.TestRunner",
            "junit.textui.TestRunner",
            "org.apache.tools.ant.CommandlineJavaTest",
        };
            
        // only the second iteration would pass because of PR 27218
        for (int i = 0; i < 3; i++) {
            String[] s = c.getCommandline();
            assertEquals(expected.length, s.length);
            for (int j = 1; j < expected.length; j++) {
                assertEquals(expected[j], s[j]);
            }
        }
        CommandlineJava c2 = (CommandlineJava) c.clone();
        String[] s = c2.getCommandline();
        assertEquals(expected.length, s.length);
        for (int j = 1; j < expected.length; j++) {
            assertEquals(expected[j], s[j]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16474.java