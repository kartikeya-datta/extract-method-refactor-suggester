error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7738.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7738.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7738.java
text:
```scala
n@@ew NewAppointmentAction(this, range).actionPerformed(null);

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
package org.columba.calendar.ui.frame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.columba.api.gui.frame.IContainer;
import org.columba.calendar.base.api.IActivity;
import org.columba.calendar.model.api.IDateRange;
import org.columba.calendar.ui.action.ActivityMovedAction;
import org.columba.calendar.ui.action.EditActivityAction;
import org.columba.calendar.ui.action.NewAppointmentAction;
import org.columba.calendar.ui.calendar.MainCalendarController;
import org.columba.calendar.ui.calendar.api.ICalendarView;
import org.columba.calendar.ui.frame.api.ICalendarMediator;
import org.columba.calendar.ui.list.CalendarListController;
import org.columba.calendar.ui.list.api.ICalendarListView;
import org.columba.calendar.ui.navigation.NavigationController;
import org.columba.calendar.ui.navigation.api.DateRangeChangedEvent;
import org.columba.calendar.ui.navigation.api.ICalendarNavigationView;
import org.columba.calendar.ui.navigation.api.IDateRangeChangedListener;
import org.columba.core.config.ViewItem;
import org.columba.core.gui.docking.DockableView;
import org.columba.core.gui.frame.DockFrameController;
import org.flexdock.docking.DockingConstants;

/**
 * @author fdietz
 * 
 */
public class CalendarFrameMediator extends DockFrameController implements
		ICalendarMediator {

	public static final String PROP_FILTERED = "filterRow";

	private CalendarListController listController;

	public MainCalendarController calendarController;

	private ICalendarNavigationView navigationController;

	private DockableView listPanel;

	private DockableView calendarPanel;

	private DockableView navigationPanel;

	/**
	 * @param viewItem
	 */
	public CalendarFrameMediator(ViewItem viewItem) {
		super(viewItem);

		// TestDataGenerator.generateTestData();

		calendarController = new MainCalendarController(this);

		navigationController = new NavigationController();

		navigationController
				.addSelectionChangedListener(new IDateRangeChangedListener() {
					public void selectionChanged(DateRangeChangedEvent object) {
						calendarController.setVisibleDateRange(object
								.getDateRange());

					}
				});

		listController = new CalendarListController(this);
		listController.getView().addMouseListener(new ListMouseListener());

		registerDockables();

		initContextMenu();

	}

	private void initContextMenu() {
		listController.createPopupMenu(this);
		calendarController.createPopupMenu(this);
	}

	public void registerDockables() {
		// init dockable panels
		listPanel = new DockableView("calendar_tree", "Calendar");
		JScrollPane treeScrollPane = new JScrollPane(listController.getView());
		// set background of scrollpane, in case the list is smaller than the dockable
		treeScrollPane.getViewport().setBackground(UIManager.getColor("List.background"));
		treeScrollPane.setBackground(UIManager.getColor("List.background"));
		treeScrollPane.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		listPanel.setContentPane(treeScrollPane);

		navigationPanel = new DockableView("navigation", "Navigation");
		JScrollPane tableScrollPane = new JScrollPane(navigationController
				.getView());
		tableScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		navigationPanel.setContentPane(tableScrollPane);

		calendarPanel = new DockableView("main_calendar", "Main Calendar");

		calendarPanel.setContentPane(calendarController.getView());

	}

	/**
	 * @see org.columba.core.gui.frame.DockFrameController#loadDefaultPosition()
	 */
	public void loadDefaultPosition() {

		super.dock(calendarPanel, DockingConstants.CENTER_REGION);
		calendarPanel.dock(listPanel, DockingConstants.WEST_REGION, 0.2f);
		listPanel.dock(navigationPanel, DockingConstants.SOUTH_REGION, 0.2f);

		super.setSplitProportion(listPanel, 0.2f);
		super.setSplitProportion(calendarPanel, 0.2f);
	}

	public String[] getDockableIds() {

		return new String[] { "calendar_tree", "navigation", "main_calendar" };
	}

	/** *********************** container callbacks ************* */

	/**
	 * @see org.columba.api.gui.frame.IFrameMediator#extendMenu(org.columba.api.gui.frame.IContainer)
	 */
	public void extendMenu(IContainer container) {
		InputStream is = this.getClass().getResourceAsStream(
				"/org/columba/calendar/action/menu.xml");
		getContainer().extendMenu(this, is);
	}

	/**
	 * @see org.columba.api.gui.frame.IFrameMediator#extendToolBar(org.columba.api.gui.frame.IContainer)
	 */
	public void extendToolBar(IContainer container) {

		InputStream is2 = this.getClass().getResourceAsStream(
				"/org/columba/calendar/action/toolbar.xml");
		getContainer().extendToolbar(this, is2);
	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#showDayView()
	 */
	public void showDayView() {

		calendarController.setViewMode(ICalendarView.VIEW_MODE_DAY);

		navigationController
				.setSelectionMode(NavigationController.SELECTION_MODE_DAY);

	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#showWeekView()
	 */
	public void showWeekView() {
		calendarController.setViewMode(ICalendarView.VIEW_MODE_WEEK);

		navigationController
				.setSelectionMode(NavigationController.SELECTION_MODE_WEEK);

	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#showWorkWeekView()
	 */
	public void showWorkWeekView() {
		calendarController.setViewMode(ICalendarView.VIEW_MODE_WORK_WEEK);

		navigationController
				.setSelectionMode(NavigationController.SELECTION_MODE_WORK_WEEK);

	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#showMonthView()
	 */
	public void showMonthView() {
		calendarController.setViewMode(ICalendarView.VIEW_MODE_MONTH);

		navigationController
				.setSelectionMode(NavigationController.SELECTION_MODE_MONTH);

	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#getCalendarView()
	 */
	public ICalendarView getCalendarView() {
		return calendarController;
	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#getListView()
	 */
	public ICalendarListView getListView() {
		return listController;
	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#getNavigationView()
	 */
	public ICalendarNavigationView getNavigationView() {
		return navigationController;
	}

	/**
	 * Double-click mouse listener for calendar list component.
	 */
	class ListMouseListener extends MouseAdapter {

		/**
		 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent event) {
			if (event.isPopupTrigger()) {
				processPopup(event);
			}
		}

		/**
		 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent event) {
			if (event.isPopupTrigger()) {
				processPopup(event);
			}
		}

		/**
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent event) {
			// if mouse button was pressed twice times
			if (event.getClickCount() == 2) {

			}
		}

		protected void processPopup(final MouseEvent event) {

			listController.getPopupMenu().show(event.getComponent(),
					event.getX(), event.getY());

		}
	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#fireFilterUpdated()
	 */
	public void fireFilterUpdated() {
		calendarController.recreateFilterRows();

	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#fireActivityMoved(org.columba.calendar.base.api.IActivity)
	 */
	public void fireActivityMoved(IActivity activity) {
		new ActivityMovedAction(this).actionPerformed(null);
	}

	/**
	 * @see org.columba.calendar.ui.frame.api.ICalendarMediator#fireStartActivityEditing(org.columba.calendar.base.api.IActivity)
	 */
	public void fireStartActivityEditing(IActivity activity) {
		new EditActivityAction(this).actionPerformed(null);
	}

	public void fireCreateActivity(IDateRange range) {
		new NewAppointmentAction(this).actionPerformed(null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7738.java