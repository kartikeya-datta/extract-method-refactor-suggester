error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12825.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12825.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12825.java
text:
```scala
r@@etrieveAttributes(reader, context, res);

/*
 * Copyright 2004-2005 The Apache Software Foundation.
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
 *  
 */

/*
 * Created on Sep 14, 2004
 *
 */
package org.apache.jmeter.protocol.http.util;

import java.net.URL;

import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.save.converters.SampleResultConverter;

import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author mstover
 * 
 */
public class HTTPResultConverter extends SampleResultConverter
{

   /**
    * @param arg0
    * @param arg1
    */
   public HTTPResultConverter(ClassMapper arg0, String arg1)
   {
      super(arg0, arg1);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
    */
   public boolean canConvert(Class arg0)
   {
      return HTTPSampleResult.class.equals(arg0);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
    *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
    *      com.thoughtworks.xstream.converters.MarshallingContext)
    */
   public void marshal(Object obj, HierarchicalStreamWriter writer,
         MarshallingContext context)
   {
      HTTPSampleResult res = (HTTPSampleResult) obj;
      SampleSaveConfiguration save = res.getSaveConfig();
      setAttributes(writer, context, res, save);
      saveAssertions(writer, context, res, save);
      saveSubResults(writer, context, res, save);
      saveResponseHeaders(writer, context, res, save);
      saveRequestHeaders(writer, context, res, save);
      saveResponseData(writer, context, res, save);
      saveSamplerData(writer, context, res, save);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.jmeter.save.converters.SampleResultConverter#saveSamplerData(com.thoughtworks.xstream.io.HierarchicalStreamWriter,
    *      org.apache.jmeter.samplers.SampleResult,
    *      org.apache.jmeter.samplers.SampleSaveConfiguration)
    */
   protected void saveSamplerData(HierarchicalStreamWriter writer,
         MarshallingContext context, HTTPSampleResult res,
         SampleSaveConfiguration save)
   {
      if (save.saveSamplerData(res))
      {
         writeString(writer, "cookies", res.getCookies());
         writeString(writer, "method", res.getHTTPMethod());
         writeString(writer, "queryString", res.getQueryString());
         writeString(writer, "redirectLocation", res.getRedirectLocation());
         writeItem(res.getURL(), context, writer);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
    *      com.thoughtworks.xstream.converters.UnmarshallingContext)
    */
   public Object unmarshal(HierarchicalStreamReader reader,
         UnmarshallingContext context)
   {
      HTTPSampleResult res = (HTTPSampleResult) createCollection(context
            .getRequiredType());
      retrieveAttributes(reader, res);
      while (reader.hasMoreChildren())
      {
         reader.moveDown();
         Object subItem = readItem(reader, context, res);
         if (!retrieveItem(reader, context, res, subItem))
         {
            retrieveHTTPItem(reader, context, res, subItem);
         }
         reader.moveUp();
      }
      return res;
   }

   protected void retrieveHTTPItem(HierarchicalStreamReader reader,
         UnmarshallingContext context, HTTPSampleResult res, Object subItem)
   {
      if (subItem instanceof URL)
      {
         res.setURL((URL) subItem);
      }
      else if (reader.getNodeName().equals("cookies"))
      {
         res.setCookies((String) subItem);
      }
      else if (reader.getNodeName().equals("method"))
      {
         res.setHTTPMethod((String) subItem);
      }
      else if (reader.getNodeName().equals("queryString"))
      {
         res.setQueryString((String) subItem);
      }
      else if (reader.getNodeName().equals("redirectLocation"))
      {
         res.setRedirectLocation((String) subItem);
      }
   }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12825.java