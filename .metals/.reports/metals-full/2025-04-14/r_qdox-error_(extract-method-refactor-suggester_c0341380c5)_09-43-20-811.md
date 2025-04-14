error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1468.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1468.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1468.java
text:
```scala
transient private static L@@ogger log =

package org.apache.jmeter.gui.action;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.ComponentUtil;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.util.LoggingManager;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class Help implements Command
{
	private static Logger log =
		Hierarchy.getDefaultHierarchy().getLoggerFor(LoggingManager.GUI);
	private static Set commands = new HashSet();
	public final static String HELP = "help";
	private static String helpPage =
		"file://"
			+ JMeterUtils.getJMeterHome()
			+ "/docs/usermanual/component_reference.html";
	private static JDialog helpWindow;
	private static JTextPane helpDoc;
	private static JScrollPane scroller;
	static {
		commands.add(HELP);
		helpDoc = new JTextPane();
		scroller = new JScrollPane(helpDoc);
		helpDoc.setEditable(false);
		try
		{
			helpDoc.setPage(helpPage);
		}
		catch (IOException err)
		{
			log.error("Couldn't load " + helpPage, err);
			JOptionPane.showMessageDialog(
				GuiPackage.getInstance().getMainFrame(),
				JMeterUtils.getResString("error_loading_help"),
				"Error",
				JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * @see org.apache.jmeter.gui.action.Command#doAction(ActionEvent)
	 */
	public void doAction(ActionEvent e)
	{
		if (helpWindow == null)
		{
			helpWindow =
				new JDialog(
					GuiPackage.getInstance().getMainFrame(),
					JMeterUtils.getResString("help"),
					false);
			helpWindow.getContentPane().setLayout(new GridLayout(1, 1));
			ComponentUtil.centerComponentInWindow(helpWindow, 60);
		}
		helpDoc.scrollToReference(GuiPackage
				.getInstance()
				.getTreeListener()
				.getCurrentNode()
				.getStaticLabel().replace(' ','_'));
		helpWindow.getContentPane().removeAll();
		helpWindow.getContentPane().add(scroller);
		helpWindow.show();
	}
	/**
	 * @see org.apache.jmeter.gui.action.Command#getActionNames()
	 */
	public Set getActionNames()
	{
		return commands;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1468.java