error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14734.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14734.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,93]

error in qdox parser
file content:
```java
offset: 93
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14734.java
text:
```scala
"test_setSelectionEmpty(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TabFolder)",@@

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

/**
 * Suite for running all SWT test cases.
 */
public class AllCarbonTests extends TestSuite {

/**
 * Tests not run because they consistently fail
 */
static String[] excludeTests = {
	"test_postLorg_eclipse_swt_widgets_Event(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Display)",
	"test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_Font)",
	"test_getBoundsI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TableItem)",
	"test_getBoundsI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TreeItem)",
	"test_getBounds(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TreeItem)",
	"test_appendLjava_lang_String(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_getTopPixel(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_getTopIndex(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_clearSelection(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_getSelection(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_getSelectionIndex(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_setSelectionLorg_eclipse_swt_graphics_Point(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_setSelectionI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TabFolder)",
	"Browser4(org.eclipse.swt.tests.junit.browser.Test_BrowserSuite)",
	"Browser5(org.eclipse.swt.tests.junit.browser.Test_BrowserSuite)",
	//
	"test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_selectAll(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText)",
	"test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText)",
	"test_invokeActionI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText)",
	"test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText)",
	
	// The following tests (cut/copy/paste) fail during the build process. They do not fail locally.
	"test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo)",
	"test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo)",
	"test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo)",
};

static boolean isExcluded(String name) {
	for (int i = 0; i < excludeTests.length; i++) {
		if (name.equals(excludeTests[i])) return true;
	}
	return false;
}

public static void main(String[] args) {
	SwtTestCase.unimplementedMethods = 0;
	TestRunner.run(suite());
	if (SwtTestCase.unimplementedMethods > 0) {
		System.out.println("\nCalls to warnUnimpl: " + SwtTestCase.unimplementedMethods);
		System.out.println("\nExcluded Tests: " + excludeTests.length);
	}
}
public static Test suite() {
	TestSuite fullSuite = (TestSuite)AllTests.suite();
	TestSuite filteredSuite = new TestSuite();
	for (int i = 0; i < fullSuite.testCount(); i++) {
		Test candidateTest = fullSuite.testAt(i);
		if (candidateTest instanceof TestSuite) {
			TestSuite suite = (TestSuite)candidateTest;
			for (int j = 0; j < suite.testCount(); j++) {
				Test test = suite.testAt(j);
				if (!isExcluded(test.toString())) filteredSuite.addTest(test);				
			}
		}
	}
	return filteredSuite;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14734.java