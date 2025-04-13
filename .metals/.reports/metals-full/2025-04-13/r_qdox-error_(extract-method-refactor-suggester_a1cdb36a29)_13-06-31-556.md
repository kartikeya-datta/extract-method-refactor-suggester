error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8762.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8762.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8762.java
text:
```scala
r@@eturn (Assembly)completeMatches.iterator().next();

package org.jboss.ejb.plugins.cmp.ejbql;

import java.util.Iterator;

public abstract class Parser {
	private Assembler assembler;

	public Parser() {
	}

	public abstract AssemblySet match(AssemblySet in);
	
	public AssemblySet matchAndAssemble(AssemblySet in) {
		AssemblySet matchedSet = match(in);
		if(assembler == null) {
			return matchedSet;
		}
		
		AssemblySet out = new AssemblySet();
		for(Iterator i = matchedSet.iterator(); i.hasNext(); ) {
			Assembly a = (Assembly)i.next();
			assembler.workOn(a);
			if(a.isValid()) {
				out.add(a);
			}
		}
		return out;
	}
	
	public Assembly bestMatch(Assembly a) {
		AssemblySet set = new AssemblySet();
		set.add(a);
		set = matchAndAssemble(set);
		return best(set);
	}
	
	public Assembly completeMatch(Assembly a) {
		Assembly best = bestMatch(a);
		if(best == null || best.hasNextToken()) {
			return null;
		}
		return best;
	}
	
	public Assembly soleMatch(Assembly in) {
		AssemblySet set = new AssemblySet();
		set.add(in);
		set = matchAndAssemble(set);
		
		AssemblySet completeMatches = new AssemblySet();
		for(Iterator i = set.iterator(); i.hasNext(); ) {
			Assembly a = (Assembly) i.next();

			// is this a complete match, can't get better then that
			if(!a.hasNextToken()) {
				completeMatches.add(a);
			}
		}

		if(completeMatches.size()==0) {
			return best(set);
		}
		if(completeMatches.size() > 1) {
			throw new IllegalStateException("Multiple assemblies matched: "+set.size());
		}
		return (Assembly)completeMatches.iterator().next();;
	}
	
	public void setAssembler(Assembler assembler) {
		this.assembler = assembler;
	}
	
	private Assembly best(AssemblySet set) {
		Assembly best = null;
		for(Iterator i = set.iterator(); i.hasNext(); ) {
			Assembly a = (Assembly) i.next();

			// is this a complete match, can't get better then that
			if(!a.hasNextToken()) {
				return a;
			}
			
			// do we have a best
			if(best == null) {
				best = a;
			} else {
				// did a match more tokens then the current best
				if(a.getTokensUsed() > best.getTokensUsed()) {
					best = a;
				}
			}
		}
		return best;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8762.java