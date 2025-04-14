error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3363.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3363.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3363.java
text:
```scala
S@@tring content =  "<!DOCTYPE root SYSTEM \"http://192.168.28.42/1.jsp\" [" +

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

import java.nio.charset.Charset;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import static org.custommonkey.xmlunit.XMLAssert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MockHttpInputMessage;
import org.springframework.http.MockHttpOutputMessage;

/**
 * Tests for {@link Jaxb2RootElementHttpMessageConverter}.
 *
 * @author Arjen Poutsma
 * @author Sebastien Deleuze
 */
public class Jaxb2RootElementHttpMessageConverterTests {

	private Jaxb2RootElementHttpMessageConverter converter;

	private RootElement rootElement;

	private RootElement rootElementCglib;

	@Before
	public void setUp() {
		converter = new Jaxb2RootElementHttpMessageConverter();
		rootElement = new RootElement();
		DefaultAopProxyFactory proxyFactory = new DefaultAopProxyFactory();
		AdvisedSupport advisedSupport = new AdvisedSupport();
		advisedSupport.setTarget(rootElement);
		advisedSupport.setProxyTargetClass(true);
		AopProxy proxy = proxyFactory.createAopProxy(advisedSupport);
		rootElementCglib = (RootElement) proxy.getProxy();
	}

	@Test
	public void canRead() throws Exception {
		assertTrue("Converter does not support reading @XmlRootElement", converter.canRead(RootElement.class, null));
		assertTrue("Converter does not support reading @XmlType", converter.canRead(Type.class, null));
	}

	@Test
	public void canWrite() throws Exception {
		assertTrue("Converter does not support writing @XmlRootElement", converter.canWrite(RootElement.class, null));
		assertTrue("Converter does not support writing @XmlRootElement subclass", converter.canWrite(RootElementSubclass.class, null));
		assertTrue("Converter does not support writing @XmlRootElement subclass", converter.canWrite(rootElementCglib.getClass(), null));
		assertFalse("Converter supports writing @XmlType", converter.canWrite(Type.class, null));
	}

	@Test
	public void readXmlRootElement() throws Exception {
		byte[] body = "<rootElement><type s=\"Hello World\"/></rootElement>".getBytes("UTF-8");
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(body);
		RootElement result = (RootElement) converter.read(RootElement.class, inputMessage);
		assertEquals("Invalid result", "Hello World", result.type.s);
	}

	@Test
	public void readXmlRootElementSubclass() throws Exception {
		byte[] body = "<rootElement><type s=\"Hello World\"/></rootElement>".getBytes("UTF-8");
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(body);
		RootElementSubclass result = (RootElementSubclass) converter.read(RootElementSubclass.class, inputMessage);
		assertEquals("Invalid result", "Hello World", result.getType().s);
	}

	@Test
	public void readXmlType() throws Exception {
		byte[] body = "<foo s=\"Hello World\"/>".getBytes("UTF-8");
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(body);
		Type result = (Type) converter.read(Type.class, inputMessage);
		assertEquals("Invalid result", "Hello World", result.s);
	}

	@Test
	public void readXmlRootElementExternalEntityDisabled() throws Exception {
		Resource external = new ClassPathResource("external.txt", getClass());
		String content =  "<!DOCTYPE root [" +
				"  <!ELEMENT external ANY >\n" +
				"  <!ENTITY ext SYSTEM \"" + external.getURI() + "\" >]>" +
				"  <rootElement><external>&ext;</external></rootElement>";
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(content.getBytes("UTF-8"));
		RootElement rootElement = (RootElement) converter.read(RootElement.class, inputMessage);

		assertEquals("", rootElement.external);
	}

	@Test
	public void readXmlRootElementExternalEntityEnabled() throws Exception {
		Resource external = new ClassPathResource("external.txt", getClass());
		String content =  "<!DOCTYPE root [" +
				"  <!ELEMENT external ANY >\n" +
				"  <!ENTITY ext SYSTEM \"" + external.getURI() + "\" >]>" +
				"  <rootElement><external>&ext;</external></rootElement>";
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(content.getBytes("UTF-8"));
		this.converter.setProcessExternalEntities(true);
		RootElement rootElement = (RootElement) converter.read(RootElement.class, inputMessage);

		assertEquals("Foo Bar", rootElement.external);
	}

	@Test
	public void writeXmlRootElement() throws Exception {
		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		converter.write(rootElement, null, outputMessage);
		assertEquals("Invalid content-type", new MediaType("application", "xml"),
				outputMessage.getHeaders().getContentType());
		assertXMLEqual("Invalid result", "<rootElement><type s=\"Hello World\"/></rootElement>",
				outputMessage.getBodyAsString(Charset.forName("UTF-8")));
	}

	@Test
	public void writeXmlRootElementSubclass() throws Exception {
		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		converter.write(rootElementCglib, null, outputMessage);
		assertEquals("Invalid content-type", new MediaType("application", "xml"),
				outputMessage.getHeaders().getContentType());
		assertXMLEqual("Invalid result", "<rootElement><type s=\"Hello World\"/></rootElement>",
				outputMessage.getBodyAsString(Charset.forName("UTF-8")));
	}

	// SPR-11488

	@Test
	public void customizeMarshaller() throws Exception {
		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		MyJaxb2RootElementHttpMessageConverter myConverter = new MyJaxb2RootElementHttpMessageConverter();
		myConverter.write(new MyRootElement(new MyCustomElement("a", "b")), null, outputMessage);
		assertXMLEqual("Invalid result", "<myRootElement><element>a|||b</element></myRootElement>",
				outputMessage.getBodyAsString(Charset.forName("UTF-8")));
	}

	@Test
	public void customizeUnmarshaller() throws Exception {
		byte[] body = "<myRootElement><element>a|||b</element></myRootElement>".getBytes("UTF-8");
		MyJaxb2RootElementHttpMessageConverter myConverter = new MyJaxb2RootElementHttpMessageConverter();
		MockHttpInputMessage inputMessage = new MockHttpInputMessage(body);
		MyRootElement result = (MyRootElement) myConverter.read(MyRootElement.class, inputMessage);
		assertEquals("a", result.getElement().getField1());
		assertEquals("b", result.getElement().getField2());
	}

	@XmlRootElement
	public static class RootElement {

		private Type type = new Type();

		@XmlElement(required=false)
		public String external;

		public Type getType() {
			return this.type;
		}

		@XmlElement
		public void setType(Type type) {
			this.type = type;
		}
	}

	@XmlType
	public static class Type {

		@XmlAttribute
		public String s = "Hello World";

	}

	public static class RootElementSubclass extends RootElement {

	}

	public static class MyJaxb2RootElementHttpMessageConverter extends Jaxb2RootElementHttpMessageConverter {

		@Override
		protected void customizeMarshaller(Marshaller marshaller) {
			marshaller.setAdapter(new MyCustomElementAdapter());
		}

		@Override
		protected void customizeUnmarshaller(Unmarshaller unmarshaller) {
			unmarshaller.setAdapter(new MyCustomElementAdapter());
		}
	}

	public static class MyCustomElement {
		private String field1;
		private String field2;

		public MyCustomElement() {
		}

		public MyCustomElement(String field1, String field2) {
			this.field1 = field1;
			this.field2 = field2;
		}

		public String getField1() {
			return field1;
		}

		public void setField1(String field1) {
			this.field1 = field1;
		}

		public String getField2() {
			return field2;
		}

		public void setField2(String field2) {
			this.field2 = field2;
		}
	}

	@XmlRootElement
	public static class MyRootElement {

		private MyCustomElement element;

		public MyRootElement() {

		}

		public MyRootElement(MyCustomElement element) {
			this.element = element;
		}

		@XmlJavaTypeAdapter(MyCustomElementAdapter.class)
		public MyCustomElement getElement() {
			return element;
		}

		public void setElement(MyCustomElement element) {
			this.element = element;
		}
	}

	public static class MyCustomElementAdapter extends XmlAdapter<String, MyCustomElement> {

		@Override
		public String marshal(MyCustomElement c) throws Exception {
			return c.getField1() + "|||" + c.getField2();
		}

		@Override
		public MyCustomElement unmarshal(String c) throws Exception {
			String[] t = c.split("\\|\\|\\|");
			return new MyCustomElement(t[0], t[1]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3363.java