error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4657.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4657.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4657.java
text:
```scala
i@@f (ftp.isConnected()) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

package org.apache.jmeter.protocol.ftp.sampler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * A sampler which understands FTP file requests.
 * 
 */
public class FTPSampler extends AbstractSampler {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public final static String SERVER = "FTPSampler.server"; // $NON-NLS-1$

	// N.B. Originally there was only one filename, and only get(RETR) was supported
	// To maintain backwards compatibility, the property name needs to remain the same
	public final static String REMOTE_FILENAME = "FTPSampler.filename"; // $NON-NLS-1$
	
	public final static String LOCAL_FILENAME = "FTPSampler.localfilename"; // $NON-NLS-1$

    public final static String INPUT_DATA = "FTPSampler.inputdata"; // $NON-NLS-1$

	// Use binary mode file transfer?
	public final static String BINARY_MODE = "FTPSampler.binarymode"; // $NON-NLS-1$

	// Are we uploading?
	public final static String UPLOAD_FILE = "FTPSampler.upload"; // $NON-NLS-1$
	
	// Should the file data be saved in the response?
	public final static String SAVE_RESPONSE = "FTPSampler.saveresponse"; // $NON-NLS-1$

	public FTPSampler() {
	}

	public String getUsername() {
		return getPropertyAsString(ConfigTestElement.USERNAME);
	}

	public String getPassword() {
		return getPropertyAsString(ConfigTestElement.PASSWORD);
	}

	public void setServer(String newServer) {
		this.setProperty(SERVER, newServer);
	}

	public String getServer() {
		return getPropertyAsString(SERVER);
	}

	public String getRemoteFilename() {
		return getPropertyAsString(REMOTE_FILENAME);
	}

	public String getLocalFilename() {
		return getPropertyAsString(LOCAL_FILENAME);
	}

    private String getLocalFileContents() {
        return getPropertyAsString(INPUT_DATA);
    }

	public boolean isBinaryMode(){
		return getPropertyAsBoolean(BINARY_MODE,false);
	}

	public boolean isSaveResponse(){
		return getPropertyAsBoolean(SAVE_RESPONSE,false);
	}

	public boolean isUpload(){
		return getPropertyAsBoolean(UPLOAD_FILE,false);
	}

	
	/**
	 * Returns a formatted string label describing this sampler Example output:
	 * ftp://ftp.nowhere.com/pub/README.txt
	 * 
	 * @return a formatted string label describing this sampler
	 */
	public String getLabel() {
	    StrBuilder sb = new StrBuilder();
	    sb.setNullText("null");// $NON-NLS-1$
	    sb.append("ftp://");// $NON-NLS-1$
	    sb.append(getServer());
	    sb.append("/");// $NON-NLS-1$
	    sb.append(getRemoteFilename());
	    sb.append(isBinaryMode() ? " (Binary) " : " (Ascii) ");// $NON-NLS-1$ $NON-NLS-2$
		sb.append(isUpload() ? " <- " : " -> "); // $NON-NLS-1$ $NON-NLS-2$
		sb.append(getLocalFilename());
		return sb.toString();
	}

	public SampleResult sample(Entry e) {
		SampleResult res = new SampleResult();
		res.setSuccessful(false); // Assume failure
		String remote = getRemoteFilename();
		String local = getLocalFilename();
		boolean binaryTransfer = isBinaryMode();
		res.setSampleLabel(getName());
        final String label = getLabel();
        res.setSamplerData(label);
        try {
            res.setURL(new URL(label));
        } catch (MalformedURLException e1) {
            log.warn("Cannot set URL: "+e1.getLocalizedMessage());
        }
        InputStream input = null;
        OutputStream output = null;

        res.sampleStart();
        FTPClient ftp = new FTPClient();
		try {
			ftp.connect(getServer());
			res.latencyEnd();
			int reply = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply))
            {
	            if (ftp.login( getUsername(), getPassword())){
	                if (binaryTransfer) {
	                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
	                }
					ftp.enterLocalPassiveMode();// should probably come from the setup dialog
					boolean ftpOK=false;
		            if (isUpload()) {
		                String contents=getLocalFileContents();
		                if (contents.length() > 0){
		                    byte bytes[] = contents.getBytes();// TODO this assumes local encoding
		                    input = new ByteArrayInputStream(bytes);
	                        res.setBytes(bytes.length);
		                } else {
	                        File infile = new File(local);
	                        res.setBytes((int)infile.length());
	                        input = new FileInputStream(infile);		                    
		                }
		                ftpOK = ftp.storeFile(remote, input);		                
		            } else {
		                final boolean saveResponse = isSaveResponse();
		            	ByteArrayOutputStream baos=null; // No need to close this
		            	OutputStream target=null; // No need to close this
		            	if (saveResponse){
		            		baos  = new ByteArrayOutputStream();
		            		target=baos;
		            	}
		            	if (local.length()>0){
		            		output=new FileOutputStream(local);
		            		if (target==null) {
		            			target=output;
		            		} else {
		            			target = new TeeOutputStream(output,baos);
		            		}
		            	}
		            	if (target == null){
		            		target=new NullOutputStream();
		            	}
		                input = ftp.retrieveFileStream(remote);
		                if (input == null){// Could not access file or other error
	                        res.setResponseCode(Integer.toString(ftp.getReplyCode()));
	                        res.setResponseMessage(ftp.getReplyString());		                    
		                } else {
    		                long bytes = IOUtils.copy(input,target);
    		                ftpOK = bytes > 0;
    						if (saveResponse){
    							res.setResponseData(baos.toByteArray());
    							if (!binaryTransfer) {
    							    res.setDataType(SampleResult.TEXT);
    							}
    		                } else {
    		                	res.setBytes((int) bytes);
    		                }
		                }
		            }

		            if (ftpOK) {
		            	res.setResponseCodeOK();
			            res.setResponseMessageOK();
			    		res.setSuccessful(true);
		            } else {
		            	res.setResponseCode(Integer.toString(ftp.getReplyCode()));
		            	res.setResponseMessage(ftp.getReplyString());
		            }
	            } else {
	            	res.setResponseCode(Integer.toString(ftp.getReplyCode()));
	            	res.setResponseMessage(ftp.getReplyString());
	            }
            } else {
            	res.setResponseCode("501"); // TODO
            	res.setResponseMessage("Could not connect");            	
            	//res.setResponseCode(Integer.toString(ftp.getReplyCode()));
            	res.setResponseMessage(ftp.getReplyString());
            }
		} catch (IOException ex) {
        	res.setResponseCode("000"); // TODO
            res.setResponseMessage(ex.toString());
        } finally {
            if (ftp != null && ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ignored) {
                }
            }
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }

		res.sampleEnd();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4657.java