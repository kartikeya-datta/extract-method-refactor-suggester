error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18321.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18321.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18321.java
text:
```scala
private static final L@@ogger log = LoggingManager.getLoggerForClass();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *  
 */

package org.apache.jmeter.protocol.http.sampler;

import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.util.accesslog.Filter;
import org.apache.jmeter.protocol.http.util.accesslog.LogParser;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestCloneable;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterException;
import org.apache.log.Logger;

/**
 * Description: <br>
 * <br>
 * AccessLogSampler is responsible for a couple of things:
 * <p>
 * <ul>
 * <li>creating instances of Generator
 * <li>creating instances of Parser
 * <li>triggering popup windows
 * <li>calling Generator.generateRequest()
 * <li>checking to make sure the classes are valid
 * <li>making sure a class can be instantiated
 * </ul>
 * The intent of this sampler is it uses the generator and parser to create a
 * HTTPSampler when it is needed. It does not contain logic about how to parse
 * the logs. It also doesn't care how Generator is implemented, as long as it
 * implements the interface. This means a person could simply implement a dummy
 * parser to generate random parameters and the generator consumes the results.
 * This wasn't the original intent of the sampler. I originaly wanted to write
 * this sampler, so that I can take production logs to simulate production
 * traffic in a test environment. Doing so is desirable to study odd or unusual
 * behavior. It's also good to compare a new system against an existing system
 * to get near apples- to-apples comparison. I've been asked if benchmarks are
 * really fair comparisons just about every single time, so this helps me
 * accomplish that task.
 * <p>
 * Some bugs only appear under production traffic, so it is useful to generate
 * traffic using production logs. This way, JMeter can record when problems
 * occur and provide a way to match the server logs.
 * <p>
 * Created on: Jun 26, 2003
 * 
 */
public class AccessLogSampler extends HTTPSampler implements TestBean,ThreadListener {
	private static Logger log = LoggingManager.getLoggerForClass();

	private static final long serialVersionUID = 232L; // Remember to change this when the class changes ...
	
	public static final String DEFAULT_CLASS = "org.apache.jmeter.protocol.http.util.accesslog.TCLogParser"; // $NON-NLS-1$

	/** private members used by class * */
	transient private LogParser PARSER = null;

	// NOTUSED private Class PARSERCLASS = null;
	private String logFile, parserClassName, filterClassName;

	transient private Filter filter;

	private int count = 0;

	private boolean started = false;

	/**
	 * Set the path where XML messages are stored for random selection.
	 */
	public void setLogFile(String path) {
		logFile = path;
	}

	/**
	 * Get the path where XML messages are stored. this is the directory where
	 * JMeter will randomly select a file.
	 */
	public String getLogFile() {
		return logFile;
	}

	/**
	 * it's kinda obvious, but we state it anyways. Set the xml file with a
	 * string path.
	 * 
	 * @param classname -
	 *            parser class name
	 */
	public void setParserClassName(String classname) {
		parserClassName = classname;
	}

	/**
	 * Get the file location of the xml file.
	 * 
	 * @return String file path.
	 */
	public String getParserClassName() {
		return parserClassName;
	}

	/**
	 * sample gets a new HTTPSampler from the generator and calls it's sample()
	 * method.
	 */
	public SampleResult sampleWithParser() {
		initFilter();
		instantiateParser();
		SampleResult res = null;
		try {

			if (PARSER == null) {
				throw new JMeterException("No Parser available");
			}
			/*
			 * samp.setDomain(this.getDomain()); samp.setPort(this.getPort());
			 */
			// we call parse with 1 to get only one.
			// this also means if we change the implementation
			// to use 2, it would use every other entry and
			// so on. Not that it is really useful, but a
			// person could use it that way if they have a
			// huge gigabyte log file and they only want to
			// use a quarter of the entries.
			int thisCount = PARSER.parseAndConfigure(1, this);
			if (thisCount < 0) // Was there an error?
			{
				return errorResult(new Error("Problem parsing the log file"), new HTTPSampleResult());
			}
			if (thisCount == 0) {
				if (count == 0 || filter == null) {
					JMeterContextService.getContext().getThread().stop();
				}
				if (filter != null) {
					filter.reset();
				}
				CookieManager cm = getCookieManager();
				if (cm != null) {
					cm.clear();
				}
				count = 0;
				return errorResult(new Error("No entries found"), new HTTPSampleResult());
			}
			count = thisCount;
			res = sample();
			res.setSampleLabel(toString());
		} catch (Exception e) {
			log.warn("Sampling failure", e);
			return errorResult(e, new HTTPSampleResult());
		}
		return res;
	}

	/**
	 * sample(Entry e) simply calls sample().
	 * 
	 * @param e -
	 *            ignored
	 * @return the new sample
	 */
	public SampleResult sample(Entry e) {
		return sampleWithParser();
	}

	/**
	 * Method will instantiate the log parser based on the class in the text
	 * field. This was done to make it easier for people to plugin their own log
	 * parser and use different log parser.
	 */
	public void instantiateParser() {
		if (PARSER == null) {
			try {
				if (this.getParserClassName() != null && this.getParserClassName().length() > 0) {
					if (this.getLogFile() != null && this.getLogFile().length() > 0) {
						PARSER = (LogParser) Class.forName(getParserClassName()).newInstance();
						PARSER.setSourceFile(this.getLogFile());
						PARSER.setFilter(filter);
					} else {
						log.error("No log file specified");
					}
				}
			} catch (InstantiationException e) {
				log.error("", e);
			} catch (IllegalAccessException e) {
				log.error("", e);
			} catch (ClassNotFoundException e) {
				log.error("", e);
			}
		}
	}

	/**
	 * @return Returns the filterClassName.
	 */
	public String getFilterClassName() {
		return filterClassName;
	}

	/**
	 * @param filterClassName
	 *            The filterClassName to set.
	 */
	public void setFilterClassName(String filterClassName) {
		this.filterClassName = filterClassName;
	}

	/**
	 * @return Returns the domain.
	 */
	public String getDomain() { // N.B. Must be in this class for the TestBean code to work
		return super.getDomain();
	}

	/**
	 * @param domain
	 *            The domain to set.
	 */
	public void setDomain(String domain) { // N.B. Must be in this class for the TestBean code to work
		super.setDomain(domain);
	}

	/**
	 * @return Returns the imageParsing.
	 */
	public boolean isImageParsing() {
		return super.isImageParser();
	}

	/**
	 * @param imageParsing
	 *            The imageParsing to set.
	 */
	public void setImageParsing(boolean imageParsing) {
		super.setImageParser(imageParsing);
	}

	/**
	 * @return Returns the port.
	 */
	public String getPortString() {
		return super.getPropertyAsString(HTTPSamplerBase.PORT);
	}

	/**
	 * @param port
	 *            The port to set.
	 */
	public void setPortString(String port) {
		super.setProperty(HTTPSamplerBase.PORT, port);
	}

	/**
	 * 
	 */
	public AccessLogSampler() {
		super();
	}

	protected void initFilter() {
		if (filter == null && filterClassName != null && filterClassName.length() > 0) {
			try {
				filter = (Filter) Class.forName(filterClassName).newInstance();
			} catch (Exception e) {
				log.warn("Couldn't instantiate filter '" + filterClassName + "'", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		AccessLogSampler s = (AccessLogSampler) super.clone();
		if (started) {
			if (filterClassName != null && filterClassName.length() > 0) {

				try {
					if (TestCloneable.class.isAssignableFrom(Class.forName(filterClassName))) {
						initFilter();
						s.filter = (Filter) ((TestCloneable) filter).clone();
					}
                    if(TestCloneable.class.isAssignableFrom(Class.forName(parserClassName)))
                    {
                        instantiateParser();
                        s.PARSER = (LogParser)((TestCloneable)PARSER).clone();
                        if(filter != null)
                        {
                            s.PARSER.setFilter(s.filter);
                        }
                    }
				} catch (Exception e) {
					log.warn("Could not clone cloneable filter", e);
				}
			}
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded()
	 */
	public void testEnded() {
		if (PARSER != null) {
			PARSER.close();
		}
        filter = null;
		started = false;
		super.testEnded();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted()
	 */
	public void testStarted() {
		started = true;
		super.testStarted();
	}

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.AbstractTestElement#threadFinished()
     */
    public void threadFinished() {
        if(PARSER instanceof ThreadListener) {
            ((ThreadListener)PARSER).threadFinished();
        }
        if(filter instanceof ThreadListener) {
            ((ThreadListener)filter).threadFinished();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18321.java