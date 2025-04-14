error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8946.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8946.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8946.java
text:
```scala
l@@og.warn("Problem loading new style: "+e.getLocalizedMessage());

/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

package org.apache.jmeter.save;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.save.converters.BooleanPropertyConverter;
import org.apache.jmeter.save.converters.HashTreeConverter;
import org.apache.jmeter.save.converters.IntegerPropertyConverter;
import org.apache.jmeter.save.converters.LongPropertyConverter;
import org.apache.jmeter.save.converters.MultiPropertyConverter;
import org.apache.jmeter.save.converters.SampleResultConverter;
import org.apache.jmeter.save.converters.StringPropertyConverter;
import org.apache.jmeter.save.converters.TestElementConverter;
import org.apache.jmeter.save.converters.TestElementPropertyConverter;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.alias.CannotResolveClassException;
import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.Converter;

/**
 * @author Mike Stover
 * @author <a href="mailto:kcassell&#X0040;apache.org">Keith Cassell </a>
 */
public class SaveService
{
   private static XStream saver = new XStream();
   private static Logger log = LoggingManager.getLoggerForClass();

   // Version information for test plan header
   static String version = "1.0";
   static String propertiesVersion = "";//read from properties file
   private static final String PROPVERSION = "1.6";

   // Helper method to simplify alias creation from properties
   private static void makeAlias(String alias, String clazz)
   {
      try
      {
         saver.alias(alias, Class.forName(clazz));
      }
      catch (ClassNotFoundException e)
      {
         log.warn("Could not set up alias " + alias + " " + e.toString());
      }
   }

   private static void initProps()
   {
      // Load the alias properties
      Properties nameMap = new Properties();
      try
      {
         nameMap.load(new FileInputStream(JMeterUtils.getJMeterHome()
               + JMeterUtils.getPropDefault("saveservice_properties",
                     "/bin/saveservice.properties")));
         // now create the aliases
         Iterator it = nameMap.entrySet().iterator();
         while (it.hasNext())
         {
            Map.Entry me = (Map.Entry) it.next();
            String key = (String) me.getKey();
            String val = (String) me.getValue();
            if (!key.startsWith("_"))
            {
               makeAlias(key, val);
            }
            else
            {
               //process special keys
               if (key.equalsIgnoreCase("_version"))
               {
                  val = extractVersion(val);
                  log.info("Using SaveService properties file " + val);
                  propertiesVersion = val;
               }
               else
               {
                  key = key.substring(1);
                  try
                  {
                     if (val.trim().equals("collection"))
                     {
                        saver
                              .registerConverter((Converter)Class.forName(key)
                                    .getConstructor(
                                          new Class[] { ClassMapper.class,
                                                String.class}).newInstance(
                                          new Object[] { saver.getClassMapper(),
                                                "class"}));
                     }
                     else if(val.trim().equals("mapping"))
                     {
                        saver
                        .registerConverter((Converter)Class.forName(key)
                              .getConstructor(
                                    new Class[] { ClassMapper.class}).newInstance(
                                    new Object[] { saver.getClassMapper()}));
                     }
                     else
                     {
                        saver.registerConverter((Converter)Class.forName(key).newInstance());
                     }
                  }
                  catch (Exception e1)
                  {
                     log.warn("Can't register a converter: " + key,e1);
                  }
               }
            }
         }
      }
      catch (Exception e)
      {
         log.error("Bad saveservice properties file", e);
      }
   }

   static
   {
      initProps();
      /*saver.registerConverter(new StringPropertyConverter());
      saver.registerConverter(new BooleanPropertyConverter());
      saver.registerConverter(new IntegerPropertyConverter());
      saver.registerConverter(new LongPropertyConverter());
      saver.registerConverter(new TestElementConverter(saver.getClassMapper(),
            "class"));
      saver.registerConverter(new MultiPropertyConverter(
            saver.getClassMapper(), "class"));
      saver.registerConverter(new TestElementPropertyConverter(saver
            .getClassMapper(), "class"));
      saver.registerConverter(new HashTreeConverter(saver.getClassMapper(),
            "class"));
      saver
            .registerConverter(new ScriptWrapperConverter(saver
                  .getClassMapper()));
      saver.registerConverter(new SampleResultConverter(saver.getClassMapper(),
            "class"));
      saver.registerConverter(new TestResultWrapperConverter(saver
            .getClassMapper(), "class"));*/
      checkVersions();
   }

   public static void saveTree(HashTree tree, Writer writer) throws Exception
   {
      ScriptWrapper wrapper = new ScriptWrapper();
      wrapper.testPlan = tree;
      saver.toXML(wrapper, writer);
   }
   
   public static void saveElement(Object el,Writer writer) throws Exception
   {
       saver.toXML(el,writer);
   }
   
   public static Object loadElement(InputStream in) throws Exception
   {
       return saver.fromXML(new InputStreamReader(in));
   }
   
   public static Object loadElement(Reader in) throws Exception
   {
       return saver.fromXML(in);
   }

   public synchronized static void saveSampleResult(SampleResult res,
         Writer writer) throws Exception
   {
      saver.toXML(res, writer);
      writer.write('\n');
   }

   public synchronized static void saveTestElement(TestElement elem,
        Writer writer) throws Exception
   {
      saver.toXML(elem,writer);
      writer.write('\n');  	
   }
        
   static boolean versionsOK = true;

   // Extract version digits from String of the form #Revision: n.mm #
   // (where # is actually $ above)
   private static final String REVPFX = "$Revision: ";
   private static final String REVSFX = " $";

   private static String extractVersion(String rev)
   {
      if (rev.length() > REVPFX.length() + REVSFX.length())
      {
         return rev.substring(REVPFX.length(), rev.length() - REVSFX.length());
      }
      else
      {
         return rev;
      }
   }

   private static void checkVersion(Class clazz, String expected)
   {

      String actual = "*NONE*";
      try
      {
         actual = (String) clazz.getMethod("getVersion", null).invoke(null,
               null);
         actual = extractVersion(actual);
      }
      catch (Exception e)
      {
         //Not needed
      }
      if (0 != actual.compareTo(expected))
      {
         versionsOK = false;
         log.warn("Version mismatch: expected '" + expected + "' found '"
               + actual + "' in " + clazz.getName());
      }
   }

   private static void checkVersions()
   {
      versionsOK = true;
      checkVersion(BooleanPropertyConverter.class, "1.4");
      checkVersion(HashTreeConverter.class, "1.2");
      checkVersion(IntegerPropertyConverter.class, "1.3");
      checkVersion(LongPropertyConverter.class, "1.3");
      checkVersion(MultiPropertyConverter.class, "1.3");
      checkVersion(SampleResultConverter.class, "1.5");
      checkVersion(StringPropertyConverter.class, "1.6");
      checkVersion(TestElementConverter.class, "1.2");
      checkVersion(TestElementPropertyConverter.class, "1.3");
      checkVersion(ScriptWrapperConverter.class, "1.3");
      if (!PROPVERSION.equalsIgnoreCase(propertiesVersion))
      {
         log.warn("Property file - expected " + PROPVERSION + ", found "
               + propertiesVersion);
      }
      if (versionsOK)
      {
         log.info("All converter versions present and correct");
      }
   }

   public static TestResultWrapper loadTestResults(InputStream reader)
         throws Exception
   {
      TestResultWrapper wrapper = (TestResultWrapper) saver
            .fromXML(new InputStreamReader(reader));
      return wrapper;
   }

   public static HashTree loadTree(InputStream reader) throws Exception
   {
      if (!reader.markSupported())
      {
         reader = new BufferedInputStream(reader);
      }
      reader.mark(Integer.MAX_VALUE);
      ScriptWrapper wrapper = null;
      try
      {
         wrapper = (ScriptWrapper) saver.fromXML(new InputStreamReader(reader));
         return wrapper.testPlan;
      }
      catch (CannotResolveClassException e)
      {
         log.warn("Problem loading new style: ", e);
         reader.reset();
         return OldSaveService.loadSubTree(reader);
      }
   }

   public static class Test extends JMeterTestCase
   {
      public Test()
      {
         super();
      }

      public Test(String name)
      {
         super(name);
      }

      public void testVersions() throws Exception
      {
         initProps();
         checkVersions();
         assertTrue("Unexpected version found", versionsOK);
         assertEquals("Property Version mismatch", PROPVERSION,
               propertiesVersion);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8946.java