error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13949.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13949.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13949.java
text:
```scala
b@@efore(int i): call(* XXX.mumble()) && named() && namedWithOneArg(i) { }


import java.io.*;

class Point { 

	int x;
	static int sx;

	{ x = 0; }
	static { sx = 1; }
	
	public Point() { }
	
	public int getX() { return x;	}
	
	public void setX(int x) { this.x = x; }
	
	public int changeX(int x) { 
		this.x = x;
		return x;
	}
	
	void doIt() { 
		try {
			File f = new File(".");
			f.getCanonicalPath();
		} catch (IOException ioe) {
			System.err.println("!");	
		}	
		setX(10);
		new Point();
	}
} 

class SubPoint extends Point { }

class Line { }

aspect AdvisesRelationCoverage {
    before(): execution(*..*.new(..)) { }
    before(): get(int *.*) { }
    before(): set(int *.*) { }
    before(): initialization(Point.new(..)) { }
    before(): staticinitialization(Point) { }
    before(): handler(IOException) { }
    before(): call(* Point.setX(int)) { }
    before(): call(Point.new()) { }
    before(): within(*) && execution(* Point.setX(..)) { }
    before(): within(*) && execution(Point.new()) { }
}

aspect AdviceNamingCoverage {
	pointcut named(): call(* *.mumble());
	pointcut namedWithOneArg(int i): call(int Point.changeX(int)) && args(i);
	pointcut namedWithArgs(int i, int j): set(int Point.x) && args(i, j);

	after(): named() { }	
	after(int i, int j) returning: namedWithArgs(i, j) { }
	after() throwing: named() { }
	after(): named() { }
	
	before(): named() { }
	
	int around(int i): namedWithOneArg(i) { return i;}
	int around(int i) throws SizeException: namedWithOneArg(i) { return proceed(i); }
	
	before(): named() { }	
	before(int i): call(* *.mumble()) && named() && namedWithOneArg(i) { }	
	before(int i): named() && call(* *.mumble()) && namedWithOneArg(i) { }	
	
	before(): call(* *.mumble()) { }
}
  
abstract aspect AbstractAspect {
	abstract pointcut abPtct();	
}
  
aspect InterTypeDecCoverage {

    pointcut illegalNewFigElt(): call(Point.new(..)) && !withincode(* *.doIt(..));

    declare error: illegalNewFigElt(): "Illegal constructor call.";
    declare warning: illegalNewFigElt(): "Illegal constructor call.";

    declare parents: Point extends java.io.Serializable;
    declare parents: Point+ implements java.util.Observable;
	declare parents: Point && Line implements java.util.Observable;
    declare soft: SizeException : call(* Point.getX());
	declare precedence: AdviceCoverage, InterTypeDecCoverage, *;

	public int Point.xxx = 0;
    public int Point.check(int i, Line l) { return 1 + i; }
//	public Line.new(String s) {  }
}

class SizeException extends Exception { } 

aspect AdviceCoverage {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13949.java