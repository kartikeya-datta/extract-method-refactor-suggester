error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14119.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14119.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14119.java
text:
```scala
F@@ileProvider fp = getArchive().as(FileProvider.class);

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
package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FilterInputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipExtraField;

/**
 * A Resource representation of an entry in a zipfile.
 * @since Ant 1.7
 */
public class ZipResource extends ArchiveResource {

    private String encoding;
    private ZipExtraField[] extras;
    private int method;

    /**
     * Default constructor.
     */
    public ZipResource() {
    }

    /**
     * Construct a ZipResource representing the specified
     * entry in the specified zipfile.
     * @param z the zipfile as File.
     * @param enc the encoding used for filenames.
     * @param e the ZipEntry.
     */
    public ZipResource(File z, String enc, ZipEntry e) {
        super(z, true);
        setEncoding(enc);
        setEntry(e);
    }

    /**
     * Set the zipfile that holds this ZipResource.
     * @param z the zipfile as a File.
     */
    public void setZipfile(File z) {
        setArchive(z);
    }

    /**
     * Get the zipfile that holds this ZipResource.
     * @return the zipfile as a File.
     */
    public File getZipfile() {
        FileProvider fp = (FileProvider) getArchive().as(FileProvider.class);
        return fp.getFile();
    }

    /**
     * Sets the archive that holds this as a single element Resource
     * collection.
     * @param a the archive as a single element Resource collection.
     */
    public void addConfigured(ResourceCollection a) {
        super.addConfigured(a);
        if (!a.isFilesystemOnly()) {
            throw new BuildException("only filesystem resources are supported");
        }
    }

    /**
     * Set the encoding to use with the zipfile.
     * @param enc the String encoding.
     */
    public void setEncoding(String enc) {
        checkAttributesAllowed();
        encoding = enc;
    }

    /**
     * Get the encoding to use with the zipfile.
     * @return String encoding.
     */
    public String getEncoding() {
        return isReference()
            ? ((ZipResource) getCheckedRef()).getEncoding() : encoding;
    }

    /**
     * Overrides the super version.
     * @param r the Reference to set.
     */
    public void setRefid(Reference r) {
        if (encoding != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /**
     * Return an InputStream for reading the contents of this Resource.
     * @return an InputStream object.
     * @throws IOException if the zip file cannot be opened,
     *         or the entry cannot be read.
     */
    public InputStream getInputStream() throws IOException {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getInputStream();
        }
        final ZipFile z = new ZipFile(getZipfile(), getEncoding());
        ZipEntry ze = z.getEntry(getName());
        if (ze == null) {
            z.close();
            throw new BuildException("no entry " + getName() + " in "
                                     + getArchive());
        }
        return new FilterInputStream(z.getInputStream(ze)) {
            public void close() throws IOException {
                FileUtils.close(in);
                z.close();
            }
            protected void finalize() throws Throwable {
                try {
                    close();
                } finally {
                    super.finalize();
                }
            }
        };
    }

    /**
     * Get an OutputStream for the Resource.
     * @return an OutputStream to which content can be written.
     * @throws IOException if unable to provide the content of this
     *         Resource as a stream.
     * @throws UnsupportedOperationException if OutputStreams are not
     *         supported for this Resource type.
     */
    public OutputStream getOutputStream() throws IOException {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getOutputStream();
        }
        throw new UnsupportedOperationException(
            "Use the zip task for zip output.");
    }

    /**
     * Retrieves extra fields.
     * @return an array of the extra fields
     * @since Ant 1.8.0
     */
    public ZipExtraField[] getExtraFields() {
        if (isReference()) {
            return ((ZipResource) getCheckedRef()).getExtraFields();
        }
        checkEntry();
        if (extras == null) {
            return new ZipExtraField[0];
        }
        return extras;
    }

    /**
     * The compression method that has been used.
     * @since Ant 1.8.0
     */
    public int getMethod() {
        return method;
    }

    /**
     * fetches information from the named entry inside the archive.
     */
    protected void fetchEntry() {
        ZipFile z = null;
        try {
            z = new ZipFile(getZipfile(), getEncoding());
            setEntry(z.getEntry(getName()));
        } catch (IOException e) {
            log(e.getMessage(), Project.MSG_DEBUG);
            throw new BuildException(e);
        } finally {
            ZipFile.closeQuietly(z);
        }
    }

    private void setEntry(ZipEntry e) {
        if (e == null) {
            setExists(false);
            return;
        }
        setName(e.getName());
        setExists(true);
        setLastModified(e.getTime());
        setDirectory(e.isDirectory());
        setSize(e.getSize());
        setMode(e.getUnixMode());
        extras = e.getExtraFields(true);
        method = e.getMethod();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14119.java