error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14610.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14610.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14610.java
text:
```scala
o@@ut.putNextEntry(new ZipEntry(path + f.getName()));

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
package org.apache.wicket.util.resource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.wicket.util.file.File;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An IResourceStream that ZIPs a directory's contents on the fly
 * 
 * <p>
 * <b>NOTE 1.</b> As a future improvement, cache a map of generated ZIP files for every directory
 * and use a Watcher to detect modifications in this directory. Using ehcache would be good for
 * that, but it's not in Wicket dependencies yet. <b>No caching of the generated ZIP files is done
 * yet.</b>
 * </p>
 * 
 * <p>
 * <b>NOTE 2.</b> As a future improvement, implement getLastModified() and request
 * ResourceStreamRequestTarget to generate Last-Modified and Expires HTTP headers. <b>No HTTP cache
 * headers are provided yet</b>. See WICKET-385
 * </p>
 * 
 * @author <a href="mailto:jbq@apache.org">Jean-Baptiste Quenot</a>
 */
public class ZipResourceStream extends AbstractResourceStream
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ZipResourceStream.class);

	private final ByteArrayOutputStream bytearray;

	/**
	 * Construct.
	 * 
	 * @param dir
	 *            The directory where to look for files. The directory itself will not be included
	 *            in the ZIP.
	 * @param recursive
	 *            If true, all subdirs will be zipped as well
	 */
	public ZipResourceStream(final File dir, final boolean recursive)
	{
		if ((dir == null) || !dir.isDirectory())
		{
			throw new IllegalArgumentException("Not a directory: " + dir);
		}

		bytearray = new ByteArrayOutputStream();
		try
		{
			ZipOutputStream out = new ZipOutputStream(bytearray);
			zipDir(dir, out, "", recursive);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Construct. Until Wicket 1.4-RC3 recursive zip was not supported. In order not to change the
	 * behavior, using this constructor will default to recursive == false.
	 * 
	 * @param dir
	 *            The directory where to look for files. The directory itself will not be included
	 *            in the ZIP.
	 */
	public ZipResourceStream(final File dir)
	{
		this(dir, false);
	}

	/**
	 * Recursive method for zipping the contents of a directory including nested directories.
	 * 
	 * @param dir
	 *            dir to be zipped
	 * @param out
	 *            ZipOutputStream to write to
	 * @param path
	 *            Path to nested dirs (used in resursive calls)
	 * @param recursive
	 *            If true, all subdirs will be zipped as well
	 * @throws IOException
	 */
	private static void zipDir(final File dir, final ZipOutputStream out, final String path,
		final boolean recursive) throws IOException
	{
		if (!dir.isDirectory())
		{
			throw new IllegalArgumentException("Not a directory: " + dir);
		}

		String[] files = dir.list();

		int BUFFER = 2048;
		BufferedInputStream origin = null;
		byte data[] = new byte[BUFFER];

		for (String file : files)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Adding: " + file);
			}

			File f = new File(dir, file);
			if (f.isDirectory())
			{
				if (recursive == true)
				{
					zipDir(f, out, path + f.getName() + "/", recursive);
				}
			}
			else
			{
				out.putNextEntry(new ZipEntry(path.toString() + f.getName()));

				FileInputStream fi = new FileInputStream(f);
				origin = new BufferedInputStream(fi, BUFFER);

				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1)
				{
					out.write(data, 0, count);
				}
				origin.close();
			}
		}

		if (path.equals(""))
		{
			out.close();
		}
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#close()
	 */
	public void close() throws IOException
	{
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return null;
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#getInputStream()
	 */
	public InputStream getInputStream() throws ResourceStreamNotFoundException
	{
		return new ByteArrayInputStream(bytearray.toByteArray());
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractResourceStream#length()
	 */
	@Override
	public long length()
	{
		return bytearray.size();
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractResourceStream#lastModifiedTime()
	 */
	@Override
	public Time lastModifiedTime()
	{
		return null;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14610.java