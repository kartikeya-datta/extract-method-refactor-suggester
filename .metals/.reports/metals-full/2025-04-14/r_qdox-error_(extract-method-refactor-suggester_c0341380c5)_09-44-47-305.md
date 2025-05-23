error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7606.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7606.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7606.java
text:
```scala
p@@rojects = ((JavaModel)model).getOldJavaProjectsList();

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;

/**
 * This class is used by <code>JavaModelManager</code> to convert
 * <code>IResourceDelta</code>s into <code>IJavaElementDelta</code>s.
 * It also does some processing on the <code>JavaElement</code>s involved
 * (e.g. closing them or updating classpaths).
 */
public class DeltaProcessor {
	
	final static int IGNORE = 0;
	final static int SOURCE = 1;
	final static int BINARY = 2;
	
	/**
	 * The <code>JavaElementDelta</code> corresponding to the <code>IResourceDelta</code> being translated.
	 */
	protected JavaElementDelta fCurrentDelta;

	protected IndexManager indexManager = new IndexManager();
		
	/* A table from IPath (from a classpath entry) to IJavaProject */
	Map roots;
	
	/* A table from IPath (from a classpath entry) to HashSet of IJavaProject
	 * Used when an IPath corresponds to more than one root */
	Map otherRoots;
	
	/* The java element that was last created (see createElement(IResource). 
	 * This is used as a stack of java elements (using getParent() to pop it, and 
	 * using the various get*(...) to push it. */
	Openable currentElement;
	
	/*
	 * The type of the current event being processed (see ChangedElementEvent)
	 */
	int currentEventType;
	
	HashSet projectsToUpdate = new HashSet();

	static final IJavaElementDelta[] NO_DELTA = new IJavaElementDelta[0];

	public static boolean VERBOSE = false;

	/*
	 * Adds the dependents of the given project to the list of the projects
	 * to update.
	 */
	void addDependentsToProjectsToUpdate(IPath projectPath) {
		try {
			IJavaProject[] projects = JavaModelManager.getJavaModelManager().getJavaModel().getJavaProjects();
			for (int i = 0, length = projects.length; i < length; i++) {
				IJavaProject project = projects[i];
				IClasspathEntry[] classpath = project.getResolvedClasspath(true);
				for (int j = 0, length2 = classpath.length; j < length2; j++) {
					IClasspathEntry entry = classpath[j];
					if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT
							&& entry.getPath().equals(projectPath)) {
						this.projectsToUpdate.add(project);
					}
				}
			}
		} catch (JavaModelException e) {
		}
	}
	/*
	 * Adds the given project and its dependents to the list of the projects
	 * to update.
	 */
	void addToProjectsToUpdateWithDependents(IProject project) {
		this.projectsToUpdate.add(JavaCore.create(project));
		this.addDependentsToProjectsToUpdate(project.getFullPath());
	}
	
	/**
	 * Adds the given child handle to its parent's cache of children. 
	 */
	protected void addToParentInfo(Openable child) {

		Openable parent = (Openable) child.getParent();
		if (parent != null && parent.isOpen()) {
			try {
				JavaElementInfo info = parent.getElementInfo();
				info.addChild(child);
			} catch (JavaModelException e) {
				// do nothing - we already checked if open
			}
		}
	}



	/**
	 * Check whether the updated file is affecting some of the properties of a given project (like
	 * its classpath persisted as a file).
	 * Also force classpath problems to be refresh if not running in autobuild mode.
	 * NOTE: It can induce resource changes, and cannot be called during POST_CHANGE notification.
	 *
	 */
	public void performPreBuildCheck(
		IResourceDelta delta,
		IJavaElement parent) {
	
		try {
			if (!ResourcesPlugin.getWorkspace().isAutoBuilding()) {
				Iterator iterator = this.projectsToUpdate.iterator();
				while (iterator.hasNext()) {
					try {
						JavaProject project = (JavaProject)iterator.next();
						
						 // force classpath marker refresh
						project.getResolvedClasspath(
							true, // ignoreUnresolvedEntry
							true); // generateMarkerOnError
						
					} catch (JavaModelException e) {
					}
				}
				if (!this.projectsToUpdate.isEmpty()){
					try {
						// update all cycle markers
						JavaProject.updateAllCycleMarkers();
					} catch (JavaModelException e) {
					}
				}				
			}
		} finally {
			this.projectsToUpdate = new HashSet();
		}
	
		IResource resource = delta.getResource();
		IJavaElement element = JavaCore.create(resource);
		boolean processChildren = false;
	
		switch (resource.getType()) {
	
			case IResource.ROOT :
			case IResource.PROJECT :
				if (delta.getKind() == IResourceDelta.CHANGED) {
					processChildren = true;
				}
				break;
			case IResource.FILE :
				if (parent.getElementType() == IJavaElement.JAVA_PROJECT) {
					IFile file = (IFile) resource;
					JavaProject project = (JavaProject) parent;
	
					/* check classpath property file change */
					QualifiedName classpathProp;
					if (file.getName().equals(
							project.computeSharedPropertyFileName(
								classpathProp = project.getClasspathPropertyName()))) {
	
						switch (delta.getKind()) {
							case IResourceDelta.REMOVED : // recreate one based on in-memory path
								try {
									project.saveClasspath(project.getRawClasspath(), project.getOutputLocation());
								} catch (JavaModelException e) {
								}
								break;
							case IResourceDelta.CHANGED :
								if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
									break; // only consider content change
							case IResourceDelta.ADDED :
								// check if any actual difference
								try {
									// force to (re)read the property file
									String fileClasspathString = project.getSharedProperty(classpathProp);
									if (fileClasspathString == null)
										break; // did not find the file
									IClasspathEntry[] fileEntries = project.readPaths(fileClasspathString);
									if (fileEntries == null)
										break; // could not read, ignore 
									if (project.isClasspathEqualsTo(project.getRawClasspath(), project.getOutputLocation(), fileEntries))
										break;
	
									// will force an update of the classpath/output location based on the file information
									// extract out the output location
									IPath outputLocation = null;
									if (fileEntries != null && fileEntries.length > 0) {
										IClasspathEntry entry = fileEntries[fileEntries.length - 1];
										if (entry.getContentKind() == ClasspathEntry.K_OUTPUT) {
											outputLocation = entry.getPath();
											IClasspathEntry[] copy = new IClasspathEntry[fileEntries.length - 1];
											System.arraycopy(fileEntries, 0, copy, 0, copy.length);
											fileEntries = copy;
										}
									}
									// restore output location				
									if (outputLocation == null) {
										outputLocation = SetClasspathOperation.ReuseOutputLocation;
									}
									try {
										project.setRawClasspath(
											fileEntries, 
											outputLocation, 
											null, // monitor
											true, // canChangeResource
											false, // forceSave
											project.getResolvedClasspath(true), // ignoreUnresolvedVariable
											true); // needCycleCheck
									} catch (JavaModelException e) {
									}
								} catch (IOException e) {
									break;
								} catch (RuntimeException e) {
									break;
								} catch (CoreException e) {
									break;
								}
	
						}
					}
				}
				break;
		}
		if (processChildren) {
			IResourceDelta[] children = delta.getAffectedChildren();
			for (int i = 0; i < children.length; i++) {
				performPreBuildCheck(children[i], element);
			}
		}
	}

	/**
	 * Closes the given element, which removes it from the cache of open elements.
	 */
	protected static void close(Openable element) {

		try {
			element.close();
		} catch (JavaModelException e) {
			// do nothing
		}
	}


private void cloneCurrentDelta(IJavaProject project, IPackageFragmentRoot root) {
	JavaElementDelta delta = (JavaElementDelta)fCurrentDelta.find(root);
	if (delta == null) return;
	JavaElementDelta clone = (JavaElementDelta)delta.clone(project);
	fCurrentDelta.insertDeltaTree(clone.getElement(), clone);
	switch (clone.getKind()) {
		case IJavaElementDelta.ADDED:
			this.addToParentInfo((Openable)clone.getElement());
			break;
		case IJavaElementDelta.REMOVED:
			Openable element = (Openable)clone.getElement();
			if (element.isOpen()) {
				try {
					element.close();
				} catch (JavaModelException e) {
				}
			}
			this.removeFromParentInfo(element);
			break;
	}
}


	/**
	 * Generic processing for elements with changed contents:<ul>
	 * <li>The element is closed such that any subsequent accesses will re-open
	 * the element reflecting its new structure.
	 * <li>An entry is made in the delta reporting a content change (K_CHANGE with F_CONTENT flag set).
	 * </ul>
	 */
	protected void contentChanged(Openable element, IResourceDelta delta) {

		close(element);
		fCurrentDelta.changed(element, IJavaElementDelta.F_CONTENT);
	}

	/**
	 * Creates the openables corresponding to this resource.
	 * Returns null if none was found.
	 */
	protected Openable createElement(IResource resource, int elementType, IJavaProject project) {
		if (resource == null) return null;
		
		IPath path = resource.getFullPath();
		IJavaElement element = null;
		switch (elementType) {
			case IJavaElement.JAVA_PROJECT:
				this.popUntilPrefixOf(path);
				if (this.currentElement != null) return this.currentElement;
				IProject proj = (IProject)resource;
				boolean isOpened = proj.isOpen();
				if (isOpened && this.hasJavaNature(proj)) {
					element = project == null ? JavaCore.create(proj) : project;
				} else if (!isOpened) {
					if (project == null) {
						project = JavaCore.create(proj);
					}
					if (project.isOpen()) {
						element = project; // java project is being closed or removed
					} 
				} // else not a java-project
				break;
			case IJavaElement.PACKAGE_FRAGMENT_ROOT:
				element = project == null ? JavaCore.create(resource) : project.getPackageFragmentRoot(resource);
				break;
			case IJavaElement.PACKAGE_FRAGMENT:
				// find the element that encloses the resource
				this.popUntilPrefixOf(path);
				
				if (this.currentElement == null) {
					element = JavaModelManager.getJavaModelManager().create(resource, project);
				} else {
					// find the root
					IPackageFragmentRoot root = this.currentElement.getPackageFragmentRoot();
					if (root == null) {
						element = JavaModelManager.getJavaModelManager().create(resource, project);
					} else if (!JavaModelManager.conflictsWithOutputLocation(path, (JavaProject)root.getJavaProject())) {
						// create package handle
						IPath pkgPath = path.removeFirstSegments(root.getPath().segmentCount());
						String pkg = Util.packageName(pkgPath);
						if (pkg == null) return null;
						element = root.getPackageFragment(pkg);
					}
				}
				break;
			case IJavaElement.COMPILATION_UNIT:
			case IJavaElement.CLASS_FILE:
				// find the element that encloses the resource
				this.popUntilPrefixOf(path);
				
				if (this.currentElement == null) {
					element = element = JavaModelManager.getJavaModelManager().create(resource, project);
				} else {
					// find the package
					IPackageFragment pkgFragment = null;
					switch (this.currentElement.getElementType()) {
						case IJavaElement.PACKAGE_FRAGMENT_ROOT:
							IPackageFragmentRoot root = (IPackageFragmentRoot)this.currentElement;
							IPath rootPath = root.getPath();
							IPath pkgPath = path.removeLastSegments(1);
							String pkgName = Util.packageName(pkgPath.removeFirstSegments(rootPath.segmentCount()));
							if (pkgName != null) {
								pkgFragment = root.getPackageFragment(pkgName);
							}
							break;
						case IJavaElement.PACKAGE_FRAGMENT:
							Openable pkg = (Openable)this.currentElement;
							if (pkg.getPath().equals(path.removeLastSegments(1))) {
								pkgFragment = (IPackageFragment)pkg;
							} // else case of package x which is a prefix of x.y
							break;
						case IJavaElement.COMPILATION_UNIT:
						case IJavaElement.CLASS_FILE:
							pkgFragment = (IPackageFragment)this.currentElement.getParent();
							break;
					}
					if (pkgFragment == null) {
						element = JavaModelManager.getJavaModelManager().create(resource, project);
					} else {
						if (elementType == IJavaElement.COMPILATION_UNIT) {
							// create compilation unit handle 
							// fileName validation has been done in elementType(IResourceDelta, int, boolean)
							String fileName = path.lastSegment();
							element = pkgFragment.getCompilationUnit(fileName);
						} else {
							// create class file handle
							// fileName validation has been done in elementType(IResourceDelta, int, boolean)
							String fileName = path.lastSegment();
							element = pkgFragment.getClassFile(fileName);
						}
					}
				}
				break;
		}
		if (element == null) {
			return null;
		} else {
			this.currentElement = (Openable)element;
			return this.currentElement;
		}
	}

	/**
	 * Processing for an element that has been added:<ul>
	 * <li>If the element is a project, do nothing, and do not process
	 * children, as when a project is created it does not yet have any
	 * natures - specifically a java nature.
	 * <li>If the elemet is not a project, process it as added (see
	 * <code>basicElementAdded</code>.
	 * </ul>
	 */
	protected void elementAdded(Openable element, IResourceDelta delta) {
		int elementType = element.getElementType();
		if (elementType == IJavaElement.JAVA_PROJECT) {
			// project add is handled by JavaProject.configure() because
			// when a project is created, it does not yet have a java nature
			if (hasJavaNature((IProject)delta.getResource())) {
				addToParentInfo(element);
				if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
					Openable movedFromElement = (Openable)element.getJavaModel().getJavaProject(delta.getMovedFromPath().lastSegment());
					fCurrentDelta.movedTo(element, movedFromElement);
				} else {
					fCurrentDelta.added(element);
				}
				this.projectsToUpdate.add(element);
			}
		} else {
			addToParentInfo(element);
			
			// Force the element to be closed as it might have been opened 
			// before the resource modification came in and it might have a new child
			// For example, in an IWorkspaceRunnable:
			// 1. create a package fragment p using a java model operation
			// 2. open package p
			// 3. add file X.java in folder p
			// When the resource delta comes in, only the addition of p is notified, 
			// but the package p is already opened, thus its children are not recomputed
			// and it appears empty.
			close(element);
			
			if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
				IPath movedFromPath = delta.getMovedFromPath();
				IResource res = delta.getResource();
				IResource movedFromRes;
				if (res instanceof IFile) {
					movedFromRes = res.getWorkspace().getRoot().getFile(movedFromPath);
				} else {
					movedFromRes = res.getWorkspace().getRoot().getFolder(movedFromPath);
				}
				
				// find the element type of the moved from element
				IJavaProject projectOfRoot = (IJavaProject)this.roots.get(movedFromPath);
				boolean isPkgFragmentRoot = 
					projectOfRoot != null 
					&& (projectOfRoot.getProject().getFullPath().isPrefixOf(movedFromPath));
				int movedFromType = 
					this.elementType(
						movedFromRes, 
						delta.getKind(),
						delta.getFlags(),
						element.getParent().getElementType(), 
						isPkgFragmentRoot);
				
				// create the moved from element
				Openable movedFromElement = 
					elementType != IJavaElement.JAVA_PROJECT && movedFromType == IJavaElement.JAVA_PROJECT ? 
						null : // outside classpath
						this.createElement(movedFromRes, movedFromType, null); // pass null for the project in case the element is moving to another project
				if (movedFromElement == null) {
					// moved from outside classpath
					fCurrentDelta.added(element);
				} else {
					fCurrentDelta.movedTo(element, movedFromElement);
				}
			} else {
				fCurrentDelta.added(element);
			}
			
			switch (elementType) {
				case IJavaElement.PACKAGE_FRAGMENT_ROOT :
					// when a root is added, and is on the classpath, the project must be updated
					this.projectsToUpdate.add(element.getJavaProject());
					break;
				case IJavaElement.PACKAGE_FRAGMENT :
					// get rid of namelookup since it holds onto obsolete cached info 
					JavaProject project = (JavaProject) element.getJavaProject();
					try {
						project.getJavaProjectElementInfo().setNameLookup(null);
					} catch (JavaModelException e) {
					}
					// add subpackages
					PackageFragmentRoot root = element.getPackageFragmentRoot();
					String name = element.getElementName();
					IResourceDelta[] children = delta.getAffectedChildren();
					for (int i = 0, length = children.length; i < length; i++) {
						IResourceDelta child = children[i];
						IResource resource = child.getResource();
						if (resource instanceof IFolder) {
							String subpkgName = 
								name.length() == 0 ? 
									resource.getName() : 
									name + "." + resource.getName(); //$NON-NLS-1$
							Openable subpkg = (Openable)root.getPackageFragment(subpkgName);
							this.updateIndex(subpkg, child);
							this.elementAdded(subpkg, child);
						}
					}
					break;
			}
		}
	}





	/**
	 * Generic processing for a removed element:<ul>
	 * <li>Close the element, removing its structure from the cache
	 * <li>Remove the element from its parent's cache of children
	 * <li>Add a REMOVED entry in the delta
	 * </ul>
	 */
	protected void elementRemoved(Openable element, IResourceDelta delta) {
		
		if (element.isOpen()) {
			close(element);
		}
		removeFromParentInfo(element);
		int elementType = element.getElementType();
		if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0) {
			IPath movedToPath = delta.getMovedToPath();
			IResource res = delta.getResource();
			IResource movedToRes;
			switch (res.getType()) {
				case IResource.PROJECT:
					movedToRes = res.getWorkspace().getRoot().getProject(movedToPath.lastSegment());
					break;
				case IResource.FOLDER:
					movedToRes = res.getWorkspace().getRoot().getFolder(movedToPath);
					break;
				case IResource.FILE:
					movedToRes = res.getWorkspace().getRoot().getFile(movedToPath);
					break;
				default:
					return;
			}

			// find the element type of the moved from element
			IJavaProject projectOfRoot = (IJavaProject)this.roots.get(movedToPath);
			boolean isPkgFragmentRoot = 
				projectOfRoot != null 
				&& (projectOfRoot.getProject().getFullPath().isPrefixOf(movedToPath));
			int movedToType = 
				this.elementType(
					movedToRes, 
					delta.getKind(),
					delta.getFlags(),
					element.getParent().getElementType(), 
					isPkgFragmentRoot);
			
			// create the moved To element
			Openable movedToElement = 
				elementType != IJavaElement.JAVA_PROJECT && movedToType == IJavaElement.JAVA_PROJECT ? 
					null : // outside classpath
					this.createElement(movedToRes, movedToType, null); // pass null for the project in case the element is moving to another project
			if (movedToElement == null) {
				// moved outside classpath
				fCurrentDelta.removed(element);
			} else {
				fCurrentDelta.movedFrom(element, movedToElement);
			}
		} else {
			fCurrentDelta.removed(element);
		}

		switch (elementType) {
			case IJavaElement.JAVA_MODEL :
				element.getJavaModelManager().getIndexManager().reset();
				break;
			case IJavaElement.JAVA_PROJECT :
				JavaModelManager.getJavaModelManager().removePerProjectInfo(
					(JavaProject) element);
				break;
			case IJavaElement.PACKAGE_FRAGMENT_ROOT :
				this.projectsToUpdate.add(element.getJavaProject());
				break;
			case IJavaElement.PACKAGE_FRAGMENT :
				//1G1TW2T - get rid of namelookup since it holds onto obsolete cached info 
				JavaProject project = (JavaProject) element.getJavaProject();
				try {
					project.getJavaProjectElementInfo().setNameLookup(null);
				} catch (JavaModelException e) {
				}
				// remove subpackages
				PackageFragmentRoot root = element.getPackageFragmentRoot();
				String name = element.getElementName();
				IResourceDelta[] children = delta.getAffectedChildren();
				for (int i = 0, length = children.length; i < length; i++) {
					IResourceDelta child = children[i];
					IResource resource = child.getResource();
					if (resource instanceof IFolder) {
						String subpkgName = 
							name.length() == 0 ? 
								resource.getName() : 
								name + "." + resource.getName(); //$NON-NLS-1$
						Openable subpkg = (Openable)root.getPackageFragment(subpkgName);
						this.updateIndex(subpkg, child);
						this.elementRemoved(subpkg, child);
					}
				}
				break;
		}
	}

	/**
	 * Filters the generated <code>JavaElementDelta</code>s to remove those
	 * which should not be fired (because they don't represent a real change
	 * in the Java Model).
	 */
	protected IJavaElementDelta[] filterRealDeltas(IJavaElementDelta[] deltas) {

		int length = deltas.length;
		IJavaElementDelta[] realDeltas = null;
		int index = 0;
		for (int i = 0; i < length; i++) {
			JavaElementDelta delta = (JavaElementDelta)deltas[i];
			if (delta == null) {
				continue;
			}
			if (delta.getAffectedChildren().length > 0
 delta.getKind() == IJavaElementDelta.ADDED
 delta.getKind() == IJavaElementDelta.REMOVED
 (delta.getFlags() & IJavaElementDelta.F_CLOSED) != 0
 (delta.getFlags() & IJavaElementDelta.F_OPENED) != 0
 delta.resourceDeltasCounter > 0) {

				if (realDeltas == null) {
					realDeltas = new IJavaElementDelta[length];
				}
				realDeltas[index++] = delta;
			}
		}
		if (index > 0) {
			IJavaElementDelta[] result = new IJavaElementDelta[index];
			System.arraycopy(realDeltas, 0, result, 0, index);
			return result;
		} else {
			return NO_DELTA;
		}
	}

/**
 * Returns true if the given resource is contained in an open project
 * with a java nature, otherwise false.
 */
protected boolean hasJavaNature(IResource resource) {
	// ensure the project has a java nature (if open)
	IProject project = resource.getProject();
	if (project.isOpen()) {
		try {
			return project.hasNature(JavaCore.NATURE_ID);
		} catch (CoreException e) {
			// do nothing
		}
	}
	return false;
}


private JavaModelException newInvalidElementType() {
	return new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES));
}
	/**
	 * Generic processing for elements with changed contents:<ul>
	 * <li>The element is closed such that any subsequent accesses will re-open
	 * the element reflecting its new structure.
	 * <li>An entry is made in the delta reporting a content change (K_CHANGE with F_CONTENT flag set).
	 * </ul>
	 */
	protected void nonJavaResourcesChanged(Openable element, IResourceDelta delta)
		throws JavaModelException {

		// reset non-java resources if element was open
		if (element.isOpen()) {
			JavaElementInfo info = element.getElementInfo();
			switch (element.getElementType()) {
				case IJavaElement.JAVA_PROJECT :
					((JavaProjectElementInfo) info).setNonJavaResources(null);
	
					// if a package fragment root is the project, clear it too
					PackageFragmentRoot projectRoot =
						(PackageFragmentRoot) ((JavaProject) element).getPackageFragmentRoot(
							new Path(IPackageFragment.DEFAULT_PACKAGE_NAME));
					if (projectRoot.isOpen()) {
						((PackageFragmentRootInfo) projectRoot.getElementInfo()).setNonJavaResources(
							null);
					}
					break;
				case IJavaElement.PACKAGE_FRAGMENT :
					 ((PackageFragmentInfo) info).setNonJavaResources(null);
					break;
				case IJavaElement.PACKAGE_FRAGMENT_ROOT :
					 ((PackageFragmentRootInfo) info).setNonJavaResources(null);
			}
		}

		JavaElementDelta elementDelta = fCurrentDelta.find(element);
		if (elementDelta == null) {
			fCurrentDelta.changed(element, IJavaElementDelta.F_CONTENT);
			elementDelta = fCurrentDelta.find(element);
		}
		elementDelta.addResourceDelta(delta);
	}
	private void popUntilPrefixOf(IPath path) {
		while (this.currentElement != null) {
			IPath currentElementPath = null;
			if (this.currentElement instanceof IPackageFragmentRoot) {
				currentElementPath = ((IPackageFragmentRoot)this.currentElement).getPath();
			} else {
				IResource currentElementResource = null;
				try {
					currentElementResource = this.currentElement.getUnderlyingResource();
				} catch (JavaModelException e) {
				}
				if (currentElementResource != null) {
					currentElementPath = currentElementResource.getFullPath();
				}
			}
			if (currentElementPath != null) {
				if (this.currentElement instanceof IPackageFragment 
					&& this.currentElement.getElementName().length() == 0
					&& currentElementPath.segmentCount() != path.segmentCount()-1) {
						// default package and path is not a direct child
						this.currentElement = (Openable)this.currentElement.getParent();
				}
				if (currentElementPath.isPrefixOf(path)) {
					return;
				}
			}
			this.currentElement = (Openable)this.currentElement.getParent();
		}
	}

	/**
	 * Converts a <code>IResourceDelta</code> rooted in a <code>Workspace</code> into
	 * the corresponding set of <code>IJavaElementDelta</code>, rooted in the
	 * relevant <code>JavaModel</code>s.
	 */
	public IJavaElementDelta[] processResourceDelta(IResourceDelta changes, int eventType) {

		try {
			this.currentEventType = eventType;
			IJavaModel model = JavaModelManager.getJavaModelManager().getJavaModel();
			if (!model.isOpen()) {
				// force opening of java model so that java element delta are reported
				try {
					model.open(null);
				} catch (JavaModelException e) {
					if (VERBOSE) {
						e.printStackTrace();
					}
					return NO_DELTA;
				}
			}
			this.initializeRoots(model);
			this.currentElement = null;
			
			// get the workspace delta, and start processing there.
			IResourceDelta[] deltas = changes.getAffectedChildren();
			IJavaElementDelta[] translatedDeltas = new JavaElementDelta[deltas.length];
			for (int i = 0; i < deltas.length; i++) {
				IResourceDelta delta = deltas[i];
				IResource res = delta.getResource();
				fCurrentDelta = new JavaElementDelta(model);
				
				// find out whether the delta is a package fragment root
				IJavaProject projectOfRoot = (IJavaProject)this.roots.get(res.getFullPath());
				boolean isPkgFragmentRoot = projectOfRoot != null;
				int elementType = 
					this.elementType(
						res, 
						delta.getKind(),
						delta.getFlags(),
						IJavaElement.JAVA_MODEL, 
						isPkgFragmentRoot);
				
				this.traverseDelta(delta, elementType, projectOfRoot, null, IGNORE); // traverse delta
				translatedDeltas[i] = fCurrentDelta;
			}
			
			// update package fragment roots of projects that were affected
			Iterator iterator = this.projectsToUpdate.iterator();
			while (iterator.hasNext()) {
				JavaProject project = (JavaProject)iterator.next();
				project.updatePackageFragmentRoots();
			}
	
			return filterRealDeltas(translatedDeltas);
		} finally {
			this.projectsToUpdate = new HashSet();
		}
	}
	
/*
 * Update the current delta (ie. add/remove/change the given element) and update the correponding index.
 * Returns whether the children of the given delta must be processed.
 * @throws a JavaModelException if the delta doesn't correspond to a java element of the given type.
 */
private boolean updateCurrentDeltaAndIndex(IResourceDelta delta, int elementType, IJavaProject project) throws JavaModelException {
	Openable element;
	switch (delta.getKind()) {
		case IResourceDelta.ADDED :
			element = this.createElement(delta.getResource(), elementType, project);
			if (element == null) throw newInvalidElementType();
			this.updateIndex(element, delta);
			this.elementAdded(element, delta);
			return false;
		case IResourceDelta.REMOVED :
			element = this.createElement(delta.getResource(), elementType, project);
			if (element == null) throw newInvalidElementType();
			this.updateIndex(element, delta);
			this.elementRemoved(element, delta);
			return false;
		case IResourceDelta.CHANGED :
			int flags = delta.getFlags();
			if ((flags & IResourceDelta.CONTENT) != 0) {
				// content has changed
				element = this.createElement(delta.getResource(), elementType, project);
				if (element == null) throw newInvalidElementType();
				this.updateIndex(element, delta);
				this.contentChanged(element, delta);
			} else if (elementType == IJavaElement.JAVA_PROJECT) {
				if ((flags & IResourceDelta.OPEN) != 0) {
					// project has been opened or closed
					IProject res = (IProject)delta.getResource();
					element = this.createElement(res, elementType, project);
					if (element == null) throw newInvalidElementType();
					if (res.isOpen()) {
						if (this.hasJavaNature(res)) {
							this.elementAdded(element, delta);
							this.indexManager.indexAll(res);
						}
					} else {
						JavaModel javaModel = JavaModelManager.getJavaModelManager().getJavaModel();
						boolean wasJavaProject = javaModel.findJavaProject(res) != null;
						if (wasJavaProject) {
							this.elementRemoved(element, delta);
							this.indexManager.removeIndex(res.getFullPath());
							
						}
					}
					return false; // when a project is open/closed don't process children
				}
				if ((flags & IResourceDelta.DESCRIPTION) != 0) {
					IProject res = (IProject)delta.getResource();
					JavaModel javaModel = JavaModelManager.getJavaModelManager().getJavaModel();
					boolean wasJavaProject = javaModel.findJavaProject(res) != null;
					boolean isJavaProject = this.hasJavaNature(res);
					if (wasJavaProject != isJavaProject) {
						// project's nature has been added or removed
						element = this.createElement(res, elementType, project);
						if (element == null) throw newInvalidElementType();
						if (isJavaProject) {
							this.elementAdded(element, delta);
							this.indexManager.indexAll(res);
						} else {
							this.elementRemoved(element, delta);
							this.indexManager.removeIndex(res.getFullPath());
						}
						return false; // when a project's nature is added/removed don't process children
					}
				}
			}
			return true;
	}
	return true;
}

	/**
	 * Removes the given element from its parents cache of children. If the
	 * element does not have a parent, or the parent is not currently open,
	 * this has no effect. 
	 */
	protected void removeFromParentInfo(Openable child) {

		Openable parent = (Openable) child.getParent();
		if (parent != null && parent.isOpen()) {
			try {
				JavaElementInfo info = parent.getElementInfo();
				info.removeChild(child);
			} catch (JavaModelException e) {
				// do nothing - we already checked if open
			}
		}
	}

	/**
	 * Converts an <code>IResourceDelta</code> and its children into
	 * the corresponding <code>IJavaElementDelta</code>s.
	 * Return whether the delta corresponds to a resource on the classpath.
	 * If it is not a resource on the classpath, it will be added as a non-java
	 * resource by the sender of this method.
	 */
	protected boolean traverseDelta(
		IResourceDelta delta, 
		int elementType, 
		IJavaProject currentProject,
		IPath currentOutput,
		int outputTraverseMode) {
			
		IResource res = delta.getResource();
		
		// process current delta
		boolean processChildren = true;
		if (currentProject != null || res instanceof IProject) {
			if (this.currentElement == null || !this.currentElement.getJavaProject().equals(currentProject)) {
				// force the currentProject to be used
				this.currentElement = (Openable)currentProject;
			}
			try {
				processChildren = this.updateCurrentDeltaAndIndex(delta, elementType, currentProject);
			} catch (JavaModelException e) {
				// non java resource or invalid project
				return false;
			}
		} else {
			// not yet inside a package fragment root
			processChildren = true;
		}
		
		// get the project's output location
		if (currentOutput == null) {
			try {
				IJavaProject proj =
					currentProject == null ?
						(IJavaProject)this.createElement(res.getProject(), IJavaElement.JAVA_PROJECT, null) :
						currentProject;
				if (proj != null) {
					currentOutput = proj.getOutputLocation();
					if (proj.getProject().getFullPath().equals(currentOutput)){ // case of proj==bin==src
						outputTraverseMode = SOURCE;
					} else {
						// check case of src==bin
						IClasspathEntry[] classpath = proj.getResolvedClasspath(true);
						for (int i = 0, length = classpath.length; i < length; i++) {
							IClasspathEntry entry = classpath[i];
							if (entry.getPath().equals(currentOutput)) {
								outputTraverseMode = (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) ? SOURCE : BINARY;
								break;
							}
						}
					}
				}
			} catch (JavaModelException e) {
			}
		}

		// process children if needed
		if (processChildren) {
			IResourceDelta[] children = delta.getAffectedChildren();
			boolean oneChildOnClasspath = false;
			int length = children.length;
			IResourceDelta[] orphanChildren = new IResourceDelta[length];
			Openable parent = null;
			boolean isValidParent = true;
			for (int i = 0; i < length; i++) {
				IResourceDelta child = children[i];
				IResource childRes = child.getResource();
				IPath childPath = childRes.getFullPath();

				// find out whether the child is a package fragment root of the current project
				IJavaProject projectOfRoot = (IJavaProject)this.roots.get(childPath);
				boolean isPkgFragmentRoot = 
					projectOfRoot != null 
					&& (projectOfRoot.getProject().getFullPath().isPrefixOf(childPath));
				int childType = 
					this.elementType(
						childRes, 
						child.getKind(),
						child.getFlags(),
						elementType, 
						isPkgFragmentRoot);
				
				// filter out changes in output location
				if (currentOutput != null && currentOutput.isPrefixOf(childPath)) {
					if (outputTraverseMode != IGNORE) {
						// case of bin=src
						if (outputTraverseMode == SOURCE && childType == IJavaElement.CLASS_FILE) {
							continue;
						}
						// case of .class file under project and no source folder
						// proj=bin
						if (childType == IJavaElement.JAVA_PROJECT 
							&& childRes instanceof IFile 
							&& Util.isValidClassFileName(childRes.getName())) {
							continue;
						}
					} else {
						continue;
					}
				}
				
				// traverse delta for child in the same project
				if (childType == -1
 !this.traverseDelta(child, childType, (currentProject == null && isPkgFragmentRoot) ? projectOfRoot : currentProject, currentOutput, outputTraverseMode)) {
					try {
						if (currentProject != null) {
							if (!isValidParent) continue; 
							if (parent == null) {
								if (this.currentElement == null || !this.currentElement.getJavaProject().equals(currentProject)) {
									// force the currentProject to be used
									this.currentElement = (Openable)currentProject;
								}
								if (elementType == IJavaElement.JAVA_PROJECT
 (elementType == IJavaElement.PACKAGE_FRAGMENT_ROOT && res instanceof IProject)) { 
									// NB: attach non-java resource to project (not to its package fragment root)
									parent = (Openable)currentProject;
								} else {
									parent = this.createElement(res, elementType, currentProject);
								}
								if (parent == null) {
									isValidParent = false;
									continue;
								}
							}
							// add child as non java resource
							nonJavaResourcesChanged(parent, child);
						} else {
							orphanChildren[i] = child;
						}
					} catch (JavaModelException e) {
					}
				} else {
					oneChildOnClasspath = true;
				}
				
				// if child is a package fragment root of another project, traverse delta too
				if (projectOfRoot != null && !isPkgFragmentRoot) {
					this.traverseDelta(child, IJavaElement.PACKAGE_FRAGMENT_ROOT, projectOfRoot, null, IGNORE); // binary output of projectOfRoot cannot be this root
					// NB: No need to check the return value as the child can only be on the classpath
				}
				
				// if the child is a package fragment root of one or several other projects
				HashSet set;
				if ((set = (HashSet)this.otherRoots.get(childPath)) != null) {
					IPackageFragmentRoot currentRoot = 
						(currentProject == null ? 
							projectOfRoot : 
							currentProject).getPackageFragmentRoot(childRes);
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						IJavaProject project = (IJavaProject) iterator.next();
						this.cloneCurrentDelta(project, currentRoot);
					}
				}
			}
			if (oneChildOnClasspath || res instanceof IProject) {
				// add orphan children (case of non java resources under project)
				JavaProject adoptiveProject = (JavaProject)JavaCore.getJavaCore().create(res.getProject());
				if (adoptiveProject != null) {
					for (int i = 0; i < length; i++) {
						if (orphanChildren[i] != null) {
							try {
								nonJavaResourcesChanged(adoptiveProject, orphanChildren[i]);
							} catch (JavaModelException e) {
							}
						}
					}
				}
			} // else resource delta will be added by parent
			return isValidParent && (currentProject != null || oneChildOnClasspath);
		} else {
			// if not on classpath or if the element type is -1, 
			// it's a non-java resource
			return currentProject != null && elementType != -1;
		}
	}

	/*
	 * Returns the type of the java element the given delta matches to.
	 * Returns -1 if unknown (e.g. a non-java resource.)
	 */
	private int elementType(IResource res, int kind, int flags, int parentType, boolean isPkgFragmentRoot) {
		switch (parentType) {
			case IJavaElement.JAVA_MODEL:
				if (kind != IResourceDelta.CHANGED
 (flags & IResourceDelta.OPEN) != 0
 (flags & IResourceDelta.DESCRIPTION) != 0) {
					// project is added, removed, opened or closed, or its nature is changed
					return IJavaElement.JAVA_PROJECT;
				} // else see below
			case IJavaElement.JAVA_PROJECT:
				if (isPkgFragmentRoot) {
					return IJavaElement.PACKAGE_FRAGMENT_ROOT;
				} else {
					return IJavaElement.JAVA_PROJECT; // not yet in a package fragment root
				}
			case IJavaElement.PACKAGE_FRAGMENT_ROOT:
			case IJavaElement.PACKAGE_FRAGMENT:
				if (res instanceof IFolder) {
					if (Util.isValidFolderNameForPackage(res.getName())) {
						return IJavaElement.PACKAGE_FRAGMENT;
					} else {
						return -1;
					}
				} else {
					String fileName = res.getName();
					if (Util.isValidCompilationUnitName(fileName)) {
						return IJavaElement.COMPILATION_UNIT;
					} else if (Util.isValidClassFileName(fileName)) {
						return IJavaElement.CLASS_FILE;
					} else if (this.roots.get(res.getFullPath()) != null) {
						// case of proj=src=bin and resource is a jar file on the classpath
						return IJavaElement.PACKAGE_FRAGMENT_ROOT;
					} else {
						return -1;
					}
				}
			default:
				return -1;
		}
	}
	
private void initializeRoots(IJavaModel model) {
	this.roots = new HashMap();
	this.otherRoots = new HashMap();
	IJavaProject[] projects;
	try {
		projects = model.getJavaProjects();
	} catch (JavaModelException e) {
		// nothing can be done
		return;
	}
	for (int i = 0, length = projects.length; i < length; i++) {
		IJavaProject project = projects[i];
		IClasspathEntry[] classpath;
		try {
			classpath = project.getResolvedClasspath(true);
		} catch (JavaModelException e) {
			// continue with next project
			continue;
		}
		for (int j= 0, classpathLength = classpath.length; j < classpathLength; j++) {
			IClasspathEntry entry = classpath[j];
			if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) continue;
			IPath path = entry.getPath();
			if (this.roots.get(path) == null) {
				this.roots.put(path, project);
			} else {
				HashSet set = (HashSet)this.otherRoots.get(path);
				if (set == null) {
					set = new HashSet();
					this.otherRoots.put(path, set);
				}
				set.add(project);
			}
		}
	}
}

private boolean isOnClasspath(IPath path) {
	return this.roots.get(path) != null;
}

protected void updateIndex(Openable element, IResourceDelta delta) {

	if (indexManager == null)
		return;

	switch (element.getElementType()) {
		case IJavaElement.JAVA_PROJECT :
			switch (delta.getKind()) {
				case IResourceDelta.ADDED :
					indexManager.indexAll(element.getJavaProject().getProject());
					break;
				case IResourceDelta.REMOVED :
					indexManager.removeIndex(element.getJavaProject().getProject().getFullPath());
					break;
				// NB: Update of index if project is opened, closed, or its java nature is added or removed
				//     is done in updateCurrentDeltaAndIndex
			}
			break;
		case IJavaElement.PACKAGE_FRAGMENT_ROOT :
			if (element instanceof JarPackageFragmentRoot) {
				JarPackageFragmentRoot root = (JarPackageFragmentRoot)element;
				// index jar file only once (if the root is in its declaring project)
				IPath jarPath = root.getPath();
				switch (delta.getKind()) {
					case IResourceDelta.ADDED:
						// index the new jar
						indexManager.indexLibrary(jarPath, root.getJavaProject().getProject());
						break;
					case IResourceDelta.CHANGED:
						// first remove the index so that it is forced to be re-indexed
						indexManager.removeIndex(jarPath);
						// then index the jar
						indexManager.indexLibrary(jarPath, root.getJavaProject().getProject());
						break;
					case IResourceDelta.REMOVED:
						// the jar was physically removed: remove the index
						indexManager.removeIndex(jarPath);
						break;
				}
				break;
			} else {
				int kind = delta.getKind();
				if (kind == IResourceDelta.ADDED || kind == IResourceDelta.REMOVED) {
					IPackageFragmentRoot root = (IPackageFragmentRoot)element;
					this.updateRootIndex(root, root.getPackageFragment(""), delta); //$NON-NLS-1$
					break;
				}
			}
			// don't break as packages of the package fragment root can be indexed below
		case IJavaElement.PACKAGE_FRAGMENT :
			switch (delta.getKind()) {
				case IResourceDelta.ADDED:
				case IResourceDelta.REMOVED:
					IPackageFragment pkg = null;
					if (element instanceof IPackageFragmentRoot) {
						IPackageFragmentRoot root = (IPackageFragmentRoot)element;
						pkg = root.getPackageFragment(""); //$NON-NLS-1$
					} else {
						pkg = (IPackageFragment)element;
					}
					IResourceDelta[] children = delta.getAffectedChildren();
					for (int i = 0, length = children.length; i < length; i++) {
						IResourceDelta child = children[i];
						IResource resource = child.getResource();
						if (resource instanceof IFile) {
							String extension = resource.getFileExtension();
							if ("java".equalsIgnoreCase(extension)) { //$NON-NLS-1$
								Openable cu = (Openable)pkg.getCompilationUnit(resource.getName());
								this.updateIndex(cu, child);
							} else if ("class".equalsIgnoreCase(extension)) { //$NON-NLS-1$
								Openable classFile = (Openable)pkg.getClassFile(resource.getName());
								this.updateIndex(classFile, child);
							}
						}
					}
					break;
			}
			break;
		case IJavaElement.CLASS_FILE :
			IFile file = (IFile) delta.getResource();
			IJavaProject project = element.getJavaProject();
			IPath binaryFolderPath = element.getPackageFragmentRoot().getPath();
			// if the class file is part of the binary output, it has been created by
			// the java builder -> ignore
			try {
				if (binaryFolderPath.equals(project.getOutputLocation())) {
					break;
				}
			} catch (JavaModelException e) {
			}
			switch (delta.getKind()) {
				case IResourceDelta.CHANGED :
					// no need to index if the content has not changed
					if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
						break;
				case IResourceDelta.ADDED :
					indexManager.addBinary(file, binaryFolderPath);
					break;
				case IResourceDelta.REMOVED :
					indexManager.remove(file.getFullPath().toString(), binaryFolderPath);
					break;
			}
			break;
		case IJavaElement.COMPILATION_UNIT :
			file = (IFile) delta.getResource();
			switch (delta.getKind()) {
				case IResourceDelta.CHANGED :
					// no need to index if the content has not changed
					if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
						break;
				case IResourceDelta.ADDED :
					indexManager.addSource(file, file.getProject().getProject().getFullPath());
					break;
				case IResourceDelta.REMOVED :
					indexManager.remove(file.getFullPath().toString(), file.getProject().getProject().getFullPath());
					break;
			}
	}
}
/**
 * Upadtes the index of the given root (assuming it's an addition or a removal).
 * This is done recusively, pkg being the current package.
 */
private void updateRootIndex(IPackageFragmentRoot root, IPackageFragment pkg, IResourceDelta delta) {
	this.updateIndex((Openable)pkg, delta);
	IResourceDelta[] children = delta.getAffectedChildren();
	String name = pkg.getElementName();
	for (int i = 0, length = children.length; i < length; i++) {
		IResourceDelta child = children[i];
		IResource resource = child.getResource();
		if (resource instanceof IFolder) {
			String subpkgName = 
				name.length() == 0 ? 
					resource.getName() : 
					name + "." + resource.getName(); //$NON-NLS-1$
			IPackageFragment subpkg = root.getPackageFragment(subpkgName);
			this.updateRootIndex(root, subpkg, child);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7606.java