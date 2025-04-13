error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1976.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1976.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1976.java
text:
```scala
j@@vmName = "jdk13";

/*

   Derby - Class org.apache.derbyTesting.functionTests.harness.SysInfoLog

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

/***
 * SysInfoLog
 * Purpose: For a Suite or Test run, write out the
 * sysinfo to the suite or test output file
 *
 ***/

import java.io.*;
import java.util.Vector;

public class SysInfoLog
{

    public SysInfoLog()
    {
    }

    // Write out sysinfo for a suite or test
    public void exec(String jvmName, String javaCmd, String classpath,
        String framework, PrintWriter pw, boolean useprocess)
        throws Exception
	{
        if ( useprocess == true )
        {
            // Create a process to run sysinfo
    		Process pr = null;
			jvm javavm = null; // to quiet the compiler
    		try
    		{
                // Create the command line
                //System.out.println("jvmName: " + jvmName);
                if ( (jvmName == null) || (jvmName.length()==0) )
                    jvmName = "jdk13";
                else if (jvmName.startsWith("jdk13"))
                    jvmName = "jdk31";

				javavm = jvm.getJvm(jvmName);
                if (javaCmd != null)
                    javavm.setJavaCmd(javaCmd);
				
                if (javavm == null) System.out.println("WHOA, javavm is NULL");
                if (javavm == null) pw.println("WHOA, javavm is NULL");

                if ( (classpath != null) && (classpath.length()>0) )
                {
                    javavm.setClasspath(classpath);
                }

				Vector v = javavm.getCommandLine();
                v.addElement("org.apache.derby.tools.sysinfo");
                // Now convert the vector into a string array
                String[] sCmd = new String[v.size()];
                for (int i = 0; i < v.size(); i++)
                {
                    sCmd[i] = (String)v.elementAt(i);
                    //System.out.println(sCmd[i]);
                }
                
                pr = Runtime.getRuntime().exec(sCmd);

                // We need the process inputstream to capture into the output file
                BackgroundStreamDrainer stdout =
                    new BackgroundStreamDrainer(pr.getInputStream(), null);
                BackgroundStreamDrainer stderr =
                    new BackgroundStreamDrainer(pr.getErrorStream(), null);

                pr.waitFor();
                String result = HandleResult.handleResult(pr.exitValue(),
                    stdout.getData(), stderr.getData(), pw);
                pw.flush();

                if ( (framework != null) && (framework.length()>0) )
                {
                    pw.println("Framework: " + framework);
                }

                pr.destroy();
                pr = null;
            }
            catch(Throwable t)
            {
                if (javavm == null) System.out.println("WHOA, javavm is NULL");
                if (javavm == null) pw.println("WHOA, javavm is NULL");
                System.out.println("Process exception: " + t);
                pw.println("Process exception: " + t);
                t.printStackTrace(pw);
                if (pr != null)
                {
                    pr.destroy();
                    pr = null;
                }
            }
        }
        else
        {
            // For platforms where process exec fails or hangs
            // useprocess=false and attempt to get some info
            /*
            pw.println(org.apache.derby.impl.tools.sysinfo.Main.javaSep);
            org.apache.derby.impl.tools.sysinfo.Main.reportCloudscape(pw);
            pw.println(org.apache.derby.impl.tools.sysinfo.Main.jbmsSep);
            org.apache.derby.impl.tools.sysinfo.Main.reportCloudscape(pw);
            pw.println(org.apache.derby.impl.tools.sysinfo.Main.licSep);
            org.apache.derby.impl.tools.sysinfo.Main.printLicenseFile(pw);
            */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1976.java