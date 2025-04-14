error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5551.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5551.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5551.java
text:
```scala
A@@ssertionResult result = new AssertionResult(getName());

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

/**
 * MD5HexAssertion class creates an MD5 checksum from the response <br/>
 * and matches it with the MD5 hex provided.
 * The assertion will fail when the expected hex is different from the <br/>
 * one calculated from the response OR when the expected hex is left empty.
 * 
 * @author	<a href="mailto:jh@domek.be">Jorg Heymans</a>
 * @version $Revision$ last updated $Date$
 */
package org.apache.jmeter.assertions;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class MD5HexAssertion extends AbstractTestElement implements Serializable, Assertion {

	private static final Logger log = LoggingManager.getLoggerForClass();

	/** Key for storing assertion-informations in the jmx-file. */
	private static final String MD5HEX_KEY = "MD5HexAssertion.size";

	/*
	 * @param response @return
	 */
	public AssertionResult getResult(SampleResult response) {

		AssertionResult result = new AssertionResult();
		result.setFailure(false);
		byte[] resultData = response.getResponseData();

		if (resultData.length == 0) {
			result.setError(false);
			result.setFailure(true);
			result.setFailureMessage("Response was null");
			return result;
		}

		// no point in checking if we don't have anything to compare against
		if (getAllowedMD5Hex().equals("")) {
			result.setError(false);
			result.setFailure(true);
			result.setFailureMessage("MD5Hex to test against is empty");
			return result;
		}

		String md5Result = baMD5Hex(resultData);

		// String md5Result = DigestUtils.md5Hex(resultData);

		if (!md5Result.equalsIgnoreCase(getAllowedMD5Hex())) {
			result.setFailure(true);

			Object[] arguments = { md5Result, getAllowedMD5Hex() };
			String message = MessageFormat.format(JMeterUtils.getResString("md5hex_assertion_failure"), arguments);
			result.setFailureMessage(message);

		}

		return result;
	}

	public void setAllowedMD5Hex(String hex) {
		setProperty(new StringProperty(MD5HexAssertion.MD5HEX_KEY, hex));
	}

	public String getAllowedMD5Hex() {
		return getPropertyAsString(MD5HexAssertion.MD5HEX_KEY);
	}

	// package protected so can be accessed by test class
	static String baToHex(byte ba[]) {
		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < ba.length; i++) {
			int j = ba[i] & 0xff;
			if (j < 16)
				sb.append("0");
			sb.append(Integer.toHexString(j));
		}
		return sb.toString();
	}

	// package protected so can be accessed by test class
	static String baMD5Hex(byte ba[]) {
		byte[] md5Result = {};

		try {
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			md5Result = md.digest(ba);
		} catch (NoSuchAlgorithmException e) {
			log.error("", e);
		}
		return baToHex(md5Result);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5551.java