error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2635.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2635.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[99,2]

error in qdox parser
file content:
```java
offset: 2448
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2635.java
text:
```scala
package wicket.extensions.markup.html.beanedit;

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.wizard.framework.beanedit;

import java.beans.PropertyDescriptor;

import wicket.Component;
import wicket.model.Model;

/**
 * <p>
 * Replacement model that returns 'disabled' when the editMode == EditMode.MODE_READ_ONLY
 * or the property descriptor has no write method. Use this model with AttributeModifier
 * 'disabled'.
 * </p>
 * <p>
 * Be sure NOT to have the 'disabled' attribute in your markup, and create
 * the AttributeModifier with constructor parameter 'addAttributeIfNotPresent' true.
 * </p>
 *
 * @author Eelco Hillenius
 */
public final class EditModeReplacementModel extends Model
{
	/** edit mode. */
	private final EditMode editMode;

	/** property descriptor. */
	private final PropertyDescriptor descriptor;

	/**
	 * Construct.
	 * @param editMode edit mode
	 */
	public EditModeReplacementModel(EditMode editMode)
	{
		super();
		this.editMode = editMode;
		this.descriptor = null;
	}

	/**
	 * Construct.
	 * @param descriptor property descriptor
	 */
	public EditModeReplacementModel(PropertyDescriptor descriptor)
	{
		super();
		this.descriptor = descriptor;
		this.editMode = null;
	}

	/**
	 * Construct.
	 * @param editMode edit mode
	 * @param descriptor property descriptor
	 */
	public EditModeReplacementModel(EditMode editMode, PropertyDescriptor descriptor)
	{
		super();
		this.editMode = editMode;
		this.descriptor = descriptor;
	}

	/**
	 * @see wicket.model.IModel#getObject(wicket.Component)
	 */
	public Object getObject(Component component)
	{
		if(editMode != null)
		{
			return (editMode == EditMode.READ_ONLY) ? "disabled" : null;
		}
		if(descriptor != null)
		{
			return (descriptor.getWriteMethod() == null) ? "disabled" : null;
		}
		return null;
	}
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2635.java