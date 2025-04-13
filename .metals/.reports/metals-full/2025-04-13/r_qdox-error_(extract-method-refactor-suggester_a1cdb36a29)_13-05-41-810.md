error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1266.java
text:
```scala
private final E@@xecutorService executor = Executors.newSingleThreadExecutor(new BlueprintThreadFactory("Blueprint Event Dispatcher"));

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.container;


import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.blueprint.container.BlueprintEvent;
import org.osgi.service.blueprint.container.BlueprintListener;
import org.osgi.service.blueprint.container.EventConstants;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.aries.blueprint.utils.JavaUtils;

/**
 * The delivery of {@link BlueprintEvent}s is complicated.  The blueprint extender and its containers use this class to
 * deliver {@link BlueprintEvent}s.
 *
 * @version $Rev$, $Date$
 */
class BlueprintEventDispatcher implements BlueprintListener, SynchronousBundleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlueprintEventDispatcher.class);

    private final Set<BlueprintListener> listeners = new CopyOnWriteArraySet<BlueprintListener>();
    private final Map<Bundle, BlueprintEvent> states = new ConcurrentHashMap<Bundle, BlueprintEvent>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ExecutorService sharedExecutor;
    private final EventAdminListener eventAdminListener;
    private final ServiceTracker containerListenerTracker;

    BlueprintEventDispatcher(final BundleContext bundleContext, ExecutorService sharedExecutor) {

        assert bundleContext != null;
        assert sharedExecutor != null;

        this.sharedExecutor = sharedExecutor;

        bundleContext.addBundleListener(this);

        EventAdminListener listener = null;
        try {
            getClass().getClassLoader().loadClass("org.osgi.service.event.EventAdmin");
            listener = new EventAdminListener(bundleContext);
        } catch (Throwable t) {
            // Ignore, if the EventAdmin package is not available, just don't use it
            LOGGER.debug("EventAdmin package is not available, just don't use it");
        }
        this.eventAdminListener = listener;

        this.containerListenerTracker = new ServiceTracker(bundleContext, BlueprintListener.class.getName(), new ServiceTrackerCustomizer() {
            public Object addingService(ServiceReference reference) {
                BlueprintListener listener = (BlueprintListener) bundleContext.getService(reference);

                synchronized (listeners) {
                    sendInitialEvents(listener);
                    listeners.add(listener);
                }

                return listener;
            }

            public void modifiedService(ServiceReference reference, Object service) {
            }

            public void removedService(ServiceReference reference, Object service) {
                listeners.remove(service);
                bundleContext.ungetService(reference);
            }
        });
        this.containerListenerTracker.open();
    }

    private void sendInitialEvents(BlueprintListener listener) {
        for (Map.Entry<Bundle, BlueprintEvent> entry : states.entrySet()) {
            try {
                callListener(listener, new BlueprintEvent(entry.getValue(), true));
            } catch (RejectedExecutionException ree) {
                LOGGER.warn("Executor shut down", ree);
                break;
            }
        }
    }

    public void blueprintEvent(final BlueprintEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending blueprint container event {} for bundle {}", toString(event), event.getBundle().getSymbolicName());
        }

        synchronized (listeners) {
            callListeners(event);
            states.put(event.getBundle(), event);
        }

        if (eventAdminListener != null) {
            try {
                sharedExecutor.submit(new Runnable() {
                    public void run() {
                        eventAdminListener.blueprintEvent(event);
                    }
                });
            } catch (RejectedExecutionException ree) {
                LOGGER.warn("Executor shut down", ree);
            }
        }
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    private static String toString(BlueprintEvent event) {
        return "BlueprintEvent[type=" + getEventType(event.getType())
                + (event.getDependencies() != null ? ", dependencies=" + Arrays.asList(event.getDependencies()) : "")
                + (event.getCause() != null ? ", exception=" + event.getCause().getMessage() : "")
                + "]";
    }

    private static String getEventType(int type) {
        switch (type) {
            case BlueprintEvent.CREATING:
                return "CREATING";
            case BlueprintEvent.CREATED:
                return "CREATED";
            case BlueprintEvent.DESTROYING:
                return "DESTROYING";
            case BlueprintEvent.DESTROYED:
                return "DESTROYED";
            case BlueprintEvent.FAILURE:
                return "FAILURE";
            case BlueprintEvent.GRACE_PERIOD:
                return "GRACE_PERIOD";
            case BlueprintEvent.WAITING:
                return "WAITING";
            default:
                return "UNKNOWN";
        }
    }

    private void callListeners(BlueprintEvent event) {
        for (final BlueprintListener listener : listeners) {
            try {
                callListener(listener, event);
            } catch (RejectedExecutionException ree) {
                LOGGER.warn("Executor shut down", ree);
                break;
            }
        }
    }

    private void callListener(final BlueprintListener listener, final BlueprintEvent event) throws RejectedExecutionException {
        try {
            executor.invokeAny(Collections.<Callable<Void>>singleton(new Callable<Void>() {
                public Void call() throws Exception {
                    listener.blueprintEvent(event);
                    return null;
                }
            }), 60L, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            LOGGER.warn("Thread interrupted", ie);
            Thread.currentThread().interrupt();
        } catch (TimeoutException te) {
            LOGGER.warn("Listener timed out, will be ignored", te);
            listeners.remove(listener);
        } catch (ExecutionException ee) {
            LOGGER.warn("Listener caused an exception, will be ignored", ee);
            listeners.remove(listener);
        }
    }

    void destroy() {
        executor.shutdown();
        // wait for the queued tasks to execute
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
        containerListenerTracker.close();
        // clean up the EventAdmin tracker if we're using that
        if (eventAdminListener != null) {
            eventAdminListener.destroy();
        }
    }

    public void bundleChanged(BundleEvent event) {
        if (BundleEvent.STOPPING == event.getType()) {
            states.remove(event.getBundle());
        }
    }

    private static class EventAdminListener implements BlueprintListener {

        private final ServiceTracker tracker;

        EventAdminListener(BundleContext context) {
            tracker = new ServiceTracker(context, EventAdmin.class.getName(), null);
            tracker.open();
        }

        @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
        public void blueprintEvent(BlueprintEvent event) {
            EventAdmin eventAdmin = (EventAdmin) tracker.getService();
            if (eventAdmin == null) {
                return;
            }

            Dictionary<String, Object> props = new Hashtable<String, Object>();
            props.put(EventConstants.TYPE, event.getType());
            props.put(EventConstants.EVENT, event);
            props.put(EventConstants.TIMESTAMP, event.getTimestamp());
            props.put(EventConstants.BUNDLE, event.getBundle());
            props.put(EventConstants.BUNDLE_SYMBOLICNAME, event.getBundle().getSymbolicName());
            props.put(EventConstants.BUNDLE_ID, event.getBundle().getBundleId());
            props.put(EventConstants.BUNDLE_VERSION, JavaUtils.getBundleVersion(event.getBundle()));
            props.put(EventConstants.EXTENDER_BUNDLE, event.getExtenderBundle());
            props.put(EventConstants.EXTENDER_BUNDLE_ID, event.getExtenderBundle().getBundleId());
            props.put(EventConstants.EXTENDER_BUNDLE_SYMBOLICNAME, event.getExtenderBundle().getSymbolicName());
            props.put(EventConstants.EXTENDER_BUNDLE_VERSION, JavaUtils.getBundleVersion(event.getExtenderBundle()));

            if (event.getCause() != null) {
                props.put(EventConstants.CAUSE, event.getCause());
            }
            if (event.getDependencies() != null) {
                props.put(EventConstants.DEPENDENCIES, event.getDependencies());
            }
            String topic;
            switch (event.getType()) {
                case BlueprintEvent.CREATING:
                    topic = EventConstants.TOPIC_CREATING;
                    break;
                case BlueprintEvent.CREATED:
                    topic = EventConstants.TOPIC_CREATED;
                    break;
                case BlueprintEvent.DESTROYING:
                    topic = EventConstants.TOPIC_DESTROYING;
                    break;
                case BlueprintEvent.DESTROYED:
                    topic = EventConstants.TOPIC_DESTROYED;
                    break;
                case BlueprintEvent.FAILURE:
                    topic = EventConstants.TOPIC_FAILURE;
                    break;
                case BlueprintEvent.GRACE_PERIOD:
                    topic = EventConstants.TOPIC_GRACE_PERIOD;
                    break;
                case BlueprintEvent.WAITING:
                    topic = EventConstants.TOPIC_WAITING;
                    break;
                default:
                    throw new IllegalStateException("Unknown blueprint event type: " + event.getType());
            }
            eventAdmin.postEvent(new Event(topic, props));
        }

        /**
         * Perform cleanup at Blueprint extender shutdown.
         */
        public void destroy() {
            tracker.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1266.java