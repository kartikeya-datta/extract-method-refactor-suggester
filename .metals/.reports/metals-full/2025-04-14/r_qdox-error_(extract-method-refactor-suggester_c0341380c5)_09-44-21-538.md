error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3448.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3448.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3448.java
text:
```scala
S@@tring packageName = index == -1 ? "" : new String(CharOperation.subarray(secondaryTypeName, 0, index)); //$NON-NLS-1$

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
package org.eclipse.jdt.internal.core.builder;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.classfmt.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.SimpleLookupTable;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jdt.internal.core.util.Util;

import java.io.*;
import java.net.URI;
import java.util.*;

/**
 * The incremental image builder
 */
public class IncrementalImageBuilder extends AbstractImageBuilder {

protected ArrayList sourceFiles;
protected ArrayList previousSourceFiles;
protected StringSet qualifiedStrings;
protected StringSet simpleStrings;
protected SimpleLookupTable secondaryTypesToRemove;
protected boolean hasStructuralChanges;
protected int compileLoop;
protected boolean makeOutputFolderConsistent;

public static int MaxCompileLoop = 5; // perform a full build if it takes more than ? incremental compile loops

protected IncrementalImageBuilder(JavaBuilder javaBuilder, State buildState) {
	super(javaBuilder, true, buildState);
	this.nameEnvironment.isIncrementalBuild = true;
	this.makeOutputFolderConsistent = JavaCore.ENABLED.equals(
		javaBuilder.javaProject.getOption(JavaCore.CORE_JAVA_BUILD_RECREATE_MODIFIED_CLASS_FILES_IN_OUTPUT_FOLDER, true));
}

protected IncrementalImageBuilder(JavaBuilder javaBuilder) {
	this(javaBuilder, null);
	this.newState.copyFrom(javaBuilder.lastState);
}

protected IncrementalImageBuilder(BatchImageBuilder batchBuilder) {
	this(batchBuilder.javaBuilder, batchBuilder.newState);
	resetCollections();
}

public boolean build(SimpleLookupTable deltas) {
	// initialize builder
	// walk this project's deltas, find changed source files
	// walk prereq projects' deltas, find changed class files & add affected source files
	//   use the build state # to skip the deltas for certain prereq projects
	//   ignore changed zip/jar files since they caused a full build
	// compile the source files & acceptResult()
	// compare the produced class files against the existing ones on disk
	// recompile all dependent source files of any type with structural changes or new/removed secondary type
	// keep a loop counter to abort & perform a full build

	if (JavaBuilder.DEBUG)
		System.out.println("INCREMENTAL build"); //$NON-NLS-1$

	try {
		resetCollections();

		notifier.subTask(Messages.build_analyzingDeltas); 
		IResourceDelta sourceDelta = (IResourceDelta) deltas.get(javaBuilder.currentProject);
		if (sourceDelta != null)
			if (!findSourceFiles(sourceDelta)) return false;
		notifier.updateProgressDelta(0.10f);

		Object[] keyTable = deltas.keyTable;
		Object[] valueTable = deltas.valueTable;
		for (int i = 0, l = valueTable.length; i < l; i++) {
			IResourceDelta delta = (IResourceDelta) valueTable[i];
			if (delta != null) {
				IProject p = (IProject) keyTable[i];
				ClasspathLocation[] classFoldersAndJars = (ClasspathLocation[]) javaBuilder.binaryLocationsPerProject.get(p);
				if (classFoldersAndJars != null)
					if (!findAffectedSourceFiles(delta, classFoldersAndJars, p)) return false;
			}
		}
		notifier.updateProgressDelta(0.10f);

		notifier.subTask(Messages.build_analyzingSources); 
		addAffectedSourceFiles();
		notifier.updateProgressDelta(0.05f);

		this.compileLoop = 0;
		float increment = 0.40f;
		while (sourceFiles.size() > 0) { // added to in acceptResult
			if (++this.compileLoop > MaxCompileLoop) {
				if (JavaBuilder.DEBUG)
					System.out.println("ABORTING incremental build... exceeded loop count"); //$NON-NLS-1$
				return false;
			}
			notifier.checkCancel();

			SourceFile[] allSourceFiles = new SourceFile[sourceFiles.size()];
			sourceFiles.toArray(allSourceFiles);
			resetCollections();

			workQueue.addAll(allSourceFiles);
			notifier.setProgressPerCompilationUnit(increment / allSourceFiles.length);
			increment = increment / 2;
			compile(allSourceFiles);
			removeSecondaryTypes();
			addAffectedSourceFiles();
		}
		if (this.hasStructuralChanges && javaBuilder.javaProject.hasCycleMarker())
			javaBuilder.mustPropagateStructuralChanges();
	} catch (AbortIncrementalBuildException e) {
		// abort the incremental build and let the batch builder handle the problem
		if (JavaBuilder.DEBUG)
			System.out.println("ABORTING incremental build... problem with " + e.qualifiedTypeName + //$NON-NLS-1$
				". Likely renamed inside its existing source file."); //$NON-NLS-1$
		return false;
	} catch (CoreException e) {
		throw internalException(e);
	} finally {
		cleanUp();
	}
	return true;
}

protected void buildAfterBatchBuild() {
	// called from a batch builder once all source files have been compiled AND some changes
	// need to be propagated incrementally (annotations, missing secondary types)

	if (JavaBuilder.DEBUG)
		System.out.println("INCREMENTAL build after batch build @ " + new Date(System.currentTimeMillis())); //$NON-NLS-1$

	// this is a copy of the incremental build loop
	try {
		addAffectedSourceFiles();
		while (this.sourceFiles.size() > 0) {
			notifier.checkCancel();
			SourceFile[] allSourceFiles = new SourceFile[this.sourceFiles.size()];
			this.sourceFiles.toArray(allSourceFiles);
			resetCollections();
			notifier.setProgressPerCompilationUnit(0.08f / allSourceFiles.length);
			this.workQueue.addAll(allSourceFiles);
			compile(allSourceFiles);
			removeSecondaryTypes();
			addAffectedSourceFiles();
		}
	} catch (CoreException e) {
		throw internalException(e);
	} finally {
		cleanUp();
	}
}

protected void addAffectedSourceFiles() {
	if (qualifiedStrings.elementSize == 0 && simpleStrings.elementSize == 0) return;

	addAffectedSourceFiles(qualifiedStrings, simpleStrings);
}

protected void addAffectedSourceFiles(char[] secondaryTypeName) {
	// the secondary type search can have too many false hits if we addAffectedSource files using all the qualified type names
	// of each secondary type... so look for the dependents 1 file at a time
	int index = CharOperation.lastIndexOf('/', secondaryTypeName);
	String packageName = index == -1 ? null : new String(CharOperation.subarray(secondaryTypeName, 0, index));
	StringSet packageNames = new StringSet(1);
	packageNames.add(packageName);
	String typeName = new String(index == -1 ? secondaryTypeName : CharOperation.subarray(secondaryTypeName, index + 1, secondaryTypeName.length));
	StringSet typeNames = new StringSet(1);
	typeNames.add(typeName);

	addAffectedSourceFiles(packageNames, typeNames);
}

private void addAffectedSourceFiles(StringSet qualifiedSet, StringSet simpleSet) {
	// the qualifiedStrings are of the form 'p1/p2' & the simpleStrings are just 'X'
	char[][][] internedQualifiedNames = ReferenceCollection.internQualifiedNames(qualifiedSet);
	// if a well known qualified name was found then we can skip over these
	if (internedQualifiedNames.length < qualifiedSet.elementSize)
		internedQualifiedNames = null;
	char[][] internedSimpleNames = ReferenceCollection.internSimpleNames(simpleSet);
	// if a well known name was found then we can skip over these
	if (internedSimpleNames.length < simpleSet.elementSize)
		internedSimpleNames = null;

	Object[] keyTable = newState.references.keyTable;
	Object[] valueTable = newState.references.valueTable;
	next : for (int i = 0, l = valueTable.length; i < l; i++) {
		ReferenceCollection refs = (ReferenceCollection) valueTable[i];
		if (refs != null && refs.includes(internedQualifiedNames, internedSimpleNames)) {
			String typeLocator = (String) keyTable[i];
			IFile file = javaBuilder.currentProject.getFile(typeLocator);
			SourceFile sourceFile = findSourceFile(file);
			if (sourceFile == null) continue next;
			if (sourceFiles.contains(sourceFile)) continue next;
			if (compiledAllAtOnce && previousSourceFiles != null && previousSourceFiles.contains(sourceFile))
				continue next; // can skip previously compiled files since already saw hierarchy related problems

			if (JavaBuilder.DEBUG)
				System.out.println("  adding affected source file " + typeLocator); //$NON-NLS-1$
			sourceFiles.add(sourceFile);
		}
	}
}

protected void addDependentsOf(IPath path, boolean isStructuralChange) {
	if (isStructuralChange && !this.hasStructuralChanges) {
		newState.tagAsStructurallyChanged();
		this.hasStructuralChanges = true;
	}
	// the qualifiedStrings are of the form 'p1/p2' & the simpleStrings are just 'X'
	path = path.setDevice(null);
	String packageName = path.removeLastSegments(1).toString();
	qualifiedStrings.add(packageName);
	String typeName = path.lastSegment();
	int memberIndex = typeName.indexOf('$');
	if (memberIndex > 0)
		typeName = typeName.substring(0, memberIndex);
	if (simpleStrings.add(typeName) && JavaBuilder.DEBUG)
		System.out.println("  will look for dependents of " //$NON-NLS-1$
			+ typeName + " in " + packageName); //$NON-NLS-1$
}

protected boolean checkForClassFileChanges(IResourceDelta binaryDelta, ClasspathMultiDirectory md, int segmentCount) throws CoreException {
	IResource resource = binaryDelta.getResource();
	// remember that if inclusion & exclusion patterns change then a full build is done
	boolean isExcluded = (md.exclusionPatterns != null || md.inclusionPatterns != null)
		&& Util.isExcluded(resource, md.inclusionPatterns, md.exclusionPatterns);
	switch(resource.getType()) {
		case IResource.FOLDER :
			if (isExcluded && md.inclusionPatterns == null)
		        return true; // no need to go further with this delta since its children cannot be included

			IResourceDelta[] children = binaryDelta.getAffectedChildren();
			for (int i = 0, l = children.length; i < l; i++)
				if (!checkForClassFileChanges(children[i], md, segmentCount))
					return false;
			return true;
		case IResource.FILE :
			if (!isExcluded && org.eclipse.jdt.internal.compiler.util.Util.isClassFileName(resource.getName())) {
				// perform full build if a managed class file has been changed
				IPath typePath = resource.getFullPath().removeFirstSegments(segmentCount).removeFileExtension();
				if (newState.isKnownType(typePath.toString())) {
					if (JavaBuilder.DEBUG)
						System.out.println("MUST DO FULL BUILD. Found change to class file " + typePath); //$NON-NLS-1$
					return false;
				}
				return true;
			}
	}
	return true;
}

protected void cleanUp() {
	super.cleanUp();

	this.sourceFiles = null;
	this.previousSourceFiles = null;
	this.qualifiedStrings = null;
	this.simpleStrings = null;
	this.secondaryTypesToRemove = null;
	this.hasStructuralChanges = false;
	this.compileLoop = 0;
}

protected void deleteGeneratedFiles(IFile[] deletedGeneratedFiles) {
	// delete generated files and recompile any affected source files
	try {
		for (int j = deletedGeneratedFiles.length; --j >= 0;) {
			SourceFile sourceFile = findSourceFile(deletedGeneratedFiles[j]);
			if (sourceFile == null) continue;
			String typeLocator = sourceFile.typeLocator();
			int mdSegmentCount = sourceFile.sourceLocation.sourceFolder.getFullPath().segmentCount();
			IPath typePath = sourceFile.resource.getFullPath().removeFirstSegments(mdSegmentCount).removeFileExtension();
			char[][] definedTypeNames = newState.getDefinedTypeNamesFor(typeLocator);
			if (definedTypeNames == null) { // defined a single type matching typePath
				removeClassFile(typePath, sourceFile.sourceLocation.binaryFolder);
			} else {
				addDependentsOf(typePath, true); // add dependents of the source file since it may be involved in a name collision
				if (definedTypeNames.length > 0) { // skip it if it failed to successfully define a type
					IPath packagePath = typePath.removeLastSegments(1);
					for (int d = 0, l = definedTypeNames.length; d < l; d++)
						removeClassFile(packagePath.append(new String(definedTypeNames[d])), sourceFile.sourceLocation.binaryFolder);
				}
			}
			this.newState.removeLocator(typeLocator);
		}
	} catch (CoreException e) {
		// must continue with compile loop so just log the CoreException
		e.printStackTrace();
	}
}

protected boolean findAffectedSourceFiles(IResourceDelta delta, ClasspathLocation[] classFoldersAndJars, IProject prereqProject) {
	for (int i = 0, l = classFoldersAndJars.length; i < l; i++) {
		ClasspathLocation bLocation = classFoldersAndJars[i];
		// either a .class file folder or a zip/jar file
		if (bLocation != null) { // skip unchanged output folder
			IPath p = bLocation.getProjectRelativePath();
			if (p != null) {
				IResourceDelta binaryDelta = delta.findMember(p);
				if (binaryDelta != null) {
					if (bLocation instanceof ClasspathJar) {
						if (JavaBuilder.DEBUG)
							System.out.println("ABORTING incremental build... found delta to jar/zip file"); //$NON-NLS-1$
						return false; // do full build since jar file was changed (added/removed were caught as classpath change)
					}
					if (binaryDelta.getKind() == IResourceDelta.ADDED || binaryDelta.getKind() == IResourceDelta.REMOVED) {
						if (JavaBuilder.DEBUG)
							System.out.println("ABORTING incremental build... found added/removed binary folder"); //$NON-NLS-1$
						return false; // added/removed binary folder should not make it here (classpath change), but handle anyways
					}
					int segmentCount = binaryDelta.getFullPath().segmentCount();
					IResourceDelta[] children = binaryDelta.getAffectedChildren(); // .class files from class folder
					StringSet structurallyChangedTypes = null;
					if (bLocation.isOutputFolder())
						structurallyChangedTypes = this.newState.getStructurallyChangedTypes(javaBuilder.getLastState(prereqProject));
					for (int j = 0, m = children.length; j < m; j++)
						findAffectedSourceFiles(children[j], segmentCount, structurallyChangedTypes);
					notifier.checkCancel();
				}
			}
		}
	}
	return true;
}

protected void findAffectedSourceFiles(IResourceDelta binaryDelta, int segmentCount, StringSet structurallyChangedTypes) {
	// When a package becomes a type or vice versa, expect 2 deltas,
	// one on the folder & one on the class file
	IResource resource = binaryDelta.getResource();
	switch(resource.getType()) {
		case IResource.FOLDER :
			switch (binaryDelta.getKind()) {
				case IResourceDelta.ADDED :
				case IResourceDelta.REMOVED :
					IPath packagePath = resource.getFullPath().removeFirstSegments(segmentCount);
					String packageName = packagePath.toString();
					if (binaryDelta.getKind() == IResourceDelta.ADDED) {
						// see if any known source file is from the same package... classpath already includes new package
						if (!newState.isKnownPackage(packageName)) {
							if (JavaBuilder.DEBUG)
								System.out.println("Found added package " + packageName); //$NON-NLS-1$
							addDependentsOf(packagePath, false);
							return;
						}
						if (JavaBuilder.DEBUG)
							System.out.println("Skipped dependents of added package " + packageName); //$NON-NLS-1$
					} else {
						// see if the package still exists on the classpath
						if (!nameEnvironment.isPackage(packageName)) {
							if (JavaBuilder.DEBUG)
								System.out.println("Found removed package " + packageName); //$NON-NLS-1$
							addDependentsOf(packagePath, false);
							return;
						}
						if (JavaBuilder.DEBUG)
							System.out.println("Skipped dependents of removed package " + packageName); //$NON-NLS-1$
					}
					// fall thru & traverse the sub-packages and .class files
				case IResourceDelta.CHANGED :
					IResourceDelta[] children = binaryDelta.getAffectedChildren();
					for (int i = 0, l = children.length; i < l; i++)
						findAffectedSourceFiles(children[i], segmentCount, structurallyChangedTypes);
			}
			return;
		case IResource.FILE :
			if (org.eclipse.jdt.internal.compiler.util.Util.isClassFileName(resource.getName())) {
				IPath typePath = resource.getFullPath().removeFirstSegments(segmentCount).removeFileExtension();
				switch (binaryDelta.getKind()) {
					case IResourceDelta.ADDED :
					case IResourceDelta.REMOVED :
						if (JavaBuilder.DEBUG)
							System.out.println("Found added/removed class file " + typePath); //$NON-NLS-1$
						addDependentsOf(typePath, false);
						return;
					case IResourceDelta.CHANGED :
						if ((binaryDelta.getFlags() & IResourceDelta.CONTENT) == 0)
							return; // skip it since it really isn't changed
						if (structurallyChangedTypes != null && !structurallyChangedTypes.includes(typePath.toString()))
							return; // skip since it wasn't a structural change
						if (JavaBuilder.DEBUG)
							System.out.println("Found changed class file " + typePath); //$NON-NLS-1$
						addDependentsOf(typePath, false);
				}
				return;
			}
	}
}

protected boolean findSourceFiles(IResourceDelta delta) throws CoreException {
	ArrayList visited = this.makeOutputFolderConsistent ? new ArrayList(sourceLocations.length) : null;
	for (int i = 0, l = sourceLocations.length; i < l; i++) {
		ClasspathMultiDirectory md = sourceLocations[i];
		if (md.sourceFolder.equals(javaBuilder.currentProject)) {
			// skip nested source & output folders when the project is a source folder
			int segmentCount = delta.getFullPath().segmentCount();
			IResourceDelta[] children = delta.getAffectedChildren();
			for (int j = 0, m = children.length; j < m; j++)
				if (!isExcludedFromProject(children[j].getFullPath()))
					if (!findSourceFiles(children[j], md, segmentCount))
						return false;
		} else {
			if (this.makeOutputFolderConsistent && md.hasIndependentOutputFolder && !visited.contains(md.binaryFolder)) {
				visited.add(md.binaryFolder);
				IResourceDelta binaryDelta = delta.findMember(md.binaryFolder.getProjectRelativePath());
				if (binaryDelta != null) {
					int segmentCount = binaryDelta.getFullPath().segmentCount();
					IResourceDelta[] children = binaryDelta.getAffectedChildren();
					for (int j = 0, m = children.length; j < m; j++)
						if (!checkForClassFileChanges(children[j], md, segmentCount))
							return false;
				}
			}
			IResourceDelta sourceDelta = delta.findMember(md.sourceFolder.getProjectRelativePath());
			if (sourceDelta != null) {
				if (sourceDelta.getKind() == IResourceDelta.REMOVED) {
					if (JavaBuilder.DEBUG)
						System.out.println("ABORTING incremental build... found removed source folder"); //$NON-NLS-1$
					return false; // removed source folder should not make it here, but handle anyways (ADDED is supported)
				}
				int segmentCount = sourceDelta.getFullPath().segmentCount();
				IResourceDelta[] children = sourceDelta.getAffectedChildren();
				try {
					for (int j = 0, m = children.length; j < m; j++)
						if (!findSourceFiles(children[j], md, segmentCount))
							return false;
				} catch (CoreException e) {
					// catch the case that a package has been renamed and collides on disk with an as-yet-to-be-deleted package
					if (e.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
						if (JavaBuilder.DEBUG)
							System.out.println("ABORTING incremental build... found renamed package"); //$NON-NLS-1$
						return false;
					}
					throw e; // rethrow
				}
			}
		}
		notifier.checkCancel();
	}
	return true;
}

protected boolean findSourceFiles(IResourceDelta sourceDelta, ClasspathMultiDirectory md, int segmentCount) throws CoreException {
	// When a package becomes a type or vice versa, expect 2 deltas,
	// one on the folder & one on the source file
	IResource resource = sourceDelta.getResource();
	// remember that if inclusion & exclusion patterns change then a full build is done
	boolean isExcluded = (md.exclusionPatterns != null || md.inclusionPatterns != null)
		&& Util.isExcluded(resource, md.inclusionPatterns, md.exclusionPatterns);
	switch(resource.getType()) {
		case IResource.FOLDER :
			if (isExcluded && md.inclusionPatterns == null)
		        return true; // no need to go further with this delta since its children cannot be included

			switch (sourceDelta.getKind()) {
				case IResourceDelta.ADDED :
				    if (!isExcluded) {
						IPath addedPackagePath = resource.getFullPath().removeFirstSegments(segmentCount);
						createFolder(addedPackagePath, md.binaryFolder); // ensure package exists in the output folder
						// add dependents even when the package thinks it exists to be on the safe side
						if (JavaBuilder.DEBUG)
							System.out.println("Found added package " + addedPackagePath); //$NON-NLS-1$
						addDependentsOf(addedPackagePath, true);
				    }
					// fall thru & collect all the source files
				case IResourceDelta.CHANGED :
					IResourceDelta[] children = sourceDelta.getAffectedChildren();
					for (int i = 0, l = children.length; i < l; i++)
						if (!findSourceFiles(children[i], md, segmentCount))
							return false;
					return true;
				case IResourceDelta.REMOVED :
				    if (isExcluded) {
				    	// since this folder is excluded then there is nothing to delete (from this md), but must walk any included subfolders
						children = sourceDelta.getAffectedChildren();
						for (int i = 0, l = children.length; i < l; i++)
							if (!findSourceFiles(children[i], md, segmentCount))
								return false;
						return true;
				    }
					IPath removedPackagePath = resource.getFullPath().removeFirstSegments(segmentCount);
					if (sourceLocations.length > 1) {
						for (int i = 0, l = sourceLocations.length; i < l; i++) {
							if (sourceLocations[i].sourceFolder.getFolder(removedPackagePath).exists()) {
								// only a package fragment was removed, same as removing multiple source files
								createFolder(removedPackagePath, md.binaryFolder); // ensure package exists in the output folder
								IResourceDelta[] removedChildren = sourceDelta.getAffectedChildren();
								for (int j = 0, m = removedChildren.length; j < m; j++)
									if (!findSourceFiles(removedChildren[j], md, segmentCount))
										return false;
								return true;
							}
						}
					}
					IFolder removedPackageFolder = md.binaryFolder.getFolder(removedPackagePath);
					if (removedPackageFolder.exists())
						removedPackageFolder.delete(IResource.FORCE, null);
					// add dependents even when the package thinks it does not exist to be on the safe side
					if (JavaBuilder.DEBUG)
						System.out.println("Found removed package " + removedPackagePath); //$NON-NLS-1$
					addDependentsOf(removedPackagePath, true);
					newState.removePackage(sourceDelta);
			}
			return true;
		case IResource.FILE :
			if (isExcluded) return true;

			String resourceName = resource.getName();
			if (org.eclipse.jdt.internal.core.util.Util.isJavaLikeFileName(resourceName)) {
				IPath typePath = resource.getFullPath().removeFirstSegments(segmentCount).removeFileExtension();
				String typeLocator = resource.getProjectRelativePath().toString();
				switch (sourceDelta.getKind()) {
					case IResourceDelta.ADDED :
						if (JavaBuilder.DEBUG)
							System.out.println("Compile this added source file " + typeLocator); //$NON-NLS-1$
						sourceFiles.add(new SourceFile((IFile) resource, md, true));
						String typeName = typePath.toString();
						if (!newState.isDuplicateLocator(typeName, typeLocator)) { // adding dependents results in 2 duplicate errors
							if (JavaBuilder.DEBUG)
								System.out.println("Found added source file " + typeName); //$NON-NLS-1$
							addDependentsOf(typePath, true);
						}
						return true;
					case IResourceDelta.REMOVED :
						char[][] definedTypeNames = newState.getDefinedTypeNamesFor(typeLocator);
						if (definedTypeNames == null) { // defined a single type matching typePath
							removeClassFile(typePath, md.binaryFolder);
							if ((sourceDelta.getFlags() & IResourceDelta.MOVED_TO) != 0) {
								// remove problems and tasks for a compilation unit that is being moved (to another package or renamed)
								// if the target file is a compilation unit, the new cu will be recompiled
								// if the target file is a non-java resource, then markers are removed
								// see bug 2857
								IResource movedFile = javaBuilder.workspaceRoot.getFile(sourceDelta.getMovedToPath());
								JavaBuilder.removeProblemsAndTasksFor(movedFile); 
							}
						} else {
							if (JavaBuilder.DEBUG)
								System.out.println("Found removed source file " + typePath.toString()); //$NON-NLS-1$
							addDependentsOf(typePath, true); // add dependents of the source file since it may be involved in a name collision
							if (definedTypeNames.length > 0) { // skip it if it failed to successfully define a type
								IPath packagePath = typePath.removeLastSegments(1);
								for (int i = 0, l = definedTypeNames.length; i < l; i++)
									removeClassFile(packagePath.append(new String(definedTypeNames[i])), md.binaryFolder);
							}
						}
						newState.removeLocator(typeLocator);
						return true;
					case IResourceDelta.CHANGED :
						if ((sourceDelta.getFlags() & IResourceDelta.CONTENT) == 0
								&& (sourceDelta.getFlags() & IResourceDelta.ENCODING) == 0)
							return true; // skip it since it really isn't changed
						if (JavaBuilder.DEBUG)
							System.out.println("Compile this changed source file " + typeLocator); //$NON-NLS-1$
						sourceFiles.add(new SourceFile((IFile) resource, md, true));
				}
				return true;
			} else if (org.eclipse.jdt.internal.compiler.util.Util.isClassFileName(resourceName)) {
				// perform full build if a managed class file has been changed
				if (this.makeOutputFolderConsistent) {
					IPath typePath = resource.getFullPath().removeFirstSegments(segmentCount).removeFileExtension();
					if (newState.isKnownType(typePath.toString())) {
						if (JavaBuilder.DEBUG)
							System.out.println("MOST DO FULL BUILD. Found change to class file " + typePath); //$NON-NLS-1$
						return false;
					}
				}
				return true;
			} else if (md.hasIndependentOutputFolder) {
				if (javaBuilder.filterExtraResource(resource)) return true;

				// copy all other resource deltas to the output folder
				IPath resourcePath = resource.getFullPath().removeFirstSegments(segmentCount);
				IResource outputFile = md.binaryFolder.getFile(resourcePath);
				switch (sourceDelta.getKind()) {
					case IResourceDelta.ADDED :
						if (outputFile.exists()) {
							if (JavaBuilder.DEBUG)
								System.out.println("Deleting existing file " + resourcePath); //$NON-NLS-1$
							outputFile.delete(IResource.FORCE, null);
						}
						if (JavaBuilder.DEBUG)
							System.out.println("Copying added file " + resourcePath); //$NON-NLS-1$
						createFolder(resourcePath.removeLastSegments(1), md.binaryFolder); // ensure package exists in the output folder
						resource.copy(outputFile.getFullPath(), IResource.FORCE | IResource.DERIVED, null);
						Util.setReadOnly(outputFile, false); // just in case the original was read only
						return true;
					case IResourceDelta.REMOVED :
						if (outputFile.exists()) {
							if (JavaBuilder.DEBUG)
								System.out.println("Deleting removed file " + resourcePath); //$NON-NLS-1$
							outputFile.delete(IResource.FORCE, null);
						}
						return true;
					case IResourceDelta.CHANGED :
						if ((sourceDelta.getFlags() & IResourceDelta.CONTENT) == 0
								&& (sourceDelta.getFlags() & IResourceDelta.ENCODING) == 0)
							return true; // skip it since it really isn't changed
						if (outputFile.exists()) {
							if (JavaBuilder.DEBUG)
								System.out.println("Deleting existing file " + resourcePath); //$NON-NLS-1$
							outputFile.delete(IResource.FORCE, null);
						}
						if (JavaBuilder.DEBUG)
							System.out.println("Copying changed file " + resourcePath); //$NON-NLS-1$
						createFolder(resourcePath.removeLastSegments(1), md.binaryFolder); // ensure package exists in the output folder
						resource.copy(outputFile.getFullPath(), IResource.FORCE | IResource.DERIVED, null);
						Util.setReadOnly(outputFile, false); // just in case the original was read only
				}
				return true;
			}
	}
	return true;
}

protected void finishedWith(String sourceLocator, CompilationResult result, char[] mainTypeName, ArrayList definedTypeNames, ArrayList duplicateTypeNames) {
	char[][] previousTypeNames = newState.getDefinedTypeNamesFor(sourceLocator);
	if (previousTypeNames == null)
		previousTypeNames = new char[][] {mainTypeName};
	IPath packagePath = null;
	next : for (int i = 0, l = previousTypeNames.length; i < l; i++) {
		char[] previous = previousTypeNames[i];
		for (int j = 0, m = definedTypeNames.size(); j < m; j++)
			if (CharOperation.equals(previous, (char[]) definedTypeNames.get(j)))
				continue next;

		SourceFile sourceFile = (SourceFile) result.getCompilationUnit();
		if (packagePath == null) {
			int count = sourceFile.sourceLocation.sourceFolder.getFullPath().segmentCount();
			packagePath = sourceFile.resource.getFullPath().removeFirstSegments(count).removeLastSegments(1);
		}
		if (secondaryTypesToRemove == null)
			this.secondaryTypesToRemove = new SimpleLookupTable();
		ArrayList types = (ArrayList) secondaryTypesToRemove.get(sourceFile.sourceLocation.binaryFolder);
		if (types == null)
			types = new ArrayList(definedTypeNames.size());
		types.add(packagePath.append(new String(previous)));
		secondaryTypesToRemove.put(sourceFile.sourceLocation.binaryFolder, types);
	}
	super.finishedWith(sourceLocator, result, mainTypeName, definedTypeNames, duplicateTypeNames);
}

protected void processAnnotationResults(CompilationParticipantResult[] results) {
	for (int i = results.length; --i >= 0;) {
		CompilationParticipantResult result = results[i];
		if (result == null) continue;

		IFile[] deletedGeneratedFiles = result.deletedFiles;
		if (deletedGeneratedFiles != null)
			deleteGeneratedFiles(deletedGeneratedFiles);

		IFile[] addedGeneratedFiles = result.addedFiles;
		if (addedGeneratedFiles != null) {
			for (int j = addedGeneratedFiles.length; --j >= 0;) {
				SourceFile sourceFile = findSourceFile(addedGeneratedFiles[j]);
				if (sourceFile != null && !sourceFiles.contains(sourceFile))
					this.sourceFiles.add(sourceFile);
			}
		}

		recordParticipantResult(result);
	}
}

protected void removeClassFile(IPath typePath, IContainer outputFolder) throws CoreException {
	if (typePath.lastSegment().indexOf('$') == -1) { // is not a nested type
		newState.removeQualifiedTypeName(typePath.toString());
		// add dependents even when the type thinks it does not exist to be on the safe side
		if (JavaBuilder.DEBUG)
			System.out.println("Found removed type " + typePath); //$NON-NLS-1$
		addDependentsOf(typePath, true); // when member types are removed, their enclosing type is structurally changed
	}
	IFile classFile = outputFolder.getFile(typePath.addFileExtension(SuffixConstants.EXTENSION_class));
	if (classFile.exists()) {
		if (JavaBuilder.DEBUG)
			System.out.println("Deleting class file of removed type " + typePath); //$NON-NLS-1$
		classFile.delete(IResource.FORCE, null);
	}
}

protected void removeSecondaryTypes() throws CoreException {
	if (secondaryTypesToRemove != null) { // delayed deleting secondary types until the end of the compile loop
		Object[] keyTable = secondaryTypesToRemove.keyTable;
		Object[] valueTable = secondaryTypesToRemove.valueTable;
		for (int i = 0, l = keyTable.length; i < l; i++) {
			IContainer outputFolder = (IContainer) keyTable[i];
			if (outputFolder != null) {
				ArrayList paths = (ArrayList) valueTable[i];
				for (int j = 0, m = paths.size(); j < m; j++)
					removeClassFile((IPath) paths.get(j), outputFolder);
			}
		}
		this.secondaryTypesToRemove = null;
		if (previousSourceFiles != null && previousSourceFiles.size() > 1)
			this.previousSourceFiles = null; // cannot optimize recompile case when a secondary type is deleted
	}
}

protected void resetCollections() {
	if (this.sourceFiles == null) {
		this.sourceFiles = new ArrayList(33);
		this.previousSourceFiles = null;
		this.qualifiedStrings = new StringSet(3);
		this.simpleStrings = new StringSet(3);
		this.hasStructuralChanges = false;
		this.compileLoop = 0;
	} else {
		this.previousSourceFiles = this.sourceFiles.isEmpty() ? null : (ArrayList) this.sourceFiles.clone();

		this.sourceFiles.clear();
		this.qualifiedStrings.clear();
		this.simpleStrings.clear();
		this.workQueue.clear();
	}
}

protected void updateProblemsFor(SourceFile sourceFile, CompilationResult result) throws CoreException {
	IMarker[] markers = JavaBuilder.getProblemsFor(sourceFile.resource);
	CategorizedProblem[] problems = result.getProblems();
	if (problems == null && markers.length == 0) return;

	notifier.updateProblemCounts(markers, problems);
	JavaBuilder.removeProblemsFor(sourceFile.resource);
	storeProblemsFor(sourceFile, problems);
}

protected void updateTasksFor(SourceFile sourceFile, CompilationResult result) throws CoreException {
	IMarker[] markers = JavaBuilder.getTasksFor(sourceFile.resource);
	CategorizedProblem[] tasks = result.getTasks();
	if (tasks == null && markers.length == 0) return;

	JavaBuilder.removeTasksFor(sourceFile.resource);
	storeTasksFor(sourceFile, tasks);
}

protected void writeClassFileBytes(byte[] bytes, IFile file, String qualifiedFileName, boolean isTopLevelType, boolean updateClassFile) throws CoreException {
	// Before writing out the class file, compare it to the previous file
	// If structural changes occured then add dependent source files
	if (file.exists()) {
		if (writeClassFileCheck(file, qualifiedFileName, bytes) || updateClassFile) { // see 46093
			if (JavaBuilder.DEBUG)
				System.out.println("Writing changed class file " + file.getName());//$NON-NLS-1$
			if (!file.isDerived())
				file.setDerived(true);
			file.setContents(new ByteArrayInputStream(bytes), true, false, null);
		} else if (JavaBuilder.DEBUG) {
			System.out.println("Skipped over unchanged class file " + file.getName());//$NON-NLS-1$
		}
	} else {
		if (isTopLevelType)
			addDependentsOf(new Path(qualifiedFileName), true); // new type
		if (JavaBuilder.DEBUG)
			System.out.println("Writing new class file " + file.getName());//$NON-NLS-1$
		try {
			file.create(new ByteArrayInputStream(bytes), IResource.FORCE | IResource.DERIVED, null);
		} catch (CoreException e) {
			if (e.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS)
				// catch the case that a nested type has been renamed and collides on disk with an as-yet-to-be-deleted type
				throw new AbortCompilation(true, new AbortIncrementalBuildException(qualifiedFileName));
			throw e; // rethrow
		}
	}
}

protected boolean writeClassFileCheck(IFile file, String fileName, byte[] newBytes) throws CoreException {
	try {
		byte[] oldBytes = Util.getResourceContentsAsByteArray(file);
		notEqual : if (newBytes.length == oldBytes.length) {
			for (int i = newBytes.length; --i >= 0;)
				if (newBytes[i] != oldBytes[i]) break notEqual;
			return false; // bytes are identical so skip them
		}
		URI location = file.getLocationURI();
		if (location == null) return false; // unable to determine location of this class file
		String filePath = location.getSchemeSpecificPart();
		ClassFileReader reader = new ClassFileReader(oldBytes, filePath.toCharArray());
		// ignore local types since they're only visible inside a single method
		if (!(reader.isLocal() || reader.isAnonymous()) && reader.hasStructuralChanges(newBytes)) {
			if (JavaBuilder.DEBUG)
				System.out.println("Type has structural changes " + fileName); //$NON-NLS-1$
			addDependentsOf(new Path(fileName), true);
			this.newState.wasStructurallyChanged(fileName);
		}
	} catch (ClassFormatException e) {
		addDependentsOf(new Path(fileName), true);
		this.newState.wasStructurallyChanged(fileName);
	}
	return true;
}

public String toString() {
	return "incremental image builder for:\n\tnew state: " + newState; //$NON-NLS-1$
}


/* Debug helper

static void dump(IResourceDelta delta) {
	StringBuffer buffer = new StringBuffer();
	IPath path = delta.getFullPath();
	for (int i = path.segmentCount(); --i > 0;)
		buffer.append("  ");
	switch (delta.getKind()) {
		case IResourceDelta.ADDED:
			buffer.append('+');
			break;
		case IResourceDelta.REMOVED:
			buffer.append('-');
			break;
		case IResourceDelta.CHANGED:
			buffer.append('*');
			break;
		case IResourceDelta.NO_CHANGE:
			buffer.append('=');
			break;
		default:
			buffer.append('?');
			break;
	}
	buffer.append(path);
	System.out.println(buffer.toString());
	IResourceDelta[] children = delta.getAffectedChildren();
	for (int i = 0, l = children.length; i < l; i++)
		dump(children[i]);
}
*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3448.java