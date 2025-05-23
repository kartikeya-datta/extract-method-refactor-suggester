error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14451.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14451.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14451.java
text:
```scala
M@@ediaType textPlain = MediaType.TEXT_PLAIN;

/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.http;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import org.junit.Test;

/** @author Arjen Poutsma */
public class MediaTypeTests {

	@Test
	public void includes() throws Exception {
		MediaType textPlain = new MediaType("text", "plain");
		assertTrue("Equal types is not inclusive", textPlain.includes(textPlain));
		MediaType allText = new MediaType("text");
		assertTrue("All subtypes is not inclusive", allText.includes(textPlain));
		assertFalse("All subtypes is not inclusive", textPlain.includes(allText));
		assertTrue("All types is not inclusive", MediaType.ALL.includes(textPlain));
		assertFalse("All types is not inclusive", textPlain.includes(MediaType.ALL));

		MediaType applicationSoapXml = new MediaType("application", "soap+xml");
		MediaType applicationWildcardXml = new MediaType("application", "*+xml");

		assertTrue(applicationSoapXml.includes(applicationSoapXml));
		assertTrue(applicationWildcardXml.includes(applicationWildcardXml));

		assertTrue(applicationWildcardXml.includes(applicationSoapXml));
		assertFalse(applicationSoapXml.includes(applicationWildcardXml));
	}

	@Test
	public void testToString() throws Exception {
		MediaType mediaType = new MediaType("text", "plain", 0.7);
		String result = mediaType.toString();
		assertEquals("Invalid toString() returned", "text/plain;q=0.7", result);
	}

	@Test(expected= IllegalArgumentException.class)
	public void slashInType() {
		new MediaType("text/plain");
	}

	@Test(expected= IllegalArgumentException.class)
	public void slashInSubtype() {
		new MediaType("text", "/");
	}
	
	@Test
	public void getDefaultQualityValue() {
		MediaType mediaType = new MediaType("text", "plain");
		assertEquals("Invalid quality value", 1, mediaType.getQualityValue(), 0D);
	}

	@Test
	public void parseMediaType() throws Exception {
		String s = "audio/*; q=0.2";
		MediaType mediaType = MediaType.parseMediaType(s);
		assertEquals("Invalid type", "audio", mediaType.getType());
		assertEquals("Invalid subtype", "*", mediaType.getSubtype());
		assertEquals("Invalid quality factor", 0.2D, mediaType.getQualityValue(), 0D);
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeNoSubtype() {
		MediaType.parseMediaType("audio");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeNoSubtypeSlash() {
		MediaType.parseMediaType("audio/");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeIllegalType() {
		MediaType.parseMediaType("audio(/basic");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeIllegalSubtype() {
		MediaType.parseMediaType("audio/basic)");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeEmptyParameterAttribute() {
		MediaType.parseMediaType("audio/*;=value");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeEmptyParameterValue() {
		MediaType.parseMediaType("audio/*;attr=");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeIllegalParameterAttribute() {
		MediaType.parseMediaType("audio/*;attr<=value");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeIllegalParameterValue() {
		MediaType.parseMediaType("audio/*;attr=v>alue");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeIllegalQualityFactor() {
		MediaType.parseMediaType("audio/basic;q=1.1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeIllegalCharset() {
		MediaType.parseMediaType("text/html; charset=foo-bar");
	}

	@Test
	public void parseMediaTypeQuotedParameterValue() {
		MediaType.parseMediaType("audio/*;attr=\"v>alue\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseMediaTypeIllegalQuotedParameterValue() {
		MediaType.parseMediaType("audio/*;attr=\"");
	}

	@Test
	public void parseCharset() throws Exception {
		String s = "text/html; charset=iso-8859-1";
		MediaType mediaType = MediaType.parseMediaType(s);
		assertEquals("Invalid type", "text", mediaType.getType());
		assertEquals("Invalid subtype", "html", mediaType.getSubtype());
		assertEquals("Invalid charset", Charset.forName("ISO-8859-1"), mediaType.getCharSet());
	}

	@Test
	public void parseURLConnectionMediaType() throws Exception {
		String s = "*; q=.2";
		MediaType mediaType = MediaType.parseMediaType(s);
		assertEquals("Invalid type", "*", mediaType.getType());
		assertEquals("Invalid subtype", "*", mediaType.getSubtype());
		assertEquals("Invalid quality factor", 0.2D, mediaType.getQualityValue(), 0D);
	}

	@Test
	public void parseMediaTypes() throws Exception {
		String s = "text/plain; q=0.5, text/html, text/x-dvi; q=0.8, text/x-c";
		List<MediaType> mediaTypes = MediaType.parseMediaTypes(s);
		assertNotNull("No media types returned", mediaTypes);
		assertEquals("Invalid amount of media types", 4, mediaTypes.size());

		mediaTypes = MediaType.parseMediaTypes(null);
		assertNotNull("No media types returned", mediaTypes);
		assertEquals("Invalid amount of media types", 0, mediaTypes.size());
	}

	@Test
	public void compareTo() {
		MediaType audioBasic = new MediaType("audio", "basic");
		MediaType audio = new MediaType("audio");
		MediaType audioWave = new MediaType("audio", "wave");
		MediaType audioBasicLevel = new MediaType("audio", "basic", Collections.singletonMap("level", "1"));
		MediaType audioBasic07 = new MediaType("audio", "basic", 0.7);

		// equal
		assertEquals("Invalid comparison result", 0, audioBasic.compareTo(audioBasic));
		assertEquals("Invalid comparison result", 0, audio.compareTo(audio));
		assertEquals("Invalid comparison result", 0, audioBasicLevel.compareTo(audioBasicLevel));

		assertTrue("Invalid comparison result", audioBasicLevel.compareTo(audio) > 0);

		List<MediaType> expected = new ArrayList<MediaType>();
		expected.add(audio);
		expected.add(audioBasic);
		expected.add(audioBasicLevel);
		expected.add(audioBasic07);
		expected.add(audioWave);

		List<MediaType> result = new ArrayList<MediaType>(expected);
		Random rnd = new Random();
		// shuffle & sort 10 times
		for (int i = 0; i < 10; i++) {
			Collections.shuffle(result, rnd);
			Collections.sort(result);

			for (int j = 0; j < result.size(); j++) {
				assertSame("Invalid media type at " + j + ", run " + i, expected.get(j), result.get(j));
			}
		}
	}

	@Test
	public void compareToConsistentWithEquals() {
		MediaType m1 = MediaType.parseMediaType("text/html; q=0.7; charset=iso-8859-1");
		MediaType m2 = MediaType.parseMediaType("text/html; charset=iso-8859-1; q=0.7");

		assertEquals("Media types not equal", m1, m2);
		assertEquals("compareTo() not consistent with equals", 0, m1.compareTo(m2));
		assertEquals("compareTo() not consistent with equals", 0, m2.compareTo(m1));

		m1 = MediaType.parseMediaType("text/html; q=0.7; charset=iso-8859-1");
		m2 = MediaType.parseMediaType("text/html; Q=0.7; charset=iso-8859-1");
		assertEquals("Media types not equal", m1, m2);
		assertEquals("compareTo() not consistent with equals", 0, m1.compareTo(m2));
		assertEquals("compareTo() not consistent with equals", 0, m2.compareTo(m1));
	}

	@Test
	public void compareToCaseSensitivity() {
		MediaType m1 = new MediaType("audio", "basic");
		MediaType m2 = new MediaType("Audio", "Basic");
		assertEquals("Invalid comparison result", 0, m1.compareTo(m2));
		assertEquals("Invalid comparison result", 0, m2.compareTo(m1));

		m1 = new MediaType("audio", "basic", Collections.singletonMap("foo", "bar"));
		m2 = new MediaType("audio", "basic", Collections.singletonMap("Foo", "bar"));
		assertEquals("Invalid comparison result", 0, m1.compareTo(m2));
		assertEquals("Invalid comparison result", 0, m2.compareTo(m1));

		m1 = new MediaType("audio", "basic", Collections.singletonMap("foo", "bar"));
		m2 = new MediaType("audio", "basic", Collections.singletonMap("foo", "Bar"));
		assertTrue("Invalid comparison result", m1.compareTo(m2) != 0);
		assertTrue("Invalid comparison result", m2.compareTo(m1) != 0);


	}

	@Test
	public void specificityComparator() throws Exception {
		MediaType audioBasic = new MediaType("audio", "basic");
		MediaType audioWave = new MediaType("audio", "wave");
		MediaType audio = new MediaType("audio");
		MediaType audio03 = new MediaType("audio", "*", 0.3);
		MediaType audio07 = new MediaType("audio", "*", 0.7);
		MediaType audioBasicLevel = new MediaType("audio", "basic", Collections.singletonMap("level", "1"));
		MediaType textHtml = new MediaType("text", "html");
		MediaType all = MediaType.ALL;

		Comparator<MediaType> comp = MediaType.SPECIFICITY_COMPARATOR;

		// equal
		assertEquals("Invalid comparison result", 0, comp.compare(audioBasic,audioBasic));
		assertEquals("Invalid comparison result", 0, comp.compare(audio, audio));
		assertEquals("Invalid comparison result", 0, comp.compare(audio07, audio07));
		assertEquals("Invalid comparison result", 0, comp.compare(audio03, audio03));
		assertEquals("Invalid comparison result", 0, comp.compare(audioBasicLevel, audioBasicLevel));

		// specific to unspecific
		assertTrue("Invalid comparison result", comp.compare(audioBasic, audio) < 0);
		assertTrue("Invalid comparison result", comp.compare(audioBasic, all) < 0);
		assertTrue("Invalid comparison result", comp.compare(audio, all) < 0);

		// unspecific to specific
		assertTrue("Invalid comparison result", comp.compare(audio, audioBasic) > 0);
		assertTrue("Invalid comparison result", comp.compare(all, audioBasic) > 0);
		assertTrue("Invalid comparison result", comp.compare(all, audio) > 0);

		// qualifiers
		assertTrue("Invalid comparison result", comp.compare(audio, audio07) < 0);
		assertTrue("Invalid comparison result", comp.compare(audio07, audio) > 0);
		assertTrue("Invalid comparison result", comp.compare(audio07, audio03) < 0);
		assertTrue("Invalid comparison result", comp.compare(audio03, audio07) > 0);
		assertTrue("Invalid comparison result", comp.compare(audio03, all) < 0);
		assertTrue("Invalid comparison result", comp.compare(all, audio03) > 0);

		// other parameters
		assertTrue("Invalid comparison result", comp.compare(audioBasic, audioBasicLevel) > 0);
		assertTrue("Invalid comparison result", comp.compare(audioBasicLevel, audioBasic) < 0);

		// different types
		assertEquals("Invalid comparison result", 0, comp.compare(audioBasic, textHtml));
		assertEquals("Invalid comparison result", 0, comp.compare(textHtml, audioBasic));

		// different subtypes
		assertEquals("Invalid comparison result", 0, comp.compare(audioBasic, audioWave));
		assertEquals("Invalid comparison result", 0, comp.compare(audioWave, audioBasic));
	}

	@Test
	public void sortBySpecificityRelated() {
		MediaType audioBasic = new MediaType("audio", "basic");
		MediaType audio = new MediaType("audio");
		MediaType audio03 = new MediaType("audio", "*", 0.3);
		MediaType audio07 = new MediaType("audio", "*", 0.7);
		MediaType audioBasicLevel = new MediaType("audio", "basic", Collections.singletonMap("level", "1"));
		MediaType all = MediaType.ALL;

		List<MediaType> expected = new ArrayList<MediaType>();
		expected.add(audioBasicLevel);
		expected.add(audioBasic);
		expected.add(audio);
		expected.add(audio07);
		expected.add(audio03);
		expected.add(all);

		List<MediaType> result = new ArrayList<MediaType>(expected);
		Random rnd = new Random();
		// shuffle & sort 10 times
		for (int i = 0; i < 10; i++) {
			Collections.shuffle(result, rnd);
			MediaType.sortBySpecificity(result);

			for (int j = 0; j < result.size(); j++) {
				assertSame("Invalid media type at " + j, expected.get(j), result.get(j));
			}
		}
	}

	@Test
	public void sortBySpecificityUnrelated() {
		MediaType audioBasic = new MediaType("audio", "basic");
		MediaType audioWave = new MediaType("audio", "wave");
		MediaType textHtml = new MediaType("text", "html");

		List<MediaType> expected = new ArrayList<MediaType>();
		expected.add(textHtml);
		expected.add(audioBasic);
		expected.add(audioWave);

		List<MediaType> result = new ArrayList<MediaType>(expected);
		MediaType.sortBySpecificity(result);

		for (int i = 0; i < result.size(); i++) {
			assertSame("Invalid media type at " + i, expected.get(i), result.get(i));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14451.java