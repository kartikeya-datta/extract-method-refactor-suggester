error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/426.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/426.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/426.java
text:
```scala
I@@ndexManager indexManager = JavaModelManager.getIndexManager();

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
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.env.AccessRuleSet;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.search.*;
import org.eclipse.jdt.internal.core.search.indexing.IIndexConstants;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;
import org.eclipse.jdt.internal.core.util.ASTNodeFinder;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * Collects the super type names of a given declaring type.
 * Returns NOT_FOUND_DECLARING_TYPE if the declaring type was not found.
 * Returns null if the declaring type pattern doesn't require an exact match.
 */
public class SuperTypeNamesCollector {

	/**
	 * An ast visitor that visits type declarations and member type declarations
	 * collecting their super type names.
	 */
	public class TypeDeclarationVisitor extends ASTVisitor {
		public boolean visit(TypeDeclaration typeDeclaration, BlockScope scope) {
			ReferenceBinding binding = typeDeclaration.binding;
			if (SuperTypeNamesCollector.this.matches(binding))
				SuperTypeNamesCollector.this.collectSuperTypeNames(binding);
			return true;
		}
		public boolean visit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
			ReferenceBinding binding = typeDeclaration.binding;
			if (SuperTypeNamesCollector.this.matches(binding))
				SuperTypeNamesCollector.this.collectSuperTypeNames(binding);
			return true;
		}
		public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
			ReferenceBinding binding = memberTypeDeclaration.binding;
			if (SuperTypeNamesCollector.this.matches(binding))
				SuperTypeNamesCollector.this.collectSuperTypeNames(binding);
			return true;
		}
		public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
			return false; // don't visit field declarations
		}
		public boolean visit(Initializer initializer, MethodScope scope) {
			return false; // don't visit initializers
		}
		public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
			return false; // don't visit constructor declarations
		}
		public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
			return false; // don't visit method declarations
		}
	}
SearchPattern pattern;
char[] typeSimpleName;
char[] typeQualification;
MatchLocator locator;
IType type; 
IProgressMonitor progressMonitor;
char[][][] result;
int resultIndex;

public SuperTypeNamesCollector(
	SearchPattern pattern,
	char[] typeSimpleName,
	char[] typeQualification,
	MatchLocator locator,
	IType type, 
	IProgressMonitor progressMonitor) {

	this.pattern = pattern;
	this.typeSimpleName = typeSimpleName;
	this.typeQualification = typeQualification;
	this.locator = locator;
	this.type = type;
	this.progressMonitor = progressMonitor;
}

protected void addToResult(char[][] compoundName) {
	int resultLength = this.result.length;
	for (int i = 0; i < resultLength; i++)
		if (CharOperation.equals(this.result[i], compoundName)) return; // already known

	if (resultLength == this.resultIndex)
		System.arraycopy(this.result, 0, this.result = new char[resultLength*2][][], 0, resultLength);
	this.result[this.resultIndex++] = compoundName;
}
/*
 * Parse the given compiation unit and build its type bindings.
 */
protected CompilationUnitDeclaration buildBindings(ICompilationUnit compilationUnit, boolean isTopLevelOrMember) throws JavaModelException {
	// source unit
	org.eclipse.jdt.internal.compiler.env.ICompilationUnit sourceUnit = (org.eclipse.jdt.internal.compiler.env.ICompilationUnit) compilationUnit;

	CompilationResult compilationResult = new CompilationResult(sourceUnit, 1, 1, 0);
	CompilationUnitDeclaration unit = 
		isTopLevelOrMember ?
			this.locator.basicParser().dietParse(sourceUnit, compilationResult) :
			this.locator.basicParser().parse(sourceUnit, compilationResult);
	if (unit != null) {
		this.locator.lookupEnvironment.buildTypeBindings(unit, null /*no access restriction*/);
		this.locator.lookupEnvironment.completeTypeBindings(unit, !isTopLevelOrMember);
		if (!isTopLevelOrMember) {
			if (unit.scope != null)
				unit.scope.faultInTypes(); // fault in fields & methods
			unit.resolve();
		}
	}
	return unit;
}
public char[][][] collect() throws JavaModelException {
	if (this.type != null) {
		// Collect the paths of the cus that are in the hierarchy of the given type
		this.result = new char[1][][];
		this.resultIndex = 0;
		JavaProject javaProject = (JavaProject) this.type.getJavaProject();
		this.locator.initialize(javaProject, 0);
		try {
			if (this.type.isBinary()) {
				BinaryTypeBinding binding = this.locator.cacheBinaryType(this.type, null);
				if (binding != null)
					collectSuperTypeNames(binding);
			} else {
				ICompilationUnit unit = this.type.getCompilationUnit();
				SourceType sourceType = (SourceType) this.type;
				boolean isTopLevelOrMember = sourceType.getOuterMostLocalContext() == null;
				CompilationUnitDeclaration parsedUnit = buildBindings(unit, isTopLevelOrMember);
				if (parsedUnit != null) {
					TypeDeclaration typeDecl = new ASTNodeFinder(parsedUnit).findType(this.type);
					if (typeDecl != null && typeDecl.binding != null) 
						collectSuperTypeNames(typeDecl.binding);
				}
			}
		} catch (AbortCompilation e) {
			// problem with classpath: report inacurrate matches
			return null;
		}
		if (this.result.length > this.resultIndex)
			System.arraycopy(this.result, 0, this.result = new char[this.resultIndex][][], 0, this.resultIndex);
		return this.result;
	}

	// Collect the paths of the cus that declare a type which matches declaringQualification + declaringSimpleName
	String[] paths = this.getPathsOfDeclaringType();
	if (paths == null) return null;

	// Create bindings from source types and binary types and collect super type names of the type declaration 
	// that match the given declaring type
	Util.sort(paths); // sort by projects
	JavaProject previousProject = null;
	this.result = new char[1][][];
	this.resultIndex = 0;
	for (int i = 0, length = paths.length; i < length; i++) {
		try {
			Openable openable = this.locator.handleFactory.createOpenable(paths[i], this.locator.scope);
			if (openable == null) continue; // outside classpath

			IJavaProject project = openable.getJavaProject();
			if (!project.equals(previousProject)) {
				previousProject = (JavaProject) project;
				this.locator.initialize(previousProject, 0);
			}
			if (openable instanceof ICompilationUnit) {
				ICompilationUnit unit = (ICompilationUnit) openable;
				CompilationUnitDeclaration parsedUnit = buildBindings(unit, true /*only toplevel and member types are visible to the focus type*/);
				if (parsedUnit != null)
					parsedUnit.traverse(new TypeDeclarationVisitor(), parsedUnit.scope);
			} else if (openable instanceof IClassFile) {
				IClassFile classFile = (IClassFile) openable;
				BinaryTypeBinding binding = this.locator.cacheBinaryType(classFile.getType(), null);
				if (matches(binding))
					collectSuperTypeNames(binding);
			}
		} catch (AbortCompilation e) {
			// ignore: continue with next element
		} catch (JavaModelException e) {
			// ignore: continue with next element
		}
	}
	if (this.result.length > this.resultIndex)
		System.arraycopy(this.result, 0, this.result = new char[this.resultIndex][][], 0, this.resultIndex);
	return this.result;
}
/**
 * Collects the names of all the supertypes of the given type.
 */
protected void collectSuperTypeNames(ReferenceBinding binding) {
	ReferenceBinding superclass = binding.superclass();
	if (superclass != null) {
		this.addToResult(superclass.compoundName);
		this.collectSuperTypeNames(superclass);
	}

	ReferenceBinding[] interfaces = binding.superInterfaces();
	if (interfaces != null) {
		for (int i = 0; i < interfaces.length; i++) {
			ReferenceBinding interfaceBinding = interfaces[i];
			this.addToResult(interfaceBinding.compoundName);
			this.collectSuperTypeNames(interfaceBinding);
		}
	}
}
protected String[] getPathsOfDeclaringType() {
	if (this.typeQualification == null && this.typeSimpleName == null) return null;

	final PathCollector pathCollector = new PathCollector();
	IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
	SearchPattern searchPattern = new TypeDeclarationPattern(
		this.typeSimpleName != null ? null : this.typeQualification, // use the qualification only if no simple name
		null, // do find member types
		this.typeSimpleName,
		IIndexConstants.TYPE_SUFFIX,
		this.pattern.getMatchRule());
	IndexQueryRequestor searchRequestor = new IndexQueryRequestor(){
		public boolean acceptIndexMatch(String documentPath, SearchPattern indexRecord, SearchParticipant participant, AccessRuleSet access) {
			TypeDeclarationPattern record = (TypeDeclarationPattern)indexRecord;
			if (record.enclosingTypeNames != IIndexConstants.ONE_ZERO_CHAR) {  // filter out local and anonymous classes
				pathCollector.acceptIndexMatch(documentPath, indexRecord, participant, access);
			}
			return true;
		}		
	};		

	indexManager.performConcurrentJob(
		new PatternSearchJob(
			searchPattern, 
			new JavaSearchParticipant(),
			scope, 
			searchRequestor),
		IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
		progressMonitor == null ? null : new SubProgressMonitor(progressMonitor, 100));
	return pathCollector.getPaths();
}
protected boolean matches(char[][] compoundName) {
	int length = compoundName.length;
	if (length == 0) return false;
	char[] simpleName = compoundName[length-1];
	int last = length - 1;
	if (this.typeSimpleName == null || this.pattern.matchesName(simpleName, this.typeSimpleName)) {
		// most frequent case: simple name equals last segment of compoundName
		char[][] qualification = new char[last][];
		System.arraycopy(compoundName, 0, qualification, 0, last);
		return this.pattern.matchesName(this.typeQualification, CharOperation.concatWith(qualification, '.'));
	}

	if (!CharOperation.endsWith(simpleName, this.typeSimpleName)) return false;

	// member type -> transform A.B.C$D into A.B.C.D
	System.arraycopy(compoundName, 0, compoundName = new char[length+1][], 0, last);
	int dollar = CharOperation.indexOf('$', simpleName);
	if (dollar == -1) return false;
	compoundName[last] = CharOperation.subarray(simpleName, 0, dollar);
	compoundName[length] = CharOperation.subarray(simpleName, dollar+1, simpleName.length); 
	return this.matches(compoundName);
}
protected boolean matches(ReferenceBinding binding) {
	return binding != null && binding.compoundName != null && this.matches(binding.compoundName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/426.java