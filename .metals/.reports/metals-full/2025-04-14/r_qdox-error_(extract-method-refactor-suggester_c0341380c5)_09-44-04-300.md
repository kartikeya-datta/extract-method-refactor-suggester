error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7519.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7519.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7519.java
text:
```scala
l@@og.info("Saved "+file+" as "+alias+" delimiter=<"+frcc.getDelimiter()+">");

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
 * 
*/

package org.apache.jmeter.functions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class wraps the FileRowColContainer for use across multiple
 * threads.
 * 
 * It does this by maintaining a list of open files, keyed by file name
 * (or alias, if used).
 * A list of open files is also maintained for each thread, together with
 * the current line number.
 * 
 * @version $Revision$ $Date$
 */
public class FileWrapper
{

	transient private static Logger log = LoggingManager.getLoggerForClass();
	
	private FileRowColContainer container;
    private int currentRow;
    private static final int NO_LINE = -1;
    
    private static String defaultFile = ""; // for omitted file names
    
	private static Map fileContainers = new HashMap(); // Map file names to containers
    
    /*
     * Only needed locally 
     */
    private FileWrapper(FileRowColContainer fdc)
    {
        super();
        container = fdc;
        currentRow = -1;
    }

	/* The cache of file packs */
	private static ThreadLocal filePacks = new ThreadLocal(){
		protected Object initialValue(){
			return new HashMap();
		}
	};

    private static String checkDefault(String file)
    {
		if (file.length() == 0)
		{
			if (fileContainers.size() == 1 && defaultFile.length() > 0)
			{
				log.warn("Using default: "+defaultFile);
				file = defaultFile;
			}
			else
			{
				log.error("Cannot determine default file name");
			}
		}
    	return file;
    }
    /*
     * called by CSVRead(file,alias)
     */
    public static synchronized void open(String file, String alias)
    {
    	log.info("Opening "+file+ " as " + alias);
    	file = checkDefault(file);
		if (alias.length() == 0)
		{
			log.error("Alias cannot be empty");
			return;
		} 
    	Map m = (Map) filePacks.get();
    	if (m.get(alias) == null)
    	{
    		FileRowColContainer frcc;
            try
            {
                frcc = getFile(file, alias);
				log.info("Stored "+file+" as "+alias);
				m.put(alias,new FileWrapper(frcc));
            }
            catch (FileNotFoundException e)
            {
            	//Already logged
            }
            catch (IOException e)
            {
				//Already logged
            }
    	}
    }
    
    private static FileRowColContainer getFile(String file, String alias)
    throws FileNotFoundException, IOException
    {
    	FileRowColContainer frcc;
    	if ((frcc = (FileRowColContainer) fileContainers.get(alias)) == null)
    	{
    		frcc = new FileRowColContainer(file);
    		fileContainers.put(alias,frcc);
			log.info("Saved "+file+" as "+alias);
			if (defaultFile.length() == 0){
				defaultFile = file;// Save in case needed later
			}
    	}
    	return frcc;
    }
    
    /*
     * Called by CSVRead(x,next) - sets the row to nil so the next
     * row will be picked up the next time round
     * 
     */
    public static void endRow(String file)
    {
    	file=checkDefault(file);
		Map my = (Map) filePacks.get();
		FileWrapper fw = (FileWrapper) (my).get(file);
		if (fw == null)
		{
			log.warn("endRow(): no entry for "+file);
		}
		else
		{
			fw.endRow();
		}
    }
    
    private void endRow()
    {
		if (currentRow == NO_LINE)
		{
			log.warn("endRow() called twice in succession");
		}
		currentRow = NO_LINE;
    }

    public static String getColumn(String file,int col)
    {
    	Map my = (Map) filePacks.get();
		FileWrapper fw = (FileWrapper) (my).get(file);
		if (fw == null) // First call
		{
			if (file.startsWith("*")) {
				log.warn("Cannot perform initial open using alias "+file);
			}
			else
			{
				file=checkDefault(file);
				log.info("Attaching "+file);
				open(file,file);
				fw = (FileWrapper) my.get(file);
			}
			//TODO improve the error handling
			if (fw == null) return "";
		}
    	return fw.getColumn(col);
    }
    
    private String getColumn(int col)
    {
		if (currentRow == NO_LINE)
		{
			currentRow = container.nextRow();
    		
		}
		return container.getColumn(currentRow,col);
    }

    /**
     * Gets the current row number (mainly for error reporting)
     * 
     * @param file
     * @return the current row number for this thread
     */
    public static int getCurrentRow(String file)
    {
    	
		Map my = (Map) filePacks.get();
		FileWrapper fw = (FileWrapper) (my).get(file);
		if (fw == null) // Not yet open
		{
			return -1;
		}
		else
		{
			return fw.currentRow;
		}
    }
    
    /**
     * 
     */
    public static void clearAll()
    {
		log.debug("clearAll()");
		Map my = (Map) filePacks.get();
		for (Iterator i=my.entrySet().iterator();i.hasNext();)
		{
			Object fw = i.next();
			log.info("Removing "+fw.toString());
			i.remove();
		}
		fileContainers.clear();
		defaultFile = "";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7519.java