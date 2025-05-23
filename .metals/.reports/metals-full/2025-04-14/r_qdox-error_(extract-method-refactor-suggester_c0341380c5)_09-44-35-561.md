error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8538.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8538.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8538.java
text:
```scala
C@@ompilationResult result = new CompilationResult(sourceFileName.toCharArray(),0,0,compiler.options.maxProblemsPerUnit);

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
package org.aspectj.ajdt.internal.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.aspectj.ajdt.internal.compiler.lookup.EclipseFactory;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.IProgressListener;
import org.aspectj.weaver.bcel.BcelWeaver;
import org.aspectj.weaver.bcel.BcelWorld;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ICompilerAdapter;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

/**
 * @author colyer
 *
 * Adapts standard JDT Compiler to add in AspectJ specific behaviours.
 */
public class AjCompilerAdapter implements ICompilerAdapter {

	private Compiler compiler;
	private BcelWeaver weaver;
	private EclipseFactory eWorld;
	private boolean isBatchCompile;
	private boolean reportedErrors;
	private boolean isXNoWeave;
	private IIntermediateResultsRequestor intermediateResultsRequestor;
	private IProgressListener progressListener;
	private IOutputClassFileNameProvider outputFileNameProvider;
	private IBinarySourceProvider binarySourceProvider;
	private WeaverMessageHandler weaverMessageHandler;
	private Map /* fileName |-> List<UnwovenClassFile> */ binarySourceSetForFullWeave = new HashMap();
	private Collection /*InterimCompilationResult*/ resultSetForFullWeave = Collections.EMPTY_LIST;

	
	List /*InterimResult*/ resultsPendingWeave = new ArrayList();

	/**
	 * Create an adapter, and tell it everything it needs to now to drive the AspectJ
	 * parts of a compile cycle.
	 * @param compiler	the JDT compiler that produces class files from source
	 * @param isBatchCompile  true if this is a full build (non-incremental)
	 * @param world  the bcelWorld used for type resolution during weaving
	 * @param weaver the weaver
	 * @param intRequestor  recipient of interim compilation results from compiler (pre-weave)
	 * @param outputFileNameProvider implementor of a strategy providing output file names for results
	 * @param binarySourceEntries binary source that we didn't compile, but that we need to weave
	 * @param resultSetForFullWeave if we are doing an incremental build, and the weaver determines
	 *                              that we need to weave the world, this is the set of intermediate
	 *                              results that will be passed to the weaver.
	 * @param isXNoWeave
	 */
	public AjCompilerAdapter(Compiler compiler,
							 boolean isBatchCompile,
							 BcelWorld world,
							 BcelWeaver weaver,
							 EclipseFactory eFactory,
							 IIntermediateResultsRequestor intRequestor,
							 IProgressListener progressListener,
							 IOutputClassFileNameProvider outputFileNameProvider,
							 IBinarySourceProvider binarySourceProvider,
							 Map fullBinarySourceEntries, /* fileName |-> List<UnwovenClassFile> */
							 Collection /* InterimCompilationResult */ resultSetForFullWeave,
							 boolean isXNoWeave) {
		this.compiler = compiler;
		this.isBatchCompile = isBatchCompile;
		this.weaver = weaver;
		this.intermediateResultsRequestor = intRequestor;
		this.progressListener = progressListener;
		this.outputFileNameProvider = outputFileNameProvider;
		this.binarySourceProvider = binarySourceProvider;
		this.isXNoWeave = isXNoWeave;
		this.binarySourceSetForFullWeave = fullBinarySourceEntries;
		this.resultSetForFullWeave = resultSetForFullWeave;
		this.eWorld = eFactory;
		
		IMessageHandler msgHandler = world.getMessageHandler();
		weaverMessageHandler = new WeaverMessageHandler(msgHandler, compiler);
		world.setMessageHandler(weaverMessageHandler);
	}
	
	public void beforeCompiling(ICompilationUnit[] sourceUnits) {
		resultsPendingWeave = new ArrayList();
		reportedErrors = false;		
	}

	public void afterCompiling() {
		try {
			if (isXNoWeave || reportedErrors) {
				// no point weaving... just tell the requestor we're done
				notifyRequestor();
			} else {
				weave();  // notification happens as weave progresses...
			}
		} catch (IOException ex) {
			AbortCompilation ac = new AbortCompilation(null,ex);
			throw ac;
		} catch (RuntimeException rEx) {
			if (rEx instanceof AbortCompilation) throw rEx; // Don't wrap AbortCompilation exceptions!

			// This will be unwrapped in Compiler.handleInternalException() and the nested
			// RuntimeException thrown back to the original caller - which is AspectJ
			// which will then then log it as a compiler problem.
			throw new AbortCompilation(true,rEx);
		}
	}

	public void beforeProcessing(CompilationUnitDeclaration unit) {
		eWorld.showMessage(IMessage.INFO, "compiling " + new String(unit.getFileName()), null, null);
	}

	public void afterProcessing(CompilationUnitDeclaration unit, int unitIndex) {
		eWorld.finishedCompilationUnit(unit);
		InterimCompilationResult intRes = new InterimCompilationResult(unit.compilationResult,outputFileNameProvider);
		if (unit.compilationResult.hasErrors()) reportedErrors = true;
		
		if (intermediateResultsRequestor != null) {
			intermediateResultsRequestor.acceptResult(intRes);
		}
		
		if (isXNoWeave) {
			acceptResult(unit.compilationResult);
		} else {
			resultsPendingWeave.add(intRes);
		}
	}
	
	public void beforeResolving(CompilationUnitDeclaration unit, ICompilationUnit sourceUnit, boolean verifyMethods, boolean analyzeCode, boolean generateCode) {
		resultsPendingWeave = new ArrayList();
		reportedErrors = false;		
	}

	public void afterResolving(CompilationUnitDeclaration unit, ICompilationUnit sourceUnit, boolean verifyMethods, boolean analyzeCode, boolean generateCode) {
		InterimCompilationResult intRes = new InterimCompilationResult(unit.compilationResult,outputFileNameProvider);
		if (unit.compilationResult.hasErrors()) reportedErrors = true;
		if (isXNoWeave || !generateCode) {
			acceptResult(unit.compilationResult);
		} else if (generateCode){
			resultsPendingWeave.add(intRes);
			try {
			  weave();
			} catch (IOException ex) {
				AbortCompilation ac = new AbortCompilation(null,ex);
				throw ac;
			} 
		}
	}
	
	// helper methods...
	// ==================================================================================
	
	/*
	 * Called from the weaverAdapter once it has finished weaving the class files
	 * associated with a given compilation result.
	 */
	public void acceptResult(CompilationResult result) {
		compiler.requestor.acceptResult(result.tagAsAccepted());
		if (compiler.unitsToProcess != null) {
			for (int i = 0; i < compiler.unitsToProcess.length; i++) {
				if (compiler.unitsToProcess[i] != null) {
					if (compiler.unitsToProcess[i].compilationResult == result) {
						compiler.unitsToProcess[i] = null;
					}
				}
			}
		}
	}
	
	private List getBinarySourcesFrom(Map binarySourceEntries) {
		// Map is fileName |-> List<UnwovenClassFile>
		List ret = new ArrayList();
		for (Iterator binIter = binarySourceEntries.keySet().iterator(); binIter.hasNext();) {
			String sourceFileName = (String) binIter.next();
			List unwovenClassFiles = (List) binarySourceEntries.get(sourceFileName);
			
			CompilationResult result = new CompilationResult(sourceFileName.toCharArray(),0,0,20);
			result.noSourceAvailable();
			InterimCompilationResult binarySource = 
				new InterimCompilationResult(result,unwovenClassFiles);
			ret.add(binarySource);
		}
		return ret;
	}
	
	private void notifyRequestor() {
		for (Iterator iter = resultsPendingWeave.iterator(); iter.hasNext();) {
			InterimCompilationResult iresult = (InterimCompilationResult) iter.next();
			compiler.requestor.acceptResult(iresult.result().tagAsAccepted());
		}
	}
		
	private void weave() throws IOException {
		// ensure weaver state is set up correctly
		for (Iterator iter = resultsPendingWeave.iterator(); iter.hasNext();) {
			InterimCompilationResult iresult = (InterimCompilationResult) iter.next();
			for (int i = 0; i < iresult.unwovenClassFiles().length; i++) {
				weaver.addClassFile(iresult.unwovenClassFiles()[i]);
			}			
		}

		weaver.prepareForWeave();
		if (weaver.needToReweaveWorld()) {
			if (!isBatchCompile) addAllKnownClassesToWeaveList(); // if it's batch, they're already on the list...
			resultsPendingWeave.addAll(getBinarySourcesFrom(binarySourceSetForFullWeave));
		} else {
			Map binarySourcesToAdd = binarySourceProvider.getBinarySourcesForThisWeave();
			resultsPendingWeave.addAll(getBinarySourcesFrom(binarySourcesToAdd));
		}

//		if (isBatchCompile) {
//			resultsPendingWeave.addAll(getBinarySourcesFrom(binarySourceSetForFullWeave));  
//			// passed into the compiler, the set of classes in injars and inpath...
//		} else if (weaver.needToReweaveWorld()) {
//			addAllKnownClassesToWeaveList();
//			resultsPendingWeave.addAll(getBinarySourcesFrom(binarySourceSetForFullWeave));
//		}

		weaver.weave(new WeaverAdapter(this,weaverMessageHandler,progressListener));
	}
	
	private void addAllKnownClassesToWeaveList() {
		// results pending weave already has some results from this (incremental) compile
		// add in results from any other source
		for (Iterator iter = resultSetForFullWeave.iterator(); iter.hasNext();) {
			InterimCompilationResult ir = (InterimCompilationResult) iter.next();
			if (!resultsPendingWeave.contains(ir)) {  // equality based on source file name...
				ir.result().hasBeenAccepted = false;  // it may have been accepted before, start again
				resultsPendingWeave.add(ir);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8538.java