error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1432.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1432.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1432.java
text:
```scala
o@@ptions.put(Finder.NOT+Finder.PATH, "*.svn*");

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.find;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.io.IOException;
import java.io.File;

import java.util.HashMap;

public class FileFinderTest extends TestCase {

    private HashMap options;
    private FileFinder finder;
    private String dirStr = "src" + File.separator + "test" + File.separator + "find-data" + File.separator;
    private File dir = new File(dirStr);

    public FileFinderTest(String name) {
        super(name);
    }

    public void setUp() {
        finder = new FileFinder();
        options = new HashMap();
        options.put(Finder.NOT+Finder.PATH, "*CVS*");
        // uncomment for debugging
//        finder.addFindListener( new DebugListener() );
    }

    //-----------------------------------------------------------------------
    // To test: 
    // find(File, Map)

    public void testFindName() {
        options.put(Finder.NAME, "file");
        File[] files = finder.find(new File(dir, "name"), options);
        assertEquals(1, files.length);
    }

    public void testFindIName() {
        options.put(Finder.INAME, "FiLe");
        File[] files = finder.find(new File(dir, "name"), options);
        assertEquals(1, files.length);
    }

    public void testFindPath() {
        options.put(Finder.PATH, dirStr+"path" + File.separator + "dir" + File.separator + "file");
        File[] files = finder.find(new File(dir, "path"), options);
        assertEquals(1, files.length);
    }

    public void testFindIPath() {
        options.put(Finder.IPATH, dirStr+"PAth" + File.separator + "dIR" + File.separator + "fILe");
        File[] files = finder.find(new File(dir, "path"), options);
        assertEquals(1, files.length);
    }

    public void testFindNotPath() {
        options.put(Finder.NOT+Finder.PATH, dirStr+"*");
        File[] files = finder.find(new File(dir, "path"), options);
        assertEquals(0, files.length);
    }

    public void testFindRegex() {
        options.put(Finder.REGEX, escapePath(dirStr+"regex" + File.separator + "f.*"));
        File[] files = finder.find(new File(dir, "regex"), options);
        assertEquals(3, files.length);
    }

    public void testFindIRegex() {
        options.put(Finder.IREGEX, escapePath(dirStr+"REgeX" + File.separator + "F.*"));
        File[] files = finder.find(new File(dir, "regex"), options);
        assertEquals(3, files.length);
    }

    public void testFindEmpty() {
        options.put(Finder.EMPTY, "true");
        File[] files = finder.find(new File(dir, "empty"), options);
        assertEquals(1, files.length);
    }

    public void testFindSize() {
        options.put(Finder.SIZE, "1");
        File[] files = finder.find(new File(dir, "size"), options);
        assertEquals(1, files.length);
    }

    // finds one in file and also one in dir as 
    // CVS needs a file in dir for people to commonly have it checked out
    public void testFindTypeF() {
        options.put(Finder.TYPE, "f");
        File[] files = finder.find(new File(dir, "type"), options);
        assertEquals(2, files.length);
    }

    public void testFindTypeD() {
        options.put(Finder.TYPE, "d");
        File[] files = finder.find(new File(dir, "type"), options);
        assertEquals(2, files.length);
    }

    public void testCanReadTrue() {
        options.put(Finder.CAN_WRITE, "true");
        File[] files = finder.find(new File(dir, "can_write"), options);
        assertEquals(2, files.length);
    }

    public void testCanWriteTrue() {
        options.put(Finder.CAN_READ, "true");
        File[] files = finder.find(new File(dir, "can_read"), options);
        assertEquals(2, files.length);
    }

    public void testCanReadFalse() {
        options.put(Finder.CAN_WRITE, "false");
        File[] files = finder.find(new File(dir, "can_write"), options);
        assertEquals(0, files.length);
    }

    public void testCanWriteFalse() {
        options.put(Finder.CAN_READ, "false");
        File[] files = finder.find(new File(dir, "can_read"), options);
        assertEquals(0, files.length);
    }

    private static String escapePath(String text) {
        String repl = "\\";
        String with = "\\\\";

        StringBuffer buf = new StringBuffer(text.length());
        int start = 0, end = 0;
        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

}

class DebugListener implements FindListener {
    public void fileFound(FindEvent fe) {
        System.out.println("EVENT: "+fe);
    }
    public void directoryStarted(FindEvent fe) {
        System.out.println("EVENT: "+fe);
    }
    public void directoryFinished(FindEvent fe) {
        System.out.println("EVENT: "+fe);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1432.java