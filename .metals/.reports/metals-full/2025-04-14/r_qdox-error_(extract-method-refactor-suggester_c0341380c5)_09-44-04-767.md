error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4175.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4175.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4175.java
text:
```scala
a@@ssertEquals(String.class.getName(), specialAttrs.get("clazz"));

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

package org.springframework.core.type;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;

/**
 * @author Juergen Hoeller
 */
public class AnnotationMetadataTests extends TestCase {

	public void testStandardAnnotationMetadata() throws IOException {
		StandardAnnotationMetadata annInfo = new StandardAnnotationMetadata(AnnotatedComponent.class);
		doTestAnnotationInfo(annInfo);
		doTestMethodAnnotationInfo(annInfo);
	}

	public void testAsmAnnotationMetadata() throws IOException {
		MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
		MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(AnnotatedComponent.class.getName());
		doTestAnnotationInfo(metadataReader.getAnnotationMetadata());
		doTestMethodAnnotationInfo(metadataReader.getAnnotationMetadata());
	}

	private void doTestAnnotationInfo(AnnotationMetadata metadata) {
		assertEquals(AnnotatedComponent.class.getName(), metadata.getClassName());
		assertFalse(metadata.isInterface());
		assertFalse(metadata.isAbstract());
		assertTrue(metadata.isConcrete());
		assertTrue(metadata.hasSuperClass());
		assertEquals(Object.class.getName(), metadata.getSuperClassName());
		assertEquals(1, metadata.getInterfaceNames().length);
		assertEquals(Serializable.class.getName(), metadata.getInterfaceNames()[0]);

		assertTrue(metadata.hasAnnotation(Component.class.getName()));
		assertTrue(metadata.hasAnnotation(Scope.class.getName()));
		assertTrue(metadata.hasAnnotation(SpecialAttr.class.getName()));
		assertEquals(3, metadata.getAnnotationTypes().size());
		assertTrue(metadata.getAnnotationTypes().contains(Component.class.getName()));
		assertTrue(metadata.getAnnotationTypes().contains(Scope.class.getName()));
		assertTrue(metadata.getAnnotationTypes().contains(SpecialAttr.class.getName()));

		Map<String, Object> compAttrs = metadata.getAnnotationAttributes(Component.class.getName());
		assertEquals(1, compAttrs.size());
		assertEquals("myName", compAttrs.get("value"));
		Map<String, Object> scopeAttrs = metadata.getAnnotationAttributes(Scope.class.getName());
		assertEquals(1, scopeAttrs.size());
		assertEquals("myScope", scopeAttrs.get("value"));
		Map<String, Object> specialAttrs = metadata.getAnnotationAttributes(SpecialAttr.class.getName());
		assertEquals(2, specialAttrs.size());
		assertEquals(String.class, specialAttrs.get("clazz"));
		assertEquals(Thread.State.NEW, specialAttrs.get("state"));
	}
	
	private void doTestMethodAnnotationInfo(AnnotationMetadata classMetadata) {
		Set<MethodMetadata> methods = classMetadata.getAnnotatedMethods("org.springframework.beans.factory.annotation.Autowired");
		assertEquals(1, methods.size());
		for (MethodMetadata methodMetadata : methods) {
			Set<String> annotationTypes = methodMetadata.getAnnotationTypes();
			assertEquals(1, annotationTypes.size());
		}
		
	}


	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SpecialAttr {

		Class clazz();

		Thread.State state();
	}


	@Component("myName")
	@Scope("myScope")
	@SpecialAttr(clazz = String.class, state = Thread.State.NEW)
	private static class AnnotatedComponent implements Serializable {
		
		@Autowired
		public void doWork(@Qualifier("myColor") java.awt.Color color) {
			
		}
		@Test
		public void doSleep() 
		{
			
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4175.java