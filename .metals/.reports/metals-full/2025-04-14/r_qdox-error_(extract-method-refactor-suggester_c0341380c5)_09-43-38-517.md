error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1065.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1065.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1065.java
text:
```scala
e@@ngine.complete(sourceUnit, mapper.startPosOffset + completionPosition, mapper.startPosOffset);

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.eval;

import java.util.Map;

import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.codeassist.CompletionEngine;
import org.eclipse.jdt.internal.codeassist.ISelectionRequestor;
import org.eclipse.jdt.internal.codeassist.SelectionEngine;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.SearchableEnvironment;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext
 */
public class EvaluationContext implements EvaluationConstants, SuffixConstants {
	/**
	 * Whether timing information should be output to the stdout
	 */
	static final boolean TIMING = false;

	/**
	 * Global counters so that several evaluation context can deploy on the same runtime.
	 */
	static int VAR_CLASS_COUNTER = 0;
	static int CODE_SNIPPET_COUNTER = 0;

	GlobalVariable[] variables;
	int variableCount;
	char[][] imports;
	char[] packageName;
	boolean varsChanged;
	VariablesInfo installedVars;
	IBinaryType codeSnippetBinary;
	String lineSeparator;

	/* do names implicitly refer to a given type */
	char[] declaringTypeName;
	int[] localVariableModifiers;
	char[][] localVariableTypeNames;
	char[][] localVariableNames;
	
	/* can 'this' be used in this context */
	boolean isStatic;
	boolean isConstructorCall;
/**
 * Creates a new evaluation context.
 */
public EvaluationContext() {
	this.variables = new GlobalVariable[5];
	this.variableCount = 0;
	this.imports = CharOperation.NO_CHAR_CHAR;
	this.packageName = CharOperation.NO_CHAR;
	this.varsChanged = true;
	this.isStatic = true;
	this.isConstructorCall = false;
	this.lineSeparator = org.eclipse.jdt.internal.compiler.util.Util.LINE_SEPARATOR; // default value
}
/**
 * Returns the global variables of this evaluation context in the order they were created in.
 */
public GlobalVariable[] allVariables() {
	GlobalVariable[] result = new GlobalVariable[this.variableCount];
	System.arraycopy(this.variables, 0, result, 0, this.variableCount);
	return result;
}
/**
 * Computes a completion at the specified position of the given code snippet.
 * (Note that this evaluation context's VM doesn't need to be running.)
 *
 *  @param environment 
 *      used to resolve type/package references and search for types/packages
 *      based on partial names.
 *
 *  @param requestor 
 *      since the engine might produce answers of various forms, the engine 
 *      is associated with a requestor able to accept all possible completions.
 *
 *  @param options
 *		set of options used to configure the code assist engine.
 */
public void complete(char[] codeSnippet, int completionPosition, SearchableEnvironment environment, CompletionRequestor requestor, Map options, IJavaProject project) {
	final char[] className = "CodeSnippetCompletion".toCharArray(); //$NON-NLS-1$
	final CodeSnippetToCuMapper mapper = new CodeSnippetToCuMapper(
		codeSnippet, 
		this.packageName, 
		this.imports, 
		className, 
		this.installedVars == null ? null : this.installedVars.className,
		this.localVariableNames, 
		this.localVariableTypeNames, 
		this.localVariableModifiers, 
		this.declaringTypeName,
		this.lineSeparator
	);
	ICompilationUnit sourceUnit = new ICompilationUnit() {
		public char[] getFileName() {
			return CharOperation.concat(className, Util.defaultJavaExtension().toCharArray());
		}
		public char[] getContents() {
			return mapper.getCUSource(EvaluationContext.this.lineSeparator);
		}
		public char[] getMainTypeName() {
			return className;
		}
		public char[][] getPackageName() {
			return null;
		}
	};
	CompletionEngine engine = new CompletionEngine(environment, mapper.getCompletionRequestor(requestor), options, project);
	engine.complete(sourceUnit, mapper.startPosOffset + completionPosition, 0);
}
/**
 * Deletes the given variable from this evaluation context. This will take effect in the target VM only
 * the next time global variables are installed.
 */
public void deleteVariable(GlobalVariable variable) {
	GlobalVariable[] vars = this.variables;
	int index = -1;
	for (int i = 0; i < this.variableCount; i++) {
		if (vars[i].equals(variable)) {
			index = i;
			break;
		}
	}
	if (index == -1) {
		return;
	}
	int elementCount = this.variableCount--;
	int j = elementCount - index - 1;
	if (j > 0) {
	    System.arraycopy(vars, index + 1, vars, index, j);
	}
	vars[elementCount - 1] = null;
	this.varsChanged = true;
}
private void deployCodeSnippetClassIfNeeded(IRequestor requestor) {
	if (this.codeSnippetBinary == null) {
		// Deploy CodeSnippet class (only once)
		requestor.acceptClassFiles(
			new ClassFile[] {
				new ClassFile() {
					public byte[] getBytes() {
						return getCodeSnippetBytes();
					}
					public char[][] getCompoundName() {
						return EvaluationConstants.ROOT_COMPOUND_NAME;
					}
				}
			}, 
			null);
	}
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext
 * @exception org.eclipse.jdt.internal.eval.InstallException if the code snippet class files could not be deployed.
 */
public void evaluate(
	char[] codeSnippet, 
	char[][] contextLocalVariableTypeNames,
	char[][] contextLocalVariableNames, 
	int[] contextLocalVariableModifiers,
	char[] contextDeclaringTypeName,
	boolean contextIsStatic,
	boolean contextIsConstructorCall,
	INameEnvironment environment, 
	Map options, 
	final IRequestor requestor, 
	IProblemFactory problemFactory) throws InstallException {

	// Initialialize context
	this.localVariableTypeNames = contextLocalVariableTypeNames;
	this.localVariableNames = contextLocalVariableNames;
	this.localVariableModifiers = contextLocalVariableModifiers;
	this.declaringTypeName = contextDeclaringTypeName;
	this.isStatic = contextIsStatic;
	this.isConstructorCall = contextIsConstructorCall;

	this.deployCodeSnippetClassIfNeeded(requestor);

	try {
		// Install new variables if needed
		class ForwardingRequestor implements IRequestor {
			boolean hasErrors = false;
			public boolean acceptClassFiles(ClassFile[] classFiles, char[] codeSnippetClassName) {
				return requestor.acceptClassFiles(classFiles, codeSnippetClassName);
			}
			public void acceptProblem(CategorizedProblem problem, char[] fragmentSource, int fragmentKind) {
				requestor.acceptProblem(problem, fragmentSource, fragmentKind);
				if (problem.isError()) {
					this.hasErrors = true;
				}
			}
		}
		ForwardingRequestor forwardingRequestor = new ForwardingRequestor();
		if (this.varsChanged) {
			evaluateVariables(environment, options, forwardingRequestor, problemFactory);
		}
		
		// Compile code snippet if there was no errors while evaluating the variables
		if (!forwardingRequestor.hasErrors) {
			Evaluator evaluator = 
				new CodeSnippetEvaluator(
					codeSnippet,
					this, 
					environment,
					options, 
					requestor, 
					problemFactory);
			ClassFile[] classes = null;
			if (TIMING) {
				long start = System.currentTimeMillis();
				classes = evaluator.getClasses();
				System.out.println("Time to compile [" + new String(codeSnippet) + "] was " + (System.currentTimeMillis() - start) + "ms"); //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
			} else {
				classes = evaluator.getClasses();
			}
			// Send code snippet on target
			if (classes != null && classes.length > 0) {
				char[] simpleClassName = evaluator.getClassName();
				char[] pkgName = this.getPackageName();
				char[] qualifiedClassName =
					pkgName.length == 0 ?
						simpleClassName :
						CharOperation.concat(pkgName, simpleClassName, '.');
				CODE_SNIPPET_COUNTER++;
				requestor.acceptClassFiles(classes, qualifiedClassName);
			}
		}
	} finally {
		// Reinitialize context to default values
		this.localVariableTypeNames = null;
		this.localVariableNames = null;
		this.localVariableModifiers = null;
		this.declaringTypeName = null;
		this.isStatic = true;
		this.isConstructorCall = false;
	}
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext
 * @exception org.eclipse.jdt.internal.eval.InstallException if the code snippet class files could not be deployed.
 */
public void evaluate(char[] codeSnippet, INameEnvironment environment, Map options, final IRequestor requestor, IProblemFactory problemFactory) throws InstallException {
	this.evaluate(
		codeSnippet,
		null,
		null,
		null,
		null,
		true,
		false,
		environment,
		options,
		requestor,
		problemFactory);
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext
 */
public void evaluateImports(INameEnvironment environment, IRequestor requestor, IProblemFactory problemFactory) {
	for (int i = 0; i < this.imports.length; i++) {
		CategorizedProblem[] problems = new CategorizedProblem[] {null};
		char[] importDeclaration = this.imports[i];
		char[][] splitDeclaration = CharOperation.splitOn('.', importDeclaration);
		int splitLength = splitDeclaration.length;
		if (splitLength > 0) {
			char[] pkgName = splitDeclaration[splitLength - 1];
			if (pkgName.length == 1 && pkgName[0] == '*') {
				char[][] parentName;
				switch (splitLength) {
					case 1:
						parentName = null;
						break;
					case 2:
						parentName = null;
						pkgName = splitDeclaration[splitLength - 2];
						break;
					default:
						parentName = CharOperation.subarray(splitDeclaration, 0, splitLength - 2);
						pkgName = splitDeclaration[splitLength - 2];
				}
				if (!environment.isPackage(parentName, pkgName)) {
					String[] arguments = new String[] {new String(importDeclaration)};
					problems[0] = problemFactory.createProblem(importDeclaration, IProblem.ImportNotFound, arguments, arguments, ProblemSeverities.Warning, 0, importDeclaration.length - 1, i);
				}
			} else {
				if (environment.findType(splitDeclaration) == null) {
					String[] arguments = new String[] {new String(importDeclaration)};
					problems[0] = problemFactory.createProblem(importDeclaration, IProblem.ImportNotFound, arguments, arguments, ProblemSeverities.Warning, 0, importDeclaration.length - 1, i);
				}
			}
		} else {
			String[] arguments = new String[] {new String(importDeclaration)};
			problems[0] = problemFactory.createProblem(importDeclaration, IProblem.ImportNotFound, arguments, arguments, ProblemSeverities.Warning, 0, importDeclaration.length - 1, i);
		}
		if (problems[0] != null) {
			requestor.acceptProblem(problems[0], importDeclaration, EvaluationResult.T_IMPORT);
		}
	}
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext
 * @exception org.eclipse.jdt.internal.eval.InstallException if the code snippet class files could not be deployed.
 * @exception java.lang.IllegalArgumentException if the global has not been installed yet.
 */
public void evaluateVariable(GlobalVariable variable, INameEnvironment environment, Map options, IRequestor requestor, IProblemFactory problemFactory) throws InstallException {
	this.evaluate(variable.getName(), environment, options, requestor, problemFactory);
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext
 * @exception org.eclipse.jdt.internal.eval.InstallException if the code snippet class files could not be deployed.
 */
public void evaluateVariables(INameEnvironment environment, Map options, IRequestor requestor, IProblemFactory problemFactory) throws InstallException {
	this.deployCodeSnippetClassIfNeeded(requestor);
	VariablesEvaluator evaluator = new VariablesEvaluator(this, environment, options, requestor, problemFactory);
	ClassFile[] classes = evaluator.getClasses();
	if (classes != null) {
		if (classes.length > 0) {
			// Sort classes so that enclosing types are cached before nested types
			// otherwise an AbortCompilation is thrown in 1.5 mode since the enclosing type
			// is needed to resolve a nested type
			Util.sort(classes, new Util.Comparer() {
				public int compare(Object a, Object b) {
					if (a == b) return 0;
					ClassFile enclosing = ((ClassFile) a).enclosingClassFile;
					while (enclosing != null) {
						if (enclosing == b)
							return 1;
						enclosing = enclosing.enclosingClassFile;
					}
					return -1;
				}
			});
			
			// Send classes
			if (!requestor.acceptClassFiles(classes, null)) {
				throw new InstallException();
			}

			// Remember that the variables have been installed
			int count = this.variableCount;
			GlobalVariable[] variablesCopy = new GlobalVariable[count];
			System.arraycopy(this.variables, 0, variablesCopy, 0, count); 
			this.installedVars = new VariablesInfo(evaluator.getPackageName(), evaluator.getClassName(), classes, variablesCopy, count);
			VAR_CLASS_COUNTER++;
		}
		this.varsChanged = false;
	}
}
/**
 * Returns the bytes of the CodeSnippet class.
 * Generated using the following code snippet:
[java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("d:/temp/CodeSnippet.java"));
writer.write(org.eclipse.jdt.internal.eval.EvaluationContext.getCodeSnippetSource());
writer.close();
org.eclipse.jdt.internal.compiler.batch.Main.compile(
	"d:/temp/CodeSnippet.java -d d:/temp -classpath d:/jdk1.2.2/jre/lib/rt.jar -verbose");
java.io.FileInputStream reader =  new java.io.FileInputStream("d:/temp/org/eclipse/jdt/internal/eval/target/CodeSnippet.class");
byte[] bytes = org.eclipse.jdt.internal.core.Util.readContentsAsBytes(reader);
reader.close();
StringBuffer buffer = new StringBuffer();
buffer.append("private byte[] getCodeSnippetBytes() {\n");
buffer.append("	return new byte[] {\n");
buffer.append("		");
for (int i = 0; i < bytes.length; i++) {
	buffer.append(bytes[i]);
	if (i == bytes.length - 1) {
		buffer.append("\n");
	} else {
		buffer.append(", ");
	}
}
buffer.append("	};\n");
buffer.append("}");
buffer.toString()
]
 */
byte[] getCodeSnippetBytes() {
	return new byte[] {
		-54, -2, -70, -66, 0, 3, 0, 45, 0, 35, 1, 0, 48, 111, 114, 103, 47, 101, 99, 108, 105, 112, 115, 101, 47, 106, 100, 116, 47, 105, 110, 116, 101, 114, 110, 97, 108, 47, 101, 118, 97, 108, 47, 116, 97, 114, 103, 101, 116, 47, 67, 111, 100, 101, 83, 110, 105, 112, 112, 101, 116, 7, 0, 1, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 3, 1, 0, 10, 114, 101, 115, 117, 108, 116, 84, 121, 112, 101, 1, 0, 17, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 59, 1, 0, 11, 114, 101, 115, 117, 108, 116, 86, 97, 108, 117, 101, 1, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 1, 0, 7, 99, 108, 97, 115, 115, 36, 48, 1, 0, 9, 83, 121, 110, 116, 104, 101, 116, 105, 99, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 12, 0, 11, 0, 12, 10, 0, 4, 0, 14, 1, 0, 14, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 86, 111, 105, 100, 7, 0, 16, 1, 0, 4, 84, 89, 80, 69, 12, 0, 18, 0, 6, 9, 0, 17, 0, 19, 12, 0, 5, 0, 6, 9, 0, 2, 0, 21, 12, 0, 7, 0, 8, 9, 0, 2, 0, 23, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 13, 103, 101, 116, 82, 101, 115, 117, 108, 116, 84, 121, 112, 101, 1, 0, 19, 40, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 59, 1, 0, 14, 103, 101, 116, 82, 101, 115, 117, 108, 116, 86, 97, 108, 117, 101, 1, 0, 20, 40, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 1, 0, 3, 114, 117, 110, 1, 0, 9, 115, 101, 116, 82, 101, 115, 117, 108, 116, 1, 0, 38, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 59, 41, 86, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 16, 67, 111, 100, 101, 83, 110, 105, 112, 112, 101, 116, 46, 106, 97, 118, 97, 0, 33, 0, 2, 0, 4, 0, 0, 0, 3, 0, 2, 0, 5, 0, 6, 0, 0, 0, 2, 0, 7, 0, 8, 0, 0, 0, 8, 0, 9, 0, 6, 0, 1, 0, 10, 0, 0, 0, 0, 0, 5, 0, 1, 0, 11, 0, 12, 0, 1, 0, 13, 0, 0, 0, 53, 0, 2, 0, 1, 0, 0, 0, 17, 42, -73, 0, 15, 42, -78, 0, 20, -75, 0, 22, 42, 1, -75, 0, 24, -79, 0, 0, 0, 1, 0, 25, 0, 0, 0, 18, 0, 4, 0, 0, 0, 17, 0, 4, 0, 18, 0, 11, 0, 19, 0, 16, 0, 17, 0, 1, 0, 26, 0, 27, 0, 1, 0, 13, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -76, 0, 22, -80, 0, 0, 0, 1, 0, 25, 0, 0, 0, 6, 0, 1, 0, 0, 0, 24, 0, 1, 0, 28, 0, 29, 0, 1, 0, 13, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -76, 0, 24, -80, 0, 0, 0, 1, 0, 25, 0, 0, 0, 6, 0, 1, 0, 0, 0, 30, 0, 1, 0, 30, 0, 12, 0, 1, 0, 13, 0, 0, 0, 25, 0, 0, 0, 1, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 25, 0, 0, 0, 6, 0, 1, 0, 0, 0, 36, 0, 1, 0, 31, 0, 32, 0, 1, 0, 13, 0, 0, 0, 43, 0, 2, 0, 3, 0, 0, 0, 11, 42, 43, -75, 0, 24, 42, 44, -75, 0, 22, -79, 0, 0, 0, 1, 0, 25, 0, 0, 0, 14, 0, 3, 0, 0, 0, 42, 0, 5, 0, 43, 0, 10, 0, 41, 0, 1, 0, 33, 0, 0, 0, 2, 0, 34
	};
}
/**
 * Returns the source of the CodeSnippet class.
 * This is used to generate the binary of the CodeSnippetClass
 */
public static String getCodeSnippetSource() {
	return
		"package org.eclipse.jdt.internal.eval.target;\n" + //$NON-NLS-1$
		"\n" + //$NON-NLS-1$
		"/*\n" + //$NON-NLS-1$
		" * (c) Copyright IBM Corp. 2000, 2001.\n" + //$NON-NLS-1$
		" * All Rights Reserved.\n" + //$NON-NLS-1$
		" */\n" + //$NON-NLS-1$
		"/**\n" + //$NON-NLS-1$
		" * The root of all code snippet classes. Code snippet classes\n" + //$NON-NLS-1$
		" * are supposed to overide the run() method.\n" + //$NON-NLS-1$
		" * <p>\n" + //$NON-NLS-1$
		" * IMPORTANT NOTE:\n" + //$NON-NLS-1$
		" * All methods in this class must be public since this class is going to be loaded by the\n" + //$NON-NLS-1$
		" * bootstrap class loader, and the other code snippet support classes might be loaded by \n" + //$NON-NLS-1$
		" * another class loader (so their runtime packages are going to be different).\n" + //$NON-NLS-1$
		" */\n" + //$NON-NLS-1$
		"public class CodeSnippet {\n" + //$NON-NLS-1$
		"	private Class resultType = void.class;\n" + //$NON-NLS-1$
		"	private Object resultValue = null;\n" + //$NON-NLS-1$
		"/**\n" + //$NON-NLS-1$
		" * Returns the result type of the code snippet evaluation.\n" + //$NON-NLS-1$
		" */\n" + //$NON-NLS-1$
		"public Class getResultType() {\n" + //$NON-NLS-1$
		"	return this.resultType;\n" + //$NON-NLS-1$
		"}\n" + //$NON-NLS-1$
		"/**\n" + //$NON-NLS-1$
		" * Returns the result value of the code snippet evaluation.\n" + //$NON-NLS-1$
		" */\n" + //$NON-NLS-1$
		"public Object getResultValue() {\n" + //$NON-NLS-1$
		"	return this.resultValue;\n" + //$NON-NLS-1$
		"}\n" + //$NON-NLS-1$
		"/**\n" + //$NON-NLS-1$
		" * The code snippet. Subclasses must override this method with a transformed code snippet\n" + //$NON-NLS-1$
		" * that stores the result using setResult(Class, Object).\n" + //$NON-NLS-1$
		" */\n" + //$NON-NLS-1$
		"public void run() {\n" + //$NON-NLS-1$
		"}\n" + //$NON-NLS-1$
		"/**\n" + //$NON-NLS-1$
		" * Stores the result type and value of the code snippet evaluation.\n" + //$NON-NLS-1$
		" */\n" + //$NON-NLS-1$
		"public void setResult(Object someResultValue, Class someResultType) {\n" + //$NON-NLS-1$
		"	this.resultValue = someResultValue;\n" + //$NON-NLS-1$
		"	this.resultType = someResultType;\n" + //$NON-NLS-1$
		"}\n" + //$NON-NLS-1$
		"}\n"; //$NON-NLS-1$
}
/**
 * Returns the imports of this evaluation context. An import is the name of a package
 * or the fully qualified name of a type as defined in the import statement of
 * a compilation unit.
 */
public char[][] getImports() {
	return this.imports;
}
/**
 * Returns the dot-separated name of the package code snippets are run into.
 * Returns an empty array for the default package. This is the default if 
 * the package name has never been set.
 */
public char[] getPackageName() {
	return this.packageName;
}
/**
 * Return the binary for the root code snippet class (ie. org.eclipse.jdt.internal.eval.target.CodeSnippet).
 */
IBinaryType getRootCodeSnippetBinary() {
	if (this.codeSnippetBinary == null) {
		this.codeSnippetBinary = new CodeSnippetSkeleton();
	}
	return this.codeSnippetBinary;
}
/**
 * Creates a new global variable with the given name, type and initializer.
 * If the variable is not initialized, the initializer can be null.
 * Note that this doesn't install it to this evaluation context's VM.
 *
 * @see GlobalVariable
 */
public GlobalVariable newVariable(char[] typeName, char[] name, char[] initializer) {
	GlobalVariable var = new GlobalVariable(typeName, name, initializer);
	if (this.variableCount >= this.variables.length) // assume variables is never empty
		System.arraycopy(this.variables, 0, this.variables = new GlobalVariable[this.variableCount * 2], 0, this.variableCount);
	this.variables[this.variableCount++] = var;
	this.varsChanged = true;
	return var;
}
/**
 * Computes the selection at the specified positions of the given code snippet.
 * (Note that this evaluation context's VM doesn't need to be running.)
 *  @param codeSnippet char[]
 * 		The code snipper source
 * 
 *  @param selectionSourceStart int
 * 
 *  @param selectionSourceEnd int
 * 
 *  @param environment org.eclipse.jdt.internal.core.SearchableEnvironment
 *      used to resolve type/package references and search for types/packages
 *      based on partial names.
 *
 *  @param requestor org.eclipse.jdt.internal.codeassist.ISelectionRequestor
 *      since the engine might produce answers of various forms, the engine 
 *      is associated with a requestor able to accept all possible selections.
 *
 *  @param options java.util.Map
 *		set of options used to configure the code assist engine.
 */
public void select( 
	char[] codeSnippet,
	int selectionSourceStart,
	int selectionSourceEnd,
	SearchableEnvironment environment, 
	ISelectionRequestor requestor,
	Map options) {
		
	final char[] className = "CodeSnippetSelection".toCharArray(); //$NON-NLS-1$
	final CodeSnippetToCuMapper mapper = new CodeSnippetToCuMapper(
		codeSnippet, 
		this.packageName, 
		this.imports, 
		className, 
		this.installedVars == null ? null : this.installedVars.className,
		this.localVariableNames, 
		this.localVariableTypeNames, 
		this.localVariableModifiers, 
		this.declaringTypeName,
		this.lineSeparator
	);
	ICompilationUnit sourceUnit = new ICompilationUnit() {
		public char[] getFileName() {
			return CharOperation.concat(className, Util.defaultJavaExtension().toCharArray());
		}
		public char[] getContents() {
			return mapper.getCUSource(EvaluationContext.this.lineSeparator);
		}
		public char[] getMainTypeName() {
			return className;
		}
		public char[][] getPackageName() {
			return null;
		}
	};
	SelectionEngine engine = new SelectionEngine(environment, mapper.getSelectionRequestor(requestor), options);
	engine.select(sourceUnit, mapper.startPosOffset + selectionSourceStart, mapper.startPosOffset + selectionSourceEnd);
}
/**
 * Sets the imports of this evaluation context. An import is the name of a package
 * or the fully qualified name of a type as defined in the import statement of
 * a compilation unit (see the Java Language Specifications for more details).
 */
public void setImports(char[][] imports) {
	this.imports = imports;
	this.varsChanged = true; // this may change the visibility of the variable's types
}
/**
 * Sets the line separator used by this evaluation context.
 */
public void setLineSeparator(String lineSeparator) {
	this.lineSeparator = lineSeparator;
}
/**
 * Sets the dot-separated name of the package code snippets are ran into.
 * The default package name is an empty array.
 */
public void setPackageName(char[] packageName) {
	this.packageName = packageName;
	this.varsChanged = true; // this may change the visibility of the variable's types
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1065.java