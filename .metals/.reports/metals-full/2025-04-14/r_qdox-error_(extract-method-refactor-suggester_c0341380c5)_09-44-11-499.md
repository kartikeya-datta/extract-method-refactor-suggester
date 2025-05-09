error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2576.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2576.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2576.java
text:
```scala
public S@@tring next() throws IOException {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.lib.meta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Iterator over all metadata resources in a given zip input stream.
 *
 * @author Abe White
 * @nojavadoc
 */
public class ZipStreamMetaDataIterator
    implements MetaDataIterator, MetaDataFilter.Resource {

    private final ZipInputStream _stream;
    private final MetaDataFilter _filter;
    private ZipEntry _entry = null;
    private ZipEntry _last = null;
    private byte[] _buf = null;

    /**
     * Constructor; supply zip stream and optional metadata filter.
     */
    public ZipStreamMetaDataIterator(ZipInputStream stream,
        MetaDataFilter filter) {
        _stream = stream;
        _filter = filter;
    }

    public boolean hasNext() throws IOException {
        if (_stream == null)
            return false;
        if (_entry != null)
            return true;

        // close last rsrc
        if (_buf == null && _last != null)
            _stream.closeEntry();
        _last = null;
        _buf = null;

        // search for next file
        ZipEntry entry;
        while (_entry == null && (entry = _stream.getNextEntry()) != null) {
            _entry = entry;
            if (_filter != null && !_filter.matches(this)) {
                _entry = null;
                _stream.closeEntry();
            }
        }
        return _entry != null;
    }

    public Object next() throws IOException {
        if (!hasNext())
            throw new NoSuchElementException();
        String ret = _entry.getName();
        _last = _entry;
        _entry = null;
        return ret;
    }

    public InputStream getInputStream() {
        if (_last == null)
            throw new IllegalStateException();

        if (_buf != null)
            return new ByteArrayInputStream(_buf);
        return new NoCloseInputStream();
    }

    public File getFile() {
        return null;
    }

    public void close() {
        try {
            _stream.close();
        } catch (IOException ioe) {
        }
    }

    //////////////////////////////////////////
    // MetaDataFilter.Resource implementation
    //////////////////////////////////////////

    public String getName() {
        return _entry.getName();
    }

    public byte[] getContent() throws IOException {
        // buffer content so that future calls to getInputStream can read
        // the same data
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int r; (r = _stream.read(buf)) != -1; bout.write(buf, 0, r)) ;
        _buf = bout.toByteArray();
        _stream.closeEntry();
        return _buf;
    }

    /**
     * Non-closing input stream used to make sure the underlying zip
     * stream is not closed.
     */
    private class NoCloseInputStream extends InputStream {

        public int available() throws IOException {
            return _stream.available();
        }

        public int read() throws IOException {
            return _stream.read();
        }

        public int read(byte[] b, int off, int len) throws IOException {
            return _stream.read(b, off, len);
        }

        public void close() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2576.java