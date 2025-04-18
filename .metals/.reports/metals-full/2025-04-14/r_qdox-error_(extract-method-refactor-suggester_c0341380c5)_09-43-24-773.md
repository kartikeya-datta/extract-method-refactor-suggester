error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/977.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/977.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/977.java
text:
```scala
l@@og.error(ServiceMBeanLogger.ROOT_LOGGER.postRegisterInitializationFailed(), e);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.system;

import java.util.concurrent.atomic.AtomicLong;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

import org.jboss.logging.Logger;
import org.jboss.system.logging.ServiceMBeanLogger;

/**
 * An abstract base class JBoss services can subclass to implement a service that conforms to the ServiceMBean interface.
 * Subclasses must override {@link #getName} method and should override {@link #startService}, and {@link #stopService} as
 * approriate.
 *
 * @see ServiceMBean
 *
 * @author <a href="mailto:rickard.oberg@telkel.com">Rickard Öberg</a>
 * @author Scott.Stark@jboss.org
 * @author <a href="mailto:andreas@jboss.org">Andreas Schaefer</a>
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author Eduardo Martins (AS7)
 */
public class ServiceMBeanSupport extends NotificationBroadcasterSupport implements ServiceMBean, MBeanRegistration {

    protected Logger log;

    /** The MBeanServer which we have been register with. */
    protected MBeanServer server;

    /** The object name which we are registered under. */
    protected ObjectName serviceName;

    /** The current state this service is in. */
    private int state = UNREGISTERED;

    /** Sequence number for jmx notifications we send out */
    private final AtomicLong sequenceNumber = new AtomicLong(0);

    // on AS7 MBean lifecycle is CREATED -> STARTED -> REGISTERED -> UNREGISTERED -> STOP -> DESTROY,
    // on previous AS versions is REGISTERED -> CREATED -> STARTED -> STOP -> DESTROYED -> UNREGISTERED
    // to maintain compatibility with old ServiceMBeanSupport, we ignore some state changes, but redo
    // these when proper state change happens
    // the flags below are used to mark ignored lifecycle methods invocations
    private boolean createIgnored = false;
    private boolean startIgnored = false;
    private boolean stopIgnored = false;
    private boolean destroyIgnored = false;
    private boolean unregisterIgnored = false;

    /**
     * Construct a <t>ServiceMBeanSupport</tt>.
     *
     * <p>
     * Sets up logging.
     */
    public ServiceMBeanSupport() {
        // can not call this(Class) because we need to call getClass()
        this.log = Logger.getLogger(getClass().getName());
        if (log.isTraceEnabled()) {
            log.trace("Constructing");
        }
    }

    /**
     * Construct a <t>ServiceMBeanSupport</tt>.
     *
     * <p>
     * Sets up logging.
     *
     * @param type The class type to determine category name from.
     */
    public ServiceMBeanSupport(final Class<?> type) {
        this(type.getName());
    }

    /**
     * Construct a <t>ServiceMBeanSupport</tt>.
     *
     * <p>
     * Sets up logging.
     *
     * @param category The logger category name.
     */
    public ServiceMBeanSupport(final String category) {
        this(Logger.getLogger(category));
    }

    /**
     * Construct a <t>ServiceMBeanSupport</tt>.
     *
     * @param log The logger to use.
     */
    public ServiceMBeanSupport(final Logger log) {
        this.log = log;
        if (log.isTraceEnabled()) {
            log.trace("Constructing");
        }
    }

    /**
     * Use the short class name as the default for the service name.
     *
     * @return a description of the mbean
     */
    public String getName() {
        final String s = log.getName();
        final int i = s.lastIndexOf(".");
        return i != -1 ? s.substring(i + 1, s.length()) : s;
    }

    public ObjectName getServiceName() {
        return serviceName;
    }

    public MBeanServer getServer() {
        return server;
    }

    public int getState() {
        return state;
    }

    public String getStateString() {
        return states[state];
    }

    public Logger getLog() {
        return log;
    }

    // /////////////////////////////////////////////////////////////////////////
    // State Mutators //
    // /////////////////////////////////////////////////////////////////////////

    public void create() throws Exception {
        jbossInternalCreate();
    }

    public void start() throws Exception {
        jbossInternalStart();
    }

    public void stop() {
        try {
            jbossInternalStop();
        } catch (Throwable t) {
            log.warn(ServiceMBeanLogger.ROOT_LOGGER.errorInStop(t, jbossInternalDescription()));
        }
    }

    public void destroy() {
        try {
            jbossInternalDestroy();
        } catch (Throwable t) {
            log.warn(ServiceMBeanLogger.ROOT_LOGGER.errorInDestroy(t, jbossInternalDescription()));
        }
    }

    protected String jbossInternalDescription() {
        if (serviceName != null)
            return serviceName.toString();
        else
            return getName();
    }

    public void jbossInternalLifecycle(String method) throws Exception {
        if (method == null)
            throw ServiceMBeanLogger.ROOT_LOGGER.nullMethodName();

        if (method.equals("create"))
            jbossInternalCreate();
        else if (method.equals("start"))
            jbossInternalStart();
        else if (method.equals("stop"))
            jbossInternalStop();
        else if (method.equals("destroy"))
            jbossInternalDestroy();
        else
            throw ServiceMBeanLogger.ROOT_LOGGER.unknownLifecycleMethod(method);
    }

    protected void jbossInternalCreate() throws Exception {
        // if (state == CREATED || state == STARTING || state == STARTED
        // || state == STOPPING || state == STOPPED)
        if (state != REGISTERED) {
            createIgnored = true;
            if (log.isDebugEnabled()) {
                log.debug("Ignoring create call; current state is " + getStateString());
            }
            return;
        }

        createIgnored = false;
        if (log.isDebugEnabled()) {
            log.debug("Creating " + jbossInternalDescription());
        }

        try {
            createService();
            state = CREATED;
        } catch (Exception e) {
            log.warn(ServiceMBeanLogger.ROOT_LOGGER.initializationFailed(e, jbossInternalDescription()));
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("Created " + jbossInternalDescription());
        }

        if (startIgnored) {
            start();
        }
    }

    protected void jbossInternalStart() throws Exception {
        if (state != CREATED && state != STOPPED) {
            startIgnored = true;
            if (log.isDebugEnabled()) {
                log.debug("Ignoring start call; current state is " + getStateString());
            }
            return;
        }

        startIgnored = false;

        state = STARTING;
        sendStateChangeNotification(STOPPED, STARTING, getName() + " starting", null);
        if (log.isDebugEnabled()) {
            log.debug("Starting " + jbossInternalDescription());
        }

        try {
            startService();
        } catch (Exception e) {
            state = FAILED;
            sendStateChangeNotification(STARTING, FAILED, getName() + " failed", e);
            log.warn(ServiceMBeanLogger.ROOT_LOGGER.startingFailed(e, jbossInternalDescription()));
            throw e;
        }

        state = STARTED;
        sendStateChangeNotification(STARTING, STARTED, getName() + " started", null);
        if (log.isDebugEnabled()) {
            log.debug("Started " + jbossInternalDescription());
        }

        if (stopIgnored) {
            stop();
        }
    }

    protected void jbossInternalStop() {
        if (state != STARTED) {
            stopIgnored = true;
            if (log.isDebugEnabled()) {
                log.debug("Ignoring stop call; current state is " + getStateString());
            }
            return;
        }

        stopIgnored = false;
        state = STOPPING;
        sendStateChangeNotification(STARTED, STOPPING, getName() + " stopping", null);
        if (log.isDebugEnabled()) {
            log.debug("Stopping " + jbossInternalDescription());
        }

        try {
            stopService();
        } catch (Throwable e) {
            state = FAILED;
            sendStateChangeNotification(STOPPING, FAILED, getName() + " failed", e);
            log.warn(ServiceMBeanLogger.ROOT_LOGGER.stoppingFailed(e, jbossInternalDescription()));
            return;
        }

        state = STOPPED;
        sendStateChangeNotification(STOPPING, STOPPED, getName() + " stopped", null);
        if (log.isDebugEnabled()) {
            log.debug("Stopped " + jbossInternalDescription());
        }

        if (destroyIgnored) {
            destroy();
        }
    }

    protected void jbossInternalDestroy() {
        if (state != STOPPED) {
            destroyIgnored = true;
            if (log.isDebugEnabled()) {
                log.debug("Ignoring destroy call; current state is " + getStateString());
            }
            return;
        }

        destroyIgnored = false;
        if (log.isDebugEnabled()) {
            log.debug("Destroying " + jbossInternalDescription());
        }

        try {
            destroyService();
        } catch (Throwable t) {
            log.warn(ServiceMBeanLogger.ROOT_LOGGER.destroyingFailed(t, jbossInternalDescription()));
        }
        state = DESTROYED;
        if (log.isDebugEnabled()) {
            log.debug("Destroyed " + jbossInternalDescription());
        }

        if (unregisterIgnored) {
            postDeregister();
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // JMX Hooks //
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Callback method of {@link MBeanRegistration} before the MBean is registered at the JMX Agent.
     *
     * <p>
     * <b>Attention</b>: Always call this method when you overwrite it in a subclass because it saves the Object Name of the
     * MBean.
     *
     * @param server Reference to the JMX Agent this MBean is registered on
     * @param name Name specified by the creator of the MBean. Note that you can overwrite it when the given ObjectName is null
     *        otherwise the change is discarded (maybe a bug in JMX-RI).
     * @return the ObjectName
     * @throws Exception for any error
     */
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        this.server = server;

        serviceName = getObjectName(server, name);

        return serviceName;
    }

    public void postRegister(Boolean registrationDone) {
        if (!registrationDone.booleanValue()) {
            if (log.isDebugEnabled()) {
                log.debug("Registration is not done -> stop");
            }
            stop();
        } else {
            state = REGISTERED;
            if (createIgnored) {
                try {
                    create();
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }
    }

    public void preDeregister() throws Exception {}

    public void postDeregister() {
        if (state != DESTROYED) {
            unregisterIgnored = true;
            if (log.isDebugEnabled()) {
                log.debug("Ignoring postDeregister call; current state is " + getStateString());
            }
            return;
        }
        unregisterIgnored = false;
        server = null;
        serviceName = null;
        state = UNREGISTERED;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Concrete Service Overrides //
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Sub-classes should override this method if they only need to set their object name during MBean pre-registration.
     *
     * @param server the mbeanserver
     * @param name the suggested name, maybe null
     * @return the object name
     * @throws MalformedObjectNameException for a bad object name
     */
    protected ObjectName getObjectName(MBeanServer server, ObjectName name) throws MalformedObjectNameException {
        return name;
    }

    /**
     * Sub-classes should override this method to provide custum 'create' logic.
     *
     * <p>
     * This method is empty, and is provided for convenience when concrete service classes do not need to perform anything
     * specific for this state change.
     *
     * @throws Exception for any error
     */
    protected void createService() throws Exception {}

    /**
     * Sub-classes should override this method to provide custum 'start' logic.
     *
     * <p>
     * This method is empty, and is provided for convenience when concrete service classes do not need to perform anything
     * specific for this state change.
     *
     * @throws Exception for any error
     */
    protected void startService() throws Exception {}

    /**
     * Sub-classes should override this method to provide custum 'stop' logic.
     *
     * <p>
     * This method is empty, and is provided for convenience when concrete service classes do not need to perform anything
     * specific for this state change.
     *
     * @throws Exception for any error
     */
    protected void stopService() throws Exception {}

    /**
     * Sub-classes should override this method to provide custum 'destroy' logic.
     *
     * <p>
     * This method is empty, and is provided for convenience when concrete service classes do not need to perform anything
     * specific for this state change.
     *
     * @throws Exception for any error
     */
    protected void destroyService() throws Exception {}

    /**
     * The <code>nextNotificationSequenceNumber</code> method returns the next sequence number for use in notifications.
     *
     * @return a <code>long</code> value
     */
    public long nextNotificationSequenceNumber() {
        return sequenceNumber.incrementAndGet();
    }

    /**
     * The <code>getNextNotificationSequenceNumber</code> method returns the next sequence number for use in notifications.
     *
     * @return a <code>long</code> value
     */
    protected long getNextNotificationSequenceNumber() {
        return nextNotificationSequenceNumber();
    }

    /**
     * Helper for sending out state change notifications
     */
    private void sendStateChangeNotification(int oldState, int newState, String msg, Throwable t) {
        long now = System.currentTimeMillis();
        AttributeChangeNotification stateChangeNotification = new AttributeChangeNotification(this,
                getNextNotificationSequenceNumber(), now, msg, "State", "java.lang.Integer", new Integer(oldState),
                new Integer(newState));
        stateChangeNotification.setUserData(t);
        sendNotification(stateChangeNotification);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/977.java