error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8668.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8668.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[16,1]

error in qdox parser
file content:
```java
offset: 886
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8668.java
text:
```scala
public static class CompilationParticipants {

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Theodora Yeung (tyeung@bea.com) - ensure that JarPackageFragmentRoot make it into cache
 *                                                           before its contents
 *                                                           (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=102422) 
 *******************************************************************************/
p@@ackage org.eclipse.jdt.internal.core;

import java.io.*;
import java.net.URI;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.content.IContentTypeManager.ContentTypeChangeEvent;
import org.eclipse.core.runtime.content.IContentTypeManager.IContentTypeChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.codeassist.CompletionEngine;
import org.eclipse.jdt.internal.codeassist.SelectionEngine;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObjectToInt;
import org.eclipse.jdt.internal.core.builder.JavaBuilder;
import org.eclipse.jdt.internal.core.hierarchy.TypeHierarchy;
import org.eclipse.jdt.internal.core.search.AbstractSearchScope;
import org.eclipse.jdt.internal.core.search.BasicSearchEngine;
import org.eclipse.jdt.internal.core.search.IRestrictedAccessTypeRequestor;
import org.eclipse.jdt.internal.core.search.JavaWorkspaceScope;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;
import org.eclipse.jdt.internal.core.search.processing.JobManager;
import org.eclipse.jdt.internal.core.util.LRUCache;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jdt.internal.core.util.Util;
import org.eclipse.jdt.internal.core.util.WeakHashSet;
import org.eclipse.jdt.internal.core.util.WeakHashSetOfCharArray;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatter;
import org.osgi.service.prefs.BackingStoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The <code>JavaModelManager</code> manages instances of <code>IJavaModel</code>.
 * <code>IElementChangedListener</code>s register with the <code>JavaModelManager</code>,
 * and receive <code>ElementChangedEvent</code>s for all <code>IJavaModel</code>s.
 * <p>
 * The single instance of <code>JavaModelManager</code> is available from
 * the static method <code>JavaModelManager.getJavaModelManager()</code>.
 */
public class JavaModelManager implements ISaveParticipant, IContentTypeChangeListener { 	
 
	/**
	 * Unique handle onto the JavaModel
	 */
	final JavaModel javaModel = new JavaModel();
	
	/**
	 * Classpath variables pool
	 */
	public HashMap variables = new HashMap(5);
	public HashSet variablesWithInitializer = new HashSet(5);
	public HashMap previousSessionVariables = new HashMap(5);
	private ThreadLocal variableInitializationInProgress = new ThreadLocal();
		
	/**
	 * Classpath containers pool
	 */
	public HashMap containers = new HashMap(5);
	public HashMap previousSessionContainers = new HashMap(5);
	private ThreadLocal containerInitializationInProgress = new ThreadLocal();
	public boolean batchContainerInitializations = false;
	public HashMap containerInitializersCache = new HashMap(5);
	
	/*
	 * A HashSet that contains the IJavaProject whose classpath is being resolved.
	 */
	private ThreadLocal classpathsBeingResolved = new ThreadLocal();
	
	/*
	 * The unique workspace scope
	 */
	public JavaWorkspaceScope workspaceScope;
	
	/*
	 * Pools of symbols used in the Java model.
	 * Used as a replacement for String#intern() that could prevent garbage collection of strings on some VMs.
	 */
	private WeakHashSet stringSymbols = new WeakHashSet(5);
	private WeakHashSetOfCharArray charArraySymbols = new WeakHashSetOfCharArray(5);

	public final static String CP_VARIABLE_PREFERENCES_PREFIX = JavaCore.PLUGIN_ID+".classpathVariable."; //$NON-NLS-1$
	public final static String CP_CONTAINER_PREFERENCES_PREFIX = JavaCore.PLUGIN_ID+".classpathContainer."; //$NON-NLS-1$
	public final static String CP_ENTRY_IGNORE = "##<cp entry ignore>##"; //$NON-NLS-1$
	public final static IPath CP_ENTRY_IGNORE_PATH = new Path(CP_ENTRY_IGNORE);
	
	private final static int VARIABLES_AND_CONTAINERS_FILE_VERSION = 2;

	/**
	 * Name of the extension point for contributing classpath variable initializers
	 */
	public static final String CPVARIABLE_INITIALIZER_EXTPOINT_ID = "classpathVariableInitializer" ; //$NON-NLS-1$

	/**
	 * Name of the extension point for contributing classpath container initializers
	 */
	public static final String CPCONTAINER_INITIALIZER_EXTPOINT_ID = "classpathContainerInitializer" ; //$NON-NLS-1$

	/**
	 * Name of the extension point for contributing a source code formatter
	 */
	public static final String FORMATTER_EXTPOINT_ID = "codeFormatter" ; //$NON-NLS-1$
	
	/**
	 * Name of the extension point for contributing a compilation participant
	 */
	public static final String COMPILATION_PARTICIPANT_EXTPOINT_ID = "compilationParticipant" ; //$NON-NLS-1$
	
	/**
	 * Special value used for recognizing ongoing initialization and breaking initialization cycles
	 */
	public final static IPath VARIABLE_INITIALIZATION_IN_PROGRESS = new Path("Variable Initialization In Progress"); //$NON-NLS-1$
	public final static IClasspathContainer CONTAINER_INITIALIZATION_IN_PROGRESS = new IClasspathContainer() {
		public IClasspathEntry[] getClasspathEntries() { return null; }
		public String getDescription() { return "Container Initialization In Progress"; } //$NON-NLS-1$
		public int getKind() { return 0; }
		public IPath getPath() { return null; }
		public String toString() { return getDescription(); }
	};
	
	private static final String BUFFER_MANAGER_DEBUG = JavaCore.PLUGIN_ID + "/debug/buffermanager" ; //$NON-NLS-1$
	private static final String INDEX_MANAGER_DEBUG = JavaCore.PLUGIN_ID + "/debug/indexmanager" ; //$NON-NLS-1$
	private static final String COMPILER_DEBUG = JavaCore.PLUGIN_ID + "/debug/compiler" ; //$NON-NLS-1$
	private static final String JAVAMODEL_DEBUG = JavaCore.PLUGIN_ID + "/debug/javamodel" ; //$NON-NLS-1$
	private static final String JAVAMODELCACHE_DEBUG = JavaCore.PLUGIN_ID + "/debug/javamodel/cache" ; //$NON-NLS-1$
	private static final String CP_RESOLVE_DEBUG = JavaCore.PLUGIN_ID + "/debug/cpresolution" ; //$NON-NLS-1$
	private static final String ZIP_ACCESS_DEBUG = JavaCore.PLUGIN_ID + "/debug/zipaccess" ; //$NON-NLS-1$
	private static final String DELTA_DEBUG =JavaCore.PLUGIN_ID + "/debug/javadelta" ; //$NON-NLS-1$
	private static final String DELTA_DEBUG_VERBOSE =JavaCore.PLUGIN_ID + "/debug/javadelta/verbose" ; //$NON-NLS-1$
	private static final String HIERARCHY_DEBUG = JavaCore.PLUGIN_ID + "/debug/hierarchy" ; //$NON-NLS-1$
	private static final String POST_ACTION_DEBUG = JavaCore.PLUGIN_ID + "/debug/postaction" ; //$NON-NLS-1$
	private static final String BUILDER_DEBUG = JavaCore.PLUGIN_ID + "/debug/builder" ; //$NON-NLS-1$
	private static final String COMPLETION_DEBUG = JavaCore.PLUGIN_ID + "/debug/completion" ; //$NON-NLS-1$
	private static final String RESOLUTION_DEBUG = JavaCore.PLUGIN_ID + "/debug/resolution" ; //$NON-NLS-1$
	private static final String SELECTION_DEBUG = JavaCore.PLUGIN_ID + "/debug/selection" ; //$NON-NLS-1$
	private static final String SEARCH_DEBUG = JavaCore.PLUGIN_ID + "/debug/search" ; //$NON-NLS-1$
	private static final String SOURCE_MAPPER_DEBUG_VERBOSE = JavaCore.PLUGIN_ID + "/debug/sourcemapper" ; //$NON-NLS-1$

	public static final String COMPLETION_PERF = JavaCore.PLUGIN_ID + "/perf/completion" ; //$NON-NLS-1$
	public static final String SELECTION_PERF = JavaCore.PLUGIN_ID + "/perf/selection" ; //$NON-NLS-1$
	public static final String DELTA_LISTENER_PERF = JavaCore.PLUGIN_ID + "/perf/javadeltalistener" ; //$NON-NLS-1$
	public static final String VARIABLE_INITIALIZER_PERF = JavaCore.PLUGIN_ID + "/perf/variableinitializer" ; //$NON-NLS-1$
	public static final String CONTAINER_INITIALIZER_PERF = JavaCore.PLUGIN_ID + "/perf/containerinitializer" ; //$NON-NLS-1$
	public static final String RECONCILE_PERF = JavaCore.PLUGIN_ID + "/perf/reconcile" ; //$NON-NLS-1$
	
	private static final String ENABLE_NEW_FORMATTER = JavaCore.PLUGIN_ID + "/formatter/enable_new" ; //$NON-NLS-1$

	private final static String INDEXED_SECONDARY_TYPES = "#@*_indexing secondary cache_*@#"; //$NON-NLS-1$

	public static boolean PERF_VARIABLE_INITIALIZER = false;
	public static boolean PERF_CONTAINER_INITIALIZER = false;
	
	public final static ICompilationUnit[] NO_WORKING_COPY = new ICompilationUnit[0];
	
	// Preferences
	HashSet optionNames = new HashSet(20);
	Hashtable optionsCache;

	public final IEclipsePreferences[] preferencesLookup = new IEclipsePreferences[2];
	static final int PREF_INSTANCE = 0;
	static final int PREF_DEFAULT = 1;

	static final Object[][] NO_PARTICIPANTS = new Object[0][];
	
	public class CompilationParticipants {
		
		private final int MAX_SOURCE_LEVEL = 6; // 1.1 to 1.6
	
		/*
		 * The registered compilation participants (a table from int (source level) to Object[])
		 * The Object array contains first IConfigurationElements when not resolved yet, then
		 * it contains CompilationParticipants.
		 */
		private Object[][] registeredParticipants = null;
		private HashSet managedMarkerTypes;
				
		public CompilationParticipant[] getCompilationParticipants(IJavaProject project) {
			final Object[][] participantsPerSource = getRegisteredParticipants();
			if (participantsPerSource == NO_PARTICIPANTS)
				return null;
			String sourceLevel = project.getOption(JavaCore.COMPILER_SOURCE, true/*inherit options*/);
			final int sourceLevelIndex = indexForSourceLevel(sourceLevel);
			final Object[] participants = participantsPerSource[sourceLevelIndex];
			int length = participants.length;
			CompilationParticipant[] result = new CompilationParticipant[length];
			int index = 0;
			for (int i = 0; i < length; i++) {
				if (participants[i] instanceof IConfigurationElement) {
					final IConfigurationElement configElement = (IConfigurationElement) participants[i];
					final int participantIndex = i;
					SafeRunner.run(new ISafeRunnable() {
						public void handleException(Throwable exception) {
							Util.log(exception, "Exception occurred while creating compilation participant"); //$NON-NLS-1$
						}
						public void run() throws Exception {
							Object executableExtension = configElement.createExecutableExtension("class"); //$NON-NLS-1$ 
							for (int j = sourceLevelIndex; j < MAX_SOURCE_LEVEL; j++)
								participantsPerSource[j][participantIndex] = executableExtension;
						}
					});
				} 
				CompilationParticipant participant = (CompilationParticipant) participants[i];
				if (participant != null && participant.isActive(project))
					result[index++] = participant;
			}
			if (index == 0)
				return null;
			if (index < length)
				System.arraycopy(result, 0, result = new CompilationParticipant[index], 0, index);
			return result;
		}
		
		public HashSet managedMarkerTypes() {
			if (this.managedMarkerTypes == null) {
				// force extension points to be read
				getRegisteredParticipants();
			}
			return this.managedMarkerTypes;
		}
		
		private synchronized Object[][] getRegisteredParticipants() {
			if (this.registeredParticipants != null) {
				return this.registeredParticipants;
			}
			this.managedMarkerTypes = new HashSet();
			IExtensionPoint extension = Platform.getExtensionRegistry().getExtensionPoint(JavaCore.PLUGIN_ID, COMPILATION_PARTICIPANT_EXTPOINT_ID);
			if (extension == null)
				return this.registeredParticipants = NO_PARTICIPANTS;
			final ArrayList modifyingEnv = new ArrayList();
			final ArrayList creatingProblems = new ArrayList();
			final ArrayList others = new ArrayList();
			IExtension[] extensions = extension.getExtensions();
			// for all extensions of this point...
			for(int i = 0; i < extensions.length; i++) {
				IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				// for all config elements named "compilationParticipant"
				for(int j = 0; j < configElements.length; j++) {
					final IConfigurationElement configElement = configElements[j];
					String elementName =configElement.getName();
					if (!("compilationParticipant".equals(elementName))) { //$NON-NLS-1$
						continue;
					}
					// add config element in the group it belongs to
					if ("true".equals(configElement.getAttribute("modifiesEnvironment"))) //$NON-NLS-1$ //$NON-NLS-2$
						modifyingEnv.add(configElement);
					else if ("true".equals(configElement.getAttribute("createsProblems"))) //$NON-NLS-1$ //$NON-NLS-2$
						creatingProblems.add(configElement);
					else
						others.add(configElement);
					// add managed marker types
					IConfigurationElement[] managedMarkers = configElement.getChildren("managedMarker"); //$NON-NLS-1$
					for (int k = 0, length = managedMarkers.length; k < length; k++) {
						IConfigurationElement element = managedMarkers[k];
						String markerType = element.getAttribute("markerType"); //$NON-NLS-1$
						if (markerType != null)
							this.managedMarkerTypes.add(markerType);
					}
				}
			}
			int size = modifyingEnv.size() + creatingProblems.size() + others.size();
			if (size == 0)
				return this.registeredParticipants = NO_PARTICIPANTS;
			
			// sort config elements in each group
			IConfigurationElement[] configElements = new IConfigurationElement[size];
			int index = 0;
			index = sortParticipants(modifyingEnv, configElements, index);
			index = sortParticipants(creatingProblems, configElements, index);
			index = sortParticipants(others, configElements, index);
			
			// create result table
			Object[][] result = new Object[MAX_SOURCE_LEVEL][];
			int length = configElements.length;
			for (int i = 0; i < MAX_SOURCE_LEVEL; i++) {
				result[i] = new Object[length];
			}
			for (int i = 0; i < length; i++) {
				String sourceLevel = configElements[i].getAttribute("requiredSourceLevel"); //$NON-NLS-1$
				int sourceLevelIndex = indexForSourceLevel(sourceLevel);
				for (int j = sourceLevelIndex; j < MAX_SOURCE_LEVEL; j++) {
					result[j][i] = configElements[i];
				}
			}
			return this.registeredParticipants = result;
		}
		
		/*
		 * 1.1 -> 0
		 * 1.2 -> 1
		 * ...
		 * 1.6 -> 5
		 * null -> 0
		 */
		private int indexForSourceLevel(String sourceLevel) {
			if (sourceLevel == null) return 0;
			int majVersion = (int) (CompilerOptions.versionToJdkLevel(sourceLevel) >>> 16);
			switch (majVersion) {
				case ClassFileConstants.MAJOR_VERSION_1_2:
					return 1;
				case ClassFileConstants.MAJOR_VERSION_1_3:
					return 2;
				case ClassFileConstants.MAJOR_VERSION_1_4:
					return 3;
				case ClassFileConstants.MAJOR_VERSION_1_5:
					return 4;
				case ClassFileConstants.MAJOR_VERSION_1_6:
					return 5;
				default:
					// all other cases including ClassFileConstants.MAJOR_VERSION_1_1
					return 0;
			}
		}
		
		private int sortParticipants(ArrayList group, IConfigurationElement[] configElements, int index) {
			int size = group.size();
			if (size == 0) return index;
			Object[] elements = group.toArray();
			Util.sort(elements, new Util.Comparer() {
				public int compare(Object a, Object b) {
					if (a == b) return 0;
					String id = ((IConfigurationElement) a).getAttribute("id"); //$NON-NLS-1$
					if (id == null) return -1;
					IConfigurationElement[] requiredElements = ((IConfigurationElement) b).getChildren("requires"); //$NON-NLS-1$
					for (int i = 0, length = requiredElements.length; i < length; i++) {
						IConfigurationElement required = requiredElements[i];
						if (id.equals(required.getAttribute("id"))) //$NON-NLS-1$
							return 1;
					}
					return -1;
				}
			});
			for (int i = 0; i < size; i++)
				configElements[index+i] = (IConfigurationElement) elements[i];
			return index + size;
		}
	}
			
	public final CompilationParticipants compilationParticipants = new CompilationParticipants();
	
	/**
	 * Returns whether the given full path (for a package) conflicts with the output location
	 * of the given project.
	 */
	public static boolean conflictsWithOutputLocation(IPath folderPath, JavaProject project) {
		try {
			IPath outputLocation = project.getOutputLocation();
			if (outputLocation == null) {
				// in doubt, there is a conflict
				return true;
			}
			if (outputLocation.isPrefixOf(folderPath)) {
				// only allow nesting in project's output if there is a corresponding source folder
				// or if the project's output is not used (in other words, if all source folders have their custom output)
				IClasspathEntry[] classpath = project.getResolvedClasspath();
				boolean isOutputUsed = false;
				for (int i = 0, length = classpath.length; i < length; i++) {
					IClasspathEntry entry = classpath[i];
					if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
						if (entry.getPath().equals(outputLocation)) {
							return false;
						}
						if (entry.getOutputLocation() == null) {
							isOutputUsed = true;
						}
					}
				}
				return isOutputUsed;
			}
			return false;
		} catch (JavaModelException e) {
			// in doubt, there is a conflict
			return true;
		}
	}

	public synchronized IClasspathContainer containerGet(IJavaProject project, IPath containerPath) {	
		// check initialization in progress first
		HashSet projectInitializations = containerInitializationInProgress(project);
		if (projectInitializations.contains(containerPath)) {
			return CONTAINER_INITIALIZATION_IN_PROGRESS;
		}
		
		Map projectContainers = (Map)this.containers.get(project);
		if (projectContainers == null){
			return null;
		}
		IClasspathContainer container = (IClasspathContainer)projectContainers.get(containerPath);
		return container;
	}
	
