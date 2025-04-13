error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7763.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7763.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7763.java
text:
```scala
I@@KeyBinding keyBinding = Persistence.readKeyBinding(new ConfigurationElementMemento(element), getPluginId(element), 2);

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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.IActiveKeyConfiguration;
import org.eclipse.ui.commands.ICategory;
import org.eclipse.ui.commands.ICommand;
import org.eclipse.ui.commands.IContextBinding;
import org.eclipse.ui.commands.IImageBinding;
import org.eclipse.ui.commands.IKeyBinding;
import org.eclipse.ui.commands.IKeyConfiguration;
import org.eclipse.ui.internal.registry.RegistryReader;
import org.eclipse.ui.internal.util.ConfigurationElementMemento;

final class PluginRegistry extends AbstractRegistry {

	private final class PluginRegistryReader extends RegistryReader {

		protected boolean readElement(IConfigurationElement element) {
			String name = element.getName();

			if (Persistence.TAG_ACTIVE_KEY_CONFIGURATION.equals(name))
				return readActiveKeyConfiguration(element);

			if (Persistence.TAG_CATEGORY.equals(name))
				return readCategory(element);

			if (Persistence.TAG_COMMAND.equals(name))
				return readCommand(element);

			if (Persistence.TAG_CONTEXT_BINDING.equals(name))
				return readContextBinding(element);

			if (Persistence.TAG_IMAGE_BINDING.equals(name))
				return readImageBinding(element);

			if (Persistence.TAG_KEY_BINDING.equals(name))
				return readKeyBinding(element);

			if (Persistence.TAG_KEY_CONFIGURATION.equals(name))
				return readKeyConfiguration(element);

			return true; // TODO return false;
		}		
	}

	private final static String TAG_ROOT = Persistence.PACKAGE_BASE;
	
	private List activeKeyConfigurations;
	private List categories; 
	private List commands; 
	private List contextBindings;
	private List imageBindings;
	private List keyBindings;
	private List keyConfigurations;	
	private IPluginRegistry pluginRegistry;
	private PluginRegistryReader pluginRegistryReader;
	
	PluginRegistry(IPluginRegistry pluginRegistry) {
		super();	

		if (pluginRegistry == null)
			throw new NullPointerException();
		
		this.pluginRegistry = pluginRegistry;
	}

	public void load()
		throws IOException {
		if (activeKeyConfigurations == null)
			activeKeyConfigurations = new ArrayList();
		else 
			activeKeyConfigurations.clear();		
	
		if (categories == null)
			categories = new ArrayList();
		else 
			categories.clear();
		
		if (commands == null)
			commands = new ArrayList();
		else 
			commands.clear();

		if (contextBindings == null)
			contextBindings = new ArrayList();
		else 
			contextBindings.clear();

		if (imageBindings == null)
			imageBindings = new ArrayList();
		else 
			imageBindings.clear();
		
		if (keyBindings == null)
			keyBindings = new ArrayList();
		else 
			keyBindings.clear();

		if (keyConfigurations == null)
			keyConfigurations = new ArrayList();
		else 
			keyConfigurations.clear();

		if (pluginRegistryReader == null)
			pluginRegistryReader = new PluginRegistryReader();

		pluginRegistryReader.readRegistry(pluginRegistry, PlatformUI.PLUGIN_ID, TAG_ROOT);		
		super.activeKeyConfigurations = Collections.unmodifiableList(activeKeyConfigurations);
		super.categories = Collections.unmodifiableList(categories);
		super.commands = Collections.unmodifiableList(commands);
		super.contextBindings = Collections.unmodifiableList(contextBindings);
		super.imageBindings = Collections.unmodifiableList(imageBindings);
		super.keyBindings = Collections.unmodifiableList(keyBindings);
		super.keyConfigurations = Collections.unmodifiableList(keyConfigurations);
	}

	private String getPluginId(IConfigurationElement element) {
		String pluginId = null;	
	
		if (element != null) {	
			IExtension extension = element.getDeclaringExtension();
		
			if (extension != null) {
				IPluginDescriptor pluginDescriptor = extension.getDeclaringPluginDescriptor();
			
				if (pluginDescriptor != null) 
					pluginId = pluginDescriptor.getUniqueIdentifier();				
			}
		}

		return pluginId;
	}

	private boolean readActiveKeyConfiguration(IConfigurationElement element) {
		IActiveKeyConfiguration activeKeyConfiguration = Persistence.readActiveKeyConfiguration(new ConfigurationElementMemento(element), getPluginId(element));
	
		if (activeKeyConfiguration != null)
			activeKeyConfigurations.add(activeKeyConfiguration);	
		
		return true;
	}

	private boolean readCategory(IConfigurationElement element) {
		ICategory category = Persistence.readCategory(new ConfigurationElementMemento(element), getPluginId(element));
	
		if (category != null)
			categories.add(category);	
		
		return true;
	}
	
	private boolean readCommand(IConfigurationElement element) {
		ICommand command = Persistence.readCommand(new ConfigurationElementMemento(element), getPluginId(element));
	
		if (command != null)
			commands.add(command);	
		
		return true;
	}

	private boolean readContextBinding(IConfigurationElement element) {
		IContextBinding contextBinding = Persistence.readContextBinding(new ConfigurationElementMemento(element), getPluginId(element));
	
		if (contextBinding != null)
			contextBindings.add(contextBinding);	
		
		return true;
	}

	private boolean readImageBinding(IConfigurationElement element) {
		IImageBinding imageBinding = Persistence.readImageBinding(new ConfigurationElementMemento(element), getPluginId(element));
	
		if (imageBinding != null)
			imageBindings.add(imageBinding);	
		
		return true;
	}

	private boolean readKeyBinding(IConfigurationElement element) {
		IKeyBinding keyBinding = Persistence.readKeyBinding(new ConfigurationElementMemento(element), getPluginId(element));
	
		if (keyBinding != null)
			keyBindings.add(keyBinding);	
		
		return true;
	}
	
	private boolean readKeyConfiguration(IConfigurationElement element) {
		IKeyConfiguration keyConfiguration = Persistence.readKeyConfiguration(new ConfigurationElementMemento(element), getPluginId(element));
	
		if (keyConfiguration != null)
			keyConfigurations.add(keyConfiguration);	
		
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7763.java