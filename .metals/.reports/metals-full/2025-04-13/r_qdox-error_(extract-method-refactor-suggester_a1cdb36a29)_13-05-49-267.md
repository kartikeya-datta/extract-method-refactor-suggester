error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4911.java
text:
```scala
public i@@nt /*long*/ rectArrayForCharacterRange(NSRange charRange, NSRange selCharRange, NSTextContainer container, int[] /*long[]*/ rectCount) {

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSLayoutManager extends NSObject {

public NSLayoutManager() {
	super();
}

public NSLayoutManager(int /*long*/ id) {
	super(id);
}

public NSLayoutManager(id id) {
	super(id);
}

public void addTemporaryAttribute(NSString attrName, id value, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_addTemporaryAttribute_value_forCharacterRange_, attrName != null ? attrName.id : 0, value != null ? value.id : 0, charRange);
}

public void addTextContainer(NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_addTextContainer_, container != null ? container.id : 0);
}

public NSRect boundingRectForGlyphRange(NSRange glyphRange, NSTextContainer container) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingRectForGlyphRange_inTextContainer_, glyphRange, container != null ? container.id : 0);
	return result;
}

public int /*long*/ characterIndexForGlyphAtIndex(int /*long*/ glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_characterIndexForGlyphAtIndex_, glyphIndex);
}

public float /*double*/ defaultBaselineOffsetForFont(NSFont theFont) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_defaultBaselineOffsetForFont_, theFont != null ? theFont.id : 0);
}

public float /*double*/ defaultLineHeightForFont(NSFont theFont) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_defaultLineHeightForFont_, theFont != null ? theFont.id : 0);
}

public void drawBackgroundForGlyphRange(NSRange glyphsToShow, NSPoint origin) {
	OS.objc_msgSend(this.id, OS.sel_drawBackgroundForGlyphRange_atPoint_, glyphsToShow, origin);
}

public void drawGlyphsForGlyphRange(NSRange glyphsToShow, NSPoint origin) {
	OS.objc_msgSend(this.id, OS.sel_drawGlyphsForGlyphRange_atPoint_, glyphsToShow, origin);
}

public int /*long*/ getGlyphs(int /*long*/ glyphArray, NSRange glyphRange) {
	return OS.objc_msgSend(this.id, OS.sel_getGlyphs_range_, glyphArray, glyphRange);
}

public int /*long*/ getGlyphsInRange(NSRange glyphRange, int /*long*/ glyphBuffer, int /*long*/ charIndexBuffer, int /*long*/ inscribeBuffer, int /*long*/ elasticBuffer, byte[] bidiLevelBuffer) {
	return OS.objc_msgSend(this.id, OS.sel_getGlyphsInRange_glyphs_characterIndexes_glyphInscriptions_elasticBits_bidiLevels_, glyphRange, glyphBuffer, charIndexBuffer, inscribeBuffer, elasticBuffer, bidiLevelBuffer);
}

public int /*long*/ glyphIndexForCharacterAtIndex(int /*long*/ charIndex) {
	return OS.objc_msgSend(this.id, OS.sel_glyphIndexForCharacterAtIndex_, charIndex);
}

public int /*long*/ glyphIndexForPoint(NSPoint point, NSTextContainer container, float[] /*double[]*/ partialFraction) {
	return OS.objc_msgSend(this.id, OS.sel_glyphIndexForPoint_inTextContainer_fractionOfDistanceThroughGlyph_, point, container != null ? container.id : 0, partialFraction);
}

public NSRange glyphRangeForCharacterRange(NSRange charRange, int /*long*/ actualCharRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_glyphRangeForCharacterRange_actualCharacterRange_, charRange, actualCharRange);
	return result;
}

public NSRange glyphRangeForTextContainer(NSTextContainer container) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_glyphRangeForTextContainer_, container != null ? container.id : 0);
	return result;
}

public NSRect lineFragmentUsedRectForGlyphAtIndex(int /*long*/ glyphIndex, int /*long*/ effectiveGlyphRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_, glyphIndex, effectiveGlyphRange);
	return result;
}

public NSRect lineFragmentUsedRectForGlyphAtIndex(int /*long*/ glyphIndex, int /*long*/ effectiveGlyphRange, boolean flag) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_withoutAdditionalLayout_, glyphIndex, effectiveGlyphRange, flag);
	return result;
}

public NSPoint locationForGlyphAtIndex(int /*long*/ glyphIndex) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_locationForGlyphAtIndex_, glyphIndex);
	return result;
}

public int /*long*/ numberOfGlyphs() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfGlyphs);
}

public int /*long*/ rectArrayForCharacterRange(NSRange charRange, NSRange selCharRange, NSTextContainer container, int /*long*/ rectCount) {
	return OS.objc_msgSend(this.id, OS.sel_rectArrayForCharacterRange_withinSelectedCharacterRange_inTextContainer_rectCount_, charRange, selCharRange, container != null ? container.id : 0, rectCount);
}

public int /*long*/ rectArrayForGlyphRange(NSRange glyphRange, NSRange selGlyphRange, NSTextContainer container, int[] /*long[]*/ rectCount) {
	return OS.objc_msgSend(this.id, OS.sel_rectArrayForGlyphRange_withinSelectedGlyphRange_inTextContainer_rectCount_, glyphRange, selGlyphRange, container != null ? container.id : 0, rectCount);
}

public void removeTemporaryAttribute(NSString attrName, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_removeTemporaryAttribute_forCharacterRange_, attrName != null ? attrName.id : 0, charRange);
}

public void setBackgroundLayoutEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundLayoutEnabled_, flag);
}

public void setLineFragmentRect(NSRect fragmentRect, NSRange glyphRange, NSRect usedRect) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentRect_forGlyphRange_usedRect_, fragmentRect, glyphRange, usedRect);
}

public void setTextStorage(NSTextStorage textStorage) {
	OS.objc_msgSend(this.id, OS.sel_setTextStorage_, textStorage != null ? textStorage.id : 0);
}

public void setUsesScreenFonts(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesScreenFonts_, flag);
}

public NSTypesetter typesetter() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_typesetter);
	return result != 0 ? new NSTypesetter(result) : null;
}

public NSRect usedRectForTextContainer(NSTextContainer container) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_usedRectForTextContainer_, container != null ? container.id : 0);
	return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4911.java