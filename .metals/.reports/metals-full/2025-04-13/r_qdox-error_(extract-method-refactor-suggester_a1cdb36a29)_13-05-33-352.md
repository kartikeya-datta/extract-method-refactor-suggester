error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7556.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7556.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7556.java
text:
```scala
c@@odeStream.recordPositionsFrom(0, declaringType.sourceStart);

package org.eclipse.jdt.internal.compiler.ast;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;

public class Clinit extends AbstractMethodDeclaration {
	public final static char[] ConstantPoolName = "<clinit>" .toCharArray(); //$NON-NLS-1$

	private FieldBinding assertionSyntheticFieldBinding = null;
	private FieldBinding classLiteralSyntheticField = null;

	public Clinit() {
		modifiers = 0;
		selector = ConstantPoolName;
	}
	
	public void analyseCode(
		ClassScope classScope,
		InitializationFlowContext staticInitializerFlowContext,
		FlowInfo flowInfo) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			ExceptionHandlingFlowContext clinitContext =
				new ExceptionHandlingFlowContext(
					staticInitializerFlowContext.parent,
					this,
					NoExceptions,
					scope,
					FlowInfo.DeadEnd);

			// check for missing returning path
			needFreeReturn =
				!((flowInfo == FlowInfo.DeadEnd) || flowInfo.isFakeReachable());

			// check missing blank final field initializations
			flowInfo = flowInfo.mergedWith(staticInitializerFlowContext.initsOnReturn);
			FieldBinding[] fields = scope.enclosingSourceType().fields();
			for (int i = 0, count = fields.length; i < count; i++) {
				FieldBinding field;
				if ((field = fields[i]).isStatic()
					&& field.isFinal()
					&& (!flowInfo.isDefinitelyAssigned(fields[i]))) {
					scope.problemReporter().uninitializedBlankFinalField(
						field,
						scope.referenceType().declarationOf(field));
					// can complain against the field decl, since only one <clinit>
				}
			}
			// check static initializers thrown exceptions
			staticInitializerFlowContext.checkInitializerExceptions(
				scope,
				clinitContext,
				flowInfo);
		} catch (AbortMethod e) {
			this.ignoreFurtherInvestigation = true;
		}
	}
	
	/**
	 * Bytecode generation for a <clinit> method
	 *
	 * @param classScope org.eclipse.jdt.internal.compiler.lookup.ClassScope
	 * @param classFile org.eclipse.jdt.internal.compiler.codegen.ClassFile
	 */
	public void generateCode(ClassScope classScope, ClassFile classFile) {
		int clinitOffset = 0;
		if (ignoreFurtherInvestigation) {
			// should never have to add any <clinit> problem method
			return;
		}
		try {
			clinitOffset = classFile.contentsOffset;
			this.generateCode(classScope, classFile, clinitOffset);
		} catch (AbortMethod e) {
			// should never occur
			// the clinit referenceContext is the type declaration
			// All clinit problems will be reported against the type: AbortType instead of AbortMethod
			// reset the contentsOffset to the value before generating the clinit code
			// decrement the number of method info as well.
			// This is done in the addProblemMethod and addProblemConstructor for other
			// cases.
			if (e.compilationResult == CodeStream.RESTART_IN_WIDE_MODE) {
				// a branch target required a goto_w, restart code gen in wide mode.
				try {
					if (statements != null) {
						for (int i = 0, max = statements.length; i < max; i++)
							statements[i].resetStateForCodeGeneration();
					}
					classFile.contentsOffset = clinitOffset;
					classFile.methodCount--;
					classFile.codeStream.wideMode = true; // request wide mode 
					this.generateCode(classScope, classFile, clinitOffset); // restart method generation
				} catch(AbortMethod e2) {
					classFile.contentsOffset = clinitOffset;
					classFile.methodCount--;
				}
			} else {
				// produce a problem method accounting for this fatal error
				classFile.contentsOffset = clinitOffset;
				classFile.methodCount--;
			}
		}
	}
	
	/**
	 * Bytecode generation for a <clinit> method
	 *
	 * @param classScope org.eclipse.jdt.internal.compiler.lookup.ClassScope
	 * @param classFile org.eclipse.jdt.internal.compiler.codegen.ClassFile
	 */
	private void generateCode(ClassScope classScope, ClassFile classFile, int clinitOffset) {
			ConstantPool constantPool = classFile.constantPool;
			int constantPoolOffset = constantPool.currentOffset;
			int constantPoolIndex = constantPool.currentIndex;
			classFile.generateMethodInfoHeaderForClinit();
			int codeAttributeOffset = classFile.contentsOffset;
			classFile.generateCodeAttributeHeader();
			CodeStream codeStream = classFile.codeStream;
			this.resolve(classScope);

			codeStream.reset(this, classFile);
			TypeDeclaration declaringType = classScope.referenceContext;

			// initialize local positions - including initializer scope.
			scope.computeLocalVariablePositions(0, codeStream); // should not be necessary
			MethodScope staticInitializerScope = declaringType.staticInitializerScope;
			staticInitializerScope.computeLocalVariablePositions(0, codeStream);
			// offset by the argument size

			// 1.4 feature
			// This has to be done before any other initialization
			if (this.assertionSyntheticFieldBinding != null) {
				// generate code related to the activation of assertion for this class
				codeStream.generateClassLiteralAccessForType(
					classScope.enclosingSourceType(),
					classLiteralSyntheticField);
				codeStream.invokeJavaLangClassDesiredAssertionStatus();
				Label falseLabel = new Label(codeStream);
				codeStream.ifne(falseLabel);
				codeStream.iconst_1();
				Label jumpLabel = new Label(codeStream);
				codeStream.goto_(jumpLabel);
				falseLabel.place();
				codeStream.iconst_0();
				jumpLabel.place();
				codeStream.putstatic(this.assertionSyntheticFieldBinding);
			}
			// generate initializers
			if (declaringType.fields != null) {
				for (int i = 0, max = declaringType.fields.length; i < max; i++) {
					FieldDeclaration fieldDecl;
					if ((fieldDecl = declaringType.fields[i]).isStatic()) {
						fieldDecl.generateCode(staticInitializerScope, codeStream);
					}
				}
			}
			if (codeStream.position == 0) {
				// do not need to output a Clinit if no bytecodes
				// so we reset the offset inside the byte array contents.
				classFile.contentsOffset = clinitOffset;
				// like we don't addd a method we need to undo the increment on the method count
				classFile.methodCount--;
				// reset the constant pool to its state before the clinit
				constantPool.resetForClinit(constantPoolIndex, constantPoolOffset);
			} else {
				if (needFreeReturn) {
					int oldPosition = codeStream.position;
					codeStream.return_();
					codeStream.updateLocalVariablesAttribute(oldPosition);
				}
				// Record the end of the clinit: point to the declaration of the class
				codeStream.recordPositionsFrom(0, declaringType);
				classFile.completeCodeAttributeForClinit(codeAttributeOffset);
			}
	}


	public boolean isClinit() {
		return true;
	}

	public boolean isInitializationMethod() {
		return true;
	}

	public boolean isStatic() {
		return true;
	}
	public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {
		//the clinit is filled by hand .... 
	}
	public void resolve(ClassScope scope) {
		this.scope = new MethodScope(scope, scope.referenceContext, true);
	}

	public String toString(int tab) {
		/* slow code */
		String s = "" ; //$NON-NLS-1$
		s = s + tabString(tab);
		s = s + "<clinit>()" ; //$NON-NLS-1$
		s = s + toStringStatements(tab + 1);
		return s;
	}

	public void traverse(
		IAbstractSyntaxTreeVisitor visitor,
		ClassScope classScope) {
		visitor.visit(this, classScope);
		visitor.endVisit(this, classScope);
	}

	// 1.4 feature
	public void addSupportForAssertion(FieldBinding assertionSyntheticFieldBinding) {
		this.assertionSyntheticFieldBinding = assertionSyntheticFieldBinding;

		// we need to add the field right now, because the field infos are generated before the methods
		SourceTypeBinding sourceType =
			this.scope.outerMostMethodScope().enclosingSourceType();
		this.classLiteralSyntheticField =
			sourceType.addSyntheticField(sourceType, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7556.java