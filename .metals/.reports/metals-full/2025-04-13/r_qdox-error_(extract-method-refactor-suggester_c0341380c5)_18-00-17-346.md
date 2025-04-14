error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9452.java
text:
```scala
c@@atch (InterruptedException ignored)

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
package org.apache.wicket.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.io.Streams;
import org.apache.wicket.util.string.Strings;


/**
 * File utility methods.
 * 
 * @author Jonathan Locke
 */
public class Files
{
	/**
	 * Private constructor to prevent instantiation.
	 */
	private Files()
	{
	}

	/**
	 * Strips off the given extension (probably returned from Files.extension()) from the path,
	 * yielding a base pathname.
	 * 
	 * @param path
	 *            The path, possibly with an extension to strip
	 * @param extension
	 *            The extension to strip, or null if no extension exists
	 * @return The path without any extension
	 */
	public static String basePath(final String path, final String extension)
	{
		if (extension != null)
		{
			return path.substring(0, path.length() - extension.length() - 1);
		}
		return path;
	}

	/**
	 * Gets extension from path
	 * 
	 * @param path
	 *            The path
	 * @return The extension, like "bmp" or "html", or null if none can be found
	 */
	public static String extension(final String path)
	{
		if (path.indexOf('.') != -1)
		{
			return Strings.lastPathComponent(path, '.');
		}
		return null;
	}

	/**
	 * Gets filename from path
	 * 
	 * @param path
	 *            The path
	 * @return The filename
	 */
	public static String filename(final String path)
	{
		return Strings.lastPathComponent(path.replace('/', java.io.File.separatorChar),
			java.io.File.separatorChar);
	}

	/**
	 * Deletes a file, dealing with a particularly nasty bug on Windows.
	 * 
	 * @param file
	 *            File to delete
	 * @return True if file was deleted
	 */
	public static boolean remove(final java.io.File file)
	{
		// Delete current file
		if (!file.delete())
		{
			// NOTE: fix for java/win bug. see:
			// http://forum.java.sun.com/thread.jsp?forum=4&thread=158689&tstart=
			// 0&trange=15
			System.gc();
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
			}

			// Try one more time to delete the file
			return file.delete();
		}
		return true;
	}

	/**
	 * Writes the given input stream to the given file
	 * 
	 * @param file
	 *            The file to write to
	 * @param input
	 *            The input
	 * @return Number of bytes written
	 * @throws IOException
	 */
	public static final int writeTo(final java.io.File file, final InputStream input)
		throws IOException
	{
		return writeTo(file, input, 4096);
	}

	/**
	 * read binary file fully
	 * 
	 * @param file
	 *            file to read
	 * @return byte array representing the content of the file
	 * @throws IOException
	 *             is something went wrong
	 */
	public static byte[] readBytes(final File file) throws IOException
	{
		FileInputStream stream = new FileInputStream(file);

		try
		{
			return IOUtils.toByteArray(stream);
		}
		finally
		{
			stream.close();
		}
	}

	/**
	 * Writes the given input stream to the given file
	 * 
	 * @param file
	 *            The file to write to
	 * @param input
	 *            The input
	 * @param bufSize
	 *            The memory buffer size. 4096 is a good value.
	 * @return Number of bytes written
	 * @throws IOException
	 */
	public static final int writeTo(final java.io.File file, final InputStream input,
		final int bufSize) throws IOException
	{
		final FileOutputStream out = new FileOutputStream(file);
		try
		{
			return Streams.copy(input, out, bufSize);
		}
		finally
		{
			out.close();
		}
	}

	private static String FORBIDDEN_IN_NAME = "\"*/:<>?\\|,";

	/**
	 * <p>
	 * Replaces commonly unsupported characters with '_'
	 * </p>
	 * 
	 * @param filename
	 *            to be cleaned
	 * @return cleaned filename
	 */
	public static final String cleanupFilename(final String filename)
	{
		String name = filename;
		for (int i = 0; i < FORBIDDEN_IN_NAME.length(); i++)
		{
			name = name.replace(FORBIDDEN_IN_NAME.charAt(i), '_');
		}
		return name;
	}

	/**
	 * make a copy of a file
	 * 
	 * @param sourceFile
	 *            source file that needs to be cloned
	 * @param targetFile
	 *            target file that should be a duplicate of source file
	 * @throws IOException
	 *             if something went wrong
	 */
	public static void copy(final File sourceFile, final File targetFile) throws IOException
	{
		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		try
		{
			in = new BufferedInputStream(new FileInputStream(sourceFile));
			out = new BufferedOutputStream(new FileOutputStream(targetFile));

			IOUtils.copy(in, out);
		}
		finally
		{
			try
			{
				IOUtils.close(in);

			}
			finally
			{
				IOUtils.close(out);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9452.java