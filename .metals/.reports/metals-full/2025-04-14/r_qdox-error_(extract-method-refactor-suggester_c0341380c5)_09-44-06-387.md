error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17844.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17844.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17844.java
text:
```scala
T@@estElement.COMMENTS.equals(jmp.getName())

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.save.converters;

import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author mstover
 * 
 */
public class TestElementConverter extends AbstractCollectionConverter {
	private static final Logger log = LoggingManager.getLoggerForClass();

	
    private final boolean testFormat22=SaveService.isSaveTestPlanFormat22();

	/**
	 * Returns the converter version; used to check for possible
	 * incompatibilities
	 */
	public static String getVersion() {
		return "$Revision$"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class arg0) {
		return TestElement.class.isAssignableFrom(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
	 *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object arg0, HierarchicalStreamWriter writer, MarshallingContext context) {
		TestElement el = (TestElement) arg0;
        if (testFormat22){
            ConversionHelp.saveSpecialProperties(el,writer);
        }
        PropertyIterator iter = el.propertyIterator();
		while (iter.hasNext()) {
            JMeterProperty jmp=iter.next();
            // Skip special properties if required
            if (!testFormat22 || !ConversionHelp.isSpecialProperty(jmp.getName())) {
            	// Don't save empty comments - except for the TestPlan (to maintain compatibility)
	       		if (!(
		       			TestPlan.COMMENTS.equals(jmp.getName())
		       			&& jmp.getStringValue().length()==0
		       			&& !el.getClass().equals(TestPlan.class)
	       			))
	       		{
		            writeItem(jmp, context, writer);
	       		}
            }
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
	 *      com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String classAttribute = reader.getAttribute(ConversionHelp.ATT_CLASS);
		Class type;
		if (classAttribute == null) {
			type = mapper().realClass(reader.getNodeName());
		} else {
			type = mapper().realClass(classAttribute);
		}
		try {
			TestElement el = (TestElement) type.newInstance();
            // No need to check version, just process the attributes if present
            ConversionHelp.restoreSpecialProperties(el, reader);
			while (reader.hasMoreChildren()) {
				reader.moveDown();
				JMeterProperty prop = (JMeterProperty) readItem(reader, context, el);
				el.setProperty(prop);
				reader.moveUp();
			}
			return el;
		} catch (Exception e) {
			log.error("TestElement not instantiable: " + type, e);
			return null;
		}
	}

	/**
	 * @param arg0
	 */
	public TestElementConverter(Mapper arg0) {
		super(arg0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17844.java