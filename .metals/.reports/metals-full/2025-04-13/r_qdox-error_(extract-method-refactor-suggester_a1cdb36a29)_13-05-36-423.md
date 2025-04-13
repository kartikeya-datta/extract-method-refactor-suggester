error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6817.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6817.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6817.java
text:
```scala
i@@f (descriptor != null && !editorMap.containsKey(descriptor.getId())) {

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *      Wojciech Galanciak <wojciech.galanciak@pl.ibm.com> - Bug 236104 [EditorMgmt] File association default needs to be set twice to take effect
 *******************************************************************************/
package org.eclipse.ui.internal;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.decorators.DecoratorManager;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.eclipse.ui.internal.registry.EditorRegistry;
import org.eclipse.ui.internal.util.PrefUtil;

/**
 * The PlatformUIPreferenceListener is a class that listens to changes in the
 * preference store and propogates the change for any special cases that require
 * updating of other values within the workbench.
 */
public class PlatformUIPreferenceListener implements
		IEclipsePreferences.IPreferenceChangeListener {
	
	private static PlatformUIPreferenceListener singleton;
	
	public static IEclipsePreferences.IPreferenceChangeListener getSingleton(){
		if(singleton == null) {
			singleton = new PlatformUIPreferenceListener();
		}
	    return singleton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener#preferenceChange(org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent)
	 */
	public void preferenceChange(PreferenceChangeEvent event) {

		String propertyName = event.getKey();
		if (IPreferenceConstants.ENABLED_DECORATORS.equals(propertyName)) {
			DecoratorManager manager = WorkbenchPlugin.getDefault()
					.getDecoratorManager();
			manager.applyDecoratorsPreference();
			manager.clearCaches();
			manager.updateForEnablementChange();
			return;
		}

		if (IWorkbenchPreferenceConstants.SHOW_SYSTEM_JOBS.equals(propertyName)) {
			boolean setting = PrefUtil.getAPIPreferenceStore().getBoolean(
					IWorkbenchPreferenceConstants.SHOW_SYSTEM_JOBS);

			ProgressManager.getInstance().setShowSystemJobs(setting);
		}
		
		if (IWorkbenchPreferenceConstants.DEFAULT_PERSPECTIVE_ID.equals(propertyName)) {
			IWorkbench workbench = PlatformUI.getWorkbench();

			workbench.getPerspectiveRegistry().setDefaultPerspective(
					PrefUtil.getAPIPreferenceStore().getString(
							IWorkbenchPreferenceConstants.DEFAULT_PERSPECTIVE_ID));
			return;
		}

		if (IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR
				.equals(propertyName)) {
			IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow[] workbenchWindows = workbench
					.getWorkbenchWindows();
			for (int i = 0; i < workbenchWindows.length; i++) {
				IWorkbenchWindow window = workbenchWindows[i];
				if (window instanceof WorkbenchWindow) {
					((WorkbenchWindow) window)
							.setPerspectiveBarLocation(apiStore
									.getString(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR));
				}
			}
			return;
		}

		// TODO the banner apperance should have its own preference
		if (IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS
				.equals(propertyName)) {
			boolean newValue = PrefUtil.getAPIPreferenceStore().getBoolean(
					IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS);

			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow[] workbenchWindows = workbench
					.getWorkbenchWindows();
			for (int i = 0; i < workbenchWindows.length; i++) {
				IWorkbenchWindow window = workbenchWindows[i];
				if (window instanceof WorkbenchWindow) {
					((WorkbenchWindow) window).setBannerCurve(newValue);
				}
			}
			return;
		}

		// Update the file associations if they have changed due to an import
		if (IPreferenceConstants.RESOURCES.equals(propertyName)) {
			IEditorRegistry registry = WorkbenchPlugin.getDefault()
					.getEditorRegistry();
			if (registry instanceof EditorRegistry) {
				EditorRegistry editorRegistry = (EditorRegistry) registry;
				IPreferenceStore store = WorkbenchPlugin.getDefault()
						.getPreferenceStore();
				Reader reader = null;
				try {
					String xmlString = store
							.getString(IPreferenceConstants.RESOURCES);
					if (xmlString != null && xmlString.length() > 0) {
						reader = new StringReader(xmlString);
						// Build the editor map.
						HashMap editorMap = new HashMap();
						int i = 0;
						IEditorDescriptor[] descriptors = editorRegistry
								.getSortedEditorsFromPlugins();
						// Get the internal editors
						for (i = 0; i < descriptors.length; i++) {
							IEditorDescriptor descriptor = descriptors[i];
							editorMap.put(descriptor.getId(), descriptor);
						}
						// Get the external (OS) editors
						descriptors = editorRegistry.getSortedEditorsFromOS();
						for (i = 0; i < descriptors.length; i++) {
							IEditorDescriptor descriptor = descriptors[i];
							editorMap.put(descriptor.getId(), descriptor);
						}
						// Get default editors which are not OS or internal
						// editors
						IFileEditorMapping[] maps = editorRegistry.getFileEditorMappings();
						for (int j = 0; j < maps.length; j++) {
							IFileEditorMapping fileEditorMapping = maps[j];
							IEditorDescriptor descriptor = fileEditorMapping.getDefaultEditor();
							if (!editorMap.containsKey(descriptor.getId())) {
								editorMap.put(descriptor.getId(), descriptor);
							}
						}
						// Update the file to editor(s) mappings
						editorRegistry.readResources(editorMap, reader);
					}
				} catch (WorkbenchException e) {
					e.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6817.java