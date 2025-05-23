error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7982.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7982.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7982.java
text:
```scala
N@@ode rootNode = xpand3NodeParser.r_test_expression();

package org.eclipse.xpand3.parser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.eclipse.tmf.common.node.CompositeNode;
import org.eclipse.tmf.common.node.Node;
import org.eclipse.tmf.common.node.NodeUtil;

public class Xpand3MigratedNodeParserTest extends AbstractXpand3NodeParserTest {

	public final void testSimple() throws Exception {
		CompositeNode parse = parse("true == null");
		CompositeNode cn = checkIsRule(parse, "relationalExpression", 3);
		checkChildIsTrueLiteral(cn, 0);
		checkChildIsToken(cn, 1, "==");
		CompositeNode nl = checkChildIsRule(cn, 2, "nullLiteral", 1);
		checkChildIsToken(nl, 0, "null");
	}

	public final void testSimple2() throws Exception {
		CompositeNode ie = checkIsRule(
				parse("String.feature.test(true,{\"test\",\"hallo\"})"),
				"infixExpression", 5);
		checkChildIsSimpleType(ie, 0, "String");
		checkChildIsToken(ie, 1, ".");
		checkChildIsSimpleType(ie, 2, "feature");
		checkChildIsToken(ie, 3, ".");
		CompositeNode fc = checkChildIsRule(ie, 4, "featureCall", 4);
		checkChildIsIdentifier(fc, 0, "test");
		checkChildIsToken(fc, 1, "(");
		CompositeNode pl = checkChildIsRule(fc, 2, "parameterList", 3);
		checkChildIsTrueLiteral(pl, 0);
		checkChildIsToken(pl, 1, ",");
		CompositeNode ll = checkChildIsRule(pl, 2, "listLiteral", 5);
		checkChildIsToken(ll, 0, "{");
		checkChildIsStringLiteral(ll, 1, "\"test\"");
		checkChildIsToken(ll, 2, ",");
		checkChildIsStringLiteral(ll, 3, "\"hallo\"");
		checkChildIsToken(ll, 4, "}");
		checkChildIsToken(fc, 3, ")");
	}

	public final void testIfExpression() throws Exception {
		CompositeNode expr = parse("(client.sIdent1 != null) ? client.sIdent1 : \"XXXXXXXX\"");
		CompositeNode ie = checkIsRule(expr, "ifExpression", 5);
		CompositeNode pe = checkChildIsRule(ie, 0, "paranthesizedExpression", 3);
		checkChildIsToken(pe, 0, "(");
		CompositeNode re = checkChildIsRule(pe, 1, "relationalExpression", 3);
		CompositeNode ie0 = checkChildIsRule(re, 0, "infixExpression", 3);
		checkChildIsSimpleType(ie0, 0, "client");
		checkChildIsToken(ie0, 1, ".");
		checkChildIsSimpleType(ie0, 2, "sIdent1");
		checkChildIsToken(re, 1, "!=");
		CompositeNode nl = checkChildIsRule(re, 2, "nullLiteral", 1);
		checkChildIsToken(nl, 0, "null");
		checkChildIsToken(pe, 2, ")");
		checkChildIsToken(ie, 1, "?");
		CompositeNode ie1 = checkChildIsRule(ie, 2, "infixExpression", 3);
		checkChildIsSimpleType(ie1, 0, "client");
		checkChildIsToken(ie1, 1, ".");
		checkChildIsSimpleType(ie1, 2, "sIdent1");
		checkChildIsToken(ie, 3, ":");
		checkChildIsStringLiteral(ie, 4, "\"XXXXXXXX\"");
	}

	public void testIfExpression2() throws Exception {
		Node expr = parse("if true then true else false");
		checkIsRule(expr, "ifExpression", 6);
		checkChildIsToken(expr, 0, "if");
		checkChildIsTrueLiteral(expr, 1);
		checkChildIsToken(expr, 2, "then");
		checkChildIsTrueLiteral(expr, 3);
		checkChildIsToken(expr, 4, "else");
		checkChildIsFalseLiteral(expr, 5);
	}

	public final void testEscaped() throws Exception {
		Node expr = parse("\"\\\"\"");
		checkIsRule(expr, "stringLiteral", 1);
		checkChildIsToken(expr, 0, "\"\\\"\"");
	}

	public final void testNot() throws Exception {
		Node expr = parse("! ts.checked");
		checkIsRule(expr, "unaryExpression", 2);
		checkChildIsToken(expr, 0, "!");
	}

	public final void testCast() throws Exception {
		Node expr = parse("(List[InnerType] ) anExpr");
		checkIsRule(expr, "castedExpression", 4);
		checkChildIsToken(expr, 0, "(");
		CompositeNode ct = checkChildIsRule(expr, 1, "collectionType", 4);
		checkChildIsToken(ct, 0, "List");
		checkChildIsToken(ct, 1, "[");
		checkChildIsSimpleType(ct, 2, "InnerType");
		checkChildIsToken(ct, 3, "]");
		checkChildIsToken(expr, 2, ")");
		checkChildIsSimpleType(expr, 3, "anExpr");
	}

	public final void testGenericType() throws Exception {
		Node expr = parse("List[x]");
		checkIsRule(expr, "collectionType", 4);
		checkChildIsToken(expr, 0, "List");
		checkChildIsToken(expr, 1, "[");
		checkChildIsSimpleType(expr, 2, "x");
		checkChildIsToken(expr, 3, "]");
	}

	public final void testSwitch() throws Exception {
		Node expr = parse("switch { default : true }");
		checkIsRule(expr, "switchExpression", 6);
		checkChildIsToken(expr, 0, "switch");
		checkChildIsToken(expr, 1, "{");
		checkChildIsToken(expr, 2, "default");
		checkChildIsToken(expr, 3, ":");
		checkChildIsTrueLiteral(expr, 4);
		checkChildIsToken(expr, 5, "}");

	}

	public final void testChainExpression() throws Exception {
		Node expr = parse("1 -> 2 -> 3 -> 4");
		checkIsRule(expr, "chainExpression", 7);
		checkChildIsNumberLiteral(expr, 0, "1");
		checkChildIsToken(expr, 1, "->");
		checkChildIsNumberLiteral(expr, 2, "2");
		checkChildIsToken(expr, 3, "->");
		checkChildIsNumberLiteral(expr, 4, "3");
		checkChildIsToken(expr, 5, "->");
		checkChildIsNumberLiteral(expr, 6, "4");
	}

	public final void testPositionInfo() throws Exception {
		Node expr = parse("\n\n\n1");
		assertEquals(4, expr.line());
		assertEquals(3, expr.start());
		assertEquals(4, expr.end());
	}

	public final void testPositionInfo2() throws Exception {
		Node exp = parse("/*\n\n\n*/1");
		assertEquals(4, exp.line());
		assertEquals(7, exp.start());
		assertEquals(8, exp.end());
	}

	public final void testPositionInfo3() throws Exception {
		Node expr = parse("'/*\n\n\n*/'+1");
		assertEquals(1, expr.line());
		assertEquals(0, expr.start());
		assertEquals(11, expr.end());
		checkIsRule(expr, "additiveExpression", 3);
		CompositeNode sl = checkChildIsStringLiteral(expr, 0, "'/*\n\n\n*/'");
		assertEquals(1, sl.line());
		assertEquals(0, sl.start());
		assertEquals(9, sl.end());
		checkChildIsToken(expr, 1, "+");
		CompositeNode il = checkChildIsNumberLiteral(expr, 2, "1");
		assertEquals(4, il.line());
		assertEquals(10, il.start());
		assertEquals(11, il.end());
	}

	public final void testTypeLiterals() throws Exception {
		Node expr = parse("{" + " Object,\n" + " String,\n" + "Collection,\n"
				+ " Set,\n" + " List,\n" + " oaw::Type,\n" + " oaw::Feature,\n"
				+ " oaw::Property\n}");
		checkIsRule(expr, "listLiteral", 17);
		checkChildIsToken(expr, 0, "{");
		checkChildIsSimpleType(expr, 1, "Object");
		checkChildIsToken(expr, 2, ",");
		checkChildIsSimpleType(expr, 3, "String");
		checkChildIsToken(expr, 4, ",");
		checkChildIsCollectionType(expr, 5, "Collection");
		checkChildIsToken(expr, 6, ",");
		checkChildIsCollectionType(expr, 7, "Set");
		checkChildIsToken(expr, 8, ",");
		checkChildIsCollectionType(expr, 9, "List");
		checkChildIsToken(expr, 10, ",");
		checkChildIsScopedType(expr, 11, "oaw", "Type");
		checkChildIsToken(expr, 12, ",");
		checkChildIsScopedType(expr, 13, "oaw", "Feature");
		checkChildIsToken(expr, 14, ",");
		checkChildIsScopedType(expr, 15, "oaw", "Property");
		checkChildIsToken(expr, 16, "}");
	}

	private CompositeNode parse(String s) throws Exception {
		ANTLRStringStream stream = new ANTLRStringStream(s);
		Xpand3NodeLexer lexer = new Xpand3NodeLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		Xpand3NodeParser xpand3NodeParser = new Xpand3NodeParser(tokenStream);
		Node rootNode = xpand3NodeParser.test_expression();
		System.out.println(NodeUtil.toString(rootNode));
		return (CompositeNode) rootNode;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7982.java