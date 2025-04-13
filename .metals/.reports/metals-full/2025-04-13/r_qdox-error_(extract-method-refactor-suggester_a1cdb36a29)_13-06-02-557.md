error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1519.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1519.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1519.java
text:
```scala
t@@his.element = this.project.findType(new String(CharOperation.replaceOnCopy(fullyQualifiedName, '/', '.')), this.owner);

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.util;

import java.util.ArrayList;

import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.*;

public class JavaElementFinder extends BindingKeyParser {
	
	private JavaProject project;
	private WorkingCopyOwner owner;
	public IJavaElement element;
	public JavaModelException exception;
	private ArrayList types = new ArrayList();
	
	public JavaElementFinder(String key, JavaProject project, WorkingCopyOwner owner) {
		super(key);
		this.project = project;
		this.owner = owner;
	}
	
	private JavaElementFinder(BindingKeyParser parser, JavaProject project, WorkingCopyOwner owner) {
		super(parser);
		this.project = project;
		this.owner = owner;
	}

	public void consumeAnnotation() {
		if (!(this.element instanceof IAnnotatable)) return;
		int size = this.types.size();
		if (size == 0) return;
		IJavaElement annotationType = ((JavaElementFinder) this.types.get(size-1)).element;
		this.element = ((IAnnotatable) this.element).getAnnotation(annotationType.getElementName());
	}

	public void consumeField(char[] fieldName) {
		if (!(this.element instanceof IType)) return;
		this.element = ((IType) this.element).getField(new String(fieldName));
	}

	public void consumeFullyQualifiedName(char[] fullyQualifiedName) {
		try {
			this.element = this.project.findType(new String(CharOperation.replaceOnCopy(fullyQualifiedName, '/', '.')), owner);
		} catch (JavaModelException e) {
			this.exception = e;
		}
	}

	public void consumeLocalType(char[] uniqueKey) {
		if (this.element == null) return;
		if (this.element instanceof BinaryType) {
			int lastSlash = CharOperation.lastIndexOf('/', uniqueKey);
			int end = CharOperation.indexOf(';', uniqueKey, lastSlash+1);
			char[] localName = CharOperation.subarray(uniqueKey, lastSlash+1, end);
			IPackageFragment pkg = (IPackageFragment) this.element.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
			this.element = pkg.getClassFile(new String(localName) + SuffixConstants.SUFFIX_STRING_class);
		} else {
			int firstDollar = CharOperation.indexOf('$', uniqueKey);
			int end = CharOperation.indexOf('$', uniqueKey, firstDollar+1);
			if (end == -1)
				end = CharOperation.indexOf(';', uniqueKey, firstDollar+1);
			char[] sourceStart = CharOperation.subarray(uniqueKey, firstDollar+1, end);
			int position = Integer.parseInt(new String(sourceStart));
			try {
				this.element = ((ITypeRoot) this.element.getOpenable()).getElementAt(position);
			} catch (JavaModelException e) {
				this.exception = e;
			}
		}
	}

	public void consumeMemberType(char[] simpleTypeName) {
		if (!(this.element instanceof IType)) return;
		this.element = ((IType) this.element).getType(new String(simpleTypeName));
	}

	public void consumeMethod(char[] selector, char[] signature) {
		if (!(this.element instanceof IType)) return;
		String[] parameterTypes = Signature.getParameterTypes(new String(signature));
		IType type = (IType) this.element;
		IMethod method = type.getMethod(new String(selector), parameterTypes);
		IMethod[] methods = type.findMethods(method);
		if (methods.length > 0)
			this.element = methods[0];
	}

	public void consumePackage(char[] pkgName) {
		pkgName = CharOperation.replaceOnCopy(pkgName, '/', '.');
		try {
			this.element = this.project.findPackageFragment(new String(pkgName));
		} catch (JavaModelException e) {
			this.exception = e;
		}
	}

	public void consumeParser(BindingKeyParser parser) {
		this.types.add(parser);
	}

	public void consumeSecondaryType(char[] simpleTypeName) {
		if (this.element == null) return;
		IOpenable openable = this.element.getOpenable();
		if (!(openable instanceof ICompilationUnit)) return;
		this.element = ((ICompilationUnit) openable).getType(new String(simpleTypeName));
	}

	public void consumeTypeVariable(char[] position, char[] typeVariableName) {
		if (this.element == null) return;
		switch (this.element.getElementType()) {
		case IJavaElement.TYPE:
			this.element = ((IType) this.element).getTypeParameter(new String(typeVariableName));
			break;
		case IJavaElement.METHOD:
			this.element = ((IMethod) this.element).getTypeParameter(new String(typeVariableName));
			break;
		}
	}

	public BindingKeyParser newParser() {
		return new JavaElementFinder(this, this.project, this.owner);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1519.java