error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10864.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10864.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10864.java
text:
```scala
i@@f (siblings [i] == this) {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.accessibility.*;

/**
 * The CCombo class represents a selectable user interface object
 * that combines a text field and a list and issues notification
 * when an item is selected from the list.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER, READ_ONLY, FLAT</dd>
 * <dt><b>Events:</b>
 * <dd>Selection</dd>
 * </dl>
 */
public final class CCombo extends Composite {

	Text text;
	List list;
	int visibleItemCount = 5;
	Shell popup;
	Button arrow;
	boolean hasFocus;
	Listener listener, filter;
	Color foreground, background;
	Font font;
	
/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT#BORDER
 * @see SWT#READ_ONLY
 * @see SWT#FLAT
 * @see Widget#getStyle()
 */
public CCombo (Composite parent, int style) {
	super (parent, style = checkStyle (style));
	
	int textStyle = SWT.SINGLE;
	if ((style & SWT.READ_ONLY) != 0) textStyle |= SWT.READ_ONLY;
	if ((style & SWT.FLAT) != 0) textStyle |= SWT.FLAT;
	text = new Text (this, textStyle);
	int arrowStyle = SWT.ARROW | SWT.DOWN;
	if ((style & SWT.FLAT) != 0) arrowStyle |= SWT.FLAT;
	arrow = new Button (this, arrowStyle);

	listener = new Listener () {
		public void handleEvent (Event event) {
			if (popup == event.widget) {
				popupEvent (event);
				return;
			}
			if (text == event.widget) {
				textEvent (event);
				return;
			}
			if (list == event.widget) {
				listEvent (event);
				return;
			}
			if (arrow == event.widget) {
				arrowEvent (event);
				return;
			}
			if (CCombo.this == event.widget) {
				comboEvent (event);
				return;
			}
			if (getShell () == event.widget) {
				handleFocus (SWT.FocusOut);
			}
		}
	};
	filter = new Listener() {
		public void handleEvent(Event event) {
			Shell shell = ((Control)event.widget).getShell ();
			if (shell == CCombo.this.getShell ()) {
				handleFocus (SWT.FocusOut);
			}
		}
	};
	
	int [] comboEvents = {SWT.Dispose, SWT.Move, SWT.Resize};
	for (int i=0; i<comboEvents.length; i++) this.addListener (comboEvents [i], listener);
	
	int [] textEvents = {SWT.KeyDown, SWT.KeyUp, SWT.Modify, SWT.MouseDown, SWT.MouseUp, SWT.Traverse, SWT.FocusIn};
	for (int i=0; i<textEvents.length; i++) text.addListener (textEvents [i], listener);
	
	int [] arrowEvents = {SWT.Selection, SWT.FocusIn};
	for (int i=0; i<arrowEvents.length; i++) arrow.addListener (arrowEvents [i], listener);
	
	createPopup(null, -1);
	initAccessible();
}
static int checkStyle (int style) {
	int mask = SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	return style & mask;
}
/**
 * Adds the argument to the end of the receiver's list.
 *
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget();
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	list.add (string);
}
/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	checkWidget();
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	list.add (string, index);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the combo's list selection changes.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed the combo's text area.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}
void arrowEvent (Event event) {
	switch (event.type) {
		case SWT.FocusIn: {
			handleFocus (SWT.FocusIn);
			break;
		}
		case SWT.Selection: {
			dropDown (!isDropped ());
			break;
		}
	}
}
/**
 * Sets the selection in the receiver's text field to an empty
 * selection starting just before the first character. If the
 * text field is editable, this has the effect of placing the
 * i-beam at the start of the text.
 * <p>
 * Note: To clear the selected items in the receiver's list, 
 * use <code>deselectAll()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #deselectAll
 */
public void clearSelection () {
	checkWidget ();
	text.clearSelection ();
	list.deselectAll ();
}
void comboEvent (Event event) {
	switch (event.type) {
		case SWT.Dispose:
			if (popup != null && !popup.isDisposed ()) {
				list.removeListener (SWT.Dispose, listener);
				popup.dispose ();
			}
			Shell shell = getShell ();
			shell.removeListener (SWT.Deactivate, listener);
			Display display = getDisplay ();
			display.removeFilter (SWT.FocusIn, filter);
			popup = null;  
			text = null;  
			list = null;  
			arrow = null;
			break;
		case SWT.Move:
			dropDown (false);
			break;
		case SWT.Resize:
			internalLayout (false);
			break;
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	String[] items = list.getItems ();
	int textWidth = 0;
	GC gc = new GC (text);
	int spacer = gc.stringExtent (" ").x; //$NON-NLS-1$
	for (int i = 0; i < items.length; i++) {
		textWidth = Math.max (gc.stringExtent (items[i]).x, textWidth);
	}
	gc.dispose();
	Point textSize = text.computeSize (SWT.DEFAULT, SWT.DEFAULT, changed);
	Point arrowSize = arrow.computeSize (SWT.DEFAULT, SWT.DEFAULT, changed);
	Point listSize = list.computeSize (wHint, SWT.DEFAULT, changed);
	int borderWidth = getBorderWidth ();
	
	height = Math.max (hHint, Math.max (textSize.y, arrowSize.y) + 2*borderWidth);
	width = Math.max (wHint, Math.max (textWidth + 2*spacer + arrowSize.x + 2*borderWidth, listSize.x));
	return new Point (width, height);
}
void createPopup(String[] items, int selectionIndex) {		
		// create shell and list
		popup = new Shell (getShell (), SWT.NO_TRIM | SWT.ON_TOP);
		int style = getStyle ();
		int listStyle = SWT.SINGLE | SWT.V_SCROLL;
		if ((style & SWT.FLAT) != 0) listStyle |= SWT.FLAT;
		if ((style & SWT.RIGHT_TO_LEFT) != 0) listStyle |= SWT.RIGHT_TO_LEFT;
		if ((style & SWT.LEFT_TO_RIGHT) != 0) listStyle |= SWT.LEFT_TO_RIGHT;
		list = new List (popup, listStyle);
		if (font != null) list.setFont (font);
		if (foreground != null) list.setForeground (foreground);
		if (background != null) list.setBackground (background);
		
		int [] popupEvents = {SWT.Close, SWT.Paint, SWT.Deactivate};
		for (int i=0; i<popupEvents.length; i++) popup.addListener (popupEvents [i], listener);
		int [] listEvents = {SWT.MouseUp, SWT.Selection, SWT.Traverse, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn, SWT.Dispose};
		for (int i=0; i<listEvents.length; i++) list.addListener (listEvents [i], listener);
		
		if (items != null) list.setItems (items);
		if (selectionIndex != -1) list.setSelection (selectionIndex);
}
/**
 * Deselects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget ();
	list.deselect (index);
}
/**
 * Deselects all selected items in the receiver's list.
 * <p>
 * Note: To clear the selection in the receiver's text field,
 * use <code>clearSelection()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #clearSelection
 */
public void deselectAll () {
	checkWidget ();
	list.deselectAll ();
}
void dropDown (boolean drop) {
	if (drop == isDropped ()) return;
	if (!drop) {
		popup.setVisible (false);
		if (!isDisposed ()&& arrow.isFocusControl()) {
			text.setFocus();
		}
		return;
	}

	if (getShell() != popup.getParent ()) {
		String[] items = list.getItems ();
		int selectionIndex = list.getSelectionIndex ();
		list.removeListener (SWT.Dispose, listener);
		popup.dispose();
		popup = null;
		list = null;
		createPopup (items, selectionIndex);
	}
	
	Point size = getSize ();
	int itemCount = list.getItemCount ();
	itemCount = (itemCount == 0) ? visibleItemCount : Math.min(visibleItemCount, itemCount);
	int itemHeight = list.getItemHeight () * itemCount;
	Point listSize = list.computeSize (SWT.DEFAULT, itemHeight, false);
	list.setBounds (1, 1, Math.max (size.x - 2, listSize.x), listSize.y);
	
	int index = list.getSelectionIndex ();
	if (index != -1) list.setTopIndex (index);
	Display display = getDisplay ();
	Rectangle listRect = list.getBounds ();
	Rectangle parentRect = display.map (getParent (), null, getBounds ());
	Point comboSize = getSize ();
	Rectangle displayRect = getMonitor ().getClientArea ();
	int width = Math.max (comboSize.x, listRect.width + 2);
	int height = listRect.height + 2;
	int x = parentRect.x;
	int y = parentRect.y + comboSize.y;
	if (y + height > displayRect.y + displayRect.height) y = parentRect.y - height;
	popup.setBounds (x, y, width, height);
	popup.setVisible (true);
	list.setFocus ();
}
/*
 * Return the lowercase of the first non-'&' character following
 * an '&' character in the given string. If there are no '&'
 * characters in the given string, return '\0'.
 */
char _findMnemonic (String string) {
	if (string == null) return '\0';
	int index = 0;
	int length = string.length ();
	do {
		while (index < length && string.charAt (index) != '&') index++;
		if (++index >= length) return '\0';
		if (string.charAt (index) != '&') return Character.toLowerCase (string.charAt (index));
		index++;
	} while (index < length);
 	return '\0';
}
/* 
 * Return the Label immediately preceding the receiver in the z-order, 
 * or null if none. 
 */
Label getAssociatedLabel () {
	Control[] siblings = getParent ().getChildren ();
	for (int i = 0; i < siblings.length; i++) {
		if (siblings [i] == CCombo.this) {
			if (i > 0 && siblings [i-1] instanceof Label) {
				return (Label) siblings [i-1];
			}
		}
	}
	return null;
}
public Control [] getChildren () {
	checkWidget();
	return new Control [0];
}
/**
 * Gets the editable state.
 *
 * @return whether or not the reciever is editable
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public boolean getEditable () {
	checkWidget ();
	return text.getEditable();
}
/**
 * Returns the item at the given, zero-relative index in the
 * receiver's list. Throws an exception if the index is out
 * of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getItem (int index) {
	checkWidget();
	return list.getItem (index);
}
/**
 * Returns the number of items contained in the receiver's list.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	return list.getItemCount ();
}
/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's list.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget ();
	return list.getItemHeight ();
}
/**
 * Returns an array of <code>String</code>s which are the items
 * in the receiver's list. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget ();
	return list.getItems ();
}
/**
 * Returns a <code>Point</code> whose x coordinate is the start
 * of the selection in the receiver's text field, and whose y
 * coordinate is the end of the selection. The returned values
 * are zero-relative. An "empty" selection as indicated by
 * the the x and y coordinates having the same value.
 *
 * @return a point representing the selection start and end
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget ();
	return text.getSelection ();
}
/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver's list, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget ();
	return list.getSelectionIndex ();
}
public int getStyle () {
	int style = super.getStyle ();
	style &= ~SWT.READ_ONLY;
	if (!text.getEditable()) style |= SWT.READ_ONLY; 
	return style;
}
/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	return text.getText ();
}
/**
 * Returns the height of the receivers's text field.
 *
 * @return the text height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTextHeight () {
	checkWidget ();
	return text.getLineHeight ();
}
/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Combo.LIMIT</code>.
 * 
 * @return the text limit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTextLimit () {
	checkWidget ();
	return text.getTextLimit ();
}
/**
 * Gets the number of items that are visible in the drop
 * down portion of the receiver's list.
 *
 * @return the number of items that are visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public int getVisibleItemCount () {
	checkWidget ();
	return visibleItemCount;
}
void handleFocus (int type) {
	if (isDisposed ()) return;
	switch (type) {
		case SWT.FocusIn: {
			if (hasFocus) return;
			if (getEditable ()) text.selectAll ();
			hasFocus = true;
			Shell shell = getShell ();
			shell.removeListener (SWT.Deactivate, listener);
			shell.addListener (SWT.Deactivate, listener);
			Display display = getDisplay ();
			display.removeFilter (SWT.FocusIn, filter);
			display.addFilter (SWT.FocusIn, filter);
			Event e = new Event ();
			notifyListeners (SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			if (!hasFocus) return;
			Control focusControl = getDisplay ().getFocusControl ();
			if (focusControl == arrow || focusControl == list || focusControl == text) return;
			hasFocus = false;
			Shell shell = getShell ();
			shell.removeListener(SWT.Deactivate, listener);
			Display display = getDisplay ();
			display.removeFilter (SWT.FocusIn, filter);
			Event e = new Event ();
			notifyListeners (SWT.FocusOut, e);
			break;
		}
	}
}
/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string) {
	checkWidget ();
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return list.indexOf (string);
}
/**
 * Searches the receiver's list starting at the given, 
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @param start the zero-relative index at which to begin the search
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget ();
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return list.indexOf (string, start);
}

void initAccessible() {
	AccessibleAdapter accessibleAdapter = new AccessibleAdapter () {
		public void getName (AccessibleEvent e) {
			String name = null;
			Label label = getAssociatedLabel ();
			if (label != null) {
				name = stripMnemonic (label.getText());
			}
			e.result = name;
		}
		public void getKeyboardShortcut(AccessibleEvent e) {
			String shortcut = null;
			Label label = getAssociatedLabel ();
			if (label != null) {
				String text = label.getText ();
				if (text != null) {
					char mnemonic = _findMnemonic (text);
					if (mnemonic != '\0') {
						shortcut = "Alt+"+mnemonic; //$NON-NLS-1$
					}
				}
			}
			e.result = shortcut;
		}
		public void getHelp (AccessibleEvent e) {
			e.result = getToolTipText ();
		}
	};
	getAccessible ().addAccessibleListener (accessibleAdapter);
	text.getAccessible ().addAccessibleListener (accessibleAdapter);
	list.getAccessible ().addAccessibleListener (accessibleAdapter);
	
	arrow.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
		public void getName (AccessibleEvent e) {
			e.result = isDropped () ? SWT.getMessage ("SWT_Close") : SWT.getMessage ("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		public void getKeyboardShortcut (AccessibleEvent e) {
			e.result = "Alt+Down Arrow"; //$NON-NLS-1$
		}
		public void getHelp (AccessibleEvent e) {
			e.result = getToolTipText ();
		}
	});

	getAccessible().addAccessibleTextListener (new AccessibleTextAdapter() {
		public void getCaretOffset (AccessibleTextEvent e) {
			e.offset = text.getCaretPosition ();
		}
	});
	
	getAccessible().addAccessibleControlListener (new AccessibleControlAdapter() {
		public void getChildAtPoint (AccessibleControlEvent e) {
			Point testPoint = toControl (e.x, e.y);
			if (getBounds ().contains (testPoint)) {
				e.childID = ACC.CHILDID_SELF;
			}
		}
		
		public void getLocation (AccessibleControlEvent e) {
			Rectangle location = getBounds ();
			Point pt = toDisplay (location.x, location.y);
			e.x = pt.x;
			e.y = pt.y;
			e.width = location.width;
			e.height = location.height;
		}
		
		public void getChildCount (AccessibleControlEvent e) {
			e.detail = 0;
		}
		
		public void getRole (AccessibleControlEvent e) {
			e.detail = ACC.ROLE_COMBOBOX;
		}
		
		public void getState (AccessibleControlEvent e) {
			e.detail = ACC.STATE_NORMAL;
		}

		public void getValue (AccessibleControlEvent e) {
			e.result = getText ();
		}
	});

	text.getAccessible ().addAccessibleControlListener (new AccessibleControlAdapter () {
		public void getRole (AccessibleControlEvent e) {
			e.detail = text.getEditable () ? ACC.ROLE_TEXT : ACC.ROLE_LABEL;
		}
	});

	arrow.getAccessible ().addAccessibleControlListener (new AccessibleControlAdapter() {
		public void getDefaultAction (AccessibleControlEvent e) {
			e.result = isDropped () ? SWT.getMessage ("SWT_Close") : SWT.getMessage ("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	});
}
boolean isDropped () {
	return popup.getVisible ();
}
public boolean isFocusControl () {
	checkWidget();
	if (text.isFocusControl () || arrow.isFocusControl () || list.isFocusControl () || popup.isFocusControl ()) {
		return true;
	} 
	return super.isFocusControl ();
}
void internalLayout (boolean changed) {
	if (isDropped ()) dropDown (false);
	Rectangle rect = getClientArea ();
	int width = rect.width;
	int height = rect.height;
	Point arrowSize = arrow.computeSize (SWT.DEFAULT, height, changed);
	text.setBounds (0, 0, width - arrowSize.x, height);
	arrow.setBounds (width - arrowSize.x, 0, arrowSize.x, arrowSize.y);
}
void listEvent (Event event) {
	switch (event.type) {
		case SWT.Dispose:
			if (getShell () != popup.getParent ()) {
				String[] items = list.getItems ();
				int selectionIndex = list.getSelectionIndex ();
				popup = null;
				list = null;
				createPopup (items, selectionIndex);
			}
			break;
		case SWT.FocusIn: {
			handleFocus (SWT.FocusIn);
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1) return;
			dropDown (false);
			break;
		}
		case SWT.Selection: {
			int index = list.getSelectionIndex ();
			if (index == -1) return;
			text.setText (list.getItem (index));
			text.selectAll ();
			list.setSelection (index);
			Event e = new Event ();
			e.time = event.time;
			e.stateMask = event.stateMask;
			e.doit = event.doit;
			notifyListeners (SWT.Selection, e);
			event.doit = e.doit;
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_ARROW_PREVIOUS:
				case SWT.TRAVERSE_ARROW_NEXT:
					event.doit = false;
					break;
			}
			Event e = new Event ();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			notifyListeners (SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
		case SWT.KeyUp: {		
			Event e = new Event ();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners (SWT.KeyUp, e);
			break;
		}
		case SWT.KeyDown: {
			if (event.character == SWT.ESC) { 
				// Escape key cancels popup list
				dropDown (false);
			}
			if ((event.stateMask & SWT.ALT) != 0 && (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN)) {
				dropDown (false);
			}
			if (event.character == SWT.CR) {
				// Enter causes default selection
				dropDown (false);
				Event e = new Event ();
				e.time = event.time;
				e.stateMask = event.stateMask;
				notifyListeners (SWT.DefaultSelection, e);
			}
			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed ()) break;
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyDown, e);
			break;
			
		}
	}
}

void popupEvent(Event event) {
	switch (event.type) {
		case SWT.Paint:
			// draw black rectangle around list
			Rectangle listRect = list.getBounds();
			Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
			event.gc.setForeground(black);
			event.gc.drawRectangle(0, 0, listRect.width + 1, listRect.height + 1);
			break;
		case SWT.Close:
			event.doit = false;
			dropDown (false);
			break;
		case SWT.Deactivate:
			dropDown (false);
			break;
	}
}
public void redraw () {
	super.redraw();
	text.redraw();
	arrow.redraw();
	if (popup.isVisible()) list.redraw();
}
public void redraw (int x, int y, int width, int height, boolean all) {
	super.redraw(x, y, width, height, true);
}

/**
 * Removes the item from the receiver's list at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget();
	list.remove (index);
}
/**
 * Removes the items from the receiver's list which are
 * between the given zero-relative start and end 
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget();
	list.remove (start, end);
}
/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument, 
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget();
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	list.remove (string);
}
/**
 * Removes all of the items from the receiver's list and clear the
 * contents of receiver's text field.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();
	text.setText (""); //$NON-NLS-1$
	list.removeAll ();
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's text is modified.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Modify, listener);	
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's selection changes.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection,listener);	
}
/**
 * Selects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget();
	if (index == -1) {
		list.deselectAll ();
		text.setText (""); //$NON-NLS-1$
		return;
	}
	if (0 <= index && index < list.getItemCount()) {
		if (index != getSelectionIndex()) {
			text.setText (list.getItem (index));
			text.selectAll ();
			list.select (index);
			list.showSelection ();
		}
	}
}
public void setBackground (Color color) {
	super.setBackground(color);
	background = color;
	if (text != null) text.setBackground(color);
	if (list != null) list.setBackground(color);
	if (arrow != null) arrow.setBackground(color);
}
/**
 * Sets the editable state.
 *
 * @param editable the new editable state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setEditable (boolean editable) {
	checkWidget ();
	text.setEditable(editable);
}
public void setEnabled (boolean enabled) {
	super.setEnabled(enabled);
	if (popup != null) popup.setVisible (false);
	if (text != null) text.setEnabled(enabled);
	if (arrow != null) arrow.setEnabled(enabled);
}
public boolean setFocus () {
	checkWidget();
	return text.setFocus ();
}
public void setFont (Font font) {
	super.setFont (font);
	this.font = font;
	text.setFont (font);
	list.setFont (font);
	internalLayout (true);
}
public void setForeground (Color color) {
	super.setForeground(color);
	foreground = color;
	if (text != null) text.setForeground(color);
	if (list != null) list.setForeground(color);
	if (arrow != null) arrow.setForeground(color);
}
/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument. This is equivalent
 * to <code>remove</code>'ing the old item at the index, and then
 * <code>add</code>'ing the new item at that index.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget();
	list.setItem (index, string);
}
/**
 * Sets the receiver's list to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if an item in the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget ();
	list.setItems (items);
	if (!text.getEditable ()) text.setText (""); //$NON-NLS-1$
}
/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 * <p>
 * Note : No Layout can be set on this Control because it already
 * manages the size and position of its children.
 * </p>
 *
 * @param layout the receiver's new layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayout (Layout layout) {
	checkWidget ();
	return;
}
/**
 * Sets the selection in the receiver's text field to the
 * range specified by the argument whose x coordinate is the
 * start of the selection and whose y coordinate is the end
 * of the selection. 
 *
 * @param selection a point representing the new selection start and end
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (Point selection) {
	checkWidget();
	if (selection == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	text.setSelection (selection.x, selection.y);
}

/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to 
 * display incorrectly.
 * </p>
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget();
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int index = list.indexOf (string);
	if (index == -1) {
		list.deselectAll ();
		text.setText (string);
		return;
	}
	text.setText (string);
	text.selectAll ();
	list.setSelection (index);
	list.showSelection ();
}
/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 *
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTextLimit (int limit) {
	checkWidget();
	text.setTextLimit (limit);
}

public void setToolTipText (String string) {
	checkWidget();
	super.setToolTipText(string);
	arrow.setToolTipText (string);
	text.setToolTipText (string);		
}

public void setVisible (boolean visible) {
	super.setVisible(visible);
	if (!visible) popup.setVisible(false);
}
/**
 * Sets the number of items that are visible in the drop
 * down portion of the receiver's list.
 *
 * @param count the new number of items to be visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setVisibleItemCount (int count) {
	checkWidget ();
	if (count < 0) return;
	visibleItemCount = count;
}
String stripMnemonic (String string) {
	int index = 0;
	int length = string.length ();
	do {
		while ((index < length) && (string.charAt (index) != '&')) index++;
		if (++index >= length) return string;
		if (string.charAt (index) != '&') {
			return string.substring(0, index-1) + string.substring(index, length);
		}
		index++;
	} while (index < length);
 	return string;
}
void textEvent (Event event) {
	switch (event.type) {
		case SWT.FocusIn: {
			handleFocus (SWT.FocusIn);
			break;
		}
		case SWT.KeyDown: {
			if (event.character == SWT.CR) {
				dropDown (false);
				Event e = new Event ();
				e.time = event.time;
				e.stateMask = event.stateMask;
				notifyListeners (SWT.DefaultSelection, e);
			}
			//At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed ()) break;
			
			if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN) {
				event.doit = false;
				if ((event.stateMask & SWT.ALT) != 0) {
					boolean dropped = isDropped ();
					text.selectAll ();
					if (!dropped) setFocus ();
					dropDown (!dropped);
					break;
				}

				int oldIndex = getSelectionIndex ();
				if (event.keyCode == SWT.ARROW_UP) {
					select (Math.max (oldIndex - 1, 0));
				} else {
					select (Math.min (oldIndex + 1, getItemCount () - 1));
				}
				if (oldIndex != getSelectionIndex ()) {
					Event e = new Event();
					e.time = event.time;
					e.stateMask = event.stateMask;
					notifyListeners (SWT.Selection, e);
				}
				//At this point the widget may have been disposed.
				// If so, do not continue.
				if (isDisposed ()) break;
			}
			
			// Further work : Need to add support for incremental search in 
			// pop up list as characters typed in text widget
						
			Event e = new Event ();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners (SWT.KeyDown, e);
			break;
		}
		case SWT.KeyUp: {
			Event e = new Event ();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners (SWT.KeyUp, e);
			break;
		}
		case SWT.Modify: {
			list.deselectAll ();
			Event e = new Event ();
			e.time = event.time;
			notifyListeners (SWT.Modify, e);
			break;
		}
		case SWT.MouseDown: {
			if (event.button != 1) return;
			if (text.getEditable ()) return;
			boolean dropped = isDropped ();
			text.selectAll ();
			if (!dropped) setFocus ();
			dropDown (!dropped);
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1) return;
			if (text.getEditable ()) return;
			text.selectAll ();
			break;
		}
		case SWT.Traverse: {		
			switch (event.detail) {
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_ARROW_PREVIOUS:
				case SWT.TRAVERSE_ARROW_NEXT:
					// The enter causes default selection and
					// the arrow keys are used to manipulate the list contents so
					// do not use them for traversal.
					event.doit = false;
					break;
			}
			
			Event e = new Event ();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			notifyListeners (SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10864.java