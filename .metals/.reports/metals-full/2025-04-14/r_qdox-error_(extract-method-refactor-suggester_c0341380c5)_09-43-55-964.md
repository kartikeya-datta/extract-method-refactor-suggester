error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18316.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18316.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18316.java
text:
```scala
private static final L@@ogger log = LoggingManager.getLoggerForClass();

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

package org.apache.jmeter.functions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//@see org.apache.jmeter.functions.PackageTest for unit tests

/**
 * File data container for XML files Data is accessible via XPath
 * 
 */
public class XPathFileContainer {

	private static Logger log = LoggingManager.getLoggerForClass();

	private NodeList nodeList;

	private String fileName; // name of the file

	private String xpath;

	/** Keeping track of which row is next to be read. */
	private int nextRow;
	int getNextRow(){// give access to Test code
		return nextRow;
	}
	
	private XPathFileContainer()// Not intended to be called directly
	{
	}

	public XPathFileContainer(String file, String xpath) throws FileNotFoundException, IOException,
			ParserConfigurationException, SAXException, TransformerException {
		log.debug("XPath(" + file + ") xpath " + xpath + "");
		fileName = file;
		this.xpath = xpath;
		nextRow = 0;
		load();
	}

	private void load() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException,
			TransformerException {
		InputStream fis = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			fis = new FileInputStream(fileName);
			nodeList = XPathAPI.selectNodeList(builder.parse(fis), xpath);
			log.debug("found " + nodeList.getLength());

		} catch (FileNotFoundException e) {
			nodeList = null;
			log.warn(e.toString());
			throw e;
		} catch (IOException e) {
			nodeList = null;
			log.warn(e.toString());
			throw e;
		} catch (ParserConfigurationException e) {
			nodeList = null;
			log.warn(e.toString());
			throw e;
		} catch (SAXException e) {
			nodeList = null;
			log.warn(e.toString());
			throw e;
		} catch (TransformerException e) {
			nodeList = null;
			log.warn(e.toString());
			throw e;
		} finally {
            JOrphanUtils.closeQuietly(fis);
		}
	}

	public String getXPathString(int num) {
		return nodeList.item(num).getNodeValue();
	}

	/**
	 * Returns the next row to the caller, and updates it, allowing for wrap
	 * round
	 * 
	 * @return the first free (unread) row
	 * 
	 */
	public int nextRow() {
		int row = nextRow;
		nextRow++;
		if (nextRow >= size())// 0-based
		{
			nextRow = 0;
		}
		log.debug(new StringBuffer("Row: ").append(row).toString());
		return row;
	}

	public int size() {
		return (nodeList == null) ? -1 : nodeList.getLength();
	}

	/**
	 * @return the file name for this class
	 */
	public String getFileName() {
		return fileName;
	}

}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18316.java