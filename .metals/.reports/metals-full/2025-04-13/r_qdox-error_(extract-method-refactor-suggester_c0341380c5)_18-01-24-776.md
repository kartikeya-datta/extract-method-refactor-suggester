error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11565.java
text:
```scala
public C@@lass< ? extends Page> getHomePage()

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
package org.apache.wicket.util.io;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;

import junit.framework.Assert;

import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;

/**
 * @author jcompagner
 */
public class WicketOutputStreamTest extends WicketTestCase
{
	ByteArrayOutputStream baos;
	WicketObjectOutputStream woos;

	/**
	 * Tests serialization of a big int.
	 * 
	 * @throws Exception
	 */
	public void testBigInteger() throws Exception
	{
		BigInteger bi = new BigInteger("102312302132130123230021301023");
		woos.writeObject(bi);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		WicketObjectInputStream wois = new WicketObjectInputStream(bais);
		BigInteger bi2 = (BigInteger)wois.readObject();

		Assert.assertEquals(bi, bi2);

	}

	/**
	 * @throws Exception
	 */
	public void testGregorianCalendar() throws Exception
	{
		GregorianCalendar gc = new GregorianCalendar(2005, 10, 10);

		woos.writeObject(gc);
		woos.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		WicketObjectInputStream wois = new WicketObjectInputStream(bais);
		GregorianCalendar gc2 = (GregorianCalendar)wois.readObject();

		Assert.assertEquals(gc, gc2);

	}


	public void testNotSerializeable() throws Exception
	{
		WebApplication app = new WebApplication()
		{
			@Override
			public Class< ? extends Page< ? >> getHomePage()
			{
				return null;
			}

			@Override
			protected ISessionStore newSessionStore()
			{
				// Don't use a filestore, or we spawn lots of threads, which makes things slow.
				return new HttpSessionStore(this);
			}

		};

		try
		{
			woos.writeObject(app);
			assertFalse("webapplication is not serializeable", false);
		}
		catch (Exception e)
		{
		}
	}

	public void testLocale() throws Exception
	{
		Locale locale = new Locale("nl", "NL");
		woos.writeObject(locale);

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		WicketObjectInputStream wois = new WicketObjectInputStream(bais);
		Locale locale2 = (Locale)wois.readObject();

		Assert.assertEquals(locale, locale2);

	}

	public void testPageReference() throws Exception
	{
		PageB b = new PageB("test");
		PageA a = new PageA(b);
		b.setA(a);

		woos.writeObject(a);

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		WicketObjectInputStream wois = new WicketObjectInputStream(bais);
		PageA a2 = (PageA)wois.readObject();

		Assert.assertEquals(a, a2);

		Assert.assertSame(a2, a2.getB().getA());

		RequestCycle.get().detach();
	}


	// public void testStringsEqualsAfterSerialization() throws Exception
	// {
	// String[] strings = new String[2];
	// strings[0] = new String("wicket");
	// strings[1] = "wicket";
	//
	// assertEquals(false, strings[0] == strings[1]);
	//
	// woos.writeObject(strings);
	// woos.close();
	//
	// ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	//
	// WicketObjectInputStream wois = new WicketObjectInputStream(bais);
	// String[] strings2 = (String[])wois.readObject();
	//
	// Assert.assertEquals(strings[0], strings[1]);
	//
	// Assert.assertSame(strings[0], strings[1]);
	//
	//
	// }

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		baos = new ByteArrayOutputStream();
		woos = new WicketObjectOutputStream(baos);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11565.java