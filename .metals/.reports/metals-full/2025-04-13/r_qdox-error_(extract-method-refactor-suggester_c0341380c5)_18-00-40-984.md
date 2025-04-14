error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3394.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3394.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3394.java
text:
```scala
public P@@ropertyTypeDescriptor(PropertyDescriptor propertyDescriptor, MethodParameter methodParameter, Class<?> type) {

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

package org.springframework.core.convert.support;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;

/**
 * {@link TypeDescriptor} extension that exposes additional annotations
 * as conversion metadata: namely, annotations on other accessor methods
 * (getter/setter) and on the underlying field, if found.
 *
 * @author Juergen Hoeller
 * @since 3.0
 */
public class PropertyTypeDescriptor extends TypeDescriptor {

	private final PropertyDescriptor propertyDescriptor;

	private Annotation[] cachedAnnotations;


	/**
	 * Create a new BeanTypeDescriptor for the given bean property.
	 * @param propertyDescriptor the corresponding JavaBean PropertyDescriptor
	 * @param methodParameter the target method parameter
	 */
	public PropertyTypeDescriptor(PropertyDescriptor propertyDescriptor, MethodParameter methodParameter) {
		super(methodParameter);
		this.propertyDescriptor = propertyDescriptor;
	}

	/**
	 * Create a new BeanTypeDescriptor for the given bean property.
	 * @param propertyDescriptor the corresponding JavaBean PropertyDescriptor
	 * @param methodParameter the target method parameter
	 * @param type the specific type to expose (may be an array/collection element)
	 */
	public PropertyTypeDescriptor(PropertyDescriptor propertyDescriptor, MethodParameter methodParameter, Class type) {
		super(methodParameter, type);
		this.propertyDescriptor = propertyDescriptor;
	}


	/**
	 * Return the underlying PropertyDescriptor.
	 */
	public PropertyDescriptor getPropertyDescriptor() {
		return this.propertyDescriptor;
	}

	public Annotation[] getAnnotations() {
		Annotation[] anns = this.cachedAnnotations;
		if (anns == null) {
			Field underlyingField = ReflectionUtils.findField(
					getMethodParameter().getMethod().getDeclaringClass(), this.propertyDescriptor.getName());
			Map<Class, Annotation> annMap = new LinkedHashMap<Class, Annotation>();
			if (underlyingField != null) {
				for (Annotation ann : underlyingField.getAnnotations()) {
					annMap.put(ann.annotationType(), ann);
				}
			}
			Method writeMethod = this.propertyDescriptor.getWriteMethod();
			Method readMethod = this.propertyDescriptor.getReadMethod();
			if (writeMethod != null && writeMethod != getMethodParameter().getMethod()) {
				for (Annotation ann : writeMethod.getAnnotations()) {
					annMap.put(ann.annotationType(), ann);
				}
			}
			if (readMethod != null && readMethod != getMethodParameter().getMethod()) {
				for (Annotation ann : readMethod.getAnnotations()) {
					annMap.put(ann.annotationType(), ann);
				}
			}
			for (Annotation ann : getMethodParameter().getMethodAnnotations()) {
				annMap.put(ann.annotationType(), ann);
			}
			for (Annotation ann : getMethodParameter().getParameterAnnotations()) {
				annMap.put(ann.annotationType(), ann);
			}
			anns = annMap.values().toArray(new Annotation[annMap.size()]);
			this.cachedAnnotations = anns;
		}
		return anns;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3394.java