error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/341.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/341.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/341.java
text:
```scala
i@@f ((bits & IsReachable) == 0) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;

public class Initializer extends FieldDeclaration {
	
	public Block block;
	public int lastVisibleFieldID;
	public int bodyStart;
	public int bodyEnd;
	
	public boolean errorInSignature = false; 
	
	public Initializer(Block block, int modifiers) {
		this.block = block;
		this.modifiers = modifiers;

		declarationSourceStart = sourceStart = block.sourceStart;
	}

	public FlowInfo analyseCode(
		MethodScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		return block.analyseCode(currentScope, flowContext, flowInfo);
	}

	/**
	 * Code generation for a non-static initializer: 
	 *    standard block code gen
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

		if ((bits & IsReachableMASK) == 0) {
			return;
		}
		int pc = codeStream.position;
		block.generateCode(currentScope, codeStream);
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration#getKind()
	 */
	public int getKind() {
		return INITIALIZER;
	}
	
	public boolean isStatic() {

		return (modifiers & AccStatic) != 0;
	}
	
	public void parseStatements(
		Parser parser,
		TypeDeclaration typeDeclaration,
		CompilationUnitDeclaration unit) {

		//fill up the method body with statement
		parser.parse(this, typeDeclaration, unit);
	}

	public StringBuffer printStatement(int indent, StringBuffer output) {

		if (modifiers != 0) {
			printIndent(indent, output);
			printModifiers(modifiers, output);
			if (this.annotations != null) printAnnotations(this.annotations, output);
			output.append("{\n"); //$NON-NLS-1$
			block.printBody(indent, output);
			printIndent(indent, output).append('}'); 
			return output;
		} else {
			return block.printStatement(indent, output);
		}
	}
	
	public void resolve(MethodScope scope) {

	    FieldBinding previousField = scope.initializedField;
		int previousFieldID = scope.lastVisibleFieldID;
		try {
		    scope.initializedField = null;
			scope.lastVisibleFieldID = lastVisibleFieldID;
			if (isStatic()) {
				ReferenceBinding declaringType = scope.enclosingSourceType();
				if (declaringType.isNestedType() && !declaringType.isStatic())
					scope.problemReporter().innerTypesCannotDeclareStaticInitializers(
						declaringType,
						this);
			}
			block.resolve(scope);
		} finally {
		    scope.initializedField = previousField;
			scope.lastVisibleFieldID = previousFieldID;
		}
	}

	public void traverse(ASTVisitor visitor, MethodScope scope) {

		if (visitor.visit(this, scope)) {
			block.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/341.java