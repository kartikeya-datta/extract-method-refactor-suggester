error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13562.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13562.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13562.java
text:
```scala
a@@ssertFalse(objectMapper.getDeserializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));

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

package org.springframework.http.converter.json;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BasicDeserializerFactory;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BasicSerializerFactory;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.ClassSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.FatalBeanException;

import static org.junit.Assert.*;

/**
 * Test cases for {@link Jackson2ObjectMapperFactoryBean} class.
 *
 * @author <a href="mailto:dmitry.katsubo@gmail.com">Dmitry Katsubo</a>
 * @author Brian Clozel
 */
public class Jackson2ObjectMapperFactoryBeanTests {

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private Jackson2ObjectMapperFactoryBean factory;

	@Before
	public void setUp() {
		factory = new Jackson2ObjectMapperFactoryBean();
	}

	@Test
	public void settersWithNullValues() {
		// Should not crash:
		factory.setSerializers((JsonSerializer<?>[]) null);
		factory.setSerializersByType(null);
		factory.setDeserializersByType(null);
		factory.setFeaturesToEnable((Object[]) null);
		factory.setFeaturesToDisable((Object[]) null);
	}

	@Test(expected = FatalBeanException.class)
	public void unknownFeature() {
		this.factory.setFeaturesToEnable(Boolean.TRUE);
		this.factory.afterPropertiesSet();
	}

	@Test
	public void booleanSetters() {
		this.factory.setAutoDetectFields(false);
		this.factory.setAutoDetectGettersSetters(false);
		this.factory.setDefaultViewInclusion(false);
		this.factory.setFailOnEmptyBeans(false);
		this.factory.setIndentOutput(true);
		this.factory.afterPropertiesSet();

		ObjectMapper objectMapper = this.factory.getObject();

		assertFalse(objectMapper.getSerializationConfig().isEnabled(MapperFeature.AUTO_DETECT_FIELDS));
		assertFalse(objectMapper.getDeserializationConfig().isEnabled(MapperFeature.AUTO_DETECT_FIELDS));
		assertFalse(objectMapper.getSerializationConfig().isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
		assertFalse(objectMapper.getDeserializationConfig().isEnabled(MapperFeature.AUTO_DETECT_SETTERS));
		assertFalse(objectMapper.getDeserializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
		assertFalse(objectMapper.getSerializationConfig().isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
		assertTrue(objectMapper.getSerializationConfig().isEnabled(SerializationFeature.INDENT_OUTPUT));
		assertTrue(objectMapper.getSerializationConfig().getSerializationInclusion() == JsonInclude.Include.ALWAYS);
	}

	@Test
	public void setNotNullSerializationInclusion() {
		factory.afterPropertiesSet();
		assertTrue(factory.getObject().getSerializationConfig().getSerializationInclusion() == JsonInclude.Include.ALWAYS);

		factory.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		factory.afterPropertiesSet();
		assertTrue(factory.getObject().getSerializationConfig().getSerializationInclusion() == JsonInclude.Include.NON_NULL);
	}

	@Test
	public void setNotDefaultSerializationInclusion() {
		factory.afterPropertiesSet();
		assertTrue(factory.getObject().getSerializationConfig().getSerializationInclusion() == JsonInclude.Include.ALWAYS);

		factory.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		factory.afterPropertiesSet();
		assertTrue(factory.getObject().getSerializationConfig().getSerializationInclusion() == JsonInclude.Include.NON_DEFAULT);
	}

	@Test
	public void setNotEmptySerializationInclusion() {
		factory.afterPropertiesSet();
		assertTrue(factory.getObject().getSerializationConfig().getSerializationInclusion() == JsonInclude.Include.ALWAYS);

		factory.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		factory.afterPropertiesSet();
		assertTrue(factory.getObject().getSerializationConfig().getSerializationInclusion() == JsonInclude.Include.NON_EMPTY);
	}

	@Test
	public void dateTimeFormatSetter() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

		this.factory.setDateFormat(dateFormat);
		this.factory.afterPropertiesSet();

		assertEquals(dateFormat, this.factory.getObject().getSerializationConfig().getDateFormat());
		assertEquals(dateFormat, this.factory.getObject().getDeserializationConfig().getDateFormat());
	}

	@Test
	public void simpleDateFormatStringSetter() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

		this.factory.setSimpleDateFormat(DATE_FORMAT);
		this.factory.afterPropertiesSet();

		assertEquals(dateFormat, this.factory.getObject().getSerializationConfig().getDateFormat());
		assertEquals(dateFormat, this.factory.getObject().getDeserializationConfig().getDateFormat());
	}

	@Test
	public void setModules() {
		NumberSerializer serializer1 = new NumberSerializer();
		SimpleModule module = new SimpleModule();
		module.addSerializer(Integer.class, serializer1);

		this.factory.setModules(Arrays.asList(new Module[] {module}));
		this.factory.afterPropertiesSet();
		ObjectMapper objectMapper = this.factory.getObject();

		Serializers serializers = getSerializerFactoryConfig(objectMapper).serializers().iterator().next();
		assertTrue(serializers.findSerializer(null, SimpleType.construct(Integer.class), null) == serializer1);
	}

	@Test
	public void simpleSetup() {
		this.factory.afterPropertiesSet();

		assertNotNull(this.factory.getObject());
		assertTrue(this.factory.isSingleton());
		assertEquals(ObjectMapper.class, this.factory.getObjectType());
	}

	@Test
	public void undefinedObjectType() {
		assertEquals(null, this.factory.getObjectType());
	}

	private static SerializerFactoryConfig getSerializerFactoryConfig(ObjectMapper objectMapper) {
		return ((BasicSerializerFactory) objectMapper.getSerializerFactory()).getFactoryConfig();
	}

	private static DeserializerFactoryConfig getDeserializerFactoryConfig(ObjectMapper objectMapper) {
		return ((BasicDeserializerFactory) objectMapper.getDeserializationContext().getFactory()).getFactoryConfig();
	}

	@Test
	public void propertyNamingStrategy() {
		PropertyNamingStrategy strategy = new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy();
		this.factory.setPropertyNamingStrategy(strategy);
		this.factory.afterPropertiesSet();

		assertSame(strategy, this.factory.getObject().getSerializationConfig().getPropertyNamingStrategy());
		assertSame(strategy, this.factory.getObject().getDeserializationConfig().getPropertyNamingStrategy());
	}

	@Test
	public void completeSetup() {
		NopAnnotationIntrospector annotationIntrospector = NopAnnotationIntrospector.instance;
		ObjectMapper objectMapper = new ObjectMapper();

		factory.setObjectMapper(objectMapper);
		assertTrue(this.factory.isSingleton());
		assertEquals(ObjectMapper.class, this.factory.getObjectType());

		Map<Class<?>, JsonDeserializer<?>> deserializers = new HashMap<Class<?>, JsonDeserializer<?>>();
		deserializers.put(Date.class, new DateDeserializer());

		JsonSerializer<Class<?>> serializer1 = new ClassSerializer();
		JsonSerializer<Number> serializer2 = new NumberSerializer();

		factory.setSerializers(serializer1);
		factory.setSerializersByType(Collections.<Class<?>, JsonSerializer<?>> singletonMap(Boolean.class, serializer2));
		factory.setDeserializersByType(deserializers);
		factory.setAnnotationIntrospector(annotationIntrospector);

		this.factory.setFeaturesToEnable(SerializationFeature.FAIL_ON_EMPTY_BEANS,
				DeserializationFeature.UNWRAP_ROOT_VALUE,
				JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,
				JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);

		this.factory.setFeaturesToDisable(MapperFeature.AUTO_DETECT_GETTERS,
				MapperFeature.AUTO_DETECT_FIELDS,
				JsonParser.Feature.AUTO_CLOSE_SOURCE,
				JsonGenerator.Feature.QUOTE_FIELD_NAMES);

		assertFalse(getSerializerFactoryConfig(objectMapper).hasSerializers());
		assertFalse(getDeserializerFactoryConfig(objectMapper).hasDeserializers());

		this.factory.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		this.factory.afterPropertiesSet();

		assertTrue(objectMapper == this.factory.getObject());
		assertTrue(getSerializerFactoryConfig(objectMapper).hasSerializers());
		assertTrue(getDeserializerFactoryConfig(objectMapper).hasDeserializers());

		Serializers serializers = getSerializerFactoryConfig(objectMapper).serializers().iterator().next();
		assertTrue(serializers.findSerializer(null, SimpleType.construct(Class.class), null) == serializer1);
		assertTrue(serializers.findSerializer(null, SimpleType.construct(Boolean.class), null) == serializer2);
		assertNull(serializers.findSerializer(null, SimpleType.construct(Number.class), null));

		assertTrue(annotationIntrospector == objectMapper.getSerializationConfig().getAnnotationIntrospector());
		assertTrue(annotationIntrospector == objectMapper.getDeserializationConfig().getAnnotationIntrospector());

		assertTrue(objectMapper.getSerializationConfig().isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
		assertTrue(objectMapper.getDeserializationConfig().isEnabled(DeserializationFeature.UNWRAP_ROOT_VALUE));
		assertTrue(objectMapper.getFactory().isEnabled(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER));
		assertTrue(objectMapper.getFactory().isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));

		assertFalse(objectMapper.getSerializationConfig().isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
		assertTrue(objectMapper.getDeserializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
		assertFalse(objectMapper.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
		assertFalse(objectMapper.getDeserializationConfig().isEnabled(MapperFeature.AUTO_DETECT_FIELDS));
		assertFalse(objectMapper.getFactory().isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE));
		assertFalse(objectMapper.getFactory().isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
		assertTrue(objectMapper.getSerializationConfig().getSerializationInclusion() == JsonInclude.Include.NON_NULL);
	}

	@Test
	public void xmlMapper() {
		this.factory.setObjectMapper(new XmlMapper());
		this.factory.afterPropertiesSet();

		assertNotNull(this.factory.getObject());
		assertTrue(this.factory.isSingleton());
		assertEquals(XmlMapper.class, this.factory.getObjectType());
	}

	@Test
	public void createXmlMapper() {
		this.factory.setCreateXmlMapper(true);
		this.factory.afterPropertiesSet();

		assertNotNull(this.factory.getObject());
		assertTrue(this.factory.isSingleton());
		assertEquals(XmlMapper.class, this.factory.getObjectType());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13562.java