error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2543.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2543.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2543.java
text:
```scala
public v@@oid setUp() {

/*
 * Derby - Class org.apache.derbyTesting.functionTests.util.ExecIjTestCase
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */

package org.apache.derbyTesting.functionTests.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.derby.tools.ij;
import org.apache.derbyTesting.junit.SupportFilesSetup;


/**
 * Run a .sql script via ij's main method and compare with a canon.
 * 
 * Tests that extend this class should always wrap their suite with
 * a SupportFilesSetup so that the extinout directory where ij will
 * write the test output is created. 
 */
public class IjTestCase extends ScriptTestCase {

	String scriptName;
	String outfileName;
    File outfile;
	
    /**
     * Create a script testcase that runs the .sql script with the
     * given name. The name should not include the ".sql" suffix.
     */
	public IjTestCase(String name) {
		super(name);
		scriptName = getName() + ".sql";
		outfileName = SupportFilesSetup.EXTINOUT + "/" + getName() + ".out";
		outfile = new File(outfileName);
	}
	
	public void setUp() throws Exception{
	    super.setUp();
		setSystemProperty("ij.outfile", outfileName);
		setSystemProperty("ij.defaultResourcePackage",
				"/org/apache/derbyTesting/functionTests/tests/"
				+ getArea() + "/");
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		removeSystemProperty("ij.outfile");
		removeSystemProperty("ij.defaultResourcePackage");
	}
	
	/**
	 * Run a .sql test, calling ij's main method.
	 * Then, take the output file and read it into our OutputStream
	 * so that it can be compared via compareCanon().
	 * TODO:
	 * Note that the output will include a version number;
	 * this should get filtered/ignored in compareCanon
	 */
	public void runTest() throws Throwable {
		String [] args = { "-fr", scriptName };
		ij.main(args);
		
		String canon =
			"org/apache/derbyTesting/functionTests/master/"
			+ getName() + ".out";
		
		final File out = outfile;
		FileInputStream fis = (FileInputStream) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(out);
				} catch (FileNotFoundException e) {
					fail("Could not open ij output file.");
				}				
				return fis;
			}
		});
		OutputStream os = getOutputStream();
		int b;
		while ((b = fis.read()) != -1) {
			os.write(b);
		}
		fis.close();
		
		Boolean deleted = (Boolean) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				boolean d = outfile.delete();
				
				return new Boolean(d);
			}
		});
		
		if (!deleted.booleanValue())
			println("Could not delete outfile for " + scriptName);
		
		this.compareCanon(canon);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2543.java