error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5921.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5921.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5921.java
text:
```scala
c@@ontinue;

/*******************************************************************************
 * Copyright (c) 2002 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v0.5
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 * IBM - Initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.internal;

import java.util.Arrays;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.*;

/**
 * An DecoratorOverlayIcon consists of a main icon and several adornments.
 */
class DecoratorOverlayIcon extends CompositeImageDescriptor {
	// the base image
	private Image base;
	// the overlay images
	private ImageDescriptor[] overlays;
	// the size
	private Point size;

	public static final int TOP_LEFT = 0;
	public static final int TOP_RIGHT = 1;
	public static final int BOTTOM_LEFT = 2;
	public static final int BOTTOM_RIGHT = 3;

	/**
	 * OverlayIcon constructor.
	 * 
	 * @param base the base image
	 * @param overlays the overlay images
	 * @param locations the location of each image
	 * @param size the size
	 */
	public DecoratorOverlayIcon(
		Image base,
		ImageDescriptor[] overlays,
		Point size) {
		this.base = base;
		this.overlays = overlays;
		this.size = size;
	}
	/**
	 * Draw the overlays for the reciever.
	 */
	protected void drawOverlays(ImageDescriptor[] overlays) {
		Point size = getSize();
		for (int i = 0; i < overlays.length; i++) {
			ImageDescriptor overlay = overlays[i];
			if (overlay == null)
				break;
			ImageData overlayData = overlay.getImageData();
			switch (i) {
				case TOP_LEFT :
					drawImage(overlayData, 0, 0);
					break;
				case TOP_RIGHT :
					drawImage(overlayData, size.x - overlayData.width, 0);
					break;
				case BOTTOM_LEFT :
					drawImage(overlayData, 0, size.y - overlayData.height);
					break;
				case BOTTOM_RIGHT :
					drawImage(
						overlayData,
						size.x - overlayData.width,
						size.y - overlayData.height);
					break;
			}
		}
	}

	public boolean equals(Object o) {
		if (!(o instanceof DecoratorOverlayIcon))
			return false;
		DecoratorOverlayIcon other = (DecoratorOverlayIcon) o;
		return base.equals(other.base)
			&& Arrays.equals(overlays, other.overlays);
	}

	public int hashCode() {
		int code = base.hashCode();
		for (int i = 0; i < overlays.length; i++) {
			if(overlays[i] != null)	
				code ^= overlays[i].hashCode();
		}
		return code;
	}

	protected void drawCompositeImage(int width, int height) {
		drawImage(base.getImageData(), 0, 0);
		drawOverlays(overlays);
	}

	protected Point getSize() {
		return size;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5921.java