error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6999.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6999.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[190,2]

error in qdox parser
file content:
```java
offset: 6113
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6999.java
text:
```scala
package org.apache.wicket.util.template;

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
package org.apache.wicket.extensions.util.resource;

import java.util.Map;

import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;


/**
 * A header contributor that will contribute the contents of the given template
 * interpolated with the provided map of variables.
 * 
 * @author Eelco Hillenius
 */
public class TextTemplateHeaderContributor extends StringHeaderContributor
{
	private static final long serialVersionUID = 1L;

	/**
	 * This model holds the template and returns the interpolation of the
	 * template with of any of the
	 */
	private static final class TemplateModel extends LoadableDetachableModel
	{
		private static final long serialVersionUID = 1L;

		/**
		 * The template to operate on and which makes the contribution.
		 */
		private final TextTemplate template;

		/**
		 * The model that holds any variables for interpolation. It should
		 * return a {@link Map} or null.
		 */
		private final IModel variablesModel;

		/**
		 * Construct.
		 * 
		 * @param template
		 *            the template to work on
		 * @param variablesModel
		 *            The model that holds any variables for interpolation. It
		 *            should return a {@link Map} or null.
		 */
		protected TemplateModel(TextTemplate template, IModel variablesModel)
		{
			if (template == null)
			{
				throw new IllegalArgumentException("argument template must be not null");
			}

			this.template = template;
			this.variablesModel = variablesModel;
		}
		/**
		 * @see org.apache.wicket.model.IModel#detach()
		 */
		public void detach()
		{
			if (variablesModel != null)
			{
				variablesModel.detach();
			}
			super.detach();
		}

		protected Object load()
		{
			if (variablesModel != null)
			{
				Map variables = (Map)variablesModel.getObject();
				if (variables != null)
				{
					return template.asString(variables);
				}
			}
			return template.asString();
		}
	}

	/**
	 * Gets a css header contributor based on the given text template. The
	 * template will be interpolated with the given variables. The content will
	 * be written as the body of a script tag pair.
	 * 
	 * @param template
	 *            The text template that is the base for the contribution
	 * @param variablesModel
	 *            The variables to interpolate
	 * @return The header contributor instance
	 */
	public static TextTemplateHeaderContributor forCss(TextTemplate template, IModel variablesModel)
	{
		return new TextTemplateHeaderContributor(new CssTemplate(template), variablesModel);
	}

	/**
	 * Gets a css header contributor that will load the template from the given
	 * file name relative to (/in the same package as) the provided clazz
	 * argument. The template will be interpolated with the given variables. The
	 * content will be written as the body of a script tag pair.
	 * 
	 * @param clazz
	 *            The class to be used for retrieving the classloader for
	 *            loading the packaged template.
	 * @param fileName
	 *            The name of the file, relative to the clazz position
	 * @param variablesModel
	 *            The variables to interpolate
	 * @return The header contributor instance
	 */
	public static TextTemplateHeaderContributor forCss(final Class clazz, final String fileName,
			IModel variablesModel)
	{
		return forCss(new PackagedTextTemplate(clazz, fileName), variablesModel);
	}

	/**
	 * Gets a javascript header contributor based on the given text template.
	 * The template will be interpolated with the given variables. The content
	 * will be written as the body of a script tag pair.
	 * 
	 * @param template
	 *            The text template that is the base for the contribution
	 * @param variablesModel
	 *            The variables to interpolate
	 * @return The header contributor instance
	 */
	public static TextTemplateHeaderContributor forJavaScript(TextTemplate template,
			IModel variablesModel)
	{
		return new TextTemplateHeaderContributor(new JavaScriptTemplate(template), variablesModel);
	}

	/**
	 * Gets a javascript header contributor that will load the template from the
	 * given file name relative to (/in the same package as) the provided clazz
	 * argument. The template will be interpolated with the given variables. The
	 * content will be written as the body of a script tag pair.
	 * 
	 * @param clazz
	 *            The class to be used for retrieving the classloader for
	 *            loading the packaged template.
	 * @param fileName
	 *            The name of the file, relative to the clazz position
	 * @param variablesModel
	 *            The variables to interpolate
	 * @return The header contributor instance
	 */
	public static TextTemplateHeaderContributor forJavaScript(final Class clazz,
			final String fileName, IModel variablesModel)
	{
		return forJavaScript(new PackagedTextTemplate(clazz, fileName), variablesModel);
	}

	/**
	 * Construct.
	 * 
	 * @param template
	 *            The template with the contribution
	 * @param variablesModel
	 *            Optional model for variable substitution
	 */
	protected TextTemplateHeaderContributor(TextTemplate template, IModel variablesModel)
	{
		super(new TemplateModel(template, variablesModel));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6999.java