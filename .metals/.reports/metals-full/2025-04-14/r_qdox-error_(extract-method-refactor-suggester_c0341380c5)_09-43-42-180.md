error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9020.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9020.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9020.java
text:
```scala
r@@es.setResponseCode("200");

/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.jmeter.protocol.mail.sampler;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author Thad Smith
 */
public class MailReaderSampler extends AbstractSampler {
	private static Logger log = LoggingManager.getLoggerForClass();

	// Where we keep all of the mail connection information
	// NOTUSED private Properties props = new Properties();

	// Static data identifiers
	private final static String SERVER_TYPE = "host_type";

	private final static String SERVER = "host";

	private final static String USERNAME = "username";

	private final static String PASSWORD = "password";

	private final static String FOLDER = "folder";

	private final static String DELETE = "delete";

	private final static String NUM_MESSAGES = "num_messages";

	// Static data values
	public final static String TYPE_POP3 = "pop3";

	public final static String TYPE_IMAP = "imap";

	public MailReaderSampler() {
		setServerType(TYPE_POP3);
		setFolder("INBOX");
		setNumMessages(-1);
		setDeleteMessages(false);
	}

	/*
	 * (non-Javadoc) Performs the sample, and returns the result
	 * 
	 * @see org.apache.jmeter.samplers.Sampler#sample(org.apache.jmeter.samplers.Entry)
	 */
	public SampleResult sample(Entry e) {
		SampleResult res = new SampleResult();
		boolean isOK = false; // Did sample succeed?

		res.setSampleLabel(getName());
		/*
		 * Perform the sampling
		 */
		res.sampleStart(); // Start timing
		try {
			// Create empty properties
			Properties props = new Properties();

			// Get session
			Session session = Session.getDefaultInstance(props, null);

			// Get the store
			Store store = session.getStore(getServerType());
			store.connect(getServer(), getUserName(), getPassword());

			// Get folder
			Folder folder = store.getFolder(getFolder());
			folder.open(Folder.READ_WRITE);

			// Get directory
			Message messages[] = folder.getMessages();
			Message message;
			StringBuffer data = new StringBuffer();
			data.append(messages.length + " new messages\n");

			int n = getNumMessages();
			if (n == -1 || n > messages.length)
				n = messages.length;

			for (int i = 0; i < n; i++) {
				message = messages[i];

				if (i == 0)
					res.setContentType(message.getContentType());

				data.append("Message " + message.getMessageNumber() + ":\n");
				data.append("Date: " + message.getSentDate() + "\n");

				data.append("To: ");
				Address[] recips = message.getAllRecipients();
				for (int j = 0; j < recips.length; j++) {
					data.append(recips[j].toString());
					if (j < recips.length - 1)
						data.append("; ");
				}
				data.append("\n");

				data.append("From: ");
				Address[] from = message.getFrom();
				for (int j = 0; j < from.length; j++) {
					data.append(from[j].toString());
					if (j < from.length - 1)
						data.append("; ");
				}
				data.append("\n");

				data.append("Subject: " + message.getSubject() + "\n");
				data.append("\n" + message.getContent());

				if (getDeleteMessages()) {
					message.setFlag(Flags.Flag.DELETED, true);
				}
			}

			// Close connection
			folder.close(true);
			store.close();

			/*
			 * Set up the sample result details
			 */
			res.setSamplerData(getServerType() + "://" + getUserName() + "@" + getServer());
			res.setResponseData(data.toString().getBytes());
			res.setDataType(SampleResult.TEXT);

			res.setResponseCodeOK();
			res.setResponseMessage("OK");
			isOK = true;
		} catch (Exception ex) {
			log.debug("", ex);
			res.setResponseCode("500");
			res.setResponseMessage(ex.toString());
		}

		res.sampleEnd();
		res.setSuccessful(isOK);
		return res;
	}

	/**
	 * Sets the type of protocol to use when talking with the remote mail
	 * server. Either MailReaderSampler.TYPE_IMAP or
	 * MailReaderSampler.TYPE_POP3. Default is MailReaderSampler.TYPE_POP3.
	 * 
	 * @param serverType
	 */
	public void setServerType(String serverType) {
		setProperty(SERVER_TYPE, serverType);
	}

	/**
	 * Returns the type of the protocol set to use when talking with the remote
	 * server. Either MailReaderSampler.TYPE_IMAP or
	 * MailReaderSampler.TYPE_POP3.
	 * 
	 * @return Server Type
	 */
	public String getServerType() {
		return getProperty(SERVER_TYPE).toString();
	}

	/**
	 * @param server -
	 *            The name or address of the remote server.
	 */
	public void setServer(String server) {
		setProperty(SERVER, server);
	}

	/**
	 * @return The name or address of the remote server.
	 */
	public String getServer() {
		return getProperty(SERVER).toString();
	}

	/**
	 * @param username -
	 *            The username of the mail account.
	 */
	public void setUserName(String username) {
		setProperty(USERNAME, username);
	}

	/**
	 * @return The username of the mail account.
	 */
	public String getUserName() {
		return getProperty(USERNAME).toString();
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		setProperty(PASSWORD, password);
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return getProperty(PASSWORD).toString();
	}

	/**
	 * @param folder -
	 *            Name of the folder to read emails from. "INBOX" is the only
	 *            acceptable value if the server type is POP3.
	 */
	public void setFolder(String folder) {
		setProperty(FOLDER, folder);
	}

	/**
	 * @return folder
	 */
	public String getFolder() {
		return getProperty(FOLDER).toString();
	}

	/**
	 * @param num_messages -
	 *            The number of messages to retrieve from the mail server. Set
	 *            this value to -1 to retrieve all messages.
	 */
	public void setNumMessages(int num_messages) {
		setProperty(new IntegerProperty(NUM_MESSAGES, num_messages));
	}

	/**
	 * @return The number of messages to retrive from the mail server. -1
	 *         denotes get all messages.
	 */
	public int getNumMessages() {
		return getPropertyAsInt(NUM_MESSAGES);
	}

	/**
	 * @param delete -
	 *            Whether or not to delete the read messages from the folder.
	 */
	public void setDeleteMessages(boolean delete) {
		setProperty(new BooleanProperty(DELETE, delete));
	}

	/**
	 * @return Whether or not to delete the read messages from the folder.
	 */
	public boolean getDeleteMessages() {
		return getPropertyAsBoolean(DELETE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9020.java