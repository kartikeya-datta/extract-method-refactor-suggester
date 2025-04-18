error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/331.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/331.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/331.java
text:
```scala
P@@erProjectInfo temporaryInfo = this.project.newTemporaryInfo();

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.util.ObjectVector;
import org.eclipse.jdt.internal.core.JavaModelManager.PerProjectInfo;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;
import org.eclipse.jdt.internal.core.util.Util;

public class ClasspathChange {
	public static final int NO_DELTA = 0x00;
	public static final int HAS_DELTA = 0x01;
	public static final int HAS_PROJECT_CHANGE = 0x02;
	public static final int HAS_LIBRARY_CHANGE = 0x04;

	JavaProject project;
	IClasspathEntry[] oldRawClasspath;
	IPath oldOutputLocation;
	IClasspathEntry[] oldResolvedClasspath;

	public ClasspathChange(JavaProject project, IClasspathEntry[] oldRawClasspath, IPath oldOutputLocation, IClasspathEntry[] oldResolvedClasspath) {
		this.project = project;
		this.oldRawClasspath = oldRawClasspath;
		this.oldOutputLocation = oldOutputLocation;
		this.oldResolvedClasspath = oldResolvedClasspath;
	}

	private void addClasspathDeltas(JavaElementDelta delta, IPackageFragmentRoot[] roots, int flag) {
		for (int i = 0; i < roots.length; i++) {
			IPackageFragmentRoot root = roots[i];
			delta.changed(root, flag);
			if ((flag & IJavaElementDelta.F_REMOVED_FROM_CLASSPATH) != 0
 (flag & IJavaElementDelta.F_SOURCEATTACHED) != 0
 (flag & IJavaElementDelta.F_SOURCEDETACHED) != 0){
				try {
					root.close();
				} catch (JavaModelException e) {
					// ignore
				}
			}
		}
	}

	/*
	 * Returns the index of the item in the list if the given list contains the specified entry. If the list does
	 * not contain the entry, -1 is returned.
	 */
	private int classpathContains(IClasspathEntry[] list, IClasspathEntry entry) {
		IPath[] exclusionPatterns = entry.getExclusionPatterns();
		IPath[] inclusionPatterns = entry.getInclusionPatterns();
		nextEntry: for (int i = 0; i < list.length; i++) {
			IClasspathEntry other = list[i];
			if (other.getContentKind() == entry.getContentKind()
				&& other.getEntryKind() == entry.getEntryKind()
				&& other.isExported() == entry.isExported()
				&& other.getPath().equals(entry.getPath())) {
					// check custom outputs
					IPath entryOutput = entry.getOutputLocation();
					IPath otherOutput = other.getOutputLocation();
					if (entryOutput == null) {
						if (otherOutput != null)
							continue;
					} else {
						if (!entryOutput.equals(otherOutput))
							continue;
					}

					// check inclusion patterns
					IPath[] otherIncludes = other.getInclusionPatterns();
					if (inclusionPatterns != otherIncludes) {
					    if (inclusionPatterns == null) continue;
						int includeLength = inclusionPatterns.length;
						if (otherIncludes == null || otherIncludes.length != includeLength)
							continue;
						for (int j = 0; j < includeLength; j++) {
							// compare toStrings instead of IPaths
							// since IPath.equals is specified to ignore trailing separators
							if (!inclusionPatterns[j].toString().equals(otherIncludes[j].toString()))
								continue nextEntry;
						}
					}
					// check exclusion patterns
					IPath[] otherExcludes = other.getExclusionPatterns();
					if (exclusionPatterns != otherExcludes) {
					    if (exclusionPatterns == null) continue;
						int excludeLength = exclusionPatterns.length;
						if (otherExcludes == null || otherExcludes.length != excludeLength)
							continue;
						for (int j = 0; j < excludeLength; j++) {
							// compare toStrings instead of IPaths
							// since IPath.equals is specified to ignore trailing separators
							if (!exclusionPatterns[j].toString().equals(otherExcludes[j].toString()))
								continue nextEntry;
						}
					}
					return i;
			}
		}
		return -1;
	}

	/*
	 * Recursively adds all subfolders of <code>folder</code> to the given collection.
	 */
	private void collectAllSubfolders(IFolder folder, ArrayList collection) throws JavaModelException {
		try {
			IResource[] members= folder.members();
			for (int i = 0, max = members.length; i < max; i++) {
				IResource r= members[i];
				if (r.getType() == IResource.FOLDER) {
					collection.add(r);
					collectAllSubfolders((IFolder)r, collection);
				}
			}
		} catch (CoreException e) {
			throw new JavaModelException(e);
		}
	}

	/*
	 * Returns a collection of package fragments that have been added/removed
	 * as the result of changing the output location to/from the given
	 * location. The collection is empty if no package fragments are
	 * affected.
	 */
	private ArrayList determineAffectedPackageFragments(IPath location) throws JavaModelException {
		ArrayList fragments = new ArrayList();

		// see if this will cause any package fragments to be affected
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IResource resource = null;
		if (location != null) {
			resource = workspace.getRoot().findMember(location);
		}
		if (resource != null && resource.getType() == IResource.FOLDER) {
			IFolder folder = (IFolder) resource;
			// only changes if it actually existed
			IClasspathEntry[] classpath = this.project.getExpandedClasspath();
			for (int i = 0; i < classpath.length; i++) {
				IClasspathEntry entry = classpath[i];
				IPath path = classpath[i].getPath();
				if (entry.getEntryKind() != IClasspathEntry.CPE_PROJECT && path.isPrefixOf(location) && !path.equals(location)) {
					IPackageFragmentRoot[] roots = this.project.computePackageFragmentRoots(classpath[i]);
					PackageFragmentRoot root = (PackageFragmentRoot) roots[0];
					// now the output location becomes a package fragment - along with any subfolders
					ArrayList folders = new ArrayList();
					folders.add(folder);
					collectAllSubfolders(folder, folders);
					Iterator elements = folders.iterator();
					int segments = path.segmentCount();
					while (elements.hasNext()) {
						IFolder f = (IFolder) elements.next();
						IPath relativePath = f.getFullPath().removeFirstSegments(segments);
						String[] pkgName = relativePath.segments();
						IPackageFragment pkg = root.getPackageFragment(pkgName);
						if (!Util.isExcluded(pkg))
							fragments.add(pkg);
					}
				}
			}
		}
		return fragments;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ClasspathChange))
			return false;
		return this.project.equals(((ClasspathChange) obj).project);
	}

	/*
	 * Generates a classpath change delta for this classpath change.
	 * Returns whether a delta was generated, and whether project reference have changed.
	 */
	public int generateDelta(JavaElementDelta delta) {
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		DeltaProcessingState state = manager.deltaState;
		if (state.findJavaProject(this.project.getElementName()) == null)
			// project doesn't exist yet (we're in an IWorkspaceRunnable)
			// no need to create a delta here and no need to index (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=133334)
			// the delta processor will create an ADDED project delta, and index the project
			return NO_DELTA;

		DeltaProcessor deltaProcessor = state.getDeltaProcessor();
		IClasspathEntry[] newResolvedClasspath = null;
		IPath newOutputLocation = null;
		int result = NO_DELTA;
		try {
			PerProjectInfo perProjectInfo = this.project.getPerProjectInfo();

			// get new info
			this.project.resolveClasspath(perProjectInfo, false/*don't use previous session values*/);
			IClasspathEntry[] newRawClasspath;

			// use synchronized block to ensure consistency
			synchronized (perProjectInfo) {
				newRawClasspath = perProjectInfo.rawClasspath;
				newResolvedClasspath = perProjectInfo.getResolvedClasspath();
				newOutputLocation = perProjectInfo.outputLocation;
			}

			if (newResolvedClasspath == null) {
				// another thread reset the resolved classpath, use a temporary PerProjectInfo
				PerProjectInfo temporaryInfo = new PerProjectInfo(this.project.getProject());
				this.project.resolveClasspath(temporaryInfo, false/*don't use previous session values*/);
				newRawClasspath = temporaryInfo.rawClasspath;
				newResolvedClasspath = temporaryInfo.getResolvedClasspath();
				newOutputLocation = temporaryInfo.outputLocation;
			}

			// check if raw classpath has changed
			if (this.oldRawClasspath != null && !JavaProject.areClasspathsEqual(this.oldRawClasspath, newRawClasspath, this.oldOutputLocation, newOutputLocation)) {
				delta.changed(this.project, IJavaElementDelta.F_CLASSPATH_CHANGED);
				result |= HAS_DELTA;

				// reset containers that are no longer on the classpath
				// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=139446)
				for (int i = 0, length = this.oldRawClasspath.length; i < length; i++) {
					IClasspathEntry entry = this.oldRawClasspath[i];
					if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
						if (classpathContains(newRawClasspath, entry) == -1)
							manager.containerPut(this.project, entry.getPath(), null);
					}
				}
			}

			// if no changes to resolved classpath, nothing more to do
			if (this.oldResolvedClasspath != null && JavaProject.areClasspathsEqual(this.oldResolvedClasspath, newResolvedClasspath, this.oldOutputLocation, newOutputLocation))
				return result;

			// close cached info
			this.project.close();

			// ensure caches of dependent projects are reset as well (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=207890)
			deltaProcessor.projectCachesToReset.add(this.project);
		} catch (JavaModelException e) {
			if (DeltaProcessor.VERBOSE) {
				e.printStackTrace();
			}
			// project no longer exist
			return result;
		}

		if (this.oldResolvedClasspath == null)
			return result;

		delta.changed(this.project, IJavaElementDelta.F_RESOLVED_CLASSPATH_CHANGED);
		result |= HAS_DELTA;

		state.addForRefresh(this.project); // ensure external jars are refreshed for this project (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=212769 )

		Map removedRoots = null;
		IPackageFragmentRoot[] roots = null;
		Map allOldRoots ;
		if ((allOldRoots = deltaProcessor.oldRoots) != null) {
	 		roots = (IPackageFragmentRoot[]) allOldRoots.get(this.project);
		}
		if (roots != null) {
			removedRoots = new HashMap();
			for (int i = 0; i < roots.length; i++) {
				IPackageFragmentRoot root = roots[i];
				removedRoots.put(root.getPath(), root);
			}
		}

		int newLength = newResolvedClasspath.length;
		int oldLength = this.oldResolvedClasspath.length;
		for (int i = 0; i < oldLength; i++) {
			int index = classpathContains(newResolvedClasspath, this.oldResolvedClasspath[i]);
			if (index == -1) {
				// remote project changes
				int entryKind = this.oldResolvedClasspath[i].getEntryKind();
				if (entryKind == IClasspathEntry.CPE_PROJECT) {
					result |= HAS_PROJECT_CHANGE;
					continue;
				}
				if (entryKind == IClasspathEntry.CPE_LIBRARY) {
					result |= HAS_LIBRARY_CHANGE;
				}

				PackageFragmentRoot[] pkgFragmentRoots = null;
				if (removedRoots != null) {
					PackageFragmentRoot oldRoot = (PackageFragmentRoot)  removedRoots.get(this.oldResolvedClasspath[i].getPath());
					if (oldRoot != null) { // use old root if any (could be none if entry wasn't bound)
						pkgFragmentRoots = new PackageFragmentRoot[] { oldRoot };
					}
				}
				if (pkgFragmentRoots == null) {
					try {
						ObjectVector accumulatedRoots = new ObjectVector();
						HashSet rootIDs = new HashSet(5);
						rootIDs.add(this.project.rootID());
						this.project.computePackageFragmentRoots(
							this.oldResolvedClasspath[i],
							accumulatedRoots,
							rootIDs,
							null, // inside original project
							false, // don't retrieve exported roots
							null); /*no reverse map*/
						pkgFragmentRoots = new PackageFragmentRoot[accumulatedRoots.size()];
						accumulatedRoots.copyInto(pkgFragmentRoots);
					} catch (JavaModelException e) {
						pkgFragmentRoots =  new PackageFragmentRoot[] {};
					}
				}
				addClasspathDeltas(delta, pkgFragmentRoots, IJavaElementDelta.F_REMOVED_FROM_CLASSPATH);
			} else {
				// remote project changes
				if (this.oldResolvedClasspath[i].getEntryKind() == IClasspathEntry.CPE_PROJECT) {
					result |= HAS_PROJECT_CHANGE;
					continue;
				}
				if (index != i) { //reordering of the classpath
					addClasspathDeltas(delta, this.project.computePackageFragmentRoots(this.oldResolvedClasspath[i]),	IJavaElementDelta.F_REORDER);
				}

				// check source attachment
				IPath newSourcePath = newResolvedClasspath[index].getSourceAttachmentPath();
				int sourceAttachmentFlags = getSourceAttachmentDeltaFlag(this.oldResolvedClasspath[i].getSourceAttachmentPath(), newSourcePath);
				IPath oldRootPath = this.oldResolvedClasspath[i].getSourceAttachmentRootPath();
				IPath newRootPath = newResolvedClasspath[index].getSourceAttachmentRootPath();
				int sourceAttachmentRootFlags = getSourceAttachmentDeltaFlag(oldRootPath, newRootPath);
				int flags = sourceAttachmentFlags | sourceAttachmentRootFlags;
				if (flags != 0) {
					addClasspathDeltas(delta, this.project.computePackageFragmentRoots(this.oldResolvedClasspath[i]), flags);
				} else {
					if (oldRootPath == null && newRootPath == null) {
						// if source path is specified and no root path, it needs to be recomputed dynamically
						// force detach source on jar package fragment roots (source will be lazily computed when needed)
						IPackageFragmentRoot[] computedRoots = this.project.computePackageFragmentRoots(this.oldResolvedClasspath[i]);
						for (int j = 0; j < computedRoots.length; j++) {
							IPackageFragmentRoot root = computedRoots[j];
							// force detach source on jar package fragment roots (source will be lazily computed when needed)
							try {
								root.close();
							} catch (JavaModelException e) {
								// ignore
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < newLength; i++) {
			int index = classpathContains(this.oldResolvedClasspath, newResolvedClasspath[i]);
			if (index == -1) {
				// remote project changes
				int entryKind = newResolvedClasspath[i].getEntryKind();
				if (entryKind == IClasspathEntry.CPE_PROJECT) {
					result |= HAS_PROJECT_CHANGE;
					continue;
				}
				if (entryKind == IClasspathEntry.CPE_LIBRARY) {
					result |= HAS_LIBRARY_CHANGE;
				}
				addClasspathDeltas(delta, this.project.computePackageFragmentRoots(newResolvedClasspath[i]), IJavaElementDelta.F_ADDED_TO_CLASSPATH);
			} // classpath reordering has already been generated in previous loop
		}

		// see if a change in output location will cause any package fragments to be added/removed
		if ((newOutputLocation == null && this.oldOutputLocation != null)
 (newOutputLocation != null && !newOutputLocation.equals(this.oldOutputLocation))) {
			try {
				ArrayList added = determineAffectedPackageFragments(this.oldOutputLocation);
				Iterator iter = added.iterator();
				while (iter.hasNext()){
					IPackageFragment frag= (IPackageFragment)iter.next();
					((IPackageFragmentRoot)frag.getParent()).close();
					delta.added(frag);
				}

				// see if this will cause any package fragments to be removed
				ArrayList removed = determineAffectedPackageFragments(newOutputLocation);
				iter = removed.iterator();
				while (iter.hasNext()) {
					IPackageFragment frag= (IPackageFragment)iter.next();
					((IPackageFragmentRoot)frag.getParent()).close();
					delta.removed(frag);
				}
			} catch (JavaModelException e) {
				if (DeltaProcessor.VERBOSE)
					e.printStackTrace();
			}
		}

		return result;
	}

	/*
	 * Returns the source attachment flag for the delta between the 2 give source paths.
	 * Returns either F_SOURCEATTACHED, F_SOURCEDETACHED, F_SOURCEATTACHED | F_SOURCEDETACHED
	 * or 0 if there is no difference.
	 */
	private int getSourceAttachmentDeltaFlag(IPath oldPath, IPath newPath) {
		if (oldPath == null) {
			if (newPath != null) {
				return IJavaElementDelta.F_SOURCEATTACHED;
			} else {
				return 0;
			}
		} else if (newPath == null) {
			return IJavaElementDelta.F_SOURCEDETACHED;
		} else if (!oldPath.equals(newPath)) {
			return IJavaElementDelta.F_SOURCEATTACHED | IJavaElementDelta.F_SOURCEDETACHED;
		} else {
			return 0;
		}
	}

	public int hashCode() {
		return this.project.hashCode();
	}

	/*
	 * Request the indexing of entries that have been added, and remove the index for removed entries.
	 */
	public void requestIndexing() {
		IClasspathEntry[] newResolvedClasspath = null;
		try {
			newResolvedClasspath = this.project.getResolvedClasspath();
		} catch (JavaModelException e) {
			// project doesn't exist
			return;
		}

		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		IndexManager indexManager = manager.indexManager;
		if (indexManager == null)
			return;
		DeltaProcessingState state = manager.deltaState;

		int newLength = newResolvedClasspath.length;
		int oldLength = this.oldResolvedClasspath.length;
		for (int i = 0; i < oldLength; i++) {
			int index = classpathContains(newResolvedClasspath, this.oldResolvedClasspath[i]);
			if (index == -1) {
				// remote projects are not indexed in this project
				if (this.oldResolvedClasspath[i].getEntryKind() == IClasspathEntry.CPE_PROJECT){
					continue;
				}

				// Remove the .java files from the index for a source folder
				// For a lib folder or a .jar file, remove the corresponding index if not shared.
				IClasspathEntry oldEntry = this.oldResolvedClasspath[i];
				final IPath path = oldEntry.getPath();
				int changeKind = this.oldResolvedClasspath[i].getEntryKind();
				switch (changeKind) {
					case IClasspathEntry.CPE_SOURCE:
						char[][] inclusionPatterns = ((ClasspathEntry)oldEntry).fullInclusionPatternChars();
						char[][] exclusionPatterns = ((ClasspathEntry)oldEntry).fullExclusionPatternChars();
						indexManager.removeSourceFolderFromIndex(this.project, path, inclusionPatterns, exclusionPatterns);
						break;
					case IClasspathEntry.CPE_LIBRARY:
						if (state.otherRoots.get(path) == null) { // if root was not shared
							indexManager.discardJobs(path.toString());
							indexManager.removeIndex(path);
							// TODO (kent) we could just remove the in-memory index and have the indexing check for timestamps
						}
						break;
				}
			}
		}

		for (int i = 0; i < newLength; i++) {
			int index = classpathContains(this.oldResolvedClasspath, newResolvedClasspath[i]);
			if (index == -1) {
				// remote projects are not indexed in this project
				if (newResolvedClasspath[i].getEntryKind() == IClasspathEntry.CPE_PROJECT){
					continue;
				}

				// Request indexing
				int entryKind = newResolvedClasspath[i].getEntryKind();
				switch (entryKind) {
					case IClasspathEntry.CPE_LIBRARY:
						boolean pathHasChanged = true;
						IPath newPath = newResolvedClasspath[i].getPath();
						for (int j = 0; j < oldLength; j++) {
							IClasspathEntry oldEntry = this.oldResolvedClasspath[j];
							if (oldEntry.getPath().equals(newPath)) {
								pathHasChanged = false;
								break;
							}
						}
						if (pathHasChanged) {
							indexManager.indexLibrary(newPath, this.project.getProject());
						}
						break;
					case IClasspathEntry.CPE_SOURCE:
						IClasspathEntry entry = newResolvedClasspath[i];
						IPath path = entry.getPath();
						char[][] inclusionPatterns = ((ClasspathEntry)entry).fullInclusionPatternChars();
						char[][] exclusionPatterns = ((ClasspathEntry)entry).fullExclusionPatternChars();
						indexManager.indexSourceFolder(this.project, path, inclusionPatterns, exclusionPatterns);
						break;
				}
			}
		}
	}

	public String toString() {
		return "ClasspathChange: " + this.project.getElementName(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/331.java