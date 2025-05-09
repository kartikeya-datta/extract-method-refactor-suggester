error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7677.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7677.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7677.java
text:
```scala
" DEBUG MODE")@@;

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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.View;

import org.columba.core.config.ViewItem;
import org.columba.core.config.WindowItem;
import org.columba.core.gui.menu.ColumbaMenu;
import org.columba.core.gui.statusbar.StatusBar;
import org.columba.core.gui.toolbar.ColumbaToolBar;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.main.MainInterface;
import org.columba.core.xml.XmlElement;
import org.columba.mail.gui.frame.AbstractMailFrameController;
import org.columba.mail.gui.frame.TooltipMouseHandler;

/**
 * @author fdietz
 *  
 */
public class DefaultContainer extends JFrame implements Container,
		WindowListener {

	private static final Logger LOG = Logger
			.getLogger("org.columba.core.gui.frame");

	private FrameMediator mediator;

	private View view;

	private ViewItem viewItem;

	private String id = "core";

	protected ColumbaMenu menu;

	protected ColumbaToolBar toolbar;

	/**
	 * in order to support multiple toolbars we use a panel as parent container
	 */
	protected JPanel toolbarPane;

	protected StatusBar statusBar;

	/**
	 * Menuitems use this to display a string in the statusbar
	 */
	protected MouseAdapter mouseTooltipHandler;

	protected JPanel contentPane;

	protected ContainerInfoPanel infoPanel;

	protected boolean switchedFrameMediator = false;

	/**
	 *  
	 */
	public DefaultContainer(ViewItem viewItem) {
		super();

		this.viewItem = viewItem;

		mediator = new DefaultFrameController(this, viewItem);

		this.setIconImage(ImageLoader.getImageIcon("icon16.png").getImage());

		if (MainInterface.DEBUG) { 
			setTitle("Columba - version: " + org.columba.core.main.MainInterface.version +
					" Running in DEBUG MODE");
		} else { 
			setTitle("Columba - version: " + org.columba.core.main.MainInterface.version);
		}

		//		register statusbar at global taskmanager
		statusBar = new StatusBar(MainInterface.processor.getTaskManager());

		//		 add tooltip handler
		mouseTooltipHandler = new TooltipMouseHandler(statusBar);

		JPanel panel = (JPanel) this.getContentPane();
		panel.setLayout(new BorderLayout());

		// add statusbar
		panel.add(statusBar, BorderLayout.SOUTH);

		// add toolbar
		toolbarPane = new JPanel();
		toolbarPane.setLayout(new BoxLayout(toolbarPane, BoxLayout.Y_AXIS));
		panel.add(toolbarPane, BorderLayout.NORTH);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		panel.add(contentPane, BorderLayout.CENTER);

		// create menu
		menu = new ColumbaMenu("org/columba/core/action/menu.xml", mediator);

		if (menu != null) {
			setJMenuBar(menu);
		}

		// create toolbar
		toolbar = new ColumbaToolBar(mediator);
		setToolBar(toolbar);

		setInfoPanel(new ContainerInfoPanel());

		// add window listener
		addWindowListener(this);

	}

	/**
	 * 
	 * Create default view configuration
	 * 
	 * This is used by implementations of controllers who want to store some
	 * more information, which is specific to their domain.
	 * 
	 * @see AbstractMailFrameController for implementation example
	 * 
	 * 
	 * @param id
	 *            ID of controller
	 * @return xml treenode containing the new configuration
	 */
	protected XmlElement createDefaultConfiguration(String id) {
		// initialize default view options
		XmlElement defaultView = new XmlElement("view");
		XmlElement window = new XmlElement("window");
		window.addAttribute("x", "0");
		window.addAttribute("y", "0");
		window.addAttribute("width", "640");
		window.addAttribute("height", "480");
		window.addAttribute("maximized", "true");
		defaultView.addElement(window);

		XmlElement toolbars = new XmlElement("toolbars");
		toolbars.addAttribute("main", "true");
		defaultView.addElement(toolbars);

		defaultView.addAttribute("id", id);

		return defaultView;
	}

	/**
	 * 
	 * @return statusbar
	 */
	public StatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * Returns the mouseTooltipHandler.
	 * 
	 * @return MouseAdapter
	 */
	public MouseAdapter getMouseTooltipHandler() {
		return mouseTooltipHandler;
	}

	/**
	 * @see org.columba.core.gui.frame.Container#setFrameMediator(org.columba.core.gui.frame.FrameMediator)
	 */
	public void setFrameMediator(FrameMediator m) {
		LOG.fine("set framemediator to " + m.getClass());

		this.mediator = m;

		m.setContainer(this);

		// use new viewitem
		viewItem = m.getViewItem();

		switchedFrameMediator = false;
	}

	/**
	 * @see org.columba.core.gui.frame.Container#switchFrameMediator(org.columba.core.gui.frame.FrameMediator)
	 */
	public void switchFrameMediator(FrameMediator m) {
		LOG.fine("switching framemediator to " + m.getClass());

		this.mediator = m;

		m.setContainer(this);

		// use new viewitem
		viewItem = m.getViewItem();

		switchedFrameMediator = true;

		// update content-pane
		setContentPane((ContentPane) m);

	}

	/**
	 * @see org.columba.core.gui.frame.Container#getFrameMediator()
	 */
	public FrameMediator getFrameMediator() {

		return mediator;
	}

	/**
	 * @see org.columba.core.gui.frame.Container#getViewItem()
	 */
	public ViewItem getViewItem() {
		return viewItem;
	}

	/**
	 * Enable/Disable toolbar configuration
	 * 
	 * @param id
	 *            ID of controller
	 * @param enable
	 *            true/false
	 */
	public void enableToolBar(String id, boolean enable) {
		getViewItem().set("toolbars", id, enable);

		toolbarPane.removeAll();

		if (enable) {
			toolbarPane.add(getToolBar());
			if (isInfoPanelEnabled())
				toolbarPane.add(getInfoPanel());
		} else {
			if (isInfoPanelEnabled())
				toolbarPane.add(getInfoPanel());
		}

		validate();
	}

	/**
	 * Returns true if the toolbar is enabled
	 * 
	 * @param id
	 *            ID of controller
	 * @return true, if toolbar is enabled, false otherwise
	 */
	public boolean isToolBarEnabled(String id) {
		return getViewItem().getBoolean("toolbars", id, true);
	}

	/**
	 * Load the window position, size and maximization state
	 *  
	 */
	public void loadPositions(ViewItem viewItem) {

		// *20030831, karlpeder* Also location is restored
		int x = viewItem.getInteger("window", "x");
		int y = viewItem.getInteger("window", "y");
		int w = viewItem.getInteger("window", "width");
		int h = viewItem.getInteger("window", "height");
		boolean maximized = viewItem.getBoolean("window", "maximized", true);

		//if (WindowMaximizer.isWindowMaximized(this) == false) {
		// if window is maximized -> ignore the window size
		// properties
		if (maximized) {
			WindowMaximizer.maximize(this);
		} else {
			// otherwise, use window size property
			Dimension dim = new Dimension(w, h);
			Point p = new Point(x, y);
			setSize(dim);
			setLocation(p);

			validate();
		}
		//}

		getFrameMediator().loadPositions(viewItem);
	}

	/**
	 * 
	 * Save current window position, size and maximization state
	 *  
	 */
	public void savePositions(ViewItem viewItem) {

		java.awt.Dimension d = getSize();
		java.awt.Point loc = getLocation();

		WindowItem item = getViewItem().getWindowItem();

		// *20030831, karlpeder* Now also location is stored
		item.set("x", loc.x);
		item.set("y", loc.y);
		item.set("width", d.width);
		item.set("height", d.height);

		boolean isMaximized = WindowMaximizer.isWindowMaximized(this);

		item.set("maximized", isMaximized);

		getFrameMediator().savePositions(viewItem);
	}

	/**
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0) {
		close();
	}

	/**
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent arg0) {
	}

	/**
	 * @see org.columba.core.gui.frame.View#getToolBar()
	 */
	public ColumbaToolBar getToolBar() {

		return toolbar;
	}

	/**
	 * @see org.columba.core.gui.frame.View#extendMenuFromFile(java.lang.String)
	 */
	public void extendMenuFromFile(FrameMediator mediator, String file) {
		getMenu().extendMenuFromFile(mediator, file);
	}

	/**
	 * @see org.columba.core.gui.frame.View#setContentPane(org.columba.core.gui.frame.neu.FrameView)
	 */
	public void setContentPane(ContentPane view) {

		// remove all components from content pane
		contentPane.removeAll();

		// add new componnet
		contentPane.add(view.getComponent(), BorderLayout.CENTER);

		// show/hide new toolbar
		enableToolBar(Container.MAIN_TOOLBAR,
				isToolBarEnabled(Container.MAIN_TOOLBAR));

		// show/hide new infopanel
		enableInfoPanel(isInfoPanelEnabled());

		// make window visible
		setVisible(true);

		if (!switchedFrameMediator) {
			// load window position
			loadPositions(getViewItem());
			validate();
		}

		switchedFrameMediator = false;

	}

	/**
	 * @see org.columba.core.gui.frame.View#extendToolbar(org.columba.core.xml.XmlElement)
	 */
	public void extendToolbar(FrameMediator mediator, XmlElement element) {

		getToolBar().extendToolbar(element, mediator);

	}

	/**
	 * @see org.columba.core.gui.frame.View#getFrame()
	 */
	public JFrame getFrame() {
		return this;
	}

	/**
	 * @see org.columba.core.gui.frame.View#getMenu()
	 */
	public ColumbaMenu getMenu() {
		return menu;
	}

	/**
	 * Save window properties and close the window. This includes telling the
	 * frame model that this window/frame is closing, so it can be
	 * "unregistered" correctly
	 */
	public void close() {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Closing DefaultContainer: " + this.getClass().getName());
		}

		// save window position
		savePositions(getViewItem());

		// hide window
		setVisible(false);

		//
		// Tell frame model that frame is closing. If this frame hasn't been
		// opened using FrameModel methods, FrameModel.close does nothing.
		//
		MainInterface.frameModel.close(this);

	}

	/**
	 * @see org.columba.core.gui.frame.Container#addToolBar(javax.swing.JComponent)
	 */
	public void addToolBar(JComponent c) {
		toolbarPane.add(c);
	}

	/**
	 * @see org.columba.core.gui.frame.Container#getInfoPanel()
	 */
	public ContainerInfoPanel getInfoPanel() {
		return infoPanel;
	}

	/**
	 * @see org.columba.core.gui.frame.Container#setInfoPanel(org.columba.core.gui.frame.ContainerInfoPanel)
	 */
	public void setInfoPanel(ContainerInfoPanel panel) {
		this.infoPanel = panel;

		toolbarPane.removeAll();
		if (getToolBar() != null)
			toolbarPane.add(getToolBar());
		toolbarPane.add(panel);

	}

	/**
	 * @see org.columba.core.gui.frame.Container#setToolBar(org.columba.core.gui.toolbar.ToolBar)
	 */
	public void setToolBar(ColumbaToolBar toolbar) {
		this.toolbar = toolbar;

		toolbarPane.removeAll();
		toolbarPane.add(toolbar);
		if (getInfoPanel() != null)
			toolbarPane.add(getInfoPanel());

	}

	/**
	 * @see org.columba.core.gui.frame.Container#enableInfoPanel(boolean)
	 */
	public void enableInfoPanel(boolean enable) {
		getViewItem().set("toolbars", "infopanel", enable);

		toolbarPane.removeAll();

		if (enable) {
			if (isToolBarEnabled(Container.MAIN_TOOLBAR)) {
				toolbarPane.add(getToolBar());
			}
			toolbarPane.add(getInfoPanel());
		} else {
			if (isToolBarEnabled(Container.MAIN_TOOLBAR)) {
				toolbarPane.add(getToolBar());
			}
		}
		validate();
	}

	/**
	 * @see org.columba.core.gui.frame.Container#isInfoPanelEnabled()
	 */
	public boolean isInfoPanelEnabled() {
		return getViewItem().getBoolean("toolbars", "infopanel", true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7677.java