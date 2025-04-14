error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9535.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9535.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9535.java
text:
```scala
private transient B@@SFEngine bsfEngine;

// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.protocol.java.sampler;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFManager;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * A sampler which understands BSF
 *
 * @version    $Revision$ Updated on: $Date$
 */
public class BSFSampler extends AbstractSampler
{

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String FILENAME   = "BSFSampler.filename"; //$NON-NLS-1$
	public static final String SCRIPT     = "BSFSampler.query"; //$NON-NLS-1$
	public static final String LANGUAGE   = "BSFSampler.language"; //$NON-NLS-1$
	public static final String PARAMETERS = "BSFSampler.parameters"; //$NON-NLS-1$

	private transient BSFManager mgr;
	private BSFEngine bsfEngine;
	
	public BSFSampler()
	{
		try {
			// register beanshell with the BSF framework
			mgr = new BSFManager();
			BSFManager.registerScriptingEngine("beanshell",
			 "bsh.util.BeanShellBSFEngine", new String [] { "bsh"} );
		} catch (NoClassDefFoundError e){
		}
	     
	   //TODO: register other scripting languages ...

	}
    
	public String getFilename()
	{
		return getPropertyAsString(FILENAME);
	}
	public void setFilename(String newFilename)
	{
		this.setProperty(FILENAME, newFilename);
	}
	public String getScript()
	{
		return this.getPropertyAsString(SCRIPT);
	}
	public void setScript(String newScript)
	{
		this.setProperty(SCRIPT, newScript);
	}
	public String getParameters()
	{
		return this.getPropertyAsString(PARAMETERS);
	}
	public void setParameters(String newScript)
	{
		this.setProperty(PARAMETERS, newScript);
	}
	public String getScriptLanguage()
	{
		return this.getPropertyAsString(LANGUAGE);
	}
	public void setScriptLanguage(String lang)
	{
		this.setProperty(LANGUAGE,lang);
	}

    /**
     * Returns a formatted string label describing this sampler
     *
     * @return a formatted string label describing this sampler
     */

    public String getLabel()
    {
        return getName();
    }


    public SampleResult sample(Entry e)// Entry tends to be ignored ...
    {
    	log.info(getLabel()+" "+getFilename());
        SampleResult res = new SampleResult();
        boolean isSuccessful = false;
        res.setSampleLabel(getLabel());
        res.sampleStart();
        try
        {
        	String request=getScript();
			res.setSamplerData(request);

			mgr.registerBean("Label",getLabel());
			mgr.registerBean("Name",getFilename());

			bsfEngine = mgr.loadScriptingEngine(getScriptLanguage());

			Object bsfOut = bsfEngine.eval("Sampler",0,0,request);

	        res.setResponseData(bsfOut.toString().getBytes());
	        res.setDataType(SampleResult.TEXT);
	        res.setResponseCode("200");//TODO set from script
	        res.setResponseMessage("OK");//TODO set from script
	        isSuccessful = true;//TODO set from script
        }
		catch (NoClassDefFoundError ex)
		{
			log.warn("",ex);
			res.setResponseCode("500");
			res.setResponseMessage(ex.toString());
		}
        catch (Exception ex)
        {
        	log.warn("",ex);
			res.setResponseCode("500");
            res.setResponseMessage(ex.toString());
        }

		res.sampleEnd();

        // Set if we were successful or not
        res.setSuccessful(isSuccessful);

        return res;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9535.java