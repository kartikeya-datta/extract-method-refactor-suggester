error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4338.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4338.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4338.java
text:
```scala
public S@@impleOperationDefinitionBuilder setParameters(AttributeDefinition... parameters) {//todo add validation for same param name

/*
 *
 *  * JBoss, Home of Professional Open Source.
 *  * Copyright 2012, Red Hat, Inc., and individual contributors
 *  * as indicated by the @author tags. See the copyright.txt file in the
 *  * distribution for a full listing of individual contributors.
 *  *
 *  * This is free software; you can redistribute it and/or modify it
 *  * under the terms of the GNU Lesser General Public License as
 *  * published by the Free Software Foundation; either version 2.1 of
 *  * the License, or (at your option) any later version.
 *  *
 *  * This software is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  * Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public
 *  * License along with this software; if not, write to the Free
 *  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 */

package org.jboss.as.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import org.jboss.as.controller.access.management.AccessConstraintDefinition;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.dmr.ModelType;

/**
 * Builder for helping construct {@link SimpleOperationDefinition}
 *
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a>
 */
public class SimpleOperationDefinitionBuilder {
    private ResourceDescriptionResolver resolver;
    private ResourceDescriptionResolver attributeResolver;
    protected String name;
    protected OperationEntry.EntryType entryType = OperationEntry.EntryType.PUBLIC;
    protected EnumSet<OperationEntry.Flag> flags = EnumSet.noneOf(OperationEntry.Flag.class);
    protected AttributeDefinition[] parameters = new AttributeDefinition[0];
    protected ModelType replyType;
    protected ModelType replyValueType;
    protected boolean replyAllowNull;
    protected DeprecationData deprecationData = null;
    protected AttributeDefinition[] replyParameters = new AttributeDefinition[0];
    protected AccessConstraintDefinition[] accessConstraints;

    public SimpleOperationDefinitionBuilder(String name, ResourceDescriptionResolver resolver) {
        this.name = name;
        this.resolver = resolver;
    }


    public SimpleOperationDefinition build() {
        if (attributeResolver == null) {
            attributeResolver = resolver;
        }
        return internalBuild(resolver, attributeResolver);
    }

    protected SimpleOperationDefinition internalBuild(ResourceDescriptionResolver resolver, ResourceDescriptionResolver attributeResolver) {
        return new SimpleOperationDefinition(name, resolver, attributeResolver, entryType, flags, replyType, replyValueType, replyAllowNull, deprecationData, replyParameters, parameters, accessConstraints);
    }

    protected static EnumSet<OperationEntry.Flag> getFlagsSet(OperationEntry.Flag... vararg) {
        EnumSet<OperationEntry.Flag> result = EnumSet.noneOf(OperationEntry.Flag.class);
        if (vararg != null && vararg.length > 0) {
            Collections.addAll(result, vararg);
        }
        return result;
    }

    public SimpleOperationDefinitionBuilder setEntryType(OperationEntry.EntryType entryType) {
        this.entryType = entryType;
        return this;
    }

    public SimpleOperationDefinitionBuilder setPrivateEntry() {
        this.entryType = OperationEntry.EntryType.PRIVATE;
        return this;
    }

    public SimpleOperationDefinitionBuilder withFlags(EnumSet<OperationEntry.Flag> flags) {
        this.flags.addAll(flags);
        return this;
    }

    public SimpleOperationDefinitionBuilder withFlags(OperationEntry.Flag... flags) {
        this.flags.addAll(getFlagsSet(flags));
        return this;
    }

    public SimpleOperationDefinitionBuilder withFlag(OperationEntry.Flag flag) {
        this.flags.add(flag);
        return this;
    }

    public SimpleOperationDefinitionBuilder setRuntimeOnly() {
        return withFlag(OperationEntry.Flag.RUNTIME_ONLY);
    }

    public SimpleOperationDefinitionBuilder setReadOnly() {
        return withFlag(OperationEntry.Flag.READ_ONLY);
    }

    public SimpleOperationDefinitionBuilder setParameters(AttributeDefinition... parameters) {
        this.parameters = parameters;
        return this;
    }

    public SimpleOperationDefinitionBuilder addParameter(AttributeDefinition parameter) {
        int i = parameters.length;
        parameters = Arrays.copyOf(parameters, i + 1);
        parameters[i] = parameter;
        return this;
    }

    public SimpleOperationDefinitionBuilder setReplyType(ModelType replyType) {
        this.replyType = replyType;
        return this;
    }

    public SimpleOperationDefinitionBuilder setReplyValueType(ModelType replyValueType) {
        this.replyValueType = replyValueType;
        return this;
    }

    public SimpleOperationDefinitionBuilder allowReturnNull() {
        this.replyAllowNull = true;
        return this;
    }

    public SimpleOperationDefinitionBuilder setDeprecated(ModelVersion since) {
        this.deprecationData = new DeprecationData(since);
        return this;
    }

    public SimpleOperationDefinitionBuilder setReplyParameters(AttributeDefinition... replyParameters) {
        this.replyParameters = replyParameters;
        return this;
    }

    public SimpleOperationDefinitionBuilder setAttributeResolver(ResourceDescriptionResolver resolver) {
        this.attributeResolver = resolver;
        return this;
    }

    public SimpleOperationDefinitionBuilder setAccessConstraints(AccessConstraintDefinition... accessConstraints) {
        this.accessConstraints = accessConstraints;
        return this;
    }

    public SimpleOperationDefinitionBuilder addAccessConstraint(final AccessConstraintDefinition accessConstraint) {
        if (accessConstraints == null) {
            accessConstraints = new AccessConstraintDefinition[] {accessConstraint};
        } else {
            accessConstraints = Arrays.copyOf(accessConstraints, accessConstraints.length + 1);
            accessConstraints[accessConstraints.length - 1] = accessConstraint;
        }
        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4338.java