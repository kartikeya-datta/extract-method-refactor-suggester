error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 664
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12369.java
text:
```scala
public class QualifierAnnotationTests {

/*
 * Copyright 2002-2013 the original author or authors.
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

p@@ackage org.springframework.beans.factory.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

import org.junit.Test;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

import static java.lang.String.format;
import static org.junit.Assert.*;
import static org.springframework.util.ClassUtils.*;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class QualifierAnnotationTests {

	private static final String CLASSNAME = QualifierAnnotationTests.class.getName();
	private static final String CONFIG_LOCATION =
		format("classpath:%s-context.xml", convertClassNameToResourcePath(CLASSNAME));


	@Test
	public void testNonQualifiedFieldFails() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", NonQualifiedTestBean.class);
		try {
			context.refresh();
			fail("Should have thrown a BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getMessage().contains("found 6"));
		}
	}

	@Test
	public void testQualifiedByValue() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByValueTestBean.class);
		context.refresh();
		QualifiedByValueTestBean testBean = (QualifiedByValueTestBean) context.getBean("testBean");
		Person person = testBean.getLarry();
		assertEquals("Larry", person.getName());
	}

	@Test
	public void testQualifiedByParentValue() {
		StaticApplicationContext parent = new StaticApplicationContext();
		GenericBeanDefinition parentLarry = new GenericBeanDefinition();
		parentLarry.setBeanClass(Person.class);
		parentLarry.getPropertyValues().add("name", "ParentLarry");
		parentLarry.addQualifier(new AutowireCandidateQualifier(Qualifier.class, "parentLarry"));
		parent.registerBeanDefinition("someLarry", parentLarry);
		GenericBeanDefinition otherLarry = new GenericBeanDefinition();
		otherLarry.setBeanClass(Person.class);
		otherLarry.getPropertyValues().add("name", "OtherLarry");
		otherLarry.addQualifier(new AutowireCandidateQualifier(Qualifier.class, "otherLarry"));
		parent.registerBeanDefinition("someOtherLarry", otherLarry);
		parent.refresh();

		StaticApplicationContext context = new StaticApplicationContext(parent);
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByParentValueTestBean.class);
		context.refresh();
		QualifiedByParentValueTestBean testBean = (QualifiedByParentValueTestBean) context.getBean("testBean");
		Person person = testBean.getLarry();
		assertEquals("ParentLarry", person.getName());
	}

	@Test
	public void testQualifiedByBeanName() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByBeanNameTestBean.class);
		context.refresh();
		QualifiedByBeanNameTestBean testBean = (QualifiedByBeanNameTestBean) context.getBean("testBean");
		Person person = testBean.getLarry();
		assertEquals("LarryBean", person.getName());
		assertTrue(testBean.myProps != null && testBean.myProps.isEmpty());
	}

	@Test
	public void testQualifiedByFieldName() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByFieldNameTestBean.class);
		context.refresh();
		QualifiedByFieldNameTestBean testBean = (QualifiedByFieldNameTestBean) context.getBean("testBean");
		Person person = testBean.getLarry();
		assertEquals("LarryBean", person.getName());
	}

	@Test
	public void testQualifiedByParameterName() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByParameterNameTestBean.class);
		context.refresh();
		QualifiedByParameterNameTestBean testBean = (QualifiedByParameterNameTestBean) context.getBean("testBean");
		Person person = testBean.getLarry();
		assertEquals("LarryBean", person.getName());
	}

	@Test
	public void testQualifiedByAlias() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByAliasTestBean.class);
		context.refresh();
		QualifiedByAliasTestBean testBean = (QualifiedByAliasTestBean) context.getBean("testBean");
		Person person = testBean.getStooge();
		assertEquals("LarryBean", person.getName());
	}

	@Test
	public void testQualifiedByAnnotation() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByAnnotationTestBean.class);
		context.refresh();
		QualifiedByAnnotationTestBean testBean = (QualifiedByAnnotationTestBean) context.getBean("testBean");
		Person person = testBean.getLarry();
		assertEquals("LarrySpecial", person.getName());
	}

	@Test
	public void testQualifiedByCustomValue() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByCustomValueTestBean.class);
		context.refresh();
		QualifiedByCustomValueTestBean testBean = (QualifiedByCustomValueTestBean) context.getBean("testBean");
		Person person = testBean.getCurly();
		assertEquals("Curly", person.getName());
	}

	@Test
	public void testQualifiedByAnnotationValue() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByAnnotationValueTestBean.class);
		context.refresh();
		QualifiedByAnnotationValueTestBean testBean = (QualifiedByAnnotationValueTestBean) context.getBean("testBean");
		Person person = testBean.getLarry();
		assertEquals("LarrySpecial", person.getName());
	}

	@Test
	public void testQualifiedByAttributesFailsWithoutCustomQualifierRegistered() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		context.registerSingleton("testBean", QualifiedByAttributesTestBean.class);
		try {
			context.refresh();
			fail("should have thrown a BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getMessage().contains("found 6"));
		}
	}

	@Test
	public void testQualifiedByAttributesWithCustomQualifierRegistered() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
		QualifierAnnotationAutowireCandidateResolver resolver = (QualifierAnnotationAutowireCandidateResolver)
				context.getDefaultListableBeanFactory().getAutowireCandidateResolver();
		resolver.addQualifierType(MultipleAttributeQualifier.class);
		context.registerSingleton("testBean", MultiQualifierClient.class);
		context.refresh();

		MultiQualifierClient testBean = (MultiQualifierClient) context.getBean("testBean");

		assertNotNull( testBean.factoryTheta);
		assertNotNull( testBean.implTheta);
	}

	@Test
	public void testInterfaceWithOneQualifiedFactoryAndOneQualifiedBean() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
		reader.loadBeanDefinitions(CONFIG_LOCATION);
	}


	@SuppressWarnings("unused")
	private static class NonQualifiedTestBean {

		@Autowired
		private Person anonymous;

		public Person getAnonymous() {
			return anonymous;
		}
	}


	private static class QualifiedByValueTestBean {

		@Autowired @Qualifier("larry")
		private Person larry;

		public Person getLarry() {
			return larry;
		}
	}


	private static class QualifiedByParentValueTestBean {

		@Autowired @Qualifier("parentLarry")
		private Person larry;

		public Person getLarry() {
			return larry;
		}
	}


	private static class QualifiedByBeanNameTestBean {

		@Autowired @Qualifier("larryBean")
		private Person larry;

		@Autowired @Qualifier("testProperties")
		public Properties myProps;

		public Person getLarry() {
			return larry;
		}
	}


	private static class QualifiedByFieldNameTestBean {

		@Autowired
		private Person larryBean;

		public Person getLarry() {
			return larryBean;
		}
	}


	private static class QualifiedByParameterNameTestBean {

		private Person larryBean;

		@Autowired
		public void setLarryBean(Person larryBean) {
			this.larryBean = larryBean;
		}

		public Person getLarry() {
			return larryBean;
		}
	}


	private static class QualifiedByAliasTestBean {

		@Autowired @Qualifier("stooge")
		private Person stooge;

		public Person getStooge() {
			return stooge;
		}
	}


	private static class QualifiedByAnnotationTestBean {

		@Autowired @Qualifier("special")
		private Person larry;

		public Person getLarry() {
			return larry;
		}
	}


	private static class QualifiedByCustomValueTestBean {

		@Autowired @SimpleValueQualifier("curly")
		private Person curly;

		public Person getCurly() {
			return curly;
		}
	}


	private static class QualifiedByAnnotationValueTestBean {

		@Autowired @SimpleValueQualifier("special")
		private Person larry;

		public Person getLarry() {
			return larry;
		}
	}


	@SuppressWarnings("unused")
	private static class QualifiedByAttributesTestBean {

		@Autowired @MultipleAttributeQualifier(name="moe", age=42)
		private Person moeSenior;

		@Autowired @MultipleAttributeQualifier(name="moe", age=15)
		private Person moeJunior;

		public Person getMoeSenior() {
			return moeSenior;
		}

		public Person getMoeJunior() {
			return moeJunior;
		}
	}


	@SuppressWarnings("unused")
	private static class Person {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}


	@Qualifier("special")
	@SimpleValueQualifier("special")
	private static class SpecialPerson extends Person {
	}


	@Target({ElementType.FIELD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public @interface SimpleValueQualifier {

		String value() default "";
	}


	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MultipleAttributeQualifier {

		String name();

		int age();
	}


	private static final String FACTORY_QUALIFIER = "FACTORY";

	private static final String IMPL_QUALIFIER = "IMPL";


	public static class MultiQualifierClient {

		@Autowired @Qualifier(FACTORY_QUALIFIER)
		public Theta factoryTheta;

		@Autowired @Qualifier(IMPL_QUALIFIER)
		public Theta implTheta;
	}


	public interface Theta {
	}


	@Qualifier(IMPL_QUALIFIER)
	public static class ThetaImpl implements Theta {
	}


	@Qualifier(FACTORY_QUALIFIER)
	public static class QualifiedFactoryBean implements FactoryBean<Theta> {

		@Override
		public Theta getObject() {
			return new Theta() {};
		}

		@Override
		public Class<Theta> getObjectType() {
			return Theta.class;
		}

		@Override
		public boolean isSingleton() {
			return true;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12369.java