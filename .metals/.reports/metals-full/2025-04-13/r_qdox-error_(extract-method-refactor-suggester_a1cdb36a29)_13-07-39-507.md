error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10450.java
text:
```scala
r@@eturn newName;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
 * Created on Jul 27, 2004
 */
package org.apache.jmeter.save.converters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.NameUpdater;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Utility conversion routines for use with XStream
 *
 */
public class ConversionHelp {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String CHAR_SET = "UTF-8"; //$NON-NLS-1$

    // Attributes for TestElement and TestElementProperty
    // Must all be unique
    public static final String ATT_CLASS         = "class"; //$NON-NLS-1$
    // Also used by PropertyConverter classes
    public static final String ATT_NAME          = "name"; // $NON-NLS-1$
    public static final String ATT_ELEMENT_TYPE  = "elementType"; // $NON-NLS-1$

    private static final String ATT_TE_ENABLED   = "enabled"; //$NON-NLS-1$
    private static final String ATT_TE_TESTCLASS = "testclass"; //$NON-NLS-1$
            static final String ATT_TE_GUICLASS  = "guiclass"; //$NON-NLS-1$
    private static final String ATT_TE_NAME      = "testname"; //$NON-NLS-1$


    /*
     * These must be set before reading/writing the XML. Rather a hack, but
     * saves changing all the method calls to include an extra variable.
     *
     * AFAIK the variables should only be accessed from one thread, so no need to synchronize.
     */
    private static String inVersion;

    private static String outVersion = "1.1"; // Default for writing//$NON-NLS-1$

    public static void setInVersion(String v) {
        inVersion = v;
    }

    public static void setOutVersion(String v) {
        outVersion = v;
    }

    /**
     * Encode a string (if necessary) for output to a JTL file.
     * Strings are only encoded if the output version is 1.0,
     * but nulls are always converted to the empty string.
     *
     * @param p string to encode
     * @return encoded string (will never be null)
     */
    public static String encode(String p) {
        if (p == null) {// Nulls cannot be written using PrettyPrintWriter - they cause an NPE
            return ""; // $NON-NLS-1$
        }
        // Only encode strings if outVersion = 1.0
        if (!"1.0".equals(outVersion)) {//$NON-NLS-1$
            return p;
        }
        try {
            String p1 = URLEncoder.encode(p, CHAR_SET);
            return p1;
        } catch (UnsupportedEncodingException e) {
            log.warn("System doesn't support " + CHAR_SET, e);
            return p;
        }
    }

    public static String decode(String p) {
        if (!"1.0".equals(inVersion)) {//$NON-NLS-1$
            return p;
        }
        // Only decode strings if inVersion = 1.0
        if (p == null) {
            return null;
        }
        try {
            return URLDecoder.decode(p, CHAR_SET);
        } catch (UnsupportedEncodingException e) {
            log.warn("System doesn't support " + CHAR_SET, e);
            return p;
        }
    }

    public static String cdata(byte[] chars, String encoding) throws UnsupportedEncodingException {
        StringBuilder buf = new StringBuilder("<![CDATA[");
        buf.append(new String(chars, encoding));
        buf.append("]]>");
        return buf.toString();
    }

    // Names of properties that are handled specially
    private static final Map<String, String> propertyToAttribute=new HashMap<String, String>();

    private static void mapentry(String prop, String att){
        propertyToAttribute.put(prop,att);
    }

    static{
        mapentry(TestElement.NAME,ATT_TE_NAME);
        mapentry(TestElement.GUI_CLASS,ATT_TE_GUICLASS);//$NON-NLS-1$
        mapentry(TestElement.TEST_CLASS,ATT_TE_TESTCLASS);//$NON-NLS-1$
        mapentry(TestElement.ENABLED,ATT_TE_ENABLED);
    }

    private static void saveClass(TestElement el, HierarchicalStreamWriter writer, String prop){
        String clazz=el.getPropertyAsString(prop);
        if (clazz.length()>0) {
            writer.addAttribute(propertyToAttribute.get(prop),SaveService.classToAlias(clazz));
        }
    }

    private static void restoreClass(TestElement el, HierarchicalStreamReader reader, String prop) {
        String att=propertyToAttribute.get(prop);
        String alias=reader.getAttribute(att);
        if (alias!=null){
            alias=SaveService.aliasToClass(alias);
            if (TestElement.GUI_CLASS.equals(prop)) { // mainly for TestElementConverter
               alias = NameUpdater.getCurrentName(alias);
            }
            el.setProperty(prop,alias);
        }
    }

    private static void saveItem(TestElement el, HierarchicalStreamWriter writer, String prop,
            boolean encode){
        String item=el.getPropertyAsString(prop);
        if (item.length() > 0) {
            if (encode) {
                item=ConversionHelp.encode(item);
            }
            writer.addAttribute(propertyToAttribute.get(prop),item);
        }
    }

    private static void restoreItem(TestElement el, HierarchicalStreamReader reader, String prop,
            boolean decode) {
        String att=propertyToAttribute.get(prop);
        String value=reader.getAttribute(att);
        if (value!=null){
            if (decode) {
                value=ConversionHelp.decode(value);
            }
            el.setProperty(prop,value);
        }
    }

    public static boolean isSpecialProperty(String name) {
       return propertyToAttribute.containsKey(name);
    }

    /**
     * Get the property name, updating it if necessary using {@link NameUpdater}.
     * @param reader where to read the name attribute
     * @param context the unmarshalling context
     * 
     * @return the property name, may be null if the property has been deleted.
     * @see #getUpgradePropertyName(String, UnmarshallingContext)
     */
    public static String getPropertyName(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String name = ConversionHelp.decode(reader.getAttribute(ATT_NAME));
        return getUpgradePropertyName(name, context);
        
    }

    /**
     * Get the property value, updating it if necessary using {@link NameUpdater}.
     * 
     * Do not use for GUI_CLASS or TEST_CLASS.
     * 
     * @param reader where to read the value
     * @param context the unmarshalling context
     * 
     * @return the property value, updated if necessary.
     * @see #getUpgradePropertyValue(String, String, UnmarshallingContext)
     */
    public static String getPropertyValue(HierarchicalStreamReader reader, UnmarshallingContext context, String name) {
        String value = ConversionHelp.decode(reader.getValue());
        return getUpgradePropertyValue(name, value, context);
        
    }

    /**
     * Update a property name using {@link NameUpdater}.
     * @param name the original property name
     * @param context the unmarshalling context
     * 
     * @return the property name, may be null if the property has been deleted.
     */
    public static String getUpgradePropertyName(String name, UnmarshallingContext context) {
        String testClass = (String) context.get(SaveService.TEST_CLASS_NAME);
        final String newName = NameUpdater.getCurrentName(name, testClass);
        // Delete any properties whose name converts to the empty string
        if (name.length() != 0 && newName.length()==0) {
            return null;
        }
        return name;
    }

    /**
     * Update a property value using {@link NameUpdater#getCurrentName(String, String, String)}.
     * 
     * Do not use for GUI_CLASS or TEST_CLASS.
     * 
     * @param name the original property name
     * @param value the original property value
     * @param context the unmarshalling context
     * 
     * @return the property value, updated if necessary
     */
    public static String getUpgradePropertyValue(String name, String value, UnmarshallingContext context) {
        String testClass = (String) context.get(SaveService.TEST_CLASS_NAME);
        return NameUpdater.getCurrentName(value, name, testClass);
    }


    /**
     * Save the special properties:
     * <ul>
     * <li>TestElement.GUI_CLASS</li>
     * <li>TestElement.TEST_CLASS</li>
     * <li>TestElement.NAME</li>
     * <li>TestElement.ENABLED</li>
     * </ul>
     * @param testElement
     * @param writer
     */
    public static void saveSpecialProperties(TestElement testElement, HierarchicalStreamWriter writer) {
        saveClass(testElement,writer,TestElement.GUI_CLASS);
        saveClass(testElement,writer,TestElement.TEST_CLASS);
        saveItem(testElement,writer,TestElement.NAME,true);
        saveItem(testElement,writer,TestElement.ENABLED,false);
    }

    /**
     * Restore the special properties:
     * <ul>
     * <li>TestElement.GUI_CLASS</li>
     * <li>TestElement.TEST_CLASS</li>
     * <li>TestElement.NAME</li>
     * <li>TestElement.ENABLED</li>
     * </ul>
     * @param testElement
     * @param reader
     */
    public static void restoreSpecialProperties(TestElement testElement, HierarchicalStreamReader reader) {
        restoreClass(testElement,reader,TestElement.GUI_CLASS);
        restoreClass(testElement,reader,TestElement.TEST_CLASS);
        restoreItem(testElement,reader,TestElement.NAME,true);
        restoreItem(testElement,reader,TestElement.ENABLED,false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10450.java