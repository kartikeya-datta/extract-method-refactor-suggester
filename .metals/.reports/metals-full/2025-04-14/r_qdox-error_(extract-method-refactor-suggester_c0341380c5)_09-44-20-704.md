error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8313.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8313.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8313.java
text:
```scala
private static final S@@et<String> notTestPlan = new HashSet<String>();// not full test plans

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

package org.apache.jmeter.gui.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestSuite;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.save.SaveService;
import org.apache.jorphan.collections.HashTree;

/**
 * 
 * Test JMX files to check that they can be loaded OK.
 */
public class TestLoad extends JMeterTestCase {

    private static final String basedir = new File(System.getProperty("user.dir")).getParent();
    private static final File testfiledir = new File(basedir,"bin/testfiles");
    private static final File demofiledir = new File(basedir,"xdocs/demos");
    
    private static final Set notTestPlan = new HashSet();// not full test plans
    
    static{
        notTestPlan.add("load_bug_list.jmx");// used by TestAnchorModifier
        notTestPlan.add("Load_JMeter_Page.jmx");// used by TestAnchorModifier
        notTestPlan.add("ProxyServerTestPlan.jmx");// used by TestSaveService
    }

    private static final FilenameFilter jmxFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith(".jmx");
        }
    };

    private final File testFile;
    private final String parent;
    
    public TestLoad(String name) {
        super(name);
        testFile=null;
        parent=null;
    }

    public TestLoad(String name, File file, String dir) {
        super(name);
        testFile=file;
        parent=dir;
    }

    public static TestSuite suite(){
        TestSuite suite=new TestSuite("Load Test");
        //suite.addTest(new TestLoad("checkGuiPackage"));
        scanFiles(suite,testfiledir);
        scanFiles(suite,demofiledir);
        return suite;
    }

    private static void scanFiles(TestSuite suite, File parent) {
        File testFiles[]=parent.listFiles(jmxFilter);
        String dir = parent.getName();
        for (int i=0; i<testFiles.length; i++){
            suite.addTest(new TestLoad("checkTestFile",testFiles[i],dir));
        }
    }
    
    public void checkTestFile() throws Exception{
        HashTree tree = null;
        try {
            tree =getTree(testFile);
        } catch (Exception e) {
            fail(parent+": "+ testFile.getName()+" caused "+e);
        }
        assertTree(tree);
    }
    
    private void assertTree(HashTree tree) throws Exception {
        final Object object = tree.getArray()[0];
        final String name = testFile.getName();
        
        if (! (object instanceof org.apache.jmeter.testelement.TestPlan) && !notTestPlan.contains(name)){
            fail(parent+ ": " +name+" tree should be TestPlan, but is "+object.getClass().getName());
        }
    }

    private HashTree getTree(File f) throws Exception {
        FileInputStream fis = new FileInputStream(f);
        HashTree tree = null;
        try {
            tree = SaveService.loadTree(fis);
        } finally {
            fis.close();
        }
        return tree;
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8313.java