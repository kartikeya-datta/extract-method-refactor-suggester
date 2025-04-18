error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1087.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1087.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1087.java
text:
```scala
i@@f (actionEffect == Action.ActionEffect.ADDRESS) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

package org.jboss.as.controller.access.constraint;

import org.jboss.as.controller.access.Action;

/**
 * Configuration of sensitive data. Typically {@link org.jboss.as.controller.AttributeDefinition}, {@link org.jboss.as.controller.OperationDefinition}
 * and {@link org.jboss.as.controller.ResourceDefinition} will be annotated with zero or more
 * {@link org.jboss.as.controller.access.constraint.management.SensitiveTargetAccessConstraintDefinition} containing this information. The purpose of this
 * class is to establish a default behaviour regarding sensitivity for
 * <ul>
 *      <li><b>access</b> - to be able to even be aware of the target's existence</li>
 *      <li><b>read</b> - to be able to read the target's data</li>
 *      <li><b>write</b> - to be able to write to the target</li>
 * </ul>
 * when registering a resource, attribute or operation. This default behaviour can then be tweaked.
 *
 * @author Brian Stansberry (c) 2013 Red Hat Inc.
 */
public class AbstractSensitivity {

    /** If {@code true} access (awareness) is considered sensitive by default*/
    private final boolean defaultRequiresAccessPermission;
    /** If {@code true} reading is considered sensitive by default*/
    private final boolean defaultRequiresReadPermission;
    /** If {@code true} writing is considered sensitive by default*/
    private final boolean defaultRequiresWritePermission;
    private volatile Boolean configuredRequiresAccessPermission;
    private volatile Boolean configuredRequiresReadPermission;
    private volatile Boolean configuredRequiresWritePermission;

    protected AbstractSensitivity(boolean defaultRequiresAccessPermission, boolean defaultRequiresReadPermission, boolean defaultRequiresWritePermission) {
        this.defaultRequiresAccessPermission = defaultRequiresAccessPermission;
        this.defaultRequiresReadPermission = defaultRequiresReadPermission;
        this.defaultRequiresWritePermission = defaultRequiresWritePermission;
    }

    public boolean isDefaultRequiresAccessPermission() {
        return defaultRequiresAccessPermission;
    }

    public boolean isDefaultRequiresReadPermission() {
        return defaultRequiresReadPermission;
    }

    public boolean isDefaultRequiresWritePermission() {
        return defaultRequiresWritePermission;
    }

    public boolean getRequiresAccessPermission() {
        final Boolean requires = configuredRequiresAccessPermission;
        return requires == null ? defaultRequiresAccessPermission : requires;
    }

    public Boolean getConfiguredRequiresAccessPermission() {
        return configuredRequiresAccessPermission;
    }

    public void setConfiguredRequiresAccessPermission(Boolean requiresAccessPermission) {
        this.configuredRequiresAccessPermission = requiresAccessPermission;
    }

    public boolean getRequiresReadPermission() {
        final Boolean requires = configuredRequiresReadPermission;
        return requires == null ? defaultRequiresReadPermission : requires;
    }

    public Boolean getConfiguredRequiresReadPermission() {
        return configuredRequiresReadPermission;
    }

    public void setConfiguredRequiresReadPermission(Boolean requiresReadPermission) {
        this.configuredRequiresReadPermission = requiresReadPermission;
    }

    public boolean getRequiresWritePermission() {
        final Boolean requires = configuredRequiresWritePermission;

        return requires == null ? defaultRequiresWritePermission : requires;
    }

    public Boolean getConfiguredRequiresWritePermission() {
        return configuredRequiresWritePermission;
    }

    public boolean isSensitive(Action.ActionEffect actionEffect) {
        if (actionEffect == Action.ActionEffect.ACCESS) {
            return getRequiresAccessPermission();
        } else if (actionEffect == Action.ActionEffect.READ_CONFIG || actionEffect == Action.ActionEffect.READ_RUNTIME) {
            return getRequiresReadPermission();
        } else {
            return getRequiresWritePermission();
        }
    }

    public void setConfiguredRequiresWritePermission(Boolean requiresWritePermission) {
        this.configuredRequiresWritePermission = requiresWritePermission;
    }

    protected boolean isCompatibleWith(AbstractSensitivity other) {
        return !equals(other) ||
                (defaultRequiresAccessPermission == other.defaultRequiresAccessPermission
                        && defaultRequiresReadPermission == other.defaultRequiresReadPermission
                        && defaultRequiresWritePermission == other.defaultRequiresWritePermission);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1087.java