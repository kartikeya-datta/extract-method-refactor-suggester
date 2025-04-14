error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3361.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3361.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3361.java
text:
```scala
C@@onnector c = new Connector(prot,host,port,timeout);

package org.eclipse.ecf.provider.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ServerConfigParser {

	public static final String SERVER_ELEMENT = "server";
	public static final String CONNECTOR_ELEMENT = "connector";
	public static final String GROUP_ELEMENT = "group";
	
	public static final String PROTOCOL_ATTR = "protocol";
	public static final String HOSTNAME_ATTR = "hostname";
	public static final String PORT_ATTR = "port";
	public static final String TIMEOUT_ATTR = "timeout";
	public static final String NAME_ATTR = "name";
	
	protected void findElementsNamed(Node top, String name, List aList) {
        int type = top.getNodeType();
        switch (type) {
        case Node.DOCUMENT_TYPE_NODE:
            // Print entities if any
            NamedNodeMap nodeMap = ((DocumentType) top).getEntities();
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Entity entity = (Entity) nodeMap.item(i);
                findElementsNamed(entity, name, aList);
            }
            break;
        case Node.ELEMENT_NODE:
            String elementName = top.getNodeName();
            if (name.equals(elementName)) {
                aList.add(top);
            }
        default:
            for (Node child = top.getFirstChild(); child != null; child = child
                    .getNextSibling()) {
                findElementsNamed(child, name, aList);
            }
        }
    }
	protected List processConnectorNodes(List connectorNodes) {
		List res = new ArrayList();
		for(Iterator i=connectorNodes.iterator(); i.hasNext(); ) {
			Node n = (Node) i.next();
			String ports = getAttributeValue(n,PORT_ATTR);
			int port = Connector.DEFAULT_PORT;
			if (ports != null) {
				try {
					Integer porti = new Integer(ports);
					port = porti.intValue();
				} catch (NumberFormatException e) {
					// ignore
				}
			}
			String timeouts = getAttributeValue(n,TIMEOUT_ATTR);
			int timeout = Connector.DEFAULT_TIMEOUT;
			if (timeouts != null) {
				try {
					Integer timeouti = new Integer(timeouts);
					timeout = timeouti.intValue();
				} catch (NumberFormatException e) {
					// ignore
				}
			}
			String prot = getAttributeValue(n,PROTOCOL_ATTR);
			String host = getAttributeValue(n,HOSTNAME_ATTR);
			Connector c = new Connector(this, prot,host,port,timeout);
			processConnector(n,c);
			res.add(c);
		}
		return res;
	}
	protected void processConnector(Node n, Connector c) {
		List groupList = new ArrayList();
		findElementsNamed(n,GROUP_ELEMENT,groupList);
		for(Iterator i=groupList.iterator(); i.hasNext(); ) {
			Node node = (Node) i.next();
			String name = getAttributeValue(node,NAME_ATTR);
			if (name != null && !name.equals("")) {
				NamedGroup g = new NamedGroup(name);
				c.addGroup(g);
				g.setParent(c);
			}
		}
	}
	protected List loadConnectors(Document doc) {
    	List ps = new ArrayList();
    	findElementsNamed(doc,CONNECTOR_ELEMENT,ps);
		return processConnectorNodes(ps);
	}
    protected String getAttributeValue(Node node, String attrName) {
        NamedNodeMap attrs = node.getAttributes();
        Node attrNode = attrs.getNamedItem(attrName);
        if (attrNode != null) {
            return attrNode.getNodeValue();
        } else
            return "";
    }
	public List load(InputStream ins) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(ins);
		return loadConnectors(doc);
	}
	
	public static void main(String [] args) throws Exception {
		InputStream ins = new FileInputStream(args[0]);
		ServerConfigParser configParser = new ServerConfigParser();
		List res = configParser.load(ins);
		System.out.println("result is "+res);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3361.java