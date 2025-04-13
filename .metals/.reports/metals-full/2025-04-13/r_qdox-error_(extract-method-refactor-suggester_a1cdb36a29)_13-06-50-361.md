error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3810.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3810.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3810.java
text:
```scala
p@@luginId = extension.getNamespace();

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.ui.internal.util.ConfigurationElementMemento;

final class ExtensionActivityRegistry extends AbstractActivityRegistry {
	private List activityActivityBindingDefinitions;
	private List activityDefinitions;
	private List activityPatternBindingDefinitions;
	private List categoryActivityBindingDefinitions;
	private List categoryDefinitions;
	private List defaultEnabledActivities;
	private IExtensionRegistry extensionRegistry;

	ExtensionActivityRegistry(IExtensionRegistry extensionRegistry) {
		if (extensionRegistry == null)
			throw new NullPointerException();

		this.extensionRegistry = extensionRegistry;

		this
			.extensionRegistry
			.addRegistryChangeListener(new IRegistryChangeListener() {
			public void registryChanged(IRegistryChangeEvent registryChangeEvent) {
				IExtensionDelta[] extensionDeltas =
					registryChangeEvent.getExtensionDeltas(
						Persistence.PACKAGE_PREFIX,
						Persistence.PACKAGE_BASE);

				if (extensionDeltas.length != 0)
					try {
						load();
					} catch (IOException eIO) {
					}
			}
		});

		try {
			load();
		} catch (IOException eIO) {
		}
	}

	private String getPluginId(IConfigurationElement configurationElement) {
		String pluginId = null;

		if (configurationElement != null) {
			IExtension extension = configurationElement.getDeclaringExtension();

			if (extension != null)
				pluginId = extension.getParentIdentifier();
		}

		return pluginId;
	}

	private void load() throws IOException {
		if (activityActivityBindingDefinitions == null)
			activityActivityBindingDefinitions = new ArrayList();
		else
			activityActivityBindingDefinitions.clear();

		if (activityDefinitions == null)
			activityDefinitions = new ArrayList();
		else
			activityDefinitions.clear();

		if (activityPatternBindingDefinitions == null)
			activityPatternBindingDefinitions = new ArrayList();
		else
			activityPatternBindingDefinitions.clear();

		if (categoryActivityBindingDefinitions == null)
			categoryActivityBindingDefinitions = new ArrayList();
		else
			categoryActivityBindingDefinitions.clear();

		if (categoryDefinitions == null)
			categoryDefinitions = new ArrayList();
		else
			categoryDefinitions.clear();
		
		if (defaultEnabledActivities == null)
		    defaultEnabledActivities = new ArrayList();
		else
		    defaultEnabledActivities.clear();

		IConfigurationElement[] configurationElements =
			extensionRegistry.getConfigurationElementsFor(
				Persistence.PACKAGE_FULL);

		for (int i = 0; i < configurationElements.length; i++) {
			IConfigurationElement configurationElement =
				configurationElements[i];
			String name = configurationElement.getName();

			if (Persistence.TAG_ACTIVITY_ACTIVITY_BINDING.equals(name))
				readActivityActivityBindingDefinition(configurationElement);
			else if (Persistence.TAG_ACTIVITY.equals(name))
				readActivityDefinition(configurationElement);
			else if (Persistence.TAG_ACTIVITY_PATTERN_BINDING.equals(name))
				readActivityPatternBindingDefinition(configurationElement);
			else if (Persistence.TAG_CATEGORY_ACTIVITY_BINDING.equals(name))
				readCategoryActivityBindingDefinition(configurationElement);
			else if (Persistence.TAG_CATEGORY.equals(name))
				readCategoryDefinition(configurationElement);
			else if (Persistence.TAG_DEFAULT_ENABLEMENT.equals(name)) 
			    readDefaultEnablement(configurationElement);
		}

		boolean activityRegistryChanged = false;

		if (!activityActivityBindingDefinitions
			.equals(super.activityActivityBindingDefinitions)) {
			super.activityActivityBindingDefinitions =
				Collections.unmodifiableList(
					activityActivityBindingDefinitions);
			activityRegistryChanged = true;
		}

		if (!activityDefinitions.equals(super.activityDefinitions)) {
			super.activityDefinitions =
				Collections.unmodifiableList(activityDefinitions);
			activityRegistryChanged = true;
		}

		if (!activityPatternBindingDefinitions
			.equals(super.activityPatternBindingDefinitions)) {
			super.activityPatternBindingDefinitions =
				Collections.unmodifiableList(activityPatternBindingDefinitions);
			activityRegistryChanged = true;
		}

		if (!categoryActivityBindingDefinitions
			.equals(super.categoryActivityBindingDefinitions)) {
			super.categoryActivityBindingDefinitions =
				Collections.unmodifiableList(
					categoryActivityBindingDefinitions);
			activityRegistryChanged = true;
		}

		if (!categoryDefinitions.equals(super.categoryDefinitions)) {
			super.categoryDefinitions =
				Collections.unmodifiableList(categoryDefinitions);
			activityRegistryChanged = true;
		}
		
		if (!defaultEnabledActivities.equals(super.defaultEnabledActivities)) {
			super.defaultEnabledActivities =
				Collections.unmodifiableList(defaultEnabledActivities);
			activityRegistryChanged = true;
		}		

		if (activityRegistryChanged)
			fireActivityRegistryChanged();
	}

    private void readDefaultEnablement(IConfigurationElement configurationElement) {
		String enabledActivity =
			Persistence.readDefaultEnablement(
				new ConfigurationElementMemento(configurationElement));

		if (enabledActivity != null)
			defaultEnabledActivities.add(enabledActivity);
        
    }

    private void readActivityActivityBindingDefinition(IConfigurationElement configurationElement) {
		ActivityActivityBindingDefinition activityActivityBindingDefinition =
			Persistence.readActivityActivityBindingDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (activityActivityBindingDefinition != null)
			activityActivityBindingDefinitions.add(
				activityActivityBindingDefinition);
	}

	private void readActivityDefinition(IConfigurationElement configurationElement) {
		ActivityDefinition activityDefinition =
			Persistence.readActivityDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (activityDefinition != null)
			activityDefinitions.add(activityDefinition);
	}

	private void readActivityPatternBindingDefinition(IConfigurationElement configurationElement) {
		ActivityPatternBindingDefinition activityPatternBindingDefinition =
			Persistence.readActivityPatternBindingDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (activityPatternBindingDefinition != null)
			activityPatternBindingDefinitions.add(
				activityPatternBindingDefinition);
	}

	private void readCategoryActivityBindingDefinition(IConfigurationElement configurationElement) {
		CategoryActivityBindingDefinition categoryActivityBindingDefinition =
			Persistence.readCategoryActivityBindingDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (categoryActivityBindingDefinition != null)
			categoryActivityBindingDefinitions.add(
				categoryActivityBindingDefinition);
	}

	private void readCategoryDefinition(IConfigurationElement configurationElement) {
		CategoryDefinition categoryDefinition =
			Persistence.readCategoryDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (categoryDefinition != null)
			categoryDefinitions.add(categoryDefinition);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3810.java