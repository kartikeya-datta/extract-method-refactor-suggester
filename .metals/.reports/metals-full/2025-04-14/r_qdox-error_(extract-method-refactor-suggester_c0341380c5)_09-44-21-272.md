error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10794.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10794.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10794.java
text:
```scala
r@@eturn (content != null) ? (String)content.getObject() : null;

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.extensions.wizard;

import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * A wizard step that displays the provided static content without expecting any
 * input.
 * 
 * @author eelcohillenius
 */
public class StaticContentStep extends WizardStep<String>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Whether HTML codes should be rendered as is (true), or should be escaped
	 * (false).
	 */
	private final boolean allowHtml;

	/** The model that provided the actual content. */
	private IModel<String> content;

	/**
	 * Constructor for if you want to set all the properties yourself.
	 * 
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is.
	 *            Otherwise, it will be escaped.
	 */
	public StaticContentStep(boolean allowHtml)
	{
		this.allowHtml = allowHtml;
	}

	/**
	 * Construct.
	 * 
	 * @param title
	 *            The title of this step
	 * @param summary
	 *            The summary of this step
	 * @param content
	 *            The content of the step panel
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is.
	 *            Otherwise, it will be escaped.
	 */
	public StaticContentStep(IModel<String> title, IModel<String> summary, IModel<String> content,
			boolean allowHtml)
	{
		super(title, summary);
		this.content = content;
		this.allowHtml = allowHtml;
	}

	/**
	 * Construct.
	 * 
	 * @param title
	 *            The title of this step
	 * @param summary
	 *            The summary of this step
	 * @param content
	 *            The content of the step panel
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is.
	 *            Otherwise, it will be escaped.
	 */
	public StaticContentStep(IModel<String> title, IModel<String> summary, String content,
			boolean allowHtml)
	{
		this(title, summary, new Model<String>(content), allowHtml);
	}

	/**
	 * Construct.
	 * 
	 * @param title
	 *            The title of this step
	 * @param summary
	 *            The summary of this step
	 * @param content
	 *            The content of the step panel
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is.
	 *            Otherwise, it will be escaped.
	 */
	public StaticContentStep(String title, String summary, IModel<String> content, boolean allowHtml)
	{
		this(new Model<String>(title), new Model<String>(summary), content, allowHtml);
	}

	/**
	 * Construct.
	 * 
	 * @param title
	 *            The title of this step
	 * @param summary
	 *            The summary of this step
	 * @param content
	 *            The content of the step panel
	 * @param allowHtml
	 *            If true, any html of the content will be rendered as is.
	 *            Otherwise, it will be escaped.
	 */
	public StaticContentStep(String title, String summary, String content, boolean allowHtml)
	{
		this(title, summary, new Model<String>(content), allowHtml);
	}

	/**
	 * Gets whether html is allowed as output.
	 * 
	 * @return Whether html is allowed as output
	 */
	public final boolean getAllowHtml()
	{
		return allowHtml;
	}

	/**
	 * Gets the content from the content model.
	 * 
	 * @return The content
	 */
	public final String getContent()
	{
		return (content != null) ? (String)content.getObject(null) : null;
	}

	/**
	 * Gets the content model.
	 * 
	 * @return The content model
	 */
	public final IModel<String> getContentModel()
	{
		return content;
	}

	/**
	 * Sets the content model.
	 * 
	 * @param content
	 *            The content model
	 */
	public final void setContentModel(IModel<String> content)
	{
		this.content = content;
	}

	/**
	 * @see wicket.extensions.wizard.WizardStep#populate(wicket.markup.html.panel.Panel)
	 */
	@Override
	protected void populate(Panel contentPanel)
	{
		new Label(contentPanel, "content", content).setEscapeModelStrings(!allowHtml);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10794.java