error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17897.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17897.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17897.java
text:
```scala
i@@f (area.width <= 1 || area.height <= 1) return;

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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides the layout for SashForm
 * 
 * @see SashForm
 */
class SashFormLayout extends Layout {
protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	SashForm sashForm = (SashForm)composite;
	Control[] cArray = sashForm.getControls(true);
	int width = 0;
	int height = 0;
	if (cArray.length == 0) {		
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		return new Point(width, height);
	}
	// determine control sizes
	boolean vertical = sashForm.getOrientation() == SWT.VERTICAL;
	int maxIndex = 0;
	int maxValue = 0;
	for (int i = 0; i < cArray.length; i++) {
		if (vertical) {
			Point size = cArray[i].computeSize(wHint, SWT.DEFAULT, flushCache);
			if (size.y > maxValue) {
				maxIndex = i;
				maxValue = size.y;
			}
			width = Math.max(width, size.x);
		} else {
			Point size = cArray[i].computeSize(SWT.DEFAULT, hHint, flushCache);
			if (size.x > maxValue) {
				maxIndex = i;
				maxValue = size.x;
			}
			height = Math.max(height, size.y);
		}
	}
	// get the ratios
	long[] ratios = new long[cArray.length];
	long total = 0;
	for (int i = 0; i < cArray.length; i++) {
		Object data = cArray[i].getLayoutData();
		if (data != null && data instanceof SashFormData) {
			ratios[i] = ((SashFormData)data).weight;
		} else {
			data = new SashFormData();
			cArray[i].setLayoutData(data);
			((SashFormData)data).weight = ratios[i] = ((200 << 16) + 999) / 1000;
			
		}
		total += ratios[i];
	}
	int sashwidth = sashForm.sashes.length > 0 ? sashForm.SASH_WIDTH + sashForm.sashes [0].getBorderWidth() * 2 : sashForm.SASH_WIDTH;
	if (vertical) {
		height += (int)(total * maxValue / ratios[maxIndex]) + (cArray.length - 1) * sashwidth;
	} else {
		width += (int)(total * maxValue / ratios[maxIndex]) + (cArray.length - 1) * sashwidth;
	}
	width += sashForm.getBorderWidth()*2;
	height += sashForm.getBorderWidth()*2;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point(width, height);
}

protected boolean flushCache(Control control) {
	return true;
}

protected void layout(Composite composite, boolean flushCache) {
	SashForm sashForm = (SashForm)composite;
	Rectangle area = sashForm.getClientArea();
	if (area.width == 0 || area.height == 0) return;
	
	Control[] newControls = sashForm.getControls(true);
	if (sashForm.controls.length == 0 && newControls.length == 0) return;
	sashForm.controls = newControls;
	
	Control[] controls = sashForm.controls;
	
	if (sashForm.maxControl != null && !sashForm.maxControl.isDisposed()) {
		for (int i= 0; i < controls.length; i++){
			if (controls[i] != sashForm.maxControl) {
				controls[i].setBounds(-200, -200, 0, 0);
			} else {
				controls[i].setBounds(area);
			}
		}
		return;
	}
	
	// keep just the right number of sashes
	if (sashForm.sashes.length < controls.length - 1) {
		Sash[] newSashes = new Sash[controls.length - 1];
		System.arraycopy(sashForm.sashes, 0, newSashes, 0, sashForm.sashes.length);
		for (int i = sashForm.sashes.length; i < newSashes.length; i++) {
			newSashes[i] = new Sash(sashForm, sashForm.sashStyle);
			newSashes[i].setBackground(sashForm.background);
			newSashes[i].setForeground(sashForm.foreground);
			newSashes[i].addListener(SWT.Selection, sashForm.sashListener);
		}
		sashForm.sashes = newSashes;
	}
	if (sashForm.sashes.length > controls.length - 1) {
		if (controls.length == 0) {
			for (int i = 0; i < sashForm.sashes.length; i++) {
				sashForm.sashes[i].dispose();
			}
			sashForm.sashes = new Sash[0];
		} else {
			Sash[] newSashes = new Sash[controls.length - 1];
			System.arraycopy(sashForm.sashes, 0, newSashes, 0, newSashes.length);
			for (int i = controls.length - 1; i < sashForm.sashes.length; i++) {
				sashForm.sashes[i].dispose();
			}
			sashForm.sashes = newSashes;
		}
	}
	if (controls.length == 0) return;
	Sash[] sashes = sashForm.sashes;
	// get the ratios
	long[] ratios = new long[controls.length];
	long total = 0;
	for (int i = 0; i < controls.length; i++) {
		Object data = controls[i].getLayoutData();
		if (data != null && data instanceof SashFormData) {
			ratios[i] = ((SashFormData)data).weight;
		} else {
			data = new SashFormData();
			controls[i].setLayoutData(data);
			((SashFormData)data).weight = ratios[i] = ((200 << 16) + 999) / 1000;
			
		}
		total += ratios[i];
	}
	
	int sashwidth = sashes.length > 0 ? sashForm.SASH_WIDTH + sashes [0].getBorderWidth() * 2 : sashForm.SASH_WIDTH;
	if (sashForm.getOrientation() == SWT.HORIZONTAL) {
		int width = (int)(ratios[0] * (area.width - sashes.length * sashwidth) / total);
		int x = area.x;
		controls[0].setBounds(x, area.y, width, area.height);
		x += width;
		for (int i = 1; i < controls.length - 1; i++) {
			sashes[i - 1].setBounds(x, area.y, sashwidth, area.height);
			x += sashwidth;
			width = (int)(ratios[i] * (area.width - sashes.length * sashwidth) / total);
			controls[i].setBounds(x, area.y, width, area.height);
			x += width;
		}
		if (controls.length > 1) {
			sashes[sashes.length - 1].setBounds(x, area.y, sashwidth, area.height);
			x += sashwidth;
			width = area.width - x;
			controls[controls.length - 1].setBounds(x, area.y, width, area.height);
		}
	} else {
		int height = (int)(ratios[0] * (area.height - sashes.length * sashwidth) / total);
		int y = area.y;
		controls[0].setBounds(area.x, y, area.width, height);
		y += height;
		for (int i = 1; i < controls.length - 1; i++) {
			sashes[i - 1].setBounds(area.x, y, area.width, sashwidth);
			y += sashwidth;
			height = (int)(ratios[i] * (area.height - sashes.length * sashwidth) / total);
			controls[i].setBounds(area.x, y, area.width, height);
			y += height;
		}
		if (controls.length > 1) {
			sashes[sashes.length - 1].setBounds(area.x, y, area.width, sashwidth);
			y += sashwidth;
			height = area.height - y;
			controls[controls.length - 1].setBounds(area.x, y, area.width, height);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17897.java