error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10404.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10404.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10404.java
text:
```scala
s@@Loc != null ? sLoc.getLine() : 0,

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

import java.util.List;

import org.aspectj.ajdt.internal.compiler.lookup.EclipseSourceLocation;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.IMessage.Kind;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

/**
 * @author colyer
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WeaverMessageHandler implements IMessageHandler {
	private IMessageHandler sink;
	private CompilationResult currentlyWeaving;
	private Compiler compiler;
	
	public WeaverMessageHandler(IMessageHandler handler, Compiler compiler) {
		this.sink = handler;
		this.compiler = compiler;
	}
	
	public void setCurrentResult(CompilationResult result) {
		currentlyWeaving = result;
	}

	public boolean handleMessage(IMessage message) throws AbortException {
		if (! (message.isError() || message.isWarning()) ) return sink.handleMessage(message);
		// we only care about warnings and errors here...
		ISourceLocation sLoc = message.getSourceLocation();
		CompilationResult problemSource = currentlyWeaving;
		if (problemSource == null) {
			// must be a problem found during completeTypeBindings phase of begin to compile
			if (sLoc instanceof EclipseSourceLocation) {
				problemSource = ((EclipseSourceLocation)sLoc).getCompilationResult();
			}
			if (problemSource == null) {
				// XXX this is ok for ajc, will have to do better for AJDT in time...
				return sink.handleMessage(message);
			}
		}
		int startPos = getStartPos(sLoc,problemSource);
		int endPos = getEndPos(sLoc,problemSource);
		int severity = message.isError() ? ProblemSeverities.Error : ProblemSeverities.Warning;
		char[] filename = problemSource.fileName;
		boolean usedBinarySourceFileName = false;
		if (problemSource.isFromBinarySource()) {
			if (sLoc != null) {
				filename = sLoc.getSourceFile().getPath().toCharArray();
				usedBinarySourceFileName = true;
			}
		}
		ReferenceContext referenceContext = findReferenceContextFor(problemSource);
		IProblem problem = compiler.problemReporter.createProblem(
								filename,
								IProblem.Unclassified,
								new String[0],
								new String[] {message.getMessage()},
								severity,
								startPos,
								endPos,
								sLoc != null ? sLoc.getLine() : 1,
								referenceContext,
								problemSource
								);
		IProblem[] seeAlso = buildSeeAlsoProblems(message.getExtraSourceLocations(),
												  problemSource,	
												  usedBinarySourceFileName);
		problem.setSeeAlsoProblems(seeAlso);
		if (message.getDetails() != null) {
			problem.setSupplementaryMessageInfo(message.getDetails());
		}
		compiler.problemReporter.record(problem, problemSource, referenceContext);
		return true;
	}

	public boolean isIgnoring(Kind kind) {
		return sink.isIgnoring(kind);
	}
	
	private int getStartPos(ISourceLocation sLoc,CompilationResult result) {
		int pos = 0;
		if (sLoc == null) return 0;
		int line = sLoc.getLine();
		if (sLoc instanceof EclipseSourceLocation) {
			pos = ((EclipseSourceLocation)sLoc).getStartPos();
		} else {
			if (line <= 1) return 0;
			if (result != null) {
				if ((result.lineSeparatorPositions != null) && 
					(result.lineSeparatorPositions.length >= (line-1))) {
					pos = result.lineSeparatorPositions[line-2] + 1;
				}
			}
		}
		return pos;
	}

	private int getEndPos(ISourceLocation sLoc,CompilationResult result) {
		int pos = 0;
		if (sLoc == null) return 0;
		int line = sLoc.getLine();
		if (line <= 0) line = 1;
		if (sLoc instanceof EclipseSourceLocation) {
			pos = ((EclipseSourceLocation)sLoc).getEndPos();
		} else {
			if (result != null) {
				if ((result.lineSeparatorPositions != null) && 
					(result.lineSeparatorPositions.length >= line)) {
					pos = result.lineSeparatorPositions[line -1] -1;
				}
			}
		}
		return pos;
	}

	private ReferenceContext findReferenceContextFor(CompilationResult result) {
		ReferenceContext context = null;
		if (compiler.unitsToProcess == null) return null;
		for (int i = 0; i < compiler.unitsToProcess.length; i++) {
			if ((compiler.unitsToProcess[i] != null) &&
			    (compiler.unitsToProcess[i].compilationResult == result)) {
				context = compiler.unitsToProcess[i];
				break;
			}				
		}
		return context;
	}
	
	private IProblem[] buildSeeAlsoProblems(List sourceLocations,
											CompilationResult problemSource,
											boolean usedBinarySourceFileName) {
		int probLength = sourceLocations.size();
		if (usedBinarySourceFileName) probLength++;
		IProblem[] ret = new IProblem[probLength];
		for (int i = 0; i < sourceLocations.size(); i++) {
			ISourceLocation loc = (ISourceLocation) sourceLocations.get(i);
			ret[i] = new DefaultProblem(loc.getSourceFile().getPath().toCharArray(),
										"see also",
										0,
										new String[] {},
										ProblemSeverities.Ignore,
										getStartPos(loc,null),
										getEndPos(loc,null),
										loc.getLine());
		}
		if (usedBinarySourceFileName) {
			ret[ret.length -1] = new DefaultProblem(problemSource.fileName,"see also",0,new String[] {},
													ProblemSeverities.Ignore,0,
													0,0);
		}
		return ret;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10404.java