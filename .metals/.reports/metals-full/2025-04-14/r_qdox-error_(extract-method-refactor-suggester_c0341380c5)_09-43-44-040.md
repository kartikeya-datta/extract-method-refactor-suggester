error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6106.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6106.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6106.java
text:
```scala
t@@hrow new ShouldNotImplement(Util.bind("ast.missingStatement")); //$NON-NLS-1$

package org.eclipse.jdt.internal.compiler.ast;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.Util;

public abstract class Statement extends AstNode {
	// storage for internal flags (32 bits)
	public int bits = IsReachableMASK; // reachable by default

	// for operators only
	// Reach . . . . . . . . . . . . . . . . . O O O O O O V VrR R R R
	public static final int ReturnTypeIDMASK = 15; // 4 lower bits for operators
	public static final int ValueForReturnMASK = 16; // for binary expressions
	public static final int OnlyValueRequiredMASK = 32; // for binary expressions
	public static final int OperatorSHIFT = 6;
	public static final int OperatorMASK = 63 << OperatorSHIFT;
	
	// for name references only
	// Reach . . . . . . . . . . . . . . . . D D D D D D D D VrF R R R
	public static final int RestrictiveFlagMASK = 7; // 3 lower bits for name references
	public static final int FirstAssignmentToLocalMASK = 8; // for single name references
	public static final int DepthSHIFT = 5;
	public static final int DepthMASK = 0xFF << DepthSHIFT; // 8 bits for actual depth value (max. 255)

	// for statements only
	public static final int IsReachableMASK = 0x80000000; // highest bit
	
	// for type declaration only
	public static final int AddAssertionMASK = 1; // highest bit

	/*
	public final static int BitMask1= 0x1; // decimal 1
	public final static int BitMask2= 0x2; // decimal 2
	public final static int BitMask3= 0x4; // decimal 4
	public final static int BitMask4= 0x8; // decimal 8
	public final static int BitMask5= 0x10; // decimal 16
	public final static int BitMask6= 0x20; // decimal 32
	public final static int BitMask7= 0x40; // decimal 64
	public final static int BitMask8= 0x80; // decimal 128
	public final static int BitMask9= 0x100; // decimal 256
	public final static int BitMask10= 0x200; // decimal 512
	public final static int BitMask11= 0x400; // decimal 1024
	public final static int BitMask12= 0x800; // decimal 2048
	public final static int BitMask13= 0x1000; // decimal 4096
	public final static int BitMask14= 0x2000; // decimal 8192
	public final static int BitMask15= 0x4000; // decimal 16384
	public final static int BitMask16= 0x8000; // decimal 32768
	public final static int BitMask17= 0x10000; // decimal 65536
	public final static int BitMask18= 0x20000; // decimal 131072
	public final static int BitMask19= 0x40000; // decimal 262144
	public final static int BitMask20= 0x80000; // decimal 524288
	public final static int BitMask21= 0x100000; // decimal 1048576
	public final static int BitMask22= 0x200000; // decimal 2097152
	public final static int BitMask23= 0x400000; // decimal 4194304
	public final static int BitMask24= 0x800000; // decimal 8388608
	public final static int BitMask25= 0x1000000; // decimal 16777216
	public final static int BitMask26= 0x2000000; // decimal 33554432
	public final static int BitMask27= 0x4000000; // decimal 67108864
	public final static int BitMask28= 0x8000000; // decimal 134217728
	public final static int BitMask29= 0x10000000; // decimal 268435456
	public final static int BitMask30= 0x20000000; // decimal 536870912
	public final static int BitMask31= 0x40000000; // decimal 1073741824
	public final static int BitMask32= 0x80000000; // decimal 2147483648	
	*/
/**
 * Statement constructor comment.
 */
public Statement() {
	super();
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	return flowInfo;
}
public void generateCode(BlockScope currentScope, CodeStream codeStream){
	throw new ShouldNotImplement(Util.bind("ast.missingStatement"/*nonNLS*/));
}
public boolean isEmptyBlock(){
	return false;
}
public boolean isValidJavaStatement(){
	//the use of this method should be avoid in most cases
	//and is here mostly for documentation purpose.....
	//while the parser is responsable for creating
	//welled formed expression statement, which results
	//in the fact that java-non-semantic-expression-used-as-statement
	//should not be parsable...thus not being built.
	//It sounds like the java grammar as help the compiler job in removing
	//-by construction- some statement that would have no effect....
	//(for example all expression that may do side-effects are valid statement
	// -this is an appromative idea.....-)
	

	return true ;}
public void resolve(BlockScope scope) {
}
public Constant resolveCase(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
	// statement within a switch that are not case are treated as normal statement.... 

	resolve(scope);
	return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6106.java