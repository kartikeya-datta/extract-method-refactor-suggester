error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12948.java
text:
```scala
private L@@ist headerData;

/*
 * $Id: BaseExportView.java 5394 2006-04-16 13:36:52 +0000 (Sun, 16 Apr 2006)
 * jdonnerstag $ $Revision$ $Date: 2006-04-16 13:36:52 +0000 (Sun, 16 Apr
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
package wicket.examples.displaytag.export;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * Base abstract class for simple export views.
 * </p>
 * <p>
 * A class that extends BaseExportView simple need to provide delimiters for
 * rows and columns.
 * </p>
 * 
 * @author Fabrizio Giustina
 * @author Juergen Donnerstag
 */
public abstract class BaseExportView implements Serializable
{
	/**
	 * logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(BaseExportView.class);

	/**
	 * Headers.
	 */
	private final List headerData;

	/**
	 * TableModel to render.
	 */
	private List model;

	/**
	 * include header in export?
	 */
	private boolean header;

	/**
	 * Constructor for BaseExportView.
	 * 
	 * @param tableModel
	 *            TableModel to render
	 * @param exportFullList
	 *            boolean export full list?
	 * @param includeHeader
	 *            should header be included in export?
	 * @param decorateValues
	 *            should ouput be decorated?
	 */
	public BaseExportView(final List tableModel, final boolean exportFullList,
			final boolean includeHeader, final boolean decorateValues)
	{
		this.model = tableModel;
		this.header = includeHeader;
	}

	/**
	 * String to add before a row.
	 * 
	 * @return String
	 */
	protected abstract String getRowStart();

	/**
	 * String to add after a row.
	 * 
	 * @return String
	 */
	protected abstract String getRowEnd();

	/**
	 * String to add before a listItem.
	 * 
	 * @return String
	 */
	protected abstract String getCellStart();

	/**
	 * String to add after a listItem.
	 * 
	 * @return String
	 */
	protected abstract String getCellEnd();

	/**
	 * String to add to the top of document.
	 * 
	 * @return String
	 */
	protected abstract String getDocumentStart();

	/**
	 * String to add to the end of document.
	 * 
	 * @return String
	 */
	protected abstract String getDocumentEnd();

	/**
	 * always append cell end string?
	 * 
	 * @return boolean
	 */
	protected abstract boolean getAlwaysAppendCellEnd();

	/**
	 * always append row end string?
	 * 
	 * @return boolean
	 */
	protected abstract boolean getAlwaysAppendRowEnd();

	/**
	 * MimeType to return.
	 * 
	 * @return String myme type
	 */
	public abstract String getMimeType();

	/**
	 * Write table header.
	 * 
	 * @return String rendered header
	 */
	protected String doHeaders()
	{
		if ((headerData == null) || headerData.isEmpty())
		{
			return "";
		}

		final String ROW_START = getRowStart();
		final String ROW_END = getRowEnd();
		final String CELL_START = getCellStart();
		final String CELL_END = getCellEnd();
		final boolean ALWAYS_APPEND_CELL_END = getAlwaysAppendCellEnd();

		StringBuffer buffer = new StringBuffer(1000);

		// start row
		buffer.append(ROW_START);

		Iterator iterator = headerData.iterator();
		while (iterator.hasNext())
		{
			log.debug("iterator.hasNext()");
			String headerCell = (String)iterator.next();
			String columnHeader = headerCell;

			buffer.append(CELL_START);
			buffer.append(escapeColumnValue(columnHeader));

			if (ALWAYS_APPEND_CELL_END || iterator.hasNext())
			{
				buffer.append(CELL_END);
			}
		}

		// end row
		buffer.append(ROW_END);
		return buffer.toString();
	}

	/**
	 * Write the rendered table.
	 * 
	 * @return String rendered table body
	 */
	public String doExport()
	{
		StringBuffer buffer = new StringBuffer(8000);

		final String DOCUMENT_START = getDocumentStart();
		final String DOCUMENT_END = getDocumentEnd();
		final String ROW_START = getRowStart();
		final String ROW_END = getRowEnd();
		final String CELL_START = getCellStart();
		final String CELL_END = getCellEnd();
		final boolean ALWAYS_APPEND_CELL_END = getAlwaysAppendCellEnd();
		final boolean ALWAYS_APPEND_ROW_END = getAlwaysAppendRowEnd();

		// document start
		buffer.append(DOCUMENT_START);

		if (this.header)
		{
			buffer.append(doHeaders());
		}

		// get the correct iterator (full or partial list according to the
		// exportFull field)
		Iterator rowIterator = model.iterator();

		// iterator on rows
		while (rowIterator.hasNext())
		{
			Object row = rowIterator.next();

			buffer.append(ROW_START);
			buffer.append(escapeColumnValue(row.toString()));

			if (ALWAYS_APPEND_ROW_END || rowIterator.hasNext())
			{
				buffer.append(ROW_END);
			}
		}

		// document start
		buffer.append(DOCUMENT_END);
		return buffer.toString();
	}

	/**
	 * can be implemented to escape values for different output.
	 * 
	 * @param value
	 *            original column value
	 * @return escaped column value
	 */
	protected abstract Object escapeColumnValue(Object value);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12948.java