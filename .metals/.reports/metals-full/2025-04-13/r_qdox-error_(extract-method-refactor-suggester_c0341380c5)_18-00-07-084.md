error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9455.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9455.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9455.java
text:
```scala
final R@@esource transformed = TransformationUtils.modelToResource(address, targetDefinition, result, false);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.controller.transform;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.as.controller.ControllerLogger;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.registry.LegacyResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;

/**
 * Basic transformer working on the basis of the model rather the resource. This however
 * requires the original ResourceDefinition for conversion.
 *
 * @author Emanuel Muckenhuber
 */
public abstract class AbstractResourceModelTransformer implements ResourceTransformer {

    private final ResourceDefinitionLoader loader;
    protected AbstractResourceModelTransformer(ResourceDefinitionLoader loader) {
        this.loader = loader;
    }

    /**
     * Transform the model.
     *
     * @param context the transformation context
     * @param model the model to transform
     * @return the transformed model
     */
    protected abstract ModelNode transformModel(TransformationContext context, ModelNode model);

    @Override
    public void transformResource(final ResourceTransformationContext context, final PathAddress address, final Resource resource) {
        // Transform the model recursively
        final ModelNode recursive = Resource.Tools.readModel(resource);
        final ModelNode result = transformModel(context, recursive);
        // Create the target registration based on the old resource definition
        final TransformationTarget target = context.getTarget();
        final ResourceDefinition definition = loader.load(target);
        final ManagementResourceRegistration targetDefinition = ManagementResourceRegistration.Factory.create(definition);
        final Resource transformed = TransformationUtils.modelToResource(targetDefinition, result, false);
        // Add the model recursively
        context.addTransformedRecursiveResource(PathAddress.EMPTY_ADDRESS, transformed);
    }

    public interface ResourceDefinitionLoader {

        /**
         * Load the resource definition.
         *
         * @param target the target
         * @return the resource definition
         */
         ResourceDefinition load(TransformationTarget target);

    }

    public abstract static class AbstractDefinitionLoader implements ResourceDefinitionLoader {

        /**
         * Open the stream to the resource definition model.
         *
         * @param target the transformation target
         * @return the stream
         * @throws IOException
         */
        abstract InputStream openStream(TransformationTarget target) throws IOException;

        @Override
        public ResourceDefinition load(TransformationTarget target) {
            ModelNode model = null;
            try {
                final InputStream is = openStream(target);
                try {
                    if (is == null) {
                        return null;
                    }
                    model = ModelNode.fromStream(is);
                } catch (IOException e) {
                    ControllerLogger.ROOT_LOGGER.cannotReadTargetDefinition(e);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            //
                        }
                    }
                }
            } catch (IOException e) {
                ControllerLogger.ROOT_LOGGER.cannotReadTargetDefinition(e);
            }
            if(model != null) {
                return new LegacyResourceDefinition(model);
            } else {
                return null;
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9455.java