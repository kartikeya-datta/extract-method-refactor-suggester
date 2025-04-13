error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16105.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16105.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16105.java
text:
```scala
s@@uper (parent, checkStyle (parent, style));

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class allow the user to select a font
 * from all available fonts in the system.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class FontDialog extends Dialog {
	FontData fontData;
	RGB rgb;
	boolean open;
	int fontID, fontSize;

/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FontDialog (Shell parent) {
	this (parent, SWT.APPLICATION_MODAL);
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
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FontDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}

void changeFont(int arg0) {
}

/**
 * Returns a FontData object describing the font that was
 * selected in the dialog, or null if none is available.
 * 
 * @return the FontData for the selected font, or null
 * @deprecated use #getFontList ()
 */
public FontData getFontData () {
	return fontData;
}

/**
 * Returns a FontData set describing the font that was
 * selected in the dialog, or null if none is available.
 * 
 * @return the FontData for the selected font, or null
 * @since 2.1.1
 */
public FontData [] getFontList () {
	if (fontData == null) return null;
	FontData [] result = new FontData [1];
	result [0] = fontData;
	return result;
}

/**
 * Returns an RGB describing the color that was selected
 * in the dialog, or null if none is available.
 *
 * @return the RGB value for the selected color, or null
 *
 * @see PaletteData#getRGBs
 * 
 * @since 2.1
 */
public RGB getRGB () {
	return rgb;
}

//int fontProc (int nextHandler, int theEvent, int userData) {
//	int kind = OS.GetEventKind (theEvent);
//	switch (kind) {
//		case OS.kEventFontPanelClosed:
//			open = false;
//			break;
//		case OS.kEventFontSelection:
//			int [] fontID = new int [1];
//			if (OS.GetEventParameter (theEvent, OS.kEventParamATSUFontID, OS.typeUInt32, null, 4, null, fontID) == OS.noErr) {
//				this.fontID = fontID [0];
//			}
//			int [] fontSize = new int [1];
//			if (OS.GetEventParameter (theEvent, OS.kEventParamATSUFontSize, OS.typeFixed, null, 4, null, fontSize) == OS.noErr) {
//				this.fontSize = fontSize [0];
//			}
//			RGBColor color = new RGBColor ();
//			int [] actualSize = new int [1];
//			if (OS.GetEventParameter (theEvent, OS.kEventParamFontColor, OS.typeRGBColor, null, RGBColor.sizeof, actualSize, color) == OS.noErr) {
//				int red = (color.red >> 8) & 0xFF;
//				int green = (color.green >> 8) & 0xFF;
//				int blue =	(color.blue >> 8) & 0xFF;
//				rgb = new RGB (red, green, blue);
//			} else {
//				int [] dict = new int [1];
//				if (OS.GetEventParameter (theEvent, OS.kEventParamDictionary, OS.typeCFDictionaryRef, null, 4, actualSize, dict) == OS.noErr) {
//					int [] attrib = new int [1];
//					if (OS.CFDictionaryGetValueIfPresent (dict [0], OS.kFontPanelAttributesKey (), attrib)) {
//						int [] tags = new int [1];
//						int [] sizes = new int [1];
//						int [] values = new int [1];
//						if (OS.CFDictionaryGetValueIfPresent (attrib [0], OS.kFontPanelAttributeTagsKey (), tags) &&
//							OS.CFDictionaryGetValueIfPresent (attrib [0], OS.kFontPanelAttributeSizesKey (), sizes) &&
//							OS.CFDictionaryGetValueIfPresent (attrib [0], OS.kFontPanelAttributeValuesKey (), values)
//						) {
//							int count = OS.CFDataGetLength (tags [0]) / 4;
//							int tagPtr = OS.CFDataGetBytePtr (tags[0]);
//                            int sizePtr = OS.CFDataGetBytePtr (sizes [0]);
//                            int [] tag = new int [1];
//                            int [] size = new int [1];
//                            int valueOffset = 0;
//                            for (int i = 0 ; i < count ; i++) {
//                            	OS.memmove (tag, tagPtr + (i * 4), 4);
//                            	OS.memmove (size, sizePtr + (i * 4), 4);
//                                if (tag [0] == OS.kATSUColorTag && size[0] == RGBColor.sizeof) {
//                                    int valuePtr = OS.CFDataGetBytePtr (values [0]);
//                                	OS.memmove (color, valuePtr + valueOffset, RGBColor.sizeof);
//                                	int red = (color.red >> 8) & 0xFF;
//                    				int green = (color.green >> 8) & 0xFF;
//                    				int blue =	(color.blue >> 8) & 0xFF;
//                    				rgb = new RGB (red, green, blue);
//                                    break ;
//                                }
//                                valueOffset = size[0];
//                            }
//						}
//					}
//				}
//			}
//			break;
//	}
//	return OS.noErr;
//}
	
/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a FontData object describing the font that was selected,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public FontData open () {
	Display display = parent != null ? parent.display : Display.getCurrent ();
	
	NSFontPanel panel = NSFontPanel.sharedFontPanel();
	panel.setTitle(NSString.stringWith(title != null ? title : ""));
	if (fontData != null) {
		Font font = new Font(display, fontData);
		NSFontManager.sharedFontManager().setSelectedFont(font.handle, false);
		font.dispose();
	}
	SWTPanelDelegate delegate = (SWTPanelDelegate)new SWTPanelDelegate().alloc().init();
	int jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	delegate.setTag(jniRef);
	panel.setDelegate(delegate);
	fontData = null;
	panel.orderFront(null);
	NSApplication.sharedApplication().runModalForWindow_(panel);
	panel.setDelegate(null);
	delegate.release();
	OS.DeleteGlobalRef(jniRef);
	NSFont font = NSFontManager.sharedFontManager().selectedFont();
	if (font != null) {
		//TODO - this does work
		fontData = Font.cocoa_new(display, font).getFontData()[0];
	}
	return fontData;
}

/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the FontData to use initially, or null
 * @deprecated use #setFontList (FontData [])
 */
public void setFontData (FontData fontData) {
	this.fontData = fontData;
}

/**
 * Sets the set of FontData objects describing the font to
 * be selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the set of FontData objects to use initially, or null
 *        to let the platform select a default when open() is called
 *
 * @see Font#getFontData
 * 
 * @since 2.1.1
 */
public void setFontList (FontData [] fontData) {
	if (fontData != null && fontData.length > 0) {
		this.fontData = fontData [0];
	} else {
		this.fontData = null;
	}
}

/**
 * Sets the RGB describing the color to be selected by default
 * in the dialog, or null to let the platform choose one.
 *
 * @param rgb the RGB value to use initially, or null to let
 *        the platform select a default when open() is called
 *
 * @see PaletteData#getRGBs
 * 
 * @since 2.1
 */
public void setRGB (RGB rgb) {
	this.rgb = rgb;
}

void windowWillClose(int sender) {
	NSApplication.sharedApplication().stop(null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16105.java