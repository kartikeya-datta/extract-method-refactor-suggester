error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12111.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12111.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12111.java
text:
```scala
c@@olumn.postEvent (SWT.Selection, newEvent);

package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

public class Tree extends Composite {
	Canvas header;
	TreeColumn[] columns = new TreeColumn [0];
	TreeItem[] items = new TreeItem [0];
	TreeItem[] availableItems = new TreeItem [0];
	TreeItem[] selectedItems = new TreeItem [0];
	TreeItem focusItem;
	TreeItem anchorItem;
	TreeItem insertMarkItem;
	TreeItem lastClickedItem;
	boolean insertMarkPrecedes = false;
	boolean linesVisible;
	int topIndex = 0, horizontalOffset = 0;
	int fontHeight = 0, imageHeight = 0, itemHeight = 0;
	int col0ImageWidth = 0;
	int headerImageHeight = 0;
	TreeColumn resizeColumn;
	int resizeColumnX = -1;
	boolean inExpand = false;	/* for item creation within Expand callback */

	Rectangle expanderBounds, checkboxBounds;

	static final int MARGIN_IMAGE = 3;
	static final int MARGIN_CELL = 1;
	static final int SIZE_HORIZONTALSCROLL = 5;
	static final int TOLLERANCE_COLUMNRESIZE = 2;
	static final int WIDTH_HEADER_SHADOW = 2;
	static final int WIDTH_CELL_HIGHLIGHT = 1;
	static final String ELLIPSIS = "...";						//$NON-NLS-1$
	static final String ID_EXPANDED = "EXPANDED";				//$NON-NLS-1$
	static final String ID_COLLAPSED = "COLLAPSED";			//$NON-NLS-1$
	static final String ID_UNCHECKED = "UNCHECKED";			//$NON-NLS-1$
	static final String ID_GRAYUNCHECKED = "GRAYUNCHECKED";	//$NON-NLS-1$
	static final String ID_CHECKMARK = "CHECKMARK";			//$NON-NLS-1$
	static final String ID_CONNECTOR_COLOR = "CONNECTOR_COLOR";	//$NON-NLS-1$

public Tree (Composite parent, int style) {
	super (parent, checkStyle (style | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_REDRAW_RESIZE));
	setForeground (display.getSystemColor (SWT.COLOR_LIST_FOREGROUND));
	setBackground (display.getSystemColor (SWT.COLOR_LIST_BACKGROUND));
	GC gc = new GC (this);
	fontHeight = gc.getFontMetrics ().getHeight ();
	gc.dispose ();
	itemHeight = fontHeight + (2 * getCellPadding ());
	initImages (display);
	expanderBounds = getExpandedImage ().getBounds ();
	checkboxBounds = getUncheckedImage ().getBounds ();
	
	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			handleEvents (event);
		}
	};
	addListener (SWT.Paint, listener);
	addListener (SWT.MouseDown, listener);
	addListener (SWT.MouseUp, listener);
	addListener (SWT.MouseDoubleClick, listener);
	addListener (SWT.Dispose, listener);	
	addListener (SWT.Resize, listener);
	addListener (SWT.KeyDown, listener);
	addListener (SWT.FocusOut, listener);
	addListener (SWT.FocusIn, listener);
	addListener (SWT.Traverse, listener);
	
	header = new Canvas (this, SWT.NO_REDRAW_RESIZE | SWT.NO_FOCUS);
	header.setVisible (false);
	header.setLocation (0,0);
	header.addListener (SWT.Paint, listener);
	header.addListener (SWT.MouseDown, listener);
	header.addListener (SWT.MouseUp, listener);
	header.addListener (SWT.MouseMove, listener);
	header.addListener (SWT.MouseExit, listener);

	ScrollBar vBar = getVerticalBar ();
	ScrollBar hBar = getHorizontalBar ();
	vBar.setMaximum (1);
	hBar.setMaximum (1);
	vBar.setVisible (false);
	hBar.setVisible (false);
	vBar.addListener (SWT.Selection, listener);
	hBar.addListener (SWT.Selection, listener);
}
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);	
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}
public void addTreeListener (TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);	
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}
/*
 * Returns the index of the column that the specified x falls within, or
 * -1 if the x lies to the right of the last column.
 */
int computeColumnIntersect (int x, int startColumn) {
	if (columns.length - 1 < startColumn) return -1;
	int rightX = columns [startColumn].getX ();
	for (int i = startColumn; i < columns.length; i++) {
		rightX += columns [i].width;
		if (x <= rightX) return i;
	}
	return -1;
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint != SWT.DEFAULT) {
		width = wHint;
	} else {
		if (columns.length == 0) {
			for (int i = 0; i < items.length; i++) {
				Rectangle itemBounds = items [i].getBounds ();
				width = Math.max (width, itemBounds.x + itemBounds.width);
			}
		} else {
			TreeColumn lastColumn = columns [columns.length - 1];
			width = lastColumn.getX () + lastColumn.width;
		}
	}
	if (hHint != SWT.DEFAULT) {
		height = hHint;
	} else {
		height = getHeaderHeight () + items.length * itemHeight;
	}
	Rectangle result = computeTrim (0, 0, width, height);
	return new Point (result.width, result.height);
}
void createItem (TreeColumn column, int index) {
	TreeColumn[] newColumns = new TreeColumn [columns.length + 1];
	System.arraycopy (columns, 0, newColumns, 0, index);
	newColumns [index] = column;
	System.arraycopy (columns, index, newColumns, index + 1, columns.length - index);
	columns = newColumns;
	
	/* allow all items to update their internal structures accordingly */
	for (int i = 0; i < items.length; i++) {
		items [i].addColumn (column);
	}

	/* no visual update needed because column's initial width is 0 */
}
void createItem (TreeItem item, int index) {
	TreeItem[] newItems = new TreeItem [items.length + 1];
	System.arraycopy (items, 0, newItems, 0, index);
	newItems [index] = item;
	System.arraycopy (items, index, newItems, index + 1, items.length - index);
	items = newItems;

	/* determine the item's availability index */
	int startIndex;
	if (index == items.length - 1) {
		startIndex = availableItems.length;		/* last item */
	} else {
		startIndex = items [index + 1].availableIndex;
	}
	
	/* root items are always available so insert into available items collection */
	TreeItem[] newAvailableItems = new TreeItem [availableItems.length + 1];
	System.arraycopy (availableItems, 0, newAvailableItems, 0, startIndex);
	newAvailableItems [startIndex] = item;
	System.arraycopy (availableItems, startIndex, newAvailableItems, startIndex + 1, newAvailableItems.length - startIndex - 1);
	availableItems = newAvailableItems;
	
	/* update the availableIndex for items bumped down by this new item */
	for (int i = startIndex; i < availableItems.length; i++) {
		availableItems [i].availableIndex = i;
	}

	/* update scrollbars */
	updateVerticalBar ();
	Rectangle bounds = item.getBounds ();
	int rightX = bounds.x + bounds.width;
	updateHorizontalBar (rightX, rightX);
	/* 
	 * If new item is above viewport then adjust topIndex and the vertical
	 * scrollbar so that the current viewport items will not change.
	 */
	if (item.availableIndex < topIndex) {
		topIndex++;
		getVerticalBar ().setSelection (topIndex);
		return;
	}

	int redrawIndex = index;
	if (redrawIndex > 0 && item.isLastChild ()) redrawIndex--;
	redrawFromItemDownwards (items [redrawIndex].availableIndex);
}
public void deselectAll () {
	checkWidget ();
	TreeItem[] oldSelection = selectedItems;
	selectedItems = new TreeItem [0];
	for (int i = 0; i < oldSelection.length; i++) {
		redrawItem (oldSelection [i].availableIndex, true);
	}
}
void destroyItem (TreeColumn column) {
	int numColumns = columns.length;
	int index = column.getIndex ();

	TreeColumn[] newColumns = new TreeColumn [columns.length - 1];
	System.arraycopy (columns, 0, newColumns, 0, index);
	System.arraycopy (columns, index + 1, newColumns, index, newColumns.length - index);
	columns = newColumns;

	/* ensure that column 0 always has left-alignment */
	if (index == 0 && columns.length > 0) {
		columns [0].style |= SWT.LEFT;
		columns [0].style &= ~(SWT.CENTER | SWT.RIGHT);
	}
	
	/* allow all items to update their internal structures accordingly */
	for (int i = 0; i < items.length; i++) {
		items [i].removeColumn (column, index);
	}

	/* update horizontal scrollbar */
	int lastColumnIndex = columns.length - 1;
	if (lastColumnIndex < 0) {		/* no more columns */
		updateHorizontalBar ();
	} else {
		int newWidth = 0;
		for (int i = 0; i < columns.length; i++) {
			newWidth += columns [i].width;
		}
		ScrollBar hBar = getHorizontalBar (); 
		hBar.setMaximum (newWidth);
		hBar.setVisible (getClientArea ().width < newWidth);
		int selection = hBar.getSelection ();
		if (selection != horizontalOffset) {
			horizontalOffset = selection;
			redraw ();
		}
	}
	for (int i = index; i < columns.length; i++) {
		Event event = new Event ();
		event.widget = columns [i];
		columns [i].sendEvent (SWT.Move, event);
	}
}
/*
 * Allows the Tree to update internal structures it has that may contain the
 * item being destroyed.  The argument is not necessarily a root-level item.
 */
void destroyItem (TreeItem item) {
	/* availableItems array */
	int availableIndex = item.availableIndex; 
	if (availableIndex != -1) {
		Rectangle bounds = item.getBounds ();
		int rightX = bounds.x + bounds.width;
		TreeItem[] newAvailableItems = new TreeItem [availableItems.length - 1];
		System.arraycopy (availableItems, 0, newAvailableItems, 0, availableIndex);
		System.arraycopy (
			availableItems,
			availableIndex + 1,
			newAvailableItems,
			availableIndex,
			newAvailableItems.length - availableIndex);
		availableItems = newAvailableItems;
		/* update the availableIndex on affected items */
		for (int i = availableIndex; i < availableItems.length; i++) {
			availableItems [i].availableIndex = i;
		}
		item.availableIndex = -1;
		int oldTopIndex = topIndex;
		updateVerticalBar ();
		updateHorizontalBar (0, -rightX);
		/* 
		 * If destroyed item is above viewport then adjust topIndex and the vertical
		 * scrollbar so that the current viewport items will not change. 
		 */
		if (availableIndex < topIndex) {
			topIndex = oldTopIndex - 1;
			getVerticalBar ().setSelection (topIndex);
		}
	}
	/* selectedItems array */
	if (item.isSelected ()) {
		int selectionIndex = getSelectionIndex (item);
		TreeItem[] newSelectedItems = new TreeItem [selectedItems.length - 1];
		System.arraycopy (selectedItems, 0, newSelectedItems, 0, selectionIndex);
		System.arraycopy (
			selectedItems,
			selectionIndex + 1,
			newSelectedItems,
			selectionIndex,
			newSelectedItems.length - selectionIndex);
		selectedItems = newSelectedItems;
	}
	/* root-level items array */
	if (item.depth == 0) {
		int index = item.getIndex ();
		TreeItem[] newItems = new TreeItem [items.length - 1];
		System.arraycopy (items, 0, newItems, 0, index);
		System.arraycopy (items, index + 1, newItems, index, newItems.length - index);
		items = newItems;
	}
	if (item == focusItem) reassignFocus ();
	if (item == anchorItem) anchorItem = null;
	if (item == insertMarkItem) insertMarkItem = null;
}
TreeItem[] getAllItems () {
	int childCount = items.length;
	TreeItem[][] childResults = new TreeItem [childCount][];
	int count = 0;
	for (int i = 0; i < childCount; i++) {
		childResults [i] = items [i].computeAllDescendents ();
		count += childResults [i].length;
	}
	TreeItem[] result = new TreeItem [count];
	int index = 0;
	for (int i = 0; i < childCount; i++) {
		System.arraycopy (childResults [i], 0, result, index, childResults [i].length);
		index += childResults [i].length;
	}
	return result;
}
int getCellPadding () {
	return MARGIN_CELL + WIDTH_CELL_HIGHLIGHT; 
}
Image getCheckmarkImage () {
	return (Image) display.getData (ID_CHECKMARK);
}
public Control[] getChildren () {
	checkWidget ();
	Control[] controls = _getChildren ();
	if (header == null) return controls;
	Control[] result = new Control [controls.length - 1];
	/* remove the Header from the returned set of children */
	int index = 0;
	for (int i = 0; i < controls.length; i++) {
		 if (controls [i] != header) {
		 	result [index++] = controls [i];
		 }
	}
	return result;
}
Image getCollapsedImage () {
	return (Image) display.getData (ID_COLLAPSED);
}
public TreeColumn getColumn (int index) {
	checkWidget ();
	if (!(0 <= index && index < columns.length)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}
public int getColumnCount () {
	checkWidget ();
	return columns.length;
}
public TreeColumn[] getColumns () {
	checkWidget ();
	TreeColumn[] result = new TreeColumn [columns.length];
	System.arraycopy (columns, 0, result, 0, columns.length);
	return result;
}
Color getConnectorColor () {
	return (Color) display.getData (ID_CONNECTOR_COLOR);
}
Image getExpandedImage () {
	return (Image) display.getData (ID_EXPANDED);
}
Image getGrayUncheckedImage () {
	return (Image) display.getData (ID_GRAYUNCHECKED);
}
public int getGridLineWidth () {
	checkWidget ();
	return 1;
}
public int getHeaderHeight () {
	checkWidget ();
	if (!header.getVisible ()) return 0;
	return header.getSize ().y;
}
int getHeaderPadding () {
	return MARGIN_CELL + WIDTH_HEADER_SHADOW; 
}
public boolean getHeaderVisible () {
	checkWidget ();
	return header.getVisible ();
}
public TreeItem getItem (Point point) {
	checkWidget ();
	int index = (point.y - getHeaderHeight ()) / itemHeight - topIndex;
	if (!(0 <= index && index < availableItems.length)) return null;		/* below the last item */
	TreeItem result = availableItems [index];
	if (!result.getHitBounds ().contains (point)) return null;	/* considers the x value */
	return result;
}
public int getItemCount () {
	checkWidget ();
	return items.length;
}
public int getItemHeight () {
	checkWidget ();
	return itemHeight;
}
public TreeItem[] getItems () {
	checkWidget ();
	TreeItem[] result = new TreeItem [items.length];
	System.arraycopy (items, 0, result, 0, items.length);
	return result;	
}
/*
 * Returns the current y-coordinate that the specified item should have. 
 */
int getItemY (TreeItem item) {
	int index = item.availableIndex;
	if (index == -1) return -1;
	return (index - topIndex) * itemHeight + getHeaderHeight ();
}
public boolean getLinesVisible () {
	checkWidget ();
	return linesVisible;
}
public TreeItem getParentItem () {
	checkWidget ();
	return null;
}
public TreeItem[] getSelection () {
	checkWidget ();
	TreeItem[] result = new TreeItem [selectedItems.length];
	System.arraycopy (selectedItems, 0, result, 0, selectedItems.length);
	return result;
}
public int getSelectionCount () {
	checkWidget ();
	return selectedItems.length;
}
/*
 * Returns the index of the argument in the receiver's array of currently-
 * selected items, or -1 if the item is not currently selected.
 */
int getSelectionIndex (TreeItem item) {
	for (int i = 0; i < selectedItems.length; i++) {
		if (selectedItems [i] == item) return i;
	}
	return -1;
}
public TreeItem getTopItem () {
	checkWidget ();
	if (availableItems.length == 0) return null;
	return availableItems [topIndex];
}
Image getUncheckedImage () {
	return (Image) display.getData (ID_UNCHECKED);
}
void handleEvents (Event event) {
	switch (event.type) {
		case SWT.Paint:
			if (event.widget == header) {
				headerOnPaint (event);
			} else {
				onPaint (event);
			}
			break;
		case SWT.MouseDown:
			if (event.widget == header) {
				headerOnMouseDown (event);
			} else {
				onMouseDown (event);
			}
			break;
		case SWT.MouseUp:
			if (event.widget == header) {
				headerOnMouseUp (event);
			} else {
				onMouseUp (event);
			}
			break;
		case SWT.MouseMove:
			headerOnMouseMove (event); break;
		case SWT.MouseDoubleClick:
			onMouseDoubleClick (event); break;
		case SWT.MouseExit:
			headerOnMouseExit (); break;
		case SWT.Dispose:
			onDispose (); break;		
		case SWT.KeyDown:
			onKeyDown (event); break;
		case SWT.Resize:
			onResize (event); break;
		case SWT.Selection:
			if (event.widget == getVerticalBar ()) {
				onScrollVertical (event);
			} else {
				onScrollHorizontal (event);
			}
			break;
		case SWT.FocusOut:
			onFocusOut (); break;
		case SWT.FocusIn:
			onFocusIn (); break;	
		case SWT.Traverse:
			switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
				case SWT.TRAVERSE_PAGE_NEXT:
				case SWT.TRAVERSE_PAGE_PREVIOUS:
					event.doit = true;
					break;
			}
			break;			
	}
}
void headerOnMouseDown (Event event) {
	if (event.button != 1) return;
	for (int i = 0; i < columns.length; i++) {
		TreeColumn column = columns [i]; 
		int x = column.getX () + column.width;
		/* if close to a column separator line then begin column resize */
		if (Math.abs (x - event.x) <= TOLLERANCE_COLUMNRESIZE) {
			if (!column.resizable) return;
			resizeColumn = column;
			resizeColumnX = x;
			return;
		}
		/* if within column but not near separator line then fire column Selection */
		if (event.x < x) {
			Event newEvent = new Event ();
			newEvent.widget = column;
			postEvent (SWT.Selection, newEvent);
			return;
		}
	}
}
void headerOnMouseExit () {
	if (resizeColumn != null) return;
	setCursor (null);	/* ensure that a column resize cursor does not escape */
}
void headerOnMouseMove (Event event) {
	if (resizeColumn == null) {
		/* not currently resizing a column */
		for (int i = 0; i < columns.length; i++) {
			TreeColumn column = columns [i]; 
			int x = column.getX () + column.width;
			if (Math.abs (x - event.x) <= TOLLERANCE_COLUMNRESIZE) {
				if (column.resizable) {
					setCursor (display.getSystemCursor (SWT.CURSOR_SIZEWE));
				} else {
					setCursor (null);
				}
				return;
			}
		}
		setCursor (null);
		return;
	}
	
	/* currently resizing a column */
	
	/* don't allow the resize x to move left of the column's x position */
	if (event.x <= resizeColumn.getX ()) return;

	/* redraw the resizing line at its new location */
	GC gc = new GC (this);
	int lineHeight = getClientArea ().height;
	redraw (resizeColumnX - 1, 0, 1, lineHeight, false);
	resizeColumnX = event.x;
	gc.drawLine (resizeColumnX - 1, 0, resizeColumnX - 1, lineHeight);
	gc.dispose ();
	
}
void headerOnMouseUp (Event event) {
	if (resizeColumn == null) return;	/* not resizing a column */
	int newWidth = resizeColumnX - resizeColumn.getX ();
	if (newWidth != resizeColumn.width) {
		setCursor (null);
		updateColumnWidth (resizeColumn, newWidth);
	} else {
		/* remove the resize line */
		GC gc = new GC (this);
		int lineHeight = getClientArea ().height;
		redraw (resizeColumnX - 1, 0, 1, lineHeight, false);
		gc.dispose ();
	}
	resizeColumnX = -1;
	resizeColumn = null;
}
void headerOnPaint (Event event) {
	int numColumns = columns.length;
	GC gc = event.gc;
	Rectangle clipping = gc.getClipping ();
	int startColumn = -1, endColumn = -1;
	if (numColumns > 0) {
		startColumn = computeColumnIntersect (clipping.x, 0);
		if (startColumn != -1) {	/* the clip x is within a column's bounds */
			endColumn = computeColumnIntersect (clipping.x + clipping.width, startColumn);
			if (endColumn == -1) endColumn = numColumns - 1;
		}
	} else {
		startColumn = endColumn = 0;
	}

	/* paint the column header shadow that spans the full header width */
	Rectangle paintBounds = new Rectangle (clipping.x, 0, clipping.width, getSize ().y);
	headerPaintShadow (gc, paintBounds, true, false);

	/* if all damage is to the right of the last column then finished */
	if (startColumn == -1) return;
	
	/* paint each of the column headers */
	if (numColumns == 0) return;	/* no headers to paint */
	for (int i = startColumn; i <= endColumn; i++) {
		Rectangle bounds = new Rectangle (columns [i].getX (), 0, columns [i].width, getClientArea ().height);
		headerPaintShadow (gc, bounds, false, true);
		columns [i].paint (gc);
	}
}
void headerPaintShadow (GC gc, Rectangle bounds, boolean paintHLines, boolean paintVLines) {
	gc.setClipping (bounds.x, bounds.y, bounds.width, getHeaderHeight ());
	Color oldForeground = gc.getForeground ();
	
	/* draw highlight shadow */
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
	if (paintHLines) {
		int endX = bounds.x + bounds.width;
		gc.drawLine (bounds.x, bounds.y, endX, bounds.y);
	}
	if (paintVLines) {
		gc.drawLine (bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
	}
	
	/* draw lowlight shadow */
	Point bottomShadowStart = new Point (bounds.x + 1, bounds.height - 2);
	Point bottomShadowStop = new Point (bottomShadowStart.x + bounds.width - 2, bottomShadowStart.y);	

	/* light inner shadow */
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_NORMAL_SHADOW));
	if (paintHLines) {
		gc.drawLine (
			bottomShadowStart.x, bottomShadowStart.y,
			bottomShadowStop.x, bottomShadowStop.y);
	}
	Point rightShadowStart = new Point (bounds.x + bounds.width - 2, bounds.y + 1);
	Point rightShadowStop = new Point (rightShadowStart.x, bounds.height - 2);
	if (paintVLines) {
		gc.drawLine (
			rightShadowStart.x, rightShadowStart.y,
			rightShadowStop.x, rightShadowStop.y);
	}

	/* dark outer shadow */ 
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_DARK_SHADOW));
	--bottomShadowStart.x;
	++bottomShadowStart.y;
	++bottomShadowStop.y;
	
	if (paintHLines) {
		gc.drawLine (
			bottomShadowStart.x, bottomShadowStart.y,
			bottomShadowStop.x, bottomShadowStop.y);
	}
	if (paintVLines) {
		gc.drawLine (
			rightShadowStart.x + 1, rightShadowStart.y - 1,
			rightShadowStop.x + 1, rightShadowStop.y + 1);
	}
	
	gc.setForeground (oldForeground);
}
int indexOf (TreeColumn column) {
	checkWidget ();
	return column.getIndex ();
}
static void initImages (final Display display) {
	if (display.getData (ID_CHECKMARK) != null) return;
	
	PaletteData fourBit = new PaletteData (new RGB[] {
		new RGB (0, 0, 0), new RGB (128, 0, 0), new RGB (0, 128, 0), new RGB (128, 128, 0),
		new RGB (0, 0, 128), new RGB (128, 0, 128), new RGB (0, 128, 128), new RGB (128, 128, 128),
		new RGB (192, 192, 192), new RGB (255, 0, 0), new RGB (0, 255, 0), new RGB (255, 255, 0),
		new RGB (0, 0, 255), new RGB (255, 0, 255), new RGB (0, 255, 255), new RGB (255, 255, 255)});	
	ImageData collapsed = new ImageData (
		9, 9, 4, 										/* width, height, depth */
		fourBit, 4,
		new byte[] {
			119, 119, 119, 119, 112, 0, 0, 0, 127, -1, -1, -1,
			112, 0, 0, 0, 127, -1, 15, -1, 112, 0, 0, 0,
			127, -1, 15, -1, 112, 0, 0, 0, 127, 0, 0, 15,
			112, 0, 0, 0, 127, -1, 15, -1, 112, 0, 0, 0,
			127, -1, 15, -1, 112, 0, 0, 0, 127, -1, -1, -1,
			112, 0, 0, 0, 119, 119, 119, 119, 112, 0, 0, 0});
	collapsed.transparentPixel = 15;			/* white for transparency */
	ImageData expanded = new ImageData (
		9, 9, 4, 										/* width, height, depth */
		fourBit, 4,
		new byte[] {
			119, 119, 119, 119, 112, 0, 0, 0, 127, -1, -1, -1,
			112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0,
			127, -1, -1, -1, 112, 0, 0, 0, 127, 0, 0, 15,
			112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0,
			127, -1, -1, -1, 112, 0, 0, 0, 127, -1, -1, -1,
			112, 0, 0, 0, 119, 119, 119, 119, 112, 0, 0, 0});
	expanded.transparentPixel = 15;			/* use white for transparency */
		
	PaletteData uncheckedPalette = new PaletteData (	
		new RGB[] {new RGB (128, 128, 128), new RGB (255, 255, 255)});
	PaletteData grayUncheckedPalette = new PaletteData (	
		new RGB[] {new RGB (128, 128, 128), new RGB (192, 192, 192)});
	PaletteData checkMarkPalette = new PaletteData (	
		new RGB[] {new RGB (0, 0, 0), new RGB (252, 3, 251)});
	byte[] checkbox = new byte[] {0, 0, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 0, 0};
	ImageData unchecked = new ImageData (11, 11, 1, uncheckedPalette, 2, checkbox);
	ImageData grayUnchecked = new ImageData (11, 11, 1, grayUncheckedPalette, 2, checkbox);
	ImageData checkmark = new ImageData (7, 7, 1, checkMarkPalette, 1, new byte[] {-4, -8, 112, 34, 6, -114, -34});
	checkmark.transparentPixel = 1;

	display.setData (ID_EXPANDED, new Image (display, expanded));
	display.setData (ID_COLLAPSED, new Image (display, collapsed));
	display.setData (ID_UNCHECKED, new Image (display, unchecked));
	display.setData (ID_GRAYUNCHECKED, new Image (display, grayUnchecked));
	display.setData (ID_CHECKMARK, new Image (display, checkmark));
	display.setData (ID_CONNECTOR_COLOR, new Color (display, 170, 170, 170));
	
	display.disposeExec (new Runnable () {
		public void run() {
			Image expanded = (Image) display.getData (ID_EXPANDED);
			if (expanded != null) expanded.dispose ();
			Image collapsed = (Image) display.getData (ID_COLLAPSED);
			if (collapsed != null) collapsed.dispose ();
			Image unchecked = (Image) display.getData (ID_UNCHECKED);
			if (unchecked != null) unchecked.dispose ();
			Image grayUnchecked = (Image) display.getData (ID_GRAYUNCHECKED);
			if (grayUnchecked != null) grayUnchecked.dispose ();
			Image checkmark = (Image) display.getData (ID_CHECKMARK);
			if (checkmark != null) checkmark.dispose ();
			Color connectorColor = (Color) display.getData (ID_CONNECTOR_COLOR);
			if (connectorColor != null) connectorColor.dispose ();

			display.setData (ID_EXPANDED, null);
			display.setData (ID_COLLAPSED, null);
			display.setData (ID_UNCHECKED, null);
			display.setData (ID_GRAYUNCHECKED, null);
			display.setData (ID_CHECKMARK, null);
			display.setData (ID_CONNECTOR_COLOR, null);
		}
	});
}
/*
 * Important: Assumes that item just became available (ie.- was either created
 * or the parent item was expanded) and the parent is available.
 */
void makeAvailable (TreeItem item) {
	int parentItemCount = item.parentItem.items.length; 
	int index = 0;
	if (parentItemCount == 1) {		/* this is the only child of parentItem */
		index = item.parentItem.availableIndex + 1;
	} else {
		/* determine this item's index in its parent */
		int itemIndex = 0;
		TreeItem[] items = item.parentItem.items;
		for (int i = 0; i < items.length; i++) {
			if (items [i] == item) {
				itemIndex = i;
				break;
			}
		}
		if (itemIndex != parentItemCount - 1) {	/* this is not the last child */
			index = items [itemIndex + 1].availableIndex;
		} else {	/* this is the last child */
			TreeItem previousItem = items [itemIndex - 1];
			index = previousItem.availableIndex + previousItem.computeAvailableDescendentCount ();
		}
	}
	
	TreeItem[] newAvailableItems = new TreeItem [availableItems.length + 1];
	System.arraycopy (availableItems, 0, newAvailableItems, 0, index);
	newAvailableItems [index] = item;
	System.arraycopy (availableItems, index, newAvailableItems, index + 1, availableItems.length - index);
	availableItems = newAvailableItems;
	
	/* update availableIndex as needed */
	for (int i = index; i < availableItems.length; i++) {
		availableItems [i].availableIndex = i;
	}
}

/*
 * Important: Assumes that item is available and its descendents have just become
 * available (ie.- they were either created or the item was expanded).
 */
void makeDescendentsAvailable (TreeItem item, TreeItem[] descendents) {
	int itemAvailableIndex = item.availableIndex;
	TreeItem[] newAvailableItems = new TreeItem [availableItems.length + descendents.length - 1];
	
	System.arraycopy (availableItems, 0, newAvailableItems, 0, itemAvailableIndex);
	System.arraycopy (descendents, 0, newAvailableItems, itemAvailableIndex, descendents.length);
	int startIndex = itemAvailableIndex + 1;
	System.arraycopy (
		availableItems,
		startIndex,
		newAvailableItems,
		itemAvailableIndex + descendents.length,
		availableItems.length - startIndex);
	availableItems = newAvailableItems;
	
	/* update availableIndex as needed */
	for (int i = itemAvailableIndex; i < availableItems.length; i++) {
		availableItems [i].availableIndex = i;
	}
}

/*
 * Important: Assumes that item is available and its descendents have just become
 * unavailable (ie.- they were either disposed or the item was collapsed).
 */
void makeDescendentsUnavailable (TreeItem item, TreeItem[] descendents) {
	int descendentsLength = descendents.length;
	TreeItem[] newAvailableItems = new TreeItem [availableItems.length - descendentsLength + 1];
	
	System.arraycopy (availableItems, 0, newAvailableItems, 0, item.availableIndex + 1);
	int startIndex = item.availableIndex + descendentsLength;
	System.arraycopy (
		availableItems,
		startIndex,
		newAvailableItems,
		item.availableIndex + 1,
		availableItems.length - startIndex);
	availableItems = newAvailableItems;
	
	/* update availableIndexes */
	for (int i = 1; i < descendents.length; i++) {
		/* skip the first descendent since this is the item being collapsed */
		descendents [i].availableIndex = -1;
	}
	for (int i = item.availableIndex; i < availableItems.length; i++) {
		availableItems [i].availableIndex = i;
	}
	
	/* remove the selection from all descendents */
	for (int i = selectedItems.length - 1; i >= 0; i--) {
		if (selectedItems [i] != item && selectedItems [i].hasAncestor (item)) {
			removeSelectedItem (i);
		}
	}
	
	/* if the anchorItem is being hidden then clear it */
	if (anchorItem != null && anchorItem != item && anchorItem.hasAncestor (item)) {
		anchorItem = null;
	}
}
void onArrowDown (int stateMask) {
	if ((stateMask & (SWT.SHIFT | SWT.CTRL)) == 0) {
		/* Down Arrow with no modifiers */
		int newFocusIndex = focusItem.availableIndex + 1;
		if (newFocusIndex == availableItems.length) return; 	/* at bottom */
		selectItem (availableItems [newFocusIndex], false);
		setFocusItem (availableItems [newFocusIndex], true);
		redrawItem (newFocusIndex, true);
		showItem (availableItems [newFocusIndex]);
		Event newEvent = new Event ();
		newEvent.item = this;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+Down Arrow, CTRL+Shift+Down Arrow */
			int visibleItemCount = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
			if (availableItems.length <= topIndex + visibleItemCount) return;	/* at bottom */
			update ();
			topIndex++;
			getVerticalBar ().setSelection (topIndex);
			Rectangle clientArea = getClientArea ();
			GC gc = new GC (this);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				0, -itemHeight);
			gc.dispose ();
			return;
		}
		/* Shift+Down Arrow */
		int newFocusIndex = focusItem.availableIndex + 1;
		if (newFocusIndex == availableItems.length) return; 	/* at bottom */
		selectItem (availableItems [newFocusIndex], false);
		setFocusItem (availableItems [newFocusIndex], true);
		redrawItem (newFocusIndex, true);
		showItem (availableItems [newFocusIndex]);
		Event newEvent = new Event ();
		newEvent.item = this;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+Down Arrow */
			int visibleItemCount = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
			if (availableItems.length <= topIndex + visibleItemCount) return;	/* at bottom */
			update ();
			topIndex++;
			getVerticalBar ().setSelection (topIndex);
			Rectangle clientArea = getClientArea ();
			GC gc = new GC (this);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				0, -itemHeight);
			gc.dispose ();
			return;
		}
		/* CTRL+Down Arrow */
		int focusIndex = focusItem.availableIndex; 
		if (focusIndex == availableItems.length - 1) return;	/* at bottom */
		TreeItem newFocusItem = availableItems [focusIndex + 1];
		setFocusItem (newFocusItem, true);
		redrawItem (newFocusItem.availableIndex, true);
		showItem (newFocusItem);
		return;
	}
	/* Shift+Down Arrow */
	int newFocusIndex = focusItem.availableIndex + 1;
	if (newFocusIndex == availableItems.length) return; 	/* at bottom */
	if (anchorItem == null) anchorItem = focusItem;
	selectItem (availableItems [newFocusIndex], true);
	setFocusItem (availableItems [newFocusIndex], true);
	redrawItem (newFocusIndex, true);
	showItem (availableItems [newFocusIndex]);
	Event newEvent = new Event ();
	newEvent.item = this;
	postEvent (SWT.Selection, newEvent);
}
void onArrowLeft (int stateMask) {
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+Left Arrow, CTRL+Shift+Left Arrow */
		if (horizontalOffset == 0) return;
		int newSelection = Math.max (0, horizontalOffset - SIZE_HORIZONTALSCROLL);
		Rectangle clientArea = getClientArea ();
		update ();
		GC gc = new GC (this);
		gc.copyArea (
			0, 0,
			clientArea.width, clientArea.height,
			horizontalOffset - newSelection, 0);
		gc.dispose ();
		if (getHeaderVisible ()) {
			header.update ();
			clientArea = header.getClientArea ();
			gc = new GC (header);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				horizontalOffset - newSelection, 0);
			gc.dispose();
		}
		horizontalOffset = newSelection;
		getHorizontalBar ().setSelection (horizontalOffset);
		return;
	}
	/* Left Arrow with no modifiers, Shift+Left Arrow */
	if (focusItem.expanded) {
		focusItem.setExpanded (false);
		Event newEvent = new Event ();
		newEvent.item = focusItem;
		sendEvent (SWT.Collapse, newEvent);
		return;
	}
	TreeItem parentItem = focusItem.parentItem;
	if (parentItem == null) return;
	
	selectItem (parentItem, false);
	setFocusItem (parentItem, true);
	redrawItem (parentItem.availableIndex, true);
	showItem (parentItem);
	Event newEvent = new Event ();
	newEvent.item = this;
	postEvent (SWT.Selection, newEvent);
}
void onArrowRight (int stateMask) {
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+Right Arrow, CTRL+Shift+Right Arrow */
		ScrollBar hBar = getHorizontalBar ();
		int maximum = hBar.getMaximum ();
		int clientWidth = getClientArea ().width;
		if ((horizontalOffset + getClientArea ().width) == maximum) return;
		int newSelection = Math.min (maximum - clientWidth, horizontalOffset + SIZE_HORIZONTALSCROLL);
		Rectangle clientArea = getClientArea ();
		update ();
		GC gc = new GC (this);
		gc.copyArea (
			0, 0,
			clientArea.width, clientArea.height,
			horizontalOffset - newSelection, 0);
		gc.dispose ();
		if (getHeaderVisible ()) {
			clientArea = header.getClientArea ();
			header.update ();
			gc = new GC (header);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				horizontalOffset - newSelection, 0);
			gc.dispose();
		}
		horizontalOffset = newSelection;
		hBar.setSelection (horizontalOffset);
		return;
	}
	/* Right Arrow with no modifiers, Shift+Right Arrow */
	TreeItem[] children = focusItem.items;
	if (children.length == 0) return;
	if (!focusItem.expanded) {
		focusItem.setExpanded (true);
		Event newEvent = new Event ();
		newEvent.item = focusItem;
		inExpand = true;
		sendEvent (SWT.Expand, newEvent);
		inExpand = false;
		if (isDisposed ()) return;
		if (focusItem.items.length == 0) {
			focusItem.expanded = false;
		}
		return;
	}
	selectItem (children [0], false);
	setFocusItem (children [0], true);
	redrawItem (children [0].availableIndex, true);
	showItem (children [0]);
	Event newEvent = new Event ();
	newEvent.item = children [0];
	postEvent (SWT.Selection, newEvent);
}
void onArrowUp (int stateMask) {
	if ((stateMask & (SWT.SHIFT | SWT.CTRL)) == 0) {
		/* Up Arrow with no modifiers */
		int newFocusIndex = focusItem.availableIndex - 1;
		if (newFocusIndex < 0) return; 		/* at top */
		TreeItem item = availableItems [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (newFocusIndex, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+Up Arrow, CTRL+Shift+Up Arrow */
			if (topIndex == 0) return;	/* at top */
			update ();
			topIndex--;
			getVerticalBar ().setSelection (topIndex);
			Rectangle clientArea = getClientArea ();
			GC gc = new GC (this);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				0, itemHeight);
			gc.dispose ();
			return;
		}
		/* Shift+Up Arrow */
		int newFocusIndex = focusItem.availableIndex - 1;
		if (newFocusIndex < 0) return; 	/* at top */
		TreeItem item = availableItems [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (newFocusIndex, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+Up Arrow */
			if (topIndex == 0) return;	/* at top */
			update ();
			topIndex--;
			getVerticalBar ().setSelection (topIndex);
			Rectangle clientArea = getClientArea ();
			GC gc = new GC (this);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				0, itemHeight);
			gc.dispose ();
			return;
		}
		/* CTRL+Up Arrow */
		int focusIndex = focusItem.availableIndex; 
		if (focusIndex == 0) return;	/* at top */
		TreeItem newFocusItem = availableItems [focusIndex - 1];
		setFocusItem (newFocusItem, true);
		showItem (newFocusItem);
		redrawItem (newFocusItem.availableIndex, true);
		return;
	}
	/* Shift+Up Arrow */
	int newFocusIndex = focusItem.availableIndex - 1;
	if (newFocusIndex < 0) return; 		/* at top */
	if (anchorItem == null) anchorItem = focusItem;
	TreeItem item = availableItems [newFocusIndex];
	selectItem (item, true);
	setFocusItem (item, true);
	redrawItem (newFocusIndex, true);
	showItem (item);
	Event newEvent = new Event ();
	newEvent.item = item;
	postEvent (SWT.Selection, newEvent);
}
void onCR () {
	if (focusItem == null) return;
	Event event = new Event ();
	event.item = focusItem;
	postEvent (SWT.DefaultSelection, event);
}
void onDispose () {
	if (isDisposed ()) return;
	for (int i = 0; i < items.length; i++) {
		items [i].dispose (false);
	}
	for (int i = 0; i < columns.length; i++) {
		columns [i].dispose (false);
	}
	topIndex = 0;
	availableItems = items = selectedItems = null;
	columns = null;
	focusItem = anchorItem = insertMarkItem = lastClickedItem = null;
	header = null;
	resizeColumn = null;
	expanderBounds = null;
}
void onEnd (int stateMask) {
	int lastAvailableIndex = availableItems.length - 1;
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* End with no modifiers */
		if (focusItem.availableIndex == lastAvailableIndex) return; 	/* at bottom */
		TreeItem item = availableItems [lastAvailableIndex]; 
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (lastAvailableIndex, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+End, CTRL+Shift+End */
			int visibleItemCount = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
			setTopItem (availableItems [availableItems.length - visibleItemCount]);
			return;
		}
		/* Shift+End */
		if (focusItem.availableIndex == lastAvailableIndex) return; /* at bottom */
		TreeItem item = availableItems [lastAvailableIndex]; 
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (lastAvailableIndex, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+End */
			showItem (availableItems [lastAvailableIndex]);
			return;
		}
		/* CTRL+End */
		if (focusItem.availableIndex == lastAvailableIndex) return; /* at bottom */
		TreeItem item = availableItems [lastAvailableIndex];
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.availableIndex, true);
		return;
	}
	/* Shift+End */
	if (anchorItem == null) anchorItem = focusItem;
	TreeItem selectedItem = availableItems [lastAvailableIndex];
	if (selectedItem == focusItem && selectedItem.isSelected ()) return;
	int anchorIndex = anchorItem.availableIndex;
	int selectIndex = selectedItem.availableIndex;
	TreeItem[] newSelection = new TreeItem [selectIndex - anchorIndex + 1];
	int writeIndex = 0;
	for (int i = anchorIndex; i <= selectIndex; i++) {
		newSelection [writeIndex++] = availableItems [i];
	}
	setSelection (newSelection);
	setFocusItem (selectedItem, true);
	redrawItems (anchorIndex, selectIndex, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onFocusIn () {
	if (items.length == 0) return;
	if (focusItem != null) {
		redrawItem (focusItem.availableIndex, true);
		return;
	}
	/* an initial focus item must be selected */
	TreeItem initialFocus;
	if (selectedItems.length > 0) {
		initialFocus = selectedItems [0];
	} else {
		initialFocus = availableItems [topIndex];
		selectItem (initialFocus, false);
	}
	setFocusItem (initialFocus, false);
	redrawItem (initialFocus.availableIndex, true);
	Event newEvent = new Event ();
	newEvent.item = initialFocus;
	postEvent (SWT.Selection, newEvent);
	return;
}
void onFocusOut () {
	if (focusItem != null) {
		redrawItem (focusItem.availableIndex, true);
	}
}
void onHome (int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* Home with no modifiers */
		if (focusItem.availableIndex == 0) return; 		/* at top */
		TreeItem item = availableItems [0];
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (0, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+Home, CTRL+Shift+Home */
			setTopItem (availableItems [0]);
			return;
		}
		/* Shift+Home */
		if (focusItem.availableIndex == 0) return; 		/* at top */
		TreeItem item = availableItems [0];
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (0, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+Home */
			setTopItem (availableItems [0]);
			return;
		}
		/* CTRL+Home */
		if (focusItem.availableIndex == 0) return; /* at top */
		TreeItem item = availableItems [0];
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.availableIndex, true);
		return;
	}
	/* Shift+Home */
	if (anchorItem == null) anchorItem = focusItem;
	TreeItem selectedItem = availableItems [0];
	if (selectedItem == focusItem && selectedItem.isSelected ()) return;
	int anchorIndex = anchorItem.availableIndex;
	int selectIndex = selectedItem.availableIndex;
	TreeItem[] newSelection = new TreeItem [anchorIndex + 1];
	int writeIndex = 0;
	for (int i = anchorIndex; i >= 0; i--) {
		newSelection [writeIndex++] = availableItems [i];
	}
	setSelection (newSelection);
	setFocusItem (selectedItem, true);
	redrawItems (anchorIndex, selectIndex, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onKeyDown (Event event) {
	if (focusItem == null) return;
	if ((event.stateMask & SWT.SHIFT) == 0 && event.keyCode != SWT.SHIFT) {
		anchorItem = null;
	}
	switch (event.keyCode) {
		case SWT.ARROW_UP:
			onArrowUp (event.stateMask);
			return;
		case SWT.ARROW_DOWN:
			onArrowDown (event.stateMask);
			return;
		case SWT.ARROW_LEFT:
			onArrowLeft (event.stateMask);
			return;
		case SWT.ARROW_RIGHT:
			onArrowRight (event.stateMask);
			return;
		case SWT.PAGE_UP:
			onPageUp (event.stateMask);
			return;
		case SWT.PAGE_DOWN:
			onPageDown (event.stateMask);
			return;
		case SWT.HOME:
			onHome (event.stateMask);
			return;
		case SWT.END:
			onEnd (event.stateMask);
			return;
	}
	if (event.character == ' ') {
		onSpace ();
		return;
	}
	if (event.character == SWT.CR) {
		onCR ();
		return;
	}
	if ((event.stateMask & SWT.CTRL) != 0) return;
	
	int initialIndex = focusItem.availableIndex;
	char character = Character.toLowerCase (event.character);
	/* check available items from current focus item to bottom */
	for (int i = initialIndex + 1; i < availableItems.length; i++) {
		TreeItem item = availableItems [i];
		String text = item.getText ();
		if (text.length() > 0) {
			if (Character.toLowerCase (text.charAt (0)) == character) {
				selectItem (item, false);
				setFocusItem (item, true);
				redrawItem (i, true);
				showItem (item);
				Event newEvent = new Event ();
				newEvent.item = this;
				postEvent (SWT.Selection, newEvent);
				return;
			}
		}
	}
	/* check available items from top to current focus item */
	for (int i = 0; i < initialIndex; i++) {
		TreeItem item = availableItems [i];
		String text = item.getText ();
		if (text.length() > 0) {
			if (Character.toLowerCase (text.charAt (0)) == character) {
				selectItem (item, false);
				setFocusItem (item, true);
				redrawItem (i, true);
				showItem (item);
				Event newEvent = new Event ();
				newEvent.item = this;
				postEvent (SWT.Selection, newEvent);
				return;
			}
		}
	}
}
void onMouseDoubleClick (Event event) {
	if (!isFocusControl ()) setFocus ();
	int index = (event.y - getHeaderHeight ()) / itemHeight + topIndex;
	if  (!(0 <= index && index < availableItems.length)) return;	/* not on an available item */
	TreeItem selectedItem = availableItems [index];
	
	/* 
	 * If the two clicks of the double click did not occur over the same item then do not
	 * consider this to be a default selection.
	 */
	if (selectedItem != lastClickedItem) return;

	/* if click was in expander box then don't fire event */
	if (selectedItem.items.length > 0 && selectedItem.getExpanderBounds ().contains (event.x, event.y)) {
		return;
	}
	
	if (!selectedItem.getHitBounds ().contains (event.x, event.y)) return;	/* considers x */
	
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.DefaultSelection, newEvent);
}
void onMouseDown (Event event) {
	if (!isFocusControl ()) setFocus ();
	int index = (event.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (!(0 <= index && index < availableItems.length)) return;	/* not on an available item */
	TreeItem selectedItem = availableItems [index];
	
	/* if click was in expander box */
	if (selectedItem.items.length > 0 && selectedItem.getExpanderBounds ().contains (event.x, event.y)) {
		if (event.button != 1) return;
		boolean expand = !selectedItem.expanded;
		selectedItem.setExpanded (expand);
		Event newEvent = new Event ();
		newEvent.item = selectedItem;
		if (expand) {
			inExpand = true;
			sendEvent (SWT.Expand, newEvent);
			inExpand = false;
			if (isDisposed ()) return;
			if (selectedItem.items.length == 0) {
				selectedItem.expanded = false;
			}
		} else {
			sendEvent (SWT.Collapse, newEvent);
		}
		return;
	}
	/* if click was in checkbox */
	if ((style & SWT.CHECK) != 0 && selectedItem.getCheckboxBounds ().contains (event.x, event.y)) {
		if (event.button != 1) return;
		selectedItem.setChecked (!selectedItem.checked);
		Event newEvent = new Event ();
		newEvent.item = selectedItem;
		newEvent.detail = SWT.CHECK;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	
	if (!selectedItem.getHitBounds ().contains (event.x, event.y)) return;
	
	if ((event.stateMask & SWT.SHIFT) == 0 && event.keyCode != SWT.SHIFT) anchorItem = null;

	if ((style & SWT.SINGLE) != 0) {
		if (!selectedItem.isSelected ()) {
			if (event.button == 1) {
				selectItem (selectedItem, false);
				setFocusItem (selectedItem, true);
				redrawItem (selectedItem.availableIndex, true);
				Event newEvent = new Event ();
				newEvent.item = selectedItem;
				postEvent (SWT.Selection, newEvent);
				return;
			}
			if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
				selectItem (selectedItem, false);
				setFocusItem (selectedItem, true);
				redrawItem (selectedItem.availableIndex, true);
				Event newEvent = new Event ();
				newEvent.item = selectedItem;
				postEvent (SWT.Selection, newEvent);
				return;
			}
		}
		/* item is selected */
		if (event.button == 1) {
			/* fire a selection event, though the selection did not change */
			Event newEvent = new Event ();
			newEvent.item = selectedItem;
			postEvent (SWT.Selection, newEvent);
			return;
		}
	}
	/* SWT.MULTI */
	if (!selectedItem.isSelected ()) {
		if (event.button == 1) {
			if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == SWT.SHIFT) {
				if (anchorItem == null) anchorItem = focusItem;
				int anchorIndex = anchorItem.availableIndex;
				int selectIndex = selectedItem.availableIndex;
				TreeItem[] newSelection = new TreeItem [Math.abs (anchorIndex - selectIndex) + 1];
				int step = anchorIndex < selectIndex ? 1 : -1;
				int writeIndex = 0;
				for (int i = anchorIndex; i != selectIndex; i += step) {
					newSelection [writeIndex++] = availableItems [i];
				}
				newSelection [writeIndex] = availableItems [selectIndex];
				setSelection (newSelection);
				setFocusItem (selectedItem, true);
				redrawItems (
					Math.min (anchorIndex, selectIndex),
					Math.max (anchorIndex, selectIndex),
					true);
				Event newEvent = new Event ();
				newEvent.item = selectedItem;
				postEvent (SWT.Selection, newEvent);
				return;
			}
			selectItem (selectedItem, (event.stateMask & SWT.CTRL) != 0);
			setFocusItem (selectedItem, true);
			redrawItem (selectedItem.availableIndex, true);
			Event newEvent = new Event ();
			newEvent.item = selectedItem;
			postEvent (SWT.Selection, newEvent);
			return;
		}
		/* button 3 */
		if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
			selectItem (selectedItem, false);
			setFocusItem (selectedItem, true);
			redrawItem (selectedItem.availableIndex, true);
			Event newEvent = new Event ();
			newEvent.item = selectedItem;
			postEvent (SWT.Selection, newEvent);
			return;
		}
	}
	/* item is selected */
	if (event.button != 1) return;
	if ((event.stateMask & SWT.CTRL) != 0) {
		removeSelectedItem (getSelectionIndex (selectedItem));
		setFocusItem (selectedItem, true);
		redrawItem (selectedItem.availableIndex, true);
		Event newEvent = new Event ();
		newEvent.item = selectedItem;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((event.stateMask & SWT.SHIFT) != 0) {
		if (anchorItem == null) anchorItem = focusItem;
		int anchorIndex = anchorItem.availableIndex;
		int selectIndex = selectedItem.availableIndex;
		TreeItem[] newSelection = new TreeItem [Math.abs (anchorIndex - selectIndex) + 1];
		int step = anchorIndex < selectIndex ? 1 : -1;
		int writeIndex = 0;
		for (int i = anchorIndex; i != selectIndex; i += step) {
			newSelection [writeIndex++] = availableItems [i];
		}
		newSelection [writeIndex] = availableItems [selectIndex];
		setSelection (newSelection);
		setFocusItem (selectedItem, true);
		redrawItems (
			Math.min (anchorIndex, selectIndex),
			Math.max (anchorIndex, selectIndex),
			true);
		Event newEvent = new Event ();
		newEvent.item = selectedItem;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	selectItem (selectedItem, false);
	setFocusItem (selectedItem, true);
	redrawItem (selectedItem.availableIndex, true);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onMouseUp (Event event) {
	int index = (event.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (!(0 <= index && index < availableItems.length)) return;	/* not on an available item */
	lastClickedItem = availableItems [index];
}
void onPageDown (int stateMask) {
	int visibleItemCount = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* PageDown with no modifiers */
		int newFocusIndex = focusItem.availableIndex + visibleItemCount - 1;
		newFocusIndex = Math.min (newFocusIndex, availableItems.length - 1);
		if (newFocusIndex == focusItem.availableIndex) return;
		TreeItem item = availableItems [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.availableIndex, true);
		return;
	}
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == (SWT.CTRL | SWT.SHIFT)) {
		/* CTRL+Shift+PageDown */
		int newTopIndex = topIndex + visibleItemCount;
		newTopIndex = Math.min (newTopIndex, availableItems.length - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopItem (availableItems [newTopIndex]);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* Shift+PageDown */
			int newFocusIndex = focusItem.availableIndex + visibleItemCount - 1;
			newFocusIndex = Math.min (newFocusIndex, availableItems.length - 1);
			if (newFocusIndex == focusItem.availableIndex) return;
			TreeItem item = availableItems [newFocusIndex];
			selectItem (item, false);
			setFocusItem (item, true);
			showItem (item);
			redrawItem (item.availableIndex, true);
			return;
		}
		/* CTRL+PageDown */
		int newTopIndex = topIndex + visibleItemCount;
		newTopIndex = Math.min (newTopIndex, availableItems.length - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopItem (availableItems [newTopIndex]);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+PageDown */
		int bottomIndex = Math.min (topIndex + visibleItemCount - 1, availableItems.length - 1);
		if (focusItem.availableIndex != bottomIndex) {
			/* move focus to bottom item in viewport */
			setFocusItem (availableItems [bottomIndex], true);
			redrawItem (bottomIndex, true);
		} else {
			/* at bottom of viewport, so set focus to bottom item one page down */
			int newFocusIndex = Math.min (availableItems.length - 1, bottomIndex + visibleItemCount);
			if (newFocusIndex == focusItem.availableIndex) return;
			setFocusItem (availableItems [newFocusIndex], true);
			showItem (availableItems [newFocusIndex]);
			redrawItem (newFocusIndex, true);
		}
		return;
	}
	/* Shift+PageDown */
	if (anchorItem == null) anchorItem = focusItem;
	int anchorIndex = anchorItem.availableIndex;
	int bottomIndex = Math.min (topIndex + visibleItemCount - 1, availableItems.length - 1);
	int selectIndex;
	if (focusItem.availableIndex != bottomIndex) {
		/* select from focus to bottom item in viewport */
		selectIndex = bottomIndex;
	} else {
		/* already at bottom of viewport, so select to bottom of one page down */
		selectIndex = Math.min (availableItems.length - 1, bottomIndex + visibleItemCount);
		if (selectIndex == focusItem.availableIndex && focusItem.isSelected ()) return;
	}
	TreeItem selectedItem = availableItems [selectIndex];
	TreeItem[] newSelection = new TreeItem [Math.abs (anchorIndex - selectIndex) + 1];
	int step = anchorIndex < selectIndex ? 1 : -1;
	int writeIndex = 0;
	for (int i = anchorIndex; i != selectIndex; i += step) {
		newSelection [writeIndex++] = availableItems [i];
	}
	newSelection [writeIndex] = availableItems [selectIndex];
	setSelection (newSelection);
	setFocusItem (selectedItem, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onPageUp (int stateMask) {
	int visibleItemCount = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* PageUp with no modifiers */
		int newFocusIndex = Math.max (0, focusItem.availableIndex - visibleItemCount + 1);
		if (newFocusIndex == focusItem.availableIndex) return;
		TreeItem item = availableItems [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.availableIndex, true);
		return;
	}
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == (SWT.CTRL | SWT.SHIFT)) {
		/* CTRL+Shift+PageUp */
		int newTopIndex = Math.max (0, topIndex - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopItem (availableItems [newTopIndex]);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* Shift+PageUp */
			int newFocusIndex = Math.max (0, focusItem.availableIndex - visibleItemCount + 1);
			if (newFocusIndex == focusItem.availableIndex) return;
			TreeItem item = availableItems [newFocusIndex];
			selectItem (item, false);
			setFocusItem (item, true);
			showItem (item);
			redrawItem (item.availableIndex, true);
			return;
		}
		/* CTRL+PageUp */
		int newTopIndex = Math.max (0, topIndex - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopItem (availableItems [newTopIndex]);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+PageUp */
		if (focusItem.availableIndex != topIndex) {
			/* move focus to top item in viewport */
			setFocusItem (availableItems [topIndex], true);
			redrawItem (topIndex, true);
		} else {
			/* at top of viewport, so set focus to top item one page up */
			int newFocusIndex = Math.max (0, focusItem.availableIndex - visibleItemCount);
			if (newFocusIndex == focusItem.availableIndex) return;
			setFocusItem (availableItems [newFocusIndex], true);
			showItem (availableItems [newFocusIndex]);
			redrawItem (newFocusIndex, true);
		}
		return;
	}
	/* Shift+PageUp */
	if (anchorItem == null) anchorItem = focusItem;
	int anchorIndex = anchorItem.availableIndex;
	int selectIndex;
	if (focusItem.availableIndex != topIndex) {
		/* select from focus to top item in viewport */
		selectIndex = topIndex;
	} else {
		/* already at top of viewport, so select to top of one page up */
		selectIndex = Math.max (0, topIndex - visibleItemCount);
		if (selectIndex == focusItem.availableIndex && focusItem.isSelected ()) return;
	}
	TreeItem selectedItem = availableItems [selectIndex];
	TreeItem[] newSelection = new TreeItem [Math.abs (anchorIndex - selectIndex) + 1];
	int step = anchorIndex < selectIndex ? 1 : -1;
	int writeIndex = 0;
	for (int i = anchorIndex; i != selectIndex; i += step) {
		newSelection [writeIndex++] = availableItems [i];
	}
	newSelection [writeIndex] = availableItems [selectIndex];
	setSelection (newSelection);
	setFocusItem (selectedItem, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onPaint (Event event) {
	GC gc = event.gc;
	Rectangle clipping = gc.getClipping ();
	int numColumns = columns.length;
	int startColumn = -1, endColumn = -1;
	if (numColumns > 0) {
		startColumn = computeColumnIntersect (clipping.x, 0);
		if (startColumn != -1) {	/* the clip x is within a column's bounds */
			endColumn = computeColumnIntersect (clipping.x + clipping.width, startColumn);
			if (endColumn == -1) endColumn = numColumns - 1;
		}
	} else {
		startColumn = endColumn = 0;
	}

	/* repaint grid lines */
	if (linesVisible) {
		Color oldForeground = gc.getForeground ();
		if (numColumns > 0 && startColumn != -1) {
			gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
			/* vertical column lines */
			for (int i = startColumn; i <= endColumn; i++) {
				int x = columns [i].getX () + columns [i].width - 1;
				gc.drawLine (x, clipping.y, x, clipping.y + clipping.height);
			}
		}
		/* horizontal item lines */
		int bottomY = clipping.y + clipping.height;
		int rightX = clipping.x + clipping.width;
		int headerHeight = getHeaderHeight ();
		int y = (clipping.y - headerHeight) / itemHeight * itemHeight + headerHeight;
		while (y <= bottomY) {
			gc.drawLine (clipping.x, y, rightX, y);
			y += itemHeight;
		}
		gc.setForeground (oldForeground);
	}
	
	/* Determine the TreeItems to be painted */
	int startIndex = (clipping.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (availableItems.length < startIndex) return;		/* no items to paint */
	int endIndex = startIndex + Compatibility.ceil (clipping.height, itemHeight);
	if (endIndex < 0) return;		/* no items to paint */
	startIndex = Math.max (0, startIndex);
	endIndex = Math.min (endIndex, availableItems.length - 1);
	int current = 0;
	for (int i = startIndex; i <= endIndex; i++) {
		TreeItem item = availableItems [i];
		if (startColumn == -1) {
			/* indicates that region to paint is to the right of the last column */
			item.paint (gc, null, false);
		} else {
			if (numColumns == 0) {
				item.paint (gc, null, true);
			} else {
				for (int j = startColumn; j <= endColumn; j++) {
					item.paint (gc, columns [j], true);
				}
			}
		}
		if (isFocusControl ()) {
			if (focusItem == item) {
				Rectangle focusBounds = item.getFocusBounds ();
				gc.setClipping (focusBounds);
				int[] oldLineDash = gc.getLineDash ();
				if (item.isSelected ()) {
					gc.setLineDash (new int[] {2, 2});
				} else {
					gc.setLineDash (new int[] {1, 1});
				}
				gc.drawFocus (focusBounds.x, focusBounds.y, focusBounds.width, focusBounds.height);
				gc.setLineDash (oldLineDash);
			}
			if (insertMarkItem == item) {
				Rectangle focusBounds = item.getFocusBounds ();
				gc.setClipping (focusBounds);
				if (insertMarkPrecedes) {
					gc.drawLine (focusBounds.x, focusBounds.y, focusBounds.x + focusBounds.width, focusBounds.y);
				} else {
					int y = focusBounds.y + focusBounds.height - 1;
					gc.drawLine (focusBounds.x, y, focusBounds.x + focusBounds.width, y);
				}
			}
		}
	}
}
void onResize (Event event) {
	Rectangle clientArea = getClientArea ();
	/* vertical scrollbar */
	ScrollBar vBar = getVerticalBar ();
	int clientHeight = (clientArea.height - getHeaderHeight ()) / itemHeight;
	int thumb = Math.min (clientHeight, availableItems.length);
	vBar.setThumb (thumb);
	vBar.setPageIncrement (thumb);
	int index = vBar.getSelection ();
	if (index != topIndex) {
		topIndex = index;
		redraw ();
	}
	boolean visible = clientHeight < availableItems.length;
	if (visible != vBar.getVisible ()) {
		vBar.setVisible (visible);
		clientArea = getClientArea ();
	}
	/* horizontal scrollbar */ 
	ScrollBar hBar = getHorizontalBar ();
	int hBarMaximum = hBar.getMaximum ();
	thumb = Math.min (clientArea.width, hBarMaximum);
	hBar.setThumb (thumb);
	hBar.setPageIncrement (thumb);
	horizontalOffset = hBar.getSelection ();
	visible = clientArea.width < hBarMaximum;
	if (visible != hBar.getVisible ()) {
		hBar.setVisible (visible);
		clientArea = getClientArea ();
	}
	/* header */
	int headerHeight = Math.max (fontHeight, headerImageHeight) + 2 * getHeaderPadding ();
	header.setSize (clientArea.width, headerHeight);
}
void onScrollHorizontal (Event event) {
	int newSelection = getHorizontalBar ().getSelection ();
	Rectangle clientArea = getClientArea ();
	update ();
	GC gc = new GC (this);
	gc.copyArea (
		0, 0,
		clientArea.width, clientArea.height,
		horizontalOffset - newSelection, 0);
	gc.dispose ();
	if (header.isVisible ()) {
		header.update ();
		clientArea = header.getClientArea ();
		gc = new GC (header);
		gc.copyArea (
			0, 0,
			clientArea.width, clientArea.height,
			horizontalOffset - newSelection, 0);
		gc.dispose ();
	}
	horizontalOffset = newSelection;
}
void onScrollVertical (Event event) {
	int newSelection = getVerticalBar ().getSelection ();
	Rectangle clientArea = getClientArea ();
	update ();
	GC gc = new GC (this);
	gc.copyArea (
		0, 0,
		clientArea.width, clientArea.height,
		0, (topIndex - newSelection) * itemHeight);
	gc.dispose ();
	topIndex = newSelection;
}
void onSpace () {
	if (focusItem == null) return;
	if (!focusItem.isSelected ()) {
		selectItem (focusItem, (style & SWT.MULTI) != 0);
		redrawItem (focusItem.availableIndex, true);
	}
	if ((style & SWT.CHECK) != 0) {
		focusItem.checked = !focusItem.checked;
		Rectangle bounds = focusItem.getCheckboxBounds ();
		redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
	}	
	showItem (focusItem);
	Event event = new Event ();
	event.item = focusItem;
	postEvent (SWT.Selection, event);
	if ((style & SWT.CHECK) == 0) return;
	
	/* SWT.CHECK */
	event = new Event ();
	event.item = focusItem;
	event.detail = SWT.CHECK;
	postEvent (SWT.Selection, event);
}
/*
 * The current focus item is about to become unavailable, so reassign focus.
 */
void reassignFocus () {
	if (focusItem == null) return;
	
	/* reassign to current focus' parent item if it has one */
	if (focusItem.parentItem != null) {
		TreeItem item = focusItem.parentItem;
		setFocusItem (item, false);
		showItem (item);
		if ((style & SWT.MULTI) != 0) return;
		setSelection (new TreeItem[] {item});
		Event event = new Event ();
		event.item = item;
		sendEvent (SWT.Selection, event);
		return;
	}
	
	/* 
	 * reassign to the previous root-level item if there is one, or the next
	 * root-level item otherwise
	 */
	int index = focusItem.getIndex ();
	if (index != 0) {
		index--;
	} else {
		index++;
	}
	if (index < items.length) {
		TreeItem item = items [index];
		setFocusItem (item, false);
		showItem (item);
		if ((style & SWT.SINGLE) != 0) {
			setSelection (new TreeItem[] {item});
			Event event = new Event ();
			event.item = item;
			sendEvent (SWT.Selection, event);
		}
	} else {
		setFocusItem (null, false);		/* no items left */
	}
}
/* 
 * Redraws from the specified index down to the last available item inclusive.  Note
 * that the redraw bounds do not extend beyond the current last item, so clients
 * that reduce the number of available items should use #redrawItems(int,int) instead
 * to ensure that redrawing extends down to the previous bottom item boundary.
 */
void redrawFromItemDownwards (int index) {
	redrawItems (index, availableItems.length - 1, false);
}
/*
 * Redraws the tree item at the specified index.  It is valid for this index to reside
 * beyond the last available item.
 */
void redrawItem (int itemIndex, boolean focusBoundsOnly) {
	redrawItems (itemIndex, itemIndex, focusBoundsOnly);
}
/*
 * Redraws the tree between the start and end item indices inclusive.  It is valid
 * for the end index value to extend beyond the last available item.
 */
void redrawItems (int startIndex, int endIndex, boolean focusBoundsOnly) {
	int startY = (startIndex - topIndex) * itemHeight + getHeaderHeight ();
	int height = (endIndex - startIndex + 1) * itemHeight;
	if (focusBoundsOnly) {
		if (columns.length > 0) {
			int rightX = columns [0].width - horizontalOffset;
			if (rightX <= 0) return;	/* first column not visible */
		}
		endIndex = Math.min (endIndex, availableItems.length - 1);
		for (int i = startIndex; i <= endIndex; i++) {
			Rectangle bounds = availableItems [i].getFocusBounds ();
			redraw (bounds.x, startY, bounds.width, height, false);
		}
	} else {
		Rectangle bounds = getClientArea ();
		redraw (0, startY, bounds.width, height, false);
	}
}
public void removeAll () {
	checkWidget ();
	setFocusItem (null, false);
	TreeItem[] items = this.items;
	this.items = new TreeItem [0];
	selectedItems = new TreeItem [0];
	availableItems = new TreeItem [0];
	anchorItem = insertMarkItem = lastClickedItem = null;
	for (int i = 0; i < items.length; i++) {
		items [i].dispose (false);
	}
	topIndex = 0;
	ScrollBar vBar = getVerticalBar ();
	ScrollBar hBar = getHorizontalBar ();
	vBar.setMaximum (1);
	hBar.setMaximum (1);
	vBar.setVisible (false);
	hBar.setVisible (false);
	redraw ();
}
void removeSelectedItem (int index) {
	TreeItem[] newSelectedItems = new TreeItem [selectedItems.length - 1];
	System.arraycopy (selectedItems, 0, newSelectedItems, 0, index);
	System.arraycopy (selectedItems, index + 1, newSelectedItems, index, newSelectedItems.length - index);
	selectedItems = newSelectedItems;
}
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);	
}
public void removeTreeListener (TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Expand, listener);
	removeListener (SWT.Collapse, listener);
}
public void selectAll () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	TreeItem[] items = getAllItems ();
	selectedItems = new TreeItem [items.length];
	System.arraycopy (items, 0, selectedItems, 0, items.length);
	redraw ();
}
void selectItem (TreeItem item, boolean addToSelection) {
	TreeItem[] oldSelectedItems = selectedItems;
	if (!addToSelection || (style & SWT.SINGLE) != 0) {
		selectedItems = new TreeItem[] {item};
		for (int i = 0; i < oldSelectedItems.length; i++) {
			if (oldSelectedItems [i] != item) {
				redrawItem (oldSelectedItems [i].availableIndex, true);
			}
		}
	} else {
		selectedItems = new TreeItem [selectedItems.length + 1];
		System.arraycopy (oldSelectedItems, 0, selectedItems, 0, oldSelectedItems.length);
		selectedItems [selectedItems.length - 1] = item;
	}
}
void setFocusItem (TreeItem item, boolean redrawOldFocus) {
	if (item == focusItem) return;
	TreeItem oldFocusItem = focusItem;
	focusItem = item;
	if (redrawOldFocus && oldFocusItem != null) {
		redrawItem (oldFocusItem.availableIndex, true);
	}
}
public void setFont (Font value) {
	checkWidget ();
	Font oldFont = getFont ();
	super.setFont (value);
	Font font = getFont ();
	if (font.equals (oldFont)) return;
		
	GC gc = new GC (this);
	
	/* recompute the receiver's cached font height and item height values */
	fontHeight = gc.getFontMetrics ().getHeight ();
	itemHeight = Math.max (fontHeight, imageHeight) + 2 * getCellPadding ();
	Point headerSize = header.getSize ();
	int newHeaderHeight = Math.max (fontHeight, headerImageHeight) + 2 * getHeaderPadding ();
	if (headerSize.y != newHeaderHeight) {
		header.setSize (headerSize.x, newHeaderHeight);
	}
	header.setFont (font);

	/* 
	 * Notify all columns and items of the font change so that elements that
	 * use the receiver's font can recompute their cached string widths.
	 */
	for (int i = 0; i < columns.length; i++) {
		columns [i].updateFont (gc);
	}
	for (int i = 0; i < items.length; i++) {
		items [i].updateFont (gc);
	}
	
	gc.dispose ();
	
	if (header.isVisible ()) header.redraw ();
	
	/* update scrollbars */
	updateHorizontalBar ();
	ScrollBar vBar = getVerticalBar ();
	int thumb = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
	vBar.setThumb (thumb);
	vBar.setPageIncrement (thumb);
	topIndex = vBar.getSelection ();
	vBar.setVisible (thumb < vBar.getMaximum ());
	redraw ();
}
void setHeaderImageHeight (int value) {
	headerImageHeight = value;
	Point headerSize = header.getSize ();
	int newHeaderHeight = Math.max (fontHeight, headerImageHeight) + 2 * getHeaderPadding ();
	if (headerSize.y != newHeaderHeight) {
		header.setSize (headerSize.x, newHeaderHeight);
	}
}
public void setHeaderVisible (boolean value) {
	checkWidget ();
	if (header.getVisible () == value) return;		/* no change */
	header.setVisible (value);
	updateVerticalBar ();
	redraw ();
}
void setImageHeight (int value) {
	imageHeight = value;
	itemHeight = Math.max (fontHeight, imageHeight) + 2 * getCellPadding ();
}
public void setInsertMark (TreeItem item, boolean before) {
	checkWidget ();
	if (item != null && item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item != null && item.parent != this) return;
	if (item == insertMarkItem && before == insertMarkPrecedes) return;	/* no change */
	
	TreeItem oldInsertItem = insertMarkItem;
	insertMarkItem = item;
	insertMarkPrecedes = before;
	if (oldInsertItem != null && oldInsertItem.availableIndex != -1) {
		redrawItem (oldInsertItem.availableIndex, true);
	}
	if (item != null && item != oldInsertItem && item.availableIndex != -1) {
		redrawItem (item.availableIndex, true);
	}
}
public void setLinesVisible (boolean value) {
	checkWidget ();
	if (linesVisible == value) return;		/* no change */
	linesVisible = value;
	redraw ();
}
public void setSelection (TreeItem[] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0 || ((style & SWT.SINGLE) != 0 && items.length > 1)) {
		deselectAll ();
		return;
	}
	TreeItem[] oldSelection = selectedItems;
	
	/* remove null and duplicate items */
	int index = 0;
	selectedItems = new TreeItem [items.length];	/* assume all valid items */
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isSelected ()) {
			selectedItems [index++] = item;
		}
	}
	if (index != items.length) {
		/* an invalid item was provided so resize the array accordingly */
		TreeItem[] temp = new TreeItem [index];
		System.arraycopy (selectedItems, 0, temp, 0, index);
		selectedItems = temp;
	}

	for (int i = 0; i < oldSelection.length; i++) {
		if (!oldSelection [i].isSelected ()) {
			int availableIndex = oldSelection [i].availableIndex;
			if (availableIndex != -1) {
				redrawItem (availableIndex, true);
			}
		}
	}
	for (int i = 0; i < selectedItems.length; i++) {
		int availableIndex = selectedItems [i].availableIndex;
		if (availableIndex != -1) {
			redrawItem (availableIndex, true);
		}
	}
}
public void setTopItem (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;

	/* item must be available */
	if (!item.isAvailable ()) item.parentItem.expandAncestors ();
	
	int visibleItemCount = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
	int index = Math.min (item.availableIndex, availableItems.length - visibleItemCount);
	if (topIndex == index) return;

	update ();
	int change = topIndex - index;
	topIndex = index;
	getVerticalBar ().setSelection (topIndex);
	Rectangle clientArea = getClientArea ();
	GC gc = new GC (this);
	gc.copyArea (0, 0, clientArea.width, clientArea.height, 0, change * itemHeight);
	gc.dispose ();
}
public void showColumn (TreeColumn column) {
	checkWidget ();
	int x = column.getX ();
	int rightX = x + column.width;
	Rectangle bounds = getClientArea ();
	int boundsRight = bounds.x + bounds.width;
	if (bounds.x <= x && rightX <= boundsRight) return;	 /* column is fully visible */

	int absX = 0;	/* the X of the column irrespective of the horizontal scroll */
	for (int i = 0; i < column.getIndex (); i++) {
		absX += columns [i].width;
	}
	if (x < bounds.x) { 	/* column is to left of viewport */
		horizontalOffset = absX;
	} else {
		horizontalOffset = boundsRight - absX;
	}
	getHorizontalBar ().setSelection (horizontalOffset);
	redraw ();
}
public void showItem (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	
	/* item must be available */
	if (!item.isAvailable ()) item.parentItem.expandAncestors ();
	
	int index = item.availableIndex;
	int visibleItemCount = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
	/* nothing to do if item is already in viewport */
	if (topIndex <= index && index < topIndex + visibleItemCount) return;
	
	if (index <= topIndex) {
		/* item is above current viewport, so show on top */
		setTopItem (item);
	} else {
		/* item is below current viewport, so show on bottom */
		setTopItem (availableItems [Math.min (index - visibleItemCount + 1, availableItems.length - 1)]);
	}
}
public void showSelection () {
	checkWidget ();
	if (selectedItems.length == 0) return;
	showItem (selectedItems [0]);
}
void updateColumnWidth (TreeColumn column, int width) {
	int oldWidth = column.width;
	column.width = width;
	Rectangle bounds = getClientArea ();

	int maximum = 0;
	for (int i = 0; i < columns.length; i++) {
		maximum += columns [i].width;
	}
	ScrollBar hBar = getHorizontalBar ();
	hBar.setMaximum (maximum);
	hBar.setThumb (bounds.width);
	hBar.setPageIncrement (bounds.width);
	hBar.setVisible (bounds.width < maximum);
	boolean offsetChanged = false;
	int selection = hBar.getSelection ();
	if (selection != horizontalOffset) {
		horizontalOffset = selection;
		offsetChanged = true;
	}
	
	/* 
	 * Notify column and all items of column width change so that display labels
	 * can be recomputed if needed.
	 */
	GC gc = new GC (this);
	column.computeDisplayText (gc);
	for (int i = 0; i < items.length; i++) {
		items [i].updateColumnWidth (column, gc);
	}
	gc.dispose ();

	int x = 0;
	if (!offsetChanged) x = column.getX ();
	redraw (x, 0, bounds.width - x, bounds.height, false);
	if (getHeaderVisible ()) {
		header.redraw (x, 0, bounds.width - x, getHeaderHeight (), false);
	}

	Event event = new Event ();
	event.widget = column;
	column.sendEvent (SWT.Resize, event);
	for (int i = column.getIndex () + 1; i < columns.length; i++) {
		event = new Event ();
		event.widget = columns [i];
		columns [i].sendEvent (SWT.Move, event);
	}
}
/*
 * This is a naive implementation that computes the value from scratch.
 */
void updateHorizontalBar () {
	ScrollBar hBar = getHorizontalBar ();
	int maxX = 0;
	if (columns.length > 0) {
		for (int i = 0; i < columns.length; i++) {
			maxX += columns [i].width;
		}
	} else {
		for (int i = 0; i < availableItems.length; i++) {
			Rectangle itemBounds = availableItems [i].getBounds ();
			maxX = Math.max (maxX, itemBounds.x + itemBounds.width + horizontalOffset);
		}
	}
	
	int clientWidth = getClientArea ().width;
	if (maxX != hBar.getMaximum ()) {
		hBar.setMaximum (maxX);
	}
	int thumb = Math.min (clientWidth, maxX);
	if (thumb != hBar.getThumb ()) {
		hBar.setThumb (thumb);
		hBar.setPageIncrement (thumb);
	}
	hBar.setVisible (clientWidth < maxX);
	
	/* reclaim any space now left on the right */
	if (maxX < horizontalOffset + thumb) {
		horizontalOffset = maxX - thumb;
		hBar.setSelection (horizontalOffset);
		redraw ();
	} else {
		int selection = hBar.getSelection ();
		if (selection != horizontalOffset) {
			horizontalOffset = selection;
			redraw ();
		}
	}
}
/*
 * Update the horizontal bar, if needed, in response to an item change (eg.- created,
 * disposed, expanded, etc.).  newRightX is the new rightmost X value of the item,
 * and rightXchange is the change that led to the item's rightmost X value becoming
 * newRightX (so oldRightX + rightXchange = newRightX)
 */
void updateHorizontalBar (int newRightX, int rightXchange) {
	newRightX += horizontalOffset;
	ScrollBar hBar = getHorizontalBar ();
	int barMaximum = hBar.getMaximum ();
	if (newRightX > barMaximum) {	/* item has extended beyond previous maximum */
		hBar.setMaximum (newRightX);
		int clientAreaWidth = getClientArea ().width;
		int thumb = Math.min (newRightX, clientAreaWidth);
		hBar.setThumb (thumb);
		hBar.setPageIncrement (thumb);
		hBar.setVisible (clientAreaWidth <= newRightX);
		return;
	}

	int previousRightX = newRightX - rightXchange;
	if (previousRightX != barMaximum) {
		/* this was not the rightmost item, so just check for client width change */
		int clientAreaWidth = getClientArea ().width;
		int thumb = Math.min (barMaximum, clientAreaWidth);
		hBar.setThumb (thumb);
		hBar.setPageIncrement (thumb);
		hBar.setVisible (clientAreaWidth <= barMaximum);
		return;
	}
	updateHorizontalBar ();		/* must search for the new rightmost item */
}
void updateVerticalBar () {
	int pageSize = (getClientArea ().height - getHeaderHeight ()) / itemHeight;
	int maximum = Math.max (1, availableItems.length);
	ScrollBar vBar = getVerticalBar ();
	if (maximum != vBar.getMaximum ()) {
		vBar.setMaximum (maximum);
	}
	int thumb = Math.min (pageSize, maximum);
	if (thumb != vBar.getThumb ()) {
		vBar.setThumb (thumb);
		vBar.setPageIncrement (thumb);
	}
	vBar.setVisible (pageSize < maximum);

	/* reclaim any space now left on the bottom */
	if (maximum < topIndex + thumb) {
		topIndex = maximum - thumb;
		vBar.setSelection (topIndex);
		redraw ();
	} else {
		int selection = vBar.getSelection ();
		if (selection != topIndex) {
			topIndex = selection;
			redraw ();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12111.java