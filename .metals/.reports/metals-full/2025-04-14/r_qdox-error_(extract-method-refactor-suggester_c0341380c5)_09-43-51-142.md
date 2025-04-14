error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8833.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8833.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8833.java
text:
```scala
a@@spect InitializationSample {


package language;

public class Initialization {
    public static void main(String[] argList) {
        String[] expected = new String[] 
        {   "none after-String-constructor-execution after-initialization after-any-constructor-call", 
            "hello after-String-constructor-execution after-initialization after-any-constructor-call", 
            "none after-String-constructor-execution after-initialization", 
            "hi from-AnotherThing after-String-constructor-execution after-initialization"
        };
        String[] actual = new String[4]; 
        Thing thing = new Thing();
        actual[0] = new Thing().message;
        actual[1] = new Thing("hello").message;
        actual[2] = new AnotherThing().message;
        actual[3] = new AnotherThing("hi").message;
        
        StringBuffer errs = new StringBuffer();
        for (int i = 0; i < actual.length; i++) {
            if (!expected[i].equals(actual[i])) {
                errs.append("expected ");
                errs.append(expected[i]);
                errs.append(" but got ");
                errs.append(actual[i]);
                errs.append("\n");
            }
        }
        if (0 < errs.length()) {
            throw new Error(errs.toString());
        }
    }    
}
/** @author Erik Hilsdale, Wes Isberg */

// START-SAMPLE language-initialization Understanding object creation join points
/*
 * To work with an object right when it is constructed,
 * understand the differences between the join points for 
 * constructor call, constructor execution, and initialization.
 */
 // ------- examples of constructors and the ways they invoke each other.
class Thing {
    String message;
    Thing() {
        this("none");
    }
    Thing(String message) {
        this.message = message;
    }
}

class AnotherThing extends Thing {
    AnotherThing() {
        super(); // this does not append to message as the one below does.
    }
    
    AnotherThing(String message) {
        super(message + " from-AnotherThing");
    }
}

aspect A {
    // -------- constructor-call picks out the calls
    /**
     * After any call to any constructor, fix up the thing.
     * When creating an object, there is only one call to 
     * construct it, so use call(..) avoid duplicate advice.
     * There is no target for the call, but the object
     * constructed is returned from the call.
     * In AspectJ 1.1, this only picks  out callers in the input
     * classes or source files, and it does not pick out
     * invocations via <code>super(..)</code> 
     * or <code>this(..)</code>.
     */
    after() returning (Thing thing): 
            call(Thing.new(..)) { 
        thing.message += " after-any-constructor-call";
    }

    // -------- constructor-execution picks out each body
    /**
     * After executing the String constructor, fix up the thing.
     * The object being-constructed is available as either
     * <code>this</code> or <code>target</code>.
     * This works even if the constructor was invoked using 
     * <code>super()</code> or <code>this()</code> or by code 
     * outside the control of the AspectJ compiler.
     * However, if you advise multiple constructors, you'll advise
     * a single instance being constructed multiple times 
     * if the constructors call each other.
     * In AspectJ 1.1, this only affects constructors in the input
     * classes or source files.
     */
    after(Thing thing) returning : target(thing) && 
            execution(Thing.new(String)) { 
        thing.message += " after-String-constructor-execution";
    }

    /**
     * DANGER -- BAD!!  Before executing the String constructor,
     * this uses the target object, which is not constructed.
     */
    before (Thing thing): this(thing) && execution(Thing.new(String)) { 
        // DANGER!! thing not constructed yet.
        //thing.message += " before-String-constructor-execution";
    }
    
    // -------- initialization picks out any construction, once
    /**
     * This advises all Thing constructors in one join point, 
     * even if they call each other with <code>this()</code>, etc.
     * The object being-constructed is available as either
     * <code>this</code> or <code>target</code>.
     * In AspectJ 1.1, this only affects types input to the compiler.
     */
    after(Thing thing) returning: this(thing) 
            && initialization(Thing.new(..)) {
        thing.message += " after-initialization";
    }
}
//END-SAMPLE language-initialization

aspect B {
    static boolean log = false;
    before() : withincode(void Initialization.main(String[])) 
            && call(Thing.new(..)) && if(log) {
        System.out.println("before: " + thisJoinPointStaticPart.getSourceLocation().getLine());
    }
    before() : within(A) && adviceexecution() && if(log)  {
        System.out.println("advice: " + thisJoinPointStaticPart.getSourceLocation().getLine());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8833.java