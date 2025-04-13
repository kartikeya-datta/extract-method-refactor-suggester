error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18116.java
text:
```scala
H@@eader header = (Header) getHeaders().get(i).getObjectValue();

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.util.JMeterUtils;

/**
 * This class provides an interface to headers file to
 * pass HTTP headers along with a request.
 *
 * @author  <a href="mailto:giacomo@apache.org">Giacomo Pati</a>
 * @version $Revision$ $Date$
 */
public class HeaderManager extends ConfigTestElement implements
		Serializable
{

	 public static final String HEADERS = "HeaderManager.headers";
	 /**
	  * A vector of Headers managed by this class.
	  * @associates <{org.apache.jmeter.controllers.Header}>
	  */


	 private final static int columnCount = 2;
	 private final static String[] columnNames = {
		JMeterUtils.getResString("name"),
		JMeterUtils.getResString("value")
	 };

	 private static List addableList = new LinkedList();

	 public HeaderManager () {
		setProperty(new CollectionProperty(HEADERS,new ArrayList()));
	 }


	 public CollectionProperty getHeaders() {
		  return (CollectionProperty)getProperty(HEADERS);
	 }

	 public int getColumnCount() {
			  return columnCount;
		 }

	public String getColumnName(int column) {
			  return columnNames[column];
		 }

	public Class getColumnClass(int column) {
			  return columnNames[column].getClass();
		 }

	public Header getHeader(int row)
	{
		return (Header)getHeaders().get(row).getObjectValue();
	}

	 /** save the header data to a file */
	 public void save(String headFile) throws IOException {
		  File file = new File(headFile);
		  if (!file.isAbsolute()) {
				file = new File(System.getProperty("user.dir") + File.separator + headFile);
		  }
		  PrintWriter writer = new PrintWriter(new FileWriter(file));
		  writer.println("# JMeter generated Header file");
		  for (int i = 0; i < getHeaders().size(); i++) {
				Header head = (Header) getHeaders().get(i);
				writer.println(head.toString());
		  }
		  writer.flush();
		  writer.close();
	 }

	 /** add header data from a file */
	 public void addFile (String headerFile) throws IOException {
		  File file = new File(headerFile);
		  if (!file.isAbsolute()) {
				file = new File(System.getProperty("user.dir") + File.separator + headerFile);
		  }
		  BufferedReader reader = null;
		  if (file.canRead()) {
				reader = new BufferedReader(new FileReader(file));
		  } else {
				throw new IOException("The file you specified cannot be read.");
		  }

		  String line;
		  while ((line = reader.readLine()) != null) {
				try {
					 if (line.startsWith("#") || line.trim().length() == 0) {
						  continue;
					 }
					 String[] st = split(line, "\t"," ");
					 int name = 0;
					 int value = 1;
					 Header header = new Header(st[name], st[value]);
					 getHeaders().addItem(header);
				} catch (Exception e) {
					 throw new IOException("Error parsing header line\n\t'" + line + "'\n\t" + e);
				}
		  }
		  reader.close();
	 }

	 /** add a header */
	 public void add(Header h) {
		  getHeaders().addItem(h);
	 }

	 /** add an empty header */
	 public void add() {
		getHeaders().addItem(new Header());
	 }

	 /** remove a header */
	 public void remove(int index) {
		  getHeaders().remove(index);
	 }

	 /** return the number headers */
	 public int size() {
		  return getHeaders().size();
	 }

	 /** return the header at index i */
	 public Header get(int i) {
		  return (Header) getHeaders().get(i).getObjectValue();
	 }

	 /*
	 public String getHeaderHeaderForURL(URL url) {
		  if (!url.getProtocol().toUpperCase().trim().equals("HTTP") &&
					 ! url.getProtocol().toUpperCase().trim().equals("HTTPS")) {
				return null;
		  }

		  StringBuffer sbHeader = new StringBuffer();
		  for (Iterator enum = headers.iterator(); enum.hasNext();) {
				Header header = (Header) enum.next();
				if (url.getHost().endsWith(header.getDomain()) &&
						  url.getFile().startsWith(header.getPath()) &&
						  (System.currentTimeMillis() / 1000) <= header.getExpires()) {
					 if (sbHeader.length() > 0) {
						  sbHeader.append("; ");
					 }
					 sbHeader.append(header.getName()).append("=").append(header.getValue());
				}
		  }

		  if (sbHeader.length() != 0) {
				return sbHeader.toString();
		  } else {
				return null;
		  }
	 }
	 */

	 /*
	 public void addHeaderFromHeader(String headerHeader, URL url) {
		  StringTokenizer st = new StringTokenizer(headerHeader, ";");
		  String nvp;

		  // first n=v is name=value
		  nvp = st.nextToken();
		  int index = nvp.indexOf("=");
		  String name = nvp.substring(0,index);
		  String value = nvp.substring(index+1);
		  String domain = url.getHost();

		  Header newHeader = new Header(name, value);
		  // check the rest of the headers
		  while (st.hasMoreTokens()) {
				nvp = st.nextToken();
				nvp = nvp.trim();
				index = nvp.indexOf("=");
				if(index == -1) {
					 index = nvp.length();
				}
				String key = nvp.substring(0,index);

				Vector removeIndices = new Vector();
				for (int i = headers.size() - 1; i >= 0; i--) {
				Header header = (Header) headers.get(i);
				if (header == null) {
					 continue;
				}
				if (header.getName().equals(newHeader.getName())) {
					 removeIndices.addElement(new Integer(i));
				}
		  }

		  for (Enumeration e = removeIndices.elements(); e.hasMoreElements();) {
				index = ((Integer) e.nextElement()).intValue();
				headers.remove(index);
		  }

	 }
	 */

	 public void removeHeaderNamed(String name) {
		  Vector removeIndices = new Vector();
		  for (int i = getHeaders().size() - 1; i > 0; i--) {
				Header header = (Header) getHeaders().get(i);
				if (header == null) {
					 continue;
				}
				if (header.getName().equalsIgnoreCase(name)) {
					 removeIndices.addElement(new Integer(i));
				}
		  }

		  for (Enumeration e = removeIndices.elements(); e.hasMoreElements();) {
				getHeaders().remove(((Integer) e.nextElement()).intValue());
		  }
	 }

	 /******************************************************
	  * Takes a String and a tokenizer character, and returns
		 a new array of strings of the string split by the tokenizer
		 character.
		 @param splittee String to be split
		 @param splitChar Character to split the string on
		 @param def Default value to place between two split chars that have
		 nothing between them
		 @return Array of all the tokens.
	  ******************************************************/
	 public String[] split(String splittee, String splitChar, String def) {
		  if(splittee == null || splitChar == null) {
				return new String[0];
		  }
		  StringTokenizer tokens;
		  String temp;
		  int spot;
		  while((spot=splittee.indexOf(splitChar + splitChar))!=-1) {
				splittee=splittee.substring(0, spot + splitChar.length()) + def +
					 splittee.substring(spot + (1*splitChar.length()), splittee.length());
		  }
		  Vector returns=new Vector();
		  tokens = new StringTokenizer(splittee, splitChar);
		  while(tokens.hasMoreTokens()) {
				temp = (String)tokens.nextToken();
				returns.addElement(temp);
		  }
		  String[] values=new String[returns.size()];
		  returns.copyInto(values);
		  return values;
	 }

	 public String getClassLabel() {
		return JMeterUtils.getResString("header_manager_title");
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18116.java