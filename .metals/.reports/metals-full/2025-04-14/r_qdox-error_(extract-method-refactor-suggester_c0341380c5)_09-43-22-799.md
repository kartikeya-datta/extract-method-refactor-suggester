error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5006.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5006.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5006.java
text:
```scala
private S@@erverEnvironmentResourceDescription(final ServerEnvironment environment) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.server;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.server.controller.descriptions.ServerDescriptionConstants.SERVER_ENVIRONMENT;

import java.io.File;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.persistence.ConfigurationFile;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.server.controller.descriptions.ServerDescriptions;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * A resource description that describes the server environment.
 * <p/>
 * Date: 17.11.2011
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class ServerEnvironmentResourceDescription extends SimpleResourceDefinition {
    public static final PathElement RESOURCE_PATH = PathElement.pathElement(ModelDescriptionConstants.CORE_SERVICE, SERVER_ENVIRONMENT);

    public static final AttributeDefinition BASE_DIR = SimpleAttributeDefinitionBuilder.create("base-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition CONFIG_DIR = SimpleAttributeDefinitionBuilder.create("config-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition CONFIG_FILE = SimpleAttributeDefinitionBuilder.create("config-file", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition CONTENT_DIR = SimpleAttributeDefinitionBuilder.create("content-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition DATA_DIR = SimpleAttributeDefinitionBuilder.create("data-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition DEPLOY_DIR = SimpleAttributeDefinitionBuilder.create("deploy-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition EXT_DIRS = SimpleAttributeDefinitionBuilder.create("ext-dirs", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition HOME_DIR = SimpleAttributeDefinitionBuilder.create("home-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition HOST_NAME = SimpleAttributeDefinitionBuilder.create("host-name", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition INITIAL_RUNNING_MODE = SimpleAttributeDefinitionBuilder.create("initial-running-mode", ModelType.STRING)
            .setValidator(new EnumValidator<RunningMode>(RunningMode.class, false, false)).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition LAUNCH_TYPE = SimpleAttributeDefinitionBuilder.create("launch-type", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition LOG_DIR = SimpleAttributeDefinitionBuilder.create("log-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition MODULES_DIR = SimpleAttributeDefinitionBuilder.create("modules-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition NODE_NAME = SimpleAttributeDefinitionBuilder.create("node-name", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition QUALIFIED_HOST_NAME = SimpleAttributeDefinitionBuilder.create("qualified-host-name", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition SERVER_NAME = SimpleAttributeDefinitionBuilder.create("server-name", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();
    public static final AttributeDefinition TEMP_DIR = SimpleAttributeDefinitionBuilder.create("temp-dir", ModelType.STRING).setFlags(AttributeAccess.Flag.STORAGE_RUNTIME).build();

    public static final AttributeDefinition[] SERVER_ENV_ATTRIBUTES = {BASE_DIR, CONFIG_DIR, CONFIG_FILE, CONTENT_DIR, DATA_DIR,
            DEPLOY_DIR, EXT_DIRS, HOME_DIR, HOST_NAME, INITIAL_RUNNING_MODE, LAUNCH_TYPE, LOG_DIR, MODULES_DIR, NODE_NAME,
            QUALIFIED_HOST_NAME, SERVER_NAME, TEMP_DIR};

    private final ServerEnvironmentReadHandler osh;

    /**
     * Creates a new description provider to describe the server environment.
     *
     * @param environment the environment the resource is based on.
     */
    public ServerEnvironmentResourceDescription(final ServerEnvironment environment) {
        super(RESOURCE_PATH, ServerDescriptions.getResourceDescriptionResolver("server.env"));
        osh = new ServerEnvironmentReadHandler(environment);
    }

    /**
     * A factory method for creating a new server environment resource description.
     *
     * @param environment the environment the resource is based on.
     *
     * @return a new server environment resource description.
     */
    public static ServerEnvironmentResourceDescription of(final ServerEnvironment environment) {
        return new ServerEnvironmentResourceDescription(environment);
    }

    @Override
    public void registerAttributes(final ManagementResourceRegistration resourceRegistration) {
        for (AttributeDefinition attribute : SERVER_ENV_ATTRIBUTES) {
            resourceRegistration.registerReadOnlyAttribute(attribute, osh);
        }
    }


    /**
     * Date: 17.11.2011
     *
     * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
     */
    private static class ServerEnvironmentReadHandler implements OperationStepHandler {
        private final ServerEnvironment environment;

        public ServerEnvironmentReadHandler(final ServerEnvironment environment) {
            this.environment = environment;
        }

        @Override
        public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {
            final ModelNode result = context.getResult();
            final String name = operation.require(NAME).asString();
            if (equals(name, BASE_DIR)) {
                set(result, environment.getServerBaseDir());
            }
            if (equals(name, CONFIG_DIR)) {
                set(result, environment.getServerConfigurationDir());
            }
            if (equals(name, CONFIG_FILE)) {
                set(result, environment.getServerConfigurationFile());
            }
            if (equals(name, DATA_DIR)) {
                set(result, environment.getServerDataDir());
            }
            if (equals(name, CONTENT_DIR)) {
                set(result, environment.getServerContentDir());
            }
            if (equals(name, DEPLOY_DIR)) {
                set(result, environment.getServerContentDir());
            }
            if (equals(name, EXT_DIRS)) {
                set(result, environment.getJavaExtDirs());
            }
            if (equals(name, HOME_DIR)) {
                set(result, environment.getHomeDir());
            }
            if (equals(name, HOST_NAME)) {
                set(result, environment.getHostName());
            }
            if (equals(name, LAUNCH_TYPE)) {
                set(result, environment.getLaunchType().name());
            }
            if (equals(name, INITIAL_RUNNING_MODE)) {
                set(result, environment.getInitialRunningMode().name());
            }
            if (equals(name, LOG_DIR)) {
                set(result, environment.getServerLogDir());
            }
            if (equals(name, MODULES_DIR)) {
                @SuppressWarnings("deprecation")
                File modules = environment.getModulesDir();
                set(result, modules);
            }
            if (equals(name, NODE_NAME)) {
                set(result, environment.getNodeName());
            }
            if (equals(name, QUALIFIED_HOST_NAME)) {
                set(result, environment.getQualifiedHostName());
            }
            if (equals(name, SERVER_NAME)) {
                set(result, environment.getServerName());
            }
            if (equals(name, TEMP_DIR)) {
                set(result, environment.getServerTempDir());
            }
            context.completeStep();
        }

        private void set(final ModelNode node, final String value) {
            if (value != null) {
                node.set(value);
            }
        }

        private void set(final ModelNode node, final File value) {
            if (value != null) {
                node.set(value.getAbsolutePath());
            }
        }

        private void set(final ModelNode node, final File[] value) {
            if (value != null) {
                for (File file : value) {
                    node.add(file.getAbsolutePath());
                }
            }
        }

        private void set(final ModelNode node, final ConfigurationFile value) {
            if (value != null) {
                set(node, value.getBootFile());
            }
        }

        private boolean equals(final String name, final AttributeDefinition attribute) {
            return name.equals(attribute.getName());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5006.java