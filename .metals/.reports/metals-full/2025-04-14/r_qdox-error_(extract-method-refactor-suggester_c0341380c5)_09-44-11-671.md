error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12762.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12762.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12762.java
text:
```scala
public static final P@@athElement PATH_ADDRESS = PathElement.pathElement(ModelDescriptionConstants.PATH);

/*
* JBoss, Home of Professional Open Source.
* Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.controller.services.path;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReadResourceNameOperationStepHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.descriptions.common.ControllerResolver;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public abstract class PathResourceDefinition extends SimpleResourceDefinition {

    private static final String SPECIFIED_PATH_RESOURCE_PREFIX = "specified_path";
    private static final String NAMED_PATH_RESOURCE_PREFIX = "named_path";

    static final PathElement PATH_ADDRESS = PathElement.pathElement(ModelDescriptionConstants.PATH);

    static final SimpleAttributeDefinition NAME =
            SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.NAME, ModelType.STRING, false)
                .setValidator(new StringLengthValidator(1, false))
                .setResourceOnly()
                .build();

    static final SimpleAttributeDefinition PATH_SPECIFIED =
            SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.PATH, ModelType.STRING, false)
                .setAllowExpression(true)
                .setValidator(new StringLengthValidator(1, false))
                .build();

    static final SimpleAttributeDefinition PATH_NAMED =
            SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.PATH, ModelType.STRING, true)
                .setAllowExpression(true)
                .setValidator(new StringLengthValidator(1, true))
                .build();

    static final SimpleAttributeDefinition READ_ONLY =
            SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.READ_ONLY, ModelType.BOOLEAN, true)
                .setDefaultValue(new ModelNode(false))
                .setStorageRuntime()
                .build();

    /**
     * A path attribute definition
     */
    public static final SimpleAttributeDefinition PATH = SimpleAttributeDefinitionBuilder.create(PATH_SPECIFIED).build();

    /**
     * A relative-to attribute definition
     */
    public static final SimpleAttributeDefinition RELATIVE_TO =
            SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.RELATIVE_TO, ModelType.STRING, true)
                    .setValidator(new StringLengthValidator(1, true))
                    .build();


    protected final PathManagerService pathManager;
    private final boolean specified;
    private final boolean services;

    private PathResourceDefinition(PathManagerService pathManager, ResourceDescriptionResolver resolver, PathAddHandler addHandler, PathRemoveHandler removeHandler, boolean specified, boolean services) {
        super(PATH_ADDRESS, resolver, addHandler, removeHandler);
        this.pathManager = pathManager;
        this.specified = specified;
        this.services = services;
    }

    public static PathResourceDefinition createSpecified(PathManagerService pathManager) {
        return new SpecifiedPathResourceDefinition(pathManager);
    }

    public static PathResourceDefinition createNamed(PathManagerService pathManager) {
        return new NamedPathResourceDefinition(pathManager);
    }

    public static PathResourceDefinition createSpecifiedNoServices(PathManagerService pathManager) {
        return new SpecifiedNoServicesPathResourceDefinition(pathManager);
    }

    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        resourceRegistration.registerReadOnlyAttribute(NAME, ReadResourceNameOperationStepHandler.INSTANCE);
        resourceRegistration.registerReadWriteAttribute(RELATIVE_TO, null, new PathWriteAttributeHandler(pathManager, RELATIVE_TO, services));
        SimpleAttributeDefinition pathAttr = specified ? PATH_SPECIFIED : PATH_NAMED;
        resourceRegistration.registerReadWriteAttribute(pathAttr, null, new PathWriteAttributeHandler(pathManager, pathAttr, services));
        resourceRegistration.registerReadOnlyAttribute(READ_ONLY, null);
    }

    private static class SpecifiedPathResourceDefinition extends PathResourceDefinition {
        SpecifiedPathResourceDefinition(PathManagerService pathManager){
            super(pathManager,
                    ControllerResolver.getResolver(SPECIFIED_PATH_RESOURCE_PREFIX),
                    PathAddHandler.createSpecifiedInstance(pathManager),
                    PathRemoveHandler.createSpecifiedInstance(pathManager),
                    true,
                    true);
        }
    }

    private static class NamedPathResourceDefinition extends PathResourceDefinition {
        NamedPathResourceDefinition(PathManagerService pathManager){
            super(pathManager,
                    ControllerResolver.getResolver(NAMED_PATH_RESOURCE_PREFIX),
                    PathAddHandler.createNamedInstance(pathManager),
                    PathRemoveHandler.createNamedInstance(pathManager),
                    false,
                    false);
        }
    }

    private static class SpecifiedNoServicesPathResourceDefinition extends PathResourceDefinition {
        SpecifiedNoServicesPathResourceDefinition(PathManagerService pathManager){
            super(pathManager,
                    ControllerResolver.getResolver(SPECIFIED_PATH_RESOURCE_PREFIX),
                    PathAddHandler.createSpecifiedNoServicesInstance(pathManager),
                    PathRemoveHandler.createSpecifiedNoServicesInstance(pathManager),
                    true,
                    false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12762.java