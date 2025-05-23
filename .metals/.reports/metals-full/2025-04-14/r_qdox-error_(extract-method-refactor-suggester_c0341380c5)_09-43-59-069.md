error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13074.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13074.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13074.java
text:
```scala
public v@@oid awaitHostController(long start) throws InterruptedException, TimeoutException {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.test.integration.domain.management.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.domain.DomainClient;
import org.jboss.as.controller.client.helpers.domain.ServerIdentity;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.network.NetworkUtils;
import org.jboss.as.test.shared.TestSuiteEnvironment;
import org.jboss.dmr.ModelNode;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.Connection;
import org.jboss.sasl.util.UsernamePasswordHashUtil;
import org.xnio.IoUtils;

/**
 * Utility for controlling the lifecycle of a domain.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class DomainLifecycleUtil {

    public static final String SLAVE_HOST_PASSWORD = "slave_us3r_password";

    private static final ThreadFactory threadFactory = new AsyncThreadFactory();

    private final Logger log = Logger.getLogger(DomainLifecycleUtil.class.getName());

    // The ProcessController process wrapper
    private ProcessWrapper process;
    // The connection to the HC, which can be shared across multiple clients
    private DomainTestConnection connection;
    // A shared domain client
    private DomainTestClient domainClient;

//    private Map<ServerIdentity, ControlledProcessState.State> serverStatuses = new HashMap<ServerIdentity, ControlledProcessState.State>();
    private ExecutorService executor;

    private final JBossAsManagedConfiguration configuration;
    private final DomainControllerClientConfig clientConfiguration;
    private final PathAddress address;
    private final boolean closeClientConfig;

    public DomainLifecycleUtil(final JBossAsManagedConfiguration configuration) throws IOException {
        this(configuration, DomainControllerClientConfig.create(), true);
    }

    public DomainLifecycleUtil(final JBossAsManagedConfiguration configuration, final DomainControllerClientConfig clientConfiguration) {
        this(configuration, clientConfiguration, false);
    }

    private DomainLifecycleUtil(final JBossAsManagedConfiguration configuration,
                                final DomainControllerClientConfig clientConfiguration, final boolean closeClientConfig) {
        assert configuration != null : "configuration is null";
        assert clientConfiguration != null : "clientConfiguration is null";
        this.configuration = configuration;
        this.clientConfiguration = clientConfiguration;
        this.address = PathAddress.pathAddress(PathElement.pathElement(ModelDescriptionConstants.HOST, configuration.getHostName()));
        this.closeClientConfig = closeClientConfig;
    }

    public JBossAsManagedConfiguration getConfiguration() {
        return configuration;
    }

    public PathAddress getAddress() {
        return address;
    }

    public void start() {
        try {
            configuration.validate();

            final String address = NetworkUtils.formatPossibleIpv6Address(configuration.getHostControllerManagementAddress());
            final int port = configuration.getHostControllerManagementPort();
            final URI connectionURI = new URI(configuration.getHostControllerManagementProtocol() + "://"
                    + address + ":" + port);
            // Create the connection - this will try to connect on the first request
            connection = clientConfiguration.createConnection(connectionURI, configuration.getCallbackHandler());

            String jbossHomeDir = configuration.getJbossHome();

            final List<String> additionalJavaOpts = new ArrayList<String>();
            final String jbossOptions = System.getProperty("jboss.options");
            if (jbossOptions != null) {
                Collections.addAll(additionalJavaOpts, jbossOptions.split("\\s+"));
            }
            if (configuration.getJavaVmArguments() != null) {
                Collections.addAll(additionalJavaOpts, configuration.getJavaVmArguments().split("\\s+"));
            }

            File modulesJar = new File(jbossHomeDir + File.separatorChar + "jboss-modules.jar");
            if (!modulesJar.exists())
                throw new IllegalStateException("Cannot find: " + modulesJar);

            String javaHome = configuration.getJavaHome();
            String java = (javaHome != null) ? javaHome + File.separatorChar + "bin" + File.separatorChar + "java" : "java";

            String controllerJavaHome = configuration.getControllerJavaHome();
            String controllerJava = (controllerJavaHome != null) ?
                    controllerJavaHome + File.separatorChar + "bin" + File.separatorChar + "java" : "java";

            File domainDir = configuration.getDomainDirectory() != null ? new File(configuration.getDomainDirectory()) : new File(new File(jbossHomeDir), "domain");
            String domainPath = domainDir.getAbsolutePath();

            final String modulePath;
            if (configuration.getModulePath() != null && !configuration.getModulePath().isEmpty()) {
                modulePath = configuration.getModulePath();
            } else {
                modulePath = jbossHomeDir + File.separatorChar + "modules";
            }

            if (configuration.getMgmtUsersFile() != null) {
                copyConfigFile(new File(configuration.getMgmtUsersFile()), new File(configuration.getDomainDirectory(), "configuration"), null);
            } else {
                // No point backing up the file in a test scenario, just write what we need.
                File usersFile = new File(domainPath + "/configuration/mgmt-users.properties");
                FileOutputStream fos = new FileOutputStream(usersFile);
                PrintWriter pw = new PrintWriter(fos, true);
                pw.println("slave=" + new UsernamePasswordHashUtil().generateHashedHexURP("slave", "ManagementRealm", SLAVE_HOST_PASSWORD.toCharArray()));
                pw.close();
                fos.close();
            }
            if (configuration.getMgmtGroupsFile() != null) {
                copyConfigFile(new File(configuration.getMgmtGroupsFile()), new File(configuration.getDomainDirectory(), "configuration"), null);
            } else {
                // Put out empty mgmt-groups.properties.
                File mgmtGroupsProps = new File(domainPath + "/configuration/mgmt-groups.properties");
                FileOutputStream fos = new FileOutputStream(mgmtGroupsProps);
                PrintWriter pw = new PrintWriter(fos, true);
                pw.println("# Management groups");
                pw.close();
                fos.close();
            }
            // Put out empty application realm properties files so servers don't complain
            File appUsersProps = new File(domainPath + "/configuration/application-users.properties");
            FileOutputStream fos = new FileOutputStream(appUsersProps);
            PrintWriter pw = new PrintWriter(fos, true);
            pw.println("# Application users");
            pw.close();
            fos.close();
            File appRolesProps = new File(domainPath + "/configuration/application-roles.properties");
            fos = new FileOutputStream(appRolesProps);
            pw = new PrintWriter(fos, true);
            pw.println("# Application roles");
            pw.close();
            fos.close();

            List<String> cmd = new ArrayList<String>();
            cmd.add(controllerJava);
            cmd.addAll(additionalJavaOpts);
            TestSuiteEnvironment.getIpv6Args(cmd);
            cmd.add("-Djboss.home.dir=" + jbossHomeDir);
            cmd.add("-Dorg.jboss.boot.log.file=" + domainPath + "/log/process-controller.log");
            cmd.add("-Dlogging.configuration=file:" + jbossHomeDir + "/domain/configuration/logging.properties");
            cmd.add("-jar");
            cmd.add(modulesJar.getAbsolutePath());
            cmd.add("-mp");
            cmd.add(modulePath);
            cmd.add("org.jboss.as.process-controller");
            cmd.add("-jboss-home");
            cmd.add(jbossHomeDir);
            cmd.add("-jvm");
            cmd.add(controllerJava);
            cmd.add("--");
            cmd.add("-Dorg.jboss.boot.log.file=" + domainPath + "/log/host-controller.log");
            cmd.add("-Dlogging.configuration=file:" + jbossHomeDir + "/domain/configuration/logging.properties");
            TestSuiteEnvironment.getIpv6Args(cmd);
            cmd.addAll(additionalJavaOpts);
            cmd.add("--");
            cmd.add("-default-jvm");
            cmd.add(java);
            if (configuration.getHostCommandLineProperties() != null) {
                Collections.addAll(cmd, configuration.getHostCommandLineProperties().split("\\s+"));
            }
            if (configuration.isAdminOnly()) {
                cmd.add("--admin-only");
            }

            String domainDirectory = configuration.getDomainDirectory();
            if (domainDirectory != null) {
                cmd.add("-Djboss.domain.base.dir=" + domainDirectory);
            } else {
                domainDirectory = domainPath;
            }
            if (configuration.getDomainConfigFile() != null) {
                String name = copyConfigFile(new File(configuration.getDomainConfigFile()), new File(domainDirectory, "configuration"));
                if (configuration.isReadOnlyDomain()) {
                    cmd.add("--read-only-domain-config=" + name);
                } else {
                    cmd.add("--domain-config=" + name);
                }
            }
            if (configuration.getHostConfigFile() != null) {
                String name = copyConfigFile(new File(configuration.getHostConfigFile()), new File(domainDirectory, "configuration"));
                if (configuration.isReadOnlyHost()) {
                    cmd.add("--read-only-host-config=" + name);
                } else {
                    cmd.add("--host-config=" + name);
                }
            }
            if (configuration.getHostControllerManagementAddress() != null) {
                cmd.add("--interprocess-hc-address");
                cmd.add(configuration.getHostControllerManagementAddress());
                cmd.add("--pc-address");
                cmd.add(configuration.getHostControllerManagementAddress());
            }
            // the process working dir
            final String workingDir = configuration.getDomainDirectory();

            // Start the process
            final ProcessWrapper wrapper = new ProcessWrapper(configuration.getHostName(), cmd, Collections.<String, String>emptyMap(), workingDir);
            wrapper.start();
            process = wrapper;

            long start = System.currentTimeMillis();
            if (!configuration.isAdminOnly()) {
                // Wait a bit to let HC get going
                TimeUnit.SECONDS.sleep(2);
                // Wait for the servers to be started
                awaitServers(start);
                log.info("All servers started in " + (System.currentTimeMillis() - start) + " ms");
            }
            // Wait for the HC to be in running state. Normally if all servers are started, this is redundant
            // but there may not be any servers or we may be in --admin-only mode
            awaitHostController(start);
            log.info("HostController started in " + (System.currentTimeMillis() - start) + " ms");

        } catch (Exception e) {
            throw new RuntimeException("Could not start container", e);
        }

    }

    public Future<Void> startAsync() {
        Callable<Void> c = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                start();
                return null;
            }
        };

        return getExecutorService().submit(c);
    }

    public int getProcessExitCode() {
        return process.getExitValue();
    }

    /**
     * Stop and wait for the process to exit.
     */
    public synchronized void stop() {
        RuntimeException toThrow = null;
        try {
            if (process != null) {
                process.stop();
                process.waitFor();
                process = null;
            }
        } catch (Exception e) {
            toThrow = new RuntimeException("Could not stop container", e);
        } finally {
            closeConnection();
            final ExecutorService exec = executor;
            if (exec != null) {
                exec.shutdownNow();
                executor = null;
            }
            if (closeClientConfig) {
                try {
                    clientConfiguration.close();
                } catch (Exception e) {
                    if (toThrow == null) {
                        toThrow = new RuntimeException("Could not stop client configuration", e);
                    }
                }
            }
        }

        if (toThrow != null) {
            throw toThrow;
        }
    }

    public Future<Void> stopAsync() {
        Callable<Void> c = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                stop();
                return null;
            }
        };

        return Executors.newSingleThreadExecutor(threadFactory).submit(c);
    }

    /**
     * Execute an operation and wait until the connection is closed. This is only useful for :reload and :shutdown operations.
     *
     * @param operation the operation to execute
     * @return the operation result
     * @throws IOException for any error
     */
    public ModelNode executeAwaitConnectionClosed(final ModelNode operation) throws IOException {
        final DomainTestClient client = internalGetOrCreateClient();
        final Channel channel = client.getChannel();
        if( null == channel )
            throw new IllegalStateException("Didn't get a remoting channel from the DomainTestClient.");
        final Connection ref = channel.getConnection();
        ModelNode result = new ModelNode();
        try {
            result = client.execute(operation);
            // IN case the operation wasn't successful, don't bother waiting
            if(! "success".equals(result.get("outcome").asString())) {
                return result;
            }
        } catch(Exception e) {
            if(e instanceof IOException) {
                final Throwable cause = e.getCause();
                if(cause instanceof ExecutionException) {
                    // ignore, this might happen if the channel gets closed before we got the response
                } else {
                    throw (IOException) e;
                }
            } else {
                throw new RuntimeException(e);
            }
        }
        try {
            if(channel != null) {
                // Wait for the channel to close
                channel.awaitClosed();
            }
            // Wait for the connection to be closed
            connection.awaitConnectionClosed(ref);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }

    /**
     * Try to connect to the host controller.
     *
     * @throws IOException
     */
    public void connect() throws IOException {
        connect(30, TimeUnit.SECONDS);
    }

    /**
     * Try to connect to the host controller.
     *
     * @param timeout the timeout
     * @param timeUnit the timeUnit
     */
    public void connect(final long timeout, final TimeUnit timeUnit) throws IOException {
        final DomainTestConnection connection = this.connection;
        if(connection == null) {
            throw new IllegalStateException();
        }
        final long deadline = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        for(;;) {
            long remaining = deadline - System.currentTimeMillis();
            if(remaining <= 0) {
                return;
            }
            try {
                // Open a connection
                connection.connect();
                return;
            } catch (IOException e) {
                remaining = deadline - System.currentTimeMillis();
                if(remaining <= 0) {
                    throw e;
                }
            }
        }
    }

    /**
     * Create a new model controller client. The client can (and should) be closed without affecting other usages.
     *
     * @return the domain client
     */
    public DomainClient createDomainClient() {
        final DomainTestConnection connection = this.connection;
        if(connection == null) {
            throw new IllegalStateException();
        }
        return DomainClient.Factory.create(connection.createClient());
    }

    /**
     * Get a shared domain client.
     *
     * @return the domain client
     */
    public synchronized DomainClient getDomainClient() {
        return DomainClient.Factory.create(internalGetOrCreateClient());
    }

    /** Wait for all auto-start servers for the host to reach {@link ControlledProcessState.State#RUNNING} */
    public void awaitServers(long start) throws InterruptedException, TimeoutException {

        boolean serversAvailable = false;
        long deadline = start + configuration.getStartupTimeoutInSeconds() * 1000;
        while (!serversAvailable) {
            long remaining = deadline - System.currentTimeMillis();
            if(remaining <= 0) {
                break;
            }
            TimeUnit.MILLISECONDS.sleep(250);

            serversAvailable = areServersStarted();
        }

        if (!serversAvailable) {
            throw new TimeoutException(String.format("Managed servers were not started within [%d] seconds", configuration.getStartupTimeoutInSeconds()));
        }
    }

    private void awaitHostController(long start) throws InterruptedException, TimeoutException {

        boolean hcAvailable = false;
        long deadline = start + configuration.getStartupTimeoutInSeconds() * 1000;
        while (!hcAvailable) {
            long remaining = deadline - System.currentTimeMillis();
            if(remaining <= 0) {
                break;
            }
            if (!hcAvailable) {
                TimeUnit.MILLISECONDS.sleep(250);
            }
            hcAvailable = isHostControllerStarted();
        }
        if (!hcAvailable) {
            throw new TimeoutException(String.format("HostController was not started within [%d] seconds", configuration.getStartupTimeoutInSeconds()));
        }
    }

    private synchronized DomainTestClient internalGetOrCreateClient() {
        // Perhaps get rid of the shared client...
        if (domainClient == null) {
            try {
                domainClient = connection.createClient();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return domainClient;
    }

    private synchronized ExecutorService getExecutorService() {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor(threadFactory);
        }
        return executor;
    }

    public boolean areServersStarted() {
        try {
            Map<ServerIdentity, ControlledProcessState.State> statuses = getServerStatuses();
            for (Map.Entry<ServerIdentity, ControlledProcessState.State> entry : statuses.entrySet()) {
                switch (entry.getValue()) {
                    case RUNNING:
                        continue;
                    default:
                        log.log(Level.INFO, entry.getKey() + " status is " + entry.getValue());
                        return false;
                }
            }
//            serverStatuses.putAll(statuses);
            return true;
        } catch (Exception ignored) {
            // ignore, as we will get exceptions until the management comm services start
        }
        return false;
    }

    public boolean isHostControllerStarted() {
        try {
            ModelNode address = new ModelNode();
            address.add("host", configuration.getHostName());

            ControlledProcessState.State status = Enum.valueOf(ControlledProcessState.State.class, readAttribute("host-state", address).asString().toUpperCase(Locale.ENGLISH));
            return status == ControlledProcessState.State.RUNNING;
        } catch (Exception ignored) {
            //
        }
        return false;
    }

    private synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.log(Level.SEVERE, "Caught exception closing DomainTestConnection", e);
            }
        }
    }

    private Map<ServerIdentity, ControlledProcessState.State> getServerStatuses() {

        Map<ServerIdentity, ControlledProcessState.State> result = new HashMap<ServerIdentity, ControlledProcessState.State>();
        ModelNode op = new ModelNode();
        op.get("operation").set("read-children-names");
        op.get("child-type").set("server-config");
        op.get("address").add("host", configuration.getHostName());
        ModelNode opResult = executeForResult(new OperationBuilder(op).build());
        Set<String> servers = new HashSet<String>();
        for (ModelNode server : opResult.asList()) {
            servers.add(server.asString());
        }
        for (String server : servers) {
            ModelNode address = new ModelNode();
            address.add("host", configuration.getHostName());
            address.add("server-config", server);
            String group = readAttribute("group", address).resolve().asString();
            if (!readAttribute("auto-start", address).resolve().asBoolean()) {
                continue;
            }
            // Make sure the server is started before trying to contact it
            final ServerIdentity id = new ServerIdentity(configuration.getHostName(), group, server);
            if (!readAttribute("status", address).asString().equals("STARTED")) {
                result.put(id, ControlledProcessState.State.STARTING);
                continue;
            }

            address = new ModelNode();
            address.add("host", configuration.getHostName());
            address.add("server", server);

            ControlledProcessState.State status = Enum.valueOf(ControlledProcessState.State.class, readAttribute("server-state", address).asString().toUpperCase(Locale.ENGLISH));
            result.put(id, status);
        }

        return result;
    }

    private ModelNode readAttribute(String name, ModelNode address) {
        ModelNode op = new ModelNode();
        op.get("operation").set("read-attribute");
        op.get("address").set(address);
        op.get("name").set(name);
        return executeForResult(new OperationBuilder(op).build());
    }

    public ModelNode executeForResult(ModelNode op) {
        return executeForResult(new OperationBuilder(op).build());
    }

    public ModelNode executeForResult(Operation op) {
        try {
            ModelNode result = getDomainClient().execute(op);
            if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
                return result.get("result");
            } else if (result.hasDefined("failure-description")) {
                throw new RuntimeException(result.get("failure-description").toString());
            } else if (result.hasDefined("domain-failure-description")) {
                throw new RuntimeException(result.get("domain-failure-description").toString());
            } else if (result.hasDefined("host-failure-descriptions")) {
                throw new RuntimeException(result.get("host-failure-descriptions").toString());
            } else {
                throw new RuntimeException("Operation outcome is " + result.get("outcome").asString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class AsyncThreadFactory implements ThreadFactory {

        private int threadCount;

        @Override
        public Thread newThread(Runnable r) {

            Thread t = new Thread(r, DomainLifecycleUtil.class.getSimpleName() + "-" + (++threadCount));
            t.setDaemon(true);
            return t;
        }
    }

    private String copyConfigFile(File file, File dir) {
        return copyConfigFile(file, dir, "testing-");
    }

    private String copyConfigFile(File file, File dir, String prefix) {
        prefix = prefix == null ? "" : prefix;
        File newFile = new File(dir, prefix + file.getName());
        if (newFile.exists()) {
            newFile.delete();
        }
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            try {
                OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
                try {
                    int i = in.read();
                    while (i != -1) {
                        out.write(i);
                        i = in.read();
                    }
                } finally {
                    IoUtils.safeClose(out);
                }
            } finally {
                IoUtils.safeClose(in);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return newFile.getName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13074.java