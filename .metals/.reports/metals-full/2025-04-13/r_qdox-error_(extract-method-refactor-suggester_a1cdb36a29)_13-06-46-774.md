error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7252.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7252.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7252.java
text:
```scala
private static final A@@rrayList fileList = new ArrayList();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.commons.compress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 * Test that can read various archive file examples.
 * 
 * This is a very simple implementation.
 * 
 * Files must be in resources/archives, and there must be a file.txt containing
 * the list of files in the archives.
 * 
 * The class uses nested suites in order to be able to name the test after the file name,
 * as JUnit does not allow one to change the display name of a test.
 */
public class ArchiveReadTests extends AbstractTestCase {
    
    final static ClassLoader classLoader = ArchiveReadTests.class.getClassLoader();

    private File file;
    private static ArrayList fileList = new ArrayList();
    
    public ArchiveReadTests(String name) {
        super(name);
    }
    
    private ArchiveReadTests(String name, File file){
        super(name);
        this.file = file;
    }
    
    public static TestSuite suite() throws IOException{
        TestSuite suite = new TestSuite("ArchiveReadTests");
        File arcdir =new File(classLoader.getResource("archives").getFile());
        assertTrue(arcdir.exists());
        File listing= new File(arcdir,"files.txt");
        assertTrue("files.txt is readable",listing.canRead());
        BufferedReader br = new BufferedReader(new FileReader(listing));
        String line;
        while ((line=br.readLine())!=null){
            if (line.startsWith("#")){
                continue;
            }
            fileList.add(line);
        }
        br.close();
        File[]files=arcdir.listFiles();
        for (int i=0; i<files.length; i++){
            final File file = files[i];
            if (file.getName().endsWith(".txt")){
                continue;
            }
            // Cannot handle these tar files yet 
            if (file.getName().equals("SunOS_cAEf.tar")
 file.getName().equals("FreeBSD_pax.tar")
 file.getName().equals("SunOS_cEf.tar")){
                continue;
            }
            // Appears to be the only way to give the test a variable name
            TestSuite namedSuite = new TestSuite(file.getName());
            Test test = new ArchiveReadTests("testArchive", file);
            namedSuite.addTest(test);
            suite.addTest(namedSuite);
        }        
        return suite;
    }
    
    // files.txt contains size and filename
    protected String getExpectedString(ArchiveEntry entry) {
        return entry.getSize() + " " + entry.getName();
    }

    public void testArchive() throws Exception{
        ArrayList expected=(ArrayList) fileList.clone();
        try {
           checkArchiveContent(file, expected);
        } catch (ArchiveException e) {
            fail("Problem checking "+file);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7252.java