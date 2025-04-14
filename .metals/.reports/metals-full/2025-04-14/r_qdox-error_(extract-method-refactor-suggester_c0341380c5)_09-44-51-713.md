error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12701.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12701.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12701.java
text:
```scala
r@@esult.setSampleLabel(getName());

/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

package org.apache.jmeter.protocol.jms.sampler;

import org.apache.jorphan.io.TextFile;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.engine.event.LoopIterationEvent;

import org.apache.jmeter.protocol.jms.control.gui.JMSPublisherGui;
import org.apache.jmeter.protocol.jms.client.ClientPool;
import org.apache.jmeter.protocol.jms.client.Publisher;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author pete
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PublisherSampler
    extends BaseJMSSampler
    implements TestListener {

	public static final String INPUT_FILE = "jms.input_file";
	public static final String RANDOM_PATH = "jms.random_path";
	public static final String TEXT_MSG = "jms.text_message";
	public static final String CONFIG_CHOICE = "jms.config_choice";
	public static final String MESSAGE_CHOICE = "jms.config_msg_type";
	
	private Publisher PUB = null;
	private StringBuffer BUFFER = new StringBuffer();
	private static FileServer FSERVER = FileServer.getFileServer();
	private String file_contents = null;
	static Logger log = LoggingManager.getLoggerForClass();
	
	public PublisherSampler(){
	}
	
	/**
	 * the implementation calls testStarted() without
	 * any parameters.
	 */
	public void testStarted(String test){
		testStarted();
	}
	
	/**
	 * the implementation calls testEnded() without
	 * any parameters.
	 */
	public void testEnded(String test){
		testEnded();
	}

    /**
     * endTest cleans up the client
     * @see junit.framework.TestListener#endTest(junit.framework.Test)
     */
    public void testEnded() {
		log.info("PublisherSampler.testEnded called");
		Thread.currentThread().interrupt();
		this.PUB = null;
		this.BUFFER.setLength(0);
		this.BUFFER = null;
		ClientPool.clearClient();
		try {
			this.finalize();
		} catch (Throwable e){
			log.error(e.getMessage());
		}
    }

    /**
     * the implementation creates a new StringBuffer
     */
    public void testStarted() {
		this.BUFFER = new StringBuffer();
    }

	/**
	 * NO implementation provided for the sampler. It is
	 * necessary in this case.
	 */
	public void testIterationStart(LoopIterationEvent event){
	}

	/**
	 * initialize the Publisher client.
	 *
	 */
    public synchronized void initClient() {
        this.PUB =
            new Publisher(
                this.getUseJNDIPropertiesAsBoolean(),
                this.getJNDIInitialContextFactory(),
                this.getProviderUrl(),
                this.getConnectionFactory(),
                this.getTopic(),
                this.getUseAuth(),
                this.getUsername(),
                this.getPassword());
        ClientPool.addClient(this.PUB);
        log.info("PublisherSampler.initClient called");
    }
	
    /**
     * The implementation calls sample() without any parameters
     */
    public SampleResult sample(Entry e) {
        return this.sample();
    }

	/**
	 * The implementation will publish n messages within a for
	 * loop. Once n messages are published, it sets the attributes
	 * of SampleResult.
	 * @return
	 */    
    public SampleResult sample(){
    	SampleResult result = new SampleResult();
    	result.setSampleLabel("PublisherSampler:" + this.getTopic());
        if (this.PUB == null) {
            this.initClient();
        }
        int loop = this.getIterationCount();
        if (this.PUB != null){
			result.sampleStart();
			for (int idx = 0; idx < loop; idx++) {
				String tmsg = this.getMessageContent();
				this.PUB.publish(tmsg);
				this.BUFFER.append(tmsg);
			}
			result.sampleEnd();
			String content = this.BUFFER.toString();
			result.setBytes(content.getBytes().length);
			result.setResponseCode("message published successfully");
			result.setResponseMessage(loop + " messages published");
			result.setSuccessful(true);
			result.setResponseData(content.getBytes());
			result.setSampleCount(loop);
			this.BUFFER.setLength(0);
        }
    	return result;
    }

	/**
	 * Method will check the setting and get the contents
	 * for the message.
	 * @return
	 */
	public String getMessageContent(){
		if (this.getConfigChoice().equals(JMSPublisherGui.use_file)){
			// in the case the test uses a file, we set it locally and
			// prevent loading the file repeatedly
			if (this.file_contents == null){
				this.file_contents = this.getFileContent(this.getInputFile());
			}
			return this.file_contents;
		} else if (this.getConfigChoice().equals(JMSPublisherGui.use_random)){
			// Maybe we should consider creating a global cache for the
			// random files to make JMeter more efficient.
			String fname = 
			  FSERVER.getRandomFile(this.getRandomPath(),new String[]{".txt",".obj"}).
			  getAbsolutePath();
			return this.getFileContent(fname);
		} else {
			return this.getTextMessage();
		}
	}
	
	/**
	 * The implementation uses TextFile to load the contents
	 * of the file and returns a string.
	 * @param path
	 * @return
	 */
	public String getFileContent(String path){
		TextFile tf = new TextFile(path);
		return tf.getText();
	}
	
	//-------------  get/set properties ----------------------//
	/**
	 * set the config choice
	 * @param choice
	 */	
	public void setConfigChoice(String choice){
		setProperty(CONFIG_CHOICE,choice);
	}
	
	/**
	 * return the config choice
	 * @return
	 */
	public String getConfigChoice(){
		return getPropertyAsString(CONFIG_CHOICE);
	}

	/**
	 * set the source of the message
	 * @param choice
	 */
	public void setMessageChoice(String choice){
		setProperty(MESSAGE_CHOICE,choice);
	}

	/**
	 * return the source of the message
	 * @return
	 */	
	public String getMessageChoice(){
		return getPropertyAsString(MESSAGE_CHOICE);
	}

	/**
	 * set the input file for the publisher
	 * @param file
	 */	
	public void setInputFile(String file){
		setProperty(INPUT_FILE,file);	
	}

	/**
	 * return the path of the input file
	 * @return
	 */	
	public String getInputFile(){
		return getPropertyAsString(INPUT_FILE);
	}

	/**
	 * set the random path for the messages
	 * @param path
	 */	
	public void setRandomPath(String path){
		setProperty(RANDOM_PATH,path);
	}

	/**
	 * return the random path for messages
	 * @return
	 */	
	public String getRandomPath(){
		return getPropertyAsString(RANDOM_PATH);
	}

	/**
	 * set the text for the message
	 * @param message
	 */	
	public void setTextMessage(String message){
		setProperty(TEXT_MSG,message);
	}

	/**
	 * return the text for the message
	 * @return
	 */	
	public String getTextMessage(){
		return getPropertyAsString(TEXT_MSG);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12701.java