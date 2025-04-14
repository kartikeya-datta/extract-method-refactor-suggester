error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7983.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7983.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7983.java
text:
```scala
N@@ode rootNode = xpand3NodeParser.r_file();

package org.eclipse.xpand3.parser.node2ast;

import java.io.StringReader;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.tmf.common.node.Node;
import org.eclipse.xpand3.parser.Xpand3NodeLexer;
import org.eclipse.xpand3.parser.Xpand3NodeParser;
import org.openarchitectureware.xtend.parser.ParseFacade;

public class ParseStuff {
	private static boolean useNodeParser;

	/**
	 * @param args
	 * @throws RecognitionException
	 */
	public static void main(String[] args) throws RecognitionException {
		useNodeParser = false;
		checkFor(1);
		checkFor(10);
		checkFor(100);
		checkFor(1000);
		checkFor(5000);
		checkFor(50000);
		useNodeParser = true;
		checkFor(1);
		checkFor(10);
		checkFor(100);
		checkFor(1000);
		checkFor(5000);
		checkFor(50000);

	}

	public static void parseWithNodeParser(String s)
			throws RecognitionException {
		ANTLRStringStream stream = new ANTLRStringStream(s);
		Xpand3NodeLexer lexer = new Xpand3NodeLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		Xpand3NodeParser xpand3NodeParser = new Xpand3NodeParser(tokenStream);

		Node rootNode = xpand3NodeParser.file();
		// if (rootNode == null) {
		// System.out.println("Nothing parsed.");
		// } else {
		// System.out.println(NodeUtil.toString(rootNode));
		// }
		Node2AstTransformer node2AstTransformer = new Node2AstTransformer();
		node2AstTransformer.doSwitch(rootNode);
	}

	private static void checkFor(int extensions) throws RecognitionException {
		StringBuffer file = new StringBuffer();
		for (int i = 0; i < extensions; i++) {
			file
					.append("\nfoo")
					.append(i)
					.append(
							"(Object this, Object that) : this.toString() == that.toString();");
		}
		long before = System.currentTimeMillis();
		if (useNodeParser) {
			parseWithNodeParser(file.toString());
		} else {
			ParseFacade.file(new StringReader(file.toString()), "foo.xpt");
		}
		long after = System.currentTimeMillis();
		System.out.println("Time for " + extensions + " extensions: "
				+ (after - before));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7983.java