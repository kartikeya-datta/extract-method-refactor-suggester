error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/254.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/254.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/254.java
text:
```scala
final S@@tring deploymentName = key.getName() + ":" + key.getSha1HashAsHexString();

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

package org.jboss.as.model;

import org.jboss.as.deployment.DeploymentService;
import org.jboss.as.deployment.chain.DeploymentChain;
import org.jboss.as.deployment.chain.DeploymentChainProvider;
import org.jboss.as.deployment.module.MountHandle;
import org.jboss.as.deployment.module.TempFileProviderService;
import org.jboss.as.deployment.unit.DeploymentUnitContextImpl;
import org.jboss.as.deployment.unit.DeploymentUnitProcessingException;
import org.jboss.logging.Logger;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.Location;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

import javax.xml.stream.XMLStreamException;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.jboss.as.deployment.attachment.VirtualFileAttachment.attachVirtualFile;

/**
 * A deployment which is mapped into a {@link ServerGroupElement}.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ServerGroupDeploymentElement extends AbstractModelElement<ServerGroupDeploymentElement> implements ServiceActivator {
    private static final long serialVersionUID = -7282640684801436543L;
    private static final Logger log = Logger.getLogger("org.jboss.as.model");

    private final DeploymentUnitKey key;
    private boolean start;

    /**
     * Construct a new instance.
     *
     * @param location the declaration location of this element
     * @param deploymentName the name of the deployment unit
     * @param deploymentHash the hash of the deployment unit
     */
    public ServerGroupDeploymentElement(final Location location, final String deploymentName, final byte[] deploymentHash, final boolean start) {
        super(location);
        if (deploymentName == null) {
            throw new IllegalArgumentException("deploymentName is null");
        }
        if (deploymentHash == null) {
            throw new IllegalArgumentException("deploymentHash is null");
        }
        if (deploymentHash.length != 20) {
            throw new IllegalArgumentException("deploymentHash is not a valid length");
        }
        this.key = new DeploymentUnitKey(deploymentName, deploymentHash);
        this.start = start;
    }

    public ServerGroupDeploymentElement(XMLExtendedStreamReader reader) throws XMLStreamException {
        super(reader);
        // Handle attributes
        String fileName = null;
        byte[] sha1Hash = null;
        String start = null;
        final int count = reader.getAttributeCount();
        for(int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (reader.getAttributeNamespace(i) != null) {
                throw unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case NAME: {
                        fileName = value;
                        break;
                    }
                    case SHA1: {
                        try {
                            sha1Hash = hexStringToByteArray(value);
                        }
                        catch (Exception e) {
                            throw new XMLStreamException("Value " + value +
                                    " for attribute " + attribute.getLocalName() +
                                    " does not represent a properly hex-encoded SHA1 hash",
                                    reader.getLocation(), e);
                        }
                        break;
                    }
                    case START: {
                        start = value;
                        break;
                    }
                    default: throw unexpectedAttribute(reader, i);
                }
            }
        }
        if (fileName == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }
        if (sha1Hash == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.SHA1));
        }

        this.key = new DeploymentUnitKey(fileName, sha1Hash);
        this.start = start == null ? true : Boolean.valueOf(start);
        // Handle elements
        requireNoContent(reader);

        // TODO:  Read in serialized DIs
    }

    /**
     * Gets the identifier of this deployment that's suitable for use as a map key.
     *
     * @return the key
     */
    public DeploymentUnitKey getKey() {
        return key;
    }

    /**
     * Gets the name of the deployment.
     *
     * @return the name
     */
    public String getName() {
        return key.getName();
    }

    /**
     * Gets a defensive copy of the sha1 hash of the deployment.
     *
     * @return the hash
     */
    public byte[] getSha1Hash() {
        return key.getSha1Hash();
    }

    /**
     * Gets whether the deployment should be started upon server start.
     *
     * @return <code>true</code> if the deployment should be started; <code>false</code>
     *         if not.
     */
    public boolean isStart() {
        return start;
    }

    /**
     * Sets whether the deployments should be started upon server start.
     *
     * @param start <code>true</code> if the deployment should be started; <code>false</code>
     *         if not.
     */
    void setStart(boolean start) {
        this.start = start;
    }

    /** {@inheritDoc} */
    public long elementHash() {
        long hash = key.elementHash();
        hash = Long.rotateLeft(hash, 1) ^ Boolean.valueOf(start).hashCode() & 0xffffffffL;
        return hash;
    }

    /** {@inheritDoc} */
    protected void appendDifference(final Collection<AbstractModelUpdate<ServerGroupDeploymentElement>> target, final ServerGroupDeploymentElement other) {
        // FIXME implement appendDifference
        throw new UnsupportedOperationException("implement me");
    }

    /** {@inheritDoc} */
    protected Class<ServerGroupDeploymentElement> getElementClass() {
        return ServerGroupDeploymentElement.class;
    }

    /** {@inheritDoc} */
    public void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeAttribute(Attribute.NAME.getLocalName(), key.getName());
        streamWriter.writeAttribute(Attribute.SHA1.getLocalName(), key.getSha1HashAsHexString());
        if (!this.start) streamWriter.writeAttribute(Attribute.START.getLocalName(), "false");
    }

    @Override
    public void activate(final ServiceActivatorContext context) {
        final String deploymentName = key.getName().replace('.', '_');
        log.info("Activating server group deployment: " + deploymentName);

        final VirtualFile deploymentRoot = VFS.getChild(getFullyQualifiedDeploymentPath(key.getName()));
        if (!deploymentRoot.exists())
            throw new RuntimeException("Deployment root does not exist." + deploymentRoot);

        Closeable handle = null;
        try {
            // Mount virtual file
            try {
                if(deploymentRoot.isFile())
                    handle = VFS.mountZip(deploymentRoot, deploymentRoot, TempFileProviderService.provider());
            } catch (IOException e) {
                throw new RuntimeException("Failed to mount deployment archive", e);
            }

            final BatchBuilder batchBuilder = context.getBatchBuilder();
            // Create deployment service
            final ServiceName deploymentServiceName = DeploymentService.SERVICE_NAME.append(deploymentName);
            batchBuilder.addService(deploymentServiceName, new DeploymentService());

            // Create a sub-batch for this deployment
            final BatchBuilder deploymentSubBatch = batchBuilder.subBatchBuilder();

            // Setup a batch level dependency on deployment service
            deploymentSubBatch.addDependency(deploymentServiceName);

            // Create the deployment unit context
            final DeploymentUnitContextImpl deploymentUnitContext = new DeploymentUnitContextImpl(deploymentName, deploymentSubBatch);
            attachVirtualFile(deploymentUnitContext, deploymentRoot);
            deploymentUnitContext.putAttachment(MountHandle.ATTACHMENT_KEY, new MountHandle(handle));

            // Execute the deployment chain
            final DeploymentChainProvider deploymentChainProvider = DeploymentChainProvider.INSTANCE;
            final DeploymentChain deploymentChain = deploymentChainProvider.determineDeploymentChain(deploymentRoot);
            if(deploymentChain == null)
                throw new RuntimeException("Failed determine the deployment chain for deployment root: " + deploymentRoot);
            try {
                deploymentChain.processDeployment(deploymentUnitContext);
            } catch (DeploymentUnitProcessingException e) {
                throw new RuntimeException("Failed to process deployment chain.", e);
            }
        } catch(Throwable t) {
            VFSUtils.safeClose(handle);
            throw new RuntimeException("Failed to activate deployment unit " + key.getName(), t);
        }
    }

    private String getFullyQualifiedDeploymentPath(final String fileName) {
        return System.getProperty("jboss.server.deploy.dir") + "/" + fileName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/254.java