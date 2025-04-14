error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14621.java
text:
```scala
t@@est("org/eclipse/xpand3/parser/node2ast/test.ttst");

package org.eclipse.xpand3.parser.node2ast;

import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.tmf.common.node.Node;
import org.eclipse.tmf.common.treetest.Body;
import org.eclipse.tmf.common.treetest.Expression;
import org.eclipse.tmf.common.treetest.NodeRef;
import org.eclipse.tmf.common.treetest.PropertyCheck;
import org.eclipse.tmf.common.treetest.PropertyList;
import org.eclipse.tmf.common.treetest.Test;
import org.eclipse.tmf.common.treetest.TestSpec;
import org.eclipse.tmf.common.treetest.TreetestPackage;
import org.eclipse.tmf.common.treetest.parser.XtextParser;
import org.eclipse.xpand3.SyntaxElement;
import org.eclipse.xpand3.parser.Xpand3NodeLexer;
import org.eclipse.xpand3.parser.Xpand3NodeParser;

public class TreetestInterpreterTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"ecore", new XMIResourceFactoryImpl());

		EPackage treePackage = TreetestPackage.eINSTANCE;
		EPackage.Registry.INSTANCE.put(treePackage.getNsURI(), treePackage);
	}

	public void testTree() throws RecognitionException {
		test("org/eclipse/xpand3/parser/node2ast/test.tree");
	}

	private void test(String testFileName) throws RecognitionException {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream testFile = classLoader.getResourceAsStream(testFileName);
		XtextParser treeSpecParser = new XtextParser(testFile);
		TestSpec testSpec = (TestSpec) treeSpecParser.getRootNode()
				.getModelElement();
		System.out.println("Loading test spec from " + testSpec.getName());
		EList<Test> tests = testSpec.getTest();
		for (Test test : tests) {
			System.out.println("Starting test " + test.getName());
			Expression testExpression = test.getExpr();
			SyntaxElement rootElement = parseAndTransform(testExpression);
			performTest(rootElement, test.getExpected());
		}

	}

	private SyntaxElement parseAndTransform(Expression testExpression)
			throws RecognitionException {
		String body = testExpression.getBody();
		body = body.substring(2, body.length() - 2);
		System.out.println("Expression:" + body);
		ANTLRStringStream stream = new ANTLRStringStream(body);
		Xpand3NodeLexer lexer = new Xpand3NodeLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		Xpand3NodeParser xpand3NodeParser = new Xpand3NodeParser(tokenStream);

		Node rootNode = xpand3NodeParser.r_file();
		if (rootNode == null) {
			System.out.println("Nothing parsed.");
		}
		Node2AstTransformer node2AstTransformer = new Node2AstTransformer();
		return node2AstTransformer.doSwitch(rootNode);
	}

	@SuppressWarnings("unchecked")
	protected void performTest(EObject object,
			org.eclipse.tmf.common.treetest.Node testRootNode) {
		EClass eClass = object.eClass();
		assertEquals(eClass.getName(), testRootNode.getClassName());
		PropertyList propertyList = testRootNode.getPropertyList();
		if (propertyList != null) {
			for (PropertyCheck propertyCheck : propertyList.getChecks()) {
				String name = propertyCheck.getName();
				EStructuralFeature attribute = eClass
						.getEStructuralFeature(name);
				String value = object.eGet(attribute).toString();
				assertEquals(propertyCheck.getValue(), value);
			}
		}
		Body body = testRootNode.getBody();
		if (body != null) {
			EList<NodeRef> nodeRefs = body.getChildren();
			for (int i = 0; i < nodeRefs.size(); ++i) {
				NodeRef nodeRef = nodeRefs.get(i);
				EStructuralFeature reference = eClass
						.getEStructuralFeature(nodeRefs.get(i).getRefName());
				Object value = object.eGet(reference);
				EList<org.eclipse.tmf.common.treetest.Node> refNodes = nodeRef
						.getNodes();
				if (reference.isMany()) {
					List listValue = (List) value;
					if (refNodes.isEmpty()) {
						assertTrue(listValue == null || listValue.isEmpty());
						return;
					}
					assertTrue(refNodes.size() <= listValue.size());
					for (int j = 0; j < refNodes.size(); ++j) {
						assertTrue(listValue.get(j) instanceof EObject);
						performTest((EObject) listValue.get(j), refNodes.get(j));
					}
				} else {
					if (refNodes.isEmpty()) {
						assertTrue(value == null);
						return;
					}
					assertTrue(refNodes.size() <= 1);
					org.eclipse.tmf.common.treetest.Node refNode = refNodes
							.get(0);
					assertTrue(value instanceof EObject);
					performTest((EObject) value, refNode);
				}
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14621.java