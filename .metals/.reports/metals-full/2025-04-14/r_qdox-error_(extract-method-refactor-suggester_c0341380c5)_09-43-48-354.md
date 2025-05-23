error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3269.java
text:
```scala
i@@f (newText != null) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify text.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, LEFT, MULTI, PASSWORD, SINGLE, RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified. 
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Text extends Scrollable {
	int tabs, oldStart, oldEnd;
	boolean doubleClick, ignoreModify, ignoreVerify, ignoreCharacter;
	
	/**
	* The maximum number of characters that can be entered
	* into a text widget.
	*/
	public static final int LIMIT;
	
	/**
	* The delimiter used by multi-line text widgets.  When text
	* is queried and from the widget, it will be delimited using
	* this delimiter.
	*/
	public static final String DELIMITER;
	
	/*
	* This code is intentionally commented.
	*/
//	static final char PASSWORD;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = OS.IsWinNT ? 0x7FFFFFFF : 0x7FFF;
		DELIMITER = "\r\n";
	}
	
	static final int EditProc;
	static final TCHAR EditClass = new TCHAR (0, "EDIT", true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, EditClass, lpWndClass);
		EditProc = lpWndClass.lpfnWndProc;
		/*
		* This code is intentionally commented.
		*/
//		int hwndText = OS.CreateWindowEx (0,
//			EditClass,
//			null,
//			OS.WS_OVERLAPPED | OS.ES_PASSWORD,
//			0, 0, 0, 0,
//			0,
//			0,
//			OS.GetModuleHandle (null),
//			null);
//		char echo = (char) OS.SendMessage (hwndText, OS.EM_GETPASSWORDCHAR, 0, 0);
//		OS.DestroyWindow (hwndText);
//		PASSWORD = echo != 0 ? echo : '*';
	}

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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (EditProc, handle, msg, wParam, lParam);
}

void createHandle () {
	super.createHandle ();
	OS.SendMessage (handle, OS.EM_LIMITTEXT, 0, 0);
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
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
 * @see VerifyListener
 * @see #removeVerifyListener
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

/**
 * Appends a string.
 * <p>
 * The new text is appended to the text at
 * the end of the widget.
 * </p>
 *
 * @param string the string to be appended
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void append (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.withCrLf (string);
	int length = OS.GetWindowTextLength (handle);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		string = verifyText (string, length, length, null);
		if (string == null) return;
	}
	OS.SendMessage (handle, OS.EM_SETSEL, length, length);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	/*
	* Feature in Windows.  When an edit control with ES_MULTILINE
	* style that does not have the WS_VSCROLL style is full (i.e.
	* there is no space at the end to draw any more characters),
	* EM_REPLACESEL sends a WM_CHAR with a backspace character
	* to remove any further text that is added.  This is an
	* implementation detail of the edit control that is unexpected
	* and can cause endless recursion when EM_REPLACESEL is sent
	* from a WM_CHAR handler.  The fix is to ignore calling the
	* handler from WM_CHAR.
	*/
	ignoreCharacter = true;
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	ignoreCharacter = false;
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
	if ((style & SWT.WRAP) != 0) {
		style |= SWT.MULTI;
		style &= ~SWT.H_SCROLL;
	}
	if ((style & SWT.MULTI) != 0) style &= ~SWT.PASSWORD;
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) return style | SWT.MULTI;
	return style | SWT.SINGLE;
}

/**
 * Clears the selection.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void clearSelection () {
	checkWidget ();
	if (OS.IsWinCE) {
		/*
		* Bug in WinCE.  Calling EM_SETSEL with -1 and 0 is equivalent
		* to calling EM_SETSEL with 0 and -1.  It causes the entire
		* text to be selected instead of clearing the selection.  The
		* fix is to set the start of the selection to the  end of the
		* current selection.
		*/ 
		int [] end = new int [1];
		OS.SendMessage (handle, OS.EM_GETSEL, (int []) null, end);
		OS.SendMessage (handle, OS.EM_SETSEL, end [0], end [0]);
	} else {
		OS.SendMessage (handle, OS.EM_SETSEL, -1, 0);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int height = 0, width = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		int newFont, oldFont = 0;
		int hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
		OS.GetTextMetrics (hDC, tm);
		int count = OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
		height = count * tm.tmHeight;
		RECT rect = new RECT ();
		int flags = OS.DT_CALCRECT | OS.DT_EDITCONTROL | OS.DT_NOPREFIX;
		boolean wrap = (style & SWT.MULTI) != 0 && (style & SWT.WRAP) != 0;
		if (wrap && wHint != SWT.DEFAULT) {
			flags |= OS.DT_WORDBREAK;
			rect.right = wHint;
		}
		int length = OS.GetWindowTextLength (handle);
		if (length != 0) {
			TCHAR buffer = new TCHAR (getCodePage (), length + 1);
			OS.GetWindowText (handle, buffer, length + 1);
			OS.DrawText (hDC, buffer, length, rect, flags);
			width = rect.right - rect.left;
		}
		if (wrap && hHint == SWT.DEFAULT) {
			int newHeight = rect.bottom - rect.top;
			if (newHeight != 0) height = newHeight;
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	return new Point (trim.width, trim.height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	Rectangle rect = super.computeTrim (x, y, width, height);
	/*
	* The preferred height of a single-line text widget
	* has been hand-crafted to be the same height as
	* the single-line text widget in an editable combo
	* box.
	*/
	int margins = OS.SendMessage(handle, OS.EM_GETMARGINS, 0, 0);
	rect.x -= margins & 0xFFFF;
	rect.width += (margins & 0xFFFF) + ((margins >> 16) & 0xFFFF);
	if ((style & SWT.H_SCROLL) != 0) rect.width++;
	if ((style & SWT.BORDER) != 0) {
		rect.x -= 1;
		rect.y -= 1;
		rect.width += 2;
		rect.height += 2;
	}
	return rect;
}

/**
 * Copies the selected text.
 * <p>
 * The current selection is copied to the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void copy () {
	checkWidget ();
	OS.SendMessage (handle, OS.WM_COPY, 0, 0);
}

void createWidget () {
	super.createWidget ();
	doubleClick = true;
	setTabStops (tabs = 8);
	fixAlignment ();
}

/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.SendMessage (handle, OS.WM_CUT, 0, 0);
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

void fixAlignment () {
	/*
	* Feature in Windows.  When the edit control is not
	* mirrored, it uses WS_EX_RIGHT, WS_EX_RTLREADING and
	* WS_EX_LEFTSCROLLBAR to give the control a right to
	* left appearance.  This causes the control to be lead
	* aligned no matter what alignment was specified by
	* the programmer.  For example, setting ES_RIGHT and
	* WS_EX_LAYOUTRTL should cause the contents of the
	* control to be left (trail) aligned in a mirrored world.
	* When the orientation is changed by the user or
	* specified by the programmer, WS_EX_RIGHT conflicts
	* with the mirrored alignment.  The fix is to clear
	* or set WS_EX_RIGHT to achieve the correct alignment
	* according to the orientation and mirroring.
	*/
	if ((style & SWT.MIRRORED) != 0) return;
	int bits1 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	int bits2 = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((style & SWT.LEFT_TO_RIGHT) != 0) {
		/*
		* Bug in Windows 98. When the edit control is created
		* with the style ES_RIGHT it automatically sets the 
		* WS_EX_LEFTSCROLLBAR bit.  The fix is to clear the
		* bit when the orientation of the control is left
		* to right.
		*/
		bits1 &= ~OS.WS_EX_LEFTSCROLLBAR;
		if ((style & SWT.RIGHT) != 0) {
			bits1 |= OS.WS_EX_RIGHT;
			bits2 |= OS.ES_RIGHT;
		}
		if ((style & SWT.LEFT) != 0) {
			bits1 &= ~OS.WS_EX_RIGHT;
			bits2 &= ~OS.ES_RIGHT;
		}
	} else {
		if ((style & SWT.RIGHT) != 0) {
			bits1 &= ~OS.WS_EX_RIGHT;
			bits2 &= ~OS.ES_RIGHT;
		}
		if ((style & SWT.LEFT) != 0) {
			bits1 |= OS.WS_EX_RIGHT;
			bits2 |= OS.ES_RIGHT;
		}
	}
	if ((style & SWT.CENTER) != 0) {
		bits2 |= OS.ES_CENTER;
	}	
	OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits1);
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits2);
}

public int getBorderWidth () {
	checkWidget ();
	/*
	* Feature in Windows 2000 and XP.  Despite the fact that WS_BORDER
	* is set when the edit control is created, the style bit is cleared.
	* The fix is to avoid the check for WS_BORDER and use the SWT widget
	* style bits instead.
	*/
//	if ((style & SWT.BORDER) != 0 && (style & SWT.FLAT) != 0) {
//		return OS.GetSystemMetrics (OS.SM_CXBORDER);
//	}
	return super.getBorderWidth ();
}

/**
 * Gets the line number of the caret.
 * <p>
 * The line number of the caret is returned.
 * </p>
 *
 * @return the line number
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretLineNumber () {
	checkWidget ();
	return OS.SendMessage (handle, OS.EM_LINEFROMCHAR, -1, 0);
}

/**
 * Gets the location the caret.
 * <p>
 * The location of the caret is returned.
 * </p>
 *
 * @return a point, the location of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getCaretLocation () {
	checkWidget ();
	/*
	* Bug in Windows.  For some reason, Windows is unable
	* to return the pixel coordinates of the last character
	* in the widget.  The fix is to temporarily insert a
	* space, query the coordinates and delete the space.
	* The selection is always an i-beam in this case because
	* this is the only time the start of the selection can
	* be equal to the last character position in the widget.
	* If EM_POSFROMCHAR fails for any other reason, return
	* pixel coordinates (0,0). 
	*/
	int [] start = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, (int []) null);
	int pos = OS.SendMessage (handle, OS.EM_POSFROMCHAR, start [0], 0);
	if (pos == -1) {
		pos = 0;
		if (start [0] >= OS.GetWindowTextLength (handle)) {
			int cp = getCodePage ();
			/*
			* Feature in Windows.  When an edit control with ES_MULTILINE
			* style that does not have the WS_VSCROLL style is full (i.e.
			* there is no space at the end to draw any more characters),
			* EM_REPLACESEL sends a WM_CHAR with a backspace character
			* to remove any further text that is added.  This is an
			* implementation detail of the edit control that is unexpected
			* and can cause endless recursion when EM_REPLACESEL is sent
			* from a WM_CHAR handler.  The fix is to ignore calling the
			* handler from WM_CHAR.
			*/
			ignoreCharacter = ignoreModify = true;
			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, new TCHAR (cp, " ", true));
			pos = OS.SendMessage (handle, OS.EM_POSFROMCHAR, start [0], 0);
			OS.SendMessage (handle, OS.EM_SETSEL, start [0], start [0] + 1);
			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, new TCHAR (cp, "", true));
			ignoreCharacter = ignoreModify = false;
		}
	}
	return new Point ((short) (pos & 0xFFFF), (short) (pos >> 16));
}

/**
 * Gets the position of the caret.
 * <p>
 * The character position of the caret is returned.
 * </p>
 *
 * @return the position of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretPosition () {
	checkWidget ();
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	int startLine = OS.SendMessage (handle, OS.EM_LINEFROMCHAR, start [0], 0);
	int caretPos = OS.SendMessage (handle, OS.EM_LINEINDEX, -1, 0);
	int caretLine = OS.SendMessage (handle, OS.EM_LINEFROMCHAR, caretPos, 0);
	int caret = end [0];
	if (caretLine == startLine) caret = start [0];
	if (OS.IsDBLocale) caret = mbcsToWcsPos (caret);
	return caret;
}

/**
 * Gets the number of characters.
 *
 * @return number of characters in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCharCount () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	if (OS.IsDBLocale) length = mbcsToWcsPos (length);
	return length;
}

/**
 * Gets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 * 
 * @return whether or not double click is enabled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getDoubleClickEnabled () {
	checkWidget ();
	return doubleClick;
}

/**
 * Gets the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer.
 * </p>
 * 
 * @return the echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public char getEchoChar () {
	checkWidget ();
	char echo = (char) OS.SendMessage (handle, OS.EM_GETPASSWORDCHAR, 0, 0);
	if (echo != 0 && (echo = Display.mbcsToWcs (echo, getCodePage ())) == 0) echo = '*';
	return echo;
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
 */
public boolean getEditable () {
	checkWidget ();
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.ES_READONLY) == 0;
}

/**
 * Gets the number of lines.
 *
 * @return the number of lines in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineCount () {
	checkWidget ();
	return OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
}

/**
 * Gets the line delimiter.
 *
 * @return a string that is the line delimiter
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getLineDelimiter () {
	checkWidget ();
	return DELIMITER;
}

/**
 * Gets the height of a line.
 *
 * @return the height of a row of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineHeight () {
	checkWidget ();
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
	OS.GetTextMetrics (hDC, tm);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	return tm.tmHeight;
}

/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**
 * Gets the position of the selected text.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p>
 * 
 * @return the start and end of the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget ();
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (OS.IsDBLocale) {
		start [0] = mbcsToWcsPos (start [0]);
		end [0] = mbcsToWcsPos (end [0]);
	}
	return new Point (start [0], end [0]);
}

/**
 * Gets the number of selected characters.
 *
 * @return the number of selected characters.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget ();
	Point selection = getSelection ();
	return selection.y - selection.x;
}

/**
 * Gets the selected text.
 *
 * @return the selected text
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getSelectionText () {
	checkWidget ();
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	Point selection = getSelection ();
	return getText ().substring (selection.x, selection.y);
}

/**
 * Gets the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @return the number of tab characters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTabs () {
	checkWidget ();
	return tabs;
}

int getTabWidth (int tabs) {
	int oldFont = 0;
	RECT rect = new RECT ();
	int hDC = OS.GetDC (handle);
	int newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	TCHAR SPACE = new TCHAR (getCodePage (), " ", false);
	OS.DrawText (hDC, SPACE, SPACE.length (), rect, flags);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	return (rect.right - rect.left) * tabs;
}

/**
 * Gets the widget text.
 * <p>
 * The text for a text widget is the characters in the widget.
 * </p>
 *
 * @return the widget text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	if (length == 0) return "";
	TCHAR buffer = new TCHAR (getCodePage (), length + 1);
	OS.GetWindowText (handle, buffer, length + 1);
	return buffer.toString (0, length);
}

/**
 * Gets a range of text.  Returns an empty string if the
 * start of the range is greater than the end.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N-1 where N is
 * the number of characters in the widget.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 * @return the range of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText (int start, int end) {
	checkWidget ();
	if (!(start <= end && 0 <= end)) return "";
	int length = OS.GetWindowTextLength (handle);
	if (OS.IsDBLocale) length = mbcsToWcsPos (length);
	start = Math.max (0, start);
	end = Math.min (end, length - 1);
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return getText ().substring (start, end + 1);
}

/**
 * Returns the maximum number of characters that the receiver is capable of holding. 
 * <p>
 * If this has not been changed by <code>setTextLimit()</code>,
 * it will be the constant <code>Text.LIMIT</code>.
 * </p>
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
	return OS.SendMessage (handle, OS.EM_GETLIMITTEXT, 0, 0);
}

/**
 * Returns the zero-relative index of the line which is currently
 * at the top of the receiver.
 * <p>
 * This index can change when lines are scrolled or new lines are added or removed.
 * </p>
 *
 * @return the index of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 0;
	return OS.SendMessage (handle, OS.EM_GETFIRSTVISIBLELINE, 0, 0);
}

/**
 * Gets the top pixel.
 * <p>
 * The top pixel is the pixel position of the line
 * that is currently at the top of the widget.  On
 * some platforms, a text widget can be scrolled by
 * pixels instead of lines so that a partial line
 * is displayed at the top of the widget.
 * </p><p>
 * The top pixel changes when the widget is scrolled.
 * The top pixel does not include the widget trimming.
 * </p>
 *
 * @return the pixel position of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopPixel () {
	checkWidget ();
	/*
	* Note, EM_GETSCROLLPOS is implemented in Rich Edit 3.0
	* and greater.  The plain text widget and previous versions
	* of Rich Edit return zero.
	*/
	int [] buffer = new int [2];
	int code = OS.SendMessage (handle, OS.EM_GETSCROLLPOS, 0, buffer);
	if (code == 1) return buffer [1];
	return getTopIndex () * getLineHeight ();
}

/**
 * Inserts a string.
 * <p>
 * The old selection is replaced with the new text.
 * </p>
 *
 * @param string the string
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void insert (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.withCrLf (string);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int [] start = new int [1], end = new int [1];
		OS.SendMessage (handle, OS.EM_GETSEL, start, end);
		string = verifyText (string, start [0], end [0], null);
		if (string == null) return;
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	/*
	* Feature in Windows.  When an edit control with ES_MULTILINE
	* style that does not have the WS_VSCROLL style is full (i.e.
	* there is no space at the end to draw any more characters),
	* EM_REPLACESEL sends a WM_CHAR with a backspace character
	* to remove any further text that is added.  This is an
	* implementation detail of the edit control that is unexpected
	* and can cause endless recursion when EM_REPLACESEL is sent
	* from a WM_CHAR handler.  The fix is to ignore calling the
	* handler from WM_CHAR.
	*/
	ignoreCharacter = true;
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	ignoreCharacter = false;
}

int mbcsToWcsPos (int mbcsPos) {
	if (mbcsPos <= 0) return 0;
	if (OS.IsUnicode) return mbcsPos;
	int cp = getCodePage ();
	int wcsTotal = 0, mbcsTotal = 0;
	byte [] buffer = new byte [128];
	String delimiter = getLineDelimiter();
	int delimiterSize = delimiter.length ();
	int count = OS.SendMessageA (handle, OS.EM_GETLINECOUNT, 0, 0);
	for (int line=0; line<count; line++) {
		int wcsSize = 0;
		int linePos = OS.SendMessageA (handle, OS.EM_LINEINDEX, line, 0);
		int mbcsSize = OS.SendMessageA (handle, OS.EM_LINELENGTH, linePos, 0);
		if (mbcsSize != 0) {
			if (mbcsSize + delimiterSize > buffer.length) {
				buffer = new byte [mbcsSize + delimiterSize];
			}
			//ENDIAN
			buffer [0] = (byte) (mbcsSize & 0xFF);
			buffer [1] = (byte) (mbcsSize >> 8);
			mbcsSize = OS.SendMessageA (handle, OS.EM_GETLINE, line, buffer);
			wcsSize = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, mbcsSize, null, 0);
		}
		if (line - 1 != count) {
			for (int i=0; i<delimiterSize; i++) {
				buffer [mbcsSize++] = (byte) delimiter.charAt (i);
			}
			wcsSize += delimiterSize;
		}
		if ((mbcsTotal + mbcsSize) >= mbcsPos) {
			int bufferSize = mbcsPos - mbcsTotal;
			wcsSize = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, bufferSize, null, 0);
			return wcsTotal + wcsSize;
		}
		wcsTotal += wcsSize;
		mbcsTotal += mbcsSize;
	}
	return wcsTotal;
}

/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void paste () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.SendMessage (handle, OS.WM_PASTE, 0, 0);
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
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
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
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
 * @see VerifyListener
 * @see #addVerifyListener
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}

/**
 * Selects all the text in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	OS.SendMessage (handle, OS.EM_SETSEL, 0, -1);
}

boolean sendKeyEvent (int type, int msg, int wParam, int lParam, Event event) {
	if (!super.sendKeyEvent (type, msg, wParam, lParam, event)) {
		return false;
	}
	if ((style & SWT.READ_ONLY) != 0) return true;
	if (ignoreVerify) return true;
	if (type != SWT.KeyDown) return true;
	if (msg != OS.WM_CHAR && msg != OS.WM_KEYDOWN && msg != OS.WM_IME_CHAR) {
		return true;
	}
	if (event.character == 0) return true;
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return true;
	char key = event.character;
	int stateMask = event.stateMask;
	
	/*
	* Disable all magic keys that could modify the text
	* and don't send events when Alt, Shift or Ctrl is
	* pressed.
	*/
	switch (msg) {
		case OS.WM_CHAR:
			if (key != 0x08 && key != 0x7F && key != '\r' && key != '\t' && key != '\n') break;
			// FALL THROUGH
		case OS.WM_KEYDOWN:
			if ((stateMask & (SWT.ALT | SWT.SHIFT | SWT.CONTROL)) != 0) return false;
			break;
	}

	/*
	* If the left button is down, the text widget refuses the character.
	*/
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) {
		return true;
	}

	/* Verify the character */
	String oldText = "";
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	switch (key) {
		case 0x08:	/* Bs */
			if (start [0] == end [0]) {
				if (start [0] == 0) return true;
				int lineStart = OS.SendMessage (handle, OS.EM_LINEINDEX, -1, 0);
				if (start [0] == lineStart) {
					start [0] = start [0] - DELIMITER.length ();
				} else {
					start [0] = start [0] - 1;
					if (OS.IsDBLocale) {
						int [] newStart = new int [1], newEnd = new int [1];
						OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
						OS.SendMessage (handle, OS.EM_GETSEL, newStart, newEnd);
						if (start [0] != newStart [0]) start [0] = start [0] - 1;
					}
				}
				start [0] = Math.max (start [0], 0);
			}
			break;
		case 0x7F:	/* Del */
			if (start [0] == end [0]) {
				int length = OS.GetWindowTextLength (handle);
				if (start [0] == length) return true;
				int line = OS.SendMessage (handle, OS.EM_LINEFROMCHAR, end [0], 0);
				int lineStart = OS.SendMessage (handle, OS.EM_LINEINDEX, line + 1, 0);
				if (end [0] == lineStart - DELIMITER.length ()) {
					end [0] = end [0] + DELIMITER.length ();
				} else {
					end [0] = end [0] + 1;
					if (OS.IsDBLocale) {
						int [] newStart = new int [1], newEnd = new int [1];
						OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
						OS.SendMessage (handle, OS.EM_GETSEL, newStart, newEnd);
						if (end [0] != newEnd [0]) end [0] = end [0] + 1;
					}
				}
				end [0] = Math.min (end [0], length);
			}
			break;
		case '\r':	/* Return */
			if ((style & SWT.SINGLE) != 0) return true;
			oldText = DELIMITER;
			break;
		default:	/* Tab and other characters */
			if (key != '\t' && key < 0x20) return true;
			oldText = new String (new char [] {key});
			break;
	}
	String newText = verifyText (oldText, start [0], end [0], event);
	if (newText == null) return false;
	if (newText == oldText) return true;
	newText = Display.withCrLf (newText);
	TCHAR buffer = new TCHAR (getCodePage (), newText, true);
	OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
	/*
	* Feature in Windows.  When an edit control with ES_MULTILINE
	* style that does not have the WS_VSCROLL style is full (i.e.
	* there is no space at the end to draw any more characters),
	* EM_REPLACESEL sends a WM_CHAR with a backspace character
	* to remove any further text that is added.  This is an
	* implementation detail of the edit control that is unexpected
	* and can cause endless recursion when EM_REPLACESEL is sent
	* from a WM_CHAR handler.  The fix is to ignore calling the
	* handler from WM_CHAR.
	*/
	ignoreCharacter = true;
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	ignoreCharacter = false;
	return false;
}

void setBounds (int x, int y, int width, int height, int flags) {
	/*
	* Feature in Windows.  When the caret is moved,
	* the text widget scrolls to show the new location.
	* This means that the text widget may be scrolled
	* to the right in order to show the caret when the
	* widget is not large enough to show both the caret
	* location and all the text.  Unfortunately, when
	* the text widget is resized such that all the text
	* and the caret could be visible, Windows does not
	* scroll the widget back.  The fix is to resize the
	* text widget, set the selection to the start of the
	* text and then restore the selection.  This will
	* cause the text widget compute the correct scroll
	* position.
	*/
	if ((flags & OS.SWP_NOSIZE) == 0 && width != 0) {
		RECT rect = new RECT ();
		OS.GetWindowRect (handle, rect);
		if (rect.right - rect.left == 0) {
			int [] start = new int [1], end = new int [1];
			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
			if (start [0] != 0 || end [0] != 0) {
				SetWindowPos (handle, 0, x, y, width, height, flags);
				OS.SendMessage (handle, OS.EM_SETSEL, 0, 0);
				OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
				return;
			}
		}
	}	
	super.setBounds (x, y, width, height, flags);
}

/**
 * Sets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 * 
 * @param doubleClick the new double click flag
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDoubleClickEnabled (boolean doubleClick) {
	checkWidget ();
	this.doubleClick = doubleClick;
}

/**
 * Sets the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer. Setting
 * the echo character to '\0' clears the echo
 * character and redraws the original text.
 * If for any reason the echo character is invalid,
 * the default echo character for the platform
 * is used.
 * </p>
 *
 * @param echo the new echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEchoChar (char echo) {
	checkWidget ();
	if ((style & SWT.MULTI) != 0) return;
	if (echo != 0) {
		if ((echo = (char) Display.wcsToMbcs (echo, getCodePage ())) == 0) echo = '*';
	}
	OS.SendMessage (handle, OS.EM_SETPASSWORDCHAR, echo, 0);
	/*
	* Bug in Windows.  When the password character is changed,
	* Windows does not redraw to show the new password character.
	* The fix is to force a redraw when the character is set.
	*/
	OS.InvalidateRect (handle, null, true);
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
 */
public void setEditable (boolean editable) {
	checkWidget ();
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY; 
	OS.SendMessage (handle, OS.EM_SETREADONLY, editable ? 0 : 1, 0);
}

public void setFont (Font font) {
	checkWidget ();
	super.setFont (font);
	setTabStops (tabs);
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 *
 * @param orientation new orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public void setOrientation (int orientation) {
	checkWidget();
	if (OS.IsWinCE) return;
	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) return;
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~flags;
	style |= orientation & flags;
	int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		bits |= OS.WS_EX_RTLREADING | OS.WS_EX_LEFTSCROLLBAR;
	} else {
		bits &= ~(OS.WS_EX_RTLREADING | OS.WS_EX_LEFTSCROLLBAR);
	}
	OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
	fixAlignment ();
}

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * regular array indexing rules.
 * </p>
 *
 * @param start new caret position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start) {
	checkWidget ();
	if (OS.IsDBLocale) start = wcsToMbcsPos (start);
	OS.SendMessage (handle, OS.EM_SETSEL, start, start);
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start, int end) {
	checkWidget ();
	if (OS.IsDBLocale) {
		start = wcsToMbcsPos (start);
		end = wcsToMbcsPos (end);
	}
	OS.SendMessage (handle, OS.EM_SETSEL, start, end);
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	super.setRedraw (redraw);
	/*
	* Feature in Windows.  When WM_SETREDRAW is used to turn
	* redraw off, the edit control is not scrolled to show the
	* i-beam.  The fix is to detect that the i-beam has moved
	* while redraw is turned off and force it to be visible
	* when redraw is restored.
	*/
	if (drawCount != 0) return;
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (!redraw) {
		oldStart = start [0];  oldEnd = end [0];
	} else {
		if (oldStart == start [0] && oldEnd == end [0]) return;
		OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
	}
}

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param selection the point
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
	checkWidget ();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (selection.x, selection.y);
}

/**
 * Sets the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @param tabs the number of tabs
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabs (int tabs) {
	checkWidget ();
	if (tabs < 0) return;
	setTabStops (this.tabs = tabs);
}

void setTabStops (int tabs) {
	/*
	* Feature in Windows.  Windows expects the tab spacing in
	* dialog units so we must convert from space widths.  Due
	* to round off error, the tab spacing may not be the exact
	* number of space widths, depending on the font.
	*/
	int width = (getTabWidth (tabs) * 4) / (OS.GetDialogBaseUnits () & 0xFFFF);
	OS.SendMessage (handle, OS.EM_SETTABSTOPS, 1, new int [] {width});
}

/**
 * Sets the contents of the receiver to the given string. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.withCrLf (string);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int length = OS.GetWindowTextLength (handle);
		string = verifyText (string, 0, length, null);
		if (string == null) return;
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
	/*
	* Bug in Windows.  When the widget is multi line
	* text widget, it does not send a WM_COMMAND with
	* control code EN_CHANGE from SetWindowText () to
	* notify the application that the text has changed.
	* The fix is to send the event.
	*/
	if ((style & SWT.MULTI) != 0) {
		sendEvent (SWT.Modify);
		// widget could be disposed at this point
	}
}

/**
 * Sets the maximum number of characters that the receiver
 * is capable of holding to be the argument.
 * <p>
 * Instead of trying to set the text limit to zero, consider
 * creating a read-only text widget.
 * </p><p>
 * To reset this value to the default, use <code>setTextLimit(Text.LIMIT)</code>.
 * </p>
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
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	OS.SendMessage (handle, OS.EM_SETLIMITTEXT, limit, 0);
}

/**
 * Sets the zero-relative index of the line which is currently
 * at the top of the receiver. This index can change when lines
 * are scrolled or new lines are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	int count = OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
	index = Math.min (Math.max (index, 0), count - 1);
	int topIndex = OS.SendMessage (handle, OS.EM_GETFIRSTVISIBLELINE, 0, 0);
	OS.SendMessage (handle, OS.EM_LINESCROLL, 0, index - topIndex);
}

/**
 * Shows the selection.
 * <p>
 * If the selection is already showing
 * in the receiver, this method simply returns.  Otherwise,
 * lines are scrolled until the selection is visible.
 * </p>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget ();
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

String verifyText (String string, int start, int end, Event keyEvent) {
	if (ignoreVerify) return string;
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent != null) {
		event.character = keyEvent.character;
		event.keyCode = keyEvent.keyCode;
		event.stateMask = keyEvent.stateMask;
	}
	if (OS.IsDBLocale) {
		event.start = mbcsToWcsPos (start);
		event.end = mbcsToWcsPos (end);
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the verify
	* event.  If this happens, answer null to cancel
	* the operation.
	*/
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

int wcsToMbcsPos (int wcsPos) {
	if (wcsPos <= 0) return 0;
	if (OS.IsUnicode) return wcsPos;
	int cp = getCodePage ();
	int wcsTotal = 0, mbcsTotal = 0;
	byte [] buffer = new byte [128];
	String delimiter = getLineDelimiter ();
	int delimiterSize = delimiter.length ();
	int count = OS.SendMessageA (handle, OS.EM_GETLINECOUNT, 0, 0);
	for (int line=0; line<count; line++) {
		int wcsSize = 0;
		int linePos = OS.SendMessageA (handle, OS.EM_LINEINDEX, line, 0);
		int mbcsSize = OS.SendMessageA (handle, OS.EM_LINELENGTH, linePos, 0);
		if (mbcsSize != 0) {
			if (mbcsSize + delimiterSize > buffer.length) {
				buffer = new byte [mbcsSize + delimiterSize];
			}
			//ENDIAN
			buffer [0] = (byte) (mbcsSize & 0xFF);
			buffer [1] = (byte) (mbcsSize >> 8);
			mbcsSize = OS.SendMessageA (handle, OS.EM_GETLINE, line, buffer);
			wcsSize = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, mbcsSize, null, 0);
		}
		if (line - 1 != count) {
			for (int i=0; i<delimiterSize; i++) {
				buffer [mbcsSize++] = (byte) delimiter.charAt (i);
			}
			wcsSize += delimiterSize;
		}
		if ((wcsTotal + wcsSize) >= wcsPos) {
			wcsSize = 0;
			int index = 0;
			while (index < mbcsSize) {
				if ((wcsTotal + wcsSize) == wcsPos) {
					return mbcsTotal + index;
				}
				if (OS.IsDBCSLeadByte (buffer [index++])) index++;
				wcsSize++;
			}
			return mbcsTotal + mbcsSize;
		}
		wcsTotal += wcsSize;
		mbcsTotal += mbcsSize;
	}
	return mbcsTotal;
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.ES_AUTOHSCROLL;
	if ((style & SWT.PASSWORD) != 0) bits |= OS.ES_PASSWORD;
	if ((style & SWT.CENTER) != 0) bits |= OS.ES_CENTER;
	if ((style & SWT.RIGHT) != 0) bits |= OS.ES_RIGHT;
	if ((style & SWT.READ_ONLY) != 0) bits |= OS.ES_READONLY;
	if ((style & SWT.SINGLE) != 0) return bits;
	bits |= OS.ES_MULTILINE | OS.ES_NOHIDESEL | OS.ES_AUTOVSCROLL;	
	if ((style & SWT.WRAP) != 0) bits &= ~(OS.WS_HSCROLL | OS.ES_AUTOHSCROLL);
	return bits;
}

TCHAR windowClass () {
	return EditClass;
}

int windowProc () {
	return EditProc;
}

int windowProc (int hwnd, int msg, int wParam, int lParam) {
	if (msg == OS.EM_UNDO) {
		if ((style & SWT.SINGLE) != 0) {
			LRESULT result = wmClipboard (OS.EM_UNDO, wParam, lParam);
			if (result != null) return result.value;
			return callWindowProc (OS.EM_UNDO, wParam, lParam);
		}
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

LRESULT WM_CHAR (int wParam, int lParam) {
	if (ignoreCharacter) return null;
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	
	/*
	* Bug in Windows.  When the user types CTRL and BS
	* in an edit control, a DEL character is generated.
	* Rather than deleting the text, the DEL character
	* is inserted into the control.  The fix is to detect
	* this case and not call the window proc.
	*/
	switch (wParam) {
		case SWT.DEL:
			if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
				return LRESULT.ZERO;
			}
	}
	
	/*
	* Feature in Windows.  For some reason, when the
	* widget is a single line text widget, when the
	* user presses tab, return or escape, Windows beeps.
	* The fix is to look for these keys and not call
	* the window proc.
	*/
	if ((style & SWT.SINGLE) != 0) {
		switch (wParam) {
			case SWT.CR:
				postEvent (SWT.DefaultSelection);
				// FALL THROUGH
			case SWT.TAB:
			case SWT.ESC: return LRESULT.ZERO;
		}
	}
	return result;
}

LRESULT WM_CLEAR (int wParam, int lParam) {
	LRESULT result = super.WM_CLEAR (wParam, lParam);
	if (result != null) return result;
	return wmClipboard (OS.WM_CLEAR, wParam, lParam);
}

LRESULT WM_CUT (int wParam, int lParam) {
	LRESULT result = super.WM_CUT (wParam, lParam);
	if (result != null) return result;
	return wmClipboard (OS.WM_CUT, wParam, lParam);
}

LRESULT WM_GETDLGCODE (int wParam, int lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;
	
	/*
	* Bug in WinCE PPC.  For some reason, sending WM_GETDLGCODE
	* to a multi-line edit control causes it to ignore return and
	* tab keys.  The fix is to return the value which is normally
	* returned by the text window proc on other versions of Windows.
	*/
	if (OS.IsPPC) {
		if ((style & SWT.MULTI) != 0 && (style & SWT.READ_ONLY) == 0 && lParam == 0) {
			return new LRESULT (OS.DLGC_HASSETSEL | OS.DLGC_WANTALLKEYS | OS.DLGC_WANTCHARS);
		}
	}

	/*
	* Feature in Windows.  Despite the fact that the
	* edit control is read only, it still returns a
	* dialog code indicating that it wants all keys.  
	* The fix is to detect this case and clear the bits.
	* 
	* NOTE: A read only edit control processes arrow keys
	* so DLGC_WANTARROWS should not be cleared.
	*/
	if ((style & SWT.READ_ONLY) != 0) {
		int code = callWindowProc (OS.WM_GETDLGCODE, wParam, lParam);
		code &= ~(OS.DLGC_WANTALLKEYS | OS.DLGC_WANTTAB);
		return new LRESULT (code);
	}
	return null;
}

LRESULT WM_IME_CHAR (int wParam, int lParam) {

	/* Process a DBCS character */
	Display display = this.display;
	display.lastKey = 0;
	display.lastAscii = wParam;
	display.lastVirtual = display.lastNull = display.lastDead = false;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam)) {
		return LRESULT.ZERO;
	}

	/*
	* Feature in Windows.  The Windows text widget uses
	* two 2 WM_CHAR's to process a DBCS key instead of
	* using WM_IME_CHAR.  The fix is to allow the text
	* widget to get the WM_CHAR's but ignore sending
	* them to the application.
	*/
	ignoreCharacter = true;
	int result = callWindowProc (OS.WM_IME_CHAR, wParam, lParam);
	MSG msg = new MSG ();
	int flags = OS.PM_REMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
	while (OS.PeekMessage (msg, handle, OS.WM_CHAR, OS.WM_CHAR, flags)) {
		OS.TranslateMessage (msg);
		OS.DispatchMessage (msg);
	}
	ignoreCharacter = false;
	
	sendKeyEvent (SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
	// widget could be disposed at this point
	display.lastKey = display.lastAscii = 0;
	return new LRESULT (result);
}

LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
	/*
	* Prevent Windows from processing WM_LBUTTONDBLCLK
	* when double clicking behavior is disabled by not
	* calling the window proc.
	*/
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 1, OS.WM_LBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	if (!doubleClick) return LRESULT.ZERO;
		
	/*
	* Bug in Windows.  When the last line of text in the
	* widget is double clicked and the line is empty, Windows
	* hides the i-beam then moves it to the first line in
	* the widget but does not scroll to show the user.
	* If the user types without clicking the mouse, invalid
	* characters are displayed at the end of each line of
	* text in the widget.  The fix is to detect this case
	* and avoid calling the window proc.
	*/
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (start [0] == end [0]) {
		int length = OS.GetWindowTextLength (handle);
		if (length == start [0]) {
			int result = OS.SendMessage (handle, OS.EM_LINELENGTH, length, 0);
			if (result == 0) return LRESULT.ZERO;
		}
	}
	return null;
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	if (!OS.IsPPC) return super.WM_LBUTTONDOWN (wParam, lParam);
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
	/*
	* Note: On WinCE PPC, only attempt to recognize the gesture for
	* a context menu when the control contains a valid menu or there
	* are listeners for the MenuDetect event.
	* 
	* Note: On WinCE PPC, the gesture that brings up a popup menu
	* on the text widget must keep the current text selection.  As a
	* result, the window proc is only called if the menu is not shown.
	*/
	boolean hasMenu = menu != null && !menu.isDisposed ();
	if (hasMenu || hooks (SWT.MenuDetect)) {
		int x = (short) (lParam & 0xFFFF);
		int y = (short) (lParam >> 16);
		SHRGINFO shrg = new SHRGINFO ();
		shrg.cbSize = SHRGINFO.sizeof;
		shrg.hwndClient = handle;
		shrg.ptDown_x = x;
		shrg.ptDown_y = y; 
		shrg.dwFlags = OS.SHRG_RETURNCMD;
		int type = OS.SHRecognizeGesture (shrg);
		if (type == OS.GN_CONTEXTMENU) {
			showMenu (x, y);
			return LRESULT.ONE;
		}
	}
	int result = callWindowProc (OS.WM_LBUTTONDOWN, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	return new LRESULT (result);
}

LRESULT WM_PASTE (int wParam, int lParam) {
	LRESULT result = super.WM_PASTE (wParam, lParam);
	if (result != null) return result;
	return wmClipboard (OS.WM_PASTE, wParam, lParam);
}

LRESULT WM_UNDO (int wParam, int lParam) {
	LRESULT result = super.WM_UNDO (wParam, lParam);
	if (result != null) return result;
	return wmClipboard (OS.WM_UNDO, wParam, lParam);
}

LRESULT wmClipboard (int msg, int wParam, int lParam) {
	if ((style & SWT.READ_ONLY) != 0) return null;
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return null;
	boolean call = false;
	int [] start = new int [1], end = new int [1];
	String oldText = null, newText = null;
	switch (msg) {
		case OS.WM_CLEAR:
		case OS.WM_CUT:
			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
			if (start [0] != end [0]) {
				newText = "";
				call = true;
			}
			break;
		case OS.WM_PASTE:
			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
			newText = getClipboardText ();
			break;
		case OS.EM_UNDO:
		case OS.WM_UNDO:
			if (OS.SendMessage (handle, OS.EM_CANUNDO, 0, 0) != 0) {
				OS.SendMessage (handle, OS.EM_GETSEL, start, end);
				ignoreModify = ignoreCharacter = true;
				callWindowProc (msg, wParam, lParam);
				newText = getSelectionText ();
				callWindowProc (msg, wParam, lParam);
				ignoreModify = ignoreCharacter = false;
			}
			break;
	}
	if (newText != null && !newText.equals (oldText)) {
		oldText = newText;
		newText = verifyText (newText, start [0], end [0], null);
		if (newText == null) return LRESULT.ZERO;
		if (!newText.equals (oldText)) {
			if (call) {
				callWindowProc (msg, wParam, lParam);
			}
			newText = Display.withCrLf (newText);
			TCHAR buffer = new TCHAR (getCodePage (), newText, true);
			/*
			* Feature in Windows.  When an edit control with ES_MULTILINE
			* style that does not have the WS_VSCROLL style is full (i.e.
			* there is no space at the end to draw any more characters),
			* EM_REPLACESEL sends a WM_CHAR with a backspace character
			* to remove any further text that is added.  This is an
			* implementation detail of the edit control that is unexpected
			* and can cause endless recursion when EM_REPLACESEL is sent
			* from a WM_CHAR handler.  The fix is to ignore calling the
			* handler from WM_CHAR.
			*/
			ignoreCharacter = true;
			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
			ignoreCharacter = false;
			return LRESULT.ZERO;
		}
	}
	if (msg == OS.WM_UNDO) {
		ignoreVerify = ignoreCharacter = true;
		callWindowProc (OS.WM_UNDO, wParam, lParam);
		ignoreVerify = ignoreCharacter = false;
		return LRESULT.ONE;
	}
	return null;
}

LRESULT wmCommandChild (int wParam, int lParam) {
	int code = wParam >> 16;
	switch (code) {
		case OS.EN_CHANGE:
			if (ignoreModify) break;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the modify
			* event.  If this happens, end the processing of the
			* Windows message by returning zero as the result of
			* the window proc.
			*/
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
		case OS.EN_ALIGN_LTR_EC:
			style &= ~SWT.RIGHT_TO_LEFT;
			style |= SWT.LEFT_TO_RIGHT;
			fixAlignment ();
			break;
		case OS.EN_ALIGN_RTL_EC:
			style &= ~SWT.LEFT_TO_RIGHT;
			style |= SWT.RIGHT_TO_LEFT;
			fixAlignment ();
			break;
	}
	return super.wmCommandChild (wParam, lParam);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3269.java