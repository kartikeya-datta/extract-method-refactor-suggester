error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5904.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5904.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5904.java
text:
```scala
t@@ransferData.length = (len[0] + 3) / 4 * 4;

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

/**
 * The class <code>ImageTransfer</code> provides a platform specific mechanism 
 * for converting a Image represented as a java <code>ImageData</code> to a 
 * platform specific representation of the data and vice versa.  
 * See <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>ImageData</code> is shown 
 * below:</p>
 * 
 * <code><pre>
 *     Image image = new Image("C:\temp\img1.gif");
 *	   ImageData imgData = image.getImageData();
 * </code></pre>
 */
public class ImageTransfer extends ByteArrayTransfer {
	
	private static ImageTransfer _instance = new ImageTransfer();
	
	private static final String JPEG = "image/jpge"; //$NON-NLS-1$
	private static final int JPEG_ID = registerType(JPEG);
	private static final String PNG = "image/png"; //$NON-NLS-1$
	private static final int PNG_ID = registerType(PNG);
	private static final String BMP = "image/bmp"; //$NON-NLS-1$
	private static final int BMP_ID = registerType(BMP);
	private static final String EPS = "image/eps"; //$NON-NLS-1$
	private static final int EPS_ID = registerType(EPS);
	private static final String PCX = "image/pcx"; //$NON-NLS-1$
	private static final int PCX_ID = registerType(PCX);
	private static final String PPM = "image/ppm"; //$NON-NLS-1$
	private static final int PPM_ID = registerType(PPM);
	private static final String RGB = "image/ppm"; //$NON-NLS-1$
	private static final int RGB_ID = registerType(RGB);
	private static final String TGA = "image/tga"; //$NON-NLS-1$
	private static final int TGA_ID = registerType(TGA);
	private static final String XBM = "image/xbm"; //$NON-NLS-1$
	private static final int XBM_ID = registerType(XBM);
	private static final String XPM = "image/xpm"; //$NON-NLS-1$
	private static final int XPM_ID = registerType(XPM);
	private static final String XV = "image/xv"; //$NON-NLS-1$
	private static final int XV_ID = registerType(XV);	
	
private ImageTransfer() {}

/**
 * Returns the singleton instance of the ImageTransfer class.
 *
 * @return the singleton instance of the ImageTransfer class
 */
public static ImageTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts an ImageData object represented
 * by java <code>ImageData</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>ImageData</code> containing the ImageData to be 
 * converted
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative(Object object, TransferData transferData) {
	if (!checkImage(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) return;
	
	ImageData imgData = (ImageData)object;
	if (imgData == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	Image image = new Image(Display.getCurrent(), imgData);	
	int /*long*/ pixmap = image.pixmap; 
 	int width = imgData.width;
 	int height = imgData.height;  	
 	int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, true, 8, width, height);
	if (pixbuf == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int /*long*/ colormap = OS.gdk_colormap_get_system();
	OS.gdk_pixbuf_get_from_drawable(pixbuf, pixmap, colormap, 0, 0, 0, 0, width, height);	
	
	String typeStr = "";
	if (transferData.type ==  JPEG_ID) typeStr = "jpeg";
	if (transferData.type ==  PNG_ID) typeStr = "png";
	if (transferData.type ==  BMP_ID) typeStr = "bmp";
	if (transferData.type ==  EPS_ID) typeStr = "eps";
	if (transferData.type ==  PCX_ID) typeStr = "pcx";
	if (transferData.type ==  PPM_ID) typeStr = "ppm";
	if (transferData.type ==  RGB_ID) typeStr = "rgb";
	if (transferData.type ==  TGA_ID) typeStr = "tga";
	if (transferData.type ==  XBM_ID) typeStr = "xbm";
	if (transferData.type ==  XPM_ID) typeStr = "xpm";
	if (transferData.type ==  XV_ID) typeStr = "xv";
	byte[] type = Converter.wcsToMbcs(null, typeStr , true);
	int /*long*/ [] buffer = new int /*long*/ [1];
	int [] len = new int [1];
	if (type == null) return;
	OS.gdk_pixbuf_save_to_buffer(pixbuf, buffer, len, type , null, null);		
	OS.g_object_unref(pixbuf);
	image.dispose();
	transferData.pValue = buffer[0];
	transferData.length = len[0];
	transferData.result = 1;
	transferData.format = 32;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of an <code>ImageData</code> to java.  
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>ImageData</code> the imageData of the image if
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData) {
	ImageData imgData = null;
	if (transferData.length > 0)
	{
		int /*long*/ loader = OS.gdk_pixbuf_loader_new();
		OS.gdk_pixbuf_loader_write(loader, transferData.pValue, transferData.length, null);
		OS.gdk_pixbuf_loader_close(loader, null);
		int /*long*/ pixbuf = OS.gdk_pixbuf_loader_get_pixbuf(loader);
		if (pixbuf != 0) {
			OS.g_object_ref(pixbuf);
			int /*long*/ [] pixmap_return = new int /*long*/ [1];
			OS.gdk_pixbuf_render_pixmap_and_mask(pixbuf, pixmap_return, null, 0);
			int /*long*/ handle = pixmap_return[0];
			if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			OS.g_object_unref(loader);
			Image img = Image.gtk_new(Display.getCurrent(), SWT.BITMAP, handle, 0);		
			imgData = img.getImageData();
			img.dispose();
		}		
	}
	return imgData;
}

protected int[] getTypeIds(){
	return new int[]{JPEG_ID, PNG_ID, BMP_ID, EPS_ID, PCX_ID, PPM_ID, RGB_ID, TGA_ID, XBM_ID, XPM_ID, XV_ID};	
}

protected String[] getTypeNames(){
	return new String[]{JPEG, PNG, BMP, EPS, PCX, PPM, RGB, TGA, XBM, XPM, XV};
}

boolean checkImage(Object object) {
	if (object == null || !(object instanceof ImageData)) return false;
	return true;
}

protected boolean validate(Object object) {
	return checkImage(object);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5904.java