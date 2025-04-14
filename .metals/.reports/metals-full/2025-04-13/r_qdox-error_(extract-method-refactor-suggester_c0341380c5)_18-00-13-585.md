error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3718.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3718.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3718.java
text:
```scala
r@@eturn Util.bind("operation.renameElementProgress"/*nonNLS*/);

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.JavaModelException;

/**
 * This operation renames elements.
 *
 * <p>Notes:<ul>
 * <li>Resource rename is not supported - this operation only renames
 *	   elements contained in compilation units.
 * <li>When a main type is renamed, its compilation unit and constructors are renamed.
 * <li>Constructors cannot be renamed.
 * </ul>
 */
public class RenameElementsOperation extends MoveElementsOperation {
/**
 * When executed, this operation will rename the specified elements with the given names in the
 * corresponding destinations.
 */
public RenameElementsOperation(IJavaElement[] elements, IJavaElement[] destinations, String[] newNames, boolean force) {
	//a rename is a move to the same parent with a new name specified
	//these elements are from different parents
	super(elements, destinations, force);
	setRenamings(newNames);
}
/**
 * @see MultiOperation
 */
protected String getMainTaskName() {
	return Util.bind("operation.renameElementProgress"); //$NON-NLS-1$
}
/**
 * @see CopyElementsOperation#isRename()
 */
protected boolean isRename() {
	return true;
}
/**
 * @see MultiOperation
 */
protected IJavaModelStatus verify() {
	IJavaModelStatus status = super.verify();
	if (! status.isOK())
		return status;
	if (fRenamingsList == null || fRenamingsList.length == 0)
		return new JavaModelStatus(IJavaModelStatusConstants.NULL_NAME);
	return JavaModelStatus.VERIFIED_OK;
}
/**
 * @see MultiOperation
 */
protected void verify(IJavaElement element) throws JavaModelException {
	int elementType = element.getElementType();
	
	if (element == null || !element.exists())
		error(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, element);
		
	if (element.isReadOnly())
		error(IJavaModelStatusConstants.READ_ONLY, element);
		
	if (!(element instanceof ISourceReference))
		error(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, element);
		
	if (elementType < IJavaElement.TYPE || elementType == IJavaElement.INITIALIZER)
		error(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, element);
		
	verifyRenaming(element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3718.java