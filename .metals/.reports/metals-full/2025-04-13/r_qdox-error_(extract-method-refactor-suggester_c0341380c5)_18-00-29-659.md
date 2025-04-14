error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4829.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4829.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4829.java
text:
```scala
m@@enu.add(createSubMenu(next));

/*
 * Created on 12.03.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.columba.core.gui.menu;

import java.util.List;
import java.util.ListIterator;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import org.columba.core.action.ActionPluginHandler;
import org.columba.core.action.BasicAction;
import org.columba.core.action.CheckBoxAction;
import org.columba.core.gui.frame.FrameController;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.core.xml.XmlElement;

/**
 * @author frd
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PopupMenuGenerator extends AbstractMenuGenerator {

	/**
	 * @param frameController
	 * @param path
	 */
	public PopupMenuGenerator(FrameController frameController, String path) {
		super(frameController, path);

	}

	public void createPopupMenu(JPopupMenu menu) {
		menu.removeAll();
		createPopupMenu(getMenuRoot(), menu);

	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.AbstractMenuGenerator#getMenuRoot()
	 */
	public XmlElement getMenuRoot() {

		return xmlFile.getRoot().getElement("menu");
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.AbstractMenuGenerator#getRootElementName()
	 */
	public String getRootElementName() {
		return "menu";
	}

	protected JPopupMenu createPopupMenu(
		XmlElement menuElement,
		JPopupMenu menu) {
		List childs = menuElement.getElements();
		ListIterator it = childs.listIterator();

		while (it.hasNext()) {
			XmlElement next = (XmlElement) it.next();
			String name = next.getName();
			if (name.equals("menuitem")) {

				if (next.getAttribute("action") != null) {
					try {
						BasicAction action =
							(
								(
									ActionPluginHandler) MainInterface
										.pluginManager
										.getHandler(
									"org.columba.core.action")).getAction(
								next.getAttribute("action"),
								frameController);

						menu.add(action);

					} catch (Exception e) {
						
						ColumbaLogger.log.error(e+": "+next.getAttribute("action"));
						
					}
				} else if (next.getAttribute("checkboxaction") != null) {
					try {
						CheckBoxAction action =
							(CheckBoxAction)
								(
									(
										ActionPluginHandler) MainInterface
											.pluginManager
											.getHandler(
										"org.columba.core.action")).getAction(
								next.getAttribute("checkboxaction"),
								frameController);
						JCheckBoxMenuItem menuitem =
							new JCheckBoxMenuItem(action);
						menu.add(menuitem);
						action.setCheckBoxMenuItem(menuitem);
					} catch (Exception e) {
						ColumbaLogger.log.error(e);
					}
				} else if (next.getAttribute("imenu") != null) {
					try {
						menu.add(
							(
								(
									ActionPluginHandler) MainInterface
										.pluginManager
										.getHandler(
									"org.columba.core.action")).getIMenu(
								next.getAttribute("imenu"),
								frameController));
					} catch (Exception e) {
						ColumbaLogger.log.error(e);
					}
				}

			} else if (name.equals("separator")) {

				menu.addSeparator();

			} else if (name.equals("menu")) {
				menu.add(createMenu(next));
			}
		}

		return menu;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4829.java