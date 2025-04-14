error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/621.java
text:
```scala
.@@alwaysExpect(content().contentType(MediaType.APPLICATION_XML))

/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.test.web.mock.servlet.samples.standalone.resultmatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.mock.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.mock.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.mock.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.mock.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.mock.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.mock.Person;
import org.springframework.test.web.mock.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Examples of expectations on XML response content with XPath expressions.
 *
 * @author Rossen Stoyanchev
 *
 * @see ContentAssertionTests
 * @see XmlContentAssertionTests
 */
public class XpathAssertionTests {

	private static final Map<String, String> NS =
		Collections.singletonMap("ns", "http://example.org/music/people");

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = standaloneSetup(new MusicController())
				.defaultRequest(get("/").accept(MediaType.APPLICATION_XML))
				.alwaysExpect(status().isOk())
				.alwaysExpect(content().mimeType(MediaType.APPLICATION_XML))
				.build();
	}

	@Test
	public void testExists() throws Exception {

		String composer = "/ns:people/composers/composer[%s]";
		String performer = "/ns:people/performers/performer[%s]";

		this.mockMvc.perform(get("/music/people"))
			.andExpect(xpath(composer, NS, 1).exists())
			.andExpect(xpath(composer, NS, 2).exists())
			.andExpect(xpath(composer, NS, 3).exists())
			.andExpect(xpath(composer, NS, 4).exists())
			.andExpect(xpath(performer, NS, 1).exists())
			.andExpect(xpath(performer, NS, 2).exists())
			.andExpect(xpath(composer, NS, 1).node(notNullValue()));
	}

	@Test
	public void testDoesNotExist() throws Exception {

		String composer = "/ns:people/composers/composer[%s]";
		String performer = "/ns:people/performers/performer[%s]";

		this.mockMvc.perform(get("/music/people"))
			.andExpect(xpath(composer, NS, 0).doesNotExist())
			.andExpect(xpath(composer, NS, 5).doesNotExist())
			.andExpect(xpath(performer, NS, 0).doesNotExist())
			.andExpect(xpath(performer, NS, 3).doesNotExist())
			.andExpect(xpath(composer, NS, 0).node(nullValue()));
	}

	@Test
	public void testString() throws Exception {

		String composerName = "/ns:people/composers/composer[%s]/name";
		String performerName = "/ns:people/performers/performer[%s]/name";

		this.mockMvc.perform(get("/music/people"))
			.andExpect(xpath(composerName, NS, 1).string("Johann Sebastian Bach"))
			.andExpect(xpath(composerName, NS, 2).string("Johannes Brahms"))
			.andExpect(xpath(composerName, NS, 3).string("Edvard Grieg"))
			.andExpect(xpath(composerName, NS, 4).string("Robert Schumann"))
			.andExpect(xpath(performerName, NS, 1).string("Vladimir Ashkenazy"))
			.andExpect(xpath(performerName, NS, 2).string("Yehudi Menuhin"))
			.andExpect(xpath(composerName, NS, 1).string(equalTo("Johann Sebastian Bach"))) // Hamcrest..
			.andExpect(xpath(composerName, NS, 1).string(startsWith("Johann")))
			.andExpect(xpath(composerName, NS, 1).string(notNullValue()));
	}

	@Test
	public void testNumber() throws Exception {

		String composerDouble = "/ns:people/composers/composer[%s]/someDouble";

		this.mockMvc.perform(get("/music/people"))
			.andExpect(xpath(composerDouble, NS, 1).number(21d))
			.andExpect(xpath(composerDouble, NS, 2).number(.0025))
			.andExpect(xpath(composerDouble, NS, 3).number(1.6035))
			.andExpect(xpath(composerDouble, NS, 4).number(Double.NaN))
			.andExpect(xpath(composerDouble, NS, 1).number(equalTo(21d)))  // Hamcrest..
			.andExpect(xpath(composerDouble, NS, 3).number(closeTo(1.6, .01)));
	}

	@Test
	public void testBoolean() throws Exception {

		String performerBooleanValue = "/ns:people/performers/performer[%s]/someBoolean";

		this.mockMvc.perform(get("/music/people"))
			.andExpect(xpath(performerBooleanValue, NS, 1).booleanValue(false))
			.andExpect(xpath(performerBooleanValue, NS, 2).booleanValue(true));
	}

	@Test
	public void testNodeCount() throws Exception {

		this.mockMvc.perform(get("/music/people"))
			.andExpect(xpath("/ns:people/composers/composer", NS).nodeCount(4))
			.andExpect(xpath("/ns:people/performers/performer", NS).nodeCount(2))
			.andExpect(xpath("/ns:people/composers/composer", NS).nodeCount(lessThan(5))) // Hamcrest..
			.andExpect(xpath("/ns:people/performers/performer", NS).nodeCount(greaterThan(0)));
	}

	@Controller
	private static class MusicController {

		@RequestMapping(value="/music/people")
		public @ResponseBody PeopleWrapper getPeople() {

			List<Person> composers = Arrays.asList(
					new Person("Johann Sebastian Bach").setSomeDouble(21),
					new Person("Johannes Brahms").setSomeDouble(.0025),
					new Person("Edvard Grieg").setSomeDouble(1.6035),
					new Person("Robert Schumann").setSomeDouble(Double.NaN));

			List<Person> performers = Arrays.asList(
					new Person("Vladimir Ashkenazy").setSomeBoolean(false),
					new Person("Yehudi Menuhin").setSomeBoolean(true));

			return new PeopleWrapper(composers, performers);
		}
	}

	@SuppressWarnings("unused")
	@XmlRootElement(name="people", namespace="http://example.org/music/people")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class PeopleWrapper {

		@XmlElementWrapper(name="composers")
		@XmlElement(name="composer")
		private List<Person> composers;

		@XmlElementWrapper(name="performers")
		@XmlElement(name="performer")
		private List<Person> performers;

		public PeopleWrapper() {
		}

		public PeopleWrapper(List<Person> composers, List<Person> performers) {
			this.composers = composers;
			this.performers = performers;
		}

		public List<Person> getComposers() {
			return this.composers;
		}

		public List<Person> getPerformers() {
			return this.performers;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/621.java