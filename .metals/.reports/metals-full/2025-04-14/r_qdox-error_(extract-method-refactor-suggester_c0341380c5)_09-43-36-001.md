error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14057.java
text:
```scala
i@@f (boottimeOnly && !propResource.get(BOOT_TIME).asBoolean(false)) { //TODO SystemPropertyResourceDefinition.BOOT_TIME.resolveModelAttribute

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
package org.jboss.as.host.controller;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.BOOT_TIME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.JVM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT_SUBSYSTEM_ENDPOINT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROFILE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_CONFIG;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SYSTEM_PROPERTY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.as.host.controller.ManagedServer.ManagedServerBootConfiguration;
import org.jboss.as.host.controller.model.jvm.JvmElement;
import org.jboss.as.host.controller.model.jvm.JvmOptionsBuilderFactory;
import org.jboss.as.process.DefaultJvmUtils;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

/**
 * Combines the relevant parts of the domain-level and host-level models to
 * determine the jvm launch command needed to start an application server instance.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
class ManagedServerBootCmdFactory implements ManagedServerBootConfiguration {

    private static final String RMI_CLIENT_INTERVAL = "sun.rmi.dgc.client.gcInterval";
    private static final String RMI_SERVER_INTERVAL = "sun.rmi.dgc.server.gcInterval";
    private static final String DEFAULT_RMI_INTERVAL = "3600000";

    private static final ModelNode EMPTY = new ModelNode();
    static {
        EMPTY.setEmptyList();
        EMPTY.protect();
    }

    private final String serverName;
    private final ModelNode domainModel;
    private final ModelNode hostModel;
    private final ModelNode serverModel;
    private final ModelNode serverGroup;
    private final JvmElement jvmElement;
    private final HostControllerEnvironment environment;
    private final boolean managementSubsystemEndpoint;
    private final ModelNode endpointConfig = new ModelNode();

    ManagedServerBootCmdFactory(final String serverName, final ModelNode domainModel, final ModelNode hostModel, final HostControllerEnvironment environment) {
        this.serverName = serverName;
        this.domainModel = domainModel;
        this.hostModel = hostModel;
        this.serverModel = hostModel.require(SERVER_CONFIG).require(serverName);
        this.environment = environment;

        final String serverGroupName = serverModel.require(GROUP).asString();
        this.serverGroup = domainModel.require(SERVER_GROUP).require(serverGroupName);

        String serverVMName = null;
        ModelNode serverVM = null;
        if(serverModel.hasDefined(JVM)) {
            for(final String jvm : serverModel.get(JVM).keys()) {
                serverVMName = jvm;
                serverVM = serverModel.get(JVM, jvm);
                break;
            }
        }
        String groupVMName = null;
        ModelNode groupVM = null;
        if(serverGroup.hasDefined(JVM)) {
            for(final String jvm : serverGroup.get(JVM).keys()) {
                groupVMName = jvm;
                groupVM = serverGroup.get(JVM, jvm);
                break;
            }
        }
        // Use the subsystem endpoint
        // TODO by default use the subsystem endpoint
        this.managementSubsystemEndpoint = serverGroup.get(MANAGEMENT_SUBSYSTEM_ENDPOINT).asBoolean(false);
        // Get the endpoint configuration
        if(managementSubsystemEndpoint) {
            final String profileName = serverGroup.get(PROFILE).asString();
            final ModelNode profile = domainModel.get(PROFILE, profileName);
            if(profile.hasDefined(SUBSYSTEM) && profile.hasDefined("remoting")) {
                endpointConfig.set(profile.get(SUBSYSTEM, "remoting"));
            }
        }

        final String jvmName = serverVMName != null ? serverVMName : groupVMName;
        final ModelNode hostVM = jvmName != null ? hostModel.get(JVM, jvmName) : null;
        this.jvmElement = new JvmElement(jvmName, hostVM, groupVM, serverVM);
    }

    /**
     * Create and verify the configuration before trying to start the process.
     *
     * @return the process boot configuration
     */
    public ManagedServerBootConfiguration createConfiguration() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public HostControllerEnvironment getHostControllerEnvironment() {
        return environment;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getServerLaunchCommand() {
        final List<String> command = new ArrayList<String>();

        command.add(getJavaCommand());

        command.add("-D[" + ManagedServer.getServerProcessName(serverName) + "]");

        JvmOptionsBuilderFactory.getInstance().addOptions(jvmElement, command);

        //These need to go in on the command-line
        command.add("-D" + RMI_CLIENT_INTERVAL + "=" + SecurityActions.getSystemProperty(RMI_CLIENT_INTERVAL, DEFAULT_RMI_INTERVAL));
        command.add("-D" + RMI_SERVER_INTERVAL + "=" + SecurityActions.getSystemProperty(RMI_SERVER_INTERVAL, DEFAULT_RMI_INTERVAL));

        Map<String, String> bootTimeProperties = getAllSystemProperties(true);
        // Add in properties passed in to the ProcessController command line
        for (Map.Entry<String, String> hostProp : environment.getHostSystemProperties().entrySet()) {
            if (!bootTimeProperties.containsKey(hostProp.getKey())) {
                bootTimeProperties.put(hostProp.getKey(), hostProp.getValue());
            }
        }
        for (Entry<String, String> entry : bootTimeProperties.entrySet()) {
            String property = entry.getKey();
            if (!"org.jboss.boot.log.file".equals(property) && !"logging.configuration".equals(property)) {
                final StringBuilder sb = new StringBuilder("-D");
                sb.append(property);
                sb.append('=');
                sb.append(entry.getValue() == null ? "true" : entry.getValue());
                command.add(sb.toString());
            }
        }
        // Determine the directory grouping type and use it to set props controlling the server data/log/tmp dirs
        final DirectoryGrouping directoryGrouping = DirectoryGrouping.fromModel(hostModel);
        String serverDirProp = bootTimeProperties.get(ServerEnvironment.SERVER_BASE_DIR);
        File serverDir = serverDirProp == null ? new File(environment.getDomainServersDir(), serverName) : new File(serverDirProp);
        final String logDir = addPathProperty(command, "log", ServerEnvironment.SERVER_LOG_DIR, bootTimeProperties,
                directoryGrouping, environment.getDomainLogDir(), serverDir);
        addPathProperty(command, "tmp", ServerEnvironment.SERVER_TEMP_DIR, bootTimeProperties,
                directoryGrouping, environment.getDomainTempDir(), serverDir);
        addPathProperty(command, "data", ServerEnvironment.SERVER_DATA_DIR, bootTimeProperties,
                directoryGrouping, environment.getDomainDataDir(), serverDir);

        final File loggingConfig = new File(getAbsolutePath(environment.getDomainServersDir(), serverName, "data", "logging.properties"));
        final String path;
        if (loggingConfig.exists()) {
            path = "file:" + loggingConfig.getAbsolutePath();
        } else {
            command.add("-Dorg.jboss.boot.log.file=" + getAbsolutePath(new File(logDir), "boot.log"));
            final String fileName = SecurityActions.getSystemProperty("logging.configuration");
            if (fileName == null) {
                path = "file:" + getAbsolutePath(environment.getDomainConfigurationDir(), "logging.properties");
            } else {
                path = fileName;
            }
        }
        command.add(String.format("-Dlogging.configuration=%s", path));

        command.add("-jar");
        command.add(getAbsolutePath(environment.getHomeDir(), "jboss-modules.jar"));
        command.add("-mp");
        command.add(environment.getModulePath());
        command.add("-jaxpmodule");
        command.add("javax.xml.jaxp-provider");
        command.add("org.jboss.as.server");

        return command;
    }

    @Override
    public boolean isManagementSubsystemEndpoint() {
        return managementSubsystemEndpoint;
    }

    @Override
    public ModelNode getSubsystemEndpointConfiguration() {
        return endpointConfig;
    }

    private String getJavaCommand() {
        String javaHome = jvmElement.getJavaHome();
        if (javaHome == null) {
            if(environment.getDefaultJVM() != null) {
                String defaultJvm = environment.getDefaultJVM().getAbsolutePath();
                if (!defaultJvm.equals("java") || (defaultJvm.equals("java") && System.getenv("JAVA_HOME") != null)) {
                    return defaultJvm;
                }
            }
            javaHome = DefaultJvmUtils.getCurrentJvmHome();
        }

        return DefaultJvmUtils.findJavaExecutable(javaHome);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> getServerLaunchEnvironment() {
        final Map<String, String> env = new HashMap<String, String>();
        for(final Entry<String, String> property : jvmElement.getEnvironmentVariables().entrySet()) {
            env.put(property.getKey(), property.getValue());
        }
        return env;
    }

    private Map<String, String> getAllSystemProperties(boolean boottimeOnly){
        Map<String, String> props = new HashMap<String, String>();

        addSystemProperties(domainModel, props, boottimeOnly);
        addSystemProperties(serverGroup, props, boottimeOnly);
        addSystemProperties(hostModel, props, boottimeOnly);
        addSystemProperties(serverModel, props, boottimeOnly);

        return props;
    }

    private void addSystemProperties(final ModelNode source, final Map<String, String> props, boolean boottimeOnly) {
        if (source.hasDefined(SYSTEM_PROPERTY)) {
            for (Property prop : source.get(SYSTEM_PROPERTY).asPropertyList()) {
                ModelNode propResource = prop.getValue();
                if (boottimeOnly && !propResource.get(BOOT_TIME).asBoolean()) {
                    continue;
                }
                String val = propResource.hasDefined(VALUE) ? propResource.get(VALUE).asString() : null;
                props.put(prop.getName(), val);
            }
        }
    }

    /**
     * Adds the absolute path to command.
     *
     * @param command           the command to add the arguments to.
     * @param typeName          the type of directory.
     * @param propertyName      the name of the property.
     * @param properties        the properties where the path may already be defined.
     * @param directoryGrouping the directory group type.
     * @param typeDir           the domain level directory for the given directory type; to be used for by-type grouping
     * @param serverDir         the root directory for the server, to be used for 'by-server' grouping
     * @return the absolute path that was added.
     */
    private String addPathProperty(final List<String> command, final String typeName, final String propertyName, final Map<String, String> properties, final DirectoryGrouping directoryGrouping,
                                   final File typeDir, File serverDir) {
        final String result;
        final String value = properties.get(propertyName);
        if (value == null) {
            switch (directoryGrouping) {
                case BY_TYPE:
                    result = getAbsolutePath(typeDir, "servers", serverName);
                    break;
                case BY_SERVER:
                default:
                    result = getAbsolutePath(serverDir, typeName);
                    break;
            }
            properties.put(propertyName, result);
        } else {
            result = new File(value).getAbsolutePath();
        }
        command.add(String.format("-D%s=%s", propertyName, result));
        return result;
    }

    static String getAbsolutePath(final File root, final String... paths) {
        File path = root;
        for(String segment : paths) {
            path = new File(path, segment);
        }
        return path.getAbsolutePath();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14057.java