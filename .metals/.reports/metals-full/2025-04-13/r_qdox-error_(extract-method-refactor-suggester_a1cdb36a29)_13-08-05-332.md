error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16177.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16177.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16177.java
text:
```scala
public M@@ap<String,Object> toProperties(boolean storeDefaults);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.lib.conf;

import java.beans.BeanInfo;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.log.LogFactory;
import org.apache.openjpa.lib.util.Closeable;

/**
 * Interface for generic configuration objects. Includes the ability
 * to write configuration to and from {@link Properties} instances. Instances
 * are threadsafe for reads, but not for writes.
 *
 * @author Marc Prud'hommeaux
 * @author Abe White
 */
public interface Configuration
    extends BeanInfo, Serializable, Closeable, Cloneable {

    /**
     * Attribute of returned {@link Value} property descriptors listing
     * recognized values for the property.
     */
    public static final String ATTRIBUTE_ALLOWED_VALUES = "allowedValues";

    /**
     * Attribute of the returned {@link Value} property descriptors naming
     * the property's type or category.
     */
    public static final String ATTRIBUTE_TYPE = "propertyType";

    /**
     * Attribute of the returned {@link Value} property descriptors naming
     * the property' hierarchical category.
     */
    public static final String ATTRIBUTE_CATEGORY = "propertyCategory";

    /**
     * Attribute of the returned {@link Value} property descriptors naming
     * the property's ordering in its category.
     */
    public static final String ATTRIBUTE_ORDER = "propertyCategoryOrder";

    /**
     * Attribute of the returned {@link Value} property descriptors naming
     * the interface that plugin values for this property must implement.
     */
    public static final String ATTRIBUTE_INTERFACE = "propertyInterface";

    /**
     * Attribute of the returned {@link Value} property descriptors naming
     * the property's name in XML format (i.e. two-words instead of TwoWords).
     */
    public static final String ATTRIBUTE_XML = "xmlName";
    
    public static final int INIT_STATE_LIQUID   = 0;
    public static final int INIT_STATE_FREEZING = 1;
    public static final int INIT_STATE_FROZEN   = 2;
    

    /**
     * Return the product name. Defaults to <code>openjpa</code>.
     */
    public String getProductName();
    
    /**
     * Set the product name.
     */
    public void setProductName(String name);

    /**
     * The log factory. If no log factory has been set explicitly,
     * this method will create one.
     */
    public LogFactory getLogFactory();

    /**
     * The log factory.
     */
    public void setLogFactory(LogFactory factory);

    /**
     * Log plugin setting.
     */
    public String getLog();

    /**
     * Log plugin setting.
     */
    public void setLog(String log);

    /**
     * Return the log for the given category.
     *
     * @see #getLogFactory
     */
    public Log getLog(String category);

    /**
     * Return the log to use for configuration messages.
     */
    public Log getConfigurationLog();

    
    /**
     * An environment-specific identifier for this configuration. This 
     * might correspond to a JPA persistence-unit name, or to some other
     * more-unique value available in the current environment.
     * 
     * @since 0.9.7
     */
    public String getId();

    /**
     * An environment-specific identifier for this configuration. This 
     * might correspond to a JPA persistence-unit name, or to some other
     * more-unique value available in the current environment.
     * 
     * @since 0.9.7
     */
    public void setId(String id);

    /**
     * Return the {@link Value} for the given property, or null if none.
     */
    public Value getValue(String property);

    /**
     * Return the set of all {@link Value}s.
     */
    public Value[] getValues();

    /**
     * Add the given value to the set of configuration properties. This
     * method replaces any existing value under the same property.
     */
    public Value addValue(Value val);

    /**
     * Remove the given value from the set of configuration properties.
     */
    public boolean removeValue(Value val);
    
    /**
     * A properties representation of this Configuration.
     * Note that changes made to this properties object will
     * not be automatically reflected in this Configuration object.
     *
     * @param storeDefaults if true, then properties will be written
     * out even if they match the default value for a property
     */
    public Map toProperties(boolean storeDefaults);
    
    /**
     * Get the set of all known property keys, including any equivalent keys,
     * appropriately prefixed.
     * 
     * @param propertyName the name of the property for which the keys are
     * to be retrieved.
     * 
     * @since 2.0.0
     */
    public List<String> getPropertyKeys(String propertyName);
    
    /**
     * Get the set of all known property keys, including any equivalent keys,
     * appropriately prefixed.
     * 
     * @since 2.0.0
     */
    public Set<String> getPropertyKeys();

    /**
     * Set this Configuration via the given map. Any keys missing from
     * the given map will not be set. Note that changes made to this map
     * will not be automatically reflected in this Configuration object.
     * IMPORTANT: If the map contains instantiated objects(rather than
     * string values), only the string representation of those objects
     * are considered in this configuration's <code>equals</code> and
     * <code>hashCode</code> methods. If the object's property has no
     * string form(such as an {@link ObjectValue}), the object is not
     * part of the equality and hashing calculations.
     */
    public void fromProperties(Map map);

    /**
     * Adds a listener for any property changes. The property events fired
     * will <b>not</b> include the old value.
     *
     * @param listener the listener to receive notification of property changes
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes a listener for any property changes.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Lock down the configuration's state. Attempting to set state on a
     * read-only configuration results in an exception.
     */
    public void setReadOnly(int readOnly);

    /**
     * Return true if this configuration is immutable.
     */
    public boolean isReadOnly();

    /**
     * Call the instantiating get methods for all values. Up-front
     * instantiation allows one to avoid the synchronization necessary with
     * lazy instantiation.
     */
    public void instantiateAll();

    /**
     * Free the resources used by this object.
     */
    public void close();

    /**
     * Return a copy of this configuration.
     */
    public Object clone();
    
    /**
     * Modifies a <em>dynamic</em> property of this receiver even when 
     * {@link #setReadOnly(boolean) frozen}. 
     *
     * @since 1.0.0
     */
//    public void modifyDynamic(String property, Object newValue);
//    
//    /**
//     * Affirms if the given property can be modified <em>dynamically</em> i.e.
//     * even after the receiver is {@link #setReadOnly(boolean) frozen}. 
//     *
//     * @since 1.0.0
//     */
//    public boolean isDynamic(String property);
//    
//    /**
//     * Gets the values that can be modified <em>dynamically</em> i.e.
//     * even after the receiver is {@link #setReadOnly(boolean) frozen}. 
//     *
//     * @since 1.0.0
//     */
//    public Value[] getDynamicValues();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16177.java