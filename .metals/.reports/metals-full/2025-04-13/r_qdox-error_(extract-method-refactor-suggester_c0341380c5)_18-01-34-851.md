error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14605.java
text:
```scala
t@@mp = new File(this.domainBaseDir, "content");

/**
 *
 */
package org.jboss.as.server.manager;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Properties;

/**
 * Encapsulates the runtime environment for a {@link ServerManager}.
 *
 * @author Brian Stansberry
 */
public class ServerManagerEnvironment {


    /////////////////////////////////////////////////////////////////////////
    //                   Configuration Value Identifiers                   //
    /////////////////////////////////////////////////////////////////////////

    /**
     * Constant that holds the name of the environment property
     * for specifying the home directory for JBoss.
     */
    public static final String HOME_DIR = "jboss.home.dir";

    /**
     * Constant that holds the name of the environment property
     * for specifying the directory from which JBoss will read modules.
     *
     * <p>Defaults to <tt><em>HOME_DIR</em>/modules</tt>/
     */
    public static final String MODULES_DIR = "jboss.modules.dir";

    /**
     * Constant that holds the name of the environment property
     * for specifying the base directory for domain content.
     *
     * <p>Defaults to <tt><em>HOME_DIR</em>/domain</tt>.
     */
    public static final String DOMAIN_BASE_DIR = "jboss.domain.base.dir";

    /**
     * Constant that holds the name of the environment property
     * for specifying the server configuration URL.
     *
     * <p>Defaults to <tt><em>DOMAIN_BASE_DIR</em>/configuration</tt> .
     */
    public static final String DOMAIN_CONFIG_DIR = "jboss.domain.config.dir";

    /**
     * Constant that holds the name of the environment property
     * for specifying the directory which JBoss will use for
     * persistent data file storage.
     *
     * <p>Defaults to <tt><em>DOMAIN_BASE_DIR</em>/data</tt>.
     */
    public static final String DOMAIN_DATA_DIR = "jboss.domain.data.dir";

    /**
     * Constant that holds the name of the environment property
     * for specifying the domain deployment URL.
     *
     * <p>Defaults to <tt><em>DOMAIN_BASE_DIR</em>/deployments</tt> .
     */
    public static final String DOMAIN_DEPLOYMENT_DIR = "jboss.domain.deployment.dir";

    /**
     * Constant that holds the name of the environment property
     * for specifying the domain log directory for JBoss.
     *
     * <p>Defaults to <tt><em>DOMAIN_BASE_DIR</em>/<em>log</em></tt>.
     */
    public static final String DOMAIN_LOG_DIR = "jboss.domain.log.dir";

    /**
     * Constant that holds the name of the environment property
     * for specifying the server home directory for JBoss.
     *
     * <p>Defaults to <tt><em>DOMAIN_BASE_DIR</em>/<em>servers</em></tt>.
     */
    public static final String DOMAIN_SERVERS_DIR = "jboss.domain.servers.dir";

    /**
     * Constant that holds the name of the environment property
     * for specifying the directory which JBoss will use for
     * temporary file storage.
     *
     * <p>Defaults to <tt><em>DOMAIN_BASE_DIR</em>/tmp</tt> .
     */
    public static final String DOMAIN_TEMP_DIR = "jboss.domain.temp.dir";

    private final Properties props;
    private final String processName;
    private final InetAddress processManagerAddress;
    private final Integer processManagerPort;
    private final InetAddress serverManagerAddress;
    private final Integer serverManagerPort;
    private final File homeDir;
    private final File modulesDir;
    private final File domainBaseDir;
    private final File domainConfigurationDir;
    private final File domainDeploymentDir;
    private final File domainDataDir;
    private final File domainLogDir;
    private final File domainServersDir;
    private final File domainTempDir;
    private final File defaultJVM;
    private final boolean isRestart;

    private final InputStream stdin;
    private final PrintStream stdout;
    private final PrintStream stderr;

    public ServerManagerEnvironment(Properties props, boolean isRestart, InputStream stdin, PrintStream stdout, PrintStream stderr,
            String processName, InetAddress processManagerAddress, Integer processManagerPort, InetAddress serverManagerAddress,
            Integer serverManagerPort, String defaultJVM) {
        if (props == null) {
            throw new IllegalArgumentException("props is null");
        }
        this.props = props;

        if (stdin == null) {
             throw new IllegalArgumentException("stdin is null");
        }
        this.stdin = stdin;

        if (stdout == null) {
             throw new IllegalArgumentException("stdout is null");
        }
        this.stdout = stdout;

        if (stderr == null) {
             throw new IllegalArgumentException("stderr is null");
        }
        this.stderr = stderr;

        if (processName == null) {
            throw new IllegalArgumentException("processName is null");
        }
        if (processManagerAddress == null) {
            throw new IllegalArgumentException("processManagerAddress is null");
        }
        if (processManagerPort == null) {
            throw new IllegalArgumentException("processManagerPort is null");
        }
        if (serverManagerAddress == null) {
            throw new IllegalArgumentException("serverManagerAddress is null");
        }
        if (serverManagerPort == null) {
            throw new IllegalArgumentException("serverManagerPort is null");
        }
        this.processName = processName;
        this.processManagerPort = processManagerPort;
        this.processManagerAddress = processManagerAddress;
        this.serverManagerAddress = serverManagerAddress;
        this.serverManagerPort = serverManagerPort;
        this.isRestart = isRestart;

        File home = getFileFromProperty(HOME_DIR);
        if (home == null) {
           home = new File(System.getProperty("user.dir"));
        }
        this.homeDir = home;
        System.setProperty(HOME_DIR, homeDir.getAbsolutePath());

        File tmp = getFileFromProperty(MODULES_DIR);
        if (tmp == null) {
            tmp = new File(this.homeDir, "modules");
        }
        this.modulesDir = tmp;
        System.setProperty(MODULES_DIR, this.modulesDir.getAbsolutePath());

        tmp = getFileFromProperty(DOMAIN_BASE_DIR);
        if (tmp == null) {
            tmp = new File(this.homeDir, "domain");
        }
        this.domainBaseDir = tmp;
        System.setProperty(DOMAIN_BASE_DIR, this.domainBaseDir.getAbsolutePath());

        tmp = getFileFromProperty(DOMAIN_CONFIG_DIR);
        if (tmp == null) {
            tmp = new File(this.domainBaseDir, "configuration");
        }
        this.domainConfigurationDir = tmp;
        System.setProperty(DOMAIN_CONFIG_DIR, this.domainConfigurationDir.getAbsolutePath());

        tmp = getFileFromProperty(DOMAIN_DEPLOYMENT_DIR);
        if (tmp == null) {
            tmp = new File(this.domainBaseDir, "deployments");
        }
        this.domainDeploymentDir = tmp;
        System.setProperty(DOMAIN_DEPLOYMENT_DIR, this.domainDeploymentDir.getAbsolutePath());


        tmp = getFileFromProperty(DOMAIN_DATA_DIR);
        if (tmp == null) {
            tmp = new File(this.domainBaseDir, "data");
        }
        this.domainDataDir = tmp;
        System.setProperty(DOMAIN_DATA_DIR, this.domainDataDir.getAbsolutePath());

        tmp = getFileFromProperty(DOMAIN_LOG_DIR);
        if (tmp == null) {
            tmp = new File(this.domainBaseDir, "log");
        }
        this.domainLogDir = tmp;
        System.setProperty(DOMAIN_LOG_DIR, this.domainLogDir.getAbsolutePath());

        tmp = getFileFromProperty(DOMAIN_SERVERS_DIR);
        if (tmp == null) {
            tmp = new File(this.domainBaseDir, "servers");
        }
        this.domainServersDir = tmp;
        System.setProperty(DOMAIN_SERVERS_DIR, this.domainServersDir.getAbsolutePath());

        tmp = getFileFromProperty(DOMAIN_TEMP_DIR);
        if (tmp == null) {
            tmp = new File(this.domainBaseDir, "tmp");
        }
        this.domainTempDir = tmp;
        System.setProperty(DOMAIN_TEMP_DIR, this.domainTempDir.getAbsolutePath());

        if(defaultJVM != null) {
            this.defaultJVM = new File(defaultJVM);
        } else {
            this.defaultJVM = null;
        }
    }

    /**
     * Gets the original System.in for this process. This should only
     * be used for communication with the process manager that spawned this process.
     *
     * @return stdin
     */
    public InputStream getStdin() {
        return stdin;
    }

    /**
     * Gets the original System.out for this process. This should only
     * be used for communication with the process manager that spawned this process.
     *
     * @return stdout
     */
    public PrintStream getStdout() {
        return stdout;
    }

    /**
     * Gets the original System.err for this process. This should only
     * be used for communication with the process manager that spawned this process.
     *
     * @return stderr
     */
    public PrintStream getStderr() {
        return stderr;
    }

    /**
     * Get the process name of this process, needed to inform the process manager we have started
     *
     * @return the process name
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Gets the address the process manager passed to this process
     * to use in communicating with it.
     *
     * @return the process manager's address
     */
    public InetAddress getProcessManagerAddress() {
        return processManagerAddress;
    }

    /**
     * Gets the port number the process manager passed to this process
     * to use in communicating with it.
     *
     * @return the process manager's port
     */
    public Integer getProcessManagerPort() {
        return processManagerPort;
    }

    /**
     * Gets the address the process manager told us to listen for communication from the servers.
     *
     * @return the server manager's address
     */
    public InetAddress getServerManagerAddress() {
        return serverManagerAddress;
    }

    /**
     * Gets the port the process manager told us to listend for communication from the servers.
     *
     * @return the servermanager's port
     */
    public Integer getServerManagerPort() {
        return serverManagerPort;
    }

    /**
     * Gets whether this was a restarted server manager
     *
     * @return if it was restarted
     */
    public boolean isRestart() {
        return isRestart;
    }

    public File getHomeDir() {
        return homeDir;
    }

    public File getModulesDir() {
        return modulesDir;
    }

    public File getDomainBaseDir() {
        return domainBaseDir;
    }

    public File getDomainConfigurationDir() {
        return domainConfigurationDir;
    }

    public File getDomainDeploymentDir() {
        return domainDeploymentDir;
    }

    public File getDomainDataDir() {
        return domainDataDir;
    }

    public File getDomainLogDir() {
        return domainLogDir;
    }

    public File getDomainServersDir() {
        return domainServersDir;
    }

    public File getDomainTempDir() {
        return domainTempDir;
    }

    public File getDefaultJVM() {
        return defaultJVM;
    }

    /**
     * Get a File from configuration.
     * @return the CanonicalFile form for the given name.
     */
    private File getFileFromProperty(final String name) {
       String value = props.getProperty(name, null);
       if (value != null) {
          File f = new File(value);
          return f;
       }

       return null;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14605.java