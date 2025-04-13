error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9180.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9180.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9180.java
text:
```scala
public v@@oid testFindIsolateResources() throws Exception {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
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

package org.apache.tools.ant;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import junit.framework.TestCase;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

/**
 * Test case for ant class loader
 *
 */
public class AntClassLoaderDelegationTest extends TestCase {

    /** Instance of a utility class to use for file operations. */
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    private Project p;

    public AntClassLoaderDelegationTest(String name) {
        super(name);
    }

    public void setUp() {
        p = new Project();
        p.init();
    }

    /** Sample resource present in build/testcases/ */
    private static final String TEST_RESOURCE = "org/apache/tools/ant/IncludeTest.class";
    
    public void testFindResources() throws Exception {
        //System.err.println("loading from: " + AntClassLoader.class.getProtectionDomain().getCodeSource().getLocation());
        // See bug #30161.
        // This path should contain the class files for these testcases:
        String buildTestcases = System.getProperty("build.tests");
        assertNotNull("defined ${build.tests}", buildTestcases);
        assertTrue("have a dir " + buildTestcases, new File(buildTestcases).isDirectory());
        Path path = new Path(p, buildTestcases);
        // A special parent loader which is not the system class loader:
        ClassLoader parent = new ParentLoader();
        // An AntClassLoader which is supposed to delegate to the parent and then to the disk path:
        ClassLoader acl = new AntClassLoader(parent, p, path, true);
        // The intended result URLs:
        URL urlFromPath = new URL(FILE_UTILS.toURI(buildTestcases) + TEST_RESOURCE);
        URL urlFromParent = new URL("http://ant.apache.org/" + TEST_RESOURCE);
        assertEquals("correct resources (regular delegation order)",
            Arrays.asList(new URL[] {urlFromParent, urlFromPath}),
            enum2List(acl.getResources(TEST_RESOURCE)));
        acl = new AntClassLoader(parent, p, path, false);
        assertEquals("correct resources (reverse delegation order)",
            Arrays.asList(new URL[] {urlFromPath, urlFromParent}),
            enum2List(acl.getResources(TEST_RESOURCE)));
    }

    public void NottestFindIsolateResources() throws Exception {
        String buildTestcases = System.getProperty("build.tests");
        assertNotNull("defined ${build.tests}", buildTestcases);
        assertTrue("have a dir " + buildTestcases, new File(buildTestcases).isDirectory());
        Path path = new Path(p, buildTestcases);
        // A special parent loader which is not the system class loader:
        ClassLoader parent = new ParentLoader();

        URL urlFromPath = new URL(FILE_UTILS.toURI(buildTestcases) + TEST_RESOURCE);
        AntClassLoader acl = new AntClassLoader(parent, p, path, false);
        acl.setIsolated(true);
        assertEquals("correct resources (reverse delegation order)",
            Arrays.asList(new URL[] {urlFromPath}),
            enum2List(acl.getResources(TEST_RESOURCE)));
    }
    
    private static List enum2List(Enumeration e) {
        // JDK 1.4: return Collections.list(e);
        List l = new ArrayList();
        while (e.hasMoreElements()) {
            l.add(e.nextElement());
        }
        return l;
    }
    
    /** Special loader that just knows how to find TEST_RESOURCE. */
    private static final class ParentLoader extends ClassLoader {
        
        public ParentLoader() {}
        
        protected Enumeration findResources(String name) throws IOException {
            if (name.equals(TEST_RESOURCE)) {
                return Collections.enumeration(Collections.singleton(new URL("http://ant.apache.org/" + name)));
            } else {
                return Collections.enumeration(Collections.EMPTY_SET);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9180.java