error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7274.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7274.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7274.java
text:
```scala
r@@eturn new Dependency<>(null, key, true, -1);

/**
 * Copyright (C) 2008 Google Inc.
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

package org.elasticsearch.common.inject.spi;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.elasticsearch.common.inject.Key;

import java.util.List;
import java.util.Set;

/**
 * A variable that can be resolved by an injector.
 * <p/>
 * <p>Use {@link #get} to build a freestanding dependency, or {@link InjectionPoint} to build one
 * that's attached to a constructor, method or field.
 *
 * @author crazybob@google.com (Bob Lee)
 * @author jessewilson@google.com (Jesse Wilson)
 * @since 2.0
 */
public final class Dependency<T> {
    private final InjectionPoint injectionPoint;
    private final Key<T> key;
    private final boolean nullable;
    private final int parameterIndex;

    Dependency(InjectionPoint injectionPoint, Key<T> key,
               boolean nullable, int parameterIndex) {
        this.injectionPoint = injectionPoint;
        this.key = key;
        this.nullable = nullable;
        this.parameterIndex = parameterIndex;
    }

    /**
     * Returns a new dependency that is not attached to an injection point. The returned dependency is
     * nullable.
     */
    public static <T> Dependency<T> get(Key<T> key) {
        return new Dependency<T>(null, key, true, -1);
    }

    /**
     * Returns the dependencies from the given injection points.
     */
    public static Set<Dependency<?>> forInjectionPoints(Set<InjectionPoint> injectionPoints) {
        List<Dependency<?>> dependencies = Lists.newArrayList();
        for (InjectionPoint injectionPoint : injectionPoints) {
            dependencies.addAll(injectionPoint.getDependencies());
        }
        return ImmutableSet.copyOf(dependencies);
    }

    /**
     * Returns the key to the binding that satisfies this dependency.
     */
    public Key<T> getKey() {
        return this.key;
    }

    /**
     * Returns true if null is a legal value for this dependency.
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Returns the injection point to which this dependency belongs, or null if this dependency isn't
     * attached to a particular injection point.
     */
    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    /**
     * Returns the index of this dependency in the injection point's parameter list, or {@code -1} if
     * this dependency does not belong to a parameter list. Only method and constuctor dependencies
     * are elements in a parameter list.
     */
    public int getParameterIndex() {
        return parameterIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(injectionPoint, parameterIndex, key);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Dependency) {
            Dependency dependency = (Dependency) o;
            return Objects.equal(injectionPoint, dependency.injectionPoint)
                    && Objects.equal(parameterIndex, dependency.parameterIndex)
                    && Objects.equal(key, dependency.key);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(key);
        if (injectionPoint != null) {
            builder.append("@").append(injectionPoint);
            if (parameterIndex != -1) {
                builder.append("[").append(parameterIndex).append("]");
            }
        }
        return builder.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7274.java