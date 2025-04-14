error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3402.java
text:
```scala
private final M@@apper classMapper;

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

package org.apache.jmeter.save;

import org.apache.jmeter.save.converters.ConversionHelp;
import org.apache.jorphan.collections.HashTree;

import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Handles XStream conversion of Test Scripts
 *
 */
public class ScriptWrapperConverter implements Converter {

    private static final String ATT_PROPERTIES = "properties"; // $NON-NLS-1$
    private static final String ATT_VERSION = "version"; // $NON-NLS-1$

    /**
     * Returns the converter version; used to check for possible
     * incompatibilities
     */
    public static String getVersion() {
        return "$Revision$"; // $NON-NLS-1$
    }

    Mapper classMapper;

    public ScriptWrapperConverter(Mapper classMapper) {
        this.classMapper = classMapper;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
     */
    public boolean canConvert(Class arg0) {
        return arg0.equals(ScriptWrapper.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
     *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
     *      com.thoughtworks.xstream.converters.MarshallingContext)
     */
    public void marshal(Object arg0, HierarchicalStreamWriter writer, MarshallingContext context) {
        ScriptWrapper wrap = (ScriptWrapper) arg0;
        String version = SaveService.getVERSION();
        ConversionHelp.setOutVersion(version);// Ensure output follows version
        writer.addAttribute(ATT_VERSION, version);
        writer.addAttribute(ATT_PROPERTIES, SaveService.getPropertiesVersion());
        writer.startNode(classMapper.serializedClass(wrap.testPlan.getClass()));
        context.convertAnother(wrap.testPlan);
        writer.endNode();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
     *      com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        ScriptWrapper wrap = new ScriptWrapper();
        wrap.version = reader.getAttribute(ATT_VERSION);
        ConversionHelp.setInVersion(wrap.version);// Make sure decoding
                                                    // follows input file
        reader.moveDown();
        // Catch errors and rethrow as ConversionException so we get location details
        try {
            wrap.testPlan = (HashTree) context.convertAnother(wrap, getNextType(reader));
        } catch (NoClassDefFoundError e) {
            throw createConversionException(e);
        } catch (Exception e) {
            throw createConversionException(e);
        }
        return wrap;
    }

    private ConversionException createConversionException(Throwable e) {
        final ConversionException conversionException = new ConversionException(e);
        StackTraceElement[] ste = e.getStackTrace();
        if (ste!=null){
            for(int i=0; i<ste.length; i++){
                StackTraceElement top=ste[i];
                String className=top.getClassName();
                if (className.startsWith("org.apache.jmeter.")){
                    conversionException.add("first-jmeter-class", top.toString());
                    break;
                }
            }
        }
        return conversionException;
    }

    protected Class getNextType(HierarchicalStreamReader reader) {
        String classAttribute = reader.getAttribute(ConversionHelp.ATT_CLASS);
        Class type;
        if (classAttribute == null) {
            type = classMapper.realClass(reader.getNodeName());
        } else {
            type = classMapper.realClass(classAttribute);
        }
        return type;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3402.java