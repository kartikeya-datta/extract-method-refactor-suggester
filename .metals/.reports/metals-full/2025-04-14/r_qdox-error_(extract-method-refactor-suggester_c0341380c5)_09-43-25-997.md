error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5530.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5530.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5530.java
text:
```scala
r@@eturn new BufferedInputStream(new InflaterInputStream(stream));

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
package org.eclipse.swt.internal;

 
import java.io.*;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.zip.InflaterInputStream;
import java.util.zip.DeflaterOutputStream;

import org.eclipse.swt.SWT;

/**
 * This class is a placeholder for utility methods commonly
 * used on J2SE platforms but not supported on some J2ME
 * profiles.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms.
 * </p>
 * <p>
 * IMPORTANT: some of the methods have been modified from their
 * J2SE parents. Refer to the description of each method for 
 * specific changes.
 * </p>
 * <ul>
 * <li>Exceptions thrown may differ since J2ME's set of 
 * exceptions is a subset of J2SE's one.
 * </li>
 * <li>The range of the mathematic functions is subject to
 * change.
 * </li>		
 * </ul>
 */
public final class Compatibility {

/**
 * Returns the PI constant as a double.
 */
public static double PI = Math.PI;

static double toRadians = PI / 180;

/**
 * Answers the length of the side adjacent to the given angle
 * of a right triangle. In other words, it returns the integer
 * conversion of length * cos (angle).
 * <p>
 * IMPORTANT: the j2me version has an additional restriction on
 * the argument. length must be between -32767 and 32767 (inclusive).
 * </p>
 * 
 * @param angle the angle in degrees
 * @param length the length of the triangle's hypotenuse
 * @return the integer conversion of length * cos (angle)
 */
public static int cos(int angle, int length) {
	return (int)(Math.cos(angle * toRadians) * length);
}

/**
 * Answers the length of the side opposite to the given angle
 * of a right triangle. In other words, it returns the integer
 * conversion of length * sin (angle).
 * <p>
 * IMPORTANT: the j2me version has an additional restriction on
 * the argument. length must be between -32767 and 32767 (inclusive).
 * </p>
 * 
 * @param angle the angle in degrees
 * @param length the length of the triangle's hypotenuse
 * @return the integer conversion of length * sin (angle)
 */
public static int sin(int angle, int length) {
	return (int)(Math.sin(angle * toRadians) * length);
}

/**
 * Answers the most negative (i.e. closest to negative infinity)
 * integer value which is greater than the number obtained by dividing
 * the first argument p by the second argument q.
 * 
 * @param p numerator
 * @param q denominator (must be different from zero)
 * @return the ceiling of the rational number p / q.
 */
public static int ceil(int p, int q) {
	return (int)Math.ceil((float)p / q);
}

/**
 * Answers the most positive (i.e. closest to positive infinity)
 * integer value which is less than the number obtained by dividing
 * the first argument p by the second argument q.
 *
 * @param p numerator
 * @param q denominator (must be different from zero)
 * @return the floor of the rational number p / q.
 */
public static int floor(int p, int q) {
	return (int)Math.floor((double)p / q);
}

/**
 * Answers the result of rounding to the closest integer the number obtained 
 * by dividing the first argument p by the second argument q.
 * <p>
 * IMPORTANT: the j2me version has an additional restriction on
 * the arguments. p must be within the range 0 - 32767 (inclusive).
 * q must be within the range 1 - 32767 (inclusive).
 * </p>
 * 
 * @param p numerator
 * @param q denominator (must be different from zero)
 * @return the closest integer to the rational number p / q
 */
public static int round(int p, int q) {
	return Math.round((float)p / q);
}

/**
 * Returns 2 raised to the power of the argument.
 *
 * @param n an int value between 0 and 30 (inclusive)
 * @return 2 raised to the power of the argument
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the argument is not between 0 and 30 (inclusive)</li>
 * </ul>
 */
public static int pow2(int n) {
	if (n >= 1 && n <= 30)
		return 2 << (n - 1);
	else if (n != 0) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return 1;
}

/**
 * Create an DeflaterOutputStream if such things are supported.
 * 
 * @param stream the output stream
 * @return a deflater stream or <code>null</code>
 * @exception IOException
 * 
 * @since 3.4
 */
public static OutputStream newDeflaterOutputStream(OutputStream stream) throws IOException {
	return new DeflaterOutputStream(stream);
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 * @exception IOException
 */
public static InputStream newFileInputStream(String filename) throws IOException {
	return new FileInputStream(filename);
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 * @exception IOException
 */
public static OutputStream newFileOutputStream(String filename) throws IOException {
	return new FileOutputStream(filename);
}

/**
 * Create an InflaterInputStream if such things are supported.
 * 
 * @param stream the input stream
 * @return a inflater stream or <code>null</code>
 * @exception IOException
 * 
 * @since 3.3
 */
public static InputStream newInflaterInputStream(InputStream stream) throws IOException {
	return new InflaterInputStream(stream);
}

/**
 * Answers whether the character is a letter.
 *
 * @param c the character
 * @return true when the character is a letter
 */
public static boolean isLetter(char c) {
	return Character.isLetter(c);
}

/**
 * Answers whether the character is a letter or a digit.
 *
 * @param c the character
 * @return true when the character is a letter or a digit
 */
public static boolean isLetterOrDigit(char c) {
	return Character.isLetterOrDigit(c);
}

/**
 * Answers whether the character is a Unicode space character.
 *
 * @param c	 the character
 * @return true when the character is a Unicode space character
 */
public static boolean isSpaceChar(char c) {
	return Character.isSpaceChar(c);
}

/**
 * Answers whether the character is a whitespace character.
 *
 * @param c the character to test
 * @return true if the character is whitespace
 */
public static boolean isWhitespace(char c) {
	return Character.isWhitespace(c);
}

/**
 * Execute a program in a separate platform process if the
 * underlying platform support this.
 * <p>
 * The new process inherits the environment of the caller.
 * </p>
 *
 * @param prog the name of the program to execute
 *
 * @exception IOException
 *  if the program cannot be executed
 * @exception SecurityException
 *  if the current SecurityManager disallows program execution
 */
public static void exec(String prog) throws java.io.IOException {
	Runtime.getRuntime().exec(prog);
}

/**
 * Execute progArray[0] in a separate platform process if the
 * underlying platform support this.
 * <p>
 * The new process inherits the environment of the caller.
 * <p>
 *
 * @param progArray array containing the program to execute and its arguments
 *
 * @exception IOException
 *  if the program cannot be executed
 * @exception	SecurityException
 *  if the current SecurityManager disallows program execution
 */
public static void exec(String[] progArray) throws java.io.IOException{
	Runtime.getRuntime().exec(progArray);
}

private static ResourceBundle msgs = null;

/**
 * Returns the NLS'ed message for the given argument. This is only being
 * called from SWT.
 * 
 * @param key the key to look up
 * @return the message for the given key
 * 
 * @see SWT#getMessage(String)
 */
public static String getMessage(String key) {
	String answer = key;
	
	if (key == null) {
		SWT.error (SWT.ERROR_NULL_ARGUMENT);
	}	
	if (msgs == null) {
		try {
			msgs = ResourceBundle.getBundle("org.eclipse.swt.internal.SWTMessages"); //$NON-NLS-1$
		} catch (MissingResourceException ex) {
			answer = key + " (no resource bundle)"; //$NON-NLS-1$
		}
	}
	if (msgs != null) {
		try {
			answer = msgs.getString(key);
		} catch (MissingResourceException ex2) {}
	}
	return answer;
}

public static String getMessage(String key, Object[] args) {
	String answer = key;
	
	if (key == null || args == null) {
		SWT.error (SWT.ERROR_NULL_ARGUMENT);
	}
	if (msgs == null) {
		try {
			msgs = ResourceBundle.getBundle("org.eclipse.swt.internal.SWTMessages"); //$NON-NLS-1$
		} catch (MissingResourceException ex) {
			answer = key + " (no resource bundle)"; //$NON-NLS-1$
		}
	}
	if (msgs != null) {
		try {
			MessageFormat formatter = new MessageFormat("");			
			formatter.applyPattern(msgs.getString(key));			
			answer = formatter.format(args);
		} catch (MissingResourceException ex2) {}
	}
	return answer;
}

/**
 * Interrupt the current thread. 
 * <p>
 * Note that this is not available on CLDC.
 * </p>
 */
public static void interrupt() {
	Thread.currentThread().interrupt();
}

/**
 * Compares two instances of class String ignoring the case of the
 * characters and answers if they are equal.
 *
 * @param s1 string
 * @param s2 string
 * @return true if the two instances of class String are equal
 */
public static boolean equalsIgnoreCase(String s1, String s2) {
	return s1.equalsIgnoreCase(s2);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5530.java