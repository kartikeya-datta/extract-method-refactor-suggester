error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14025.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14025.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14025.java
text:
```scala
private volatile O@@RB orb;

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

package org.jboss.as.jacorb.service;

import org.jboss.as.jacorb.JacORBConstants;
import org.jboss.as.jacorb.ORBInitializer;
import org.jboss.as.jacorb.naming.ORBInitialContextFactory;
import org.jboss.as.network.SocketBinding;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.omg.CORBA.ORB;

import java.net.InetSocketAddress;
import java.util.EnumSet;
import java.util.Properties;

/**
 * <p>
 * This class implements a {@code Service} that creates and installs a CORBA {@code ORB}.
 * </p>
 *
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class CorbaORBService implements Service<ORB> {

    private static final Logger log = Logger.getLogger("org.jboss.as.jacorb");

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("jacorb", "orb-service");

    private static final Properties properties = new Properties();

    private final EnumSet<ORBInitializer> initializers;

    private final InjectedValue<SocketBinding> jacORBSocketBindingInjector = new InjectedValue<SocketBinding>();

    private final InjectedValue<SocketBinding> jacORBSSLSocketBindingInjector = new InjectedValue<SocketBinding>();

    private ORB orb;

    /**
     * <p>
     * Creates an instance of {@code CorbaORBService} with the specified {@code ORBImplementation} and initializers.
     * </p>
     *
     * @param initializers a list containing the {@code ORB} initializers that must be installed.
     * @param props        a {@code Properties} instance containing the properties that were configured in the JacORB subsystem.
     */
    public CorbaORBService(EnumSet<ORBInitializer> initializers, Properties props) {
        this.initializers = initializers;
        if (props != null) {
            properties.putAll(props);
        }
    }

    @Override
    public void start(StartContext context) throws StartException {
        log.debugf("Starting Service " + context.getController().getName().getCanonicalName());

        try {
            // set the ORBClass and ORBSingleton class as system properties.
            properties.setProperty(JacORBConstants.ORB_CLASS, JacORBConstants.JACORB_ORB_CLASS);
            properties.setProperty(JacORBConstants.ORB_SINGLETON_CLASS, JacORBConstants.JacORB_ORB_SINGLETON_CLASS);
            SecurityActions.setSystemProperty(JacORBConstants.ORB_CLASS, JacORBConstants.JACORB_ORB_CLASS);
            SecurityActions.setSystemProperty(JacORBConstants.ORB_SINGLETON_CLASS, JacORBConstants.JacORB_ORB_SINGLETON_CLASS);

            // set the JacORB IIOP and IIOP/SSL ports from the respective socket bindings.
            if (this.jacORBSocketBindingInjector.getValue() != null) {
                InetSocketAddress address = this.jacORBSocketBindingInjector.getValue().getSocketAddress();
                properties.setProperty(JacORBConstants.ORB_ADDRESS, address.getHostName());
                properties.setProperty(JacORBConstants.ORB_PORT, String.valueOf(address.getPort()));
            }
            if (this.jacORBSSLSocketBindingInjector.getValue() != null) {
                InetSocketAddress address = this.jacORBSSLSocketBindingInjector.getValue().getSocketAddress();
                properties.setProperty(JacORBConstants.ORB_SSL_PORT, String.valueOf(address.getPort()));
                if (!properties.containsKey(JacORBConstants.ORB_ADDRESS)) {
                    properties.setProperty(JacORBConstants.ORB_ADDRESS, address.getHostName());
                }
            }

            // configure the naming service initial reference.
            String rootContext = properties.getProperty(JacORBConstants.NAMING_ROOT_CONTEXT);
            if (rootContext == null)
                rootContext = JacORBConstants.NAMING_DEFAULT_ROOT_CONTEXT;
            String host = properties.getProperty(JacORBConstants.ORB_ADDRESS);
            String port = properties.getProperty(JacORBConstants.ORB_PORT);
            properties.setProperty(JacORBConstants.JACORB_NAME_SERVICE_INIT_REF,
                    "corbaloc::" + host + ":" + port + "/" + rootContext);

            // export the naming service corbaloc if necessary.
            String exportCorbalocProperty = properties.getProperty(JacORBConstants.NAMING_EXPORT_CORBALOC, "on");
            if (exportCorbalocProperty != null && exportCorbalocProperty.equalsIgnoreCase("on")) {
                properties.setProperty(JacORBConstants.JACORB_NAME_SERVICE_MAP_KEY, rootContext);
            }

            // set the ORB initializers.
            properties.setProperty(JacORBConstants.JACORB_STD_INITIALIZER_KEY, JacORBConstants.JACORB_STD_INITIALIZER_VALUE);
            for (ORBInitializer initializer : this.initializers) {
                properties.setProperty(JacORBConstants.ORB_INITIALIZER_PREFIX + initializer.getInitializerClass(), "");
            }

            // initialize the ORB - the thread context classloader needs to be adjusted as the ORB classes are loaded via reflection.
            ClassLoader loader = SecurityActions.getThreadContextClassLoader();
            try {
                SecurityActions.setThreadContextClassLoader(SecurityActions.getClassLoader(this.getClass()));
                this.orb = ORB.init(new String[0], properties);
            } finally {
                // restore the thread context classloader.
                SecurityActions.setThreadContextClassLoader(loader);
            }

            // start the ORB in a separate thread.
            Thread orbThread = SecurityActions.createThread(new ORBRunner(this.orb), "ORB Run Thread");
            orbThread.start();

            // set the ORBInitialContextFactory ORB.
            ORBInitialContextFactory.setORB(this.orb);

            // bind the ORB to JNDI under java:/jboss/orb.
            ServiceTarget target = context.getChildTarget();
            CorbaServiceUtil.bindObject(target, "orb", this.orb);
        } catch (Exception e) {
            throw new StartException(e);
        }
        log.info("CORBA ORB Service Started");
    }

    @Override
    public void stop(StopContext context) {
        log.debugf("Stopping Service " + context.getController().getName().getCanonicalName());
        // stop the ORB asynchronously.
        context.asynchronous();
        Thread destroyThread = SecurityActions.createThread(new ORBDestroyer(this.orb, context), "ORB Destroy Thread");
        destroyThread.start();
    }

    @Override
    public ORB getValue() throws IllegalStateException, IllegalArgumentException {
        return this.orb;
    }

    /**
     * <p>
     * Obtains a reference to the JacORB IIOP socket binding injector. This injector is used to inject a {@code ServiceBinding}
     * containing the IIOP socket properties.
     * </p>
     *
     * @return a reference to the {@code Injector<SocketBinding>} used to inject the JacORB IIOP socket properties.
     */
    public Injector<SocketBinding> getJacORBSocketBindingInjector() {
        return this.jacORBSocketBindingInjector;
    }

    /**
     * <p>
     * Obtains a reference to the JacORB IIOP/SSL socket binding injector. This injector is used to inject a
     * {@code ServiceBinding} containing the IIOP/SSL socket properties.
     * </p>
     *
     * @return a reference to the {@code Injector<SocketBinding>} used to inject the JacORB IIOP/SSL socket properties.
     */
    public Injector<SocketBinding> getJacORBSSLSocketBindingInjector() {
        return this.jacORBSSLSocketBindingInjector;
    }

    /**
     * <p>
     * Gets the value of the specified ORB property. All ORB properties can be queried using this method. This includes
     * the properties that have been explicitly set by this service prior to creating the ORB and all JacORB properties
     * that have been specified in the JacORB subsystem configuration.
     * </p>
     *
     * @param key the property key.
     * @return the property value or {@code null} if the property with the specified key hasn't been configured.
     */
    public static String getORBProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * <p>
     * The {@code ORBRunner} calls the blocking {@code run()} method on the specified {@code ORB} instance and is used
     * to start the {@code ORB} in a dedicated thread.
     * </p>
     */
    private class ORBRunner implements Runnable {

        private ORB orb;

        public ORBRunner(ORB orb) {
            this.orb = orb;
        }

        @Override
        public void run() {
            this.orb.run();
        }
    }

    /**
     * <p>
     * The {@code ORBDestroyer} is responsible for destroying the specified {@code ORB} instance without blocking the
     * thread that called {@code stop} on {@code CorbaORBService}.
     * </p>
     */
    private class ORBDestroyer implements Runnable {

        private ORB orb;

        private StopContext context;

        public ORBDestroyer(ORB orb, StopContext context) {
            this.orb = orb;
            this.context = context;
        }

        @Override
        public void run() {
            // orb.destroy blocks until the ORB has shutdown. We must signal the context when the process is complete.
            try {
                this.orb.destroy();
            } finally {
                this.context.complete();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14025.java