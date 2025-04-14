error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6017.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6017.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6017.java
text:
```scala
final J@@BossThreadFactory threadFactory = new JBossThreadFactory(new ThreadGroup("ServerDeploymentRepository-temp-threads"), Boolean.FALSE, null, "%G - %t", null, null, doPrivileged(GetAccessControlContextAction.getInstance()));

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

package org.jboss.as.server.deployment;


import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.jboss.as.server.ServerLogger;
import org.jboss.as.server.ServerMessages;
import org.wildfly.security.manager.GetAccessControlContextAction;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.threads.JBossThreadFactory;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

import static java.security.AccessController.doPrivileged;

/**
 * Provides VFS mounts of deployment content.
 *
 * @author Brian Stansberry
 */
public interface DeploymentMountProvider {

    /**
     * Standard ServiceName under which a service controller for an instance of
     * {@code Service<ServerDeploymentRepository> would be registered.
     */
    ServiceName SERVICE_NAME = ServiceName.JBOSS.append("deployment-mount-provider");

    /**
     * Requests that the given content be mounted in VFS at the given {@code mountPoint}.
     *
     *
     * @param deploymentContents the deployment contents. Cannot be <code>null</code>
     * @param mountPoint VFS location where the content should be mounted. Cannot be <code>null</code>
     * @param mountType The type of mount to perform
     * @return {@link java.io.Closeable} that can be used to close the mount
     *
     * @throws IOException  if there is an IO problem while mounting
     */
    Closeable mountDeploymentContent(VirtualFile deploymentContents, VirtualFile mountPoint, MountType mountType) throws IOException;

    static class Factory {
        public static void addService(final ServiceTarget serviceTarget) {
            serviceTarget.addService(DeploymentMountProvider.SERVICE_NAME,
                    new ServerDeploymentRepositoryImpl())
                    .install();
        }

        /**
         * Default implementation of {@link DeploymentMountProvider}.
         */
        private static class ServerDeploymentRepositoryImpl implements DeploymentMountProvider, Service<DeploymentMountProvider> {
            private volatile TempFileProvider tempFileProvider;

            /**
             * Creates a new ServerDeploymentRepositoryImpl.
             */
            private ServerDeploymentRepositoryImpl() {
            }

            @Override
            public Closeable mountDeploymentContent(final VirtualFile contents, VirtualFile mountPoint, MountType type) throws IOException {
                // according to the javadoc contents can not be null
                assert contents != null : "null contents";
                switch (type) {
                    case ZIP:
                        return VFS.mountZip(contents, mountPoint, tempFileProvider);
                    case EXPANDED:
                        return VFS.mountZipExpanded(contents, mountPoint, tempFileProvider);
                    case REAL:
                        return VFS.mountReal(contents.getPhysicalFile(), mountPoint);
                    default:
                        throw ServerMessages.MESSAGES.unknownMountType(type);
                }
            }

            @Override
            public void start(StartContext context) throws StartException {
                try {
                    final JBossThreadFactory threadFactory = new JBossThreadFactory(new ThreadGroup("ServerDeploymentRepository-temp-threads"), true, null, "%G - %t", null, null, doPrivileged(GetAccessControlContextAction.getInstance()));
                    tempFileProvider = TempFileProvider.create("temp", Executors.newScheduledThreadPool(2, threadFactory), true);
                } catch (IOException e) {
                    throw ServerMessages.MESSAGES.failedCreatingTempProvider();
                }
                ServerLogger.ROOT_LOGGER.debugf("%s started", DeploymentMountProvider.class.getSimpleName());
            }

            @Override
            public void stop(StopContext context) {
                VFSUtils.safeClose(tempFileProvider);

                ServerLogger.ROOT_LOGGER.debugf("%s stopped", DeploymentMountProvider.class.getSimpleName());
            }


            @Override
            public DeploymentMountProvider getValue() throws IllegalStateException {
                return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6017.java