error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1502.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1502.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1502.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

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

package org.apache.jmeter.protocol.http.control;

import java.io.Serializable;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 * This class is a Cookie encapsulator.
 *
 * @author  <a href="mailto:sdowd@arcmail.com">Sean Dowd</a>
 */
public class Cookie extends AbstractTestElement implements Serializable
{
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.protocol.http");
	 private static String NAME = "Cookie.name";
	 private static String VALUE = "Cookie.value";
	 private static String DOMAIN = "Cookie.domain";
	 private static String EXPIRES = "Cookie.expires";
	 private static String SECURE = "Cookie.secure";
	 private static String PATH = "Cookie.path";

	 /**
	  * create the coookie
	  */
	 public Cookie() {
		  this.setName("");
		  this.setValue("");
		  this.setDomain("");
		  this.setPath("");
		  this.setSecure(false);
		  this.setExpires(0);
	 }

	 /**
	  * create the coookie
	  */
	 public Cookie(String name, String value, String domain, String path, boolean secure, long expires) {
		  this.setName(name);
		  this.setValue(value);
		  this.setDomain(domain);
		  this.setPath(path);
		  this.setSecure(secure);
		  this.setExpires(expires);
	 }

	public void addConfigElement(ConfigElement config)
	{
	}

	public boolean expectsModification()
	{
		return false;
	}

	 public String getClassLabel()
	 {
		return "Cookie";
	 }

	 /**
	  * get the value for this object.
	  */
	 public String getValue() {
		  return (String)this.getProperty(VALUE);
	 }

	 /**
	  * set the value for this object.
	  */
	 public synchronized void setValue(String value) {
		  this.setProperty(VALUE,value);
	 }

	 /**
	  * get the domain for this object.
	  */
	 public String getDomain() {
		  return (String)getProperty(DOMAIN);
	 }

	 /**
	  * set the domain for this object.
	  */
	 public synchronized void setDomain(String domain) {
		  setProperty(DOMAIN,domain);
	 }

	 /**
	  * get the expires for this object.
	  */
	 public long getExpires() {
		Object ret = getProperty(EXPIRES);
		if(ret == null)
		{
			return 0;
		}
		else if(ret instanceof Long)
		{
			return ((Long)ret).longValue();
		}
		else if(ret instanceof String)
		{
			try
			{
				return Long.parseLong((String)ret);
			}
			catch (Exception ex)
			{
			}
		}
		return 0;
	 }

	 /**
	  * set the expires for this object.
	  */
	 public synchronized void setExpires(long expires) {
		  setProperty(EXPIRES,new Long(expires));
	 }

	 /**
	  * get the secure for this object.
	  */
	 public boolean getSecure() {
	 	log.info("Secure = "+getProperty(SECURE));
		return this.getPropertyAsBoolean(SECURE);
	 }

	 /**
	  * set the secure for this object.
	  */
	 public synchronized void setSecure(boolean secure) {
		  setProperty(SECURE,new Boolean(secure));
	 }

	 /**
	  * get the path for this object.
	  */
	 public String getPath() {
		  return (String)getProperty(PATH);
	 }

	 /**
	  * set the path for this object.
	  */
	 public synchronized void setPath(String path) {
		  setProperty(PATH,path);
	 }

	 /**
	  * creates a string representation of this cookie
	  */
	 public String toString() {
	return getDomain() + "\tTRUE\t" + getPath() + "\t" +
							 new Boolean(getSecure()).toString().toUpperCase() + "\t" +
							 getExpires() + "\t" + getName() + "\t" + getValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1502.java