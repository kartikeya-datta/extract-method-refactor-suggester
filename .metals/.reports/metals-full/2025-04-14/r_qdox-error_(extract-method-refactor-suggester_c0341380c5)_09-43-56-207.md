error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10194.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10194.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10194.java
text:
```scala
v@@oid write(File file) throws IOException;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.util.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.wicket.IClusterable;


/**
 * <p>
 * This class represents a file or form item that was received within a
 * <code>multipart/form-data</code> POST request.
 * 
 * <p>
 * After retrieving an instance of this class from a
 * {@link org.apache.wicket.util.upload.FileUpload FileUpload} instance, you may either request all
 * contents of the file at once using {@link #get()} or request an {@link java.io.InputStream
 * InputStream} with {@link #getInputStream()} and process the file without attempting to load it
 * into memory, which may come handy with large files.
 * 
 * <p>
 * While this interface does not extend <code>javax.activation.DataSource</code> per se (to avoid a
 * seldom used dependency), several of the defined methods are specifically defined with the same
 * signatures as methods in that interface. This allows an implementation of this interface to also
 * implement <code>javax.activation.DataSource</code> with minimal additional work.
 * 
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:sean@informage.net">Sean Legassick</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 */
public interface FileItem extends IClusterable
{


	// ------------------------------- Methods from javax.activation.DataSource


	/**
	 * Returns an {@link java.io.InputStream InputStream} that can be used to retrieve the contents
	 * of the file.
	 * 
	 * @return An {@link java.io.InputStream InputStream} that can be used to retrieve the contents
	 *         of the file.
	 * 
	 * @exception IOException
	 *                if an error occurs.
	 */
	InputStream getInputStream() throws IOException;


	/**
	 * Returns the content type passed by the browser or <code>null</code> if not defined.
	 * 
	 * @return The content type passed by the browser or <code>null</code> if not defined.
	 */
	String getContentType();


	/**
	 * Returns the original filename in the client's filesystem, as provided by the browser (or
	 * other client software). In most cases, this will be the base file name, without path
	 * information. However, some clients, such as the Opera browser, do include path information.
	 * 
	 * @return The original filename in the client's filesystem.
	 */
	String getName();


	// ------------------------------------------------------- FileItem methods


	/**
	 * Provides a hint as to whether or not the file contents will be read from memory.
	 * 
	 * @return <code>true</code> if the file contents will be read from memory; <code>false</code>
	 *         otherwise.
	 */
	boolean isInMemory();


	/**
	 * Returns the size of the file item.
	 * 
	 * @return The size of the file item, in bytes.
	 */
	long getSize();


	/**
	 * Returns the contents of the file item as an array of bytes.
	 * 
	 * @return The contents of the file item as an array of bytes.
	 */
	byte[] get();


	/**
	 * Returns the contents of the file item as a String, using the specified encoding. This method
	 * uses {@link #get()} to retrieve the contents of the item.
	 * 
	 * @param encoding
	 *            The character encoding to use.
	 * 
	 * @return The contents of the item, as a string.
	 * 
	 * @exception UnsupportedEncodingException
	 *                if the requested character encoding is not available.
	 */
	String getString(String encoding) throws UnsupportedEncodingException;


	/**
	 * Returns the contents of the file item as a String, using the default character encoding. This
	 * method uses {@link #get()} to retrieve the contents of the item.
	 * 
	 * @return The contents of the item, as a string.
	 */
	String getString();


	/**
	 * A convenience method to write an uploaded item to disk. The client code is not concerned with
	 * whether or not the item is stored in memory, or on disk in a temporary location. They just
	 * want to write the uploaded item to a file.
	 * <p>
	 * This method is not guaranteed to succeed if called more than once for the same item. This
	 * allows a particular implementation to use, for example, file renaming, where possible, rather
	 * than copying all of the underlying data, thus gaining a significant performance benefit.
	 * 
	 * @param file
	 *            The <code>File</code> into which the uploaded item should be stored.
	 * 
	 * @exception Exception
	 *                if an error occurs.
	 */
	void write(File file) throws Exception;


	/**
	 * Deletes the underlying storage for a file item, including deleting any associated temporary
	 * disk file. Although this storage will be deleted automatically when the <code>FileItem</code>
	 * instance is garbage collected, this method can be used to ensure that this is done at an
	 * earlier time, thus preserving system resources.
	 */
	void delete();


	/**
	 * Returns the name of the field in the multipart form corresponding to this file item.
	 * 
	 * @return The name of the form field.
	 */
	String getFieldName();


	/**
	 * Sets the field name used to reference this file item.
	 * 
	 * @param name
	 *            The name of the form field.
	 */
	void setFieldName(String name);


	/**
	 * Determines whether or not a <code>FileItem</code> instance represents a simple form field.
	 * 
	 * @return <code>true</code> if the instance represents a simple form field; <code>false</code>
	 *         if it represents an uploaded file.
	 */
	boolean isFormField();


	/**
	 * Specifies whether or not a <code>FileItem</code> instance represents a simple form field.
	 * 
	 * @param state
	 *            <code>true</code> if the instance represents a simple form field;
	 *            <code>false</code> if it represents an uploaded file.
	 */
	void setFormField(boolean state);


	/**
	 * Returns an {@link java.io.OutputStream OutputStream} that can be used for storing the
	 * contents of the file.
	 * 
	 * @return An {@link java.io.OutputStream OutputStream} that can be used for storing the
	 *         contents of the file.
	 * 
	 * @exception IOException
	 *                if an error occurs.
	 */
	OutputStream getOutputStream() throws IOException;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10194.java