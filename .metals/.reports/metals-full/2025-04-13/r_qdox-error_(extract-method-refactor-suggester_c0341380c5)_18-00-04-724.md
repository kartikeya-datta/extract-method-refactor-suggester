error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7403.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7403.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7403.java
text:
```scala
public static final S@@tring NAME = "patching";

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

package org.jboss.as.patching;

import java.io.File;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PrimitiveListAttributeDefinition;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleOperationDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author Emanuel Muckenhuber
 */
public class PatchResourceDefinition extends SimpleResourceDefinition {

    static final String NAME = "patching";
    static final String RESOURCE_NAME = PatchResourceDefinition.class.getPackage().getName() + ".LocalDescriptions";
    static final PathElement PATH = PathElement.pathElement(ModelDescriptionConstants.CORE_SERVICE, NAME);

    private static ResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
        return new StandardResourceDescriptionResolver(keyPrefix, RESOURCE_NAME, PatchResourceDefinition.class.getClassLoader(), true, false);
    }

    static final AttributeDefinition VERSION = SimpleAttributeDefinitionBuilder.create("version", ModelType.STRING)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition CUMULATIVE = SimpleAttributeDefinitionBuilder.create("cumulative", ModelType.STRING)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition PATCHES = PrimitiveListAttributeDefinition.Builder.of("patches", ModelType.STRING)
            .setStorageRuntime()
            .build();
    static final AttributeDefinition MODULE_PATH = PrimitiveListAttributeDefinition.Builder.of("module-path", ModelType.STRING)
            .setStorageRuntime()
            .build();

    // Patch operation
    static final AttributeDefinition INPUT_STREAM_IDX_DEF = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.INPUT_STREAM_INDEX, ModelType.INT)
            .setDefaultValue(new ModelNode(0))
            .setAllowNull(true)
            .build();
    static final AttributeDefinition OVERRIDE_MODULES = SimpleAttributeDefinitionBuilder.create(Constants.OVERRIDE_MODULES, ModelType.BOOLEAN)
            .setDefaultValue(new ModelNode(false))
            .setAllowNull(true)
            .build();
    static final AttributeDefinition OVERRIDE_ALL = SimpleAttributeDefinitionBuilder.create(Constants.OVERRIDE_ALL, ModelType.BOOLEAN)
            .setDefaultValue(new ModelNode(false))
            .setAllowNull(true)
            .build();
    static final AttributeDefinition OVERRIDES = PrimitiveListAttributeDefinition.Builder.of(Constants.OVERRIDES, ModelType.STRING)
            .setAllowNull(true)
            .build();
    static final AttributeDefinition PRESERVE = PrimitiveListAttributeDefinition.Builder.of(Constants.PRESERVE, ModelType.STRING)
            .setAllowNull(true)
            .build();

    static final AttributeDefinition RESTORE_CONFIGURATION = SimpleAttributeDefinitionBuilder.create("restore-configuration", ModelType.BOOLEAN)
            .setDefaultValue(new ModelNode(true))
            .setAllowNull(true)
            .build();

    static final OperationDefinition PATCH = new SimpleOperationDefinitionBuilder("patch", getResourceDescriptionResolver(PatchResourceDefinition.NAME))
            .addParameter(INPUT_STREAM_IDX_DEF)
            .addParameter(OVERRIDE_ALL)
            .addParameter(OVERRIDE_MODULES)
            .addParameter(OVERRIDES)
            .addParameter(PRESERVE)
            .build();

    static final AttributeDefinition PATCH_ID = SimpleAttributeDefinitionBuilder.create("patch-id", ModelType.STRING)
            .build();
    static final OperationDefinition ROLLBACK = new SimpleOperationDefinitionBuilder("rollback", getResourceDescriptionResolver(PatchResourceDefinition.NAME))
            .addParameter(PATCH_ID)
            .addParameter(OVERRIDE_ALL)
            .addParameter(OVERRIDE_MODULES)
            .addParameter(OVERRIDES)
            .addParameter(PRESERVE)
            .addParameter(RESTORE_CONFIGURATION)
            .build();

    static final OperationDefinition SHOW_HISTORY = new SimpleOperationDefinitionBuilder("show-history", getResourceDescriptionResolver(PatchResourceDefinition.NAME))
            .build();

    public static final ResourceDefinition INSTANCE = new PatchResourceDefinition();

    private PatchResourceDefinition() {
        super(PATH, getResourceDescriptionResolver(NAME));
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration registry) {
        super.registerAttributes(registry);

        registry.registerReadOnlyAttribute(VERSION, new PatchAttributeReadHandler() {
            @Override
            void handle(ModelNode result, PatchInfo info) {
                result.set(info.getVersion());
            }
        });
        registry.registerReadOnlyAttribute(CUMULATIVE, new PatchAttributeReadHandler() {
            @Override
            void handle(ModelNode result, PatchInfo info) {
                result.set(info.getCumulativeID());
            }
        });
        registry.registerReadOnlyAttribute(PATCHES, new PatchAttributeReadHandler() {
            @Override
            void handle(ModelNode result, PatchInfo info) {
                result.setEmptyList();
                for(final String id : info.getPatchIDs()) {
                    result.add(id);
                }
            }
        });
        registry.registerReadOnlyAttribute(MODULE_PATH, new PatchAttributeReadHandler() {
            @Override
            void handle(ModelNode result, PatchInfo info) {
                result.setEmptyList();
                for(final File id : info.getModulePath()) {
                    result.add(id.getAbsolutePath());
                }
            }
        });
    }

    @Override
    public void registerOperations(ManagementResourceRegistration registry) {
        super.registerOperations(registry);

        registry.registerOperationHandler(ROLLBACK, LocalPatchRollbackHandler.INSTANCE);
        registry.registerOperationHandler(PATCH, LocalPatchOperationStepHandler.INSTANCE);
        registry.registerOperationHandler(SHOW_HISTORY, LocalShowHistoryHandler.INSTANCE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7403.java