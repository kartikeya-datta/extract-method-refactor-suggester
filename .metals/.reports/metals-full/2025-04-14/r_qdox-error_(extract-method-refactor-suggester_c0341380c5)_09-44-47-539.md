error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/628.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/628.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/628.java
text:
```scala
U@@til.verbose("CPContainer INIT - FAILED to touch project: "+ this.project.getElementName(), System.err); //$NON-NLS-1$

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
package org.eclipse.jdt.internal.core;

import java.io.File;
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import org.eclipse.jdt.internal.compiler.util.ObjectVector;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * This operation sets an <code>IJavaProject</code>'s classpath.
 *
 * @see IJavaProject
 */
public class SetClasspathOperation extends JavaModelOperation {

	IClasspathEntry[] oldResolvedPath, newResolvedPath;
	IClasspathEntry[] newRawPath;
	boolean canChangeResources;
	boolean classpathWasSaved;
	boolean needCycleCheck;
	boolean needValidation;
	boolean needSave;
	IPath newOutputLocation;
	JavaProject project;
	boolean identicalRoots;

	public static final IClasspathEntry[] ReuseClasspath = new IClasspathEntry[0];
	public static final IClasspathEntry[] UpdateClasspath = new IClasspathEntry[0];
	// if reusing output location, then also reuse clean flag
	public static final IPath ReuseOutputLocation = new Path("Reuse Existing Output Location");  //$NON-NLS-1$
	
	/**
	 * When executed, this operation sets the classpath of the given project.
	 */
	public SetClasspathOperation(
		JavaProject project,
		IClasspathEntry[] oldResolvedPath,
		IClasspathEntry[] newRawPath,
		IPath newOutputLocation,
		boolean canChangeResource,
		boolean needValidation,
		boolean needSave) {

		super(new IJavaElement[] { project });
		this.oldResolvedPath = oldResolvedPath;
		this.newRawPath = newRawPath;
		this.newOutputLocation = newOutputLocation;
		this.canChangeResources = canChangeResource;
		this.needValidation = needValidation;
		this.needSave = needSave;
		this.project = project;
	}

	/**
	 * Adds deltas for the given roots, with the specified change flag,
	 * and closes the root. Helper method for #setClasspath
	 */
	protected void addClasspathDeltas(
		IPackageFragmentRoot[] roots,
		int flag,
		JavaElementDelta delta) {

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
				// force detach source on jar package fragment roots (source will be lazily computed when needed)
				((PackageFragmentRoot) root).setSourceAttachmentProperty(null);// loose info - will be recomputed
			}
		}
	}



	/**
	 * Returns the index of the item in the list if the given list contains the specified entry. If the list does
	 * not contain the entry, -1 is returned.
	 * A helper method for #setClasspath
	 */
	protected int classpathContains(
		IClasspathEntry[] list,
		IClasspathEntry entry) {

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

	/**
	 * Recursively adds all subfolders of <code>folder</code> to the given collection.
	 */
	protected void collectAllSubfolders(IFolder folder, ArrayList collection) throws JavaModelException {
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

	/**
	 * Returns a collection of package fragments that have been added/removed
	 * as the result of changing the output location to/from the given
	 * location. The collection is empty if no package fragments are
	 * affected.
	 */
	protected ArrayList determineAffectedPackageFragments(IPath location) throws JavaModelException {
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
			IClasspathEntry[] classpath = project.getExpandedClasspath(true);
			for (int i = 0; i < classpath.length; i++) {
				IClasspathEntry entry = classpath[i];
				IPath path = classpath[i].getPath();
				if (entry.getEntryKind() != IClasspathEntry.CPE_PROJECT && path.isPrefixOf(location) && !path.equals(location)) {
					IPackageFragmentRoot[] roots = project.computePackageFragmentRoots(classpath[i]);
					IPackageFragmentRoot root = roots[0];
					// now the output location becomes a package fragment - along with any subfolders
					ArrayList folders = new ArrayList();
					folders.add(folder);
					collectAllSubfolders(folder, folders);
					Iterator elements = folders.iterator();
					int segments = path.segmentCount();
					while (elements.hasNext()) {
						IFolder f = (IFolder) elements.next();
						IPath relativePath = f.getFullPath().removeFirstSegments(segments);
						String name = relativePath.toOSString();
						name = name.replace(File.pathSeparatorChar, '.');
						if (name.endsWith(".")) { //$NON-NLS-1$
							name = name.substring(0, name.length() - 1);
						}
						IPackageFragment pkg = root.getPackageFragment(name);
						fragments.add(pkg);
					}
				}
			}
		}
		return fragments;
	}

	/**
	 * Sets the classpath of the pre-specified project.
	 */
	protected void executeOperation() throws JavaModelException {
		// project reference updated - may throw an exception if unable to write .project file
		updateProjectReferencesIfNecessary();

		// classpath file updated - may throw an exception if unable to write .classpath file
		saveClasspathIfNecessary();
		
		// perform classpath and output location updates, if exception occurs in classpath update,
		// make sure the output location is updated before surfacing the exception (in case the output
		// location update also throws an exception, give priority to the classpath update one).
		JavaModelException originalException = null;

		try {
			if (this.newRawPath == UpdateClasspath) this.newRawPath = project.getRawClasspath();
			if (this.newRawPath != ReuseClasspath){
				updateClasspath();
				project.updatePackageFragmentRoots();
				JavaModelManager.getJavaModelManager().getDeltaProcessor().addForRefresh(project);
			}

		} catch(JavaModelException e){
			originalException = e;
			throw e;

		} finally { // if traversed by an exception we still need to update the output location when necessary

			try {
				if (this.newOutputLocation != ReuseOutputLocation) updateOutputLocation();

			} catch(JavaModelException e){
				if (originalException != null) throw originalException; 
				throw e;
			} finally {
				// ensures the project is getting rebuilt if only variable is modified
				if (!this.identicalRoots && this.canChangeResources) {
					try {
						this.project.getProject().touch(this.progressMonitor);
					} catch (CoreException e) {
						if (JavaModelManager.CP_RESOLVE_VERBOSE){
							System.out.println("CPInit - FAILED to touch project: "+ this.project.getElementName()); //$NON-NLS-1$
							e.printStackTrace();
						}
					}
				}				
			}
		}
		done();
	}

	/**
	 * Generates the delta of removed/added/reordered roots.
	 * Use three deltas in case the same root is removed/added/reordered (for
	 * instance, if it is changed from K_SOURCE to K_BINARY or vice versa)
	 */
	protected void generateClasspathChangeDeltas() {

		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		boolean needToUpdateDependents = false;
		JavaElementDelta delta = new JavaElementDelta(getJavaModel());
		boolean hasDelta = false;
		if (this.classpathWasSaved) {
			delta.changed(this.project, IJavaElementDelta.F_CLASSPATH_CHANGED);
			hasDelta = true;
		}
		int oldLength = oldResolvedPath.length;
		int newLength = newResolvedPath.length;
			
		final IndexManager indexManager = manager.getIndexManager();
		Map oldRoots = null;
		IPackageFragmentRoot[] roots = null;
		if (project.isOpen()) {
			try {
				roots = project.getPackageFragmentRoots();
			} catch (JavaModelException e) {
				// ignore
			}
		} else {
			Map allRemovedRoots ;
			if ((allRemovedRoots = manager.getDeltaProcessor().removedRoots) != null) {
		 		roots = (IPackageFragmentRoot[]) allRemovedRoots.get(project);
			}
		}
		if (roots != null) {
			oldRoots = new HashMap();
			for (int i = 0; i < roots.length; i++) {
				IPackageFragmentRoot root = roots[i];
				oldRoots.put(root.getPath(), root);
			}
		}
		for (int i = 0; i < oldLength; i++) {
			
			int index = classpathContains(newResolvedPath, oldResolvedPath[i]);
			if (index == -1) {
				// do not notify remote project changes
				if (oldResolvedPath[i].getEntryKind() == IClasspathEntry.CPE_PROJECT){
					needToUpdateDependents = true;
					this.needCycleCheck = true;
					continue; 
				}

				IPackageFragmentRoot[] pkgFragmentRoots = null;
				if (oldRoots != null) {
					IPackageFragmentRoot oldRoot = (IPackageFragmentRoot)  oldRoots.get(oldResolvedPath[i].getPath());
					if (oldRoot != null) { // use old root if any (could be none if entry wasn't bound)
						pkgFragmentRoots = new IPackageFragmentRoot[] { oldRoot };
					}
				}
				if (pkgFragmentRoots == null) {
					try {
						ObjectVector accumulatedRoots = new ObjectVector();
						HashSet rootIDs = new HashSet(5);
						rootIDs.add(project.rootID());
						project.computePackageFragmentRoots(
							oldResolvedPath[i], 
							accumulatedRoots, 
							rootIDs,
							true, // inside original project
							false, // don't check existency
							false); // don't retrieve exported roots
						pkgFragmentRoots = new IPackageFragmentRoot[accumulatedRoots.size()];
						accumulatedRoots.copyInto(pkgFragmentRoots);
					} catch (JavaModelException e) {
						pkgFragmentRoots =  new IPackageFragmentRoot[] {};
					}
				}
				addClasspathDeltas(pkgFragmentRoots, IJavaElementDelta.F_REMOVED_FROM_CLASSPATH, delta);
				
				int changeKind = oldResolvedPath[i].getEntryKind();
				needToUpdateDependents |= (changeKind == IClasspathEntry.CPE_SOURCE) || oldResolvedPath[i].isExported();

				// Remove the .java files from the index for a source folder
				// For a lib folder or a .jar file, remove the corresponding index if not shared.
				if (indexManager != null) {
					IClasspathEntry oldEntry = oldResolvedPath[i];
					final IPath path = oldEntry.getPath();
					switch (changeKind) {
						case IClasspathEntry.CPE_SOURCE:
							final char[][] inclusionPatterns = ((ClasspathEntry)oldEntry).fullInclusionPatternChars();
							final char[][] exclusionPatterns = ((ClasspathEntry)oldEntry).fullExclusionPatternChars();
							postAction(new IPostAction() {
								public String getID() {
									return path.toString();
								}
								public void run() /* throws JavaModelException */ {
									indexManager.removeSourceFolderFromIndex(project, path, inclusionPatterns, exclusionPatterns);
								}
							}, 
							REMOVEALL_APPEND);
							break;
						case IClasspathEntry.CPE_LIBRARY:
							final DeltaProcessingState deltaState = manager.deltaState;
							postAction(new IPostAction() {
								public String getID() {
									return path.toString();
								}
								public void run() /* throws JavaModelException */ {
									if (deltaState.otherRoots.get(path) == null) { // if root was not shared
										indexManager.discardJobs(path.toString());
										indexManager.removeIndex(path);
										// TODO (kent) we could just remove the in-memory index and have the indexing check for timestamps
									}
								}
							}, 
							REMOVEALL_APPEND);
							break;
					}		
				}
				hasDelta = true;

			} else {
				// do not notify remote project changes
				if (oldResolvedPath[i].getEntryKind() == IClasspathEntry.CPE_PROJECT){
					this.needCycleCheck |= (oldResolvedPath[i].isExported() != newResolvedPath[index].isExported());
					continue; 
				}				
				needToUpdateDependents |= (oldResolvedPath[i].isExported() != newResolvedPath[index].isExported());
				if (index != i) { //reordering of the classpath
						addClasspathDeltas(
							project.computePackageFragmentRoots(oldResolvedPath[i]),
							IJavaElementDelta.F_REORDER,
							delta);
						int changeKind = oldResolvedPath[i].getEntryKind();
						needToUpdateDependents |= (changeKind == IClasspathEntry.CPE_SOURCE);
		
						hasDelta = true;
				}
				
				// check source attachment
				IPath newSourcePath = newResolvedPath[index].getSourceAttachmentPath();
				int sourceAttachmentFlags = 
					this.getSourceAttachmentDeltaFlag(
						oldResolvedPath[i].getSourceAttachmentPath(),
						newSourcePath);
				IPath oldRootPath = oldResolvedPath[i].getSourceAttachmentRootPath();
				IPath newRootPath = newResolvedPath[index].getSourceAttachmentRootPath();
				int sourceAttachmentRootFlags = getSourceAttachmentDeltaFlag(oldRootPath, newRootPath);
				int flags = sourceAttachmentFlags | sourceAttachmentRootFlags;
				if (flags != 0) {
					addClasspathDeltas(project.computePackageFragmentRoots(oldResolvedPath[i]), flags, delta);
					hasDelta = true;
				} else {
					if (oldRootPath == null && newRootPath == null) {
						// if source path is specified and no root path, it needs to be recomputed dynamically
						// force detach source on jar package fragment roots (source will be lazily computed when needed)
						IPackageFragmentRoot[] computedRoots = project.computePackageFragmentRoots(oldResolvedPath[i]);
						for (int j = 0; j < computedRoots.length; j++) {
							IPackageFragmentRoot root = computedRoots[j];
							// force detach source on jar package fragment roots (source will be lazily computed when needed)
							try {
								root.close();
							} catch (JavaModelException e) {
								// ignore
							}
							((PackageFragmentRoot) root).setSourceAttachmentProperty(null);// loose info - will be recomputed
						}
					}
				}
			}
		}

		for (int i = 0; i < newLength; i++) {

			int index = classpathContains(oldResolvedPath, newResolvedPath[i]);
			if (index == -1) {
				// do not notify remote project changes
				if (newResolvedPath[i].getEntryKind() == IClasspathEntry.CPE_PROJECT){
					needToUpdateDependents = true;
					this.needCycleCheck = true;
					continue; 
				}
				addClasspathDeltas(
					project.computePackageFragmentRoots(newResolvedPath[i]),
					IJavaElementDelta.F_ADDED_TO_CLASSPATH,
					delta);
				int changeKind = newResolvedPath[i].getEntryKind();
				
				// Request indexing
				if (indexManager != null) {
					switch (changeKind) {
						case IClasspathEntry.CPE_LIBRARY:
							boolean pathHasChanged = true;
							final IPath newPath = newResolvedPath[i].getPath();
							for (int j = 0; j < oldLength; j++) {
								IClasspathEntry oldEntry = oldResolvedPath[j];
								if (oldEntry.getPath().equals(newPath)) {
									pathHasChanged = false;
									break;
								}
							}
							if (pathHasChanged) {
								postAction(new IPostAction() {
									public String getID() {
										return newPath.toString();
									}
									public void run() /* throws JavaModelException */ {
										indexManager.indexLibrary(newPath, project.getProject());
									}
								}, 
								REMOVEALL_APPEND);
							}
							break;
						case IClasspathEntry.CPE_SOURCE:
							IClasspathEntry entry = newResolvedPath[i];
							final IPath path = entry.getPath();
							final char[][] inclusionPatterns = ((ClasspathEntry)entry).fullInclusionPatternChars();
							final char[][] exclusionPatterns = ((ClasspathEntry)entry).fullExclusionPatternChars();
							postAction(new IPostAction() {
								public String getID() {
									return path.toString();
								}
								public void run() /* throws JavaModelException */ {
									indexManager.indexSourceFolder(project, path, inclusionPatterns, exclusionPatterns);
								}
							}, 
							APPEND); // append so that a removeSourceFolder action is not removed
							break;
					}
				}
				
				needToUpdateDependents |= (changeKind == IClasspathEntry.CPE_SOURCE) || newResolvedPath[i].isExported();
				hasDelta = true;

			} // classpath reordering has already been generated in previous loop
		}

		if (hasDelta) {
			this.addDelta(delta);
		} else {
			this.identicalRoots = true;
		}
		if (needToUpdateDependents){
			updateAffectedProjects(project.getProject().getFullPath());
		}
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

	/**
	 * Returns <code>true</code> if this operation performs no resource modifications,
	 * otherwise <code>false</code>. Subclasses must override.
	 */
	public boolean isReadOnly() {
		return !this.canChangeResources;
	}

	protected void saveClasspathIfNecessary() throws JavaModelException {
		
		if (!this.canChangeResources || !this.needSave) return;
				
		IClasspathEntry[] classpathForSave;
		if (this.newRawPath == ReuseClasspath || this.newRawPath == UpdateClasspath){
			classpathForSave = project.getRawClasspath();
		} else {
			classpathForSave = this.newRawPath;
		}
		IPath outputLocationForSave;
		if (this.newOutputLocation == ReuseOutputLocation){
			outputLocationForSave = project.getOutputLocation();
		} else {
			outputLocationForSave = this.newOutputLocation;
		}
		// if read-only .classpath, then the classpath setting will never been performed completely
		if (project.saveClasspath(classpathForSave, outputLocationForSave)) {
			this.classpathWasSaved = true;
			this.setAttribute(HAS_MODIFIED_RESOURCE_ATTR, TRUE); 
		}
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer(20);
		buffer.append("SetClasspathOperation\n"); //$NON-NLS-1$
		buffer.append(" - classpath : "); //$NON-NLS-1$
		if (this.newRawPath == ReuseClasspath){
			buffer.append("<Reuse Existing Classpath>"); //$NON-NLS-1$
		} else {
			buffer.append("{"); //$NON-NLS-1$
			for (int i = 0; i < this.newRawPath.length; i++) {
				if (i > 0) buffer.append(","); //$NON-NLS-1$
				IClasspathEntry element = this.newRawPath[i];
				buffer.append(" ").append(element.toString()); //$NON-NLS-1$
			}
		}
		buffer.append("\n - output location : ");  //$NON-NLS-1$
		if (this.newOutputLocation == ReuseOutputLocation){
			buffer.append("<Reuse Existing Output Location>"); //$NON-NLS-1$
		} else {
			buffer.append(this.newOutputLocation.toString()); //$NON-NLS-1$
		}
		return buffer.toString();
	}

	private void updateClasspath() throws JavaModelException {

		beginTask(Util.bind("classpath.settingProgress", project.getElementName()), 2); //$NON-NLS-1$

		// SIDE-EFFECT: from thereon, the classpath got modified
		project.getPerProjectInfo().updateClasspathInformation(this.newRawPath);

		// resolve new path (asking for marker creation if problems)
		if (this.newResolvedPath == null) {
			this.newResolvedPath = project.getResolvedClasspath(true, this.canChangeResources);
		}
		
		if (this.oldResolvedPath != null) {
			generateClasspathChangeDeltas();
		} else {
			this.needCycleCheck = true;
			updateAffectedProjects(project.getProject().getFullPath());
		}
		
		updateCycleMarkersIfNecessary();
	}

	/**
	 * Update projects which are affected by this classpath change:
	 * those which refers to the current project as source (indirectly)
	 */
	protected void updateAffectedProjects(IPath prerequisiteProjectPath) {

		// remove all update classpath post actions for this project
		removeAllPostAction(prerequisiteProjectPath.toString());
		
		try {
			IJavaModel model = JavaModelManager.getJavaModelManager().getJavaModel();
			IJavaProject initialProject = this.project;
			IJavaProject[] projects = model.getJavaProjects();
			for (int i = 0, projectCount = projects.length; i < projectCount; i++) {
				try {
					final JavaProject affectedProject = (JavaProject) projects[i];
					if (affectedProject.equals(initialProject)) continue; // skip itself
					
					// consider ALL dependents (even indirect ones), since they may need to
					// flush their respective namelookup caches (all pkg fragment roots).

					IClasspathEntry[] classpath = affectedProject.getExpandedClasspath(true);
					for (int j = 0, entryCount = classpath.length; j < entryCount; j++) {
						IClasspathEntry entry = classpath[j];
						if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT
							&& entry.getPath().equals(prerequisiteProjectPath)) {
								
							postAction(new IPostAction() {
									public String getID() {
										return affectedProject.getPath().toString();
									}
									public void run() throws JavaModelException {
										affectedProject.setRawClasspath(
											UpdateClasspath, 
											SetClasspathOperation.ReuseOutputLocation, 
											SetClasspathOperation.this.progressMonitor, 
											SetClasspathOperation.this.canChangeResources,  
											affectedProject.getResolvedClasspath(true), 
											false, // updating only - no validation
											false); // updating only - no need to save
									}
								},
								REMOVEALL_APPEND);
							break;
						}
					}
				} catch (JavaModelException e) {
					// ignore
				}
			}
		} catch (JavaModelException e) {
			// ignore
		}
		
	}

	/**
	 * Update cycle markers
	 */
	protected void updateCycleMarkersIfNecessary() {

		if (!this.needCycleCheck) return;
		if (!this.canChangeResources) return;
		 
		if (!project.hasCycleMarker() && !project.hasClasspathCycle(newResolvedPath)){
			return;
		}
	
		postAction(
			new IPostAction() {
				public String getID() {
					return "updateCycleMarkers";  //$NON-NLS-1$
				}
				public void run() throws JavaModelException {
					JavaProject.updateAllCycleMarkers(null);
				}
			},
			REMOVEALL_APPEND);
	}

	/**
	 * Sets the output location of the pre-specified project.
	 *
	 * <p>This can cause changes in package fragments, in case either  the
	 * old or new output location folder are considered as a package fragment.
	 */
	protected void updateOutputLocation() throws JavaModelException {
		
		beginTask(Util.bind("classpath.settingOutputLocationProgress", project.getElementName()), 2); //$NON-NLS-1$
		
		IPath oldLocation= project.getOutputLocation();
	
		// see if this will cause any package fragments to be added
		boolean deltaToFire= false;
		JavaElementDelta delta = newJavaElementDelta();
		ArrayList added= determineAffectedPackageFragments(oldLocation);
		Iterator iter = added.iterator();
		while (iter.hasNext()){
			IPackageFragment frag= (IPackageFragment)iter.next();
			((IPackageFragmentRoot)frag.getParent()).close();
			if (!Util.isExcluded(frag)) {
				delta.added(frag);
				deltaToFire = true;
			}
		}
	
		// see if this will cause any package fragments to be removed
		ArrayList removed= determineAffectedPackageFragments(this.newOutputLocation);
		iter = removed.iterator();
		while (iter.hasNext()){
			IPackageFragment frag= (IPackageFragment)iter.next();
			((IPackageFragmentRoot)frag.getParent()).close(); 
			if (!Util.isExcluded(frag)) {
				delta.removed(frag);
				deltaToFire = true;
			}
		}

		JavaModelManager.PerProjectInfo perProjectInfo = project.getPerProjectInfo();
		synchronized (perProjectInfo) {
			perProjectInfo.outputLocation = this.newOutputLocation;
		}
				
		if (deltaToFire) {
			addDelta(delta);	
		}
		worked(1);
	}
	
	/**
	 * Update projects references so that the build order is consistent with the classpath
	 */
	protected void updateProjectReferencesIfNecessary() throws JavaModelException {
		
		if (!this.canChangeResources) return;
		if (this.newRawPath == ReuseClasspath || this.newRawPath == UpdateClasspath) return;
	
		String[] oldRequired = this.project.projectPrerequisites(this.oldResolvedPath);

		if (this.newResolvedPath == null) {
			this.newResolvedPath = this.project.getResolvedClasspath(this.newRawPath, null, true, true, null/*no reverse map*/);
		}
		String[] newRequired = this.project.projectPrerequisites(this.newResolvedPath);
	
		try {		
			IProject projectResource = this.project.getProject();
			IProjectDescription description = projectResource.getDescription();
			 
			IProject[] projectReferences = description.getDynamicReferences();
			
			HashSet oldReferences = new HashSet(projectReferences.length);
			for (int i = 0; i < projectReferences.length; i++){
				String projectName = projectReferences[i].getName();
				oldReferences.add(projectName);
			}
			HashSet newReferences = (HashSet)oldReferences.clone();
	
			for (int i = 0; i < oldRequired.length; i++){
				String projectName = oldRequired[i];
				newReferences.remove(projectName);
			}
			for (int i = 0; i < newRequired.length; i++){
				String projectName = newRequired[i];
				newReferences.add(projectName);
			}
	
			Iterator iter;
			int newSize = newReferences.size();
			
			checkIdentity: {
				if (oldReferences.size() == newSize){
					iter = newReferences.iterator();
					while (iter.hasNext()){
						if (!oldReferences.contains(iter.next())){
							break checkIdentity;
						}
					}
					return;
				}
			}
			String[] requiredProjectNames = new String[newSize];
			int index = 0;
			iter = newReferences.iterator();
			while (iter.hasNext()){
				requiredProjectNames[index++] = (String)iter.next();
			}
			Util.sort(requiredProjectNames); // ensure that if changed, the order is consistent
			
			IProject[] requiredProjectArray = new IProject[newSize];
			IWorkspaceRoot wksRoot = projectResource.getWorkspace().getRoot();
			for (int i = 0; i < newSize; i++){
				requiredProjectArray[i] = wksRoot.getProject(requiredProjectNames[i]);
			}
	
			description.setDynamicReferences(requiredProjectArray);
			projectResource.setDescription(description, this.progressMonitor);
	
		} catch(CoreException e){
			throw new JavaModelException(e);
		}
	}

	public IJavaModelStatus verify() {

		IJavaModelStatus status = super.verify();
		if (!status.isOK()) {
			return status;
		}

		if (needValidation) {
			// retrieve classpath 
			IClasspathEntry[] entries = this.newRawPath;
			if (entries == ReuseClasspath){
				try {
					entries = project.getRawClasspath();			
				} catch (JavaModelException e) {
					return e.getJavaModelStatus();
				}
			}		
			// retrieve output location
			IPath outputLocation = this.newOutputLocation;
			if (outputLocation == ReuseOutputLocation){
				try {
					outputLocation = project.getOutputLocation();
				} catch (JavaModelException e) {
					return e.getJavaModelStatus();
				}
			}
					
			// perform validation
			return ClasspathEntry.validateClasspath(
				project,
				entries,
				outputLocation);
		}
		
		return JavaModelStatus.VERIFIED_OK;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/628.java