error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7062.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7062.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1092
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7062.java
text:
```scala
class LocalOnlyEjbClientConfiguration implements EJBClientConfiguration {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
p@@ackage org.jboss.as.ejb3.remote;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.DeploymentNodeSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.xnio.OptionMap;

import javax.security.auth.callback.CallbackHandler;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Iterator;

/**
 * Service that manages an EJBClientContext
 *
 * @author Stuart Douglas
 */
public class DefaultEjbClientContextService implements Service<EJBClientContext> {

    private static final Logger logger = Logger.getLogger(DefaultEjbClientContextService.class);

    /**
     * The base service name for these services
     */
    public static final ServiceName BASE_SERVICE_NAME = ServiceName.JBOSS.append("ejb3", "ejbClientContext");

    /**
     * The default service name. There will always be a service registered under this name
     */
    public static final ServiceName DEFAULT_SERVICE_NAME = BASE_SERVICE_NAME.append("default");

    // setup the EJB client context selector in a static block so that the service restart
    // doesn't run into trouble while resetting a selector, since the ability to switch the
    // selector will be locked https://issues.jboss.org/browse/AS7-2998
    static {
        // setup the selector
        AccessController.doPrivileged(new SetSelectorAction(TCCLEJBClientContextSelector.INSTANCE));
    }

    private final InjectedValue<TCCLEJBClientContextSelectorService> tcclEJBClientContextSelector = new InjectedValue<TCCLEJBClientContextSelectorService>();

    private final InjectedValue<LocalEjbReceiver> defaultLocalEJBReceiver = new InjectedValue<LocalEjbReceiver>();

    /**
     * The client context
     */
    private volatile EJBClientContext context;

    private final boolean lockSelectorOnStart;

    /**
     * @param lockEJBClientContextSelectorOnStart
     *         True if the EJB client context selector should be locked on start of this
     *         service. False otherwise.
     */
    public DefaultEjbClientContextService(final boolean lockEJBClientContextSelectorOnStart) {
        this.lockSelectorOnStart = lockEJBClientContextSelectorOnStart;
    }

    @Override
    public synchronized void start(final StartContext context) throws StartException {
        final EJBClientContext clientContext = EJBClientContext.create(new LocalOnlyEjbClientConfiguration());
        // register the default local EJB receiver (if present - app clients don't have local EJB receivers)
        final LocalEjbReceiver localEjbReceiver = this.defaultLocalEJBReceiver.getOptionalValue();
        if (localEjbReceiver != null) {
            clientContext.registerEJBReceiver(localEjbReceiver);
        }
        this.context = clientContext;
        if (this.lockSelectorOnStart) {
            // lock the EJB client context selector
            AccessController.doPrivileged(new LockSelectorAction());
        }

        // the EJBClientContext selector is set to TCCLEJBClientContextSelector and is *locked* once
        // (in a static block of this service) so that restarting this service will not cause failures related
        // to resetting the selector. The TCCLEJBClientContextSelector is backed by a TCCLEJBClientContextSelectorService
        // which is what we set here during the service start, so that the selector has the correct service to return the
        // EJBClientContext. @see https://issues.jboss.org/browse/AS7-2998 for details
        TCCLEJBClientContextSelector.INSTANCE.setup(this.tcclEJBClientContextSelector.getValue(), this.context);

    }

    @Override
    public synchronized void stop(final StopContext context) {
        this.context = null;
        TCCLEJBClientContextSelector.INSTANCE.destroy();
    }

    @Override
    public EJBClientContext getValue() throws IllegalStateException, IllegalArgumentException {
        return context;
    }


    public Injector<TCCLEJBClientContextSelectorService> getTCCLBasedEJBClientContextSelectorInjector() {
        return this.tcclEJBClientContextSelector;
    }

    public Injector<LocalEjbReceiver> getDefaultLocalEJBReceiverInjector() {
        return this.defaultLocalEJBReceiver;
    }

    private static final class SetSelectorAction implements PrivilegedAction<ContextSelector<EJBClientContext>> {

        private final ContextSelector<EJBClientContext> selector;

        private SetSelectorAction(final ContextSelector<EJBClientContext> selector) {
            this.selector = selector;
        }

        @Override
        public ContextSelector<EJBClientContext> run() {
            return EJBClientContext.setSelector(selector);
        }
    }

    private static final class LockSelectorAction implements PrivilegedAction<Void> {
        @Override
        public Void run() {
            EJBClientContext.lockSelector();
            return null;
        }
    }

    /**
     * A {@link EJBClientConfiguration} which is applicable only for a {@link EJBClientContext}
     * consisting of just the {@link LocalEjbReceiver}. i.e. this client configuration cannot be used
     * for setting up connections to remote servers
     */
    private class LocalOnlyEjbClientConfiguration implements EJBClientConfiguration {

        private final DeploymentNodeSelector localPreferringDeploymentNodeSelector = new LocalEJBReceiverPreferringDeploymentNodeSelector();

        @Override
        public String getEndpointName() {
            // This client configuration will *not* be used to create endpoints
            return null;
        }

        @Override
        public OptionMap getEndpointCreationOptions() {
            // This client configuration will *not* be used to create endpoints
            return OptionMap.EMPTY;
        }

        @Override
        public OptionMap getRemoteConnectionProviderCreationOptions() {
            // This client configuration will *not* be used to register connection providers
            return OptionMap.EMPTY;
        }

        @Override
        public CallbackHandler getCallbackHandler() {
            // This client configuration is not applicable for registering remote connections
            return null;
        }

        @Override
        public Iterator<RemotingConnectionConfiguration> getConnectionConfigurations() {
            // This client configuration will *not* be used for auto creating connections to remote servers.
            return Collections.EMPTY_SET.iterator();
        }

        @Override
        public Iterator<ClusterConfiguration> getClusterConfigurations() {
            return Collections.EMPTY_SET.iterator();
        }

        @Override
        public ClusterConfiguration getClusterConfiguration(String nodeName) {
            return null;
        }

        @Override
        public long getInvocationTimeout() {
            return 0;
        }

        @Override
        public long getReconnectTasksTimeout() {
            return 0;
        }

        @Override
        public DeploymentNodeSelector getDeploymentNodeSelector() {
            return this.localPreferringDeploymentNodeSelector;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7062.java