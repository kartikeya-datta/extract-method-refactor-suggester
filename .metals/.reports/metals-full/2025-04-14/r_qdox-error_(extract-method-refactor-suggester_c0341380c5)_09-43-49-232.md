error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8710.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8710.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8710.java
text:
```scala
S@@earchableEnvironment searchableEnvironment = project.newSearchableNameEnvironment(unitsToLookInside);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.hierarchy;

import java.util.*;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.search.IndexQueryRequestor;
import org.eclipse.jdt.internal.core.search.JavaSearchParticipant;
import org.eclipse.jdt.internal.core.search.SubTypeSearchJob;
import org.eclipse.jdt.internal.core.search.indexing.IIndexConstants;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;
import org.eclipse.jdt.internal.core.search.matching.MatchLocator;
import org.eclipse.jdt.internal.core.search.matching.SuperTypeReferencePattern;
import org.eclipse.jdt.internal.core.util.HandleFactory;
import org.eclipse.jdt.internal.core.util.Util;

public class IndexBasedHierarchyBuilder extends HierarchyBuilder implements SuffixConstants {
	public static final int MAXTICKS = 800; // heuristic so that there still progress for deep hierachies
	/**
	 * A temporary cache of compilation units to handles to speed info
	 * to handle translation - it only contains the entries
	 * for the types in the region (in other words, it contains no supertypes outside
	 * the region).
	 */
	protected Map cuToHandle;
	/**
	 * A map from compilation unit handles to working copies.
	 */
	protected Map handleToWorkingCopy;

	/**
	 * The scope this hierarchy builder should restrain results to.
	 */
	protected IJavaSearchScope scope;

	/**
	 * Cache used to record binaries recreated from index matches
	 */
	protected Map binariesFromIndexMatches;
	
	/**
	 * Collection used to queue subtype index queries
	 */
	private static class Queue {
		public char[][] names = new char[10][];
		public int start = 0;
		public int end = -1;
		public void add(char[] name){
			if (++this.end == this.names.length){
				this.end -= this.start;
				System.arraycopy(this.names, this.start, this.names = new char[this.end*2][], 0, this.end);
				this.start = 0;
			}
			this.names[this.end] = name;
		}
		public char[] retrieve(){
			if (this.start > this.end) return null; // none
			
			char[] name = this.names[this.start++];
			if (this.start > this.end){
				this.start = 0;
				this.end = -1;
			}
			return name;
		}
		public String toString(){
			StringBuffer buffer = new StringBuffer("Queue:\n"); //$NON-NLS-1$
			for (int i = this.start; i <= this.end; i++){
				buffer.append(this.names[i]).append('\n');		
			}
			return buffer.toString();
		}
	}
public IndexBasedHierarchyBuilder(TypeHierarchy hierarchy, IJavaSearchScope scope) throws JavaModelException {
	super(hierarchy);
	this.cuToHandle = new HashMap(5);
	this.binariesFromIndexMatches = new HashMap(10);
	this.scope = scope;
}
public void build(boolean computeSubtypes) {
	JavaModelManager manager = JavaModelManager.getJavaModelManager();
	try {
		// optimize access to zip files while building hierarchy
		manager.cacheZipFiles();
				
		if (computeSubtypes) {
			// Note by construction there always is a focus type here
			IType focusType = getType();
			boolean focusIsObject = focusType.getElementName().equals(new String(IIndexConstants.OBJECT));
			int amountOfWorkForSubtypes = focusIsObject ? 5 : 80; // percentage of work needed to get possible subtypes
			IProgressMonitor possibleSubtypesMonitor = 
				this.hierarchy.progressMonitor == null ? 
					null : 
					new SubProgressMonitor(this.hierarchy.progressMonitor, amountOfWorkForSubtypes);
			HashSet localTypes = new HashSet(10); // contains the paths that have potential subtypes that are local/anonymous types
			String[] allPossibleSubtypes;
			if (((Member)focusType).getOuterMostLocalContext() == null) {
				// top level or member type
				allPossibleSubtypes = this.determinePossibleSubTypes(localTypes, possibleSubtypesMonitor);
			} else {
				// local or anonymous type
				allPossibleSubtypes = new String[0];
			}
			if (allPossibleSubtypes != null) {
				IProgressMonitor buildMonitor = 
					this.hierarchy.progressMonitor == null ? 
						null : 
						new SubProgressMonitor(this.hierarchy.progressMonitor, 100 - amountOfWorkForSubtypes);
				this.hierarchy.initialize(allPossibleSubtypes.length);
				buildFromPotentialSubtypes(allPossibleSubtypes, localTypes, buildMonitor);
			}
		} else {
			this.hierarchy.initialize(1);
			this.buildSupertypes();
		}
	} finally {
		manager.flushZipFiles();
	}
}
private void buildForProject(JavaProject project, ArrayList potentialSubtypes, org.eclipse.jdt.core.ICompilationUnit[] workingCopies, HashSet localTypes, IProgressMonitor monitor) throws JavaModelException {
	// copy vectors into arrays
	int openablesLength = potentialSubtypes.size();
	Openable[] openables = new Openable[openablesLength];
	potentialSubtypes.toArray(openables);

	// resolve
	if (openablesLength > 0) {
		IType focusType = this.getType();
		boolean inProjectOfFocusType = focusType != null && focusType.getJavaProject().equals(project);
		org.eclipse.jdt.core.ICompilationUnit[] unitsToLookInside = null;
		if (inProjectOfFocusType) {
			org.eclipse.jdt.core.ICompilationUnit unitToLookInside = focusType.getCompilationUnit();
			if (unitToLookInside != null) {
				int wcLength = workingCopies == null ? 0 : workingCopies.length;
				if (wcLength == 0) {
					unitsToLookInside = new org.eclipse.jdt.core.ICompilationUnit[] {unitToLookInside};
				} else {
					unitsToLookInside = new org.eclipse.jdt.core.ICompilationUnit[wcLength+1];
					unitsToLookInside[0] = unitToLookInside;
					System.arraycopy(workingCopies, 0, unitsToLookInside, 1, wcLength);
				}
			} else {
				unitsToLookInside = workingCopies;
			}
		}

		SearchableEnvironment searchableEnvironment = (SearchableEnvironment)project.newSearchableNameEnvironment(unitsToLookInside);
		this.nameLookup = searchableEnvironment.nameLookup;
		this.hierarchyResolver = 
			new HierarchyResolver(searchableEnvironment, project.getOptions(true), this, new DefaultProblemFactory());
		if (focusType != null) {
			Member declaringMember = ((Member)focusType).getOuterMostLocalContext();
			if (declaringMember == null) {
				// top level or member type
				char[] fullyQualifiedName = focusType.getFullyQualifiedName().toCharArray();
				if (!inProjectOfFocusType && searchableEnvironment.findType(CharOperation.splitOn('.', fullyQualifiedName)) == null) {
					// focus type is not visible in this project: no need to go further
					return;
				}
			} else {
				// local or anonymous type
				Openable openable;
				if (declaringMember.isBinary()) {
					openable = (Openable)declaringMember.getClassFile();
				} else {
					openable = (Openable)declaringMember.getCompilationUnit();
				}
				localTypes = new HashSet();
				localTypes.add(openable.getPath().toString());
				this.hierarchyResolver.resolve(new Openable[] {openable}, localTypes, monitor);
				return;
			}
		}
		this.hierarchyResolver.resolve(openables, localTypes, monitor);
	}
}
/**
 * Configure this type hierarchy based on the given potential subtypes.
 */
private void buildFromPotentialSubtypes(String[] allPotentialSubTypes, HashSet localTypes, IProgressMonitor monitor) {
	IType focusType = this.getType();
		
	// substitute compilation units with working copies
	HashMap wcPaths = new HashMap(); // a map from path to working copies
	int wcLength;
	org.eclipse.jdt.core.ICompilationUnit[] workingCopies = this.hierarchy.workingCopies;
	if (workingCopies != null && (wcLength = workingCopies.length) > 0) {
		String[] newPaths = new String[wcLength];
		for (int i = 0; i < wcLength; i++) {
			org.eclipse.jdt.core.ICompilationUnit workingCopy = workingCopies[i];
			String path = workingCopy.getPath().toString();
			wcPaths.put(path, workingCopy);
			newPaths[i] = path;
		}
		int potentialSubtypesLength = allPotentialSubTypes.length;
		System.arraycopy(allPotentialSubTypes, 0, allPotentialSubTypes = new String[potentialSubtypesLength+wcLength], 0, potentialSubtypesLength);
		System.arraycopy(newPaths, 0, allPotentialSubTypes, potentialSubtypesLength, wcLength);
	}
			
	int length = allPotentialSubTypes.length;

	// inject the compilation unit of the focus type (so that types in
	// this cu have special visibility permission (this is also usefull
	// when the cu is a working copy)
	Openable focusCU = (Openable)focusType.getCompilationUnit();
	String focusPath = null;
	if (focusCU != null) {
		focusPath = focusCU.getPath().toString();
		if (length > 0) {
			System.arraycopy(allPotentialSubTypes, 0, allPotentialSubTypes = new String[length+1], 0, length);
			allPotentialSubTypes[length] = focusPath;	
		} else {
			allPotentialSubTypes = new String[] {focusPath};
		}
		length++;
	}
	
	// sort by projects
	/*
	 * NOTE: To workaround pb with hierarchy resolver that requests top  
	 * level types in the process of caching an enclosing type, this needs to
	 * be sorted in reverse alphabetical order so that top level types are cached
	 * before their inner types.
	 */
	org.eclipse.jdt.internal.core.util.Util.sortReverseOrder(allPotentialSubTypes);
	
	ArrayList potentialSubtypes = new ArrayList();

	try {
		// create element infos for subtypes
		HandleFactory factory = new HandleFactory();
		IJavaProject currentProject = null;
		if (monitor != null) monitor.beginTask("", length*2 /* 1 for build binding, 1 for connect hierarchy*/); //$NON-NLS-1$
		for (int i = 0; i < length; i++) {
			try {
				String resourcePath = allPotentialSubTypes[i];
				
				// skip duplicate paths (e.g. if focus path was injected when it was already a potential subtype)
				if (i > 0 && resourcePath.equals(allPotentialSubTypes[i-1])) continue;
				
				Openable handle;
				org.eclipse.jdt.core.ICompilationUnit workingCopy = (org.eclipse.jdt.core.ICompilationUnit)wcPaths.get(resourcePath);
				if (workingCopy != null) {
					handle = (Openable)workingCopy;
				} else {
					handle = 
						resourcePath.equals(focusPath) ? 
							focusCU :
							factory.createOpenable(resourcePath, this.scope);
					if (handle == null) continue; // match is outside classpath
				}
				
				IJavaProject project = handle.getJavaProject();
				if (currentProject == null) {
					currentProject = project;
					potentialSubtypes = new ArrayList(5);
				} else if (!currentProject.equals(project)) {
					// build current project
					this.buildForProject((JavaProject)currentProject, potentialSubtypes, workingCopies, localTypes, monitor);
					currentProject = project;
					potentialSubtypes = new ArrayList(5);
				}
				
				potentialSubtypes.add(handle);
			} catch (JavaModelException e) {
				continue;
			}
		}
		
		// build last project
		try {
			if (currentProject == null) {
				// case of no potential subtypes
				currentProject = focusType.getJavaProject();
				if (focusType.isBinary()) {
					potentialSubtypes.add(focusType.getClassFile());
				} else {
					potentialSubtypes.add(focusType.getCompilationUnit());
				}
			}
			this.buildForProject((JavaProject)currentProject, potentialSubtypes, workingCopies, localTypes, monitor);
		} catch (JavaModelException e) {
			// ignore
		}
		
		// Compute hierarchy of focus type if not already done (case of a type with potential subtypes that are not real subtypes)
		if (!this.hierarchy.contains(focusType)) {
			try {
				currentProject = focusType.getJavaProject();
				potentialSubtypes = new ArrayList();
				if (focusType.isBinary()) {
					potentialSubtypes.add(focusType.getClassFile());
				} else {
					potentialSubtypes.add(focusType.getCompilationUnit());
				}
				this.buildForProject((JavaProject)currentProject, potentialSubtypes, workingCopies, localTypes, monitor);
			} catch (JavaModelException e) {
				// ignore
			}
		}
		
		// Add focus if not already in (case of a type with no explicit super type)
		if (!this.hierarchy.contains(focusType)) {
			this.hierarchy.addRootClass(focusType);
		}
	} finally {
		if (monitor != null) monitor.done();
	}
}
protected ICompilationUnit createCompilationUnitFromPath(Openable handle,String osPath) {
	ICompilationUnit unit = super.createCompilationUnitFromPath(handle, osPath);
	this.cuToHandle.put(unit, handle);
	return unit;
}
protected IBinaryType createInfoFromClassFile(Openable classFile, String osPath) {
	String documentPath = classFile.getPath().toString();
	IBinaryType binaryType = (IBinaryType)this.binariesFromIndexMatches.get(documentPath);
	if (binaryType != null) {
		this.infoToHandle.put(binaryType, classFile);
		return binaryType;
	} else {
		return super.createInfoFromClassFile(classFile, osPath);
	}
}
protected IBinaryType createInfoFromClassFileInJar(Openable classFile) {
	String filePath = (((ClassFile)classFile).getType().getFullyQualifiedName('$')).replace('.', '/') + SuffixConstants.SUFFIX_STRING_class;
	IPackageFragmentRoot root = classFile.getPackageFragmentRoot();
	String rootPath = root.isExternal() ? root.getPath().toOSString() : root.getPath().toString();
	String documentPath = rootPath + IJavaSearchScope.JAR_FILE_ENTRY_SEPARATOR + filePath;
	IBinaryType binaryType = (IBinaryType)this.binariesFromIndexMatches.get(documentPath);
	if (binaryType != null) {
		this.infoToHandle.put(binaryType, classFile);
		return binaryType;
	} else {
		return super.createInfoFromClassFileInJar(classFile);
	}
}
/**
 * Returns all of the possible subtypes of this type hierarchy.
 * Returns null if they could not be determine.
 */
private String[] determinePossibleSubTypes(final HashSet localTypes, IProgressMonitor monitor) {

	class PathCollector implements IPathRequestor {
		HashSet paths = new HashSet(10);
		public void acceptPath(String path, boolean containsLocalTypes) {
			this.paths.add(path);
			if (containsLocalTypes) {
				localTypes.add(path);
			}
		}
	}
	PathCollector collector = new PathCollector();
	
	try {
		if (monitor != null) monitor.beginTask("", MAXTICKS); //$NON-NLS-1$
		searchAllPossibleSubTypes(
			this.getType(),
			this.scope,
			this.binariesFromIndexMatches,
			collector,
			IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
			monitor);
	} finally {
		if (monitor != null) monitor.done();
	}

	HashSet paths = collector.paths;
	int length = paths.size();
	String[] result = new String[length];
	int count = 0;
	for (Iterator iter = paths.iterator(); iter.hasNext();) {
		result[count++] = (String) iter.next();
	} 
	return result;
}

/**
 * Find the set of candidate subtypes of a given type.
 *
 * The requestor is notified of super type references (with actual path of
 * its occurrence) for all types which are potentially involved inside a particular
 * hierarchy.
 * The match locator is not used here to narrow down the results, the type hierarchy
 * resolver is rather used to compute the whole hierarchy at once.
 * @param type
 * @param scope
 * @param binariesFromIndexMatches
 * @param pathRequestor
 * @param waitingPolicy
 * @param progressMonitor
 */
public static void searchAllPossibleSubTypes(
	IType type,
	IJavaSearchScope scope,
	final Map binariesFromIndexMatches,
	final IPathRequestor pathRequestor,
	int waitingPolicy,	// WaitUntilReadyToSearch | ForceImmediateSearch | CancelIfNotReadyToSearch
	IProgressMonitor progressMonitor) {

	/* embed constructs inside arrays so as to pass them to (inner) collector */
	final Queue queue = new Queue();
	final HashtableOfObject foundSuperNames = new HashtableOfObject(5);

	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();

	/* use a special collector to collect paths and queue new subtype names */
	IndexQueryRequestor searchRequestor = new IndexQueryRequestor() {
		public boolean acceptIndexMatch(String documentPath, SearchPattern indexRecord, SearchParticipant participant) {
			SuperTypeReferencePattern record = (SuperTypeReferencePattern)indexRecord;
			pathRequestor.acceptPath(documentPath, record.enclosingTypeName == IIndexConstants.ONE_ZERO);
			char[] typeName = record.simpleName;
			int suffix = documentPath.toLowerCase().indexOf(SUFFIX_STRING_class);
			if (suffix != -1){ 
				HierarchyBinaryType binaryType = (HierarchyBinaryType)binariesFromIndexMatches.get(documentPath);
				if (binaryType == null){
					char[] enclosingTypeName = record.enclosingTypeName;
					if (enclosingTypeName == IIndexConstants.ONE_ZERO) { // local or anonymous type
						int lastSlash = documentPath.lastIndexOf('/');
						int lastDollar = documentPath.lastIndexOf('$');
						if (lastDollar == -1) {
							// malformed local or anonymous type: it doesn't contain a $ in its name
							// treat it as a top level type
							enclosingTypeName = null;
							typeName = documentPath.substring(lastSlash+1, suffix).toCharArray();
						} else {
							enclosingTypeName = documentPath.substring(lastSlash+1, lastDollar).toCharArray();
							typeName = Util.localTypeName(documentPath, lastDollar, suffix).toCharArray();
						}
					}
					binaryType = new HierarchyBinaryType(record.modifiers, record.pkgName, typeName, enclosingTypeName, record.classOrInterface);
					binariesFromIndexMatches.put(documentPath, binaryType);
				}
				binaryType.recordSuperType(record.superSimpleName, record.superQualification, record.superClassOrInterface);
			}
			if (!foundSuperNames.containsKey(typeName)){
				foundSuperNames.put(typeName, typeName);
				queue.add(typeName);
			}
			return true;
		}		
	};

	SuperTypeReferencePattern pattern =
		new SuperTypeReferencePattern(null, null, false, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
	MatchLocator.setFocus(pattern, type);
	SubTypeSearchJob job = new SubTypeSearchJob(
		pattern, 
		new JavaSearchParticipant(), // java search only
		scope, 
		searchRequestor);

	int ticks = 0;
	queue.add(type.getElementName().toCharArray());
	try {
		while (queue.start <= queue.end) {
			if (progressMonitor != null && progressMonitor.isCanceled()) return;

			// all subclasses of OBJECT are actually all types
			char[] currentTypeName = queue.retrieve();
			if (CharOperation.equals(currentTypeName, IIndexConstants.OBJECT))
				currentTypeName = null;

			// search all index references to a given supertype
			pattern.superSimpleName = currentTypeName;
			indexManager.performConcurrentJob(job, waitingPolicy, null); // no sub progress monitor since its too costly for deep hierarchies
			if (progressMonitor != null && ++ticks <= MAXTICKS)
				progressMonitor.worked(1);

			// in case, we search all subtypes, no need to search further
			if (currentTypeName == null) break;
		}
	} finally {
		job.finished();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8710.java