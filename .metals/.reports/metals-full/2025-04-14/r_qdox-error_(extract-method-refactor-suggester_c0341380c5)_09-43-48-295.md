error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4324.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4324.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 877
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4324.java
text:
```scala
public class InterceptedNonContextualComponentInjectionTarget<T> extends BasicInjectionTarget<T> {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
p@@ackage org.jboss.as.weld.injection;

import javax.enterprise.inject.spi.Bean;

import org.jboss.weld.annotated.enhanced.EnhancedAnnotatedType;
import org.jboss.weld.injection.producer.BasicInjectionTarget;
import org.jboss.weld.injection.producer.ConstructorInterceptionInstantiator;
import org.jboss.weld.injection.producer.DefaultInstantiator;
import org.jboss.weld.injection.producer.InterceptionModelInitializer;
import org.jboss.weld.injection.producer.InterceptorApplyingInstantiator;
import org.jboss.weld.injection.producer.SubclassedComponentInstantiator;
import org.jboss.weld.interceptor.spi.metadata.ClassMetadata;
import org.jboss.weld.interceptor.spi.model.InterceptionModel;
import org.jboss.weld.manager.BeanManagerImpl;

/**
 * {@link javax.enterprise.inject.spi.InjectionTarget} implementation used for non-contextual EE components such as
 * servlets, filters, web socket endpoints, ...
 *
 * This {@link javax.enterprise.inject.spi.InjectionTarget} implementation does not provide resource injection as it would
 * otherwise be performed twice.
 *
 * Interception support is provided to instances by Weld.
 *
 * @author Jozef Hartinger
 *
 * @param <T>
 */
class InterceptedNonContextualComponentInjectionTarget<T> extends BasicInjectionTarget<T> {

    public InterceptedNonContextualComponentInjectionTarget(EnhancedAnnotatedType<T> type, Bean<T> bean, BeanManagerImpl beanManager) {
        super(type, bean, beanManager);
        initializeInterceptors(type);
    }

    public void initializeInterceptors(EnhancedAnnotatedType<T> annotatedType) {
        initializeInterceptionModel(annotatedType);

        InterceptionModel<ClassMetadata<?>> interceptionModel = beanManager.getInterceptorModelRegistry().get(getType());
        boolean hasNonConstructorInterceptors = interceptionModel != null
                && (interceptionModel.hasExternalNonConstructorInterceptors() || interceptionModel.hasTargetClassInterceptors());

        if (hasNonConstructorInterceptors) {
            DefaultInstantiator<T> delegate = (DefaultInstantiator<T>) getInstantiator();
            setInstantiator(new SubclassedComponentInstantiator<T>(annotatedType, getBean(), delegate, beanManager));
            if (hasNonConstructorInterceptors) {
                setInstantiator(new InterceptorApplyingInstantiator<T>(getInstantiator(), interceptionModel, getType()));
            }
        }

        setupConstructorInterceptionInstantiator(interceptionModel);
    }

    private void initializeInterceptionModel(EnhancedAnnotatedType<T> annotatedType) {
        DefaultInstantiator<T> instantiator = (DefaultInstantiator<T>) getInstantiator();
        if (!beanManager.getInterceptorModelRegistry().containsKey(getType())) {
            new InterceptionModelInitializer<T>(beanManager, annotatedType, instantiator.getConstructorInjectionPoint().getAnnotated(), getBean()).init();
        }
    }

    private void setupConstructorInterceptionInstantiator(InterceptionModel<ClassMetadata<?>> interceptionModel) {
        if (interceptionModel != null && interceptionModel.hasExternalConstructorInterceptors()) {
            setInstantiator(new ConstructorInterceptionInstantiator<T>(getInstantiator(), interceptionModel, getType()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4324.java