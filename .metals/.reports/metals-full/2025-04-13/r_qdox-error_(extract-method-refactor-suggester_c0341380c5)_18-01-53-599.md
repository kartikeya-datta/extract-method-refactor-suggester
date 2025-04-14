error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9871.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9871.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9871.java
text:
```scala
b@@uffer.append('#');

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;

public class LocalVariableBinding extends VariableBinding {

	public boolean isArgument;
	public int resolvedPosition; // for code generation (position in method context)
	
	public static final int UNUSED = 0;
	public static final int USED = 1;
	public static final int FAKE_USED = 2;
	public int useFlag; // for flow analysis (default is UNUSED)
	
	public BlockScope declaringScope; // back-pointer to its declaring scope
	public LocalDeclaration declaration; // for source-positions

	public int[] initializationPCs;
	public int initializationCount = 0;

	// for synthetic local variables	
	// if declaration slot is not positionned, the variable will not be listed in attribute
	// note that the name of a variable should be chosen so as not to conflict with user ones (usually starting with a space char is all needed)
	public LocalVariableBinding(char[] name, TypeBinding type, int modifiers, boolean isArgument) {
		super(name, type, modifiers, isArgument ? Constant.NotAConstant : null);
		this.isArgument = isArgument;
	}
	
	// regular local variable or argument
	public LocalVariableBinding(LocalDeclaration declaration, TypeBinding type, int modifiers, boolean isArgument) {

		this(declaration.name, type, modifiers, isArgument);
		this.declaration = declaration;
	}

	/* API
	* Answer the receiver's binding type from Binding.BindingID.
	*/
	public final int bindingType() {

		return LOCAL;
	}
	
	/*
	 * declaringUniqueKey # scopeIndex / varName
	 * p.X { void foo() { int local; } } --> Lp/X;.foo()V#1/local
	 */
	public char[] computeUniqueKey() {
		StringBuffer buffer = new StringBuffer();
		
		// declaring method or type
		BlockScope scope = this.declaringScope;
		MethodScope methodScope = scope instanceof MethodScope ? (MethodScope) scope : scope.enclosingMethodScope();
		ReferenceContext referenceContext = methodScope.referenceContext;
		if (referenceContext instanceof AbstractMethodDeclaration) {
			MethodBinding methodBinding = ((AbstractMethodDeclaration) referenceContext).binding;
			if (methodBinding != null) {
				buffer.append(methodBinding.computeUniqueKey());
			}
		} else if (referenceContext instanceof TypeDeclaration) {
			TypeBinding typeBinding = ((TypeDeclaration) referenceContext).binding;
			if (typeBinding != null) {
				buffer.append(typeBinding.computeUniqueKey());
			}
		}

		// scope index
		getScopeKey(scope, buffer);

		// variable name
		buffer.append('/');
		buffer.append(this.name);
		
		int length = buffer.length();
		char[] uniqueKey = new char[length];
		buffer.getChars(0, length, uniqueKey, 0);
		return uniqueKey;
	}
	
	private void getScopeKey(BlockScope scope, StringBuffer buffer) {
		int scopeIndex = scope.scopeIndex();
		if (scopeIndex != -1) {
			getScopeKey((BlockScope)scope.parent, buffer);
			buffer.append('#');
			buffer.append(scopeIndex);
		}
	}
	
	// Answer whether the variable binding is a secret variable added for code gen purposes
	public boolean isSecret() {

		return declaration == null && !isArgument;
	}

	public void recordInitializationEndPC(int pc) {

		if (initializationPCs[((initializationCount - 1) << 1) + 1] == -1)
			initializationPCs[((initializationCount - 1) << 1) + 1] = pc;
	}

	public void recordInitializationStartPC(int pc) {

		if (initializationPCs == null) 	return;
		// optimize cases where reopening a contiguous interval
		if ((initializationCount > 0) && (initializationPCs[ ((initializationCount - 1) << 1) + 1] == pc)) {
			initializationPCs[ ((initializationCount - 1) << 1) + 1] = -1; // reuse previous interval (its range will be augmented)
		} else {
			int index = initializationCount << 1;
			if (index == initializationPCs.length) {
				System.arraycopy(initializationPCs, 0, (initializationPCs = new int[initializationCount << 2]), 0, index);
			}
			initializationPCs[index] = pc;
			initializationPCs[index + 1] = -1;
			initializationCount++;
		}
	}

	public String toString() {

		String s = super.toString();
		switch (useFlag){
			case USED:
				s += "[pos: " + String.valueOf(resolvedPosition) + "]"; //$NON-NLS-2$ //$NON-NLS-1$
				break;
			case UNUSED:
				s += "[pos: unused]"; //$NON-NLS-1$
				break;
			case FAKE_USED:
				s += "[pos: fake_used]"; //$NON-NLS-1$
				break;
		}
		s += "[id:" + String.valueOf(id) + "]"; //$NON-NLS-2$ //$NON-NLS-1$
		if (initializationCount > 0) {
			s += "[pc: "; //$NON-NLS-1$
			for (int i = 0; i < initializationCount; i++) {
				if (i > 0)
					s += ", "; //$NON-NLS-1$
				s += String.valueOf(initializationPCs[i << 1]) + "-" + ((initializationPCs[(i << 1) + 1] == -1) ? "?" : String.valueOf(initializationPCs[(i<< 1) + 1])); //$NON-NLS-2$ //$NON-NLS-1$
			}
			s += "]"; //$NON-NLS-1$
		}
		return s;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9871.java