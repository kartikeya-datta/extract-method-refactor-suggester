error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8877.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8877.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8877.java
text:
```scala
S@@tring url = urlFor(ref);

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
package wicket.markup.html.resources;

import wicket.Application;
import wicket.AttributeModifier;
import wicket.Component;
import wicket.ResourceReference;
import wicket.markup.html.PackageResourceReference;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * Base class for components that render references to packaged resources.
 * 
 * @author Eelco Hillenius
 */
public class PackagedResourceReference extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            component id
	 * @param referer
	 *            the class that is refering; is used as the relative root for
	 *            gettting the resource
	 * @param file
	 *            relative location of the packaged file
	 * @param attributeToReplace
	 *            the attribute to replace of the target tag
	 */
	public PackagedResourceReference(final String id, final Class referer, final String file,
			final String attributeToReplace)
	{
		this(id, referer, new Model(file), attributeToReplace);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            component id
	 * @param referer
	 *            the class that is refering; is used as the relative root for
	 *            gettting the resource
	 * @param file
	 *            model that supplies the relative location of the packaged
	 *            file. Must return an instance of {@link String}
	 * @param attributeToReplace
	 *            the attribute to replace of the target tag
	 */
	public PackagedResourceReference(final String id, final Class referer, final IModel file,
			final String attributeToReplace)
	{
		super(id);

		if (referer == null)
		{
			throw new IllegalArgumentException("Referer may not be null");
		}
		if (file == null)
		{
			throw new IllegalArgumentException("File may not be null");
		}
		if (attributeToReplace == null)
		{
			throw new IllegalArgumentException("AttributeToReplace may not be null");
		}

		IModel srcReplacement = new Model()
		{
			private static final long serialVersionUID = 1L;

			public Object getObject(Component component)
			{
				Object o = file.getObject(component);
				if (o == null)
				{
					throw new IllegalArgumentException(
							"The model must provide a non-null object (component == " + component
									+ ")");
				}
				if (!(o instanceof String))
				{
					throw new IllegalArgumentException(
							"The model must provide an instance of String");
				}
				String f = (String)component.getConverter().convert(file.getObject(component),
						String.class);
				PackageResourceReference ref = new PackageResourceReference(Application.get(),
						referer, f);
				String url = getRequestCycle().urlFor(ref);
				return url;
			}
		};
		add(new AttributeModifier(attributeToReplace, true, srcReplacement));
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            component id
	 * @param resourceReference
	 *            the reference to the resource
	 * @param attributeToReplace
	 *            the attribute to replace of the target tag
	 */
	public PackagedResourceReference(final String id, final ResourceReference resourceReference,
			final String attributeToReplace)
	{
		this(id, new Model(resourceReference), attributeToReplace);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            component id
	 * @param resourceReference
	 *            the reference to the resource. Must return an instance of
	 *            {@link ResourceReference}
	 * @param attributeToReplace
	 *            the attribute to replace of the target tag
	 */
	public PackagedResourceReference(final String id, final IModel resourceReference,
			final String attributeToReplace)
	{
		super(id);

		if (resourceReference == null)
		{
			throw new IllegalArgumentException("ResourceReference may not be null");
		}
		if (attributeToReplace == null)
		{
			throw new IllegalArgumentException("AttributeToReplace may not be null");
		}

		IModel srcReplacement = new Model()
		{
			private static final long serialVersionUID = 1L;

			public Object getObject(Component component)
			{
				Object o = resourceReference.getObject(component);
				if (o == null)
				{
					throw new IllegalArgumentException(
							"The model must provide a non-null object (component == " + component
									+ ")");
				}
				if (!(o instanceof ResourceReference))
				{
					throw new IllegalArgumentException(
							"The model must provide an instance of ResourceReference");
				}

				ResourceReference ref = (ResourceReference)o;
				String url = getPage().urlFor(ref);
				return url;
			}
		};
		add(new AttributeModifier(attributeToReplace, true, srcReplacement));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8877.java