error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5492.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5492.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5492.java
text:
```scala
d@@efinition.getPropertyValues().add("foo", "bar");

/*
 * Copyright 2002-2008 the original author or authors.
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

package org.springframework.beans.factory.support;

import junit.framework.TestCase;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;

import test.beans.TestBean;

/**
 * @author Rob Harrop
 */
public class DefinitionMetadataEqualsHashCodeTests extends TestCase {

	public void testRootBeanDefinitionEqualsAndHashCode() throws Exception {
		RootBeanDefinition master = new RootBeanDefinition(TestBean.class);
		RootBeanDefinition equal = new RootBeanDefinition(TestBean.class);
		RootBeanDefinition notEqual = new RootBeanDefinition(String.class);
		RootBeanDefinition subclass = new RootBeanDefinition(TestBean.class) {};
		setBaseProperties(master);
		setBaseProperties(equal);
		setBaseProperties(notEqual);
		setBaseProperties(subclass);

		assertEqualsContract(master, equal, notEqual, subclass);
		assertEquals("Hash code for equal instances should match", master.hashCode(), equal.hashCode());
	}

	public void testChildBeanDefinitionEqualsAndHashCode() throws Exception {
		ChildBeanDefinition master = new ChildBeanDefinition("foo");
		ChildBeanDefinition equal = new ChildBeanDefinition("foo");
		ChildBeanDefinition notEqual = new ChildBeanDefinition("bar");
		ChildBeanDefinition subclass = new ChildBeanDefinition("foo"){};
		setBaseProperties(master);
		setBaseProperties(equal);
		setBaseProperties(notEqual);
		setBaseProperties(subclass);

		assertEqualsContract(master, equal, notEqual, subclass);
		assertEquals("Hash code for equal instances should match", master.hashCode(), equal.hashCode());
	}

	public void testRuntimeBeanReference() throws Exception {
		RuntimeBeanReference master = new RuntimeBeanReference("name");
		RuntimeBeanReference equal = new RuntimeBeanReference("name");
		RuntimeBeanReference notEqual = new RuntimeBeanReference("someOtherName");
		RuntimeBeanReference subclass = new RuntimeBeanReference("name"){};
		assertEqualsContract(master, equal, notEqual, subclass);
	}
	private void setBaseProperties(AbstractBeanDefinition definition) {
		definition.setAbstract(true);
		definition.setAttribute("foo", "bar");
		definition.setAutowireCandidate(false);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		//definition.getConstructorArgumentValues().addGenericArgumentValue("foo");
		definition.setDependencyCheck(AbstractBeanDefinition.DEPENDENCY_CHECK_OBJECTS);
		definition.setDependsOn(new String[]{"foo", "bar"});
		definition.setDestroyMethodName("destroy");
		definition.setEnforceDestroyMethod(false);
		definition.setEnforceInitMethod(true);
		definition.setFactoryBeanName("factoryBean");
		definition.setFactoryMethodName("factoryMethod");
		definition.setInitMethodName("init");
		definition.setLazyInit(true);
		definition.getMethodOverrides().addOverride(new LookupOverride("foo", "bar"));
		definition.getMethodOverrides().addOverride(new ReplaceOverride("foo", "bar"));
		definition.getPropertyValues().addPropertyValue("foo", "bar");
		definition.setResourceDescription("desc");
		definition.setRole(BeanDefinition.ROLE_APPLICATION);
		definition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		definition.setSource("foo");
	}

	private void assertEqualsContract(Object master, Object equal, Object notEqual, Object subclass) {
		assertEquals("Should be equal", master, equal);
		assertFalse("Should not be equal", master.equals(notEqual));
		assertEquals("Subclass should be equal", master, subclass);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5492.java