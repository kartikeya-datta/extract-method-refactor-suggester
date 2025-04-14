error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4717.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4717.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4717.java
text:
```scala
I@@nternal, External, Absolute

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx;

import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;

/**
 * This interface encapsulates the access of internal, external and absolute files. Internal files are read-only and come with the
 * application when deployed. On Android assets are internal files, on the desktop anything in the applications root directory is
 * considered to be a an internal file. External files can be read and written to. On Android they are relative to the SD-card, on
 * the desktop they are relative to the user home directory. Absolute files are just that, fully qualified filenames. To ensure
 * portability across platforms use absolute files only when absolutely necessary.
 * 
 * @author mzechner
 * 
 */
public interface Files {
	/**
	 * Enum describing the three file types, internal, external and absolute. Internal files are located in the asset directory on
	 * Android and are relative to the applications root directory on the desktop. External files are relative to the SD-card on
	 * Android and relative to the home directory of the current user on the Desktop. Absolute files are just that, absolute files
	 * that can point anywhere.
	 * @author mzechner
	 * 
	 */
	public enum FileType {
		Internal, External, Absolut
	}

	/**
	 * Returns an {@link InputStream} to the given file. If type is equal to {@link FileType.Internal} an internal file will be
	 * opened. On Android this is relative to the assets directory, on the desktop it is relative to the applications root
	 * directory. If type is equal to {@link FileType.External} an external file will be opened. On Android this is relative to the
	 * SD-card, on the desktop it is relative to the current user's home directory. If type is equal to {@link FileType.Absolut}
	 * the filename is interpreted as an absolute filename.
	 * 
	 * @param fileName the name of the file to open.
	 * @param type the type of file to open.
	 * @return the InputStream
	 * @throws GDXRuntimeException in case the file could not be opened.
	 */
	public InputStream readFile (String fileName, FileType type);

	/**
	 * Returns and {@link OutputStream} to the given file. If the file does not exist it is created. If the file exists it will be
	 * overwritten. If type is equal to {@link FileType.Internal} null will be returned as on Android assets can not be written. If
	 * type is equal to {@link FileType.External} an external file will be opened. On Android this is relative to the SD-card, on
	 * the desktop it is relative to the current user's home directory. If type is equal to {@link FileType.Absolut} the filename
	 * is interpreted as an absolut filename.
	 * 
	 * @param filename the name of the file to open
	 * @param type the type of the file to open
	 * @return the OutputStream
	 * @throws GdxRuntimeException in case the file could not be opened
	 */
	public OutputStream writeFile (String filename, FileType type);

	/**
	 * Creates a new directory or directory hierarchy on the external storage. If the directory parameter contains sub folders and
	 * the parent folders don't exist yet they will be created. If type is equal to {@link FileType.Internal} false will be
	 * returned as on Android new directories in the asset directory can not be created. If type is equal to
	 * {@link FileType.External} an external directory will be created. On Android this is relative to the SD-card, on the desktop
	 * it is relative to the current user's home directory. If type is equal to {@link FileType.Absolut} the directory is
	 * interpreted as an absolute directory name.
	 * 
	 * @param directory the directory
	 * @param type the type of the directory
	 * @return true in case the directory could be created, false otherwise
	 */
	public boolean makeDirectory (String directory, FileType type);

	/**
	 * Lists the files and directories in the given directory. If type is equal to {@link FileType.Internal} an internal directory
	 * will be listed. On Android this is relative to the assets directory, on the desktop it is relative to the applications root
	 * directory. If type is equal to {@link FileType.External} an external directory will be listed. On Android this is relative
	 * to the SD-card, on the desktop it is relative to the current user's home directory. If type is equal to
	 * {@link FileType.Absolut} the filename is interpreted as an absolut directory.
	 * 
	 * @param directory the directory
	 * @param type the type of the directory
	 * @return the files and directories in the given directory
	 * @throws GDXRuntimeException if the directory does not exist
	 */
	public String[] listDirectory (String directory, FileType type);

	/**
	 * Returns a {@link FileHandle} object for a file. If type is equal to {@link FileType.Internal} an internal file will be
	 * opened. On Android this is relative to the assets directory, on the desktop it is relative to the applications root
	 * directory. If type is equal to {@link FileType.External} an external file will be opened. On Android this is relative to the
	 * SD-card, on the desktop it is relative to the current user's home directory. If type is equal to {@link FileType.Absolut}
	 * the filename is interpreted as an absolute filename.
	 * 
	 * @param filename the name of the file
	 * @param type the type of the file
	 * @return the FileDescriptor or null if the descriptor could not be created
	 * @throws GdxRuntimeException if the file does not exist
	 */
	public FileHandle getFileHandle (String filename, FileType type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4717.java