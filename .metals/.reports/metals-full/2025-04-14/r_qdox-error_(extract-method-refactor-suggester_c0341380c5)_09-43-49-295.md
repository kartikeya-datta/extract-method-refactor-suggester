error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5764.java
text:
```scala
public static final i@@nt FillModeWinding = 1;

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
package org.eclipse.swt.internal.gdip;

import org.eclipse.swt.internal.*;

public class Gdip extends Platform {
	static {
		Library.loadLibrary ("swt-gdip"); //$NON-NLS-1$
	}
	
	/** GdiPlus constants */
	public static final int BrushTypeSolidColor = 0;
	public static final int BrushTypeHatchFill = 1;
	public static final int BrushTypeTextureFill = 2;
	public static final int BrushTypePathGradient = 3;
	public static final int BrushTypeLinearGradient = 4;
	public static final int CombineModeReplace = 0;
	public static final int CombineModeIntersect = 1;
	public static final int CombineModeUnion = 2;
	public static final int CombineModeXor = 3;
	public static final int CombineModeExclude = 4;
	public static final int CombineModeComplement = 5;
	public static final int FillModeAlternate = 0;
	public static final int FillModeWinding = 0;
	public static final int DashCapFlat = 0;
	public static final int DashCapRound = 2;
    public static final int DashCapTriangle = 3;
    public static final int DashStyleSolid = 0;
    public static final int DashStyleDash = 1;
    public static final int DashStyleDot = 2;
    public static final int DashStyleDashDot = 3;
    public static final int DashStyleDashDotDot = 4;
    public static final int DashStyleCustom = 5;
    public static final int FlushIntentionFlush = 0;
    public static final int FlushIntentionSync = 1;
    public static final int HotkeyPrefixNone = 0;
    public static final int HotkeyPrefixShow = 1;
    public static final int HotkeyPrefixHide = 2;
    public static final int LineJoinMiter = 0;
    public static final int LineJoinBevel = 1;
    public static final int LineJoinRound = 2;
    public static final int LineCapFlat = 0;
    public static final int LineCapSquare = 1;
    public static final int LineCapRound = 2;
    public static final int MatrixOrderPrepend = 0;
    public static final int MatrixOrderAppend = 1;
    public static final int QualityModeDefault = 0;
    public static final int QualityModeLow = 1;
    public static final int QualityModeHigh = 2;
    public static final int InterpolationModeDefault = QualityModeDefault;
    public static final int InterpolationModeLowQuality = QualityModeLow;
    public static final int InterpolationModeHighQuality = QualityModeHigh;
    public static final int InterpolationModeBilinear = QualityModeHigh + 1;
    public static final int InterpolationModeBicubic = QualityModeHigh + 2;
    public static final int InterpolationModeNearestNeighbor = QualityModeHigh + 3;
    public static final int InterpolationModeHighQualityBilinear = QualityModeHigh + 4;
    public static final int InterpolationModeHighQualityBicubic = QualityModeHigh + 5;
    public static final int PathPointTypeStart = 0;
    public static final int PathPointTypeLine = 1;
    public static final int PathPointTypeBezier = 3;
    public static final int PathPointTypePathTypeMask = 0x7;
    public static final int PathPointTypePathDashMode = 0x10;
    public static final int PathPointTypePathMarker = 0x20;
    public static final int PathPointTypeCloseSubpath = 0x80;
    public static final int PathPointTypeBezier3 = 3;
    public static final int PixelFormatIndexed = 0x00010000;
    public static final int PixelFormatGDI = 0x00020000;
    public static final int PixelFormatAlpha = 0x00040000;
    public static final int PixelFormatPAlpha = 0x00080000;
    public static final int PixelFormatExtended = 0x00100000;
    public static final int PixelFormatCanonical = 0x00200000;
    public static final int PixelFormat1bppIndexed = (1 | ( 1 << 8) | PixelFormatIndexed | PixelFormatGDI);
    public static final int PixelFormat4bppIndexed = (2 | ( 4 << 8) | PixelFormatIndexed | PixelFormatGDI);
    public static final int PixelFormat8bppIndexed = (3 | ( 8 << 8) | PixelFormatIndexed | PixelFormatGDI);
    public static final int PixelFormat16bppGrayScale = (4 | (16 << 8) | PixelFormatExtended);
    public static final int PixelFormat16bppRGB555 = (5 | (16 << 8) | PixelFormatGDI);
    public static final int PixelFormat16bppRGB565 = (6 | (16 << 8) | PixelFormatGDI);
    public static final int PixelFormat16bppARGB1555 = (7 | (16 << 8) | PixelFormatAlpha | PixelFormatGDI);
    public static final int PixelFormat24bppRGB = (8 | (24 << 8) | PixelFormatGDI);
    public static final int PixelFormat32bppRGB = (9 | (32 << 8) | PixelFormatGDI);
    public static final int PixelFormat32bppARGB = (10 | (32 << 8) | PixelFormatAlpha | PixelFormatGDI | PixelFormatCanonical);
    public static final int PixelFormat32bppPARGB = (11 | (32 << 8) | PixelFormatAlpha | PixelFormatPAlpha | PixelFormatGDI);
    public static final int PixelFormat48bppRGB = (12 | (48 << 8) | PixelFormatExtended);
    public static final int PixelFormat64bppARGB = (13 | (64 << 8) | PixelFormatAlpha  | PixelFormatCanonical | PixelFormatExtended);
    public static final int PixelFormat64bppPARGB = (14 | (64 << 8) | PixelFormatAlpha  | PixelFormatPAlpha | PixelFormatExtended);
    public static final int PixelFormatMax = 15;
    public static final int SmoothingModeDefault = QualityModeDefault;
    public static final int SmoothingModeHighSpeed = QualityModeLow;
    public static final int SmoothingModeHighQuality = QualityModeHigh;
    public static final int SmoothingModeNone = 3;
    public static final int SmoothingModeAntiAlias8x4 = 4;
    public static final int SmoothingModeAntiAlias = SmoothingModeAntiAlias8x4;
    public static final int SmoothingModeAntiAlias8x8 = 5;
    public static final int TextRenderingHintSystemDefault = 0;
    public static final int TextRenderingHintSingleBitPerPixelGridFit = 1;
    public static final int TextRenderingHintSingleBitPerPixel = 2;
    public static final int TextRenderingHintAntiAliasGridFit = 3;
    public static final int TextRenderingHintAntiAlias = 4;
    public static final int TextRenderingHintClearTypeGridFit = 5;
    public static final int UnitPixel = 2;
    public static final int WrapModeTile = 0;
    public static final int WrapModeTileFlipX = 1;
    public static final int WrapModeTileFlipY = 2;
    public static final int WrapModeTileFlipXY = 3;
    public static final int WrapModeClamp = 4;


/** GdiPlus natives */
public static final native int GdiplusStartup(int[] token, GdiplusStartupInput input, int output);
public static final native void GdiplusShutdown(int[] token);
public static final native int Bitmap_new(int hbm, int hpal);
public static final native int Bitmap_new(int hicon);
public static final native int Bitmap_new(int width, int height, int stride, int format, int scan0);
public static final native void Bitmap_delete(int bitmap);
public static final native int Brush_Clone(int brush);
public static final native int Brush_GetType(int brush);
public static final native int Graphics_new(int hdc);
public static final native void Graphics_delete(int graphics);
public static final native int Graphics_DrawArc(int graphics, int pen, int x, int y, int width, int height, float startAngle, float sweepAngle);
public static final native int Graphics_DrawEllipse(int graphics, int pen, int x, int y, int width, int height);
public static final native int Graphics_DrawImage(int graphics, int image, int x, int y);
public static final native int Graphics_DrawImage(int graphics, int image, Rect destRect, int srcx, int srcy, int srcwidth, int srcheight, int srcUnit, int imageAttributes, int callback, int callbackData);
public static final native int Graphics_DrawLine(int graphics, int pen, int x1, int y1, int x2, int y2);
public static final native int Graphics_DrawLines(int graphics, int pen, int[] points, int count);
public static final native int Graphics_DrawPath(int graphics, int pen, int path);
public static final native int Graphics_DrawPolygon(int graphics, int pen, int[] points, int count);
public static final native int Graphics_DrawRectangle(int graphics, int pen, int x, int y, int width, int height);
public static final native int Graphics_DrawString(int graphics, char[] string, int length, int font, PointF origin, int brush);
public static final native int Graphics_DrawString(int graphics, char[] string, int length, int font, PointF origin, int format, int brush);
public static final native int Graphics_FillEllipse(int graphics, int brush, int x, int y, int width, int height);
public static final native int Graphics_FillPath(int graphics, int brush, int path);
public static final native void Graphics_Flush(int graphics, int intention);
public static final native int Graphics_FillPie(int graphics, int brush, int x, int y, int width, int height, float startAngle, float sweepAngle);
public static final native int Graphics_FillPolygon(int graphics, int brush, int[] points, int count, int fillMode);
public static final native int Graphics_FillRectangle(int graphics, int brush, int x, int y, int width, int height);
public static final native int Graphics_GetClipBounds(int graphics, RectF rect);
public static final native int Graphics_GetClipBounds(int graphics, Rect rect);
public static final native int Graphics_GetClip(int graphics, int region);
public static final native int Graphics_GetHDC(int graphics);
public static final native void Graphics_ReleaseHDC(int graphics, int hdc);
public static final native int Graphics_GetInterpolationMode(int graphics);
public static final native int Graphics_GetSmoothingMode(int graphics);
public static final native int Graphics_GetTextRenderingHint(int graphics);
public static final native int Graphics_GetTransform(int graphics, int matrix);
public static final native int Graphics_MeasureString(int graphics, char[] string, int length, int font, PointF origin, RectF boundingBox);
public static final native int Graphics_MeasureString(int graphics, char[] string, int length, int font, PointF origin, int format, RectF boundingBox);
public static final native int Graphics_ResetClip(int graphics);
public static final native int Graphics_SetClip(int graphics, int hrgn, int combineMode);
public static final native int Graphics_SetClip(int graphics, int path);
public static final native int Graphics_SetClip(int graphics, RectF rect);
public static final native int Graphics_SetSmoothingMode(int graphics, int smoothingMode);
public static final native int Graphics_SetTransform(int graphics, int matrix);
public static final native int Graphics_SetInterpolationMode(int graphics, int mode);
public static final native int Graphics_SetTextRenderingHint(int graphics, int mode);
public static final native int Color_new(int argb);
public static final native void Color_delete(int color);
public static final native int Font_new(int hdc, int hfont);
public static final native void Font_delete(int font);
public static final native int Font_GetFamily(int font, int family);
public static final native float Font_GetSize(int font);
public static final native int Font_GetStyle(int font);
public static final native int FontFamily_new();
public static final native void FontFamily_delete(int family);
public static final native int LinearGradientBrush_new(PointF point1, PointF point2, int color1, int color2);
public static final native void LinearGradientBrush_delete(int brush);
public static final native int LinearGradientBrush_SetWrapMode(int brush, int wrapMode);
public static final native int SolidBrush_new(int color);
public static final native void SolidBrush_delete(int pen);
public static final native int Pen_new(int color, float width);
public static final native void Pen_delete(int pen);
public static final native int Pen_GetBrush(int pen);
public static final native int Pen_SetBrush(int pen, int brush);
public static final native int Pen_SetDashPattern(int pen, float[] dashArray, int count);
public static final native int Pen_SetDashStyle(int pen, int dashStyle);
public static final native int Pen_SetLineCap(int pen, int startCap, int endCap, int dashCap);
public static final native int Pen_SetLineJoin(int pen, int lineJoin);
public static final native int Point_new(int x, int y);
public static final native void Point_delete(int point);
public static final native int GraphicsPath_new(int brushMode);
public static final native void GraphicsPath_delete(int path);
public static final native int GraphicsPath_AddArc(int path, float x, float y, float width, float height, float startAngle, float sweepAngle);
public static final native int GraphicsPath_AddBezier(int path, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);
public static final native int GraphicsPath_AddLine(int path, float x1, float y1, float x2, float y2);
public static final native int GraphicsPath_AddPath(int path, int addingPath, boolean connect);
public static final native int GraphicsPath_AddRectangle(int path, RectF rect);
public static final native int GraphicsPath_AddString(int path, char[] string, int length, int family, int style, float emSize, PointF origin, int format);
public static final native int GraphicsPath_CloseFigure(int path);
public static final native int GraphicsPath_GetBounds(int path, RectF bounds, int matrix, int pen);
public static final native int GraphicsPath_GetLastPoint(int path, PointF lastPoint);
public static final native int GraphicsPath_GetPathPoints(int path, float[] points, int count);
public static final native int GraphicsPath_GetPathTypes(int path, byte[] types, int count);
public static final native int GraphicsPath_GetPointCount(int path);
public static final native boolean GraphicsPath_IsOutlineVisible(int path, float x, float y, int pen, int g);
public static final native boolean GraphicsPath_IsVisible(int path, float x, float y, int g);
public static final native int GraphicsPath_SetFillMode(int path, int fillmode);
public static final native int HatchBrush_new(int hatchStyle, int foreColor, int backColor);
public static final native int Image_GetWidth(int image);
public static final native int Image_GetHeight(int image);
public static final native void HatchBrush_delete(int brush);
public static final native int Matrix_new(float m11, float m12, float m21, float m22, float dx, float dy);
public static final native void Matrix_delete(int matrix);
public static final native int Matrix_GetElements(int matrix, float[] m);
public static final native int Matrix_Invert(int matrix);
public static final native boolean Matrix_IsIdentity(int metrix);
public static final native int Matrix_Multiply(int matrix, int matrix1, int order);
public static final native int Matrix_Rotate(int matrix, float angle, int order);
public static final native int Matrix_Scale(int matrix, float scaleX, float scaleY, int order);
public static final native int Matrix_Shear(int matrix, float shearX, float shearY, int order);
public static final native int Matrix_TransformPoints(int matrix, PointF pts, int count);
public static final native int Matrix_TransformPoints(int matrix, float[] pts, int count);
public static final native int Matrix_Translate(int matrix, float offsetX, float offsetY, int order);
public static final native int Matrix_SetElements(int matrix, float m11, float m12, float m21, float m22, float dx, float dy);
public static final native int PathGradientBrush_new(int path);
public static final native void PathGradientBrush_delete(int brush);
public static final native int PathGradientBrush_SetCenterColor(int brush, int color);
public static final native int PathGradientBrush_SetCenterPoint(int brush, PointF pt);
public static final native int PathGradientBrush_SetSurroundColors(int brush, int[] colors, int[] count);
public static final native int PathGradientBrush_SetGraphicsPath(int brush, int path);
public static final native int Region_new(int hRgn);
public static final native int Region_new();
public static final native void Region_delete(int region);
public static final native int Region_GetHRGN(int region, int graphics);
public static final native void StringFormat_delete(int format);
public static final native int StringFormat_Clone(int format);
public static final native int StringFormat_GenericDefault();
public static final native int StringFormat_GenericTypographic();
public static final native int StringFormat_SetHotkeyPrefix(int format, int hotkeyPrefix);
public static final native int StringFormat_SetTabStops(int format, float firstTabOffset, int count, float[] tabStops);
public static final native int TextureBrush_new(int image, int wrapMode, float dstX, float dstY, float dstWidth, float dstHeight);
public static final native void TextureBrush_delete(int brush);
public static final native void TextureBrush_SetTransform(int brush, int matrix);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5764.java