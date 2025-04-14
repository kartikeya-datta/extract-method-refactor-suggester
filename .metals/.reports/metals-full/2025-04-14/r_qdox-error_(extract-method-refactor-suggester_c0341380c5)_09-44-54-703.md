error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17845.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17845.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17845.java
text:
```scala
i@@f (!(TestElement.COMMENTS.equals(jmp.getName())

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

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.TestElementProperty;
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
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TestElementPropertyConverter extends AbstractCollectionConverter {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private final boolean testFormat22=SaveService.isSaveTestPlanFormat22();
    
    private static final String HEADER_CLASSNAME 
        = "org.apache.jmeter.protocol.http.control.Header"; // $NON-NLS-1$

	/**
	 * Returns the converter version; used to check for possible
	 * incompatibilities
	 */
	public static String getVersion() {
		return "$Revision$"; // $NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class arg0) {
		return arg0.equals(TestElementProperty.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
	 *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object arg0, HierarchicalStreamWriter writer, MarshallingContext context) {
		TestElementProperty prop = (TestElementProperty) arg0;
		writer.addAttribute(ConversionHelp.ATT_NAME, ConversionHelp.encode(prop.getName()));
        Class clazz = prop.getObjectValue().getClass();
		writer.addAttribute(ConversionHelp.ATT_ELEMENT_TYPE,
                testFormat22 ?  mapper().serializedClass(clazz) : clazz.getName());
        if (testFormat22){
            TestElement te = (TestElement)prop.getObjectValue();
            ConversionHelp.saveSpecialProperties(te,writer);
        }
		PropertyIterator iter = prop.iterator();
		while (iter.hasNext()) {
            JMeterProperty jmp=iter.next();
            // Skip special properties if required
            if (!testFormat22 || !ConversionHelp.isSpecialProperty(jmp.getName())) 
            {
            	// Don't save empty comments
	       		if (!(TestPlan.COMMENTS.equals(jmp.getName())
		       			&& jmp.getStringValue().length()==0))
		       	{
		            writeItem(jmp, context, writer);
		       	}
            }
		}
        //TODO clazz is probably always the same as testclass
	}

	/*
	 * TODO - convert to work more like upgrade.properties/NameUpdater.java
	 * 
	 * Special processing is carried out for the Header Class The String
	 * property TestElement.name is converted to Header.name for example:
	 * <elementProp name="User-Agent"
	 * elementType="org.apache.jmeter.protocol.http.control.Header"> <stringProp
	 * name="Header.value">Mozilla%2F4.0+%28compatible%3B+MSIE+5.5%3B+Windows+98%29</stringProp>
	 * <stringProp name="TestElement.name">User-Agent</stringProp>
	 * </elementProp> becomes <elementProp name="User-Agent"
	 * elementType="org.apache.jmeter.protocol.http.control.Header"> <stringProp
	 * name="Header.value">Mozilla%2F4.0+%28compatible%3B+MSIE+5.5%3B+Windows+98%29</stringProp>
	 * <stringProp name="Header.name">User-Agent</stringProp> </elementProp>
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
	 *      com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		try {
			TestElementProperty prop = (TestElementProperty) createCollection(context.getRequiredType());
			prop.setName(ConversionHelp.decode(reader.getAttribute(ConversionHelp.ATT_NAME)));
			String element = reader.getAttribute(ConversionHelp.ATT_ELEMENT_TYPE);
			boolean isHeader = HEADER_CLASSNAME.equals(element);
			prop.setObjectValue(mapper().realClass(element).newInstance());// Always decode
            TestElement te = (TestElement)prop.getObjectValue();
            // No need to check version, just process the attributes if present
            ConversionHelp.restoreSpecialProperties(te, reader);
			while (reader.hasMoreChildren()) {
				reader.moveDown();
				JMeterProperty subProp = (JMeterProperty) readItem(reader, context, prop);
				if (isHeader) {
					String name = subProp.getName();
					if (TestElement.NAME.equals(name)) {
						subProp.setName("Header.name");// $NON-NLS-1$
						// Must be same as Header.HNAME - but that is built
						// later
					}
				}
				prop.addProperty(subProp);
				reader.moveUp();
			}
			return prop;
		} catch (Exception e) {
			log.error("Couldn't unmarshall TestElementProperty", e);
			return new TestElementProperty("ERROR", new ConfigTestElement());// $NON-NLS-1$
		}
	}

	/**
	 * @param arg0
	 */
	public TestElementPropertyConverter(Mapper arg0) {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17845.java