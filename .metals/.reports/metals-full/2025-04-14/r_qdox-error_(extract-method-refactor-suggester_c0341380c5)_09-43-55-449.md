error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2721.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2721.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 571
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2721.java
text:
```scala
final class Persistence {

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

p@@ackage org.eclipse.ui.internal.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.internal.util.Util;

class Persistence {

	final static String PACKAGE_BASE = "commands"; //$NON-NLS-1$
	final static String PACKAGE_FULL = "org.eclipse.ui." + PACKAGE_BASE; //$NON-NLS-1$
	final static String TAG_COMMAND = "command"; //$NON-NLS-1$	
	final static String TAG_COMMAND_ID = "commandId"; //$NON-NLS-1$	
	final static String TAG_CONTEXT_BINDING = "contextBinding"; //$NON-NLS-1$	
	final static String TAG_CONTEXT_ID = "contextId"; //$NON-NLS-1$	
	final static String TAG_DESCRIPTION = "description"; //$NON-NLS-1$
	final static String TAG_ID = "id"; //$NON-NLS-1$
	final static String TAG_IMAGE_BINDING = "imageBinding"; //$NON-NLS-1$	
	final static String TAG_IMAGE_STYLE = "imageStyle"; //$NON-NLS-1$	
	final static String TAG_IMAGE_URI = "imageUri"; //$NON-NLS-1$	
	final static String TAG_LOCALE = "locale"; //$NON-NLS-1$	
	final static String TAG_NAME = "name"; //$NON-NLS-1$	
	final static String TAG_PARENT_ID = "parentId"; //$NON-NLS-1$
	final static String TAG_PLATFORM = "platform"; //$NON-NLS-1$	
	final static String TAG_PLUGIN_ID = "pluginId"; //$NON-NLS-1$

	static CommandElement readCommandElement(IMemento memento, String pluginIdOverride)
		throws IllegalArgumentException {
		if (memento == null)
			throw new IllegalArgumentException();			

		String description = memento.getString(TAG_DESCRIPTION);
		String id = memento.getString(TAG_ID);

		if (id == null)
			id = Util.ZERO_LENGTH_STRING;
		
		String name = memento.getString(TAG_NAME);

		if (name == null)
			name = Util.ZERO_LENGTH_STRING;
		
		String parentId = memento.getString(TAG_PARENT_ID);
		String pluginId = pluginIdOverride != null ? pluginIdOverride : memento.getString(TAG_PLUGIN_ID);
		return CommandElement.create(description, id, name, parentId, pluginId);
	}

	static List readCommandElements(IMemento memento, String name, String pluginIdOverride)
		throws IllegalArgumentException {		
		if (memento == null || name == null)
			throw new IllegalArgumentException();			
	
		IMemento[] mementos = memento.getChildren(name);
	
		if (mementos == null)
			throw new IllegalArgumentException();
	
		List list = new ArrayList(mementos.length);
	
		for (int i = 0; i < mementos.length; i++)
			list.add(readCommandElement(mementos[i], pluginIdOverride));
	
		return list;				
	}

	static ContextBindingElement readContextBindingElement(IMemento memento, String pluginIdOverride)
		throws IllegalArgumentException {
		if (memento == null)
			throw new IllegalArgumentException();			

		String commandId = memento.getString(TAG_COMMAND_ID);

		if (commandId == null)
			commandId = Util.ZERO_LENGTH_STRING;

		String contextId = memento.getString(TAG_CONTEXT_ID);

		if (contextId == null)
			contextId = Util.ZERO_LENGTH_STRING;

		String pluginId = pluginIdOverride != null ? pluginIdOverride : memento.getString(TAG_PLUGIN_ID);
		return ContextBindingElement.create(commandId, contextId, pluginId);
	}

	static List readContextBindingElements(IMemento memento, String name, String pluginIdOverride)
		throws IllegalArgumentException {		
		if (memento == null || name == null)
			throw new IllegalArgumentException();			
	
		IMemento[] mementos = memento.getChildren(name);
	
		if (mementos == null)
			throw new IllegalArgumentException();
	
		List list = new ArrayList(mementos.length);
	
		for (int i = 0; i < mementos.length; i++)
			list.add(readContextBindingElement(mementos[i], pluginIdOverride));
	
		return list;				
	}

	static ImageBindingElement readImageBindingElement(IMemento memento, String pluginIdOverride)
		throws IllegalArgumentException {
		if (memento == null)
			throw new IllegalArgumentException();			

		String commandId = memento.getString(TAG_COMMAND_ID);

		if (commandId == null)
			commandId = Util.ZERO_LENGTH_STRING;

		String imageStyle = memento.getString(TAG_IMAGE_STYLE);

		if (imageStyle == null)
			imageStyle = Util.ZERO_LENGTH_STRING;

		String imageUri = memento.getString(TAG_IMAGE_URI);

		if (imageUri == null)
			imageUri = Util.ZERO_LENGTH_STRING;

		String locale = memento.getString(TAG_LOCALE);

		if (locale == null)
			locale = Util.ZERO_LENGTH_STRING;

		String platform = memento.getString(TAG_PLATFORM);

		if (platform == null)
			platform = Util.ZERO_LENGTH_STRING;

		String pluginId = pluginIdOverride != null ? pluginIdOverride : memento.getString(TAG_PLUGIN_ID);
		return ImageBindingElement.create(commandId, imageStyle, imageUri, locale, platform, pluginId);
	}

	static List readImageBindingElements(IMemento memento, String name, String pluginIdOverride)
		throws IllegalArgumentException {		
		if (memento == null || name == null)
			throw new IllegalArgumentException();			
	
		IMemento[] mementos = memento.getChildren(name);
	
		if (mementos == null)
			throw new IllegalArgumentException();
	
		List list = new ArrayList(mementos.length);
	
		for (int i = 0; i < mementos.length; i++)
			list.add(readImageBindingElement(mementos[i], pluginIdOverride));
	
		return list;				
	}

	static void writeCommandElement(IMemento memento, CommandElement commandElement)
		throws IllegalArgumentException {
		if (memento == null || commandElement == null)
			throw new IllegalArgumentException();

		memento.putString(TAG_DESCRIPTION, commandElement.getDescription());
		memento.putString(TAG_ID, commandElement.getId());
		memento.putString(TAG_NAME, commandElement.getName());
		memento.putString(TAG_PARENT_ID, commandElement.getParentId());
		memento.putString(TAG_PLUGIN_ID, commandElement.getPluginId());
	}

	static void writeCommandElements(IMemento memento, String name, List commandElements)
		throws IllegalArgumentException {
		if (memento == null || name == null || commandElements == null)
			throw new IllegalArgumentException();
		
		commandElements = new ArrayList(commandElements);
		Iterator iterator = commandElements.iterator();
		
		while (iterator.hasNext()) 
			if (!(iterator.next() instanceof CommandElement))
				throw new IllegalArgumentException();

		iterator = commandElements.iterator();

		while (iterator.hasNext()) 
			writeCommandElement(memento.createChild(name), (CommandElement) iterator.next());
	}

	static void writeContextBindingElement(IMemento memento, ContextBindingElement contextBindingElement)
		throws IllegalArgumentException {
		if (memento == null || contextBindingElement == null)
			throw new IllegalArgumentException();

		memento.putString(TAG_COMMAND_ID, contextBindingElement.getCommandId());
		memento.putString(TAG_CONTEXT_ID, contextBindingElement.getContextId());
		memento.putString(TAG_PLUGIN_ID, contextBindingElement.getPluginId());
	}

	static void writeContextBindingElements(IMemento memento, String name, List contextBindingElements)
		throws IllegalArgumentException {
		if (memento == null || name == null || contextBindingElements == null)
			throw new IllegalArgumentException();
		
		contextBindingElements = new ArrayList(contextBindingElements);
		Iterator iterator = contextBindingElements.iterator();
		
		while (iterator.hasNext()) 
			if (!(iterator.next() instanceof ContextBindingElement))
				throw new IllegalArgumentException();

		iterator = contextBindingElements.iterator();

		while (iterator.hasNext()) 
			writeContextBindingElement(memento.createChild(name), (ContextBindingElement) iterator.next());
	}

	static void writeImageBindingElement(IMemento memento, ImageBindingElement imageBindingElement)
		throws IllegalArgumentException {
		if (memento == null || imageBindingElement == null)
			throw new IllegalArgumentException();

		memento.putString(TAG_COMMAND_ID, imageBindingElement.getCommandId());
		memento.putString(TAG_IMAGE_STYLE, imageBindingElement.getImageStyle());
		memento.putString(TAG_IMAGE_URI, imageBindingElement.getImageUri());
		memento.putString(TAG_LOCALE, imageBindingElement.getLocale());
		memento.putString(TAG_PLATFORM, imageBindingElement.getPlatform());
		memento.putString(TAG_PLUGIN_ID, imageBindingElement.getPluginId());
	}

	static void writeImageBindingElements(IMemento memento, String name, List imageBindingElements)
		throws IllegalArgumentException {
		if (memento == null || name == null || imageBindingElements == null)
			throw new IllegalArgumentException();
		
		imageBindingElements = new ArrayList(imageBindingElements);
		Iterator iterator = imageBindingElements.iterator();
		
		while (iterator.hasNext()) 
			if (!(iterator.next() instanceof ImageBindingElement))
				throw new IllegalArgumentException();

		iterator = imageBindingElements.iterator();

		while (iterator.hasNext()) 
			writeImageBindingElement(memento.createChild(name), (ImageBindingElement) iterator.next());
	}

	private Persistence() {
		super();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2721.java