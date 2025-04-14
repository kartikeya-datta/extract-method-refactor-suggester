error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2897.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2897.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2897.java
text:
```scala
c@@loudDesc.setCollectionName(name.isEmpty() ? coreContainer.getDefaultCoreName() : name);

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.core;

import java.util.Properties;
import java.io.File;

import org.apache.solr.cloud.CloudDescriptor;

/**
 * A Solr core descriptor
 *
 * @since solr 1.3
 */
public class CoreDescriptor {
  protected String name;
  protected String instanceDir;
  protected String dataDir;
  protected String configName;
  protected String propertiesName;
  protected String schemaName;
  private final CoreContainer coreContainer;
  private Properties coreProperties;
  
  private CloudDescriptor cloudDesc;

  public CoreDescriptor(CoreContainer coreContainer, String name, String instanceDir) {
    this.coreContainer = coreContainer;
    this.name = name;
    
    if(coreContainer.getZkController() != null) {
      this.cloudDesc = new CloudDescriptor();
      // cloud collection defaults to core name
      cloudDesc.setCollectionName(name == "" ? coreContainer.getDefaultCoreName() : name);
      this.cloudDesc.setShardId(coreContainer.getZkController().getNodeName() + "_" + name);
    }
    
    if (name == null) {
      throw new RuntimeException("Core needs a name");
    }
    if (instanceDir == null) {
      throw new NullPointerException("Missing required \'instanceDir\'");
    }
    instanceDir = SolrResourceLoader.normalizeDir(instanceDir);
    this.instanceDir = instanceDir;
    this.configName = getDefaultConfigName();
    this.schemaName = getDefaultSchemaName();
  }

  public CoreDescriptor(CoreDescriptor descr) {
    this.instanceDir = descr.instanceDir;
    this.configName = descr.configName;
    this.schemaName = descr.schemaName;
    this.name = descr.name;
    this.dataDir = descr.dataDir;
    coreContainer = descr.coreContainer;
  }

  private Properties initImplicitProperties() {
    Properties implicitProperties = new Properties(coreContainer.getContainerProperties());
    implicitProperties.setProperty("solr.core.name", name);
    implicitProperties.setProperty("solr.core.instanceDir", instanceDir);
    implicitProperties.setProperty("solr.core.dataDir", getDataDir());
    implicitProperties.setProperty("solr.core.configName", configName);
    implicitProperties.setProperty("solr.core.schemaName", schemaName);
    return implicitProperties;
  }

  /**@return the default config name. */
  public String getDefaultConfigName() {
    return "solrconfig.xml";
  }

  /**@return the default schema name. */
  public String getDefaultSchemaName() {
    return "schema.xml";
  }

  /**@return the default data directory. */
  public String getDefaultDataDir() {
    return "data" + File.separator;
  }

  public String getPropertiesName() {
    return propertiesName;
  }

  public void setPropertiesName(String propertiesName) {
    this.propertiesName = propertiesName;
  }

  public String getDataDir() {
    String dataDir = this.dataDir;
    if (dataDir == null) dataDir = getDefaultDataDir();
    if (new File(dataDir).isAbsolute()) {
      return dataDir;
    } else {
      if (new File(instanceDir).isAbsolute()) {
        return SolrResourceLoader.normalizeDir(SolrResourceLoader.normalizeDir(instanceDir) + dataDir);
      } else  {
        return SolrResourceLoader.normalizeDir(coreContainer.getSolrHome() +
                SolrResourceLoader.normalizeDir(instanceDir) + dataDir);
      }
    }
  }

  public void setDataDir(String s) {
    dataDir = s;
    // normalize zero length to null.
    if (dataDir != null && dataDir.length()==0) dataDir=null;
  }
  
  public boolean usingDefaultDataDir() {
    return this.dataDir == null;
  }

  /**@return the core instance directory. */
  public String getInstanceDir() {
    return instanceDir;
  }

  /**Sets the core configuration resource name. */
  public void setConfigName(String name) {
    if (name == null || name.length() == 0)
      throw new IllegalArgumentException("name can not be null or empty");
    this.configName = name;
  }

  /**@return the core configuration resource name. */
  public String getConfigName() {
    return this.configName;
  }

  /**Sets the core schema resource name. */
  public void setSchemaName(String name) {
    if (name == null || name.length() == 0)
      throw new IllegalArgumentException("name can not be null or empty");
    this.schemaName = name;
  }

  /**@return the core schema resource name. */
  public String getSchemaName() {
    return this.schemaName;
  }

  /**@return the initial core name */
  public String getName() {
    return this.name;
  }

  public CoreContainer getCoreContainer() {
    return coreContainer;
  }

  Properties getCoreProperties() {
    return coreProperties;
  }

  /**
   * Set this core's properties. Please note that some implicit values will be added to the
   * Properties instance passed into this method. This means that the Properties instance
   * set to this method will have different (less) key/value pairs than the Properties
   * instance returned by #getCoreProperties method.
   * 
   * @param coreProperties
   */
  public void setCoreProperties(Properties coreProperties) {
    if (this.coreProperties == null) {
      Properties p = initImplicitProperties();
      this.coreProperties = new Properties(p);
      if(coreProperties != null)
        this.coreProperties.putAll(coreProperties);
    }
  }

  public CloudDescriptor getCloudDescriptor() {
    return cloudDesc;
  }
  
  public void setCloudDescriptor(CloudDescriptor cloudDesc) {
    this.cloudDesc = cloudDesc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2897.java