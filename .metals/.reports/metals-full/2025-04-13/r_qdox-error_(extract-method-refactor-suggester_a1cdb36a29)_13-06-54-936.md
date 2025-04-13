error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2689.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2689.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2689.java
text:
```scala
r@@eturn Util.bind("operation.createInitializerProgress"); //$NON-NLS-1$

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;

import org.eclipse.jdt.core.jdom.*;
import org.eclipse.jdt.core.*;

/**
 * <p>This operation creates a initializer in a type.
 *
 * <p>Required Attributes:<ul>
 *  <li>Containing Type
 *  <li>The source code for the initializer. No verification of the source is
 *      performed.
 * </ul>
 */
public class CreateInitializerOperation extends CreateTypeMemberOperation {
	/**
	 * The current number of initializers in the parent type.
	 * Used to retrieve the handle of the newly created initializer.
	 */
	protected int fNumberOfInitializers= 1;
/**
 * When executed, this operation will create an initializer with the given name
 * in the given type with the specified source.
 *
 * <p>By default the new initializer is positioned after the last existing initializer
 * declaration, or as the first member in the type if there are no
 * initializers.
 */
public CreateInitializerOperation(IType parentElement, String source) {
	super(parentElement, source, false);
}
/**
 * @see CreateTypeMemberOperation#generateElementDOM
 */
protected IDOMNode generateElementDOM() throws JavaModelException {
	IDOMInitializer domInitializer = (new DOMFactory()).createInitializer(fSource);
	if (domInitializer == null) {
		domInitializer = (IDOMInitializer) generateSyntaxIncorrectDOM();
	}
	return domInitializer;
}
/**
 * @see CreateElementInCUOperation#generateResultHandle
 */
protected IJavaElement generateResultHandle() {
	try {
		//update the children to be current
		getType().getCompilationUnit().close();
		if (fAnchorElement == null) {
			return getType().getInitializer(fNumberOfInitializers);
		} else {
			IJavaElement[] children = getType().getChildren();
			int count = 0;
			for (int i = 0; i < children.length; i++) {
				IJavaElement child = children[i];
				if (child.equals(fAnchorElement)) {
					if (child .getElementType() == IJavaElement.INITIALIZER && fInsertionPolicy == CreateElementInCUOperation.INSERT_AFTER) {
						count++;
					}
					return getType().getInitializer(count);
				} else
					if (child.getElementType() == IJavaElement.INITIALIZER) {
						count++;
					}
			}
		}
	} catch (JavaModelException jme) {
	}
	return null;
}
/**
 * @see CreateElementInCUOperation#getMainTaskName
 */
public String getMainTaskName(){
	return Util.bind("operation.createInitializerProgress"/*nonNLS*/);
}
/**
 * By default the new initializer is positioned after the last existing initializer
 * declaration, or as the first member in the type if there are no
 * initializers.
 */
protected void initializeDefaultPosition() {
	IType parentElement = getType();
	try {
		IJavaElement[] elements = parentElement.getInitializers();
		if (elements != null && elements.length > 0) {
			fNumberOfInitializers= elements.length;
			createAfter(elements[elements.length - 1]);
		} else {
			elements = parentElement.getChildren();
			if (elements != null && elements.length > 0) {
				createBefore(elements[0]);
			}
		}
	} catch (JavaModelException e) {
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2689.java