error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5891.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5891.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5891.java
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

public class ProgressBarDrawData extends RangeDrawData {

public ProgressBarDrawData() {
	state = new int[1];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int hTheme = OS.OpenThemeData(0, getClassId());
		RECT rect = new RECT ();
		rect.left = bounds.x;
		rect.right = rect.left + bounds.width;
		rect.top = bounds.y;
		rect.bottom = rect.top + bounds.height;
		int[] buffer = new int[1];
		OS.GetThemeInt(hTheme, 0, 0, OS.PROGRESSCHUNKSIZE, buffer);
		int chunkSize = buffer[0];
		OS.GetThemeInt(hTheme, 0, 0, OS.PROGRESSSPACESIZE, buffer);
		int spaceSize = buffer[0];		
		RECT content = new RECT();
		int[] part = getPartId(DrawData.WIDGET_WHOLE);
		if ((style & SWT.VERTICAL) != 0) {
			OS.GetThemeBackgroundContentRect(hTheme, gc.handle, part[0], part[1], rect, content);
			OS.DrawThemeBackground(hTheme, gc.handle, part[0], part[1], rect, null);
			int top = content.bottom - (((content.bottom - content.top) * (selection - minimum)) / (maximum - minimum));
			content.top = content.bottom - chunkSize;
			while (content.top >= top) {
				OS.DrawThemeBackground(hTheme, gc.handle, OS.PP_CHUNKVERT, 0, content, null);
				content.bottom -= chunkSize + spaceSize;
				content.top = content.bottom - chunkSize;
			}
			if (selection != 0) {
				OS.DrawThemeBackground(hTheme, gc.handle, OS.PP_CHUNKVERT, 0, content, null);
			}
		} else {
			OS.GetThemeBackgroundContentRect(hTheme, gc.handle, part[0], part[1], rect, content);
			OS.DrawThemeBackground(hTheme, gc.handle, part[0], part[1], rect, null);
			int right = content.left + (((content.right - content.left) * (selection - minimum)) / (maximum - minimum));
			content.right = content.left + chunkSize;
			while (content.right <= right) {
				OS.DrawThemeBackground(hTheme, gc.handle, OS.PP_CHUNK, 0, content, null);
				content.left += chunkSize + spaceSize;
				content.right = content.left + chunkSize;
			}
			if (selection != 0) {
				OS.DrawThemeBackground(hTheme, gc.handle, OS.PP_CHUNK, 0, content, null);				
			}
		}
		OS.CloseThemeData (hTheme);
	}
}

char[] getClassId() {
	return PROGRESS;
}

int[] getPartId(int part) {
	int iPartId = 0, iStateId = 0;	
	if ((style & SWT.VERTICAL) != 0) {
		iPartId = OS.PP_BARVERT;
	} else {
		iPartId = OS.PP_BAR;
	}
	return new int[]{iPartId, iStateId};	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5891.java