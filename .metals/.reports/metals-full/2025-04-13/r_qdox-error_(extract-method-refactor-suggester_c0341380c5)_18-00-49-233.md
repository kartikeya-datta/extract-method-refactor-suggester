error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4378.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4378.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4378.java
text:
```scala
S@@tringBuilder sb = new StringBuilder();

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

package org.apache.jorphan.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * Utility class to handle text files as a single lump of text.
 * <p>
 * Note this is just as memory-inefficient as handling a text file can be. Use
 * with restraint.
 *
 * @version $Revision$
 */
public class TextFile extends File {
    private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     * File encoding. null means use the platform's default.
     */
    private String encoding = null;

    /**
     * Create a TextFile object to handle the named file with the given
     * encoding.
     *
     * @param filename
     *            File to be read & written through this object.
     * @param encoding
     *            Encoding to be used when reading & writing this file.
     */
    public TextFile(File filename, String encoding) {
        super(filename.toString());
        setEncoding(encoding);
    }

    /**
     * Create a TextFile object to handle the named file with the platform
     * default encoding.
     *
     * @param filename
     *            File to be read & written through this object.
     */
    public TextFile(File filename) {
        super(filename.toString());
    }

    /**
     * Create a TextFile object to handle the named file with the platform
     * default encoding.
     *
     * @param filename
     *            Name of the file to be read & written through this object.
     */
    public TextFile(String filename) {
        super(filename);
    }

    /**
     * Create a TextFile object to handle the named file with the given
     * encoding.
     *
     * @param filename
     *            Name of the file to be read & written through this object.
     * @param encoding
     *            Encoding to be used when reading & writing this file.
     */
    public TextFile(String filename, String encoding) {
        super(filename);
    }

    /**
     * Create the file with the given string as content -- or replace it's
     * content with the given string if the file already existed.
     *
     * @param body
     *            New content for the file.
     */
    public void setText(String body) {
        Writer writer = null;
        try {
            if (encoding == null) {
                writer = new FileWriter(this);
            } else {
                writer = new OutputStreamWriter(new FileOutputStream(this), encoding);
            }
            writer.write(body);
            writer.flush();
        } catch (IOException ioe) {
            log.error("", ioe);
        } finally {
            JOrphanUtils.closeQuietly(writer);
        }
    }

    /**
     * Read the whole file content and return it as a string.
     *
     * @return the content of the file
     */
    public String getText() {
        String lineEnd = System.getProperty("line.separator"); //$NON-NLS-1$
        StringBuffer sb = new StringBuffer();
        Reader reader = null;
        BufferedReader br = null;
        try {
            if (encoding == null) {
                reader = new FileReader(this);
            } else {
                reader = new InputStreamReader(new FileInputStream(this), encoding);
            }
            br = new BufferedReader(reader);
            String line = "NOTNULL"; //$NON-NLS-1$
            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    sb.append(line + lineEnd);
                }
            }
        } catch (IOException ioe) {
            log.error("", ioe); //$NON-NLS-1$
        } finally {
            JOrphanUtils.closeQuietly(br); // closes reader as well
        }

        return sb.toString();
    }

    /**
     * @return Encoding being used to read & write this file.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param string
     *            Encoding to be used to read & write this file.
     */
    public void setEncoding(String string) {
        encoding = string;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4378.java