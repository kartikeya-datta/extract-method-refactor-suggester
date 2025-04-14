error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5501.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5501.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5501.java
text:
```scala
m@@appingDef.getPropertyValues().add("useDefaultSuffixPattern", false);

/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.web.servlet.mvc.annotation;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/** @author Arjen Poutsma */
public class UriTemplateServletAnnotationControllerTests {

	private DispatcherServlet servlet;

	@Test
	public void simple() throws Exception {
		initServlet(SimpleUriTemplateController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/42");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test-42", response.getContentAsString());
	}

	@Test
	public void multiple() throws Exception {
		initServlet(MultipleUriTemplateController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42/bookings/21-other");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test-42-21-other", response.getContentAsString());
	}

	@Test
	public void binding() throws Exception {
		initServlet(BindingUriTemplateController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42/dates/2008-11-18");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals(200, response.getStatus());

		request = new MockHttpServletRequest("GET", "/hotels/42/dates/2008-foo-bar");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals(400, response.getStatus());

		initServlet(NonBindingUriTemplateController.class);
		request = new MockHttpServletRequest("GET", "/hotels/42/dates/2008-foo-bar");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals(500, response.getStatus());
	}

	@Test
	public void ambiguous() throws Exception {
		initServlet(AmbiguousUriTemplateController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/new");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("specific", response.getContentAsString());
	}

	@Test
	public void relative() throws Exception {
		initServlet(RelativePathUriTemplateController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42/bookings/21");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test-42-21", response.getContentAsString());

		request = new MockHttpServletRequest("GET", "/hotels/42/bookings/21.html");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test-42-21", response.getContentAsString());
	}

	@Test
	public void extension() throws Exception {
		initServlet(SimpleUriTemplateController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/42.xml");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test-42", response.getContentAsString());

	}

	@Test
	public void typeConversionError() throws Exception {
		initServlet(SimpleUriTemplateController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo.xml");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("Invalid response status code", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}

	@Test
	public void explicitSubPath() throws Exception {
		initServlet(ExplicitSubPathController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test-42", response.getContentAsString());
	}

	@Test
	public void implicitSubPath() throws Exception {
		initServlet(ImplicitSubPathController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test-42", response.getContentAsString());
	}

	@Test
	public void crud() throws Exception {
		initServlet(CrudController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("list", response.getContentAsString());

		request = new MockHttpServletRequest("GET", "/hotels/");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("list", response.getContentAsString());

		request = new MockHttpServletRequest("POST", "/hotels");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("create", response.getContentAsString());

		request = new MockHttpServletRequest("GET", "/hotels/42");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("show-42", response.getContentAsString());

		request = new MockHttpServletRequest("GET", "/hotels/42/");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("show-42", response.getContentAsString());

		request = new MockHttpServletRequest("PUT", "/hotels/42");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("createOrUpdate-42", response.getContentAsString());

		request = new MockHttpServletRequest("DELETE", "/hotels/42");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("remove-42", response.getContentAsString());
	}

	@Test
	public void methodNotSupported() throws Exception {
		initServlet(MethodNotAllowedController.class);

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/1");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals(200, response.getStatus());

		request = new MockHttpServletRequest("POST", "/hotels/1");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals(405, response.getStatus());

		request = new MockHttpServletRequest("GET", "/hotels");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals(200, response.getStatus());

		request = new MockHttpServletRequest("POST", "/hotels");
		response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals(405, response.getStatus());


	}

	private void initServlet(final Class<?> controllerclass) throws ServletException {
		servlet = new DispatcherServlet() {
			@Override
			protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent)
					throws BeansException {
				GenericWebApplicationContext wac = new GenericWebApplicationContext();
				wac.registerBeanDefinition("controller", new RootBeanDefinition(controllerclass));
				wac.refresh();
				return wac;
			}
		};
		servlet.init(new MockServletConfig());
	}

	@Test
	public void noDefaultSuffixPattern() throws Exception {
		servlet = new DispatcherServlet() {
			@Override
			protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent)
					throws BeansException {
				GenericWebApplicationContext wac = new GenericWebApplicationContext();
				wac.registerBeanDefinition("controller", new RootBeanDefinition(ImplicitSubPathController.class));
				RootBeanDefinition mappingDef = new RootBeanDefinition(DefaultAnnotationHandlerMapping.class);
				mappingDef.getPropertyValues().addPropertyValue("useDefaultSuffixPattern", false);
				wac.registerBeanDefinition("handlerMapping", mappingDef);
				wac.refresh();
				return wac;
			}
		};
		servlet.init(new MockServletConfig());

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/hotel.with.dot");
		MockHttpServletResponse response = new MockHttpServletResponse();
		servlet.service(request, response);
		assertEquals("test-hotel.with.dot", response.getContentAsString());
	}

	/*
	 * Controllers
	 */

	@Controller
	public static class SimpleUriTemplateController {

		@RequestMapping("/{root}")
		public void handle(@PathVariable("root") int root, Writer writer) throws IOException {
			assertEquals("Invalid path variable value", 42, root);
			writer.write("test-" + root);
		}

	}

	@Controller
	public static class MultipleUriTemplateController {

		@RequestMapping("/hotels/{hotel}/bookings/{booking}-{other}")
		public void handle(@PathVariable("hotel") String hotel,
				@PathVariable int booking,
				@PathVariable String other,
				Writer writer) throws IOException {
			assertEquals("Invalid path variable value", "42", hotel);
			assertEquals("Invalid path variable value", 21, booking);
			writer.write("test-" + hotel + "-" + booking + "-" + other);
		}

	}

	@Controller
	public static class BindingUriTemplateController {

		@InitBinder
		public void initBinder(WebDataBinder binder, @PathVariable("hotel") String hotel) {
			assertEquals("Invalid path variable value", "42", hotel);
			binder.initBeanPropertyAccess();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
		}

		@RequestMapping("/hotels/{hotel}/dates/{date}")
		public void handle(@PathVariable("hotel") String hotel, @PathVariable Date date, Writer writer)
				throws IOException {
			assertEquals("Invalid path variable value", "42", hotel);
			assertEquals("Invalid path variable value", new Date(108, 10, 18), date);
			writer.write("test-" + hotel);
		}

	}

	@Controller
	public static class NonBindingUriTemplateController {

		@RequestMapping("/hotels/{hotel}/dates/{date}")
		public void handle(@PathVariable("hotel") String hotel, @PathVariable Date date, Writer writer)
				throws IOException {
		}

	}

	@Controller
	@RequestMapping("/hotels/{hotel}")
	public static class RelativePathUriTemplateController {

		@RequestMapping("bookings/{booking}")
		public void handle(@PathVariable("hotel") String hotel, @PathVariable int booking, Writer writer)
				throws IOException {
			assertEquals("Invalid path variable value", "42", hotel);
			assertEquals("Invalid path variable value", 21, booking);
			writer.write("test-" + hotel + "-" + booking);
		}

	}

	@Controller
	@RequestMapping("/hotels")
	public static class AmbiguousUriTemplateController {

		@RequestMapping("/{hotel}")
		public void handleVars(@PathVariable("hotel") String hotel, Writer writer) throws IOException {
			assertEquals("Invalid path variable value", "42", hotel);
			writer.write("variables");
		}

		@RequestMapping("/new")
		public void handleSpecific(Writer writer) throws IOException {
			writer.write("specific");
		}

		@RequestMapping("/*")
		public void handleWildCard(Writer writer) throws IOException {
			writer.write("wildcard");
		}

	}

	@Controller
	@RequestMapping("/hotels/*")
	public static class ExplicitSubPathController {

		@RequestMapping("{hotel}")
		public void handleHotel(@PathVariable String hotel, Writer writer) throws IOException {
			writer.write("test-" + hotel);
		}

	}

	@Controller
	@RequestMapping("hotels")
	public static class ImplicitSubPathController {

		@RequestMapping("{hotel}")
		public void handleHotel(@PathVariable String hotel, Writer writer) throws IOException {
			writer.write("test-" + hotel);
		}
	}

	@Controller
	@RequestMapping("hotels")
	public static class CrudController {

		@RequestMapping(method = RequestMethod.GET)
		public void list(Writer writer) throws IOException {
			writer.write("list");
		}

		@RequestMapping(method = RequestMethod.POST)
		public void create(Writer writer) throws IOException {
			writer.write("create");
		}

		@RequestMapping(value = "/{hotel}", method = RequestMethod.GET)
		public void show(@PathVariable String hotel, Writer writer) throws IOException {
			writer.write("show-" + hotel);
		}

		@RequestMapping(value = "{hotel}", method = RequestMethod.PUT)
		public void createOrUpdate(@PathVariable String hotel, Writer writer) throws IOException {
			writer.write("createOrUpdate-" + hotel);
		}

		@RequestMapping(value = "{hotel}", method = RequestMethod.DELETE)
		public void remove(@PathVariable String hotel, Writer writer) throws IOException {
			writer.write("remove-" + hotel);
		}

	}

	@Controller
	@RequestMapping("/hotels")
	public static class MethodNotAllowedController {

		@RequestMapping(method = RequestMethod.GET)
		public void list(Writer writer) {
		}

		@RequestMapping(method = RequestMethod.GET, value = "{hotelId}")
		public void show(@PathVariable long hotelId, Writer writer) {
		}

		@RequestMapping(method = RequestMethod.PUT, value = "{hotelId}")
		public void createOrUpdate(@PathVariable long hotelId, Writer writer) {
		}

		@RequestMapping(method = RequestMethod.DELETE, value = "/{hotelId}")
		public void remove(@PathVariable long hotelId, Writer writer) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5501.java