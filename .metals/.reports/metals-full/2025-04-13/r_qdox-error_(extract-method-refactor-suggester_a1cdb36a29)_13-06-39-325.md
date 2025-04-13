error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4307.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4307.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4307.java
text:
```scala
public S@@tring getContextId() {

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.keys.KeySequence;

public final class KeySequenceBindingDefinition
	implements IKeySequenceBindingDefinition {

	private final static int HASH_FACTOR = 89;
	private final static int HASH_INITIAL =
		KeySequenceBindingDefinition.class.getName().hashCode();

	static Map keySequenceBindingDefinitionsByCommandId(Collection keySequenceBindingDefinitions) {
		if (keySequenceBindingDefinitions == null)
			throw new NullPointerException();

		Map map = new HashMap();
		Iterator iterator = keySequenceBindingDefinitions.iterator();

		while (iterator.hasNext()) {
			Object object = iterator.next();
			Util.assertInstance(object, IKeySequenceBindingDefinition.class);
			IKeySequenceBindingDefinition keySequenceBindingDefinition =
				(IKeySequenceBindingDefinition) object;
			String commandId = keySequenceBindingDefinition.getCommandId();

			if (commandId != null) {
				Collection keySequenceBindingDefinitions2 =
					(Collection) map.get(commandId);

				if (keySequenceBindingDefinitions2 == null) {
					keySequenceBindingDefinitions2 = new ArrayList();
					map.put(commandId, keySequenceBindingDefinitions2);
				}

				keySequenceBindingDefinitions2.add(
					keySequenceBindingDefinition);
			}
		}

		return map;
	}

	private String activityId;
	private String commandId;

	private transient int hashCode;
	private transient boolean hashCodeComputed;
	private String keyConfigurationId;
	private KeySequence keySequence;
	private String locale;
	private String platform;
	private String pluginId;
	private transient String string;

	public KeySequenceBindingDefinition(
		String activityId,
		String commandId,
		String keyConfigurationId,
		KeySequence keySequence,
		String locale,
		String platform,
		String pluginId) {
		this.activityId = activityId;
		this.commandId = commandId;
		this.keyConfigurationId = keyConfigurationId;
		this.keySequence = keySequence;
		this.locale = locale;
		this.platform = platform;
		this.pluginId = pluginId;
	}

	public int compareTo(Object object) {
		KeySequenceBindingDefinition castedObject =
			(KeySequenceBindingDefinition) object;
		int compareTo = Util.compare(activityId, castedObject.activityId);

		if (compareTo == 0) {
			compareTo = Util.compare(commandId, castedObject.commandId);

			if (compareTo == 0) {
				compareTo =
					Util.compare(
						keyConfigurationId,
						castedObject.keyConfigurationId);

				if (compareTo == 0) {
					compareTo =
						Util.compare(keySequence, castedObject.keySequence);

					if (compareTo == 0) {
						compareTo = Util.compare(locale, castedObject.locale);

						if (compareTo == 0) {
							compareTo =
								Util.compare(platform, castedObject.platform);

							if (compareTo == 0)
								compareTo =
									Util.compare(
										pluginId,
										castedObject.pluginId);
						}
					}
				}
			}
		}

		return compareTo;
	}

	public boolean equals(Object object) {
		if (!(object instanceof KeySequenceBindingDefinition))
			return false;

		KeySequenceBindingDefinition castedObject =
			(KeySequenceBindingDefinition) object;
		boolean equals = true;
		equals &= Util.equals(activityId, castedObject.activityId);
		equals &= Util.equals(commandId, castedObject.commandId);
		equals
			&= Util.equals(keyConfigurationId, castedObject.keyConfigurationId);
		equals &= Util.equals(keySequence, castedObject.keySequence);
		equals &= Util.equals(locale, castedObject.locale);
		equals &= Util.equals(platform, castedObject.platform);
		equals &= Util.equals(pluginId, castedObject.pluginId);
		return equals;
	}

	public String getActivityId() {
		return activityId;
	}

	public String getCommandId() {
		return commandId;
	}

	public String getKeyConfigurationId() {
		return keyConfigurationId;
	}

	public KeySequence getKeySequence() {
		return keySequence;
	}

	public String getLocale() {
		return locale;
	}

	public String getPlatform() {
		return platform;
	}

	public String getPluginId() {
		return pluginId;
	}

	public int hashCode() {
		if (!hashCodeComputed) {
			hashCode = HASH_INITIAL;
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(activityId);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(commandId);
			hashCode =
				hashCode * HASH_FACTOR + Util.hashCode(keyConfigurationId);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(keySequence);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(locale);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(platform);
			hashCode = hashCode * HASH_FACTOR + Util.hashCode(pluginId);
			hashCodeComputed = true;
		}

		return hashCode;
	}

	public String toString() {
		if (string == null) {
			final StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append('[');
			stringBuffer.append(activityId);
			stringBuffer.append(',');
			stringBuffer.append(commandId);
			stringBuffer.append(',');
			stringBuffer.append(keyConfigurationId);
			stringBuffer.append(',');
			stringBuffer.append(keySequence);
			stringBuffer.append(',');
			stringBuffer.append(locale);
			stringBuffer.append(',');
			stringBuffer.append(platform);
			stringBuffer.append(',');
			stringBuffer.append(pluginId);
			stringBuffer.append(']');
			string = stringBuffer.toString();
		}

		return string;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4307.java