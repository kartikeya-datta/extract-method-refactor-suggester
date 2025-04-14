error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12466.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12466.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12466.java
text:
```scala
S@@tring s = "foo.bar.honolulu('lola',{true,false,45}).collect(e|2.minor + 34 / (x - 2))";

package org.eclipse.xpand3.parser;

import junit.framework.TestCase;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.eclipse.xpand3.node.Node;
import org.eclipse.xpand3.node.NodeUtil;
import static org.eclipse.xpand3.parser.SyntaxUtil.*;

public class Xpand3NodeParserTest extends TestCase {
	
	private Node parse(String s) throws Exception {
		Xpand3NodeParser parser = createParser(s);
		parser.r_file();
		return parser.getRootNode();
	}
	
	public void testname() throws Exception {
		Node x = parse(LG + "IMPORT foo" + RG
				+ "import foo; myFunction(String this) : doStuff('holla');"
				+ LG + "DEFINE foo FOR Entity" + RG + "bla" + LG + "ENDDEFINE"
				+ RG);
		System.out.println(NodeUtil.toString(x));
	}

	public void testFoo() throws Exception {
		Node node = parse("import foo; myFunction(String this) : doStuff('holla');");
		System.out.println(NodeUtil.toString(node));
		System.out.println(NodeUtil.serialize(node));
	}

	public void testXpandXtendCheckMixedUp1() throws Exception {
		Node node = parse(LG + "IMPORT foo" + RG
				+ "import foo; myFunction(String this) : doStuff('holla');"
				+ LG + "DEFINE foo FOR Entity" + RG + "bla" + LG + "ENDDEFINE"
				+ RG);
		System.out.println(NodeUtil.toString(node));
		System.out.println(NodeUtil.serialize(node));
	}
//	
	public void testPerf() throws Exception {
		String s = "foo.bar.honolulu('lola',[true,false,45]).collect(e|2.minor + 34 / (x - 2))";
		for (int i = 0; i<10;i++) {
			s = s+" -> "+s;
		}
		Xpand3Parser parser = createParser(s);
		long n = System.currentTimeMillis();
		parser.r_expression();
		long after = System.currentTimeMillis();
		System.out.println("Time : "+(after-n)/1000.+" Expressionlength was : "+s.length());
	}

	private Xpand3NodeParser createParser(String s) {
		ANTLRStringStream stream = new ANTLRStringStream(s);
		Xpand3Lexer lexer = new Xpand3Lexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		Xpand3NodeParser parser = new Xpand3NodeParser(tokenStream);
		return parser;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12466.java