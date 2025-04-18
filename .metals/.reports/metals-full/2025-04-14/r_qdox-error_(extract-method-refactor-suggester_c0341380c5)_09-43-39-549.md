error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11218.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11218.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11218.java
text:
```scala
public static final l@@ong PRIORITY = DeploymentPhases.STRUCTURE.plus(100);

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

package org.jboss.as.deployment.module;

import static org.jboss.as.deployment.attachment.VirtualFileAttachment.getVirtualFileAttachment;

import java.io.Closeable;
import java.io.IOException;

import org.jboss.as.deployment.DeploymentPhases;
import org.jboss.as.deployment.unit.DeploymentUnitContext;
import org.jboss.as.deployment.unit.DeploymentUnitProcessingException;
import org.jboss.as.deployment.unit.DeploymentUnitProcessor;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.StartException;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VirtualFileVisitor;
import org.jboss.vfs.VisitorAttributes;

/**
 * Processor responsible for discovering nested jars and mounting/attaching them to the deployment
 *
 * @author Jason T. Greene
 */
public class NestedJarInlineProcessor implements DeploymentUnitProcessor {
    private static final Logger log = Logger.getLogger("org.jboss.as.deployment");

    public static final long PRIORITY = DeploymentPhases.MODULARIZE.plus(90L);


    /**
     * Mounts all nested jars inline with the mount of the deployment jar.
     *
     * @param context the deployment unit context
     * @throws DeploymentUnitProcessingException
     */
    public void processDeployment(DeploymentUnitContext context) throws DeploymentUnitProcessingException {

        final VirtualFile deploymentRoot = getVirtualFileAttachment(context);
        final NestedMounts mounts = new NestedMounts();

        try {
            deploymentRoot.visit(new VirtualFileVisitor() {
                public void visit(VirtualFile virtualFile) {
                    if (virtualFile.getName().endsWith(".jar")) {
                        try {
                            MountHandle handle = new MountHandle(VFS.mountZip(virtualFile, virtualFile, TempFileProviderService.provider()));
                            mounts.add(virtualFile, handle);
                        } catch (IOException e) {
                            log.warnf("Could not mount %s in deployment %s, skipping", virtualFile.getPathNameRelativeTo(deploymentRoot), deploymentRoot.getName());
                        }
                    }
                }
                public VisitorAttributes getAttributes() {
                    return VisitorAttributes.RECURSE_LEAVES_ONLY;
                }
            });
        } catch (IOException e) {
            for (NestedMounts.Entry mount: mounts) {
                mount.mount().close();
            }

            throw new DeploymentUnitProcessingException("Could not mount nested jars in deployment: " + deploymentRoot.getName(), e);
        }

        if (mounts.size() > 0) {
            context.putAttachment(NestedMounts.ATTACHMENT_KEY, mounts);
            context.getBatchServiceBuilder().addListener(new CloseListener(mounts.getClosables()));
        }
    }

    static class CloseListener implements ServiceListener<Void> {
        private Closeable[] closeables;

        CloseListener(Closeable[] closeables) {
            this.closeables = closeables;
        }

        @Override
        public void serviceStopped(ServiceController<? extends Void> controller) {
            if (closeables != null) {
                for (Closeable close : closeables) {
                    try {
                        close.close();
                    } catch (IOException e) {
                        // Munch munch
                    }
                }
                closeables = null;
            }
        }

        public void listenerAdded(ServiceController<? extends Void> controller) {
        }

        public void serviceStarting(ServiceController<? extends Void> controller) {
        }

        public void serviceStarted(ServiceController<? extends Void> controller) {
        }

        public void serviceFailed(ServiceController<? extends Void> controller, StartException reason) {
        }

        public void serviceStopping(ServiceController<? extends Void> controller) {
        }

        public void serviceRemoved(ServiceController<? extends Void> controller) {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11218.java