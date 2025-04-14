error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3922.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3922.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3922.java
text:
```scala
C@@oncreteAttributeTransformationDescriptionBuilder getAttributeBuilder();

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

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.PathAddressTransformer;
import org.jboss.as.controller.transform.ResourceTransformer;

/**
 * Resource specific transformation description builder. This is a convenience API over the
 * {@linkplain org.jboss.as.controller.transform.TransformersSubRegistration} and can be used to add common policies
 * when registering resource or operation transformers.
 *
 * @author Emanuel Muckenhuber
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public interface ResourceTransformationDescriptionBuilder extends TransformationDescriptionBuilder {

    /**
     * Get a builder to define custom attribute transformation rules.
     *
     * @return the attribute transformation builder
     */
    AttributeTransformationDescriptionBuilder getAttributeBuilder();

    /**
     * Add an operation transformation entry for a given operation. By default all operations inherit the attribute
     * transformation rules from this transformation description. This behavior can be overridden for a given operation.
     *
     * @param operationName the operation name
     * @return the operation transformation builder
     */
    OperationTransformationOverrideBuilder addOperationTransformationOverride(String operationName);

    /**
     * Add an operation transformer. Unlike the the {@linkplain #addOperationTransformationOverride(String)} this will
     * use the {@linkplain OperationTransformer} without adding any additional capabilities.
     *
     * @param operationName the operation name
     * @param operationTransformer the operation transformer
     * @return the builder for the current instance
     */
    ResourceTransformationDescriptionBuilder addRawOperationTransformationOverride(String operationName, OperationTransformer operationTransformer);

    /**
     * Set a (optional) custom resource transformer. This transformer is going to be called after all attribute transformations
     * happened and needs to take care of adding the currently transformed resource properly. If not specified, the resource
     * will be added according to other rules defined by this builder.
     *
     * @param resourceTransformer the resource transformer
     * @return the builder for the current resource
     */
    ResourceTransformationDescriptionBuilder setCustomResourceTransformer(ResourceTransformer resourceTransformer);

    /**
     * Add a child resource to this builder. This is going to register the child automatically at the
     * {@linkplain org.jboss.as.controller.transform.TransformersSubRegistration} when registering the transformation
     * description created by this builder.
     *
     * @param pathElement the path element
     * @return the builder for the child resource
     */
    ResourceTransformationDescriptionBuilder addChildResource(PathElement pathElement);

    /**
     * Add a child resource to this builder. This is going to register the child automatically at the
     * {@linkplain org.jboss.as.controller.transform.TransformersSubRegistration} when registering the transformation
     * description created by this builder.
     *
     * @param definition the resource definition
     * @return the builder for the child resource
     */
    ResourceTransformationDescriptionBuilder addChildResource(ResourceDefinition definition);

    /**
     * Recursively discards all child resources and its operations.
     *
     * @param pathElement the path element
     * @return the builder for the child resource
     */
    DiscardTransformationDescriptionBuilder discardChildResource(PathElement pathElement);

    /**
     * Add a child resource, where all operations will get redirected to the legacy address.
     *
     * @param current the current path element
     * @param legacy the legacy path element
     * @return the builder for the child resource
     */
    ResourceTransformationDescriptionBuilder addChildRedirection(PathElement current, PathElement legacy);

    /**
     * Add a child resource, where all operation will get redirected to a different address defined by
     * the path transformation.
     *
     * @param pathElement the path element of the child
     * @param pathAddressTransformer the path transformation
     * @return the builder for the child resource
     */
    ResourceTransformationDescriptionBuilder addChildRedirection(PathElement pathElement, PathAddressTransformer pathAddressTransformer);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3922.java