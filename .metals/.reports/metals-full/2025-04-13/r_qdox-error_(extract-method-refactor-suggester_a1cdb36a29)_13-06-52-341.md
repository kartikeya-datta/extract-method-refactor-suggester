error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 858
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7909.java
text:
```scala
static class Test2 implements HasName, HasString {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

p@@ackage org.apache.jorphan.reflect;

import java.util.Map;
import java.util.Properties;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterError;

/*
 * Unit tests for classes that use Functors
 * 
 */
public class TestFunctor extends JMeterTestCase {

	interface HasName {
		String getName();
	}
	
	interface HasString {
		String getString(String s);
	}

	class Test1 implements HasName {
		private final String name;
		public Test1(){
			this("");
		}
		public Test1(String s){
			name=s;
		}
		public String getName(){
			return name;
		}
		public String getString(String s){
			return s;
		}
	}
	class Test1a extends Test1{
		Test1a(){
			super("1a");
		}
		Test1a(String s){
			super("1a:"+s);
		}
		public String getName(){
			return super.getName()+".";
		}
	}
	class Test2 implements HasName, HasString {
		private final String name;
		public Test2(){
			this("");
		}
		public Test2(String s){
			name=s;
		}
		public String getName(){
			return name;
		}
		public String getString(String s){
			return s;
		}
	}
	
	public TestFunctor(String arg0) {
		super(arg0);
	}
    
	public void setUp(){
		LoggingManager.setPriority("FATAL_ERROR",LoggingManager.removePrefix(Functor.class.getName()));		
	}

	public void testName() throws Exception{
		Functor f1 = new Functor("getName");
		Functor f2 = new Functor("getName");
		Functor f1a = new Functor("getName");
		Test1 t1 = new Test1("t1");
		Test2 t2 = new Test2("t2");
		Test1a t1a = new Test1a("aa");
		assertEquals("t1",f1.invoke(t1));
		//assertEquals("t1",f1.invoke());
		try {
		    f1.invoke(t2);
		    fail("Should have generated error");
		} catch (JMeterError e){
			
		}
		assertEquals("t2",f2.invoke(t2));
		//assertEquals("t2",f2.invoke());
		assertEquals("1a:aa.",f1a.invoke(t1a));
		//assertEquals("1a:aa.",f1a.invoke());
		try {
		    f1a.invoke(t1);// can't call invoke using super class
		    fail("Should have generated error");
		} catch (JMeterError e){
			
		}
		// OK (currently) to invoke using sub-class 
		assertEquals("1a:aa.",f1.invoke(t1a));
		//assertEquals("1a:aa.",f1.invoke());// N.B. returns different result from before
	}
    
	public void testNameTypes() throws Exception{
		Functor f = new Functor("getString",new Class[]{String.class});
		Functor f2 = new Functor("getString");// Args will be provided later
		Test1 t1 = new Test1("t1");
		assertEquals("x1",f.invoke(t1,new String[]{"x1"}));
		try {
			assertEquals("x1",f.invoke(t1));
		    fail("Should have generated an Exception");
		} catch (JMeterError ok){
		}
		assertEquals("x2",f2.invoke(t1,new String[]{"x2"}));
		try {
			assertEquals("x2",f2.invoke(t1));
		    fail("Should have generated an Exception");
		} catch (JMeterError ok){
		}
	}
	public void testObjectName() throws Exception{
		Test1 t1 = new Test1("t1");
		Test2 t2 = new Test2("t2");
		Functor f1 = new Functor(t1,"getName");
		assertEquals("t1",f1.invoke(t1));
		assertEquals("t1",f1.invoke(t2)); // should use original object
	}
	
	// Check how Class definition behaves
	public void testClass() throws Exception{
		Test1 t1 = new Test1("t1");
		Test1 t1a = new Test1a("t1a");
		Test2 t2 = new Test2("t2");
		Functor f1 = new Functor(HasName.class,"getName");
		assertEquals("t1",f1.invoke(t1));
		assertEquals("1a:t1a.",f1.invoke(t1a));
		assertEquals("t2",f1.invoke(t2));
		try {
			f1.invoke();
			fail("Should have failed");
		} catch (IllegalStateException ok){
			
		}
		Functor f2 = new Functor(HasString.class,"getString");
		assertEquals("xyz",f2.invoke(t2,new String[]{"xyz"}));
		try {
			f2.invoke(t1,new String[]{"xyz"});
			fail("Should have failed");
		} catch (JMeterError ok){
			
		}
		Functor f3 = new Functor(t2,"getString");
		assertEquals("xyz",f3.invoke(t2,new Object[]{"xyz"}));
		
		Properties p = new Properties();
		p.put("Name","Value");
		Functor fk = new Functor(Map.Entry.class,"getKey");
		Functor fv = new Functor(Map.Entry.class,"getValue");
		Object o = p.entrySet().iterator().next();
		assertEquals("Name",fk.invoke(o));
		assertEquals("Value",fv.invoke(o));
	}
	
	public void testBadParameters() throws Exception{
		try {
			new Functor(null);
			fail("should have generated IllegalArgumentException;");
		} catch (IllegalArgumentException ok){}
		try {
			new Functor(null,new Class[]{});
			fail("should have generated IllegalArgumentException;");
		} catch (IllegalArgumentException ok){}
		try {
			new Functor(null,new Object[]{});
			fail("should have generated IllegalArgumentException;");
		} catch (IllegalArgumentException ok){}
		try {
			new Functor(String.class,null);
			fail("should have generated IllegalArgumentException;");
		} catch (IllegalArgumentException ok){}
		try {
			new Functor(new Object(),null);
			fail("should have generated IllegalArgumentException;");
		} catch (IllegalArgumentException ok){}
		try {
			new Functor(new Object(),null, new Class[]{});
			fail("should have generated IllegalArgumentException;");
		} catch (IllegalArgumentException ok){}
		try {
			new Functor(new Object(),null, new Object[]{});
			fail("should have generated IllegalArgumentException;");
		} catch (IllegalArgumentException ok){}
	}
	public void testIllegalState() throws Exception{
		Functor f = new Functor("method");
		try {
			f.invoke();
			fail("should have generated IllegalStateException;");
		} catch (IllegalStateException ok){}		
		try {
			f.invoke(new Object[]{});
			fail("should have generated IllegalStateException;");
		} catch (IllegalStateException ok){}		
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7909.java