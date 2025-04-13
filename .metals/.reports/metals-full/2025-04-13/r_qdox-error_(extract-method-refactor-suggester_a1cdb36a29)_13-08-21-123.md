error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12796.java
text:
```scala
s@@b.append(form.getHiddenFieldId());

/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.markup.html.form;

import wicket.markup.ComponentTag;
import wicket.model.IModel;

/**
 * A link which can be used exactly like a Button to submit a Form. The href of
 * the link will use JavaScript to submit the form.
 * 
 * <p>
 * You can use this class 2 ways. First with the constructor without a Form
 * object then this Link must be inside a Form so that it knows what form to
 * submit to. Second way is to use the Form constructor then that form will be
 * used to submit to.
 * </p>
 * 
 * <pre>
 *             Form f = new Form(&quot;linkForm&quot;, new CompoundPropertyModel(mod));
 *             f.add(new TextField(&quot;value1&quot;));
 *             f.add(new SubmitLink(&quot;link1&quot;) {
 *                 protected void onSubmit() {
 *                     System.out.println(&quot;Link1 was clicked, value1 is: &quot;
 *                             + mod.getValue1());
 *                 };
 *             });
 *             add(new SubmitLink(&quot;link2&quot;,f) {
 *                 protected void onSubmit() {
 *                     System.out.println(&quot;Link2 was clicked, value1 is: &quot;
 *                             + mod.getValue1());
 *                 };
 *             });
 *      
 *          &lt;form wicket:id=&quot;linkForm&quot; &gt;
 *             &lt;input wicket:id=&quot;value1&quot; type=&quot;text&quot; size=&quot;30&quot;/&gt;
 *             &lt;a wicket:id=&quot;link1&quot;&gt;Press link1 to submit&lt;/a&gt;
 *             &lt;input type=&quot;submit&quot; value=&quot;Send&quot;/&gt;
 *         &lt;/form&gt;
 *           &lt;a wicket:id=&quot;link2&quot;&gt;Press link 2 to submit&lt;/a&gt;
 *         
 * </pre>
 * 
 * @author chris
 * @author jcompagner
 * @author Igor Vaynberg (ivaynberg)
 */
public class SubmitLink extends Button
{
	private static final long serialVersionUID = 1L;

	private Form form;

	/**
	 * With this constructor the SubmitLink must be inside a Form.
	 * 
	 * @param id
	 *            The id of the submitlink.
	 */
	public SubmitLink(String id)
	{
		super(id);
	}

	/**
	 * With this constructor the SubmitLink must be inside a Form.
	 * 
	 * @param id
	 *            The id of the submitlink.
	 * @param model
	 *            The model for this submitlink, It won't be used by the submit
	 *            link itself, but it can be used for keeping state
	 */
	public SubmitLink(String id, IModel model)
	{
		super(id, model);
	}


	/**
	 * With this constructor the SubmitLink will submit the {@link Form} that is
	 * given when the link is clicked on.
	 * 
	 * The SubmitLink doesn't have to be in inside the {@link Form}. But
	 * currently if it is outside the {@link Form} and the SubmitLink will be
	 * rendered first. Then the {@link Form} will have a generated
	 * javascript/css id. The markup javascript/css id that can exist will be
	 * overridden.
	 * 
	 * @param id
	 *            The id of the submitlink.
	 * @param form
	 *            The form which this submitlink must submit.
	 */
	public SubmitLink(String id, Form form)
	{
		super(id);
		this.form = form;
	}

	/**
	 * With this constructor the SubmitLink will submit the {@link Form} that is
	 * given when the link is clicked on.
	 * 
	 * The SubmitLink doesn't have to be in inside the {@link Form}. But
	 * currently if it is outside the {@link Form} and the SubmitLink will be
	 * rendered first. Then the {@link Form} will have a generated
	 * javascript/css id. The markup javascript/css id that can exist will be
	 * overridden.
	 * 
	 * @param id
	 *            The id of the submitlink.
	 * @param model
	 *            The model for this submitlink, It won't be used by the submit
	 *            link itself, but it can be used for keeping state
	 * @param form
	 *            The form which this submitlink must submit.
	 */
	public SubmitLink(String id, IModel model, Form form)
	{
		super(id);
		this.form = form;
	}

	/**
	 * @inheritDoc
	 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
	 */
	protected void onComponentTag(ComponentTag tag)
	{
		// If we're disabled
		if (!isEnabled())
		{
			// if the tag is an anchor proper
			if (tag.getName().equalsIgnoreCase("a"))
			{
				// Change anchor link to span tag
				tag.setName("span");

				// Remove any href from the old link
				tag.remove("href");
			}
			else
			{
				// Remove any onclick design time code
				tag.remove("onclick");
			}
		}
		else
		{
			if (tag.getName().equalsIgnoreCase("a"))
			{
				tag.put("href", "#");
			}
			tag.put("onclick", getTriggerJavaScript());
		}
	}

	/**
	 * The javascript which trigges this link
	 * 
	 * @return The javascript
	 */
	private String getTriggerJavaScript()
	{
		Form form = getForm();
		StringBuffer sb = new StringBuffer(100);
		sb.append("document.getElementById('");
		sb.append(form.getHiddenFieldId(Form.HIDDEN_FIELD_FAKE_SUBMIT));
		sb.append("').name=\'");
		sb.append(getInputName());
		sb.append("';");

		sb.append("var f=document.getElementById('");
		sb.append(form.getJavascriptId());
		sb.append("');");

		if (shouldInvokeJavascriptFormOnsubmit())
		{

			sb.append("if (f.onsubmit != undefined) { if (f.onsubmit()==false) return false; }");
		}


		sb.append("f.submit();return false;");
		return sb.toString();
	}

	/**
	 * @return the Form for which this submit link submits
	 */
	public final Form getForm()
	{
		if (form == null)
		{
			form = super.getForm();
		}
		return form;
	}

	/**
	 * Controls whether or not clicking on this link will invoke form's
	 * javascript onsubmit handler. True by default.
	 * 
	 * @return true if form's javascript onsubmit handler should be invoked,
	 *         false otherwise
	 */
	protected boolean shouldInvokeJavascriptFormOnsubmit()
	{
		return true;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12796.java