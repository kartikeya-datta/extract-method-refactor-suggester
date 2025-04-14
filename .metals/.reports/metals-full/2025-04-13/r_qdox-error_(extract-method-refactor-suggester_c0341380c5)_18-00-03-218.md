error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10348.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10348.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10348.java
text:
```scala
T@@upleElementImpl<?> element = new TupleElementImpl(value == null ? Object.class : value.getClass());

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.openjpa.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.apache.openjpa.kernel.ExpressionStoreQuery;
import org.apache.openjpa.lib.util.Localizer;

public class TupleImpl implements Tuple {
    private static final Localizer _loc = Localizer.forPackage(TupleImpl.class);
    List<TupleElement<?>> elements = new ArrayList<TupleElement<?>>();

    /**
     * Get the value of the specified tuple element.
     * 
     * @param tupleElement
     *            tuple element
     * @return value of tuple element
     * @throws IllegalArgumentException
     *             if tuple element does not correspond to an element in the query result tuple
     */
    public <X> X get(TupleElement<X> tupleElement) {
        if (!elements.contains(tupleElement)) {
            throw new IllegalArgumentException(_loc.get(
                "tuple-element-not-found",
                new Object[] { tupleElement, elements }).getMessage());
        }
        TupleElementImpl<X> impl = (TupleElementImpl<X>) tupleElement;
        return impl.getValue();
    }

    /**
     * Get the value of the tuple element to which the specified alias has been assigned.
     * 
     * @param alias
     *            alias assigned to tuple element
     * @param type
     *            of the tuple element
     * @return value of the tuple element
     * @throws IllegalArgumentException
     *             if alias does not correspond to an element in the query result tuple or element cannot be assigned to
     *             the specified type
     */
    @SuppressWarnings("unchecked")
    public <X> X get(String alias, Class<X> type) {
        if (type == null) {
            throw new IllegalArgumentException(_loc.get("tuple-was-null", "type").getMessage());
        }
        Object rval = get(alias);
        if (!type.isAssignableFrom(rval.getClass())) {
            throw new IllegalArgumentException(_loc.get(
                "tuple-element-wrong-type",
                new Object[] { alias, type, rval.getClass() }).getMessage());
        }
        return (X) rval;
    }

    /**
     * Get the value of the tuple element to which the specified alias has been assigned.
     * 
     * @param alias
     *            alias assigned to tuple element
     * @return value of the tuple element
     * @throws IllegalArgumentException
     *             if alias does not correspond to an element in the query result tuple
     */
    public Object get(String alias) {
        if (alias == null) {
            // TODO MDD determine if we can support this. 
            throw new IllegalArgumentException(_loc.get("typle-was-null", "alias").getMessage());
        }
        for (TupleElement<?> te : elements) {
            if (alias.equals(te.getAlias())) {
                return ((TupleElementImpl<?>) te).getValue();
            }
        }

        List<String> knownAliases = new ArrayList<String>();
        for(TupleElement<?> te : elements) { 
            knownAliases.add(te.getAlias());
        }
        throw new IllegalArgumentException(_loc.get("tuple-alias-not-found",
            new Object[] { alias, knownAliases }).getMessage());
    }

    /**
     * Get the value of the element at the specified position in the result tuple. The first position is 0.
     * 
     * @param i
     *            position in result tuple
     * @param type
     *            type of the tuple element
     * @return value of the tuple element
     * @throws IllegalArgumentException
     *             if i exceeds length of result tuple or element cannot be assigned to the specified type
     */
    @SuppressWarnings("unchecked")
    public <X> X get(int i, Class<X> type) {
        if (type == null) {
            throw new IllegalArgumentException(_loc.get("tuple-was-null", "type").getMessage());
        }
        Object rval = get(i);
        if(! type.isAssignableFrom(rval.getClass())) { 
            throw new IllegalArgumentException(_loc.get(
                "tuple-element-wrong-type",
                new Object[] { "position", i, type, type.getClass() }).getMessage());
        }
        return (X) rval;
    }

    /**
     * Get the value of the element at the specified position in the result tuple. The first position is 0.
     * 
     * @param i
     *            position in result tuple
     * @return value of the tuple element
     * @throws IllegalArgumentException
     *             if i exceeds length of result tuple
     */
    public Object get(int i) {
        if (i > elements.size()) {
            throw new IllegalArgumentException(_loc.get("tuple-exceeded-size",
                new Object[] { i, elements.size() }).getMessage());
        }
        if (i <= -1) {
            throw new IllegalArgumentException(_loc.get("tuple-stop-thinking-in-python").getMessage());
        }
        return toArray()[i];
    }

    /**
     * Return the values of the result tuple elements as an array.
     * 
     * @return tuple element values
     */
    public Object[] toArray() {
        Object[] rval = new Object[elements.size()];
        int i = 0;
        for (TupleElement<?> tupleElement : elements) {
            rval[i] = ((TupleElementImpl<?>) tupleElement).getValue();
            i++;
        }
        return rval;
    }

    /**
     * Return the tuple elements
     * 
     * @return tuple elements
     */
    public List<TupleElement<?>> getElements() {
        return elements;
    }

    @SuppressWarnings("unchecked")
    public void put(Object key, Object value) {
        // TODO check for duplicate aliases? 
        TupleElementImpl<?> element = new TupleElementImpl(value.getClass());
        element.setAlias((String) key);
        element.setValue(value);
        elements.add(element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10348.java