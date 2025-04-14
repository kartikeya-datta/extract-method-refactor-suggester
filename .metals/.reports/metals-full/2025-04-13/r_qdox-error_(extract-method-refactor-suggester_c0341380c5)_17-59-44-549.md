error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16419.java
text:
```scala
n@@ew JMeterFileFilter(new String[] { ".txt",".obj" }));

/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.apache.jmeter.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.gui.JMeterFileFilter;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author mstover
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileServer
{
    static Logger log = LoggingManager.getLoggerForClass();
    File base;
    Map files = new HashMap();
    private static FileServer server = new FileServer();
	private Random random = new Random();
    
    private FileServer()
    {
        base = new File(JMeterUtils.getProperty("user.dir"));
    }
    
    public static FileServer getFileServer()
    {
        return server;
    }
    
    public void setBasedir(String basedir) throws IOException
    {
        log.info("Setting basedir to: " + basedir);
        if(filesOpen())
        {
            throw new IOException("Files are still open, cannot change base directory");
        }
        files.clear();
        if(basedir != null)
        {
            base = new File(basedir);
            if(!base.isDirectory())
            {
                base = base.getParentFile();
            }
        }
    }
    
    public String getBaseDir()
    {
        return base.getAbsolutePath();
    }
    
    public synchronized void reserveFile(String filename)
    {
        log.info("filename = "+ filename+ " base = "+ base);
        if(!files.containsKey(filename))
        {
            Object[] file = new Object[]{new File(base,filename),null};
            files.put(filename,file);
        }
    }
    
    /**
     * Get the next line of the named file.
     * @param filename
     * @return
     * @throws IOException
     */
    public synchronized String readLine(String filename) throws IOException
    {
        Object[] file = (Object[])files.get(filename);
        if(file != null)
        {
            if(file[1] == null)
            {
                BufferedReader r = new BufferedReader(new FileReader((File)file[0]));
                file[1] = r;
            }
            BufferedReader reader = (BufferedReader)file[1];
            String line = reader.readLine();
            if(line == null)
            {
                reader.close();
                reader = new BufferedReader(new FileReader((File)file[0]));
                file[1] = reader;
                line = reader.readLine();
            }
            return line;
        }
        throw new IOException("File never reserved");
    }
    
    public void closeFiles() throws IOException
    {
        Iterator iter = files.keySet().iterator();
        while(iter.hasNext())
        {
            String name = (String)iter.next();
            Object[] file = (Object[])files.get(name);
            if(file[1] != null)
            {
                ((Reader)file[1]).close();
                file[1] = null;
            }
        }  
        files.clear();
    }
    
    protected boolean filesOpen()
    {
        Iterator iter = files.keySet().iterator();
        while(iter.hasNext())
        {
            String name = (String)iter.next();
            Object[] file = (Object[])files.get(name);
            if(file[1] != null)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Method will get a random file in a base directory
     * @param basedir
     * @return
     */
    public File getRandomFile(String basedir){
    	File input = null;
		if (basedir != null)
		{
			File src = new File(basedir);
			if (src.isDirectory() && src.list() != null)
			{
				File[] files =
					src.listFiles(
						new JMeterFileFilter(new String[] { ".txt,.obj" }));
				int count = files.length;
				input = files[random.nextInt(count)];
			}
		}
		return input;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16419.java