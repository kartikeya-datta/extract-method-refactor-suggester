error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3810.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3810.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3810.java
text:
```scala
n@@ew StopAction(subsystems.getRootSubsystem(), subsystems.getRootSubsystem(), true).run();

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.aries.subsystem.core.internal;

import static org.apache.aries.application.utils.AppConstants.LOG_ENTRY;
import static org.apache.aries.application.utils.AppConstants.LOG_EXIT;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;

import org.eclipse.equinox.region.RegionDigraph;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.bundle.EventHook;
import org.osgi.framework.hooks.resolver.ResolverHookFactory;
import org.osgi.service.coordinator.Coordinator;
import org.osgi.service.repository.Repository;
import org.osgi.service.resolver.Resolver;
import org.osgi.service.subsystem.SubsystemException;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bundle activator for the this bundle. When the bundle is starting, this
 * activator will create and register the SubsystemAdmin service.
 */
public class Activator implements BundleActivator, ServiceTrackerCustomizer<Object, Object> {
	private static final Logger logger = LoggerFactory.getLogger(Activator.class);
	
	private static Activator instance;
	
	public static synchronized Activator getInstance() {
		logger.debug(LOG_ENTRY, "getInstance");
		checkInstance();
		logger.debug(LOG_EXIT, "getInstance", instance);
		return instance;
	}
	
	private static synchronized void checkInstance() {
		logger.debug(LOG_ENTRY, "checkInstance");
		if (instance == null)
			throw new IllegalStateException("The activator has not been initialized or has been shutdown");
		logger.debug(LOG_EXIT, "checkInstance");
	}
	
	private BundleContext bundleContext;
	private volatile Coordinator coordinator;
	private volatile SubsystemServiceRegistrar registrar;
	private volatile RegionDigraph regionDigraph;
	private volatile Resolver resolver;
	private ServiceTracker<?,?> serviceTracker;
	private volatile Subsystems subsystems;
	
	private final Collection<ServiceRegistration<?>> registrations = new HashSet<ServiceRegistration<?>>();
	private final Collection<Repository> repositories = Collections.synchronizedSet(new HashSet<Repository>());
	
	public BundleContext getBundleContext() {
		return bundleContext;
	}
	
	public Coordinator getCoordinator() {
		return coordinator;
	}
	
	public RegionDigraph getRegionDigraph() {
		return regionDigraph;
	}
	
	public Collection<Repository> getRepositories() {
		return Collections.unmodifiableCollection(repositories);
	}
	
	public Resolver getResolver() {
		return resolver;
	}
	
	public Subsystems getSubsystems() {
		return subsystems;
	}
	
	public SubsystemServiceRegistrar getSubsystemServiceRegistrar() {
		logger.debug(LOG_ENTRY, "getSubsystemServiceRegistrar");
		SubsystemServiceRegistrar result = registrar;
		logger.debug(LOG_EXIT, "getSubsystemServiceRegistrar", result);
		return result;
	}
	
	public Repository getSystemRepository() {
		return new SystemRepository(subsystems.getRootSubsystem());
	}

	@Override
	public synchronized void start(BundleContext context) throws Exception {
		logger.debug(LOG_ENTRY, "start", context);
		bundleContext = context;
		serviceTracker = new ServiceTracker<Object, Object>(bundleContext, generateServiceFilter(), this);
		serviceTracker.open();
		logger.debug(LOG_EXIT, "start");
	}

	@Override
	public synchronized void stop(BundleContext context) {
		logger.debug(LOG_ENTRY, "stop", context);
		serviceTracker.close();
		serviceTracker = null;
		bundleContext = null;
		logger.debug(LOG_EXIT, "stop");
	}
	
	private void activate() {
		if (isActive() || !hasRequiredServices())
			return;
		synchronized (Activator.class) {
			instance = Activator.this;
		}
		registerBundleEventHook();
		registrations.add(bundleContext.registerService(ResolverHookFactory.class, new SubsystemResolverHookFactory(), null));
		registrar = new SubsystemServiceRegistrar(bundleContext);
		try {
			subsystems = new Subsystems();
		}
		catch (SubsystemException e) {
			throw e;
		}
		catch (Exception e) {
			throw new SubsystemException(e);
		}
		AriesSubsystem root = subsystems.getRootSubsystem();
		root.start();
	}
	
	private void deactivate() {
		if (!isActive())
			return;
		new StopAction(subsystems.getRootSubsystem(), true, false).run();
		for (ServiceRegistration<?> registration : registrations) {
			try {
				registration.unregister();
			}
			catch (IllegalStateException e) {
				logger.debug("Service had already been unregistered", e);
			}
		}
		synchronized (Activator.class) {
			instance = null;
		}
	}
	
	private Object findAlternateServiceFor(Object service) {
		Object[] services = serviceTracker.getServices();
		if (services == null)
			return null;
		for (Object alternate : services)
			if (alternate.getClass().equals(service.getClass()))
					return alternate;
		return null;
	}
	
	private Filter generateServiceFilter() throws InvalidSyntaxException {
		return FrameworkUtil.createFilter(generateServiceFilterString());
	}
	
	private String generateServiceFilterString() {
		return new StringBuilder("(|(")
				.append(org.osgi.framework.Constants.OBJECTCLASS).append('=')
				.append(Coordinator.class.getName()).append(")(")
				.append(org.osgi.framework.Constants.OBJECTCLASS).append('=')
				.append(RegionDigraph.class.getName()).append(")(")
				.append(org.osgi.framework.Constants.OBJECTCLASS).append('=')
				.append(Resolver.class.getName()).append(")(")
				.append(org.osgi.framework.Constants.OBJECTCLASS).append('=')
				.append(Repository.class.getName()).append("))").toString();
	}
	
	private boolean hasRequiredServices() {
		return coordinator != null &&
				regionDigraph != null &&
				resolver != null;
	}
	
	private boolean isActive() {
		synchronized (Activator.class) {
			return instance != null && subsystems != null;
		}
	}
	
	private void registerBundleEventHook() {
		Dictionary<String, Object> properties = new Hashtable<String, Object>(1);
		properties.put(org.osgi.framework.Constants.SERVICE_RANKING, Integer.MIN_VALUE);
		registrations.add(bundleContext.registerService(EventHook.class, new BundleEventHook(), properties));
	}
	
	/* Begin ServiceTrackerCustomizer methods */

	@Override
	public synchronized Object addingService(ServiceReference<Object> reference) {
		Object service = bundleContext.getService(reference);
		if (service instanceof Coordinator) {
			if (coordinator == null) {
				coordinator = (Coordinator)service;
				activate();
			}
		}
		else if (service instanceof RegionDigraph) {
			if (regionDigraph == null) {
				regionDigraph = (RegionDigraph)service;
				activate();
			}
		}
		else if (service instanceof Resolver) {
			if (resolver == null) {
				resolver = (Resolver)service;
				activate();
			}
		}
		else
			repositories.add((Repository)service);
		return service;
	}

	@Override
	public void modifiedService(ServiceReference<Object> reference, Object service) {
		// Nothing
	}

	@Override
	public synchronized void removedService(ServiceReference<Object> reference, Object service) {
		if (service instanceof Coordinator) {
			if (service.equals(coordinator)) {
				Coordinator coordinator = (Coordinator)findAlternateServiceFor(this.coordinator);
				if (coordinator == null)
					deactivate();
				this.coordinator = coordinator;
			}
		}
		else if (service instanceof RegionDigraph) {
			if (service.equals(regionDigraph)) {
				RegionDigraph regionDigraph = (RegionDigraph)findAlternateServiceFor(this.regionDigraph);
				if (regionDigraph == null)
					deactivate();
				this.regionDigraph = regionDigraph;
			}
		}
		else if (service instanceof Resolver) {
			if (service.equals(resolver)) {
				Resolver resolver = (Resolver)findAlternateServiceFor(this.resolver);
				if (resolver == null)
					deactivate();
				this.resolver = resolver;
			}
		}
		else
			repositories.remove(service);
	}
	
	/* End ServiceTrackerCustomizer methods */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3810.java