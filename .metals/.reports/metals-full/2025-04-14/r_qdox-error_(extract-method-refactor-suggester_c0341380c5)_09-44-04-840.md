error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1617.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1617.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1617.java
text:
```scala
C@@olor color = (Color)colors.get(elements.nextElement());

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
package org.eclipse.swt.tests.junit;


import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.StyleRange
 *
 * @see org.eclipse.swt.custom.StyleRange
 */
public class Test_org_eclipse_swt_custom_StyleRange extends SwtTestCase {
	final static RGB RED = new RGB(255,0,0);
	final static RGB BLUE = new RGB(0,0,255);
	final static RGB GREEN = new RGB(0,255,0);
	Hashtable colors = new Hashtable();
public Test_org_eclipse_swt_custom_StyleRange(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}
private Color getColor(RGB rgb) {
	return (Color)colors.get(rgb);
}
protected void initializeColors() {
	Display display = Display.getDefault();
	colors.put(RED, new Color (display, RED));
	colors.put(GREEN, new Color (display, GREEN));
	colors.put(BLUE, new Color (display, BLUE));
}

protected void setUp() {
	initializeColors();
}

protected void tearDown() {
	Enumeration elements = colors.keys();
	while (elements.hasMoreElements()) {
		Color color = (Color)colors.get((RGB)elements.nextElement());
		color.dispose();
	}
}

public void test_Constructor() {
	StyleRange styleRange = new StyleRange();
	assertTrue(":a:", styleRange.start == 0);
	assertTrue(":a:", styleRange.length == 0);
	assertTrue(":a:", styleRange.foreground == null);
	assertTrue(":a:", styleRange.background == null);
	assertTrue(":a:", styleRange.fontStyle == SWT.NORMAL);
}

public void test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_Color() {
	StyleRange styleRange = new StyleRange(5, 10, getColor(RED), getColor(BLUE));
	assertTrue(":b:", styleRange.start == 5);
	assertTrue(":b:", styleRange.length == 10);
	assertTrue(":b:", styleRange.foreground == getColor(RED));
	assertTrue(":b:", styleRange.background == getColor(BLUE));
	assertTrue(":b:", styleRange.fontStyle == SWT.NORMAL);
}

public void test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_ColorI() {
	StyleRange styleRange = new StyleRange(5, 10, getColor(RED), getColor(BLUE), SWT.BOLD);
	assertTrue(":c:", styleRange.start == 5);
	assertTrue(":c:", styleRange.length == 10);
	assertTrue(":c:", styleRange.foreground == getColor(RED));
	assertTrue(":c:", styleRange.background == getColor(BLUE));
	assertTrue(":c:", styleRange.fontStyle == SWT.BOLD);
}

public void test_clone() {
	StyleRange styleRangeA = new StyleRange(6, 10, null, getColor(BLUE), SWT.NORMAL);
	StyleRange styleRangeB = (StyleRange)styleRangeA.clone();
	assertTrue(":g:", styleRangeA.equals(styleRangeB));
}

public void test_equalsLjava_lang_Object() {
	StyleRange styleRangeA = new StyleRange(5, 10, getColor(RED), getColor(BLUE), SWT.BOLD);
	StyleRange styleRangeB = new StyleRange(5, 10, getColor(RED), getColor(BLUE), SWT.BOLD);
	StyleRange styleRangeC = new StyleRange(5, 10, getColor(BLUE), getColor(BLUE), SWT.BOLD);
	StyleRange styleRangeD = new StyleRange(6, 10, getColor(RED), getColor(BLUE), SWT.BOLD);
	StyleRange styleRangeE = new StyleRange(5, 11, getColor(RED), getColor(BLUE), SWT.BOLD);
	StyleRange styleRangeF = new StyleRange(5, 11, getColor(RED), getColor(RED), SWT.BOLD);
	StyleRange styleRangeG = new StyleRange(5, 11, getColor(RED), getColor(BLUE), SWT.NORMAL);
	assertTrue(":d:", styleRangeA.equals(styleRangeB));
	assertTrue(":d:",!styleRangeA.equals(styleRangeC));
	assertTrue(":d:",!styleRangeA.equals(styleRangeD));
	assertTrue(":d:",!styleRangeA.equals(styleRangeE));
	assertTrue(":d:",!styleRangeA.equals(styleRangeF));
	assertTrue(":d:",!styleRangeA.equals(styleRangeG));
}

public void test_hashCode() {
	Set set = new HashSet();
	Color testColor = new Color(Display.getDefault(), 0, 0, 0);
	
	//regression test for bug 30924
	set.add(new StyleRange());
	set.add(new StyleRange(0, 1, testColor, null));
	set.add(new StyleRange(0, 1, testColor, testColor));
	set.add(new StyleRange(0, 1, testColor, testColor, SWT.BOLD));
	testColor.dispose();
}

public void test_isUnstyled() {
	StyleRange styleRangeA = new StyleRange(5, 10, null, null, SWT.NORMAL);
	StyleRange styleRangeB = new StyleRange(5, 10, getColor(RED), null, SWT.NORMAL);
	StyleRange styleRangeC = new StyleRange(5, 10, null, null, SWT.BOLD);
	StyleRange styleRangeD = new StyleRange(6, 10, null, getColor(BLUE), SWT.NORMAL);
	StyleRange styleRangeE = new StyleRange(5, 10, null, null);
	assertTrue(":e:", styleRangeA.isUnstyled());
	assertTrue(":e:",!styleRangeB.isUnstyled());
	assertTrue(":e:",!styleRangeC.isUnstyled());
	assertTrue(":e:",!styleRangeD.isUnstyled());
	assertTrue(":e:", styleRangeE.isUnstyled());
}

public void test_similarToLorg_eclipse_swt_custom_StyleRange() {
	StyleRange styleRangeA = new StyleRange(6, 10, getColor(RED), getColor(BLUE), SWT.NORMAL);
	StyleRange styleRangeB = new StyleRange(5, 5, getColor(RED), getColor(BLUE), SWT.NORMAL);
	StyleRange styleRangeC = new StyleRange(6, 10, getColor(RED), getColor(BLUE), SWT.NORMAL);
	StyleRange styleRangeD = new StyleRange(6, 10, getColor(BLUE), getColor(BLUE), SWT.NORMAL);
	StyleRange styleRangeE = new StyleRange(6, 10, getColor(RED), getColor(RED), SWT.NORMAL);
	StyleRange styleRangeF = new StyleRange(6, 10, getColor(RED), getColor(BLUE), SWT.BOLD);
	assertTrue(":f:", styleRangeA.similarTo(styleRangeB));
	assertTrue(":f:", styleRangeA.similarTo(styleRangeC));
	assertTrue(":f:", !styleRangeA.similarTo(styleRangeD));
	assertTrue(":f:", !styleRangeA.similarTo(styleRangeE));
	assertTrue(":f:", !styleRangeA.similarTo(styleRangeF));
}

public void test_toString() {
	StyleRange styleRange = new StyleRange(6, 10, null, getColor(BLUE), SWT.NORMAL);
	styleRange.toString();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_StyleRange((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_ColorI");
	methodNames.addElement("test_clone");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_isUnstyled");
	methodNames.addElement("test_similarToLorg_eclipse_swt_custom_StyleRange");
	methodNames.addElement("test_toString");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_Color")) test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_ColorI")) test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_ColorI();
	else if (getName().equals("test_clone")) test_clone();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_isUnstyled")) test_isUnstyled();
	else if (getName().equals("test_similarToLorg_eclipse_swt_custom_StyleRange")) test_similarToLorg_eclipse_swt_custom_StyleRange();
	else if (getName().equals("test_toString")) test_toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1617.java