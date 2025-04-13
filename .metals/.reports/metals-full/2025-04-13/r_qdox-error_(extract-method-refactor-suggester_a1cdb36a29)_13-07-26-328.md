error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9595.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9595.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9595.java
text:
```scala
C@@ollection newCol = value.getClass().newInstance();

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

package org.apache.jmeter.testelement.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.jmeter.testelement.TestElement;

public class CollectionProperty extends MultiProperty {

    private static final long serialVersionUID = 221L; // Remember to change this when the class changes ...

    private Collection value;

    private transient Collection savedValue;

    public CollectionProperty(String name, Collection value) {
        super(name);
        this.value = normalizeList(value);
    }

    public CollectionProperty() {
        super();
        value = new ArrayList();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CollectionProperty) {
            if (value != null) {
                return value.equals(((JMeterProperty) o).getObjectValue());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (value == null ? 0 : value.hashCode());
    }

    public void remove(String prop) {
        PropertyIterator iter = iterator();
        while (iter.hasNext()) {
            if (iter.next().getName().equals(prop)) {
                iter.remove();
            }
        }
    }

    public void set(int index, String prop) {
        if (value instanceof List) {
            ((List) value).set(index, new StringProperty(prop, prop));
        }
    }

    public void set(int index, JMeterProperty prop) {
        if (value instanceof List) {
            ((List) value).set(index, prop);
        }
    }

    public JMeterProperty get(int row) {
        if (value instanceof List) {
            return (JMeterProperty) ((List) value).get(row);
        }
        return null;
    }

    public void remove(int index) {
        if (value instanceof List) {
            ((List) value).remove(index);
        }
    }

    public void setObjectValue(Object v) {
        if (v instanceof Collection) {
            setCollection((Collection) v);
        }

    }

    @Override
    public PropertyIterator iterator() {
        return getIterator(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see JMeterProperty#getStringValue()
     */
    public String getStringValue() {
        return value.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see JMeterProperty#getObjectValue()
     */
    public Object getObjectValue() {
        return value;
    }

    public int size() {
        return value.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see Object#clone()
     */
    @Override
    public Object clone() {
        CollectionProperty prop = (CollectionProperty) super.clone();
        prop.value = cloneCollection();
        return prop;
    }

    private Collection cloneCollection() {
        try {
            Collection newCol = (Collection) value.getClass().newInstance();
            PropertyIterator iter = iterator();
            while (iter.hasNext()) {
                newCol.add(iter.next().clone());
            }
            return newCol;
        } catch (Exception e) {
            log.error("Couldn't clone collection", e);
            return value;
        }
    }

    public void setCollection(Collection coll) {
        value = normalizeList(coll);
    }

    @Override
    public void clear() {
        value.clear();
    }

    /**
     * Easy way to add properties to the list.
     *
     * @param prop
     */
    @Override
    public void addProperty(JMeterProperty prop) {
        value.add(prop);
    }

    public void addItem(Object item) {
        addProperty(convertObject(item));
    }

    /**
     * Figures out what kind of properties this collection is holding and
     * returns the class type.
     *
     * @see AbstractProperty#getPropertyType()
     */
    @Override
    protected Class getPropertyType() {
        if (value.size() > 0) {
            return value.iterator().next().getClass();
        }
        return NullProperty.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see JMeterProperty#recoverRunningVersion(TestElement)
     */
    public void recoverRunningVersion(TestElement owner) {
        if (savedValue != null) {
            value = savedValue;
        }
        recoverRunningVersionOfSubElements(owner);
    }

    /*
     * (non-Javadoc)
     *
     * @see JMeterProperty#setRunningVersion(boolean)
     */
    @Override
    public void setRunningVersion(boolean running) {
        super.setRunningVersion(running);
        if (running) {
            savedValue = value;
        } else {
            savedValue = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9595.java