error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17289.java
text:
```scala
g@@etProperty()).getMessage());

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.lib.conf;

import org.apache.commons.lang.ObjectUtils;
import org.apache.openjpa.lib.util.Localizer;

/**
 * An object {@link Value}.
 *
 * @author Abe White
 */
public class ObjectValue extends Value {

    private static final Localizer _loc = Localizer.forPackage
        (ObjectValue.class);

    private Object _value = null;

    public ObjectValue(String prop) {
        super(prop);
    }

    /**
     * The internal value.
     */
    public Object get() {
        return _value;
    }

    /**
     * The internal value.
     */
    public void set(Object obj) {
        set(obj, false);
    }

    /**
     * The internal value.
     *
     * @param derived if true, this value was derived from other properties
     */
    public void set(Object obj, boolean derived) {
        Object oldValue = _value;
        _value = obj;
        if (!derived && !ObjectUtils.equals(obj, oldValue)) {
            objectChanged();
            valueChanged();
        }
    }

    /**
     * Instantiate the object as an instance of the given class. Equivalent
     * to <code>instantiate(type, conf, true)</code>.
     */
    public Object instantiate(Class type, Configuration conf) {
        return instantiate(type, conf, true);
    }

    /**
     * Instantiate the object as an instance of the given class.
     */
    public Object instantiate(Class type, Configuration conf, boolean fatal) {
        throw new UnsupportedOperationException();
    }

    /**
     * Allow subclasses to instantiate additional plugins. This method does
     * not perform configuration.
     */
    public Object newInstance(String clsName, Class type,
        Configuration conf, boolean fatal) {
        return Configurations.newInstance(clsName, this, conf,
            type.getClassLoader(), fatal);
    }

    public Class getValueType() {
        return Object.class;
    }

    /**
     * Implement this method to synchronize internal data with the new
     * object value.
     */
    protected void objectChanged() {
    }

    protected String getInternalString() {
        return null;
    }

    protected void setInternalString(String str) {
        if (str == null)
            set(null);
        else
            throw new IllegalArgumentException(_loc.get("cant-set-string",
                getProperty()));
    }

    protected void setInternalObject(Object obj) {
        set(obj);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17289.java