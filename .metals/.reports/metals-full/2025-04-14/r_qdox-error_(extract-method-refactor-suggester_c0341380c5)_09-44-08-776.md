error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15090.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15090.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15090.java
text:
```scala
O@@bject cls = BrokerFactoryValue.get(conf);

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
package org.apache.openjpa.kernel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.openjpa.conf.BrokerFactoryValue;
import org.apache.openjpa.conf.ProductDerivations;
import org.apache.openjpa.lib.conf.ConfigurationProvider;
import org.apache.openjpa.lib.conf.MapConfigurationProvider;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.util.InternalException;
import org.apache.openjpa.util.OpenJPAException;
import org.apache.openjpa.util.UserException;

/**
 * Helper methods for acquiring {@link BrokerFactory} objects
 *
 * @since 4.0.0
 */
public class Bootstrap {

    private static final Class[] FACTORY_ARGS =
        new Class[]{ ConfigurationProvider.class };

    private static Localizer s_loc = Localizer.forPackage(Bootstrap.class);

    /**
     * Return a new factory for the default configuration.
     */
    public static BrokerFactory newBrokerFactory() {
        return Bootstrap.newBrokerFactory(null, null);
    }

    /**
     * Return a new factory for the given configuration. The classloader
     * will be used to load the factory class. If no classloader is given,
     * the thread's context classloader is used.
     */
    public static BrokerFactory newBrokerFactory(ConfigurationProvider conf,
        ClassLoader loader) {
        if (conf == null)
            conf = new MapConfigurationProvider();
        ProductDerivations.beforeConfigurationConstruct(conf);

        Class cls = getFactoryClass(conf, loader);
        try {
            Method meth = cls.getMethod("newInstance", FACTORY_ARGS);
            return (BrokerFactory) meth.invoke(null, new Object[]{ conf });
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getTargetException();
            if (cause instanceof OpenJPAException)
                throw (OpenJPAException) cause;
            throw new InternalException(s_loc.get("new-brokerfactory-excep",
                cls), cause);
        } catch (Exception e) {
            throw new UserException(s_loc.get("bad-new-brokerfactory", cls),
                e).setFatal(true);
        }
    }

    /**
     * Return a pooled factory for the default configuration.
     */
    public static BrokerFactory getBrokerFactory() {
        return Bootstrap.getBrokerFactory(null, null);
    }

    /**
     * Return a pooled factory for the given configuration. The classloader
     * will be used to load the factory class. If no classloader is given,
     * the thread's context classloader is used.
     */
    public static BrokerFactory getBrokerFactory(ConfigurationProvider conf,
        ClassLoader loader) {
        if (conf == null)
            conf = new MapConfigurationProvider();
        ProductDerivations.beforeConfigurationConstruct(conf);

        Class cls = getFactoryClass(conf, loader);
        try {
            Method meth = cls.getMethod("getInstance", FACTORY_ARGS);
            return (BrokerFactory) meth.invoke(null, new Object[]{ conf });
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getTargetException();
            if (cause instanceof OpenJPAException)
                throw (OpenJPAException) cause;
            throw new InternalException(s_loc.get("brokerfactory-excep", cls),
                cause);
        } catch (Exception e) {
            throw new UserException(s_loc.get("bad-brokerfactory", cls), e).
                setFatal(true);
        }
    }

    /**
     * Instantiate the factory class designated in properties.
     */
    private static Class getFactoryClass(ConfigurationProvider conf,
        ClassLoader loader) {
        if (loader == null)
            loader = Thread.currentThread().getContextClassLoader();

        Object cls = BrokerFactoryValue.getBrokerFactoryClassName(conf);
        if (cls instanceof Class)
            return (Class) cls;

        BrokerFactoryValue value = new BrokerFactoryValue();
        value.setString((String) cls);
        String clsName = value.getClassName();
        if (clsName == null)
            throw new UserException(s_loc.get("no-brokerfactory", 
                conf.getProperties())).setFatal(true);

        try {
            return Class.forName(clsName, true, loader);
        } catch (Exception e) {
            throw new UserException(s_loc.get("bad-brokerfactory-class",
                clsName), e).setFatal(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15090.java