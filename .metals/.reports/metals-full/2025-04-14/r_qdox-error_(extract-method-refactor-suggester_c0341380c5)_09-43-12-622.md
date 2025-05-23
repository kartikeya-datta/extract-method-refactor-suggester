error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10455.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10455.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10455.java
text:
```scala
a@@jOptions.noWeave,ajOptions.proceedOnError,ajOptions.noAtAspectJProcessing);

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.aspectj.ajdt.internal.core.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.aspectj.ajdt.core.AspectJCore;
import org.aspectj.ajdt.internal.compiler.AjCompilerAdapter;
import org.aspectj.ajdt.internal.compiler.IBinarySourceProvider;
import org.aspectj.ajdt.internal.compiler.IIntermediateResultsRequestor;
import org.aspectj.ajdt.internal.compiler.IOutputClassFileNameProvider;
import org.aspectj.ajdt.internal.compiler.InterimCompilationResult;
import org.aspectj.ajdt.internal.compiler.lookup.AjLookupEnvironment;
import org.aspectj.ajdt.internal.compiler.lookup.EclipseFactory;
import org.aspectj.ajdt.internal.compiler.problem.AjProblemReporter;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.IMessage.Kind;
import org.aspectj.weaver.Lint;
import org.aspectj.weaver.bcel.BcelWeaver;
import org.aspectj.weaver.bcel.BcelWorld;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.aspectj.org.eclipse.jdt.core.IJavaModelMarker;
import org.aspectj.org.eclipse.jdt.core.JavaCore;
import org.aspectj.org.eclipse.jdt.core.JavaModelException;
import org.aspectj.org.eclipse.jdt.internal.compiler.CompilationResult;
import org.aspectj.org.eclipse.jdt.internal.compiler.Compiler;
import org.aspectj.org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.aspectj.org.eclipse.jdt.internal.compiler.ICompilerAdapter;
import org.aspectj.org.eclipse.jdt.internal.compiler.ICompilerAdapterFactory;
import org.aspectj.org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.aspectj.org.eclipse.jdt.internal.core.builder.BatchImageBuilder;
import org.aspectj.org.eclipse.jdt.internal.core.builder.BuildNotifier;
import org.aspectj.org.eclipse.jdt.internal.core.builder.IncrementalImageBuilder;
import org.aspectj.org.eclipse.jdt.internal.core.builder.JavaBuilder;

/**
 * @author colyer
 *
 * This is the builder class used by AJDT, and that the org.eclipse.ajdt.core
 * plugin references.
 */
public class AspectJBuilder extends JavaBuilder implements ICompilerAdapterFactory {
	
	
	// One builder instance per project  (important)
	private BcelWeaver myWeaver = null;
	private BcelWorld myBcelWorld = null;
	private EclipseClassPathManager cpManager = null;
	private UnwovenResultCollector unwovenResultCollector = null;
	private OutputFileNameProvider fileNameProvider = null;
		
	private boolean isBatchBuild = false;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map ignored, IProgressMonitor monitor)
			throws CoreException {
		// super method always causes construction of a new XXXImageBuilder, which
		// causes construction of a new Compiler, so we will be detected as the 
		// adapter.
		Compiler.setCompilerAdapterFactory(this);
		return super.build(kind, ignored, monitor);
	}

	protected BatchImageBuilder getBatchImageBuilder() {
		isBatchBuild = true;
		return new AjBatchImageBuilder(this);
	}
	
	protected IncrementalImageBuilder getIncrementalImageBuilder() {
		isBatchBuild = false;
		return new AjIncrementalImageBuilder(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.ICompilerAdapterFactory#getAdapter(org.eclipse.jdt.internal.compiler.Compiler)
	 */
	public ICompilerAdapter getAdapter(Compiler forCompiler) {
		Map javaOptions = forCompiler.options.getMap();
		// TODO get aspectj options from project and add into map before...
		AjCompilerOptions ajOptions = new AjCompilerOptions(javaOptions);
		forCompiler.options = ajOptions;
		
		if (isBatchBuild || myBcelWorld == null || myWeaver == null) {
			initWorldAndWeaver(ajOptions);
		} else {
			// update the nameEnvironment each time we compile...
			cpManager.setNameEnvironment(nameEnvironment);
		}
		
		// * an eclipse factory  -- create from AjLookupEnvironment, need to hide AjBuildManager field
		AjProblemReporter pr =
			new AjProblemReporter(DefaultErrorHandlingPolicies.proceedWithAllProblems(),
								  forCompiler.options, new DefaultProblemFactory(Locale.getDefault()));		
		forCompiler.problemReporter = pr;			
		AjLookupEnvironment le =
			new AjLookupEnvironment(forCompiler, forCompiler.options, pr,nameEnvironment);		
		EclipseFactory eFactory = new EclipseFactory(le,myBcelWorld,ajOptions.xSerializableAspects);
		le.factory = eFactory;
		forCompiler.lookupEnvironment = le;
		
		AjBuildNotifier ajNotifier = (AjBuildNotifier) notifier;
		if (fileNameProvider == null ) fileNameProvider = new OutputFileNameProvider(getProject());
		
		// * the set of binary source entries for this compile  -- from analyzing deltas, or everything if batch
		// * the full set of binary source entries for the project -- from IAspectJProject
		// TODO deal with inpath, injars here...
		IBinarySourceProvider bsProvider = new NullBinarySourceProvider();
		Map fullBinarySourceEntries = new HashMap();

		// * the intermediate result set from the last batch compile
		if (isBatchBuild) {
			unwovenResultCollector = new UnwovenResultCollector();
		}
		Collection resultSetForFullWeave = unwovenResultCollector.getIntermediateResults();
		
		return new AjCompilerAdapter(forCompiler,isBatchBuild,myBcelWorld,
				     myWeaver,eFactory,unwovenResultCollector,ajNotifier,fileNameProvider,bsProvider,
					 fullBinarySourceEntries,resultSetForFullWeave,
					 ajOptions.noWeave,ajOptions.proceedOnError);
	}		
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.core.builder.JavaBuilder#createBuildNotifier(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.resources.IProject)
	 */
	protected BuildNotifier createBuildNotifier(IProgressMonitor monitor,
			IProject currentProject) {
		return new AjBuildNotifier(monitor, currentProject);
	}

	
	private void initWorldAndWeaver(AjCompilerOptions options) {
		cpManager = new EclipseClassPathManager(nameEnvironment);
		myBcelWorld = new BcelWorld(cpManager,new UnhandledMessageHandler(getProject()),null /*(xrefHandler)*/);
		myBcelWorld.setBehaveInJava5Way(options.behaveInJava5Way);
		myBcelWorld.setXnoInline(options.xNoInline);
		myBcelWorld.setXlazyTjp(options.xLazyThisJoinPoint);
		setLintProperties(myBcelWorld,options);
		myWeaver = new BcelWeaver(myBcelWorld);
		myWeaver.setReweavableMode(options.xReweavable,options.xReweavableCompress);
		// TODO deal with injars, inpath, and aspectpath here...
	}
	
	private void setLintProperties(BcelWorld world, AjCompilerOptions options) {
		Properties p = new Properties();
		Lint lintSettings = world.getLint();
		Map map = options.getMap();
		p.put(lintSettings.invalidAbsoluteTypeName.getName(),map.get(AjCompilerOptions.OPTION_ReportInvalidAbsoluteTypeName));
		p.put(lintSettings.invalidWildcardTypeName.getName(),map.get(AjCompilerOptions.OPTION_ReportInvalidWildcardTypeName));
		p.put(lintSettings.unresolvableMember.getName(),map.get(AjCompilerOptions.OPTION_ReportUnresolvableMember));
		p.put(lintSettings.typeNotExposedToWeaver.getName(),map.get(AjCompilerOptions.OPTION_ReportTypeNotExposedToWeaver));
		p.put(lintSettings.shadowNotInStructure.getName(),map.get(AjCompilerOptions.OPTION_ReportShadowNotInStructure));
		p.put(lintSettings.unmatchedSuperTypeInCall.getName(),map.get(AjCompilerOptions.OPTION_ReportUnmatchedSuperTypeInCall));
		p.put(lintSettings.canNotImplementLazyTjp.getName(),map.get(AjCompilerOptions.OPTION_ReportCannotImplementLazyTJP));
		p.put(lintSettings.needsSerialVersionUIDField.getName(),map.get(AjCompilerOptions.OPTION_ReportNeedSerialVersionUIDField));
		p.put(lintSettings.serialVersionUIDBroken.getName(),map.get(AjCompilerOptions.OPTION_ReportIncompatibleSerialVersion));
		lintSettings.setFromProperties(p);
	}
	
	private static class UnwovenResultCollector implements IIntermediateResultsRequestor {

		private Collection results = new ArrayList();
		
		/* (non-Javadoc)
		 * @see org.aspectj.ajdt.internal.compiler.IIntermediateResultsRequestor#acceptResult(org.aspectj.ajdt.internal.compiler.InterimCompilationResult)
		 */
		public void acceptResult(InterimCompilationResult intRes) {
			results.add(intRes);
		}
		
		public Collection getIntermediateResults() {
			return results;
		}
		
	}
	
	// this class will only get messages that the weaver adapter couldn't tie into
	// an originating resource in the project - make them messages on the project
	// itself.
	private static class UnhandledMessageHandler implements IMessageHandler {
		
		private IProject project;
		
		public UnhandledMessageHandler(IProject p) {
			this.project = p;
		}

		/* (non-Javadoc)
		 * @see org.aspectj.bridge.IMessageHandler#handleMessage(org.aspectj.bridge.IMessage)
		 */
		public boolean handleMessage(IMessage message) throws AbortException {
			try {
				IMarker marker = project.createMarker(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER);
				marker.setAttribute(IMarker.MESSAGE, message.getMessage()); 
				marker.setAttribute(IMarker.SEVERITY, message.isError() ? IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING);
			} catch (CoreException e) {
				AspectJCore.getPlugin().getLog().log(e.getStatus());
			}
			return true;
		}

		/* (non-Javadoc)
		 * @see org.aspectj.bridge.IMessageHandler#isIgnoring(org.aspectj.bridge.IMessage.Kind)
		 */
		public boolean isIgnoring(Kind kind) {
			if (kind == IMessage.DEBUG || kind == IMessage.INFO) return true;
			return false;
		}
		
	}
	
	
	private static class OutputFileNameProvider implements IOutputClassFileNameProvider {

		private IPath outputLocation;
		
		public OutputFileNameProvider(IProject p) {
			try {
				outputLocation = JavaCore.create(p).getOutputLocation();
			} catch (JavaModelException e) {
				outputLocation = new Path(".");
				AspectJCore.getPlugin().getLog().log(e.getStatus());				
			}
		}
		
		/* (non-Javadoc)
		 * @see org.aspectj.ajdt.internal.compiler.IOutputClassFileNameProvider#getOutputClassFileName(char[], org.eclipse.jdt.internal.compiler.CompilationResult)
		 */
		public String getOutputClassFileName(char[] eclipseClassFileName, CompilationResult result) {
			// In the AJDT implementation, the name provided here will be ignored, we write the results
			// out in xxxImageBuilder.acceptResult() instead.
			// simply return the default output directory for the project.
			String filename = new String(eclipseClassFileName);
			IPath out = outputLocation.append(filename);
			out.addFileExtension(".class");
			return out.toOSString();
		}
		
	}

	// default impl class until the implementation is extended to cope with inpath, injars
	private static class NullBinarySourceProvider implements IBinarySourceProvider {

		/* (non-Javadoc)
		 * @see org.aspectj.ajdt.internal.compiler.IBinarySourceProvider#getBinarySourcesForThisWeave()
		 */
		public Map getBinarySourcesForThisWeave() {
			return new HashMap();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10455.java