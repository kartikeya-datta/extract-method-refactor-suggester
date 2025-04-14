error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/961.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/961.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/961.java
text:
```scala
R@@MIManageableSocketFactory.register(getPort(), objectPort, registryInetAddress, "cmi");

/**
 * IIOPCosNaming.java 1.0 02/07/15 Copyright (C) 2002 - INRIA
 * * Copyright (C) 2003-2006 - Simon Nieuviarts
 *
 * CAROL: Common Architecture for RMI ObjectWeb Layer
 *
 * This library is developed inside the ObjectWeb Consortium,
 * http://www.objectweb.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 */
package org.objectweb.carol.jndi.ns;

import java.net.InetAddress;

import org.objectweb.carol.cmi.Naming;
import org.objectweb.carol.cmi.Registry;
import org.objectweb.carol.cmi.RegistryImpl;
import org.objectweb.carol.cmi.RegistryKiller;
import org.objectweb.carol.cmi.DistributedEquiv;
import org.objectweb.carol.jndi.registry.RMIManageableSocketFactory;
import org.objectweb.carol.rmi.util.PortNumber;
import org.objectweb.carol.util.configuration.CarolDefaultValues;
import org.objectweb.carol.util.configuration.ConfigurationUtil;
import org.objectweb.carol.util.configuration.TraceCarol;

/**
 * Class <code>CmiRegistry</code>
 * @author Simon Nieuviarts (Simon.Nieuviarts@inrialpes.fr)
 * @author Florent Benoit (Refactoring)
 */
public class CmiRegistry extends AbsRegistry implements NameService {

    /**
     * Cluster equivalence system
     */
    private static DistributedEquiv de = null;

    /**
     * To kill the registry server
     */
    private static RegistryKiller cregk = null;

    /**
     * Instance port number (firewall)
     */
    private static int objectPort = 0;

    /**
     * InetAddress to use for creating registry (by default use all interfaces)
     */
    private InetAddress registryInetAddress = null;


    /**
     * Default constructor
     */
    public CmiRegistry() {
        super(Registry.DEFAULT_PORT);
    }


    /**
     * start Method, Start a new NameService or do nothing if the name service
     * is all ready start
     * @throws NameServiceException if a problem occure
     */
    public void start() throws NameServiceException {
        if (TraceCarol.isDebugJndiCarol()) {
            TraceCarol.debugJndiCarol("CmiRegistry.start() on port:" + getPort());
        }
        try {

            // Set factory which allow to fix rmi port if defined and if running inside a server
            if (System.getProperty(CarolDefaultValues.SERVER_MODE, "false").equalsIgnoreCase("true")) {
                if (getConfigProperties() != null) {
                    String propertyName = CarolDefaultValues.SERVER_JRMP_PORT;
                    objectPort = PortNumber.strToint(getConfigProperties().getProperty(propertyName, "0"),
                            propertyName);

                    // Read if regstry should use a single interface
                    propertyName = CarolDefaultValues.SERVER_JRMP_SINGLE_ITF;
                    boolean useSingleItf = Boolean.valueOf(getConfigProperties().getProperty(propertyName, "false")).booleanValue();
                    if (useSingleItf) {
                        String url = getConfigProperties().getProperty(CarolDefaultValues.CAROL_PREFIX + ".jrmp." + CarolDefaultValues.URL_PREFIX);
                        registryInetAddress = InetAddress.getByName(ConfigurationUtil.getHostOfUrl(url));
                    }
                } else {
                    TraceCarol.debugCarol("No properties '" + CarolDefaultValues.SERVER_JRMP_PORT
                            + "' defined in carol.properties file.");
                }
            }

            if (!isStarted()) {

                if (objectPort > 0) {
                    TraceCarol.infoCarol("Using JRMP fixed server port number '" + objectPort + "'.");
                }

                if (registryInetAddress != null) {
                    TraceCarol.infoCarol("Using Specific address to bind registry '" + registryInetAddress + "'.");
                }

                if (getPort() >= 0) {
                    RMIManageableSocketFactory.register(objectPort, registryInetAddress, "cmi");
                    de = DistributedEquiv.start();
                    cregk = RegistryImpl.start(getPort());
                    // add a shudown hook for this process
                    Runtime.getRuntime().addShutdownHook(new Thread() {

                        public void run() {
                            try {
                                CmiRegistry.this.stop();
                            } catch (Exception e) {
                                TraceCarol.error("CmiRegistry ShutdownHook problem", e);
                            }
                        }
                    });
                } else {
                    if (TraceCarol.isDebugJndiCarol()) {
                        TraceCarol.debugJndiCarol("Can't start CmiRegistry, port=" + getPort() + " is < 0");
                    }
                }
            } else {
                if (TraceCarol.isDebugJndiCarol()) {
                    TraceCarol.debugJndiCarol("CmiRegistry is already start on port:" + getPort());
                }
            }
        } catch (Exception e) {
            String msg = "can not start cluster registry: " + e;
            TraceCarol.error(msg);
            throw new NameServiceException(msg);
        }
    }

    /**
     * stop Method, Stop a NameService or do nothing if the name service is
     * already stop
     * @throws NameServiceException if a problem occure
     */
    public void stop() throws NameServiceException {
        if (TraceCarol.isDebugJndiCarol()) {
            TraceCarol.debugJndiCarol("CmiRegistry.stop()");
        }
        try {
            if (cregk != null) {
                cregk.stop();
                de.stop();
                cregk = null;
            }
        } catch (Exception e) {
            throw new NameServiceException("can not stop cluster registry: " + e);
        }
    }

    /**
     * isStarted Method, check if a name service is started
     * @return boolean true if the name service is started
     */
    public boolean isStarted() {
        if (cregk != null) {
            return true;
        }
        return false;
    }

    /**
     * @return the current registry
     */
    public static Registry getRegistry() {
        try {
            return Naming.getRegistry();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * isLocal Method, check if a name service is local
     * @return boolean true if the name service is local
     */
    public static boolean isLocal() {
        return (cregk != null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/961.java