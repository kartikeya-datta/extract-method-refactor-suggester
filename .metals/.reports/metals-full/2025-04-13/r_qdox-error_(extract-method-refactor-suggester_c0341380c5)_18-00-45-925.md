error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3350.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3350.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3350.java
text:
```scala
v@@alue = new ArrayList(Arrays.asList( (Object[])value ));

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

package org.apache.solr.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


/**
 * A concrete representation of a document within a Solr index.  Unlike a lucene
 * Document, a SolrDocument may have an Object value matching the type defined in
 * schema.xml
 * 
 * For indexing documents, use the SolrInputDocumet that contains extra information
 * for document and field boosting.
 * 
 * @version $Id$
 * @since solr 1.3
 */
public class SolrDocument implements Serializable, Iterable<Map.Entry<String, Object>>
{
  private Map<String,Object> _fields = null;
  
  public SolrDocument()
  {
    _fields = new HashMap<String,Object>();
  }

  /**
   * @return a list of fields defined in this document
   */
  public Collection<String> getFieldNames() {
    return _fields.keySet();
  }

  ///////////////////////////////////////////////////////////////////
  // Add / Set / Remove Fields
  ///////////////////////////////////////////////////////////////////

  /**
   * Remove all fields from the document
   */
  public void clear()
  {
    _fields.clear();
  }
  
  /**
   * Remove all fields with the name
   */
  public boolean removeFields(String name) 
  {
    return _fields.remove( name ) != null;
  }

  /**
   * Set a field with the given object.  If the object is an Array, it will 
   * set multiple fields with the included contents.  This will replace any existing 
   * field with the given name
   */
  @SuppressWarnings("unchecked")
  public void setField(String name, Object value) 
  {
    if( value instanceof Object[] ) {
      value = Arrays.asList( (Object[])value );
    }
    else if( value instanceof Collection ) {
      // nothing
    }
    else if( value instanceof Iterable ) {
      ArrayList<Object> lst = new ArrayList<Object>();
      for( Object o : (Iterable)value ) {
        lst.add( o );
      }
      value = lst;
    }
    _fields.put(name, value);
  }
  
  /**
   * This will add a field to the document.  If fields already exist with this name
   * it will append the collection
   */
  @SuppressWarnings("unchecked")
  public void addField(String name, Object value) 
  { 
    Object existing = _fields.get(name);
    if (existing == null) {
      this.setField( name, value );
      return;
    }
    
    Collection<Object> vals = null;
    if( existing instanceof Collection ) {
      vals = (Collection<Object>)existing;
    }
    else {
      vals = new ArrayList<Object>( 3 );
      vals.add( existing );
    }
    
    // Add the values to the collection
    if( value instanceof Iterable ) {
      for( Object o : (Iterable<Object>)value ) {
        vals.add( o );
      }
    }
    else if( value instanceof Object[] ) {
      for( Object o : (Object[])value ) {
        vals.add( o );
      }
    }
    else {
      vals.add( value );
    }
    _fields.put( name, vals );
  }

  ///////////////////////////////////////////////////////////////////
  // Get the field values
  ///////////////////////////////////////////////////////////////////

  /**
   * returns the first value for a field
   */
  public Object getFirstValue(String name) {
    Object v = _fields.get( name );
    if (v == null || !(v instanceof Collection)) return v;
    Collection c = (Collection)v;
    if (c.size() > 0 ) {
      return c.iterator().next();
    }
    return null;
  }
  
  /**
   * Get the value or collection of values for a given field.  
   */
  public Object getFieldValue(String name) {
    return _fields.get( name );
  }

  /**
   * Get a collection of values for a given field name
   */
  @SuppressWarnings("unchecked")
  public Collection<Object> getFieldValues(String name) {
    Object v = _fields.get( name );
    if( v instanceof Collection ) {
      return (Collection<Object>)v;
    }
    if( v != null ) {
      ArrayList<Object> arr = new ArrayList<Object>(1);
      arr.add( v );
      return arr;
    }
    return null;
  }
    
  @Override
  public String toString()
  {
    return "SolrDocument["+_fields.toString()+"]";
  }

  /**
   * Iterate of String->Object keys
   */
  public Iterator<Entry<String, Object>> iterator() {
    return _fields.entrySet().iterator();
  }
  
  //-----------------------------------------------------------------------------------------
  // JSTL Helpers
  //-----------------------------------------------------------------------------------------
  
  /**
   * Expose a Map interface to the solr field value collection.
   */
  public Map<String,Collection<Object>> getFieldValuesMap()
  {
    return new Map<String,Collection<Object>>() {
      /** Get the field Value */
      public Collection<Object> get(Object key) { 
        return getFieldValues( (String)key ); 
      }
      
      // Easily Supported methods
      public boolean containsKey(Object key) { return _fields.containsKey( key ); }
      public Set<String>  keySet()           { return _fields.keySet();  }
      public int          size()             { return _fields.size();    }
      public boolean      isEmpty()          { return _fields.isEmpty(); }

      // Unsupported operations.  These are not necessary for JSTL
      public void clear() { throw new UnsupportedOperationException(); }
      public boolean containsValue(Object value) {throw new UnsupportedOperationException();}
      public Set<java.util.Map.Entry<String, Collection<Object>>> entrySet() {throw new UnsupportedOperationException();}
      public void putAll(Map<? extends String, ? extends Collection<Object>> t) {throw new UnsupportedOperationException();}
      public Collection<Collection<Object>> values() {throw new UnsupportedOperationException();}
      public Collection<Object> put(String key, Collection<Object> value) {throw new UnsupportedOperationException();}
      public Collection<Object> remove(Object key) {throw new UnsupportedOperationException();}      
    };
  }

  /**
   * Expose a Map interface to the solr fields.  This function is useful for JSTL
   */
  public Map<String,Object> getFieldValueMap() {
    return new Map<String,Object>() {
      /** Get the field Value */
      public Object get(Object key) { 
        return getFirstValue( (String)key ); 
      }
      
      // Easily Supported methods
      public boolean containsKey(Object key) { return _fields.containsKey( key ); }
      public Set<String>  keySet()           { return _fields.keySet();  }
      public int          size()             { return _fields.size();    }
      public boolean      isEmpty()          { return _fields.isEmpty(); }

      // Unsupported operations.  These are not necessary for JSTL
      public void clear() { throw new UnsupportedOperationException(); }
      public boolean containsValue(Object value) {throw new UnsupportedOperationException();}
      public Set<java.util.Map.Entry<String, Object>> entrySet() {throw new UnsupportedOperationException();}
      public void putAll(Map<? extends String, ? extends Object> t) {throw new UnsupportedOperationException();}
      public Collection<Object> values() {throw new UnsupportedOperationException();}
      public Collection<Object> put(String key, Object value) {throw new UnsupportedOperationException();}
      public Collection<Object> remove(Object key) {throw new UnsupportedOperationException();}      
   };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3350.java