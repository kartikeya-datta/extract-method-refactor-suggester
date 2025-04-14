error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7606.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7606.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7606.java
text:
```scala
a@@spect MultiExec extends Multi { declare precedence: MultiExec, MultiCall;

import org.aspectj.testing.Tester;

public class MultiDispatchCp {
    public static void main(String[] args) {
        C c = new C();

        Tester.event("**** exec ****");
        MultiExec.enabled = true;
        run(new C());

        Tester.event("**** call ****");
        MultiExec.enabled = false;
        MultiCall.enabled = true;
        run(new C());

        Tester.event("**** both=call ****");
        MultiExec.enabled = true;
        run(new C());

        Tester.checkEventsFromFile("MultiDispatchCp.out");
        //Tester.printEvents();
    }
    
    static void run(C c) {
        Tester.event(c.doit("s1"));
        Tester.event(c.doit(new Integer(10)));
        Tester.event(c.doit(new Double(1.25)));

        Object o;
        o = "s2"; Tester.event(c.doit(o));
        o = new Integer(20); Tester.event(c.doit(o));
        o = new Double(2.25); Tester.event(c.doit(o));
    }
}


class C {
    String doit(Object o) {
        return "did-" + o.toString();
    }
}

abstract aspect Multi {
    abstract pointcut m();

    abstract String getPrefix();

    String around(String s): m() && args(s) {
        return getPrefix() + "-string-" + s;
    }
    String around(Integer i): m() && args(i) {
        //System.out.println(thisJoinPoint + " would return " + proceed(i));
        return getPrefix() + "-integer-" + i;
    }
    String around(Double d): m() && args(d) {
        return getPrefix()  + "-double-" + d;
    }
}

aspect MultiCall extends Multi {  
    public static boolean enabled = false;

    String getPrefix() { return "call"; }

    pointcut m(): call(String C.doit(Object)) && if(enabled);
}


// dominates should have no effect as call join points
// always come before executions
aspect MultiExec extends Multi { declare dominates: MultiExec, MultiCall;
    public static boolean enabled = false;

    String getPrefix() { return "exec"; }

    pointcut m(): execution(String C.doit(Object)) && if(enabled);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7606.java