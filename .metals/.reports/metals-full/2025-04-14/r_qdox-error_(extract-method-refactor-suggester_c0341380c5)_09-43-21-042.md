error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8537.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8537.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8537.java
text:
```scala
private transient N@@ode xmlNode;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.visualizers;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * A extended class of DefaultMutableTreeNode except that it also attached XML
 * node and convert XML document into DefaultMutableTreeNode author <a
 * href="mailto:d.maung@mdl.com">Dave Maung</a>
 * 
 */
public class XMLDefaultMutableTreeNode extends DefaultMutableTreeNode {
    private static final Logger log = LoggingManager.getLoggerForClass();
	// private static final int LIMIT_STR_SIZE = 100;
	// private boolean isRoot;
	private Node xmlNode;

    public XMLDefaultMutableTreeNode(){
        log.warn("Constructor only intended for use in testing"); // $NON-NLS-1$
    }
    
	public XMLDefaultMutableTreeNode(Node root) throws SAXException {
		super(root.getNodeName());
		initAttributeNode(root, this);
		initRoot(root);

	}

	public XMLDefaultMutableTreeNode(String name, Node xmlNode) {
		super(name);
		this.xmlNode = xmlNode;

	}

	/**
	 * init root
	 * 
	 * @param root
	 * @throws SAXException
	 */
	private void initRoot(Node xmlRoot) throws SAXException {

		NodeList childNodes = xmlRoot.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			initNode(childNode, this);
		}

	}

	/**
	 * init node
	 * 
	 * @param node
	 * @param mTreeNode
	 * @throws SAXException
	 */
	private void initNode(Node node, XMLDefaultMutableTreeNode mTreeNode) throws SAXException {

		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			initElementNode(node, mTreeNode);
			break;

		case Node.TEXT_NODE:
			initTextNode((Text) node, mTreeNode);
			break;

		case Node.CDATA_SECTION_NODE:
			initCDATASectionNode((CDATASection) node, mTreeNode);
			break;
		case Node.COMMENT_NODE:
			initCommentNode((Comment) node, mTreeNode);
			break;

		default:
			// if other node type, we will just skip it
			break;

		}

	}

	/**
	 * init element node
	 * 
	 * @param node
	 * @param mTreeNode
	 * @throws SAXException
	 */
	private void initElementNode(Node node, DefaultMutableTreeNode mTreeNode) throws SAXException {
		String nodeName = node.getNodeName();

		NodeList childNodes = node.getChildNodes();
		XMLDefaultMutableTreeNode childTreeNode = new XMLDefaultMutableTreeNode(nodeName, node);

		mTreeNode.add(childTreeNode);
		initAttributeNode(node, childTreeNode);
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			initNode(childNode, childTreeNode);
		}

	}

	/**
	 * init attribute node
	 * 
	 * @param node
	 * @param mTreeNode
	 * @throws SAXException
	 */
	private void initAttributeNode(Node node, DefaultMutableTreeNode mTreeNode) throws SAXException {
		NamedNodeMap nm = node.getAttributes();
		for (int i = 0; i < nm.getLength(); i++) {
			Attr nmNode = (Attr) nm.item(i);
			String value = nmNode.getName() + " = \"" + nmNode.getValue() + "\""; // $NON-NLS-1$ $NON-NLS-2$
			XMLDefaultMutableTreeNode attributeNode = new XMLDefaultMutableTreeNode(value, nmNode);
			mTreeNode.add(attributeNode);

		}
	}

	/**
	 * init comment Node
	 * 
	 * @param node
	 * @param mTreeNode
	 * @throws SAXException
	 */
	private void initCommentNode(Comment node, DefaultMutableTreeNode mTreeNode) throws SAXException {
		String data = node.getData();
		if (data != null && data.length() > 0) {
			String value = "<!--" + node.getData() + "-->"; // $NON-NLS-1$ $NON-NLS-2$
			XMLDefaultMutableTreeNode commentNode = new XMLDefaultMutableTreeNode(value, node);
			mTreeNode.add(commentNode);
		}
	}

	/**
	 * init CDATASection Node
	 * 
	 * @param node
	 * @param mTreeNode
	 * @throws SAXException
	 */
	private void initCDATASectionNode(CDATASection node, DefaultMutableTreeNode mTreeNode) throws SAXException {
		String data = node.getData();
		if (data != null && data.length() > 0) {
			String value = "<!-[CDATA" + node.getData() + "]]>"; // $NON-NLS-1$ $NON-NLS-2$
			XMLDefaultMutableTreeNode commentNode = new XMLDefaultMutableTreeNode(value, node);
			mTreeNode.add(commentNode);
		}
	}

	/**
	 * init the TextNode
	 * 
	 * @param node
	 * @param mTreeNode
	 * @throws SAXException
	 */
	private void initTextNode(Text node, DefaultMutableTreeNode mTreeNode) throws SAXException {
		String text = node.getNodeValue().trim();
		if (text != null && text.length() > 0) {
			XMLDefaultMutableTreeNode textNode = new XMLDefaultMutableTreeNode(text, node);
			mTreeNode.add(textNode);
		}
	}

	/**
	 * get the xml node
	 * 
	 * @return
	 */
	public Node getXMLNode() {
		return xmlNode;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8537.java