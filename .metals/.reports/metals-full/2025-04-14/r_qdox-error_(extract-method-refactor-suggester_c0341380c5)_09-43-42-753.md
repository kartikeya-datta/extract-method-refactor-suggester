error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5412.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5412.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5412.java
text:
```scala
I@@Resource underlyingResource = getResource();

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
import java.util.Enumeration;
import java.util.Map;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.core.util.MementoTokenizer;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * @see IPackageFragmentRoot
 */
public class PackageFragmentRoot extends Openable implements IPackageFragmentRoot {

	/**
	 * The delimiter between the source path and root path in the
	 * attachment server property.
	 */
	protected final static char ATTACHMENT_PROPERTY_DELIMITER= '*';
	/*
	 * No source attachment property
	 */
	public final static String NO_SOURCE_ATTACHMENT = ""; //$NON-NLS-1$

	/**
	 * The resource associated with this root (null for external jar)
	 */
	protected IResource resource;

/**
 * Constructs a package fragment root which is the root of the java package
 * directory hierarchy.
 */
protected PackageFragmentRoot(IResource resource, JavaProject project) {
	super(project);
	this.resource = resource;
}

/**
 * @see IPackageFragmentRoot
 */
public void attachSource(IPath sourcePath, IPath rootPath, IProgressMonitor monitor) throws JavaModelException {
	try {
		verifyAttachSource(sourcePath);
		if (monitor != null) {
			monitor.beginTask(Messages.element_attachingSource, 2);
		}
		SourceMapper oldMapper= getSourceMapper();
		boolean rootNeedsToBeClosed= false;

		if (sourcePath == null) {
			//source being detached
			rootNeedsToBeClosed= true;
			setSourceMapper(null);
		/* Disable deltas (see 1GDTUSD)
			// fire a delta to notify the UI about the source detachement.
			JavaModelManager manager = (JavaModelManager) JavaModelManager.getJavaModelManager();
			JavaModel model = (JavaModel) getJavaModel();
			JavaElementDelta attachedSourceDelta = new JavaElementDelta(model);
			attachedSourceDelta .sourceDetached(this); // this would be a PackageFragmentRoot
			manager.registerResourceDelta(attachedSourceDelta );
			manager.fire(); // maybe you want to fire the change later. Let us know about it.
		*/
		} else {
		/*
			// fire a delta to notify the UI about the source attachment.
			JavaModelManager manager = (JavaModelManager) JavaModelManager.getJavaModelManager();
			JavaModel model = (JavaModel) getJavaModel();
			JavaElementDelta attachedSourceDelta = new JavaElementDelta(model);
			attachedSourceDelta .sourceAttached(this); // this would be a PackageFragmentRoot
			manager.registerResourceDelta(attachedSourceDelta );
			manager.fire(); // maybe you want to fire the change later. Let us know about it.
		 */

			//check if different from the current attachment
			IPath storedSourcePath= getSourceAttachmentPath();
			IPath storedRootPath= getSourceAttachmentRootPath();
			if (monitor != null) {
				monitor.worked(1);
			}
			if (storedSourcePath != null) {
				if (!(storedSourcePath.equals(sourcePath) && (rootPath != null && rootPath.equals(storedRootPath)) || storedRootPath == null)) {
					rootNeedsToBeClosed= true;
				}
			}
			// check if source path is valid
			Object target = JavaModel.getTarget(sourcePath, false);
			if (target == null) {
				throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.INVALID_PATH, sourcePath));
			}
			SourceMapper mapper = createSourceMapper(sourcePath, rootPath);
			if (rootPath == null && mapper.rootPath != null) {
				// as a side effect of calling the SourceMapper constructor, the root path was computed
				rootPath = new Path(mapper.rootPath);
			}
			setSourceMapper(mapper);
		}
		if (sourcePath == null) {
			Util.setSourceAttachmentProperty(getPath(), null); //remove the property
		} else {
			//set the property to the path of the mapped source
			Util.setSourceAttachmentProperty(
				getPath(),
				sourcePath.toString()
				+ (rootPath == null ? "" : (ATTACHMENT_PROPERTY_DELIMITER + rootPath.toString()))); //$NON-NLS-1$
		}
		if (rootNeedsToBeClosed) {
			if (oldMapper != null) {
				oldMapper.close();
			}
			BufferManager manager= BufferManager.getDefaultBufferManager();
			Enumeration openBuffers= manager.getOpenBuffers();
			while (openBuffers.hasMoreElements()) {
				IBuffer buffer= (IBuffer) openBuffers.nextElement();
				IOpenable possibleMember= buffer.getOwner();
				if (isAncestorOf((IJavaElement) possibleMember)) {
					buffer.close();
				}
			}
			if (monitor != null) {
				monitor.worked(1);
			}
		}
	} catch (JavaModelException e) {
		Util.setSourceAttachmentProperty(getPath(), null); // loose info - will be recomputed
		throw e;
	} finally {
		if (monitor != null) {
			monitor.done();
		}
	}
}

/**
 * @see Openable
 */
protected boolean buildStructure(OpenableElementInfo info, IProgressMonitor pm, Map newElements, IResource underlyingResource) throws JavaModelException {
	((PackageFragmentRootInfo) info).setRootKind(determineKind(underlyingResource));
	return computeChildren(info, underlyingResource);
}

SourceMapper createSourceMapper(IPath sourcePath, IPath rootPath) {
	SourceMapper mapper = new SourceMapper(
		sourcePath,
		rootPath == null ? null : rootPath.toOSString(),
		getJavaProject().getOptions(true)); // cannot use workspace options if external jar is 1.5 jar and workspace options are 1.4 options
	return mapper;
}
/*
 * @see org.eclipse.jdt.core.IPackageFragmentRoot#delete
 */
public void delete(
	int updateResourceFlags,
	int updateModelFlags,
	IProgressMonitor monitor)
	throws JavaModelException {

	DeletePackageFragmentRootOperation op = new DeletePackageFragmentRootOperation(this, updateResourceFlags, updateModelFlags);
	op.runOperation(monitor);
}

/**
 * Compute the package fragment children of this package fragment root.
 *
 * @exception JavaModelException  The resource associated with this package fragment root does not exist
 */
protected boolean computeChildren(OpenableElementInfo info, IResource underlyingResource) throws JavaModelException {
	// Note the children are not opened (so not added to newElements) for a regular package fragment root
	// However they are opened for a Jar package fragment root (see JarPackageFragmentRoot#computeChildren)
	try {
		// the underlying resource may be a folder or a project (in the case that the project folder
		// is actually the package fragment root)
		if (underlyingResource.getType() == IResource.FOLDER || underlyingResource.getType() == IResource.PROJECT) {
			ArrayList vChildren = new ArrayList(5);
			IContainer rootFolder = (IContainer) underlyingResource;
			char[][] inclusionPatterns = fullInclusionPatternChars();
			char[][] exclusionPatterns = fullExclusionPatternChars();
			computeFolderChildren(rootFolder, !Util.isExcluded(rootFolder, inclusionPatterns, exclusionPatterns), CharOperation.NO_STRINGS, vChildren, inclusionPatterns, exclusionPatterns);
			IJavaElement[] children = new IJavaElement[vChildren.size()];
			vChildren.toArray(children);
			info.setChildren(children);
		}
	} catch (JavaModelException e) {
		//problem resolving children; structure remains unknown
		info.setChildren(new IJavaElement[]{});
		throw e;
	}
	return true;
}

/**
 * Starting at this folder, create package fragments and add the fragments that are not exclused
 * to the collection of children.
 *
 * @exception JavaModelException  The resource associated with this package fragment does not exist
 */
protected void computeFolderChildren(IContainer folder, boolean isIncluded, String[] pkgName, ArrayList vChildren, char[][] inclusionPatterns, char[][] exclusionPatterns) throws JavaModelException {

	if (isIncluded) {
	    IPackageFragment pkg = getPackageFragment(pkgName);
		vChildren.add(pkg);
	}
	try {
		JavaProject javaProject = (JavaProject)getJavaProject();
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		IResource[] members = folder.members();
		boolean hasIncluded = isIncluded;
		int length = members.length;
		if (length >0) {
			String sourceLevel = javaProject.getOption(JavaCore.COMPILER_SOURCE, true);
			String complianceLevel = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
			for (int i = 0; i < length; i++) {
				IResource member = members[i];
				String memberName = member.getName();

				switch(member.getType()) {

			    	case IResource.FOLDER:
			    		// recurse into sub folders even even parent not included as a sub folder could be included
			    		// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=65637)
			    		if (Util.isValidFolderNameForPackage(memberName, sourceLevel, complianceLevel)) {
			    			// eliminate binary output only if nested inside direct subfolders
			    			if (javaProject.contains(member)) {
			    				String[] newNames = Util.arrayConcat(pkgName, manager.intern(memberName));
			    				boolean isMemberIncluded = !Util.isExcluded(member, inclusionPatterns, exclusionPatterns);
			    				computeFolderChildren((IFolder) member, isMemberIncluded, newNames, vChildren, inclusionPatterns, exclusionPatterns);
			    			}
			    		}
			    		break;
			    	case IResource.FILE:
			    		// inclusion filter may only include files, in which case we still want to include the immediate parent package (lazily)
			    		if (!hasIncluded
			    				&& Util.isValidCompilationUnitName(memberName, sourceLevel, complianceLevel)
								&& !Util.isExcluded(member, inclusionPatterns, exclusionPatterns)) {
			    			hasIncluded = true;
			    			IPackageFragment pkg = getPackageFragment(pkgName);
			    			vChildren.add(pkg);
			    		}
			    		break;
				}
			}
		}
	} catch(IllegalArgumentException e){
		throw new JavaModelException(e, IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST); // could be thrown by ElementTree when path is not found
	} catch (CoreException e) {
		throw new JavaModelException(e);
	}
}

/*
 * @see org.eclipse.jdt.core.IPackageFragmentRoot#copy
 */
public void copy(
	IPath destination,
	int updateResourceFlags,
	int updateModelFlags,
	IClasspathEntry sibling,
	IProgressMonitor monitor)
	throws JavaModelException {

	CopyPackageFragmentRootOperation op =
		new CopyPackageFragmentRootOperation(this, destination, updateResourceFlags, updateModelFlags, sibling);
	op.runOperation(monitor);
}

/**
 * Returns a new element info for this element.
 */
protected Object createElementInfo() {
	return new PackageFragmentRootInfo();
}

/**
 * @see IPackageFragmentRoot
 */
public IPackageFragment createPackageFragment(String pkgName, boolean force, IProgressMonitor monitor) throws JavaModelException {
	CreatePackageFragmentOperation op = new CreatePackageFragmentOperation(this, pkgName, force);
	op.runOperation(monitor);
	return getPackageFragment(op.pkgName);
}

/**
 * Returns the root's kind - K_SOURCE or K_BINARY, defaults
 * to K_SOURCE if it is not on the classpath.
 *
 * @exception JavaModelException if the project and root do
 * 		not exist.
 */
protected int determineKind(IResource underlyingResource) throws JavaModelException {
	IClasspathEntry entry = ((JavaProject)getJavaProject()).getClasspathEntryFor(underlyingResource.getFullPath());
	if (entry != null) {
		return entry.getContentKind();
	}
	return IPackageFragmentRoot.K_SOURCE;
}

/**
 * Compares two objects for equality;
 * for <code>PackageFragmentRoot</code>s, equality is having the
 * same parent, same resources, and occurrence count.
 *
 */
public boolean equals(Object o) {
	if (this == o)
		return true;
	if (!(o instanceof PackageFragmentRoot))
		return false;
	PackageFragmentRoot other = (PackageFragmentRoot) o;
	return resource().equals(other.resource()) &&
			this.parent.equals(other.parent);
}

private IClasspathEntry findSourceAttachmentRecommendation() {
	try {
		IPath rootPath = getPath();
		IClasspathEntry entry;

		// try on enclosing project first
		JavaProject parentProject = (JavaProject) getJavaProject();
		try {
			entry = parentProject.getClasspathEntryFor(rootPath);
			if (entry != null) {
				Object target = JavaModel.getTarget(entry.getSourceAttachmentPath(), true);
				if (target != null) {
					return entry;
				}
			}
		} catch(JavaModelException e){
			// ignore
		}

		// iterate over all projects
		IJavaModel model = getJavaModel();
		IJavaProject[] jProjects = model.getJavaProjects();
		for (int i = 0, max = jProjects.length; i < max; i++){
			JavaProject jProject = (JavaProject) jProjects[i];
			if (jProject == parentProject) continue; // already done
			try {
				entry = jProject.getClasspathEntryFor(rootPath);
				if (entry != null){
					Object target = JavaModel.getTarget(entry.getSourceAttachmentPath(), true);
					if (target != null) {
						return entry;
					}
				}
			} catch(JavaModelException e){
				// ignore
			}
		}
	} catch(JavaModelException e){
		// ignore
	}

	return null;
}

/*
 * Returns the exclusion patterns from the classpath entry associated with this root.
 */
public char[][] fullExclusionPatternChars() {
	try {
		if (isOpen() && getKind() != IPackageFragmentRoot.K_SOURCE) return null;
		ClasspathEntry entry = (ClasspathEntry) getRawClasspathEntry();
		if (entry == null) {
			return null;
		} else {
			return entry.fullExclusionPatternChars();
		}
	} catch (JavaModelException e) {
		return null;
	}
}

/*
 * Returns the inclusion patterns from the classpath entry associated with this root.
 */
public char[][] fullInclusionPatternChars() {
	try {
		if (isOpen() && getKind() != IPackageFragmentRoot.K_SOURCE) return null;
		ClasspathEntry entry = (ClasspathEntry)getRawClasspathEntry();
		if (entry == null) {
			return null;
		} else {
			return entry.fullInclusionPatternChars();
		}
	} catch (JavaModelException e) {
		return null;
	}
}
public String getElementName() {
	IResource res = resource();
	if (res instanceof IFolder)
		return ((IFolder) res).getName();
	return ""; //$NON-NLS-1$
}
/**
 * @see IJavaElement
 */
public int getElementType() {
	return PACKAGE_FRAGMENT_ROOT;
}
/**
 * @see JavaElement#getHandleMemento()
 */
protected char getHandleMementoDelimiter() {
	return JavaElement.JEM_PACKAGEFRAGMENTROOT;
}
/*
 * @see JavaElement
 */
public IJavaElement getHandleFromMemento(String token, MementoTokenizer memento, WorkingCopyOwner owner) {
	switch (token.charAt(0)) {
		case JEM_PACKAGEFRAGMENT:
			String[] pkgName;
			if (memento.hasMoreTokens()) {
				token = memento.nextToken();
				char firstChar = token.charAt(0);
				if (firstChar == JEM_CLASSFILE || firstChar == JEM_COMPILATIONUNIT || firstChar == JEM_COUNT) {
					pkgName = CharOperation.NO_STRINGS;
				} else {
					pkgName = Util.splitOn('.', token, 0, token.length());
					token = null;
				}
			} else {
				pkgName = CharOperation.NO_STRINGS;
				token = null;
			}
			JavaElement pkg = getPackageFragment(pkgName);
			if (token == null) {
				return pkg.getHandleFromMemento(memento, owner);
			} else {
				return pkg.getHandleFromMemento(token, memento, owner);
			}
	}
	return null;
}
/**
 * @see JavaElement#getHandleMemento(StringBuffer)
 */
protected void getHandleMemento(StringBuffer buff) {
	IPath path;
	IResource underlyingResource = resource();
	if (underlyingResource != null) {
		// internal jar or regular root
		if (resource().getProject().equals(getJavaProject().getProject())) {
			path = underlyingResource.getProjectRelativePath();
		} else {
			path = underlyingResource.getFullPath();
		}
	} else {
		// external jar
		path = getPath();
	}
	((JavaElement)getParent()).getHandleMemento(buff);
	buff.append(getHandleMementoDelimiter());
	escapeMementoName(buff, path.toString());
}
/**
 * @see IPackageFragmentRoot
 */
public int getKind() throws JavaModelException {
	return ((PackageFragmentRootInfo)getElementInfo()).getRootKind();
}

/*
 * A version of getKind() that doesn't update the timestamp of the info in the Java model cache
 * to speed things up
 */
int internalKind() throws JavaModelException {
	JavaModelManager manager = JavaModelManager.getJavaModelManager();
	PackageFragmentRootInfo info = (PackageFragmentRootInfo) manager.peekAtInfo(this);
	if (info == null) {
		info = (PackageFragmentRootInfo) openWhenClosed(createElementInfo(), null);
	}
	return info.getRootKind();
}

/**
 * Returns an array of non-java resources contained in the receiver.
 */
public Object[] getNonJavaResources() throws JavaModelException {
	return ((PackageFragmentRootInfo) getElementInfo()).getNonJavaResources(getJavaProject(), resource(), this);
}

/**
 * @see IPackageFragmentRoot
 */
public IPackageFragment getPackageFragment(String packageName) {
	// tolerate package names with spaces (e.g. 'x . y') (http://bugs.eclipse.org/bugs/show_bug.cgi?id=21957)
	String[] pkgName = Util.getTrimmedSimpleNames(packageName);
	return getPackageFragment(pkgName);
}
public PackageFragment getPackageFragment(String[] pkgName) {
	return new PackageFragment(this, pkgName);
}
/**
 * Returns the package name for the given folder
 * (which is a decendent of this root).
 */
protected String getPackageName(IFolder folder) {
	IPath myPath= getPath();
	IPath pkgPath= folder.getFullPath();
	int mySegmentCount= myPath.segmentCount();
	int pkgSegmentCount= pkgPath.segmentCount();
	StringBuffer pkgName = new StringBuffer(IPackageFragment.DEFAULT_PACKAGE_NAME);
	for (int i= mySegmentCount; i < pkgSegmentCount; i++) {
		if (i > mySegmentCount) {
			pkgName.append('.');
		}
		pkgName.append(pkgPath.segment(i));
	}
	return pkgName.toString();
}

/**
 * @see IJavaElement
 */
public IPath getPath() {
	return internalPath();
}

public IPath internalPath() {
	return resource().getFullPath();
}
/*
 * @see IPackageFragmentRoot
 */
public IClasspathEntry getRawClasspathEntry() throws JavaModelException {

	IClasspathEntry rawEntry = null;
	JavaProject project = (JavaProject)getJavaProject();
	project.getResolvedClasspath(); // force the reverse rawEntry cache to be populated
	Map rootPathToRawEntries = project.getPerProjectInfo().rootPathToRawEntries;
	if (rootPathToRawEntries != null) {
		rawEntry = (IClasspathEntry) rootPathToRawEntries.get(getPath());
	}
	if (rawEntry == null) {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_NOT_ON_CLASSPATH, this));
	}
	return rawEntry;
}


public IResource resource() {
	if (this.resource != null) // perf improvement to avoid message send in resource()
		return this.resource;
	return super.resource();
}
/*
 * @see IJavaElement
 */
public IResource resource(PackageFragmentRoot root) {
	return this.resource;
}

/**
 * @see IPackageFragmentRoot
 */
public IPath getSourceAttachmentPath() throws JavaModelException {
	if (getKind() != K_BINARY) return null;

	// 1) look source attachment property (set iff attachSource(...) was called
	IPath path = getPath();
	String serverPathString= Util.getSourceAttachmentProperty(path);
	if (serverPathString != null) {
		int index= serverPathString.lastIndexOf(ATTACHMENT_PROPERTY_DELIMITER);
		if (index < 0) {
			// no root path specified
			return new Path(serverPathString);
		} else {
			String serverSourcePathString= serverPathString.substring(0, index);
			return new Path(serverSourcePathString);
		}
	}

	// 2) look at classpath entry
	IClasspathEntry entry = ((JavaProject) getParent()).getClasspathEntryFor(path);
	IPath sourceAttachmentPath;
	if (entry != null && (sourceAttachmentPath = entry.getSourceAttachmentPath()) != null)
		return sourceAttachmentPath;

	// 3) look for a recommendation
	entry = findSourceAttachmentRecommendation();
	if (entry != null && (sourceAttachmentPath = entry.getSourceAttachmentPath()) != null) {
		return sourceAttachmentPath;
	}

	return null;
}

/**
 * For use by <code>AttachSourceOperation</code> only.
 * Sets the source mapper associated with this root.
 */
public void setSourceMapper(SourceMapper mapper) throws JavaModelException {
	((PackageFragmentRootInfo) getElementInfo()).setSourceMapper(mapper);
}



/**
 * @see IPackageFragmentRoot
 */
public IPath getSourceAttachmentRootPath() throws JavaModelException {
	if (getKind() != K_BINARY) return null;

	// 1) look source attachment property (set iff attachSource(...) was called
	IPath path = getPath();
	String serverPathString= Util.getSourceAttachmentProperty(path);
	if (serverPathString != null) {
		int index = serverPathString.lastIndexOf(ATTACHMENT_PROPERTY_DELIMITER);
		if (index == -1) return null;
		String serverRootPathString= IPackageFragmentRoot.DEFAULT_PACKAGEROOT_PATH;
		if (index != serverPathString.length() - 1) {
			serverRootPathString= serverPathString.substring(index + 1);
		}
		return new Path(serverRootPathString);
	}

	// 2) look at classpath entry
	IClasspathEntry entry = ((JavaProject) getParent()).getClasspathEntryFor(path);
	IPath sourceAttachmentRootPath;
	if (entry != null && (sourceAttachmentRootPath = entry.getSourceAttachmentRootPath()) != null)
		return sourceAttachmentRootPath;

	// 3) look for a recomendation
	entry = findSourceAttachmentRecommendation();
	if (entry != null && (sourceAttachmentRootPath = entry.getSourceAttachmentRootPath()) != null)
		return sourceAttachmentRootPath;

	return null;
}

/**
 * @see JavaElement
 */
public SourceMapper getSourceMapper() {
	SourceMapper mapper;
	try {
		PackageFragmentRootInfo rootInfo = (PackageFragmentRootInfo) getElementInfo();
		mapper = rootInfo.getSourceMapper();
		if (mapper == null) {
			// first call to this method
			IPath sourcePath= getSourceAttachmentPath();
			IPath rootPath= getSourceAttachmentRootPath();
			if (sourcePath == null)
				mapper = createSourceMapper(getPath(), rootPath); // attach root to itself
			else
				mapper = createSourceMapper(sourcePath, rootPath);
			rootInfo.setSourceMapper(mapper);
		}
	} catch (JavaModelException e) {
		// no source can be attached
		mapper = null;
	}
	return mapper;
}

/**
 * @see IJavaElement
 */
public IResource getUnderlyingResource() throws JavaModelException {
	if (!exists()) throw newNotPresentException();
	return resource();
}

/**
 * @see IParent
 */
public boolean hasChildren() throws JavaModelException {
	// a package fragment root always has the default package as a child
	return true;
}

public int hashCode() {
	return resource().hashCode();
}

/**
 * @see IPackageFragmentRoot
 */
public boolean isArchive() {
	return false;
}

/**
 * @see IPackageFragmentRoot
 */
public boolean isExternal() {
	return false;
}

/*
 * Validate whether this package fragment root is on the classpath of its project.
 */
protected IStatus validateOnClasspath() {

	IPath path = getPath();
	try {
		// check package fragment root on classpath of its project
		JavaProject project = (JavaProject) getJavaProject();
		IClasspathEntry entry = project.getClasspathEntryFor(path);
		if (entry != null) {
			return Status.OK_STATUS;
		}
	} catch(JavaModelException e){
		// could not read classpath, then assume it is outside
		return e.getJavaModelStatus();
	}
	return new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_NOT_ON_CLASSPATH, this);
}
/*
 * @see org.eclipse.jdt.core.IPackageFragmentRoot#move
 */
public void move(
	IPath destination,
	int updateResourceFlags,
	int updateModelFlags,
	IClasspathEntry sibling,
	IProgressMonitor monitor)
	throws JavaModelException {

	MovePackageFragmentRootOperation op =
		new MovePackageFragmentRootOperation(this, destination, updateResourceFlags, updateModelFlags, sibling);
	op.runOperation(monitor);
}

/**
 * @private Debugging purposes
 */
protected void toStringInfo(int tab, StringBuffer buffer, Object info, boolean showResolvedInfo) {
	buffer.append(tabString(tab));
	IPath path = getPath();
	if (isExternal()) {
		buffer.append(path.toOSString());
	} else if (getJavaProject().getElementName().equals(path.segment(0))) {
	    if (path.segmentCount() == 1) {
			buffer.append("<project root>"); //$NON-NLS-1$
	    } else {
			buffer.append(path.removeFirstSegments(1).makeRelative());
	    }
	} else {
		buffer.append(path);
	}
	if (info == null) {
		buffer.append(" (not open)"); //$NON-NLS-1$
	}
}

protected IStatus validateExistence(IResource underlyingResource) {
	// check whether this pkg fragment root can be opened
	IStatus status = validateOnClasspath();
	if (!status.isOK())
		return status;
	if (!resourceExists(underlyingResource))
		return newDoesNotExistStatus();
	return JavaModelStatus.VERIFIED_OK;
}

/**
 * Possible failures: <ul>
 *  <li>ELEMENT_NOT_PRESENT - the root supplied to the operation
 *      does not exist
 *  <li>INVALID_ELEMENT_TYPES - the root is not of kind K_BINARY
 *   <li>RELATIVE_PATH - the path supplied to this operation must be
 *      an absolute path
 *  </ul>
 */
protected void verifyAttachSource(IPath sourcePath) throws JavaModelException {
	if (!exists()) {
		throw newNotPresentException();
	} else if (getKind() != K_BINARY) {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, this));
	} else if (sourcePath != null && !sourcePath.isAbsolute()) {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.RELATIVE_PATH, sourcePath));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5412.java