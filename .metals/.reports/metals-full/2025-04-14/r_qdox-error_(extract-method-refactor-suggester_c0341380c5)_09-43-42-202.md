error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11009.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11009.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 840
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11009.java
text:
```scala
public final class XPathWrapper {

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

p@@ackage org.apache.jmeter.functions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.xml.sax.SAXException;

/**
 * This class wraps the XPathFileContainer for use across multiple threads.
 *
 * It maintains a list of nodelist containers, one for each file/xpath combination
 *
 */
public class XPathWrapper {

    private static final Logger log = LoggingManager.getLoggerForClass();

    /*
     * This Map serves two purposes:
     * - maps names to  containers
     * - ensures only one container per file across all threads
     * The key is the concatenation of the file name and the XPath string
     */
    //@GuardedBy("fileContainers")
    private static final Map<String, XPathFileContainer> fileContainers =
        new HashMap<String, XPathFileContainer>();

    /* The cache of file packs - for faster local access */
    private static final ThreadLocal<Map<String, XPathFileContainer>> filePacks =
        new ThreadLocal<Map<String, XPathFileContainer>>() {
        @Override
        protected Map<String, XPathFileContainer> initialValue() {
            return new HashMap<String, XPathFileContainer>();
        }
    };

    private XPathWrapper() {// Prevent separate instantiation
        super();
    }

    private static XPathFileContainer open(String file, String xpathString) {
        String tname = Thread.currentThread().getName();
        log.info(tname+": Opening " + file);
        XPathFileContainer frcc=null;
        try {
            frcc = new XPathFileContainer(file, xpathString);
        } catch (FileNotFoundException e) {
            log.warn(e.getLocalizedMessage());
        } catch (IOException e) {
            log.warn(e.getLocalizedMessage());
        } catch (ParserConfigurationException e) {
            log.warn(e.getLocalizedMessage());
        } catch (SAXException e) {
            log.warn(e.getLocalizedMessage());
        } catch (TransformerException e) {
            log.warn(e.getLocalizedMessage());
        }
        return frcc;
    }

    /**
     * Not thread-safe - must be called from a synchronized method.
     *
     * @param file
     * @param xpathString
     * @return the next row from the file container
     */
    public static String getXPathString(String file, String xpathString) {
        Map<String, XPathFileContainer> my = filePacks.get();
        String key = file+xpathString;
        XPathFileContainer xpfc = my.get(key);
        if (xpfc == null) // We don't have a local copy
        {
            synchronized(fileContainers){
                xpfc = fileContainers.get(key);
                if (xpfc == null) { // There's no global copy either
                    xpfc=open(file, xpathString);
                }
                if (xpfc != null) {
                    fileContainers.put(key, xpfc);// save the global copy
                }
            }
            // TODO improve the error handling
            if (xpfc == null) {
                log.error("XPathFileContainer is null!");
                return ""; //$NON-NLS-1$
            }
            my.put(key,xpfc); // save our local copy
        }
        if (xpfc.size()==0){
            log.warn("XPathFileContainer has no nodes: "+file+" "+xpathString);
            return ""; //$NON-NLS-1$
        }
        int currentRow = xpfc.nextRow();
        log.debug("getting match number " + currentRow);
        return xpfc.getXPathString(currentRow);
    }

    public static void clearAll() {
        log.debug("clearAll()");
        filePacks.get().clear();
        String tname = Thread.currentThread().getName();
        log.info(tname+": clearing container");
        synchronized (fileContainers) {
            fileContainers.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11009.java