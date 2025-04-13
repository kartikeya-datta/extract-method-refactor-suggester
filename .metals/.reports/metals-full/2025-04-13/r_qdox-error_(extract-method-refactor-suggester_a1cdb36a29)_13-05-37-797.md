error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9622.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9622.java
### java.lang.NullPointerException: Cannot invoke "com.thoughtworks.qdox.model.JavaMember.getName()" because "m$1" is null

error in qdox parser


#### Error stacktrace:

```
scala.meta.internal.mtags.JavaMtags.$anonfun$visitMember$1(JavaMtags.scala:196)
	scala.meta.internal.mtags.MtagsIndexer.withOwner(MtagsIndexer.scala:52)
	scala.meta.internal.mtags.MtagsIndexer.withOwner$(MtagsIndexer.scala:49)
	scala.meta.internal.mtags.JavaMtags.withOwner(JavaMtags.scala:38)
	scala.meta.internal.mtags.JavaMtags.visitMember(JavaMtags.scala:195)
	scala.meta.internal.mtags.JavaMtags.$anonfun$visitMembers$1(JavaMtags.scala:102)
	scala.meta.internal.mtags.JavaMtags.$anonfun$visitMembers$1$adapted(JavaMtags.scala:102)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterable.foreach(Iterable.scala:935)
	scala.meta.internal.mtags.JavaMtags.visitMembers(JavaMtags.scala:102)
	scala.meta.internal.mtags.JavaMtags.$anonfun$visitClass$1(JavaMtags.scala:135)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.meta.internal.mtags.MtagsIndexer.withOwner(MtagsIndexer.scala:52)
	scala.meta.internal.mtags.MtagsIndexer.withOwner$(MtagsIndexer.scala:49)
	scala.meta.internal.mtags.JavaMtags.withOwner(JavaMtags.scala:38)
	scala.meta.internal.mtags.JavaMtags.visitClass(JavaMtags.scala:123)
	scala.meta.internal.mtags.JavaMtags.$anonfun$indexRoot$2(JavaMtags.scala:57)
	scala.meta.internal.mtags.JavaMtags.$anonfun$indexRoot$2$adapted(JavaMtags.scala:57)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterable.foreach(Iterable.scala:935)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:57)
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

QDox null pointer exception in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9622.java