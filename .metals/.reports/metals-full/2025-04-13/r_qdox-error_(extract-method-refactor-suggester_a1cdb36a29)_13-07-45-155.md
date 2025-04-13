error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5014.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5014.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5014.java
text:
```scala
private S@@tring translationType = translationTypes.get(0);

/*
 * $Id: HelloWorld.java 4942 2006-03-14 22:38:34 -0800 (Tue, 14 Mar 2006)
 * ivaynberg $ $Revision: 4942 $ $Date: 2006-03-14 22:38:34 -0800 (Tue, 14 Mar
 * 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.examples.unicodeconverter;

import java.util.Arrays;
import java.util.List;

import wicket.Component;
import wicket.examples.WicketExamplePage;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.model.AbstractModel;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.util.string.Strings;

/**
 * Converts between unescaped and escaped unicode and shows a custom model.
 * Handy for message bundles.
 * 
 * @author Eelco Hillenius
 */
public class UnicodeConverter extends WicketExamplePage
{
	private static final String FROM_ESCAPED_UNICODE = "from escaped unicode";

	private static final String TO_ESCAPED_UNICODE = "to escaped unicode";

	private static List<String> translationTypes = Arrays.asList(new String[] { TO_ESCAPED_UNICODE,
			FROM_ESCAPED_UNICODE });

	private String source = "";

	private String translationType = (String)translationTypes.get(0);

	/**
	 * Model that does the conversion. Note that as we 'pull' the value every
	 * time we render (we get the current value of message), we don't need to
	 * update the model itself. The alternative strategy would be to have a
	 * model with it's own, translated, string representation of the source,
	 * which should be updated on every form post (e.g. by overriding
	 * {@link Form#onSubmit} and in that method explicitly setting the new
	 * value). But as you can see, this method is slighly easier, and if we
	 * wanted to use the translated value in e.g. a database, we could just
	 * query this model directly or indirectly by calling
	 * {@link Component#getModelObject()} on the component that holds it, and we
	 * would have a recent value.
	 */
	private final class ConverterModel extends AbstractModel<String>
	{
		/**
		 * @see wicket.model.IModel#getObject()
		 */
		public String getObject()
		{
			String result;
			if (TO_ESCAPED_UNICODE.equals(translationType))
			{
				result = Strings.toEscapedUnicode(source);
			}
			else
			{
				result = Strings.fromEscapedUnicode(source);
			}
			return result;
		}

		/**
		 * @see wicket.model.IModel#setObject(java.lang.Object)
		 */
		public void setObject(String object)
		{
			// Ignore. We are not interested in updating any value,
			// and we don't want to throw an exception like
			// AbstractReadOnlyModel either. Alternatively, we
			// could have overriden updateModel of FormInputComponent
			// and ignore any input there.
		}

		/**
		 * @see wicket.model.IModel#getNestedModel()
		 */
		@Override
		public IModel getNestedModel()
		{
			// Return null as we never have to operate as part of a nested model
			return null;
		}
	}

	/**
	 * Constructor.
	 */
	public UnicodeConverter()
	{
		Form form = new Form<UnicodeConverter>(this, "form",
				new CompoundPropertyModel<UnicodeConverter>(this));
		new TextArea(form, "source");
		new DropDownChoice<String>(form, "translationType", translationTypes);
		new TextArea<String>(form, "target", new ConverterModel());
	}

	/**
	 * @return the source to translate
	 */
	public String getSource()
	{
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source)
	{
		this.source = source;
	}

	/**
	 * @return the selection
	 */
	public String getTranslationType()
	{
		return translationType;
	}

	/**
	 * @param translationType
	 *            the selection
	 */
	public void setTranslationType(String translationType)
	{
		this.translationType = translationType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5014.java