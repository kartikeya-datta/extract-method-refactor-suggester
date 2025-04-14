error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3140.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3140.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3140.java
text:
```scala
private static final S@@tring CONTRIBUTOR_ID = "org.eclipse.xtend.shared.ui.metamodelContributor";

/*******************************************************************************
 * Copyright (c) 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.xtend.shared.ui.core.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.MetamodelContributor;
import org.eclipse.xtend.shared.ui.core.preferences.PreferenceConstants;
import org.eclipse.xtend.shared.ui.internal.XtendLog;

/**
 * Provides access to metamodel contributors.
 * 
 * @author Peter
 */
public class MetamodelContributorRegistry {

	private static final String CONTRIBUTOR_ID = "org.eclipse.base.metamodelContributor";

	/**
	 * Retrieves the registered metamodel contributors.
	 * 
	 * @return A map containing all registered metamodel contributors.
	 */
	private static Map<String, Contributor> contributors = null;

	public static Map<String, Contributor> getRegisteredMetamodelContributors() {
		if (contributors == null) {
			contributors = new HashMap<String, Contributor>();
			final IExtensionRegistry registry = Platform.getExtensionRegistry();
			final IExtensionPoint point = registry.getExtensionPoint(MetamodelContributorRegistry.CONTRIBUTOR_ID);
			if (point != null) {
				final IExtension[] extensions = point.getExtensions();
				for (final IExtension extension : extensions) {
					final IConfigurationElement[] configs = extension.getConfigurationElements();
					for (final IConfigurationElement element : configs) {
						final String label = element.getAttribute("name");
						final String className = element.getAttribute("class");
						final Contributor contributor = new Contributor(label, className, element, false);
						contributors.put(className, contributor);
					}
				}
			}
		}
		return contributors;
	}

	public static List<? extends MetamodelContributor> getActiveMetamodelContributors(final IJavaProject project) {
		final ScopedPreferenceStore scopedPreferenceStore = new ScopedPreferenceStore(new InstanceScope(), Activator
				.getId());
		final IScopeContext[] scopes = { new ProjectScope(project.getProject()), new InstanceScope() };
		scopedPreferenceStore.setSearchContexts(scopes);
		fixMetamodelContributorPreferences(scopedPreferenceStore);

		final String metamodelContr = scopedPreferenceStore.getString(PreferenceConstants.METAMODELCONTRIBUTORS);
		final String[] mm = metamodelContr.split(",");

		final List<MetamodelContributor> result = new ArrayList<MetamodelContributor>();
		for (final String metamodelContributorName : mm) {
			if (metamodelContributorName != null && !metamodelContributorName.equals("")) {
				final MetamodelContributor metamodelContributor = MetamodelContributorRegistry
						.getMetamodelContributorByClassName(metamodelContributorName);
				if (metamodelContributor != null) {
					result.add(metamodelContributor);
				}
				else {
					XtendLog.logInfo("Metamodel contributor '" + metamodelContributorName + "' is not available\n");
				}
			}
		}
		return result;
	}

	/**
	 * fix: package name from UML2MetamodelContributor was wrong. For
	 * compatibility reasons we fix this name here on the fly. Later we could
	 * offer a migration path.
	 */
	private static void fixMetamodelContributorPreferences(final IPreferenceStore prefStore) {
		String metamodelContr = prefStore.getString(PreferenceConstants.METAMODELCONTRIBUTORS);
		if (metamodelContr.indexOf("openarchitecturware") > 0) {
			metamodelContr = metamodelContr.replace("openarchitecturware", "openarchitectureware");
			prefStore.setValue(PreferenceConstants.METAMODELCONTRIBUTORS, metamodelContr);
		}
	}

	private static MetamodelContributor getMetamodelContributorByClassName(final String metamodelContr) {
		final Contributor contributor = getRegisteredMetamodelContributors().get(metamodelContr);
		if (contributor == null)
			return null;
		return contributor.getMetaModelContributor();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3140.java