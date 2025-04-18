error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7456.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7456.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7456.java
text:
```scala
protected v@@oid checkBuilderState() {

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
package org.eclipse.jdt.internal.core.eval;

import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.eval.ICodeSnippetRequestor;
import org.eclipse.jdt.core.eval.IEvaluationContext;
import org.eclipse.jdt.core.eval.IGlobalVariable;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.BinaryType;
import org.eclipse.jdt.internal.core.ClassFile;
import org.eclipse.jdt.internal.core.CompletionRequestorWrapper;
import org.eclipse.jdt.internal.core.JavaModelStatus;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.SelectionRequestor;
import org.eclipse.jdt.internal.core.SourceMapper;
import org.eclipse.jdt.internal.core.builder.NameEnvironment;
import org.eclipse.jdt.internal.core.builder.ProblemFactory;
import org.eclipse.jdt.internal.eval.EvaluationContext;
import org.eclipse.jdt.internal.eval.GlobalVariable;
import org.eclipse.jdt.internal.eval.IRequestor;
import org.eclipse.jdt.internal.eval.InstallException;

/**
 * A wrapper around the infrastructure evaluation context. 
 */
public class EvaluationContextWrapper implements IEvaluationContext {
	protected EvaluationContext context;
	protected JavaProject project;
/**
 * Creates a new wrapper around the given infrastructure evaluation context
 * and project.
 */
public EvaluationContextWrapper(EvaluationContext context, JavaProject project) {
	this.context = context;
	this.project = project;
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#allVariables()
 */
public IGlobalVariable[] allVariables() {
	GlobalVariable[] vars = this.context.allVariables();
	int length = vars.length;
	GlobalVariableWrapper[] result = new GlobalVariableWrapper[length];
	for (int i = 0; i < length; i++) {
		result[i] = new GlobalVariableWrapper(vars[i]);
	}
	return result;
}
/**
 * Checks to ensure that there is a previously built state.
 */
protected void checkBuilderState() throws JavaModelException {
	
	return;
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#codeComplete(String, int, ICompletionRequestor)
 */
public void codeComplete(String codeSnippet, int position, ICompletionRequestor requestor) throws JavaModelException {
	codeComplete(codeSnippet, position, requestor, DefaultWorkingCopyOwner.PRIMARY);
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#codeComplete(String, int, ICompletionRequestor, WorkingCopyOwner)
 */
public void codeComplete(String codeSnippet, int position, ICompletionRequestor requestor, WorkingCopyOwner owner) throws JavaModelException {
	NameLookup lookup = null;
	try {
		// set the units to look inside
		lookup = this.project.getNameLookup();
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		ICompilationUnit[] workingCopies = manager.getWorkingCopies(owner, true/*add primary WCs*/);
		lookup.setUnitsToLookInside(workingCopies);

		// code complete
		this.context.complete(
			codeSnippet.toCharArray(),
			position,
			this.project.getSearchableNameEnvironment(),
			new CompletionRequestorWrapper(requestor, lookup),
			this.project.getOptions(true),
			this.project
		);
	} finally {
		if (lookup != null) {
			lookup.setUnitsToLookInside(null);
		}
	}
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#codeSelect(String, int, int)
 */
public IJavaElement[] codeSelect(String codeSnippet, int offset, int length) throws JavaModelException {
	return codeSelect(codeSnippet, offset, length, DefaultWorkingCopyOwner.PRIMARY);
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#codeSelect(String, int, int, WorkingCopyOwner)
 */
public IJavaElement[] codeSelect(String codeSnippet, int offset, int length, WorkingCopyOwner owner) throws JavaModelException {
	NameLookup lookup = null;
	try {
		// set the units to look inside
		lookup = this.project.getNameLookup();
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		ICompilationUnit[] workingCopies = manager.getWorkingCopies(owner, true/*add primary WCs*/);
		lookup.setUnitsToLookInside(workingCopies);

		// code select
		SelectionRequestor requestor= new SelectionRequestor(lookup, null); // null because there is no need to look inside the code snippet itself
		this.context.select(
			codeSnippet.toCharArray(),
			offset,
			offset + length - 1,
			this.project.getSearchableNameEnvironment(),
			requestor,
			this.project.getOptions(true)
		);
		return requestor.getElements();
	} finally {
		if (lookup != null) {
			lookup.setUnitsToLookInside(null);
		}
	}
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#deleteVariable(IGlobalVariable)
 */
public void deleteVariable(IGlobalVariable variable) {
	if (variable instanceof GlobalVariableWrapper) {
		GlobalVariableWrapper wrapper = (GlobalVariableWrapper)variable;
		this.context.deleteVariable(wrapper.variable);
	} else {
		throw new Error("Unknown implementation of IGlobalVariable"); //$NON-NLS-1$
	}
}
/**
 * @see IEvaluationContext#evaluateCodeSnippet(String, String[], String[], int[], IType, boolean, boolean, ICodeSnippetRequestor, IProgressMonitor)
 */
public void evaluateCodeSnippet(
	String codeSnippet, 
	String[] localVariableTypeNames, 
	String[] localVariableNames, 
	int[] localVariableModifiers, 
	IType declaringType, 
	boolean isStatic, 
	boolean isConstructorCall, 
	ICodeSnippetRequestor requestor, 
	IProgressMonitor progressMonitor) throws org.eclipse.jdt.core.JavaModelException {
		
	checkBuilderState();
	
	int length = localVariableTypeNames.length;
	char[][] varTypeNames = new char[length][];
	for (int i = 0; i < length; i++){
		varTypeNames[i] = localVariableTypeNames[i].toCharArray();
	}

	length = localVariableNames.length;
	char[][] varNames = new char[length][];
	for (int i = 0; i < length; i++){
		varNames[i] = localVariableNames[i].toCharArray();
	}

	Map options = this.project.getOptions(true);
	// transfer the imports of the IType to the evaluation context
	if (declaringType != null) {
		// retrieves the package statement 
		this.context.setPackageName(declaringType.getPackageFragment().getElementName().toCharArray());
		ICompilationUnit compilationUnit = declaringType.getCompilationUnit();
		if (compilationUnit != null) {
			// retrieves the import statement
			IImportDeclaration[] imports = compilationUnit.getImports();
			int importsLength = imports.length;
			if (importsLength != 0) {
				char[][] importsNames = new char[importsLength][];
				for (int i = 0; i < importsLength; i++) {
					importsNames[i] = imports[i].getElementName().toCharArray();
				}
				this.context.setImports(importsNames);
				// turn off import complaints for implicitly added ones
				options.put(CompilerOptions.OPTION_ReportUnusedImport, CompilerOptions.IGNORE);
				options.put(CompilerOptions.OPTION_ReportInvalidImport, CompilerOptions.IGNORE);
			}
		} else {
			// try to retrieve imports from the source
			SourceMapper sourceMapper = ((ClassFile) declaringType.getClassFile()).getSourceMapper();
			if (sourceMapper != null) {
				char[][] imports = sourceMapper.getImports((BinaryType) declaringType);
				if (imports != null) {
					this.context.setImports(imports);
					// turn off import complaints for implicitly added ones
					options.put(CompilerOptions.OPTION_ReportUnusedImport, CompilerOptions.IGNORE);
					options.put(CompilerOptions.OPTION_ReportInvalidImport, CompilerOptions.IGNORE);
				}
			}
		}
	}
	INameEnvironment environment = null;
	try {
		this.context.evaluate(
			codeSnippet.toCharArray(),
			varTypeNames,
			varNames,
			localVariableModifiers,
			declaringType == null? null : declaringType.getFullyQualifiedName().toCharArray(),
			isStatic,
			isConstructorCall,
			environment = getBuildNameEnvironment(), 
			options, 
			getInfrastructureEvaluationRequestor(requestor), 
			getProblemFactory());
	} catch (InstallException e) {
		handleInstallException(e);
	} finally {
		if (environment != null) environment.cleanup();
	}
}
/**
 * @see IEvaluationContext#evaluateCodeSnippet(String, ICodeSnippetRequestor, IProgressMonitor)
 */
public void evaluateCodeSnippet(String codeSnippet, ICodeSnippetRequestor requestor, IProgressMonitor progressMonitor) throws JavaModelException {

	checkBuilderState();
	INameEnvironment environment = null;
	try {
		this.context.evaluate(
			codeSnippet.toCharArray(), 
			environment = getBuildNameEnvironment(), 
			this.project.getOptions(true), 
			getInfrastructureEvaluationRequestor(requestor), 
			getProblemFactory());
	} catch (InstallException e) {
		handleInstallException(e);
	} finally {
		if (environment != null) environment.cleanup();
	}
}
/**
 * @see IEvaluationContext#evaluateVariable(IGlobalVariable, ICodeSnippetRequestor, IProgressMonitor)
 */
public void evaluateVariable(IGlobalVariable variable, ICodeSnippetRequestor requestor, IProgressMonitor progressMonitor) throws JavaModelException {

	checkBuilderState();
	INameEnvironment environment = null;
	try {
		this.context.evaluateVariable(
			((GlobalVariableWrapper)variable).variable, 
			environment = getBuildNameEnvironment(), 
			this.project.getOptions(true), 
			getInfrastructureEvaluationRequestor(requestor), 
			getProblemFactory());
	} catch (InstallException e) {
		handleInstallException(e);
	} finally {
		if (environment != null) environment.cleanup();
	}
}
/**
 * Returns a name environment for the last built state.
 */
protected INameEnvironment getBuildNameEnvironment() throws JavaModelException {
	return new NameEnvironment(getProject());
}

/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#getImports()
 */
public String[] getImports() {
	char[][] imports = this.context.getImports();
	int length = imports.length;
	String[] result = new String[length];
	for (int i = 0; i < length; i++) {
		result[i] = new String(imports[i]);
	}
	return result;
}
/**
 * Returns the infrastructure evaluation context.
 */
public EvaluationContext getInfrastructureEvaluationContext() {
	return this.context;
}
/**
 * Returns a new infrastructure evaluation requestor instance.
 */
protected IRequestor getInfrastructureEvaluationRequestor(ICodeSnippetRequestor requestor) {
	return new RequestorWrapper(requestor);
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#getPackageName()
 */
public String getPackageName() {
	return new String(this.context.getPackageName());
}
/**
 * Returns the problem factory to be used during evaluation.
 */
protected IProblemFactory getProblemFactory() throws JavaModelException {
	return ProblemFactory.getProblemFactory(Locale.getDefault());
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#getProject()
 */
public IJavaProject getProject() {
	return this.project;
}
/**
 * Handles an install exception by throwing a Java Model exception.
 */
protected void handleInstallException(InstallException e) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.EVALUATION_ERROR, e.toString()));
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#newVariable(String, String, String)
 */
public IGlobalVariable newVariable(String typeName, String name, String initializer) {
	GlobalVariable newVar = 
		this.context.newVariable(
			typeName.toCharArray(), 
			name.toCharArray(), 
			(initializer == null) ?
				null :
				initializer.toCharArray());
	return new GlobalVariableWrapper(newVar);
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#setImports(String[])
 */
public void setImports(String[] imports) {
	int length = imports.length;
	char[][] result = new char[length][];
	for (int i = 0; i < length; i++) {
		result[i] = imports[i].toCharArray();
	}
	this.context.setImports(result);
}
/**
 * @see org.eclipse.jdt.core.eval.IEvaluationContext#setPackageName(String)
 */
public void setPackageName(String packageName) {
	this.context.setPackageName(packageName.toCharArray());
}
/**
 * @see IEvaluationContext#validateImports(ICodeSnippetRequestor)
 */
public void validateImports(ICodeSnippetRequestor requestor) throws JavaModelException {
	
	checkBuilderState();
	INameEnvironment environment = null;
	try {
		this.context.evaluateImports(
			environment = getBuildNameEnvironment(), 
			getInfrastructureEvaluationRequestor(requestor), 
			getProblemFactory());
	} finally {
		if (environment != null) environment.cleanup();
	}
}
/**
 * @see IEvaluationContext#codeComplete(String, int, ICodeCompletionRequestor)
 * @deprecated - use codeComplete(String, int, ICompletionRequestor) instead
 */
public void codeComplete(String codeSnippet, int position, final org.eclipse.jdt.core.ICodeCompletionRequestor requestor) throws JavaModelException {

	if (requestor == null){
		codeComplete(codeSnippet, position, (ICompletionRequestor)null);
		return;
	}
	codeComplete(
		codeSnippet,
		position,
		new ICompletionRequestor(){
			public void acceptAnonymousType(char[] superTypePackageName,char[] superTypeName,char[][] parameterPackageNames,char[][] parameterTypeNames,char[][] parameterNames,char[] completionName,int modifiers,int completionStart,int completionEnd, int relevance) {
				// implements interface method
			}
			public void acceptClass(char[] packageName, char[] className, char[] completionName, int modifiers, int completionStart, int completionEnd, int relevance) {
				requestor.acceptClass(packageName, className, completionName, modifiers, completionStart, completionEnd);
			}
			public void acceptError(IProblem error) {
				if (true) return; // was disabled in 1.0

				try {
					IMarker marker = ResourcesPlugin.getWorkspace().getRoot().createMarker(IJavaModelMarker.TRANSIENT_PROBLEM);
					marker.setAttribute(IJavaModelMarker.ID, error.getID());
					marker.setAttribute(IMarker.CHAR_START, error.getSourceStart());
					marker.setAttribute(IMarker.CHAR_END, error.getSourceEnd() + 1);
					marker.setAttribute(IMarker.LINE_NUMBER, error.getSourceLineNumber());
					marker.setAttribute(IMarker.MESSAGE, error.getMessage());
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					requestor.acceptError(marker);
				} catch(CoreException e){
					// ignore
				}
			}
			public void acceptField(char[] declaringTypePackageName, char[] declaringTypeName, char[] name, char[] typePackageName, char[] typeName, char[] completionName, int modifiers, int completionStart, int completionEnd, int relevance) {
				requestor.acceptField(declaringTypePackageName, declaringTypeName, name, typePackageName, typeName, completionName, modifiers, completionStart, completionEnd);
			}
			public void acceptInterface(char[] packageName,char[] interfaceName,char[] completionName,int modifiers,int completionStart,int completionEnd, int relevance) {
				requestor.acceptInterface(packageName, interfaceName, completionName, modifiers, completionStart, completionEnd);
			}
			public void acceptKeyword(char[] keywordName,int completionStart,int completionEnd, int relevance){
				requestor.acceptKeyword(keywordName, completionStart, completionEnd);
			}
			public void acceptLabel(char[] labelName,int completionStart,int completionEnd, int relevance){
				requestor.acceptLabel(labelName, completionStart, completionEnd);
			}
			public void acceptLocalVariable(char[] name,char[] typePackageName,char[] typeName,int modifiers,int completionStart,int completionEnd, int relevance){
				// ignore
			}
			public void acceptMethod(char[] declaringTypePackageName,char[] declaringTypeName,char[] selector,char[][] parameterPackageNames,char[][] parameterTypeNames,char[][] parameterNames,char[] returnTypePackageName,char[] returnTypeName,char[] completionName,int modifiers,int completionStart,int completionEnd, int relevance){
				// skip parameter names
				requestor.acceptMethod(declaringTypePackageName, declaringTypeName, selector, parameterPackageNames, parameterTypeNames, returnTypePackageName, returnTypeName, completionName, modifiers, completionStart, completionEnd);
			}
			public void acceptMethodDeclaration(char[] declaringTypePackageName,char[] declaringTypeName,char[] selector,char[][] parameterPackageNames,char[][] parameterTypeNames,char[][] parameterNames,char[] returnTypePackageName,char[] returnTypeName,char[] completionName,int modifiers,int completionStart,int completionEnd, int relevance){
				// ignore
			}
			public void acceptModifier(char[] modifierName,int completionStart,int completionEnd, int relevance){
				requestor.acceptModifier(modifierName, completionStart, completionEnd);
			}
			public void acceptPackage(char[] packageName,char[] completionName,int completionStart,int completionEnd, int relevance){
				requestor.acceptPackage(packageName, completionName, completionStart, completionEnd);
			}
			public void acceptType(char[] packageName,char[] typeName,char[] completionName,int completionStart,int completionEnd, int relevance){
				requestor.acceptType(packageName, typeName, completionName, completionStart, completionEnd);
			}
			public void acceptVariableName(char[] typePackageName,char[] typeName,char[] name,char[] completionName,int completionStart,int completionEnd, int relevance){
				// ignore
			}
		});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7456.java