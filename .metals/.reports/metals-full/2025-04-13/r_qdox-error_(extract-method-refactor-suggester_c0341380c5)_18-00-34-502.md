error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1973.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1973.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1973.java
text:
```scala
r@@eturn Util.bind("operation.createMethodProgress"/*nonNLS*/);

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.jdom.*;

/**
 * <p>This operation creates an instance method. 
 *
 * <p>Required Attributes:<ul>
 *  <li>Containing type
 *  <li>The source code for the method. No verification of the source is
 *      performed.
 * </ul>
 */
public class CreateMethodOperation extends CreateTypeMemberOperation {
	protected String[] fParameterTypes;
/**
 * When executed, this operation will create a method
 * in the given type with the specified source.
 */
public CreateMethodOperation(IType parentElement, String source, boolean force) {
	super(parentElement, source, force);
}
/**
 * Returns the type signatures of the parameter types of the
 * current <code>DOMMethod</code>
 */
protected String[] convertDOMMethodTypesToSignatures() {
	if (fParameterTypes == null) {
		if (fDOMNode != null) {
			String[] domParameterTypes = ((IDOMMethod)fDOMNode).getParameterTypes();
			if (domParameterTypes != null) {
				fParameterTypes = new String[domParameterTypes.length];
				// convert the DOM types to signatures
				int i;
				for (i = 0; i < fParameterTypes.length; i++) {
					fParameterTypes[i] = Signature.createTypeSignature(domParameterTypes[i].toCharArray(), false);
				}
			}
		}
	}
	return fParameterTypes;
}
/**
 * @see CreateTypeMemberOperation#generateElementDOM
 */
protected IDOMNode generateElementDOM() throws JavaModelException {
	if (fDOMNode == null) {
		fDOMNode = (new DOMFactory()).createMethod(fSource);
		if (fDOMNode == null) { //syntactically incorrect source
			fDOMNode = generateSyntaxIncorrectDOM();
		}
		if (fAlteredName != null && fDOMNode != null) {
			fDOMNode.setName(fAlteredName);
		}
	}
	return fDOMNode;
}
/**
 * @see CreateElementInCUOperation#generateResultHandle
 */
protected IJavaElement generateResultHandle() {
	String[] types = convertDOMMethodTypesToSignatures();
	String name;
	if (((IDOMMethod) fDOMNode).isConstructor()) {
		name = fDOMNode.getParent().getName();
	} else {
		name = fDOMNode.getName();
	}
	return getType().getMethod(name, types);
}
/**
 * @see CreateElementInCUOperation#getMainTaskName
 */
public String getMainTaskName(){
	return Util.bind("operation.createMethodProgress"); //$NON-NLS-1$
}
/**
 * @see CreateTypeMemberOperation#verifyNameCollision
 */
protected IJavaModelStatus verifyNameCollision() {
	if (fDOMNode != null) {
		IType type = getType();
		String name = fDOMNode.getName();
		if (name == null) { //constructor
			name = type.getElementName();
		}
		String[] types = convertDOMMethodTypesToSignatures();
		if (type.getMethod(name, types).exists()) {
			return new JavaModelStatus(IJavaModelStatusConstants.NAME_COLLISION);
		}
	}
	return JavaModelStatus.VERIFIED_OK;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1973.java