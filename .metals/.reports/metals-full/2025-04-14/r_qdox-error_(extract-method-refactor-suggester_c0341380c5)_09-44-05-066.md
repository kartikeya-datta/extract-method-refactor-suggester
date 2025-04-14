error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12760.java
text:
```scala
t@@hrow new WicketRuntimeException("Unable to decrypt the text '" + encrypted.toString() + "'", e);

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
package org.apache.wicket.util.crypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;

import org.apache.wicket.WicketRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract base class for JCE based ICrypt implementations.
 * 
 * @author Juergen Donnerstag
 */
public abstract class AbstractCrypt implements ICrypt
{
	/** Default encryption key */
	private static final String DEFAULT_ENCRYPTION_KEY = "WiCkEt-CrYpT";

	/** Encoding used to convert java String from and to byte[] */
	private static final String CHARACTER_ENCODING = "UTF-8";

	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(AbstractCrypt.class);

	/** Key used to de-/encrypt the data */
	private String encryptionKey = DEFAULT_ENCRYPTION_KEY;

	/**
	 * Constructor
	 */
	public AbstractCrypt()
	{
	}

	/**
	 * Decrypts a string into a string.
	 * 
	 * @param text
	 *            text to decript
	 * @return the decrypted text
	 */
	public final String decryptUrlSafe(final String text)
	{
		try
		{
			byte[] encrypted = Base64UrlSafe.decodeBase64(text.getBytes());
			return new String(decryptByteArray(encrypted), CHARACTER_ENCODING);
		}
		catch (UnsupportedEncodingException ex)
		{
			throw new WicketRuntimeException(ex.getMessage());
		}
	}

	/**
	 * Encrypt a string into a string using URL safe Base64 encoding.
	 * 
	 * @param plainText
	 *            text to encrypt
	 * @return encrypted string
	 */
	public final String encryptUrlSafe(final String plainText)
	{
		try
		{
			byte[] cipherText = encryptStringToByteArray(plainText);
			return new String(Base64UrlSafe.encodeBase64(cipherText));
		}
		catch (GeneralSecurityException e)
		{
			log.error("Unable to encrypt text '" + plainText + "'", e);
			return null;
		}
	}

	/**
	 * Get encryption private key
	 * 
	 * @return encryption private key
	 */
	public String getKey()
	{
		return this.encryptionKey;
	}

	/**
	 * Set encryption private key
	 * 
	 * @param key
	 *            private key to make de-/encryption unique
	 */
	public void setKey(final String key)
	{
		this.encryptionKey = key;
	}

	/**
	 * Crypts the given byte array
	 * 
	 * @param input
	 *            byte array to be crypted
	 * @param mode
	 *            crypt mode
	 * @return the input crypted. Null in case of an error
	 * @throws GeneralSecurityException
	 */
	protected abstract byte[] crypt(final byte[] input, final int mode)
			throws GeneralSecurityException;

	/**
	 * Decrypts an encrypted, but Base64 decoded byte array into a byte array.
	 * 
	 * @param encrypted
	 *            byte array to decrypt
	 * @return the decrypted text
	 */
	private final byte[] decryptByteArray(final byte[] encrypted)
	{
		try
		{
			return crypt(encrypted, Cipher.DECRYPT_MODE);
		}
		catch (GeneralSecurityException e)
		{
			throw new WicketRuntimeException("Unable to decrypt the text '" + encrypted + "'", e);
		}
	}

	/**
	 * Encrypts the given text into a byte array.
	 * 
	 * @param plainText
	 *            text to encrypt
	 * @return the string encrypted
	 * @throws GeneralSecurityException
	 */
	private final byte[] encryptStringToByteArray(final String plainText)
			throws GeneralSecurityException
	{
		try
		{
			return crypt(plainText.getBytes(CHARACTER_ENCODING), Cipher.ENCRYPT_MODE);
		}
		catch (UnsupportedEncodingException ex)
		{
			throw new WicketRuntimeException(ex.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12760.java