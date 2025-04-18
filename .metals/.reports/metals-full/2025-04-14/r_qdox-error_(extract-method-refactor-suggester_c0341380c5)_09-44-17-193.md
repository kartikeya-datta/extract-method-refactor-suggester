error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10337.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10337.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10337.java
text:
```scala
r@@enderHeaderSections(getPage());

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar 2006) eelco12 $
 * $Revision: 5004 $
 * $Date: 2006-03-17 20:47:08 -0800 (Fri, 17 Mar 2006) $
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
package wicket.markup.html.internal;

import wicket.MarkupContainer;
import wicket.Response;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.response.StringResponse;


/**
 * The HtmlHeaderContainer is automatically created and added to the component
 * hierarchy by a HtmlHeaderResolver instance. HtmlHeaderContainer tries to
 * handle/render the &gt;head&gt; tag and its body. However depending on the
 * parent component, the behavior must be different. E.g. if parent component is
 * a Page all components of the page's hierarchy must be asked if they have
 * something to contribute to the &lt;head&gt; section of the html response. If
 * yes, it must <b>immediately</b> be rendered.
 * <p>
 * &lt;head&gt; regions may contain additional wicket components, which can be
 * added by means of add(Component) as usual.
 * <p>
 * &lt;wicket:head&gt; tags are handled by simple WebMarkupContainers also
 * created by a HtmlHeaderResolver.
 * <p>
 * <ul>
 * <li> &lt;head&gt; will be inserted in output automatically if required</li>
 * <li> &lt;head&gt; is <b>not</b> a wicket specific tag and you must use add()
 * to add components referenced in body of the head tag</li>
 * <li> &lt;head&gt; is supported by panels, borders and inherited markup, but
 * is <b>not</b> copied to the output. They are for previewability only (except
 * on Pages)</li>
 * <li> &lt;wicket:head&gt; does not make sense in page markup (but does in
 * inherited page markup)</li>
 * <li> &lt;wicket:head&gt; makes sense in Panels, Borders and inherited markup
 * (of Panels, Borders and Pages)</li>
 * <li> components within &lt;wicket:head&gt; must be added by means of add(),
 * like allways with Wicket. No difference.</li>
 * <li> &lt;wicket:head&gt; and it's content is copied to the output. Components
 * contained in &lt;wicket.head&gt; are rendered as usual</li>
 * </ul>
 * 
 * @author Juergen Donnerstag
 * @author Janne Hietam&auml;ki
 */
public class HtmlHeaderContainer extends HeaderContainer
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construct.
	 * 
	 * @param parent
	 * @param id
	 */
	public HtmlHeaderContainer(final MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * First render the body of the component. And if it is the header component
	 * of a Page (compared to a Panel or Border), than get the header sections
	 * from all component in the hierachie and render them as well.
	 * 
	 * @see wicket.MarkupContainer#onComponentTagBody(wicket.markup.MarkupStream,
	 *      wicket.markup.ComponentTag)
	 */
	@Override
	protected final void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		// We are able to automatically add <head> to the page if it is
		// missing. But we only want to add it, if we have content to be
		// written to its body. Thus we first write the output into a
		// StringResponse and if not empty, we copy it to the original
		// web response.

		// Temporarily replace the web response with a String response
		final Response webResponse = this.getResponse();

		try
		{
			final StringResponse response = new StringResponse();
			this.getRequestCycle().setResponse(response);

			// In any case, first render the header section directly associated
			// with the markup
			super.onComponentTagBody(markupStream, openTag);

			renderHeaderSections(getPage(), this);

			// Automatically add <head> if necessary
			CharSequence output = response.getBuffer();
			if (output.length() > 0)
			{
				if (output.charAt(0) == '\r')
				{
					for (int i = 2; i < output.length(); i += 2)
					{
						char ch = output.charAt(i);
						if (ch != '\r')
						{
							output = output.subSequence(i - 2, output.length());
							break;
						}
					}
				}
				else if (output.charAt(0) == '\n')
				{
					for (int i = 1; i < output.length(); i++)
					{
						char ch = output.charAt(i);
						if (ch != '\n')
						{
							output = output.subSequence(i - 1, output.length());
							break;
						}
					}
				}
			}

			if (output.length() > 0)
			{
				webResponse.write("<head>");
				webResponse.write(output);
				webResponse.write("</head>");
			}
		}
		finally
		{
			// Restore the original response
			this.getRequestCycle().setResponse(webResponse);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10337.java