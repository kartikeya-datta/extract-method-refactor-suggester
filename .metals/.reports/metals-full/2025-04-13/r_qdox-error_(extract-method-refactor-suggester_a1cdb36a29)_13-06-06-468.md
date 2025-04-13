error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12862.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12862.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12862.java
text:
```scala
F@@oo.new(int n) { this(); ranIntroducedConstructor = true; }

import org.aspectj.testing.Tester;
public class Driver {
  
  public static void main(String[] args) { test(); }

  public static void test() {
    Foo.staticMethod();
    Foo.introducedStaticMethod();

    Foo foo = new Foo(10);

    foo.nonStaticMethod();
    foo.introducedNonStaticMethod();

      Tester.checkEqual(A.fooStaticCounter, 1, "A.fooStaticCounter");
      Tester.checkEqual(A.fooCounter, 1, "A.fooCounter");
      Tester.checkEqual(A.aStaticCounter, 1, "A.aStaticCounter");
      Tester.checkEqual(A.aCounter, 1, "A.aCounter");
      // these is only one constructor call, for Foo
      Tester.checkEqual(A.constructorCounter, 1, "constructor calls");
      // one for Foo, one for A
      Tester.checkEqual(A.initializationCounter, 2, "initializations");
      Tester.check(A.ranIntroducedConstructor, 
                    "no overriding of the real thing");
  }
}

class Foo { 
  
  static void staticMethod() { }
         void nonStaticMethod() { }
}

aspect A0_8beta1 {
    after() returning(): /*target(*) &&*/ call(new(int)) {
        A.constructorCounter++;
    }
    after() returning(): /*target(*) &&*/ initialization(new(..)) && !within(A0_8beta1) {
    	System.out.println("init at " + thisJoinPoint);
        A.initializationCounter++;
    }
    
    before(): within(Foo) && execution(static * Foo.*(..)) {
        A.fooStaticCounter++;
    }
    
    before(): within(A) && execution(static * Foo.*(..)) {
        A.aStaticCounter++;
    }
    
    before(): within(A) && execution(!static * Foo.*(..)) {
        A.aCounter++;
        System.out.println("external before advise on " + thisJoinPoint);
    }
}

aspect A pertarget(target(Foo)){
  
    static int constructorCounter = 0;
    static int initializationCounter = 0;
    static int aStaticCounter = 0;
    static int aCounter = 0;
    static int fooStaticCounter = 0;
    static int fooCounter = 0;

    static boolean ranIntroducedConstructor = false;

    //introduction Foo {
    static void Foo.introducedStaticMethod() {
        // System.out.println(thisJoinPoint.className +"."+ 
        // thisJoinPoint.methodName);
    }
    void Foo.introducedNonStaticMethod() {
        // System.out.println(thisJoinPoint.className +"."+ 
        // thisJoinPoint.methodName);
    }
    Foo.new(int n) { ranIntroducedConstructor = true; }

	// make sure advice doesn't go on the toString() method
	// this would result in an infinite recursion
	before(): within(Foo) && execution(!static * Foo.*(..)) {
	    fooCounter++;
	    //System.out.println("before advise on " + 
	    //thisJoinPoint.className +"."+ thisJoinPoint.methodName);
	}
	
	public A() { System.err.println("creating: " + this); }

    //XXX moved to other aspect, need to think about this...
    //before(): within(A) && executions(!static * Foo.*(..)) {
    //aCounter++;
    //System.out.println("before advise on " + thisJoinPoint);
    //}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12862.java