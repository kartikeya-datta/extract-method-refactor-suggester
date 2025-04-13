error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16780.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16780.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16780.java
text:
```scala
i@@f (o != null)

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
package org.apache.wicket.devutils.stateless;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;

/**
 * Stateless checker. Checks if components with {@link StatelessComponent} annotation are really
 * stateless. This is a utility that is intended for use primarily during development. If you add an
 * instance of this class to your application, it will check all components or pages marked with the
 * <tt>StatelessComponent</tt> annotation to make sure that they are stateless as you intended.
 * 
 * This is useful when trying to maintain stateless pages since it is very easy to inadvertantly add
 * a component to a page that internally uses stateful links, etc.
 * 
 * @author Marat Radchenko
 * @see StatelessComponent
 */
public class StatelessChecker implements IComponentOnBeforeRenderListener
{
	/**
	 * Returns <code>true</code> if checker must check given component, <code>false</code>
	 * otherwise.
	 * 
	 * @param component
	 *            component to check.
	 * @return <code>true</code> if checker must check given component.
	 */
	private static boolean mustCheck(final Component component)
	{
		final StatelessComponent ann = component.getClass().getAnnotation(StatelessComponent.class);
		return ann != null && ann.enabled();
	}

	/**
	 * @see org.apache.wicket.application.IComponentOnBeforeRenderListener#onBeforeRender(org.apache.wicket.Component)
	 */
	public void onBeforeRender(final Component component)
	{
		if (StatelessChecker.mustCheck(component))
		{
			final IVisitor<Component> visitor = new Component.IVisitor<Component>()
			{
				public Object component(final Component comp)
				{
					if (component instanceof Page && StatelessChecker.mustCheck(comp))
					{
						// Do not go deeper, because this component will be checked by checker
						// itself.
						// Actually we could go deeper but that would mean we traverse it twice
						// (for current component and for inspected one).
						// We go deeper for Page because full tree will be inspected during
						// isPageStateless call.
						return IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
					}
					else if (!comp.isStateless())
					{
						return comp;
					}
					else
					{
						return IVisitor.CONTINUE_TRAVERSAL;
					}
				}
			};

			final String msg = "'" + component + "' claims to be stateless but isn't.";
			if (!component.isStateless())
			{
				throw new IllegalArgumentException(msg +
					" Possible reasons: no stateless hint, statefull behaviors");
			}

			if (component instanceof MarkupContainer)
			{
				// Traverse children
				final Object o = ((MarkupContainer)component).visitChildren(visitor);
				if (o == null)
				{
					throw new IllegalArgumentException(msg + " Offending component: " + o);
				}
			}

			if (component instanceof Page)
			{
				final Page p = (Page)component;
				if (!p.isBookmarkable())
				{
					throw new IllegalArgumentException(msg +
						" Only bookmarkable pages can be stateless");
				}
				if (!p.isPageStateless())
				{
					throw new IllegalArgumentException(msg + " for unknown reason");
				}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16780.java