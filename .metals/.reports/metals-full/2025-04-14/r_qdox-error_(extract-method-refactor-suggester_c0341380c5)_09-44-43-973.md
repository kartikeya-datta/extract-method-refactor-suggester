error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/694.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/694.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/694.java
text:
```scala
S@@tring hmapKey = ignoreCase ? keyStr.toLowerCase(Locale.ENGLISH) : keyStr;

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

package org.apache.lucene.analysis;

import java.util.*;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

public class TestCharArrayMap extends LuceneTestCase {
  Random r = newRandom();

  public void doRandom(int iter, boolean ignoreCase) {
    CharArrayMap<Integer> map = new CharArrayMap<Integer>(TEST_VERSION_CURRENT, 1, ignoreCase);
    HashMap<String,Integer> hmap = new HashMap<String,Integer>();

    char[] key;
    for (int i=0; i<iter; i++) {
      int len = r.nextInt(5);
      key = new char[len];
      for (int j=0; j<key.length; j++) {
        key[j] = (char)r.nextInt(127);
      }
      String keyStr = new String(key);
      String hmapKey = ignoreCase ? keyStr.toLowerCase() : keyStr; 

      int val = r.nextInt();

      Object o1 = map.put(key, val);
      Object o2 = hmap.put(hmapKey,val);
      assertEquals(o1,o2);

      // add it again with the string method
      assertEquals(val, map.put(keyStr,val).intValue());

      assertEquals(val, map.get(key,0,key.length).intValue());
      assertEquals(val, map.get(key).intValue());
      assertEquals(val, map.get(keyStr).intValue());

      assertEquals(hmap.size(), map.size());
    }
  }

  public void testCharArrayMap() {
    for (int i=0; i<5*_TestUtil.getRandomMultiplier(); i++) {  // pump this up for more random testing
      doRandom(1000,false);
      doRandom(1000,true);      
    }
  }

  public void testMethods() {
    CharArrayMap<Integer> cm = new CharArrayMap<Integer>(TEST_VERSION_CURRENT, 2, false);
    HashMap<String,Integer> hm = new HashMap<String,Integer>();
    hm.put("foo",1);
    hm.put("bar",2);
    cm.putAll(hm);
    assertEquals(hm.size(), cm.size());
    hm.put("baz", 3);
    cm.putAll(hm);
    assertEquals(hm.size(), cm.size());

    CharArraySet cs = cm.keySet();
    int n=0;
    for (Object o : cs) {
      assertTrue(cm.containsKey(o));
      char[] co = (char[]) o;
      assertTrue(cm.containsKey(co, 0, co.length));
      n++;
    }
    assertEquals(hm.size(), n);
    assertEquals(hm.size(), cs.size());
    assertEquals(cm.size(), cs.size());
    cs.clear();
    assertEquals(0, cs.size());
    assertEquals(0, cm.size());
    try {
      cs.add("test");
      fail("keySet() allows adding new keys");
    } catch (UnsupportedOperationException ue) {
      // pass
    }
    cm.putAll(hm);
    assertEquals(hm.size(), cs.size());
    assertEquals(cm.size(), cs.size());

    Iterator<Map.Entry<Object,Integer>> iter1 = cm.entrySet().iterator();
    n=0;
    while (iter1.hasNext()) {
      Map.Entry<Object,Integer> entry = iter1.next();
      Object key = entry.getKey();
      Integer val = entry.getValue();
      assertEquals(cm.get(key), val);
      entry.setValue(val*100);
      assertEquals(val*100, (int)cm.get(key));
      n++;
    }
    assertEquals(hm.size(), n);
    cm.clear();
    cm.putAll(hm);
    assertEquals(cm.size(), n);

    CharArrayMap<Integer>.EntryIterator iter2 = cm.entrySet().iterator();
    n=0;
    while (iter2.hasNext()) {
      char[] keyc = iter2.nextKey();
      Integer val = iter2.currentValue();
      assertEquals(hm.get(new String(keyc)), val);
      iter2.setValue(val*100);
      assertEquals(val*100, (int)cm.get(keyc));
      n++;
    }
    assertEquals(hm.size(), n);

    cm.entrySet().clear();
    assertEquals(0, cm.size());
    assertEquals(0, cm.entrySet().size());
    assertTrue(cm.isEmpty());
  }

  public void testModifyOnUnmodifiable(){
    CharArrayMap<Integer> map = new CharArrayMap<Integer>(TEST_VERSION_CURRENT, 2, false);
    map.put("foo",1);
    map.put("bar",2);
    final int size = map.size();
    assertEquals(2, size);
    assertTrue(map.containsKey("foo"));  
    assertEquals(1, map.get("foo").intValue());  
    assertTrue(map.containsKey("bar"));  
    assertEquals(2, map.get("bar").intValue());  

    map = CharArrayMap.unmodifiableMap(map);
    assertEquals("Map size changed due to unmodifiableMap call" , size, map.size());
    String NOT_IN_MAP = "SirGallahad";
    assertFalse("Test String already exists in map", map.containsKey(NOT_IN_MAP));
    assertNull("Test String already exists in map", map.get(NOT_IN_MAP));
    
    try{
      map.put(NOT_IN_MAP.toCharArray(), 3);  
      fail("Modified unmodifiable map");
    }catch (UnsupportedOperationException e) {
      // expected
      assertFalse("Test String has been added to unmodifiable map", map.containsKey(NOT_IN_MAP));
      assertNull("Test String has been added to unmodifiable map", map.get(NOT_IN_MAP));
      assertEquals("Size of unmodifiable map has changed", size, map.size());
    }
    
    try{
      map.put(NOT_IN_MAP, 3);  
      fail("Modified unmodifiable map");
    }catch (UnsupportedOperationException e) {
      // expected
      assertFalse("Test String has been added to unmodifiable map", map.containsKey(NOT_IN_MAP));
      assertNull("Test String has been added to unmodifiable map", map.get(NOT_IN_MAP));
      assertEquals("Size of unmodifiable map has changed", size, map.size());
    }
    
    try{
      map.put(new StringBuilder(NOT_IN_MAP), 3);  
      fail("Modified unmodifiable map");
    }catch (UnsupportedOperationException e) {
      // expected
      assertFalse("Test String has been added to unmodifiable map", map.containsKey(NOT_IN_MAP));
      assertNull("Test String has been added to unmodifiable map", map.get(NOT_IN_MAP));
      assertEquals("Size of unmodifiable map has changed", size, map.size());
    }
    
    try{
      map.clear();  
      fail("Modified unmodifiable map");
    }catch (UnsupportedOperationException e) {
      // expected
      assertEquals("Size of unmodifiable map has changed", size, map.size());
    }
    
    try{
      map.entrySet().clear();  
      fail("Modified unmodifiable map");
    }catch (UnsupportedOperationException e) {
      // expected
      assertEquals("Size of unmodifiable map has changed", size, map.size());
    }
    
    try{
      map.keySet().clear();  
      fail("Modified unmodifiable map");
    }catch (UnsupportedOperationException e) {
      // expected
      assertEquals("Size of unmodifiable map has changed", size, map.size());
    }
    
    try{
      map.put((Object) NOT_IN_MAP, 3);  
      fail("Modified unmodifiable map");
    }catch (UnsupportedOperationException e) {
      // expected
      assertFalse("Test String has been added to unmodifiable map", map.containsKey(NOT_IN_MAP));
      assertNull("Test String has been added to unmodifiable map", map.get(NOT_IN_MAP));
      assertEquals("Size of unmodifiable map has changed", size, map.size());
    }
    
    try{
      map.putAll(Collections.singletonMap(NOT_IN_MAP, 3));  
      fail("Modified unmodifiable map");
    }catch (UnsupportedOperationException e) {
      // expected
      assertFalse("Test String has been added to unmodifiable map", map.containsKey(NOT_IN_MAP));
      assertNull("Test String has been added to unmodifiable map", map.get(NOT_IN_MAP));
      assertEquals("Size of unmodifiable map has changed", size, map.size());
    }
    
    assertTrue(map.containsKey("foo"));  
    assertEquals(1, map.get("foo").intValue());  
    assertTrue(map.containsKey("bar"));  
    assertEquals(2, map.get("bar").intValue());  
  }
  
  public void testToString() {
    CharArrayMap<Integer> cm = new CharArrayMap<Integer>(TEST_VERSION_CURRENT, Collections.singletonMap("test",1), false);
    assertEquals("[test]",cm.keySet().toString());
    assertEquals("[1]",cm.values().toString());
    assertEquals("[test=1]",cm.entrySet().toString());
    assertEquals("{test=1}",cm.toString());
    cm.put("test2", 2);
    assertTrue(cm.keySet().toString().contains(", "));
    assertTrue(cm.values().toString().contains(", "));
    assertTrue(cm.entrySet().toString().contains(", "));
    assertTrue(cm.toString().contains(", "));
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/694.java