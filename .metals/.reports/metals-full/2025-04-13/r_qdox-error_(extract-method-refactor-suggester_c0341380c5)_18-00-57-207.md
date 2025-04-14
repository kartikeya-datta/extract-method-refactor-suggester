error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7833.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7833.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7833.java
text:
```scala
o@@ut.print(usage("domain"));

package org.jboss.as.process;

import static org.jboss.as.process.ProcessMessages.MESSAGES;

import java.io.PrintStream;

public class CommandLineArgumentUsageImpl extends CommandLineArgumentUsage {

    public static void init(){

        addArguments(CommandLineConstants.ADMIN_ONLY);
        instructions.add(MESSAGES.argAdminOnly());

        addArguments(CommandLineConstants.PUBLIC_BIND_ADDRESS + " <value>", CommandLineConstants.PUBLIC_BIND_ADDRESS + "=<value>" );
        instructions.add(MESSAGES.argPublicBindAddress());

        addArguments(CommandLineConstants.PUBLIC_BIND_ADDRESS + "<interface>=<value>" );
        instructions.add(MESSAGES.argInterfaceBindAddress());

        addArguments(CommandLineConstants.BACKUP_DC);
        instructions.add(MESSAGES.argBackup());

        addArguments(CommandLineConstants.SHORT_DOMAIN_CONFIG + " <config>", CommandLineConstants.SHORT_DOMAIN_CONFIG + "=<config>");
        instructions.add(MESSAGES.argShortDomainConfig());

        addArguments(CommandLineConstants.CACHED_DC);
        instructions.add(MESSAGES.argCachedDc());

        addArguments(CommandLineConstants.SYS_PROP + "<name>[=<value>]");
        instructions.add(MESSAGES.argSystem());

        addArguments(CommandLineConstants.DOMAIN_CONFIG + "=<config>");
        instructions.add(MESSAGES.argDomainConfig());

        addArguments(CommandLineConstants.SHORT_HELP, CommandLineConstants.HELP);
        instructions.add(MESSAGES.argHelp());

        addArguments(CommandLineConstants.HOST_CONFIG + "=<config>");
        instructions.add(MESSAGES.argHostConfig());

        addArguments(CommandLineConstants.INTERPROCESS_HC_ADDRESS + "=<address>");
        instructions.add(MESSAGES.argInterProcessHcAddress());

        addArguments(CommandLineConstants.INTERPROCESS_HC_PORT + "=<port>");
        instructions.add(MESSAGES.argInterProcessHcPort());

        addArguments(CommandLineConstants.MASTER_ADDRESS +"=<address>");
        instructions.add(MESSAGES.argMasterAddress());

        addArguments(CommandLineConstants.MASTER_PORT + "=<port>");
        instructions.add(MESSAGES.argMasterPort());

        addArguments(CommandLineConstants.READ_ONLY_DOMAIN_CONFIG + "=<config>");
        instructions.add(MESSAGES.argReadOnlyDomainConfig());

        addArguments(CommandLineConstants.READ_ONLY_HOST_CONFIG + "=<config>");
        instructions.add(MESSAGES.argReadOnlyHostConfig());

        addArguments(CommandLineConstants.SHORT_PROPERTIES + " <url>", CommandLineConstants.SHORT_PROPERTIES + "=<url>", CommandLineConstants.PROPERTIES + "=<url>");
        instructions.add(MESSAGES.argProperties());

        addArguments(CommandLineConstants.PROCESS_CONTROLLER_BIND_ADDR + "=<address>");
        instructions.add(MESSAGES.argPcAddress());

        addArguments(CommandLineConstants.PROCESS_CONTROLLER_BIND_PORT + "=<port>");
        instructions.add(MESSAGES.argPcPort());

        addArguments(CommandLineConstants.DEFAULT_MULTICAST_ADDRESS + " <value>", CommandLineConstants.DEFAULT_MULTICAST_ADDRESS + "=<value>");
        instructions.add(MESSAGES.argDefaultMulticastAddress());

        addArguments(CommandLineConstants.OLD_SHORT_VERSION, CommandLineConstants.SHORT_VERSION, CommandLineConstants.VERSION);
        instructions.add(MESSAGES.argVersion());
    }

    public static void printUsage(final PrintStream out) {
        init();
        out.print(usage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7833.java