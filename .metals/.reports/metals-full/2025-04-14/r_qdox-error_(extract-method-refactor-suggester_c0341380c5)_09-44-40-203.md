error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15431.java
text:
```scala
r@@eturn XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), parent, dialogTitle, text);

/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIPromptService extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 9;

	public static final String NS_IPROMPTSERVICE_IID_STRING =
		"1630C61A-325E-49ca-8759-A31B16C47AA5";

	public static final nsID NS_IPROMPTSERVICE_IID =
		new nsID(NS_IPROMPTSERVICE_IID_STRING);

	public nsIPromptService(int address) {
		super(address);
	}

	public int Alert(int parent, char[] dialogTitle, char[] text) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), parent, dialogTitle, text);
	}

	public int AlertCheck(int parent, char[] dialogTitle, char[] text, char[] checkMsg, boolean[] checkValue) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int Confirm(int parent, char[] dialogTitle, char[] text, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int ConfirmCheck(int parent, char[] dialogTitle, char[] text, char[] checkMsg, boolean[] checkValue, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public static final int BUTTON_POS_0 = 1;

	public static final int BUTTON_POS_1 = 256;

	public static final int BUTTON_POS_2 = 65536;

	public static final int BUTTON_TITLE_OK = 1;

	public static final int BUTTON_TITLE_CANCEL = 2;

	public static final int BUTTON_TITLE_YES = 3;

	public static final int BUTTON_TITLE_NO = 4;

	public static final int BUTTON_TITLE_SAVE = 5;

	public static final int BUTTON_TITLE_DONT_SAVE = 6;

	public static final int BUTTON_TITLE_REVERT = 7;

	public static final int BUTTON_TITLE_IS_STRING = 127;

	public static final int STD_OK_CANCEL_BUTTONS = 513;

	public int ConfirmEx(int parent, char[] dialogTitle, char[] text, int buttonFlags, char[] button0Title, char[] button1Title, char[] button2Title, char[] checkMsg, boolean[] checkValue, int[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int Prompt(int parent, char[] dialogTitle, char[] text, int[] value, char[] checkMsg, boolean[] checkValue, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int PromptUsernameAndPassword(int parent, char[] dialogTitle, char[] text, int[] username, int[] password, char[] checkMsg, boolean[] checkValue, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int PromptPassword(int parent, char[] dialogTitle, char[] text, int[] password, char[] checkMsg, boolean[] checkValue, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int Select(int parent, char[] dialogTitle, char[] text, int count, char[] selectList, int[] outSelection, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}
}
 No newline at end of file
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15431.java