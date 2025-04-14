error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5114.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5114.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5114.java
text:
```scala
t@@hrow new IllegalStateException("Invalid bundles directory: " + bundlesDir);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.arquillian.container.embedded;

import java.io.File;
import java.util.logging.LogManager;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.as.arquillian.container.CommonDeployableContainer;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.log.JDKModuleLogger;

/**
 * {@link DeployableContainer} implementation to bootstrap JBoss Logging (installing the LogManager if possible), use the JBoss
 * Modules modular ClassLoading Environment to create a new server instance, and handle lifecycle of the Application Server in
 * the currently-running environment.
 *
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 * @author <a href="mailto:mmatloka@gmail.com">Michal Matloka</a>
 */
public final class EmbeddedDeployableContainer extends CommonDeployableContainer<EmbeddedContainerConfiguration> {

    private static final String MODULE_ID_LOGMANAGER = "org.jboss.logmanager";
    private static final String MODULE_ID_VFS = "org.jboss.vfs";
    private static final String SYSPROP_KEY_CLASS_PATH = "java.class.path";
    private static final String SYSPROP_KEY_MODULE_PATH = "module.path";
    private static final String SYSPROP_KEY_BUNDLE_PATH = "jboss.bundles.dir";
    private static final String SYSPROP_KEY_LOGMANAGER = "java.util.logging.manager";
    private static final String SYSPROP_KEY_JBOSS_HOME_DIR = "jboss.home.dir";
    private static final String SYSPROP_VALUE_JBOSS_LOGMANAGER = "org.jboss.logmanager.LogManager";

    /**
     * Hook to the server; used in start/stop, created by setup
     */
    private StandaloneServerIndirection server;

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.as.arquillian.container.CommonDeployableContainer#setup(org.jboss.as.arquillian.container.CommonContainerConfiguration)
     */
    @Override
    public void setup(final EmbeddedContainerConfiguration config) {
        super.setup(config);

        final ModuleLoader moduleLoader = setupModuleLoader(config.getModulePath());

        setupBundlePath(config.getBundlePath());

        setupVfsModule(moduleLoader);

        setupLoggingSystem(moduleLoader);

        setupServer(moduleLoader, config.getJbossHome());
    }

    /**
     * Creates/obtains the boot {@link ModuleLoader} for the specified, required modules path
     *
     * @param modulePath
     * @return module loader
     */
    private ModuleLoader setupModuleLoader(final String modulePath) {
        if (modulePath == null || modulePath.isEmpty()) {
            throw new IllegalStateException("Module path must be defined in the configuration");
        }

        final File modulesDir = new File(modulePath);
        if (!modulesDir.isDirectory()) {
            throw new IllegalStateException("Invalid modules directory: " + modulesDir);
        }

        final String classPath = SecurityActions.getSystemProperty(SYSPROP_KEY_CLASS_PATH);
        try {
            // Set up sysprop env
            SecurityActions.clearSystemProperty(SYSPROP_KEY_CLASS_PATH);
            SecurityActions.setSystemProperty(SYSPROP_KEY_MODULE_PATH, modulesDir.getAbsolutePath());

            // Get the module loader
            final ModuleLoader moduleLoader = Module.getBootModuleLoader();
            return moduleLoader;
        } finally {
            // Return to previous state for classpath prop
            SecurityActions.setSystemProperty(SYSPROP_KEY_CLASS_PATH, classPath);
        }
    }

    /**
     * Set bundle path
     *
     * @param bundlePath
     */
    private void setupBundlePath(final String bundlePath) {
        if (bundlePath == null || bundlePath.isEmpty()) {
            throw new IllegalStateException("Bundle path must be defined in the configuration");
        }

        final File bundlesDir = new File(bundlePath);
        if (!bundlesDir.isDirectory()) {
            throw new IllegalStateException("Invalid modules directory: " + bundlesDir);
        }
        SecurityActions.setSystemProperty(SYSPROP_KEY_BUNDLE_PATH, bundlePath);
    }

    /**
     * Register URL Stream Handlers for the VFS Module
     *
     * @param moduleLoader
     */
    private void setupVfsModule(final ModuleLoader moduleLoader) {
        final ModuleIdentifier vfsModuleID = ModuleIdentifier.create(MODULE_ID_VFS);
        final Module vfsModule;

        try {
            vfsModule = moduleLoader.loadModule(vfsModuleID);
        } catch (final ModuleLoadException mle) {
            throw new RuntimeException("Could not load VFS module", mle);
        }

        Module.registerURLStreamHandlerFactoryModule(vfsModule);
    }

    /**
     * Initialize the Logging system
     *
     * @param moduleLoader
     */
    private void setupLoggingSystem(ModuleLoader moduleLoader) {
        final ModuleIdentifier logModuleId = ModuleIdentifier.create(MODULE_ID_LOGMANAGER);
        final Module logModule;
        try {
            logModule = moduleLoader.loadModule(logModuleId);
        } catch (final ModuleLoadException mle) {
            throw new RuntimeException("Could not load logging module", mle);
        }

        final ModuleClassLoader logModuleClassLoader = logModule.getClassLoader();
        final ClassLoader tccl = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(logModuleClassLoader);
            SecurityActions.setSystemProperty(SYSPROP_KEY_LOGMANAGER, SYSPROP_VALUE_JBOSS_LOGMANAGER);

            final Class<?> actualLogManagerClass = LogManager.getLogManager().getClass();
            if (actualLogManagerClass == LogManager.class) {
                System.err
                        .println("Could not load JBoss LogManager; the LogManager or Logging subsystem has likely been accessed prior to this initialization.");
            } else {
                Module.setModuleLogger(new JDKModuleLogger());
            }
        } finally {
            // Reset TCCL
            SecurityActions.setContextClassLoader(tccl);
        }
    }

    /**
     * Create and set the server
     *
     * @param moduleLoader
     * @param jbossHomePath
     */
    private void setupServer(final ModuleLoader moduleLoader, final String jbossHomePath) {
        if (jbossHomePath == null || jbossHomePath.isEmpty()) {
            throw new IllegalStateException("JBoss home path must be defined");
        }

        final File jbossHome = new File(jbossHomePath);
        if (!jbossHome.isDirectory()) {
            throw new IllegalStateException("Invalid jboss home directory: " + jbossHome);
        }

        // Embedded Server wants this, too. Seems redundant, but supply it.
        SecurityActions.setSystemProperty(SYSPROP_KEY_JBOSS_HOME_DIR, jbossHome.getAbsolutePath());
        this.server = new StandaloneServerIndirection(moduleLoader, jbossHome);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.arquillian.container.spi.client.container.DeployableContainer#getConfigurationClass()
     */
    @Override
    public Class<EmbeddedContainerConfiguration> getConfigurationClass() {
        return EmbeddedContainerConfiguration.class;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.as.arquillian.container.CommonDeployableContainer#startInternal()
     */
    @Override
    protected void startInternal() throws LifecycleException {
        // The comment here in AS4/5 used to be "Go get a cup of coffee", but this is FAST now. ;)
        server.start();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.as.arquillian.container.CommonDeployableContainer#stopInternal()
     */
    @Override
    protected void stopInternal() throws LifecycleException {
        this.server.stop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5114.java