error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9260.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9260.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9260.java
text:
```scala
private static M@@ap<String, Color> colorNames = new HashMap<String, Color>();

/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.vex.core.internal.css;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.xml.vex.core.internal.core.Color;
import org.w3c.css.sac.LexicalUnit;

/**
 * Color-valued properties.
 */
public class ColorProperty extends AbstractProperty {

	private static Map colorNames = new HashMap();

	static {
		colorNames.put(CSS.AQUA, new Color(0, 255, 255));
		colorNames.put(CSS.BLACK, new Color(0, 0, 0));
		colorNames.put(CSS.BLUE, new Color(0, 0, 255));
		colorNames.put(CSS.FUCHSIA, new Color(255, 0, 255));
		colorNames.put(CSS.GRAY, new Color(128, 128, 128));
		colorNames.put(CSS.GREEN, new Color(0, 128, 0));
		colorNames.put(CSS.LIME, new Color(0, 255, 0));
		colorNames.put(CSS.MAROON, new Color(128, 0, 0));
		colorNames.put(CSS.NAVY, new Color(0, 0, 128));
		colorNames.put(CSS.OLIVE, new Color(128, 128, 0));
		colorNames.put(CSS.ORANGE, new Color(255, 165, 0));
		colorNames.put(CSS.PURPLE, new Color(128, 0, 128));
		colorNames.put(CSS.RED, new Color(255, 0, 0));
		colorNames.put(CSS.SILVER, new Color(192, 192, 192));
		colorNames.put(CSS.TEAL, new Color(0, 128, 128));
		colorNames.put(CSS.WHITE, new Color(255, 255, 255));
		colorNames.put(CSS.YELLOW, new Color(255, 255, 0));
	}

	/**
	 * Class constructor. The names CSS.COLOR and CSS.BACKGROUND_COLOR are
	 * treated specially, as follows.
	 * 
	 * <ul>
	 * <li>If name is CSS.COLOR, it is inherited and defaults to black.</li>
	 * <li>If name is CSS.BACKGROUND_COLOR, it is not inherited and defaults to
	 * transparent (null).</li>
	 * <li>Otherwise, it is not inherited and defaults to the current color.</li>
	 * </ul>
	 * 
	 * <p>
	 * Because of the default in the third case, the ColorProperty for CSS.COLOR
	 * must be processed before any others.
	 * </p>
	 * 
	 * @param name
	 *            Name of the element.
	 */
	public ColorProperty(String name) {
		super(name);
	}

	public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {

		boolean inherit = isInherit(lu) || this.getName().equals(CSS.COLOR);

		if (isColor(lu)) {
			return getColor(lu);
		} else if (inherit && parentStyles != null) {
			return parentStyles.get(this.getName());
		} else {
			if (this.getName().equals(CSS.COLOR)) {
				return Color.BLACK;
			} else if (this.getName().equals(CSS.BACKGROUND_COLOR)) {
				return null; // transparent
			} else {
				return styles.getColor();
			}
		}
	}

	/**
	 * Returns true if the given lexical unit represents a color.
	 * 
	 * @param lu
	 *            LexicalUnit to check.
	 */
	public static boolean isColor(LexicalUnit lu) {
		if (lu == null) {
			return false;
		} else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT
				&& colorNames.containsKey(lu.getStringValue())) {
			return true;
		} else if (lu.getLexicalUnitType() == LexicalUnit.SAC_RGBCOLOR) {
			return true;
		} else {
			return false;
		}
	}

	// ========================================================== PRIVATE

	private static Color getColor(LexicalUnit lu) {
		if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
			String s = lu.getStringValue();
			if (colorNames.containsKey(s)) {
				return (Color) colorNames.get(s);
			} else {
				return null;
			}
		} else if (lu.getLexicalUnitType() == LexicalUnit.SAC_RGBCOLOR) {
			lu = lu.getParameters();
			int red = getColorPart(lu);
			lu = lu.getNextLexicalUnit(); // gobble comma
			lu = lu.getNextLexicalUnit();
			int green = getColorPart(lu);
			lu = lu.getNextLexicalUnit(); // gobble comma
			lu = lu.getNextLexicalUnit();
			int blue = getColorPart(lu);

			if (red == -1 || green == -1 || blue == -1) {
				return null;
			} else {
				return new Color(red, green, blue);
			}
		} else {
			System.out.println("WARNING: unsupported color type: " + lu);
			return null;
		}
	}

	/**
	 * Converts one of the color channels into an int from 0-255, or -1 if
	 * there's an error.
	 */
	private static int getColorPart(LexicalUnit lu) {
		int value;
		if (lu.getLexicalUnitType() == LexicalUnit.SAC_INTEGER) {
			value = lu.getIntegerValue();
		} else if (lu.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE) {
			value = Math.round(lu.getFloatValue() * 255);
		} else {
			System.out.println("WARNING: unsupported color part: " + lu);
			return -1;
		}
		return Math.max(0, Math.min(255, value));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9260.java