error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1118.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1118.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1118.java
text:
```scala
i@@f (! success && (stream != null)) {

package org.apache.lucene.index;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.OutputStream;
import org.apache.lucene.store.Lock;
import java.util.HashMap;
import java.io.IOException;


/**
 * Class for accessing a compound stream.
 * This class implements a directory, but is limited to only read operations.
 * Directory methods that would normally modify data throw an exception.
 *
 * @author Dmitry Serebrennikov
 * @version $Id$
 */
class CompoundFileReader extends Directory {

    private static final class FileEntry {
        long offset;
        long length;
    }


    // Base info
    private Directory directory;
    private String fileName;

    // Reference count
    private boolean open;

    private InputStream stream;
    private HashMap entries = new HashMap();


    public CompoundFileReader(Directory dir, String name)
    throws IOException
    {
        directory = dir;
        fileName = name;

        boolean success = false;

        try {
            stream = dir.openFile(name);

            // read the directory and init files
            int count = stream.readVInt();
            FileEntry entry = null;
            for (int i=0; i<count; i++) {
                long offset = stream.readLong();
                String id = stream.readString();

                if (entry != null) {
                    // set length of the previous entry
                    entry.length = offset - entry.offset;
                }

                entry = new FileEntry();
                entry.offset = offset;
                entries.put(id, entry);
            }

            // set the length of the final entry
            if (entry != null) {
                entry.length = stream.length() - entry.offset;
            }

            success = true;

        } finally {
            if (! success) {
                try {
                    stream.close();
                } catch (IOException e) { }
            }
        }
    }

    public Directory getDirectory() {
        return directory;
    }

    public String getName() {
        return fileName;
    }

    public synchronized void close() throws IOException {
        if (stream == null)
            throw new IOException("Already closed");

        entries.clear();
        stream.close();
        stream = null;
    }

    public synchronized InputStream openFile(String id)
    throws IOException
    {
        if (stream == null)
            throw new IOException("Stream closed");

        FileEntry entry = (FileEntry) entries.get(id);
        if (entry == null)
            throw new IOException("No sub-file with id " + id + " found");

        return new CSInputStream(stream, entry.offset, entry.length);
    }

    /** Returns an array of strings, one for each file in the directory. */
    public String[] list() {
        String res[] = new String[entries.size()];
        return (String[]) entries.keySet().toArray(res);
    }

    /** Returns true iff a file with the given name exists. */
    public boolean fileExists(String name) {
        return entries.containsKey(name);
    }

    /** Returns the time the named file was last modified. */
    public long fileModified(String name) throws IOException {
        return directory.fileModified(fileName);
    }

    /** Set the modified time of an existing file to now. */
    public void touchFile(String name) throws IOException {
        directory.touchFile(fileName);
    }

    /** Removes an existing file in the directory. */
    public void deleteFile(String name)
    {
        throw new UnsupportedOperationException();
    }

    /** Renames an existing file in the directory.
    If a file already exists with the new name, then it is replaced.
    This replacement should be atomic. */
    public void renameFile(String from, String to)
    {
        throw new UnsupportedOperationException();
    }

    /** Returns the length of a file in the directory. */
    public long fileLength(String name)
    throws IOException
    {
        FileEntry e = (FileEntry) entries.get(name);
        if (e == null)
            throw new IOException("File " + name + " does not exist");
        return e.length;
    }

    /** Creates a new, empty file in the directory with the given name.
      Returns a stream writing this file. */
    public OutputStream createFile(String name)
    {
        throw new UnsupportedOperationException();
    }

    /** Construct a {@link Lock}.
     * @param name the name of the lock file
     */
    public Lock makeLock(String name)
    {
        throw new UnsupportedOperationException();
    }

    /** Implementation of an InputStream that reads from a portion of the
     *  compound file. The visibility is left as "package" *only* because
     *  this helps with testing since JUnit test cases in a different class
     *  can then access package fields of this class.
     */
    static final class CSInputStream extends InputStream {

        InputStream base;
        long fileOffset;

        CSInputStream(final InputStream base, final long fileOffset, final long length)
          throws IOException
        {
            this.base = base;
            this.fileOffset = fileOffset;
            this.length = length;   // variable in the superclass
        }

        /** Expert: implements buffer refill.  Reads bytes from the current
         *  position in the input.
         * @param b the array to read bytes into
         * @param offset the offset in the array to start storing bytes
         * @param length the number of bytes to read
         */
        protected void readInternal(byte[] b, int offset, int len)
        throws IOException
        {
            synchronized (base) {
              long start = getFilePointer();
              if(start + len > length)
                throw new IOException("read past EOF");
              base.seek(fileOffset + start);
              base.readBytes(b, offset, len);
            }
        }

        /** Expert: implements seek.  Sets current position in this file, where
         *  the next {@link #readInternal(byte[],int,int)} will occur.
         * @see #readInternal(byte[],int,int)
         */
        protected void seekInternal(long pos) throws IOException {}

        /** Closes the stream to futher operations. */
        public void close() throws IOException {}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1118.java