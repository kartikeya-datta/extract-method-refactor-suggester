error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8164.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8164.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8164.java
text:
```scala
class Node {

p@@ackage org.apache.log4j.lbel;

public class Node {
	
	public static final int FALSE = 1;
	public static final int TRUE = 2;
  public static final int COMPARATOR = 3;

	public static final int OR = 1000;
	public static final int AND = 1100;
	public static final int NOT = 1200;
	
  Node left;
	Node right;

  final int type;
  final Object value;
  
  Node(int type) {
  	this(type, null);
  }
  
  Node(int type, Object value) {
  	this.type = type;
  	this.value = value;
  }
  
  public int getType() {
	  return type;
  }
  
  public Object getValue() {
  	return value;
  }

	public Node getLeft() {
		return left;
	}
	public void setLeft(Node leftSide) {
		if(this.left != null) {
		  throw new IllegalStateException("The left side already set. (old="+this.left+", new="+leftSide+")");
		}
		this.left = leftSide;
	}

  public Node getRight() {
		return right;
	}
	public void setRight(Node rightSide) {
		if(this.right != null) {
			throw new IllegalStateException("The right side already set. (old="+this.right+", new="+rightSide+")");
		}
		this.right = rightSide;
	}
	
	public String toString() {
		return "Node: type="+type+", value="+value;
	}
	
	public void leftFirstDump(String offset) {
		
		System.out.println(offset + this);
		
		offset += "  ";
		
		Node l = this.getLeft();
		if(l != null) {
		  System.out.println(offset +"Printing left side");
		  l.leftFirstDump(offset);
		}

		//Node m = this.getMiddle();
		//if(m != null) {
			//System.out.println(offset +"Printing middle");
			//m.leftFirstDump(offset);
		//} 
				
		Node r = this.getRight();
		if(r != null) {
  		System.out.println(offset +"Printing right side");
	  	r.leftFirstDump(offset);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8164.java