error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3822.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3822.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3822.java
text:
```scala
I@@ContextBindingDefinition activityBindingDefinition =

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

package org.eclipse.ui.internal.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.registry.IConfigurationElement;
import org.eclipse.core.runtime.registry.IExtension;
import org.eclipse.core.runtime.registry.IExtensionDelta;
import org.eclipse.core.runtime.registry.IExtensionRegistry;
import org.eclipse.core.runtime.registry.IRegistryChangeEvent;
import org.eclipse.core.runtime.registry.IRegistryChangeListener;
import org.eclipse.ui.internal.util.ConfigurationElementMemento;

public final class ExtensionCommandRegistry extends AbstractCommandRegistry {

	private List activeKeyConfigurationDefinitions;
	private List activityBindingDefinitions;
	private List categoryDefinitions;
	private List commandDefinitions;
	private IExtensionRegistry extensionRegistry;
	private List imageBindingDefinitions;
	private List keyConfigurationDefinitions;
	private List keySequenceBindingDefinitions;

	public ExtensionCommandRegistry(IExtensionRegistry extensionRegistry) {
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
		if (activeKeyConfigurationDefinitions == null)
			activeKeyConfigurationDefinitions = new ArrayList();
		else
			activeKeyConfigurationDefinitions.clear();

		if (activityBindingDefinitions == null)
			activityBindingDefinitions = new ArrayList();
		else
			activityBindingDefinitions.clear();

		if (categoryDefinitions == null)
			categoryDefinitions = new ArrayList();
		else
			categoryDefinitions.clear();

		if (commandDefinitions == null)
			commandDefinitions = new ArrayList();
		else
			commandDefinitions.clear();

		if (imageBindingDefinitions == null)
			imageBindingDefinitions = new ArrayList();
		else
			imageBindingDefinitions.clear();

		if (keyConfigurationDefinitions == null)
			keyConfigurationDefinitions = new ArrayList();
		else
			keyConfigurationDefinitions.clear();

		if (keySequenceBindingDefinitions == null)
			keySequenceBindingDefinitions = new ArrayList();
		else
			keySequenceBindingDefinitions.clear();

		IConfigurationElement[] configurationElements =
			extensionRegistry.getConfigurationElementsFor(
				Persistence.PACKAGE_FULL);

		for (int i = 0; i < configurationElements.length; i++) {
			IConfigurationElement configurationElement =
				configurationElements[i];
			String name = configurationElement.getName();

			if (Persistence.TAG_ACTIVE_KEY_CONFIGURATION.equals(name))
				readActiveKeyConfigurationDefinition(configurationElement);
			else if (Persistence.TAG_ACTIVITY_BINDING.equals(name))
				readActivityBindingDefinition(configurationElement);
			else if (Persistence.TAG_CATEGORY.equals(name))
				readCategoryDefinition(configurationElement);
			else if (Persistence.TAG_COMMAND.equals(name))
				readCommandDefinition(configurationElement);
			else if (Persistence.TAG_IMAGE_BINDING.equals(name))
				readImageBindingDefinition(configurationElement);
			else if (Persistence.TAG_KEY_CONFIGURATION.equals(name))
				readKeyConfigurationDefinition(configurationElement);
			else if (Persistence.TAG_KEY_SEQUENCE_BINDING.equals(name))
				readKeySequenceBindingDefinition(configurationElement);
		}

		boolean commandRegistryChanged = false;

		if (!activeKeyConfigurationDefinitions
			.equals(super.activeKeyConfigurationDefinitions)) {
			super.activeKeyConfigurationDefinitions =
				Collections.unmodifiableList(activeKeyConfigurationDefinitions);
			commandRegistryChanged = true;
		}

		if (!activityBindingDefinitions
			.equals(super.activityBindingDefinitions)) {
			super.activityBindingDefinitions =
				Collections.unmodifiableList(activityBindingDefinitions);
			commandRegistryChanged = true;
		}

		if (!categoryDefinitions.equals(super.categoryDefinitions)) {
			super.categoryDefinitions =
				Collections.unmodifiableList(categoryDefinitions);
			commandRegistryChanged = true;
		}

		if (!commandDefinitions.equals(super.commandDefinitions)) {
			super.commandDefinitions =
				Collections.unmodifiableList(commandDefinitions);
			commandRegistryChanged = true;
		}

		if (!imageBindingDefinitions.equals(super.imageBindingDefinitions)) {
			super.imageBindingDefinitions =
				Collections.unmodifiableList(imageBindingDefinitions);
			commandRegistryChanged = true;
		}

		if (!keyConfigurationDefinitions
			.equals(super.keyConfigurationDefinitions)) {
			super.keyConfigurationDefinitions =
				Collections.unmodifiableList(keyConfigurationDefinitions);
			commandRegistryChanged = true;
		}

		if (!keySequenceBindingDefinitions
			.equals(super.keySequenceBindingDefinitions)) {
			super.keySequenceBindingDefinitions =
				Collections.unmodifiableList(keySequenceBindingDefinitions);
			commandRegistryChanged = true;
		}

		if (commandRegistryChanged)
			fireCommandRegistryChanged();
	}

	private void readActiveKeyConfigurationDefinition(IConfigurationElement configurationElement) {
		IActiveKeyConfigurationDefinition activeKeyConfigurationDefinition =
			Persistence.readActiveKeyConfigurationDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (activeKeyConfigurationDefinition != null)
			activeKeyConfigurationDefinitions.add(
				activeKeyConfigurationDefinition);
	}

	private void readActivityBindingDefinition(IConfigurationElement configurationElement) {
		IActivityBindingDefinition activityBindingDefinition =
			Persistence.readActivityBindingDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (activityBindingDefinition != null)
			activityBindingDefinitions.add(activityBindingDefinition);
	}

	private void readCategoryDefinition(IConfigurationElement configurationElement) {
		ICategoryDefinition categoryDefinition =
			Persistence.readCategoryDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (categoryDefinition != null)
			categoryDefinitions.add(categoryDefinition);
	}

	private void readCommandDefinition(IConfigurationElement configurationElement) {
		ICommandDefinition commandDefinition =
			Persistence.readCommandDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (commandDefinition != null)
			commandDefinitions.add(commandDefinition);
	}

	private void readImageBindingDefinition(IConfigurationElement configurationElement) {
		IImageBindingDefinition imageBinding =
			Persistence.readImageBindingDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (imageBinding != null)
			imageBindingDefinitions.add(imageBinding);
	}

	private void readKeyConfigurationDefinition(IConfigurationElement configurationElement) {
		IKeyConfigurationDefinition keyConfigurationDefinition =
			Persistence.readKeyConfigurationDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (keyConfigurationDefinition != null)
			keyConfigurationDefinitions.add(keyConfigurationDefinition);
	}

	private void readKeySequenceBindingDefinition(IConfigurationElement configurationElement) {
		IKeySequenceBindingDefinition keySequenceBindingDefinition =
			Persistence.readKeySequenceBindingDefinition(
				new ConfigurationElementMemento(configurationElement),
				getPluginId(configurationElement));

		if (keySequenceBindingDefinition != null)
			keySequenceBindingDefinitions.add(keySequenceBindingDefinition);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3822.java