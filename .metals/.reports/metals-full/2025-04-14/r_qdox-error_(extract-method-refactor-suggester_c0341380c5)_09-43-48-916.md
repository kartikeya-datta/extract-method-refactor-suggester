error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8906.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8906.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8906.java
text:
```scala
b@@uffer.append(Util.LINE_SEPARATOR).append('}').append(Util.LINE_SEPARATOR);

package org.eclipse.jdt.internal.eval;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.codeassist.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.IConstants;
import org.eclipse.jdt.internal.compiler.util.CharOperation;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.eclipse.jdt.internal.core.JavaModelManager;

/**
 * Maps back and forth a code snippet to a compilation unit.
 * The structure of the compilation unit is as follows:
 * [package <package name>;]
 * [import <import name>;]*
 * public class <code snippet class name> extends <global variable class name> {
 *   public void run() {
 *     <code snippet>
 *   }
 * }
 */
class CodeSnippetToCuMapper implements EvaluationConstants {
	/**
	 * The generated compilation unit.
	 */
	public char[] cuSource;
	
	/**
	 * Where the code snippet starts in the generated compilation unit.
	 */
	public int lineNumberOffset = 0;
	public int startPosOffset = 0;

	// Internal fields
	private char[] codeSnippet;
	private char[] packageName;
	private char[][] imports;
	char[] className; // NB: Make it package default visibility to optimize access from inner classes
	private char[] varClassName;

	// Mapping of external local variables
	private char[][] localVarNames;
	private char[][] localVarTypeNames;
	private int[] localVarModifiers;
	private char[] declaringTypeName;

/**
 * Rebuild source in presence of external local variables
 */
 public CodeSnippetToCuMapper(char[] codeSnippet, char[] packageName, char[][] imports, char[] className, char[] varClassName, char[][] localVarNames, char[][] localVarTypeNames, int[] localVarModifiers, char[] declaringTypeName) {
	this.codeSnippet = codeSnippet;
	this.packageName = packageName;
	this.imports = imports;
	this.className = className;
	this.varClassName = varClassName;
	this.localVarNames = localVarNames;
	this.localVarTypeNames = localVarTypeNames;
	this.localVarModifiers = localVarModifiers;
	this.declaringTypeName = declaringTypeName;
	this.buildCUSource();
}
private void buildCUSource() {
	StringBuffer buffer = new StringBuffer();

	// package declaration
	if (this.packageName != null && this.packageName.length != 0) {
		buffer.append("package "); //$NON-NLS-1$
		buffer.append(this.packageName);
		buffer.append(";").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
		this.lineNumberOffset++;
	}

	// import declarations
	char[][] imports = this.imports;
	for (int i = 0; i < imports.length; i++) {
		buffer.append("import "); //$NON-NLS-1$
		buffer.append(imports[i]);
		buffer.append(';').append(Util.LINE_SEPARATOR);
		this.lineNumberOffset++;
	}

	// class declaration
	buffer.append("public class "); //$NON-NLS-1$
	buffer.append(this.className);

	// super class is either a global variable class or the CodeSnippet class
	if (this.varClassName != null) {
		buffer.append(" extends "); //$NON-NLS-1$
		buffer.append(this.varClassName);
	} else {
		buffer.append(" extends "); //$NON-NLS-1$
		buffer.append(PACKAGE_NAME);
		buffer.append("."); //$NON-NLS-1$
		buffer.append(ROOT_CLASS_NAME);
	}
	buffer.append(" {").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
	this.lineNumberOffset++;

	if (this.declaringTypeName != null){
		buffer.append("  "); //$NON-NLS-1$
		buffer.append(this.declaringTypeName);
		buffer.append(" "); //$NON-NLS-1$
		buffer.append(DELEGATE_THIS); // val$this
		buffer.append(';').append(Util.LINE_SEPARATOR);
		this.lineNumberOffset++;
	}
	// add some storage location for local variable persisted state
	if (localVarNames != null) {
		for (int i = 0, max = localVarNames.length; i < max; i++) {
			buffer.append("    "); //$NON-NLS-1$
			buffer.append(localVarTypeNames[i]);
			buffer.append(" "); //$NON-NLS-1$
			buffer.append(LOCAL_VAR_PREFIX); // val$...
			buffer.append(localVarNames[i]);
			buffer.append(';').append(Util.LINE_SEPARATOR);
			this.lineNumberOffset++;
		}
	}
	// run() method declaration
	buffer.append("public void run() throws Throwable {").append(Util.LINE_SEPARATOR); //$NON-NLS-1$
	this.lineNumberOffset++;
	startPosOffset = buffer.length();
	buffer.append(codeSnippet);
	// a line separator is required after the code snippet source code
	// in case the code snippet source code ends with a line comment
	// http://dev.eclipse.org/bugs/show_bug.cgi?id=14838
	buffer.append(' ').append(Util.LINE_SEPARATOR).append('}').append(Util.LINE_SEPARATOR);

	// end of class declaration
	buffer.append('}').append(Util.LINE_SEPARATOR);

	// store result
	int length = buffer.length();
	this.cuSource = new char[length];
	buffer.getChars(0, length, this.cuSource, 0);
}
/**
 * Returns a completion requestor that wraps the given requestor and shift the results
 * according to the start offset and line number offset of the code snippet in the generated compilation unit. 
 */
public ICompletionRequestor getCompletionRequestor(final ICompletionRequestor originalRequestor) {
	final int startPosOffset = this.startPosOffset;
	final int lineNumberOffset = this.lineNumberOffset;
	return new ICompletionRequestor() {
		public void acceptAnonymousType(char[] superTypePackageName,char[] superTypeName,char[][] parameterPackageNames,char[][] parameterTypeNames,char[][] parameterNames,char[] completionName,int modifiers,int completionStart,int completionEnd, int relevance){
			originalRequestor.acceptAnonymousType(superTypePackageName, superTypeName, parameterPackageNames, parameterTypeNames, parameterNames, completionName, modifiers, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		
		public void acceptClass(char[] packageName, char[] className, char[] completionName, int modifiers, int completionStart, int completionEnd, int relevance) {
			// Remove completion on generated class name or generated global variable class name
			if (CharOperation.equals(packageName, CodeSnippetToCuMapper.this.packageName) 
					&& (CharOperation.equals(className, CodeSnippetToCuMapper.this.className)
 CharOperation.equals(className, CodeSnippetToCuMapper.this.varClassName))) return;
			originalRequestor.acceptClass(packageName, className, completionName, modifiers, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptError(IProblem error) {

			error.setSourceStart(error.getSourceStart() - startPosOffset);
			error.setSourceEnd(error.getSourceEnd() - startPosOffset);
			error.setSourceLineNumber(error.getSourceLineNumber() - lineNumberOffset);
			originalRequestor.acceptError(error);
		}
		public void acceptField(char[] declaringTypePackageName, char[] declaringTypeName, char[] name, char[] typePackageName, char[] typeName, char[] completionName, int modifiers, int completionStart, int completionEnd, int relevance) {
			originalRequestor.acceptField(declaringTypePackageName, declaringTypeName, name, typePackageName, typeName, completionName, modifiers, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptInterface(char[] packageName, char[] interfaceName, char[] completionName, int modifiers, int completionStart, int completionEnd, int relevance) {
			originalRequestor.acceptInterface(packageName, interfaceName, completionName, modifiers, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptKeyword(char[] keywordName, int completionStart, int completionEnd, int relevance) {
			originalRequestor.acceptKeyword(keywordName, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptLabel(char[] labelName, int completionStart, int completionEnd, int relevance) {
			originalRequestor.acceptLabel(labelName, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptLocalVariable(char[] name, char[] typePackageName, char[] typeName, int modifiers, int completionStart, int completionEnd, int relevance) {
			originalRequestor.acceptLocalVariable(name, typePackageName, typeName, modifiers, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptMethod(char[] declaringTypePackageName, char[] declaringTypeName, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, char[][] parameterNames, char[] returnTypePackageName, char[] returnTypeName, char[] completionName, int modifiers, int completionStart, int completionEnd, int relevance) {
			// Remove completion on generated method
			if (CharOperation.equals(declaringTypePackageName, CodeSnippetToCuMapper.this.packageName) 
					&& CharOperation.equals(declaringTypeName, CodeSnippetToCuMapper.this.className)
					&& CharOperation.equals(selector, "run".toCharArray())) return; //$NON-NLS-1$
			originalRequestor.acceptMethod(declaringTypePackageName, declaringTypeName, selector, parameterPackageNames, parameterTypeNames, parameterNames, returnTypePackageName, returnTypeName, completionName, modifiers, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptMethodDeclaration(char[] declaringTypePackageName, char[] declaringTypeName, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, char[][] parameterNames, char[] returnTypePackageName, char[] returnTypeName, char[] completionName, int modifiers, int completionStart, int completionEnd, int relevance) {
			// Remove completion on generated method
			if (CharOperation.equals(declaringTypePackageName, CodeSnippetToCuMapper.this.packageName) 
					&& CharOperation.equals(declaringTypeName, CodeSnippetToCuMapper.this.className)
					&& CharOperation.equals(selector, "run".toCharArray())) return;//$NON-NLS-1$
			originalRequestor.acceptMethodDeclaration(declaringTypePackageName, declaringTypeName, selector, parameterPackageNames, parameterTypeNames, parameterNames, returnTypePackageName, returnTypeName, completionName, modifiers, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptModifier(char[] modifierName, int completionStart, int completionEnd, int relevance) {
			originalRequestor.acceptModifier(modifierName, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptPackage(char[] packageName, char[] completionName, int completionStart, int completionEnd, int relevance) {
			originalRequestor.acceptPackage(packageName, completionName, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptType(char[] packageName, char[] typeName, char[] completionName, int completionStart, int completionEnd, int relevance) {
			// Remove completion on generated class name or generated global variable class name
			if (CharOperation.equals(packageName, CodeSnippetToCuMapper.this.packageName) 
					&& (CharOperation.equals(className, CodeSnippetToCuMapper.this.className)
 CharOperation.equals(className, CodeSnippetToCuMapper.this.varClassName))) return;
			originalRequestor.acceptType(packageName, typeName, completionName, completionStart - startPosOffset, completionEnd - startPosOffset, relevance);
		}
		public void acceptVariableName(char[] typePackageName, char[] typeName, char[] name, char[] completionName, int completionStart, int completionEnd, int relevance){
			originalRequestor.acceptVariableName(typePackageName, typeName, name, completionName, completionStart, completionEnd, relevance);
		}
	};
}
public char[] getCUSource() {
	if (this.cuSource == null) {
		buildCUSource();
	}
	return this.cuSource;
}
/**
 * Returns the type of evaluation that corresponds to the given line number in the generated compilation unit.
 */
public int getEvaluationType(int lineNumber) {
	int currentLine = 1;

	// check package declaration	
	if (this.packageName != null && this.packageName.length != 0) {
		if (lineNumber == 1) {
			return EvaluationResult.T_PACKAGE;
		}
		currentLine++;
	}

	// check imports
	char[][] imports = this.imports;
	if ((currentLine <= lineNumber) && (lineNumber < (currentLine + imports.length))) {
		return EvaluationResult.T_IMPORT;
	}
	currentLine += imports.length + 1; // + 1 to skip the class declaration line

	// check generated fields
	currentLine +=
		(this.declaringTypeName == null ? 0 : 1) 
		+ (this.localVarNames == null ? 0 : this.localVarNames.length);
	if (currentLine > lineNumber) {
		return EvaluationResult.T_INTERNAL;
	}
	currentLine ++; // + 1 to skip the method declaration line

	// check code snippet
	if (currentLine >= this.lineNumberOffset) {
		return EvaluationResult.T_CODE_SNIPPET;
	}

	// default
	return EvaluationResult.T_INTERNAL;
}
/**
 * Returns the import defined at the given line number. 
 */
public char[] getImport(int lineNumber) {
	int importStartLine = this.lineNumberOffset - 2 - this.imports.length;
	return this.imports[lineNumber - importStartLine];
}
/**
 * Returns a selection requestor that wraps the given requestor and shift the problems
 * according to the start offset and line number offset of the code snippet in the generated compilation unit. 
 */
public ISelectionRequestor getSelectionRequestor(final ISelectionRequestor originalRequestor) {
	final int startPosOffset = this.startPosOffset;
	final int lineNumberOffset = this.lineNumberOffset;
	return new ISelectionRequestor() {
		public void acceptClass(char[] packageName, char[] className, boolean needQualification) {
			originalRequestor.acceptClass(packageName, className, needQualification);
		}
		public void acceptError(IProblem error) {
			error.setSourceLineNumber(error.getSourceLineNumber() - lineNumberOffset);
			error.setSourceStart(error.getSourceStart() - startPosOffset);
			error.setSourceEnd(error.getSourceEnd() - startPosOffset);
			originalRequestor.acceptError(error);
		}
		public void acceptField(char[] declaringTypePackageName, char[] declaringTypeName, char[] name) {
			originalRequestor.acceptField(declaringTypePackageName, declaringTypeName, name);
		}
		public void acceptInterface(char[] packageName, char[] interfaceName, boolean needQualification) {
			originalRequestor.acceptInterface(packageName, interfaceName, needQualification);
		}
		public void acceptMethod(char[] declaringTypePackageName, char[] declaringTypeName, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, boolean isConstructor) {
			originalRequestor.acceptMethod(declaringTypePackageName, declaringTypeName, selector, parameterPackageNames, parameterTypeNames, isConstructor);
		}
		public void acceptPackage(char[] packageName) {
			originalRequestor.acceptPackage(packageName);
		}
	};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8906.java