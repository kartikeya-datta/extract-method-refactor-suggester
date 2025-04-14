error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10381.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10381.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10381.java
text:
```scala
a@@ssertEquals(type, HttpTagType.CONDITIONAL_COMMENT_ENDIF);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.parser;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import junit.framework.TestCase;

import org.apache.wicket.markup.parser.IXmlPullParser.HttpTagType;
import org.apache.wicket.util.resource.StringResourceStream;

/**
 * Quite some tests are already with MarkupParser.
 * 
 * @author Juergen Donnerstag
 */
public class XmlPullParserTest extends TestCase
{
	/**
	 * 
	 * @throws Exception
	 */
	public final void testBasics() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("This is a text");
		XmlTag elem = parser.nextTag();
		assertNull(elem);

		parser.parse("<tag/>");
		XmlTag tag = parser.nextTag();
		assertFalse(tag.isOpen());
		// assertFalse(tag.isOpen("tag"));
		// assertFalse(tag.isOpen("xxx"));
		assertFalse(tag.isClose());
		assertTrue(tag.isOpenClose());
		// assertTrue(tag.isOpenClose("tag"));
		assertEquals("tag", tag.getName());
		assertNull(tag.getNamespace());
		assertEquals(0, tag.getAttributes().size());

		// extra spaces
		parser.parse("<tag ></tag >");
		tag = parser.nextTag();
		assertTrue(tag.isOpen());
		// assertTrue(tag.isOpen("tag"));
		// assertFalse(tag.isOpen("xxx"));
		assertFalse(tag.isClose());
		assertFalse(tag.isOpenClose());
		// assertFalse(tag.isOpenClose("tag"));
		assertEquals("tag", tag.getName());
		assertNull(tag.getNamespace());
		assertEquals(0, tag.getAttributes().size());

		tag = parser.nextTag();
		assertFalse(tag.isOpen());
		// assertFalse(tag.isOpen("tag"));
		// assertFalse(tag.isOpen("xxx"));
		assertTrue(tag.isClose());
		assertFalse(tag.isOpenClose());
		// assertFalse(tag.isOpenClose("tag"));
		assertEquals("tag", tag.getName());
		assertNull(tag.getNamespace());
		assertEquals(0, tag.getAttributes().size());

		parser.parse("<tag>  </tag>");
		tag = parser.nextTag();
		// assertTrue(tag.isOpen("tag"));
		tag = parser.nextTag();
		assertTrue(tag.isClose());

		parser.parse("xx <tag> yy </tag> zz");
		tag = parser.nextTag();
		// assertTrue(tag.isOpen("tag"));
		tag = parser.nextTag();
		assertTrue(tag.isClose());

		// XmlPullParser does NOT check that tags get properly closed
		parser.parse("<tag>");
		tag = parser.nextTag();
		tag = parser.nextTag();
		assertNull(elem);

		parser.parse("<tag> <tag> <tag>");
		tag = parser.nextTag();
		// assertTrue(tag.isOpen("tag"));
		tag = parser.nextTag();
// assertTrue(tag.isOpen("tag"));
		tag = parser.nextTag();
// assertTrue(tag.isOpen("tag"));

		parser.parse("<ns:tag/>");
		tag = parser.nextTag();
// assertTrue(tag.isOpenClose("tag"));
		assertEquals("ns", tag.getNamespace());
		assertEquals("tag", tag.getName());

		parser.parse("<ns:tag></ns:tag>");
		tag = parser.nextTag();
// assertTrue(tag.isOpen("tag"));
		assertEquals("ns", tag.getNamespace());
		assertEquals("tag", tag.getName());

		XmlTag closeTag = parser.nextTag();
		assertTrue(closeTag.isClose());
		assertEquals("ns", closeTag.getNamespace());
		assertEquals("tag", closeTag.getName());
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testEncoding() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse(
			new StringResourceStream("<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>").getInputStream(),
			null);
		assertEquals("iso-8859-1", parser.getEncoding());
		XmlTag tag = parser.nextTag();
		assertNull(tag);

		parser.parse(new StringResourceStream(
			"<?xml version=\"1.0\" encoding='iso-8859-1' ?> test test").getInputStream(), null);
		assertEquals("iso-8859-1", parser.getEncoding());
		tag = parser.nextTag();
		assertNull(tag);

		// re-order and move close (remove whitespaces
		parser.parse(new StringResourceStream(
			"   <?xml encoding='iso-8859-1'version=\"1.0\"?> test test").getInputStream(), null);
		assertEquals("iso-8859-1", parser.getEncoding());
		tag = parser.nextTag();
		assertNull(tag);

		// attribute value must be enclosed by ""
		parser.parse(
			new StringResourceStream("<?xml encoding=iso-8859-1 ?> test test").getInputStream(),
			null);
		assertEquals("iso-8859-1", parser.getEncoding());

		// Invaluid encoding
		Exception ex = null;
		try
		{
			parser.parse(new StringResourceStream("<?xml encoding='XXX' ?>").getInputStream(), null);
		}
		catch (UnsupportedEncodingException e)
		{
			ex = e;
		}
		assertNotNull(ex);

		// no extra characters allowed before <?xml>
		// TODO General: I'd certainly prefer an exception
		parser.parse(
			new StringResourceStream("xxxx <?xml encoding='iso-8859-1' ?>").getInputStream(), null);
		assertNull(parser.getEncoding());
		tag = parser.nextTag();
		assertNull(tag);

		// no extra characters allowed before <?xml>
		// Are comments allowed preceding the encoding string?
		parser.parse(
			new StringResourceStream("<!-- Comment --> <?xml encoding='iso-8859-1' ?>").getInputStream(),
			null);
		assertNull(parser.getEncoding());
		tag = parser.nextTag();
		assertNull(tag);

		// 'test' is not a valid attribut. But we currently don't test it.
		parser.parse(new StringResourceStream("<?xml test='123' >").getInputStream(), null);
		assertNull(parser.getEncoding());
		tag = parser.nextTag();
		assertNull(tag);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testAttributes() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<tag>");
		XmlTag tag = parser.nextTag();
		assertEquals(0, tag.getAttributes().size());
		// assertTrue(tag.isOpen("tag"));
		assertFalse(tag.getAttributes().containsKey("attr"));
		assertNull(tag.getAttributes().getString("attr"));

		parser.parse("<tag attr='1234'>");
		tag = parser.nextTag();
		assertEquals(1, tag.getAttributes().size());
		assertTrue(tag.getAttributes().containsKey("attr"));
		assertEquals("1234", tag.getAttributes().getString("attr"));

		parser.parse("<tag attr=1234>");
		tag = parser.nextTag();
		assertEquals(1, tag.getAttributes().size());
		assertTrue(tag.getAttributes().containsKey("attr"));
		assertEquals("1234", tag.getAttributes().getString("attr"));

		parser.parse("<tag attr=1234 >");
		tag = parser.nextTag();
		assertEquals(1, tag.getAttributes().size());
		assertTrue(tag.getAttributes().containsKey("attr"));
		assertEquals("1234", tag.getAttributes().getString("attr"));

		parser.parse("<tag attr-withHypen=1234 >");
		tag = parser.nextTag();
		assertEquals(1, tag.getAttributes().size());
		assertTrue(tag.getAttributes().containsKey("attr-withHypen"));
		assertEquals("1234", tag.getAttributes().getString("attr-withHypen"));

		parser.parse("<tag attr=\"1234\">");
		tag = parser.nextTag();
		assertEquals(1, tag.getAttributes().size());
		assertTrue(tag.getAttributes().containsKey("attr"));
		assertEquals("1234", tag.getAttributes().getString("attr"));

		parser.parse("<tag attr='1234' test='23'>");
		tag = parser.nextTag();
		assertEquals(2, tag.getAttributes().size());
		assertTrue(tag.getAttributes().containsKey("attr"));
		assertEquals("1234", tag.getAttributes().getString("attr"));
		assertTrue(tag.getAttributes().containsKey("test"));
		assertEquals("23", tag.getAttributes().getString("test"));

		parser.parse("<tag attr='1234' attr='23'>");
		Exception ex = null;
		try
		{
			tag = parser.nextTag();
		}
		catch (ParseException e)
		{
			ex = e;
		}
		assertNotNull(ex);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testComments() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<!-- test --><tag>");
		XmlTag tag = parser.nextTag();
// assertTrue(tag.isOpen("tag"));

		parser.parse("<!-- test --><tag> aaa <!-- test 1 --> bbb <tag> <!-- test --> </tag>");
		tag = parser.nextTag();
// assertTrue(tag.isOpen("tag"));
		tag = parser.nextTag();
// assertTrue(tag.isOpen("tag"));
		tag = parser.nextTag();
		assertTrue(tag.isClose());
		tag = parser.nextTag();
		assertNull(tag);

		// As you can see, XmlPullParser is really a shallow parser only
		parser.parse("<!-- test --><tag> aaa <?tag test 1 ?> bbb <tag> <!DOCTYPE test > </tag>");
		tag = parser.nextTag();
// assertTrue(tag.isOpen("tag"));
		tag = parser.nextTag();
// assertTrue(tag.isOpen("tag"));
		tag = parser.nextTag();
		assertTrue(tag.isClose());
		tag = parser.nextTag();
		assertNull(tag);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testCompressWhitespace() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>");
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testScript() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<html><script language=\"JavaScript\">... <x a> ...</script></html>");
		XmlTag tag = parser.nextTag();
		assertTrue(tag.isOpen());
		assertEquals("html", tag.getName());
		tag = parser.nextTag();
		assertTrue(tag.isOpen());
		assertEquals("script", tag.getName());
		tag = parser.nextTag();
		assertTrue(tag.isClose());
		assertEquals("script", tag.getName());
		tag = parser.nextTag();
		assertTrue(tag.isClose());
		assertEquals("html", tag.getName());
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testConditionalComments() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<!--[if IE]><a href='test.html'>my link</a><![endif]-->");
		XmlTag tag = parser.nextTag();
		assertTrue(tag.isOpen());
		assertEquals("a", tag.getName());
		tag = parser.nextTag();
		assertTrue(tag.isClose());
		assertEquals("a", tag.getName());
		tag = parser.nextTag();
		assertNull(tag);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testConditionalComments2() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<!--[if IE]><a href='test.html'>my link</a><![endif]-->");
		HttpTagType type = parser.next();
		assertEquals(type, HttpTagType.CONDITIONAL_COMMENT);
		type = parser.next();
		assertEquals(type, HttpTagType.TAG);
		assertTrue((parser.getElement()).isOpen());
		type = parser.next();
		assertEquals(type, HttpTagType.BODY);
		type = parser.next();
		assertEquals(type, HttpTagType.TAG);
		assertEquals("a", (parser.getElement()).getName());
		assertTrue((parser.getElement()).isClose());
		type = parser.next();
		assertEquals(type, HttpTagType.CONDITIONAL_COMMENT);
		type = parser.next();
		assertEquals(type, HttpTagType.NOT_INITIALIZED);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testNames() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<filter-mapping>");
		XmlTag tag = parser.nextTag();
		assertTrue(tag.isOpen());
		assertEquals("filter-mapping", tag.getName());

		parser.parse("<filter.mapping>");
		tag = parser.nextTag();
		assertTrue(tag.isOpen());
		assertEquals("filter.mapping", tag.getName());

		parser.parse("<filter_mapping>");
		tag = parser.nextTag();
		assertTrue(tag.isOpen());
		assertEquals("filter_mapping", tag.getName());
	}

	/**
	 * 
	 * @throws Exception
	 */
	public final void testDoctype() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<!DOCTYPE html>");
		HttpTagType type = parser.next();
		assertEquals(HttpTagType.DOCTYPE, type);
		assertEquals("!DOCTYPE html", parser.getDoctype());
	}

	/**
	 * @throws Exception
	 */
	public final void testDownlevelRevealedConditionalComments() throws Exception
	{
		final XmlPullParser parser = new XmlPullParser();
		parser.parse("<!--[if (gt IE 9)|!(IE)]><!--><html lang=\"en\" class=\"no-js\"><!--<![endif]--> <span>test</span>");
		HttpTagType type = parser.next();
		assertEquals(HttpTagType.CONDITIONAL_COMMENT, type);

		type = parser.next();
		assertEquals(HttpTagType.COMMENT, type);

		type = parser.next();
		assertEquals(HttpTagType.TAG, type);
		XmlTag componentTag = parser.getElement();
		assertEquals("html", componentTag.getName());

		type = parser.next();
		assertEquals(HttpTagType.CONDITIONAL_COMMENT_ENDIF, type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10381.java