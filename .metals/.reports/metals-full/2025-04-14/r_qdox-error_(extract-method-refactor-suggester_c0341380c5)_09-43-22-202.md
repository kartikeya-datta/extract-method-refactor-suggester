error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5411.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5411.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5411.java
text:
```scala
C@@opyright (c) 2003 IBM Corporation and others.

/************************************************************************
Copyright (c) 2002 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.WorkbenchPlugin;

public final class LocalRegistry extends AbstractMutableRegistry {

	private final static String PATH = Persistence.TAG_PACKAGE + ".xml"; //$NON-NLS-1$

	public static LocalRegistry instance;
	
	public static LocalRegistry getInstance() {
		if (instance == null)
			instance = new LocalRegistry();
	
		return instance;
	}

	private LocalRegistry() {
		super();
	}

	public void load()
		throws IOException {
		IPath path = WorkbenchPlugin.getDefault().getStateLocation();
		path = path.append(PATH);
		File file = path.toFile();		
		Reader reader = new BufferedReader(new FileReader(file));

		try {
			IMemento memento = XMLMemento.createReadRoot(reader);
			activeGestureConfigurations = Collections.unmodifiableList(Persistence.readItems(memento, Persistence.TAG_ACTIVE_GESTURE_CONFIGURATION, null));
			activeKeyConfigurations = Collections.unmodifiableList(Persistence.readItems(memento, Persistence.TAG_ACTIVE_KEY_CONFIGURATION, null));
			categories = Collections.unmodifiableList(Persistence.readItems(memento, Persistence.TAG_CATEGORY, null));
			commands = Collections.unmodifiableList(Persistence.readItems(memento, Persistence.TAG_COMMAND, null));
			gestureBindings = Collections.unmodifiableList(Persistence.readGestureBindings(memento, Persistence.TAG_GESTURE_BINDING, null));
			gestureConfigurations = Collections.unmodifiableList(Persistence.readItems(memento, Persistence.TAG_GESTURE_CONFIGURATION, null));
			keyBindings = Collections.unmodifiableList(Persistence.readKeyBindings(memento, Persistence.TAG_KEY_BINDING, null));
			keyConfigurations = Collections.unmodifiableList(Persistence.readItems(memento, Persistence.TAG_KEY_CONFIGURATION, null));
			regionalGestureBindings = Collections.unmodifiableList(Persistence.readRegionalGestureBindings(memento, Persistence.TAG_REGIONAL_GESTURE_BINDING, null));
			regionalKeyBindings = Collections.unmodifiableList(Persistence.readRegionalKeyBindings(memento, Persistence.TAG_REGIONAL_KEY_BINDING, null));
			scopes = Collections.unmodifiableList(Persistence.readItems(memento, Persistence.TAG_SCOPE, null));
		} catch (WorkbenchException eWorkbench) {
			throw new IOException();
		} finally {
			reader.close();
		}
	}
	
	public void save()
		throws IOException {
		XMLMemento xmlMemento = XMLMemento.createWriteRoot(Persistence.TAG_PACKAGE);		
		Persistence.writeItems(xmlMemento, Persistence.TAG_ACTIVE_GESTURE_CONFIGURATION, activeGestureConfigurations);		
		Persistence.writeItems(xmlMemento, Persistence.TAG_ACTIVE_KEY_CONFIGURATION, activeKeyConfigurations);		
		Persistence.writeItems(xmlMemento, Persistence.TAG_CATEGORY, categories);		
		Persistence.writeItems(xmlMemento, Persistence.TAG_COMMAND, commands);
		Persistence.writeGestureBindings(xmlMemento, Persistence.TAG_GESTURE_BINDING, gestureBindings);
		Persistence.writeItems(xmlMemento, Persistence.TAG_GESTURE_CONFIGURATION, gestureConfigurations);
		Persistence.writeKeyBindings(xmlMemento, Persistence.TAG_KEY_BINDING, keyBindings);
		Persistence.writeItems(xmlMemento, Persistence.TAG_KEY_CONFIGURATION, keyConfigurations);
		Persistence.writeRegionalGestureBindings(xmlMemento, Persistence.TAG_REGIONAL_GESTURE_BINDING, regionalGestureBindings);
		Persistence.writeRegionalKeyBindings(xmlMemento, Persistence.TAG_REGIONAL_KEY_BINDING, regionalKeyBindings);
		Persistence.writeItems(xmlMemento, Persistence.TAG_SCOPE, scopes);
		IPath path = WorkbenchPlugin.getDefault().getStateLocation();
		path = path.append(PATH);
		File file = path.toFile();		
		Writer writer = new BufferedWriter(new FileWriter(file));		
		
		try {
			xmlMemento.save(writer);
		} finally {
			writer.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5411.java