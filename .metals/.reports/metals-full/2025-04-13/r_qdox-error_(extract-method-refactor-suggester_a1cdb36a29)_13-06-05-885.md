error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/369.java
text:
```scala
B@@undle.INSTALLED | Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE, new ConsumerBundleTrackerCustomizer(this, consumerHeaderName));

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
package org.apache.aries.spifly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.ServiceTracker;

public abstract class BaseActivator implements BundleActivator {
    private static final Set<WeavingData> NON_WOVEN_BUNDLE = Collections.emptySet();

    // Static access to the activator used by the woven code, therefore
    // this bundle must be a singleton.
    // TODO see if we can get rid of the static access.
    public static BaseActivator activator;

    private BundleContext bundleContext;
    private LogServiceTracker logServiceTracker;
    private List<LogService> logServices = new CopyOnWriteArrayList<LogService>();
    private BundleTracker consumerBundleTracker;
    private BundleTracker providerBundleTracker;

    private final ConcurrentMap<Bundle, Set<WeavingData>> bundleWeavingData =
        new ConcurrentHashMap<Bundle, Set<WeavingData>>();

    private final ConcurrentMap<String, SortedMap<Long, Bundle>> registeredProviders =
            new ConcurrentHashMap<String, SortedMap<Long, Bundle>>();

    private final ConcurrentMap<Bundle, Map<ConsumerRestriction, List<BundleDescriptor>>> consumerRestrictions =
            new ConcurrentHashMap<Bundle, Map<ConsumerRestriction, List<BundleDescriptor>>>();

    public synchronized void start(BundleContext context, final String consumerHeaderName) throws Exception {
        bundleContext = context;

        logServiceTracker = new LogServiceTracker(context);
        logServiceTracker.open();

        providerBundleTracker = new BundleTracker(context,
                Bundle.ACTIVE, new ProviderBundleTrackerCustomizer(this, context.getBundle()));
        providerBundleTracker.open();

        consumerBundleTracker = new BundleTracker(context,
                Bundle.INSTALLED, new ConsumerBundleTrackerCustomizer(this, consumerHeaderName));
        consumerBundleTracker.open();

        for (Bundle bundle : context.getBundles()) {
            addConsumerWeavingData(bundle, consumerHeaderName);
        }

        activator = this;
    }

    public void addConsumerWeavingData(Bundle bundle, String consumerHeaderName) {
        if (bundleWeavingData.containsKey(bundle)) {
            // This bundle was already processed
            return;
        }

        Object consumerHeader = bundle.getHeaders().get(consumerHeaderName);
        if (consumerHeader instanceof String) {
            Set<WeavingData> wd = ConsumerHeaderProcessor.processHeader((String) consumerHeader);
            bundleWeavingData.put(bundle, Collections.unmodifiableSet(wd));

            for (WeavingData w : wd) {
                registerConsumerBundle(bundle, w.getArgRestrictions(), w.getAllowedBundles());
            }
        } else {
            bundleWeavingData.put(bundle, NON_WOVEN_BUNDLE);
        }
    }

    public void removeWeavingData(Bundle bundle) {
        bundleWeavingData.remove(bundle);
    }

    @Override
    public synchronized void stop(BundleContext context) throws Exception {
        activator = null;

        consumerBundleTracker.close();
        providerBundleTracker.close();
        logServiceTracker.close();
    }

    public void log(int level, String message) {
        synchronized (logServices) {
            for (LogService log : logServices) {
                log.log(level, message);
            }
        }
    }

    public void log(int level, String message, Throwable th) {
        synchronized (logServices) {
            for (LogService log : logServices) {
                log.log(level, message, th);
            }
        }
    }

    public Set<WeavingData> getWeavingData(Bundle b) {
        // Simply return the value as it's already an unmovable set.
        Set<WeavingData> wd = bundleWeavingData.get(b);
        if (wd == null)
            return null;

        if (wd.size() == 0)
            return null;

        return wd;
    }

    public void registerProviderBundle(String registrationClassName, Bundle bundle) {
        registeredProviders.putIfAbsent(registrationClassName, Collections.synchronizedSortedMap(new TreeMap<Long, Bundle>()));
        SortedMap<Long, Bundle> map = registeredProviders.get(registrationClassName);
        map.put(bundle.getBundleId(), bundle);
    }

    public Collection<Bundle> findProviderBundles(String name) {
        SortedMap<Long, Bundle> map = registeredProviders.get(name);
        return map == null ? Collections.<Bundle>emptyList() : map.values();
    }

    // TODO unRegisterProviderBundle();
    public void registerConsumerBundle( Bundle consumerBundle,
            Set<ConsumerRestriction> restrictions, List<BundleDescriptor> allowedBundles) {
        consumerRestrictions.putIfAbsent(consumerBundle, new HashMap<ConsumerRestriction, List<BundleDescriptor>>());
        Map<ConsumerRestriction, List<BundleDescriptor>> map = consumerRestrictions.get(consumerBundle);
        for (ConsumerRestriction restriction : restrictions) {
            map.put(restriction, allowedBundles);
        }
    }

    public Collection<Bundle> findConsumerRestrictions(Bundle consumer, String className, String methodName,
            Map<Pair<Integer, String>, String> args) {
        Map<ConsumerRestriction, List<BundleDescriptor>> restrictions = consumerRestrictions.get(consumer);
        if (restrictions == null) {
            // Null means: no restrictions
            return null;
        }

        for (Map.Entry<ConsumerRestriction, List<BundleDescriptor>> entry : restrictions.entrySet()) {
            if (entry.getKey().matches(className, methodName, args)) {
                return getBundles(entry.getValue());
            }
        }

        // Empty collection: nothing matches
        return Collections.emptySet();
    }

    private Collection<Bundle> getBundles(List<BundleDescriptor> descriptors) {
        if (descriptors == null) {
            return null;
        }

        List<Bundle> bundles = new ArrayList<Bundle>();
        for (Bundle b : bundleContext.getBundles()) {
            for (BundleDescriptor desc : descriptors) {
                if (desc.getBundleID() != BundleDescriptor.BUNDLE_ID_UNSPECIFIED) {
                    if (b.getBundleId() == desc.getBundleID()) {
                        bundles.add(b);
                    }
                } else {
                    if (b.getSymbolicName().equals(desc.getSymbolicName())) {
                        if (desc.getVersion() == null || b.getVersion().equals(desc.getVersion())) {
                            bundles.add(b);
                        }
                    }
                }
            }
        }
        return bundles;
    }

    // TODO unRegisterConsumerBundle();

    private class LogServiceTracker extends ServiceTracker {
        public LogServiceTracker(BundleContext context) {
            super(context, LogService.class.getName(), null);
        }

        public Object addingService(ServiceReference reference) {
            Object svc = super.addingService(reference);
            if (svc instanceof LogService)
                logServices.add((LogService) svc);
            return svc;
        }

        @Override
        public void removedService(ServiceReference reference, Object service) {
            logServices.remove(service);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/369.java