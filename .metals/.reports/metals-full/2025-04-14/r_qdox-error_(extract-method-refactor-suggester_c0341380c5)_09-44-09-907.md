error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9208.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9208.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9208.java
text:
```scala
"Unable to load resource")@@;

/*
 * Copyright  2001-2002,2004 The Apache Software Foundation
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
package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import java.io.File;

/**
 * Test the load file task
 *
 * @created 10 December 2001
 */
public class LoadFileTest extends BuildFileTest {

    /**
     * Constructor for the LoadFileTest object
     *
     * @param name Description of Parameter
     */
    public LoadFileTest(String name) {
        super(name);
    }


    /**
     * The JUnit setup method
     */
    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/loadfile.xml");
    }


    /**
     * The teardown method for JUnit
     */
    public void tearDown() {
        executeTarget("cleanup");
    }


    /**
     * A unit test for JUnit
     */
    public void testNoSourcefileDefined() {
        expectBuildException("testNoSourcefileDefined",
                "source file not defined");
    }


    /**
     * A unit test for JUnit
     */
    public void testNoPropertyDefined() {
        expectBuildException("testNoPropertyDefined",
                "output property not defined");
    }


    /**
     * A unit test for JUnit
     */
    public void testNoSourcefilefound() {
        expectBuildExceptionContaining("testNoSourcefilefound",
                "File not found",
                "Unable to load file");
    }

    /**
     * A unit test for JUnit
     */
    public void testFailOnError()
            throws BuildException {
        expectPropertyUnset("testFailOnError","testFailOnError");
    }


    /**
     * A unit test for JUnit
     */
    public void testLoadAFile()
            throws BuildException {
        executeTarget("testLoadAFile");
        if(project.getProperty("testLoadAFile").indexOf("eh?")<0) {
            fail("property is not all in the file");
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testLoadAFileEnc()
            throws BuildException {
        executeTarget("testLoadAFileEnc");
        if(project.getProperty("testLoadAFileEnc")==null) {
            fail("file load failed");
        }
    }

    /**
     * A unit test for JUnit
     */
    public void testEvalProps()
            throws BuildException {
        executeTarget("testEvalProps");
        if(project.getProperty("testEvalProps").indexOf("rain")<0) {
            fail("property eval broken");
        }
    }

    /**
     * Test FilterChain and FilterReaders
     */
    public void testFilterChain()
            throws BuildException {
        executeTarget("testFilterChain");
        if(project.getProperty("testFilterChain").indexOf("World!")<0) {
            fail("Filter Chain broken");
        }
    }

    /**
     * Test StripJavaComments filterreader functionality.
     */
    public final void testStripJavaComments()
            throws BuildException {
        executeTarget("testStripJavaComments");
        final String expected = project.getProperty("expected");
        final String generated = project.getProperty("testStripJavaComments");
        assertEquals(expected, generated);
    }

    /**
     * A unit test for JUnit
     */
    public void testOneLine()
            throws BuildException {
            expectPropertySet("testOneLine","testOneLine","1,2,3,4");

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9208.java