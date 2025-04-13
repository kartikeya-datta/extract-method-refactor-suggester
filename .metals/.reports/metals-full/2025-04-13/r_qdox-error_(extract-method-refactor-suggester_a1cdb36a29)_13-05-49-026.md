error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12645.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12645.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12645.java
text:
```scala
final S@@tringBuilder sb = new StringBuilder ("dereferenced null " + ((pos != null) ? ("(" + pos + ")") : ""));

/*
Copyright (c) 2008 Arno Haase.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
 */
package org.eclipse.xtend.backend.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.eclipse.xtend.backend.common.BackendTypesystem;
import org.eclipse.xtend.backend.common.Constants;
import org.eclipse.xtend.backend.common.ContributionStateContext;
import org.eclipse.xtend.backend.common.CreationCache;
import org.eclipse.xtend.backend.common.ExecutionContext;
import org.eclipse.xtend.backend.common.FunctionDefContext;
import org.eclipse.xtend.backend.common.FunctionInvoker;
import org.eclipse.xtend.backend.common.GlobalParamContext;
import org.eclipse.xtend.backend.common.LocalVarContext;
import org.eclipse.xtend.backend.common.SourcePos;
import org.eclipse.xtend.backend.common.StacktraceEntry;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 */
public final class ExecutionContextImpl implements ExecutionContext {
	private final CreationCache _creationCache = new CreationCacheImpl ();
	private FunctionDefContext _functionDefContext;
	private LocalVarContext _localVarContext = new LocalVarContext ();
	private final FunctionInvoker _functionInvoker = new FunctionInvokerImpl ();
	private final BackendTypesystem _typeSystem;
	private final GlobalParamContext _globalParamContext = new GlobalParamContextImpl ();
	private final boolean _logStacktrace;
	private final List<StacktraceEntry> _stacktrace = new ArrayList<StacktraceEntry> ();
	
	private final ContributionStateContext _contributionStateContext = new ContributionStateContext ();
	
	public ExecutionContextImpl (FunctionDefContext initialFunctionDefContext, BackendTypesystem typesystem, boolean logStacktrace) {
		_functionDefContext = initialFunctionDefContext;
		_typeSystem = typesystem;
		_logStacktrace = logStacktrace;
	}

	public CreationCache getCreationCache() {
		return _creationCache;
	}

	public FunctionDefContext getFunctionDefContext() {
		return _functionDefContext;
	}

	public void setFunctionDefContext(FunctionDefContext ctx) {
		_functionDefContext = ctx;
	}

	public LocalVarContext getLocalVarContext() {
		return _localVarContext;
	}

	public void setLocalVarContext(LocalVarContext ctx) {
		_localVarContext = ctx;
	}

	public FunctionInvoker getFunctionInvoker() {
		return _functionInvoker;
	}

	public BackendTypesystem getTypesystem() {
		return _typeSystem;
	}

	public GlobalParamContext getGlobalParamContext() {
		return _globalParamContext;
	}

	public void logNullDeRef (SourcePos pos) {
	    final StringBuilder sb = new StringBuilder ("dereferenced null (" + pos + ")");
	    for (int i=_stacktrace.size() - 1; i>= 0; i--)
	        sb.append ("\n  " + _stacktrace.get (i));
	    
	    LogFactory.getLog (Constants.LOG_NULL_DEREF).warn (sb);
	}
	
	public ContributionStateContext getContributionStateContext () {
	    return _contributionStateContext;
	}

    public List<StacktraceEntry> getStacktrace () {
        return _stacktrace;
    }

    public boolean isLogStacktrace () {
        return _logStacktrace;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12645.java