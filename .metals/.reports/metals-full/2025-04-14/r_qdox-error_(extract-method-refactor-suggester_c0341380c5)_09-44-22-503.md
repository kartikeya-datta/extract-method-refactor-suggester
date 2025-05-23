error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13769.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13769.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13769.java
text:
```scala
r@@eturn auth.toBasicHeader();

// $Header$
/*
 * Copyright 2001-2005 The Apache Software Foundation.
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

package org.apache.jmeter.protocol.http.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.protocol.http.util.Base64Encoder;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class provides a way to provide Authorization in jmeter requests. The
 * format of the authorization file is: URL user pass where URL is an HTTP URL,
 * user a username to use and pass the appropriate password.
 * 
 * @author <a href="mailto:luta.raphael@networks.vivendi.com">Raphael Luta</a>
 * @version $Revision$
 */
public class AuthManager extends ConfigTestElement implements ConfigElement, Serializable {
	private static final Logger log = LoggingManager.getLoggerForClass();

	private final static String AUTH_LIST = "AuthManager.auth_list";

	private final static int columnCount = 3;

	private final static String[] columnNames = { JMeterUtils.getResString("auth_base_url"),
			JMeterUtils.getResString("username"), JMeterUtils.getResString("password") };

	/**
	 * Default Constructor.
	 */
	public AuthManager() {
		setProperty(new CollectionProperty(AUTH_LIST, new ArrayList()));
	}

	public void clear() {
		super.clear();
		setProperty(new CollectionProperty(AUTH_LIST, new ArrayList()));
	}

	/**
	 * Update an authentication record.
	 */
	public void set(int index, String url, String user, String pass) {
		Authorization auth = new Authorization(url, user, pass);
		if (index >= 0) {
			getAuthObjects().set(index, new TestElementProperty(auth.getName(), auth));
		} else {
			getAuthObjects().addItem(auth);
		}
	}

	public void setName(String newName) {
		setProperty(TestElement.NAME, newName);
	}

	public CollectionProperty getAuthObjects() {
		return (CollectionProperty) getProperty(AUTH_LIST);
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

	public Authorization getAuthObjectAt(int row) {
		return (Authorization) getAuthObjects().get(row).getObjectValue();
	}

	public boolean isEditable() {
		return true;
	}

	public String getClassLabel() {
		return JMeterUtils.getResString("auth_manager_title");
	}

	public Class getGuiClass() {
		return org.apache.jmeter.protocol.http.gui.AuthPanel.class;
	}

	public Collection getAddList() {
		return null;
	}

	/**
	 * Return the record at index i
	 */
	public Authorization get(int i) {
		return (Authorization) getAuthObjects().get(i).getObjectValue();
	}

	public String getAuthHeaderForURL(URL url) {
		Authorization auth = getAuthForURL(url);
		if (auth == null)
			return null;
		return "Basic " + Base64Encoder.encode(auth.getUser() + ":" + auth.getPass());
	}

	public Authorization getAuthForURL(URL url) {
		if (!isSupportedProtocol(url)) {
			return null;
		}

		// TODO: replace all this url2 mess with a proper method
		// "areEquivalent(url1, url2)" that
		// would also ignore case in protocol and host names, etc. -- use that
		// method in the CookieManager too.

		URL url2 = null;

		try {
			if (url.getPort() == -1) {
				// Obtain another URL with an explicit port:
				int port = url.getProtocol().equalsIgnoreCase("http") ? 80 : 443;
				// only http and https are supported
				url2 = new URL(url.getProtocol(), url.getHost(), port, url.getPath());
			} else if ((url.getPort() == 80 && url.getProtocol().equalsIgnoreCase("http"))
 (url.getPort() == 443 && url.getProtocol().equalsIgnoreCase("https"))) {
				url2 = new URL(url.getProtocol(), url.getHost(), url.getPath());
			}
		} catch (MalformedURLException e) {
			log.error("Internal error!", e); // this should never happen
			// anyway, we'll continue with url2 set to null.
		}

		String s1 = url.toString();
		String s2 = null;
		if (url2 != null)
			s2 = url2.toString();

		// TODO should really return most specific (i.e. longest) match.
		for (PropertyIterator iter = getAuthObjects().iterator(); iter.hasNext();) {
			Authorization auth = (Authorization) iter.next().getObjectValue();

			String uRL = auth.getURL();
			if (s1.startsWith(uRL) || s2 != null && s2.startsWith(uRL)) {
				return auth;
			}
		}
		return null;
	}

	public String getName() {
		return getPropertyAsString(TestElement.NAME);
	}

	public void addConfigElement(ConfigElement config) {
	}

	public void addAuth(Authorization auth) {
		getAuthObjects().addItem(auth);
	}

	public void addAuth() {
		getAuthObjects().addItem(new Authorization());
	}

	public boolean expectsModification() {
		return false;
	}

	public void uncompile() {
	}

	/**
	 * Save the authentication data to a file.
	 */
	public void save(String authFile) throws IOException {
		File file = new File(authFile);
		if (!file.isAbsolute()) {
			file = new File(System.getProperty("user.dir") + File.separator + authFile);
		}
		PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.println("# JMeter generated Authorization file");
		for (int i = 0; i < getAuthObjects().size(); i++) {
			Authorization auth = (Authorization) getAuthObjects().get(i).getObjectValue();
			writer.println(auth.toString());
		}
		writer.flush();
		writer.close();
	}

	/**
	 * Add authentication data from a file.
	 */
	public void addFile(String authFile) throws IOException {
		File file = new File(authFile);
		if (!file.isAbsolute()) {
			file = new File(System.getProperty("user.dir") + File.separator + authFile);
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
				StringTokenizer st = new StringTokenizer(line, "\t");
				String url = st.nextToken();
				String user = st.nextToken();
				String pass = st.nextToken();
				Authorization auth = new Authorization(url, user, pass);
				getAuthObjects().addItem(auth);
			} catch (Exception e) {
				reader.close();
				throw new IOException("Error parsing auth line\n\t'" + line + "'\n\t" + e);
			}
		}
		reader.close();
	}

	/**
	 * Remove an authentication record.
	 */
	public void remove(int index) {
		getAuthObjects().remove(index);
	}

	/**
	 * Return the number of records.
	 */
	public int getAuthCount() {
		return getAuthObjects().size();
	}

	private static boolean isSupportedProtocol(URL url) {
		return url.getProtocol().toUpperCase().equals("HTTP") || url.getProtocol().toUpperCase().equals("HTTPS");
	}

	// ////////////////////// UNIT TESTS ////////////////////////////

	public static class Test extends JMeterTestCase {
		public Test(String name) {
			super(name);
		}

		public void testHttp() throws Exception {
			assertTrue(isSupportedProtocol(new URL("http:")));
		}

		public void testHttps() throws Exception {
			assertTrue(isSupportedProtocol(new URL("https:")));
		}

		public void testFile() throws Exception {
			AuthManager am = new AuthManager();
			CollectionProperty ao = am.getAuthObjects();
			assertEquals(0, ao.size());
			am.addFile("testfiles/TestAuth.txt");
			assertEquals(5, ao.size());
			Authorization at;
			at = am.getAuthForURL(new URL("http://a.b.c/"));
			assertEquals("login", at.getUser());
			assertEquals("password", at.getPass());
			at = am.getAuthForURL(new URL("http://a.b.c/1"));
			assertEquals("login1", at.getUser());
			assertEquals("password1", at.getPass());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13769.java