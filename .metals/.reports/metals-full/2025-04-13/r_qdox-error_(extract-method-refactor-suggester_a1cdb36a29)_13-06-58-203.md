error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1413.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1413.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1413.java
text:
```scala
protected b@@oolean isNewLine(final char c) {

/*******************************************************************************
 * Copyright (c) 2005, 2009 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.xpand2.output;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.internal.xpand2.ast.TextStatement;
import org.eclipse.internal.xtend.expression.ast.SyntaxElement;
import org.eclipse.internal.xtend.util.Pair;
import org.eclipse.xpand2.XpandExecutionContext;

/**
 * *
 * 
 * @author Sven Efftinge (http://www.efftinge.de) *
 */
public class OutputImpl implements Output {

	private boolean automaticHyphenation = false;

	public void setAutomaticHyphens(final boolean automaticHyphenation) {
		this.automaticHyphenation = automaticHyphenation;
	}

	protected Stack<FileHandle> fileHandles = new Stack<FileHandle>();

	private final Map<String, Outlet> outlets = new HashMap<String, Outlet>();

	public void addOutlet(final Outlet outlet) {
		if (outlets.containsKey(outlet.getName())) {
			if (outlet.getName() == null)
				throw new IllegalArgumentException("A default outlet is already registered!");
			else
				throw new IllegalArgumentException("An outlet with name " + outlet.getName()
						+ " is already registered!");
		}
		outlets.put(outlet.getName(), outlet);
	}

	public Outlet getOutlet(final String name) {
		return outlets.get(name);
	}

	protected FileHandle current() {
		return fileHandles.isEmpty() ? null : fileHandles.peek();
	}

	/**
	 * DO NOT CALL THIS METHOD - FOR TESTS ONLY
	 */
	public FileHandle current__testONLY() {
		return current();
	}

	private boolean deleteLine = false;

	public void write(final String bytes) {
		if (current() != null) {
			if (deleteLine) {
				final String temp = trimUntilNewline(bytes);
				removeWSAfterLastNewline(current().getBuffer());
				((StringBuffer) current().getBuffer()).append(temp);
			}
			else {
				((StringBuffer) current().getBuffer()).append(bytes);
			}
		}
		deleteLine = false;
	}

	public void removeWSAfterLastNewline(final CharSequence cs) {
		final StringBuffer buffer = (StringBuffer) cs;
		int i = buffer.length();
		boolean wsOnly = true;
		for (; i > 0 && wsOnly; i--) {
			final char c = buffer.charAt(i - 1);
			wsOnly = Character.isWhitespace(c);
			if (wsOnly && isNewLine(c)) {
				buffer.delete(i, buffer.length());
				return;
			}
		}
		return;
	}

	private boolean isNewLine(final char c) {
		return c == '\n' || c == '\r';
	}

	public String trimUntilNewline(final String bytes) {
		int i = 0;
		boolean wsOnly = true;
		for (; i < bytes.length() && wsOnly; i++) {
			final char c = bytes.charAt(i);
			wsOnly = Character.isWhitespace(c);
			if (wsOnly && isNewLine(c)) {
				if (c == '\r' && i + 1 < bytes.length() && bytes.charAt(i + 1) == '\n') {
					i++;
				}
				return bytes.substring(i + 1);
			}
		}
		return bytes;
	}

	private final static Pattern p = Pattern.compile("(.+)://(.+)");

	public static Pair<Outlet, String> resolveOutlet(final Map<String, Outlet> allOutlets, String path,
			String outletName) {
		if (outletName == null) {
			final Matcher m = p.matcher(path);
			if (m.matches()) {
				outletName = m.group(1);
				path = m.group(2);
			}
		}
		final Outlet o = allOutlets.get(outletName);
		if (o == null) {
			if (outletName == null)
				throw new IllegalArgumentException("No default outlet was configured!");
			else
				throw new IllegalArgumentException("No outlet with the name " + outletName + " could be found!");
		}

		return new Pair<Outlet, String>(o, path);
	}

	public void openFile(final String path, final String outletName) {
		final Pair<Outlet, String> raw = resolveOutlet(outlets, path, outletName);

		final Outlet actualOutlet = raw.getFirst();
		final String actualPath = raw.getSecond();

		fileHandles.push(actualOutlet.createFileHandle(actualPath));
	}

	public void closeFile() {
		final FileHandle fi = fileHandles.pop();
		fi.writeAndClose();
	}

	private final Stack<SyntaxElement> s = new Stack<SyntaxElement>();

	public void pushStatement(final SyntaxElement stmt, final XpandExecutionContext ctx) {
		if (stmt instanceof TextStatement) {
			deleteLine = ((TextStatement) stmt).isDeleteLine();
			if (automaticHyphenation) {
				deleteLine = true;
			}
		}
		s.push(stmt);
	}

	public SyntaxElement popStatement() {
		final SyntaxElement se = s.pop();
		return se;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1413.java