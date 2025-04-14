error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3977.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3977.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3977.java
text:
```scala
v@@alues.add(Converter.convert(propVal.getStringValue(), Class.forName(type)));

// $Header$
/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.jmeter.testbeans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.jmeter.testbeans.gui.TableEditor;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.MultiProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.Converter;
import org.apache.log.Logger;

/**
 * This is an experimental class. An attempt to address the complexity of
 * writing new JMeter components.
 * <p>
 * TestBean currently extends AbstractTestElement to support
 * backward-compatibility, but the property-value-map may later on be separated
 * from the test beans themselves. To ensure this will be doable with minimum
 * damage, all inherited methods are deprecated.
 * 
 * @author <a href="mailto:jsalvata@apache.org">Jordi Salvat i Alabart </a>
 * @version $Revision$ updated on $Date$
 */
public class TestBeanHelper {
	protected static final Logger log = LoggingManager.getLoggerForClass();

	/**
	 * Prepare the bean for work by populating the bean's properties from the
	 * property value map.
	 * <p>
	 * 
	 * @deprecated to limit it's usage in expectation of moving it elsewhere.
	 */
	public static void prepare(TestElement el) {
		if (!(el instanceof TestBean)) {
			return;
		}
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(el.getClass());
			PropertyDescriptor[] desc = beanInfo.getPropertyDescriptors();
			Object[] param = new Object[1];

			if (log.isDebugEnabled())
				log.debug("Preparing " + el.getClass());

			for (int x = 0; x < desc.length; x++) {
				// Obtain a value of the appropriate type for this property.
				JMeterProperty jprop = el.getProperty(desc[x].getName());
				Class type = desc[x].getPropertyType();
				Object value = null;
				value = unwrapProperty(desc[x], jprop, type);

				if (log.isDebugEnabled())
					log.debug("Setting " + jprop.getName() + "=" + value);

				// Set the bean's property to the value we just obtained:
				if (value != null || !type.isPrimitive())
				// We can't assign null to primitive types.
				{
					param[0] = value;
					invokeOrBailOut(el, desc[x].getWriteMethod(), param);
				}
			}
		} catch (IntrospectionException e) {
			log.error("Couldn't set properties for " + el.getClass().getName(), e);
		}
	}

	/**
	 * @param desc
	 * @param x
	 * @param jprop
	 * @param type
	 * @return
	 */
	private static Object unwrapProperty(PropertyDescriptor desc, JMeterProperty jprop, Class type) {
		Object value;
		if(jprop instanceof MultiProperty)
		{
			value = unwrapCollection((MultiProperty)jprop,(String)desc.getValue(TableEditor.CLASSNAME));
		}
		else value = Converter.convert(jprop.getStringValue(), type);
		return value;
	}
	
	private static Object unwrapCollection(MultiProperty prop,String type)
	{
		if(prop instanceof CollectionProperty)
		{
			Collection values = new LinkedList();
			PropertyIterator iter = prop.iterator();
			while(iter.hasNext())
			{
				JMeterProperty propVal = iter.next();
				try
				{
					values.add(Converter.convert(propVal.getObjectValue(), Class.forName(type)));
				}
				catch(Exception e)
				{
					log.error("Couldn't convert object: " + propVal.getObjectValue() + " to " + type);
				}
			}
			return values;
		}
		return null;
	}

	/**
	 * Utility method that invokes a method and does the error handling around
	 * the invocation.
	 * 
	 * @param method
	 * @param params
	 * @return the result of the method invocation.
	 */
	private static Object invokeOrBailOut(Object invokee, Method method, Object[] params) {
		try {
			return method.invoke(invokee, params);
		} catch (IllegalArgumentException e) {
			log.error("This should never happen.", e);
			throw new Error(e.toString()); // Programming error: bail out.
		} catch (IllegalAccessException e) {
			log.error("This should never happen.", e);
			throw new Error(e.toString()); // Programming error: bail out.
		} catch (InvocationTargetException e) {
			log.error("This should never happen.", e);
			throw new Error(e.toString()); // Programming error: bail out.
		}
	}

	/**
	 * Utility method to obtain the value of a property in the given type.
	 * <p>
	 * I plan to get rid of this sooner than later, so please don't use it much.
	 * 
	 * @param property
	 *            Property to get the value of.
	 * @param type
	 *            Type of the result.
	 * @return an object of the given type if it is one of the known supported
	 *         types, or the value returned by property.getObjectValue
	 * @deprecated
	 */
	private static Object unwrapProperty(JMeterProperty property, Class type) {
		return Converter.convert(property.getObjectValue(), type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3977.java