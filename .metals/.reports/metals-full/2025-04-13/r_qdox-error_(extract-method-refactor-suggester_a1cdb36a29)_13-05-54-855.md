error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2196.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2196.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2196.java
text:
```scala
i@@f ((fileName.indexOf("sql") > 0) || (fileName.indexOf("txt") > 0) || (fileName.indexOf(".view") > 0) || (fileName.indexOf(".policy") > 0) || (fileName.indexOf(".multi") > 0) || (fileName.indexOf(".properties") > 0))

/*

   Derby - Class org.apache.derbyTesting.functionTests.harness.CopySuppFiles

   Copyright 1999, 2004 The Apache Software Foundation or its licensors, as applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derbyTesting.functionTests.harness;

import java.io.*;
import java.util.StringTokenizer;

/**
  For tests which require support files.
  Copy them to the output directory for the test.
  */
public class CopySuppFiles
{

	public static void main(String[] args) throws Exception
	{
	}

	public static void copyFiles(File outDir, String suppFiles)
	    throws ClassNotFoundException, IOException
	{
	    // suppFiles is a comma separated list of the files
	    StringTokenizer st = new StringTokenizer(suppFiles,",");
	    String scriptName = ""; // example: test/math.sql
	    InputStream is = null; // To be used for each support file
        while (st.hasMoreTokens())
        {
            scriptName = st.nextToken();
    	    File suppFile = null;
    	    String fileName = "";
    	    // Try to locate the file
            is = RunTest.loadTestResource(scriptName); 
    		if ( is == null )
    			System.out.println("Could not locate: " + scriptName);
    		else
    		{
    		    // Copy the support file so the test can use it
    			int index = scriptName.lastIndexOf('/');
    			fileName = scriptName.substring(index+1);
 //   			suppFile = new File((new File(outDir, fileName)).getCanonicalPath());

		//these calls to getCanonicalPath catch IOExceptions as a workaround to
		//a bug in the EPOC jvm. 
    		try {suppFile = new File((new File(outDir, fileName)).getCanonicalPath());}
		catch (IOException e) {
		    File f = new File(outDir, fileName);
		    FileWriter fw = new FileWriter(f);
		    fw.close();
		    suppFile = new File(f.getCanonicalPath());
		}
                // need to make a guess so we copy text files to local encoding
                // on non-ascii systems...
		        if ((fileName.indexOf("sql") > 0) || (fileName.indexOf("txt") > 0) || (fileName.indexOf(".view") > 0) || (fileName.indexOf(".policy") > 0))
                {
                    BufferedReader inFile = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    PrintWriter pw = new PrintWriter
                       ( new BufferedWriter(new FileWriter(suppFile), 10000), true );
                    int c;
                    while ((c = inFile.read()) != -1)
                        pw.write(c);
                    pw.flush();
                    pw.close();
                }
                else
                {
                    FileOutputStream fos = new FileOutputStream(suppFile);
                    byte[] data = new byte[4096];
                    int len;
                    while ((len = is.read(data)) != -1)
                    {
                        fos.write(data, 0, len);
                    }
                    fos.close();
                }
    		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2196.java