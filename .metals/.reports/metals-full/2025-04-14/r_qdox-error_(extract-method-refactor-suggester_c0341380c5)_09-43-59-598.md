error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7265.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7265.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7265.java
text:
```scala
I@@njectableReference<T> initializable = new InjectableReference<>(injector, instance, source);

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

package org.elasticsearch.common.inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.common.inject.internal.Errors;
import org.elasticsearch.common.inject.internal.ErrorsException;
import org.elasticsearch.common.inject.spi.InjectionPoint;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Manages and injects instances at injector-creation time. This is made more complicated by
 * instances that request other instances while they're being injected. We overcome this by using
 * {@link Initializable}, which attempts to perform injection before use.
 *
 * @author jessewilson@google.com (Jesse Wilson)
 */
class Initializer {
    /**
     * the only thread that we'll use to inject members.
     */
    private final Thread creatingThread = Thread.currentThread();

    /**
     * zero means everything is injected.
     */
    private final CountDownLatch ready = new CountDownLatch(1);

    /**
     * Maps instances that need injection to a source that registered them
     */
    private final Map<Object, InjectableReference<?>> pendingInjection = Maps.newIdentityHashMap();

    /**
     * Registers an instance for member injection when that step is performed.
     *
     * @param instance an instance that optionally has members to be injected (each annotated with
     * @param source   the source location that this injection was requested
     * @Inject).
     */
    public <T> Initializable<T> requestInjection(InjectorImpl injector, T instance, Object source,
                                                 Set<InjectionPoint> injectionPoints) {
        checkNotNull(source);

        // short circuit if the object has no injections
        if (instance == null
 (injectionPoints.isEmpty() && !injector.membersInjectorStore.hasTypeListeners())) {
            return Initializables.of(instance);
        }

        InjectableReference<T> initializable = new InjectableReference<T>(injector, instance, source);
        pendingInjection.put(instance, initializable);
        return initializable;
    }

    /**
     * Prepares member injectors for all injected instances. This prompts Guice to do static analysis
     * on the injected instances.
     */
    void validateOustandingInjections(Errors errors) {
        for (InjectableReference<?> reference : pendingInjection.values()) {
            try {
                reference.validate(errors);
            } catch (ErrorsException e) {
                errors.merge(e.getErrors());
            }
        }
    }

    /**
     * Performs creation-time injections on all objects that require it. Whenever fulfilling an
     * injection depends on another object that requires injection, we inject it first. If the two
     * instances are codependent (directly or transitively), ordering of injection is arbitrary.
     */
    void injectAll(final Errors errors) {
        // loop over a defensive copy since ensureInjected() mutates the set. Unfortunately, that copy
        // is made complicated by a bug in IBM's JDK, wherein entrySet().toArray(Object[]) doesn't work
        for (InjectableReference<?> reference : Lists.newArrayList(pendingInjection.values())) {
            try {
                reference.get(errors);
            } catch (ErrorsException e) {
                errors.merge(e.getErrors());
            }
        }

        if (!pendingInjection.isEmpty()) {
            throw new AssertionError("Failed to satisfy " + pendingInjection);
        }

        ready.countDown();
    }

    private class InjectableReference<T> implements Initializable<T> {
        private final InjectorImpl injector;
        private final T instance;
        private final Object source;
        private MembersInjectorImpl<T> membersInjector;

        public InjectableReference(InjectorImpl injector, T instance, Object source) {
            this.injector = injector;
            this.instance = checkNotNull(instance, "instance");
            this.source = checkNotNull(source, "source");
        }

        public void validate(Errors errors) throws ErrorsException {
            @SuppressWarnings("unchecked") // the type of 'T' is a TypeLiteral<T>
                    TypeLiteral<T> type = TypeLiteral.get((Class<T>) instance.getClass());
            membersInjector = injector.membersInjectorStore.get(type, errors.withSource(source));
        }

        /**
         * Reentrant. If {@code instance} was registered for injection at injector-creation time, this
         * method will ensure that all its members have been injected before returning.
         */
        public T get(Errors errors) throws ErrorsException {
            if (ready.getCount() == 0) {
                return instance;
            }

            // just wait for everything to be injected by another thread
            if (Thread.currentThread() != creatingThread) {
                try {
                    ready.await();
                    return instance;
                } catch (InterruptedException e) {
                    // Give up, since we don't know if our injection is ready
                    throw new RuntimeException(e);
                }
            }

            // toInject needs injection, do it right away. we only do this once, even if it fails
            if (pendingInjection.remove(instance) != null) {
                membersInjector.injectAndNotify(instance, errors.withSource(source));
            }

            return instance;
        }

        @Override
        public String toString() {
            return instance.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7265.java