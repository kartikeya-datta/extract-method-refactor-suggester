error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14927.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14927.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14927.java
text:
```scala
t@@his.rootObject = TypedValue.NULL;

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

package org.springframework.expression.spel.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.ConstructorResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.OperatorOverloader;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypeComparator;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypeLocator;
import org.springframework.expression.TypedValue;
import org.springframework.util.Assert;

/**
 * Provides a default EvaluationContext implementation.
 *
 * <p>To resolve properties/methods/fields this context uses a reflection mechanism.
 *
 * @author Andy Clement
 * @author Juergen Hoeller
 * @since 3.0
 */
public class StandardEvaluationContext implements EvaluationContext {

	private TypedValue rootObject;

	private List<ConstructorResolver> constructorResolvers;

	private List<MethodResolver> methodResolvers;

	private List<PropertyAccessor> propertyAccessors;

	private TypeLocator typeLocator;

	private TypeConverter typeConverter;

	private TypeComparator typeComparator = new StandardTypeComparator();

	private OperatorOverloader operatorOverloader = new StandardOperatorOverloader();

	private final Map<String, Object> variables = new HashMap<String, Object>();


	public StandardEvaluationContext() {
		setRootObject(null);
	}
	
	public StandardEvaluationContext(Object rootObject) {
		this();
		setRootObject(rootObject);
	}


	public void setRootObject(Object rootObject) {
		if (this.rootObject == null) {
			this.rootObject = TypedValue.NULL_TYPED_VALUE; 
		} else {
			this.rootObject = new TypedValue(rootObject);//, TypeDescriptor.forObject(rootObject));
		}
	}

	public void setRootObject(Object rootObject, TypeDescriptor typeDescriptor) {
		this.rootObject = new TypedValue(rootObject, typeDescriptor);
	}

	public TypedValue getRootObject() {
		return this.rootObject;
	}

	public void addConstructorResolver(ConstructorResolver resolver) {
		ensureConstructorResolversInitialized();
		this.constructorResolvers.add(this.constructorResolvers.size() - 1, resolver);
	}
	
	public List<ConstructorResolver> getConstructorResolvers() {
		ensureConstructorResolversInitialized();
		return this.constructorResolvers;
	}

	private void ensureConstructorResolversInitialized() {
		if (this.constructorResolvers == null) {
			this.constructorResolvers = new ArrayList<ConstructorResolver>();
			this.constructorResolvers.add(new ReflectiveConstructorResolver());
		}
	}

	public void addMethodResolver(MethodResolver resolver) {
		ensureMethodResolversInitialized();
		this.methodResolvers.add(this.methodResolvers.size() - 1, resolver);
	}

	public List<MethodResolver> getMethodResolvers() {
		ensureMethodResolversInitialized();
		return this.methodResolvers;
	}
	
	private void ensureMethodResolversInitialized() {
		if (this.methodResolvers == null) {
			this.methodResolvers = new ArrayList<MethodResolver>();
			this.methodResolvers.add(new ReflectiveMethodResolver());
		}
	}

	public void addPropertyAccessor(PropertyAccessor accessor) {
		ensurePropertyAccessorsInitialized();
		this.propertyAccessors.add(this.propertyAccessors.size() - 1, accessor);
	}

	public List<PropertyAccessor> getPropertyAccessors() {
		ensurePropertyAccessorsInitialized();
		return this.propertyAccessors;
	}

	private void ensurePropertyAccessorsInitialized() {
		if (this.propertyAccessors == null) {
			this.propertyAccessors = new ArrayList<PropertyAccessor>();
			this.propertyAccessors.add(new ReflectivePropertyAccessor());
		}
	}

	public void setTypeLocator(TypeLocator typeLocator) {
		Assert.notNull(typeLocator, "TypeLocator must not be null");
		this.typeLocator = typeLocator;
	}

	public TypeLocator getTypeLocator() {
		if (this.typeLocator == null) {
			 this.typeLocator = new StandardTypeLocator();
		}
		return this.typeLocator;
	}

	public void setTypeConverter(TypeConverter typeConverter) {
		Assert.notNull(typeConverter, "TypeConverter must not be null");
		this.typeConverter = typeConverter;
	}

	public TypeConverter getTypeConverter() {
		if (this.typeConverter == null) {
			 this.typeConverter = new StandardTypeConverter();
		}
		return this.typeConverter;
	}

	public void setTypeComparator(TypeComparator typeComparator) {
		Assert.notNull(typeComparator, "TypeComparator must not be null");
		this.typeComparator = typeComparator;
	}

	public TypeComparator getTypeComparator() {
		return this.typeComparator;
	}

	public void setOperatorOverloader(OperatorOverloader operatorOverloader) {
		Assert.notNull(operatorOverloader, "OperatorOverloader must not be null");
		this.operatorOverloader = operatorOverloader;
	}

	public OperatorOverloader getOperatorOverloader() {
		return this.operatorOverloader;
	}

	public void setVariable(String name, Object value) {
		this.variables.put(name, value);
	}

	public void setVariables(Map<String,Object> variables) {
		this.variables.putAll(variables);
	}

	public void registerFunction(String name, Method method) {
		this.variables.put(name, method);
	}

	public Object lookupVariable(String name) {
		return this.variables.get(name);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14927.java