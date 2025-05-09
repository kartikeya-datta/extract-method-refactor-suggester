error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9124.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9124.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9124.java
text:
```scala
p@@attern.mustResolve = true; // force resolve to compute type bindings

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
package org.eclipse.jdt.core.search;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.search.*;
import org.eclipse.jdt.internal.core.search.HierarchyScope;
import org.eclipse.jdt.internal.core.search.IndexSearchAdapter;
import org.eclipse.jdt.internal.core.search.IIndexSearchRequestor;
import org.eclipse.jdt.internal.core.search.IInfoConstants;
import org.eclipse.jdt.internal.core.search.JavaSearchScope;
import org.eclipse.jdt.internal.core.search.JavaWorkspaceScope;
import org.eclipse.jdt.internal.core.search.PatternSearchJob;
import org.eclipse.jdt.internal.core.search.PathCollector;
import org.eclipse.jdt.internal.core.search.Util;
import org.eclipse.jdt.internal.core.search.indexing.*;
import org.eclipse.jdt.internal.core.search.matching.*;

import java.util.*;

/**
 * A <code>SearchEngine</code> searches for java elements following a search pattern.
 * The search can be limited to a search scope.
 * <p>
 * Various search patterns can be created using the factory methods 
 * <code>createSearchPattern(String, int, int, boolean)</code>, <code>createSearchPattern(IJavaElement, int)</code>,
 * <code>createOrSearchPattern(ISearchPattern, ISearchPattern)</code>.
 * </p>
 * <p>For example, one can search for references to a method in the hierarchy of a type, 
 * or one can search for the declarations of types starting with "Abstract" in a project.
 * </p>
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * TODO: remove IWorkspace argument on the search methods before 3.0
 */
public class SearchEngine {

/**
 * A list of working copies that take precedence over their original 
 * compilation units.
 */
private ICompilationUnit[] workingCopies = null;

/**
 * For tracing purpose.
 */	
public static boolean VERBOSE = false;	

/**
 * Creates a new search engine.
 */
public SearchEngine() {
}
/**
 * Creates a new search engine with a list of working copies that will take precedence over 
 * their original compilation units in the subsequent search operations.
 * <p>
 * Note that passing an empty working copy will be as if the original compilation
 * unit had been deleted.</p>
 * 
 * @param workingCopies the working copies that take precedence over their original compilation units
 * @since 2.0
 * TODO remove before 3.0
 */
public SearchEngine(IWorkingCopy[] workingCopies) {
	int length = workingCopies.length;
	System.arraycopy(workingCopies, 0, this.workingCopies = new ICompilationUnit[length], 0, length);
}
/**
 * Creates a new search engine with the given working copy owner.
 * The working copies owned by this owner will take precedence over 
 * the primary compilation units in the subsequent search operations.
 * 
 * @param workingCopies the working copies that take precedence over their original compilation units
 * @since 3.0
 */
public SearchEngine(WorkingCopyOwner workingCopyOwner) {
	this.workingCopies = JavaModelManager.getJavaModelManager().getWorkingCopies(workingCopyOwner, true/*add primary WCs*/);
}
/*
 * Removes from the given list of working copies the ones that cannot see the given focus.
 */
private ICompilationUnit[] filterWorkingCopies(ICompilationUnit[] copies, IJavaElement focus, boolean isPolymorphicSearch) {
	if (focus == null || copies == null) return copies;
	while (!(focus instanceof IJavaProject) && !(focus instanceof JarPackageFragmentRoot)) {
		focus = focus.getParent();
	}
	int length = copies.length;
	ICompilationUnit[] result = null;
	int index = -1;
	for (int i=0; i<length; i++) {
		ICompilationUnit workingCopy = copies[i];
		IPath projectOrJar = IndexSelector.getProjectOrJar(workingCopy).getPath();
		if (!IndexSelector.canSeeFocus(focus, isPolymorphicSearch, projectOrJar)) {
			if (result == null) {
				result = new ICompilationUnit[length-1];
				System.arraycopy(copies, 0, result, 0, i);
				index = i;
			}
		} else if (result != null) {
			result[index++] = workingCopy;
		}
	}
	if (result != null) {
		if (result.length != index) {
			System.arraycopy(result, 0, result = new ICompilationUnit[index], 0, index);
		}
		return result;
	} else {
		return copies;
	}
}
/**
 * Returns a java search scope limited to the hierarchy of the given type.
 * The java elements resulting from a search with this scope will
 * be types in this hierarchy, or members of the types in this hierarchy.
 *
 * @param type the focus of the hierarchy scope
 * @return a new hierarchy scope
 * @exception JavaModelException if the hierarchy could not be computed on the given type
 */
public static IJavaSearchScope createHierarchyScope(IType type) throws JavaModelException {
	return createHierarchyScope(type, DefaultWorkingCopyOwner.PRIMARY);
}
/**
 * Returns a java search scope limited to the hierarchy of the given type.
 * When the hierarchy is computed, the types defined in the working copies owned
 * by the given owner take precedence over the original compilation units.
 * The java elements resulting from a search with this scope will
 * be types in this hierarchy, or members of the types in this hierarchy.
 *
 * @param type the focus of the hierarchy scope
 * @param the owner of working copies that take precedence over original compilation units
 * @return a new hierarchy scope
 * @exception JavaModelException if the hierarchy could not be computed on the given type
 * @since 3.0
 */
public static IJavaSearchScope createHierarchyScope(IType type, WorkingCopyOwner owner) throws JavaModelException {
	return new HierarchyScope(type, owner);
}
/**
 * Returns a java search scope limited to the given resources.
 * The java elements resulting from a search with this scope will
 * have their underlying resource included in or equals to one of the given
 * resources.
 * <p>
 * Resources must not overlap, for example, one cannot include a folder and its children.
 * </p>
 *
 * @param resources the resources the scope is limited to
 * @return a new java search scope
 * @deprecated Use createJavaSearchScope(IJavaElement[]) instead
 */
public static IJavaSearchScope createJavaSearchScope(IResource[] resources) {
	int length = resources.length;
	IJavaElement[] elements = new IJavaElement[length];
	for (int i = 0; i < length; i++) {
		elements[i] = JavaCore.create(resources[i]);
	}
	return createJavaSearchScope(elements);
}
/**
 * Returns a java search scope limited to the given java elements.
 * The java elements resulting from a search with this scope will
 * be children of the given elements.
 * <p>
 * If an element is an IJavaProject, then the project's source folders, 
 * its jars (external and internal) and its referenced projects (with their source 
 * folders and jars, recursively) will be included.
 * If an element is an IPackageFragmentRoot, then only the package fragments of 
 * this package fragment root will be included.
 * If an element is an IPackageFragment, then only the compilation unit and class 
 * files of this package fragment will be included. Subpackages will NOT be 
 * included.</p>
 * <p>
 * In other words, this is equivalent to using SearchEngine.createJavaSearchScope(elements, true).</p>
 *
 * @param elements the java elements the scope is limited to
 * @return a new java search scope
 * @since 2.0
 */
public static IJavaSearchScope createJavaSearchScope(IJavaElement[] elements) {
	return createJavaSearchScope(elements, true);
}
/**
 * Returns a java search scope limited to the given java elements.
 * The java elements resulting from a search with this scope will
 * be children of the given elements.
 * 
 * If an element is an IJavaProject, then the project's source folders, 
 * its jars (external and internal) and - if specified - its referenced projects 
 * (with their source folders and jars, recursively) will be included.
 * If an element is an IPackageFragmentRoot, then only the package fragments of 
 * this package fragment root will be included.
 * If an element is an IPackageFragment, then only the compilation unit and class 
 * files of this package fragment will be included. Subpackages will NOT be 
 * included.
 *
 * @param elements the java elements the scope is limited to
 * @param includeReferencedProjects a flag indicating if referenced projects must be 
 * 									 recursively included
 * @return a new java search scope
 * @since 2.0
 */
public static IJavaSearchScope createJavaSearchScope(IJavaElement[] elements, boolean includeReferencedProjects) {
	JavaSearchScope scope = new JavaSearchScope();
	HashSet visitedProjects = new HashSet(2);
	for (int i = 0, length = elements.length; i < length; i++) {
		IJavaElement element = elements[i];
		if (element != null) {
			try {
				if (element instanceof IJavaProject) {
					scope.add((IJavaProject)element, includeReferencedProjects, visitedProjects);
				} else {
					scope.add(element);
				}
			} catch (JavaModelException e) {
				// ignore
			}
		}
	}
	return scope;
}
/**
 * Returns a search pattern that combines the given two patterns into a "or" pattern.
 * The search result will match either the left pattern or the right pattern.
 *
 * @param leftPattern the left pattern
 * @param rightPattern the right pattern
 * @return a "or" pattern
 */
public static ISearchPattern createOrSearchPattern(ISearchPattern leftPattern, ISearchPattern rightPattern) {
	return new OrPattern((SearchPattern)leftPattern, (SearchPattern)rightPattern);
}
/**
 * Returns a search pattern based on a given string pattern. The string patterns support '*' wild-cards.
 * The remaining parameters are used to narrow down the type of expected results.
 *
 * <br>
 *	Examples:
 *	<ul>
 * 		<li>search for case insensitive references to <code>Object</code>:
 *			<code>createSearchPattern("Object", TYPE, REFERENCES, false);</code></li>
 *  	<li>search for case sensitive references to exact <code>Object()</code> constructor:
 *			<code>createSearchPattern("java.lang.Object()", CONSTRUCTOR, REFERENCES, true);</code></li>
 *  	<li>search for implementers of <code>java.lang.Runnable</code>:
 *			<code>createSearchPattern("java.lang.Runnable", TYPE, IMPLEMENTORS, true);</code></li>
 *  </ul>
 * @param stringPattern the given pattern
 * @param searchFor determines the nature of the searched elements
 *	<ul>
 * 		<li><code>IJavaSearchConstants.CLASS</code>: only look for classes</li>
 *		<li><code>IJavaSearchConstants.INTERFACE</code>: only look for interfaces</li>
 * 		<li><code>IJavaSearchConstants.TYPE</code>: look for both classes and interfaces</li>
 *		<li><code>IJavaSearchConstants.FIELD</code>: look for fields</li>
 *		<li><code>IJavaSearchConstants.METHOD</code>: look for methods</li>
 *		<li><code>IJavaSearchConstants.CONSTRUCTOR</code>: look for constructors</li>
 *		<li><code>IJavaSearchConstants.PACKAGE</code>: look for packages</li>
 *	</ul>
 * @param limitTo determines the nature of the expected matches
 *	<ul>
 * 		<li><code>IJavaSearchConstants.DECLARATIONS</code>: will search declarations matching with the corresponding
 * 			element. In case the element is a method, declarations of matching methods in subtypes will also
 *  		be found, allowing to find declarations of abstract methods, etc.</li>
 *
 *		 <li><code>IJavaSearchConstants.REFERENCES</code>: will search references to the given element.</li>
 *
 *		 <li><code>IJavaSearchConstants.ALL_OCCURRENCES</code>: will search for either declarations or references as specified
 *  		above.</li>
 *
 *		 <li><code>IJavaSearchConstants.IMPLEMENTORS</code>: for interface, will find all types which implements a given interface.</li>
 *	</ul>
 *
 * @param isCaseSensitive indicates whether the search is case sensitive or not.
 * @return a search pattern on the given string pattern, or <code>null</code> if the string pattern is ill-formed.
 */
public static ISearchPattern createSearchPattern(String stringPattern, int searchFor, int limitTo, boolean isCaseSensitive) {
	int matchMode;
	if (stringPattern.indexOf('*') != -1 || stringPattern.indexOf('?') != -1) {
		matchMode = IJavaSearchConstants.PATTERN_MATCH;
	} else {
		matchMode = IJavaSearchConstants.EXACT_MATCH;
	}
	return SearchPattern.createPattern(stringPattern, searchFor, limitTo, matchMode, isCaseSensitive);
}
/**
 * Returns a search pattern based on a given Java element. 
 * The pattern is used to trigger the appropriate search, and can be parameterized as follows:
 *
 * @param element the java element the search pattern is based on
 * @param limitTo determines the nature of the expected matches
 * 	<ul>
 * 		<li><code>IJavaSearchConstants.DECLARATIONS</code>: will search declarations matching with the corresponding
 * 			element. In case the element is a method, declarations of matching methods in subtypes will also
 *  		be found, allowing to find declarations of abstract methods, etc.</li>
 *
 *		 <li><code>IJavaSearchConstants.REFERENCES</code>: will search references to the given element.</li>
 *
 *		 <li><code>IJavaSearchConstants.ALL_OCCURRENCES</code>: will search for either declarations or references as specified
 *  		above.</li>
 *
 *		 <li><code>IJavaSearchConstants.IMPLEMENTORS</code>: for interface, will find all types which implements a given interface.</li>
 *	</ul>
 * @return a search pattern for a java element or <code>null</code> if the given element is ill-formed
 */
public static ISearchPattern createSearchPattern(IJavaElement element, int limitTo) {

	return SearchPattern.createPattern(element, limitTo);
}
/**
 * Returns a java search scope with the workspace as the only limit.
 *
 * @return a new workspace scope
 */
public static IJavaSearchScope createWorkspaceScope() {
	return new JavaWorkspaceScope();
}
/**
 * Returns the underlying resource of the given element.
 */
private IResource getResource(IJavaElement element) {
	if (element instanceof IMember) {
		ICompilationUnit cu = ((IMember)element).getCompilationUnit();
		if (cu != null) {
			return cu.getResource();
		} 
	} 
	return element.getResource();
}
/**
 * Returns the list of working copies used to do the search on the given java element.
 */
private ICompilationUnit[] getWorkingCopies(IJavaElement element) {
	if (element instanceof IMember) {
		ICompilationUnit cu = ((IMember)element).getCompilationUnit();
		if (cu != null && cu.isWorkingCopy()) {
			int length = this.workingCopies == null ? 0 : this.workingCopies.length;
			if (length > 0) {
				ICompilationUnit[] newWorkingCopies = new ICompilationUnit[length+1];
				System.arraycopy(this.workingCopies, 0, newWorkingCopies, 0, length);
				newWorkingCopies[length] = cu;
				return newWorkingCopies;
			} else {
				return new ICompilationUnit[] {cu};
			}
		}
	}
	return this.workingCopies;
}
/**
 * Searches for the Java element determined by the given signature. The signature
 * can be incomplete. For example, a call like 
 * <code>search(ws, "run()", METHOD,REFERENCES, col)</code>
 * searches for all references to the method <code>run</code>.
 *
 * Note that by default the pattern will be case insensitive. For specifying case s
 * sensitive search, use <code>search(workspace, createSearchPattern(patternString, searchFor, limitTo, true), scope, resultCollector);</code>
 * 
 * @param workspace the workspace
 * @param pattern the pattern to be searched for
 * @param searchFor a hint what kind of Java element the string pattern represents.
 *  Look into <code>IJavaSearchConstants</code> for valid values
 * @param limitTo one of the following values:
 *	<ul>
 *	  <li><code>IJavaSearchConstants.DECLARATIONS</code>: search 
 *		  for declarations only </li>
 *	  <li><code>IJavaSearchConstants.REFERENCES</code>: search 
 *		  for all references </li>
 *	  <li><code>IJavaSearchConstants.ALL_OCCURENCES</code>: search 
 *		  for both declarations and all references </li>
 *	  <li><code>IJavaSearchConstants.IMPLEMENTORS</code>: search for
 *		  all implementors of an interface; the value is only valid if
 *		  the Java element represents an interface</li>
 * 	</ul>
 * @param scope the search result has to be limited to the given scope
 * @param resultCollector a callback object to which each match is reported	 
 * @exception JavaModelException if the search failed. Reasons include:
 *	<ul>
 *		<li>the classpath is incorrectly set</li>
 *	</ul>
 */
public void search(IWorkspace workspace, String patternString, int searchFor, int limitTo, IJavaSearchScope scope, IJavaSearchResultCollector resultCollector) throws JavaModelException {

	search(workspace, createSearchPattern(patternString, searchFor, limitTo, true), scope, resultCollector);
}
/**
 * Searches for the given Java element.
 *
 * @param workspace the workspace
 * @param element the Java element to be searched for
 * @param limitTo one of the following values:
 *	<ul>
 *	  <li><code>IJavaSearchConstants.DECLARATIONS</code>: search 
 *		  for declarations only </li>
 *	  <li><code>IJavaSearchConstants.REFERENCES</code>: search 
 *		  for all references </li>
 *	  <li><code>IJavaSearchConstants.ALL_OCCURENCES</code>: search 
 *		  for both declarations and all references </li>
 *	  <li><code>IJavaSearchConstants.IMPLEMENTORS</code>: search for
 *		  all implementors of an interface; the value is only valid if
 *		  the Java element represents an interface</li>
 * 	</ul>
 * @param scope the search result has to be limited to the given scope
 * @param resultCollector a callback object to which each match is reported
 * @exception JavaModelException if the search failed. Reasons include:
 *	<ul>
 *		<li>the element doesn't exist</li>
 *		<li>the classpath is incorrectly set</li>
 *	</ul>
 */
public void search(IWorkspace workspace, IJavaElement element, int limitTo, IJavaSearchScope scope, IJavaSearchResultCollector resultCollector) throws JavaModelException {

	search(workspace, createSearchPattern(element, limitTo), scope, resultCollector);
}
/**
 * Searches for matches of a given search pattern. Search patterns can be created using helper
 * methods (from a String pattern or a Java element) and encapsulate the description of what is
 * being searched (for example, search method declarations in a case sensitive way).
 *
 * @param workspace the workspace
 * @param searchPattern the pattern to be searched for
 * @param scope the search result has to be limited to the given scope
 * @param resultCollector a callback object to which each match is reported
 * @exception JavaModelException if the search failed. Reasons include:
 *	<ul>
 *		<li>the classpath is incorrectly set</li>
 *	</ul>
 */
public void search(IWorkspace workspace, ISearchPattern searchPattern, IJavaSearchScope scope, IJavaSearchResultCollector resultCollector) throws JavaModelException {
	
	long start = -1;
	if (VERBOSE) {
		start = System.currentTimeMillis();
		System.out.println("Searching for " + searchPattern + " in " + scope); //$NON-NLS-1$//$NON-NLS-2$
	}

	/* search is starting */
	resultCollector.aboutToStart();

	MatchLocator matchLocator = null;
	try {	
		if (searchPattern == null) return;

		/* initialize progress monitor */
		IProgressMonitor progressMonitor = resultCollector.getProgressMonitor();
		if (progressMonitor != null) {
			progressMonitor.beginTask(Util.bind("engine.searching"), 100); //$NON-NLS-1$
		}

		/* index search */
		PathCollector pathCollector = new PathCollector();
		
		// In the case of a hierarchy scope make sure that the hierarchy is not computed.
		// MatchLocator will filter out elements not in the hierarchy
		SearchPattern pattern = (SearchPattern)searchPattern;
		if (scope instanceof HierarchyScope) {
			((HierarchyScope)scope).needsRefresh = false;
			pattern.needsResolve = true; // force resolve to compute type bindings
		}

		IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
		int detailLevel = IInfoConstants.PathInfo | IInfoConstants.PositionInfo;
		matchLocator = 
			new MatchLocator(
				pattern, 
				detailLevel, 
				resultCollector, 
				scope,
				progressMonitor == null ? null : new SubProgressMonitor(progressMonitor, 95)
		);

		indexManager.performConcurrentJob(
			new PatternSearchJob(
				pattern, 
				scope, 
				pattern.focus,
				pattern.isPolymorphicSearch(),
				detailLevel, 
				pathCollector, 
				indexManager),
			IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
			progressMonitor == null ? null : new SubProgressMonitor(progressMonitor, 5));
			
		/* eliminating false matches and locating them */
		if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();
		matchLocator.locateMatches(
			pathCollector.getPaths(), 
			workspace,
			filterWorkingCopies(this.workingCopies, pattern.focus, pattern.isPolymorphicSearch())
		);
		

		if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();
		
		if (progressMonitor != null) {
			progressMonitor.done();
		}

		matchLocator.locatePackageDeclarations(workspace);
	} finally {
		/* search has ended */
		resultCollector.done();
		if (VERBOSE) {
			System.out.println("Total time: " + (System.currentTimeMillis()-start) + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
			if (matchLocator != null) System.out.println("Time in result collector: " + matchLocator.resultCollectorTime + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
/**
 * Searches for all top-level types and member types in the given scope.
 * The search can be selecting specific types (given a package or a type name
 * prefix and match modes). 
 * 
 * @param workspace the workspace to search in
 * @param packageName the full name of the package of the searched types, or a prefix for this
 *						package, or a wild-carded string for this package.
 * @param typeName the dot-separated qualified name of the searched type (the qualification include
 *					the enclosing types if the searched type is a member type), or a prefix
 *					for this type, or a wild-carded string for this type.
 * @param matchMode one of
 * <ul>
 *		<li><code>IJavaSearchConstants.EXACT_MATCH</code> if the package name and type name are the full names
 *			of the searched types.</li>
 *		<li><code>IJavaSearchConstants.PREFIX_MATCH</code> if the package name and type name are prefixes of the names
 *			of the searched types.</li>
 *		<li><code>IJavaSearchConstants.PATTERN_MATCH</code> if the package name and type name contain wild-cards.</li>
 * </ul>
 * @param isCaseSensitive whether the search should be case sensitive
 * @param searchFor one of
 * <ul>
 * 		<li><code>IJavaSearchConstants.CLASS</code> if searching for classes only</li>
 * 		<li><code>IJavaSearchConstants.INTERFACE</code> if searching for interfaces only</li>
 * 		<li><code>IJavaSearchConstants.TYPE</code> if searching for both classes and interfaces</li>
 * </ul>
 * @param scope the scope to search in
 * @param nameRequestor the requestor that collects the results of the search
 * @param waitingPolicy one of
 * <ul>
 *		<li><code>IJavaSearchConstants.FORCE_IMMEDIATE_SEARCH</code> if the search should start immediately</li>
 *		<li><code>IJavaSearchConstants.CANCEL_IF_NOT_READY_TO_SEARCH</code> if the search should be cancelled if the
 *			underlying indexer has not finished indexing the workspace</li>
 *		<li><code>IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH</code> if the search should wait for the
 *			underlying indexer to finish indexing the workspace</li>
 * </ul>
 * @param progressMonitor the progress monitor to report progress to, or <code>null</code> if no progress
 *							monitor is provided
 * @exception JavaModelException if the search failed. Reasons include:
 *	<ul>
 *		<li>the classpath is incorrectly set</li>
 *	</ul>
 */
public void searchAllTypeNames(
	IWorkspace workspace,
	char[] packageName, 
	char[] typeName,
	int matchMode, 
	boolean isCaseSensitive,
	int searchFor, 
	IJavaSearchScope scope, 
	final ITypeNameRequestor nameRequestor,
	int waitingPolicy,
	IProgressMonitor progressMonitor)  throws JavaModelException {

	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
		
	char classOrInterface;
	switch(searchFor){
		case IJavaSearchConstants.CLASS :
			classOrInterface = IIndexConstants.CLASS_SUFFIX;
			break;
		case IJavaSearchConstants.INTERFACE :
			classOrInterface = IIndexConstants.INTERFACE_SUFFIX;
			break;
		default : 
			classOrInterface = IIndexConstants.TYPE_SUFFIX;
			break;
	}
	SearchPattern pattern = new TypeDeclarationPattern(
		packageName,
		null, // do find member types
		typeName,
		classOrInterface,
		matchMode, 
		isCaseSensitive);
	
	IIndexSearchRequestor searchRequestor = new IndexSearchAdapter(){
		public void acceptClassDeclaration(String resourcePath, char[] simpleTypeName, char[][] enclosingTypeNames, char[] pkgName) {
			if (enclosingTypeNames != IIndexConstants.ONE_ZERO_CHAR) { // filter out local and anonymous classes
				nameRequestor.acceptClass(pkgName, simpleTypeName, enclosingTypeNames, resourcePath);
			}
		}		
		public void acceptInterfaceDeclaration(String resourcePath, char[] simpleTypeName, char[][] enclosingTypeNames, char[] pkgName) {
			if (enclosingTypeNames != IIndexConstants.ONE_ZERO_CHAR) { // filter out local and anonymous classes
				nameRequestor.acceptInterface(pkgName, simpleTypeName, enclosingTypeNames, resourcePath);
			}
		}		
	};

	try {
		if (progressMonitor != null) {
			progressMonitor.beginTask(Util.bind("engine.searching"), 100); //$NON-NLS-1$
		}
		indexManager.performConcurrentJob(
			new PatternSearchJob(pattern, scope, IInfoConstants.NameInfo | IInfoConstants.PathInfo, searchRequestor, indexManager),
			waitingPolicy,
			progressMonitor == null ? null : new SubProgressMonitor(progressMonitor, 100));	
	} finally {
		if (progressMonitor != null) {
			progressMonitor.done();
		}
	}
}
/**
 * Searches for all declarations of the fields accessed in the given element.
 * The element can be a compilation unit, a type, or a method.
 * Reports the field declarations using the given collector.
 * <p>
 * Consider the following code:
 * <code>
 * <pre>
 *		class A {
 *			int field1;
 *		}
 *		class B extends A {
 *			String value;
 *		}
 *		class X {
 *			void test() {
 *				B b = new B();
 *				System.out.println(b.value + b.field1);
 *			};
 *		}
 * </pre>
 * </code>
 * then searching for declarations of accessed fields in method 
 * <code>X.test()</code> would collect the fields
 * <code>B.value</code> and <code>A.field1</code>.
 * </p>
 *
 * @param workspace the workspace
 * @param enclosingElement the method, type, or compilation unit to be searched in
 * @param resultCollector a callback object to which each match is reported
 * @exception JavaModelException if the search failed. Reasons include:
 *	<ul>
 *		<li>the element doesn't exist</li>
 *		<li>the classpath is incorrectly set</li>
 *	</ul>
 */	
public void searchDeclarationsOfAccessedFields(IWorkspace workspace, IJavaElement enclosingElement, IJavaSearchResultCollector resultCollector) throws JavaModelException {
	SearchPattern pattern = new DeclarationOfAccessedFieldsPattern(enclosingElement);
	IJavaSearchScope scope = createJavaSearchScope(new IJavaElement[] {enclosingElement});
	IResource resource = this.getResource(enclosingElement);
	if (resource instanceof IFile) {
		if (VERBOSE) {
			System.out.println("Searching for " + pattern + " in " + resource.getFullPath()); //$NON-NLS-1$//$NON-NLS-2$
		}
		MatchLocator locator = new MatchLocator(
			pattern,
			IInfoConstants.DeclarationInfo,
			resultCollector,
			scope,
			resultCollector.getProgressMonitor());
		locator.locateMatches(
			new String[] {resource.getFullPath().toString()}, 
			workspace,
			this.getWorkingCopies(enclosingElement));
	} else {
		search(workspace, pattern, scope, resultCollector);
	}
}
/**
 * Searches for all declarations of the types referenced in the given element.
 * The element can be a compilation unit, a type, or a method.
 * Reports the type declarations using the given collector.
 * <p>
 * Consider the following code:
 * <code>
 * <pre>
 *		class A {
 *		}
 *		class B extends A {
 *		}
 *		interface I {
 *		  int VALUE = 0;
 *		}
 *		class X {
 *			void test() {
 *				B b = new B();
 *				this.foo(b, I.VALUE);
 *			};
 *		}
 * </pre>
 * </code>
 * then searching for declarations of referenced types in method <code>X.test()</code>
 * would collect the class <code>B</code> and the interface <code>I</code>.
 * </p>
 *
 * @param workspace the workspace
 * @param enclosingElement the method, type, or compilation unit to be searched in
 * @param resultCollector a callback object to which each match is reported
 * @exception JavaModelException if the search failed. Reasons include:
 *	<ul>
 *		<li>the element doesn't exist</li>
 *		<li>the classpath is incorrectly set</li>
 *	</ul>
 */	
public void searchDeclarationsOfReferencedTypes(IWorkspace workspace, IJavaElement enclosingElement, IJavaSearchResultCollector resultCollector) throws JavaModelException {
	SearchPattern pattern = new DeclarationOfReferencedTypesPattern(enclosingElement);
	IJavaSearchScope scope = createJavaSearchScope(new IJavaElement[] {enclosingElement});
	IResource resource = this.getResource(enclosingElement);
	if (resource instanceof IFile) {
		if (VERBOSE) {
			System.out.println("Searching for " + pattern + " in " + resource.getFullPath()); //$NON-NLS-1$//$NON-NLS-2$
		}
		MatchLocator locator = new MatchLocator(
			pattern,
			IInfoConstants.DeclarationInfo,
			resultCollector,
			scope,
			resultCollector.getProgressMonitor());
		locator.locateMatches(
			new String[] {resource.getFullPath().toString()}, 
			workspace,
			this.getWorkingCopies(enclosingElement));
	} else {
		search(workspace, pattern, scope, resultCollector);
	}
}
/**
 * Searches for all declarations of the methods invoked in the given element.
 * The element can be a compilation unit, a type, or a method.
 * Reports the method declarations using the given collector.
 * <p>
 * Consider the following code:
 * <code>
 * <pre>
 *		class A {
 *			void foo() {};
 *			void bar() {};
 *		}
 *		class B extends A {
 *			void foo() {};
 *		}
 *		class X {
 *			void test() {
 *				A a = new B();
 *				a.foo();
 *				B b = (B)a;
 *				b.bar();
 *			};
 *		}
 * </pre>
 * </code>
 * then searching for declarations of sent messages in method 
 * <code>X.test()</code> would collect the methods
 * <code>A.foo()</code>, <code>B.foo()</code>, and <code>A.bar()</code>.
 * </p>
 *
 * @param workspace the workspace
 * @param enclosingElement the method, type, or compilation unit to be searched in
 * @param resultCollector a callback object to which each match is reported
 * @exception JavaModelException if the search failed. Reasons include:
 *	<ul>
 *		<li>the element doesn't exist</li>
 *		<li>the classpath is incorrectly set</li>
 *	</ul>
 */	
public void searchDeclarationsOfSentMessages(IWorkspace workspace, IJavaElement enclosingElement, IJavaSearchResultCollector resultCollector) throws JavaModelException {
	SearchPattern pattern = new DeclarationOfReferencedMethodsPattern(enclosingElement);
	IJavaSearchScope scope = createJavaSearchScope(new IJavaElement[] {enclosingElement});
	IResource resource = this.getResource(enclosingElement);
	if (resource instanceof IFile) {
		if (VERBOSE) {
			System.out.println("Searching for " + pattern + " in " + resource.getFullPath()); //$NON-NLS-1$//$NON-NLS-2$
		}
		MatchLocator locator = new MatchLocator(
			pattern,
			IInfoConstants.DeclarationInfo,
			resultCollector,
			scope,
			resultCollector.getProgressMonitor());
		locator.locateMatches(
			new String[] {resource.getFullPath().toString()}, 
			workspace,
			this.getWorkingCopies(enclosingElement));
	} else {
		search(workspace, pattern, scope, resultCollector);
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9124.java