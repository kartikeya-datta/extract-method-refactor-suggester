error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9596.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9596.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9596.java
text:
```scala
M@@ap newCol = value.getClass().newInstance();

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

import java.util.Map;

import org.apache.jmeter.testelement.TestElement;

public class MapProperty extends MultiProperty {

    private static final long serialVersionUID = 221L; // Remember to change this when the class changes ...

    private Map value;

    private transient Map savedValue = null;

    public MapProperty(String name, Map value) {
        super(name);
        log.info("map = " + value);
        this.value = normalizeMap(value);
        log.info("normalized map = " + this.value);
    }

    public MapProperty() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MapProperty) {
            if (value != null) {
                return value.equals(((JMeterProperty) o).getObjectValue());
            }
        }
        return false;
    }

    public void setObjectValue(Object v) {
        if (v instanceof Map) {
            setMap((Map) v);
        }
    }

    @Override
    public void addProperty(JMeterProperty prop) {
        addProperty(prop.getName(), prop);
    }

    public JMeterProperty get(String key) {
        return (JMeterProperty) value.get(key);
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
            return valueIterator().next().getClass();
        }
        return NullProperty.class;
    }

    /**
     * @see JMeterProperty#getStringValue()
     */
    public String getStringValue() {
        return value.toString();
    }

    /**
     * @see JMeterProperty#getObjectValue()
     */
    public Object getObjectValue() {
        return value;
    }

    @Override
    public Object clone() {
        MapProperty prop = (MapProperty) super.clone();
        prop.value = cloneMap();
        return prop;
    }

    private Map cloneMap() {
        try {
            Map newCol = (Map) value.getClass().newInstance();
            PropertyIterator iter = valueIterator();
            while (iter.hasNext()) {
                JMeterProperty item = iter.next();
                newCol.put(item.getName(), item.clone());
            }
            return newCol;
        } catch (Exception e) {
            log.error("Couldn't clone map", e);
            return value;
        }
    }

    public PropertyIterator valueIterator() {
        return getIterator(value.values());
    }

    public void addProperty(String name, JMeterProperty prop) {
        if (!value.containsKey(name)) {
            value.put(name, prop);
        }
    }

    public void setMap(Map newMap) {
        value = normalizeMap(newMap);
    }

    /**
     * @see JMeterProperty#recoverRunningVersion(TestElement)
     */
    public void recoverRunningVersion(TestElement owner) {
        if (savedValue != null) {
            value = savedValue;
        }
        recoverRunningVersionOfSubElements(owner);
    }

    @Override
    public void clear() {
        value.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see MultiProperty#iterator()
     */
    @Override
    public PropertyIterator iterator() {
        return valueIterator();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9596.java