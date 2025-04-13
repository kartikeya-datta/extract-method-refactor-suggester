error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2672.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2672.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2672.java
text:
```scala
c@@onfigurationPersister = new BackupXmlConfigurationPersister(serverEnvironment.getServerConfigurationFile(), rootElement, parser, parser);

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

import java.io.File;
import java.util.List;

import javax.xml.namespace.QName;

import org.jboss.as.controller.parsing.Namespace;
import org.jboss.as.controller.parsing.StandaloneXml;
import org.jboss.as.controller.persistence.BackupXmlConfigurationPersister;
import org.jboss.as.controller.persistence.ExtensibleConfigurationPersister;
import org.jboss.as.controller.persistence.NullConfigurationPersister;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleLoader;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.threads.AsyncFuture;

/**
 * The application server bootstrap interface.  Get a new instance via {@link Factory#newInstance()}.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author Thomas.Diesler@jboss.com
 */
public interface Bootstrap {

    /**
     * Bootstrap a new server instance.  The list of updates should begin with extensions, followed by
     * subsystem adds, followed by deployments.  The boot action of each update will be executed; this is
     * the only time that this will happen.  This method will not block; the return value may be used to
     * wait for the result (with an optional timeout) or register an asynchronous callback.
     *
     * @param configuration the server configuration
     * @param extraServices additional services to start and stop with the server instance
     * @return the future service container
     */
    AsyncFuture<ServiceContainer> bootstrap(Configuration configuration, List<ServiceActivator> extraServices);

    /**
     * Calls {@link #bootstrap(Configuration, List)} to bootstrap the container. The value for the returned future
     * becomes available when all installed services have been started/failed.
     *
     * @param configuration the server configuration
     * @param extraServices additional services to start and stop with the server instance
     * @return the future service container
     */
    AsyncFuture<ServiceContainer> startup(Configuration configuration, List<ServiceActivator> extraServices);

    /**
     * The configuration for server bootstrap.
     */
    final class Configuration {

        private ServerEnvironment serverEnvironment;
        private ModuleLoader moduleLoader = Module.getBootModuleLoader();
        private ExtensibleConfigurationPersister configurationPersister;
        private long startTime = Module.getStartTime();

        /**
         * Set the port offset.
         *
         * @param portOffset the port offset
         */
        public void setPortOffset(final int portOffset) {
            if (portOffset < 0) {
                throw new IllegalArgumentException("portOffset may not be less than 0");
            }
        }

        /**
         * Get the server environment.
         *
         * @return the server environment
         */
        public ServerEnvironment getServerEnvironment() {
            return serverEnvironment;
        }

        /**
         * Set the server environment.
         *
         * @param serverEnvironment the server environment
         */
        public synchronized void setServerEnvironment(final ServerEnvironment serverEnvironment) {
            this.serverEnvironment = serverEnvironment;
        }

        /**
         * Get the application server module loader.
         *
         * @return the module loader
         */
        public ModuleLoader getModuleLoader() {
            return moduleLoader;
        }

        /**
         * Set the application server module loader.
         *
         * @param moduleLoader the module loader
         */
        public void setModuleLoader(final ModuleLoader moduleLoader) {
            this.moduleLoader = moduleLoader;
        }

        /**
         * Get the configuration persister to use.
         *
         * @return the configuration persister
         */
        public synchronized ExtensibleConfigurationPersister getConfigurationPersister() {
            if (configurationPersister == null) {
                if (serverEnvironment == null) {
                    configurationPersister = new NullConfigurationPersister(new StandaloneXml(moduleLoader));
                }
                else {
                    QName rootElement = new QName(Namespace.CURRENT.getUriString(), "server");
                    StandaloneXml parser = new StandaloneXml(Module.getBootModuleLoader());
                    configurationPersister = new BackupXmlConfigurationPersister(new File(serverEnvironment.getServerConfigurationDir(), "standalone.xml"), rootElement, parser, parser);
                }
            }
            return configurationPersister;
        }

        /**
         * Set the configuration persister to use.
         *
         * @param configurationPersister the configuration persister
         */
        public synchronized void setConfigurationPersister(final ExtensibleConfigurationPersister configurationPersister) {
            this.configurationPersister = configurationPersister;
        }

        /**
         * Get the server start time to report in the logs.
         *
         * @return the server start time
         */
        public long getStartTime() {
            return startTime;
        }

        /**
         * Set the server start time to report in the logs.
         *
         * @param startTime the server start time
         */
        public void setStartTime(final long startTime) {
            this.startTime = startTime;
        }
    }

    /**
     * The factory for creating new instances of {@link org.jboss.as.server.Bootstrap}.
     */
    final class Factory {

        private Factory() {
        }

        /**
         * Create a new instance.
         *
         * @return the new bootstrap instance
         */
        public static Bootstrap newInstance() {
            return new BootstrapImpl();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2672.java