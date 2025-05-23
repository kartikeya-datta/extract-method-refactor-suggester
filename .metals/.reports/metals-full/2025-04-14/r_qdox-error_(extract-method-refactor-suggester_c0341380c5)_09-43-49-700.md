error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15952.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15952.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15952.java
text:
```scala
private final L@@ist buffers = new java.util.ArrayList();

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
package org.apache.wicket.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * This class implements an output stream in which the data is written into a byte array.
 * The buffer automatically grows as data is written to it.
 * <p>
 * The data can be retrieved using <code>toByteArray()</code> and
 * <code>toString()</code>.
 * <p>
 * Closing a <tt>ByteArrayOutputStream</tt> has no effect. The methods in this class can
 * be called after the stream has been closed without generating an <tt>IOException</tt>.
 * <p>
 * This is an alternative implementation of the java.io.ByteArrayOutputStream class. The
 * original implementation only allocates 32 bytes at the beginning. As this class is
 * designed for heavy duty it starts at 1024 bytes. In contrast to the original it doesn't
 * reallocate the whole memory block but allocates additional buffers. This way no buffers
 * need to be garbage collected and the contents don't have to be copied to the new
 * buffer. This class is designed to behave exactly like the original. The only exception
 * is the deprecated toString(int) method that has been ignored.
 * @author <a href="mailto:jeremias@apache.org">Jeremias Maerki</a>
 * @version $Id$
 */
public class ByteArrayOutputStream extends OutputStream
{
	private List buffers = new java.util.ArrayList();
	private int count;
	private byte[] currentBuffer;
	private int currentBufferIndex;
	private int filledBufferSum;

	/**
	 * Creates a new byte array output stream. The buffer capacity is initially 1024
	 * bytes, though its size increases if necessary.
	 */
	public ByteArrayOutputStream()
	{
		this(1024);
	}

	/**
	 * Creates a new byte array output stream, with a buffer capacity of the specified
	 * size, in bytes.
	 * @param size the initial size.
	 * @exception IllegalArgumentException if size is negative.
	 */
	public ByteArrayOutputStream(int size)
	{
		if (size < 0)
		{
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		needNewBuffer(size);
	}

	/**
	 * Closing a <tt>ByteArrayOutputStream</tt> has no effect. The methods in this class
	 * can be called after the stream has been closed without generating an
	 * <tt>IOException</tt>.
	 * @throws IOException in case an I/O error occurs
	 */
	public void close() throws IOException
	{
		// nop
	}

	/**
	 * @see java.io.ByteArrayOutputStream#reset()
	 */
	public synchronized void reset()
	{
		count = 0;
		filledBufferSum = 0;
		currentBufferIndex = 0;
		currentBuffer = getBuffer(currentBufferIndex);
	}

	/**
	 * Gets the size.
	 * @return the size
	 */
	public int size()
	{
		return count;
	}

	/**
	 * Writes to a byte array.
	 * @return this is a byte array
	 */
	public synchronized byte[] toByteArray()
	{
		int remaining = count;
		int pos = 0;
		byte newbuf[] = new byte[count];
		for (int i = 0; i < buffers.size(); i++)
		{
			byte[] buf = getBuffer(i);
			int c = Math.min(buf.length, remaining);
			System.arraycopy(buf, 0, newbuf, pos, c);
			pos += c;
			remaining -= c;
			if (remaining == 0)
			{
				break;
			}
		}
		return newbuf;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new String(toByteArray());
	}

	/**
	 * This as a string using the provided encoding.
	 * @param enc the encoding to use
	 * @return This as a string using the provided encoding
	 * @throws UnsupportedEncodingException
	 */
	public String toString(String enc) throws UnsupportedEncodingException
	{
		return new String(toByteArray(), enc);
	}

	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public synchronized void write(byte[] b, int off, int len)
	{
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)
 ((off + len) < 0))
		{
			throw new IndexOutOfBoundsException();
		}
		else if (len == 0)
		{
			return;
		}
		int newcount = count + len;
		int remaining = len;
		int inBufferPos = count - filledBufferSum;
		while (remaining > 0)
		{
			int part = Math.min(remaining, currentBuffer.length - inBufferPos);
			System.arraycopy(b, off + len - remaining, currentBuffer, inBufferPos, part);
			remaining -= part;
			if (remaining > 0)
			{
				needNewBuffer(newcount);
				inBufferPos = 0;
			}
		}
		count = newcount;
	}

	/**
	 * Calls the write(byte[]) method.
	 * @see java.io.OutputStream#write(int)
	 */
	public synchronized void write(int b)
	{
		write(new byte[] { (byte)b }, 0, 1);
	}

	/**
	 * Write to the given output stream.
	 * @param out the output stream to write to
	 * @throws IOException
	 * @see java.io.ByteArrayOutputStream#writeTo(OutputStream)
	 */
	public synchronized void writeTo(OutputStream out) throws IOException
	{
		int remaining = count;
		for (int i = 0; i < buffers.size(); i++)
		{
			byte[] buf = getBuffer(i);
			int c = Math.min(buf.length, remaining);
			out.write(buf, 0, c);
			remaining -= c;
			if (remaining == 0)
			{
				break;
			}
		}
	}

	private byte[] getBuffer(int index)
	{
		return (byte[])buffers.get(index);
	}

	private void needNewBuffer(int newcount)
	{
		if (currentBufferIndex < buffers.size() - 1)
		{
			// Recycling old buffer
			filledBufferSum += currentBuffer.length;

			currentBufferIndex++;
			currentBuffer = getBuffer(currentBufferIndex);
		}
		else
		{
			// Creating new buffer
			int newBufferSize;
			if (currentBuffer == null)
			{
				newBufferSize = newcount;
				filledBufferSum = 0;
			}
			else
			{
				newBufferSize = Math.max(currentBuffer.length << 1, newcount - filledBufferSum);
				filledBufferSum += currentBuffer.length;
			}

			currentBufferIndex++;
			currentBuffer = new byte[newBufferSize];
			buffers.add(currentBuffer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15952.java