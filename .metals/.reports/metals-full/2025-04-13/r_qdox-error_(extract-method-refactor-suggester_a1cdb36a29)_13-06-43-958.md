error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2291.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2291.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2291.java
text:
```scala
C@@olumbaLogger.log.error(e+" - "+ next.getAttribute("checkboxaction"));

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
import javax.swing.JMenu;

import org.columba.core.action.ActionPluginHandler;
import org.columba.core.action.BasicAction;
import org.columba.core.action.CheckBoxAction;
import org.columba.core.action.IMenu;
import org.columba.core.gui.frame.AbstractFrameController;
import org.columba.core.gui.util.CMenu;
import org.columba.core.io.DiskIO;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.core.util.GlobalResourceLoader;
import org.columba.core.xml.XmlElement;
import org.columba.core.xml.XmlIO;

/**
 * @author frd
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractMenuGenerator {

	protected XmlElement menuRoot;
	protected XmlIO xmlFile;
	protected AbstractFrameController frameController;

	/**
	 *
	 */
	public AbstractMenuGenerator(
		AbstractFrameController frameController,
		String path) {
		this.frameController = frameController;

		xmlFile = new XmlIO(DiskIO.getResourceURL(path));
		xmlFile.load();

	}

	public String getString(String sPath, String sName, String sID) {
		return GlobalResourceLoader.getString(sPath, sName, sID);
	}

	// XmlIO.getRoot().getElement("menubar");
	// or
	// XmlIO.getRoot().getElement("menu");
	public abstract XmlElement getMenuRoot();

	// this should be "menubar" or "menu"
	public abstract String getRootElementName();

	public void extendMenuFromFile(String path) {
		XmlIO menuXml = new XmlIO();
		menuXml.setURL(DiskIO.getResourceURL(path));
		menuXml.load();

		ListIterator iterator =
			menuXml
				.getRoot()
				.getElement(getRootElementName())
				.getElements()
				.listIterator();
		while (iterator.hasNext()) {
			extendMenu((XmlElement) iterator.next());
		}

	}

	public void extendMenu(XmlElement menuExtension) {

		XmlElement menu, extension;
		String menuName = menuExtension.getAttribute("name");
		String extensionName = menuExtension.getAttribute("extensionpoint");
		if (extensionName == null) {
			// new menu
			getMenuRoot().insertElement(
				(XmlElement) menuExtension.clone(),
				getMenuRoot().count() - 1);
			return;
		}

		ListIterator iterator = getMenuRoot().getElements().listIterator();
		while (iterator.hasNext()) {
			menu = ((XmlElement) iterator.next());
			if (menu.getAttribute("name").equals(menuName)) {
				createExtension(
					menu,
					(XmlElement) menuExtension.clone(),
					extensionName);
			}
		}

	}

	private void createExtension(
		XmlElement menu,
		XmlElement menuExtension,
		String extensionName) {

		XmlElement extension;
		int insertIndex = 0;

		ListIterator iterator;

		iterator = menu.getElements().listIterator();
		while (iterator.hasNext()) {
			extension = ((XmlElement) iterator.next());
			if (extension.getName().equals("extensionpoint")) {
				if (extension.getAttribute("name").equals(extensionName)) {
					int size = menuExtension.count();
					if (size > 0)
						menu.insertElement(
							new XmlElement("separator"),
							insertIndex);
					for (int i = 0; i < size; i++) {
						menu.insertElement(
							menuExtension.getElement(0),
							insertIndex + i + 1);
					}
					if (size > 0)
						menu.insertElement(
							new XmlElement("separator"),
							insertIndex + size + 1);
					return;
				}
			} else if (extension.getName().equals("menu")) {
				createExtension(extension, menuExtension, extensionName);
			}
			insertIndex++;
		}
	}

	protected JMenu createMenu(XmlElement menuElement) {
		List childs = menuElement.getElements();
		ListIterator it = childs.listIterator();

		JMenu menu =
			new JMenu(
				getString(
					"menu",
					"mainframe",
					menuElement.getAttribute("name")));

		createMenuEntries(menu, it);

		return menu;
	}

	protected void createMenuEntries(JMenu menu, ListIterator it) {
		boolean lastWasSeparator = false;

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
						if (action != null) {
							menu.add(action);
							lastWasSeparator = false;
						}

					} catch (Exception e) {
						ColumbaLogger.log.error(e);
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
						if (action != null) {
							JCheckBoxMenuItem menuitem =
								new JCheckBoxMenuItem(action);
							menu.add(menuitem);
							action.setCheckBoxMenuItem(menuitem);
							lastWasSeparator = false;
						}
					} catch (Exception e) {
						ColumbaLogger.log.error(e);
					}
				} else if (next.getAttribute("imenu") != null) {
					try {
						IMenu imenu =
							(
								(
									ActionPluginHandler) MainInterface
										.pluginManager
										.getHandler(
									"org.columba.core.action")).getIMenu(
								next.getAttribute("imenu"),
								frameController);
					
						if ( imenu != null )
							menu.add(imenu);

						lastWasSeparator = false;

					} catch (Exception e) {
						e.printStackTrace();
						ColumbaLogger.log.error(e);
					}
				}

			} else if (name.equals("separator")) {
				if (!lastWasSeparator)
					menu.addSeparator();

				lastWasSeparator = true;
			} else if (name.equals("menu")) {
				menu.add(createSubMenu(next));
				lastWasSeparator = false;
			}
		}
		if (lastWasSeparator) {
			menu.remove(menu.getMenuComponentCount() - 1);
		}
	}

	protected JMenu createSubMenu(XmlElement menuElement) {
		List childs = menuElement.getElements();
		ListIterator it = childs.listIterator();

		CMenu menu =
			new CMenu(
				getString(
					"menu",
					"mainframe",
					menuElement.getAttribute("name")));

		createMenuEntries(menu, it);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2291.java