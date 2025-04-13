error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2160.java
text:
```scala
final A@@ppendingStringBuffer buf = new AppendingStringBuffer(length);

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.util.string;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base class for string list implementations. Besides having an
 * implementation for IStringSequence (iterator(), get(int index) and size()),
 * an AbstractStringList can be converted to a String array or a List of
 * Strings.
 * <p>
 * The total length of all Strings in the list can be determined by calling
 * totalLength().
 * <p>
 * Strings or a subset of Strings in the list can be formatted using three
 * join() methods:
 * <p>
 * <ul>
 * <li>join(String) Joins strings together using a given separator
 * <li>join() Joins Strings using comma as a separator
 * <li>join(int first, int last, String) Joins a sublist of strings using a
 * given separator
 * </ul>
 * 
 * @author Jonathan Locke
 */
public abstract class AbstractStringList implements IStringSequence, Serializable
{
	/**
	 * @return String iterator
	 * @see wicket.util.string.IStringSequence#iterator()
	 */
	public abstract IStringIterator iterator();

	/**
	 * @return Number of strings in this string list
	 * @see wicket.util.string.IStringSequence#size()
	 */
	public abstract int size();

	/**
	 * @param index
	 *            The index into this string list
	 * @return The string at the given index
	 * @see wicket.util.string.IStringSequence#get(int)
	 */
	public abstract String get(int index);

	/**
	 * Returns this String sequence as an array of Strings. Subclasses may
	 * provide a more efficient implementation than the one provided here.
	 * 
	 * @return An array containing exactly this sequence of Strings
	 */
	public String[] toArray()
	{
		// Get number of Strings
		final int size = size();

		// Allocate array
		final String[] strings = new String[size];

		// Copy string references
		for (int i = 0; i < size; i++)
		{
			strings[i] = get(i);
		}

		return strings;
	}

	/**
	 * Returns this String sequence as an array of Strings. Subclasses may
	 * provide a more efficient implementation than the one provided here.
	 * 
	 * @return An array containing exactly this sequence of Strings
	 */
	public final List toList()
	{
		// Get number of Strings
		final int size = size();

		// Allocate list of exactly the right size
		final List strings = new ArrayList(size);

		// Add strings to list
		for (int i = 0; i < size; i++)
		{
			strings.add(get(i));
		}

		return strings;
	}

	/**
	 * @return The total length of all Strings in this sequence.
	 */
	public int totalLength()
	{
		// Get number of Strings
		final int size = size();

		// Add strings to list
		int totalLength = 0;

		for (int i = 0; i < size; i++)
		{
			totalLength += get(i).length();
		}

		return totalLength;
	}

	/**
	 * Joins this sequence of strings using a comma separator. For example, if
	 * this sequence contains [1 2 3], the result of calling this method will be
	 * "1, 2, 3".
	 * 
	 * @return The joined String
	 */
	public final String join()
	{
		return join(", ");
	}

	/**
	 * Joins this sequence of strings using a separator
	 * 
	 * @param separator
	 *            The separator to use
	 * @return The joined String
	 */
	public final String join(final String separator)
	{
		return join(0, size(), separator);
	}

	/**
	 * Joins this sequence of strings from first index to last using a separator
	 * 
	 * @param first
	 *            The first index to use, inclusive
	 * @param last
	 *            The last index to use, exclusive
	 * @param separator
	 *            The separator to use
	 * @return The joined String
	 */
	public final String join(final int first, final int last, final String separator)
	{
		// Allocate buffer of exactly the right length
		final int length = totalLength() + (separator.length() * (Math.max(0, last - first - 1)));
		final StringBuffer buf = new StringBuffer(length);

		// Loop through indexes requested
		for (int i = first; i < last; i++)
		{
			// Add next string
			buf.append(get(i));

			// Add separator?
			if (i != (last - 1))
			{
				buf.append(separator);
			}
		}

		return buf.toString();
	}

	/**
	 * Converts this object to a string representation
	 * 
	 * @return String version of this object
	 */
	public String toString()
	{
		return "[" + join() + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2160.java