error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3364.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3364.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3364.java
text:
```scala
b@@odyExternal = "<!DOCTYPE root SYSTEM \"http://192.168.28.42/1.jsp\" [" +

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.http.converter.xml;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MockHttpInputMessage;
import org.springframework.http.MockHttpOutputMessage;
import org.springframework.util.FileCopyUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Arjen Poutsma
 */
public class SourceHttpMessageConverterTests {

	private static final String BODY = "<root>Hello World</root>";

	private SourceHttpMessageConverter<Source> converter;

	private String bodyExternal;

	@Before
	public void setUp() throws IOException {
		converter = new SourceHttpMessageConverter<Source>();
		Resource external = new ClassPathResource("external.txt", getClass());

		bodyExternal = "<!DOCTYPE root [" +
				"  <!ELEMENT root ANY >\n" +
				"  <!ENTITY ext SYSTEM \"" + external.getURI() + "\" >]><root>&ext;</root>";
	}

	@Test
	public void canRead() {
		assertTrue(converter.canRead(Source.class, new MediaType("application", "xml")));
		assertTrue(converter.canRead(Source.class, new MediaType("application", "soap+xml")));
	}

	@Test
	public void canWrite() {
		assertTrue(converter.canWrite(Source.class, new MediaType("application", "xml")));
		assertTrue(converter.canWrite(Source.class, new MediaType("application", "soap+xml")));
		assertTrue(converter.canWrite(Source.class, MediaType.ALL));
	}

	@Test
	public void readDOMSource() throws Exception {
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(BODY.getBytes("UTF-8"));
		inputMessage.getHeaders().setContentType(new MediaType("application", "xml"));
		DOMSource result = (DOMSource) converter.read(DOMSource.class, inputMessage);
		Document document = (Document) result.getNode();
		assertEquals("Invalid result", "root", document.getDocumentElement().getLocalName());
	}

	@Test
	public void readDOMSourceExternal() throws Exception {
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(bodyExternal.getBytes("UTF-8"));
		inputMessage.getHeaders().setContentType(new MediaType("application", "xml"));
		DOMSource result = (DOMSource) converter.read(DOMSource.class, inputMessage);
		Document document = (Document) result.getNode();
		assertEquals("Invalid result", "root", document.getDocumentElement().getLocalName());
		assertNotEquals("Invalid result", "Foo Bar", document.getDocumentElement().getTextContent());
	}

	@Test
	public void readSAXSource() throws Exception {
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(BODY.getBytes("UTF-8"));
		inputMessage.getHeaders().setContentType(new MediaType("application", "xml"));
		SAXSource result = (SAXSource) converter.read(SAXSource.class, inputMessage);
		InputSource inputSource = result.getInputSource();
		String s = FileCopyUtils.copyToString(new InputStreamReader(inputSource.getByteStream()));
		assertXMLEqual("Invalid result", BODY, s);
	}

	@Test
	public void readSAXSourceExternal() throws Exception {
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(bodyExternal.getBytes("UTF-8"));
		inputMessage.getHeaders().setContentType(new MediaType("application", "xml"));
		SAXSource result = (SAXSource) converter.read(SAXSource.class, inputMessage);
		InputSource inputSource = result.getInputSource();
		XMLReader reader = result.getXMLReader();
		reader.setContentHandler(new DefaultHandler() {
			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {
				String s = new String(ch, start, length);
				assertNotEquals("Invalid result", "Foo Bar", s);
			}
		});
		reader.parse(inputSource);
	}

	@Test
	public void readStAXSource() throws Exception {
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(BODY.getBytes("UTF-8"));
		inputMessage.getHeaders().setContentType(new MediaType("application", "xml"));
		StAXSource result = (StAXSource) converter.read(StAXSource.class, inputMessage);
		XMLStreamReader streamReader = result.getXMLStreamReader();
		assertTrue(streamReader.hasNext());
		streamReader.nextTag();
		String s = streamReader.getLocalName();
		assertEquals("root", s);
		s = streamReader.getElementText();
		assertEquals("Hello World", s);
		streamReader.close();
	}

	@Test
	public void readStAXSourceExternal() throws Exception {
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(bodyExternal.getBytes("UTF-8"));
		inputMessage.getHeaders().setContentType(new MediaType("application", "xml"));
		StAXSource result = (StAXSource) converter.read(StAXSource.class, inputMessage);
		XMLStreamReader streamReader = result.getXMLStreamReader();
		assertTrue(streamReader.hasNext());
		streamReader.next();
		streamReader.next();
		String s = streamReader.getLocalName();
		assertEquals("root", s);
		s = streamReader.getElementText();
		assertNotEquals("Foo Bar", s);
		streamReader.close();
	}

	@Test
	public void readStreamSource() throws Exception {
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(BODY.getBytes("UTF-8"));
		inputMessage.getHeaders().setContentType(new MediaType("application", "xml"));
		StreamSource result = (StreamSource) converter.read(StreamSource.class, inputMessage);
		String s = FileCopyUtils.copyToString(new InputStreamReader(result.getInputStream()));
		assertXMLEqual("Invalid result", BODY, s);
	}

	@Test
	public void readSource() throws Exception {
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(BODY.getBytes("UTF-8"));
		inputMessage.getHeaders().setContentType(new MediaType("application", "xml"));
		converter.read(Source.class, inputMessage);
	}

	@Test
	public void writeDOMSource() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		Document document = documentBuilderFactory.newDocumentBuilder().newDocument();
		Element rootElement = document.createElement("root");
		document.appendChild(rootElement);
		rootElement.setTextContent("Hello World");
		DOMSource domSource = new DOMSource(document);

		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		converter.write(domSource, null, outputMessage);
		assertXMLEqual("Invalid result", "<root>Hello World</root>",
				outputMessage.getBodyAsString(Charset.forName("UTF-8")));
		assertEquals("Invalid content-type", new MediaType("application", "xml"),
				outputMessage.getHeaders().getContentType());
		assertEquals("Invalid content-length", outputMessage.getBodyAsBytes().length,
				outputMessage.getHeaders().getContentLength());
	}

	@Test
	public void writeSAXSource() throws Exception {
		String xml = "<root>Hello World</root>";
		SAXSource saxSource = new SAXSource(new InputSource(new StringReader(xml)));

		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		converter.write(saxSource, null, outputMessage);
		assertXMLEqual("Invalid result", "<root>Hello World</root>",
				outputMessage.getBodyAsString(Charset.forName("UTF-8")));
		assertEquals("Invalid content-type", new MediaType("application", "xml"),
				outputMessage.getHeaders().getContentType());
	}

	@Test
	public void writeStreamSource() throws Exception {
		String xml = "<root>Hello World</root>";
		StreamSource streamSource = new StreamSource(new StringReader(xml));

		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		converter.write(streamSource, null, outputMessage);
		assertXMLEqual("Invalid result", "<root>Hello World</root>",
				outputMessage.getBodyAsString(Charset.forName("UTF-8")));
		assertEquals("Invalid content-type", new MediaType("application", "xml"),
				outputMessage.getHeaders().getContentType());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3364.java