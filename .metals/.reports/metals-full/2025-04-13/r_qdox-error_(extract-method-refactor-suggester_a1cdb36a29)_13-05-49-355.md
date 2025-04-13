error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1596.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1596.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1596.java
text:
```scala
r@@esponse.renderCSSReference(new GroupedAndOrderedResourceReference(ResourceGroup.GLOBAL, 0,

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.examples.resourcedecoration;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.examples.resourcedecoration.GroupedAndOrderedResourceReference.ResourceGroup;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.time.Duration;

/**
 * A demo page showing how to render grouped resources
 * 
 * @author jthomerson
 */
public class HomePage extends WicketExamplePage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param parameters
	 */
	public HomePage(final PageParameters parameters)
	{
		super(parameters);

		final WebMarkupContainer jsPlaceholder = new WebMarkupContainer("jsProofPlaceholder");
		jsPlaceholder.add(new Behavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void renderHead(Component component, IHeaderResponse response)
			{
				jsPlaceholder.setOutputMarkupId(true);
				response.renderOnDomReadyJavaScript("$('#" + jsPlaceholder.getMarkupId() +
					"').html('the ondomready script ran').css('border-color', 'green');");
			}
		});
		add(jsPlaceholder);

		add(new AjaxProofContainer("ajaxProofPlaceholder"));
		add(new AbstractAjaxTimerBehavior(Duration.seconds(4))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTimer(AjaxRequestTarget target)
			{
				HomePage.this.replace(new AjaxProofContainer("ajaxProofPlaceholder"));
				target.add(HomePage.this.get("ajaxProofPlaceholder"));
				stop();
			}
		});
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		// example of things that may be shared for all your applications across your company,

		// two CSS resources in the same group. header.css is rendered first because has lower
		// "order" number
		response.renderCSSReference(new GroupedAndOrderedResourceReference(ResourceGroup.GLOBAL, 1,
			HomePage.class, "footer.css"));
		response.renderCSSReference(new GroupedAndOrderedResourceReference(ResourceGroup.GLOBAL, 0,
			HomePage.class, "header.css"));

		response.renderJavaScriptReference(new GroupedAndOrderedResourceReference(
			ResourceGroup.GLOBAL, 0, HomePage.class, "jquery-1.4.3.min.js"));

		// example of something that may be in this single application:
		response.renderCSSReference(new GroupedAndOrderedResourceReference(
			ResourceGroup.APPLICATION, 0, HomePage.class, "app.css"));

		// example of something that may be limited to certain pages:
		response.renderCSSReference(new GroupedAndOrderedResourceReference(ResourceGroup.PAGE, 0,
			HomePage.class, "HomePage.css"));
		response.renderJavaScriptReference(new GroupedAndOrderedResourceReference(
			ResourceGroup.PAGE, 0, HomePage.class, "HomePage.js"));
	}

	private static class AjaxProofContainer extends WebMarkupContainer
		implements
			IHeaderContributor
	{
		private static final long serialVersionUID = 1L;

		public AjaxProofContainer(String id)
		{
			super(id);
			setOutputMarkupId(true);
		}

		@Override
		public void renderHead(IHeaderResponse response)
		{
			if (AjaxRequestTarget.get() != null)
			{
				response.renderCSSReference(new PackageResourceReference(HomePage.class, "ajax.css"));
				response.renderJavaScriptReference(new PackageResourceReference(HomePage.class,
					"ajax.js"));
				response.renderOnDomReadyJavaScript("updatePending();");
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1596.java