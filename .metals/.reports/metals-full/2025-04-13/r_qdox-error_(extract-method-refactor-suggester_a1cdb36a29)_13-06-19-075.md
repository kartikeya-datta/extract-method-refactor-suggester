error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12171.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12171.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12171.java
text:
```scala
public H@@ashTree remove(Object key) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jorphan.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * ListedHashTree is a different implementation of the {@link HashTree}
 * collection class. In the ListedHashTree, the order in which values are added
 * is preserved (not to be confused with {@link SortedHashTree}, which sorts
 * the order of the values using the compare() function). Any listing of nodes
 * or iteration through the list of nodes of a ListedHashTree will be given in
 * the order in which the nodes were added to the tree.
 *
 * @see HashTree
 */
public class ListedHashTree extends HashTree implements Serializable, Cloneable {
    private static final long serialVersionUID = 240L;

    private final List<Object> order;

    public ListedHashTree() {
        super();
        order = new LinkedList<Object>();
    }

    public ListedHashTree(Object key) {
        this();
        data.put(key, new ListedHashTree());
        order.add(key);
    }

    public ListedHashTree(Collection<?> keys) {
        this();
        Iterator<?> it = keys.iterator();
        while (it.hasNext()) {
            Object temp = it.next();
            data.put(temp, new ListedHashTree());
            order.add(temp);
        }
    }

    public ListedHashTree(Object[] keys) {
        this();
        for (int x = 0; x < keys.length; x++) {
            data.put(keys[x], new ListedHashTree());
            order.add(keys[x]);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object clone() {
        ListedHashTree newTree = new ListedHashTree();
        cloneTree(newTree);
        return newTree;
    }

    /** {@inheritDoc} */
    @Override
    public void set(Object key, Object value) {
        if (!data.containsKey(key)) {
            order.add(key);
        }
        super.set(key, value);
    }

    /** {@inheritDoc} */
    @Override
    public void set(Object key, HashTree t) {
        if (!data.containsKey(key)) {
            order.add(key);
        }
        super.set(key, t);
    }

    /** {@inheritDoc} */
    @Override
    public void set(Object key, Object[] values) {
        if (!data.containsKey(key)) {
            order.add(key);
        }
        super.set(key, values);
    }

    /** {@inheritDoc} */
    @Override
    public void set(Object key, Collection<?> values) {
        if (!data.containsKey(key)) {
            order.add(key);
        }
        super.set(key, values);
    }

    /** {@inheritDoc} */
    @Override
    public void replace(Object currentKey, Object newKey) {
        HashTree tree = getTree(currentKey);
        data.remove(currentKey);
        data.put(newKey, tree);
        order.set(order.indexOf(currentKey), newKey);
    }

    /** {@inheritDoc} */
    @Override
    public HashTree createNewTree() {
        return new ListedHashTree();
    }

    /** {@inheritDoc} */
    @Override
    public HashTree createNewTree(Object key) {
        return new ListedHashTree(key);
    }

    /** {@inheritDoc} */
    @Override
    public HashTree createNewTree(Collection<?> values) {
        return new ListedHashTree(values);
    }

    /** {@inheritDoc} */
    @Override
    public HashTree add(Object key) {
        if (!data.containsKey(key)) {
            HashTree newTree = createNewTree();
            data.put(key, newTree);
            order.add(key);
            return newTree;
        }
        return getTree(key);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<Object> list() {
        return order;
    }

    /** {@inheritDoc} */
    @Override
    public Object remove(Object key) {
        order.remove(key);
        return data.remove(key);
    }

    /** {@inheritDoc} */
    @Override
    public Object[] getArray() {
        return order.toArray();
    }

    /** {@inheritDoc} */
    // Make sure the hashCode depends on the order as well
    @Override
    public int hashCode() {
        int hc = 17;
        hc = hc * 37 + (order == null ? 0 : order.hashCode());
        hc = hc * 37 + super.hashCode();
        return hc;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ListedHashTree)) {
            return false;
        }
        ListedHashTree lht = (ListedHashTree) o;
        return (super.equals(lht) && order.equals(lht.order));

        // boolean flag = true;
        // if (o instanceof ListedHashTree)
        // {
        // ListedHashTree oo = (ListedHashTree) o;
        // Iterator it = order.iterator();
        // Iterator it2 = oo.order.iterator();
        // if (size() != oo.size())
        // {
        // flag = false;
        // }
        // while (it.hasNext() && it2.hasNext() && flag)
        // {
        // if (!it.next().equals(it2.next()))
        // {
        // flag = false;
        // }
        // }
        // if (flag)
        // {
        // it = order.iterator();
        // while (it.hasNext() && flag)
        // {
        // Object temp = it.next();
        // flag = get(temp).equals(oo.get(temp));
        // }
        // }
        // }
        // else
        // {
        // flag = false;
        // }
        // return flag;
    }

    /** {@inheritDoc} */
    @Override
    public Set<Object> keySet() {
        return data.keySet();
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return data.size();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        super.clear();
        order.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12171.java