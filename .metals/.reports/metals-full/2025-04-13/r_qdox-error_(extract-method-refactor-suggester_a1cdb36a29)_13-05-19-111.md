error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3671.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3671.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3671.java
text:
```scala
o@@utput = new StringBuffer(10+length+problemArguments.length*20);

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.problem;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.util.HashtableOfInt;
import org.eclipse.jdt.internal.compiler.util.Util;

public class DefaultProblemFactory implements IProblemFactory {

	public HashtableOfInt messageTemplates;
	private Locale locale;
	private static HashtableOfInt DEFAULT_LOCALE_TEMPLATES;
	private final static char[] DOUBLE_QUOTES = "''".toCharArray(); //$NON-NLS-1$
	private final static char[] SINGLE_QUOTE = "'".toCharArray(); //$NON-NLS-1$

public DefaultProblemFactory() {
	this(Locale.getDefault());
}
/**
 * @param loc the locale used to get the right message
 */
public DefaultProblemFactory(Locale loc) {
	setLocale(loc);
}
/**
 * Answer a new IProblem created according to the parameters value
 * <ul>
 * <li>originatingFileName the name of the file name from which the problem is originated
 * <li>problemId the problem id
 * <li>problemArguments the fully qualified arguments recorded inside the problem
 * <li>messageArguments the arguments needed to set the error message (shorter names than problemArguments ones)
 * <li>severity the severity of the problem
 * <li>startPosition the starting position of the problem
 * <li>endPosition the end position of the problem
 * <li>lineNumber the line on which the problem occured
 * </ul>
 * @param originatingFileName char[]
 * @param problemId int
 * @param problemArguments String[]
 * @param messageArguments String[]
 * @param severity int
 * @param startPosition int
 * @param endPosition int
 * @param lineNumber int
 * @return CategorizedProblem
 */
public CategorizedProblem createProblem(
	char[] originatingFileName, 
	int problemId, 
	String[] problemArguments, 
	String[] messageArguments, 
	int severity, 
	int startPosition, 
	int endPosition, 
	int lineNumber,
	int columnNumber) {

	return new DefaultProblem(
		originatingFileName, 
		this.getLocalizedMessage(problemId, messageArguments),
		problemId, 
		problemArguments, 
		severity, 
		startPosition, 
		endPosition, 
		lineNumber,
		columnNumber); 
}
private final static int keyFromID(int id) {
    return id + 1; // keys are offsetted by one in table, since it cannot handle 0 key
}
/**
 * Answer the locale used to retrieve the error messages
 * @return java.util.Locale
 */
public Locale getLocale() {
	return this.locale;
}
public void setLocale(Locale locale) {
	if (locale == this.locale) return;
	this.locale = locale;
	if (Locale.getDefault().equals(locale)){
		if (DEFAULT_LOCALE_TEMPLATES == null){
			DEFAULT_LOCALE_TEMPLATES = loadMessageTemplates(locale);
		}
		this.messageTemplates = DEFAULT_LOCALE_TEMPLATES;
	} else {
		this.messageTemplates = loadMessageTemplates(locale);
	}
}

public final String getLocalizedMessage(int id, String[] problemArguments) {
	String message = (String) this.messageTemplates.get(keyFromID(id & IProblem.IgnoreCategoriesMask)); 
	if (message == null) {
		return "Unable to retrieve the error message for problem id: " //$NON-NLS-1$
			+ (id & IProblem.IgnoreCategoriesMask)
			+ ". Check compiler resources.";  //$NON-NLS-1$
	}

	// for compatibility with MessageFormat which eliminates double quotes in original message
	char[] messageWithNoDoubleQuotes =
		CharOperation.replace(message.toCharArray(), DOUBLE_QUOTES, SINGLE_QUOTE);

	if (problemArguments == null) return new String(messageWithNoDoubleQuotes);

	int length = messageWithNoDoubleQuotes.length;
	int start = 0;
	int end = length;
	StringBuffer output = null;
	if ((id & IProblem.Javadoc) != 0) {
		if (output == null) output = new StringBuffer(10+length+problemArguments.length*20);
		output.append((String) this.messageTemplates.get(keyFromID(IProblem.JavadocMessagePrefix & IProblem.IgnoreCategoriesMask)));
	}
	while (true) {
		if ((end = CharOperation.indexOf('{', messageWithNoDoubleQuotes, start)) > -1) {
			if (output == null) output = new StringBuffer(length+problemArguments.length*20);
			output.append(messageWithNoDoubleQuotes, start, end - start);
			if ((start = CharOperation.indexOf('}', messageWithNoDoubleQuotes, end + 1)) > -1) {
				int index = -1;
				String argId = new String(messageWithNoDoubleQuotes, end + 1, start - end - 1);
				try {
					index = Integer.parseInt(argId);
					output.append(problemArguments[index]);
				} catch (NumberFormatException nfe) {
					output.append(messageWithNoDoubleQuotes, end + 1, start - end);
				} catch (ArrayIndexOutOfBoundsException e) {
					return "Cannot bind message for problem (id: " //$NON-NLS-1$
						+ (id & IProblem.IgnoreCategoriesMask)
						+ ") \""  //$NON-NLS-1$
						+ message
						+ "\" with arguments: {" //$NON-NLS-1$
						+ Util.toString(problemArguments)
						+"}"; //$NON-NLS-1$
				}
				start++;
			} else {
				output.append(messageWithNoDoubleQuotes, end, length);
				break;
			}
		} else {
			if (output == null) return new String(messageWithNoDoubleQuotes);
			output.append(messageWithNoDoubleQuotes, start, length - start);
			break;
		}
	}

	// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=120410
	return new String(output.toString());
}
/**
 * @param problem CategorizedProblem
 * @return String
 */
public final String localizedMessage(CategorizedProblem problem) {
	return getLocalizedMessage(problem.getID(), problem.getArguments());
}

/**
 * This method initializes the MessageTemplates class variable according
 * to the current Locale.
 * @param loc Locale
 * @return HashtableOfInt
 */
public static HashtableOfInt loadMessageTemplates(Locale loc) {
	ResourceBundle bundle = null;
	String bundleName = "org.eclipse.jdt.internal.compiler.problem.messages"; //$NON-NLS-1$
	try {
		bundle = ResourceBundle.getBundle(bundleName, loc); 
	} catch(MissingResourceException e) {
		System.out.println("Missing resource : " + bundleName.replace('.', '/') + ".properties for locale " + loc); //$NON-NLS-1$//$NON-NLS-2$
		throw e;
	}
	HashtableOfInt templates = new HashtableOfInt(700);
	Enumeration keys = bundle.getKeys();
	while (keys.hasMoreElements()) {
	    String key = (String)keys.nextElement();
	    try {
	        int messageID = Integer.parseInt(key);
			templates.put(keyFromID(messageID), bundle.getString(key));
	    } catch(NumberFormatException e) {
	        // key ill-formed
		} catch (MissingResourceException e) {
			// available ID
	    }
	}
	return templates;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3671.java