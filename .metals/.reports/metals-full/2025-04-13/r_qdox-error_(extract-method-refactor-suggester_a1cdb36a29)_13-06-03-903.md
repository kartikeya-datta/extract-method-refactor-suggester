error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1617.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1617.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1617.java
text:
```scala
class Token {

p@@ackage org.apache.log4j.lbel;


public class Token {

	private int type;
	private Object value;
  public static final int TRUE = 1;
  public static final int FALSE = 2;
  public static final int OR = 10;
  public static final int AND = 11;
  public static final int NOT = 12;
  public static final int LITERAL = 20;
  public static final int NUMBER = 21;
  public static final int OPERATOR = 30;
  public static final int LP = 40;
  public static final int RP = 41;
  public static final int LOGGER = 100;
  public static final int MESSAGE = 110;
  public static final int LEVEL = 120;
  public static final int TIMESTAMP = 130;
  public static final int THREAD = 140;
  public static final int PROPERTY = 150;
  public static final int DATE = 160;
  public static final int EOF = 1000;
	
	public Token(int type) {
	  this(type, null);
	}
	
	public Token(int type, Object value) {
		this.type = type;
		this.value = value;
	}
	

	
	public int getType() {
		return type;
	}
	
	public Object getValue() {
		return value;
	}
  
  public String toString() {
    String typeStr = null;
    switch(type) {

    case TRUE: typeStr = "TRUE"; break;
    case FALSE: typeStr = "FALSE"; break;
    case OR: typeStr = "OR"; break;
    case AND: typeStr = "AND"; break;
    case NOT: typeStr = "NOT"; break;
    case LITERAL: typeStr = "IDENTIFIER"; break;
    case NUMBER: typeStr = "NUMBER"; break;
    case OPERATOR: typeStr = "OPERATOR"; break;
    case LP: typeStr = "LP"; break;
    case RP: typeStr = "RP"; break;
    case LOGGER: typeStr = "LOGGER"; break;
    case MESSAGE: typeStr = "MESSAGE"; break;
    case LEVEL: typeStr = "LEVEL"; break;
    case TIMESTAMP: typeStr = "TIMESTAMP"; break;
    case THREAD: typeStr = "THREAD"; break;
    case PROPERTY: typeStr = "PROPERTY"; break;
    case DATE: typeStr = "DATE"; break;
    case EOF: typeStr = "EOF"; break;
    default:  typeStr = "UNKNOWN";
    }
    return "Token("+typeStr +", " + value+")";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1617.java