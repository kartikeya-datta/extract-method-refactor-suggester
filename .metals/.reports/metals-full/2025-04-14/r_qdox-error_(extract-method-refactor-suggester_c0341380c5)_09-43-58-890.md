error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8237.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8237.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8237.java
text:
```scala
r@@eferencedColumnName="entityI4 id", table="mk j col")

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
package org.apache.openjpa.persistence.delimited.identifiers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

@Entity
@Table(name="entity h", schema="delim id2")
@SecondaryTable(name="h sec join table", schema="delim id2",
    pkJoinColumns=@PrimaryKeyJoinColumn(name="entity h",
        referencedColumnName="h id"))
public class EntityH {
    @Id
    @Column(name="h id")
    private int id;
    private String name;
    
    @Column(table="h sec join table")
    private String secName;
    
    @ManyToMany
    @JoinTable(name="h i", schema="delim id2")
    private Collection<EntityI> entityIs = new HashSet<EntityI>();
    
    @OneToOne
    @JoinColumn(name="enti2 id", referencedColumnName="entityI2 id", table="join column")
    private EntityI2 entityI2;
    
    @ManyToMany
    @JoinTable(name="map3 join table", schema="delim id2")
    @MapKeyJoinColumn(name="map_ei3", referencedColumnName="entityI3 id")
    Map<EntityI3,EntityI4> map = new HashMap<EntityI3,EntityI4>();
    
    @ManyToMany
    @JoinTable(name="map4 join table", schema="delim id2")
    @MapKeyJoinColumn(name="map ei4", 
        referencedColumnName="entityI4 id", table="map key join column")
    Map<EntityI4,EntityI3> map2 = new HashMap<EntityI4,EntityI3>();

    public EntityH() {}
    
    public EntityH(int id) {
        this.id = id;
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
    
    public Collection<EntityI> getEntityIs() {
        return entityIs;
    }
    /**
     * @param entityIs the entityIs to set
     */
    public void setEntityIs(Collection<EntityI> entityIs) {
        this.entityIs = entityIs;
    }
    
    public void addEntityI(EntityI entityI) {
        entityIs.add(entityI);
    }

    /**
     * @return the entityI2
     */
    public EntityI2 getEntityI2() {
        return entityI2;
    }

    /**
     * @param entityI2 the entityI2 to set
     */
    public void setEntityI2(EntityI2 entityI2) {
        this.entityI2 = entityI2;
    }

    /**
     * @return the map
     */
    public Map<EntityI3, EntityI4> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<EntityI3, EntityI4> map) {
        this.map = map;
    }
    
    public void addMapValues(EntityI3 key, EntityI4 value) {
        map.put(key, value);
    }

    /**
     * @return the map2
     */
    public Map<EntityI4, EntityI3> getMap2() {
        return map2;
    }

    /**
     * @param map2 the map2 to set
     */
    public void setMap2(Map<EntityI4, EntityI3> map2) {
        this.map2 = map2;
    }
    
    public void addMap2Values(EntityI4 key, EntityI3 value) {
        map2.put(key, value);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8237.java