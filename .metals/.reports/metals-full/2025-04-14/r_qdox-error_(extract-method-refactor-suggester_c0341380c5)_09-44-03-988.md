error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7779.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7779.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7779.java
text:
```scala
t@@his.setIconImage(ImageLoader.getMiscIcon("icon16.png").getImage());

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
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.event.EventListenerList;

import org.columba.api.exception.PluginException;
import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.gui.frame.IContainer;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.gui.frame.event.FrameEvent;
import org.columba.api.gui.frame.event.IContainerListener;
import org.columba.api.gui.frame.event.IFrameMediatorListener;
import org.columba.api.plugin.ExtensionMetadata;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.api.statusbar.IStatusBar;
import org.columba.api.statusbar.IStatusBarExtension;
import org.columba.core.command.TaskManager;
import org.columba.core.config.ViewItem;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.gui.menu.ExtendableMenuBar;
import org.columba.core.gui.menu.MenuXMLDecoder;
import org.columba.core.gui.statusbar.StatusBar;
import org.columba.core.gui.toolbar.ExtendableToolBar;
import org.columba.core.gui.toolbar.ToolBarXMLDecoder;
import org.columba.core.gui.util.MenuThrobber;
import org.columba.core.io.DiskIO;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.Extension;
import org.columba.core.plugin.PluginManager;
import org.columba.core.resourceloader.ImageLoader;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.drag.effects.EffectsManager;
import org.flexdock.docking.drag.preview.GhostPreview;
import org.flexdock.perspective.PerspectiveManager;

/**
 * @author fdietz
 * 
 */

public class DefaultContainer extends JFrame implements IContainer,
		WindowListener, IFrameMediatorListener {

	protected static final int DEFAULT_WIDTH = (int) Math.round(Toolkit
			.getDefaultToolkit().getScreenSize().width * .66);

	protected static final int DEFAULT_HEIGHT = (int) Math.round(Toolkit
			.getDefaultToolkit().getScreenSize().height * .66);

	private static final int DEFAULT_X = (int) Math.round(Toolkit
			.getDefaultToolkit().getScreenSize().width * .16);

	private static final int DEFAULT_Y = (int) Math.round(Toolkit
			.getDefaultToolkit().getScreenSize().height * .16);

	private static final Logger LOG = Logger
			.getLogger("org.columba.core.gui.frame");

	private DefaultFrameController mediator;

	private ViewItem viewItem;

	protected ExtendableMenuBar menubar;

	protected ExtendableToolBar toolbar;

	protected StatusBar statusBar;

	protected JPanel contentPane;

	protected boolean switchedFrameMediator = false;

	private String windowname;

	private boolean defaultCloseOperation;

	protected EventListenerList listenerList = new EventListenerList();

	public DefaultContainer(DefaultFrameController mediator) {
		super();

		if (mediator == null)
			throw new IllegalArgumentException("mediator == null");

		this.viewItem = mediator.getViewItem();
		this.mediator = mediator;

		defaultCloseOperation = true;

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		mediator.setContainer(this);
		initComponents();
		createMenuBar();
		setFrameMediator(mediator);

		mediator.addListener(this);
	}

	/**
	 * 
	 */
	public DefaultContainer(ViewItem viewItem) {
		super();

		this.viewItem = viewItem;

		defaultCloseOperation = true;

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// create new default frame controller
		mediator = new DefaultFrameController(viewItem);

		initComponents();

		mediator.setContainer(this);
		createMenuBar();

		mediator.addListener(this);
	}

	/**
	 * 
	 */
	protected void initComponents() {

		// Set the icon and the title
		this.setIconImage(ImageLoader.getImageIcon("icon16.png").getImage());
		windowname = "Columba";

		setTitle("");

		// register statusbar at global taskmanager
		statusBar = new StatusBar(TaskManager.getInstance());

		// JPanel panel = (JPanel) this.getContentPane();
		this.getContentPane().setLayout(new BorderLayout());

		// add statusbar

		// add toolbar
		// panel.add(toolbar, BorderLayout.NORTH);

		contentPane = new JPanel();
		// contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());

		contentPane.add(statusBar, BorderLayout.SOUTH);

		this.getContentPane().add(contentPane, BorderLayout.CENTER);

		initDockingConfiguration();

		// contentPane.add(dockingPort, BorderLayout.CENTER);

		// createMenuBar();

		// create toolbar
		toolbar = new ExtendableToolBar();
		setToolBar(toolbar);

		// add window listener
		addWindowListener(this);

	}

	private void initDockingConfiguration() {
		// turn on floating support
		DockingManager.setFloatingEnabled(true);
		PerspectiveManager.setRestoreFloatingOnLoad(true);

		// enable flexdock ghost preview
		EffectsManager.setPreview(new GhostPreview());

	}

	private void createMenuBar() {
		try {
			InputStream is = DiskIO
					.getResourceStream("org/columba/core/action/menu.xml");

			// create menu
			menubar = new MenuXMLDecoder(mediator).createMenuBar(is);

			if (menubar != null) {
				setJMenuBar(menubar);
			}
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
	}

	/**
	 * 
	 * @return statusbar
	 */
	public IStatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#setFrameMediator(org.columba.api.gui.frame.IFrameMediator)
	 */
	public void setFrameMediator(final IFrameMediator m) {
		LOG.fine("set framemediator to " + m.getClass());

		// remove from old mediator's listener list
		this.mediator.removeListener(this);

		this.mediator = (DefaultFrameController) m;

		m.setContainer(this);

		// use new viewitem
		viewItem = mediator.getViewItem();

		switchedFrameMediator = false;

		mediator.extendMenu(this);
		mediator.extendToolBar(this);
		mediator.initFrame(this);

		// update content-pane
		setContentPane(m.getContentPane());

		try {
			IExtensionHandler handler =  PluginManager
					.getInstance().getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_STATUSBAR);
			Enumeration e = handler.getExtensionEnumeration();
			while (e.hasMoreElements()) {
				Extension ext = (Extension) e.nextElement();
				try {
					IStatusBarExtension comp = (IStatusBarExtension) ext
							.instanciateExtension(new Object[] { m });
					statusBar.addComponent(comp);
				} catch (PluginException e1) {
					e1.printStackTrace();
				}
			}
		} catch (PluginHandlerNotFoundException e) {
			e.printStackTrace();
		}
		// add to new mediator's listener list
		mediator.addListener(this);

		mediator.fireComponentChanged();
	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#switchFrameMediator(org.columba.api.gui.frame.IFrameMediator)
	 */
	public void switchFrameMediator(IFrameMediator m) {
		LOG.fine("switching framemediator to " + m.getClass());

		// remove from old mediator's listener list
		this.mediator.removeListener(this);

		this.mediator = (DefaultFrameController) m;

		m.setContainer(this);

		// use new viewitem
		viewItem = mediator.getViewItem();

		switchedFrameMediator = true;

		try {
			InputStream is = DiskIO
					.getResourceStream("org/columba/core/action/menu.xml");

			// default core menu
			menubar = new MenuXMLDecoder(mediator).createMenuBar(is);

			// setJMenuBar(menubar);

		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}

		mediator.extendMenu(this);

		mediator.initFrame(this);

		// default toolbar
		toolbar = new ExtendableToolBar();
		mediator.extendToolBar(this);

		setToolBar(toolbar);

		// update content-pane
		setContentPane(m.getContentPane());

		try {
			IExtensionHandler handler =  PluginManager
					.getInstance().getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_STATUSBAR);
			Enumeration e = handler.getExtensionEnumeration();
			while (e.hasMoreElements()) {
				Extension ext = (Extension) e.nextElement();
				try {
					IStatusBarExtension comp = (IStatusBarExtension) ext
							.instanciateExtension(new Object[] { m });
					statusBar.addComponent(comp);
				} catch (PluginException e1) {
					e1.printStackTrace();
				}
			}
		} catch (PluginHandlerNotFoundException e) {
			e.printStackTrace();
		}

		// add to new mediator's listener list
		mediator.addListener(this);

		fireComponentChanged(mediator);

		mediator.fireComponentChanged();
	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#getFrameMediator()
	 */
	public IFrameMediator getFrameMediator() {

		return mediator;
	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#getViewItem()
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
		getViewItem().setBoolean("toolbars", id, enable);

		getToolBar().setVisible(enable);

		// toolbarPane.removeAll();
		//
		// if (enable) {
		// toolbarPane.add(getToolBar());
		//
		// }

		// awt-event-thread
		// javax.swing.SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		// validate();
		// }
		// });
	}

	/**
	 * Returns true if the toolbar is enabled
	 * 
	 * @param id
	 *            ID of controller
	 * @return true, if toolbar is enabled, false otherwise
	 */
	public boolean isToolBarEnabled(String id) {
		return getViewItem().getBooleanWithDefault("toolbars", id, true);
	}

	/**
	 * Load the window position, size and maximization state
	 * 
	 */
	public void loadPositions(ViewItem viewItem) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// *20030831, karlpeder* Also location is restored
		int x = viewItem.getIntegerWithDefault(ViewItem.WINDOW,
				ViewItem.POSITION_X_INT, DEFAULT_X);
		int y = viewItem.getIntegerWithDefault(ViewItem.WINDOW,
				ViewItem.POSITION_Y_INT, DEFAULT_Y);
		int w = viewItem.getIntegerWithDefault(ViewItem.WINDOW,
				ViewItem.WIDTH_INT, DEFAULT_WIDTH);
		int h = viewItem.getIntegerWithDefault(ViewItem.WINDOW,
				ViewItem.HEIGHT_INT, DEFAULT_HEIGHT);
		final boolean maximized = viewItem.getBooleanWithDefault(
				ViewItem.WINDOW, ViewItem.MAXIMIZED_BOOL, false);

		// if window is maximized -> ignore the window size properties
		// otherwise, use window size property
		// but ensure that the window is completly visible on the
		// desktop
		x = Math.max(x, 0);
		y = Math.max(y, 0);

		final Dimension dim = new Dimension(Math.min(w, screenSize.width - x),
				Math.min(h, screenSize.height - y));

		final Point p = new Point(x, y);
		final Frame frame = this;

		if (maximized) {
			WindowMaximizer.maximize(frame);
		} else {
			frame.setLocation(p);
			frame.setSize(dim);
		}

		mediator.loadPositions();

		// awt-event-thread
		// javax.swing.SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		//				
		//				
		//				
		//				
		// }
		// });

	}

	/**
	 * 
	 * Save current window position, size and maximization state
	 * 
	 */
	private void savePositions() {

		java.awt.Dimension d = getSize();
		java.awt.Point loc = getLocation();

		ViewItem item = getViewItem();

		boolean isMaximized = WindowMaximizer.isWindowMaximized(this);
		item.setBoolean(ViewItem.WINDOW, ViewItem.MAXIMIZED_BOOL, isMaximized);

		if (!isMaximized) {
			// *20030831, karlpeder* Now also location is stored
			item.setInteger(ViewItem.WINDOW, ViewItem.POSITION_X_INT, loc.x);
			item.setInteger(ViewItem.WINDOW, ViewItem.POSITION_Y_INT, loc.y);
			item.setInteger(ViewItem.WINDOW, ViewItem.WIDTH_INT, d.width);
			item.setInteger(ViewItem.WINDOW, ViewItem.HEIGHT_INT, d.height);
		}

		mediator.savePositions();

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
	 * @see org.columba.api.gui.frame.View#getToolBar()
	 */
	public JToolBar getToolBar() {
		return toolbar;
	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#extendMenu(org.columba.api.gui.frame.IFrameMediator,
	 *      java.io.InputStream)
	 */
	public void extendMenu(IFrameMediator mediator, InputStream is) {

		new MenuXMLDecoder(mediator).extendMenuBar(menubar, is);

		try {
			IExtensionHandler handler = PluginManager
					.getInstance().getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_ACTION);
			Enumeration e = handler.getExternalExtensionsEnumeration();
			while (e.hasMoreElements()) {
				IExtension extension = (IExtension) e.nextElement();
				// retrieve metadata
				ExtensionMetadata metadata = extension.getMetadata();
				String menuId = metadata.getAttribute("menuid");
				String placeholderId = metadata.getAttribute("placeholderid");

				if (menuId == null || placeholderId == null)
					continue;

				// add action to menu
				String extensionId = metadata.getId();
				try {
					AbstractColumbaAction action = (AbstractColumbaAction) extension
							.instanciateExtension(new Object[] { mediator });
					if (action == null)
						LOG.severe("action could not be instanciated: "
								+ extensionId);
					else
						((ExtendableMenuBar) menubar).insertAction(menuId,
								placeholderId, action);
				} catch (PluginException e1) {
					e1.printStackTrace();
				}
			}

		} catch (PluginHandlerNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see org.columba.api.gui.frame.View#setContentPane(org.columba.api.gui.frame.neu.FrameView)
	 */
	public void setContentPane(JPanel view) {

		LOG.finest("setting content-pane");

		getContentPane().removeAll();

		// remove old content pane
		contentPane.removeAll();
		// contentPane.remove(mediator.getContentPane());

		setJMenuBar(menubar);
		// add animated icon to right-hand side corner of menubar
		MenuThrobber.setThrobber(menubar);

		// // add new componnet
		contentPane.add(view, BorderLayout.CENTER);

		getContentPane().add(toolbar, BorderLayout.NORTH);

		contentPane.add(statusBar, BorderLayout.SOUTH);

		getContentPane().add(contentPane, BorderLayout.CENTER);

		// show/hide new toolbar
		enableToolBar(IContainer.MAIN_TOOLBAR,
				isToolBarEnabled(IContainer.MAIN_TOOLBAR));

		// getContentPane().validate();

		if (!switchedFrameMediator) {

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			// *20030831, karlpeder* Also location is restored
			int x = viewItem.getIntegerWithDefault(ViewItem.WINDOW,
					ViewItem.POSITION_X_INT, DEFAULT_X);
			int y = viewItem.getIntegerWithDefault(ViewItem.WINDOW,
					ViewItem.POSITION_Y_INT, DEFAULT_Y);
			int w = viewItem.getIntegerWithDefault(ViewItem.WINDOW,
					ViewItem.WIDTH_INT, DEFAULT_WIDTH);
			int h = viewItem.getIntegerWithDefault(ViewItem.WINDOW,
					ViewItem.HEIGHT_INT, DEFAULT_HEIGHT);
			final boolean maximized = viewItem.getBooleanWithDefault(
					ViewItem.WINDOW, ViewItem.MAXIMIZED_BOOL, false);

			// if window is maximized -> ignore the window size properties
			// otherwise, use window size property
			// but ensure that the window is completly visible on the
			// desktop
			x = Math.max(x, 0);
			y = Math.max(y, 0);

			final Dimension dim = new Dimension(Math.min(w, screenSize.width
					- x), Math.min(h, screenSize.height - y));

			final Point p = new Point(x, y);
			final Frame frame = this;

			if (maximized) {

				frame.setVisible(true);

				WindowMaximizer.maximize(frame);

				// awt-event-thread
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						mediator.loadPositions();
					}
				});

			} else {
				frame.setLocation(p);
				frame.setSize(dim);

				mediator.loadPositions();

				setVisible(true);
			}
			// make window visible
			// LOG.finest("setVisible()");

			// load window position
			// loadPositions(getViewItem());

		} else {
			if (!mediator.isInitialized())
				mediator.loadPositions();

			validateTree();

			view.repaint();
		}

		switchedFrameMediator = false;

	}

	/**
	 * @see org.columba.api.gui.frame.View#extendToolbar(org.columba.core.xml.XmlElement)
	 */
	public void extendToolbar(IFrameMediator mediator, InputStream is) {

		new ToolBarXMLDecoder(mediator).extendToolBar(
				(ExtendableToolBar) getToolBar(), is);

	}

	/**
	 * @see org.columba.api.gui.frame.View#getFrame()
	 */
	public JFrame getFrame() {
		return this;
	}

	/**
	 * @see org.columba.core.gui.frame.View#getMenuBar()
	 */
	// public ColumbaMenu getMenuBar() {
	// return menu;
	// }
	/**
	 * Save window properties and close the window. This includes telling the
	 * frame model that this window/frame is closing, so it can be
	 * "unregistered" correctly
	 */
	public void close() {

		// save window position
		savePositions();

		getFrameMediator().close(this);

		if (defaultCloseOperation == false)
			return;

		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Closing DefaultContainer: " + this.getClass().getName());
		}

		// hide window
		setVisible(false);

		//
		// Tell frame model that frame is closing. If this frame hasn't been
		// opened using FrameManager methods, FrameManager.close does nothing.
		//
		FrameManager.getInstance().close(this);

	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#addToolBar(javax.swing.JComponent)
	 */
	public void addToolBar(JComponent c) {
		// FIXME remove method
		// toolbarPane.add(c);
	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#setToolBar(org.columba.core.gui.toolbar.ToolBar)
	 */
	public void setToolBar(JToolBar toolbar) {
		if ((toolbar instanceof ExtendableToolBar) == false)
			throw new IllegalArgumentException(
					"only instances of ExtendableToolBar allowed");

		// getContentPane().remove(this.toolbar);

		this.toolbar = (ExtendableToolBar) toolbar;

		//
		// getContentPane().validate();

	}

	/**
	 * @see java.awt.Frame#setTitle(java.lang.String)
	 */
	public void setTitle(String arg0) {
		String title = windowname;

		if (Logging.DEBUG) {
			title += " DEBUG MODE";
		}

		if (arg0.length() > 0) {
			title = arg0 + " - " + title;
		}

		super.setTitle(title);
	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#setWindowName(java.lang.String)
	 */
	public void setWindowName(String name) {
		this.windowname = name;
		setTitle("");
	}

	/**
	 * @see org.columba.api.gui.frame.IContainer#setCloseOperation(boolean)
	 */
	public void setCloseOperation(boolean close) {
		this.defaultCloseOperation = close;
	}

	public JPanel getContentPanel() {
		return contentPane;
	}

	/** ********************* frame eventing ******************** */

	public void titleChanged(FrameEvent event) {
		setTitle(event.getText());
	}

	public void statusMessageChanged(FrameEvent event) {
		getStatusBar().displayTooltipMessage(event.getText());
	}

	public void taskStatusChanged(FrameEvent event) {
		getStatusBar().getDisplayedWorker().cancel();
	}

	public void visibilityChanged(FrameEvent event) {
		if (event.isVisible())
			setVisible(true);
		else
			setVisible(false);
	}

	public void layoutChanged(FrameEvent event) {
		validate();
	}

	public void closed(FrameEvent event) {
		close();
	}

	public void toolBarVisibilityChanged(FrameEvent event) {
		enableToolBar(IContainer.MAIN_TOOLBAR, event.isVisible());
	}

	public void switchedComponent(FrameEvent event) {
		// don't do anything
	}

	/** ******************** event listener ****************************** */

	public void addListener(IContainerListener l) {
		listenerList.add(IContainerListener.class, l);
	}

	public void removeListener(IContainerListener l) {
		listenerList.remove(IContainerListener.class, l);
	}

	public void fireComponentChanged(IFrameMediator mediator) {
		FrameEvent e = new FrameEvent(this, mediator);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IContainerListener.class) {
				((IContainerListener) listeners[i + 1]).switchedComponent(e);
			}
		}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7779.java