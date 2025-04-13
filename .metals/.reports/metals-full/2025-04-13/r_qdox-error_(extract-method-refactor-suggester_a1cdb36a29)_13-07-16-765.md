error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4507.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4507.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4507.java
text:
```scala
n@@ew BuildJob(project.getProject(), null).schedule();

/*******************************************************************************
 * Copyright (c) 2005, 2009 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.xtend.typesystem.emf.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.converter.util.ConverterUtil;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ExternalPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.core.internal.JDTUtil;
import org.eclipse.xtend.shared.ui.core.internal.ResourceID;
import org.eclipse.xtend.typesystem.emf.ui.internal.EmfToolsLog;

/**
 * Analyzes a project's classpath for ecore metamodels. We need to take care of
 * Ecore files
 * <ul>
 * <li>directly contained in the classpath</li>
 * <li>in JARs within the classpath</li>
 * <li>in the classpath of projects referenced by this project (recursively)</li>
 * <li>in plugins referenced by the project, either within the target platform
 * or as referenced plugin projects (recursively)</li>
 * </ul>
 * <p>
 * Reading Ecore files occurs in background Jobs. Each Job uses its own
 * ResourceSet. This avoids concurrency issues.
 * </p>
 */
@SuppressWarnings("restriction")
final class ProjectAnalyzer extends Job {
	private final IJavaProject project;
	private ResourceSet rs;
	private Map<IStorage, Resource> mapping;
	private Map<String, EPackage> packages;

	public ProjectAnalyzer(final IJavaProject project) {
		super(Messages.ProjectAnalyzer_AnalysingPrompt + project.getProject().getProject().getName());
		// see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=268516
		setRule(project.getSchedulingRule());
		this.project = project;
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		if (EmfToolsPlugin.trace) {
			System.out.println(Messages.ProjectAnalyzer_1 + project.getProject().getProject().getName());
		}

		// load models
		rs = ConverterUtil.createResourceSet();
		mapping = new HashMap<IStorage, Resource>();
		// TODO: ensure that the packages map contains the ePackages from reexported projects as well
		packages = new HashMap<String, EPackage>();
		loadMetamodelsForProject(project, rs, monitor);
		// always add ecore
		packages.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		
		EPackage.Registry registry = new EPackageRegistryImpl(rs.getPackageRegistry());
		registry.putAll(packages);
		rs.setPackageRegistry(registry);
		
		// done. now trigger build for the project.
		// only do this if it is an Xpand project
		// do not build referencing projects. the EmfToolsPlugin will take care
		// of this
		if (Activator.getExtXptModelManager().findProject(project.getProject()) != null) {
			new BuildJob(project.getProject()).schedule();
		}

		return Status.OK_STATUS;
	}

	private void loadMetamodelsForProject(final IJavaProject javaProject, final ResourceSet rs,
			final IProgressMonitor monitor) {
		try {
			final String ext = Messages.ProjectAnalyzer_2;
			for (final IPackageFragmentRoot root : javaProject.getPackageFragmentRoots()) {
				if (!root.isArchive()) {
					IResource rootResource = null;
					if (root instanceof ExternalPackageFragmentRoot) {
						rootResource = ((ExternalPackageFragmentRoot) root).resource();
					}
					else {
						rootResource = root.getUnderlyingResource();
					}
					if (rootResource != null) {
						try {
							if (!rootResource.exists())
								rootResource.refreshLocal(IResource.DEPTH_INFINITE, monitor);
							rootResource.accept(new IResourceVisitor() {
								public boolean visit(final IResource resource) throws CoreException {
									if (resource instanceof IFile && ext.equals(((IFile) resource).getFileExtension())) {
										loadModelFromStorage(rs, (IFile) resource);
									}
									return true;
								}
							});
						}
						catch (final CoreException e) {
							EmfToolsLog.logError(e);
						}
					}
				}
				else {
					// skip JRE jars
					if (((JarPackageFragmentRoot) root).getPath().toString().contains(Messages.ProjectAnalyzer_3)) {
						if (EmfToolsPlugin.trace) {
							System.out.println(Messages.ProjectAnalyzer_4 + ((JarPackageFragmentRoot) root).getPath().toString());
						}
						continue;
					}

					root.open(monitor);
					try {
						final ZipFile zip = ((JarPackageFragmentRoot) root).getJar();

						final Enumeration<? extends ZipEntry> entries = zip.entries();
						while (entries.hasMoreElements()) {
							final ZipEntry entry = entries.nextElement();
							final String name = entry.getName();
							if (name.endsWith(ext)) {
								final String fqn = name.substring(0, name.length() - ext.length() - 1).replaceAll(Messages.ProjectAnalyzer_5,
										Messages.ProjectAnalyzer_6);
								final ResourceID resourceID = new ResourceID(fqn, ext);
								final IStorage findStorage = JDTUtil.loadFromJar(resourceID, root);
								if (findStorage != null) {
									loadModelFromStorage(rs, findStorage);
								}
							}
						}
					}
					catch (final CoreException e) {
						EmfToolsLog.logError(e);
					}
					finally {
						root.close();
					}
				}
			}
		}
		catch (final JavaModelException e) {
			EmfToolsLog.logError(e);
		}
	}

	private void loadModelFromStorage(final ResourceSet rs, final IStorage storage) {
		final URI uri = URI.createPlatformResourceURI(storage.getFullPath().toString(), true);
		if (EmfToolsPlugin.trace) {
			System.out.println(Messages.ProjectAnalyzer_7 + storage.getFullPath().toString());
		}

		final Resource r = rs.createResource(uri);
		if (r.isLoaded() && !r.isModified())
			return;
		try {
			r.load(storage.getContents(), Collections.EMPTY_MAP);
			mapping.put(storage, r);
			registerEPackages(rs, r, false);
		}
		catch (final IOException e) {
			EmfToolsLog.logError(Messages.ProjectAnalyzer_8 + uri, e);
		}
		catch (final CoreException e) {
			EmfToolsLog.logError(Messages.ProjectAnalyzer_9 + uri, e);
		}
		catch (final RuntimeException e) {
			EmfToolsLog.logError(Messages.ProjectAnalyzer_10 + uri, e);
		}
	}

	private void registerEPackages(final ResourceSet rs, final Resource r, boolean overwrite) {
		final Collection<EPackage> packages = EcoreUtil.getObjectsByType(r.getContents(),
				EcorePackage.Literals.EPACKAGE);
		for (final EPackage pack : packages) {
			registerPackage(pack, rs, overwrite);
		}
	}

	private void registerPackage(final EPackage pack, ResourceSet rs, boolean overwrite) {
		// finding duplicates by nsURI is better than by name since package
		// names may be used across MMs
		if (!overwrite && packages.containsKey(pack.getNsURI())) {
			if (EmfToolsPlugin.trace) {
				System.out.println(Messages.ProjectAnalyzer_11 + pack.getName()
						+ Messages.ProjectAnalyzer_12);
			}
		}
		else {
			packages.put(pack.getNsURI(), pack);
		}
		// recurse into subpackages
		for (final EPackage p : pack.getESubpackages()) {
			registerPackage(p, rs, overwrite);
		}
	}

	public Map<String, EPackage> getNamedEPackageMap() {
		if (packages == null) {
			run(new NullProgressMonitor());
		}
		return Collections.unmodifiableMap(packages);
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4507.java