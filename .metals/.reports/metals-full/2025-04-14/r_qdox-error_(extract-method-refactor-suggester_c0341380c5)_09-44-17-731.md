error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14730.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14730.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14730.java
text:
```scala
public synchronized S@@tatus parseBytes(byte[] bytes){

// $Header$
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
 */
package org.apache.jmeter.monitor.model;


import org.apache.jmeter.monitor.parser.Parser;
import org.apache.jmeter.monitor.parser.ParserImpl;
import org.apache.jmeter.samplers.SampleResult;

/**
 * ObjectFactory is a simple factory class which creates
 * new instances of objects. It also provides convienant
 * method to parse XML status results.
 */
public class ObjectFactory
{

	private static ObjectFactory FACTORY = null;
	private static Parser PARSER = null;
	
    /**
     * 
     */
    protected ObjectFactory()
    {
        super();
		PARSER = new MonitorParser(this);
    }

	public static ObjectFactory getInstance(){
		if (FACTORY == null){
			FACTORY = new ObjectFactory();
		}
		return FACTORY;
	}
	
	public Status parseBytes(byte[] bytes){
		return PARSER.parseBytes(bytes);
	}
	
	public Status parseString(String content){
		return PARSER.parseString(content);
	}

	public Status parseSampleResult(SampleResult result){
		return PARSER.parseSampleResult(result);
	}
	
	public Status createStatus(){
		return new StatusImpl();
	}
	
	public Connector createConnector(){
		return new ConnectorImpl();
	}

	public Jvm createJvm(){
		return new JvmImpl();
	}

	public Memory createMemory(){
		return new MemoryImpl();	
	}
	
	public RequestInfo createRequestInfo(){
		return new RequestInfoImpl();
	}
	
	public ThreadInfo createThreadInfo(){
		return new ThreadInfoImpl();
	}
	
	public Worker createWorker(){
		return new WorkerImpl();
	}
	
	public Workers createWorkers(){
		return new WorkersImpl();
	}
	
	protected class MonitorParser extends ParserImpl {
		public MonitorParser(ObjectFactory factory){
			super(factory);
		}
	}

	/**
	 * Basic method for testing the class
	 * @param args
	 */	
	public static void main(String[] args){
		if (args != null & args.length == 2){
			String file = null;
			int count = 1;
			if (args[0] != null){
				file = args[0];
			}
			if (args[1] != null){
				count = Integer.parseInt(args[1]);
			}
			try {
				ObjectFactory of = ObjectFactory.getInstance();
				java.io.File infile = new java.io.File(file);
				java.io.FileInputStream fis =
					new java.io.FileInputStream(infile);
				java.io.InputStreamReader isr =
					new java.io.InputStreamReader(fis);
				StringBuffer buf = new StringBuffer();
				java.io.BufferedReader br = new java.io.BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null){
					buf.append(line);
				}
				System.out.println("contents: ");
				System.out.println(buf.toString());
				System.out.println("----------------------");
				Status st = of.parseBytes(buf.toString().getBytes());
				if (st == null){
					System.out.println("parse failed");
				} else {
					System.out.println("parse successful:");
					System.out.println(st.getJvm().getMemory().getFree());
					System.out.println(st.getJvm().getMemory().getTotal());
					System.out.println(st.getJvm().getMemory().getMax());
					System.out.println("connector size: " +
						st.getConnector().size());
					Connector conn = (Connector)st.getConnector().get(0);
					System.out.println("conn: " +
						conn.getThreadInfo().getMaxThreads());
				}
			} catch (java.io.FileNotFoundException e){
				e.printStackTrace();
			} catch (java.io.IOException e){
				e.printStackTrace();
			}
		} else {
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14730.java