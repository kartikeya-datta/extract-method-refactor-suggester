error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3426.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3426.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3426.java
text:
```scala
public static final A@@ttachmentKey<Set<ModelNode>> DONT_PROPAGATE_TO_SERVERS_ATTACHMENT = AttachmentKey.create(Set.class);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

/**
 *
 */
package org.jboss.as.domain.controller.operations.coordination;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationContext.AttachmentKey;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ProxyController;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.DomainOperationTransformer;
import org.jboss.as.controller.operations.OperationAttachments;
import org.jboss.as.controller.operations.common.ResolveExpressionHandler;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.resource.InterfaceDefinition;
import org.jboss.as.domain.controller.ServerIdentity;
import org.jboss.as.domain.controller.operations.ResolveExpressionOnDomainHandler;
import org.jboss.as.domain.controller.operations.deployment.DeploymentFullReplaceHandler;
import org.jboss.as.server.operations.ServerRestartRequiredHandler;
import org.jboss.as.server.operations.SystemPropertyAddHandler;
import org.jboss.as.server.operations.SystemPropertyRemoveHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONTENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT_OVERLAY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.JVM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PATH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROFILE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REPLACE_DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNTIME_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_CONFIG;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_PORT_OFFSET;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SYSTEM_PROPERTY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;
import static org.jboss.as.domain.controller.DomainControllerLogger.HOST_CONTROLLER_LOGGER;
import static org.jboss.as.domain.controller.DomainControllerMessages.MESSAGES;
import static org.jboss.as.domain.controller.operations.coordination.DomainServerUtils.getAllRunningServers;
import static org.jboss.as.domain.controller.operations.coordination.DomainServerUtils.getRelatedElements;
import static org.jboss.as.domain.controller.operations.coordination.DomainServerUtils.getServersForGroup;
import static org.jboss.as.domain.controller.operations.coordination.DomainServerUtils.getServersForType;

/**
 * Logic for creating a server-level operation that realizes the effect
 * of a domain or host level change on the server.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class ServerOperationResolver {

    public static AttachmentKey<Set<ModelNode>> DONT_PROPAGATE_TO_SERVERS_ATTACHMENT = AttachmentKey.create(Set.class);

    private enum DomainKey {

        UNKNOWN(null),
        EXTENSION("extension"),
        PATH("path"),
        SYSTEM_PROPERTY("system-property"),
        PROFILE("profile"),
        INTERFACE("interface"),
        SOCKET_BINDING_GROUP("socket-binding-group"),
        DEPLOYMENT("deployment"),
        SERVER_GROUP("server-group"),
        MANAGMENT_CLIENT_CONTENT("management-client-content"),
        HOST("host"),
        DEPLOYMENT_OVERLAY("deployment-overlay"),;

        private final String name;

        DomainKey(final String name) {
            this.name = name;
        }

        private static final Map<String, DomainKey> MAP;

        static {
            final Map<String, DomainKey> map = new HashMap<String, DomainKey>();
            for (DomainKey element : values()) {
                final String name = element.name;
                if (name != null) map.put(name, element);
            }
            MAP = map;
        }

        public static DomainKey forName(String localName) {
            final DomainKey element = MAP.get(localName);
            return element == null ? UNKNOWN : element;
        }

    }

    private enum HostKey {

        UNKNOWN(null),
        PATH("path"),
        SYSTEM_PROPERTY("system-property"),
        CORE_SERVICE("core-service"),
        INTERFACE("interface"),
        JVM("jvm"),
        SERVER("server"),
        SERVER_CONFIG("server-config");

        private final String name;

        HostKey(final String name) {
            this.name = name;
        }

        private static final Map<String, HostKey> MAP;

        static {
            final Map<String, HostKey> map = new HashMap<String, HostKey>();
            for (HostKey element : values()) {
                final String name = element.name;
                if (name != null) map.put(name, element);
            }
            MAP = map;
        }

        public static HostKey forName(String localName) {
            final HostKey element = MAP.get(localName);
            return element == null ? UNKNOWN : element;
        }

    }

    private enum Level {
        DOMAIN, SERVER_GROUP, HOST, SERVER;
    }

    private final String localHostName;
    private final Map<String, ProxyController> serverProxies;

    public ServerOperationResolver(final String localHostName, final Map<String, ProxyController> serverProxies) {
        this.localHostName = localHostName;
        this.serverProxies = serverProxies;
    }

    public static synchronized void addToDontPropagateToServersAttachment(OperationContext context, ModelNode op) {
        Set<ModelNode> ops = context.getAttachment(DONT_PROPAGATE_TO_SERVERS_ATTACHMENT);
        if (ops == null) {
            ops = new HashSet<ModelNode>();
            context.attach(DONT_PROPAGATE_TO_SERVERS_ATTACHMENT, ops);
        }
        ops.add(op);
    }

    public Map<Set<ServerIdentity>, ModelNode> getServerOperations(OperationContext context, ModelNode originalOperation, PathAddress address) {
        if (HOST_CONTROLLER_LOGGER.isTraceEnabled()) {
            HOST_CONTROLLER_LOGGER.tracef("Resolving %s", originalOperation);
        }

        List<DomainOperationTransformer> transformers = context.getAttachment(OperationAttachments.SLAVE_SERVER_OPERATION_TRANSFORMERS);
        ModelNode operation = originalOperation;
        if(transformers != null) {
            for(DomainOperationTransformer transformer : transformers) {
                operation = transformer.transform(context, operation);
            }
        }

        Set<ModelNode> dontPropagate = context.getAttachment(DONT_PROPAGATE_TO_SERVERS_ATTACHMENT);
        if (dontPropagate != null && dontPropagate.contains(operation)) {
            return Collections.emptyMap();
        }

        final ModelNode domain = Resource.Tools.readModel(context.readResourceFromRoot(PathAddress.EMPTY_ADDRESS, true));
        final ModelNode host = domain.get(HOST, localHostName);
        if (address.size() == 0) {
            return resolveDomainRootOperation(operation, domain, host);
        } else {
            DomainKey domainKey = DomainKey.forName(address.getElement(0).getKey());
            switch (domainKey) {
                case EXTENSION: {
                    Set<ServerIdentity> allServers = getAllRunningServers(host, localHostName, serverProxies);
                    return Collections.singletonMap(allServers, operation);
                }
                case DEPLOYMENT: {
                    return Collections.emptyMap();
                }
                case PATH: {
                    return getServerPathOperations(operation, address, host, true);
                }
                case SYSTEM_PROPERTY: {
                    return getServerSystemPropertyOperations(operation, address, Level.DOMAIN, domain, null, host);
                }
                case PROFILE: {
                    return getServerProfileOperations(operation, address, domain, host);
                }
                case INTERFACE: {
                    return getServerInterfaceOperations(operation, address, host, true);
                }
                case SOCKET_BINDING_GROUP: {
                    return getServerSocketBindingGroupOperations(operation, address, domain, host);
                }
                case SERVER_GROUP: {
                    return getServerGroupOperations(operation, address, domain, host);
                }
                case MANAGMENT_CLIENT_CONTENT: {
                    return Collections.emptyMap();
                }
                case HOST: {
                    return getServerHostOperations(operation, address, domain, host);
                }
                case DEPLOYMENT_OVERLAY: {
                    return getDeploymentOverlayOperations(operation, host);
                }
                default:
                    throw MESSAGES.unexpectedInitialPathKey(address.getElement(0).getKey());
            }
        }
    }


    private Map<Set<ServerIdentity>, ModelNode> getServerProfileOperations(ModelNode operation, PathAddress address,
                                                                           ModelNode domain, ModelNode host) {
        if (address.size() == 1) {
            return Collections.emptyMap();
        }
        String profileName = address.getElement(0).getValue();
        Set<String> relatedProfiles = getRelatedElements(PROFILE, profileName, domain);
        Set<ServerIdentity> allServers = new HashSet<ServerIdentity>();
        for (String profile : relatedProfiles) {
            allServers.addAll(getServersForType(PROFILE, profile, domain, host, localHostName, serverProxies));
        }
        ModelNode serverOp = operation.clone();
        PathAddress serverAddress = address.subAddress(1);
        serverOp.get(OP_ADDR).set(serverAddress.toModelNode());
        return Collections.singletonMap(allServers, serverOp);
    }

    private Map<Set<ServerIdentity>, ModelNode> getDeploymentOverlayOperations(ModelNode operation,
                                                                               ModelNode host) {
        final Set<ServerIdentity> allServers = getAllRunningServers(host, localHostName, serverProxies);
        return Collections.singletonMap(allServers, operation.clone());
    }

    private Map<Set<ServerIdentity>, ModelNode> getServerInterfaceOperations(ModelNode operation, PathAddress address,
                                                                             ModelNode hostModel, boolean forDomain) {
        String pathName = address.getElement(0).getValue();
        Map<Set<ServerIdentity>, ModelNode> result;
        if (forDomain && hostModel.hasDefined(INTERFACE) && hostModel.get(INTERFACE).keys().contains(pathName)) {
            // Host will take precedence; ignore the domain
            result = Collections.emptyMap();
        } else if (forDomain && ADD.equals(operation.get(OP).asString()) && InterfaceDefinition.isOperationDefined(operation)) {
            // don't create named interfaces
            result = Collections.emptyMap();
        } else if (hostModel.hasDefined(SERVER_CONFIG)) {
            Set<ServerIdentity> servers = new HashSet<ServerIdentity>();
            for (Property prop : hostModel.get(SERVER_CONFIG).asPropertyList()) {

                String serverName = prop.getName();
                if (serverProxies.get(serverName) == null) {
                    continue;
                }

                ModelNode server = prop.getValue();

                String serverGroupName = server.require(GROUP).asString();

                if (server.hasDefined(INTERFACE) && server.get(INTERFACE).keys().contains(pathName)) {
                    // Server takes precedence; ignore domain
                    continue;
                }

                ServerIdentity groupedServer = new ServerIdentity(localHostName, serverGroupName, serverName);
                servers.add(groupedServer);
            }

            ModelNode serverOp = operation.clone();
            serverOp.get(OP_ADDR).setEmptyList().add(INTERFACE, pathName);
            result = Collections.singletonMap(servers, serverOp);
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    private Map<Set<ServerIdentity>, ModelNode> getJVMRestartOperations(final PathAddress address, final ModelNode hostModel) {
        // See which servers are affected by this JVM change
        final String pathName = address.getElement(0).getValue();
        final Map<Set<ServerIdentity>, ModelNode> result;
        if (hostModel.hasDefined(SERVER_CONFIG)) {
            final Set<ServerIdentity> servers = new HashSet<ServerIdentity>();
            for (Property prop : hostModel.get(SERVER_CONFIG).asPropertyList()) {
                final String serverName = prop.getName();
                if (serverProxies.get(serverName) == null) {
                    // No running server
                    continue;
                }
                final ModelNode server = prop.getValue();
                if (server.hasDefined(JVM) && server.get(JVM).keys().contains(pathName)) {
                    final String serverGroupName = server.require(GROUP).asString();
                    final ServerIdentity groupedServer = new ServerIdentity(localHostName, serverGroupName, serverName);
                    servers.add(groupedServer);
                }
            }
            result = getServerRestartRequiredOperations(servers);
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }


    private Map<Set<ServerIdentity>, ModelNode> getServerPathOperations(ModelNode operation, PathAddress address, ModelNode hostModel, boolean forDomain) {
        String pathName = address.getElement(0).getValue();
        Map<Set<ServerIdentity>, ModelNode> result;
        if (forDomain && hostModel.hasDefined(PATH) && hostModel.get(PATH).keys().contains(pathName)) {
            // Host will take precedence; ignore the domain
            result = Collections.emptyMap();
        } else if (ADD.equals(operation.get(OP).asString()) && !operation.hasDefined(PATH)) {
            // don't push named only paths
            result = Collections.emptyMap();
        } else if (hostModel.hasDefined(SERVER_CONFIG)) {
            Set<ServerIdentity> servers = new HashSet<ServerIdentity>();
            for (Property prop : hostModel.get(SERVER_CONFIG).asPropertyList()) {

                String serverName = prop.getName();
                if (serverProxies.get(serverName) == null) {
                    continue;
                }

                ModelNode server = prop.getValue();

                String serverGroupName = server.require(GROUP).asString();

                if (server.hasDefined(PATH) && server.get(PATH).keys().contains(pathName)) {
                    // Server takes precedence; ignore domain
                    continue;
                }

                ServerIdentity groupedServer = new ServerIdentity(localHostName, serverGroupName, serverName);
                servers.add(groupedServer);
            }

            ModelNode serverOp = operation.clone();
            serverOp.get(OP_ADDR).setEmptyList().add(PATH, pathName);
            result = Collections.singletonMap(servers, serverOp);
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    private Map<Set<ServerIdentity>, ModelNode> getServerSocketBindingGroupOperations(ModelNode operation,
                                                                                      PathAddress address, ModelNode domain, ModelNode host) {
        String bindingGroupName = address.getElement(0).getValue();
        Set<String> relatedBindingGroups = getRelatedElements(SOCKET_BINDING_GROUP, bindingGroupName, domain);
        Set<ServerIdentity> result = new HashSet<ServerIdentity>();
        for (String bindingGroup : relatedBindingGroups) {
            result.addAll(getServersForType(SOCKET_BINDING_GROUP, bindingGroup, domain, host, localHostName, serverProxies));
        }
        for (Iterator<ServerIdentity> iter = result.iterator(); iter.hasNext(); ) {
            ServerIdentity gs = iter.next();
            ModelNode server = host.get(SERVER_CONFIG, gs.getServerName());
            if (server.hasDefined(SOCKET_BINDING_GROUP) && !bindingGroupName.equals(server.get(SOCKET_BINDING_GROUP).asString())) {
                iter.remove();
            }
        }
        ModelNode serverOp = operation.clone();
        return Collections.singletonMap(result, serverOp);
    }

    private Map<Set<ServerIdentity>, ModelNode> getServerGroupOperations(ModelNode operation, PathAddress address,
                                                                         ModelNode domain, ModelNode host) {
        Map<Set<ServerIdentity>, ModelNode> result = null;
        if (address.size() > 1) {
            String type = address.getElement(1).getKey();
            if (JVM.equals(type)) {
                // Changes to the JVM require a restart
                String groupName = address.getElement(0).getValue();
                Set<ServerIdentity> servers = getServersForGroup(groupName, host, localHostName, serverProxies);
                return getServerRestartRequiredOperations(servers);
            } else if (DEPLOYMENT.equals(type)) {
                String groupName = address.getElement(0).getValue();
                Set<ServerIdentity> servers = getServersForGroup(groupName, host, localHostName, serverProxies);
                ModelNode serverOp = operation.clone();
                if (ADD.equals(serverOp.get(OP).asString())) {
                    // The op is missing the runtime-name and content values that the server will need
                    ModelNode domainDeployment = domain.get(DEPLOYMENT, address.getElement(1).getValue());
                    if (!serverOp.hasDefined(RUNTIME_NAME)) {
                        serverOp.get(RUNTIME_NAME).set(domainDeployment.get(RUNTIME_NAME));
                    }
                    serverOp.get(CONTENT).set(domainDeployment.require(CONTENT));
                }
                PathAddress serverAddress = address.subAddress(1);
                serverOp.get(OP_ADDR).set(serverAddress.toModelNode());
                result = Collections.singletonMap(servers, serverOp);
            } else if (SYSTEM_PROPERTY.equals(type)) {
                String affectedGroup = address.getElement(0).getValue();
                result = getServerSystemPropertyOperations(operation, address, Level.SERVER_GROUP, domain, affectedGroup, host);
            } else if (DEPLOYMENT_OVERLAY.equals(type) && address.getLastElement().getKey().equals(DEPLOYMENT)) {
                String groupName = address.getElement(0).getValue();
                Set<ServerIdentity> servers = getServersForGroup(groupName, host, localHostName, serverProxies);
                ModelNode serverOp = operation.clone();
                PathAddress serverAddress = address.subAddress(1);
                serverOp.get(OP_ADDR).set(serverAddress.toModelNode());
                result = Collections.singletonMap(servers, serverOp);
            }
        } else if (REPLACE_DEPLOYMENT.equals(operation.require(OP).asString())) {
            String groupName = address.getElement(0).getValue();
            Set<ServerIdentity> servers = getServersForGroup(groupName, host, localHostName, serverProxies);
            ModelNode serverOp = operation.clone();
            serverOp.get(OP_ADDR).setEmptyList();
            // The op is missing the runtime-name and content values that the server will need
            ModelNode domainDeployment = domain.get(DEPLOYMENT, operation.require(NAME).asString());
            serverOp.get(RUNTIME_NAME).set(domainDeployment.get(RUNTIME_NAME));
            serverOp.get(CONTENT).set(domainDeployment.require(CONTENT));
            result = Collections.singletonMap(servers, serverOp);
        } else if (ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION.equals(operation.require(OP).asString())) {
            if (PROFILE.equals(operation.get(NAME).asString())) {
                String groupName = address.getElement(0).getValue();
                Set<ServerIdentity> servers = getServersForGroup(groupName, host, localHostName, serverProxies);
                return getServerRestartRequiredOperations(servers);
            }
        }

        if (result == null) {
            result = Collections.emptyMap();
        }
        return result;
    }

    private Map<Set<ServerIdentity>, ModelNode> resolveDomainRootOperation(ModelNode operation, ModelNode domain, ModelNode host) {
        Map<Set<ServerIdentity>, ModelNode> result = null;
        String opName = operation.require(OP).asString();
        if (DeploymentFullReplaceHandler.OPERATION_NAME.equals(opName)) {
            String propName = operation.require(NAME).asString();
            Set<String> groups = getServerGroupsForDeployment(propName, domain);
            Set<ServerIdentity> allServers = new HashSet<ServerIdentity>();
            for (String group : groups) {
                allServers.addAll(getServersForGroup(group, host, localHostName, serverProxies));
            }
            result = Collections.singletonMap(allServers, operation);
        } else if (ResolveExpressionOnDomainHandler.OPERATION_NAME.equals(opName)) {
            final ModelNode serverOp = operation.clone();
            serverOp.get(OP).set(ResolveExpressionHandler.OPERATION_NAME);
            serverOp.get(OP_ADDR).setEmptyList();
            final Set<ServerIdentity> allServers = getAllRunningServers(host, localHostName, serverProxies);
            result = Collections.singletonMap(allServers, serverOp);
        }
        if (result == null) {
            result = Collections.emptyMap();
        }

        return result;
    }

    private Set<String> getServerGroupsForDeployment(String deploymentName, ModelNode domainModel) {
        Set<String> groups;
        if (domainModel.hasDefined(SERVER_GROUP)) {
            groups = new HashSet<String>();
            for (Property prop : domainModel.get(SERVER_GROUP).asPropertyList()) {
                ModelNode serverGroup = prop.getValue();
                if (serverGroup.hasDefined(DEPLOYMENT) && serverGroup.get(DEPLOYMENT).hasDefined(deploymentName)) {
                    groups.add(prop.getName());
                }
            }
        } else {
            groups = Collections.emptySet();
        }
        return groups;
    }

    private boolean hasSystemProperty(ModelNode resource, String propName) {
        return resource.hasDefined(SYSTEM_PROPERTY) && resource.get(SYSTEM_PROPERTY).hasDefined(propName);
    }

    private Map<Set<ServerIdentity>, ModelNode> getServerHostOperations(ModelNode operation, PathAddress address,
                                                                        ModelNode domain, ModelNode host) {
        if (address.size() == 1) {
            return resolveHostRootOperation(operation, domain, host);
        } else {
            HostKey hostKey = HostKey.forName(address.getElement(1).getKey());
            address = address.subAddress(1); // Get rid of the host=hostName
            switch (hostKey) {
                case PATH: {
                    return getServerPathOperations(operation, address, host, false);
                }
                case SYSTEM_PROPERTY: {
                    return getServerSystemPropertyOperations(operation, address, Level.HOST, domain, null, host);
                }
                case CORE_SERVICE: {
                    // TODO does server need to know about change?
                    return Collections.emptyMap();
                }
                case INTERFACE: {
                    return getServerInterfaceOperations(operation, address, host, false);
                }
                case JVM: {
                    return getJVMRestartOperations(address, host);
                }
                case SERVER_CONFIG: {
                    return resolveServerConfigOperation(operation, address, domain, host);
                }
                case SERVER:
                default:
                    throw MESSAGES.unexpectedInitialPathKey(address.getElement(0).getKey());
            }
        }
    }

    private Map<Set<ServerIdentity>, ModelNode> resolveHostRootOperation(ModelNode operation, ModelNode domain, ModelNode host) {
        Map<Set<ServerIdentity>, ModelNode> result = null;
        String opName = operation.require(OP).asString();
        if (ResolveExpressionOnDomainHandler.OPERATION_NAME.equals(opName)) {
            final ModelNode serverOp = operation.clone();
            serverOp.get(OP).set(ResolveExpressionHandler.OPERATION_NAME);
            serverOp.get(OP_ADDR).setEmptyList();
            final Set<ServerIdentity> allServers = getAllRunningServers(host, localHostName, serverProxies);
            result = Collections.singletonMap(allServers, serverOp);
        }

        if (result == null) {
            result = Collections.emptyMap();
        }

        return result;
    }

    /**
     * Get server operations to affect a change to a system property.
     *
     * @param operation     the domain or host level operation
     * @param address       address associated with {@code operation}
     * @param domain        the domain model, or {@code null} if {@code address} isn't for a domain level resource
     * @param affectedGroup the name of the server group affected by the operation, or {@code null}
     *                      if {@code address} isn't for a server group level resource
     * @param host          the host model
     * @return the server operations
     */
    private Map<Set<ServerIdentity>, ModelNode> getServerSystemPropertyOperations(ModelNode operation, PathAddress address, Level level,
                                                                                  ModelNode domain, String affectedGroup, ModelNode host) {

        Map<Set<ServerIdentity>, ModelNode> result = null;

        if (isServerAffectingSystemPropertyOperation(operation)) {
            String propName = address.getLastElement().getValue();

            boolean overridden = false;
            Set<String> groups = null;
            if (level == Level.DOMAIN || level == Level.SERVER_GROUP) {
                if (hasSystemProperty(host, propName)) {
                    // host level value takes precedence
                    overridden = true;
                } else if (affectedGroup != null) {
                    groups = Collections.singleton(affectedGroup);
                } else if (domain.hasDefined(SERVER_GROUP)) {
                    // Top level domain update applies to all groups where it was not overridden
                    groups = new HashSet<String>();
                    for (Property groupProp : domain.get(SERVER_GROUP).asPropertyList()) {
                        String groupName = groupProp.getName();
                        if (!hasSystemProperty(groupProp.getValue(), propName)) {
                            groups.add(groupName);
                        }
                    }
                }
            }

            Set<ServerIdentity> servers = null;
            if (!overridden && host.hasDefined(SERVER_CONFIG)) {
                servers = new HashSet<ServerIdentity>();
                for (Property serverProp : host.get(SERVER_CONFIG).asPropertyList()) {

                    String serverName = serverProp.getName();
                    if (serverProxies.get(serverName) == null) {
                        continue;
                    }

                    ModelNode server = serverProp.getValue();
                    if (!hasSystemProperty(server, propName)) {
                        String groupName = server.require(GROUP).asString();
                        if (groups == null || groups.contains(groupName)) {
                            servers.add(new ServerIdentity(localHostName, groupName, serverName));
                        }
                    }
                }
            }

            if (servers != null && servers.size() > 0) {
                Map<ModelNode, Set<ServerIdentity>> ops = new HashMap<ModelNode, Set<ServerIdentity>>();
                for (ServerIdentity server : servers) {
                    ModelNode serverOp = getServerSystemPropertyOperation(operation, propName, server, level, domain, host);
                    Set<ServerIdentity> set = ops.get(serverOp);
                    if (set == null) {
                        set = new HashSet<ServerIdentity>();
                        ops.put(serverOp, set);
                    }
                    set.add(server);
                }
                result = new HashMap<Set<ServerIdentity>, ModelNode>();
                for (Map.Entry<ModelNode, Set<ServerIdentity>> entry : ops.entrySet()) {
                    result.put(entry.getValue(), entry.getKey());
                }
            }
        }

        if (result == null) {
            result = Collections.emptyMap();
        }
        return result;
    }

    private ModelNode getServerSystemPropertyOperation(ModelNode operation, String propName, ServerIdentity server, Level level, ModelNode domain, ModelNode host) {

        ModelNode result = null;
        String opName = operation.get(OP).asString();
        if (ADD.equals(opName) || REMOVE.equals(opName)) {
            // See if there is a higher level value
            ModelNode value = null;
            switch (level) {
                case SERVER: {
                    value = getSystemPropertyValue(host, propName);
                    if (value == null) {
                        value = getSystemPropertyValue(domain.get(SERVER_GROUP, server.getServerGroupName()), propName);
                    }
                    if (value == null) {
                        value = getSystemPropertyValue(domain, propName);
                    }
                    break;
                }
                case HOST: {
                    value = getSystemPropertyValue(domain.get(SERVER_GROUP, server.getServerGroupName()), propName);
                    if (value == null) {
                        value = getSystemPropertyValue(domain, propName);
                    }
                    break;
                }
                case SERVER_GROUP: {
                    value = getSystemPropertyValue(domain, propName);
                    break;
                }
                default: {
                    break;
                }

            }
            if (value != null) {
                // A higher level defined the property, so we know this property exists on the server.
                // We convert the op to WRITE_ATTRIBUTE since we cannot ADD again and a REMOVE
                // means the higher level definition again takes effect.
                if (ADD.equals(opName)) {
                    // Use the ADD op's value
                    value = operation.has(VALUE) ? operation.get(VALUE) : new ModelNode();
                }
                // else use the higher level value that is no longer overridden
                ModelNode addr = new ModelNode();
                addr.add(SYSTEM_PROPERTY, propName);
                result = Util.getEmptyOperation(WRITE_ATTRIBUTE_OPERATION, addr);
                result.get(NAME).set(VALUE);
                if (value.isDefined()) {
                    result.get(VALUE).set(value);
                }
            }
        }

        if (result == null) {
            result = operation.clone();
            ModelNode addr = new ModelNode();
            addr.add(SYSTEM_PROPERTY, propName);
            result.get(OP_ADDR).set(addr);
        }
        return result;
    }

    private ModelNode getSystemPropertyValue(ModelNode root, String propName) {
        ModelNode result = null;
        if (root.hasDefined(SYSTEM_PROPERTY) && root.get(SYSTEM_PROPERTY).hasDefined(propName)) {
            ModelNode resource = root.get(SYSTEM_PROPERTY, propName);
            result = resource.hasDefined(VALUE) ? resource.get(VALUE) : new ModelNode();
        }
        return result;
    }


    private Map<Set<ServerIdentity>, ModelNode> resolveServerConfigOperation(ModelNode operation, PathAddress address,
                                                                             ModelNode domain, ModelNode host) {
        Map<Set<ServerIdentity>, ModelNode> result;
        ModelNode serverOp = null;
        if (address.size() > 1) {
            String type = address.getElement(1).getKey();
            if (PATH.equals(type) || INTERFACE.equals(type)) {
                serverOp = operation.clone();
                PathAddress serverAddress = address.subAddress(1);
                serverOp.get(OP_ADDR).set(serverAddress.toModelNode());
            } else if(JVM.equals(type)) {
                final String serverName = address.getElement(0).getValue();
                // If the server is running require a restart
                if(serverProxies.containsKey(serverName)) {
                    final String group = host.get(address.getLastElement().getKey(), address.getLastElement().getValue(), GROUP).asString();
                    final ServerIdentity id = new ServerIdentity(localHostName, group, serverName);
                    return getServerRestartRequiredOperations(Collections.singleton(id));
                }
            } else if (SYSTEM_PROPERTY.equals(type) && isServerAffectingSystemPropertyOperation(operation)) {
                String propName = address.getLastElement().getValue();
                String serverName = address.getElement(0).getValue();
                ServerIdentity serverId = getServerIdentity(serverName, host);
                serverOp = getServerSystemPropertyOperation(operation, propName, serverId, Level.SERVER, domain, host);
            }

        } else if (address.size() == 1) {
            // TODO - deal with "add", "remove" and changing "auto-start" attribute
            if (ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION.equals(operation.require(OP).asString())) {
                final String attr = operation.get(NAME).asString();
                if (GROUP.equals(attr) || SOCKET_BINDING_GROUP.equals(attr) || SOCKET_BINDING_PORT_OFFSET.equals(attr)) {
                    final String serverName = address.getElement(0).getValue();
                    // If the server is running require a restart
                    if(serverProxies.containsKey(serverName)) {
                        final String group = host.get(address.getLastElement().getKey(), address.getLastElement().getValue(), GROUP).asString();
                        final ServerIdentity id = new ServerIdentity(localHostName, group, serverName);
                        result = getServerRestartRequiredOperations(Collections.singleton(id));
                        return result;
                    }
                }
            }
        }

        if (serverOp == null) {
            result = Collections.emptyMap();
        } else {
            String serverName = address.getElement(0).getValue();
            ServerIdentity gs = getServerIdentity(serverName, host);
            Set<ServerIdentity> set = Collections.singleton(gs);
            result = Collections.singletonMap(set, serverOp);
        }
        return result;
    }

    private Map<Set<ServerIdentity>, ModelNode> getServerRestartRequiredOperations(Set<ServerIdentity> servers) {
        ModelNode op = new ModelNode();
        op.get(OP).set(ServerRestartRequiredHandler.OPERATION_NAME);
        op.get(OP_ADDR).setEmptyList();
        return Collections.singletonMap(servers, op);
    }


    private ServerIdentity getServerIdentity(String serverName, ModelNode host) {
        ModelNode serverNode = host.get(SERVER_CONFIG, serverName);
        return new ServerIdentity(localHostName, serverNode.require(GROUP).asString(), serverName);
    }

    private boolean isServerAffectingSystemPropertyOperation(ModelNode operation) {
        String opName = operation.require(OP).asString();
        return (SystemPropertyAddHandler.OPERATION_NAME.equals(opName)
 SystemPropertyRemoveHandler.OPERATION_NAME.equals(opName)
 (ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION.equals(opName) && VALUE.equals(operation.require(NAME).asString())));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3426.java