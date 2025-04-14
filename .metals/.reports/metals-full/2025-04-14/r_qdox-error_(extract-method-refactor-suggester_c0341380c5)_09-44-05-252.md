error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9525.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9525.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9525.java
text:
```scala
private transient I@@nterpreter bshInterpreter;

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

package org.apache.jmeter.assertions;

import java.io.IOException;
import java.io.Serializable;

//import bsh.EvalError;
import bsh.Interpreter;
   
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * A sampler which understands BeanShell
 *
 */
public class BeanShellAssertion extends AbstractTestElement
    implements Serializable, Assertion
{
	protected static final Logger log = LoggingManager.getLoggerForClass();

    public static final String FILENAME   = "BeanShellAssertion.filename"; //$NON-NLS-1$
	public static final String SCRIPT     = "BeanShellAssertion.query"; //$NON-NLS-1$
	public static final String PARAMETERS = "BeanShellAssertion.parameters"; //$NON-NLS-1$

    private Interpreter bshInterpreter;
	
	public BeanShellAssertion()
	{
		try{
			bshInterpreter = new Interpreter();
		} catch (NoClassDefFoundError e){
			bshInterpreter=null;
		}
	}

	public String getScript()
	{
		return getPropertyAsString(SCRIPT);
	}
    
	public String getFilename()
	{
		return getPropertyAsString(FILENAME);
	}

	public String getParameters()
	{
		return getPropertyAsString(PARAMETERS);
	}

		/* (non-Javadoc)
		 * @see org.apache.jmeter.assertions.Assertion#getResult(org.apache.jmeter.samplers.SampleResult)
		 */
	public AssertionResult getResult(SampleResult response)
	{
		AssertionResult result = new AssertionResult();
		
		try
        {
        	String request=getScript();
        	String fileName=getFilename();

			bshInterpreter.set("FileName",getFilename());
			bshInterpreter.set("Parameters",getParameters());// as a single line
			bshInterpreter.set("bsh.args",JOrphanUtils.split(getParameters()," "));
			
			bshInterpreter.set("Response",response);// Raw access to the response
			bshInterpreter.set("ResponseData",response.getResponseData());
			bshInterpreter.set("ResponseCode",response.getResponseCode());
			bshInterpreter.set("ResponseMessage",response.getResponseMessage());
			bshInterpreter.set("ResponseHeaders",response.getResponseHeaders());
			bshInterpreter.set("RequestHeaders",response.getRequestHeaders());
			bshInterpreter.set("SampleLabel",response.getSampleLabel());
			bshInterpreter.set("SamplerData",response.getSamplerData());
			bshInterpreter.set("Successful",response.isSuccessful());

			// The following are used to set the Result details on return from the script:
			bshInterpreter.set("FailureMessage","");
			bshInterpreter.set("Failure",false);

			//Object bshOut;
			
			if (fileName.length() == 0){
				//bshOut = 
				bshInterpreter.eval(request);
			} else {
				//bshOut = 
				bshInterpreter.source(fileName);
			}
			
	        result.setFailureMessage(bshInterpreter.get("FailureMessage").toString());//$NON-NLS-1$
	        result.setFailure(Boolean.valueOf(bshInterpreter.get("Failure") //$NON-NLS-1$
	            .toString()).booleanValue());
			result.setError(false);
        }
/*
 * To avoid class loading problems when the BSH jar is missing,
 * we don't try to catch this error separately
 * 		catch (bsh.EvalError ex)
		{
			log.debug("",ex);
			result.setError(true);
			result.setFailureMessage(ex.toString());
		} 
 */
        // but we do trap this error to make tests work better
        catch(NoClassDefFoundError ex){
			log.error("BeanShell Jar missing? "+ex.toString());
			result.setError(true);
			result.setFailureMessage("BeanShell Jar missing? "+ex.toString());
			response.setStopThread(true); // No point continuing
        }
		catch (IOException ex)
		{
			result.setError(true);
			result.setFailureMessage(ex.toString());
			log.warn(ex.toString());
		}
		catch (Exception ex) // Mainly for bsh.EvalError
		{
			result.setError(true);
			result.setFailureMessage(ex.toString());
			log.warn(ex.toString());
		}

        return result;
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9525.java