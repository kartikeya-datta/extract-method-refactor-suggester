error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9738.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9738.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9738.java
text:
```scala
t@@his(ViewItem.createDefault(id));

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.core.gui.frame;

import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.columba.core.config.ViewItem;
import org.columba.core.gui.selection.SelectionManager;
import org.columba.core.util.GlobalResourceLoader;

/**
 * @author fdietz
 *  
 */
public class DefaultFrameController implements FrameMediator {

	
	private static final Logger LOG = Logger
			.getLogger("org.columba.core.gui.frame");

	/**
	 * Saves view information like position, size and maximization state
	 */
	protected ViewItem viewItem;

	/**
	 * Selection handler
	 */
	protected SelectionManager selectionManager;

	/**
	 * ID of controller
	 */
	protected String id;

	private Container container;

	/**
	 *  
	 */
	public DefaultFrameController(ViewItem viewItem) {

		super();

		this.viewItem = viewItem;

		this.id = viewItem.get("id");

		// init selection handler
		selectionManager = new SelectionManager();

	}
	
	public DefaultFrameController(String id) {
		this(new ViewItem(DefaultContainer.createDefaultConfiguration(id)));

	}

	/**
	 * @return ViewItem
	 */
	public ViewItem getViewItem() {
		return viewItem;
	}

	/**
	 * Sets the item.
	 * 
	 * @param item
	 *            The item to set
	 */
	public void setViewItem(ViewItem item) {
		this.viewItem = item;
	}

	/**
	 * @return SelectionManager
	 */
	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	/**
	 * Sets the selectionManager.
	 * 
	 * @param selectionManager
	 *            The selectionManager to set
	 */
	public void setSelectionManager(SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
	}

	/**
	 * @see org.columba.core.gui.frame.FrameMediator#getContainer()
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * @see org.columba.core.gui.frame.FrameMediator#loadPositions(org.columba.core.config.ViewItem)
	 */
	public void loadPositions(ViewItem viewItem) {

	}

	/**
	 * @see org.columba.core.gui.frame.FrameMediator#savePositions(org.columba.core.config.ViewItem)
	 */
	public void savePositions(ViewItem viewItem) {

	}

	/**
	 * @see org.columba.core.gui.frame.FrameMediator#setContainer(org.columba.core.gui.frame.Container)
	 */
	public void setContainer(Container c) {
		container = c;
	}

	/**
	 * @see org.columba.core.gui.frame.FrameMediator#getView()
	 */
	public Container getView() {
		return container;
	}

	/**
	 * @see org.columba.core.gui.frame.FrameMediator#getString(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public String getString(String sPath, String sName, String sID) {
		return GlobalResourceLoader.getString(sPath, sName, sID);
	}

	/**
	 * @see org.columba.core.gui.frame.FrameMediator#getContentPane()
	 */
	public ContentPane getContentPane() {
		return new EmptyContentPane();
	}
	
	class EmptyContentPane implements ContentPane {
		public EmptyContentPane() {
			super();
		}

		/**
		 * @see org.columba.core.gui.frame.ContentPane#getComponent()
		 */
		public JComponent getComponent() {
			return new JPanel();
		}

	}
	
	/**
	 * @see org.columba.core.gui.frame.FrameMediator#close()
	 */
	public void close() {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9738.java