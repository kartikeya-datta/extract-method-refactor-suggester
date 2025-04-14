error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/484.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/484.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/484.java
text:
```scala
g@@etDefault().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, REMOVE_SHAREDOBJECT_ERRORCODE, "Exception removing sharedobject extension", e)); //$NON-NLS-1$

package org.eclipse.ecf.internal.core.sharedobject;

import java.util.Map;
import java.util.Properties;
import org.eclipse.core.runtime.*;
import org.eclipse.ecf.core.sharedobject.*;
import org.eclipse.ecf.core.sharedobject.provider.ISharedObjectInstantiator;
import org.eclipse.ecf.core.util.*;
import org.osgi.framework.*;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.ecf.sharedobject"; //$NON-NLS-1$

	protected static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	protected static final String NAME_ATTRIBUTE = "name"; //$NON-NLS-1$

	protected static final String PROPERTY_ELEMENT_NAME = "property"; //$NON-NLS-1$

	protected static final String VALUE_ATTRIBUTE = "value"; //$NON-NLS-1$

	protected static final String NAMESPACE_NAME = "sharedObjectFactory"; //$NON-NLS-1$

	protected static final String SHAREDOBJECT_FACTORY_EPOINT = PLUGIN_ID + "." //$NON-NLS-1$
			+ NAMESPACE_NAME;

	protected static final String DESCRIPTION_ATTRIBUTE = "description"; //$NON-NLS-1$

	private static final int REMOVE_SHAREDOBJECT_ERRORCODE = 1001;

	private static final int FACTORY_NAME_COLLISION_ERRORCODE = 2001;

	// The shared instance
	private static Activator plugin;

	private BundleContext context = null;

	private IRegistryChangeListener registryManager = null;

	private ServiceTracker extensionRegistryTracker = null;

	private ServiceTracker logServiceTracker = null;

	private ServiceTracker adapterManagerTracker = null;

	/**
	 * The constructor
	 */
	public Activator() {
		// null constructor
	}

	public IExtensionRegistry getExtensionRegistry() {
		return (IExtensionRegistry) extensionRegistryTracker.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext ctxt) throws Exception {
		this.context = ctxt;
		plugin = this;
		this.extensionRegistryTracker = new ServiceTracker(ctxt, IExtensionRegistry.class.getName(), null);
		this.extensionRegistryTracker.open();
		IExtensionRegistry registry = getExtensionRegistry();
		if (registry != null) {
			this.registryManager = new SharedObjectRegistryManager();
			registry.addRegistryChangeListener(registryManager);
		}
		setupSharedObjectExtensionPoint(ctxt);
		Trace.exiting(Activator.PLUGIN_ID, SharedObjectDebugOptions.METHODS_ENTERING, Activator.class, "start"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext ctxt) throws Exception {
		Trace.entering(Activator.PLUGIN_ID, SharedObjectDebugOptions.METHODS_EXITING, Activator.class, "stop"); //$NON-NLS-1$
		IExtensionRegistry reg = getExtensionRegistry();
		if (reg != null)
			reg.removeRegistryChangeListener(registryManager);
		this.registryManager = null;
		if (extensionRegistryTracker != null) {
			extensionRegistryTracker.close();
			extensionRegistryTracker = null;
		}
		if (adapterManagerTracker != null) {
			adapterManagerTracker.close();
			adapterManagerTracker = null;
		}
		plugin = null;
		this.context = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public Bundle getBundle() {
		if (context == null)
			return null;
		return context.getBundle();
	}

	protected LogService getLogService() {
		if (logServiceTracker == null) {
			logServiceTracker = new ServiceTracker(this.context, LogService.class.getName(), null);
			logServiceTracker.open();
		}
		return (LogService) logServiceTracker.getService();
	}

	public void log(IStatus status) {
		LogService logService = getLogService();
		if (logService != null) {
			logService.log(LogHelper.getLogCode(status), LogHelper.getLogMessage(status), status.getException());
		}
	}

	public IAdapterManager getAdapterManager() {
		// First, try to get the adapter manager via
		if (adapterManagerTracker == null) {
			adapterManagerTracker = new ServiceTracker(this.context, IAdapterManager.class.getName(), null);
			adapterManagerTracker.open();
		}
		IAdapterManager adapterManager = (IAdapterManager) adapterManagerTracker.getService();
		// Then, if the service isn't there, try to get from Platform class via
		// PlatformHelper class
		if (adapterManager == null)
			adapterManager = PlatformHelper.getPlatformAdapterManager();
		if (adapterManager == null)
			getDefault().log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, "Cannot get adapter manager", null)); //$NON-NLS-1$
		return adapterManager;
	}

	/**
	 * Remove extensions for shared object extension point
	 * 
	 * @param members
	 *            the members to remove
	 */
	protected void removeSharedObjectExtensions(IConfigurationElement[] members) {
		for (int m = 0; m < members.length; m++) {
			IConfigurationElement member = members[m];
			String name = null;
			try {
				name = member.getAttribute(NAME_ATTRIBUTE);
				if (name == null) {
					name = member.getAttribute(CLASS_ATTRIBUTE);
				}
				if (name == null)
					continue;
				ISharedObjectFactory factory = SharedObjectFactory.getDefault();
				SharedObjectTypeDescription sd = factory.getDescriptionByName(name);
				if (sd == null || !factory.containsDescription(sd)) {
					continue;
				}
				// remove
				factory.removeDescription(sd);
				org.eclipse.ecf.core.util.Trace.trace(Activator.PLUGIN_ID, SharedObjectDebugOptions.DEBUG, "removeSharedObjectExtensions.removedDescription(" + sd //$NON-NLS-1$
						+ ")"); //$NON-NLS-1$
			} catch (Exception e) {
				org.eclipse.ecf.core.util.Trace.catching(Activator.PLUGIN_ID, SharedObjectDebugOptions.EXCEPTIONS_CATCHING, Activator.class, "removeSharedObjectExtensions", e); //$NON-NLS-1$
				getDefault().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, REMOVE_SHAREDOBJECT_ERRORCODE, Messages.Activator_Exception_Removing_Extension, e));
			}
		}
	}

	/**
	 * Add shared object extension point extensions
	 * 
	 * @param members
	 *            to add
	 */
	protected void addSharedObjectExtensions(IConfigurationElement[] members) {
		String bundleName = getDefault().getBundle().getSymbolicName();
		// For each configuration element
		for (int m = 0; m < members.length; m++) {
			IConfigurationElement member = members[m];
			// Get the label of the extender plugin and the ID of the extension.
			IExtension extension = member.getDeclaringExtension();
			ISharedObjectInstantiator exten = null;
			String name = null;
			try {
				// The only required attribute is "class"
				exten = (ISharedObjectInstantiator) member.createExecutableExtension(CLASS_ATTRIBUTE);
				name = member.getAttribute(NAME_ATTRIBUTE);
				if (name == null) {
					name = member.getAttribute(CLASS_ATTRIBUTE);
				}
				// Get description, if present
				String description = member.getAttribute(DESCRIPTION_ATTRIBUTE);
				if (description == null) {
					description = ""; //$NON-NLS-1$
				}
				// Get any property elements
				Map properties = getProperties(member.getChildren(PROPERTY_ELEMENT_NAME));
				// Now make description instance
				SharedObjectTypeDescription scd = new SharedObjectTypeDescription(name, exten, description, properties);
				org.eclipse.ecf.core.util.Trace.trace(Activator.PLUGIN_ID, SharedObjectDebugOptions.DEBUG, "setupSharedObjectExtensionPoint:createdDescription(" //$NON-NLS-1$
						+ scd + ")"); //$NON-NLS-1$
				ISharedObjectFactory factory = SharedObjectFactory.getDefault();
				if (factory.containsDescription(scd))
					throw new CoreException(new Status(IStatus.ERROR, bundleName, FACTORY_NAME_COLLISION_ERRORCODE, "name=" //$NON-NLS-1$
							+ name + ";extension point id=" //$NON-NLS-1$
							+ extension.getExtensionPointUniqueIdentifier(), null));

				// Now add the description and we're ready to go.
				factory.addDescription(scd);
				org.eclipse.ecf.core.util.Trace.trace(Activator.PLUGIN_ID, SharedObjectDebugOptions.DEBUG, "setupSharedObjectExtensionPoint.addedDescriptionToFactory(" //$NON-NLS-1$
						+ scd + ")"); //$NON-NLS-1$
			} catch (CoreException e) {
				getDefault().log(e.getStatus());
				org.eclipse.ecf.core.util.Trace.catching(Activator.PLUGIN_ID, SharedObjectDebugOptions.EXCEPTIONS_CATCHING, Activator.class, "addSharedObjectExtensions", e); //$NON-NLS-1$
			} catch (Exception e) {
				getDefault().log(new Status(IStatus.ERROR, bundleName, FACTORY_NAME_COLLISION_ERRORCODE, "name=" //$NON-NLS-1$
						+ name + ";extension point id=" //$NON-NLS-1$
						+ extension.getExtensionPointUniqueIdentifier(), null));
				org.eclipse.ecf.core.util.Trace.catching(Activator.PLUGIN_ID, SharedObjectDebugOptions.EXCEPTIONS_CATCHING, Activator.class, "addSharedObjectExtensions", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Setup shared object extension point
	 * 
	 * @param bc
	 *            the BundleContext for this bundle
	 */
	protected void setupSharedObjectExtensionPoint(BundleContext bc) {
		IExtensionRegistry reg = getExtensionRegistry();
		if (reg != null) {
			IExtensionPoint extensionPoint = reg.getExtensionPoint(SHAREDOBJECT_FACTORY_EPOINT);
			if (extensionPoint == null) {
				return;
			}
			addSharedObjectExtensions(extensionPoint.getConfigurationElements());
		}
	}

	protected Map getProperties(IConfigurationElement[] propertyElements) {
		Properties props = new Properties();
		if (propertyElements != null) {
			if (propertyElements.length > 0) {
				for (int i = 0; i < propertyElements.length; i++) {
					String name = propertyElements[i].getAttribute(NAME_ATTRIBUTE);
					String value = propertyElements[i].getAttribute(VALUE_ATTRIBUTE);
					if (name != null && !name.equals("") && value != null //$NON-NLS-1$
							&& !value.equals("")) { //$NON-NLS-1$
						props.setProperty(name, value);
					}
				}
			}
		}
		return props;
	}

	protected class SharedObjectRegistryManager implements IRegistryChangeListener {
		public void registryChanged(IRegistryChangeEvent event) {
			IExtensionDelta delta[] = event.getExtensionDeltas(PLUGIN_ID, NAMESPACE_NAME);
			for (int i = 0; i < delta.length; i++) {
				switch (delta[i].getKind()) {
					case IExtensionDelta.ADDED :
						addSharedObjectExtensions(delta[i].getExtension().getConfigurationElements());
						break;
					case IExtensionDelta.REMOVED :
						removeSharedObjectExtensions(delta[i].getExtension().getConfigurationElements());
						break;
				}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/484.java