error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7268.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7268.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7268.java
text:
```scala
t@@his.binding = new UntargettedBindingImpl<>(source, key, Scoping.UNSCOPED);

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

import org.elasticsearch.common.inject.Binder;
import org.elasticsearch.common.inject.Key;
import org.elasticsearch.common.inject.Scope;
import org.elasticsearch.common.inject.spi.Element;
import org.elasticsearch.common.inject.spi.InstanceBinding;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Bind a value or constant.
 *
 * @author jessewilson@google.com (Jesse Wilson)
 */
public abstract class AbstractBindingBuilder<T> {

    public static final String IMPLEMENTATION_ALREADY_SET = "Implementation is set more than once.";
    public static final String SINGLE_INSTANCE_AND_SCOPE
            = "Setting the scope is not permitted when binding to a single instance.";
    public static final String SCOPE_ALREADY_SET = "Scope is set more than once.";
    public static final String BINDING_TO_NULL = "Binding to null instances is not allowed. "
            + "Use toProvider(Providers.of(null)) if this is your intended behaviour.";
    public static final String CONSTANT_VALUE_ALREADY_SET = "Constant value is set more than once.";
    public static final String ANNOTATION_ALREADY_SPECIFIED
            = "More than one annotation is specified for this binding.";

    protected static final Key<?> NULL_KEY = Key.get(Void.class);

    protected List<Element> elements;
    protected int position;
    protected final Binder binder;
    private BindingImpl<T> binding;

    public AbstractBindingBuilder(Binder binder, List<Element> elements, Object source, Key<T> key) {
        this.binder = binder;
        this.elements = elements;
        this.position = elements.size();
        this.binding = new UntargettedBindingImpl<T>(source, key, Scoping.UNSCOPED);
        elements.add(position, this.binding);
    }

    protected BindingImpl<T> getBinding() {
        return binding;
    }

    protected BindingImpl<T> setBinding(BindingImpl<T> binding) {
        this.binding = binding;
        elements.set(position, binding);
        return binding;
    }

    /**
     * Sets the binding to a copy with the specified annotation on the bound key
     */
    protected BindingImpl<T> annotatedWithInternal(Class<? extends Annotation> annotationType) {
        checkNotNull(annotationType, "annotationType");
        checkNotAnnotated();
        return setBinding(binding.withKey(
                Key.get(this.binding.getKey().getTypeLiteral(), annotationType)));
    }

    /**
     * Sets the binding to a copy with the specified annotation on the bound key
     */
    protected BindingImpl<T> annotatedWithInternal(Annotation annotation) {
        checkNotNull(annotation, "annotation");
        checkNotAnnotated();
        return setBinding(binding.withKey(
                Key.get(this.binding.getKey().getTypeLiteral(), annotation)));
    }

    public void in(final Class<? extends Annotation> scopeAnnotation) {
        checkNotNull(scopeAnnotation, "scopeAnnotation");
        checkNotScoped();
        setBinding(getBinding().withScoping(Scoping.forAnnotation(scopeAnnotation)));
    }

    public void in(final Scope scope) {
        checkNotNull(scope, "scope");
        checkNotScoped();
        setBinding(getBinding().withScoping(Scoping.forInstance(scope)));
    }

    public void asEagerSingleton() {
        checkNotScoped();
        setBinding(getBinding().withScoping(Scoping.EAGER_SINGLETON));
    }

    protected boolean keyTypeIsSet() {
        return !Void.class.equals(binding.getKey().getTypeLiteral().getType());
    }

    protected void checkNotTargetted() {
        if (!(binding instanceof UntargettedBindingImpl)) {
            binder.addError(IMPLEMENTATION_ALREADY_SET);
        }
    }

    protected void checkNotAnnotated() {
        if (binding.getKey().getAnnotationType() != null) {
            binder.addError(ANNOTATION_ALREADY_SPECIFIED);
        }
    }

    protected void checkNotScoped() {
        // Scoping isn't allowed when we have only one instance.
        if (binding instanceof InstanceBinding) {
            binder.addError(SINGLE_INSTANCE_AND_SCOPE);
            return;
        }

        if (binding.getScoping().isExplicitlyScoped()) {
            binder.addError(SCOPE_ALREADY_SET);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7268.java