	private synchronized Map containerClone(IJavaProject project) {
		Map originalProjectContainers = (Map)this.containers.get(project);
		if (originalProjectContainers == null) return null;
		Map projectContainers = new HashMap(originalProjectContainers.size());
		projectContainers.putAll(originalProjectContainers);
		return projectContainers;
	}
	
	/*
	 * Returns the set of container paths for the given project that are being initialized in the current thread.
	 */
	private HashSet containerInitializationInProgress(IJavaProject project) {
		Map initializations = (Map)this.containerInitializationInProgress.get();
		if (initializations == null) {
			initializations = new HashMap();
			this.containerInitializationInProgress.set(initializations);
		}
		HashSet projectInitializations = (HashSet)initializations.get(project);
		if (projectInitializations == null) {
			projectInitializations = new HashSet();
			initializations.put(project, projectInitializations);
		}
		return projectInitializations;
	}

	public synchronized void containerPut(IJavaProject project, IPath containerPath, IClasspathContainer container){

		// set/unset the initialization in progress
		if (container == CONTAINER_INITIALIZATION_IN_PROGRESS) {
			HashSet projectInitializations = containerInitializationInProgress(project);
			projectInitializations.add(containerPath);
			
			// do not write out intermediate initialization value
			return;
		} else {
			containerRemoveInitializationInProgress(project, containerPath);

			Map projectContainers = (Map)this.containers.get(project);	
 			if (projectContainers == null){
				projectContainers = new HashMap(1);
				this.containers.put(project, projectContainers);
			}
	
			if (container == null) {
				projectContainers.remove(containerPath);
			} else {
  				projectContainers.put(containerPath, container);
			}
			// discard obsoleted information about previous session
			Map previousContainers = (Map)this.previousSessionContainers.get(project);
			if (previousContainers != null){
				previousContainers.remove(containerPath);
			}
		}
		// container values are persisted in preferences during save operations, see #saving(ISaveContext)
	}
	
	/*
	 * The given project is being removed. Remove all containers for this project from the cache.
	 */
	public synchronized void containerRemove(IJavaProject project) {
		Map initializations = (Map) this.containerInitializationInProgress.get();
		if (initializations != null) {
			initializations.remove(project);
		}
		this.containers.remove(project);
	}
	
	/*
	 * Optimize startup case where a container for 1 project is initialized at a time with the same entries as on shutdown.
	 */
	public boolean containerPutIfInitializingWithSameEntries(IPath containerPath, IJavaProject[] projects, IClasspathContainer[] respectiveContainers) {
		int projectLength = projects.length;
		if (projectLength != 1) 
			return false;
		final IClasspathContainer container = respectiveContainers[0];
		if (container == null)
			return false;
		IJavaProject project = projects[0];
		if (!containerInitializationInProgress(project).contains(containerPath))
			return false;
		IClasspathContainer previousSessionContainer = getPreviousSessionContainer(containerPath, project);
		final IClasspathEntry[] newEntries = container.getClasspathEntries();
		if (previousSessionContainer == null) 
			if (newEntries.length == 0) {
				containerPut(project, containerPath, container);
				return true;
			} else {
				return false;
			}
		final IClasspathEntry[] oldEntries = previousSessionContainer.getClasspathEntries();
		if (oldEntries.length != newEntries.length) 
			return false;
		for (int i = 0, length = newEntries.length; i < length; i++) {
			if (!newEntries[i].equals(oldEntries[i])) {
				if (CP_RESOLVE_VERBOSE) {
					Util.verbose(
						"CPContainer SET  - missbehaving container\n" + //$NON-NLS-1$
						"	container path: " + containerPath + '\n' + //$NON-NLS-1$
						"	projects: {" +//$NON-NLS-1$
						org.eclipse.jdt.internal.compiler.util.Util.toString(
							projects, 
							new org.eclipse.jdt.internal.compiler.util.Util.Displayable(){ 
								public String displayString(Object o) { return ((IJavaProject) o).getElementName(); }
							}) +
						"}\n	values on previous session: {\n"  +//$NON-NLS-1$
						org.eclipse.jdt.internal.compiler.util.Util.toString(
							respectiveContainers, 
							new org.eclipse.jdt.internal.compiler.util.Util.Displayable(){ 
								public String displayString(Object o) { 
									StringBuffer buffer = new StringBuffer("		"); //$NON-NLS-1$
									if (o == null) {
										buffer.append("<null>"); //$NON-NLS-1$
										return buffer.toString();
									}
									buffer.append(container.getDescription());
									buffer.append(" {\n"); //$NON-NLS-1$
									for (int j = 0; j < oldEntries.length; j++){
										buffer.append(" 			"); //$NON-NLS-1$
										buffer.append(oldEntries[j]); 
										buffer.append('\n'); 
									}
									buffer.append(" 		}"); //$NON-NLS-1$
									return buffer.toString();
								}
							}) +
						"}\n	new values: {\n"  +//$NON-NLS-1$
						org.eclipse.jdt.internal.compiler.util.Util.toString(
							respectiveContainers, 
							new org.eclipse.jdt.internal.compiler.util.Util.Displayable(){ 
								public String displayString(Object o) { 
									StringBuffer buffer = new StringBuffer("		"); //$NON-NLS-1$
									if (o == null) {
										buffer.append("<null>"); //$NON-NLS-1$
										return buffer.toString();
									}
									buffer.append(container.getDescription());
									buffer.append(" {\n"); //$NON-NLS-1$
									for (int j = 0; j < newEntries.length; j++){
										buffer.append(" 			"); //$NON-NLS-1$
										buffer.append(newEntries[j]); 
										buffer.append('\n'); 
									}
									buffer.append(" 		}"); //$NON-NLS-1$
									return buffer.toString();
								}
							}) +
						"\n	}"); //$NON-NLS-1$
				}
				return false;
			}
		}
		containerPut(project, containerPath, container);
		return true;
	}
	
	private void containerRemoveInitializationInProgress(IJavaProject project, IPath containerPath) {
		HashSet projectInitializations = containerInitializationInProgress(project);
		projectInitializations.remove(containerPath);
		if (projectInitializations.size() == 0) {
			Map initializations = (Map)this.containerInitializationInProgress.get();
			initializations.remove(project);
		}
	}
	
	private synchronized void containersReset(String[] containerIDs) {
		for (int i = 0; i < containerIDs.length; i++) {
			String containerID = containerIDs[i];
			Iterator projectIterator = this.containers.values().iterator();
			while (projectIterator.hasNext()){
				Map projectContainers = (Map) projectIterator.next();
				if (projectContainers != null){
					Iterator containerIterator = projectContainers.keySet().iterator();
					while (containerIterator.hasNext()){
						IPath containerPath = (IPath)containerIterator.next();
						if (containerPath.segment(0).equals(containerID)) { // registered container
							projectContainers.put(containerPath, null); // reset container value, but leave entry in Map
						}
					}
				}
			}
		}
	}

	/**
	 * Returns the Java element corresponding to the given resource, or
	 * <code>null</code> if unable to associate the given resource
	 * with a Java element.
	 * <p>
	 * The resource must be one of:<ul>
	 *	<li>a project - the element returned is the corresponding <code>IJavaProject</code></li>
	 *	<li>a <code>.java</code> file - the element returned is the corresponding <code>ICompilationUnit</code></li>
	 *	<li>a <code>.class</code> file - the element returned is the corresponding <code>IClassFile</code></li>
	 *	<li>a <code>.jar</code> file - the element returned is the corresponding <code>IPackageFragmentRoot</code></li>
	 *  <li>a folder - the element returned is the corresponding <code>IPackageFragmentRoot</code>
	 *			or <code>IPackageFragment</code></li>
	 *  <li>the workspace root resource - the element returned is the <code>IJavaModel</code></li>
	 *	</ul>
	 * <p>
	 * Creating a Java element has the side effect of creating and opening all of the
	 * element's parents if they are not yet open.
	 */
	public static IJavaElement create(IResource resource, IJavaProject project) {
		if (resource == null) {
			return null;
		}
		int type = resource.getType();
		switch (type) {
			case IResource.PROJECT :
				return JavaCore.create((IProject) resource);
			case IResource.FILE :
				return create((IFile) resource, project);
			case IResource.FOLDER :
				return create((IFolder) resource, project);
			case IResource.ROOT :
				return JavaCore.create((IWorkspaceRoot) resource);
			default :
				return null;
		}
	}

	/**
	 * Returns the Java element corresponding to the given file, its project being the given
	 * project.
	 * Returns <code>null</code> if unable to associate the given file
	 * with a Java element.
	 *
	 * <p>The file must be one of:<ul>
	 *	<li>a <code>.java</code> file - the element returned is the corresponding <code>ICompilationUnit</code></li>
	 *	<li>a <code>.class</code> file - the element returned is the corresponding <code>IClassFile</code></li>
	 *	<li>a <code>.jar</code> file - the element returned is the corresponding <code>IPackageFragmentRoot</code></li>
	 *	</ul>
	 * <p>
	 * Creating a Java element has the side effect of creating and opening all of the
	 * element's parents if they are not yet open.
	 */
	public static IJavaElement create(IFile file, IJavaProject project) {
		if (file == null) {
			return null;
		}
		if (project == null) {
			project = JavaCore.create(file.getProject());
		}
	
		if (file.getFileExtension() != null) {
			String name = file.getName();
			if (org.eclipse.jdt.internal.core.util.Util.isJavaLikeFileName(name))
				return createCompilationUnitFrom(file, project);
			if (org.eclipse.jdt.internal.compiler.util.Util.isClassFileName(name))
				return createClassFileFrom(file, project);
			if (org.eclipse.jdt.internal.compiler.util.Util.isArchiveFileName(name))
				return createJarPackageFragmentRootFrom(file, project);
		}
		return null;
	}

	/**
	 * Returns the package fragment or package fragment root corresponding to the given folder,
	 * its parent or great parent being the given project. 
	 * or <code>null</code> if unable to associate the given folder with a Java element.
	 * <p>
	 * Note that a package fragment root is returned rather than a default package.
	 * <p>
	 * Creating a Java element has the side effect of creating and opening all of the
	 * element's parents if they are not yet open.
	 */
	public static IJavaElement create(IFolder folder, IJavaProject project) {
		if (folder == null) {
			return null;
		}
		IJavaElement element;
		if (project == null) {
			project = JavaCore.create(folder.getProject());
			element = determineIfOnClasspath(folder, project);
			if (element == null) {
				// walk all projects and find one that have the given folder on its classpath
				IJavaProject[] projects;
				try {
					projects = JavaModelManager.getJavaModelManager().getJavaModel().getJavaProjects();
				} catch (JavaModelException e) {
					return null;
				}
				for (int i = 0, length = projects.length; i < length; i++) {
					project = projects[i];
					element = determineIfOnClasspath(folder, project);
					if (element != null)
						break;
				}
			}
		} else {
			element = determineIfOnClasspath(folder, project);
		}
		
		if (conflictsWithOutputLocation(folder.getFullPath(), (JavaProject)project)
 (folder.getName().indexOf('.') >= 0 
		 		&& !(element instanceof IPackageFragmentRoot))) {
			return null; // only package fragment roots are allowed with dot names
		} else {
			return element;
		}
	}

	/**
	 * Creates and returns a class file element for the given <code>.class</code> file,
	 * its project being the given project. Returns <code>null</code> if unable
	 * to recognize the class file.
	 */
	public static IClassFile createClassFileFrom(IFile file, IJavaProject project ) {
		if (file == null) {
			return null;
		}
		if (project == null) {
			project = JavaCore.create(file.getProject());
		}
		IPackageFragment pkg = (IPackageFragment) determineIfOnClasspath(file, project);
		if (pkg == null) {
			// fix for 1FVS7WE
			// not on classpath - make the root its folder, and a default package
			PackageFragmentRoot root = (PackageFragmentRoot) project.getPackageFragmentRoot(file.getParent());
			pkg = root.getPackageFragment(CharOperation.NO_STRINGS);
		}
		return pkg.getClassFile(file.getName());
	}
	
