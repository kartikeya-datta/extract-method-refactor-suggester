error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3573.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3573.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3573.java
text:
```scala
c@@odeStream.recordPositionsFrom(0, this.sourceStart);

package org.eclipse.jdt.internal.compiler.ast;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.parser.*;

public abstract class AbstractMethodDeclaration extends AstNode implements ProblemSeverities, ReferenceContext {
	public MethodScope scope;
	//it is not relevent for constructor but it helps to have the name of the constructor here 
	//which is always the name of the class.....parsing do extra work to fill it up while it do not have to....
	public char[] selector;
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public int modifiers;
	public int modifiersSourceStart;
	public Argument[] arguments;
	public TypeReference[] thrownExceptions;
	public Statement[] statements;
	public int explicitDeclarations;
	public MethodBinding binding;
	public boolean ignoreFurtherInvestigation = false;
	public boolean needFreeReturn = false;
	public LocalVariableBinding secretReturnValue;
	static final char[] SecretLocalDeclarationName = " returnValue".toCharArray(); //$NON-NLS-1$

	public int bodyStart;
	public int bodyEnd = -1;
/**
 * AbstractMethodDeclaration constructor comment.
 */
public AbstractMethodDeclaration() {
	super();
}
/*
 *	We cause the compilation task to abort to a given extent.
 */
public void abort(int abortLevel) {

	if (scope == null){
		throw new AbortCompilation(); // cannot do better
	}
	
	CompilationResult compilationResult = scope.referenceCompilationUnit().compilationResult;
	
	switch (abortLevel) {
		case AbortCompilation :
			throw new AbortCompilation(compilationResult);
		case AbortCompilationUnit :
			throw new AbortCompilationUnit(compilationResult);
		case AbortType :
			throw new AbortType(compilationResult);
		default :
			throw new AbortMethod(compilationResult);
	}
}
public void analyseCode(ClassScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {

	// starting of the code analysis for methods
	if (ignoreFurtherInvestigation)	return;
	try {
		if (binding == null) return;// may be in a non necessary <clinit> for innerclass with static final constant fields
		if (binding.isAbstract() || binding.isNative())	return;
			
		ExceptionHandlingFlowContext methodContext = new ExceptionHandlingFlowContext(flowContext, this, binding.thrownExceptions, scope, FlowInfo.DeadEnd);

		// propagate to statements
		if (statements != null) {
			for (int i = 0, count = statements.length; i < count; i++) {
				Statement stat;
				if (!flowInfo.complainIfUnreachable((stat = statements[i]), scope)) {
					flowInfo = stat.analyseCode(scope, methodContext, flowInfo);
				}
			}
		}
		// check for missing returning path
		TypeBinding returnType = binding.returnType;
		if ((returnType == VoidBinding) || isAbstract()) {
			needFreeReturn = !((flowInfo == FlowInfo.DeadEnd) || flowInfo.isFakeReachable());
		} else {
			if (flowInfo != FlowInfo.DeadEnd) {
				// special test for empty methods that should return something
				if ((statements == null) && (returnType != VoidBinding)) {
					scope.problemReporter().shouldReturn(returnType, this);
				} else {
					scope.problemReporter().shouldReturn(returnType, statements[statements.length - 1]);
				}
			}
		}
	} catch (AbortMethod e) {
		this.ignoreFurtherInvestigation = true;		
	}
}
public void bindArguments() {
	//bind and add argument's binding into the scope of the method

	if (arguments != null) {
		int length = arguments.length;
		for (int i = 0; i < length; i++) {
			arguments[i].bind(scope, binding.parameters[i], binding.isAbstract() | binding.isNative());// by default arguments in abstract/native methods are considered to be used (no complaint is expected)
		}
	}
}
public void checkName(){
	//look if the name of the method is correct
}
public CompilationResult compilationResult(){
	return scope.referenceCompilationUnit().compilationResult;
}
/**
 * Bytecode generation for a method
 */
public void generateCode(ClassScope classScope, ClassFile classFile) {
	int problemResetPC = 0;
	classFile.codeStream.wideMode = false; // reset wideMode to false
	if (ignoreFurtherInvestigation) {
		// method is known to have errors, dump a problem method
		if (this.binding == null) return; // handle methods with invalid signature or duplicates
		int problemsLength;
		IProblem[] problems = scope.referenceCompilationUnit().compilationResult.getProblems();
		IProblem[] problemsCopy = new IProblem[problemsLength = problems.length];
		System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
		classFile.addProblemMethod(this, binding, problemsCopy);
		return;
	}
	// regular code generation
	try {
		problemResetPC = classFile.contentsOffset;
		this.generateCode(classFile);
	} catch (AbortMethod e) {
		// a fatal error was detected during code generation, need to restart code gen if possible
		if (e.compilationResult == CodeStream.RESTART_IN_WIDE_MODE) {
			// a branch target required a goto_w, restart code gen in wide mode.
			try {
				this.traverse(new ResetSateForCodeGenerationVisitor(), classScope);
				classFile.contentsOffset = problemResetPC;
				classFile.methodCount--;
				classFile.codeStream.wideMode = true; // request wide mode 
				this.generateCode(classFile); // restart method generation
			} catch(AbortMethod e2) {
				int problemsLength;
				IProblem[] problems = scope.referenceCompilationUnit().compilationResult.getProblems();
				IProblem[] problemsCopy = new IProblem[problemsLength = problems.length];
				System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
				classFile.addProblemMethod(this, binding, problemsCopy, problemResetPC);
			}
		} else {
			// produce a problem method accounting for this fatal error
			int problemsLength;
			IProblem[] problems = scope.referenceCompilationUnit().compilationResult.getProblems();
			IProblem[] problemsCopy = new IProblem[problemsLength = problems.length];
			System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
			classFile.addProblemMethod(this, binding, problemsCopy, problemResetPC);
		}
	}
}

private void generateCode(ClassFile classFile) {
	classFile.generateMethodInfoHeader(binding);
	int methodAttributeOffset = classFile.contentsOffset;
	int attributeNumber = classFile.generateMethodInfoAttribute(binding);
	if ((!binding.isNative()) && (!binding.isAbstract())) {
		int codeAttributeOffset = classFile.contentsOffset;
		classFile.generateCodeAttributeHeader();
		CodeStream codeStream = classFile.codeStream;
		codeStream.reset(this, classFile);
		// initialize local positions
		scope.computeLocalVariablePositions(binding.isStatic() ? 0 : 1, codeStream);

		// arguments initialization for local variable debug attributes
		if (arguments != null) {
			for (int i = 0, max = arguments.length; i < max; i++) {
				LocalVariableBinding argBinding;
				codeStream.addVisibleLocalVariable(argBinding = arguments[i].binding);
				argBinding.recordInitializationStartPC(0);
			}
		}
		if (statements != null) {
			for (int i = 0, max = statements.length; i < max; i++)
				statements[i].generateCode(scope, codeStream);
		}
		if (needFreeReturn) {
			codeStream.return_();
		}
		// local variable attributes
		codeStream.exitUserScope(scope);
		codeStream.recordPositionsFrom(0, this);
		classFile.completeCodeAttribute(codeAttributeOffset);
		attributeNumber++;
	}
	classFile.completeMethodInfo(methodAttributeOffset, attributeNumber);

	// if a problem got reported during code gen, then trigger problem method creation
	if (ignoreFurtherInvestigation){
		throw new AbortMethod(scope.referenceCompilationUnit().compilationResult);
	}
}
public boolean isAbstract(){

	if (binding != null) return binding.isAbstract();
	return (modifiers & AccAbstract) != 0 ;
}
public boolean isClinit() {
	return false;
}
/**
 * @return boolean
 */
public boolean isConstructor() {
	return false;
}
public boolean isDefaultConstructor() {
	return false;
}
public boolean isInitializationMethod() {
	return false;
}
public boolean isNative(){
	
	if (binding != null) return binding.isNative();
	return (modifiers & AccNative) != 0 ;
}
public boolean isStatic() {
	if (binding != null) return binding.isStatic();	
	return (modifiers & AccStatic) != 0;
}
public abstract void  parseStatements(Parser parser, CompilationUnitDeclaration unit);
	//fill up the method body with statement
public void resolve(ClassScope upperScope) {
	if (binding == null) {
		ignoreFurtherInvestigation = true;
		return;
	}

	// ========= abort on fatal error =============
	try {
		bindArguments(); //<-- shoud be done at binding/scope creation time
		checkName();

		// create secret value location
		scope.addLocalVariable(
			secretReturnValue = new LocalVariableBinding(SecretLocalDeclarationName, binding.returnType, AccDefault));
		secretReturnValue.constant = NotAConstant; // not inlinable

		// and then ....deep jump into statements.....
		if (statements != null) {
			int i = 0, length = statements.length;
			while (i < length)
				statements[i++].resolve(scope);
		}
	} catch (AbortMethod e) {
		this.ignoreFurtherInvestigation = true;		
	}
}
public String returnTypeToString(int tab) {
	/*slow code */

	return ""; //$NON-NLS-1$
}
public void tagAsHavingErrors() {
	ignoreFurtherInvestigation = true;
}
public String toString(int tab) {
	/* slow code */

	String s = tabString(tab);
	if (modifiers != AccDefault) {
		s += modifiersString(modifiers);
	}

	s += returnTypeToString(0);

	s += new String(selector) + "("; //$NON-NLS-1$
	if (arguments != null) {
		for (int i = 0; i < arguments.length; i++) {
			s += arguments[i].toString(0);
			if (i != (arguments.length - 1))
				s = s + ", "; //$NON-NLS-1$
		};
	};
	s += ")"; //$NON-NLS-1$
	if (thrownExceptions != null) {
		s += " throws "; //$NON-NLS-1$
		for (int i = 0; i < thrownExceptions.length; i++) {
			s += thrownExceptions[i].toString(0);
			if (i != (thrownExceptions.length - 1))
				s = s + ", "; //$NON-NLS-1$
		};
	};

	s += toStringStatements(tab + 1);

	return s;
}
public String toStringStatements(int tab) {
	/* slow code */

	if (isAbstract() || (this.modifiers & AccSemicolonBody) != 0) return ";"; //$NON-NLS-1$
	
	String s = " {"; //$NON-NLS-1$
	if (statements != null) {
		for (int i = 0; i < statements.length; i++){
			s = s + "\n" + statements[i].toString(tab); //$NON-NLS-1$
			if (!(statements[i] instanceof Block)){
				s += ";"; //$NON-NLS-1$
			}
		}
	}
	s += "\n" + tabString(tab == 0 ? 0 : tab - 1) + "}"; //$NON-NLS-2$ //$NON-NLS-1$
	return s;
}
public void traverse(IAbstractSyntaxTreeVisitor visitor, ClassScope classScope) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3573.java