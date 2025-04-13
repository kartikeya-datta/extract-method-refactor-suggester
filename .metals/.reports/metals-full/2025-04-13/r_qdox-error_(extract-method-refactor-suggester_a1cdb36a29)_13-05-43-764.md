error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/36.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/36.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/36.java
text:
```scala
public S@@tring getVariation()

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
package org.apache.wicket.ng.resource;

import java.io.Serializable;
import java.util.Locale;

import org.apache.wicket.ng.request.Request;
import org.apache.wicket.ng.request.component.PageParameters;
import org.apache.wicket.ng.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.ng.request.response.Response;
import org.apache.wicket.util.lang.Checks;

/**
 * Resource is an object capable of writing output to response.
 * 
 * @author Matej Knopp
 */
public interface Resource extends Serializable
{
	/**
	 * Attributes that are provided to resource in the {@link Resource#respond(Attributes)} method.
	 * Attributes are set by the {@link ResourceRequestHandler}.
	 * 
	 * @author Matej Knopp
	 */
	public static class Attributes
	{
		private final Request request;
		private final Response response;
		private final Locale locale;
		private final String style;
		private final String variation;
		private final PageParameters parameters;

		/**
		 * Construct.
		 * 
		 * @param request
		 * 
		 * @param response
		 * @param locale
		 * @param style
		 * @param variation
		 * @param parameters
		 */
		public Attributes(Request request, Response response, Locale locale, String style,
			String variation, PageParameters parameters)
		{
			Checks.argumentNotNull(request, "request");
			Checks.argumentNotNull(response, "response");
			Checks.argumentNotNull(locale, "locale");

			this.request = request;
			this.response = response;
			this.locale = locale;
			this.style = style;
			this.variation = variation;
			this.parameters = parameters;
		}

		/**
		 * Returns current request.
		 * 
		 * @return current request
		 */
		public Request getRequest()
		{
			return request;
		}

		/**
		 * Returns current response. The resource must write output to the response.
		 * 
		 * @return response
		 */
		public Response getResponse()
		{
			return response;
		}

		/**
		 * Returns requested locale. The locale is never null.
		 * 
		 * @return locale
		 */
		public Locale getLocale()
		{
			return locale;
		}

		/**
		 * If specified returns requested style. The style is optional.
		 * 
		 * @return style or <code>null</code>
		 */
		public String getStyle()
		{
			return style;
		}

		/**
		 * If specified returns requested variation. The variation is optional.
		 * 
		 * @return variation or <code>null</code>
		 */
		public String getVariant()
		{
			return variation;
		}

		/**
		 * Returns additional parameters extracted from the request. If resource is created mounted
		 * {@link ResourceReference}, this method returns all (indexed and query) parameters after
		 * the mount path. For non mounted {@link ResourceReference}s this method will only return
		 * the query parameters. For component specific resources the behavior depends on the
		 * component.
		 * 
		 * @return page parameters or <code>null</code>
		 */
		public PageParameters getParameters()
		{
			return parameters;
		}
	};

	/**
	 * Renders this resource to response using the provided attributes.
	 * 
	 * @param attributes
	 */
	public void respond(Attributes attributes);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/36.java