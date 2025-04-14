error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17775.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17775.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[307,80]

error in qdox parser
file content:
```java
offset: 7463
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17775.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.util.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A typesafe, mutable list of strings supporting a variety of convenient operations. The
 * class is not multithread safe.
 * @author Jonathan Locke
 */
public final class StringList extends AbstractStringList
{
	/** serialVersionUID */
	private static final long serialVersionUID = 3913242296604884174L;

	// The underlying list of strings
    private final List strings;

    // The total length of all strings in the list
    private int totalLength;

    /**
     * Constructor
     */
    public StringList()
    {
        this.strings = new ArrayList();
    }

    /**
     * Constructor
     * @param size Number of elements to preallocate
     */
    public StringList(final int size)
    {
        this.strings = new ArrayList(size);
    }

    /**
     * Converts a string array to a string list
     * @param array The array
     * @return The list
     */
    public static StringList valueOf(final String[] array)
    {
        final StringList strings = new StringList(array.length);

        for (int i = 0; i < array.length; i++)
        {
            strings.add(array[i]);
        }

        return strings;
    }

    /**
     * Returns a string list with just one string in it
     * @param string The string
     * @return The list of one string
     */
    public static StringList valueOf(final String string)
    {
        final StringList strings = new StringList();

        strings.add(string);

        return strings;
    }

    /**
     * Converts a collection of objects into a list of string values by using the
     * conversion methods of the StringValue class.
     * @param collection The collection to add as strings
     * @return The list
     */
    public static StringList valueOf(final Collection collection)
    {
        if (collection != null)
        {
            final StringList strings = new StringList(collection.size());

            for (final Iterator iterator = collection.iterator(); iterator.hasNext();)
            {
                strings.add(StringValue.valueOf(iterator.next()));
            }

            return strings;
        }
        else
        {
            return new StringList();
        }
    }

    /**
     * Converts an array of objects into a list of strings by using the object to string
     * conversion method of the StringValue class.
     * @param objects The objects to convert
     * @return The list of strings
     */
    public static StringList valueOf(final Object[] objects)
    {
        final StringList strings = new StringList(objects.length);

        for (int i = 0; i < objects.length; i++)
        {
            strings.add(StringValue.valueOf(objects[i]));
        }

        return strings;
    }

    /**
     * Extracts tokens from a comma and space delimited string
     * @param string The string
     * @return The string tokens as a list
     */
    public static StringList tokenize(final String string)
    {
        return tokenize(string, ", ");
    }

    /**
     * Extracts tokens from a delimited string
     * @param string The string
     * @param delimiters The delimiters
     * @return The string tokens as a list
     */
    public static StringList tokenize(final String string, final String delimiters)
    {
        final StringTokenizer tokenizer = new StringTokenizer(string, delimiters);
        final StringList strings = new StringList();

        while (tokenizer.hasMoreTokens())
        {
            strings.add(tokenizer.nextToken());
        }

        return strings;
    }

    /**
     * Returns a list of a string repeated a given number of times
     * @param count The number of times to repeat the string
     * @param string The string to repeat
     * @return The list of strings
     */
    public static StringList repeat(final int count, final String string)
    {
        final StringList list = new StringList(count);

        for (int i = 0; i < count; i++)
        {
            list.add(string);
        }

        return list;
    }

    /**
     * Returns a typesafe iterator over this collection of strings
     * @return Typesafe string iterator
     */
    public IStringIterator iterator()
    {
        return new IStringIterator()
        {
            public String next()
            {
                return (String) iterator.next();
            }

            public boolean hasNext()
            {
                return iterator.hasNext();
            }

            private final Iterator iterator = strings.iterator();
        };
    }

    /**
     * @return The number of strings in this list
     */
    public int size()
    {
        return strings.size();
    }

    /**
     * Gets the string at the given index
     * @param index The index
     * @return The string at the index
     * @throws IndexOutOfBoundsException
     */
    public String get(final int index)
    {
        return (String) strings.get(index);
    }

    /**
     * Converts this string list to a string array
     * @return The string array
     */
    public String[] toArray()
    {
        return (String[]) strings.toArray(new String[size()]);
    }

    /**
     * @return List value (not a copy of this list)
     */
    public List getList()
    {
        return strings;
    }

    /**
     * @return The total length of all strings in this list
     */
    public int totalLength()
    {
        return totalLength;
    }

    /**
     * Adds a string value to this list as a string
     * @param value The value to add
     */
    public void add(final StringValue value)
    {
        add(value.toString());
    }

    /**
     * Adds a string to this list
     * @param string String to add
     */
    public void add(final String string)
    {
        // Add to list
        strings.add(string);

        // Increase total length
        totalLength += string.length();
    }

    /**
     * Removes the string at the given index
     * @param index The index
     */
    public void remove(final int index)
    {
        strings.remove(index);
    }

    /**
     * Removes the last string in this list
     */
    public void removeLast()
    {
        remove(size() - 1);
    }

    /**
     * Adds the given string to the front of the list
     * @param string The string to add
     */
    public void prepend(final String string)
    {
        strings.add(0, string);
    }

    /**
     * Sorts this string list alphabetically
     */
    public void sort()
    {
        Collections.sort(strings);
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17775.java