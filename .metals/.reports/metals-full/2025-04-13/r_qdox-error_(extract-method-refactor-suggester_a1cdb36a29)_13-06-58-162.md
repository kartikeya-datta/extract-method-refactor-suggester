error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14172.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14172.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14172.java
text:
```scala
a@@dd(new AttributeModifier("xmlns:wicket", true, new Model<String>("http://wicket.sourceforge.net")));

/*
 * $Id: OutputTransformerContainer.java,v 1.1 2005/12/31 10:09:31 jdonnerstag
 * Exp $ $Revision$ $Date$
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
package wicket.markup.transformer;

import wicket.AttributeModifier;
import wicket.Component;
import wicket.MarkupContainer;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * A container which output markup will be processes by a XSLT processor prior
 * to writing the output into the web response. The *.xsl resource must be
 * located in the same path as the nearest parent with an associated markup and
 * must have a filename equal to the component's id.
 * <p>
 * The containers tag will be the root element of the xml data applied for
 * transformation to ensure the xml data are well formed (single root element).
 * In addition the attribute
 * <code>xmlns:wicket="http://wicket.sourceforge.net"</code> is added to the
 * root element to allow the XSL processor to handle the wicket namespace.
 * <p>
 * Similar to this container, a <code>IBehavior</code> is available which does
 * the same, but does not require an additional Container.
 * 
 * @see wicket.markup.transformer.XsltTransfomerBehavior
 * 
 * @author Juergen Donnerstag
 */
public class XsltOutputTransformerContainer extends AbstractOutputTransformerContainer
{
	private static final long serialVersionUID = 1L;

	/** An optional xsl file path */
	private final String xslFile;

	/**
	 * Instead of using the default mechanism to determine the associated XSL
	 * file, it is given by the user.
	 * 
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 * 
	 * @param xslFilePath
	 *            XSL input file path
	 */
	public XsltOutputTransformerContainer(MarkupContainer parent, final String id,
			final IModel model, final String xslFilePath)
	{
		super(parent, id);

		this.xslFile = xslFilePath;

		// The containers tag will be transformed as well. Thus we make sure
		// that
		// the xml provided to the xsl processor is well formed (has a single
		// root element)
		setTransformBodyOnly(false);

		// Make the XSLT processor happy and allow him to handle the wicket
		// tags and attributes which are in the wicket namespace
		add(new AttributeModifier("xmlns:wicket", true, new Model("http://wicket.sourceforge.net")));
	}

	/**
	 * Construct
	 * 
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public XsltOutputTransformerContainer(MarkupContainer parent, final String id,
			final IModel model)
	{
		this(parent, id, model, null);
	}

	/**
	 * Construct
	 * 
	 * @see wicket.Component#Component(MarkupContainer,String)
	 */
	public XsltOutputTransformerContainer(MarkupContainer parent, final String id)
	{
		this(parent, id, null, null);
	}

	/**
	 * 
	 * @see wicket.MarkupContainer#getMarkupType()
	 */
	@Override
	public String getMarkupType()
	{
		return "xsl";
	}

	/**
	 * 
	 * @see wicket.markup.transformer.ITransformer#transform(wicket.Component,
	 *      java.lang.String)
	 */
	@Override
	public CharSequence transform(final Component component, final String output) throws Exception
	{
		return new XsltTransformer(this.xslFile).transform(component, output);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14172.java