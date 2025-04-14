error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1508.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1508.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1508.java
text:
```scala
transient private static L@@ogger log =

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.protocol.http.modifier;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.Modifier;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestListener;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
/************************************************************
 *  Title: Jakarta-JMeter Description: Copyright: Copyright (c) 2001 Company:
 *  Apache
 *
 *<P>This modifier will replace any http sampler's url parameter values with
 * parameter values defined in a XML file for each simulated user
 *<BR>
 *<P>For example if userid and password are defined in the XML parameter file
 * for each user (ie thread), then simulated multiple user activity can occur
 *@author     Mark Walsh
 *@created    $Date$
 *@version    1.0
 ***********************************************************/
public class UserParameterModifier
	extends ConfigTestElement
	implements Modifier, Serializable, TestListener
{
	private static Logger log =
		Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.protocol.http");
	private static final String XMLURI = "UserParameterModifier.xmluri";
	//-------------------------------------------
	// Constants and Data Members
	//-------------------------------------------
	private UserSequence allAvailableUsers;
	//-------------------------------------------
	// Constructors
	//-------------------------------------------
	/**
	 * Default constructor
	 */
	public UserParameterModifier()
	{
	} //end constructor
	//-------------------------------------------
	// Methods
	//-------------------------------------------
	/*----------------------------------------------------------------------------------------------
	 * Methods overriden/implemented from org.apache.jmeter.config.AbstractConfigElement
	 *--------------------------------------------------------------------------------------------*/
	/**
	 * Runs before the start of every test. Reload the Sequencer with the
	 * latest parameter data for each user
	 */
	public void testStarted()
	{
		// try to populate allUsers, if fail, leave as any empty set
		List allUsers = new LinkedList();
		try
		{
			UserParameterXMLParser readXMLParameters = new UserParameterXMLParser();
			allUsers = readXMLParameters.getXMLParameters(getXmlUri());
		}
		catch (Exception e)
		{
			// do nothing, now object allUsers contains an empty set
			log.error("Unable to read parameters from xml file " + getXmlUri());
			log.error(
				"No unique values for http requests will be substituted for each thread",
				e);
		}
		allAvailableUsers = new UserSequence(allUsers);
	}
	public void testEnded()
	{
	}
	public void testStarted(String host)
	{
		testStarted();
	}
	public void testEnded(String host)
	{
	}
	/*----------------------------------------------------------------------------------------------
	 * Methods implemented from interface org.apache.jmeter.config.Modifier
	 *--------------------------------------------------------------------------------------------*/
	/**
	 * Modifies an entry object to replace the value of any url parameter that matches a parameter
	 * name in the XML file.
	 *
	 * @param entry Entry object containing information about the current test
	 * @return <code>True</code> if modified, else <code>false</code>
	 */
	public boolean modifyEntry(Sampler entry)
	{
		if (!(entry instanceof HTTPSampler))
		{
			return false;
		}
		HTTPSampler config = (HTTPSampler) entry;
		Map currentUser = allAvailableUsers.getNextUserMods();
		boolean changeValue = false;
		Iterator iter = config.getArguments().iterator();
		while (iter.hasNext())
		{
			Argument arg = (Argument) iter.next();
			// if parameter name exists in http request
			// then change its value
			// (Note: each jmeter thread (ie user) gets to have unique values)			
			if (currentUser.containsKey(arg.getName()))
			{
				arg.setValue(currentUser.get(arg.getName()));
			}
		}
		return changeValue;
	}
	/*----------------------------------------------------------------------------------------------
	 * Methods (used by UserParameterModifierGui to get/set the name of XML parameter file)
	 *--------------------------------------------------------------------------------------------*/
	/**
	 * return the current XML file name to be read to obtain the parameter data for all users
	 * @return the name of the XML file containing parameter data for each user
	 */
	public String getXmlUri()
	{
		return this.getPropertyAsString(XMLURI);
	}
	/**
	 * From the GUI screen, set file name of XML to read
	 * @param the name of the XML file containing the HTTP name value pair parameters per user
	 */
	public void setXmlUri(String xmlURI)
	{
		setProperty(XMLURI, xmlURI);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1508.java