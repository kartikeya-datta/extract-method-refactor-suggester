error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9570.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9570.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9570.java
text:
```scala
r@@eturn KeyBinding.create(command, keyConfiguration, keySequence, Persistence.ZERO_LENGTH_STRING, Persistence.ZERO_LENGTH_STRING, plugin, RANK_PREFERENCE, scope);

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.WorkbenchPlugin;

public final class PreferenceRegistry extends AbstractMutableRegistry {

	private final static String DEPRECATED_KEY_ACTIVE_KEY_CONFIGURATIONS = "acceleratorConfigurationId"; //$NON-NLS-1$
	private final static String DEPRECATED_KEY_KEY_BINDINGS = "org.eclipse.ui.keybindings"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_BINDING = "binding"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_BINDINGS = "bindings"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_ACCELERATOR = "accelerator"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_ACTION = "action"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_CONFIGURATION = "configuration"; //$NON-NLS-1$		
	private final static String DEPRECATED_TAG_KEY_SEQUENCE = "keysequence"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_KEY_STROKE = "keystroke"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_PLUGIN = "plugin"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_RANK = "rank"; //$NON-NLS-1$
	private final static String DEPRECATED_TAG_SCOPE = "scope"; //$NON-NLS-1$
	private final static String KEY = Persistence.PACKAGE_FULL;
	private final static int RANK_PREFERENCE = 0;
	private final static String TAG_ROOT = Persistence.PACKAGE_FULL;

	public static PreferenceRegistry instance;
	
	public static PreferenceRegistry getInstance() {
		if (instance == null)
			instance = new PreferenceRegistry();
	
		return instance;
	}

	private PreferenceRegistry() {
		super();
	}

	public void load() 
		throws IOException {
		IPreferenceStore preferenceStore = WorkbenchPlugin.getDefault().getPreferenceStore();

		String deprecatedActiveKeyConfigurationsString = preferenceStore.getString(DEPRECATED_KEY_ACTIVE_KEY_CONFIGURATIONS);
		List deprecatedActiveKeyConfigurations = Collections.EMPTY_LIST;
	
		if (deprecatedActiveKeyConfigurationsString != null && deprecatedActiveKeyConfigurationsString.length() != 0)		
			deprecatedActiveKeyConfigurations = Collections.singletonList(ActiveKeyConfiguration.create(null, deprecatedActiveKeyConfigurationsString));

		String deprecatedKeyBindingsString = preferenceStore.getString(DEPRECATED_KEY_KEY_BINDINGS);
		List deprecatedKeyBindings = Collections.EMPTY_LIST;

		if (deprecatedKeyBindingsString != null && deprecatedKeyBindingsString.length() != 0) {
			Reader reader = new BufferedReader(new StringReader(deprecatedKeyBindingsString));
		
			try {
				IMemento memento = XMLMemento.createReadRoot(reader);
				IMemento mementoKeyBindings = memento.getChild(DEPRECATED_TAG_BINDINGS);
	
				if (mementoKeyBindings != null)
					deprecatedKeyBindings = Collections.unmodifiableList(readDeprecatedKeyBindings(mementoKeyBindings, DEPRECATED_TAG_BINDING));
			} catch (WorkbenchException eWorkbench) {
				throw new IOException();
			} finally {
				reader.close();
			}
		}	
		
		String preferenceString = preferenceStore.getString(KEY);
		
		// TODO if (preferenceString == null || preferenceString.length() == 0)
		//	throw new IOException();
		
		if (preferenceString != null && preferenceString.length() != 0) {
			Reader reader = new StringReader(preferenceString);
			
			try {
				IMemento memento = XMLMemento.createReadRoot(reader);
				activeGestureConfigurations = Collections.unmodifiableList(Persistence.readActiveGestureConfigurations(memento, Persistence.TAG_ACTIVE_GESTURE_CONFIGURATION, null));
				activeKeyConfigurations = Collections.unmodifiableList(Persistence.readActiveKeyConfigurations(memento, Persistence.TAG_ACTIVE_KEY_CONFIGURATION, null));
				categories = Collections.unmodifiableList(Persistence.readCategories(memento, Persistence.TAG_CATEGORY, null));
				commands = Collections.unmodifiableList(Persistence.readCommands(memento, Persistence.TAG_COMMAND, null));
				gestureBindings = Collections.unmodifiableList(Persistence.readGestureBindings(memento, Persistence.TAG_GESTURE_BINDING, null, RANK_PREFERENCE));
				gestureConfigurations = Collections.unmodifiableList(Persistence.readGestureConfigurations(memento, Persistence.TAG_GESTURE_CONFIGURATION, null));
				keyBindings = Collections.unmodifiableList(Persistence.readKeyBindings(memento, Persistence.TAG_KEY_BINDING, null, RANK_PREFERENCE));
				keyConfigurations = Collections.unmodifiableList(Persistence.readKeyConfigurations(memento, Persistence.TAG_KEY_CONFIGURATION, null));
				scopes = Collections.unmodifiableList(Persistence.readScopes(memento, Persistence.TAG_SCOPE, null));
			} catch (WorkbenchException eWorkbench) {
				throw new IOException();
			} finally {
				reader.close();
			}
		}

		List activeKeyConfigurations = new ArrayList();
		activeKeyConfigurations.addAll(deprecatedActiveKeyConfigurations);
		activeKeyConfigurations.addAll(this.activeKeyConfigurations);
		this.activeKeyConfigurations = Collections.unmodifiableList(activeKeyConfigurations);
	
		List keyBindings = new ArrayList();
		keyBindings.addAll(deprecatedKeyBindings);
		keyBindings.addAll(this.keyBindings);
		this.keyBindings = Collections.unmodifiableList(keyBindings);
	}
	
	public void save()
		throws IOException {
		XMLMemento xmlMemento = XMLMemento.createWriteRoot(TAG_ROOT);		
		Persistence.writeActiveGestureConfigurations(xmlMemento, Persistence.TAG_ACTIVE_GESTURE_CONFIGURATION, activeGestureConfigurations);		
		Persistence.writeActiveKeyConfigurations(xmlMemento, Persistence.TAG_ACTIVE_KEY_CONFIGURATION, activeKeyConfigurations);		
		Persistence.writeCategories(xmlMemento, Persistence.TAG_CATEGORY, categories);		
		Persistence.writeCommands(xmlMemento, Persistence.TAG_COMMAND, commands);
		Persistence.writeGestureBindings(xmlMemento, Persistence.TAG_GESTURE_BINDING, gestureBindings);
		Persistence.writeGestureConfigurations(xmlMemento, Persistence.TAG_GESTURE_CONFIGURATION, gestureConfigurations);
		Persistence.writeKeyBindings(xmlMemento, Persistence.TAG_KEY_BINDING, keyBindings);
		Persistence.writeKeyConfigurations(xmlMemento, Persistence.TAG_KEY_CONFIGURATION, keyConfigurations);
		Persistence.writeScopes(xmlMemento, Persistence.TAG_SCOPE, scopes);
		Writer writer = new StringWriter();

		try {
			xmlMemento.save(writer);
			IPreferenceStore preferenceStore = WorkbenchPlugin.getDefault().getPreferenceStore();
			preferenceStore.setValue(KEY, writer.toString());					
		} finally {
			writer.close();
		}
	}

	private static KeyBinding readDeprecatedKeyBinding(IMemento memento)
		throws IllegalArgumentException {
		if (memento == null)
			throw new IllegalArgumentException();

		String command = memento.getString(DEPRECATED_TAG_ACTION);
		String keyConfiguration = memento.getString(DEPRECATED_TAG_CONFIGURATION);
		
		if (keyConfiguration == null)
			keyConfiguration = Persistence.ZERO_LENGTH_STRING;

		KeySequence keySequence = null;
		IMemento mementoKeySequence = memento.getChild(DEPRECATED_TAG_KEY_SEQUENCE);
		
		if (mementoKeySequence != null) 
			keySequence = readDeprecatedKeySequence(mementoKeySequence);	

		if (keySequence == null)
			keySequence = Persistence.ZERO_LENGTH_KEY_SEQUENCE;
		
		String plugin = memento.getString(DEPRECATED_TAG_PLUGIN);	
		String scope = memento.getString(DEPRECATED_TAG_SCOPE);

		if (scope == null)
			scope = Persistence.ZERO_LENGTH_STRING;

		return KeyBinding.create(command, keyConfiguration, keySequence, Persistence.ZERO_LENGTH_STRING, Persistence.ZERO_LENGTH_STRING, plugin, 0, scope);
	}

	private static List readDeprecatedKeyBindings(IMemento memento, String name)
		throws IllegalArgumentException {		
		if (memento == null || name == null)
			throw new IllegalArgumentException();			
	
		IMemento[] mementos = memento.getChildren(name);
	
		if (mementos == null)
			throw new IllegalArgumentException();
	
		List list = new ArrayList(mementos.length);
	
		for (int i = 0; i < mementos.length; i++)
			list.add(readDeprecatedKeyBinding(mementos[i]));
	
		return list;				
	}
	
	private static KeySequence readDeprecatedKeySequence(IMemento memento)
		throws IllegalArgumentException {
		if (memento == null)
			throw new IllegalArgumentException();
			
		IMemento[] mementos = memento.getChildren(DEPRECATED_TAG_KEY_STROKE);
		
		if (mementos == null)
			throw new IllegalArgumentException();
		
		List keyStrokes = new ArrayList(mementos.length);
		
		for (int i = 0; i < mementos.length; i++)
			keyStrokes.add(readDeprecatedKeyStroke(mementos[i]));
		
		return KeySequence.create(keyStrokes);
	}

	private static KeyStroke readDeprecatedKeyStroke(IMemento memento)
		throws IllegalArgumentException {
		if (memento == null)
			throw new IllegalArgumentException();

		Integer value = memento.getInteger(DEPRECATED_TAG_ACCELERATOR);
		
		if (value == null)
			value = Persistence.ZERO;
		
		return KeyStroke.create(value.intValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9570.java