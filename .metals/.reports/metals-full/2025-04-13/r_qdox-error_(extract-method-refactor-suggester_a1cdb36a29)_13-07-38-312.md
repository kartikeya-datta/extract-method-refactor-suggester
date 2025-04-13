error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10711.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10711.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10711.java
text:
```scala
u@@rls.add(new File(element).toURI().toURL());

/*
 * Copyright 2004-2008 the original author or authors.
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
package org.springframework.expression.spel.standard;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.expression.ConstructorResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypeLocator;
import org.springframework.expression.TypeUtils;
import org.springframework.expression.spel.reflection.ReflectionConstructorResolver;
import org.springframework.expression.spel.reflection.ReflectionMethodResolver;
import org.springframework.expression.spel.reflection.ReflectionPropertyResolver;

/**
 * Provides a default EvaluationContext implementation.
 * <p>
 * To resolved properties/methods/fields this context uses a reflection mechanism.
 * 
 * @author Andy Clement
 * 
 */
public class StandardEvaluationContext implements EvaluationContext {

	private Object rootObject;
	private StandardTypeUtilities typeUtils;
	private final Map<String, Object> variables = new HashMap<String, Object>();
	private final List<MethodResolver> methodResolvers = new ArrayList<MethodResolver>();
	private final List<ConstructorResolver> constructorResolvers = new ArrayList<ConstructorResolver>();
	private final List<PropertyAccessor> propertyResolvers = new ArrayList<PropertyAccessor>();
	private final Map<String, Map<String, Object>> simpleReferencesMap = new HashMap<String, Map<String, Object>>();

	public StandardEvaluationContext() {
		typeUtils = new StandardTypeUtilities();
		addMethodResolver(new ReflectionMethodResolver());
		addConstructorResolver(new ReflectionConstructorResolver());
		addPropertyAccessor(new ReflectionPropertyResolver());
	}

	public StandardEvaluationContext(Object rootContextObject) {
		this();
		this.rootObject = rootContextObject;
	}

	public void setClassLoader(ClassLoader loader) {
		TypeLocator tLocator = typeUtils.getTypeLocator();
		if (tLocator instanceof StandardTypeLocator) {
			((StandardTypeLocator) tLocator).setClassLoader(loader);
		}
	}

	public void registerImport(String importPrefix) {
		TypeLocator tLocator = typeUtils.getTypeLocator();
		if (tLocator instanceof StandardTypeLocator) {
			((StandardTypeLocator) tLocator).registerImport(importPrefix);
		}
	}

	public void setClasspath(String classpath) {
		StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
		List<URL> urls = new ArrayList<URL>();
		while (st.hasMoreTokens()) {
			String element = st.nextToken();
			try {
				urls.add(new File(element).toURL());
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid element in classpath " + element);
			}
		}
		ClassLoader cl = new URLClassLoader(urls.toArray(new URL[] {}), Thread.currentThread().getContextClassLoader());
		TypeLocator tLocator = typeUtils.getTypeLocator();
		if (tLocator instanceof StandardTypeLocator) {
			((StandardTypeLocator) tLocator).setClassLoader(cl);
		}
	}

	public Object lookupVariable(String name) {
		return variables.get(name);
	}

	public TypeUtils getTypeUtils() {
		return typeUtils;
	}

	public Object getRootContextObject() {
		return rootObject;
	}

	public Object lookupReference(Object contextName, Object objectName) {
		String contextToLookup = (contextName == null ? "root" : (String) contextName);
		// if (contextName==null) return simpleReferencesMap;
		Map<String, Object> contextMap = simpleReferencesMap.get(contextToLookup);
		if (contextMap == null)
			return null;
		if (objectName == null)
			return contextMap;
		return contextMap.get(objectName);
	}

	public List<PropertyAccessor> getPropertyAccessors() {
		return propertyResolvers;
	}
	
	public void addPropertyAccessor(PropertyAccessor accessor) {
		propertyResolvers.add(accessor);
	}
	
	public void removePropertyAccessor(PropertyAccessor accessor) {
		propertyResolvers.remove(accessor);
	}
	
	public void insertPropertyAccessor(int position,PropertyAccessor accessor) {
		propertyResolvers.add(position,accessor);
	}


	public List<MethodResolver> getMethodResolvers() {
		return methodResolvers;
	}

	public List<ConstructorResolver> getConstructorResolvers() {
		return constructorResolvers;
	}

	public void setVariable(String name, Object value) {
		variables.put(name, value);
	}

	public void registerFunction(String name, Method m) {
		variables.put(name, m);
	}

	public void setRootObject(Object o) {
		this.rootObject = o;
	}

	// TODO 3 have a variant that adds at position (same for ctor/propOrField)
	public void addMethodResolver(MethodResolver resolver) {
		methodResolvers.add(resolver);
	}
	
	public void removeMethodResolver(MethodResolver resolver) {
		methodResolvers.remove(resolver);
	}

	public void insertMethodResolver(int pos, MethodResolver resolver) {
		methodResolvers.add(pos, resolver);
	}


	public void addConstructorResolver(ConstructorResolver resolver) {
		constructorResolvers.add(resolver);
	}


	public void addReference(String contextName, String objectName, Object value) {
		Map<String, Object> contextMap = simpleReferencesMap.get(contextName);
		if (contextMap == null) {
			contextMap = new HashMap<String, Object>();
			simpleReferencesMap.put(contextName, contextMap);
		}
		contextMap.put(objectName, value);
	}

	public void addTypeConverter(StandardIndividualTypeConverter newConverter) {
		((StandardTypeConverter)typeUtils.getTypeConverter()).registerConverter(newConverter);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10711.java