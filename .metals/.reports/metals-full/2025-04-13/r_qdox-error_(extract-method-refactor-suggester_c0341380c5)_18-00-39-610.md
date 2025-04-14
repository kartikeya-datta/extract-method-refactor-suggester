error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1428.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1428.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1428.java
text:
```scala
h@@ostSed.exec(tmpFile, orgFile, sedIs, false, false, false);

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.derbynet.testij

   Copyright 2002, 2004 The Apache Software Foundation or its licensors, as applicable.

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
package org.apache.derbyTesting.functionTests.tests.derbynet;

import java.sql.*;
import java.util.Vector;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import org.apache.derbyTesting.functionTests.harness.jvm;
import org.apache.derbyTesting.functionTests.harness.ProcessStreamResult;
import org.apache.derbyTesting.functionTests.harness.Sed;
import org.apache.derbyTesting.functionTests.util.TestUtil;

import org.apache.derby.drda.NetworkServerControl;


public class testij
{


	private static Properties properties = new java.util.Properties();
	private static jvm jvm;
	private static Vector vCmd;

	private static String IjCmd="org.apache.derby.tools.ij";
	private static String SqlDir="extin";
	private static String jccSqlFile="testij.sql";
	private static String sep;
	private static String clientSqlFile="testclientij.sql";
	private static String altExtinDir;
	private static boolean useAltExtinDir=false;
	
	private static void execCmd (String[] args) throws Exception
	{
		int totalSize = vCmd.size() + args.length;
		String serverCmd[] = new String[totalSize];
		int i;
		for (i = 0; i < vCmd.size(); i++)
		{
			serverCmd[i] = (String)vCmd.elementAt(i);
		//	System.out.println("serverCmd["+i+"]: "+serverCmd[i]);
		}
		int j = 0;
		for (; i < totalSize; i++)
		{
			serverCmd[i] = args[j++];
		//	System.out.println("serverCmd["+i+"]: "+serverCmd[i]);
		}
 
		// Start a process to run the command
		Process pr = Runtime.getRuntime().exec(serverCmd);
		pr.waitFor();		// make sure this is executed first
	}
	/**
	 * Execute the given command and dump the results to standard out
	 *
	 * @param args	command and arguments
	 * @exception Exception
	 */

	private static void execCmdDumpResults (String[] args) throws Exception
	{
        // We need the process inputstream and errorstream
        ProcessStreamResult prout = null;
        ProcessStreamResult prerr = null;
        BufferedOutputStream bos = null;
            
        StringBuffer sb = new StringBuffer();
            
        for (int i = 0; i < args.length; i++)
        {
            sb.append(args[i] + " ");                    
        }
        System.out.println(sb.toString());
		int totalSize = vCmd.size() + args.length;
		String serverCmd[] = new String[totalSize];
		int i;
		for (i = 0; i < vCmd.size(); i++)
		{
			serverCmd[i] = (String)vCmd.elementAt(i);
		//	System.out.println("serverCmd["+i+"]: "+serverCmd[i]);
		}
		int j = 0;
		for (; i < totalSize; i++)
		{
			serverCmd[i] = args[j++];
		//	System.out.println("serverCmd["+i+"]: "+serverCmd[i]);
		}
 
		// Start a process to run the command
		Process pr = Runtime.getRuntime().exec(serverCmd);
        bos = new BufferedOutputStream(System.out, 1024);
        prout = new ProcessStreamResult(pr.getInputStream(), bos, null);
        prerr = new ProcessStreamResult(pr.getErrorStream(), bos, null);

		// wait until all the results have been processed
		prout.Wait();
		prerr.Wait();

		bos.close();

	}
	
    public static void massageSqlFile (String hostName, String fileName) throws Exception {
        // only called if hostName is *not* localhost. 
        // Need to replace each occurrence of the string 'localhost' with 
        // whatever is the hostName
        File tmpFile = new File("extin", "tmpFile.sql");
        File orgFile = new File("extin", fileName);
        // wrap this in a try to possibly try using user.dir to find the file
        InputStream original; 
        OutputStream copy; 
        try { 
            fileName = SqlDir + sep + fileName; 
            original = new FileInputStream(fileName);
            copy = new FileOutputStream(tmpFile);
        }
        catch (FileNotFoundException fnfe) {
            // we must be running from within a suite...
            useAltExtinDir = true;
            String userdir =  System.getProperty("user.dir");
            altExtinDir = userdir + sep + ".."; 
            tmpFile = new File(altExtinDir, "tmpFile.sql");
            orgFile = new File (altExtinDir,  fileName); 
            fileName = altExtinDir + sep + fileName;
            original = new FileInputStream(fileName);
            copy = new FileOutputStream(tmpFile);
        }
        int content;
        while ((content = original.read())> 0 ) {
            copy.write(content);
        }
        copy.close();
        original.close();
        Sed hostSed = new Sed();
        InputStream sedIs = new ByteArrayInputStream(("substitute=localhost;" + hostName).getBytes("UTF-8"));
        hostSed.exec(tmpFile, orgFile, sedIs, false, false);		
    }

	public static void main (String args[]) throws Exception
	{
		if ((System.getProperty("java.vm.name") != null) && System.getProperty("java.vm.name").equals("J9"))
			jvm = jvm.getJvm("j9_13");
		else
			jvm = jvm.getJvm("currentjvm");		// ensure compatibility
		vCmd = jvm.getCommandLine();
		sep =  System.getProperty("file.separator");
		try
		{
			/************************************************************
			 *  Test comments in front of select's doesn't cause problems
			 ************************************************************/
			//create wombat database
			NetworkServerControl server = new NetworkServerControl();
			System.out.println("Testing various ij connections and comments in front of selects");
			
			// first, we have to massage the .sql file to replace localhost, if 
			// there is a system property set.
						
			String hostName=TestUtil.getHostName();
			if (TestUtil.isJCCFramework()){
				// use jccSqlfile
				if (!hostName.equals("localhost")) 
					massageSqlFile(hostName,jccSqlFile);
				if (useAltExtinDir)	
					execCmdDumpResults(new String[]{IjCmd,(altExtinDir + sep + SqlDir + sep + jccSqlFile)});
				execCmdDumpResults(new String[]{IjCmd,(SqlDir + sep + jccSqlFile)});
			} else {   // Derby Client
				// use clientSqlFile
				if(!hostName.equals("localhost")) {
					massageSqlFile(hostName,clientSqlFile);
				if (useAltExtinDir)	
					execCmdDumpResults(new String[]{IjCmd,(altExtinDir + sep + SqlDir + sep + clientSqlFile)});
				}
				execCmdDumpResults(new String[]{IjCmd,(SqlDir + sep + clientSqlFile)});
			}
			System.out.println("End test");
		}
		catch (Exception e)
		{
			e.printStackTrace();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1428.java