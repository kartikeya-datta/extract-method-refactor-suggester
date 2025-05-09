error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2972.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2972.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2972.java
text:
```scala
K@@eySequence keySequence = KeySequence.parseKeySequence(orTokenizer.nextToken());

/************************************************************************
Copyright (c) 2003 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;

public final class CoreRegistry extends AbstractRegistry {

	private final class RegistryReader extends org.eclipse.ui.internal.registry.RegistryReader {

		private final static String DEPRECATED_KEY_SEQUENCE_SEPARATOR = "||"; //$NON-NLS-1$
		private final static String DEPRECATED_TAG_ACCELERATOR = "accelerator"; //$NON-NLS-1$	
		private final static String DEPRECATED_TAG_ACCELERATOR_CONFIGURATION = "acceleratorConfiguration"; //$NON-NLS-1$
		private final static String DEPRECATED_TAG_ACCELERATOR_CONFIGURATIONS = "acceleratorConfigurations"; //$NON-NLS-1$		
		private final static String DEPRECATED_TAG_ACCELERATOR_SCOPE = "acceleratorScope"; //$NON-NLS-1$
		private final static String DEPRECATED_TAG_ACCELERATOR_SCOPES = "acceleratorScopes"; //$NON-NLS-1$		
		private final static String DEPRECATED_TAG_ACCELERATOR_SET = "acceleratorSet"; //$NON-NLS-1$
		private final static String DEPRECATED_TAG_ACCELERATOR_SETS = "acceleratorSets"; //$NON-NLS-1$
		private final static String DEPRECATED_TAG_ACTION_DEFINITION = "actionDefinition"; //$NON-NLS-1$
		private final static String DEPRECATED_TAG_ACTION_DEFINITIONS = "actionDefinitions"; //$NON-NLS-1$
		private final static String DEPRECATED_TAG_CONFIGURATION_ID = "configurationId"; //$NON-NLS-1$		
		private final static String DEPRECATED_TAG_KEY = "key"; //$NON-NLS-1$
		private final static String DEPRECATED_TAG_SCOPE_ID = "scopeId"; //$NON-NLS-1$	
		private final static int RANK_CORE = 2;
		private final static String TAG_ROOT = Persistence.PACKAGE_BASE;
		
		private List activeGestureConfigurations;
		private List activeKeyConfigurations;		
		private List categories;
		private List commands;
		private List gestureBindings;
		private List gestureConfigurations;
		private List keyBindings;
		private String keyConfiguration;
		private List keyConfigurations;
		private String scope;
		private List scopes;
	
		private RegistryReader(IPluginRegistry pluginRegistry) {
			super();
			activeGestureConfigurations = new ArrayList();
			activeKeyConfigurations = new ArrayList();		
			categories = new ArrayList();
			commands = new ArrayList();
			gestureBindings = new ArrayList();
			gestureConfigurations = new ArrayList();
			keyBindings = new ArrayList();
			keyConfigurations = new ArrayList();
			scopes = new ArrayList();

			if (pluginRegistry != null) {
				readRegistry(pluginRegistry, PlatformUI.PLUGIN_ID, DEPRECATED_TAG_ACCELERATOR_CONFIGURATIONS);
				readRegistry(pluginRegistry, PlatformUI.PLUGIN_ID, DEPRECATED_TAG_ACCELERATOR_SCOPES);
				readRegistry(pluginRegistry, PlatformUI.PLUGIN_ID, DEPRECATED_TAG_ACCELERATOR_SETS);
				readRegistry(pluginRegistry, PlatformUI.PLUGIN_ID, DEPRECATED_TAG_ACTION_DEFINITIONS);				
				readRegistry(pluginRegistry, PlatformUI.PLUGIN_ID, TAG_ROOT);
			}

			CoreRegistry.this.activeGestureConfigurations = Collections.unmodifiableList(activeGestureConfigurations);
			CoreRegistry.this.activeKeyConfigurations = Collections.unmodifiableList(activeKeyConfigurations);
			CoreRegistry.this.categories = Collections.unmodifiableList(categories);
			CoreRegistry.this.commands = Collections.unmodifiableList(commands);
			CoreRegistry.this.gestureBindings = Collections.unmodifiableList(gestureBindings);
			CoreRegistry.this.gestureConfigurations = Collections.unmodifiableList(gestureConfigurations);
			CoreRegistry.this.keyBindings = Collections.unmodifiableList(keyBindings);
			CoreRegistry.this.keyConfigurations = Collections.unmodifiableList(keyConfigurations);
			CoreRegistry.this.scopes = Collections.unmodifiableList(scopes);
		}

		protected boolean readElement(IConfigurationElement element) {
			String name = element.getName();

			if (DEPRECATED_TAG_ACCELERATOR.equals(name) || DEPRECATED_TAG_ACCELERATOR_SET.equals(name)) {
				/*
				logError(element, 
					"'" + TAG_ACCELERATOR + //$NON-NLS-1$
					"', '" + TAG_ACCELERATOR_SET + //$NON-NLS-1$
					"' and '" + TAG_ACCELERATOR_SETS + //$NON-NLS-1$ 
					"' have been deprecated. please replace them with '" + Persistence.TAG_REGIONAL_KEY_BINDING + //$NON-NLS-1$ 
					"' and '" + Persistence.TAG_REGIONAL_KEY_BINDINGS + //$NON-NLS-1$
					"' in all places."); //$NON-NLS-1$
				*/
				
				if (DEPRECATED_TAG_ACCELERATOR.equals(name))
					return readDeprecatedAccelerator(element);
				else 
					return readDeprecatedAcceleratorSet(element);
			} 

			if (DEPRECATED_TAG_ACCELERATOR_CONFIGURATION.equals(name)) {
				/*
				logError(element, 
					"'" + TAG_ACCELERATOR_CONFIGURATION + //$NON-NLS-1$
					"' and '" + TAG_ACCELERATOR_CONFIGURATIONS + //$NON-NLS-1$ 
					"' have been deprecated. please replace them with '" + Persistence.TAG_KEY_CONFIGURATION + //$NON-NLS-1$ 
					"' and '" + Persistence.TAG_KEY_CONFIGURATIONS + //$NON-NLS-1$
					"' in all places."); //$NON-NLS-1$
				*/
				return readKeyConfiguration(element);
			}

			if (DEPRECATED_TAG_ACCELERATOR_SCOPE.equals(name)) {
				/*
				logError(element, 
					"'" + TAG_ACCELERATOR_SCOPE + //$NON-NLS-1$
					"' and '" + TAG_ACCELERATOR_SCOPES + //$NON-NLS-1$ 
					"' have been deprecated. please replace them with '" + Persistence.TAG_SCOPE + //$NON-NLS-1$ 
					"' and '" + Persistence.TAG_SCOPES + //$NON-NLS-1$
					"' in all places."); //$NON-NLS-1$
				*/
				return readScope(element);
			}

			if (DEPRECATED_TAG_ACTION_DEFINITION.equals(name)) {
				/*
				logError(element, 
					"'" + TAG_ACTION_DEFINITION + //$NON-NLS-1$
					"' and '" + TAG_ACTION_DEFINITIONS + //$NON-NLS-1$ 
					"' have been deprecated. please replace them with '" + Persistence.TAG_COMMAND + //$NON-NLS-1$ 
					"' and '" + Persistence.TAG_COMMANDS + //$NON-NLS-1$
					"' in all places."); //$NON-NLS-1$
				*/
				return readCommand(element);
			}

			if (Persistence.TAG_ACTIVE_GESTURE_CONFIGURATION.equals(name))
				return readActiveGestureConfiguration(element);

			if (Persistence.TAG_ACTIVE_KEY_CONFIGURATION.equals(name))
				return readActiveKeyConfiguration(element);

			if (Persistence.TAG_CATEGORY.equals(name))
				return readCategory(element);

			if (Persistence.TAG_COMMAND.equals(name))
				return readCommand(element);

			if (Persistence.TAG_GESTURE_BINDING.equals(name))
				return readGestureBinding(element);

			if (Persistence.TAG_GESTURE_CONFIGURATION.equals(name))
				return readGestureConfiguration(element);

			if (Persistence.TAG_KEY_BINDING.equals(name))
				return readKeyBinding(element);

			if (Persistence.TAG_KEY_CONFIGURATION.equals(name))
				return readKeyConfiguration(element);

			if (Persistence.TAG_SCOPE.equals(name))
				return readScope(element);
	
			return false;
		}

		private String getPlugin(IConfigurationElement element) {
			String plugin = null;	
		
			if (element != null) {	
				IExtension extension = element.getDeclaringExtension();
			
				if (extension != null) {
					IPluginDescriptor pluginDescriptor = extension.getDeclaringPluginDescriptor();
				
					if (pluginDescriptor != null) 
						plugin = pluginDescriptor.getUniqueIdentifier();				
				}
			}

			return plugin;
		}

		private boolean readActiveGestureConfiguration(IConfigurationElement element) {
			ActiveGestureConfiguration activeGestureConfiguration = Persistence.readActiveGestureConfiguration(ConfigurationElementMemento.create(element), getPlugin(element));
		
			if (activeGestureConfiguration != null)
				activeGestureConfigurations.add(activeGestureConfiguration);	
			
			return true;
		}

		private boolean readActiveKeyConfiguration(IConfigurationElement element) {
			ActiveKeyConfiguration activeKeyConfiguration = Persistence.readActiveKeyConfiguration(ConfigurationElementMemento.create(element), getPlugin(element));
		
			if (activeKeyConfiguration != null)
				activeKeyConfigurations.add(activeKeyConfiguration);	
			
			return true;
		}

		private boolean readCategory(IConfigurationElement element) {
			Category category = Persistence.readCategory(ConfigurationElementMemento.create(element), getPlugin(element));
		
			if (category != null)
				categories.add(category);	
			
			return true;
		}

		private boolean readCommand(IConfigurationElement element) {
			Command command = Persistence.readCommand(ConfigurationElementMemento.create(element), getPlugin(element));
		
			if (command != null)
				commands.add(command);	
			
			return true;
		}

		private boolean readDeprecatedAccelerator(IConfigurationElement element) {
			if (keyConfiguration == null || scope == null)
				return false;

			String id = element.getAttribute(Persistence.TAG_ID);
			String key = element.getAttribute(DEPRECATED_TAG_KEY);

			if (key != null) {
				List keySequences = new ArrayList();	
				StringTokenizer orTokenizer = new StringTokenizer(key, DEPRECATED_KEY_SEQUENCE_SEPARATOR); 
			
				while (orTokenizer.hasMoreTokens()) {					
					try {			
						KeySequence keySequence = KeySequence.parse(orTokenizer.nextToken());

						if (keySequence.getKeyStrokes().size() >= 1)
							keySequences.add(keySequence);		
					} catch (IllegalArgumentException eIllegalArgument) {					
					}
				}		
				
				if (keySequences.size() >= 1) {
					String locale = element.getAttribute(Persistence.TAG_LOCALE);
		
					if (locale == null)
						locale = Persistence.ZERO_LENGTH_STRING;
		
					String platform = element.getAttribute(Persistence.TAG_PLATFORM);
		
					if (platform == null)
						platform = Persistence.ZERO_LENGTH_STRING;
		
					String plugin = getPlugin(element);				
					Iterator iterator = keySequences.iterator();
				
					while (iterator.hasNext()) {
						KeySequence keySequence = (KeySequence) iterator.next();			
						keyBindings.add(KeyBinding.create(id, keyConfiguration, keySequence, locale, platform, plugin, RANK_CORE, scope));	
					}
				}
			}

			return true;
		}

		private boolean readDeprecatedAcceleratorSet(IConfigurationElement element) {
			keyConfiguration = element.getAttribute(DEPRECATED_TAG_CONFIGURATION_ID);
			scope = element.getAttribute(DEPRECATED_TAG_SCOPE_ID);
			
			if (keyConfiguration != null && scope != null)
				readElementChildren(element);
				
			keyConfiguration = null;
			scope = null;
			return true;
		}	

		private boolean readGestureBinding(IConfigurationElement element) {
			GestureBinding gestureBinding = Persistence.readGestureBinding(ConfigurationElementMemento.create(element), getPlugin(element), RANK_CORE);

			if (gestureBinding != null)
				gestureBindings.add(gestureBinding);

			return true;
		}

		private boolean readGestureConfiguration(IConfigurationElement element) {
			GestureConfiguration gestureConfiguration = Persistence.readGestureConfiguration(ConfigurationElementMemento.create(element), getPlugin(element));
		
			if (gestureConfiguration != null)
				gestureConfigurations.add(gestureConfiguration);	
			
			return true;
		}

		private boolean readKeyBinding(IConfigurationElement element) {
			KeyBinding keyBinding = Persistence.readKeyBinding(ConfigurationElementMemento.create(element), getPlugin(element), RANK_CORE);

			if (keyBinding != null)
				keyBindings.add(keyBinding);

			return true;
		}
	
		private boolean readKeyConfiguration(IConfigurationElement element) {
			KeyConfiguration keyConfiguration = Persistence.readKeyConfiguration(ConfigurationElementMemento.create(element), getPlugin(element));
		
			if (keyConfiguration != null)
				keyConfigurations.add(keyConfiguration);	
			
			return true;
		}

		private boolean readScope(IConfigurationElement element) {
			Scope scope = Persistence.readScope(ConfigurationElementMemento.create(element), getPlugin(element));
		
			if (scope != null)
				scopes.add(scope);	
			
			return true;
		}
	}

	private static CoreRegistry instance;
	
	public static CoreRegistry getInstance() {
		if (instance == null)
			instance = new CoreRegistry();
	
		return instance;
	}

	private CoreRegistry() {
		super();
	}

	public void load()
		throws IOException {
		new RegistryReader(Platform.getPluginRegistry());			
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2972.java