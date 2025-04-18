error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12674.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12674.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12674.java
text:
```scala
final b@@yte[] hash = computeDigest(streamInfo.getStream());

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.request.resource.caching.version;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * computes the message digest of a {@link PackageResource} 
 * and uses it as a version string
 * <p/>
 * you can use any message digest algorithm that can be retrieved 
 * by Java Cryptography Architecture (JCA) on your current platform.
 * Check <a href="http://download.oracle.com/javase/1.5.0/docs/guide/security/CryptoSpec.html#AppA">here</a>
 * for more information on possible algorithms.
 * 
 * @author Peter Ertl
 * 
 * @since 1.5
 */
public class MessageDigestResourceVersion implements IResourceVersion
{
	private static final Logger log = LoggerFactory.getLogger(MessageDigestResourceVersion.class);

	private static final String DEFAULT_ALGORITHM = "MD5";
	private static final int DEFAULT_BUFFER_BYTES = 8192; // needed for javadoc {@value ..}
	private static final Bytes DEFAULT_BUFFER_SIZE = Bytes.bytes(DEFAULT_BUFFER_BYTES);

	/** 
	 * message digest algorithm for computing hashes 
	 */
	private final String algorithm;

	/** 
	 * buffer size for computing the digest 
	 */
	private final Bytes bufferSize;

	/**
	 * create an instance of the message digest 
	 * resource version provider using algorithm {@value #DEFAULT_ALGORITHM}
	 * 
	 * @see #MessageDigestResourceVersion(String) 
	 * @see #MessageDigestResourceVersion(String, org.apache.wicket.util.lang.Bytes)
	 */
	public MessageDigestResourceVersion()
	{
		this(DEFAULT_ALGORITHM, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * create an instance of the message digest resource version provider 
	 * using the specified algorithm. The algorithm name must be one
	 * that can be retrieved by Java Cryptography Architecture (JCA) 
	 * using {@link MessageDigest#getInstance(String)}. For digest computation
	 * an internal buffer of up to {@value #DEFAULT_BUFFER_BYTES}
	 * bytes will be used.
	 *
	 * @param algorithm
	 *            digest algorithm
	 *
	 * @see #MessageDigestResourceVersion()
	 * @see #MessageDigestResourceVersion(String, org.apache.wicket.util.lang.Bytes)
	 */
	public MessageDigestResourceVersion(String algorithm)
	{
		this(algorithm, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * create an instance of the message digest resource version provider 
	 * using the specified algorithm. The algorithm name must be one
	 * that can be retrieved by Java Cryptography Architecture (JCA) 
	 * using {@link MessageDigest#getInstance(String)}. For digest computation
	 * an internal buffer with a maximum size specified by parameter 
	 * <code>bufferSize</code> will be used. 
	 *
	 * @param algorithm
	 *            digest algorithm
	 * @param bufferSize
	 *            maximum size for internal buffer            
	 */
	public MessageDigestResourceVersion(String algorithm, Bytes bufferSize)
	{
		this.algorithm = Args.notEmpty(algorithm, "algorithm");
		this.bufferSize = Args.notNull(bufferSize, "bufferSize");
	}

	public String getVersion(PackageResourceReference resourceReference)
	{
		// get current stream information for package resource
		final PackageResourceReference.StreamInfo streamInfo = resourceReference.getCurrentStreamInfo();

		// if no stream info is available we can not provide a version
		if (streamInfo == null)
		{
			return null;
		}

		try
		{
			// get binary hash
			final byte[] hash = computeDigest(streamInfo.stream);

			// convert to hexadecimal
			return Strings.toHexString(hash);
		}
		catch (ResourceStreamNotFoundException e)
		{
			log.warn("resource stream not found for '{}'", resourceReference);
			return null;
		}
		catch (IOException e)
		{
			log.warn("resource stream not be read for " + resourceReference, e);
			return null;
		}
	}

	/**
	 * get instance of message digest provider from JCA
	 * 
	 * @return message digest provider
	 */
	protected MessageDigest getMessageDigest()
	{
		try
		{
			return MessageDigest.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new WicketRuntimeException("message digest " + algorithm + " not found", e);
		}
	}

	/**
	 * compute digest for resource stream
	 * 
	 * @param resourceStream
	 *            resource stream to compute message digest for
	 * 
	 * @return binary message digest
	 * 
	 * @throws ResourceStreamNotFoundException
	 * @throws IOException
	 */
	protected byte[] computeDigest(IResourceStream resourceStream)
		throws ResourceStreamNotFoundException, IOException
	{
		final MessageDigest digest = getMessageDigest();
		final InputStream inputStream = resourceStream.getInputStream();

		try
		{
			// get actual buffer size
			final int bufferLen = (int)Math.min(Integer.MAX_VALUE, bufferSize.bytes());

			// allocate read buffer
			final byte[] buf = new byte[bufferLen];
			int len;

			// read stream and update message digest
			while ((len = inputStream.read(buf)) != -1)
			{
				digest.update(buf, 0, len);
			}
			// finish message digest and return hash
			return digest.digest();
		}
		finally
		{
			IOUtils.close(inputStream);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12674.java