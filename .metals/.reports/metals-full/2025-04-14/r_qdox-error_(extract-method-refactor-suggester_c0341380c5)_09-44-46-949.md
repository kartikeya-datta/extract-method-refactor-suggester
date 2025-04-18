error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14633.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14633.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14633.java
text:
```scala
S@@erverEnvironment serverEnviromment = Main.determineEnvironment(new String[0], systemProps, systemEnv, ServerEnvironment.LaunchType.EMBEDDED);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.client.Cancellable;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.OperationResult;
import org.jboss.as.controller.client.ResultHandler;
import org.jboss.as.controller.client.helpers.standalone.DeploymentPlan;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentManager;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentPlanResult;
import org.jboss.as.controller.parsing.Namespace;
import org.jboss.as.controller.parsing.StandaloneXml;
import org.jboss.as.controller.persistence.TransientConfigurationPersister;
import org.jboss.as.embedded.ServerStartException;
import org.jboss.as.embedded.StandaloneServer;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.server.deployment.client.ModelControllerServerDeploymentManager;
import org.jboss.dmr.ModelNode;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleLoader;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.value.Value;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * This is the counter-part of EmbeddedServerFactory which lives behind a module class loader.
 * <p>
 * ServerFactory that sets up a standalone server using modular classloading.
 * </p>
 * <p>
 * To use this class the <code>jboss.home.dir</code> system property must be set to the
 * application server home directory. By default it will use the directories
 * <code>{$jboss.home.dir}/standalone/config</code> as the <i>configuration</i> directory and
 * <code>{$jboss.home.dir}/standalone/data</code> as the <i>data</i> directory. This can be overridden
 * with the <code>${jboss.server.base.dir}</code>, <code>${jboss.server.config.dir}</code> or <code>${jboss.server.config.dir}</code>
 * system properties as for normal server startup.
 * </p>
 * <p>
 * If a clean run is wanted, you can specify <code>${jboss.embedded.root}</code> to an existing directory
 * which will copy the contents of the data and configuration directories under a temporary folder. This
 * has the effect of this run not polluting later runs of the embedded server.
 * </p>
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author Thomas.Diesler@jboss.com
 * @see org.jboss.as.embedded.EmbeddedServerFactory
 */
public class EmbeddedStandAloneServerFactory {

    public static final String JBOSS_EMBEDDED_ROOT = "jboss.embedded.root";

    private static class ModelControllerToModelControllerClientAdapter implements ModelControllerClient {
        private static class OperationResultAdapter implements Cancellable, OperationResult {
            private final org.jboss.as.controller.OperationResult delegate;

            OperationResultAdapter(org.jboss.as.controller.OperationResult delegate) {
                this.delegate = delegate;
            }

            @Override
            public boolean cancel() throws IOException {
                return delegate.getCancellable().cancel();
            }

            @Override
            public Cancellable getCancellable() {
                return this;
            }

            @Override
            public ModelNode getCompensatingOperation() {
                return delegate.getCompensatingOperation();
            }
        }

        private static class ResultHandlerAdapter implements org.jboss.as.controller.ResultHandler {
            private final ResultHandler delegate;

            ResultHandlerAdapter(ResultHandler delegate) {
                this.delegate = delegate;
            }

            @Override
            public void handleResultFragment(String[] location, ModelNode result) {
                delegate.handleResultFragment(location, result);
            }

            @Override
            public void handleResultComplete() {
                delegate.handleResultComplete();
            }

            @Override
            public void handleFailed(ModelNode failureDescription) {
                delegate.handleFailed(failureDescription);
            }

            @Override
            public void handleCancellation() {
                delegate.handleCancellation();
            }
        };

        private final ModelController delegate;

        ModelControllerToModelControllerClientAdapter(ModelController delegate) {
            this.delegate = delegate;
        }

        @Override
        public OperationResult execute(ModelNode operation, ResultHandler handler) {
            return execute(OperationBuilder.Factory.create(operation).build(), handler);
        }

        @Override
        public ModelNode execute(ModelNode operation) throws CancellationException, IOException {
            return execute(OperationBuilder.Factory.create(operation).build());
        }

        @Override
        public OperationResult execute(Operation operation, ResultHandler handler) {
            return new OperationResultAdapter(delegate.execute(operation, new ResultHandlerAdapter(handler)));
        }

        @Override
        public ModelNode execute(Operation operation) throws CancellationException, IOException {
            return delegate.execute(operation);
        }

        @Override
        public void close() throws IOException {
            // no-op
        }
    };

    private EmbeddedStandAloneServerFactory() {
    }

    public static StandaloneServer create(final File jbossHomeDir, final ModuleLoader moduleLoader, final Properties systemProps, final Map<String, String> systemEnv) {
        setupCleanDirectories(jbossHomeDir, systemProps);

        StandaloneServer standaloneServer = new StandaloneServer() {

            private ServiceContainer serviceContainer;
            private ServerDeploymentManager serverDeploymentManager;
            private Context context;
            private ModelControllerClient modelControllerClient;

            @Override
            public void deploy(File file) throws IOException, ExecutionException, InterruptedException {
                // the current deployment manager only accepts jar input stream, so hack one together
                execute(serverDeploymentManager.newDeploymentPlan()
                        .add(file.getName(), VFSUtils.createJarFileInputStream(VFS.getChild(file.toURI()))).andDeploy()
                        .build());
            }

            private ServerDeploymentPlanResult execute(DeploymentPlan deploymentPlan) throws ExecutionException, InterruptedException {
                return serverDeploymentManager.execute(deploymentPlan).get();
            }

            @Override
            public Context getContext() {
                return ifSet(context, "Server has not been started");
            }

            @Override
            public ModelControllerClient getModelControllerClient() {
                return modelControllerClient;
            }

            @Override
            public void start() throws ServerStartException {
                try {
                    // Determine the ServerEnvironment
                    ServerEnvironment serverEnviromment = Main.determineEnvironment(new String[0], systemProps, systemEnv);

                    Bootstrap bootstrap = Bootstrap.Factory.newInstance();

                    Bootstrap.Configuration configuration = new Bootstrap.Configuration();

                    // do not persist anything in embedded mode
                    final QName rootElement = new QName(Namespace.CURRENT.getUriString(), "server");
                    final StandaloneXml parser = new StandaloneXml(Module.getBootModuleLoader());
                    configuration.setConfigurationPersister(new TransientConfigurationPersister(serverEnviromment.getServerConfigurationFile(), rootElement, parser, parser));

                    configuration.setServerEnvironment(serverEnviromment);

                    configuration.setModuleLoader(moduleLoader);

                    Future<ServiceContainer> future = bootstrap.startup(configuration, Collections.<ServiceActivator>emptyList());

                    serviceContainer = future.get();

                    final Value<ServerController> serverControllerService = (Value<ServerController>) serviceContainer.getRequiredService(Services.JBOSS_SERVER_CONTROLLER);
                    final ServerController controller = serverControllerService.getValue();
                    serverDeploymentManager = new ModelControllerServerDeploymentManager(controller);
                    modelControllerClient = new ModelControllerToModelControllerClientAdapter(controller);

                    context = new InitialContext();
                } catch (RuntimeException rte) {
                    throw rte;
                } catch (Exception ex) {
                    throw new ServerStartException(ex);
                }
            }

            @Override
            public void stop() {
                if (context != null) {
                    try {
                        context.close();

                        context = null;
                    } catch (NamingException e) {
                        // TODO: use logging?
                        e.printStackTrace();
                    }
                }
                serverDeploymentManager = null;
                if (serviceContainer != null) {
                    try {
                        serviceContainer.shutdown();

                        serviceContainer.awaitTermination();
                    } catch (RuntimeException rte) {
                        throw rte;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void undeploy(File file) throws ExecutionException, InterruptedException {
                execute(serverDeploymentManager.newDeploymentPlan()
                        .undeploy(file.getName()).andRemoveUndeployed()
                        .build());
            }
        };
        return standaloneServer;
    }

    private static <T> T ifSet(T value, String message) {
        if (value == null)
            throw new IllegalStateException(message);
        return value;
    }

    public static void setupCleanDirectories(Properties props) {
        File jbossHomeDir = new File(props.getProperty(ServerEnvironment.HOME_DIR));
        setupCleanDirectories(jbossHomeDir, props);
    }

    static void setupCleanDirectories(File jbossHomeDir, Properties props) {
        File tempRoot = getTempRoot(props);
        if (tempRoot == null) {
            return;
        }

        File originalConfigDir = getFileUnderAsRoot(jbossHomeDir, props, ServerEnvironment.SERVER_CONFIG_DIR, "configuration", true);
        File originalDataDir = getFileUnderAsRoot(jbossHomeDir, props, ServerEnvironment.SERVER_DATA_DIR, "data", false);

        File configDir = new File(tempRoot, "config");
        configDir.mkdir();
        File dataDir = new File(tempRoot, "data");
        dataDir.mkdir();
        // For jboss.server.deployment.scanner.default
        File deploymentsDir = new File(tempRoot, "deployments");
        deploymentsDir.mkdir();

        copyDirectory(originalConfigDir, configDir);
        if (originalDataDir.exists()) {
            copyDirectory(originalDataDir, dataDir);
        }

        props.put(ServerEnvironment.SERVER_BASE_DIR, tempRoot.getAbsolutePath());
        props.put(ServerEnvironment.SERVER_CONFIG_DIR, configDir.getAbsolutePath());
        props.put(ServerEnvironment.SERVER_DATA_DIR, dataDir.getAbsolutePath());

    }

    private static File getFileUnderAsRoot(File jbossHomeDir, Properties props, String propName, String relativeLocation, boolean mustExist) {
        String prop = props.getProperty(propName, null);
        if (prop == null) {
            prop = props.getProperty(ServerEnvironment.SERVER_BASE_DIR, null);
            if (prop == null) {
                File dir = new File(jbossHomeDir, "standalone/" + relativeLocation);
                if (mustExist && (!dir.exists() || !dir.isDirectory())) {
                    throw new IllegalArgumentException("No directory called 'standalone/' " + relativeLocation + " under " + jbossHomeDir.getAbsolutePath());
                }
                return dir;
            } else {
                File server = new File(prop);
                validateDirectory(ServerEnvironment.SERVER_BASE_DIR, server);
                return new File(server, relativeLocation);
            }
        } else {
            File dir = new File(prop);
            validateDirectory(ServerEnvironment.SERVER_CONFIG_DIR, dir);
            return dir;
        }

    }

    private static File getTempRoot(Properties props) {
        String tempRoot = props.getProperty(JBOSS_EMBEDDED_ROOT, null);
        if (tempRoot == null) {
            return null;
        }

        File root = new File(tempRoot);
        if (!root.exists()) {
            //Attempt to try to create the directory, in case something like target/embedded was specified
            root.mkdirs();
        }
        validateDirectory("jboss.test.clean.root", root);
        root = new File(root, "configs");
        root.mkdir();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        root = new File(root, format.format(new Date()));
        root.mkdir();
        return root;
    }

    private static void validateDirectory(String property, File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("-D" + property + "=" + file.getAbsolutePath() + " does not exist");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("-D" + property + "=" + file.getAbsolutePath() + " is not a directory");
        }
    }

    private static void copyDirectory(File src, File dest) {
        for (String current : src.list()) {
            final File srcFile = new File(src, current);
            final File destFile = new File(dest, current);

            if (srcFile.isDirectory()) {
                destFile.mkdir();
                copyDirectory(srcFile, destFile);
            } else {
                try {
                    final InputStream in = new BufferedInputStream(new FileInputStream(srcFile));
                    final OutputStream out = new BufferedOutputStream(new FileOutputStream(destFile));

                    try {
                        int i;
                        while ((i = in.read()) != -1) {
                            out.write(i);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Error copying " + srcFile.getAbsolutePath() + " to " + destFile.getAbsolutePath(), e);
                    } finally {
                        StreamUtils.safeClose(in);
                        StreamUtils.safeClose(out);
                    }

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14633.java