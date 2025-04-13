error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13441.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13441.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[129,2]

error in qdox parser
file content:
```java
offset: 3513
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13441.java
text:
```scala
import org.apache.wicket.request.mapper.parameter.PageParameters;

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
package org.apache.wicket.examples.captcha;

import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameters.PageParameters;
import org.apache.wicket.util.value.ValueMap;


/**
 * Captcha example page.
 * 
 * @author Joshua Perlow
 */
public class Captcha extends WicketExamplePage
{
	private final class CaptchaForm<T> extends Form<T>
	{
		private static final long serialVersionUID = 1L;

		private final CaptchaImageResource captchaImageResource;

		/**
		 * Construct.
		 * 
		 * @param id
		 */
		public CaptchaForm(String id)
		{
			super(id);

			captchaImageResource = new CaptchaImageResource(imagePass);
			add(new Image("captchaImage", captchaImageResource));
			add(new RequiredTextField<String>("password", new PropertyModel<String>(properties,
				"password"))
			{
				@Override
				protected final void onComponentTag(final ComponentTag tag)
				{
					super.onComponentTag(tag);
					// clear the field after each render
					tag.put("value", "");
				}
			});
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public void onSubmit()
		{
			if (!imagePass.equals(getPassword()))
			{
				error("Captcha password '" + getPassword() + "' is wrong.\n" +
					"Correct password was: " + imagePass);
			}
			else
			{
				info("Success!");
			}

			// force redrawing
			captchaImageResource.invalidate();
		}
	}

	private static final long serialVersionUID = 1L;

	private static int randomInt(int min, int max)
	{
		return (int)(Math.random() * (max - min) + min);
	}

	private static String randomString(int min, int max)
	{
		int num = randomInt(min, max);
		byte b[] = new byte[num];
		for (int i = 0; i < num; i++)
			b[i] = (byte)randomInt('a', 'z');
		return new String(b);
	}

	/** Random captcha password to match against. */
	private final String imagePass = randomString(6, 8);

	private final ValueMap properties = new ValueMap();

	/**
	 * Construct.
	 * 
	 * @param parameters
	 */
	public Captcha(final PageParameters parameters)
	{
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new CaptchaForm<Void>("captchaForm"));
	}

	private String getPassword()
	{
		return properties.getString("password");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13441.java