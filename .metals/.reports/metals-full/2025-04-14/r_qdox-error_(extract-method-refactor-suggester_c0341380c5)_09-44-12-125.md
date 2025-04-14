error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4260.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4260.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4260.java
text:
```scala
r@@es.setResponseData(("Can't support the char set: "

/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.io.UnsupportedEncodingException;

import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.Converter;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import com.thoughtworks.xstream.alias.ClassMapper;
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
public class SampleResultConverter extends AbstractCollectionConverter
{
   private static Logger log = LoggingManager.getLoggerForClass();

   /**
    * Returns the converter version; used to check for possible
    * incompatibilities
    */
   public static String getVersion()
   {
      return "$Revision$";
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
    */
   public boolean canConvert(Class arg0)
   {
      return SampleResult.class.equals(arg0);
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
      SampleResult res = (SampleResult) obj;
      SampleSaveConfiguration save = res.getSaveConfig();
      setAttributes(writer, res, save);
      saveAssertions(writer, context, res, save);
      saveSubResults(writer, context, res, save);
      saveResponseHeaders(writer, res, save);
      saveRequestHeaders(writer, res, save);
      saveResponseData(writer, res, save);
      saveSamplerData(writer, res, save);
   }

   /**
    * @param writer
    * @param res
    * @param save
    */
   protected void saveSamplerData(HierarchicalStreamWriter writer,
         SampleResult res, SampleSaveConfiguration save)
   {
      if (save.saveSamplerData())
      {
         writeString(writer, "samplerData", res.getSamplerData());
      }
   }

   /**
    * @param writer
    * @param res
    * @param save
    */
   protected void saveResponseData(HierarchicalStreamWriter writer,
         SampleResult res, SampleSaveConfiguration save)
   {
      if (save.saveResponseData())
      {
         writer.startNode("responseData");
         try
         {
            writer.addAttribute("class", "java.lang.String");
            writer.setValue(new String(res.getResponseData(), res
                  .getDataEncoding()));
         }
         catch (UnsupportedEncodingException e)
         {
            writer
                  .setValue("Unsupported encoding in response data, can't record.");
         }
         writer.endNode();
      }
   }

   /**
    * @param writer
    * @param res
    * @param save
    */
   protected void saveRequestHeaders(HierarchicalStreamWriter writer,
         SampleResult res, SampleSaveConfiguration save)
   {
      if (save.saveRequestHeaders())
      {
         writeString(writer, "requestHeader", res.getRequestHeaders());
      }
   }

   /**
    * @param writer
    * @param res
    * @param save
    */
   protected void saveResponseHeaders(HierarchicalStreamWriter writer,
         SampleResult res, SampleSaveConfiguration save)
   {
      if (save.saveResponseHeaders())
      {
         writeString(writer, "responseHeader", res.getResponseHeaders());
      }
   }

   /**
    * @param writer
    * @param context
    * @param res
    * @param save
    */
   protected void saveSubResults(HierarchicalStreamWriter writer,
         MarshallingContext context, SampleResult res,
         SampleSaveConfiguration save)
   {
      if (save.saveSubresults())
      {
         SampleResult[] subResults = res.getSubResults();
         for (int i = 0; i < subResults.length; i++)
         {
            subResults[i].setSaveConfig(save);
            writeItem(subResults[i], context, writer);
         }
      }
   }

   /**
    * @param writer
    * @param context
    * @param res
    * @param save
    */
   protected void saveAssertions(HierarchicalStreamWriter writer,
         MarshallingContext context, SampleResult res,
         SampleSaveConfiguration save)
   {
      if (save.saveAssertions())
      {
         AssertionResult[] assertionResults = res.getAssertionResults();
         for (int i = 0; i < assertionResults.length; i++)
         {
            writeItem(assertionResults[i], context, writer);
         }
      }
   }

   /**
    * @param writer
    * @param res
    * @param save
    */
   protected void setAttributes(HierarchicalStreamWriter writer,
         SampleResult res, SampleSaveConfiguration save)
   {
      if (save.saveTime())
            writer.addAttribute("t", Long.toString(res.getTime()));
      if (save.saveLatency())
            writer.addAttribute("lt", Long.toString(res.getLatency()));
      if (save.saveTimestamp())
            writer.addAttribute("ts", Long.toString(res.getTimeStamp()));
      if (save.saveSuccess())
            writer.addAttribute("s", //JDK1.4 Boolean.toString(res.isSuccessful()));
            		JOrphanUtils.booleanToString(res.isSuccessful()));
      if (save.saveLabel())
            writer.addAttribute("lb", ConversionHelp.encode(res
                  .getSampleLabel()));
      if (save.saveCode())
            writer.addAttribute("rs", ConversionHelp.encode(res
                  .getResponseCode()));
      if (save.saveMessage())
            writer.addAttribute("rm", ConversionHelp.encode(res
                  .getResponseMessage()));
      if (save.saveThreadName())
            writer.addAttribute("tn", ConversionHelp
                  .encode(res.getThreadName()));
      if (save.saveDataType())
            writer.addAttribute("dt", ConversionHelp.encode(res.getDataType()));
      if (save.saveEncoding())
            writer.addAttribute("de", ConversionHelp.encode(res
                  .getDataEncoding()));
   }

   /**
    * @param writer
    * @param hres
    */
   protected void writeString(HierarchicalStreamWriter writer, String tag,
         String value)
   {
      if(value != null)
      {
	      writer.startNode(tag);
	      writer.addAttribute("class", "java.lang.String");
	      writer.setValue(value);
	      writer.endNode();
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
      SampleResult res = (SampleResult) createCollection(context
            .getRequiredType());
      retrieveAttributes(reader, res);
      while (reader.hasMoreChildren())
      {
         reader.moveDown();
         Object subItem = readItem(reader, context, res);
         retrieveItem(reader, context, res, subItem);
         reader.moveUp();
      }
      return res;
   }

   /**
    * @param reader
    * @param context
    * @param res
    */
   protected boolean retrieveItem(HierarchicalStreamReader reader,
         UnmarshallingContext context, SampleResult res, Object subItem)
   {
      if (subItem instanceof AssertionResult)
      {
         res.addAssertionResult((AssertionResult) subItem);
      }
      else if (subItem instanceof SampleResult)
      {
         res.addSubResult((SampleResult) subItem);
      }
      else if (reader.getNodeName().equals("responseHeader"))
      {
         res.setResponseHeaders((String) subItem);
      }
      else if (reader.getNodeName().equals("requestHeader"))
      {
         res.setRequestHeaders((String) subItem);
      }
      else if (reader.getNodeName().equals("responseData"))
      {
         try
         {
            res.setResponseData(((String) subItem).getBytes(res
                  .getDataEncoding()));
         }
         catch (UnsupportedEncodingException e)
         {
            res.setResponseData(new String("Can't support the char set: "
                  + res.getDataEncoding()).getBytes());
         }
      }
      else if (reader.getNodeName().equals("samplerData"))
      {
         res.setSamplerData((String) subItem);
      }
      else
      {
         return false;
      }
      return true;
   }

   /**
    * @param reader
    * @param res
    */
   protected void retrieveAttributes(HierarchicalStreamReader reader,
         SampleResult res)
   {
      res.setSampleLabel(ConversionHelp.decode(reader.getAttribute("lb")));
      res.setDataEncoding(ConversionHelp.decode(reader.getAttribute("de")));
      res.setDataType(ConversionHelp.decode(reader.getAttribute("dt")));
      res.setResponseCode(ConversionHelp.decode(reader.getAttribute("rc")));
      res.setResponseMessage(ConversionHelp.decode(reader.getAttribute("rm")));
      res.setSuccessful(Converter.getBoolean(reader.getAttribute("s"), true));
      res.setThreadName(ConversionHelp.decode(reader.getAttribute("tn")));
      res.setTime(Converter.getLong(reader.getAttribute("t")));
      res.setTimeStamp(Converter.getLong(reader.getAttribute("ts")));
      res.setLatency(Converter.getLong(reader.getAttribute("lt")));
   }

   /**
    * @param arg0
    * @param arg1
    */
   public SampleResultConverter(ClassMapper arg0, String arg1)
   {
      super(arg0, arg1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4260.java