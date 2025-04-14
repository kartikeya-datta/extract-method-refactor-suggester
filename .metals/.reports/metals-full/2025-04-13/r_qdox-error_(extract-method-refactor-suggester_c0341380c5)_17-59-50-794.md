error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9094.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9094.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9094.java
text:
```scala
+ "." + C@@ONTAINER_FACTORY_NAME;

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.core;

import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.provider.IContainerInstantiator;
import org.eclipse.ecf.core.start.ECFStartJob;
import org.eclipse.ecf.core.start.IECFStart;
import org.eclipse.ecf.core.util.Trace;
import org.osgi.framework.BundleContext;

public class ECFPlugin extends Plugin {

	public static final String PLUGIN_ID = "org.eclipse.ecf";

	private static final String ECFNAMESPACE = PLUGIN_ID;

	private static final String CONTAINER_FACTORY_NAME = "containerFactory";

	private static final String CONTAINER_FACTORY_EPOINT = ECFNAMESPACE
			+ CONTAINER_FACTORY_NAME;

	private static final String STARTUP_NAME = "startup";

	public static final String START_EPOINT = ECFNAMESPACE + "." + STARTUP_NAME;

	public static final String PLUGIN_RESOURCE_BUNDLE = ECFNAMESPACE
			+ ".ECFPluginResources";

	public static final String CLASS_ATTRIBUTE = "class";

	public static final String NAME_ATTRIBUTE = "name";

	public static final String DESCRIPTION_ATTRIBUTE = "description";

	public static final String ARG_ELEMENT_NAME = "defaultargument";

	public static final String VALUE_ATTRIBUTE = "value";

	public static final String PROPERTY_ELEMENT_NAME = "property";

	public static final int FACTORY_DOES_NOT_IMPLEMENT_ERRORCODE = 10;

	public static final int FACTORY_NAME_COLLISION_ERRORCODE = 20;

	public static final int INSTANTIATOR_DOES_NOT_IMPLEMENT_ERRORCODE = 30;

	public static final int INSTANTIATOR_NAME_COLLISION_ERRORCODE = 50;

	public static final int INSTANTIATOR_NAMESPACE_LOAD_ERRORCODE = 60;

	public static final int START_ERRORCODE = 70;

	protected static final int REMOVE_NAMESPACE_ERRORCODE = 80;

	// The shared instance.
	private static ECFPlugin plugin;

	// Resource bundle.
	private ResourceBundle resourceBundle;

	BundleContext bundlecontext = null;

	private Map disposables = new WeakHashMap();

	private IRegistryChangeListener registryManager = null;

	public ECFPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle(PLUGIN_RESOURCE_BUNDLE);
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	public void addDisposable(IDisposable disposable) {
		disposables.put(disposable, null);
	}

	public void removeDisposable(IDisposable disposable) {
		disposables.remove(disposable);
	}

	protected void fireDisposables() {
		for (Iterator i = disposables.keySet().iterator(); i.hasNext();) {
			IDisposable d = (IDisposable) i.next();
			if (d != null)
				d.dispose();
		}
	}

	public static void log(IStatus status) {
		if (status == null)
			return;
		ILog log = plugin.getLog();
		if (log != null)
			log.log(status);
		else
			System.err.println("No log output.  Status Message: "
					+ status.getMessage());
	}

	protected String[] getDefaultArgs(IConfigurationElement[] argElements) {
		String[] argDefaults = new String[0];
		if (argElements != null) {
			if (argElements.length > 0) {
				argDefaults = new String[argElements.length];
				for (int i = 0; i < argElements.length; i++)
					argDefaults[i] = argElements[i]
							.getAttribute(VALUE_ATTRIBUTE);
			}
		}
		return argDefaults;
	}

	protected Map getProperties(IConfigurationElement[] propertyElements) {
		Properties props = new Properties();
		if (propertyElements != null) {
			if (propertyElements.length > 0) {
				for (int i = 0; i < propertyElements.length; i++) {
					String name = propertyElements[i]
							.getAttribute(NAME_ATTRIBUTE);
					String value = propertyElements[i]
							.getAttribute(VALUE_ATTRIBUTE);
					if (name != null && !name.equals("") && value != null
							&& !value.equals("")) {
						props.setProperty(name, value);
					}
				}
			}
		}
		return props;
	}

	protected void logException(IStatus status, String method,
			Throwable exception) {
		log(status);
		Trace.catching(ECFPlugin.getDefault(),
				ECFDebugOptions.EXCEPTIONS_CATCHING, ECFPlugin.class, method,
				exception);
	}

	/**
	 * Remove extensions for container factory extension point
	 * 
	 * @param members
	 *            the members to remove
	 */
	protected void removeContainerFactoryExtensions(
			IConfigurationElement[] members) {
		String method = "removeContainerFactoryExtensions";
		Trace.entering(ECFPlugin.getDefault(),
				ECFDebugOptions.METHODS_ENTERING, ECFPlugin.class, method,
				members);
		// For each configuration element
		for (int m = 0; m < members.length; m++) {
			IConfigurationElement member = members[m];
			// Get the label of the extender plugin and the ID of the extension.
			IExtension extension = member.getDeclaringExtension();
			String name = null;
			try {
				// Get name and get version, if available
				name = member.getAttribute(NAME_ATTRIBUTE);
				if (name == null) {
					name = member.getAttribute(CLASS_ATTRIBUTE);
				}
				IContainerFactory factory = ContainerFactory.getDefault();
				ContainerTypeDescription cd = factory
						.getDescriptionByName(name);
				if (cd == null || !factory.containsDescription(cd)) {
					continue;
				}
				// remove
				factory.removeDescription(cd);
				Trace.trace(ECFPlugin.getDefault(), ECFDebugOptions.DEBUG,
						method + ".removed " + cd + " from factory");
			} catch (Exception e) {
				logException(
						new Status(
								Status.ERROR,
								getDefault().getBundle().getSymbolicName(),
								FACTORY_NAME_COLLISION_ERRORCODE,
								getResourceString("ExtPointError.ContainerNameCollisionPrefix")
										+ name
										+ getResourceString("ExtPointError.ContainerNameCollisionSuffix")
										+ extension
												.getExtensionPointUniqueIdentifier(),
								null), method, e);
			}
		}
	}

	/**
	 * Add container factory extension point extensions
	 * 
	 * @param members
	 *            to add
	 */
	protected void addContainerFactoryExtensions(IConfigurationElement[] members) {
		String method = "addContainerFactoryExtensions";
		Trace.entering(ECFPlugin.getDefault(),
				ECFDebugOptions.METHODS_ENTERING, ECFPlugin.class, method,
				members);
		// For each configuration element
		for (int m = 0; m < members.length; m++) {
			IConfigurationElement member = members[m];
			// Get the label of the extender plugin and the ID of the extension.
			IExtension extension = member.getDeclaringExtension();
			Object exten = null;
			String name = null;
			try {
				// The only required attribute is "class"
				exten = member.createExecutableExtension(CLASS_ATTRIBUTE);
				String clazz = exten.getClass().getName();
				// Get name and get version, if available
				name = member.getAttribute(NAME_ATTRIBUTE);
				if (name == null) {
					name = clazz;
				}
				// Get description, if present
				String description = member.getAttribute(DESCRIPTION_ATTRIBUTE);
				if (description == null) {
					description = "";
				}
				// Get any arguments
				String[] defaults = getDefaultArgs(member
						.getChildren(ARG_ELEMENT_NAME));
				// Get any property elements
				Map properties = getProperties(member
						.getChildren(PROPERTY_ELEMENT_NAME));
				// Now make description instance
				ContainerTypeDescription scd = new ContainerTypeDescription(
						name, (IContainerInstantiator) exten, description,
						defaults, properties);
				IContainerFactory factory = ContainerFactory.getDefault();
				if (factory.containsDescription(scd)) {
					throw new CoreException(
							new Status(
									Status.ERROR,
									getDefault().getBundle().getSymbolicName(),
									FACTORY_NAME_COLLISION_ERRORCODE,
									getResourceString("ExtPointError.ContainerNameCollisionPrefix")
											+ name
											+ getResourceString("ExtPointError.ContainerNameCollisionSuffix")
											+ extension
													.getExtensionPointUniqueIdentifier(),
									null));
				}
				// Now add the description and we're ready to go.
				factory.addDescription(scd);
				Trace.trace(ECFPlugin.getDefault(), ECFDebugOptions.DEBUG,
						method + ".added " + scd + " to factory " + factory);
			} catch (CoreException e) {
				logException(e.getStatus(), method, e);
			} catch (Exception e) {
				logException(
						new Status(
								Status.ERROR,
								getDefault().getBundle().getSymbolicName(),
								FACTORY_NAME_COLLISION_ERRORCODE,
								getResourceString("ExtPointError.ContainerNameCollisionPrefix")
										+ name
										+ getResourceString("ExtPointError.ContainerNameCollisionSuffix")
										+ extension
												.getExtensionPointUniqueIdentifier(),
								null), method, e);
			}
		}
	}

	/**
	 * Setup container factory extension point
	 * 
	 * @param context
	 *            the BundleContext for this bundle
	 */
	protected void setupContainerFactoryExtensionPoint(BundleContext bc) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = reg
				.getExtensionPoint(CONTAINER_FACTORY_EPOINT);
		if (extensionPoint == null) {
			return;
		}
		addContainerFactoryExtensions(extensionPoint.getConfigurationElements());
	}

	/**
	 * Setup start extension point
	 * 
	 * @param bc
	 *            the BundleContext fro this bundle
	 */
	protected void setupStartExtensionPoint(BundleContext bc) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = reg.getExtensionPoint(START_EPOINT);
		if (extensionPoint == null) {
			return;
		}
		runStartExtensions(extensionPoint.getConfigurationElements());
	}

	protected void runStartExtensions(
			IConfigurationElement[] configurationElements) {
		String method = "runStartExtensions";
		// For each configuration element
		for (int m = 0; m < configurationElements.length; m++) {
			IConfigurationElement member = configurationElements[m];
			IECFStart exten = null;
			String name = null;
			try {
				// The only required attribute is "class"
				exten = (IECFStart) member
						.createExecutableExtension(CLASS_ATTRIBUTE);
				// Get name and get version, if available
				name = member.getAttribute(NAME_ATTRIBUTE);
				if (name == null)
					name = exten.getClass().getName();
				startExtension(name, exten);
			} catch (CoreException e) {
				logException(e.getStatus(), method, e);
			} catch (Exception e) {
				logException(new Status(Status.ERROR, getDefault().getBundle()
						.getSymbolicName(), START_ERRORCODE,
						"Unknown start exception", e), method, e);
			}
		}
	}

	private void startExtension(String name, IECFStart exten) {
		// Create job to do start, and schedule
		ECFStartJob job = new ECFStartJob(name, exten);
		job.schedule();
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.bundlecontext = context;
		this.registryManager = new ECFRegistryManager();
		Platform.getExtensionRegistry().addRegistryChangeListener(
				registryManager);
		setupContainerFactoryExtensionPoint(context);
		setupStartExtensionPoint(context);
	}

	protected class ECFRegistryManager implements IRegistryChangeListener {
		public void registryChanged(IRegistryChangeEvent event) {
			IExtensionDelta delta[] = event.getExtensionDeltas(ECFNAMESPACE,
					CONTAINER_FACTORY_NAME);
			for (int i = 0; i < delta.length; i++) {
				switch (delta[i].getKind()) {
				case IExtensionDelta.ADDED:
					addContainerFactoryExtensions(delta[i].getExtension()
							.getConfigurationElements());
					break;
				case IExtensionDelta.REMOVED:
					removeContainerFactoryExtensions(delta[i].getExtension()
							.getConfigurationElements());
					break;
				}
			}
		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		fireDisposables();
		this.disposables = null;
		this.bundlecontext = null;
		Platform.getExtensionRegistry().removeRegistryChangeListener(
				registryManager);
		this.registryManager = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ECFPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ECFPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : "!" + key + "!";
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9094.java