	/**
	 * Creates and returns a compilation unit element for the given <code>.java</code> 
	 * file, its project being the given project. Returns <code>null</code> if unable
	 * to recognize the compilation unit.
	 */
	public static ICompilationUnit createCompilationUnitFrom(IFile file, IJavaProject project) {

		if (file == null) return null;

		if (project == null) {
			project = JavaCore.create(file.getProject());
		}
		IPackageFragment pkg = (IPackageFragment) determineIfOnClasspath(file, project);
		if (pkg == null) {
			// not on classpath - make the root its folder, and a default package
			IPackageFragmentRoot root = project.getPackageFragmentRoot(file.getParent());
			pkg = root.getPackageFragment(IPackageFragment.DEFAULT_PACKAGE_NAME);
			
			if (VERBOSE){
				System.out.println("WARNING : creating unit element outside classpath ("+ Thread.currentThread()+"): " + file.getFullPath()); //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		return pkg.getCompilationUnit(file.getName());
	}
	
	/**
	 * Creates and returns a handle for the given JAR file, its project being the given project.
	 * The Java model associated with the JAR's project may be
	 * created as a side effect. 
	 * Returns <code>null</code> if unable to create a JAR package fragment root.
	 * (for example, if the JAR file represents a non-Java resource)
	 */
	public static IPackageFragmentRoot createJarPackageFragmentRootFrom(IFile file, IJavaProject project) {
		if (file == null) {
			return null;
		}
		if (project == null) {
			project = JavaCore.create(file.getProject());
		}
	
		// Create a jar package fragment root only if on the classpath
		IPath resourcePath = file.getFullPath();
		try {
			IClasspathEntry[] entries = ((JavaProject)project).getResolvedClasspath();
			for (int i = 0, length = entries.length; i < length; i++) {
				IClasspathEntry entry = entries[i];
				IPath rootPath = entry.getPath();
				if (rootPath.equals(resourcePath)) {
					return project.getPackageFragmentRoot(file);
				}
			}
		} catch (JavaModelException e) {
			// project doesn't exist: return null
		}
		return null;
	}
	
	/**
	 * Returns the package fragment root represented by the resource, or
	 * the package fragment the given resource is located in, or <code>null</code>
	 * if the given resource is not on the classpath of the given project.
	 */
	public static IJavaElement determineIfOnClasspath(
		IResource resource,
		IJavaProject project) {
			
		IPath resourcePath = resource.getFullPath();
		try {
			IClasspathEntry[] entries = 
				org.eclipse.jdt.internal.core.util.Util.isJavaLikeFileName(resourcePath.lastSegment())
					? project.getRawClasspath() // JAVA file can only live inside SRC folder (on the raw path)
					: ((JavaProject)project).getResolvedClasspath();
			
			int length	= entries.length;
			if (length > 0) {
				String sourceLevel = project.getOption(JavaCore.COMPILER_SOURCE, true);
				String complianceLevel = project.getOption(JavaCore.COMPILER_COMPLIANCE, true);
				for (int i = 0; i < length; i++) {
					IClasspathEntry entry = entries[i];
					if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) continue;
					IPath rootPath = entry.getPath();
					if (rootPath.equals(resourcePath)) {
						return project.getPackageFragmentRoot(resource);
					} else if (rootPath.isPrefixOf(resourcePath)) {
						// allow creation of package fragment if it contains a .java file that is included
						if (!Util.isExcluded(resource, ((ClasspathEntry)entry).fullInclusionPatternChars(), ((ClasspathEntry)entry).fullExclusionPatternChars())) {
							// given we have a resource child of the root, it cannot be a JAR pkg root
							PackageFragmentRoot root =(PackageFragmentRoot) ((JavaProject) project).getFolderPackageFragmentRoot(rootPath);
							if (root == null) return null;
							IPath pkgPath = resourcePath.removeFirstSegments(rootPath.segmentCount());
	
							if (resource.getType() == IResource.FILE) {
								// if the resource is a file, then remove the last segment which
								// is the file name in the package
								pkgPath = pkgPath.removeLastSegments(1);
							}
							String[] pkgName = pkgPath.segments();
							if (pkgName.length != 0 && JavaConventions.validatePackageName(Util.packageName(pkgPath, sourceLevel, complianceLevel), sourceLevel, complianceLevel).getSeverity() == IStatus.ERROR) {
								return null;
							}
							return root.getPackageFragment(pkgName);
						}
					}
				}
			}
		} catch (JavaModelException npe) {
			return null;
		}
		return null;
	}
	
	/**
	 * The singleton manager
	 */
	private static JavaModelManager MANAGER= new JavaModelManager();

	/**
	 * Infos cache.
	 */
	private JavaModelCache cache;
	
	/*
	 * Temporary cache of newly opened elements
	 */
	private ThreadLocal temporaryCache = new ThreadLocal();

	/**
	 * Set of elements which are out of sync with their buffers.
	 */
	protected HashSet elementsOutOfSynchWithBuffers = new HashSet(11);
	
	/**
	 * Holds the state used for delta processing.
	 */
	public DeltaProcessingState deltaState = new DeltaProcessingState();

	public IndexManager indexManager = null;
	
	/**
	 * Table from IProject to PerProjectInfo.
	 * NOTE: this object itself is used as a lock to synchronize creation/removal of per project infos
	 */
	protected Map perProjectInfos = new HashMap(5);
	
	/**
	 * Table from WorkingCopyOwner to a table of ICompilationUnit (working copy handle) to PerWorkingCopyInfo.
	 * NOTE: this object itself is used as a lock to synchronize creation/removal of per working copy infos
	 */
	protected Map perWorkingCopyInfos = new HashMap(5);
	
	/**
	 * A weak set of the known search scopes.
	 */
	protected WeakHashMap searchScopes = new WeakHashMap();

	public static class PerProjectInfo {
		private static final int JAVADOC_CACHE_INITIAL_SIZE = 10;
		
		public IProject project;
		public Object savedState;
		public boolean triedRead;
		public IClasspathEntry[] rawClasspath;
		public IJavaModelStatus rawClasspathStatus;
		public IClasspathEntry[] resolvedClasspath;
		public IJavaModelStatus unresolvedEntryStatus;
		public Map resolvedPathToRawEntries; // reverse map from resolved path to raw entries
		public IPath outputLocation;
		
		public IEclipsePreferences preferences;
		public Hashtable options;
		public Hashtable secondaryTypes;
		public LRUCache javadocCache;
		
		public PerProjectInfo(IProject project) {

			this.triedRead = false;
			this.savedState = null;
			this.project = project;
			this.javadocCache = new LRUCache(JAVADOC_CACHE_INITIAL_SIZE);
		}
		
		public void rememberExternalLibTimestamps() {
			IClasspathEntry[] classpath = this.resolvedClasspath;
			if (classpath == null) return;
			IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
			Map externalTimeStamps = JavaModelManager.getJavaModelManager().deltaState.getExternalLibTimeStamps();
			for (int i = 0, length = classpath.length; i < length; i++) {
				IClasspathEntry entry = classpath[i];
				if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					IPath path = entry.getPath();
					if (externalTimeStamps.get(path) == null) {
						Object target = JavaModel.getTarget(wRoot, path, true);
						if (target instanceof java.io.File) {
							long timestamp = DeltaProcessor.getTimeStamp((java.io.File)target);
							externalTimeStamps.put(path, new Long(timestamp));							
						}
					}
				}
			}
		}
		
		public synchronized void resetResolvedClasspath() {
			// null out resolved information
			setClasspath(this.rawClasspath, this.outputLocation, this.rawClasspathStatus, null, null, null);
		}
		
		public synchronized void setClasspath(IClasspathEntry[] newRawClasspath, IPath newOutputLocation, IJavaModelStatus newRawClasspathStatus, IClasspathEntry[] newResolvedClasspath, Map newResolvedPathToRawEntries, IJavaModelStatus newUnresolvedEntryStatus) {
			// remember old info
			JavaModelManager manager = JavaModelManager.getJavaModelManager();
			DeltaProcessor deltaProcessor = manager.deltaState.getDeltaProcessor();
			deltaProcessor.addClasspathChange(this.project, this.rawClasspath, this.outputLocation, this.resolvedClasspath);
			
			this.rawClasspath = newRawClasspath;
			this.outputLocation = newOutputLocation;
			this.rawClasspathStatus = newRawClasspathStatus;
			this.resolvedClasspath = newResolvedClasspath;
			this.resolvedPathToRawEntries = newResolvedPathToRawEntries;
			this.unresolvedEntryStatus = newUnresolvedEntryStatus;
			this.javadocCache = new LRUCache(JAVADOC_CACHE_INITIAL_SIZE);
		}
		
		/*
		 * Reads the raw classpath and output location from disk, and remember them.
		 * Return the raw classpath, or JavaProject#INVALID_CLASSPATH if unable to read it.
		 */
		public synchronized IClasspathEntry[] readAndCacheClasspath(JavaProject javaProject) {
			// read file entries and update status
			IClasspathEntry[] classpath;
			IJavaModelStatus status;
			try {
				classpath = javaProject.readFileEntriesWithException(null/*not interested in unknown elements*/);
				status = JavaModelStatus.VERIFIED_OK;
			} catch (CoreException e) {
				classpath = JavaProject.INVALID_CLASSPATH;
				status = 
					new JavaModelStatus(
						IJavaModelStatusConstants.INVALID_CLASSPATH_FILE_FORMAT,
						Messages.bind(Messages.classpath_cannotReadClasspathFile, javaProject.getElementName()));
			} catch (IOException e) {
				classpath = JavaProject.INVALID_CLASSPATH;
				if (Messages.file_badFormat.equals(e.getMessage()))
					status = 
						new JavaModelStatus(
							IJavaModelStatusConstants.INVALID_CLASSPATH_FILE_FORMAT,
							Messages.bind(Messages.classpath_xmlFormatError, javaProject.getElementName(), Messages.file_badFormat));
				else				
					status = 
						new JavaModelStatus(
							IJavaModelStatusConstants.INVALID_CLASSPATH_FILE_FORMAT,
							Messages.bind(Messages.classpath_cannotReadClasspathFile, javaProject.getElementName()));
			} catch (AssertionFailedException e) {
				classpath = JavaProject.INVALID_CLASSPATH;
				status =  
					new JavaModelStatus(
						IJavaModelStatusConstants.INVALID_CLASSPATH_FILE_FORMAT,
						Messages.bind(Messages.classpath_illegalEntryInClasspathFile, new String[] {javaProject.getElementName(), e.getMessage()}));
			}
		
			// extract out the output location
			IPath output = null;
			if (classpath.length > 0) {
				IClasspathEntry entry = classpath[classpath.length - 1];
				if (entry.getContentKind() == ClasspathEntry.K_OUTPUT) {
					output = entry.getPath();
					IClasspathEntry[] copy = new IClasspathEntry[classpath.length - 1];
					System.arraycopy(classpath, 0, copy, 0, copy.length);
					classpath = copy;
				}
			}
			
			// store new raw classpath, new output and new status, and null out resolved info
			setClasspath(classpath, output, status, null, null, null);
			
			return classpath;
		}
		
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Info for "); //$NON-NLS-1$
			buffer.append(this.project.getFullPath());
			buffer.append("\nRaw classpath:\n"); //$NON-NLS-1$
			if (this.rawClasspath == null) {
				buffer.append("  <null>\n"); //$NON-NLS-1$
			} else {
				for (int i = 0, length = this.rawClasspath.length; i < length; i++) {
					buffer.append("  "); //$NON-NLS-1$
					buffer.append(this.rawClasspath[i]);
					buffer.append('\n');
				}
			}
			buffer.append("Resolved classpath:\n"); //$NON-NLS-1$
			IClasspathEntry[] resolvedCP = this.resolvedClasspath;
			if (resolvedCP == null) {
				buffer.append("  <null>\n"); //$NON-NLS-1$
			} else {
				for (int i = 0, length = resolvedCP.length; i < length; i++) {
					buffer.append("  "); //$NON-NLS-1$
					buffer.append(resolvedCP[i]);
					buffer.append('\n');
				}
			}
			buffer.append("Output location:\n  "); //$NON-NLS-1$
			if (this.outputLocation == null) {
				buffer.append("<null>"); //$NON-NLS-1$
			} else {
				buffer.append(this.outputLocation);
			}
			return buffer.toString();
		}
	}
	
	public static class PerWorkingCopyInfo implements IProblemRequestor {
		int useCount = 0;
		IProblemRequestor problemRequestor;
		ICompilationUnit workingCopy;
		public PerWorkingCopyInfo(ICompilationUnit workingCopy, IProblemRequestor problemRequestor) {
			this.workingCopy = workingCopy;
			this.problemRequestor = problemRequestor;
		}
		public void acceptProblem(IProblem problem) {
			if (this.problemRequestor == null) return;
			this.problemRequestor.acceptProblem(problem);
		}
		public void beginReporting() {
			if (this.problemRequestor == null) return;
			this.problemRequestor.beginReporting();
		}
		public void endReporting() {
			if (this.problemRequestor == null) return;
			this.problemRequestor.endReporting();
		}
		public ICompilationUnit getWorkingCopy() {
			return this.workingCopy;
		}
		public boolean isActive() {
			return this.problemRequestor != null && this.problemRequestor.isActive();
		}
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Info for "); //$NON-NLS-1$
			buffer.append(((JavaElement)this.workingCopy).toStringWithAncestors());
			buffer.append("\nUse count = "); //$NON-NLS-1$
			buffer.append(this.useCount);
			buffer.append("\nProblem requestor:\n  "); //$NON-NLS-1$
			buffer.append(this.problemRequestor);
			return buffer.toString();
		}
	}
	
	public static boolean VERBOSE = false;
	public static boolean CP_RESOLVE_VERBOSE = false;
	public static boolean ZIP_ACCESS_VERBOSE = false;
	
	/**
	 * A cache of opened zip files per thread.
	 * (for a given thread, the object value is a HashMap from IPath to java.io.ZipFile)
	 */
	private ThreadLocal zipFiles = new ThreadLocal();
	
	
	/**
	 * Update the classpath variable cache
	 */
	public static class EclipsePreferencesListener implements IEclipsePreferences.IPreferenceChangeListener {
		/**
		 * @see org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener#preferenceChange(org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent)
		 */
		public void preferenceChange(IEclipsePreferences.PreferenceChangeEvent event) {
			String propertyName = event.getKey();
			if (propertyName.startsWith(CP_VARIABLE_PREFERENCES_PREFIX)) {
				String varName = propertyName.substring(CP_VARIABLE_PREFERENCES_PREFIX.length());
				JavaModelManager manager = getJavaModelManager();
				if (manager.variablesWithInitializer.contains(varName)) {
					// revert preference value as we will not apply it to JavaCore classpath variable
					String oldValue = (String) event.getOldValue();
					if (oldValue == null) {
						// unexpected old value => remove variable from set
						manager.variablesWithInitializer.remove(varName);
					} else {
						manager.getInstancePreferences().put(varName, oldValue);
					}
				} else {
					String newValue = (String)event.getNewValue();
					IPath newPath;
					if (newValue != null && !(newValue = newValue.trim()).equals(CP_ENTRY_IGNORE)) {
						newPath = new Path(newValue);
					} else {
						newPath = null;
					}
					try {
						SetVariablesOperation operation = new SetVariablesOperation(new String[] {varName}, new IPath[] {newPath}, false/*don't update preferences*/);
						operation.runOperation(null/*no progress available*/);
					} catch (JavaModelException e) {
						Util.log(e, "Could not set classpath variable " + varName + " to " + newPath); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
			if (propertyName.startsWith(CP_CONTAINER_PREFERENCES_PREFIX)) {
				recreatePersistedContainer(propertyName, (String)event.getNewValue(), false);
			}
		}
	}

	/**
	 * Constructs a new JavaModelManager
	 */
	private JavaModelManager() {
		// singleton: prevent others from creating a new instance
		if (Platform.isRunning()) this.indexManager = new IndexManager();
	}

	/**
	 * @deprecated
	 */
	private void addDeprecatedOptions(Hashtable options) {
		options.put(JavaCore.COMPILER_PB_INVALID_IMPORT, JavaCore.ERROR);		
		options.put(JavaCore.COMPILER_PB_UNREACHABLE_CODE, JavaCore.ERROR);
	}

	/**
	 * Starts caching ZipFiles.
	 * Ignores if there are already clients.
	 */
	public void cacheZipFiles() {
		if (this.zipFiles.get() != null) return;
		this.zipFiles.set(new HashMap());
	}
	public void closeZipFile(ZipFile zipFile) {
		if (zipFile == null) return;
		if (this.zipFiles.get() != null) {
			return; // zip file will be closed by call to flushZipFiles
		}
		try {
			if (JavaModelManager.ZIP_ACCESS_VERBOSE) {
				System.out.println("(" + Thread.currentThread() + ") [JavaModelManager.closeZipFile(ZipFile)] Closing ZipFile on " +zipFile.getName()); //$NON-NLS-1$	//$NON-NLS-2$
			}
			zipFile.close();
		} catch (IOException e) {
			// problem occured closing zip file: cannot do much more
		}
	}

	/**
	 * Configure the plugin with respect to option settings defined in ".options" file
	 */
	public void configurePluginDebugOptions(){
		if(JavaCore.getPlugin().isDebugging()){
			String option = Platform.getDebugOption(BUFFER_MANAGER_DEBUG);
			if(option != null) BufferManager.VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$
			
			option = Platform.getDebugOption(BUILDER_DEBUG);
			if(option != null) JavaBuilder.DEBUG = option.equalsIgnoreCase("true") ; //$NON-NLS-1$
			
			option = Platform.getDebugOption(COMPILER_DEBUG);
			if(option != null) Compiler.DEBUG = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(COMPLETION_DEBUG);
			if(option != null) CompletionEngine.DEBUG = option.equalsIgnoreCase("true") ; //$NON-NLS-1$
			
			option = Platform.getDebugOption(CP_RESOLVE_DEBUG);
			if(option != null) JavaModelManager.CP_RESOLVE_VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(DELTA_DEBUG);
			if(option != null) DeltaProcessor.DEBUG = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(DELTA_DEBUG_VERBOSE);
			if(option != null) DeltaProcessor.VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(HIERARCHY_DEBUG);
			if(option != null) TypeHierarchy.DEBUG = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(INDEX_MANAGER_DEBUG);
			if(option != null) JobManager.VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$
			
			option = Platform.getDebugOption(JAVAMODEL_DEBUG);
			if(option != null) JavaModelManager.VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(JAVAMODELCACHE_DEBUG);
			if(option != null) JavaModelCache.VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(POST_ACTION_DEBUG);
			if(option != null) JavaModelOperation.POST_ACTION_VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(RESOLUTION_DEBUG);
			if(option != null) NameLookup.VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(SEARCH_DEBUG);
			if(option != null) BasicSearchEngine.VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(SELECTION_DEBUG);
			if(option != null) SelectionEngine.DEBUG = option.equalsIgnoreCase("true") ; //$NON-NLS-1$

			option = Platform.getDebugOption(ZIP_ACCESS_DEBUG);
			if(option != null) JavaModelManager.ZIP_ACCESS_VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$
			
			option = Platform.getDebugOption(SOURCE_MAPPER_DEBUG_VERBOSE);
			if(option != null) SourceMapper.VERBOSE = option.equalsIgnoreCase("true") ; //$NON-NLS-1$
			
			option = Platform.getDebugOption(ENABLE_NEW_FORMATTER);
			if(option != null) DefaultCodeFormatter.USE_NEW_FORMATTER = option.equalsIgnoreCase("true") ; //$NON-NLS-1$
		}
		
		// configure performance options
		if(PerformanceStats.ENABLED) {
			CompletionEngine.PERF = PerformanceStats.isEnabled(COMPLETION_PERF);
			SelectionEngine.PERF = PerformanceStats.isEnabled(SELECTION_PERF);
			DeltaProcessor.PERF = PerformanceStats.isEnabled(DELTA_LISTENER_PERF);
			JavaModelManager.PERF_VARIABLE_INITIALIZER = PerformanceStats.isEnabled(VARIABLE_INITIALIZER_PERF);
			JavaModelManager.PERF_CONTAINER_INITIALIZER = PerformanceStats.isEnabled(CONTAINER_INITIALIZER_PERF);
			ReconcileWorkingCopyOperation.PERF = PerformanceStats.isEnabled(RECONCILE_PERF);
		}
	}
	
	/*
	 * Discards the per working copy info for the given working copy (making it a compilation unit)
	 * if its use count was 1. Otherwise, just decrement the use count.
	 * If the working copy is primary, computes the delta between its state and the original compilation unit
	 * and register it.
	 * Close the working copy, its buffer and remove it from the shared working copy table.
	 * Ignore if no per-working copy info existed.
	 * NOTE: it must NOT be synchronized as it may interact with the element info cache (if useCount is decremented to 0), see bug 50667.
	 * Returns the new use count (or -1 if it didn't exist).
	 */
	public int discardPerWorkingCopyInfo(CompilationUnit workingCopy) throws JavaModelException {
		
		// create the delta builder (this remembers the current content of the working copy)
		// outside the perWorkingCopyInfos lock (see bug 50667)
		JavaElementDeltaBuilder deltaBuilder = null;
		if (workingCopy.isPrimary() && workingCopy.hasUnsavedChanges()) {
			deltaBuilder = new JavaElementDeltaBuilder(workingCopy);
		}
		PerWorkingCopyInfo info = null;
		synchronized(this.perWorkingCopyInfos) {
			WorkingCopyOwner owner = workingCopy.owner;
			Map workingCopyToInfos = (Map)this.perWorkingCopyInfos.get(owner);
			if (workingCopyToInfos == null) return -1;
			
			info = (PerWorkingCopyInfo)workingCopyToInfos.get(workingCopy);
			if (info == null) return -1;
			
			if (--info.useCount == 0) {
				// remove per working copy info
				workingCopyToInfos.remove(workingCopy);
				if (workingCopyToInfos.isEmpty()) {
					this.perWorkingCopyInfos.remove(owner);
				}
			}
		}
		if (info.useCount == 0) { // info cannot be null here (check was done above)
			// remove infos + close buffer (since no longer working copy)
			// outside the perWorkingCopyInfos lock (see bug 50667)
			removeInfoAndChildren(workingCopy);
			workingCopy.closeBuffer();

			// compute the delta if needed and register it if there are changes
			if (deltaBuilder != null) {
				deltaBuilder.buildDeltas();
				if (deltaBuilder.delta != null) {
					getDeltaProcessor().registerJavaModelDelta(deltaBuilder.delta);
				}
			}
		}
		return info.useCount;
	}
	
	/**
	 * @see ISaveParticipant
	 */
	public void doneSaving(ISaveContext context){
		// nothing to do for jdt.core
	}

	/**
	 * Flushes ZipFiles cache if there are no more clients.
	 */
	public void flushZipFiles() {
		Thread currentThread = Thread.currentThread();
		HashMap map = (HashMap)this.zipFiles.get();
		if (map == null) return;
		this.zipFiles.set(null);
		Iterator iterator = map.values().iterator();
		while (iterator.hasNext()) {
			try {
				ZipFile zipFile = (ZipFile)iterator.next();
				if (JavaModelManager.ZIP_ACCESS_VERBOSE) {
					System.out.println("(" + currentThread + ") [JavaModelManager.flushZipFiles()] Closing ZipFile on " +zipFile.getName()); //$NON-NLS-1$//$NON-NLS-2$
				}
				zipFile.close();
			} catch (IOException e) {
				// problem occured closing zip file: cannot do much more
			}
		}
	}
	
	public IClasspathContainer getClasspathContainer(final IPath containerPath, final IJavaProject project) throws JavaModelException {

		IClasspathContainer container = containerGet(project, containerPath);

		if (container == null) {
			if (this.batchContainerInitializations) {
				// avoid deep recursion while initializaing container on workspace restart
				// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=60437)
				this.batchContainerInitializations = false;
				container = initializeAllContainers(project, containerPath);
			} else {
				container = initializeContainer(project, containerPath);
			}
			if (container == null) { // initializer failed to do its job: redirect to the failure container
				ClasspathContainerInitializer initializer = JavaCore.getClasspathContainerInitializer(containerPath.segment(0));
				if (initializer == null) {
					// create a dummy initializer and get the default failure container
					container = (new ClasspathContainerInitializer() {
						public void initialize(IPath path, IJavaProject javaProject) throws CoreException {
							// not used
						}
					}).getFailureContainer(containerPath, project);
				} else {
					container = initializer.getFailureContainer(containerPath, project);
				}
				if (container != null)
					containerPut(project, containerPath, container);
			}
		}
		return container;			
	}

	public DeltaProcessor getDeltaProcessor() {
		return this.deltaState.getDeltaProcessor();
	}
	
	/** 
	 * Returns the set of elements which are out of synch with their buffers.
	 */
	protected HashSet getElementsOutOfSynchWithBuffers() {
		return this.elementsOutOfSynchWithBuffers;
	}

	public IndexManager getIndexManager() {
		return this.indexManager;
	}

	/**
	 *  Returns the info for the element.
	 */
	public synchronized Object getInfo(IJavaElement element) {
		HashMap tempCache = (HashMap)this.temporaryCache.get();
		if (tempCache != null) {
			Object result = tempCache.get(element);
			if (result != null) {
				return result;
			}
		}
		return this.cache.getInfo(element);
	}

	/**
	 * Get workpsace eclipse preference for JavaCore plugin.
	 */
	public IEclipsePreferences getInstancePreferences() {
		return preferencesLookup[PREF_INSTANCE];
	}
 
	// If modified, also modify the method getDefaultOptionsNoInitialization()
	public Hashtable getDefaultOptions(){

		Hashtable defaultOptions = new Hashtable(10);

		// see JavaCorePreferenceInitializer#initializeDefaultPluginPreferences() for changing default settings
		// If modified, also modify the method getDefaultOptionsNoInitialization()
		IEclipsePreferences defaultPreferences = getDefaultPreferences();
		
		// initialize preferences to their default
		Iterator iterator = this.optionNames.iterator();
		while (iterator.hasNext()) {
		    String propertyName = (String) iterator.next();
		    String value = defaultPreferences.get(propertyName, null);
		    if (value != null) defaultOptions.put(propertyName, value);
		}
		// get encoding through resource plugin
		defaultOptions.put(JavaCore.CORE_ENCODING, JavaCore.getEncoding());
		// backward compatibility
		addDeprecatedOptions(defaultOptions);
		
		return defaultOptions;
	}
	
	/**
	 * Get default eclipse preference for JavaCore plugin.
	 */
	public IEclipsePreferences getDefaultPreferences() {
		return preferencesLookup[PREF_DEFAULT];
	}

	/**
	 * Returns the handle to the active Java Model.
	 */
	public final JavaModel getJavaModel() {
		return this.javaModel;
	}

	/**
	 * Returns the singleton JavaModelManager
	 */
	public final static JavaModelManager getJavaModelManager() {
		return MANAGER;
	}

	/**
	 * Returns the last built state for the given project, or null if there is none.
	 * Deserializes the state if necessary.
	 *
	 * For use by image builder and evaluation support only
	 */
	public Object getLastBuiltState(IProject project, IProgressMonitor monitor) {
		if (!JavaProject.hasJavaNature(project)) {
			if (JavaBuilder.DEBUG)
				System.out.println(project + " is not a Java project"); //$NON-NLS-1$
			return null; // should never be requested on non-Java projects
		}
		PerProjectInfo info = getPerProjectInfo(project, true/*create if missing*/);
		if (!info.triedRead) {
			info.triedRead = true;
			try {
				if (monitor != null)
					monitor.subTask(Messages.bind(Messages.build_readStateProgress, project.getName())); 
				info.savedState = readState(project);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return info.savedState;
	}

	public String getOption(String optionName) {
		
		if (JavaCore.CORE_ENCODING.equals(optionName)){
			return JavaCore.getEncoding();
		}
		// backward compatibility
		if (isDeprecatedOption(optionName)) {
			return JavaCore.ERROR;
		}
		String propertyName = optionName;
		if (this.optionNames.contains(propertyName)){
			IPreferencesService service = Platform.getPreferencesService();
			String value =  service.get(optionName, null, this.preferencesLookup);
			return value==null ? null : value.trim();
		}
		return null;
	}
	
	public Hashtable getOptions() {

		// return cached options if already computed
		if (this.optionsCache != null) return new Hashtable(this.optionsCache);

		if (!Platform.isRunning()) {
			return this.optionsCache = getDefaultOptionsNoInitialization();
		}
		// init
		Hashtable options = new Hashtable(10);
		IPreferencesService service = Platform.getPreferencesService();

		// set options using preferences service lookup
		Iterator iterator = optionNames.iterator();
		while (iterator.hasNext()) {
		    String propertyName = (String) iterator.next();
		    String propertyValue = service.get(propertyName, null, this.preferencesLookup);
		    if (propertyValue != null) {
			    options.put(propertyName, propertyValue);
		    }
		}

		// get encoding through resource plugin
		options.put(JavaCore.CORE_ENCODING, JavaCore.getEncoding()); 

		// backward compatibility
		addDeprecatedOptions(options);

		// store built map in cache
		this.optionsCache = new Hashtable(options);

		// return built map
		return options;
	}

	// Do not modify without modifying getDefaultOptions()
	private Hashtable getDefaultOptionsNoInitialization() {
		Map defaultOptionsMap = new CompilerOptions().getMap(); // compiler defaults
		
		// Override some compiler defaults
		defaultOptionsMap.put(JavaCore.COMPILER_LOCAL_VARIABLE_ATTR, JavaCore.GENERATE);
		defaultOptionsMap.put(JavaCore.COMPILER_CODEGEN_UNUSED_LOCAL, JavaCore.PRESERVE);
		defaultOptionsMap.put(JavaCore.COMPILER_TASK_TAGS, JavaCore.DEFAULT_TASK_TAGS);
		defaultOptionsMap.put(JavaCore.COMPILER_TASK_PRIORITIES, JavaCore.DEFAULT_TASK_PRIORITIES);
		defaultOptionsMap.put(JavaCore.COMPILER_TASK_CASE_SENSITIVE, JavaCore.ENABLED);
		defaultOptionsMap.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
		defaultOptionsMap.put(JavaCore.COMPILER_PB_FORBIDDEN_REFERENCE, JavaCore.ERROR);
		
		// Builder settings
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_RESOURCE_COPY_FILTER, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_INVALID_CLASSPATH, JavaCore.ABORT); 
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_DUPLICATE_RESOURCE, JavaCore.WARNING); 
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_CLEAN_OUTPUT_FOLDER, JavaCore.CLEAN); 

		// JavaCore settings
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_ORDER, JavaCore.IGNORE); 
		defaultOptionsMap.put(JavaCore.CORE_INCOMPLETE_CLASSPATH, JavaCore.ERROR); 
		defaultOptionsMap.put(JavaCore.CORE_CIRCULAR_CLASSPATH, JavaCore.ERROR); 
		defaultOptionsMap.put(JavaCore.CORE_INCOMPATIBLE_JDK_LEVEL, JavaCore.IGNORE); 
		defaultOptionsMap.put(JavaCore.CORE_ENABLE_CLASSPATH_EXCLUSION_PATTERNS, JavaCore.ENABLED); 
		defaultOptionsMap.put(JavaCore.CORE_ENABLE_CLASSPATH_MULTIPLE_OUTPUT_LOCATIONS, JavaCore.ENABLED); 

		// Formatter settings
		defaultOptionsMap.putAll(DefaultCodeFormatterConstants.getEclipseDefaultSettings());

		// CodeAssist settings
		defaultOptionsMap.put(JavaCore.CODEASSIST_VISIBILITY_CHECK, JavaCore.DISABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_DEPRECATION_CHECK, JavaCore.DISABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_IMPLICIT_QUALIFICATION, JavaCore.DISABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_FIELD_PREFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_STATIC_FIELD_PREFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_LOCAL_PREFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_ARGUMENT_PREFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_FIELD_SUFFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_STATIC_FIELD_SUFFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_LOCAL_SUFFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_ARGUMENT_SUFFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_FORBIDDEN_REFERENCE_CHECK, JavaCore.ENABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_DISCOURAGED_REFERENCE_CHECK, JavaCore.DISABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_CAMEL_CASE_MATCH, JavaCore.ENABLED);
		
		// Time out for parameter names
		defaultOptionsMap.put(JavaCore.TIMEOUT_FOR_PARAMETER_NAME_FROM_ATTACHED_JAVADOC, "50"); //$NON-NLS-1$
		
		return new Hashtable(defaultOptionsMap);
	}
	
	/*
	 * Returns the per-project info for the given project. If specified, create the info if the info doesn't exist.
	 */
	public PerProjectInfo getPerProjectInfo(IProject project, boolean create) {
		synchronized(this.perProjectInfos) { // use the perProjectInfo collection as its own lock
			PerProjectInfo info= (PerProjectInfo) this.perProjectInfos.get(project);
			if (info == null && create) {
				info= new PerProjectInfo(project);
				this.perProjectInfos.put(project, info);
			}
			return info;
		}
	}	
	
	/*
	 * Returns  the per-project info for the given project.
	 * If the info doesn't exist, check for the project existence and create the info.
	 * @throws JavaModelException if the project doesn't exist.
	 */
	public PerProjectInfo getPerProjectInfoCheckExistence(IProject project) throws JavaModelException {
		JavaModelManager.PerProjectInfo info = getPerProjectInfo(project, false /* don't create info */);
		if (info == null) {
			if (!JavaProject.hasJavaNature(project)) {
				throw ((JavaProject)JavaCore.create(project)).newNotPresentException();
			}
			info = getPerProjectInfo(project, true /* create info */);
		}
		return info;
	}
	
	/*
	 * Returns the per-working copy info for the given working copy at the given path.
	 * If it doesn't exist and if create, add a new per-working copy info with the given problem requestor.
	 * If recordUsage, increment the per-working copy info's use count.
	 * Returns null if it doesn't exist and not create.
	 */
	public PerWorkingCopyInfo getPerWorkingCopyInfo(CompilationUnit workingCopy,boolean create, boolean recordUsage, IProblemRequestor problemRequestor) {
		synchronized(this.perWorkingCopyInfos) { // use the perWorkingCopyInfo collection as its own lock
			WorkingCopyOwner owner = workingCopy.owner;
			Map workingCopyToInfos = (Map)this.perWorkingCopyInfos.get(owner);
			if (workingCopyToInfos == null && create) {
				workingCopyToInfos = new HashMap();
				this.perWorkingCopyInfos.put(owner, workingCopyToInfos);
			}

			PerWorkingCopyInfo info = workingCopyToInfos == null ? null : (PerWorkingCopyInfo) workingCopyToInfos.get(workingCopy);
			if (info == null && create) {
				info= new PerWorkingCopyInfo(workingCopy, problemRequestor);
				workingCopyToInfos.put(workingCopy, info);
			}
			if (info != null && recordUsage) info.useCount++;
			return info;
		}
	}	

	/**
	 * Returns a persisted container from previous session if any. Note that it is not the original container from previous
	 * session (i.e. it did not get serialized) but rather a summary of its entries recreated for CP initialization purpose.
	 * As such it should not be stored into container caches.
	 */
	public IClasspathContainer getPreviousSessionContainer(IPath containerPath, IJavaProject project) {
			Map previousContainerValues = (Map)this.previousSessionContainers.get(project);
			if (previousContainerValues != null){
			    IClasspathContainer previousContainer = (IClasspathContainer)previousContainerValues.get(containerPath);
			    if (previousContainer != null) {
					if (JavaModelManager.CP_RESOLVE_VERBOSE){
						StringBuffer buffer = new StringBuffer();
						buffer.append("CPContainer INIT - reentering access to project container during its initialization, will see previous value\n"); //$NON-NLS-1$ 
						buffer.append("	project: " + project.getElementName() + '\n'); //$NON-NLS-1$
						buffer.append("	container path: " + containerPath + '\n'); //$NON-NLS-1$
						buffer.append("	previous value: "); //$NON-NLS-1$
						buffer.append(previousContainer.getDescription());
						buffer.append(" {\n"); //$NON-NLS-1$
						IClasspathEntry[] entries = previousContainer.getClasspathEntries();
						if (entries != null){
							for (int j = 0; j < entries.length; j++){
								buffer.append(" 		"); //$NON-NLS-1$
								buffer.append(entries[j]); 
								buffer.append('\n'); 
							}
						}
						buffer.append(" 	}"); //$NON-NLS-1$
						Util.verbose(buffer.toString());
						new Exception("<Fake exception>").printStackTrace(System.out); //$NON-NLS-1$
					}			    
					return previousContainer;
			    }
			}
		    return null; // break cycle if none found
	}
	
	/**
	 * Returns a persisted container from previous session if any
	 */
	public IPath getPreviousSessionVariable(String variableName) {
		IPath previousPath = (IPath)this.previousSessionVariables.get(variableName);
		if (previousPath != null){
			if (CP_RESOLVE_VERBOSE){
				Util.verbose(
					"CPVariable INIT - reentering access to variable during its initialization, will see previous value\n" + //$NON-NLS-1$
					"	variable: "+ variableName + '\n' + //$NON-NLS-1$
					"	previous value: " + previousPath); //$NON-NLS-1$
				new Exception("<Fake exception>").printStackTrace(System.out); //$NON-NLS-1$
			}
			return previousPath;
		}
	    return null; // break cycle
	}

	/**
	 * Returns the temporary cache for newly opened elements for the current thread.
	 * Creates it if not already created.
	 */
	public HashMap getTemporaryCache() {
		HashMap result = (HashMap)this.temporaryCache.get();
		if (result == null) {
			result = new HashMap();
			this.temporaryCache.set(result);
		}
		return result;
	}
	
	private File getVariableAndContainersFile() {
		return JavaCore.getPlugin().getStateLocation().append("variablesAndContainers.dat").toFile(); //$NON-NLS-1$
	}

	/**
 	 * Returns the name of the variables for which an CP variable initializer is registered through an extension point
 	 */
	public static String[] getRegisteredVariableNames(){
		
		Plugin jdtCorePlugin = JavaCore.getPlugin();
		if (jdtCorePlugin == null) return null;

		ArrayList variableList = new ArrayList(5);
		IExtensionPoint extension = Platform.getExtensionRegistry().getExtensionPoint(JavaCore.PLUGIN_ID, JavaModelManager.CPVARIABLE_INITIALIZER_EXTPOINT_ID);
		if (extension != null) {
			IExtension[] extensions =  extension.getExtensions();
			for(int i = 0; i < extensions.length; i++){
				IConfigurationElement [] configElements = extensions[i].getConfigurationElements();
				for(int j = 0; j < configElements.length; j++){
					String varAttribute = configElements[j].getAttribute("variable"); //$NON-NLS-1$
					if (varAttribute != null) variableList.add(varAttribute);
				}
			}	
		}
		String[] variableNames = new String[variableList.size()];
		variableList.toArray(variableNames);
		return variableNames;
	}	

	/**
 	 * Returns the name of the container IDs for which an CP container initializer is registered through an extension point
 	 */
	public static String[] getRegisteredContainerIDs(){
		
		Plugin jdtCorePlugin = JavaCore.getPlugin();
		if (jdtCorePlugin == null) return null;

		ArrayList containerIDList = new ArrayList(5);
		IExtensionPoint extension = Platform.getExtensionRegistry().getExtensionPoint(JavaCore.PLUGIN_ID, JavaModelManager.CPCONTAINER_INITIALIZER_EXTPOINT_ID);
		if (extension != null) {
			IExtension[] extensions =  extension.getExtensions();
			for(int i = 0; i < extensions.length; i++){
				IConfigurationElement [] configElements = extensions[i].getConfigurationElements();
				for(int j = 0; j < configElements.length; j++){
					String idAttribute = configElements[j].getAttribute("id"); //$NON-NLS-1$
					if (idAttribute != null) containerIDList.add(idAttribute);
				}
			}	
		}
		String[] containerIDs = new String[containerIDList.size()];
		containerIDList.toArray(containerIDs);
		return containerIDs;
	}	

	/**
	 * Returns the File to use for saving and restoring the last built state for the given project.
	 */
	private File getSerializationFile(IProject project) {
		if (!project.exists()) return null;
		IPath workingLocation = project.getWorkingLocation(JavaCore.PLUGIN_ID);
		return workingLocation.append("state.dat").toFile(); //$NON-NLS-1$
	}
	
	/*
	 * Returns all the working copies which have the given owner.
	 * Adds the working copies of the primary owner if specified.
	 * Returns null if it has none.
	 */
	public ICompilationUnit[] getWorkingCopies(WorkingCopyOwner owner, boolean addPrimary) {
		synchronized(this.perWorkingCopyInfos) {
			ICompilationUnit[] primaryWCs = addPrimary && owner != DefaultWorkingCopyOwner.PRIMARY 
				? getWorkingCopies(DefaultWorkingCopyOwner.PRIMARY, false) 
				: null;
			Map workingCopyToInfos = (Map)this.perWorkingCopyInfos.get(owner);
			if (workingCopyToInfos == null) return primaryWCs;
			int primaryLength = primaryWCs == null ? 0 : primaryWCs.length;
			int size = workingCopyToInfos.size(); // note size is > 0 otherwise pathToPerWorkingCopyInfos would be null
			ICompilationUnit[] result = new ICompilationUnit[primaryLength + size];
			int index = 0;
			if (primaryWCs != null) {
				for (int i = 0; i < primaryLength; i++) {
					ICompilationUnit primaryWorkingCopy = primaryWCs[i];
					ICompilationUnit workingCopy = new CompilationUnit((PackageFragment) primaryWorkingCopy.getParent(), primaryWorkingCopy.getElementName(), owner);
					if (!workingCopyToInfos.containsKey(workingCopy))
						result[index++] = primaryWorkingCopy;
				}
				if (index != primaryLength)
					System.arraycopy(result, 0, result = new ICompilationUnit[index+size], 0, index);
			}
			Iterator iterator = workingCopyToInfos.values().iterator();
			while(iterator.hasNext()) {
				result[index++] = ((JavaModelManager.PerWorkingCopyInfo)iterator.next()).getWorkingCopy();
			}
			return result;
		}		
	}
	
	public JavaWorkspaceScope getWorkspaceScope() {
		if (this.workspaceScope == null) {
			this.workspaceScope = new JavaWorkspaceScope();
		}
		return this.workspaceScope;
	}
	
	/**
	 * Returns the open ZipFile at the given path. If the ZipFile
	 * does not yet exist, it is created, opened, and added to the cache
	 * of open ZipFiles. 
	 * 
	 * The path must be a file system path if representing an external 
	 * zip/jar, or it must be an absolute workspace relative path if 
	 * representing a zip/jar inside the workspace.
	 *
	 * @exception CoreException If unable to create/open the ZipFile
	 */
	public ZipFile getZipFile(IPath path) throws CoreException {
			
		HashMap map;
		ZipFile zipFile;
		if ((map = (HashMap)this.zipFiles.get()) != null 
				&& (zipFile = (ZipFile)map.get(path)) != null) {
				
			return zipFile;
		}
		File localFile = null;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource file = root.findMember(path);
		if (file != null) {
			// internal resource
			URI location;
			if (file.getType() != IResource.FILE || (location = file.getLocationURI()) == null) {
				throw new CoreException(new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, -1, Messages.bind(Messages.file_notFound, path.toString()), null)); 
			}
			localFile = Util.toLocalFile(location, null/*no progress availaible*/);
			if (localFile == null)
				throw new CoreException(new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, -1, Messages.bind(Messages.file_notFound, path.toString()), null)); 
		} else {
			// external resource -> it is ok to use toFile()
			localFile= path.toFile();
		}

		try {
			if (ZIP_ACCESS_VERBOSE) {
				System.out.println("(" + Thread.currentThread() + ") [JavaModelManager.getZipFile(IPath)] Creating ZipFile on " + localFile ); //$NON-NLS-1$ //$NON-NLS-2$
			}
			zipFile = new ZipFile(localFile);
			if (map != null) {
				map.put(path, zipFile);
			}
			return zipFile;
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, -1, Messages.status_IOException, e)); 
		}
	}
	
	/*
	 * Returns whether there is a temporary cache for the current thread.
	 */
	public boolean hasTemporaryCache() {
		return this.temporaryCache.get() != null;
	}
	
	/*
	 * Initialize all container at the same time as the given container.
	 * Return the container for the given path and project.
	 */
	private IClasspathContainer initializeAllContainers(IJavaProject javaProjectToInit, IPath containerToInit) throws JavaModelException {
		if (CP_RESOLVE_VERBOSE) {
			Util.verbose(
				"CPContainer INIT - batching containers initialization\n" + //$NON-NLS-1$
				"	project to init: " + javaProjectToInit.getElementName() + '\n' + //$NON-NLS-1$
				"	container path to init: " + containerToInit); //$NON-NLS-1$
		}

		// collect all container paths
		final HashMap allContainerPaths = new HashMap();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0, length = projects.length; i < length; i++) {
			IProject project = projects[i];
			if (!JavaProject.hasJavaNature(project)) continue;
			IJavaProject javaProject = new JavaProject(project, getJavaModel());
			HashSet paths = null;
			IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
			for (int j = 0, length2 = rawClasspath.length; j < length2; j++) {
				IClasspathEntry entry = rawClasspath[j];
				IPath path = entry.getPath();
				if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER
						&& containerGet(javaProject, path) == null) {
					if (paths == null) {
						paths = new HashSet();
						allContainerPaths.put(javaProject, paths);
					}
					paths.add(path);
				}
			}
			/* TODO (frederic) put back when JDT/UI dummy project will be thrown away...
			 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=97524
			 *
			if (javaProject.equals(javaProjectToInit)) {
				if (paths == null) {
					paths = new HashSet();
					allContainerPaths.put(javaProject, paths);
				}
				paths.add(containerToInit);
			}
			*/
		}
		// TODO (frederic) remove following block when JDT/UI dummy project will be thrown away...
		HashSet containerPaths = (HashSet) allContainerPaths.get(javaProjectToInit);
		if (containerPaths == null) {
			containerPaths = new HashSet();
			allContainerPaths.put(javaProjectToInit, containerPaths);
		}
		containerPaths.add(containerToInit);
		// end block
		
		// mark all containers as being initialized
		this.containerInitializationInProgress.set(allContainerPaths);
		
		// initialize all containers
		boolean ok = false;
		try {
			// if possible run inside an IWokspaceRunnable with AVOID_UPATE to avoid unwanted builds
			// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=118507)
			IWorkspaceRunnable runnable = 				
				new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor) throws CoreException {
						Set entrySet = allContainerPaths.entrySet();
						int length = entrySet.size();
						Map.Entry[] entries = new Map.Entry[length]; // clone as the following will have a side effect
						entrySet.toArray(entries);
						for (int i = 0; i < length; i++) {
							Map.Entry entry = entries[i];
							IJavaProject javaProject = (IJavaProject) entry.getKey();
							HashSet pathSet = (HashSet) entry.getValue();
							if (pathSet == null) continue;
							int length2 = pathSet.size();
							IPath[] paths = new IPath[length2];
							pathSet.toArray(paths); // clone as the following will have a side effect
							for (int j = 0; j < length2; j++) {
								IPath path = paths[j];
								initializeContainer(javaProject, path);
							}
						}
					}
				};
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			if (workspace.isTreeLocked())
				runnable.run(null/*no progress available*/);
			else
				workspace.run(
					runnable,
					null/*don't take any lock*/,
					IWorkspace.AVOID_UPDATE,
					null/*no progress available here*/);
			ok = true;
		} catch (CoreException e) {
			// ignore
			Util.log(e, "Exception while initializing all containers"); //$NON-NLS-1$
		} finally {
			if (!ok) { 
				// if we're being traversed by an exception, ensure that that containers are 
				// no longer marked as initialization in progress
				// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=66437)
				this.containerInitializationInProgress.set(null);
			}
		}
		
		return containerGet(javaProjectToInit, containerToInit);
	}

	IClasspathContainer initializeContainer(IJavaProject project, IPath containerPath) throws JavaModelException {

		IClasspathContainer container = null;
		final ClasspathContainerInitializer initializer = JavaCore.getClasspathContainerInitializer(containerPath.segment(0));
		if (initializer != null){
			if (CP_RESOLVE_VERBOSE){
				Util.verbose(
					"CPContainer INIT - triggering initialization\n" + //$NON-NLS-1$
					"	project: " + project.getElementName() + '\n' + //$NON-NLS-1$
					"	container path: " + containerPath + '\n' + //$NON-NLS-1$
					"	initializer: " + initializer + '\n' + //$NON-NLS-1$
					"	invocation stack trace:"); //$NON-NLS-1$
				new Exception("<Fake exception>").printStackTrace(System.out); //$NON-NLS-1$
			}
			PerformanceStats stats = null;
			if(JavaModelManager.PERF_CONTAINER_INITIALIZER) {
				stats = PerformanceStats.getStats(JavaModelManager.CONTAINER_INITIALIZER_PERF, this);
				stats.startRun(containerPath + " of " + project.getPath()); //$NON-NLS-1$
			}
			containerPut(project, containerPath, CONTAINER_INITIALIZATION_IN_PROGRESS); // avoid initialization cycles
			boolean ok = false;
			try {
				// let OperationCanceledException go through
				// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=59363)
				initializer.initialize(containerPath, project);
				
				// retrieve value (if initialization was successful)
				container = containerGet(project, containerPath);
				if (container == CONTAINER_INITIALIZATION_IN_PROGRESS) return null; // break cycle
				ok = true;
			} catch (CoreException e) {
				if (e instanceof JavaModelException) {
					throw (JavaModelException) e;
				} else {
					throw new JavaModelException(e);
				}
			} catch (RuntimeException e) {
				if (JavaModelManager.CP_RESOLVE_VERBOSE) {
					e.printStackTrace();
				}
				throw e;
			} catch (Error e) {
				if (JavaModelManager.CP_RESOLVE_VERBOSE) {
					e.printStackTrace();
				}
				throw e;
			} finally {
				if(JavaModelManager.PERF_CONTAINER_INITIALIZER) {
					stats.endRun();
				}
				if (!ok) {
					// just remove initialization in progress and keep previous session container so as to avoid a full build
					// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=92588
					containerRemoveInitializationInProgress(project, containerPath); 
					if (CP_RESOLVE_VERBOSE) {
						if (container == CONTAINER_INITIALIZATION_IN_PROGRESS) {
							Util.verbose(
								"CPContainer INIT - FAILED (initializer did not initialize container)\n" + //$NON-NLS-1$
								"	project: " + project.getElementName() + '\n' + //$NON-NLS-1$
								"	container path: " + containerPath + '\n' + //$NON-NLS-1$
								"	initializer: " + initializer); //$NON-NLS-1$
							
						} else {
							Util.verbose(
								"CPContainer INIT - FAILED (see exception above)\n" + //$NON-NLS-1$
								"	project: " + project.getElementName() + '\n' + //$NON-NLS-1$
								"	container path: " + containerPath + '\n' + //$NON-NLS-1$
								"	initializer: " + initializer); //$NON-NLS-1$
						}
					}
				}
			}
			if (CP_RESOLVE_VERBOSE){
				StringBuffer buffer = new StringBuffer();
				buffer.append("CPContainer INIT - after resolution\n"); //$NON-NLS-1$
				buffer.append("	project: " + project.getElementName() + '\n'); //$NON-NLS-1$
				buffer.append("	container path: " + containerPath + '\n'); //$NON-NLS-1$
				if (container != null){
					buffer.append("	container: "+container.getDescription()+" {\n"); //$NON-NLS-2$//$NON-NLS-1$
					IClasspathEntry[] entries = container.getClasspathEntries();
					if (entries != null){
						for (int i = 0; i < entries.length; i++){
							buffer.append("		" + entries[i] + '\n'); //$NON-NLS-1$
						}
					}
					buffer.append("	}");//$NON-NLS-1$
				} else {
					buffer.append("	container: {unbound}");//$NON-NLS-1$
				}
				Util.verbose(buffer.toString());
			}
		} else {
			if (CP_RESOLVE_VERBOSE){
				Util.verbose(
					"CPContainer INIT - no initializer found\n" + //$NON-NLS-1$
					"	project: " + project.getElementName() + '\n' + //$NON-NLS-1$
					"	container path: " + containerPath); //$NON-NLS-1$
			}
		}
		return container;
	}

	/**
	 * Initialize preferences lookups for JavaCore plugin.
	 */
	public void initializePreferences() {
		
		// Create lookups
		preferencesLookup[PREF_INSTANCE] = ((IScopeContext) new InstanceScope()).getNode(JavaCore.PLUGIN_ID);
		preferencesLookup[PREF_DEFAULT] = ((IScopeContext) new DefaultScope()).getNode(JavaCore.PLUGIN_ID);

		// Listen to instance preferences node removal from parent in order to refresh stored one
		IEclipsePreferences.INodeChangeListener listener = new IEclipsePreferences.INodeChangeListener() {
			public void added(IEclipsePreferences.NodeChangeEvent event) {
				// do nothing
			}
			public void removed(IEclipsePreferences.NodeChangeEvent event) {
				if (event.getChild() == preferencesLookup[PREF_INSTANCE]) {
					preferencesLookup[PREF_INSTANCE] = ((IScopeContext) new InstanceScope()).getNode(JavaCore.PLUGIN_ID);
					preferencesLookup[PREF_INSTANCE].addPreferenceChangeListener(new EclipsePreferencesListener());
				}
			}
		};
		((IEclipsePreferences) preferencesLookup[PREF_INSTANCE].parent()).addNodeChangeListener(listener);
		preferencesLookup[PREF_INSTANCE].addPreferenceChangeListener(new EclipsePreferencesListener());

		// Listen to default preferences node removal from parent in order to refresh stored one
		listener = new IEclipsePreferences.INodeChangeListener() {
			public void added(IEclipsePreferences.NodeChangeEvent event) {
				// do nothing
			}
			public void removed(IEclipsePreferences.NodeChangeEvent event) {
				if (event.getChild() == preferencesLookup[PREF_DEFAULT]) {
					preferencesLookup[PREF_DEFAULT] = ((IScopeContext) new DefaultScope()).getNode(JavaCore.PLUGIN_ID);
				}
			}
		};
		((IEclipsePreferences) preferencesLookup[PREF_DEFAULT].parent()).addNodeChangeListener(listener);
	}

	public synchronized char[] intern(char[] array) {
		return this.charArraySymbols.add(array);
	}
	
	public synchronized String intern(String s) {
		// make sure to copy the string (so that it doesn't hold on the underlying char[] that might be much bigger than necessary)
		return (String) this.stringSymbols.add(new String(s));
		
		// Note1: String#intern() cannot be used as on some VMs this prevents the string from being garbage collected
		// Note 2: Instead of using a WeakHashset, one could use a WeakHashMap with the following implementation
		// 			   This would costs more per entry (one Entry object and one WeakReference more))
		
		/*
		WeakReference reference = (WeakReference) this.symbols.get(s);
		String existing;
		if (reference != null && (existing = (String) reference.get()) != null)
			return existing;
		this.symbols.put(s, new WeakReference(s));
		return s;
		*/	
	}
	
	private HashSet getClasspathBeingResolved() {
	    HashSet result = (HashSet) this.classpathsBeingResolved.get();
	    if (result == null) {
	        result = new HashSet();
	        this.classpathsBeingResolved.set(result);
	    }
	    return result;
	}
	
	public boolean isClasspathBeingResolved(IJavaProject project) {
	    return getClasspathBeingResolved().contains(project);
	}
	
	/**
	 * @deprecated
	 */
	private boolean isDeprecatedOption(String optionName) {
		return JavaCore.COMPILER_PB_INVALID_IMPORT.equals(optionName)
 JavaCore.COMPILER_PB_UNREACHABLE_CODE.equals(optionName);
	}
	
	public void setClasspathBeingResolved(IJavaProject project, boolean classpathIsResolved) {
	    if (classpathIsResolved) {
	        getClasspathBeingResolved().add(project);
	    } else {
	        getClasspathBeingResolved().remove(project);
	    }
	}

	public void loadVariablesAndContainers() throws CoreException {
		// backward compatibility, consider persistent property	
		QualifiedName qName = new QualifiedName(JavaCore.PLUGIN_ID, "variables"); //$NON-NLS-1$
		String xmlString = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(qName);
		
		try {
			if (xmlString != null){
				StringReader reader = new StringReader(xmlString);
				Element cpElement;
				try {
					DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					cpElement = parser.parse(new InputSource(reader)).getDocumentElement();
				} catch(SAXException e) {
					return;
				} catch(ParserConfigurationException e){
					return;
				} finally {
					reader.close();
				}
				if (cpElement == null) return;
				if (!cpElement.getNodeName().equalsIgnoreCase("variables")) { //$NON-NLS-1$
					return;
				}
				
				NodeList list= cpElement.getChildNodes();
				int length= list.getLength();
				for (int i= 0; i < length; ++i) {
					Node node= list.item(i);
					short type= node.getNodeType();
					if (type == Node.ELEMENT_NODE) {
						Element element= (Element) node;
						if (element.getNodeName().equalsIgnoreCase("variable")) { //$NON-NLS-1$
							variablePut( 
								element.getAttribute("name"), //$NON-NLS-1$
								new Path(element.getAttribute("path"))); //$NON-NLS-1$
						}
					}
				}
			}
		} catch(IOException e){
			// problem loading xml file: nothing we can do
		} finally {
			if (xmlString != null){
				ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(qName, null); // flush old one
			}
		}

		// backward compatibility, load variables and containers from preferences into cache
		loadVariablesAndContainers(getDefaultPreferences());
		loadVariablesAndContainers(getInstancePreferences());

		// load variables and containers from saved file into cache
		File file = getVariableAndContainersFile();
		DataInputStream in = null;
		try {
			in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			switch (in.readInt()) {
				case 2 :
					new VariablesAndContainersLoadHelper(in).load();
					break;
				case 1 : // backward compatibility, load old format
					// variables
					int size = in.readInt();
					while (size-- > 0) {
						String varName = in.readUTF();
						String pathString = in.readUTF();
						if (CP_ENTRY_IGNORE.equals(pathString))
							continue;
						IPath varPath = Path.fromPortableString(pathString);
						this.variables.put(varName, varPath);
						this.previousSessionVariables.put(varName, varPath);
					}
					
					// containers
					IJavaModel model = getJavaModel();
					int projectSize = in.readInt();
					while (projectSize-- > 0) {
						String projectName = in.readUTF();
						IJavaProject project = model.getJavaProject(projectName);
						int containerSize = in.readInt();
						while (containerSize-- > 0) {
							IPath containerPath = Path.fromPortableString(in.readUTF());
							int length = in.readInt();
							byte[] containerString = new byte[length];
							in.readFully(containerString);
							recreatePersistedContainer(project, containerPath, new String(containerString), true/*add to container values*/);
						}
					}
					break;
			}
		} catch (IOException e) {
			if (file.exists())
				Util.log(e, "Unable to read variable and containers file"); //$NON-NLS-1$
		} catch (RuntimeException e) {
			if (file.exists())
				Util.log(e, "Unable to read variable and containers file (file is corrupt)"); //$NON-NLS-1$
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// nothing we can do: ignore
				}
			}
		}

		// override persisted values for variables which have a registered initializer
		String[] registeredVariables = getRegisteredVariableNames();
		for (int i = 0; i < registeredVariables.length; i++) {
			String varName = registeredVariables[i];
			this.variables.put(varName, null); // reset variable, but leave its entry in the Map, so it will be part of variable names.
		}
		// override persisted values for containers which have a registered initializer
		containersReset(getRegisteredContainerIDs());
	}

	private void loadVariablesAndContainers(IEclipsePreferences preferences) {
		try {
			// only get variable from preferences not set to their default
			String[] propertyNames = preferences.keys();
			int variablePrefixLength = CP_VARIABLE_PREFERENCES_PREFIX.length();
			for (int i = 0; i < propertyNames.length; i++){
				String propertyName = propertyNames[i];
				if (propertyName.startsWith(CP_VARIABLE_PREFERENCES_PREFIX)){
					String varName = propertyName.substring(variablePrefixLength);
					String propertyValue = preferences.get(propertyName, null);
					if (propertyValue != null) {
						String pathString = propertyValue.trim();
						
						if (CP_ENTRY_IGNORE.equals(pathString)) {
							// cleanup old preferences
							preferences.remove(propertyName); 
							continue;
						}
						
						// add variable to table
						IPath varPath = new Path(pathString);
						this.variables.put(varName, varPath); 
						this.previousSessionVariables.put(varName, varPath);
					}
				} else if (propertyName.startsWith(CP_CONTAINER_PREFERENCES_PREFIX)){
					String propertyValue = preferences.get(propertyName, null);
					if (propertyValue != null) {
						// cleanup old preferences
						preferences.remove(propertyName); 
						
						// recreate container
						recreatePersistedContainer(propertyName, propertyValue, true/*add to container values*/);
					}
				}
			}
		} catch (BackingStoreException e1) {
			// TODO (frederic) see if it's necessary to report this failure...
		}
	}

	private static final class PersistedClasspathContainer implements
			IClasspathContainer {

		private final IPath containerPath;

		private final IClasspathEntry[] entries;

		private final IJavaProject project;

		PersistedClasspathContainer(IJavaProject project, IPath containerPath,
				IClasspathEntry[] entries) {
			super();
			this.containerPath = containerPath;
			this.entries = entries;
			this.project = project;
		}

		public IClasspathEntry[] getClasspathEntries() {
			return entries;
		}

		public String getDescription() {
			return "Persisted container [" + containerPath //$NON-NLS-1$
					+ " for project [" + project.getElementName() //$NON-NLS-1$
					+ "]]"; //$NON-NLS-1$  
		}

		public int getKind() {
			return 0;
		}

		public IPath getPath() {
			return containerPath;
		}

		public String toString() {
			return getDescription();
		}
	}

	private final class VariablesAndContainersLoadHelper {

		private static final int ARRAY_INCREMENT = 200;

		private IClasspathEntry[] allClasspathEntries;
		private int allClasspathEntryCount;

		private final Map allPaths; // String -> IPath

		private String[] allStrings;
		private int allStringsCount;

		private final DataInputStream in;

		VariablesAndContainersLoadHelper(DataInputStream in) {
			super();
			this.allClasspathEntries = null;
			this.allClasspathEntryCount = 0;
			this.allPaths = new HashMap();
			this.allStrings = null;
			this.allStringsCount = 0;
			this.in = in;
		}

		void load() throws IOException {
			loadProjects(JavaModelManager.this.getJavaModel());
			loadVariables();
		}

		private IAccessRule loadAccessRule() throws IOException {
			int problemId = loadInt();
			IPath pattern = loadPath();
			return new ClasspathAccessRule(pattern.toString().toCharArray(), problemId);
		}

		private IAccessRule[] loadAccessRules() throws IOException {
			int count = loadInt();

			if (count == 0)
				return ClasspathEntry.NO_ACCESS_RULES;

			IAccessRule[] rules = new IAccessRule[count];

			for (int i = 0; i < count; ++i)
				rules[i] = loadAccessRule();

			return rules;
		}

		private IClasspathAttribute loadAttribute() throws IOException {
			String name = loadString();
			String value = loadString();

			return new ClasspathAttribute(name, value);
		}

		private IClasspathAttribute[] loadAttributes() throws IOException {
			int count = loadInt();

			if (count == 0)
				return ClasspathEntry.NO_EXTRA_ATTRIBUTES;

			IClasspathAttribute[] attributes = new IClasspathAttribute[count];

			for (int i = 0; i < count; ++i)
				attributes[i] = loadAttribute();

			return attributes;
		}

		private boolean loadBoolean() throws IOException {
			return this.in.readBoolean();
		}

		private IClasspathEntry[] loadClasspathEntries() throws IOException {
			int count = loadInt();
			IClasspathEntry[] entries = new IClasspathEntry[count];

			for (int i = 0; i < count; ++i)
				entries[i] = loadClasspathEntry();

			return entries;
		}

		private IClasspathEntry loadClasspathEntry() throws IOException {
			int id = loadInt();

			if (id < 0 || id > this.allClasspathEntryCount)
				throw new IOException("Unexpected classpathentry id"); //$NON-NLS-1$

			if (id < this.allClasspathEntryCount)
				return this.allClasspathEntries[id];

			int contentKind = loadInt();
			int entryKind = loadInt();
			IPath path = loadPath();
			IPath[] inclusionPatterns = loadPaths();
			IPath[] exclusionPatterns = loadPaths();
			IPath sourceAttachmentPath = loadPath();
			IPath sourceAttachmentRootPath = loadPath();
			IPath specificOutputLocation = loadPath();
			boolean isExported = loadBoolean();
			IAccessRule[] accessRules = loadAccessRules();
			boolean combineAccessRules = loadBoolean();
			IClasspathAttribute[] extraAttributes = loadAttributes();

			IClasspathEntry entry = new ClasspathEntry(contentKind, entryKind,
					path, inclusionPatterns, exclusionPatterns,
					sourceAttachmentPath, sourceAttachmentRootPath,
					specificOutputLocation, isExported, accessRules,
					combineAccessRules, extraAttributes);

			IClasspathEntry[] array = this.allClasspathEntries;

			if (array == null || id == array.length) {
				array = new IClasspathEntry[id + ARRAY_INCREMENT];

				if (id != 0)
					System.arraycopy(this.allClasspathEntries, 0, array, 0, id);

				this.allClasspathEntries = array;
			}

			array[id] = entry;
			this.allClasspathEntryCount = id + 1;

			return entry;
		}

		private void loadContainers(IJavaProject project) throws IOException {
			boolean projectIsAccessible = project.getProject().isAccessible();
			int count = loadInt();
			for (int i = 0; i < count; ++i) {
				IPath path = loadPath();
				IClasspathEntry[] entries = loadClasspathEntries();
				
				if (!projectIsAccessible) 
					// avoid leaking deleted project's persisted container,
					// but still read the container as it is is part of the file format
					continue; 

				IClasspathContainer container = new PersistedClasspathContainer(project, path, entries);

				JavaModelManager.this.containerPut(project, path, container);

				Map oldContainers = (Map) JavaModelManager.this.previousSessionContainers.get(project);

				if (oldContainers == null) {
					oldContainers = new HashMap();
					JavaModelManager.this.previousSessionContainers.put(project, oldContainers);
				}

				oldContainers.put(path, container);
			}
		}

		private int loadInt() throws IOException {
			return this.in.readInt();
		}

		private IPath loadPath() throws IOException {
			if (loadBoolean())
				return null;

			String portableString = loadString();
			IPath path = (IPath) this.allPaths.get(portableString);

			if (path == null) {
				path = Path.fromPortableString(portableString);
				this.allPaths.put(portableString, path);
			}

			return path;
		}

		private IPath[] loadPaths() throws IOException {
			int count = loadInt();
			IPath[] pathArray = new IPath[count];

			for (int i = 0; i < count; ++i)
				pathArray[i] = loadPath();

			return pathArray;
		}

		private void loadProjects(IJavaModel model) throws IOException {
			int count = loadInt();

			for (int i = 0; i < count; ++i) {
				String projectName = loadString();

				loadContainers(model.getJavaProject(projectName));
			}
		}

		private String loadString() throws IOException {
			int id = loadInt();

			if (id < 0 || id > this.allStringsCount)
				throw new IOException("Unexpected string id"); //$NON-NLS-1$

			if (id < this.allStringsCount)
				return this.allStrings[id];

			String string = this.in.readUTF();
			String[] array = this.allStrings;

			if (array == null || id == array.length) {
				array = new String[id + ARRAY_INCREMENT];

				if (id != 0)
					System.arraycopy(this.allStrings, 0, array, 0, id);

				this.allStrings = array;
			}

			array[id] = string;
			this.allStringsCount = id + 1;

			return string;
		}

		private void loadVariables() throws IOException {
			int size = loadInt();
			Map loadedVars = new HashMap(size);

			for (int i = 0; i < size; ++i) {
				String varName = loadString();
				IPath varPath = loadPath();

				if (varPath != null)
					loadedVars.put(varName, varPath);
			}

			JavaModelManager.this.previousSessionVariables.putAll(loadedVars);
			JavaModelManager.this.variables.putAll(loadedVars);
		}
	}

	/**
	 *  Returns the info for this element without
	 *  disturbing the cache ordering.
	 */
	protected synchronized Object peekAtInfo(IJavaElement element) {
		HashMap tempCache = (HashMap)this.temporaryCache.get();
		if (tempCache != null) {
			Object result = tempCache.get(element);
			if (result != null) {
				return result;
			}
		}
		return this.cache.peekAtInfo(element);
	}

	/**
	 * @see ISaveParticipant
	 */
	public void prepareToSave(ISaveContext context) /*throws CoreException*/ {
		// nothing to do
	}
	/*
	 * Puts the infos in the given map (keys are IJavaElements and values are JavaElementInfos)
	 * in the Java model cache in an atomic way.
	 * First checks that the info for the opened element (or one of its ancestors) has not been 
	 * added to the cache. If it is the case, another thread has opened the element (or one of
	 * its ancestors). So returns without updating the cache.
	 */
	protected synchronized void putInfos(IJavaElement openedElement, Map newElements) {
		// remove children
		Object existingInfo = this.cache.peekAtInfo(openedElement);
		if (openedElement instanceof IParent && existingInfo instanceof JavaElementInfo) {
			IJavaElement[] children = ((JavaElementInfo)existingInfo).getChildren();
			for (int i = 0, size = children.length; i < size; ++i) {
				JavaElement child = (JavaElement) children[i];
				try {
					child.close();
				} catch (JavaModelException e) {
					// ignore
				}
			}
		}
		
		// Need to put any JarPackageFragmentRoot in first.
		// This is due to the way the LRU cache flushes entries.
		// When a JarPackageFragment is flused from the LRU cache, the entire
		// jar is flushed by removing the JarPackageFragmentRoot and all of its
		// children (see ElementCache.close()). If we flush the JarPackageFragment 
		// when its JarPackageFragmentRoot is not in the cache and the root is about to be 
		// added (during the 'while' loop), we will end up in an inconsist state. 
		// Subsequent resolution against package in the jar would fail as a result.
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=102422
		// (theodora)
		for(Iterator it = newElements.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry entry = (Map.Entry)it.next();
			IJavaElement element = (IJavaElement)entry.getKey();
			if( element instanceof JarPackageFragmentRoot ){
				Object info = entry.getValue();
				it.remove();
				this.cache.putInfo(element, info);
			}
		}	
	
		Iterator iterator = newElements.entrySet().iterator();	
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			this.cache.putInfo((IJavaElement) entry.getKey(), entry.getValue());
		}
	}
	
	/*
	 * Remember the info for the jar binary type
	 */
	protected synchronized void putJarTypeInfo(IJavaElement type, Object info) {
		this.cache.jarTypeCache.put(type, info);
	}

	/**
	 * Reads the build state for the relevant project.
	 */
	protected Object readState(IProject project) throws CoreException {
		File file = getSerializationFile(project);
		if (file != null && file.exists()) {
			try {
				DataInputStream in= new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
				try {
					String pluginID= in.readUTF();
					if (!pluginID.equals(JavaCore.PLUGIN_ID))
						throw new IOException(Messages.build_wrongFileFormat); 
					String kind= in.readUTF();
					if (!kind.equals("STATE")) //$NON-NLS-1$
						throw new IOException(Messages.build_wrongFileFormat); 
					if (in.readBoolean())
						return JavaBuilder.readState(project, in);
					if (JavaBuilder.DEBUG)
						System.out.println("Saved state thinks last build failed for " + project.getName()); //$NON-NLS-1$
				} finally {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new CoreException(new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, Platform.PLUGIN_ERROR, "Error reading last build state for project "+ project.getName(), e)); //$NON-NLS-1$
			}
		} else if (JavaBuilder.DEBUG) {
			if (file == null)
				System.out.println("Project does not exist: " + project); //$NON-NLS-1$
			else
				System.out.println("Build state file " + file.getPath() + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return null;
	}

	public static void recreatePersistedContainer(String propertyName, String containerString, boolean addToContainerValues) {
		int containerPrefixLength = CP_CONTAINER_PREFERENCES_PREFIX.length();
		int index = propertyName.indexOf('|', containerPrefixLength);
		if (containerString != null) containerString = containerString.trim();
		if (index > 0) {
			String projectName = propertyName.substring(containerPrefixLength, index).trim();
			IJavaProject project = getJavaModelManager().getJavaModel().getJavaProject(projectName);
			IPath containerPath = new Path(propertyName.substring(index+1).trim());
			recreatePersistedContainer(project, containerPath, containerString, addToContainerValues);
		}
	}
	
	private static void recreatePersistedContainer(final IJavaProject project, final IPath containerPath, String containerString, boolean addToContainerValues) {
		if (!project.getProject().isAccessible()) return; // avoid leaking deleted project's persisted container	
		if (containerString == null) {
			getJavaModelManager().containerPut(project, containerPath, null);
		} else {
			IClasspathEntry[] entries;
			try {
				entries = ((JavaProject) project).decodeClasspath(containerString, null/*not interested in unknown elements*/);
			} catch (IOException e) {
				Util.log(e, "Could not recreate persisted container: \n" + containerString); //$NON-NLS-1$
				entries = JavaProject.INVALID_CLASSPATH;
			}
			if (entries != JavaProject.INVALID_CLASSPATH) {
				final IClasspathEntry[] containerEntries = entries;
				IClasspathContainer container = new IClasspathContainer() {
					public IClasspathEntry[] getClasspathEntries() {
						return containerEntries;
					}
					public String getDescription() {
						return "Persisted container ["+containerPath+" for project ["+ project.getElementName()+"]"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
					}
					public int getKind() {
						return 0; 
					}
					public IPath getPath() {
						return containerPath;
					}
					public String toString() {
						return getDescription();
					}

				};
				if (addToContainerValues) {
					getJavaModelManager().containerPut(project, containerPath, container);
				}
				Map projectContainers = (Map)getJavaModelManager().previousSessionContainers.get(project);
				if (projectContainers == null){
					projectContainers = new HashMap(1);
					getJavaModelManager().previousSessionContainers.put(project, projectContainers);
				}
				projectContainers.put(containerPath, container);
			}
		}
	}
	
	/**
	 * Remembers the given scope in a weak set
	 * (so no need to remove it: it will be removed by the garbage collector)
	 */
	public void rememberScope(AbstractSearchScope scope) {
		// NB: The value has to be null so as to not create a strong reference on the scope
		this.searchScopes.put(scope, null); 
	}
	
	/*
	 * Removes all cached info for the given element (including all children)
	 * from the cache.
	 * Returns the info for the given element, or null if it was closed.
	 */
	public synchronized Object removeInfoAndChildren(JavaElement element) throws JavaModelException {
		Object info = this.cache.peekAtInfo(element);
		if (info != null) {
			boolean wasVerbose = false;
			try {
				if (JavaModelCache.VERBOSE) {
					String elementType;
					switch (element.getElementType()) {
						case IJavaElement.JAVA_PROJECT:
							elementType = "project"; //$NON-NLS-1$
							break;
						case IJavaElement.PACKAGE_FRAGMENT_ROOT:
							elementType = "root"; //$NON-NLS-1$
							break;
						case IJavaElement.PACKAGE_FRAGMENT:
							elementType = "package"; //$NON-NLS-1$
							break;
						case IJavaElement.CLASS_FILE:
							elementType = "class file"; //$NON-NLS-1$
							break;
						case IJavaElement.COMPILATION_UNIT:
							elementType = "compilation unit"; //$NON-NLS-1$
							break;
						default:
							elementType = "element"; //$NON-NLS-1$
					}
					System.out.println(Thread.currentThread() + " CLOSING "+ elementType + " " + element.toStringWithAncestors());  //$NON-NLS-1$//$NON-NLS-2$
					wasVerbose = true;
					JavaModelCache.VERBOSE = false;
				}
				element.closing(info);
				if (element instanceof IParent && info instanceof JavaElementInfo) {
					IJavaElement[] children = ((JavaElementInfo)info).getChildren();
					for (int i = 0, size = children.length; i < size; ++i) {
						JavaElement child = (JavaElement) children[i];
						child.close();
					}
				}
				this.cache.removeInfo(element);
				if (wasVerbose) {
					System.out.println(this.cache.toStringFillingRation("-> ")); //$NON-NLS-1$
				}
			} finally {
				JavaModelCache.VERBOSE = wasVerbose;
			}
			return info;
		}
		return null;
	}	

	public void removePerProjectInfo(JavaProject javaProject) {
		synchronized(this.perProjectInfos) { // use the perProjectInfo collection as its own lock
			IProject project = javaProject.getProject();
			PerProjectInfo info= (PerProjectInfo) this.perProjectInfos.get(project);
			if (info != null) {
				this.perProjectInfos.remove(project);
			}
		}
	}

	/*
	 * Reset project options stored in info cache.
	 */
	public void resetProjectOptions(JavaProject javaProject) {
		synchronized(this.perProjectInfos) { // use the perProjectInfo collection as its own lock
			IProject project = javaProject.getProject();
			PerProjectInfo info= (PerProjectInfo) this.perProjectInfos.get(project);
			if (info != null) {
				info.options = null;
			}
		}
	}

	/*
	 * Reset project preferences stored in info cache.
	 */
	public void resetProjectPreferences(JavaProject javaProject) {
		synchronized(this.perProjectInfos) { // use the perProjectInfo collection as its own lock
			IProject project = javaProject.getProject();
			PerProjectInfo info= (PerProjectInfo) this.perProjectInfos.get(project);
			if (info != null) {
				info.preferences = null;
			}
		}
	}
	
	public static final void doNotUse() {
		// used by tests to simulate a startup
		MANAGER = new JavaModelManager();
	}
	
	/*
	 * Resets the cache that holds on binary type in jar files
	 */
	protected synchronized void resetJarTypeCache() {
		this.cache.resetJarTypeCache();
	}

	/*
	 * Resets the temporary cache for newly created elements to null.
	 */
	public void resetTemporaryCache() {
		this.temporaryCache.set(null);
	}

	/**
	 * @see ISaveParticipant
	 */
	public void rollback(ISaveContext context){
		// nothing to do
	}

	private void saveState(PerProjectInfo info, ISaveContext context) throws CoreException {

		// passed this point, save actions are non trivial
		if (context.getKind() == ISaveContext.SNAPSHOT) return;
		
		// save built state
		if (info.triedRead) saveBuiltState(info);
	}
	
	/**
	 * Saves the built state for the project.
	 */
	private void saveBuiltState(PerProjectInfo info) throws CoreException {
		if (JavaBuilder.DEBUG)
			System.out.println(Messages.bind(Messages.build_saveStateProgress, info.project.getName())); 
		File file = getSerializationFile(info.project);
		if (file == null) return;
		long t = System.currentTimeMillis();
		try {
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			try {
				out.writeUTF(JavaCore.PLUGIN_ID);
				out.writeUTF("STATE"); //$NON-NLS-1$
				if (info.savedState == null) {
					out.writeBoolean(false);
				} else {
					out.writeBoolean(true);
					JavaBuilder.writeState(info.savedState, out);
				}
			} finally {
				out.close();
			}
		} catch (RuntimeException e) {
			try {
				file.delete();
			} catch(SecurityException se) {
				// could not delete file: cannot do much more
			}
			throw new CoreException(
				new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, Platform.PLUGIN_ERROR,
					Messages.bind(Messages.build_cannotSaveState, info.project.getName()), e)); 
		} catch (IOException e) {
			try {
				file.delete();
			} catch(SecurityException se) {
				// could not delete file: cannot do much more
			}
			throw new CoreException(
				new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, Platform.PLUGIN_ERROR,
					Messages.bind(Messages.build_cannotSaveState, info.project.getName()), e)); 
		}
		if (JavaBuilder.DEBUG) {
			t = System.currentTimeMillis() - t;
			System.out.println(Messages.bind(Messages.build_saveStateComplete, String.valueOf(t))); 
		}
	}
	
	private void saveVariablesAndContainers() throws CoreException {
		File file = getVariableAndContainersFile();
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			out.writeInt(VARIABLES_AND_CONTAINERS_FILE_VERSION);
			if (VARIABLES_AND_CONTAINERS_FILE_VERSION != 1)
				new VariablesAndContainersSaveHelper(out).save();
			else {
				// old code retained for performance comparisons
    			
    			// variables
    			out.writeInt(this.variables.size());
    			Iterator iterator = this.variables.entrySet().iterator();
    			while (iterator.hasNext()) {
    				Map.Entry entry = (Map.Entry) iterator.next();
    				String variableName = (String) entry.getKey();
    				out.writeUTF(variableName);
    				IPath path = (IPath) entry.getValue();
    				out.writeUTF(path == null ? CP_ENTRY_IGNORE : path.toPortableString());
    			}
    			
    			// containers
    			IJavaProject[] projects = getJavaModel().getJavaProjects();
    			int length = projects.length;
    			out.writeInt(length);
    			for (int i = 0; i < length; i++) {
    			    IJavaProject project = projects[i];
    				// clone while iterating (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=59638)
    				Map projectContainers = containerClone(project);
    				out.writeUTF(project.getElementName());
    				if (projectContainers == null) {
    					out.writeInt(0);
    					continue;
    				}
    				HashMap containersToSave = new HashMap();
    				for (iterator = projectContainers.entrySet().iterator(); iterator.hasNext();) {
    					Map.Entry entry = (Map.Entry) iterator.next();
    				    IPath containerPath = (IPath) entry.getKey();
    				    IClasspathContainer container = (IClasspathContainer) entry.getValue();
    					String containerString = null;
    					try {
    						if (container == null) {
    							// container has not been initialized yet, use previous session value
    							// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=73969)
    							container = getPreviousSessionContainer(containerPath, project);
    						}
    						if (container != null) {
    							IClasspathEntry[] entries = container.getClasspathEntries();
    							containerString = ((JavaProject)project).encodeClasspath(
    									entries, 
    									null, 
    									false,
    									null/*not interested in unknown elements*/);
    						}
    					} catch(JavaModelException e){
    						// could not encode entry: will not persist
    						Util.log(e, "Could not persist container " + containerPath + " for project " + project.getElementName()); //$NON-NLS-1$ //$NON-NLS-2$
    					}
    					if (containerString != null)
    						containersToSave.put(containerPath, containerString);
    				}
    				out.writeInt(containersToSave.size());
    				iterator = containersToSave.entrySet().iterator();
    				while (iterator.hasNext()) {
    					Map.Entry entry = (Map.Entry) iterator.next();
    					IPath containerPath = (IPath) entry.getKey();
    					out.writeUTF(containerPath.toPortableString());
    					String containerString = (String) entry.getValue();
    					out.writeInt(containerString.length());
    					out.writeBytes(containerString);
    				}
    			}
			}
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, IStatus.ERROR, "Problems while saving variables and containers", e); //$NON-NLS-1$
			throw new CoreException(status);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// nothing we can do: ignore
				}
			}
		}
	}
	
	private final class VariablesAndContainersSaveHelper {

		private final HashtableOfObjectToInt classpathEntryIds; // IClasspathEntry -> int
		private final DataOutputStream out;
		private final HashtableOfObjectToInt stringIds; // Strings -> int

		VariablesAndContainersSaveHelper(DataOutputStream out) {
			super();
			this.classpathEntryIds = new HashtableOfObjectToInt();
			this.out = out;
			this.stringIds = new HashtableOfObjectToInt();
		}

		void save() throws IOException, JavaModelException {
			saveProjects(JavaModelManager.this.getJavaModel().getJavaProjects());
			
			// remove variables that should not be saved
			HashMap varsToSave = null;
			Iterator iterator = JavaModelManager.this.variables.entrySet().iterator();
			IEclipsePreferences defaultPreferences = getDefaultPreferences();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String varName = (String) entry.getKey();
				if (defaultPreferences.get(CP_VARIABLE_PREFERENCES_PREFIX + varName, null) != null // don't save classpath variables from the default preferences as there is no delta if they are removed
 CP_ENTRY_IGNORE_PATH.equals(entry.getValue())) {
					
					if (varsToSave == null)
						varsToSave = new HashMap(JavaModelManager.this.variables);
					varsToSave.remove(varName);
				}
					
			}
			
			saveVariables(varsToSave != null ? varsToSave : JavaModelManager.this.variables);
		}

		private void saveAccessRule(ClasspathAccessRule rule) throws IOException {
			saveInt(rule.problemId);
			savePath(rule.getPattern());
		}

		private void saveAccessRules(IAccessRule[] rules) throws IOException {
			int count = rules == null ? 0 : rules.length;

			saveInt(count);
			for (int i = 0; i < count; ++i)
				saveAccessRule((ClasspathAccessRule) rules[i]);
		}

		private void saveAttribute(IClasspathAttribute attribute)
				throws IOException {
			saveString(attribute.getName());
			saveString(attribute.getValue());
		}

		private void saveAttributes(IClasspathAttribute[] attributes)
				throws IOException {
			int count = attributes == null ? 0 : attributes.length;

			saveInt(count);
			for (int i = 0; i < count; ++i)
				saveAttribute(attributes[i]);
		}

		private void saveClasspathEntries(IClasspathEntry[] entries)
				throws IOException {
			int count = entries == null ? 0 : entries.length;

			saveInt(count);
			for (int i = 0; i < count; ++i)
				saveClasspathEntry(entries[i]);
		}

		private void saveClasspathEntry(IClasspathEntry entry)
				throws IOException {
			if (saveNewId(entry, this.classpathEntryIds)) {
				saveInt(entry.getContentKind());
				saveInt(entry.getEntryKind());
				savePath(entry.getPath());
				savePaths(entry.getInclusionPatterns());
				savePaths(entry.getExclusionPatterns());
				savePath(entry.getSourceAttachmentPath());
				savePath(entry.getSourceAttachmentRootPath());
				savePath(entry.getOutputLocation());
				this.out.writeBoolean(entry.isExported());
				saveAccessRules(entry.getAccessRules());
				this.out.writeBoolean(entry.combineAccessRules());
				saveAttributes(entry.getExtraAttributes());
			}
		}

		private void saveContainers(IJavaProject project, Map containerMap)
				throws IOException {
			saveInt(containerMap.size());

			for (Iterator i = containerMap.entrySet().iterator(); i.hasNext();) {
				Entry entry = (Entry) i.next();
				IPath path = (IPath) entry.getKey();
				IClasspathContainer container = (IClasspathContainer) entry.getValue();
				IClasspathEntry[] cpEntries = null;

				if (container == null) {
					// container has not been initialized yet, use previous
					// session value
					// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=73969)
					container = JavaModelManager.this.getPreviousSessionContainer(path, project);
				}

				if (container != null)
					cpEntries = container.getClasspathEntries();

				savePath(path);
				saveClasspathEntries(cpEntries);
			}
		}

		private void saveInt(int value) throws IOException {
			this.out.writeInt(value);
		}

		private boolean saveNewId(Object key, HashtableOfObjectToInt map) throws IOException {
			int id = map.get(key);

			if (id == -1) {
				int newId = map.size();

				map.put(key, newId);

				saveInt(newId);

				return true;
			} else {
				saveInt(id);

				return false;
			}
		}

		private void savePath(IPath path) throws IOException {
			if (path == null) {
				this.out.writeBoolean(true);
			} else {
				this.out.writeBoolean(false);
				saveString(path.toPortableString());
			}
		}

		private void savePaths(IPath[] paths) throws IOException {
			int count = paths == null ? 0 : paths.length;

			saveInt(count);
			for (int i = 0; i < count; ++i)
				savePath(paths[i]);
		}

		private void saveProjects(IJavaProject[] projects) throws IOException,
				JavaModelException {
			int count = projects.length;

			saveInt(count);

			for (int i = 0; i < count; ++i) {
				IJavaProject project = projects[i];

				saveString(project.getElementName());

				Map containerMap = (Map) JavaModelManager.this.containers.get(project);

				if (containerMap == null) {
					containerMap = Collections.EMPTY_MAP;
				} else {
					// clone while iterating
					// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=59638)
					containerMap = new HashMap(containerMap);
				}

				saveContainers(project, containerMap);
			}
		}

		private void saveString(String string) throws IOException {
			if (saveNewId(string, this.stringIds))
				this.out.writeUTF(string);
		}

		private void saveVariables(Map map) throws IOException {
			saveInt(map.size());

			for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
				Entry entry = (Entry) i.next();
				String varName = (String) entry.getKey();
				IPath varPath = (IPath) entry.getValue();

				saveString(varName);
				savePath(varPath);
			}
		}
	}

	private void traceVariableAndContainers(String action, long start) {

		Long delta = new Long(System.currentTimeMillis() - start);
		Long length = new Long(getVariableAndContainersFile().length());
		String pattern = "{0} {1} bytes in variablesAndContainers.dat in {2}ms"; //$NON-NLS-1$
		String message = MessageFormat.format(pattern, new Object[]{action, length, delta});

		System.out.println(message);
	}

	/**
	 * @see ISaveParticipant
	 */
	public void saving(ISaveContext context) throws CoreException {
		
	    // save variable and container values on snapshot/full save
		long start = -1;
		if (VERBOSE)
			start = System.currentTimeMillis();
		saveVariablesAndContainers();
		if (VERBOSE)
			traceVariableAndContainers("Saved", start); //$NON-NLS-1$
		
		if (context.getKind() == ISaveContext.FULL_SAVE) {
			// will need delta since this save (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=38658)
			context.needDelta();
			
			// clean up indexes on workspace full save
			// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=52347)
			IndexManager manager = this.indexManager;
			if (manager != null 
					// don't force initialization of workspace scope as we could be shutting down
					// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=93941)
					&& this.workspaceScope != null) { 
				manager.cleanUpIndexes();
			}
		}
	
		IProject savedProject = context.getProject();
		if (savedProject != null) {
			if (!JavaProject.hasJavaNature(savedProject)) return; // ignore
			PerProjectInfo info = getPerProjectInfo(savedProject, true /* create info */);
			saveState(info, context);
			info.rememberExternalLibTimestamps();
			return;
		}
	
		ArrayList vStats= null; // lazy initialized
		ArrayList values = null;
		synchronized(this.perProjectInfos) {
			values = new ArrayList(this.perProjectInfos.values());
		}
		if (values != null) {
			Iterator iterator = values.iterator();
			while (iterator.hasNext()) {
				try {
					PerProjectInfo info = (PerProjectInfo) iterator.next();
					saveState(info, context);
					info.rememberExternalLibTimestamps();
				} catch (CoreException e) {
					if (vStats == null)
						vStats= new ArrayList();
					vStats.add(e.getStatus());
				}
			}
		}
		if (vStats != null) {
			IStatus[] stats= new IStatus[vStats.size()];
			vStats.toArray(stats);
			throw new CoreException(new MultiStatus(JavaCore.PLUGIN_ID, IStatus.ERROR, stats, Messages.build_cannotSaveStates, null)); 
		}
		
		// save external libs timestamps
		this.deltaState.saveExternalLibTimeStamps();
	}

	/**
	 * Add a secondary type in temporary indexing cache for a project got from given path.
	 * 
	 * Current secondary types cache is not modified as we want to wait that indexing
	 * was finished before taking new secondary types into account.
	 * 
	 * Indexing cache is a specific entry in secondary types cache which key is
	 * {@link #INDEXED_SECONDARY_TYPES } and value a map with same structure than
	 * secondary types cache itself.
	 * 
	 * @see #secondaryTypes(IJavaProject, boolean, IProgressMonitor)
	 */
	public void secondaryTypeAdding(String path, char[] key) {
		if (VERBOSE) {
			StringBuffer buffer = new StringBuffer("JavaModelManager.addSecondaryType("); //$NON-NLS-1$
			buffer.append(path);
			buffer.append(',');
			buffer.append('[');
			buffer.append(new String(key));
			buffer.append(']');
			buffer.append(')');
			Util.verbose(buffer.toString());
		}
		IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = wRoot.findMember(path);
		if (resource != null) {
			if (org.eclipse.jdt.internal.core.util.Util.isJavaLikeFileName(path) && resource.getType() == IResource.FILE) {
				IProject project = resource.getProject();
				try {
					PerProjectInfo projectInfo = getPerProjectInfoCheckExistence(project);
					// Get or create map to cache secondary types while indexing (can be not synchronized as indexing insure a non-concurrent usage)
					HashMap indexedSecondaryTypes = null;
					if (projectInfo.secondaryTypes == null) {
						projectInfo.secondaryTypes = new Hashtable(3);
						indexedSecondaryTypes = new HashMap(3);
						projectInfo.secondaryTypes.put(INDEXED_SECONDARY_TYPES, indexedSecondaryTypes);
					} else {
						indexedSecondaryTypes = (HashMap) projectInfo.secondaryTypes.get(INDEXED_SECONDARY_TYPES);
						if (indexedSecondaryTypes == null) {
							indexedSecondaryTypes = new HashMap(3);
							projectInfo.secondaryTypes.put(INDEXED_SECONDARY_TYPES, indexedSecondaryTypes);
						}
					}
					// Store the secondary type in temporary cache (these are just handles => no problem to create it now...)
					HashMap allTypes = (HashMap) indexedSecondaryTypes.get(resource);
					if (allTypes == null) {
						allTypes = new HashMap(3);
						indexedSecondaryTypes.put(resource, allTypes);
					}
					ICompilationUnit unit = JavaModelManager.createCompilationUnitFrom((IFile)resource, null);
					if (unit != null) {
						char[][] names = CharOperation.splitOn('/', key);
						String typeName = new String(names[0]);
						String packName = new String(names[1]);
						HashMap packageTypes = (HashMap) allTypes.get(packName);
						if (packageTypes == null) {
							packageTypes = new HashMap(3);
							allTypes.put(packName, packageTypes);
						}
						packageTypes.put(typeName, unit.getType(typeName));
					}
					if (VERBOSE) {
						Util.verbose("	- indexing cache:"); //$NON-NLS-1$
						Iterator entries = indexedSecondaryTypes.entrySet().iterator();
						while (entries.hasNext()) {
							Map.Entry entry = (Map.Entry) entries.next();
							IFile file = (IFile) entry.getKey();
							Util.verbose("		+ "+file.getFullPath()+':'+ entry.getValue()); //$NON-NLS-1$
						}
					}
				}
				catch (JavaModelException jme) {
					// do nothing
				}
			}
		}
	}

	/**
	 * Get all secondary types for a project and store result in per project info cache.
	 * 
	 * This cache is an Hashtable<String, HashMap<String, IType>>:
	 * 	- key: package name
	 * 	- value:
	 * 		+ key: type name
	 * 		+ value: java model handle for the secondary type
	 * Hashtable was used to protect callers from possible concurrent access.
	 * 
	 * Note that this map may have a specific entry which key is {@link #INDEXED_SECONDARY_TYPES }
	 * and value is a map containing all secondary types created during indexing.
	 * When this key is in cache and indexing is finished, returned map is merged
	 * with the value of this special key. If indexing is not finished and caller does
	 * not wait for the end of indexing, returned map is the current secondary
	 * types cache content which may be invalid...
	 * 
	 * @param project Project we want get secondary types from
	 * @return HashMap Table of secondary type names->path for given project
	 */
	public Map secondaryTypes(IJavaProject project, boolean waitForIndexes, IProgressMonitor monitor) throws JavaModelException {
		if (VERBOSE) {
			StringBuffer buffer = new StringBuffer("JavaModelManager.secondaryTypes("); //$NON-NLS-1$
			buffer.append(project.getElementName());
			buffer.append(',');
			buffer.append(waitForIndexes);
			buffer.append(')');
			Util.verbose(buffer.toString());
		}

		// Return cache if not empty and there's no new secondary types created during indexing
		final PerProjectInfo projectInfo = getPerProjectInfoCheckExistence(project.getProject());
		Map indexingSecondaryCache = projectInfo.secondaryTypes == null ? null : (Map) projectInfo.secondaryTypes.get(INDEXED_SECONDARY_TYPES);
		if (projectInfo.secondaryTypes != null && indexingSecondaryCache == null) {
			return projectInfo.secondaryTypes;
		}

		// Perform search request only if secondary types cache is not initialized yet (this will happen only once!)
		if (projectInfo.secondaryTypes == null) {
			return secondaryTypesSearching(project, waitForIndexes, monitor, projectInfo);
		}

		// New secondary types have been created while indexing secondary types cache
		// => need to know whether the indexing is finished or not
		boolean indexing = this.indexManager.awaitingJobsCount() > 0;
		if (indexing) {
			if (!waitForIndexes)  {
				// Indexing is running but caller cannot wait => return current cache
				return projectInfo.secondaryTypes;
			}

			// Wait for the end of indexing or a cancel
			while (this.indexManager.awaitingJobsCount() > 0) {
				if (monitor != null && monitor.isCanceled()) {
					return projectInfo.secondaryTypes;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					return projectInfo.secondaryTypes;
				}
			}
		}

		// Indexing is finished => merge caches and return result
		return secondaryTypesMerging(projectInfo.secondaryTypes);
	}
	
	/*
	 * Return secondary types cache merged with new secondary types created while indexing
	 * Note that merge result is directly stored in given parameter map.
	 */
	private Hashtable secondaryTypesMerging(Hashtable secondaryTypes) {
		if (VERBOSE) {
			Util.verbose("JavaModelManager.getSecondaryTypesMerged()"); //$NON-NLS-1$
			Util.verbose("	- current cache to merge:"); //$NON-NLS-1$
			Iterator entries = secondaryTypes.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String packName = (String) entry.getKey();
				Util.verbose("		+ "+packName+':'+ entry.getValue() ); //$NON-NLS-1$
			}
		}

		// Return current cache if there's no indexing cache (double check, this should not happen)
		HashMap indexedSecondaryTypes = (HashMap) secondaryTypes.remove(INDEXED_SECONDARY_TYPES);
		if (indexedSecondaryTypes == null) {
			return secondaryTypes;
		}

		// Merge indexing cache in secondary types one
		Iterator entries = indexedSecondaryTypes.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			IFile file = (IFile) entry.getKey();
	
			// Remove all secondary types of indexed file from cache
			secondaryTypesRemoving(secondaryTypes, file);
			
			// Add all indexing file secondary types in given secondary types cache
			HashMap fileSecondaryTypes = (HashMap) entry.getValue();
			Iterator entries2 = fileSecondaryTypes.entrySet().iterator();
			while (entries2.hasNext()) {
				Map.Entry entry2 = (Map.Entry) entries2.next();
				String packageName = (String) entry2.getKey();
				HashMap cachedTypes = (HashMap) secondaryTypes.get(packageName);
				if (cachedTypes == null) {
					secondaryTypes.put(packageName, entry2.getValue());
				} else {
					HashMap types = (HashMap) entry2.getValue();
					Iterator entries3 = types.entrySet().iterator();
					while (entries3.hasNext()) {
						Map.Entry entry3 = (Map.Entry) entries3.next();
						String typeName = (String) entry3.getKey();
						cachedTypes.put(typeName, entry3.getValue());
					}
				}
			}
		}
		if (VERBOSE) {
			Util.verbose("	- secondary types cache merged:"); //$NON-NLS-1$
			entries = secondaryTypes.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String packName = (String) entry.getKey();
				Util.verbose("		+ "+packName+':'+ entry.getValue()); //$NON-NLS-1$
			}
		}
		return secondaryTypes;
	}

	/*
	 * Perform search request to get all secondary types of a given project.
	 * If not waiting for indexes and indexing is running, will return types found in current built indexes...
	 */
	private Map secondaryTypesSearching(IJavaProject project, boolean waitForIndexes, IProgressMonitor monitor, final PerProjectInfo projectInfo) throws JavaModelException {
		if (VERBOSE || BasicSearchEngine.VERBOSE) {
			StringBuffer buffer = new StringBuffer("JavaModelManager.secondaryTypesSearch("); //$NON-NLS-1$
			buffer.append(project.getElementName());
			buffer.append(',');
			buffer.append(waitForIndexes);
			buffer.append(')');
			Util.verbose(buffer.toString());
		}

		final Hashtable secondaryTypes = new Hashtable(3);
		IRestrictedAccessTypeRequestor nameRequestor = new IRestrictedAccessTypeRequestor() {
			public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, String path, AccessRestriction access) {
				String key = packageName==null ? "" : new String(packageName); //$NON-NLS-1$
				HashMap types = (HashMap) secondaryTypes.get(key);
				if (types == null) types = new HashMap(3);
				types.put(new String(simpleTypeName), path);
				secondaryTypes.put(key, types);
			}
		};

		// Build scope using prereq projects but only source folders
		IPackageFragmentRoot[] allRoots = project.getAllPackageFragmentRoots();
		int length = allRoots.length, size = 0;
		IPackageFragmentRoot[] allSourceFolders = new IPackageFragmentRoot[length];
		for (int i=0; i<length; i++) {
			if (allRoots[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
				allSourceFolders[size++] = allRoots[i];
			}
		}
		if (size < length) {
			System.arraycopy(allSourceFolders, 0, allSourceFolders = new IPackageFragmentRoot[size], 0, size);
		}

		// Search all secondary types on scope
		new BasicSearchEngine().searchAllSecondaryTypeNames(allSourceFolders, nameRequestor, waitForIndexes, monitor);

		// Build types from paths
		Iterator packages = secondaryTypes.values().iterator();
		while (packages.hasNext()) {
			HashMap types = (HashMap) packages.next();
			Iterator names = types.entrySet().iterator();
			while (names.hasNext()) {
				Map.Entry entry = (Map.Entry) names.next();
				String typeName = (String) entry.getKey();
				String path = (String) entry.getValue();
				if (org.eclipse.jdt.internal.core.util.Util.isJavaLikeFileName(path)) {
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
					ICompilationUnit unit = JavaModelManager.createCompilationUnitFrom(file, null);
					IType type = unit.getType(typeName);
					types.put(typeName, type); // replace stored path with type itself
				}
			}
		}

		// Store result in per project info cache if still null or there's still an indexing cache (may have been set by another thread...)
		if (projectInfo.secondaryTypes == null || projectInfo.secondaryTypes.get(INDEXED_SECONDARY_TYPES) != null) {
			projectInfo.secondaryTypes = secondaryTypes;
			if (VERBOSE || BasicSearchEngine.VERBOSE) {
				System.out.print(Thread.currentThread() + "	-> secondary paths stored in cache: ");  //$NON-NLS-1$
				System.out.println();
				Iterator entries = secondaryTypes.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry entry = (Map.Entry) entries.next();
					String qualifiedName = (String) entry.getKey();
					Util.verbose("		- "+qualifiedName+'-'+ entry.getValue()); //$NON-NLS-1$
				}
			}
		}
		return projectInfo.secondaryTypes;
	}

	/**
	 * Remove from secondary types cache all types belonging to a given file.
	 * Clean secondary types cache built while indexing if requested.
	 * 
	 * Project's secondary types cache is found using file location.
	 * 
	 * @param file File to remove
	 */
	public void secondaryTypesRemoving(IFile file, boolean cleanIndexCache) {
		if (VERBOSE) {
			StringBuffer buffer = new StringBuffer("JavaModelManager.removeFromSecondaryTypesCache("); //$NON-NLS-1$
			buffer.append(file.getName());
			buffer.append(')');
			Util.verbose(buffer.toString());
		}
		if (file != null) {
			PerProjectInfo projectInfo = getPerProjectInfo(file.getProject(), false);
			if (projectInfo != null && projectInfo.secondaryTypes != null) {
				if (VERBOSE) {
					Util.verbose("-> remove file from cache of project: "+file.getProject().getName()); //$NON-NLS-1$
				}

				// Clean current cache
				secondaryTypesRemoving(projectInfo.secondaryTypes, file);
				
				// Clean indexing cache if necessary
				if (!cleanIndexCache) return;
				HashMap indexingCache = (HashMap) projectInfo.secondaryTypes.get(INDEXED_SECONDARY_TYPES);
				if (indexingCache != null) {
					Set keys = indexingCache.keySet();
					int filesSize = keys.size(), filesCount = 0;
					IFile[] removed = null;
					Iterator cachedFiles = keys.iterator();
					while (cachedFiles.hasNext()) {
						IFile cachedFile = (IFile) cachedFiles.next();
						if (file.equals(cachedFile)) {
							if (removed == null) removed = new IFile[filesSize];
							filesSize--;
							removed[filesCount++] = cachedFile;
						}
					}
					if (removed != null) {
						for (int i=0; i<filesCount; i++) {
							indexingCache.remove(removed[i]);
						}
					}
				}
			}
		}
	}

	/*
	 * Remove from a given cache map all secondary types belonging to a given file.
	 * Note that there can have several secondary types per file...
	 */
	private void secondaryTypesRemoving(Hashtable secondaryTypesMap, IFile file) {
		if (VERBOSE) {
			StringBuffer buffer = new StringBuffer("JavaModelManager.removeSecondaryTypesFromMap("); //$NON-NLS-1$
			Iterator entries = secondaryTypesMap.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String qualifiedName = (String) entry.getKey();
				buffer.append(qualifiedName+':'+ entry.getValue());
			}
			buffer.append(',');
			buffer.append(file.getFullPath());
			buffer.append(')');
			Util.verbose(buffer.toString());
		}
		Set packageEntries = secondaryTypesMap.entrySet();
		int packagesSize = packageEntries.size(), removedPackagesCount = 0;
		String[] removedPackages = null;
		Iterator packages = packageEntries.iterator();
		while (packages.hasNext()) {
			Map.Entry entry = (Map.Entry) packages.next();
			String packName = (String) entry.getKey();
			if (packName != INDEXED_SECONDARY_TYPES) { // skip indexing cache entry if present (!= is intentional)
				HashMap types = (HashMap) entry.getValue();
				Set nameEntries = types.entrySet();
				int namesSize = nameEntries.size(), removedNamesCount = 0;
				String[] removedNames = null;
				Iterator names = nameEntries.iterator();
				while (names.hasNext()) {
					Map.Entry entry2 = (Map.Entry) names.next();
					String typeName = (String) entry2.getKey();
					IType type = (IType) entry2.getValue();
					if (file.equals(type.getResource())) {
						if (removedNames == null) removedNames = new String[namesSize];
						namesSize--;
						removedNames[removedNamesCount++] = typeName;
					}
				}
				if (removedNames != null) {
					for (int i=0; i<removedNamesCount; i++) {
						types.remove(removedNames[i]);
					}
				}
				if (types.size() == 0) {
					if (removedPackages == null) removedPackages = new String[packagesSize];
					packagesSize--;
					removedPackages[removedPackagesCount++] = packName;
				}
			}
		}
		if (removedPackages != null) {
			for (int i=0; i<removedPackagesCount; i++) {
				secondaryTypesMap.remove(removedPackages[i]);
			}
		}
		if (VERBOSE) {
			Util.verbose("	- new secondary types map:"); //$NON-NLS-1$
			Iterator entries = secondaryTypesMap.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String qualifiedName = (String) entry.getKey();
				Util.verbose("		+ "+qualifiedName+':'+ entry.getValue()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Record the order in which to build the java projects (batch build). This order is based
	 * on the projects classpath settings.
	 */
	protected void setBuildOrder(String[] javaBuildOrder) throws JavaModelException {

		// optional behaviour
		// possible value of index 0 is Compute
		if (!JavaCore.COMPUTE.equals(JavaCore.getOption(JavaCore.CORE_JAVA_BUILD_ORDER))) return; // cannot be customized at project level
		
		if (javaBuildOrder == null || javaBuildOrder.length <= 1) return;
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription description = workspace.getDescription();
		String[] wksBuildOrder = description.getBuildOrder();

		String[] newOrder;
		if (wksBuildOrder == null){
			newOrder = javaBuildOrder;
		} else {
			// remove projects which are already mentionned in java builder order
			int javaCount = javaBuildOrder.length;
			HashMap newSet = new HashMap(javaCount); // create a set for fast check
			for (int i = 0; i < javaCount; i++){
				newSet.put(javaBuildOrder[i], javaBuildOrder[i]);
			}
			int removed = 0;
			int oldCount = wksBuildOrder.length;
			for (int i = 0; i < oldCount; i++){
				if (newSet.containsKey(wksBuildOrder[i])){
					wksBuildOrder[i] = null;
					removed++;
				}
			}
			// add Java ones first
			newOrder = new String[oldCount - removed + javaCount];
			System.arraycopy(javaBuildOrder, 0, newOrder, 0, javaCount); // java projects are built first

			// copy previous items in their respective order
			int index = javaCount;
			for (int i = 0; i < oldCount; i++){
				if (wksBuildOrder[i] != null){
					newOrder[index++] = wksBuildOrder[i];
				}
			}
		}
		// commit the new build order out
		description.setBuildOrder(newOrder);
		try {
			workspace.setDescription(description);
		} catch(CoreException e){
			throw new JavaModelException(e);
		}
	}

	/**
	 * Sets the last built state for the given project, or null to reset it.
	 */
	public void setLastBuiltState(IProject project, Object state) {
		if (JavaProject.hasJavaNature(project)) {
			// should never be requested on non-Java projects
			PerProjectInfo info = getPerProjectInfo(project, true /*create if missing*/);
			info.triedRead = true; // no point trying to re-read once using setter
			info.savedState = state;
		}
		if (state == null) { // delete state file to ensure a full build happens if the workspace crashes
			try {
				File file = getSerializationFile(project);
				if (file != null && file.exists())
					file.delete();
			} catch(SecurityException se) {
				// could not delete file: cannot do much more
			}
		}
	}
	
	public void setOptions(Hashtable newOptions) {
		
		try {
			IEclipsePreferences defaultPreferences = getDefaultPreferences();
			IEclipsePreferences instancePreferences = getInstancePreferences();

			if (newOptions == null){
				instancePreferences.clear();
			} else {
				Enumeration keys = newOptions.keys();
				while (keys.hasMoreElements()){
					String key = (String)keys.nextElement();
					if (!this.optionNames.contains(key)) continue; // unrecognized option
					if (key.equals(JavaCore.CORE_ENCODING)) continue; // skipped, contributed by resource prefs
					String value = (String)newOptions.get(key);
					String defaultValue = defaultPreferences.get(key, null);
					if (defaultValue != null && defaultValue.equals(value)) {
						instancePreferences.remove(key);
					} else {
						instancePreferences.put(key, value);
					}
				}
			}

			// persist options
			instancePreferences.flush();
			
			// update cache
			this.optionsCache = newOptions==null ? null : new Hashtable(newOptions);
		} catch (BackingStoreException e) {
			// ignore
		}
	}
		
	public void startup() throws CoreException {
		try {
			configurePluginDebugOptions();
			
			// initialize Java model cache
			this.cache = new JavaModelCache();

			// request state folder creation (workaround 19885)
			JavaCore.getPlugin().getStateLocation();

			// Initialize eclipse preferences
			initializePreferences();

			// Listen to preference changes
			Preferences.IPropertyChangeListener propertyListener = new Preferences.IPropertyChangeListener() {
				public void propertyChange(Preferences.PropertyChangeEvent event) {
					JavaModelManager.this.optionsCache = null;
				}
			};
			JavaCore.getPlugin().getPluginPreferences().addPropertyChangeListener(propertyListener);
			
			// Listen to content-type changes
			 Platform.getContentTypeManager().addContentTypeChangeListener(this);

			// retrieve variable values
			long start = -1;
			if (VERBOSE)
				start = System.currentTimeMillis();
 			loadVariablesAndContainers();
 			if (VERBOSE)
				traceVariableAndContainers("Loaded", start); //$NON-NLS-1$

			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.addResourceChangeListener(
				this.deltaState,
				/* update spec in JavaCore#addPreProcessingResourceChangedListener(...) if adding more event types */
				IResourceChangeEvent.PRE_BUILD
 IResourceChangeEvent.POST_BUILD
 IResourceChangeEvent.POST_CHANGE
 IResourceChangeEvent.PRE_DELETE
 IResourceChangeEvent.PRE_CLOSE);

			startIndexing();
			
			// process deltas since last activated in indexer thread so that indexes are up-to-date.
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=38658
			Job processSavedState = new Job(Messages.savedState_jobName) { 
				protected IStatus run(IProgressMonitor monitor) {
					try {
						// add save participant and process delta atomically
						// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=59937
						workspace.run(
							new IWorkspaceRunnable() {
								public void run(IProgressMonitor progress) throws CoreException {
									ISavedState savedState = workspace.addSaveParticipant(JavaCore.getJavaCore(), JavaModelManager.this);
									if (savedState != null) {
										// the event type coming from the saved state is always POST_AUTO_BUILD
										// force it to be POST_CHANGE so that the delta processor can handle it
										JavaModelManager.this.deltaState.getDeltaProcessor().overridenEventType = IResourceChangeEvent.POST_CHANGE;
										savedState.processResourceChangeEvents(JavaModelManager.this.deltaState);
									}
								}
							},
							monitor);
					} catch (CoreException e) {
						return e.getStatus();
					}
					return Status.OK_STATUS;
				}
			};
			processSavedState.setSystem(true);
			processSavedState.setPriority(Job.SHORT); // process asap
			processSavedState.schedule();
		} catch (RuntimeException e) {
			shutdown();
			throw e;
		}
	}

	/**
	 * Initiate the background indexing process.
	 * This should be deferred after the plugin activation.
	 */
	private void startIndexing() {
		getIndexManager().reset();
	}

	public void shutdown () {
		JavaCore javaCore = JavaCore.getJavaCore();
		javaCore.savePluginPreferences();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(this.deltaState);
		workspace.removeSaveParticipant(javaCore);
		
		// Stop listening to content-type changes
		Platform.getContentTypeManager().removeContentTypeChangeListener(this);
	
		if (this.indexManager != null){ // no more indexing
			this.indexManager.shutdown();
		}
		
		// wait for the initialization job to finish
		try {
			Job.getJobManager().join(JavaCore.PLUGIN_ID, null);
		} catch (InterruptedException e) {
			// ignore
		}
		
		// Note: no need to close the Java model as this just removes Java element infos from the Java model cache
	}
		
	public synchronized IPath variableGet(String variableName){
		// check initialization in progress first
		HashSet initializations = variableInitializationInProgress();
		if (initializations.contains(variableName)) {
			return VARIABLE_INITIALIZATION_IN_PROGRESS;
		}
		return (IPath)this.variables.get(variableName);
	}

	/*
	 * Returns the set of variable names that are being initialized in the current thread.
	 */
	private HashSet variableInitializationInProgress() {
		HashSet initializations = (HashSet)this.variableInitializationInProgress.get();
		if (initializations == null) {
			initializations = new HashSet();
			this.variableInitializationInProgress.set(initializations);
		}
		return initializations;
	}

	public synchronized String[] variableNames(){
		int length = this.variables.size();
		String[] result = new String[length];
		Iterator vars = this.variables.keySet().iterator();
		int index = 0;
		while (vars.hasNext()) {
			result[index++] = (String) vars.next();
		}
		return result;
	}

	public synchronized void variablePut(String variableName, IPath variablePath){		

		// set/unset the initialization in progress
		HashSet initializations = variableInitializationInProgress();
		if (variablePath == VARIABLE_INITIALIZATION_IN_PROGRESS) {
			initializations.add(variableName);
			
			// do not write out intermediate initialization value
			return;
		} else {
			initializations.remove(variableName);

			// update cache - do not only rely on listener refresh		
			if (variablePath == null) {
				// if path is null, record that the variable was removed to avoid asking the initializer to initialize it again
				// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=112609
				this.variables.put(variableName, CP_ENTRY_IGNORE_PATH);
			} else {
				this.variables.put(variableName, variablePath);
			}
			// discard obsoleted information about previous session
			this.previousSessionVariables.remove(variableName);
		}
	}

	public void variablePreferencesPut(String variableName, IPath variablePath) {
		String variableKey = CP_VARIABLE_PREFERENCES_PREFIX+variableName;
		if (variablePath == null) {
			this.variablesWithInitializer.remove(variableName);
			getInstancePreferences().remove(variableKey);
		} else {
			getInstancePreferences().put(variableKey, variablePath.toString());
		}
		try {
			getInstancePreferences().flush();
		} catch (BackingStoreException e) {
			// ignore exception
		}
	}

	public void contentTypeChanged(ContentTypeChangeEvent event) {
		Util.resetJavaLikeExtensions();
		
	}

	public synchronized String cacheToString(String prefix) {
		return this.cache.toStringFillingRation(prefix);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8668.java