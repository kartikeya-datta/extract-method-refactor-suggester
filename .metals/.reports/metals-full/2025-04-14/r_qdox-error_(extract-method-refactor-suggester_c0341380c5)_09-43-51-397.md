error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5918.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5918.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5918.java
text:
```scala
r@@eturn CharOperation.concat(EvaluationConstants.GLOBAL_VARS_CLASS_NAME_PREFIX, Integer.toString(EvaluationContext.VAR_CLASS_COUNTER + 1).toCharArray());

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
package org.eclipse.jdt.internal.eval;

import java.util.Map;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 * A variables evaluator compiles the global variables of an evaluation context and returns
 * the corresponding class files. Or it reports problems against these variables.
 */
public class VariablesEvaluator extends Evaluator implements EvaluationConstants {
	int startPosOffset = 0;
/**
 * Creates a new global variables evaluator.
 */
VariablesEvaluator(EvaluationContext context, INameEnvironment environment, Map options, IRequestor requestor, IProblemFactory problemFactory) {
	super(context, environment, options, requestor, problemFactory);
}
/**
 * @see org.eclipse.jdt.internal.eval.Evaluator
 */
protected void addEvaluationResultForCompilationProblem(Map resultsByIDs, IProblem problem, char[] cuSource) {
	// set evaluation id and type to an internal problem by default
	char[] evaluationID = cuSource;
	int evaluationType = EvaluationResult.T_INTERNAL;

	int pbLine = problem.getSourceLineNumber();
	int currentLine = 1;

	// check package declaration	
	char[] packageName = getPackageName();
	if (packageName.length > 0) {
		if (pbLine == 1) {
			// set evaluation id and type
			evaluationID = packageName;
			evaluationType = EvaluationResult.T_PACKAGE;

			// shift line number, source start and source end
			problem.setSourceLineNumber(1);
			problem.setSourceStart(0);
			problem.setSourceEnd(evaluationID.length - 1);
		}
		currentLine++;
	}

	// check imports
	char[][] imports = this.context.imports;
	if ((currentLine <= pbLine) && (pbLine < (currentLine + imports.length))) {
		// set evaluation id and type
		evaluationID = imports[pbLine - currentLine];
		evaluationType = EvaluationResult.T_IMPORT;

		// shift line number, source start and source end
		problem.setSourceLineNumber(1);
		problem.setSourceStart(0);
		problem.setSourceEnd(evaluationID.length - 1);
	}
	currentLine += imports.length + 1; // + 1 to skip the class declaration line

	// check variable declarations
	int varCount = this.context.variableCount;
	if ((currentLine <= pbLine) && (pbLine < currentLine + varCount)) {
		GlobalVariable var = this.context.variables[pbLine - currentLine];
		
		// set evaluation id and type
		evaluationID = var.getName();
		evaluationType = EvaluationResult.T_VARIABLE;

		// shift line number, source start and source end
		int pbStart = problem.getSourceStart() - var.declarationStart;
		int pbEnd = problem.getSourceEnd() - var.declarationStart;
		int typeLength = var.getTypeName().length;
		if ((0 <= pbStart) && (pbEnd < typeLength)) {
			// problem on the type of the variable
			problem.setSourceLineNumber(-1);
		} else {
			// problem on the name of the variable
			pbStart -= typeLength + 1; // type length + space
			pbEnd -= typeLength + 1; // type length + space
			problem.setSourceLineNumber(0);
		}
		problem.setSourceStart(pbStart);
		problem.setSourceEnd(pbEnd);
	}
	currentLine = -1; // not needed any longer

	// check variable initializers
	for (int i = 0; i < varCount; i++) {
		GlobalVariable var = this.context.variables[i];
		char[] initializer = var.getInitializer();
		int initializerLength = initializer == null ? 0 : initializer.length;
		if ((var.initializerStart <= problem.getSourceStart()) && (problem.getSourceEnd() < var.initializerStart + var.name.length)) {
			/* Problem with the variable name.
			   Ignore because it must have already been reported
			   when checking the declaration.
			 */
			return;
		} else if ((var.initExpressionStart <= problem.getSourceStart()) && (problem.getSourceEnd() < var.initExpressionStart + initializerLength)) {
			// set evaluation id and type
			evaluationID = var.name;
			evaluationType = EvaluationResult.T_VARIABLE;

			// shift line number, source start and source end
			problem.setSourceLineNumber(pbLine - var.initializerLineStart + 1);
			problem.setSourceStart(problem.getSourceStart() - var.initExpressionStart);
			problem.setSourceEnd(problem.getSourceEnd() - var.initExpressionStart);

			break;
		}
	}

	EvaluationResult result = (EvaluationResult)resultsByIDs.get(evaluationID);
	if (result == null) {
		resultsByIDs.put(evaluationID, new EvaluationResult(evaluationID, evaluationType, new IProblem[] {problem}));
	} else {
		result.addProblem(problem);
	}
}
/**
 * @see org.eclipse.jdt.internal.eval.Evaluator
 */
protected char[] getClassName() {
	return CharOperation.concat(EvaluationContext.GLOBAL_VARS_CLASS_NAME_PREFIX, Integer.toString(EvaluationContext.VAR_CLASS_COUNTER + 1).toCharArray());
}
/**
 * Creates and returns a compiler for this evaluator.
 */
Compiler getCompiler(ICompilerRequestor compilerRequestor) {
	Compiler compiler = super.getCompiler(compilerRequestor);
	
	// Initialize the compiler's lookup environment with the already compiled super class
	IBinaryType binaryType = this.context.getRootCodeSnippetBinary();
	if (binaryType != null) {
		compiler.lookupEnvironment.cacheBinaryType(binaryType);
	}

	// and the installed global variable classes
	VariablesInfo installedVars = this.context.installedVars;
	if (installedVars != null) {
		ClassFile[] classFiles = installedVars.classFiles;
		for (int i = 0; i < classFiles.length; i++) {
			ClassFile classFile = classFiles[i];
			IBinaryType binary = null;
			try {
				binary = new ClassFileReader(classFile.getBytes(), null);
			} catch (ClassFormatException e) {
				e.printStackTrace(); // Should never happen since we compiled this type
			}
			compiler.lookupEnvironment.cacheBinaryType(binary);
		}
	}
	
	return compiler;	
}
/**
 * Returns the name of package of the current compilation unit.
 */
protected char[] getPackageName() {
	return this.context.packageName;
}
/**
 * @see org.eclipse.jdt.internal.eval.Evaluator
 */
protected char[] getSource() {
	StringBuffer buffer = new StringBuffer();
	int lineNumberOffset = 1;
	
	// package declaration
	char[] packageName = getPackageName();
	if (packageName.length != 0) {
		buffer.append("package "); //$NON-NLS-1$
		buffer.append(packageName);
		buffer.append(';').append(Util.LINE_SEPARATOR);
		lineNumberOffset++;
	}

	// import declarations
	char[][] imports = this.context.imports;
	for (int i = 0; i < imports.length; i++) {
		buffer.append("import "); //$NON-NLS-1$
		buffer.append(imports[i]);
		buffer.append(';').append(Util.LINE_SEPARATOR);
		lineNumberOffset++;
	}

	// class declaration
	buffer.append("public class "); //$NON-NLS-1$
	buffer.append(getClassName());
	buffer.append(" extends "); //$NON-NLS-1$
	buffer.append(PACKAGE_NAME);
	buffer.append("."); //$NON-NLS-1$
	buffer.append(ROOT_CLASS_NAME);
	buffer.append(" {").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
	lineNumberOffset++;
	startPosOffset = buffer.length();

	// field declarations
	GlobalVariable[] vars = this.context.variables;
	VariablesInfo installedVars = this.context.installedVars;
	for (int i = 0; i < this.context.variableCount; i++){
		GlobalVariable var = vars[i];
		buffer.append("\tpublic static "); //$NON-NLS-1$
		var.declarationStart = buffer.length();
		buffer.append(var.typeName);
		buffer.append(" "); //$NON-NLS-1$
		char[] varName = var.name;
		buffer.append(varName);
		buffer.append(';').append(Util.LINE_SEPARATOR);
		lineNumberOffset++;
	}

	// field initializations
	buffer.append("\tstatic {").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
	lineNumberOffset++;
	for (int i = 0; i < this.context.variableCount; i++){
		GlobalVariable var = vars[i];
		char[] varName = var.name;
		GlobalVariable installedVar = installedVars == null ? null : installedVars.varNamed(varName);
		if (installedVar == null || !CharOperation.equals(installedVar.typeName, var.typeName)) {
			// Initialize with initializer if there was no previous value
			char[] initializer = var.initializer;
			if (initializer != null) {
				buffer.append("\t\ttry {").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
				lineNumberOffset++;
				var.initializerLineStart = lineNumberOffset;
				buffer.append("\t\t\t"); //$NON-NLS-1$
				var.initializerStart = buffer.length();
				buffer.append(varName);
				buffer.append("= "); //$NON-NLS-1$
				var.initExpressionStart = buffer.length();
				buffer.append(initializer);
				lineNumberOffset += numberOfCRs(initializer);
				buffer.append(';').append(Util.LINE_SEPARATOR);
				buffer.append("\t\t} catch (Throwable e) {").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
				buffer.append("\t\t\te.printStackTrace();").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
				buffer.append("\t\t}").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
				lineNumberOffset += 4; // 4 CRs
			}
		} else {
			// Initialize with previous value if name and type are the same
			buffer.append("\t\t"); //$NON-NLS-1$
			buffer.append(varName);
			buffer.append("= "); //$NON-NLS-1$
			char[] installedPackageName = installedVars.packageName;
			if (installedPackageName != null && installedPackageName.length != 0) {
				buffer.append(installedPackageName);
				buffer.append("."); //$NON-NLS-1$
			}
			buffer.append(installedVars.className);
			buffer.append("."); //$NON-NLS-1$
			buffer.append(varName);
			buffer.append(';').append(Util.LINE_SEPARATOR);
			lineNumberOffset++;
		}
	}
	buffer.append("\t}").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
	
	// end of class declaration
	buffer.append('}').append(Util.LINE_SEPARATOR);

	// return result
	int length = buffer.length();
	char[] result = new char[length];
	buffer.getChars(0, length, result, 0);
	return result;
}
/**
 * Returns the number of cariage returns included in the given source.
 */
private int numberOfCRs(char[] source) {
	int numberOfCRs = 0;
	boolean lastWasCR = false;
	for (int i = 0; i < source.length; i++) {
		char currentChar = source[i];
		switch(currentChar){
			case '\r' :
				lastWasCR = true;
				numberOfCRs++;
				break;
			case '\n' :
				if (!lastWasCR) numberOfCRs++; // merge CR-LF
				lastWasCR = false;
				break;
			default :
				lastWasCR = false;
		}
	}
	return numberOfCRs;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5918.java