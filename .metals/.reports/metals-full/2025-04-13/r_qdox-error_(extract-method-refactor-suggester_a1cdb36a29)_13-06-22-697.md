error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7269.java
text:
```scala
s@@etBinding(new InstanceBindingImpl<>(

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

package org.elasticsearch.common.inject.internal;

import com.google.common.collect.ImmutableSet;
import org.elasticsearch.common.inject.Binder;
import org.elasticsearch.common.inject.Key;
import org.elasticsearch.common.inject.binder.AnnotatedConstantBindingBuilder;
import org.elasticsearch.common.inject.binder.ConstantBindingBuilder;
import org.elasticsearch.common.inject.spi.Element;
import org.elasticsearch.common.inject.spi.InjectionPoint;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Bind a constant.
 *
 * @author jessewilson@google.com (Jesse Wilson)
 */
public final class ConstantBindingBuilderImpl<T>
        extends AbstractBindingBuilder<T>
        implements AnnotatedConstantBindingBuilder, ConstantBindingBuilder {

    @SuppressWarnings("unchecked") // constant bindings start out with T unknown
    public ConstantBindingBuilderImpl(Binder binder, List<Element> elements, Object source) {
        super(binder, elements, source, (Key<T>) NULL_KEY);
    }

    public ConstantBindingBuilder annotatedWith(Class<? extends Annotation> annotationType) {
        annotatedWithInternal(annotationType);
        return this;
    }

    public ConstantBindingBuilder annotatedWith(Annotation annotation) {
        annotatedWithInternal(annotation);
        return this;
    }

    public void to(final String value) {
        toConstant(String.class, value);
    }

    public void to(final int value) {
        toConstant(Integer.class, value);
    }

    public void to(final long value) {
        toConstant(Long.class, value);
    }

    public void to(final boolean value) {
        toConstant(Boolean.class, value);
    }

    public void to(final double value) {
        toConstant(Double.class, value);
    }

    public void to(final float value) {
        toConstant(Float.class, value);
    }

    public void to(final short value) {
        toConstant(Short.class, value);
    }

    public void to(final char value) {
        toConstant(Character.class, value);
    }

    public void to(final Class<?> value) {
        toConstant(Class.class, value);
    }

    public <E extends Enum<E>> void to(final E value) {
        toConstant(value.getDeclaringClass(), value);
    }

    private void toConstant(Class<?> type, Object instance) {
        // this type will define T, so these assignments are safe
        @SuppressWarnings("unchecked")
        Class<T> typeAsClassT = (Class<T>) type;
        @SuppressWarnings("unchecked")
        T instanceAsT = (T) instance;

        if (keyTypeIsSet()) {
            binder.addError(CONSTANT_VALUE_ALREADY_SET);
            return;
        }

        BindingImpl<T> base = getBinding();
        Key<T> key;
        if (base.getKey().getAnnotation() != null) {
            key = Key.get(typeAsClassT, base.getKey().getAnnotation());
        } else if (base.getKey().getAnnotationType() != null) {
            key = Key.get(typeAsClassT, base.getKey().getAnnotationType());
        } else {
            key = Key.get(typeAsClassT);
        }

        if (instanceAsT == null) {
            binder.addError(BINDING_TO_NULL);
        }

        setBinding(new InstanceBindingImpl<T>(
                base.getSource(), key, base.getScoping(), ImmutableSet.<InjectionPoint>of(), instanceAsT));
    }

    @Override
    public String toString() {
        return "ConstantBindingBuilder";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7269.java