error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7145.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7145.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7145.java
text:
```scala
i@@f(test()) && !within(A) && !call(* A.*(..)) && !preinitialization(new(..)) {

import java.util.*;
import org.aspectj.testing.Tester;

public class AroundAll {
   public static void main(String[] args) {
      new C();
      new C("9");
      //A.printLog();
      A.checkLog();
   } 
}

class C extends SuperC {
    static final int i;
    final int x;

    int y = 42;

    static {
        i = 23;
    }

    C(String s) {
        this(Integer.valueOf(s).intValue());
        A.log("C(" + s + ")");
        A.log("y = " + y);
    }

    C(int i) {
        super(i);
        x = i;
        i = i+1;
        //System.out.println(i + 1);
        A.log("x = " + x);
    }

    C() {
        this("2");
        A.log("C()");
    }
}

class SuperC {
    SuperC(int x) {
        A.log("SuperC(" + x + ")");
    }
}

aspect A {
    static String[] expectedSteps = new String[] {
        "enter staticinitialization(AroundAll.<clinit>)",
        "exit staticinitialization(AroundAll.<clinit>)",
        "enter execution(void AroundAll.main(String[]))",
        "enter call(C())",
        "enter staticinitialization(SuperC.<clinit>)",
        "exit staticinitialization(SuperC.<clinit>)",
        "enter staticinitialization(C.<clinit>)",
        "enter set(int C.i)",
        "exit set(int C.i)",
        "exit staticinitialization(C.<clinit>)",
        "enter call(Integer java.lang.Integer.valueOf(String))",
        "exit call(Integer java.lang.Integer.valueOf(String))",
        "enter call(int java.lang.Integer.intValue())",
        "exit call(int java.lang.Integer.intValue())",
        "enter initialization(SuperC(int))",
        "enter execution(SuperC.<init>)",
        "exit execution(SuperC.<init>)",
        "enter execution(SuperC(int))",
        "SuperC(2)",
        "exit execution(SuperC(int))",
        "exit initialization(SuperC(int))",
        "enter initialization(C())",
        "enter execution(C.<init>)",
        "enter set(int C.y)",
        "exit set(int C.y)",
        "exit execution(C.<init>)",
        "enter execution(C(int))",
        "enter set(int C.x)",
        "exit set(int C.x)",
        "enter get(int C.x)",
        "exit get(int C.x)",
        "x = 2",
        "exit execution(C(int))",
        "enter execution(C(String))",
        "C(2)",
        "enter get(int C.y)",
        "exit get(int C.y)",
        "y = 42",
        "exit execution(C(String))",
        "exit initialization(C())",
        "enter execution(C())",
        "C()",
        "exit execution(C())",
        "exit call(C())",
        "enter call(C(String))",
        "enter call(Integer java.lang.Integer.valueOf(String))",
        "exit call(Integer java.lang.Integer.valueOf(String))",
        "enter call(int java.lang.Integer.intValue())",
        "exit call(int java.lang.Integer.intValue())",
        "enter initialization(SuperC(int))",
        "enter execution(SuperC.<init>)",
        "exit execution(SuperC.<init>)",
        "enter execution(SuperC(int))",
        "SuperC(9)",
        "exit execution(SuperC(int))",
        "exit initialization(SuperC(int))",
        "C.new(9)",
        "enter initialization(C(String))",
        "enter execution(C.<init>)",
        "enter set(int C.y)",
        "exit set(int C.y)",
        "exit execution(C.<init>)",
        "enter execution(C(int))",
        "enter set(int C.x)",
        "exit set(int C.x)",
        "enter get(int C.x)",
        "exit get(int C.x)",
        "x = 9",
        "exit execution(C(int))",
        "enter execution(C(String))",
        "C(91)",
        "enter get(int C.y)",
        "exit get(int C.y)",
        "y = 42",
        "exit execution(C(String))",
        "exit initialization(C(String))",
        "exit call(C(String))",
        };

    static List logList = new ArrayList();

    static void printLog() {
        for (Iterator i = logList.iterator(); i.hasNext(); ) {
            System.out.println("        \"" + i.next() + "\", ");
        }
    }

    static void checkLog() {
      Tester.checkEqual(expectedSteps, A.logList.toArray(), "steps");
      Tester.checkEqual(A.logList, expectedSteps, "steps");
    }

    static void log(String s) {
        logList.add(s);
    }

    static boolean test() { return true; }

    //before(): initialization(C.new(String)) { }

    void around(String s): initialization(C.new(String)) && args(s) && if(s.equals("9")) {
        log("C.new(9)");
        proceed(s+"1");
    }

    Object around(): //initialization(C.new(String)) { 
                    if(test()) && !within(A) && !call(* A.*(..)) {
       A.log("enter " + thisJoinPoint);
       Object ret = proceed();
       A.log("exit " + thisJoinPoint);
       //proceed();
       //System.err.println("run twice");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7145.java