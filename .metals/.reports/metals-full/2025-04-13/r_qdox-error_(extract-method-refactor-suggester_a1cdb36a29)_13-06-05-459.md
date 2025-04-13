error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6363.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6363.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6363.java
text:
```scala
r@@eturn getObject() != null ? getObject().hashCode() : 0;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.ObjectUtils;

/**
 * <p>
 * A very simple implementation of the {@link ConcurrentInitializer} interface
 * which always returns the same object.
 * </p>
 * <p>
 * An instance of this class is passed a reference to an object when it is
 * constructed. The {@link #get()} method just returns this object. No
 * synchronization is required.
 * </p>
 * <p>
 * This class is useful for instance for unit testing or in cases where a
 * specific object has to be passed to an object which expects a
 * {@link ConcurrentInitializer}.
 * </p>
 *
 * @since 3.0
 * @version $Id$
 * @param <T> the type of the object managed by this initializer
 */
public class ConstantInitializer<T> implements ConcurrentInitializer<T> {
    /** Constant for the format of the string representation. */
    private static final String FMT_TO_STRING = "ConstantInitializer@%d [ object = %s ]";

    /** Stores the managed object. */
    private final T object;

    /**
     * Creates a new instance of {@code ConstantInitializer} and initializes it
     * with the object to be managed. The {@code get()} method will always
     * return the object passed here. This class does not place any restrictions
     * on the object. It may be <b>null</b>, then {@code get()} will return
     * <b>null</b>, too.
     *
     * @param obj the object to be managed by this initializer
     */
    public ConstantInitializer(T obj) {
        object = obj;
    }

    /**
     * Directly returns the object that was passed to the constructor. This is
     * the same object as returned by {@code get()}. However, this method does
     * not declare that it throws an exception.
     *
     * @return the object managed by this initializer
     */
    public final T getObject() {
        return object;
    }

    /**
     * Returns the object managed by this initializer. This implementation just
     * returns the object passed to the constructor.
     *
     * @return the object managed by this initializer
     * @throws ConcurrentException if an error occurs
     */
    public T get() throws ConcurrentException {
        return getObject();
    }

    /**
     * Returns a hash code for this object. This implementation returns the hash
     * code of the managed object.
     *
     * @return a hash code for this object
     */
    @Override
    public int hashCode() {
        return (getObject() != null) ? getObject().hashCode() : 0;
    }

    /**
     * Compares this object with another one. This implementation returns
     * <b>true</b> if and only if the passed in object is an instance of
     * {@code ConstantInitializer} which refers to an object equals to the
     * object managed by this instance.
     *
     * @param obj the object to compare to
     * @return a flag whether the objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConstantInitializer<?>)) {
            return false;
        }

        ConstantInitializer<?> c = (ConstantInitializer<?>) obj;
        return ObjectUtils.equals(getObject(), c.getObject());
    }

    /**
     * Returns a string representation for this object. This string also
     * contains a string representation of the object managed by this
     * initializer.
     *
     * @return a string for this object
     */
    @Override
    public String toString() {
        return String.format(FMT_TO_STRING, Integer.valueOf(System.identityHashCode(this)),
                String.valueOf(getObject()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6363.java