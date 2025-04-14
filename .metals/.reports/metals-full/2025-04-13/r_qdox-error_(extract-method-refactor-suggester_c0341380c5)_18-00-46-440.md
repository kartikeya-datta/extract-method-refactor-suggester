error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10397.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10397.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10397.java
text:
```scala
c@@onfig.setValue(value != null ? value.toString() : "");

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

package org.apache.jmeter.save;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestElementTraverser;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.MapProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;

/**
 * Helper class for OldSaveService
 */
public class TestElementSaver implements TestElementTraverser {
    private String name;

    private LinkedList stack = new LinkedList();

    private DefaultConfiguration rootConfig = null;

    public TestElementSaver(String name) {
        this.name = name;
    }

    public Configuration getConfiguration() {
        return rootConfig;
    }

    /*
     * (non-Javadoc)
     *
     * @see TestElementTraverser#startTestElement(TestElement)
     */
    public void startTestElement(TestElement el) {
        DefaultConfiguration config = new DefaultConfiguration("testelement", "testelement");
        config.setAttribute("class", el.getClass().getName());
        if (rootConfig == null) {
            rootConfig = config;
            if (name != null && name.length() > 0) {
                rootConfig.setAttribute("name", name);
            }
        } else {
            setConfigName(config);
        }
        stack.add(config);
    }

    public void setConfigName(DefaultConfiguration config) {
        if (!(stack.getLast() instanceof Configuration)) {
            Object key = stack.removeLast();
            config.setAttribute("name", key.toString());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see TestElementTraverser#endTestElement(TestElement)
     */
    public void endTestElement(TestElement el) {
    }

    /*
     * (non-Javadoc)
     *
     * @see TestElementTraverser#simplePropertyValue(JMeterProperty)
     */
    public void simplePropertyValue(JMeterProperty value) {
        try {
            Object parent = stack.getLast();
            if (!(parent instanceof Configuration)) {
                DefaultConfiguration config = new DefaultConfiguration("property", "property");
                config.setValue(value != null ? value.toString() : "");
                config.setAttribute("name", parent.toString());
                config.setAttribute(OldSaveService.XML_SPACE, OldSaveService.PRESERVE);
                stack.removeLast();
                stack.add(config);
            }

            if (parent instanceof DefaultConfiguration && value instanceof Configuration) {
                ((DefaultConfiguration) parent).addChild((Configuration) value);
            } else if (parent instanceof DefaultConfiguration && !(value instanceof Configuration)) {
                DefaultConfiguration config = new DefaultConfiguration("string", "string");
                config.setValue(value.toString());
                config.setAttribute(OldSaveService.XML_SPACE, OldSaveService.PRESERVE);
                ((DefaultConfiguration) parent).addChild(config);
            }
        } catch (NoSuchElementException e) {
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see TestElementTraverser#startMap(MapProperty)
     */
    public void startMap(MapProperty map) {
        DefaultConfiguration config = new DefaultConfiguration("map", "map");
        config.setAttribute("class", map.getObjectValue().getClass().getName());
        config.setAttribute("name", map.getName());
        config.setAttribute("propType", map.getClass().getName());
        stack.add(config);
    }

    /*
     * It appears that this method is no longer used. jeremy_a@bigfoot.com 02
     * May 2003 public void endMap(MapProperty map) { finishConfig(); }
     */

    /*
     * (non-Javadoc)
     *
     * @see TestElementTraverser#startCollection(CollectionProperty)
     */
    public void startCollection(CollectionProperty col) {
        DefaultConfiguration config = new DefaultConfiguration("collection", "collection");
        config.setAttribute("class", col.getObjectValue().getClass().getName());
        config.setAttribute("name", col.getName());
        config.setAttribute("propType", col.getClass().getName());
        stack.add(config);
    }

    /*
     * It appears that this method is no longer used. jeremy_a@bigfoot.com 02
     * May 2003 public void endCollection(CollectionProperty col) {
     * finishConfig(); }
     */

    /*
     * (non-Javadoc)
     *
     * @see TestElementTraverser#endProperty(JMeterProperty)
     */
    public void endProperty(JMeterProperty key) {
        finishConfig();
    }

    private void finishConfig() {
        if (stack.size() > 1) {
            Configuration config = (Configuration) stack.removeLast();
            ((DefaultConfiguration) stack.getLast()).addChild(config);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see TestElementTraverser#startProperty(JMeterProperty)
     */
    public void startProperty(JMeterProperty key) {
        if (key instanceof CollectionProperty) {
            startCollection((CollectionProperty) key);
        } else if (key instanceof MapProperty) {
            startMap((MapProperty) key);
        } else if (key instanceof TestElementProperty) {
            stack.addLast(key.getName());
        } else {
            DefaultConfiguration config = new DefaultConfiguration("property", "property");
            config.setValue(key.getStringValue());
            config.setAttribute("name", key.getName());
            config.setAttribute("propType", key.getClass().getName());
            config.setAttribute(OldSaveService.XML_SPACE, OldSaveService.PRESERVE);
            stack.addLast(config);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10397.java