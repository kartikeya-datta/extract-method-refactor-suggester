error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5117.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5117.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5117.java
text:
```scala
h@@elper.addPathElementToPath("classpath", "${distlib.dir}/antlibs/system.jar");

package org.apache.ant.builder;
public class Ant1CompatBuilder {
    protected void _init(BuildHelper helper) {
        helper.setProperty("src.dir", "src");
        helper.setProperty("lib.dir", "lib");
        helper.setProperty("java.dir", "${src.dir}/java");
        helper.setProperty("bin.dir", "bin");
        helper.setProperty("dist.dir", "dist");
        helper.setProperty("javadocs.dir", "${dist.dir}/javadocs");
        helper.setProperty("distlib.dir", "${dist.dir}/lib");
        helper.setProperty("debug", "true");
        helper.setProperty("ant.package", "org/apache/tools/ant");
        helper.setProperty("optional.package", "${ant.package}/taskdefs/optional");
        helper.setProperty("optional.type.package", "${ant.package}/types/optional");
        helper.setProperty("util.package", "${ant.package}/util");
        helper.setProperty("regexp.package", "${util.package}/regexp");
        helper.createPath("classpath");
        helper.addFileSetToPath("classpath", 
                        "${lib.dir}/parser", "*.jar");
        helper.addFileSetToPath("classpath", 
                        "${lib.dir}/ant1compat", "*.jar");
        helper.addPathElementToPath("classpath", "${distlib.dir}/init.jar");
        helper.addPathElementToPath("classpath", "${distlib.dir}/common/common.jar");
        helper.addPathElementToPath("classpath", "${distlib.dir}/antlibs/system.tsk");
    }
    protected void check_for_optional_packages(BuildHelper helper) {
    }
    protected void ant1compat(BuildHelper helper) {
        helper.mkdir("${bin.dir}/ant1src");
        helper.mkdir("${bin.dir}/ant1compat");
        helper.copyFilesetRef("ant1src", "${bin.dir}/ant1src");
        helper.javac("${bin.dir}/ant1src:${java.dir}/antlibs/ant1compat", "${bin.dir}/ant1compat", "classpath");
        helper.copyFileset("${bin.dir}/ant1src", "${bin.dir}/ant1compat");
        helper.jar("${bin.dir}/ant1compat", "${distlib.dir}/antlibs/ant1compat.jar",
                   "${java.dir}/antlibs/ant1compat", "antlib.xml");
    }
    protected void clean(BuildHelper helper) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5117.java