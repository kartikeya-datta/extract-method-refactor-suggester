error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9903.java
text:
```scala
protected S@@ingleSimpleTypeConstructorBean(String testString, boolean secondBoolean) {

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

package org.springframework.beans.factory.xml;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.ITestBean;
import org.springframework.beans.IndexedTestBean;
import org.springframework.beans.TestBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.DummyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.MethodReplacer;

/**
 * Types used by {@link XmlBeanFactoryTests} and its attendant XML config files.
 * 
 * @author Chris Beams
 */
final class XmlBeanFactoryTestTypes { }

/**
 * Simple bean used to check constructor dependency checking.
 *
 * @author Juergen Hoeller
 * @since 09.11.2003
 */
@SuppressWarnings("serial")
class ConstructorDependenciesBean implements Serializable {

	private int age;

	private String name;

	private TestBean spouse1;

	private TestBean spouse2;

	private IndexedTestBean other;

	public ConstructorDependenciesBean(int age) {
		this.age = age;
	}

	public ConstructorDependenciesBean(String name) {
		this.name = name;
	}

	public ConstructorDependenciesBean(TestBean spouse1) {
		this.spouse1 = spouse1;
	}

	public ConstructorDependenciesBean(TestBean spouse1, TestBean spouse2) {
		this.spouse1 = spouse1;
		this.spouse2 = spouse2;
	}

	@ConstructorProperties({"spouse", "otherSpouse", "myAge"})
	public ConstructorDependenciesBean(TestBean spouse1, TestBean spouse2, int age) {
		this.spouse1 = spouse1;
		this.spouse2 = spouse2;
		this.age = age;
	}

	public ConstructorDependenciesBean(TestBean spouse1, TestBean spouse2, IndexedTestBean other) {
		this.spouse1 = spouse1;
		this.spouse2 = spouse2;
		this.other = other;
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}

	public TestBean getSpouse1() {
		return spouse1;
	}

	public TestBean getSpouse2() {
		return spouse2;
	}

	public IndexedTestBean getOther() {
		return other;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setName(String name) {
		this.name = name;
	}
}


class SimpleConstructorArgBean {

	private int age;

	private String name;

	public SimpleConstructorArgBean() {
	}

	public SimpleConstructorArgBean(int age) {
		this.age = age;
	}

	public SimpleConstructorArgBean(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}
}


/**
 * Bean testing the ability to use both lookup method overrides
 * and constructor injection.
 * There is also a property ("setterString") to be set via
 * Setter Injection.
 *
 * @author Rod Johnson
 */
abstract class ConstructorInjectedOverrides {
	
	private ITestBean tb;
	
	private String setterString;
	
	public ConstructorInjectedOverrides(ITestBean tb) {
		this.tb = tb;
	}
	
	public ITestBean getTestBean() {
		return this.tb;
	}

	
	protected abstract FactoryMethods createFactoryMethods();

	/**
	 * @return Returns the setterString.
	 */
	public String getSetterString() {
		return setterString;
	}
	/**
	 * @param setterString The setterString to set.
	 */
	public void setSetterString(String setterString) {
		this.setterString = setterString;
	}
}


/**
 * Simple bean used to check constructor dependency checking.
 *
 * @author Juergen Hoeller
 * @since 09.11.2003
 */
@SuppressWarnings("serial")
class DerivedConstructorDependenciesBean extends ConstructorDependenciesBean {

	boolean initialized;
	boolean destroyed;

	DerivedConstructorDependenciesBean(TestBean spouse1, TestBean spouse2, IndexedTestBean other) {
		super(spouse1, spouse2, other);
	}

	private DerivedConstructorDependenciesBean(TestBean spouse1, Object spouse2, IndexedTestBean other) {
		super(spouse1, null, other);
	}

	protected DerivedConstructorDependenciesBean(TestBean spouse1, TestBean spouse2, IndexedTestBean other, int age, int otherAge) {
		super(spouse1, spouse2, other);
	}

	public DerivedConstructorDependenciesBean(TestBean spouse1, TestBean spouse2, IndexedTestBean other, int age, String name) {
		super(spouse1, spouse2, other);
		setAge(age);
		setName(name);
	}

	private void init() {
		this.initialized = true;
	}

	private void destroy() {
		this.destroyed = true;
	}

}


/**
 * 
 * @author Rod Johnson
 */
interface DummyBo {

	void something();

}


/**
 * 
 * @author Rod Johnson
 */
class DummyBoImpl implements DummyBo {
	
	DummyDao dao;

	public DummyBoImpl(DummyDao dao) {
		this.dao = dao;
	}
	
	public void something() {
		
	}

}


/**
 * @author Rod Johnson
 */
class DummyDao {
	
	DataSource ds;

	public DummyDao(DataSource ds) {
		this.ds = ds;
	}

}


/**
 * @author Juergen Hoeller
 * @since 21.07.2003
 */
class DummyReferencer {

	private TestBean testBean1;

	private TestBean testBean2;

	private DummyFactory dummyFactory;


	public DummyReferencer() {
	}

	public DummyReferencer(DummyFactory dummyFactory) {
		this.dummyFactory = dummyFactory;
	}

	public void setDummyFactory(DummyFactory dummyFactory) {
		this.dummyFactory = dummyFactory;
	}

	public DummyFactory getDummyFactory() {
		return dummyFactory;
	}

	public void setTestBean1(TestBean testBean1) {
		this.testBean1 = testBean1;
	}

	public TestBean getTestBean1() {
		return testBean1;
	}

	public void setTestBean2(TestBean testBean2) {
		this.testBean2 = testBean2;
	}

	public TestBean getTestBean2() {
		return testBean2;
	}

}


/**
 * Test class for Spring's ability to create objects using static
 * factory methods, rather than constructors.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
class FactoryMethods {
	
	public static FactoryMethods nullInstance() {
		return null;
	}

	public static FactoryMethods defaultInstance() {
		TestBean tb = new TestBean();
		tb.setName("defaultInstance");
		return new FactoryMethods(tb, "default", 0);
	}
	
	/**
	 * Note that overloaded methods are supported.
	 */
	public static FactoryMethods newInstance(TestBean tb) {
		return new FactoryMethods(tb, "default", 0);
	}
	
	protected static FactoryMethods newInstance(TestBean tb, int num, String name) {
		if (name == null) {
			throw new IllegalStateException("Should never be called with null value");
		}
		return new FactoryMethods(tb, name, num);
	}
	
	static FactoryMethods newInstance(TestBean tb, int num, Integer something) {
		if (something != null) {
			throw new IllegalStateException("Should never be called with non-null value");
		}
		return new FactoryMethods(tb, null, num);
	}

	private static List<?> listInstance() {
		return Collections.EMPTY_LIST;
	}


	private int num = 0;
	private String name = "default";
	private TestBean tb;
	private String stringValue;


	/**
	 * Constructor is private: not for use outside this class,
	 * even by IoC container.
	 */
	private FactoryMethods(TestBean tb, String name, int num) {
		this.tb = tb;
		this.name = name;
		this.num = num;
	}
	
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
	public String getStringValue() {
		return this.stringValue;
	}
	
	public TestBean getTestBean() {
		return this.tb;
	}
	
	protected TestBean protectedGetTestBean() {
		return this.tb;
	}
	
	private TestBean privateGetTestBean() {
		return this.tb;
	}
	
	public int getNum() {
		return num;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Set via Setter Injection once instance is created.
	 */
	public void setName(String name) {
		this.name = name;
	}

}

/**
 * Fixed method replacer for String return types
 * @author Rod Johnson
 */
class FixedMethodReplacer implements MethodReplacer {

	public static final String VALUE = "fixedMethodReplacer";

	public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
		return VALUE;
	}

}


/**
 * @author Chris Beams
 */
class MapAndSet {

	private Object obj;

	public MapAndSet(Map<?, ?> map) {
		this.obj = map;
	}

	public MapAndSet(Set<?> set) {
		this.obj = set;
	}

	public Object getObject() {
		return obj;
	}
}


/**
 * @author Rod Johnson
 */
class MethodReplaceCandidate {

	public String replaceMe(String echo) {
		return echo;
	}

}


/**
 * Bean that exposes a simple property that can be set
 * to a mix of references and individual values.
 *
 * @author Rod Johnson
 * @since 27.05.2003
 */
class MixedCollectionBean {

	private Collection<?> jumble;


	public void setJumble(Collection<?> jumble) {
		this.jumble = jumble;
	}

	public Collection<?> getJumble() {
		return jumble;
	}

}


/**
 * @author Juergen Hoeller
 */
interface OverrideInterface {

	TestBean getPrototypeDependency();

	TestBean getPrototypeDependency(Object someParam);

}


/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
abstract class OverrideOneMethod extends MethodReplaceCandidate implements OverrideInterface {

	protected abstract TestBean protectedOverrideSingleton();

	public TestBean getPrototypeDependency(Object someParam) {
		return new TestBean();
	}

	public TestBean invokesOverridenMethodOnSelf() {
		return getPrototypeDependency();
	}

	public String echo(String echo) {
		return echo;
	}

	/**
	 * Overloaded form of replaceMe.
	 */
	public String replaceMe() {
		return "replaceMe";
	}

	/**
	 * Another overloaded form of replaceMe, not getting replaced.
	 * Must not cause errors when the other replaceMe methods get replaced.
	 */
	public String replaceMe(int someParam) {
		return "replaceMe:" + someParam;
	}

}


/**
 * Subclass of OverrideOneMethod, to check that overriding is
 * supported for inherited methods.
 *
 * @author Rod Johnson
 */
abstract class OverrideOneMethodSubclass extends OverrideOneMethod {
	
	protected void doSomething(String arg) {
		// This implementation does nothing!
		// It's not overloaded
	}

}


/**
 * Simple test of BeanFactory initialization and lifecycle callbacks.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
class ProtectedLifecycleBean implements BeanNameAware, BeanFactoryAware, InitializingBean, DisposableBean {

	protected boolean initMethodDeclared = false;

	protected String beanName;

	protected BeanFactory owningFactory;

	protected boolean postProcessedBeforeInit;

	protected boolean inited;

	protected boolean initedViaDeclaredInitMethod;

	protected boolean postProcessedAfterInit;

	protected boolean destroyed;


	public void setInitMethodDeclared(boolean initMethodDeclared) {
		this.initMethodDeclared = initMethodDeclared;
	}

	public boolean isInitMethodDeclared() {
		return initMethodDeclared;
	}

	public void setBeanName(String name) {
		this.beanName = name;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.owningFactory = beanFactory;
	}

	public void postProcessBeforeInit() {
		if (this.inited || this.initedViaDeclaredInitMethod) {
			throw new RuntimeException("Factory called postProcessBeforeInit after afterPropertiesSet");
		}
		if (this.postProcessedBeforeInit) {
			throw new RuntimeException("Factory called postProcessBeforeInit twice");
		}
		this.postProcessedBeforeInit = true;
	}

	public void afterPropertiesSet() {
		if (this.owningFactory == null) {
			throw new RuntimeException("Factory didn't call setBeanFactory before afterPropertiesSet on lifecycle bean");
		}
		if (!this.postProcessedBeforeInit) {
			throw new RuntimeException("Factory didn't call postProcessBeforeInit before afterPropertiesSet on lifecycle bean");
		}
		if (this.initedViaDeclaredInitMethod) {
			throw new RuntimeException("Factory initialized via declared init method before initializing via afterPropertiesSet");
		}
		if (this.inited) {
			throw new RuntimeException("Factory called afterPropertiesSet twice");
		}
		this.inited = true;
	}

	public void declaredInitMethod() {
		if (!this.inited) {
			throw new RuntimeException("Factory didn't call afterPropertiesSet before declared init method");
		}

		if (this.initedViaDeclaredInitMethod) {
			throw new RuntimeException("Factory called declared init method twice");
		}
		this.initedViaDeclaredInitMethod = true;
	}

	public void postProcessAfterInit() {
		if (!this.inited) {
			throw new RuntimeException("Factory called postProcessAfterInit before afterPropertiesSet");
		}
		if (this.initMethodDeclared && !this.initedViaDeclaredInitMethod) {
			throw new RuntimeException("Factory called postProcessAfterInit before calling declared init method");
		}
		if (this.postProcessedAfterInit) {
			throw new RuntimeException("Factory called postProcessAfterInit twice");
		}
		this.postProcessedAfterInit = true;
	}

	/**
	 * Dummy business method that will fail unless the factory
	 * managed the bean's lifecycle correctly
	 */
	public void businessMethod() {
		if (!this.inited || (this.initMethodDeclared && !this.initedViaDeclaredInitMethod) ||
				!this.postProcessedAfterInit) {
			throw new RuntimeException("Factory didn't initialize lifecycle object correctly");
		}
	}

	public void destroy() {
		if (this.destroyed) {
			throw new IllegalStateException("Already destroyed");
		}
		this.destroyed = true;
	}

	public boolean isDestroyed() {
		return destroyed;
	}


	public static class PostProcessor implements BeanPostProcessor {

		public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
			if (bean instanceof ProtectedLifecycleBean) {
				((ProtectedLifecycleBean) bean).postProcessBeforeInit();
			}
			return bean;
		}

		public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
			if (bean instanceof ProtectedLifecycleBean) {
				((ProtectedLifecycleBean) bean).postProcessAfterInit();
			}
			return bean;
		}
	}

}


/**
 * @author Rod Johnson
 */
@SuppressWarnings("serial")
class ReverseMethodReplacer implements MethodReplacer, Serializable {

	public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
		String s = (String) args[0];
		return new StringBuffer(s).reverse().toString();
	}

}


/**
 * @author Rod Johnson
 */
@SuppressWarnings("serial")
abstract class SerializableMethodReplacerCandidate extends MethodReplaceCandidate implements Serializable {

	//public abstract Point getPoint();

}


/**
 * @author Juergen Hoeller
 * @since 23.10.2004
 */
class SingleSimpleTypeConstructorBean {

	private boolean singleBoolean;

	private boolean secondBoolean;

	private String testString;

	public SingleSimpleTypeConstructorBean(boolean singleBoolean) {
		this.singleBoolean = singleBoolean;
	}

	public SingleSimpleTypeConstructorBean(String testString, boolean secondBoolean) {
		this.testString = testString;
		this.secondBoolean = secondBoolean;
	}

	public boolean isSingleBoolean() {
		return singleBoolean;
	}

	public boolean isSecondBoolean() {
		return secondBoolean;
	}

	public String getTestString() {
		return testString;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9903.java