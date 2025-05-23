error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17199.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17199.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17199.java
text:
```scala
public static S@@tring[] reparentablePlatforms = new String[] {"win32", "gtk", "carbon", "cocoa"};

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


import java.io.*;
import junit.framework.*;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

public class SwtTestCase extends TestCase {
	/**
	 * The following flags are used to mark test cases that
	 * are not handled correctly by SWT at this time, or test
	 * cases that maybe themselves dubious (eg. when the correct
	 * behaviour may not be clear). Most of these flagged test
	 * cases involve handling error conditions.
	 *
	 * Setting these flags to true will run those tests. As api
	 * is implemented this gives us a convenient way to include
	 * it in the junit tests.
	 */

	// run test cases that may themselves be dubious
	// these should be eventually checked to see if 
	// there is a valid failure or the test is bogus
	public static boolean fCheckBogusTestCases = false;

	// check visibility api (eg in menu)
	public static boolean fCheckVisibility = false;

	// run test cases that check SWT policy not covered by the flags above
	public static boolean fCheckSWTPolicy = false;

	// make dialog open calls, operator must then close them
	public static boolean fTestDialogOpen = false;
	
	public static boolean fTestConsistency = false;

	// variable to keep track of the number of unimplemented methods
	public static int unimplementedMethods;
	
	// variable to keep track of the number of unimplemented API methods
	public static int unimplementedAPI;
	
	// used to specify verbose mode, if true unimplemented warning messages will 
	// be written to System.out
	public static boolean verbose = false;
	
	// allow specific image formats to be tested
	public static String[] imageFormats = new String[] {"bmp", "jpg", "gif", "png"};
	public static String[] imageFilenames = new String[] {"folder", "folderOpen", "target"};
	public static String[] invalidImageFilenames = new String[] {"corrupt", "corruptBadBitDepth.png"};
	public static String[] transparentImageFilenames = new String[] {"transparent.png"};
	
	// specify reparentable platforms
	public static String[] reparentablePlatforms = new String[] {"win32", "gtk", "carbon"};
	
public SwtTestCase(String name) {
	super(name);
}
static public void assertSame(String message, Object expected[], Object actual[]) {
	if (expected == null && actual == null) return;
	boolean same = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0) return;
		same = true;
		boolean[] matched = new boolean[expected.length];
		for (int i = 0; i < actual.length; i++) {
			boolean match = false;
			for (int j = 0; j < expected.length; j++) {
				if (!matched[j]) {
					if ((actual[i] == null && expected [j] == null) ||
					    (actual[i] != null && actual[i].equals(expected[j]))) {
						match = true;
						matched[j] = true;
						break;
					}
				}
			}
			if (!match) {
				same = false;
				break;
			}
		}
	}
	if (!same) {
		failNotEquals(message, expected, actual);
	}
}
static public void assertSame(Object expected[], Object actual[]) {
	assertSame(null, expected, actual);
}
static public void assertSame(String message, int expected[], int actual[]) {
	if (expected == null && actual == null) return;
	boolean same = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0) return;
		same = true;
		boolean[] matched = new boolean[expected.length];
		for (int i = 0; i < actual.length; i++) {
			boolean match = false;
			for (int j = 0; j < expected.length; j++) {
				if (!matched[j] && actual[i] == expected[j]) {
					match = true;
					matched[j] = true;
					break;
				}
			}
			if (!match) {
				same = false;
				break;
			}
		}
	}
	if (!same) {
		failNotEquals(message, expected, actual);
	}
}
static public void assertSame(int expected[], int actual[]) {
	assertSame(null, expected, actual);
}
static public void assertEquals(String message, Object expected[], Object actual[]) {
	if (expected == null && actual == null)
		return;
	boolean equal = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0)
			return;
		equal = true;
		for (int i = 0; i < expected.length; i++) {
			if (!expected[i].equals(actual[i])) {
				equal = false;
			}
		}
	}
	if (!equal) {
		failNotEquals(message, expected, actual);
	}
}
static public void assertEquals(String message, int expectedCode, Throwable actualThrowable) {
	if (actualThrowable instanceof SWTError) {
		SWTError error = (SWTError) actualThrowable;
		assertEquals(message, expectedCode, error.code);
	} else if (actualThrowable instanceof SWTException) {
		SWTException exception = (SWTException) actualThrowable;
		assertEquals(message, expectedCode, exception.code);
	} else {
		try {
			SWT.error(expectedCode);
		} catch (Throwable expectedThrowable) {
			assertEquals(message, expectedThrowable.getMessage(), actualThrowable.getMessage());
		}
	}
}
static public void assertEquals(Object expected[], Object actual[]) {
    assertEquals(null, expected, actual);
}
static public void assertEquals(String message, int expected[], int actual[]) {
	if (expected == null && actual == null)
		return;
	boolean equal = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0)
			return;
		equal = true;
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] != actual[i]) {
				equal = false;
			}
		}
	}
	if (!equal) {
		failNotEquals(message, expected, actual);
	}
}
static public void assertEquals(int expected[], int actual[]) {
    assertEquals(null, expected, actual);
}

static private void failNotEquals(String message, Object[] expected, Object[] actual) {
	String formatted= "";
	if (message != null)
		formatted= message+" ";
	formatted += "expected:<";
	if(expected != null) {
	    for(int i=0; i<Math.min(expected.length, 5); i++)
	        formatted += expected[i] +", ";
	    if(expected.length != 0)
	        formatted = formatted.substring(0, formatted.length()-2);
	}
	if(expected.length > 5)
	    formatted += "...";
	formatted += "> but was:<";
	if(actual != null) {
	    for(int i=0; i<Math.min(actual.length, 5); i++)
	        formatted += actual[i] +", ";
	    if(actual.length != 0)
	        formatted = formatted.substring(0, formatted.length()-2);
	}
	if(actual.length > 5)
	    formatted += "...";
	fail(formatted+">");
}

protected boolean isJ2ME() {
	try {
		Compatibility.newFileInputStream("");
	} catch (FileNotFoundException e) {
		return false;
	} catch (IOException e) {
	}
	return true;
}
protected boolean isReparentablePlatform() {
	String platform = SWT.getPlatform();
	for (int i=0; i<reparentablePlatforms.length; i++) {
		if (reparentablePlatforms[i].equals(platform)) return true;
	}
	return false;
}

protected void warnUnimpl(String message) {
	if (verbose) {
		System.out.println(this.getClass() + ": " + message);
	}
	unimplementedMethods++;
}
protected void warnUnimplAPI(String message) {
	if (verbose) {
		System.out.println("API not implemented " + this.getClass() + " " + getName());
	}
	unimplementedAPI++;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17199.java