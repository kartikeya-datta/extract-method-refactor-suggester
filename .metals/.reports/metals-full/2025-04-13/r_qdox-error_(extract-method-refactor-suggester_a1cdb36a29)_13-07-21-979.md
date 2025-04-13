error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7796.java
text:
```scala
public static S@@tring NewExtXptResourceWizardPage_Error;

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

package org.eclipse.xtend.shared.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.xtend.shared.ui.messages"; //$NON-NLS-1$
	public static String AbstractExtXptContentAssistProcessor_Error;
	public static String AbstractExtXptContentAssistProcessor_Prompt;
	public static String Activator_AnalyzingProgress;
	public static String EclipseHelper_OvewritePrompt;
	public static String EclipseHelper_TaskTitle;
	public static String EclipseHelper_Warning1;
	public static String EclipseHelper_Warning2;
	public static String EnableDisableBreakpointAction_Description;
	public static String EnableDisableBreakpointAction_DisableAction;
	public static String EnableDisableBreakpointAction_EnableAction;
	public static String EnableDisableBreakpointAction_Error;
	public static String FindDeclarationsAction_Description;
	public static String FindDeclarationsAction_Label;
	public static String FindReferencesAction_Description;
	public static String FindReferencesAction_Label;
	public static String MetamodelContributorsPropertyAndPreferencePage_19;
	public static String MetamodelContributorsPropertyAndPreferencePage_Analyzing;
	public static String MetamodelContributorsPropertyAndPreferencePage_ContributorsLabel;
	public static String MetamodelContributorsPropertyAndPreferencePage_CurrentAndDependentProjects;
	public static String MetamodelContributorsPropertyAndPreferencePage_DownButton;
	public static String MetamodelContributorsPropertyAndPreferencePage_FileOnly;
	public static String MetamodelContributorsPropertyAndPreferencePage_IncrementalAnalysisLabel;
	public static String MetamodelContributorsPropertyAndPreferencePage_JobName;
	public static String MetamodelContributorsPropertyAndPreferencePage_UpButton;
	public static String MetamodelContributorsPropertyAndPreferencePage_WholeProject;
	public static String NewExtXptResourceWizard_Title;
	public static String NewOAWResourcePage_Error;
	public static String OpenAction_ActionName;
	public static String ReferencesSearchGroup_Label;
	public static String SearchActionGroup_Name;
	public static String ToggleBreakpointAction_Description;
	public static String ToggleBreakpointAction_ToggleAction;
	public static String XtendXpandMarkerManager_Line;
	public static String XtendXpandModelManager_AnalyzingPrompt;
	public static String XtendXpandProblemHover_MultipleMarkers;
	public static String XtendXpandProjectWizard_ErrorLabel;
	public static String XtendXpandProjectWizard_ProjectCreationMessage;
	public static String XtendXpandProjectWizardPage_DefaultFileName;
	public static String XtendXpandProjectWizardPage_Description;
	public static String XtendXpandProjectWizardPage_Error;
	public static String XtendXpandProjectWizardPage_Name;
	public static String XtendXpandProjectWizardPage_ProjectLabel;
	public static String XtendXpandProjectWizardPage_SampleLabel;
	public static String XtendXpandProjectWizardPage_Title;
	public static String XtendXpandSearchQuery_Label;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7796.java