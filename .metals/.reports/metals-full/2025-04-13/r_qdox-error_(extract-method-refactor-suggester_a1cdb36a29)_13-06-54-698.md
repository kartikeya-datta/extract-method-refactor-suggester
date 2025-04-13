error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10834.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10834.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10834.java
text:
```scala
private static final S@@tring QUALIFIEDNAME_STRING = "(?:[^\\s.:()\\[\\]{}+\\-*/&|%$!\\\"\'=?]+::)*[^\\s.:()\\[\\]{}+\\-*/&|%!\"'=?]+";

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.xtend.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;

/**
 * Utility class for retrieving information from various type name presentations
 * used within Xtend's type system.
 * <p>
 * The following type formats can occur:
 * <ul>
 * <li> <code>&lt;typename&gt;</code>
 * <li> <code>&lt;collectionTypeName&gt;[&lt;typename&gt;]</code>
 * <li>
 * <code>&lt;collectionTypeName&gt;[&lt;metamodelname&gt;!&lt;typename&gt;]</code>
 * </ul>
 * Examples:
 * <ul>
 * <li> <code>entities::Account</code>
 * <li> <code>List[entities::Account]</code>
 * </ul>
 * <p>'<code>&lt;typename&gt;</code>' is the fully qualified name
 * <code>namespace1::namespace2::name</code>
 * 
 * @author Sven Efftinge (http://www.efftinge.de) - Initial implementation
 * @author Arno Haase - Initial implementation
 * @author Karsten Thoms - Documentation
 * @since 4.0
 */
public class TypeNameUtil {
	private static final String QUALIFIEDNAME_STRING = "(?:[^\\s.:()\\[\\]{}+\\-*/&|%$!\\\"\'=?]+::)*[^\\s.:()\\[\\]{}+\\-*/&|%$!\"'=?]+";
	/**
	 * This pattern is used to find the type name within possible query strings
	 */
	final static Pattern TYPE_PATTERN = Pattern.compile("\\A(?:(\\w+)\\[)?(?:(\\w+)!)?(" + QUALIFIEDNAME_STRING
			+ ")(?:\\])?\\z");
	final static Pattern GENERICLIST_PATTERN = Pattern.compile("\\A\\[L(" + QUALIFIEDNAME_STRING + ");");

	/**
	 * Retrieves the collection type.
	 * 
	 * @param name
	 *            Qualified type string
	 * @return The name of the collection type if present ('List', 'Set'),
	 *         otherwise <code>null</code>
	 */
	public static String getCollectionTypeName(final String name) {
		String group = getGroup(name, TYPE_PATTERN, 1);
		if (group == null) {
			if (GENERICLIST_PATTERN.matcher(name).matches()) {
				group = "List";
			}
		}
		return group;
	}

	/**
	 * Retrieves the type name
	 * 
	 * @param name
	 *            Qualified type string
	 * @return The type's name
	 */
	public static String getTypeName(final String name) {
		String group = getGroup(name, TYPE_PATTERN, 3);
		if (group == null) {
			group = getGroup(name, GENERICLIST_PATTERN, 1);
		}
		if (group == null) {
			group = "";
		}
		return group;
	}

	private static String getGroup(final String input, final Pattern pattern, final int group) {
		final Matcher m = pattern.matcher(input);

		if (m.matches() && m.groupCount() >= group)
			return m.group(group);
		return null;
	}

	/**
	 * Gets the internal representation of a class name.
	 * 
	 * @param class1
	 *            A class instance for which the name should be retrieved
	 * @return Xtend's classname representation (
	 *         <code>package1::package2::TheClassname</code>)
	 */
	public static String getName(final Class<?> class1) {
		return class1.getName().replaceAll("\\.", SyntaxConstants.NS_DELIM);
	}

	/**
	 * Cuts the last segment from a qualified type name.
	 * 
	 * @param fqn
	 *            Qualified type name
	 * @return <pre>
	 * ns1::ns2::name   -&gt; ns1::ns2
	 * name             -&gt; &lt;null&gt;
	 * </pre>
	 */
	public static String withoutLastSegment(final String fqn) {
		if (fqn.lastIndexOf(SyntaxConstants.NS_DELIM) == -1)
			return null;
		return fqn.substring(0, fqn.lastIndexOf(SyntaxConstants.NS_DELIM));
	}

	/**
	 * Retrieves only the last segment of a qualified name and therefore cuts
	 * the namespace part
	 * 
	 * @param fqn
	 *            Qualified type name
	 * @return <pre>
	 * ns1::ns2::name   -&gt; name
	 * name             -&gt; name
	 * </pre>
	 */
	public static String getLastSegment(final String fqn) {
		if (fqn.lastIndexOf(SyntaxConstants.NS_DELIM) == -1)
			return fqn;
		return fqn.substring(fqn.lastIndexOf(SyntaxConstants.NS_DELIM) + SyntaxConstants.NS_DELIM.length());
	}

	/**
	 * Cuts the namespace qualification from the type string.
	 * 
	 * @param fqn
	 *            Qualified type string
	 * @return <pre>
	 * ns1::ns2::type             -&gt; type
	 * List[type]                 -&gt; List[type]
	 * List[Metamodel!ns1::type]  -&gt; List[Metamodel!type]
	 * </pre>
	 */
	public static String getSimpleName(final String fqn) {
		final String ct = getCollectionTypeName(fqn);
		final String inner = getLastSegment(getTypeName(fqn));
		final StringBuffer sb = new StringBuffer();
		if (ct != null) {
			sb.append(ct).append("[");
		}
		sb.append(inner);
		if (ct != null) {
			sb.append("]");
		}
		return sb.toString();
	}

	public static Object getPackage(final String insertString) {
		if (insertString == null || insertString.trim().length() == 0)
			return null;
		final int index = insertString.indexOf(SyntaxConstants.NS_DELIM);
		if (index != -1)
			return insertString.substring(0, index);
		return null;
	}

	/**
	 * Converts a Java qualified name (dot seperated) to Xtend qualified name
	 * ('::' seperated)
	 * 
	 * @param javaTypeName
	 *            A java qualifier
	 * @return All dots are replaced by '::'. Returns <code>null</code> if
	 *         <tt>javaTypeName</tt> is null.
	 * @since 4.3.1
	 */
	public static String convertJavaTypeName(final String javaTypeName) {
		if (javaTypeName == null)
			return null;
		else
			return javaTypeName.replaceAll("\\.", SyntaxConstants.NS_DELIM);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10834.java