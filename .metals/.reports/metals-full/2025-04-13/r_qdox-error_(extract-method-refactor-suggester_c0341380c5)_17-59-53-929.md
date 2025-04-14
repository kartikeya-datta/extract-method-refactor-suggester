error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8864.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8864.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8864.java
text:
```scala
r@@eturn browser.isInitialized();

package org.columba.core.gui.htmlviewer;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.columba.core.logging.Logging;
import org.jdesktop.jdic.browser.WebBrowser;

/**
 * JDIC-enabled web browser component used by the Message Viewer in component
 * mail.
 * <p>
 * Note: Java Proxy support/configuration doesn't has any effect. This component
 * uses your system's proxy settings. For example, when using Firefox, you have
 * to set your proxy in Firefox and these same options are also used in Columba.
 * <p>
 * Javascript support can be used to access DOM. This way we can for example
 * print the HTML page using:
 * <code>webBrowser.executeScript("window.print();");</code>
 * <p>
 * TODO (@author fdietz): how to use images in Message Viewer, we can't set a base URL and load
 * images from columba.jar?
 * 
 * @author Frederik Dietz
 * 
 */
public class JDICHTMLViewerPlugin extends JPanel implements IHTMLViewerPlugin {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.core.gui.htmlviewer");

	private WebBrowser browser;

	public JDICHTMLViewerPlugin() {
		super();

		try {
			WebBrowser.setDebug(true);

			browser = new WebBrowser();

			// turn of focus stealing (workaround should be removed in the
			// future!)
			browser.setFocusable(false);

			setLayout(new BorderLayout());
			add(browser, BorderLayout.CENTER);
		} catch (Error e) {
			LOG.severe("Error while initializing JDIC native browser: "
					+ e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();
		} catch (Exception e) {
			LOG
					.severe("Exception error while initializing JDIC native browser: "
							+ e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();
		}

		addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent e) {
				browser.setVisible(false);
				browser = null;
			}

			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void view(String htmlSource) {
		browser.setContent(htmlSource);
	}

	public JComponent getComponent() {
		return this;
	}

	public String getSelectedText() {
		return "getSelected() not yet supported by JDIC";
	}

	public boolean initialized() {
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8864.java