error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5892.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5892.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5892.java
text:
```scala
i@@nt /*long*/ hTheme = OS.OpenThemeData(0, getClassId());

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.theme;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

public class ScaleDrawData extends RangeDrawData {
	public int increment;
	public int pageIncrement;
	
	static final int TICS_MARGIN = 10;

public ScaleDrawData() {
	state = new int[4];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		// TODO - drawScale not done
		int style = this.style;
		int minimum = this.minimum;
		int maximum = this.maximum;
		int selection = this.selection;
		int pageIncrement = this.pageIncrement;
		int hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = rect.left + bounds.width;
		rect.top = bounds.y;
		rect.bottom = rect.top + bounds.height;
		SIZE size = new SIZE();
		if ((style & SWT.VERTICAL) != 0) {
			OS.GetThemePartSize(hTheme, gc.handle, OS.TKP_TRACKVERT, 0, null, OS.TS_TRUE, size);
			int trackWidth = size.cx - 1;
			OS.GetThemePartSize(hTheme, gc.handle, OS.TKP_THUMBVERT, 0, null, OS.TS_TRUE, size);
			int thumbWidth = size.cx, thumbHeight = size.cy;
			OS.GetThemePartSize(hTheme, gc.handle, OS.TKP_TICS, 0, rect, OS.TS_TRUE, size);
			int ticWidth = size.cx;
			int marginX = (thumbWidth - trackWidth) / 2;
			int marginY = marginX;
			marginX += TICS_MARGIN;
			rect.left += marginX;
			rect.top += marginY;
			rect.right = rect.left + trackWidth;
			rect.bottom -= marginY;
			int trackHeight = rect.bottom - rect.top;
			OS.DrawThemeBackground(hTheme, gc.handle, OS.TKP_TRACKVERT, 0, rect, null);
			rect.top += ((trackHeight - thumbHeight) * (selection - minimum)) / Math.max(1, maximum - minimum);
			rect.left -= (thumbWidth - trackWidth) / 2;
			rect.right = rect.left + thumbWidth;
			rect.bottom = rect.top + thumbHeight;
			OS.DrawThemeBackground(hTheme, gc.handle, OS.TKP_THUMBVERT, 0, rect, null);
			rect.top = bounds.y + marginY + thumbHeight / 2;
			rect.bottom = rect.top + 1;
			for (int sel = minimum; sel <= maximum; sel += pageIncrement) {
				rect.left = bounds.x + TICS_MARGIN / 2;
				rect.right = rect.left + ticWidth;
				if (sel != minimum && sel != maximum) rect.left++;
				rect.top = bounds.y + marginY + thumbHeight / 2;
				rect.top += ((trackHeight - thumbHeight) * (sel - minimum)) / Math.max(1, maximum - minimum);
				rect.bottom = rect.top + 1;
				//TODO - why tics are ot drawn
				OS.DrawThemeBackground(hTheme, gc.handle, OS.TKP_TICSVERT, 1, rect, null);
				gc.drawLine(rect.left, rect.top, rect.right, rect.top);
				rect.left = bounds.x + TICS_MARGIN + thumbWidth + 1;
				rect.right = rect.left + ticWidth;
				if (sel != minimum && sel != maximum) rect.right--;
				//TODO - why tics are ot drawn
				OS.DrawThemeBackground(hTheme, gc.handle, OS.TKP_TICSVERT, 1, rect, null);
				gc.drawLine(rect.left, rect.top, rect.right, rect.top);
			}
		} else {

		}
		OS.CloseThemeData (hTheme);
	}
}

char[] getClassId() {
	return TRACKBAR;
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5892.java