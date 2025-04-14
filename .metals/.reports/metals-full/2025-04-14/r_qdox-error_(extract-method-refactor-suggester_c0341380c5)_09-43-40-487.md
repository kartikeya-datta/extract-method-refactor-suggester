error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2928.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2928.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2928.java
text:
```scala
U@@RL baseUrl = DiskIO.getResourceURL("org/columba/core/icons/MISC/");

package org.columba.core.gui.htmlviewer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.columba.core.io.DiskIO;

public class JavaHTMLViewerPlugin extends JScrollPane implements
		IHTMLViewerPlugin {

	private HTMLEditorKit htmlEditorKit;

	private AsynchronousHTMLDocument doc;

	private JTextPane textPane;

	public JavaHTMLViewerPlugin() {
		super();

		textPane = new JTextPane();

		setViewportView(textPane);
		// textPane.setMargin(new Insets(5, 5, 5, 5));
		textPane.setEditable(false);

		htmlEditorKit = new HTMLEditorKit();
		textPane.setEditorKit(htmlEditorKit);

		textPane.setContentType("text/html");

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}

	/*
	 * public void view(String htmlSource) {
	 * 
	 * setText(htmlSource);
	 * 
	 * postView(); }
	 */

	private void postView() {
		// setup base url in order to be able to display images
		// in html-component
		URL baseUrl = DiskIO.getResourceURL("org/columba/core/images/");

		((HTMLDocument) textPane.getDocument()).setBase(baseUrl);

		// scroll window to the beginning
		textPane.setCaretPosition(0);
	}

	public void view(String text) {
		if (text == null)
			return;

		doc = new AsynchronousHTMLDocument();

		Reader rd = new StringReader(text);
		try {
			htmlEditorKit.read(rd, doc, 0);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textPane.setDocument(doc);

		postView();
	}

	public JComponent getComponent() {
		return textPane;
	}

	/**
	 * Setting HTMLDocument to be an asynchronize model.
	 * <p>
	 * JTextPane therefore uses a background thread to display the message. This
	 * dramatically improves the performance of displaying a message.
	 * <p>
	 * Trick is to overwrite the getAsynchronousLoadPriority() to return a
	 * decent value.
	 * 
	 * @author fdietz
	 */
	
	public class AsynchronousHTMLDocument extends HTMLDocument {

		/**
		 * 
		 */
		public AsynchronousHTMLDocument() {
			super();
			putProperty("IgnoreCharsetDirective", new Boolean(true));
		}

		/**
		 * From the JDK1.4 reference:
		 * <p>
		 * This may load either synchronously or asynchronously depending upon
		 * the document returned by the EditorKit. If the Document is of type
		 * AbstractDocument and has a value returned by
		 * AbstractDocument.getAsynchronousLoadPriority that is greater than or
		 * equal to zero, the page will be loaded on a separate thread using
		 * that priority.
		 * 
		 * @see javax.swing.text.AbstractDocument#getAsynchronousLoadPriority()
		 */
		public int getAsynchronousLoadPriority() {
			return 10;
		}

		public String getTextWithLineBreaks(int start, int end)
				throws BadLocationException {
			StringBuffer result = new StringBuffer(end - start);
			ElementIterator iter = new ElementIterator(this);

			// First find the beginning element
			for (iter.next(); iter.current() != null; iter.next()) {
				Element e = iter.current();
				if (e.isLeaf()
						&& (e.getStartOffset() >= start || e.getEndOffset() >= start)
						&& e.getStartOffset() <= end) {
					Object a = e.getAttributes().getAttribute(
							StyleContext.NamedStyle.NameAttribute);
					if (a == HTML.Tag.CONTENT) {
						int as = Math.max(e.getStartOffset(), start);
						int ae = Math.min(e.getEndOffset(), end);
						result.append(super.getText(as, ae - as));
					}
					if (a == HTML.Tag.BR) {
						result.append("\n");
					}
				}
			}

			return result.toString();
		}
	}

	public boolean initialized() {
		return true;
	}

	/**
	 * @see javax.swing.text.JTextComponent#getSelectedText()
	 */
	public String getSelectedText() {
		try {
			return doc.getTextWithLineBreaks(textPane.getSelectionStart(),
					textPane.getSelectionEnd());
		} catch (BadLocationException e) {
			return "";
		}
	}

	public JComponent getContainer() {
		return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2928.java