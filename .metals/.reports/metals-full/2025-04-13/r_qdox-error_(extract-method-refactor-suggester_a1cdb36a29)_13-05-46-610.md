error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11291.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11291.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11291.java
text:
```scala
public v@@oid detach(Component c)

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
package org.apache.wicket.velocity;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

/**
 * An IHeaderContributor implementation that renders a velocity template and writes it to the
 * response. The template is loaded via Velocity's resource loading mechanism, as defined in your
 * velocity.properties. If you do not have a velocity.properties for your app, it will default to a
 * directory "templates" in the root of your app.
 */
public class VelocityContributor extends AbstractBehavior implements IHeaderContributor
{
	private static final long serialVersionUID = 1L;

	private String encoding = "ISO-8859-1";

	private final IModel< ? extends Map< ? , ? >> model;

	private final String templateName;

	/**
	 * The templateName needs to have the full path relative to where the resource loader starts
	 * looking. For example, if there is a template next to this class in the package called foo.vm,
	 * and you have configured the ClassPathResourceLoader, template name will then be
	 * "wicket/contrib/util/resource/foo.vm". Wicket provides a nice utility
	 * {@link org.apache.wicket.util.lang.Packages} for this.
	 * 
	 * 
	 * @param templateName
	 * @param model
	 */
	public VelocityContributor(String templateName, final IModel< ? extends Map< ? , ? >> model)
	{
		this.templateName = templateName;
		this.model = model;
	}

	/**
	 * @see org.apache.wicket.behavior.AbstractBehavior#detach(org.apache.wicket.Component)
	 */
	@Override
	public void detach(Component< ? > c)
	{
		if (model instanceof IDetachable)
		{
			((IDetachable)model).detach();
		}
	}

	/**
	 * @return The encoding
	 */
	public String getEncoding()
	{
		return encoding;
	}

	/**
	 * @see org.apache.wicket.behavior.AbstractBehavior#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response)
	{
		CharSequence s = evaluate();
		if (null != s)
		{
			response.renderString(s);
		}
	}

	/**
	 * @param encoding
	 *            The encoding
	 */
	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	/**
	 * @return whether to escape HTML characters. The default value is false
	 */
	protected boolean escapeHtml()
	{
		return false;
	}

	/**
	 * Evaluate the template.
	 * 
	 * @return The evaluated template
	 */
	protected final CharSequence evaluate()
	{
		if (!Velocity.resourceExists(templateName))
		{
			return null;
		}
		// create a Velocity context object using the model if set
		final VelocityContext ctx = new VelocityContext(model.getObject());

		// create a writer for capturing the Velocity output
		StringWriter writer = new StringWriter();

		try
		{
			// execute the velocity script and capture the output in writer
			if (!Velocity.mergeTemplate(templateName, encoding, ctx, writer))
			{
				return null;
			}

			// replace the tag's body the Velocity output
			CharSequence result = writer.getBuffer();

			if (escapeHtml())
			{
				// encode the result in order to get valid html output that
				// does not break the rest of the page
				result = Strings.escapeMarkup(result.toString());
			}
			return result;
		}
		catch (Exception e)
		{
			throw new WicketRuntimeException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11291.java