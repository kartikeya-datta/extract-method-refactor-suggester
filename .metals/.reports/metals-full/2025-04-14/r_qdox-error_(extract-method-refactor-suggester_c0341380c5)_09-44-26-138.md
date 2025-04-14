error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5944.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5944.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5944.java
text:
```scala
private transient J@@MeterEngine backingEngine;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.engine;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This is the JMeter server main code.
 */
public class RemoteJMeterEngineImpl extends java.rmi.server.UnicastRemoteObject implements RemoteJMeterEngine {
    private static final long serialVersionUID = 240L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    static final String JMETER_ENGINE_RMI_NAME = "JMeterEngine"; // $NON-NLS-1$

    private JMeterEngine backingEngine;

    public static final int DEFAULT_RMI_PORT =
        JMeterUtils.getPropDefault("server.rmi.port", 1099); // $NON-NLS-1$

    private static final int DEFAULT_LOCAL_PORT =
        JMeterUtils.getPropDefault("server.rmi.localport", 0); // $NON-NLS-1$

    static{
        if (DEFAULT_LOCAL_PORT != 0){
            System.out.println("Using local port: "+DEFAULT_LOCAL_PORT);
        }
    }
    // Should we create our own copy of the RMI registry?
    private static final boolean createServer =
        JMeterUtils.getPropDefault("server.rmi.create", true); // $NON-NLS-1$

    private RemoteJMeterEngineImpl(int port) throws RemoteException {
        super(port); // Create this object using the specified port (0 means anonymous)
        System.out.println("Created remote object: "+this.getRef().remoteToString());
    }

    public static void startServer(int port) throws RemoteException {
        RemoteJMeterEngineImpl engine = new RemoteJMeterEngineImpl(DEFAULT_LOCAL_PORT);
        engine.init(port == 0 ? DEFAULT_RMI_PORT : port);
    }

    private void init(int port) throws RemoteException {
        log.info("Starting backing engine on " + port);
        InetAddress localHost=null;
        try {
            // Bug 47980
            String host = System.getProperties().getProperty("java.rmi.server.hostname"); // $NON-NLS-1$
            if( host==null ) {
                localHost = InetAddress.getLocalHost();
            } else {
                localHost = InetAddress.getByName(host);
            }
        } catch (UnknownHostException e1) {
            throw new RemoteException("Cannot start. Unable to get local host IP address.");
        }
        log.info("IP address="+localHost.getHostAddress());
        String hostName = localHost.getHostName();
        if (localHost.isLoopbackAddress()){
            throw new RemoteException("Cannot start. "+hostName+" is a loopback address.");
        }
        log.debug("This = " + this);
        if (createServer){
            log.info("Creating RMI registry (server.rmi.create=true)");
            try {
                LocateRegistry.createRegistry(port);
            } catch (RemoteException e){
                String msg="Problem creating registry: "+e;
                log.warn(msg);
                System.err.println(msg);
                System.err.println("Continuing...");
            }
        }
        try {
            Registry reg = LocateRegistry.getRegistry(port);
            reg.rebind(JMETER_ENGINE_RMI_NAME, this);
            log.info("Bound to registry on port " + port);
        } catch (Exception ex) {
            log.error("rmiregistry needs to be running to start JMeter in server " + "mode\n\t" + ex.toString());
            // Throw an Exception to ensure caller knows ...
            throw new RemoteException("Cannot start. See server log file.");
        }
    }

    /**
     * Adds a feature to the ThreadGroup attribute of the RemoteJMeterEngineImpl
     * object.
     *
     * @param testTree
     *            the feature to be added to the ThreadGroup attribute
     */
    public void configure(HashTree testTree, String host) throws RemoteException {
        log.info("Creating JMeter engine on host "+host);
        backingEngine = new StandardJMeterEngine(host);
        backingEngine.configure(testTree);
    }

    public void runTest() throws RemoteException, JMeterEngineException {
        log.info("running test");
        backingEngine.runTest();
    }

    public void reset() throws RemoteException {
        log.info("Reset");
        backingEngine.reset();
    }

    public void stopTest() throws RemoteException {
        log.info("Stopping test");
        backingEngine.stopTest();// TODO: askThreadsToStop() instead?
    }

    public void exit() throws RemoteException {
        log.info("Exitting");
        backingEngine.exit();
    }

    public void setProperties(Properties p) throws RemoteException {
        backingEngine.setProperties(p);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5944.java