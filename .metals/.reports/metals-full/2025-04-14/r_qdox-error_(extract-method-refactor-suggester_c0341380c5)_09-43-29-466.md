error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1617.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1617.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1617.java
text:
```scala
h@@ashInput = ParseUtils.hexStringToByteArray(value);

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

import java.util.Collections;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.deployment.client.api.server.ServerDeploymentActionResult;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * A deployment which is mapped into a {@link ServerGroupElement}.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ServerGroupDeploymentElement extends AbstractModelElement<ServerGroupDeploymentElement> {
    private static final long serialVersionUID = -7282640684801436543L;



    private final String uniqueName;
    private final String runtimeName;
    private final byte[] sha1Hash;
    private boolean start;

    /**
     * Construct a new instance.
     *
     * @param uniqueName the name of the deployment unit
     * @param runtimeName file name of the deployment unit
     * @param sha1Hash the hash of the deployment unit
     */
    public ServerGroupDeploymentElement(final String uniqueName,
            final String runtimeName,
            final byte[] sha1Hash,
            final boolean start) {
        if (uniqueName == null) {
            throw new IllegalArgumentException("uniqueName is null");
        }
        if (runtimeName == null) {
            throw new IllegalArgumentException("runtimeName is null");
        }
        if (sha1Hash.length != 20) {
            throw new IllegalArgumentException("sha1Hash is not a valid length");
        }
        this.uniqueName = uniqueName;
        this.runtimeName = runtimeName;
        this.sha1Hash = sha1Hash;
        this.start = start;
    }

    public ServerGroupDeploymentElement(XMLExtendedStreamReader reader, final RefResolver<String, DeploymentRepositoryElement> repositoryResolver) throws XMLStreamException {
        super();
        if (repositoryResolver == null) {
            throw new IllegalArgumentException("repositoryResolver is null");
        }

        // Handle attributes
        String uniqueInput = null;
        String runtimeInput = null;
        byte[] hashInput = null;
        String start = null;
        final int count = reader.getAttributeCount();
        for(int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (reader.getAttributeNamespace(i) != null) {
                throw ParseUtils.unexpectedAttribute(reader, i);
            } else {
                final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                switch (attribute) {
                    case NAME: {
                        uniqueInput = value;
                        break;
                    }
                    case RUNTIME_NAME: {
                        runtimeInput = value;
                        break;
                    }
                    case SHA1: {
                        try {
                            hashInput = hexStringToByteArray(value);
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
                    default:
                        throw ParseUtils.unexpectedAttribute(reader, i);
                }
            }
        }
        if (uniqueInput == null) {
            throw ParseUtils.missingRequired(reader, Collections.singleton(Attribute.NAME));
        }
        if (runtimeInput == null) {
            throw ParseUtils.missingRequired(reader, Collections.singleton(Attribute.RUNTIME_NAME));
        }
        if (hashInput == null) {
            throw ParseUtils.missingRequired(reader, Collections.singleton(Attribute.SHA1));
        }

        this.uniqueName = uniqueInput;
        this.runtimeName = runtimeInput;
        this.sha1Hash = hashInput;
        this.start = start == null ? true : Boolean.valueOf(start);
        // Handle elements
        ParseUtils.requireNoContent(reader);

        // TODO:  Read in serialized DIs
    }

    /**
     * Gets the unique identifier of this deployment.
     *
     * @return the key
     */
    public String getUniqueName() {
        return uniqueName;
    }

    /**
     * Gets the name by which the deployment is known within a running server.
     *
     * @return the runtime name
     */
    public String getRuntimeName() {
        return runtimeName;
    }

    /**
     * Gets a defensive copy of the sha1 hash of the deployment.
     *
     * @return the hash
     */
    public byte[] getSha1Hash() {
        byte[] copy = new byte[sha1Hash.length];
        System.arraycopy(sha1Hash, 0, copy, 0, sha1Hash.length);
        return copy;
    }

    /**
     * Gets the sha1 hash of the deployment in string form.
     *
     * @return the hash
     */
    public String getSha1HashAsHexString() {
        return bytesToHexString(sha1Hash);
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
    @Override
    protected Class<ServerGroupDeploymentElement> getElementClass() {
        return ServerGroupDeploymentElement.class;
    }

    /** {@inheritDoc} */
    @Override
    public void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeAttribute(Attribute.NAME.getLocalName(), uniqueName);
        streamWriter.writeAttribute(Attribute.RUNTIME_NAME.getLocalName(), runtimeName);
        streamWriter.writeAttribute(Attribute.SHA1.getLocalName(), getSha1HashAsHexString());
        if (!this.start) streamWriter.writeAttribute(Attribute.START.getLocalName(), "false");

        streamWriter.writeEndElement();
    }

//    @Override
    public void activate(final ServiceActivatorContext context, final ServiceContainer serviceContainer) {
        // TODO move this into an update class
        ServerDeploymentStartStopHandler support = new ServerDeploymentStartStopHandler();
        support.deploy(uniqueName, runtimeName, sha1Hash, context.getBatchBuilder(), serviceContainer, NoOpUpdateResultHandler.INSTANCE, null);
    }

    private static class NoOpUpdateResultHandler implements UpdateResultHandler<ServerDeploymentActionResult, Void> {

        private static final NoOpUpdateResultHandler INSTANCE = new NoOpUpdateResultHandler();
        @Override
        public void handleFailure(Throwable cause, Void param) {
        }

        @Override
        public void handleSuccess(ServerDeploymentActionResult result, Void param) {
        }

        @Override
        public void handleTimeout(Void param) {
        }

        @Override
        public void handleCancellation(Void param) {
        }

        @Override
        public void handleRollbackFailure(Throwable cause, Void param) {
        }

        @Override
        public void handleRollbackSuccess(Void param) {
        }

        @Override
        public void handleRollbackCancellation(Void param) {
        }

        @Override
        public void handleRollbackTimeout(Void param) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1617.java