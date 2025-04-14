error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7318.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7318.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7318.java
text:
```scala
S@@tring[] search = settings.getArray(SEARCHHISTORY);

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * The FilteredTextTree is a filtered tree that uses an editable text.
 */
public class FilteredTextTree extends FilteredTree {
	// A list contains all strings in search history
	private List searchHistory;
	
	//A popup shell to hold the currentSeachTable 
	private Shell popupShell;

	//A key which is paired with a search history string as part of dialog settings
	private static final String SEARCHHISTORY = "search"; //$NON-NLS-1$

	// A table which contains only strings begin with typed strings
	private Table currentSeachTable;

	/**
	 * Create a new instance of the receiver.
	 * 
	 * @param parent
	 * @param treeStyle
	 */
	public FilteredTextTree(Composite parent, int treeStyle) {
		super(parent, treeStyle);
	}

	/**
	 * Create a new instance of the receiver with a supplied filter.
	 * 
	 * @param parent
	 * @param treeStyle
	 * @param filter
	 */
	public FilteredTextTree(Composite parent, int treeStyle,
			PatternItemFilter filter) {
		super(parent, treeStyle, filter);
		treeViewer.getControl().addFocusListener(new FocusAdapter(){
			/* Each time the tree gains focus, the current text in text area is saved as search history
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.FocusEvent)
			 */
			public void focusGained(FocusEvent e) {
				String newText = filterText.getText();				
				Object[] textValues = searchHistory.toArray();
				
				if((newText.equals(""))||(newText.equals(initialText)))//$NON-NLS-1$
					return;
		
				for (int i = 0; i < textValues.length; i++) {
					if(((String)textValues[i]).equals(newText))
						return;					
				}	
				searchHistory.add(newText);
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.dialogs.FilteredTree#createFilterControl(org.eclipse.swt.widgets.Composite)
	 */
	protected void createFilterControl(final Composite parent) {
		filterText = new Text(parent, SWT.DROP_DOWN | SWT.BORDER);
		filterText.setFont(parent.getFont());
		searchHistory = getPreferenceSearchHistory();

		popupShell = new Shell(parent.getShell(), SWT.NO_TRIM);
		popupShell
				.setBackground(parent.getDisplay().getSystemColor(
						SWT.COLOR_WHITE));
		GridLayout shellGL = new GridLayout();
		shellGL.marginHeight = 0;
		shellGL.marginWidth = 0;
		popupShell.setLayout(shellGL);
		popupShell.setLayoutData(new GridData(
				(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL)));

		currentSeachTable = new Table(popupShell, SWT.SINGLE | SWT.BORDER);
		currentSeachTable.setLayoutData(new GridData(
				(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL)));
		Font font = parent.getFont();	
		
		//Make sure the popup shell show whole words without scrollable horizontally
		currentSeachTable.setFont(new Font
						(parent.getDisplay(),font.getFontData()[0].getName(),
						 font.getFontData()[0].getHeight()-1,font.getFontData()[0].getStyle()));
		
		filterText.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
					popupShell.setVisible(false);
					if (getViewer().getTree().getItemCount() == 0) {
						Display.getCurrent().beep();
						setFilterText(""); //$NON-NLS-1$
					} else {
						getViewer().getTree().setFocus();
					}
				}
			}
		});

		filterText.addKeyListener(new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			public void keyReleased(KeyEvent e) {

				if (e.keyCode == SWT.ARROW_DOWN) {
					if (currentSeachTable.isVisible()) {
						// Make selection at popup table
						if (currentSeachTable.getSelectionCount() < 1)
							currentSeachTable.setSelection(0);
						currentSeachTable.setFocus();
					} else
						// Make selection be on the left tree
						treeViewer.getTree().setFocus();
				} else {
					if (e.character == SWT.CR){						
						int index =currentSeachTable.getSelectionIndex();
						setFilterText(currentSeachTable.getItem(index).getText());
						textChanged();						
						popupShell.setVisible(false);
						return;
					}						
					textChanged();
					List result = new ArrayList();
					result = reduceSearch(searchHistory, filterText.getText());
					updateTable(currentSeachTable, result);

					if (currentSeachTable.getItemCount() > 0) {
						Rectangle textBounds = filterText.getBounds();
												
						setShellLocationAndSize(parent,textBounds);

						if (popupShell.isDisposed())
							popupShell.open();

						if (!popupShell.getVisible()) {
							popupShell.setVisible(true);
							filterText.setFocus();
						}

					} else
						popupShell.setVisible(false);
				}

			}
		});

		parent.getDisplay().addFilter(SWT.MouseDown, new Listener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Events)
			 */
			public void handleEvent(Event event) {
				if (!popupShell.isDisposed())
					popupShell.setVisible(false);
			}
		});

		getShell().addControlListener(new ControlAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
			 */
			public void controlMoved(ControlEvent e) {
				popupShell.setVisible(false);
			}
		});

		currentSeachTable.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {

				setFilterText(currentSeachTable.getSelection()[0].getText());
				textChanged();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				popupShell.setVisible(false);
			}
		});

		filterText.addDisposeListener(new DisposeListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
			 */
			public void widgetDisposed(DisposeEvent e) {
				saveDialogSettings();
			}
		});

		filterText.getAccessible().addAccessibleListener(
				getAccessibleListener());

	}

	/**
	 * Find all items which start with typed words list the list contains all
	 * strings of the search history
	 * 
	 * @param list
	 *            the list to search
	 * @param wordsEntered
	 *            String
	 * @return a list in which all strings start from the typed letter(s)
	 */
	private List reduceSearch(List list, String wordsEntered) {
		List result = new ArrayList();
		if (list == null)
			return result;
		for (int i = 0; i < list.size(); i++) {
			if (filterText.getText() == "") //$NON-NLS-1$
						return result;
		            String historyString = (String) list.get(i);
				    String typedString = wordsEntered;
				    if (historyString.toLowerCase().startsWith(typedString.toLowerCase()))
					    result.add(historyString);
				}
		
				return result;
		 	}
     /**
	 * Calculate and set the position and size of the popup shell
	 * @param parent
	 * @param textBounds
	 */
	private void setShellLocationAndSize(Composite parent, Rectangle  textBounds){
		
		//Caculate size of the popup shell
		int space = currentSeachTable.getItemHeight();
		int tableHeight = currentSeachTable
			    .getItemHeight()* currentSeachTable.getItemCount() + space;
		int tableWidth = textBounds.width;	
		popupShell.setSize(tableWidth,tableHeight);
		
		//Caculate x,y coordinator of the popup shell
		Point point = getDisplay().map(parent, null,
				textBounds.x, textBounds.y);	
		final int xCoord = point.x;
		final int yCoord = point.y + textBounds.height;
		
		final Point location = new Point(xCoord, yCoord);
		
		//Try to show whole popup shell through relocating its x and y coordinator
		final Display display = popupShell.getDisplay();		
		final Rectangle displayBounds = display.getClientArea();
		final int displayRightEdge = displayBounds.x + displayBounds.width;
		
		if (location.x <0) 
			location.x = 0;
		if ((location.x + tableWidth) > displayRightEdge) 
			location.x = displayRightEdge - tableWidth;
		
		final int displayBottomEdge = displayBounds.y + displayBounds.height;	
		if ((location.y + tableHeight) > displayBottomEdge)
			location.y = displayBottomEdge - tableHeight;
		
		// Set the location.
		popupShell.setLocation(location);		
	}

	/**
	 * Copy all elements from a list to a table
	 * 
	 * @param table
	 * @param list
	 */
	private void updateTable(Table table, List list) {
		table.removeAll();
		if (list.size() > 0) {
			TableItem newItem;
			for (int i = 0; i < list.size(); i++) {
				newItem = new TableItem(table, SWT.NULL, i);
				newItem.setText((String) list.get(i));

			}
		}

	}

	/**
	 * Return a dialog setting section for this dialog
	 * 
	 * @return IDialogSettings
	 */
	private IDialogSettings getDialogSettings(){
		IDialogSettings settings = WorkbenchPlugin.getDefault()
				.getDialogSettings();
		IDialogSettings thisSettings = settings
				.getSection(getClass().getName());
		if (thisSettings == null)
			thisSettings = settings.addNewSection(getClass().getName());

		return thisSettings;
	}

	/**
	 * Get the preferences search history for this eclipse's start, Note that
	 * this history will not be cleared until this eclipse closes
	 * 
	 * @return a list
	 */
	private List getPreferenceSearchHistory() {

		List searchList = new ArrayList();
		IDialogSettings settings = getDialogSettings();
		String[] search = settings.getArray(SEARCHHISTORY); //$NON-NLS-1$

		if (search != null) {
			for (int i = 0; i < search.length; i++)
				searchList.add(search[i]);
		}
		return searchList;

	}

	/**
	 * Saves the search history.
	 */
	private void saveDialogSettings(){
		IDialogSettings settings = getDialogSettings();

		// If the settings contains the same key, the previous value will be
		// replaced by new one
		String[] result = new String[searchHistory.size()];
		listToArray(searchHistory, result);
		settings.put(SEARCHHISTORY, result);

	}

	private void listToArray(List list, String[] string) {
		int size = list.size();
		for (int i = 0; i < size; i++)
			string[i] = (String) list.get(i);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7318.java