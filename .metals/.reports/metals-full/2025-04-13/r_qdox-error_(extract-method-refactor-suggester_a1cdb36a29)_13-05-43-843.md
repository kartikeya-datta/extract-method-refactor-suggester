error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7894.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7894.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7894.java
text:
```scala
C@@lass<?> type = desc[x].getPropertyType();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.jmeter.testbeans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.Converter;
import org.apache.log.Logger;

/**
 * This is an experimental class. An attempt to address the complexity of
 * writing new JMeter components.
 * <p>
 * TestBean currently extends AbstractTestElement to support
 * backward-compatibility, but the property-value-map may later on be separated
 * from the test beans themselves. To ensure this will be doable with minimum
 * damage, all inherited methods are deprecated.
 *
 */
public class TestBeanHelper {
    protected static final Logger log = LoggingManager.getLoggerForClass();

    /**
     * Prepare the bean for work by populating the bean's properties from the
     * property value map.
     * <p>
     *
     * @deprecated to limit it's usage in expectation of moving it elsewhere.
     */
    @Deprecated
    public static void prepare(TestElement el) {
        if (!(el instanceof TestBean)) {
            return;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(el.getClass());
            PropertyDescriptor[] desc = beanInfo.getPropertyDescriptors();
            Object[] param = new Object[1];

            if (log.isDebugEnabled()) {
                log.debug("Preparing " + el.getClass());
            }

            for (int x = 0; x < desc.length; x++) {
                // Obtain a value of the appropriate type for this property.
                JMeterProperty jprop = el.getProperty(desc[x].getName());
                Class type = desc[x].getPropertyType();
                Object value = Converter.convert(jprop.getStringValue(), type);

                if (log.isDebugEnabled()) {
                    log.debug("Setting " + jprop.getName() + "=" + value);
                }

                // Set the bean's property to the value we just obtained:
                if (value != null || !type.isPrimitive())
                // We can't assign null to primitive types.
                {
                    param[0] = value;
                    Method writeMethod = desc[x].getWriteMethod();
                    if (writeMethod!=null) {
                        invokeOrBailOut(el, writeMethod, param);
                    }
                }
            }
        } catch (IntrospectionException e) {
            log.error("Couldn't set properties for " + el.getClass().getName(), e);
        }
    }

    /**
     * Utility method that invokes a method and does the error handling around
     * the invocation.
     *
     * @param method
     * @param params
     * @return the result of the method invocation.
     */
    private static Object invokeOrBailOut(Object invokee, Method method, Object[] params) {
        try {
            return method.invoke(invokee, params);
        } catch (IllegalArgumentException e) {
            log.error("This should never happen.", e);
            throw new Error(e.toString()); // Programming error: bail out.
        } catch (IllegalAccessException e) {
            log.error("This should never happen.", e);
            throw new Error(e.toString()); // Programming error: bail out.
        } catch (InvocationTargetException e) {
            log.error("This should never happen.", e);
            throw new Error(e.toString()); // Programming error: bail out.
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7894.java