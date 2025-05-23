error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/623.java
text:
```scala
final M@@ountHandle mountHandle = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_ROOT).getMountHandle();

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

package org.jboss.as.web.deployment;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.module.MountHandle;
import org.jboss.as.server.deployment.module.TempFileProviderService;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.web.deployment.helpers.DeploymentStructure;
import org.jboss.as.web.deployment.helpers.DeploymentStructure.ClassPathEntry;
import org.jboss.metadata.web.spec.TldMetaData;
import org.jboss.metadata.web.spec.WebMetaData;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VirtualFileFilter;
import org.jboss.vfs.VisitorAttributes;
import org.jboss.vfs.util.SuffixMatchFilter;

import static org.jboss.as.web.deployment.WarDeploymentMarker.isWarDeployment;

/**
 * Create and mount classpath entries in the .war deployment.
 *
 * @author Emanuel Muckenhuber
 */
public class WarStructureDeploymentProcessor implements DeploymentUnitProcessor {

    public static final String WEB_INF_LIB = "WEB-INF/lib";
    public static final String WEB_INF_CLASSES = "WEB-INF/classes";

    public static final VirtualFileFilter DEFAULT_WEB_INF_LIB_FILTER = new SuffixMatchFilter(".jar", VisitorAttributes.DEFAULT);

    private final WebMetaData sharedWebMetaData;
    private final List<TldMetaData> sharedTldsMetaData;

    public WarStructureDeploymentProcessor(final WebMetaData sharedWebMetaData, final List<TldMetaData> sharedTldsMetaData) {
        this.sharedWebMetaData = sharedWebMetaData;
        this.sharedTldsMetaData = sharedTldsMetaData;
    }

    /** {@inheritDoc} */
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if(!isWarDeployment(deploymentUnit)) {
            return; // Skip non web deployments
        }
        final VirtualFile deploymentRoot = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_ROOT).getRoot();
        if(deploymentRoot == null) {
            return;
        }
        // TODO: This needs to be ported to add additional resource roots the standard way
        final MountHandle mountHandle = phaseContext.getDeploymentUnit().getAttachment(Attachments.DEPLOYMENT_ROOT_MOUNT_HANDLE);
        try {
            final ClassPathEntry[] entries = createResourceRoots(deploymentRoot, mountHandle);
            final DeploymentStructure structure = new DeploymentStructure(entries);
            deploymentUnit.putAttachment(DeploymentStructure.ATTACHMENT_KEY, structure);

            final ServiceTarget target = phaseContext.getServiceTarget();
            final ServiceName sName = phaseContext.getPhaseServiceName().append("war", "structure");
            target.addService(sName, new DeploymentStructureService(structure)).addDependency(phaseContext.getPhaseServiceName()).install();
            target.addDependency(sName);

        } catch(Exception e) {
            throw new DeploymentUnitProcessingException(e);
        }
        // Add the war metadata
        final WarMetaData warMetaData = new WarMetaData();
        warMetaData.setSharedWebMetaData(sharedWebMetaData);
        deploymentUnit.putAttachment(WarMetaData.ATTACHMENT_KEY, warMetaData);
        // Add the shared TLDs metadata
        final TldsMetaData tldsMetaData = new TldsMetaData();
        tldsMetaData.setSharedTlds(sharedTldsMetaData);
        deploymentUnit.putAttachment(TldsMetaData.ATTACHMENT_KEY, tldsMetaData);
    }

    public void undeploy(final DeploymentUnit context) {
    }

    /**
     * Create the resource roots for a .war deployment
     *
     * @param deploymentRoot the deployment root
     * @param mountHandle the root mount handle
     * @return the resource roots
     * @throws IOException for any error
     */
    ClassPathEntry[] createResourceRoots(final VirtualFile deploymentRoot, MountHandle mountHandle) throws IOException, DeploymentUnitProcessingException {
        final List<ClassPathEntry> entries = new ArrayList<ClassPathEntry>();
        // WEB-INF classes
        entries.add(new ClassPathEntry(deploymentRoot.getChild(WEB_INF_CLASSES), null));
        // WEB-INF lib
        createWebInfLibResources(deploymentRoot, entries);
        return entries.toArray(new ClassPathEntry[entries.size()]);
    }

    /**
     * Create the ResourceRoots for .jars in the WEB-INF/lib folder.
     *
     * @param deploymentRoot the deployment root
     * @param resourcesRoots the resource root map
     * @throws IOException for any error
     */
    void createWebInfLibResources(final VirtualFile deploymentRoot, List<ClassPathEntry> entries) throws IOException, DeploymentUnitProcessingException {
        final VirtualFile webinfLib = deploymentRoot.getChild(WEB_INF_LIB);
        if(webinfLib.exists()) {
            final List<VirtualFile> archives = webinfLib.getChildren(DEFAULT_WEB_INF_LIB_FILTER);
            for(final VirtualFile archive : archives) {
                try {
                    final Closeable closable = VFS.mountZip(archive, archive, TempFileProviderService.provider());
                    entries.add(new ClassPathEntry(archive, closable));
                } catch (IOException e) {
                    throw new DeploymentUnitProcessingException("failed to process " + archive, e);
                }
            }
        }
    }


    static class DeploymentStructureService implements Service<Void> {
        final DeploymentStructure structure;
        public DeploymentStructureService(DeploymentStructure structure) {
            this.structure = structure;
        }

        /** {@inheritDoc} */
        public Void getValue() throws IllegalStateException {
            return null;
        }

        /** {@inheritDoc} */
        public void start(StartContext context) throws StartException {
            //
        }

        /** {@inheritDoc} */
        public void stop(StopContext context) {
            for(final ClassPathEntry entry : structure.getEntries()) {
                try {
                    entry.close();
                } catch(IOException ignore) {
                    //
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/623.java