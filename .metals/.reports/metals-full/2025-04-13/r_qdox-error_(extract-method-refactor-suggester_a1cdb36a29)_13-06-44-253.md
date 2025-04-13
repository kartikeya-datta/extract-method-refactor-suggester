error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12479.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12479.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,27]

error in qdox parser
file content:
```java
offset: 27
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12479.java
text:
```scala
"javascript:validate('" + g@@etCallbackUrl() + "', '" + formComponent.getInput() + "', this);";

/*
 * $Id$
 * $Revision$
 * $Date$
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
package wicket.markup.html.ajax.dojo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Component;
import wicket.feedback.IFeedback;
import wicket.markup.ComponentTag;
import wicket.markup.html.HtmlHeaderContainer;
import wicket.markup.html.form.FormComponent;
import wicket.util.resource.IResourceStream;
import wicket.util.resource.StringBufferResourceStream;
import wicket.util.value.ValueMap;

/**
 * WORK IN PROGRESS; DO NOT USE THIS CLASS YET!
 * TODO finish it.
 *
 * Handles event requests, like AJAX (XmlHttp) requests.
 *
 * @author Eelco Hillenius
 */
public final class ValidationAjaxHandler extends DojoAjaxHandler
{
	/** log. */
	private static Log log = LogFactory.getLog(ValidationAjaxHandler.class);

	/** name event, like onblur. */
	private final String eventName;

	/** component this handler is attached to. */
	private FormComponent formComponent;

	/**
	 * Construct.
	 * @param eventName name of the event to attach to, e.g. 'onchange'
	 */
	public ValidationAjaxHandler(String eventName)
	{
		this.eventName = eventName;
	}

	/**
	 * Gets the name of the event to attach to.
	 * @return the name of the event to attach to
	 */
	public final String getEventName()
	{
		return eventName;
	}

	/**
	 * @see wicket.AjaxHandler#renderHeadContribution(wicket.markup.html.HtmlHeaderContainer)
	 */
	public final void renderHeadContribution(HtmlHeaderContainer container)
	{
		String s =

			"\t<script language=\"JavaScript\" type=\"text/javascript\">\n" +
			"\tfunction validate(componentUrl, componentPath, field) { \n" +
			"\t\tdojo.io.bind({\n" +
			"\t\t\turl: componentUrl + '&' + componentPath + '=' + field.value,\n" +
			"\t\t\tmimetype: \"text/plain\",\n" +
			"\t\t\tload: function(type, data, evt) {\n" +
			"\t\t\t\talert(data);\n" +
			"\t\t\t}\n" +
			"\t\t});\n" +
			"\t}\n" +
			"\t</script>\n";

		container.getResponse().write(s);
	}

	/**
	 * Attaches the event handler for the given component to the given tag.
	 * @param tag
	 *            The tag to attache
	 */
	public final void onComponentTag(final ComponentTag tag)
	{
		final ValueMap attributes = tag.getAttributes();
		final String attributeValue =
			"javascript:validate('" + getCallbackUrl() + "', '" + formComponent.getPath() + "', this);";
		attributes.put(getEventName(), attributeValue);
	}

	/**
	 * @see wicket.EventRequestHandler#getResourceStream()
	 */
	/**
	 * Gets the resource to render to the requester.
	 * @return the resource to render to the requester
	 */
	protected final IResourceStream getResponse()
	{
		StringBufferResourceStream s = new StringBufferResourceStream();

		formComponent.validate();

		// When validation failed...
		if (!formComponent.isValid())
		{
			//TODO finish
			// The plan here is the visit all feedback components, re-render them, and
			// return the render results to the browser with the components (top level)
			// ids attached. We could then use this information to replace the dom
			// elements in the browser

			// We need a couple of things for this to work first:
			// 1) The ability to let a component render on its' own
			// 2) Trap that render result somewhere. Either by setting the response to
			//			render to on that component, or passing a response as a parameter
			//			of the render call
			// Furthermore, we need to have the javascript side covered. That could
			// be tricky too, but the cool thing about that is that if we would fix
			// that in a generic fashion, our ajax support would be pretty usable at once

			formComponent.getPage().visitChildren(IFeedback.class, new Component.IVisitor()
			{
				public Object component(Component component)
				{
					// this doesn't work yet.
					//component.render();
					return Component.IVisitor.CONTINUE_TRAVERSAL;
				}
			});
		}

		// for now, just display a simple message
		s.append("ajax validation executed");

		return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12479.java