error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5118.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5118.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5118.java
text:
```scala
h@@elper.jar("${bin.dir}/antlibs/${libset}", "${distlib.dir}/antlibs/${libset}.jar",

package org.apache.ant.builder;
public class MutantBuilder {
    protected void _init(BuildHelper helper) {
        helper.setProperty("src.dir", "src");
        helper.setProperty("lib.dir", "lib");
        helper.setProperty("java.dir", "${src.dir}/java");
        helper.setProperty("bin.dir", "bin");
        helper.setProperty("dist.dir", "dist");
        helper.setProperty("javadocs.dir", "${dist.dir}/javadocs");
        helper.setProperty("distlib.dir", "${dist.dir}/lib");
        helper.setProperty("debug", "true");
        helper.createPath("classpath.parser");
        helper.addFileSetToPath("classpath.parser", 
                        "${lib.dir}/parser", "*.jar");
        helper.createPath("classpath.common");
        helper.addPathElementToPath("classpath.common", "${distlib.dir}/init.jar");
        helper.createPath("classpath.antcore");
        helper.addPathElementToPath("classpath.antcore", "${distlib.dir}/common/common.jar");
        helper.addPathToPath("classpath.antcore", "classpath.common");
        helper.addPathToPath("classpath.antcore", "classpath.parser");
        helper.createPath("classpath.cli");
        helper.addPathElementToPath("classpath.cli", "${distlib.dir}/antcore/antcore.jar");
        helper.addPathToPath("classpath.cli", "classpath.antcore");
        helper.createPath("classpath.start");
        helper.addPathElementToPath("classpath.start", "${distlib.dir}/init.jar");
    }
    protected void buildsetup(BuildHelper helper) {
        helper.mkdir("${bin.dir}");
        helper.mkdir("${distlib.dir}");
        helper.copyFileset("${lib.dir}/parser", "${distlib.dir}/parser");
    }
    protected void init(BuildHelper helper) {
        helper.mkdir("${bin.dir}/init");
        helper.javac("${java.dir}/init", "${bin.dir}/init", null);
        helper.jar("${bin.dir}/init", "${distlib.dir}/init.jar",
                   null, null);
    }
    protected void common(BuildHelper helper) {
        helper.mkdir("${bin.dir}/common");
        helper.mkdir("${distlib.dir}/common");
        helper.javac("${java.dir}/common", "${bin.dir}/common", "classpath.common");
        helper.jar("${bin.dir}/common", "${distlib.dir}/common/common.jar",
                   null, null);
    }
    protected void antcore(BuildHelper helper) {
        helper.mkdir("${bin.dir}/antcore");
        helper.mkdir("${distlib.dir}/antcore");
        helper.javac("${java.dir}/antcore", "${bin.dir}/antcore", "classpath.antcore");
        helper.jar("${bin.dir}/antcore", "${distlib.dir}/antcore/antcore.jar",
                   null, null);
    }
    protected void cli(BuildHelper helper) {
        helper.mkdir("${bin.dir}/cli");
        helper.mkdir("${distlib.dir}/cli");
        helper.javac("${java.dir}/cli", "${bin.dir}/cli", "classpath.cli");
        helper.jar("${bin.dir}/cli", "${distlib.dir}/cli/cli.jar",
                   null, null);
    }
    protected void start(BuildHelper helper) {
        helper.mkdir("${bin.dir}/start");
        helper.javac("${java.dir}/start", "${bin.dir}/start", "classpath.start");
        helper.jar("${bin.dir}/start", "${distlib.dir}/start.jar",
                   null, null);
        helper.jar("${bin.dir}/start", "${distlib.dir}/ant.jar",
                   null, null);
    }
    protected void ant1compat(BuildHelper helper) {
    }
    protected void remote(BuildHelper helper) {
        helper.mkdir("${bin.dir}/remote");
        helper.javac("${java.dir}/remote", "${bin.dir}/remote", "classpath.start");
        helper.jar("${bin.dir}/remote", "${distlib.dir}/remote.jar",
                   null, null);
    }
    protected void clean(BuildHelper helper) {
    }
    protected void antlibs(BuildHelper helper) {
    }
    protected void build_lib(BuildHelper helper) {
        helper.mkdir("${bin.dir}/antlibs/${libset}");
        helper.mkdir("${distlib.dir}/antlibs");
        helper.createPath("classpath.antlibs");
        helper.addPathElementToPath("classpath.antlibs", "${distlib.dir}/common/common.jar");
        helper.addPathToPath("classpath.antlibs", "classpath.common");
        helper.javac("${java.dir}/antlibs/${libset}", "${bin.dir}/antlibs/${libset}", "classpath.antlibs");
        helper.jar("${bin.dir}/antlibs/${libset}", "${distlib.dir}/antlibs/${libset}.tsk",
                   "${java.dir}/antlibs/${libset}", "antlib.xml");
    }
    protected void main(BuildHelper helper) {
    }
    protected void checkstyle(BuildHelper helper) {
        helper.mkdir("${bin.dir}/check");
    }
    protected void javadocs(BuildHelper helper) {
        helper.mkdir("${javadocs.dir}");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5118.java