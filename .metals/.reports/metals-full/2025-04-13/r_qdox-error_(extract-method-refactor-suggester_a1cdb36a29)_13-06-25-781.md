error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2170.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2170.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2170.java
text:
```scala
r@@eadRegistry(Platform.getPluginRegistry(), PlatformUI.PLUGIN_ID, IWorkbenchConstants.PL_MARKER_IMAGE_PROVIDER);

package org.eclipse.ui.internal.registry;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.*;
import java.util.*;
import java.net.*;

/**
 * Implementation of a marker image registry which maps either
 * a marker type to a provider or to a static image.
 */
public class MarkerImageProviderRegistry {
	private static final String ATT_PROVIDER_CLASS = "class";//$NON-NLS-1$
	private static final String ATT_ICON = "icon";//$NON-NLS-1$
	private static final String ATT_MARKER_TYPE = "markertype";//$NON-NLS-1$
	private static final String ATT_ID = "id";//$NON-NLS-1$
	private static final String MARKER_ATT_KEY = "org.eclipse.ui.internal.registry.MarkerImageProviderRegistry";//$NON-NLS-1$
	private static final String TAG_PROVIDER = "imageprovider";//$NON-NLS-1$

	private ArrayList descriptors = new ArrayList();

	class Descriptor {
		String id;
		String markerType;
		String className;
		String imagePath;
		ImageDescriptor imageDescriptor;
		IConfigurationElement element;
		IPluginDescriptor pluginDescriptor;
		IMarkerImageProvider provider;
	}
/**
 * Initialize this new MarkerImageProviderRegistry.
 */
public MarkerImageProviderRegistry() {
	class MarkerImageReader extends RegistryReader {
		protected boolean readElement(IConfigurationElement element) {
			if (element.getName().equals(TAG_PROVIDER)) {
				addProvider(element);
				return true;
			}
			
			return false;
		}
		public void readRegistry() {
		    readRegistry(Platform.getPluginRegistry(), IWorkbenchConstants.PLUGIN_ID, IWorkbenchConstants.PL_MARKER_IMAGE_PROVIDER);
		}
	}
	
	new MarkerImageReader().readRegistry();
}
/**
 * Creates a descriptor for the marker provider extension
 * and add it to the list of providers.
 */
public void addProvider(IConfigurationElement element) {
	Descriptor desc = new Descriptor();
	desc.element = element;
	desc.pluginDescriptor = element.getDeclaringExtension().getDeclaringPluginDescriptor();
	desc.id = element.getAttribute(ATT_ID);
	desc.markerType = element.getAttribute(ATT_MARKER_TYPE);
	desc.imagePath = element.getAttribute(ATT_ICON);
	desc.className = element.getAttribute(ATT_PROVIDER_CLASS);
	if(desc.imagePath != null) {
		desc.imageDescriptor = getImageDescriptor(desc);
	}
	if(desc.className == null) {
		//Don't need to keep these references.
		desc.element = null;
		desc.pluginDescriptor = null;
	}
	descriptors.add(desc);
}
/**
 * @see IWorkbenchAdapter#getImageDescriptor
 */
public ImageDescriptor getImageDescriptor(IMarker marker) {
	int size = descriptors.size();
	for (int i = 0; i < size; i++) {
		Descriptor desc = (Descriptor)descriptors.get(i);
		try {
			if(marker.isSubtypeOf(desc.markerType)) {
				if(desc.className != null) {
					if(desc.pluginDescriptor.isPluginActivated()) {
						//-- Get the image descriptor from the provider.
						//-- Save the image descriptor url as a persistable property, so a
						//image descriptor can be created without activating the plugin next
						//time the workbench is started.
						if(desc.provider == null)
							desc.provider = (IMarkerImageProvider)WorkbenchPlugin.createExtension(
								desc.element, ATT_PROVIDER_CLASS);
						String path = desc.provider.getImagePath(marker);
						if(path != desc.imagePath) {
							desc.imagePath = path;
							desc.imageDescriptor = getImageDescriptor(desc);
							return desc.imageDescriptor;
						}
						return desc.imageDescriptor;
					} else {
						if(desc.imageDescriptor == null) {
							//Create a image descriptor to be used until the plugin gets activated.
							desc.imagePath = (String)marker.getAttribute(MARKER_ATT_KEY);
							desc.imageDescriptor = getImageDescriptor(desc);
						}
						return desc.imageDescriptor;
					}
				} else if(desc.imageDescriptor != null) {
					return desc.imageDescriptor;
				}
			}
		} catch (CoreException e) {
			WorkbenchPlugin.getDefault().getLog().log(
				new Status(
					IStatus.ERROR,PlatformUI.PLUGIN_ID,0,
					"Exception creating image descriptor for: " +  desc.markerType,//$NON-NLS-1$
					e));
			return null;
		}
	}
	return null;
}
/**
 * Returns the image descriptor with the given relative path.
 */
ImageDescriptor getImageDescriptor(Descriptor desc) {
	try {
		URL installURL = desc.pluginDescriptor.getInstallURL();
		URL url = new URL(installURL, desc.imagePath);
		return ImageDescriptor.createFromURL(url);
	}
	catch (MalformedURLException e) {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2170.java