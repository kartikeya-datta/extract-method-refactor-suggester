error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16967.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16967.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16967.java
text:
```scala
r@@eturn new ArrayList<T>();

/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must
 * include the following acknowledgement: "This product includes software
 * developed by the Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgement may appear in the software itself, if and
 * wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 * Foundation" must not be used to endorse or promote products derived from this
 * software without prior written permission. For written permission, please
 * contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" nor may
 * "Apache" appear in their names without prior written permission of the Apache
 * Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE APACHE
 * SOFTWARE FOUNDATION OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many individuals on
 * behalf of the Apache Software Foundation. For more information on the Apache
 * Software Foundation, please see <http://www.apache.org/>.
 *
 */

package org.apache.wicket.util.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Holds a information about a part of the text involved in a differencing or patching operation.
 * 
 * @version $Id: Chunk.java,v 1.1 2006/03/12 00:24:21 juanca Exp $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @see Diff
 * @see Delta
 */
public class Chunk extends ToString
{

	protected int anchor;

	protected int count;

	protected List<Object> chunk;

	/**
	 * Creates a chunk that doesn't copy the original text.
	 * 
	 * @param pos
	 *            the start position in the text.
	 * @param count
	 *            the size of the chunk.
	 */
	public Chunk(int pos, int count)
	{
		anchor = pos;
		this.count = (count >= 0 ? count : 0);
	}

	/**
	 * Creates a chunk and saves a copy the original chunk's text.
	 * 
	 * @param iseq
	 *            the original text.
	 * @param pos
	 *            the start position in the text.
	 * @param count
	 *            the size of the chunk.
	 */
	public Chunk(Object[] iseq, int pos, int count)
	{
		this(pos, count);
		chunk = slice(iseq, pos, count);
	}

	/**
	 * Creates a chunk that will be displaced in the resulting text, and saves a copy the original
	 * chunk's text.
	 * 
	 * @param iseq
	 *            the original text.
	 * @param pos
	 *            the start position in the text.
	 * @param count
	 *            the size of the chunk.
	 * @param offset
	 *            the position the chunk should have in the resulting text.
	 */
	public Chunk(Object[] iseq, int pos, int count, int offset)
	{
		this(offset, count);
		chunk = slice(iseq, pos, count);
	}

	/**
	 * Creates a chunk and saves a copy the original chunk's text.
	 * 
	 * @param iseq
	 *            the original text.
	 * @param pos
	 *            the start position in the text.
	 * @param count
	 *            the size of the chunk.
	 */
	public Chunk(List<Object> iseq, int pos, int count)
	{
		this(pos, count);
		chunk = slice(iseq, pos, count);
	}

	/**
	 * Creates a chunk that will be displaced in the resulting text, and saves a copy the original
	 * chunk's text.
	 * 
	 * @param iseq
	 *            the original text.
	 * @param pos
	 *            the start position in the text.
	 * @param count
	 *            the size of the chunk.
	 * @param offset
	 *            the position the chunk should have in the resulting text.
	 */
	public Chunk(List<Object> iseq, int pos, int count, int offset)
	{
		this(offset, count);
		chunk = slice(iseq, pos, count);
	}

	/**
	 * Returns the anchor position of the chunk.
	 * 
	 * @return the anchor position.
	 */
	public int anchor()
	{
		return anchor;
	}

	/**
	 * Returns the size of the chunk.
	 * 
	 * @return the size.
	 */
	public int size()
	{
		return count;
	}

	/**
	 * Returns the index of the first line of the chunk.
	 * 
	 * @return int
	 */
	public int first()
	{
		return anchor();
	}

	/**
	 * Returns the index of the last line of the chunk.
	 * 
	 * @return int
	 */
	public int last()
	{
		return anchor() + size() - 1;
	}

	/**
	 * Returns the <i>from</i> index of the chunk in RCS terms.
	 * 
	 * @return int
	 */
	public int rcsfrom()
	{
		return anchor + 1;
	}

	/**
	 * Returns the <i>to</i> index of the chunk in RCS terms.
	 * 
	 * @return int
	 */
	public int rcsto()
	{
		return anchor + count;
	}

	/**
	 * Returns the text saved for this chunk.
	 * 
	 * @return the text.
	 */
	public List<Object> chunk()
	{
		return chunk;
	}

	/**
	 * Verifies that this chunk's saved text matches the corresponding text in the given sequence.
	 * 
	 * @param target
	 *            the sequence to verify against.
	 * @return true if the texts match.
	 */
	public boolean verify(List<Object> target)
	{
		if (chunk == null)
		{
			return true;
		}
		if (last() > target.size())
		{
			return false;
		}
		for (int i = 0; i < count; i++)
		{
			if (!target.get(anchor + i).equals(chunk.get(i)))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Delete this chunk from he given text.
	 * 
	 * @param target
	 *            the text to delete from.
	 */
	public void applyDelete(List<Object> target)
	{
		for (int i = last(); i >= first(); i--)
		{
			target.remove(i);
		}
	}

	/**
	 * Add the text of this chunk to the target at the given position.
	 * 
	 * @param start
	 *            where to add the text.
	 * @param target
	 *            the text to add to.
	 */
	public void applyAdd(int start, List<Object> target)
	{
		Iterator<Object> i = chunk.iterator();
		while (i.hasNext())
		{
			target.add(start++, i.next());
		}
	}

	/**
	 * Provide a string image of the chunk using the an empty prefix and postfix.
	 * 
	 * @param s
	 */
	@Override
	public void toString(StringBuffer s)
	{
		toString(s, "", "");
	}

	/**
	 * Provide a string image of the chunk using the given prefix and postfix.
	 * 
	 * @param s
	 *            where the string image should be appended.
	 * @param prefix
	 *            the text that should prefix each line.
	 * @param postfix
	 *            the text that should end each line.
	 * @return StringBuffer
	 */
	public StringBuffer toString(StringBuffer s, String prefix, String postfix)
	{
		if (chunk != null)
		{
			Iterator<?> i = chunk.iterator();
			while (i.hasNext())
			{
				s.append(prefix);
				s.append(i.next());
				s.append(postfix);
			}
		}
		return s;
	}

	/**
	 * Retrieves the specified part from a {@link List List}.
	 * 
	 * @param <T>
	 *            the type of objects contained in <code>seq</code>
	 * 
	 * @param seq
	 *            the list to retrieve a slice from.
	 * @param pos
	 *            the start position.
	 * @param count
	 *            the number of items in the slice.
	 * @return a {@link List List} containing the specified items.
	 */
	public static <T> List<T> slice(List<T> seq, int pos, int count)
	{
		if (count <= 0)
		{
			return new ArrayList<T>(seq.subList(pos, pos));
		}
		else
		{
			return new ArrayList<T>(seq.subList(pos, pos + count));
		}
	}

	/**
	 * Retrieves a slice from an {@link Object Object} array.
	 * 
	 * @param seq
	 *            the list to retrieve a slice from.
	 * @param pos
	 *            the start position.
	 * @param count
	 *            the number of items in the slice.
	 * @return a {@link List List} containing the specified items.
	 */
	public static List<Object> slice(Object[] seq, int pos, int count)
	{
		return slice(Arrays.asList(seq), pos, count);
	}

	/**
	 * Provide a string representation of the numeric range of this chunk.
	 * 
	 * @return String
	 */
	public String rangeString()
	{
		StringBuffer result = new StringBuffer();
		rangeString(result);
		return result.toString();
	}

	/**
	 * Provide a string representation of the numeric range of this chunk.
	 * 
	 * @param s
	 *            where the string representation should be appended.
	 */
	public void rangeString(StringBuffer s)
	{
		rangeString(s, ",");
	}

	/**
	 * Provide a string representation of the numeric range of this chunk.
	 * 
	 * @param s
	 *            where the string representation should be appended.
	 * @param separ
	 *            what to use as line separator.
	 */
	public void rangeString(StringBuffer s, String separ)
	{
		if (size() <= 1)
		{
			s.append(Integer.toString(rcsfrom()));
		}
		else
		{
			s.append(Integer.toString(rcsfrom()));
			s.append(separ);
			s.append(Integer.toString(rcsto()));
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16967.java