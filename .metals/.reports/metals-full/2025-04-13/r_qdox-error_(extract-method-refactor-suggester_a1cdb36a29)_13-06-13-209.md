error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10735.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10735.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10735.java
text:
```scala
public abstract D@@iscardTransformationDescriptionBuilder discardChildResource(PathElement pathElement);

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

package org.jboss.as.controller.transform.description;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.transform.ResourceTransformer;

import java.util.Collection;

/**
 * @author Emanuel Muckenhuber
 */
public abstract class ResourceTransformationDescriptionBuilder extends TransformationDescriptionBuilder {

    protected ResourceTransformationDescriptionBuilder(PathElement pathElement) {
        super(pathElement);
    }

    /**
     * Reject expressions as attribute values.
     *
     * @param attributeNames the attributes names
     * @return the builder for the current resource
     */
    public abstract ResourceTransformationDescriptionBuilder rejectExpressions(Collection<String> attributeNames);

    /**
     * Reject expressions as attribute values.
     *
     * @param attributeNames the attribute names
     * @return the builder for the current resource
     */
    public abstract ResourceTransformationDescriptionBuilder rejectExpressions(String... attributeNames);

    /**
     * Reject expressions as attributes values.
     *
     * @param defintions the attribute definitions
     * @return the builder for the current resource
     */
    public abstract ResourceTransformationDescriptionBuilder rejectExpressions(AttributeDefinition... defintions);

    /**
     * Set a custom resource transformer. This transformer is going to be called after all attribute transformations happened
     * and needs to take care of adding the currently transformed resource properly. If not specified, the resource will be
     * added according to other rules defined by this builder.
     *
     * @param resourceTransformer the resource transformer
     * @return the builder for the current resource
     */
    public abstract ResourceTransformationDescriptionBuilder setResourceTransformer(ResourceTransformer resourceTransformer);

    /**
     * Add a custom transformation step. This will be called for model as well as operation transformation.
     *
     * @param transformer the model transformer
     */
    public abstract ResourceTransformationDescriptionBuilder addCustomTransformation(ModelTransformer transformer);

    /**
     * Add a child resource.
     *
     * @param pathElement the path element
     * @return the builder for the child resource
     */
    public abstract ResourceTransformationDescriptionBuilder addChildResource(PathElement pathElement);

    /**
     * Add a child resource.
     *
     * @param definition the resource definition
     * @return the builder for the child resource
     */
    public abstract ResourceTransformationDescriptionBuilder addChildResource(ResourceDefinition definition);

    /**
     * Recursively discards all child resources and its operations.
     *
     * @param pathElement the path element
     * @return the builder for the child resource
     */
    public abstract TransformationDescriptionBuilder discardChildResource(PathElement pathElement);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10735.java