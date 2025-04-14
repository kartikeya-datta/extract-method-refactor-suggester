error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18186.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18186.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,9]

error in qdox parser
file content:
```java
offset: 854
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18186.java
text:
```scala
@UniqueConstraint(name="\"sec unq\"",

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
package o@@rg.apache.openjpa.persistence.delimited.identifiers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="\"primary entityF\"", schema="\"delim id\"",
    uniqueConstraints=
        @UniqueConstraint(columnNames={"\"f name\"", "f_nonDelimName"}))
@SecondaryTable(name="\"secondary entityF\"", schema="\"delim id\"",
    uniqueConstraints=
        @UniqueConstraint(name="\"sec_unq\"", 
            columnNames={"\"secondary name\""}))         
public class EntityF {
    @TableGenerator(name = "f_id_gen", table = "\"f_id_gen\"", 
        schema = "\"delim id\"",
        pkColumnName = "\"gen_pk\"", valueColumnName = "\"gen_value\"")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "f_id_gen")
    @Id
    private int id;
    // Note: Delimited columnDefinition is not supported on some DBs
    // TODO: copy into a separate entity and conditionally run a different test
    @Column(name="\"f name\"", columnDefinition="char(15)")
    private String name;
    @Column(name="f_nonDelimName")
    private String nonDelimName;
    @Column(name="\"secondary name\"", table="\"secondary entityF\"")
    private String secName;
    
    @ElementCollection
    // CollectionTable with default name generation
    @CollectionTable
    private Set<String> collectionSet = new HashSet<String>();
    
    @ElementCollection
    @CollectionTable(name="\"collectionDelimSet\"", schema="\"delim id\"")
    private Set<String> collectionDelimSet = new HashSet<String>();
    
    @ElementCollection
    // MapKeyColumn with default name generation
    @MapKeyColumn
    private Map<String, String> collectionMap = new HashMap<String, String>();
    
    @ElementCollection
    // Note: Delimited column definition is not supported on some DBs, so
    // it is not delimited here
    // TODO: create a separate entity and conditionally run the test on a supported DB
    @MapKeyColumn(name="\"mapKey\"", columnDefinition="varchar(20)", table="\"delim collection map\"")
    private Map<String, String> delimCollectionMap = 
        new HashMap<String, String>();
    
    public EntityF(String name) {
        this.name = name;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the nonDelimName
     */
    public String getNonDelimName() {
        return nonDelimName;
    }

    /**
     * @param nonDelimName the nonDelimName to set
     */
    public void setNonDelimName(String nonDelimName) {
        this.nonDelimName = nonDelimName;
    }

    /**
     * @return the secName
     */
    public String getSecName() {
        return secName;
    }

    /**
     * @param secName the secName to set
     */
    public void setSecName(String secName) {
        this.secName = secName;
    }

    /**
     * @return the collectionSet
     */
    public Set<String> getCollectionSet() {
        return collectionSet;
    }

    /**
     * @param collectionSet the collectionSet to set
     */
    public void setCollectionSet(Set<String> collectionSet) {
        this.collectionSet = collectionSet;
    }
    
    public void addCollectionSet(String item) {
        collectionSet.add(item);
    }

    /**
     * @return the collectionNamedSet
     */
    public Set<String> getCollectionDelimSet() {
        return collectionDelimSet;
    }

    /**
     * @param collectionNamedSet the collectionNamedSet to set
     */
    public void setCollectionDelimSet(Set<String> collectionDelimSet) {
        this.collectionDelimSet = collectionDelimSet;
    } 
    
    public void addCollectionDelimSet(String item) {
        this.collectionDelimSet.add(item);
    }

    /**
     * @return the collectionMap
     */
    public Map<String, String> getCollectionMap() {
        return collectionMap;
    }

    /**
     * @param collectionMap the collectionMap to set
     */
    public void setCollectionMap(Map<String, String> collectionMap) {
        this.collectionMap = collectionMap;
    }

    public void addCollectionMap(String key, String value) {
        collectionMap.put(key, value);
    }

    /**
     * @return the delimCollectionMap
     */
    public Map<String, String> getDelimCollectionMap() {
        return delimCollectionMap;
    }

    /**
     * @param delimCollectionMap the delimCollectionMap to set
     */
    public void setDelimCollectionMap(Map<String, String> delimCollectionMap) {
        this.delimCollectionMap = delimCollectionMap;
    }
    
    public void addDelimCollectionMap(String key, String value) {
        delimCollectionMap.put(key, value);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18186.java