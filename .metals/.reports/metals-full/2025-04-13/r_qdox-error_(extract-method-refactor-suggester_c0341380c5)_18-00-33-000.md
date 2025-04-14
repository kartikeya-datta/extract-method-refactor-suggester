error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9262.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9262.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9262.java
text:
```scala
r@@eturn Integer.valueOf(calculateInternal(lu, parentStyles, styles));

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

import org.w3c.css.sac.LexicalUnit;

/**
 * The CSS font-weight property.
 */
public class FontWeightProperty extends AbstractProperty {

	private static final int FONT_WEIGHT_NORMAL = 400;
	private static final int FONT_WEIGHT_BOLD = 700;

	/**
	 * Class constructor.
	 */
	public FontWeightProperty() {
		super(CSS.FONT_WEIGHT);
	}

	public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {
		return new Integer(this.calculateInternal(lu, parentStyles, styles));
	}

	public int calculateInternal(LexicalUnit lu, Styles parentStyles,
			Styles styles) {
		if (isFontWeight(lu)) {
			return getFontWeight(lu, parentStyles);
		} else {
			// not specified, "inherit", or some other value
			if (parentStyles != null) {
				return parentStyles.getFontWeight();
			} else {
				return FONT_WEIGHT_NORMAL;
			}
		}

	}

	/**
	 * Returns true if the given lexical unit represents a font weight.
	 * 
	 * @param lu
	 *            LexicalUnit to check.
	 */
	public static boolean isFontWeight(LexicalUnit lu) {
		if (lu == null) {
			return false;
		} else if (lu.getLexicalUnitType() == LexicalUnit.SAC_INTEGER) {
			return true;
		} else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
			String s = lu.getStringValue();
			return s.equals(CSS.NORMAL) || s.equals(CSS.BOLD)
 s.equals(CSS.BOLDER) || s.equals(CSS.LIGHTER);
		} else {
			return false;
		}
	}

	private static int getFontWeight(LexicalUnit lu, Styles parentStyles) {
		if (lu == null) {
			return FONT_WEIGHT_NORMAL;
		} else if (lu.getLexicalUnitType() == LexicalUnit.SAC_INTEGER) {
			return lu.getIntegerValue();
		} else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
			String s = lu.getStringValue();
			if (s.equals(CSS.NORMAL)) {
				return FONT_WEIGHT_NORMAL;
			} else if (s.equals(CSS.BOLD)) {
				return FONT_WEIGHT_BOLD;
			} else if (s.equals(CSS.BOLDER)) {
				if (parentStyles != null) {
					return parentStyles.getFontWeight() + 151;
				} else {
					return FONT_WEIGHT_BOLD;
				}
			} else if (s.equals(CSS.LIGHTER)) {
				if (parentStyles != null) {
					return parentStyles.getFontWeight() - 151;
				} else {
					return FONT_WEIGHT_NORMAL;
				}
			} else {
				return FONT_WEIGHT_NORMAL;
			}
		} else {
			return FONT_WEIGHT_NORMAL;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9262.java