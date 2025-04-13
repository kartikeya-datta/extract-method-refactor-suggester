error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7204.java
text:
```scala
S@@tring[] extensions = {"xml", "txt"};

package org.apache.commons.io;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.testtools.FileBasedTestCase;

/**
 * Test cases for FileUtils.listFiles() methods.
 */
public class FileUtilsListFilesTestCase extends FileBasedTestCase {

    public FileUtilsListFilesTestCase(String name) {
        super(name);
    }
    
    private File getLocalTestDirectory() {
        return new File(getTestDirectory(), "list-files");
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        File dir = getLocalTestDirectory();
        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
        }
        dir.mkdirs();
        File file = new File(dir, "dummy-build.xml");
        FileUtils.touch(file);
        file = new File(dir, "README");
        FileUtils.touch(file);
        
        dir = new File(dir, "subdir1");
        dir.mkdirs();
        file = new File(dir, "dummy-build.xml");
        FileUtils.touch(file);
        file = new File(dir, "dummy-readme.txt");
        FileUtils.touch(file);
        
        dir = new File(dir, "subsubdir1");
        dir.mkdirs();
        file = new File(dir, "dummy-file.txt");
        FileUtils.touch(file);
        file = new File(dir, "dummy-index.html");
        FileUtils.touch(file);
        
        dir = dir.getParentFile();
        dir = new File(dir, "CVS");
        dir.mkdirs();
        file = new File(dir, "Entries");
        FileUtils.touch(file);
        file = new File(dir, "Repository");
        FileUtils.touch(file);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        File dir = getLocalTestDirectory();
        FileUtils.deleteDirectory(dir);
    }
    
    private Collection filesToFilenames(Collection files) {
        Collection filenames = new java.util.ArrayList(files.size());
        Iterator i = files.iterator();
        while (i.hasNext()) {
            filenames.add(((File)i.next()).getName());
        }
        return filenames;
    }
    
    public void testListFilesByExtension() throws Exception {
        final String[] extensions = {"xml", "txt"};
        
        Collection files = FileUtils.listFiles(getLocalTestDirectory(), extensions, false);
        assertEquals(1, files.size());
        Collection filenames = filesToFilenames(files);
        assertTrue(filenames.contains("dummy-build.xml"));
        assertFalse(filenames.contains("README"));
        assertFalse(filenames.contains("dummy-file.txt"));
        
        files = FileUtils.listFiles(getLocalTestDirectory(), extensions, true);
        filenames = filesToFilenames(files);
        assertEquals(4, filenames.size());
        assertTrue(filenames.contains("dummy-file.txt"));
        assertFalse(filenames.contains("dummy-index.html"));
        
        files = FileUtils.listFiles(getLocalTestDirectory(), null, false);
        assertEquals(2, files.size());
        filenames = filesToFilenames(files);
        assertTrue(filenames.contains("dummy-build.xml"));
        assertTrue(filenames.contains("README"));
        assertFalse(filenames.contains("dummy-file.txt"));
        
    }

    public void testListFiles() throws Exception {
        Collection files;
        Collection filenames;
        IOFileFilter fileFilter;
        IOFileFilter dirFilter;
        
        //First, find non-recursively
        fileFilter = FileFilterUtils.trueFileFilter();
        files = FileUtils.listFiles(getLocalTestDirectory(), fileFilter, null);
        filenames = filesToFilenames(files);
        assertTrue("'dummy-build.xml' is missing", filenames.contains("dummy-build.xml"));
        assertFalse("'dummy-index.html' shouldn't be found", filenames.contains("dummy-index.html"));
        assertFalse("'Entries' shouldn't be found", filenames.contains("Entries"));
        
        //Second, find recursively
        fileFilter = FileFilterUtils.trueFileFilter();
        dirFilter = FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("CVS"));
        files = FileUtils.listFiles(getLocalTestDirectory(), fileFilter, dirFilter);
        filenames = filesToFilenames(files);
        assertTrue("'dummy-build.xml' is missing", filenames.contains("dummy-build.xml"));
        assertTrue("'dummy-index.html' is missing", filenames.contains("dummy-index.html"));
        assertFalse("'Entries' shouldn't be found", filenames.contains("Entries"));
        
        //Do the same as above but now with the filter coming from FileFilterUtils
        fileFilter = FileFilterUtils.trueFileFilter();
        dirFilter = FileFilterUtils.makeCVSAware(null);
        files = FileUtils.listFiles(getLocalTestDirectory(), fileFilter, dirFilter);
        filenames = filesToFilenames(files);
        assertTrue("'dummy-build.xml' is missing", filenames.contains("dummy-build.xml"));
        assertTrue("'dummy-index.html' is missing", filenames.contains("dummy-index.html"));
        assertFalse("'Entries' shouldn't be found", filenames.contains("Entries"));

        //Again with the CVS filter but now with a non-null parameter
        fileFilter = FileFilterUtils.trueFileFilter();
        dirFilter = FileFilterUtils.prefixFileFilter("sub");
        dirFilter = FileFilterUtils.makeCVSAware(dirFilter);
        files = FileUtils.listFiles(getLocalTestDirectory(), fileFilter, dirFilter);
        filenames = filesToFilenames(files);
        assertTrue("'dummy-build.xml' is missing", filenames.contains("dummy-build.xml"));
        assertTrue("'dummy-index.html' is missing", filenames.contains("dummy-index.html"));
        assertFalse("'Entries' shouldn't be found", filenames.contains("Entries"));

        try {
            FileUtils.listFiles(getLocalTestDirectory(), null, null);
            fail("Expected error about null parameter");
        } catch (Exception e) {
            //fine
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7204.java