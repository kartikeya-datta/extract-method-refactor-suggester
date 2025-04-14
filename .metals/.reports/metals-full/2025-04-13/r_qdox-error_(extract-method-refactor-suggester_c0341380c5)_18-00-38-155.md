error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1618.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1618.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1618.java
text:
```scala
n@@ew DirectoryDialog(shell);

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.DirectoryDialog
 *
 * @see org.eclipse.swt.widgets.DirectoryDialog
 */
public class Test_org_eclipse_swt_widgets_DirectoryDialog extends Test_org_eclipse_swt_widgets_Dialog {

public Test_org_eclipse_swt_widgets_DirectoryDialog(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	dirDialog = new DirectoryDialog(shell, SWT.NULL);
	setDialog(dirDialog);
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	DirectoryDialog dd = new DirectoryDialog(shell);
	try {
		new DirectoryDialog(null);
		fail("No exception thrown for null parent");
	}
	catch (IllegalArgumentException e) {
	}		
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_ShellI not written");
}

public void test_getFilterPath() {
	// tested in test_setFilterPathLjava_lang_String
}

public void test_getMessage() {
	// tested in test_setMessageLjava_lang_String
}

public void test_open() {
	if (fTestDialogOpen)
		dirDialog.open();
}

public void test_setFilterPathLjava_lang_String() {
	assertTrue(":1:", dirDialog.getFilterPath() == "");
	String testStr = "./*";
	dirDialog.setFilterPath(testStr);
	assertTrue(":2:", dirDialog.getFilterPath().equals(testStr));
	dirDialog.setFilterPath("");
	assertTrue(":3:", dirDialog.getFilterPath().equals(""));
	dirDialog.setFilterPath(null);
	assertTrue(":4:", dirDialog.getFilterPath() == null);
}

public void test_setMessageLjava_lang_String() {
	assertTrue(":1:", dirDialog.getMessage() == "");
	String testStr = "test string";
	dirDialog.setMessage(testStr);
	assertTrue(":2:", dirDialog.getMessage().equals(testStr));
	dirDialog.setMessage("");
	assertTrue(":3:", dirDialog.getMessage().equals(""));
	try {
		dirDialog.setMessage(null);
		fail ("null argument did not throw IllegalArgumentException");
	} catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_DirectoryDialog((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addElement("test_getFilterPath");
	methodNames.addElement("test_getMessage");
	methodNames.addElement("test_open");
	methodNames.addElement("test_setFilterPathLjava_lang_String");
	methodNames.addElement("test_setMessageLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Dialog.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else if (getName().equals("test_getFilterPath")) test_getFilterPath();
	else if (getName().equals("test_getMessage")) test_getMessage();
	else if (getName().equals("test_open")) test_open();
	else if (getName().equals("test_setFilterPathLjava_lang_String")) test_setFilterPathLjava_lang_String();
	else if (getName().equals("test_setMessageLjava_lang_String")) test_setMessageLjava_lang_String();
	else super.runTest();
}

/* custom */
DirectoryDialog dirDialog;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1618.java