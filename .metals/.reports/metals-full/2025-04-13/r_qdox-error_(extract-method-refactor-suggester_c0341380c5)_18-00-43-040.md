error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/243.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/243.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/243.java
text:
```scala
s@@etBackgroundColor(result.getBackgroundColor());

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
package org.eclipse.ui.internal.decorators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * The Decoration builder is the object that builds a decoration.
 */
class DecorationBuilder implements IDecoration {

	private static int DECORATOR_ARRAY_SIZE = 5;

	private List prefixes = new ArrayList();

	private List suffixes = new ArrayList();

	private ImageDescriptor[] descriptors = new ImageDescriptor[DECORATOR_ARRAY_SIZE];

	private Color foregroundColor;

	private Color backgroundColor;

	private Font font;

	LightweightDecoratorDefinition currentDefinition;

	//A flag set if a value has been added
	private boolean valueSet = false;

	/**
	 * Default constructor.
	 */
	DecorationBuilder() {
		//Nothing to initialize
	}

	/**
	 * Set the value of the definition we are currently working on.
	 * 
	 * @param definition
	 */
	void setCurrentDefinition(LightweightDecoratorDefinition definition) {
		this.currentDefinition = definition;
	}

	/**
	 * @see org.eclipse.jface.viewers.IDecoration#addOverlay(org.eclipse.jface.resource.ImageDescriptor)
	 */
	public void addOverlay(ImageDescriptor overlay) {
		int quadrant = currentDefinition.getQuadrant();
		if (descriptors[quadrant] == null)
			descriptors[quadrant] = overlay;
		valueSet = true;
	}

	/**
	 * @see org.eclipse.jface.viewers.IDecoration#addOverlay(org.eclipse.jface.resource.ImageDescriptor)
	 */
	public void addOverlay(ImageDescriptor overlay, int quadrant) {
		if (quadrant >= 0 && quadrant <= DECORATOR_ARRAY_SIZE) {
			if (descriptors[quadrant] == null)
				descriptors[quadrant] = overlay;
			valueSet = true;
		} else {
			WorkbenchPlugin
					.log("Unable to apply decoration for " + currentDefinition.getId() + " invalid quadrant: " + quadrant); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.IDecoration#addPrefix(java.lang.String)
	 */
	public void addPrefix(String prefixString) {
		prefixes.add(prefixString);
		valueSet = true;
	}

	/**
	 * @see org.eclipse.jface.viewers.IDecoration#addSuffix(java.lang.String)
	 */
	public void addSuffix(String suffixString) {
		suffixes.add(suffixString);
		valueSet = true;
	}

	/**
	 * Clear the current values and return a DecorationResult.
	 * @return DecorationResult
	 */
	DecorationResult createResult() {
		DecorationResult newResult = new DecorationResult(new ArrayList(
				prefixes), new ArrayList(suffixes), descriptors,
				foregroundColor, backgroundColor, font);

		return newResult;
	}

	/**
	 * Clear the contents of the result so it can be reused.
	 */
	void clearContents() {
		this.prefixes.clear();
		this.suffixes.clear();
		this.descriptors = new ImageDescriptor[DECORATOR_ARRAY_SIZE];
		valueSet = false;
	}

	/**
	 * Return whether or not a value has been set.
	 * 
	 * @return boolean
	 */
	boolean hasValue() {
		return valueSet;
	}

	/**
	 * Apply the previously calculates result to the receiver.
	 * 
	 * @param result
	 */
	void applyResult(DecorationResult result) {
		prefixes.addAll(result.getPrefixes());
		suffixes.addAll(result.getSuffixes());
		ImageDescriptor[] resultDescriptors = result.getDescriptors();
		if (resultDescriptors != null) {
			for (int i = 0; i < descriptors.length; i++) {
				if (resultDescriptors[i] != null)
					descriptors[i] = resultDescriptors[i];
			}
		}
		
		setForegroundColor(result.getForegroundColor());
		setBackgroundColor(result.getForegroundColor());
		setFont(result.getFont());
		valueSet = true;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IDecoration#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	
	public void setBackgroundColor(Color bgColor) {
		this.backgroundColor = bgColor;
		valueSet = true;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IDecoration#setFont(org.eclipse.swt.graphics.Font)
	 */
	public void setFont(Font newFont) {
		this.font = newFont;
		valueSet = true;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IDecoration#setForegroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setForegroundColor(Color fgColor) {
		this.foregroundColor = fgColor;
		valueSet = true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/243.java