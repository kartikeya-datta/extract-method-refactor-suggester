error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2993.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2993.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2993.java
text:
```scala
t@@his.contextSelector = new LazyConnectionContextSelector(hostUrl, callbackHandler, classLoader);

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
package org.jboss.as.appclient.service;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.security.auth.callback.CallbackHandler;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ee.naming.InjectedEENamespaceContextSelector;
import org.jboss.as.naming.context.NamespaceContextSelector;
import org.jboss.as.server.deployment.SetupAction;
import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.security.manager.WildFlySecurityManager;

import static org.jboss.as.appclient.logging.AppClientLogger.ROOT_LOGGER;


/**
 * Service that is responsible for running an application clients main method, and shutting down the server once it
 * completes
 *
 * @author Stuart Douglas
 */
public class ApplicationClientStartService implements Service<ApplicationClientStartService> {


    public static final ServiceName SERVICE_NAME = ServiceName.of("appClientStart");

    private final InjectedValue<ApplicationClientDeploymentService> applicationClientDeploymentServiceInjectedValue = new InjectedValue<ApplicationClientDeploymentService>();
    private final InjectedValue<Component> applicationClientComponent = new InjectedValue<Component>();
    private final InjectedEENamespaceContextSelector namespaceContextSelectorInjectedValue;
    private final List<SetupAction> setupActions;
    private final Method mainMethod;
    private final String[] parameters;
    private final ClassLoader classLoader;
    private final ContextSelector<EJBClientContext> contextSelector;

    private Thread thread;
    private ComponentInstance instance;

    public ApplicationClientStartService(final Method mainMethod, final String[] parameters, final InjectedEENamespaceContextSelector namespaceContextSelectorInjectedValue, final ClassLoader classLoader,  final List<SetupAction> setupActions, final String hostUrl, final CallbackHandler callbackHandler) {
        this.mainMethod = mainMethod;
        this.parameters = parameters;
        this.namespaceContextSelectorInjectedValue = namespaceContextSelectorInjectedValue;
        this.classLoader = classLoader;
        this.setupActions = setupActions;
        this.contextSelector = new LazyConnectionContextSelector(hostUrl, callbackHandler);
    }

    public ApplicationClientStartService(final Method mainMethod, final String[] parameters, final InjectedEENamespaceContextSelector namespaceContextSelectorInjectedValue, final ClassLoader classLoader,  final List<SetupAction> setupActions, final EJBClientConfiguration configuration) {
        this.mainMethod = mainMethod;
        this.parameters = parameters;
        this.namespaceContextSelectorInjectedValue = namespaceContextSelectorInjectedValue;
        this.classLoader = classLoader;
        this.setupActions = setupActions;
        this.contextSelector = new ConfigBasedEJBClientContextSelector(configuration);
    }
    @Override
    public synchronized void start(final StartContext context) throws StartException {
        final ServiceContainer serviceContainer = context.getController().getServiceContainer();

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                final ClassLoader oldTccl = WildFlySecurityManager.getCurrentContextClassLoaderPrivileged();
                try {
                    try {
                        try {
                            WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(classLoader);
                            AccessController.doPrivileged(new SetSelectorAction(contextSelector));
                            applicationClientDeploymentServiceInjectedValue.getValue().getDeploymentCompleteLatch().await();
                            NamespaceContextSelector.setDefault(namespaceContextSelectorInjectedValue);

                            try {
                                //perform any additional setup that may be needed
                                for (SetupAction action : setupActions) {
                                    action.setup(Collections.<String, Object>emptyMap());
                                }

                                //do static injection etc
                                instance = applicationClientComponent.getValue().createInstance();

                                mainMethod.invoke(null, new Object[]{parameters});
                            } finally {
                                final ListIterator<SetupAction> iterator = setupActions.listIterator(setupActions.size());
                                Throwable error = null;
                                while (iterator.hasPrevious()) {
                                    SetupAction action = iterator.previous();
                                    try {
                                        action.teardown(Collections.<String, Object>emptyMap());
                                    } catch (Throwable e) {
                                        error = e;
                                    }
                                }
                                if (error != null) {
                                    throw new RuntimeException(error);
                                }
                            }
                        } catch (Exception e) {
                            ROOT_LOGGER.exceptionRunningAppClient(e, e.getClass().getSimpleName());
                        } finally {
                            WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(oldTccl);
                        }
                    } finally {
                        if(contextSelector instanceof LazyConnectionContextSelector) {
                            ((LazyConnectionContextSelector)contextSelector).close();
                        }
                    }
                } finally {
                    serviceContainer.shutdown();
                }
            }
        });
        thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if(serviceContainer != null) {
                    serviceContainer.shutdown();
                }
            }
        }));

    }

    @Override
    public synchronized void stop(final StopContext context) {
        if (instance != null) {
            instance.destroy();
        }
        thread.interrupt();
        thread = null;
    }

    @Override
    public ApplicationClientStartService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    public InjectedValue<ApplicationClientDeploymentService> getApplicationClientDeploymentServiceInjectedValue() {
        return applicationClientDeploymentServiceInjectedValue;
    }

    public InjectedValue<Component> getApplicationClientComponent() {
        return applicationClientComponent;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2993.java