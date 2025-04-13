error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17702.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17702.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[314,80]

error in qdox parser
file content:
```java
offset: 7068
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17702.java
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
package wicket.util.collections;

import java.io.Serializable;

import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of the Map interface which can hold just a single object.
 * @author Jonathan Locke
 */
public final class MicroMap implements Map, Serializable
{
	/** serialVersionUID */
	private static final long serialVersionUID = 6392759678359952206L;

	/** The maximum number of entries this map supports. */
    public static final int MAX_ENTRIES = 1;

    // The key
    private Object key;

    // The value
    private Object value;

    /**
     * Constructor
     */
    public MicroMap()
    {
    }

    /**
     * Constructs map with a key and value
     * @param key The key
     * @param value The value
     */
    public MicroMap(final Object key, final Object value)
    {
        put(key, value);
    }

    /**
     * @return True if this MicroMap is full
     */
    public boolean isFull()
    {
        return size() == MAX_ENTRIES;
    }

    /**
     * @see java.util.Map#size()
     */
    public int size()
    {
        return (key != null) ? 1 : 0;
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key)
    {
        return key.equals(this.key);
    }

    /**
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object value)
    {
        return value.equals(this.value);
    }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(final Object key)
    {
        if (key.equals(this.key))
        {
            return value;
        }

        return null;
    }

    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(final Object key, final Object value)
    {
        // Replace?
        if (key.equals(this.key))
        {
            final Object oldValue = this.value;

            this.value = value;

            return oldValue;
        }
        else
        {
            // Is there room for a new entry?
            if (size() < MAX_ENTRIES)
            {
                // Store
                this.key = key;
                this.value = value;

                return null;
            }
            else
            {
                throw new IllegalStateException("Map full");
            }
        }
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(final Object key)
    {
        if (key.equals(this.key))
        {
            final Object oldValue = this.value;

            this.key = null;
            this.value = null;

            return oldValue;
        }

        return null;
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(final Map map)
    {
        if (map.size() <= MAX_ENTRIES)
        {
            final Map.Entry e = (Map.Entry) map.entrySet().iterator().next();

            put(e.getKey(), e.getValue());
        }
        else
        {
            throw new IllegalStateException("Map full.  Cannot add " + map.size() + " entries");
        }
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear()
    {
        key = null;
        value = null;
    }

    /**
     * @see java.util.Map#keySet()
     */
    public Set keySet()
    {
        return new AbstractSet()
        {
            public Iterator iterator()
            {
                return new Iterator()
                {
                    public boolean hasNext()
                    {
                        return index < MicroMap.this.size();
                    }

                    public Object next()
                    {
                        index++;

                        return key;
                    }

                    public void remove()
                    {
                        MicroMap.this.clear();
                    }

                    int index;
                };
            }

            public int size()
            {
                return MicroMap.this.size();
            }
        };
    }

    /**
     * @see java.util.Map#values()
     */
    public Collection values()
    {
        return new AbstractList()
        {
            public Object get(final int index)
            {
                return value;
            }

            public int size()
            {
                return MicroMap.this.size();
            }
        };
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set entrySet()
    {
        return new AbstractSet()
        {
            public Iterator iterator()
            {
                return new Iterator()
                {
                    public boolean hasNext()
                    {
                        return index < MicroMap.this.size();
                    }

                    public Object next()
                    {
                        index++;

                        return new Map.Entry()
                        {
                            public Object getKey()
                            {
                                return key;
                            }

                            public Object getValue()
                            {
                                return value;
                            }

                            public Object setValue(final Object value)
                            {
                                final Object oldValue = MicroMap.this.value;

                                MicroMap.this.value = value;

                                return oldValue;
                            }
                        };
                    }

                    public void remove()
                    {
                        clear();
                    }

                    int index = 0;
                };
            }

            public int size()
            {
                return MicroMap.this.size();
            }
        };
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17702.java