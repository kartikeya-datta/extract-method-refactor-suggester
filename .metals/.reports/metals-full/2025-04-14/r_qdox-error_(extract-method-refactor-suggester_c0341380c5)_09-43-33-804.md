error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2537.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2537.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2537.java
text:
```scala
T@@ypeNameMatchRequestorWrapper requestorWrapper = new TypeNameMatchRequestorWrapper(nameMatchRequestor, scope);

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
package org.eclipse.jdt.core.search;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.core.search.*;
import org.eclipse.jdt.internal.core.search.matching.*;

/**
 * A {@link SearchEngine} searches for Java elements following a search pattern.
 * The search can be limited to a search scope.
 * <p>
 * Various search patterns can be created using the factory methods 
 * {@link SearchPattern#createPattern(String, int, int, int)}, {@link SearchPattern#createPattern(IJavaElement, int)},
 * {@link SearchPattern#createOrPattern(SearchPattern, SearchPattern)}.
 * </p>
 * <p>For example, one can search for references to a method in the hierarchy of a type, 
 * or one can search for the declarations of types starting with "Abstract" in a project.
 * </p>
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class SearchEngine {

	/**
	 * Internal adapter class.
	 * @deprecated marking deprecated as it uses deprecated ISearchPattern
	 */
	static class SearchPatternAdapter implements ISearchPattern {
		SearchPattern pattern;
		SearchPatternAdapter(SearchPattern pattern) {
			this.pattern = pattern;
		}
	}

	/**
	 * Internal adapter class.
	 * @deprecated marking deprecated as it uses deprecated IJavaSearchResultCollector
	 */
	class ResultCollectorAdapter extends SearchRequestor {
		IJavaSearchResultCollector resultCollector;
		ResultCollectorAdapter(IJavaSearchResultCollector resultCollector) {
			this.resultCollector = resultCollector;
		}
		/**
		 * @see org.eclipse.jdt.core.search.SearchRequestor#acceptSearchMatch(org.eclipse.jdt.core.search.SearchMatch)
		 */
		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			this.resultCollector.accept(
				match.getResource(),
				match.getOffset(),
				match.getOffset() + match.getLength(),
				(IJavaElement) match.getElement(),
				match.getAccuracy()
			);
		}
		/**
		 * @see org.eclipse.jdt.core.search.SearchRequestor#beginReporting()
		 */
		public void beginReporting() {
			this.resultCollector.aboutToStart();
		}
		/**
		 * @see org.eclipse.jdt.core.search.SearchRequestor#endReporting()
		 */
		public void endReporting() {
			this.resultCollector.done();
		}
	}

	/**
	 * Internal adapter class.
	 * @deprecated marking deprecated as it uses deprecated ITypeNameRequestor
	 */
	class TypeNameRequestorAdapter implements IRestrictedAccessTypeRequestor {
		ITypeNameRequestor nameRequestor;
		TypeNameRequestorAdapter(ITypeNameRequestor requestor) {
			this.nameRequestor = requestor;
		}
		public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, String path, AccessRestriction access) {
			if (Flags.isInterface(modifiers)) {
				nameRequestor.acceptInterface(packageName, simpleTypeName, enclosingTypeNames, path);
			} else {
				nameRequestor.acceptClass(packageName, simpleTypeName, enclosingTypeNames, path);
			}
		}
	}
		
	// Search engine now uses basic engine functionalities
	private BasicSearchEngine basicEngine;

	/**
	 * Creates a new search engine.
	 */
	public SearchEngine() {
		this.basicEngine = new BasicSearchEngine();
	}
	
	/**
	 * Creates a new search engine with a list of working copies that will take precedence over 
	 * their original compilation units in the subsequent search operations.
	 * <p>
	 * Note that passing an empty working copy will be as if the original compilation
	 * unit had been deleted.</p>
	 * <p>
	 * Since 3.0 the given working copies take precedence over primary working copies (if any).
	 * 
	 * @param workingCopies the working copies that take precedence over their original compilation units
	 * @since 3.0
	 */
	public SearchEngine(ICompilationUnit[] workingCopies) {
		this.basicEngine = new BasicSearchEngine(workingCopies);
	}
	/**
	 * Creates a new search engine with a list of working copies that will take precedence over 
	 * their original compilation units in the subsequent search operations.
	 * <p>
	 * Note that passing an empty working copy will be as if the original compilation
	 * unit had been deleted.</p>
	 * <p>
	 * Since 3.0 the given working copies take precedence over primary working copies (if any).
	 * 
	 * @param workingCopies the working copies that take precedence over their original compilation units
	 * @since 2.0
	 * @deprecated Use {@link #SearchEngine(ICompilationUnit[])} instead.
	 */
	public SearchEngine(IWorkingCopy[] workingCopies) {
		int length = workingCopies.length;
		ICompilationUnit[] units = new ICompilationUnit[length];
		System.arraycopy(workingCopies, 0, units, 0, length);
		this.basicEngine = new BasicSearchEngine(units);
	}
	
	/**
	 * Creates a new search engine with the given working copy owner.
	 * The working copies owned by this owner will take precedence over 
	 * the primary compilation units in the subsequent search operations.
	 * 
	 * @param workingCopyOwner the owner of the working copies that take precedence over their original compilation units
	 * @since 3.0
	 */
	public SearchEngine(WorkingCopyOwner workingCopyOwner) {
		this.basicEngine = new BasicSearchEngine(workingCopyOwner);
	}

	/**
	 * Returns a Java search scope limited to the hierarchy of the given type.
	 * The Java elements resulting from a search with this scope will
	 * be types in this hierarchy, or members of the types in this hierarchy.
	 *
	 * @param type the focus of the hierarchy scope
	 * @return a new hierarchy scope
	 * @exception JavaModelException if the hierarchy could not be computed on the given type
	 */
	public static IJavaSearchScope createHierarchyScope(IType type) throws JavaModelException {
		return BasicSearchEngine.createHierarchyScope(type);
	}
	
	/**
	 * Returns a Java search scope limited to the hierarchy of the given type.
	 * When the hierarchy is computed, the types defined in the working copies owned
	 * by the given owner take precedence over the original compilation units.
	 * The Java elements resulting from a search with this scope will
	 * be types in this hierarchy, or members of the types in this hierarchy.
	 *
	 * @param type the focus of the hierarchy scope
	 * @param owner the owner of working copies that take precedence over original compilation units
	 * @return a new hierarchy scope
	 * @exception JavaModelException if the hierarchy could not be computed on the given type
	 * @since 3.0
	 */
	public static IJavaSearchScope createHierarchyScope(IType type, WorkingCopyOwner owner) throws JavaModelException {
		return BasicSearchEngine.createHierarchyScope(type, owner);
	}

	/**
	 * Returns a Java search scope limited to the given resources.
	 * The Java elements resulting from a search with this scope will
	 * have their underlying resource included in or equals to one of the given
	 * resources.
	 * <p>
	 * Resources must not overlap, for example, one cannot include a folder and its children.
	 * </p>
	 *
	 * @param resources the resources the scope is limited to
	 * @return a new Java search scope
	 * @deprecated Use {@link #createJavaSearchScope(IJavaElement[])} instead.
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
	 * Returns a Java search scope limited to the given Java elements.
	 * The Java elements resulting from a search with this scope will
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
	 * @param elements the Java elements the scope is limited to
	 * @return a new Java search scope
	 * @since 2.0
	 */
	public static IJavaSearchScope createJavaSearchScope(IJavaElement[] elements) {
		return BasicSearchEngine.createJavaSearchScope(elements);
	}

	/**
	 * Returns a Java search scope limited to the given Java elements.
	 * The Java elements resulting from a search with this scope will
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
	 * @param elements the Java elements the scope is limited to
	 * @param includeReferencedProjects a flag indicating if referenced projects must be 
	 * 									 recursively included
	 * @return a new Java search scope
	 * @since 2.0
	 */
	public static IJavaSearchScope createJavaSearchScope(IJavaElement[] elements, boolean includeReferencedProjects) {
		return BasicSearchEngine.createJavaSearchScope(elements, includeReferencedProjects);
	}

	/**
	 * Returns a Java search scope limited to the given Java elements.
	 * The Java elements resulting from a search with this scope will
	 * be children of the given elements.
	 * 
	 * If an element is an IJavaProject, then it includes:
	 * - its source folders if IJavaSearchScope.SOURCES is specified, 
	 * - its application libraries (internal and external jars, class folders that are on the raw classpath, 
	 *   or the ones that are coming from a classpath path variable,
	 *   or the ones that are coming from a classpath container with the K_APPLICATION kind)
	 *   if IJavaSearchScope.APPLICATION_LIBRARIES is specified
	 * - its system libraries (internal and external jars, class folders that are coming from an 
	 *   IClasspathContainer with the K_SYSTEM kind) 
	 *   if IJavaSearchScope.APPLICATION_LIBRARIES is specified
	 * - its referenced projects (with their source folders and jars, recursively) 
	 *   if IJavaSearchScope.REFERENCED_PROJECTS is specified.
	 * If an element is an IPackageFragmentRoot, then only the package fragments of 
	 * this package fragment root will be included.
	 * If an element is an IPackageFragment, then only the compilation unit and class 
	 * files of this package fragment will be included. Subpackages will NOT be 
	 * included.
	 *
	 * @param elements the Java elements the scope is limited to
	 * @param includeMask the bit-wise OR of all include types of interest
	 * @return a new Java search scope
	 * @see IJavaSearchScope#SOURCES
	 * @see IJavaSearchScope#APPLICATION_LIBRARIES
	 * @see IJavaSearchScope#SYSTEM_LIBRARIES
	 * @see IJavaSearchScope#REFERENCED_PROJECTS
	 * @since 3.0
	 */
	public static IJavaSearchScope createJavaSearchScope(IJavaElement[] elements, int includeMask) {
		return BasicSearchEngine.createJavaSearchScope(elements, includeMask);
	}
	
	/**
	 * Returns a search pattern that combines the given two patterns into a "or" pattern.
	 * The search result will match either the left pattern or the right pattern.
	 *
	 * @param leftPattern the left pattern
	 * @param rightPattern the right pattern
	 * @return a "or" pattern
	 * @deprecated Use {@link SearchPattern#createOrPattern(SearchPattern, SearchPattern)} instead.
	 */
	public static ISearchPattern createOrSearchPattern(ISearchPattern leftPattern, ISearchPattern rightPattern) {
		SearchPattern left = ((SearchPatternAdapter) leftPattern).pattern;
		SearchPattern right = ((SearchPatternAdapter) rightPattern).pattern;
		SearchPattern pattern = SearchPattern.createOrPattern(left, right);
		return new SearchPatternAdapter(pattern);
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
	 * 	<li>{@link IJavaSearchConstants#CLASS}: only look for classes</li>
	 *		<li>{@link IJavaSearchConstants#INTERFACE}: only look for interfaces</li>
	 * 	<li>{@link IJavaSearchConstants#TYPE}: look for both classes and interfaces</li>
	 *		<li>{@link IJavaSearchConstants#FIELD}: look for fields</li>
	 *		<li>{@link IJavaSearchConstants#METHOD}: look for methods</li>
	 *		<li>{@link IJavaSearchConstants#CONSTRUCTOR}: look for constructors</li>
	 *		<li>{@link IJavaSearchConstants#PACKAGE}: look for packages</li>
	 *	</ul>
	 * @param limitTo determines the nature of the expected matches
	 *	<ul>
	 * 		<li>{@link IJavaSearchConstants#DECLARATIONS}: will search declarations matching with the corresponding
	 * 			element. In case the element is a method, declarations of matching methods in subtypes will also
	 *  		be found, allowing to find declarations of abstract methods, etc.</li>
	 *
	 *		 <li>{@link IJavaSearchConstants#REFERENCES}: will search references to the given element.</li>
	 *
	 *		 <li>{@link IJavaSearchConstants#ALL_OCCURRENCES}: will search for either declarations or references as specified
	 *  		above.</li>
	 *
	 *		 <li>{@link IJavaSearchConstants#IMPLEMENTORS}: for types, will find all types
	 *				which directly implement/extend a given interface.
	 *				Note that types may be only classes or only interfaces if {@link IJavaSearchConstants#CLASS } or
	 *				{@link IJavaSearchConstants#INTERFACE} is respectively used instead of {@link IJavaSearchConstants#TYPE}.
	 *		</li>
	 *	</ul>
	 *
	 * @param isCaseSensitive indicates whether the search is case sensitive or not.
	 * @return a search pattern on the given string pattern, or <code>null</code> if the string pattern is ill-formed.
	 * @deprecated Use {@link SearchPattern#createPattern(String, int, int, int)} instead.
	 */
	public static ISearchPattern createSearchPattern(String stringPattern, int searchFor, int limitTo, boolean isCaseSensitive) {
		int matchMode = stringPattern.indexOf('*') != -1 || stringPattern.indexOf('?') != -1
			? SearchPattern.R_PATTERN_MATCH
			: SearchPattern.R_EXACT_MATCH;
		int matchRule = isCaseSensitive ? matchMode | SearchPattern.R_CASE_SENSITIVE : matchMode;
		return  new SearchPatternAdapter(SearchPattern.createPattern(stringPattern, searchFor, limitTo, matchRule));
	}
	
	/**
	 * Returns a search pattern based on a given Java element. 
	 * The pattern is used to trigger the appropriate search, and can be parameterized as follows:
	 *
	 * @param element the Java element the search pattern is based on
	 * @param limitTo determines the nature of the expected matches
	 * 	<ul>
	 * 		<li>{@link IJavaSearchConstants#DECLARATIONS}: will search declarations matching with the corresponding
	 * 			element. In case the element is a method, declarations of matching methods in subtypes will also
	 *  		be found, allowing to find declarations of abstract methods, etc.</li>
	 *
	 *		 <li>{@link IJavaSearchConstants#REFERENCES}: will search references to the given element.</li>
	 *
	 *		 <li>{@link IJavaSearchConstants#ALL_OCCURRENCES}: will search for either declarations or references as specified
	 *  		above.</li>
	 *
	 *		 <li>{@link IJavaSearchConstants#IMPLEMENTORS}: for types, will find all types
	 *				which directly implement/extend a given interface.</li>
	 *	</ul>
	 * @return a search pattern for a Java element or <code>null</code> if the given element is ill-formed
	 * @deprecated Use {@link SearchPattern#createPattern(IJavaElement, int)} instead.
	 */
	public static ISearchPattern createSearchPattern(IJavaElement element, int limitTo) {
		return new SearchPatternAdapter(SearchPattern.createPattern(element, limitTo));
	}

	/**
	 * Returns a Java search scope with the workspace as the only limit.
	 *
	 * @return a new workspace scope
	 */
	public static IJavaSearchScope createWorkspaceScope() {
		return BasicSearchEngine.createWorkspaceScope();
	}
	/**
	 * Returns a new default Java search participant.
	 * 
	 * @return a new default Java search participant
	 * @since 3.0
	 */
	public static SearchParticipant getDefaultSearchParticipant() {
		return BasicSearchEngine.getDefaultSearchParticipant();
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
	 * @param patternString the pattern to be searched for
	 * @param searchFor a hint what kind of Java element the string pattern represents.
	 *  Look into {@link IJavaSearchConstants} for valid values
	 * @param limitTo one of the following values:
	 *	<ul>
	 *	  <li>{@link IJavaSearchConstants#DECLARATIONS}: search 
	 *		  for declarations only </li>
	 *	  <li>{@link IJavaSearchConstants#REFERENCES}: search 
	 *		  for all references </li>
	 *	  <li>{@link IJavaSearchConstants#ALL_OCCURRENCES}: search 
	 *		  for both declarations and all references </li>
	 *	  <li>{@link IJavaSearchConstants#IMPLEMENTORS}: for types, will find all types
	 *			which directly implement/extend a given interface.<br>
	 *			Note that types may be only classes or only interfaces if respectively {@link IJavaSearchConstants#CLASS} or
	 *			{@link IJavaSearchConstants#INTERFACE} is used for searchFor parameter instead of {@link IJavaSearchConstants#TYPE}.
	 *	  </li>
	 * </ul>
	 * @param scope the search result has to be limited to the given scope
	 * @param resultCollector a callback object to which each match is reported	 
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @deprecated Use {@link  #search(SearchPattern, SearchParticipant[], IJavaSearchScope, SearchRequestor, IProgressMonitor)} instead.
	 */
	public void search(IWorkspace workspace, String patternString, int searchFor, int limitTo, IJavaSearchScope scope, IJavaSearchResultCollector resultCollector) throws JavaModelException {
		try {
			int matchMode = patternString.indexOf('*') != -1 || patternString.indexOf('?') != -1
				? SearchPattern.R_PATTERN_MATCH
				: SearchPattern.R_EXACT_MATCH;
			search(
				SearchPattern.createPattern(patternString, searchFor, limitTo, matchMode | SearchPattern.R_CASE_SENSITIVE), 
				new SearchParticipant[] {getDefaultSearchParticipant()}, 
				scope, 
				new ResultCollectorAdapter(resultCollector), 
				resultCollector.getProgressMonitor());
		} catch (CoreException e) {
			if (e instanceof JavaModelException)
				throw (JavaModelException) e;
			throw new JavaModelException(e);
		}
	}

	/**
	 * Searches for the given Java element.
	 *
	 * @param workspace the workspace
	 * @param element the Java element to be searched for
	 * @param limitTo one of the following values:
	 *	<ul>
	 *	  <li>{@link IJavaSearchConstants#DECLARATIONS}: search 
	 *		  for declarations only </li>
	 *	  <li>{@link IJavaSearchConstants#REFERENCES}: search 
	 *		  for all references </li>
	 *	  <li>{@link IJavaSearchConstants#ALL_OCCURRENCES}: search 
	 *		  for both declarations and all references </li>
	 *	  <li>{@link IJavaSearchConstants#IMPLEMENTORS}: for types, will find all types
	 *				which directly implement/extend a given interface.</li>
	 * 	</ul>
	 * @param scope the search result has to be limited to the given scope
	 * @param resultCollector a callback object to which each match is reported
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the element doesn't exist</li>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @deprecated Use {@link #search(SearchPattern, SearchParticipant[], IJavaSearchScope, SearchRequestor, IProgressMonitor)} instead.
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
	 * @deprecated Use {@link  #search(SearchPattern, SearchParticipant[], IJavaSearchScope, SearchRequestor, IProgressMonitor)} instead.
	 */
	public void search(IWorkspace workspace, ISearchPattern searchPattern, IJavaSearchScope scope, IJavaSearchResultCollector resultCollector) throws JavaModelException {
		try {
			search(
				((SearchPatternAdapter)searchPattern).pattern, 
				new SearchParticipant[] {getDefaultSearchParticipant()}, 
				scope, 
				new ResultCollectorAdapter(resultCollector), 
				resultCollector.getProgressMonitor());
		} catch (CoreException e) {
			if (e instanceof JavaModelException)
				throw (JavaModelException) e;
			throw new JavaModelException(e);
		}
	}
	
	/**
	 * Searches for matches of a given search pattern. Search patterns can be created using helper
	 * methods (from a String pattern or a Java element) and encapsulate the description of what is
	 * being searched (for example, search method declarations in a case sensitive way).
	 *
	 * @param pattern the pattern to search
	 * @param participants the particpants in the search
	 * @param scope the search scope
	 * @param requestor the requestor to report the matches to
	 * @param monitor the progress monitor used to report progress
	 * @exception CoreException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 *@since 3.0
	 */
	public void search(SearchPattern pattern, SearchParticipant[] participants, IJavaSearchScope scope, SearchRequestor requestor, IProgressMonitor monitor) throws CoreException {
		this.basicEngine.search(pattern, participants, scope, requestor, monitor);
	}

	/**
	 * Searches for all top-level types and member types in the given scope.
	 * The search can be selecting specific types (given a package exact full name or
	 * a type name with specific match mode). 
	 * 
	 * @param packageExactName the exact package full name of the searched types.<br>
	 * 					If you want to use a prefix or a wild-carded string for package, you need to use
	 * 					{@link #searchAllTypeNames(char[], int, char[], int, int, IJavaSearchScope, TypeNameRequestor, int, IProgressMonitor)}
	 * 					method  instead. May be <code>null</code>, then any package name is accepted.
	 * @param typeName the dot-separated qualified name of the searched type (the qualification include
	 *					the enclosing types if the searched type is a member type), or a prefix
	 *					for this type, or a wild-carded string for this type.
	 *					May be <code>null</code>, then any type name is accepted.
	 * @param matchRule type name match rule one of
	 * <ul>
	 *		<li>{@link SearchPattern#R_EXACT_MATCH} if the package name and type name are the full names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PREFIX_MATCH} if the package name and type name are prefixes of the names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PATTERN_MATCH} if the package name and type name contain wild-cards.</li>
	 *		<li>{@link SearchPattern#R_CAMELCASE_MATCH} if type name are camel case of the names of the searched types.</li>
	 * </ul>
	 * combined with {@link SearchPattern#R_CASE_SENSITIVE},
	 *   e.g. {@link SearchPattern#R_EXACT_MATCH} | {@link SearchPattern#R_CASE_SENSITIVE} if an exact and case sensitive match is requested, 
	 *   or {@link SearchPattern#R_PREFIX_MATCH} if a prefix non case sensitive match is requested.
	 * @param searchFor determines the nature of the searched elements
	 *	<ul>
	 * 	<li>{@link IJavaSearchConstants#CLASS}: only look for classes</li>
	 *		<li>{@link IJavaSearchConstants#INTERFACE}: only look for interfaces</li>
	 * 	<li>{@link IJavaSearchConstants#ENUM}: only look for enumeration</li>
	 *		<li>{@link IJavaSearchConstants#ANNOTATION_TYPE}: only look for annotation type</li>
	 * 	<li>{@link IJavaSearchConstants#CLASS_AND_ENUM}: only look for classes and enumerations</li>
	 *		<li>{@link IJavaSearchConstants#CLASS_AND_INTERFACE}: only look for classes and interfaces</li>
	 * 	<li>{@link IJavaSearchConstants#TYPE}: look for all types (ie. classes, interfaces, enum and annotation types)</li>
	 *	</ul>
	 * @param scope the scope to search in
	 * @param nameRequestor the requestor that collects the results of the search
	 * @param waitingPolicy one of
	 * <ul>
	 *		<li>{@link IJavaSearchConstants#FORCE_IMMEDIATE_SEARCH} if the search should start immediately</li>
	 *		<li>{@link IJavaSearchConstants#CANCEL_IF_NOT_READY_TO_SEARCH} if the search should be cancelled if the
	 *			underlying indexer has not finished indexing the workspace</li>
	 *		<li>{@link IJavaSearchConstants#WAIT_UNTIL_READY_TO_SEARCH} if the search should wait for the
	 *			underlying indexer to finish indexing the workspace</li>
	 * </ul>
	 * @param progressMonitor the progress monitor to report progress to, or <code>null</code> if no progress
	 *							monitor is provided
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @since 3.1
	 * @deprecated Use {@link #searchAllTypeNames(char[], int, char[], int, int, IJavaSearchScope, TypeNameRequestor, int, IProgressMonitor)}
	 * 	instead
	 */
	public void searchAllTypeNames(
		final char[] packageExactName, 
		final char[] typeName,
		final int matchRule, 
		int searchFor, 
		IJavaSearchScope scope, 
		final TypeNameRequestor nameRequestor,
		int waitingPolicy,
		IProgressMonitor progressMonitor)  throws JavaModelException {
		
		searchAllTypeNames(packageExactName, SearchPattern.R_EXACT_MATCH, typeName, matchRule, searchFor, scope, nameRequestor, waitingPolicy, progressMonitor);
	}

	/**
	 * Searches for all top-level types and member types in the given scope.
	 * The search can be selecting specific types (given a package name using specific match mode
	 * and/or a type name using another specific match mode). 
	 * 
	 * @param packageName the full name of the package of the searched types, or a prefix for this
	 *						package, or a wild-carded string for this package.
	 *						May be <code>null</code>, then any package name is accepted.
	 * @param typeName the dot-separated qualified name of the searched type (the qualification include
	 *					the enclosing types if the searched type is a member type), or a prefix
	 *					for this type, or a wild-carded string for this type.
	 *					May be <code>null</code>, then any type name is accepted.
	 * @param packageMatchRule one of
	 * <ul>
	 *		<li>{@link SearchPattern#R_EXACT_MATCH} if the package name and type name are the full names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PREFIX_MATCH} if the package name and type name are prefixes of the names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PATTERN_MATCH} if the package name and type name contain wild-cards.</li>
	 *		<li>{@link SearchPattern#R_CAMELCASE_MATCH} if type name are camel case of the names of the searched types.</li>
	 * </ul>
	 * combined with {@link SearchPattern#R_CASE_SENSITIVE},
	 *   e.g. {@link SearchPattern#R_EXACT_MATCH} | {@link SearchPattern#R_CASE_SENSITIVE} if an exact and case sensitive match is requested, 
	 *   or {@link SearchPattern#R_PREFIX_MATCH} if a prefix non case sensitive match is requested.
	 * @param typeMatchRule one of
	 * <ul>
	 *		<li>{@link SearchPattern#R_EXACT_MATCH} if the package name and type name are the full names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PREFIX_MATCH} if the package name and type name are prefixes of the names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PATTERN_MATCH} if the package name and type name contain wild-cards.</li>
	 *		<li>{@link SearchPattern#R_CAMELCASE_MATCH} if type name are camel case of the names of the searched types.</li>
	 * </ul>
	 * combined with {@link SearchPattern#R_CASE_SENSITIVE},
	 *   e.g. {@link SearchPattern#R_EXACT_MATCH} | {@link SearchPattern#R_CASE_SENSITIVE} if an exact and case sensitive match is requested, 
	 *   or {@link SearchPattern#R_PREFIX_MATCH} if a prefix non case sensitive match is requested.
	 * @param searchFor determines the nature of the searched elements
	 *	<ul>
	 * 	<li>{@link IJavaSearchConstants#CLASS}: only look for classes</li>
	 *		<li>{@link IJavaSearchConstants#INTERFACE}: only look for interfaces</li>
	 * 	<li>{@link IJavaSearchConstants#ENUM}: only look for enumeration</li>
	 *		<li>{@link IJavaSearchConstants#ANNOTATION_TYPE}: only look for annotation type</li>
	 * 	<li>{@link IJavaSearchConstants#CLASS_AND_ENUM}: only look for classes and enumerations</li>
	 *		<li>{@link IJavaSearchConstants#CLASS_AND_INTERFACE}: only look for classes and interfaces</li>
	 * 	<li>{@link IJavaSearchConstants#TYPE}: look for all types (ie. classes, interfaces, enum and annotation types)</li>
	 *	</ul>
	 * @param scope the scope to search in
	 * @param nameRequestor the requestor that collects the results of the search
	 * @param waitingPolicy one of
	 * <ul>
	 *		<li>{@link IJavaSearchConstants#FORCE_IMMEDIATE_SEARCH} if the search should start immediately</li>
	 *		<li>{@link IJavaSearchConstants#CANCEL_IF_NOT_READY_TO_SEARCH} if the search should be cancelled if the
	 *			underlying indexer has not finished indexing the workspace</li>
	 *		<li>{@link IJavaSearchConstants#WAIT_UNTIL_READY_TO_SEARCH} if the search should wait for the
	 *			underlying indexer to finish indexing the workspace</li>
	 * </ul>
	 * @param progressMonitor the progress monitor to report progress to, or <code>null</code> if no progress
	 *							monitor is provided
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @since 3.3
	 */
	public void searchAllTypeNames(
		final char[] packageName, 
		final int packageMatchRule, 
		final char[] typeName,
		final int typeMatchRule, 
		int searchFor, 
		IJavaSearchScope scope, 
		final TypeNameRequestor nameRequestor,
		int waitingPolicy,
		IProgressMonitor progressMonitor)  throws JavaModelException {
		
		TypeNameRequestorWrapper requestorWrapper = new TypeNameRequestorWrapper(nameRequestor);
		this.basicEngine.searchAllTypeNames(packageName, packageMatchRule, typeName, typeMatchRule, searchFor, scope, requestorWrapper, waitingPolicy, progressMonitor);
	}

	/**
	 * Searches for all top-level types and member types in the given scope.
	 * The search can be selecting specific types (given a package name using specific match mode
	 * and/or a type name using another specific match mode).
	 * <p>
	 * Provided {@link TypeNameMatchRequestor} requestor will collect {@link TypeNameMatch}
	 * matches found during the search.
	 * </p>
	 * 
	 * @param packageName the full name of the package of the searched types, or a prefix for this
	 *						package, or a wild-carded string for this package.
	 *						May be <code>null</code>, then any package name is accepted.
	 * @param typeName the dot-separated qualified name of the searched type (the qualification include
	 *					the enclosing types if the searched type is a member type), or a prefix
	 *					for this type, or a wild-carded string for this type.
	 *					May be <code>null</code>, then any type name is accepted.
	 * @param packageMatchRule one of
	 * <ul>
	 *		<li>{@link SearchPattern#R_EXACT_MATCH} if the package name and type name are the full names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PREFIX_MATCH} if the package name and type name are prefixes of the names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PATTERN_MATCH} if the package name and type name contain wild-cards.</li>
	 *		<li>{@link SearchPattern#R_CAMELCASE_MATCH} if type name are camel case of the names of the searched types.</li>
	 * </ul>
	 * combined with {@link SearchPattern#R_CASE_SENSITIVE},
	 *   e.g. {@link SearchPattern#R_EXACT_MATCH} | {@link SearchPattern#R_CASE_SENSITIVE} if an exact and case sensitive match is requested, 
	 *   or {@link SearchPattern#R_PREFIX_MATCH} if a prefix non case sensitive match is requested.
	 * @param typeMatchRule one of
	 * <ul>
	 *		<li>{@link SearchPattern#R_EXACT_MATCH} if the package name and type name are the full names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PREFIX_MATCH} if the package name and type name are prefixes of the names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PATTERN_MATCH} if the package name and type name contain wild-cards.</li>
	 *		<li>{@link SearchPattern#R_CAMELCASE_MATCH} if type name are camel case of the names of the searched types.</li>
	 * </ul>
	 * combined with {@link SearchPattern#R_CASE_SENSITIVE},
	 *   e.g. {@link SearchPattern#R_EXACT_MATCH} | {@link SearchPattern#R_CASE_SENSITIVE} if an exact and case sensitive match is requested, 
	 *   or {@link SearchPattern#R_PREFIX_MATCH} if a prefix non case sensitive match is requested.
	 * @param searchFor determines the nature of the searched elements
	 *	<ul>
	 * 	<li>{@link IJavaSearchConstants#CLASS}: only look for classes</li>
	 *		<li>{@link IJavaSearchConstants#INTERFACE}: only look for interfaces</li>
	 * 	<li>{@link IJavaSearchConstants#ENUM}: only look for enumeration</li>
	 *		<li>{@link IJavaSearchConstants#ANNOTATION_TYPE}: only look for annotation type</li>
	 * 	<li>{@link IJavaSearchConstants#CLASS_AND_ENUM}: only look for classes and enumerations</li>
	 *		<li>{@link IJavaSearchConstants#CLASS_AND_INTERFACE}: only look for classes and interfaces</li>
	 * 	<li>{@link IJavaSearchConstants#TYPE}: look for all types (ie. classes, interfaces, enum and annotation types)</li>
	 *	</ul>
	 * @param scope the scope to search in
	 * @param nameMatchRequestor the {@link TypeNameMatchRequestor requestor} that collects
	 * 				{@link TypeNameMatch matches} of the search.
	 * @param waitingPolicy one of
	 * <ul>
	 *		<li>{@link IJavaSearchConstants#FORCE_IMMEDIATE_SEARCH} if the search should start immediately</li>
	 *		<li>{@link IJavaSearchConstants#CANCEL_IF_NOT_READY_TO_SEARCH} if the search should be cancelled if the
	 *			underlying indexer has not finished indexing the workspace</li>
	 *		<li>{@link IJavaSearchConstants#WAIT_UNTIL_READY_TO_SEARCH} if the search should wait for the
	 *			underlying indexer to finish indexing the workspace</li>
	 * </ul>
	 * @param progressMonitor the progress monitor to report progress to, or <code>null</code> if no progress
	 *							monitor is provided
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @since 3.3
	 */
	public void searchAllTypeNames(
		final char[] packageName, 
		final int packageMatchRule, 
		final char[] typeName,
		final int typeMatchRule, 
		int searchFor, 
		IJavaSearchScope scope, 
		final TypeNameMatchRequestor nameMatchRequestor,
		int waitingPolicy,
		IProgressMonitor progressMonitor)  throws JavaModelException {
		
		TypeNameMatchRequestorWrapper requestorWrapper = new TypeNameMatchRequestorWrapper(nameMatchRequestor, scope, this.basicEngine.getWorkingCopies());
		this.basicEngine.searchAllTypeNames(packageName, packageMatchRule, typeName, typeMatchRule, searchFor, scope, requestorWrapper, waitingPolicy, progressMonitor);
	}

	/**
	 * Searches for all top-level types and member types in the given scope matching any of the given qualifications
	 * and type names in a case sensitive way.
	 * 
	 * @param qualifications the qualified name of the package/enclosing type of the searched types.
	 *					May be <code>null</code>, then any package name is accepted.
	 * @param typeNames the simple names of the searched types.
	 *					May be <code>null</code>, then any type name is accepted.
	 * @param scope the scope to search in
	 * @param nameRequestor the requestor that collects the results of the search
	 * @param waitingPolicy one of
	 * <ul>
	 *		<li>{@link IJavaSearchConstants#FORCE_IMMEDIATE_SEARCH} if the search should start immediately</li>
	 *		<li>{@link IJavaSearchConstants#CANCEL_IF_NOT_READY_TO_SEARCH} if the search should be cancelled if the
	 *			underlying indexer has not finished indexing the workspace</li>
	 *		<li>{@link IJavaSearchConstants#WAIT_UNTIL_READY_TO_SEARCH} if the search should wait for the
	 *			underlying indexer to finish indexing the workspace</li>
	 * </ul>
	 * @param progressMonitor the progress monitor to report progress to, or <code>null</code> if no progress
	 *							monitor is provided
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @since 3.1
	 */
	public void searchAllTypeNames(
		final char[][] qualifications, 
		final char[][] typeNames,
		IJavaSearchScope scope, 
		final TypeNameRequestor nameRequestor,
		int waitingPolicy,
		IProgressMonitor progressMonitor)  throws JavaModelException {

		TypeNameRequestorWrapper requestorWrapper = new TypeNameRequestorWrapper(nameRequestor);
		this.basicEngine.searchAllTypeNames(
			qualifications,
			typeNames,
			SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE,
			IJavaSearchConstants.TYPE,
			scope,
			requestorWrapper,
			waitingPolicy,
			progressMonitor);
	}

	/**
	 * Searches for all top-level types and member types in the given scope.
	 * The search can be selecting specific types (given a package or a type name
	 * prefix and match modes). 
	 * 
	 * @param packageName the full name of the package of the searched types, or a prefix for this
	 *						package, or a wild-carded string for this package.
	 * @param typeName the dot-separated qualified name of the searched type (the qualification include
	 *					the enclosing types if the searched type is a member type), or a prefix
	 *					for this type, or a wild-carded string for this type.
	 * @param matchRule one of
	 * <ul>
	 *		<li>{@link SearchPattern#R_EXACT_MATCH} if the package name and type name are the full names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PREFIX_MATCH} if the package name and type name are prefixes of the names
	 *			of the searched types.</li>
	 *		<li>{@link SearchPattern#R_PATTERN_MATCH} if the package name and type name contain wild-cards.</li>
	 * </ul>
	 * combined with {@link SearchPattern#R_CASE_SENSITIVE},
	 *   e.g. {@link SearchPattern#R_EXACT_MATCH} | {@link SearchPattern#R_CASE_SENSITIVE} if an exact and case sensitive match is requested, 
	 *   or {@link SearchPattern#R_PREFIX_MATCH} if a prefix non case sensitive match is requested.
	 * @param searchFor one of
	 * <ul>
	 * 		<li>{@link IJavaSearchConstants#CLASS} if searching for classes only</li>
	 * 		<li>{@link IJavaSearchConstants#INTERFACE} if searching for interfaces only</li>
	 * 		<li>{@link IJavaSearchConstants#TYPE} if searching for both classes and interfaces</li>
	 * </ul>
	 * @param scope the scope to search in
	 * @param nameRequestor the requestor that collects the results of the search
	 * @param waitingPolicy one of
	 * <ul>
	 *		<li>{@link IJavaSearchConstants#FORCE_IMMEDIATE_SEARCH} if the search should start immediately</li>
	 *		<li>{@link IJavaSearchConstants#CANCEL_IF_NOT_READY_TO_SEARCH} if the search should be cancelled if the
	 *			underlying indexer has not finished indexing the workspace</li>
	 *		<li>{@link IJavaSearchConstants#WAIT_UNTIL_READY_TO_SEARCH} if the search should wait for the
	 *			underlying indexer to finish indexing the workspace</li>
	 * </ul>
	 * @param progressMonitor the progress monitor to report progress to, or <code>null</code> if no progress
	 *							monitor is provided
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @since 3.0
	 *@deprecated Use {@link #searchAllTypeNames(char[], char[], int, int, IJavaSearchScope, TypeNameRequestor, int, IProgressMonitor)} instead
	 */
	public void searchAllTypeNames(
		final char[] packageName, 
		final char[] typeName,
		final int matchRule, 
		int searchFor, 
		IJavaSearchScope scope, 
		final ITypeNameRequestor nameRequestor,
		int waitingPolicy,
		IProgressMonitor progressMonitor)  throws JavaModelException {
		
		TypeNameRequestorAdapter requestorAdapter = new TypeNameRequestorAdapter(nameRequestor);
		this.basicEngine.searchAllTypeNames(packageName, SearchPattern.R_EXACT_MATCH, typeName, matchRule, searchFor, scope, requestorAdapter, waitingPolicy, progressMonitor);
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
	 *		<li>{@link IJavaSearchConstants#EXACT_MATCH} if the package name and type name are the full names
	 *			of the searched types.</li>
	 *		<li>{@link IJavaSearchConstants#PREFIX_MATCH} if the package name and type name are prefixes of the names
	 *			of the searched types.</li>
	 *		<li>{@link IJavaSearchConstants#PATTERN_MATCH} if the package name and type name contain wild-cards.</li>
	 * </ul>
	 * @param isCaseSensitive whether the search should be case sensitive
	 * @param searchFor one of
	 * <ul>
	 * 		<li>{@link IJavaSearchConstants#CLASS} if searching for classes only</li>
	 * 		<li>{@link IJavaSearchConstants#INTERFACE} if searching for interfaces only</li>
	 * 		<li>{@link IJavaSearchConstants#TYPE} if searching for both classes and interfaces</li>
	 * </ul>
	 * @param scope the scope to search in
	 * @param nameRequestor the requestor that collects the results of the search
	 * @param waitingPolicy one of
	 * <ul>
	 *		<li>{@link IJavaSearchConstants#FORCE_IMMEDIATE_SEARCH} if the search should start immediately</li>
	 *		<li>{@link IJavaSearchConstants#CANCEL_IF_NOT_READY_TO_SEARCH} if the search should be cancelled if the
	 *			underlying indexer has not finished indexing the workspace</li>
	 *		<li>{@link IJavaSearchConstants#WAIT_UNTIL_READY_TO_SEARCH} if the search should wait for the
	 *			underlying indexer to finish indexing the workspace</li>
	 * </ul>
	 * @param progressMonitor the progress monitor to report progress to, or <code>null</code> if no progress
	 *							monitor is provided
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 *@deprecated Use {@link #searchAllTypeNames(char[], char[], int, int, IJavaSearchScope, ITypeNameRequestor, int, IProgressMonitor)} instead
	 */
	public void searchAllTypeNames(
		IWorkspace workspace,
		final char[] packageName, 
		final char[] typeName,
		final int matchMode, 
		final boolean isCaseSensitive,
		int searchFor, 
		IJavaSearchScope scope, 
		final ITypeNameRequestor nameRequestor,
		int waitingPolicy,
		IProgressMonitor progressMonitor)  throws JavaModelException {
		
		searchAllTypeNames(
			packageName, 
			typeName, 
			isCaseSensitive ? matchMode | SearchPattern.R_CASE_SENSITIVE : matchMode, 
			searchFor, 
			scope, 
			nameRequestor, 
			waitingPolicy, 
			progressMonitor);
	}	

	/**
	 * Searches for all declarations of the fields accessed in the given element.
	 * The element can be a compilation unit, a source type, or a source method.
	 * Reports the field declarations using the given requestor.
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
	 * @param enclosingElement the method, type, or compilation unit to be searched in
	 * @param requestor a callback object to which each match is reported
	 * @param monitor the progress monitor used to report progress
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the element doesn't exist</li>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @since 3.0
	 */	
	public void searchDeclarationsOfAccessedFields(IJavaElement enclosingElement, SearchRequestor requestor, IProgressMonitor monitor) throws JavaModelException {
		this.basicEngine.searchDeclarationsOfAccessedFields(enclosingElement, requestor, monitor);
	}
	
	/**
	 * Searches for all declarations of the fields accessed in the given element.
	 * The element can be a compilation unit, a source type, or a source method.
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
	 * @deprecated Use {@link  #searchDeclarationsOfAccessedFields(IJavaElement, SearchRequestor, IProgressMonitor)} instead.
	 */	
	public void searchDeclarationsOfAccessedFields(IWorkspace workspace, IJavaElement enclosingElement, IJavaSearchResultCollector resultCollector) throws JavaModelException {
		SearchPattern pattern = new DeclarationOfAccessedFieldsPattern(enclosingElement);
		this.basicEngine.searchDeclarations(enclosingElement, new ResultCollectorAdapter(resultCollector), pattern, resultCollector.getProgressMonitor());
	}
	
	/**
	 * Searches for all declarations of the types referenced in the given element.
	 * The element can be a compilation unit, a source type, or a source method.
	 * Reports the type declarations using the given requestor.
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
	 * @param enclosingElement the method, type, or compilation unit to be searched in
	 * @param requestor a callback object to which each match is reported
	 * @param monitor the progress monitor used to report progress
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the element doesn't exist</li>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @since 3.0
	 */	
	public void searchDeclarationsOfReferencedTypes(IJavaElement enclosingElement, SearchRequestor requestor, IProgressMonitor monitor) throws JavaModelException {
		this.basicEngine.searchDeclarationsOfReferencedTypes(enclosingElement, requestor, monitor);
	}
	
	/**
	 * Searches for all declarations of the types referenced in the given element.
	 * The element can be a compilation unit, a source type, or a source method.
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
	 * @deprecated Use {@link #searchDeclarationsOfReferencedTypes(IJavaElement, SearchRequestor, IProgressMonitor)} instead.
	 */	
	public void searchDeclarationsOfReferencedTypes(IWorkspace workspace, IJavaElement enclosingElement, IJavaSearchResultCollector resultCollector) throws JavaModelException {
		SearchPattern pattern = new DeclarationOfReferencedTypesPattern(enclosingElement);
		this.basicEngine.searchDeclarations(enclosingElement, new ResultCollectorAdapter(resultCollector), pattern, resultCollector.getProgressMonitor());
	}
	
	/**
	 * Searches for all declarations of the methods invoked in the given element.
	 * The element can be a compilation unit, a source type, or a source method.
	 * Reports the method declarations using the given requestor.
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
	 * @param enclosingElement the method, type, or compilation unit to be searched in
	 * @param requestor a callback object to which each match is reported
	 * @param monitor the progress monitor used to report progress
	 * @exception JavaModelException if the search failed. Reasons include:
	 *	<ul>
	 *		<li>the element doesn't exist</li>
	 *		<li>the classpath is incorrectly set</li>
	 *	</ul>
	 * @since 3.0
	 */	
	public void searchDeclarationsOfSentMessages(IJavaElement enclosingElement, SearchRequestor requestor, IProgressMonitor monitor) throws JavaModelException {
		this.basicEngine.searchDeclarationsOfSentMessages(enclosingElement, requestor, monitor);
	}

	/**
	 * Searches for all declarations of the methods invoked in the given element.
	 * The element can be a compilation unit, a source type, or a source method.
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
	 * @deprecated Use {@link #searchDeclarationsOfSentMessages(IJavaElement, SearchRequestor, IProgressMonitor)} instead.
	 */	
	public void searchDeclarationsOfSentMessages(IWorkspace workspace, IJavaElement enclosingElement, IJavaSearchResultCollector resultCollector) throws JavaModelException {
		SearchPattern pattern = new DeclarationOfReferencedMethodsPattern(enclosingElement);
		this.basicEngine.searchDeclarations(enclosingElement, new ResultCollectorAdapter(resultCollector), pattern, resultCollector.getProgressMonitor());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2537.java