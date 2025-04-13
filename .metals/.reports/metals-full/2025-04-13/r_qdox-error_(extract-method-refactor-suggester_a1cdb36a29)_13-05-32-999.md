error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17292.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17292.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17292.java
text:
```scala
S@@tring.valueOf(SCAN_LIMIT)).getMessage());

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.lib.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.openjpa.lib.util.Localizer;

/**
 * Iterator over a file, or over all metadata resources below a given directory.
 *
 * @author Abe White
 * @nojavadoc
 */
public class FileMetaDataIterator implements MetaDataIterator {

    private static final long SCAN_LIMIT = 100000;

    private static final Localizer _loc = Localizer.forPackage
        (FileMetaDataIterator.class);

    private final Iterator _itr;
    private File _file = null;

    /**
     * Constructor; supply metadata file.
     */
    public FileMetaDataIterator(File file) {
        _itr = Collections.singleton(file).iterator();
    }

    /**
     * Constructor; supply root of directory tree to search and optional
     * file filter.
     */
    public FileMetaDataIterator(File dir, MetaDataFilter filter)
        throws IOException {
        if (dir == null)
            _itr = null;
        else {
            Collection metas = new ArrayList();
            FileResource rsrc = (filter == null) ? null : new FileResource();
            scan(dir, filter, rsrc, metas, 0);
            _itr = metas.iterator();
        }
    }

    /**
     * Scan all files below the given one for metadata files, adding them
     * to the given collection.
     */
    private int scan(File file, MetaDataFilter filter, FileResource rsrc,
        Collection metas, int scanned) throws IOException {
        if (scanned > SCAN_LIMIT)
            throw new IllegalStateException(_loc.get("too-many-files",
                String.valueOf(SCAN_LIMIT)));
        scanned++;

        if (filter == null)
            metas.add(file);
        else {
            rsrc.setFile(file);
            if (filter.matches(rsrc))
                metas.add(file);
            else {
                File[] files = file.listFiles();
                if (files != null)
                    for (int i = 0; i < files.length; i++)
                        scanned = scan(files[i], filter, rsrc, metas, scanned);
            }
        }
        return scanned;
    }

    public boolean hasNext() {
        return _itr != null && _itr.hasNext();
    }

    public Object next() throws IOException {
        if (_itr == null)
            throw new NoSuchElementException();

        _file = (File) _itr.next();
        return _file.getAbsoluteFile().toURL();
    }

    public InputStream getInputStream() throws IOException {
        if (_file == null)
            throw new IllegalStateException();
        return new FileInputStream(_file);
    }

    public File getFile() {
        if (_file == null)
            throw new IllegalStateException();
        return _file;
    }

    public void close() {
    }

    private static class FileResource implements MetaDataFilter.Resource {

        private File _file = null;

        public void setFile(File file) {
            _file = file;
        }

        public String getName() {
            return _file.getName();
        }

        public byte[] getContent() throws IOException {
            long len = _file.length();
            if (len <= 0)
                return new byte[0];

            byte[] content = new byte[(int) len];
            FileInputStream fin = new FileInputStream(_file);
            fin.read(content);
            fin.close();
            return content;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17292.java