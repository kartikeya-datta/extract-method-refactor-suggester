error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17193.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17193.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17193.java
text:
```scala
l@@og.debug(getLabel()+" "+getFilename()+" "+getUsername()+" "+getPassword());

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

package org.apache.jmeter.protocol.tcp.sampler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * A sampler which understands Tcp requests.
 *
 */
public class TCPSampler extends AbstractSampler implements TestListener
{
	private static final Logger log = LoggingManager.getLoggerForClass();

    public final static String SERVER     = "TCPSampler.server";   //$NON-NLS-1$
    public final static String PORT       = "TCPSampler.port";     //$NON-NLS-1$
	public final static String FILENAME   = "TCPSampler.filename"; //$NON-NLS-1$
	public final static String CLASSNAME  = "TCPSampler.classname";//$NON-NLS-1$
	public final static String NODELAY    = "TCPSampler.nodelay";  //$NON-NLS-1$
	public final static String TIMEOUT    = "TCPSampler.timeout";  //$NON-NLS-1$
	public final static String REQUEST    = "TCPSampler.request";  //$NON-NLS-1$

	private final static String TCPKEY = "TCP"; //$NON-NLS-1$ key for HashMap
	private final static String ERRKEY = "ERR"; //$NON-NLS-1$ key for HashMap

	private static Set allSockets = new HashSet();// Keep track of connections to allow close

	// If set, this is the regex that is used to extract the status from the response
	//NOT implemented yet private final static String STATUS_REGEX = JMeterUtils.getPropDefault("tcp.status.regex","");
	
	// Otherwise, the response is scanned for these strings
	private final static String STATUS_PREFIX = JMeterUtils.getPropDefault("tcp.status.prefix","");
	private final static String STATUS_SUFFIX = JMeterUtils.getPropDefault("tcp.status.suffix","");
	
	private final static String STATUS_PROPERTIES = JMeterUtils.getPropDefault("tcp.status.properties","");
	private final static Properties statusProps = new Properties();
	private static boolean haveStatusProps = false;
	
	static
	{
		log.info("Protocol Handler name="+getClassname());
		log.info("Status prefix="+STATUS_PREFIX);
		log.info("Status suffix="+STATUS_SUFFIX);
		log.info("Status properties="+STATUS_PROPERTIES);
		if (STATUS_PROPERTIES.length()>0)
		{
	        File f = new File(STATUS_PROPERTIES);
	        try {
				statusProps.load(new FileInputStream(f));
				log.info("Successfully loaded properties");
				haveStatusProps = true;
			} catch (FileNotFoundException e) {
				log.info("Property file not found");
			} catch (IOException e) {
				log.info("Property file error "+e.toString());
			}
		}
	}
    
	/** the cache of TCP Connections */
	private static ThreadLocal tp = new ThreadLocal(){
		protected Object initialValue(){
			return new HashMap();
		}
	};

	private transient TCPClient protocolHandler;
	
	public TCPSampler()
	{
		log.debug("Created "+this);
		protocolHandler=getProtocol();
		log.debug("Using Protocol Handler: "
				+protocolHandler.getClass().getName());
	}

	private String getError(){
		Map cp = (Map) tp.get();
		return  (String) cp.get(ERRKEY);
	}

	private Socket getSocket() {
		Map cp = (Map) tp.get();
		Socket con = (Socket) cp.get(TCPKEY);
		if (con != null) {
			log.debug(this+" Reusing connection "+con); //$NON-NLS-1$
			return (Socket) con; 
		}
	
		// Not in cache, so create new one and cache it
		try
	    {
	        con = new Socket(getServer(),getPort());
			con.setSoTimeout(getTimeout());
			con.setTcpNoDelay(getNoDelay());
	        
			log.debug(this+"  Timeout "+getTimeout()+" NoDelay "+getNoDelay()); //$NON-NLS-1$
			log.debug("Created new connection "+con); //$NON-NLS-1$
			cp.put(TCPKEY,con);
			allSockets.add(con);// Save so can be closed
	    }
	    catch (UnknownHostException e)
	    {
	    	log.warn("Unknown host for "+getLabel(),e);//$NON-NLS-1$
			cp.put(ERRKEY,e.toString());
	    }
	    catch (IOException e)
	    {
			log.warn("Could not create socket for "+getLabel(),e); //$NON-NLS-1$
			cp.put(ERRKEY,e.toString());
	    }
		return con;
	}

	public String getUsername()
	{
		return getPropertyAsString(ConfigTestElement.USERNAME);
	}

	public String getPassword()
	{
		return getPropertyAsString(ConfigTestElement.PASSWORD);
	}

    public void setServer(String newServer)
    {
        this.setProperty(SERVER, newServer);
    }
    public String getServer()
    {
        return getPropertyAsString(SERVER);
    }
    public void setPort(String newFilename)
    {
        this.setProperty(PORT, newFilename);
    }
    public int getPort()
    {
        return getPropertyAsInt(PORT);
    }
    
	public void setFilename(String newFilename)
	{
		this.setProperty(FILENAME, newFilename);
	}
	public String getFilename()
	{
		return getPropertyAsString(FILENAME);
	}


	public void setRequestData(String newRequestData)
	{
		this.setProperty(REQUEST, newRequestData);
	}
	
	public String getRequestData()
	{
		return getPropertyAsString(REQUEST);
	}


	public void setTimeout(String newTimeout)
	{
		this.setProperty(FILENAME, newTimeout);
	}
	public int getTimeout()
	{
		return getPropertyAsInt(TIMEOUT);
	}


	public void setNoDelay(String newNoDelay)
	{
		this.setProperty(NODELAY, newNoDelay);
	}
	
	public boolean getNoDelay()
	{
		return getPropertyAsBoolean(NODELAY);
	}



    /**
     * Returns a formatted string label describing this sampler
     * Example output:
     *      Tcp://Tcp.nowhere.com/pub/README.txt
     *
     * @return a formatted string label describing this sampler
     */
    public String getLabel()
    {
        return ("tcp://" + this.getServer() + ":" + this.getPort());//$NON-NLS-1$
    }

	private static String getClassname()
	{
		String className = JMeterUtils.getPropDefault("tcp.handler","TCPClientImpl");
		return className;
	}

	private static final String protoPrefix = "org.apache.jmeter.protocol.tcp.sampler."; 
	private Class getClass(String className)
	{
		Class c = null;
		try
        {
            c = Class.forName(className
            	,false,Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
			try
            {
                c = Class.forName(protoPrefix+className
                	,false,Thread.currentThread().getContextClassLoader());
            }
            catch (ClassNotFoundException e1)
            {
            	log.error("Could not find protocol class "+ className);
            }
        }
		return c;
        
	}

    private TCPClient getProtocol(){
    	TCPClient TCPClient = null;
    	Class javaClass = getClass(getClassname());
		try
		{
			TCPClient = (TCPClient) javaClass.newInstance();
			if (log.isDebugEnabled())
			{
				log.debug(this
						+ "Created: "
						+ getClassname()
						+ "@"
						+ Integer.toHexString(TCPClient.hashCode()));
			}
		}
		catch (Exception e)
		{
			log.error(
				this + " Exception creating: " + getClassname(),e);
		}
		return TCPClient;
    }

    public SampleResult sample(Entry e)// Entry tends to be ignored ...
    {
    	log.info(getLabel()+" "+getFilename()+" "+getUsername()+" "+getPassword());
        SampleResult res = new SampleResult();
        boolean isSuccessful = false;
        res.setSampleLabel(getName());//Use the test element name for the label
        res.sampleStart();
        try
        {
			Socket sock = getSocket();
			if (sock == null){
				res.setResponseCode("500");
				res.setResponseMessage(getError());
			} else {
				InputStream is = sock.getInputStream();
				OutputStream os = sock.getOutputStream();
				String req = getRequestData();
				//TODO handle filenames
				res.setSamplerData(req);
				protocolHandler.write(os,req);
				String in = protocolHandler.read(is);
	            res.setResponseData(in.getBytes());
	            res.setDataType(SampleResult.TEXT);
	            res.setResponseCode("200");
	            res.setResponseMessage("OK");
	            isSuccessful = true;
	            //Reset the status code if the message contains one
	            if (STATUS_PREFIX.length() > 0)
	            {
	            	int i = in.indexOf(STATUS_PREFIX);
            		int j = in.indexOf(STATUS_SUFFIX,i+STATUS_PREFIX.length());
	            	if (i != -1 && j > i)
	            	{
	            		String rc = in.substring(i+STATUS_PREFIX.length(),j);
	            		res.setResponseCode(rc);
	            		isSuccessful = checkResponseCode(rc);
	            		if (haveStatusProps)
	            		{
		            		res.setResponseMessage(
		            				statusProps.getProperty(rc,"Status code not found in properties"));
	            		}
	            		else
	            		{
	            			res.setResponseMessage("No status property file");
	            		}
	            	}
	            	else
	            	{
	            		res.setResponseCode("999");
	    	            res.setResponseMessage("Status value not found");
	    	            isSuccessful=false;
	            	}
	            } 
			}
        }
        catch (Exception ex)
        {
        	log.debug("",ex);
			res.setResponseCode("500");
            res.setResponseMessage(ex.toString());
        }

        // Calculate response time
        res.sampleEnd();

        // Set if we were successful or not
        res.setSuccessful(isSuccessful);

        return res;
    }

     /**
	 * @param rc response code
	 * @return whether this represents success or not
	 */
	private boolean checkResponseCode(String rc) {
		if (rc.compareTo("400")>=0 && rc.compareTo("499")<=0)
		{
			return false;
		}
		if (rc.compareTo("500")>=0 && rc.compareTo("599")<=0)
		{
			return false;
		}
		return true;
	}

	private void disconnectAll(){
		synchronized (allSockets)
		{
			Iterator i = allSockets.iterator();
			while (i.hasNext())
			{
				Socket socket = (Socket) i.next();
				try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                    log.warn("Error closing socket ",e);
                } finally {
					i.remove();
                }
			}
		}
     }


	 /* (non-Javadoc)
	  * @see org.apache.jmeter.testelement.TestListener#testStarted()
	  */
	 public void testStarted() // Only called once per class?
	 {
		 log.debug(this+" test started");
	 }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestListener#testEnded()
     */
    public void testEnded() // Only called once per class?
    {
		log.debug(this+" test ended");
		disconnectAll();
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestListener#testStarted(java.lang.String)
     */
    public void testStarted(String host)
    {
		log.debug(this+" test started on "+host);
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestListener#testEnded(java.lang.String)
     */
    public void testEnded(String host)
    {
		log.debug(this+" test ended on "+host);
		disconnectAll();
        
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestListener#testIterationStart(org.apache.jmeter.engine.event.LoopIterationEvent)
     */
    public void testIterationStart(LoopIterationEvent event)
    {
		log.debug(this+" test iteration start on "+event.getIteration());
        // TODO Auto-generated method stub
        
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17193.java