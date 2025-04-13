error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7878.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7878.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7878.java
text:
```scala
r@@eturn "Search Results for \"<font class=\"italic\">" + r.getTitle() + "</font>\":";

package org.columba.core.gui.search;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.columba.core.gui.htmlviewer.HTMLViewerFactory;
import org.columba.core.gui.htmlviewer.IHTMLViewerPlugin;
import org.columba.core.search.api.IResultEvent;
import org.columba.core.search.api.IResultListener;
import org.columba.core.search.api.ISearchCriteria;
import org.columba.core.search.api.ISearchResult;

public class SearchResultView extends JPanel implements IResultListener {

	private IHTMLViewerPlugin viewerPlugin;

	private StringBuffer buf;

	public SearchResultView() {
		super();

		viewerPlugin = HTMLViewerFactory.createHTMLViewer();

		setLayout(new BorderLayout());

		add(viewerPlugin.getContainer(), BorderLayout.CENTER);
	}

	public void resultArrived(IResultEvent event) {
		List<ISearchResult> result = event.getSearchResults();

		buf.append("<p>" + createCriteria(event.getSearchCriteria())
				+ "</p><br>");

		Iterator<ISearchResult> it = result.iterator();
		while (it.hasNext()) {
			ISearchResult r = it.next();
			buf.append("<p>");
			buf.append(createTitle(r));
			buf.append("</p><p>");
			buf.append(getDescription(r));
			buf.append("</p><p><br></p>");
		}

		StringBuffer doc = new StringBuffer();
		startDocument(doc);
		doc.append(buf.toString());
		endDocument(doc);

		viewerPlugin.view(doc.toString());

	}

	private String createCriteria(ISearchCriteria r) {
		return "Search Results for \"<font class=\"italic\">" + r.getName() + "</font>\":";
	}

	private String createTitle(ISearchResult r) {
		return "<a href=\"" + r.getLocation().toString() + "\">" + r.getTitle()
				+ "</a>";
	}

	private String getDescription(ISearchResult r) {
		return "<font class=\"quoting\">" + r.getDescription() + "</font>";
	}

	public void clearSearch(IResultEvent event) {
		buf = new StringBuffer();
		viewerPlugin.view("");
	}

	private void startDocument(StringBuffer b) {
		String css = "<style type=\"text/css\">\n"
				+ "a { color: blue; text-decoration: underline }\n"
				+ "font.quoting {color:#949494;} \n font.italic {font-style:italic;color:#000;} \n" + "</style>\n";

		b.append("<HTML><HEAD>" + css + "</HEAD><BODY>");
	}

	private void endDocument(StringBuffer b) {
		b.append("</P></BODY></HTML>");
	}

	public void reset(IResultEvent event) {
		buf = new StringBuffer();
		viewerPlugin.view("");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7878.java