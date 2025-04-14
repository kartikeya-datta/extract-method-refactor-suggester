error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/99.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/99.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/99.java
text:
```scala
I@@ssues issuesImpl = new IssuesImpl();

/**
 * <copyright> 
 *
 * Copyright (c) 2008 itemis AG and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   itemis AG - Initial API and implementation
 *
 * </copyright>
 *
 */
package org.eclipse.emf.editor.extxpt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.editor.EEPlugin;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.issues.IssuesImpl;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.internal.xtend.xtend.ast.ExtensionFile;
import org.eclipse.xtend.XtendFacade;
import org.eclipse.xtend.check.CheckUtils;
import org.eclipse.xtend.expression.EvaluationException;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.core.IXtendXpandProject;
import org.eclipse.xtend.shared.ui.core.IXtendXpandResource;

/**
 * @author Dennis Hübner - Initial contribution and API
 * 
 */
public class ExtXptFacade {

	private IProject project;
	private final ExecutionContext context;
	public static final String CHECK_EXT = "Checks";
	public static final String STYLE_EXT = "ItemLabelProvider";
	public static final String PROPOSAL_EXT = "Proposals";

	public ExtXptFacade(IProject project, ExecutionContext context) {
		this.project = project;
		this.context = context;
	}

	public Object style(String extension, EObject object) {
		String extendFile = path(object) + ExtXptFacade.STYLE_EXT;
		Object retVal = evaluate(extendFile, extension, object);
		return retVal;
	}

	/**
	 * @param extensionFile
	 * @param extensionName
	 * @param params
	 * @return
	 */
	private Object evaluate(String extensionFile, String extensionName, Object... params) {
		Object retVal = null;
		try {
			XtendFacade facade = XtendFacade.create(context, extensionFile);
			retVal = facade.call(extensionName, params);
		}
		catch (IllegalArgumentException e) {
			// no extension specified
		}
		catch (EvaluationException e) {
			EEPlugin.logError("Exception during extension evaluation", e);
		}
		catch (RuntimeException e) {
			// TODO check file exists
			// extension file not found
		}
		catch (Throwable e) {
			EEPlugin.logError("Exception during extension evaluation", new RuntimeException(e));
		}
		return retVal;
	}

	// TODO split method
	public List<?> proposals(EStructuralFeature feature, EObject ctx, List<?> fromList) {
		String extFile = path(ctx) + ExtXptFacade.PROPOSAL_EXT;
		List<?> retVal = new ArrayList<Object>();
		Object eval;
		if (fromList != null) {
			retVal = fromList;
			eval = evaluate(extFile, feature.getName(), ctx, fromList);
		}
		else {
			eval = evaluate(extFile, feature.getName(), ctx);
		}
		if (eval != null) {
			if (eval instanceof List<?>) {
				retVal = (List<?>) eval;
			}
			else {
				EEPlugin.logError("Returned type must be a List! File:" + extFile + ", Extension:" + feature.getName());
			}
		}
		return retVal;
	}

	public Issues check(EObject rootObject) {
		String checkFile = path(rootObject) + ExtXptFacade.CHECK_EXT;
		List<EObject> all = new ArrayList<EObject>();
		all.add(rootObject);
		EObject rootContainer = EcoreUtil.getRootContainer(rootObject);
		TreeIterator<EObject> iter = rootContainer.eAllContents();
		while (iter.hasNext())
			all.add(iter.next());
		IssuesImpl issuesImpl = new IssuesImpl();
		IXtendXpandProject extxptProject = Activator.getExtXptModelManager().findProject(project);
		if (extxptProject != null) {
			IXtendXpandResource extxptResource = extxptProject.findExtXptResource(checkFile, CheckUtils.FILE_EXTENSION);
			if (extxptResource != null) {
				ExtensionFile file = (ExtensionFile) extxptResource.getExtXptResource();
				try {
					file.check(context, all, issuesImpl, false);
				}
				catch (IllegalArgumentException e) {
					// no extension specified
				}
				catch (Exception e) {
					EEPlugin.logError("Exception during check evaluation", e);
				}
			}
		}
		else {
			EEPlugin.logWarning("Enable Xtend/Xpand-Nature for '" + project.getName() + "' to check models.");
		}
		return issuesImpl;
	}

	private String path(EObject object) {
		return object.eClass().getEPackage().getName() + SyntaxConstants.NS_DELIM;
	}

	public IProject getProject() {
		return project;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/99.java