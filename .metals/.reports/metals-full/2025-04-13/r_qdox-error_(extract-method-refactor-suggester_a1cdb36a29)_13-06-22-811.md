error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8544.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8544.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8544.java
text:
```scala
b@@uilder.setPostActivate(override.getPostActivate());

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.ee.component.interceptors;

import org.jboss.invocation.proxy.MethodIdentifier;

/**
 * Post class loading description of an interceptor class.
 *
 * @author Stuart Douglas
 */
public class InterceptorClassDescription {

    public static final InterceptorClassDescription EMPTY_INSTANCE = new Builder().build();
    private final MethodIdentifier aroundInvoke;
    private final MethodIdentifier aroundTimeout;
    private final MethodIdentifier preDestroy;
    private final MethodIdentifier postConstruct;
    private final MethodIdentifier prePassivate;
    private final MethodIdentifier postActivate;

    public InterceptorClassDescription(final MethodIdentifier aroundInvoke, final MethodIdentifier aroundTimeout, final MethodIdentifier preDestroy, final MethodIdentifier postConstruct, final MethodIdentifier postActivate, final MethodIdentifier prePassivate) {
        this.aroundInvoke = aroundInvoke;
        this.aroundTimeout = aroundTimeout;
        this.preDestroy = preDestroy;
        this.postConstruct = postConstruct;
        this.postActivate = postActivate;
        this.prePassivate = prePassivate;
    }

    /**
     * Merges two descriptors, either of the parameters will be null.
     * <p/>
     * this method will never return null;
     *
     * @param existing
     * @param override
     * @return
     */
    public static InterceptorClassDescription merge(InterceptorClassDescription existing, InterceptorClassDescription override) {
        if (existing == null && override == null) {
            return EMPTY_INSTANCE;
        }
        if (override == null) {
            return existing;
        }
        if (existing == null) {
            return override;
        }
        final Builder builder = builder(existing);
        if (override.getAroundInvoke() != null) {
            builder.setAroundInvoke(override.getAroundInvoke());
        }
        if (override.getAroundTimeout() != null) {
            builder.setAroundTimeout(override.getAroundTimeout());
        }
        if (override.getPostConstruct() != null) {
            builder.setPostConstruct(override.getPostConstruct());
        }
        if (override.getPreDestroy() != null) {
            builder.setPreDestroy(override.getPreDestroy());
        }
        if (override.getPrePassivate() != null) {
            builder.setPrePassivate(override.getPrePassivate());
        }
        if (override.getPostActivate() != null) {
            builder.setPrePassivate(override.getPostActivate());
        }
        return builder.build();
    }

    /**
     * Constructs a new empty builder
     *
     * @return An empty builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @param base The existing description, or null for an empty builder
     * @return A builder based on the existing description
     */
    public static Builder builder(InterceptorClassDescription base) {
        if (base == null) {
            return new Builder();
        }
        return new Builder(base);
    }


    public static class Builder {

        private MethodIdentifier aroundInvoke;
        private MethodIdentifier aroundTimeout;
        private MethodIdentifier preDestroy;
        private MethodIdentifier postConstruct;
        private MethodIdentifier prePassivate;
        private MethodIdentifier postActivate;

        Builder() {

        }

        Builder(InterceptorClassDescription existing) {
            this.aroundInvoke = existing.aroundInvoke;
            this.aroundTimeout = existing.aroundTimeout;
            this.preDestroy = existing.preDestroy;
            this.postConstruct = existing.postConstruct;
            this.prePassivate = existing.prePassivate;
            this.postActivate = existing.postActivate;
        }

        public InterceptorClassDescription build() {
            return new InterceptorClassDescription(aroundInvoke, aroundTimeout, preDestroy, postConstruct, postActivate, prePassivate);
        }


        public MethodIdentifier getAroundInvoke() {
            return aroundInvoke;
        }

        public void setAroundInvoke(final MethodIdentifier aroundInvoke) {
            this.aroundInvoke = aroundInvoke;
        }

        public MethodIdentifier getAroundTimeout() {
            return aroundTimeout;
        }

        public void setAroundTimeout(final MethodIdentifier aroundTimeout) {
            this.aroundTimeout = aroundTimeout;
        }

        public MethodIdentifier getPostConstruct() {
            return postConstruct;
        }

        public void setPostConstruct(final MethodIdentifier postConstruct) {
            this.postConstruct = postConstruct;
        }

        public MethodIdentifier getPreDestroy() {
            return preDestroy;
        }

        public void setPreDestroy(final MethodIdentifier preDestroy) {
            this.preDestroy = preDestroy;
        }

        public MethodIdentifier getPrePassivate() {
            return prePassivate;
        }

        public void setPrePassivate(final MethodIdentifier prePassivate) {
            this.prePassivate = prePassivate;
        }

        public MethodIdentifier getPostActivate() {
            return postActivate;
        }

        public void setPostActivate(final MethodIdentifier postActivate) {
            this.postActivate = postActivate;
        }
    }

    public MethodIdentifier getAroundInvoke() {
        return aroundInvoke;
    }

    public MethodIdentifier getAroundTimeout() {
        return aroundTimeout;
    }

    public MethodIdentifier getPostConstruct() {
        return postConstruct;
    }

    public MethodIdentifier getPreDestroy() {
        return preDestroy;
    }

    public MethodIdentifier getPrePassivate() {
        return prePassivate;
    }

    public MethodIdentifier getPostActivate() {
        return postActivate;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8544.java