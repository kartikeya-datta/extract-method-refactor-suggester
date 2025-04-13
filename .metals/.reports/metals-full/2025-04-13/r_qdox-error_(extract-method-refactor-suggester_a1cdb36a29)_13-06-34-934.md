error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11311.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11311.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11311.java
text:
```scala
public v@@oid detach(Component component)

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
package org.apache.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Objects;

/**
 * A simple header contributor that just spits out the string it is constructed with as a header
 * contribution.
 * 
 * @author Eelco Hillenius
 */
public class StringHeaderContributor extends AbstractHeaderContributor
{
	private static final long serialVersionUID = 1L;

	/** the contributor instance. */
	private final StringContributor contributor;

	/**
	 * Simply writes out the string it was constructed with whenever it is called for a header
	 * contribution.
	 */
	private static final class StringContributor implements IHeaderContributor
	{
		private static final long serialVersionUID = 1L;

		/** The contribution as a model that returns a plain string. */
		private final IModel< ? > contribution;

		/**
		 * Construct.
		 * 
		 * @param contribution
		 *            The contribution as a plain string
		 */
		public StringContributor(String contribution)
		{
			if (contribution == null)
			{
				throw new IllegalArgumentException("argument contribition must be not null");
			}

			this.contribution = new Model<String>(contribution);
		}

		/**
		 * Construct.
		 * 
		 * @param contribution
		 *            The contribution as a model that returns a plain string
		 */
		public StringContributor(IModel< ? > contribution)
		{
			if (contribution == null)
			{
				throw new IllegalArgumentException("argument contribition must be not null");
			}

			this.contribution = contribution;
		}

		/**
		 * @see org.apache.wicket.markup.html.IHeaderContributor#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
		 */
		public void renderHead(IHeaderResponse response)
		{
			Object object = contribution.getObject();
			if (object != null)
			{
				response.getResponse().println(object.toString());
			}
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			Object object = contribution.getObject();
			return (object != null) ? object.hashCode() : 0;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof StringContributor)
			{
				Object thisContrib = contribution.getObject();
				Object thatContrib = ((StringContributor)obj).contribution.getObject();
				return Objects.equal(thisContrib, thatContrib);
			}
			return false;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "StringContributor[contribution=" + contribution + "]";
		}
	}

	/**
	 * Construct.
	 * 
	 * @param contribution
	 *            header contribution as a plain string
	 */
	public StringHeaderContributor(String contribution)
	{
		contributor = new StringContributor(contribution);
	}

	/**
	 * Construct.
	 * 
	 * @param contribution
	 *            header contribution as a model that returns a plain string
	 */
	public StringHeaderContributor(IModel< ? > contribution)
	{
		contributor = new StringContributor(contribution);
	}

	/**
	 * @see org.apache.wicket.behavior.AbstractHeaderContributor#getHeaderContributors()
	 */
	@Override
	public final IHeaderContributor[] getHeaderContributors()
	{
		return new IHeaderContributor[] { contributor };
	}

	/**
	 * @see org.apache.wicket.behavior.AbstractBehavior#detach(org.apache.wicket.Component)
	 */
	@Override
	public void detach(Component< ? > component)
	{
		contributor.contribution.detach();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		Object string = contributor.contribution.getObject();
		return (string != null) ? string.toString() : "";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11311.java