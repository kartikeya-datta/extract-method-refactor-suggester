error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5928.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5928.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5928.java
text:
```scala
R@@esourceBundle bundle = ResourceBundle.getBundle("org.eclipse.jdt.internal.compiler.problem.messages", loc); //$NON-NLS-1$

package org.eclipse.jdt.internal.compiler.problem;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.text.*;
import java.util.*;
import org.eclipse.jdt.internal.compiler.*;

public class DefaultProblemFactory implements IProblemFactory {


	public String[] messageTemplates;
	private Locale locale;
	private static String[] DEFAULT_LOCALE_TEMPLATES;

/**
 * @param loc the locale used to get the right message
 */
public DefaultProblemFactory(Locale loc) {
	this.locale = loc;
	if (Locale.getDefault().equals(loc)){
		if (DEFAULT_LOCALE_TEMPLATES == null){
			DEFAULT_LOCALE_TEMPLATES = loadMessageTemplates(loc);
		}
		this.messageTemplates = DEFAULT_LOCALE_TEMPLATES;
	} else {
		this.messageTemplates = loadMessageTemplates(loc);
	}
}
/**
 * Answer a new IProblem created according to the parameters value
 * @param originatingFileName the name of the file name from which the problem is originated
 * @param problemId the problem id
 * @param arguments the arguments needed to set the error message
 * @param severity the severity of the problem
 * @param startPosition the starting position of the problem
 * @param endPosition the end position of the problem
 * @param lineNumber the line on which the problem occured
 * @return com.ibm.compiler.java.api.IProblem
 */
public IProblem createProblem(
	char[] originatingFileName, 
	int problemId, 
	String[] arguments, 
	int severity, 
	int startPosition, 
	int endPosition, 
	int lineNumber) {

	return new DefaultProblem(
		originatingFileName, 
		this.getLocalizedMessage(problemId, arguments),
		problemId, 
		arguments, 
		severity, 
		startPosition, 
		endPosition, 
		lineNumber); 
}
/**
 * Answer the locale used to retrieve the error messages
 * @return java.util.Locale
 */
public Locale getLocale() {
	return locale;
}
public final String getLocalizedMessage(int id, String[] problemArguments) {
	StringBuffer output = new StringBuffer(80);
	String message = 
		messageTemplates[(id & ProblemIrritants.IgnoreCategoriesMask)]; 
	if (message == null) {
		return "Unable to retrieve the error message for problem id: " //$NON-NLS-1$
			+ id
			+ ". Check compiler resources.";  //$NON-NLS-1$
	}

	int length = message.length();
	int start = -1, end = length;
	while (true) {
		if ((end = message.indexOf('{', start)) > -1) {
			output.append(message.substring(start + 1, end));
			if ((start = message.indexOf('}', end)) > -1) {
				try {
					output.append(
						problemArguments[Integer.parseInt(message.substring(end + 1, start))]); 
				} catch (NumberFormatException nfe) {
					output.append(message.substring(end + 1, start + 1));
				} catch (ArrayIndexOutOfBoundsException e) {
					return "Corrupted compiler resources for problem id: " //$NON-NLS-1$
						+ (id & ProblemIrritants.IgnoreCategoriesMask)
						+ ". Check compiler resources.";  //$NON-NLS-1$
				}
			} else {
				output.append(message.substring(end, length));
				break;
			}
		} else {
			output.append(message.substring(start + 1, length));
			break;
		}
	}
	return output.toString();
}
/**
 * @return com.ibm.compiler.java.problem.LocalizedProblem
 * @param problem com.ibm.compiler.java.problem.Problem
 */
public final String localizedMessage(IProblem problem) {
	return getLocalizedMessage(problem.getID(), problem.getArguments());
}

/**
 * This method initializes the MessageTemplates class variable according
 * to the current Locale.
 */
public static String[] loadMessageTemplates(Locale loc) {
	ResourceBundle bundle = ResourceBundle.getBundle("org.eclipse.jdt.internal.compiler.problem.Messages", loc); //$NON-NLS-1$
	String[] templates = new String[500];
	for (int i = 0, max = templates.length; i < max; i++) {
		try {
			templates[i] = bundle.getString(String.valueOf(i));
		} catch (MissingResourceException e) {
		}
	}
	return templates;
}

public DefaultProblemFactory() {
	this(Locale.getDefault());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5928.java