error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10095.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10095.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10095.java
text:
```scala
c@@onnectionCreationOptionsModel.get(optionName).set(optionValue);

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

package org.jboss.as.test.integration.ejb.remote.common;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PORT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_REF;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import javax.security.auth.callback.CallbackHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.ejb3.subsystem.EJB3Extension;
import org.jboss.as.ejb3.subsystem.EJB3SubsystemModel;
import org.jboss.as.remoting.RemotingExtension;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * @author Jaikiran Pai
 */
public class EJBRemoteManagementUtil {

    private static final Logger logger = Logger.getLogger(EJBRemoteManagementUtil.class);

    /**
     * Returns the EJB remoting connector port that can be used for EJB remote invocations
     *
     * @param managementServerHostName The hostname of the server
     * @param managementPort           The management port
     * @return
     */
    public static int getEJBRemoteConnectorPort(final String managementServerHostName, final int managementPort, final CallbackHandler handler) {
        final ModelControllerClient modelControllerClient = getModelControllerClient(managementServerHostName, managementPort, handler);
        try {
            // first get the remote-connector from the EJB3 subsystem to find the remote connector ref
            // /subsystem=ejb3/service=remote:read-attribute(name=connector-ref)
            final ModelNode readConnectorRefAttribute = new ModelNode();
            readConnectorRefAttribute.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION);
            readConnectorRefAttribute.get(NAME).set(EJB3SubsystemModel.CONNECTOR_REF);

            final PathAddress ejbRemotingServiceAddress = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, EJB3Extension.SUBSYSTEM_NAME),
                    EJB3SubsystemModel.REMOTE_SERVICE_PATH);
            readConnectorRefAttribute.get(OP_ADDR).set(ejbRemotingServiceAddress.toModelNode());

            // execute the read-attribute
            final ModelNode connectorRefResult = execute(modelControllerClient, readConnectorRefAttribute);
            final String connectorRef = connectorRefResult.get(RESULT).asString();

            // now get the socket-binding ref for this connector ref, from the remoting subsystem
            // /subsystem=remoting/connector=<connector-ref>:read-attribute(name=socket-binding)
            final ModelNode readSocketBindingRefAttribute = new ModelNode();
            readSocketBindingRefAttribute.get(OP).set(READ_ATTRIBUTE_OPERATION);
            readSocketBindingRefAttribute.get(NAME).set(SOCKET_BINDING);

            final PathAddress remotingSubsystemAddress = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, RemotingExtension.SUBSYSTEM_NAME),
                    PathElement.pathElement("connector", connectorRef));
            readSocketBindingRefAttribute.get(OP_ADDR).set(remotingSubsystemAddress.toModelNode());

            // execute the read-attribute
            final ModelNode socketBindingRefResult = execute(modelControllerClient, readSocketBindingRefAttribute);
            final String socketBindingRef = socketBindingRefResult.get(RESULT).asString();

            // now get the port value of that socket binding ref
            // /socket-binding-group=standard-sockets/socket-binding=<socket-binding-ref>:read-attribute(name=port)
            final ModelNode readPortAttribute = new ModelNode();
            readPortAttribute.get(OP).set(READ_ATTRIBUTE_OPERATION);
            readPortAttribute.get(NAME).set(PORT);
            // TODO: "standard-sockets" group is hardcoded for now
            final PathAddress socketBindingAddress = PathAddress.pathAddress(PathElement.pathElement(SOCKET_BINDING_GROUP, "standard-sockets"),
                    PathElement.pathElement(SOCKET_BINDING, socketBindingRef));
            readPortAttribute.get(OP_ADDR).set(socketBindingAddress.toModelNode());

            // execute the read-attribute
            final ModelNode portResult = execute(modelControllerClient, readPortAttribute);
            return portResult.get(RESULT).asInt();

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            // close the controller client connection
            try {
                modelControllerClient.close();
            } catch (IOException e) {
                logger.warn("Error closing model controller client", e);
            }
        }
    }

    public static void createRemoteOutboundSocket(final String managementServerHostName, final int managementPort,
                                                  final String socketGroupName, final String outboundSocketName, final String destinationHost, final int destinationPort,
                                                  final CallbackHandler callbackHandler) {
        final ModelControllerClient modelControllerClient = getModelControllerClient(managementServerHostName, managementPort, callbackHandler);
        try {
            // /socket-binding-group=<group-name>/remote-destination-outbound-socket-binding=<name>:add(host=<host>, port=<port>)
            final ModelNode outboundSocketAddOperation = new ModelNode();
            outboundSocketAddOperation.get(OP).set(ADD);

            final PathAddress address = PathAddress.pathAddress(PathElement.pathElement(SOCKET_BINDING_GROUP, socketGroupName),
                    PathElement.pathElement(ModelDescriptionConstants.REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING, outboundSocketName));
            outboundSocketAddOperation.get(OP_ADDR).set(address.toModelNode());
            // setup the other parameters for the add operation
            outboundSocketAddOperation.get(HOST).set(destinationHost);
            outboundSocketAddOperation.get(PORT).set(destinationPort);
            // execute the add operation
            execute(modelControllerClient, outboundSocketAddOperation);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            // close the controller client connection
            try {
                modelControllerClient.close();
            } catch (IOException e) {
                logger.warn("Error closing model controller client", e);
            }
        }
    }

    public static void createLocalOutboundSocket(final String managementServerHostName, final int managementPort,
                                                  final String socketGroupName, final String outboundSocketName,
                                                  final String socketBindingRef,
                                                  final CallbackHandler callbackHandler) {
        final ModelControllerClient modelControllerClient = getModelControllerClient(managementServerHostName, managementPort, callbackHandler);
        try {
            // /socket-binding-group=<group-name>/local-destination-outbound-socket-binding=<name>:add(socket-binding-ref=<ref>)
            final ModelNode outboundSocketAddOperation = new ModelNode();
            outboundSocketAddOperation.get(OP).set(ADD);

            final PathAddress address = PathAddress.pathAddress(PathElement.pathElement(SOCKET_BINDING_GROUP, socketGroupName),
                    PathElement.pathElement(ModelDescriptionConstants.LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING, outboundSocketName));
            outboundSocketAddOperation.get(OP_ADDR).set(address.toModelNode());
            // setup the other parameters for the add operation
            outboundSocketAddOperation.get(SOCKET_BINDING_REF).set(socketBindingRef);
            // execute the add operation
            execute(modelControllerClient, outboundSocketAddOperation);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            // close the controller client connection
            try {
                modelControllerClient.close();
            } catch (IOException e) {
                logger.warn("Error closing model controller client", e);
            }
        }
    }

    public static void removeLocalOutboundSocket(final String managementServerHostName, final int managementPort,
                                                  final String socketGroupName, final String outboundSocketName,
                                                  final CallbackHandler callbackHandler) {
        final ModelControllerClient modelControllerClient = getModelControllerClient(managementServerHostName, managementPort, callbackHandler);
        try {
            // /socket-binding-group=<group-name>/local-destination-outbound-socket-binding=<name>:remove()
            final ModelNode outboundSocketRemoveOperation = new ModelNode();
            outboundSocketRemoveOperation.get(OP).set(REMOVE);

            final PathAddress address = PathAddress.pathAddress(PathElement.pathElement(SOCKET_BINDING_GROUP, socketGroupName),
                    PathElement.pathElement(ModelDescriptionConstants.LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING, outboundSocketName));
            outboundSocketRemoveOperation.get(OP_ADDR).set(address.toModelNode());
            // execute the remove operation
            execute(modelControllerClient, outboundSocketRemoveOperation);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            // close the controller client connection
            try {
                modelControllerClient.close();
            } catch (IOException e) {
                logger.warn("Error closing model controller client", e);
            }
        }
    }

    public static void createRemoteOutboundConnection(final String managementServerHostName, final int managementPort,
                                                      final String connectionName, final String outboundSocketRef,
                                                      final Map<String, String> connectionCreationOptions, final CallbackHandler callbackHandler) {
        final ModelControllerClient modelControllerClient = getModelControllerClient(managementServerHostName, managementPort, callbackHandler);
        try {
            // /subsystem=remoting/remote-outbound-connection=<name>:add(outbound-socket-ref=<ref>)
            final ModelNode addRemoteOutboundConnection = new ModelNode();
            addRemoteOutboundConnection.get(OP).set(ADD);
            final PathAddress address = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, RemotingExtension.SUBSYSTEM_NAME),
                    PathElement.pathElement("remote-outbound-connection", connectionName));
            addRemoteOutboundConnection.get(OP_ADDR).set(address.toModelNode());

            // set the other properties
            addRemoteOutboundConnection.get("outbound-socket-binding-ref").set(outboundSocketRef);
            if (!connectionCreationOptions.isEmpty()) {
                final ModelNode connectionCreationOptionsModel = addRemoteOutboundConnection.get("connection-creation-options");
                for (final Map.Entry<String, String> entry : connectionCreationOptions.entrySet()) {
                    final String optionName = entry.getKey();
                    final String optionValue = entry.getValue();
                    connectionCreationOptionsModel.set(optionName, optionValue);
                }
            }

            // execute the add operation
            execute(modelControllerClient, addRemoteOutboundConnection);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            // close the controller client connection
            try {
                modelControllerClient.close();
            } catch (IOException e) {
                logger.warn("Error closing model controller client", e);
            }
        }
    }

    public static void removeRemoteOutboundConnection(final String managementServerHostName, final int managementPort,
                                                      final String connectionName, final CallbackHandler callbackHandler) {
        final ModelControllerClient modelControllerClient = getModelControllerClient(managementServerHostName, managementPort, callbackHandler);
        try {
            // /subsystem=remoting/remote-outbound-connection=<name>:remove()
            final ModelNode removeRemoteOutboundConnection = new ModelNode();
            removeRemoteOutboundConnection.get(OP).set(REMOVE);
            final PathAddress address = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, RemotingExtension.SUBSYSTEM_NAME),
                    PathElement.pathElement("remote-outbound-connection", connectionName));
            removeRemoteOutboundConnection.get(OP_ADDR).set(address.toModelNode());

            // execute the remove operation
            execute(modelControllerClient, removeRemoteOutboundConnection);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            // close the controller client connection
            try {
                modelControllerClient.close();
            } catch (IOException e) {
                logger.warn("Error closing model controller client", e);
            }
        }
    }

    public static String getNodeName(final String managementServerHostName, final int managementPort) {
        // TODO: FIXME: Right now, we just return the "hostname" of the client as the node name of the server.
        // This works only when both the client (test) and server are on the same system.
        // We need to fix this once I know if there's any management operation exposed for retrieving the
        // jboss.node.name system property http://lists.jboss.org/pipermail/jboss-as7-dev/2011-November/004434.html
        final String nodeName = getNodeName();
        if (nodeName == null) {
            throw new IllegalStateException("jboss.node.name could not be determined");
        }
        return nodeName;
    }

    private static ModelControllerClient getModelControllerClient(final String managementServerHostName, final int managementPort, final CallbackHandler handler) {
        try {
            return ModelControllerClient.Factory.create(InetAddress.getByName(managementServerHostName), managementPort, handler);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Cannot create model controller client for host: " + managementServerHostName + " and port " + managementPort, e);
        }
    }

    /**
     * Executes the operation and returns the result if successful. Else throws an exception
     *
     * @param modelControllerClient
     * @param operation
     * @return
     * @throws IOException
     */
    private static ModelNode execute(final ModelControllerClient modelControllerClient, final ModelNode operation) throws IOException {
        final ModelNode result = modelControllerClient.execute(operation);
        if (result.hasDefined(ClientConstants.OUTCOME) && ClientConstants.SUCCESS.equals(result.get(ClientConstants.OUTCOME).asString())) {
            logger.info("Operation " + operation.toString() + " successful");
            return result;
        } else if (result.hasDefined(ClientConstants.FAILURE_DESCRIPTION)) {
            final String failureDesc = result.get(ClientConstants.FAILURE_DESCRIPTION).toString();
            throw new RuntimeException(failureDesc);
        } else {
            throw new RuntimeException("Operation not successful; outcome = " + result.get(ClientConstants.OUTCOME));
        }

    }

    // TODO: This method is temporary hack till we figure out the management operation to get the
    // jboss.node.name system property from the server http://lists.jboss.org/pipermail/jboss-as7-dev/2011-November/004434.html
    private static String getNodeName() {
        // Logic copied from org.jboss.as.server.ServerEnvironment constructor
        final Properties props = System.getProperties();
        final Map<String, String> env = System.getenv();
        // Calculate host and default server name
        String hostName = props.getProperty("jboss.host.name");
        String qualifiedHostName = props.getProperty("jboss.qualified.host.name");
        if (qualifiedHostName == null) {
            // if host name is specified, don't pick a qualified host name that isn't related to it
            qualifiedHostName = hostName;
            if (qualifiedHostName == null) {
                // POSIX-like OSes including Mac should have this set
                qualifiedHostName = env.get("HOSTNAME");
            }
            if (qualifiedHostName == null) {
                // Certain versions of Windows
                qualifiedHostName = env.get("COMPUTERNAME");
            }
            if (qualifiedHostName == null) {
                try {
                    qualifiedHostName = InetAddress.getLocalHost().getHostName();
                } catch (UnknownHostException e) {
                    qualifiedHostName = null;
                }
            }
            if (qualifiedHostName != null && qualifiedHostName.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$|:")) {
                // IP address is not acceptable
                qualifiedHostName = null;
            }
            if (qualifiedHostName == null) {
                // Give up
                qualifiedHostName = "unknown-host.unknown-domain";
            }
            qualifiedHostName = qualifiedHostName.trim().toLowerCase();
        }
        if (hostName == null) {
            // Use the host part of the qualified host name
            final int idx = qualifiedHostName.indexOf('.');
            hostName = idx == -1 ? qualifiedHostName : qualifiedHostName.substring(0, idx);
        }

        // Set up the server name for management purposes
        String serverName = props.getProperty("jboss.server.name");
        if (serverName == null) {
            serverName = hostName;
        }
        // Set up the clustering node name
        String nodeName = props.getProperty("jboss.node.name");
        if (nodeName == null) {
            nodeName = serverName;
        }
        return nodeName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10095.java