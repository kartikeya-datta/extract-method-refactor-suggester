error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8161.java
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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.IMemento;

final class ConfigurationElementMemento implements IMemento {

	static ConfigurationElementMemento create(IConfigurationElement configurationElement)
		throws IllegalArgumentException {
		return new ConfigurationElementMemento(configurationElement);
	}		

	private IConfigurationElement configurationElement;

	private ConfigurationElementMemento(IConfigurationElement configurationElement)
		throws IllegalArgumentException {
		super();
		
		if (configurationElement == null)
			throw new IllegalArgumentException();
		
		this.configurationElement = configurationElement;
	}

	public IMemento createChild(String type) {	
		return null;
	}

	public IMemento createChild(String type, String id) {
		return null;
	}

	public IMemento getChild(String type) {
		IConfigurationElement[] configurationElements = configurationElement.getChildren(type);
		
		if (configurationElements != null && configurationElements.length >= 1)
			return create(configurationElements[0]);
		
		return null;
	}

	public IMemento[] getChildren(String type) {
		IConfigurationElement[] configurationElements = configurationElement.getChildren(type);
		
		if (configurationElements != null && configurationElements.length >= 1) {
			IMemento mementos[] = new ConfigurationElementMemento[configurationElements.length];
			
			for (int i = 0; i < configurationElements.length; i++)
				mementos[i] = create(configurationElements[i]);
				
			return mementos;			
		}
		
		return new IMemento[0];
	}

	public Float getFloat(String key) {
		String string = configurationElement.getAttribute(key);
		
		if (string != null)
			try {
				return new Float(string);
			} catch (NumberFormatException eNumberFormat) {
			}
		
		return null;
	}

	public String getID() {
		return configurationElement.getAttribute(TAG_ID);
	}

	public Integer getInteger(String key) {
		String string = configurationElement.getAttribute(key);
		
		if (string != null)
			try {
				return new Integer(string);
			} catch (NumberFormatException eNumberFormat) {
			}
		
		return null;
	}

	public String getString(String key) {
		return configurationElement.getAttribute(key);
	}

	public String getTextData() {
		return configurationElement.getValue();
	}

	public void putFloat(String key, float value) {
	}

	public void putInteger(String key, int value) {
	}

	public void putMemento(IMemento memento) {
	}

	public void putString(String key, String value) {
	}

	public void putTextData(String data) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8161.java