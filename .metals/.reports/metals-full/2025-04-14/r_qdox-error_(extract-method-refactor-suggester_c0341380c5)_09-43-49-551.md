error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17290.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17290.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17290.java
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

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.lib.util.Localizer;

/**
 * A plugin {@link Value} consisting of plugin name and properties.
 * Plugins should be specified in the form:<br />
 * <code>&lt;plugin-name&gt;(&lt;prop1&gt;=&lt;val1&gt;, ...)</code><br />
 * Both the plugin name and prop list are optional, so that the following
 * forms are also valid:<br />
 * <code>&lt;plugin-name&gt;</code><br />
 * <code>&lt;prop1&gt;=&lt;val1&gt; ...</code>
 * Defaults and aliases on plugin values apply only to the plugin name.
 *
 * @author Abe White
 */
public class PluginValue extends ObjectValue {

    private static final Localizer _loc = Localizer.forPackage
        (PluginValue.class);

    private final boolean _singleton;
    private String _name = null;
    private String _props = null;

    public PluginValue(String prop, boolean singleton) {
        super(prop);
        _singleton = singleton;
    }

    /**
     * Whether this value is a singleton.
     */
    public boolean isSingleton() {
        return _singleton;
    }

    /**
     * The plugin class name.
     */
    public String getClassName() {
        return _name;
    }

    /**
     * The plugin class name.
     */
    public void setClassName(String name) {
        String oldName = _name;
        _name = name;
        if (!StringUtils.equals(oldName, name)) {
            if (_singleton)
                set(null, true);
            valueChanged();
        }
    }

    /**
     * The plugin properties.
     */
    public String getProperties() {
        return _props;
    }

    /**
     * The plugin properties.
     */
    public void setProperties(String props) {
        String oldProps = _props;
        _props = props;
        if (!StringUtils.equals(oldProps, props)) {
            if (_singleton)
                set(null, true);
            valueChanged();
        }
    }

    /**
     * Instantiate the plugin as an instance of the given class.
     */
    public Object instantiate(Class type, Configuration conf, boolean fatal) {
        Object obj = newInstance(_name, type, conf, fatal);
        Configurations.configureInstance(obj, conf, _props,
            (fatal) ? getProperty() : null);
        if (_singleton)
            set(obj, true);
        return obj;
    }

    public void set(Object obj, boolean derived) {
        if (!_singleton)
            throw new IllegalStateException(_loc.get("not-singleton",
                getProperty()));
        super.set(obj, derived);
    }

    public String getString() {
        return Configurations.getPlugin(alias(_name), _props);
    }

    public void setString(String str) {
        _name = Configurations.getClassName(str);
        _name = unalias(_name);
        _props = Configurations.getProperties(str);
        if (_singleton)
            set(null, true);
        valueChanged();
    }

    public Class getValueType() {
        return Object.class;
    }

    protected void objectChanged() {
        Object obj = get();
        _name = (obj == null) ? unalias(null) : obj.getClass().getName();
        _props = null;
    }

    protected String getInternalString() {
        // should never get called
        throw new IllegalStateException();
    }

    protected void setInternalString(String str) {
        // should never get called
        throw new IllegalStateException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17290.java