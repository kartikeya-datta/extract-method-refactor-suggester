error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16232.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16232.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16232.java
text:
```scala
b@@its += (bmiHeader.biClrUsed == 0 ? (1 << bmiHeader.biBitCount) : bmiHeader.biClrUsed) * 4;

/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

/**
 * The class <code>ImageTransfer</code> provides a platform specific mechanism 
 * for converting an Image represented as a java <code>ImageData</code> to a 
 * platform specific representation of the data and vice versa.  
 * 
 * <p>An example of a java <code>ImageData</code> is shown below:</p>
 * 
 * <code><pre>
 *     Image image = new Image(display, "C:\temp\img1.gif");
 *	   ImageData imgData = image.getImageData();
 * </code></pre>
 *
 * @see Transfer
 * 
 * @since 3.4
 */
public class ImageTransfer extends ByteArrayTransfer {
	
	private static ImageTransfer _instance = new ImageTransfer();
	private static final String CF_DIB = "CF_DIB"; //$NON-NLS-1$
	private static final int CF_DIBID = COM.CF_DIB;
	
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
 * 
 * @param object a java <code>ImageData</code> containing the ImageData to be converted
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative(Object object, TransferData transferData) {
	if (!checkImage(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	ImageData imgData = (ImageData)object;
	if (imgData == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	int imageSize = imgData.data.length;
	int imageHeight = imgData.height;
	int bytesPerLine = imgData.bytesPerLine;

	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biSizeImage = imageSize; 
	bmiHeader.biWidth = imgData.width;
	bmiHeader.biHeight = imageHeight;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = (short)imgData.depth;
	bmiHeader.biCompression = OS.DIB_RGB_COLORS; 

	int colorSize = 0;
	if (bmiHeader.biBitCount <= 8) {
		colorSize += (1 << bmiHeader.biBitCount) * 4;
	}
	byte[] bmi = new byte[BITMAPINFOHEADER.sizeof + colorSize];
	OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);	

	RGB[] rgbs = imgData.palette.getRGBs();	
	if (rgbs != null && colorSize > 0) {
		int offset = BITMAPINFOHEADER.sizeof;
		for (int j = 0; j < rgbs.length; j++) {
			bmi[offset] = (byte)rgbs[j].blue;
			bmi[offset + 1] = (byte)rgbs[j].green;
			bmi[offset + 2] = (byte)rgbs[j].red;
			bmi[offset + 3] = 0;
			offset += 4;
		}
	}
	int /*long*/ newPtr = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, BITMAPINFOHEADER.sizeof + colorSize + imageSize);
	OS.MoveMemory(newPtr, bmi, bmi.length);	
	int /*long*/ pBitDest = newPtr + BITMAPINFOHEADER.sizeof + colorSize;

	if (imageHeight <= 0) {
		OS.MoveMemory(pBitDest, imgData.data, imageSize);
	} else {
		int offset = 0;
		pBitDest += bytesPerLine * (imageHeight - 1);
		byte[] scanline = new byte[bytesPerLine];
		for (int i = 0; i < imageHeight; i++) {
			System.arraycopy(imgData.data, offset, scanline, 0, bytesPerLine);
			OS.MoveMemory(pBitDest, scanline, bytesPerLine);
			offset += bytesPerLine;
			pBitDest -= bytesPerLine;
		}
	}			
	transferData.stgmedium = new STGMEDIUM();
	transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
	transferData.stgmedium.unionField = newPtr;
	transferData.stgmedium.pUnkForRelease = 0;
	transferData.result = COM.S_OK;
}


/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of an image to java <code>ImageData</code>.  
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>ImageData</code> of the image if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData) {
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) return null;
	IDataObject dataObject = new IDataObject(transferData.pIDataObject);
	dataObject.AddRef();
	FORMATETC formatetc = new FORMATETC();
	formatetc.cfFormat = COM.CF_DIB;
	formatetc.ptd = 0;
	formatetc.dwAspect = COM.DVASPECT_CONTENT;
	formatetc.lindex = -1;
	formatetc.tymed = COM.TYMED_HGLOBAL;
	STGMEDIUM stgmedium = new STGMEDIUM();
	stgmedium.tymed = COM.TYMED_HGLOBAL;
	transferData.result = getData(dataObject, formatetc, stgmedium);

	if (transferData.result != COM.S_OK) return null;
	int /*long*/ hMem = stgmedium.unionField;
	dataObject.Release();
	try {
		int /*long*/ ptr = OS.GlobalLock(hMem);
		if (ptr == 0) return null;
		try {
			BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();				
			OS.MoveMemory(bmiHeader, ptr, BITMAPINFOHEADER.sizeof);
			int /*long*/[] pBits = new int /*long*/[1]; 
			int /*long*/ memDib = OS.CreateDIBSection(0, ptr, OS.DIB_RGB_COLORS, pBits, 0, 0);
			if (memDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);			
			int /*long*/ bits = ptr + bmiHeader.biSize;
			if (bmiHeader.biBitCount <= 8) {
				bits += (1 << bmiHeader.biBitCount) * 4;
			} else if (bmiHeader.biCompression == OS.BI_BITFIELDS) {
				bits += 12;
			}
			if (bmiHeader.biHeight < 0) {
				OS.MoveMemory(pBits[0], bits, bmiHeader.biSizeImage);
			} else {
				DIBSECTION dib = new DIBSECTION();
				OS.GetObject(memDib, DIBSECTION.sizeof, dib);
				int biHeight = dib.biHeight;
				int scanline = dib.biSizeImage / biHeight;
				int /*long*/ pDestBits = pBits[0];
				int /*long*/ pSourceBits = bits + scanline * (biHeight - 1);
				for (int i = 0; i < biHeight; i++) {
					OS.MoveMemory(pDestBits, pSourceBits, scanline);
					pDestBits += scanline;
					pSourceBits -= scanline;
				}
			}
			Image image = Image.win32_new(null, SWT.BITMAP, memDib);
			ImageData data = image.getImageData();
			OS.DeleteObject(memDib);
			image.dispose();
			return data;
		} finally {
			OS.GlobalUnlock(hMem);
		}
	} finally {
		OS.GlobalFree(hMem);
	}
}

protected int[] getTypeIds(){
	return new int[] {CF_DIBID};
}

protected String[] getTypeNames(){
	return new String[] {CF_DIB};
}
boolean checkImage(Object object) {
	if (object == null || !(object instanceof ImageData))  return false; 
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16232.java