error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7505.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7505.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7505.java
text:
```scala
I@@Status status = getStatus();

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.jdt.internal.core.JavaModelStatus;

/**
 * A checked exception representing a failure in the Java model.
 * Java model exceptions contain a Java-specific status object describing the
 * cause of the exception.
 * <p>
 * Instances of this class are automatically created by the Java model
 * when problems arise, so there is generally no need for clients to create
 * instances.
 * </p>
 *
 * @see IJavaModelStatus
 * @see IJavaModelStatusConstants
 *
 * @noextend This class is not intended to be subclassed by clients.
 */
public class JavaModelException extends CoreException {

	private static final long serialVersionUID = -760398656505871287L; // backward compatible
	
	CoreException nestedCoreException;
/**
 * Creates a Java model exception that wrappers the given <code>Throwable</code>.
 * The exception contains a Java-specific status object with severity
 * <code>IStatus.ERROR</code> and the given status code.
 *
 * @param e the <code>Throwable</code>
 * @param code one of the Java-specific status codes declared in
 *   <code>IJavaModelStatusConstants</code>
 * @see IJavaModelStatusConstants
 * @see org.eclipse.core.runtime.IStatus#ERROR
 */
public JavaModelException(Throwable e, int code) {
	this(new JavaModelStatus(code, e)); 
}
/**
 * Creates a Java model exception for the given <code>CoreException</code>.
 * Equivalent to 
 * <code>JavaModelException(exception,IJavaModelStatusConstants.CORE_EXCEPTION</code>.
 *
 * @param exception the <code>CoreException</code>
 */
public JavaModelException(CoreException exception) {
	super(exception.getStatus());
	this.nestedCoreException = exception;
}
/**
 * Creates a Java model exception for the given Java-specific status object.
 *
 * @param status the Java-specific status object
 */
public JavaModelException(IJavaModelStatus status) {
	super(status);
}
/**
 * Returns the underlying <code>Throwable</code> that caused the failure.
 *
 * @return the wrappered <code>Throwable</code>, or <code>null</code> if the
 *   direct case of the failure was at the Java model layer
 */
public Throwable getException() {
	if (this.nestedCoreException == null) {
		return getStatus().getException();
	} else {
		return this.nestedCoreException;
	}
}
/**
 * Returns the Java model status object for this exception.
 * Equivalent to <code>(IJavaModelStatus) getStatus()</code>.
 *
 * @return a status object
 */
public IJavaModelStatus getJavaModelStatus() {
	IStatus status = this.getStatus();
	if (status instanceof IJavaModelStatus) {
		return (IJavaModelStatus)status;
	} else {
		// A regular IStatus is created only in the case of a CoreException.
		// See bug 13492 Should handle JavaModelExceptions that contains CoreException more gracefully  
		return new JavaModelStatus(this.nestedCoreException);
	}
}
/**
 * Returns whether this exception indicates that a Java model element does not
 * exist. Such exceptions have a status with a code of
 * <code>IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST</code> or
 * <code>IJavaModelStatusConstants.ELEMENT_NOT_ON_CLASSPATH</code>.
 * This is a convenience method.
 *
 * @return <code>true</code> if this exception indicates that a Java model
 *   element does not exist
 * @see IJavaModelStatus#isDoesNotExist()
 * @see IJavaModelStatusConstants#ELEMENT_DOES_NOT_EXIST
 * @see IJavaModelStatusConstants#ELEMENT_NOT_ON_CLASSPATH
 */
public boolean isDoesNotExist() {
	IJavaModelStatus javaModelStatus = getJavaModelStatus();
	return javaModelStatus != null && javaModelStatus.isDoesNotExist();
}

/**
 * Prints this exception's stack trace to the given print stream.
 * 
 * @param output the print stream
 * @since 3.0
 */
public void printStackTrace(PrintStream output) {
	synchronized(output) {
		super.printStackTrace(output);
		Throwable throwable = getException();
		if (throwable != null) {
			output.print("Caused by: "); //$NON-NLS-1$
			throwable.printStackTrace(output);
		}
	}
}

/**
 * Prints this exception's stack trace to the given print writer.
 * 
 * @param output the print writer
 * @since 3.0
 */
public void printStackTrace(PrintWriter output) {
	synchronized(output) {
		super.printStackTrace(output);
		Throwable throwable = getException();
		if (throwable != null) {
			output.print("Caused by: "); //$NON-NLS-1$
			throwable.printStackTrace(output);
		}
	}
}
/*
 * Returns a printable representation of this exception suitable for debugging
 * purposes only.
 */
public String toString() {
	StringBuffer buffer= new StringBuffer();
	buffer.append("Java Model Exception: "); //$NON-NLS-1$
	if (getException() != null) {
		if (getException() instanceof CoreException) {
			CoreException c= (CoreException)getException();
			buffer.append("Core Exception [code "); //$NON-NLS-1$
			buffer.append(c.getStatus().getCode());
			buffer.append("] "); //$NON-NLS-1$
			buffer.append(c.getStatus().getMessage());
		} else {
			buffer.append(getException().toString());
		}
	} else {
		buffer.append(getStatus().toString());
	}
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7505.java