error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1715.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1715.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1715.java
text:
```scala
I@@CompilationUnit unit = copy.getPrimary();

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.util.Comparator;
import java.util.Locale;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.SourceElementParser;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.core.builder.ProblemFactory;

/**
 * This operation is used to sort elements in a compilation unit according to
 * certain criteria.
 * 
 * @since 2.1
 */
public class SortElementsOperation extends JavaModelOperation {
	
	Comparator comparator;
	boolean hasChanged;
	int[] positions;
	
	/**
	 * Constructor for SortElementsOperation.
	 * @param elements
	 */
	public SortElementsOperation(IJavaElement[] elements, int[] positions, Comparator comparator) {
		super(elements);
		this.comparator = comparator;
		this.positions = positions;
	}

	/**
	 * Returns the amount of work for the main task of this operation for
	 * progress reporting.
	 */
	protected int getMainAmountOfWork(){
		return fElementsToProcess.length;
	}
	
	/**
	 * @see org.eclipse.jdt.internal.core.JavaModelOperation#executeOperation()
	 */
	protected void executeOperation() throws JavaModelException {
		try {
			beginTask(Util.bind("operation.sortelements"), getMainAmountOfWork()); //$NON-NLS-1$
			CompilationUnit copy = (CompilationUnit) fElementsToProcess[0];
			ICompilationUnit unit = (ICompilationUnit) copy.getOriginalElement();
			IBuffer buffer = copy.getBuffer();
			if (buffer  == null) { 
				return;
			}
			char[] bufferContents = buffer.getCharacters();
			String result = processElement(unit, positions, bufferContents);
			if (!CharOperation.equals(result.toCharArray(), bufferContents)) {
				copy.getBuffer().setContents(result);
			}
			worked(1);
		} finally {
			done();
		}
	}

	/**
	 * Method processElement.
	 * @param unit
	 * @param bufferContents
	 */
	private String processElement(ICompilationUnit unit, int[] positionsToMap, char[] source) throws JavaModelException {
		this.hasChanged = false;
		SortElementBuilder builder = new SortElementBuilder(source, positionsToMap, comparator);
		SourceElementParser parser = new SourceElementParser(builder,
			ProblemFactory.getProblemFactory(Locale.getDefault()), new CompilerOptions(JavaCore.getOptions()), true);
		
		if (unit.exists()) {
			IPackageFragment packageFragment = (IPackageFragment)unit.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
			char[][] expectedPackageName = null;
			if (packageFragment != null){
				expectedPackageName = CharOperation.splitOn('.', packageFragment.getElementName().toCharArray());
			}
			parser.parseCompilationUnit(
				new BasicCompilationUnit(
					source,
					expectedPackageName,
					unit.getElementName(),
					null),
				false/*diet parse*/);
		} else {
			parser.parseCompilationUnit(
				new BasicCompilationUnit(
					source,
					null,
					"",//$NON-NLS-1$
					null),
				false/*diet parse*/);
		}
		return builder.getSource();
	}

	/**
	 * Possible failures:
	 * <ul>
	 *  <li>NO_ELEMENTS_TO_PROCESS - the compilation unit supplied to the operation is <code>null</code></li>.
	 *  <li>INVALID_ELEMENT_TYPES - the supplied elements are not an instance of IWorkingCopy</li>.
	 * </ul>
	 * @see IJavaModelStatus
	 * @see JavaConventions
	 */
	public IJavaModelStatus verify() {
		if (fElementsToProcess.length != 1) {
			return new JavaModelStatus(IJavaModelStatusConstants.NO_ELEMENTS_TO_PROCESS);
		}
		if (fElementsToProcess[0] == null) {
			return new JavaModelStatus(IJavaModelStatusConstants.NO_ELEMENTS_TO_PROCESS);
		}
		if (!(fElementsToProcess[0] instanceof ICompilationUnit) || !((ICompilationUnit) fElementsToProcess[0]).isWorkingCopy()) {
			return new JavaModelStatus(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, fElementsToProcess[0]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1715.java