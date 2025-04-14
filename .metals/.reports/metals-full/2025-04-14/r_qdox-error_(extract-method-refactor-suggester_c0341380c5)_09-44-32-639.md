error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16133.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16133.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16133.java
text:
```scala
private v@@oid assertBooksEquals(Book[] expectBooks, List<Book> actualBooks)

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
package org.apache.wicket.util.tester.apps_3;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.apps_1.Book;


/**
 * @author Ingram Chen
 */
public class FormTesterTest extends WicketTestCase
{
	private Book[] books;

	private ChoicePage choicePage;

	private FormTester formTester;

	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public FormTesterTest(String name)
	{
		super(name);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		books = new Book[] { new Book("1", "book1"), new Book("2", "book2"),
				new Book("3", "book3"), new Book("4", "book4") };

		choicePage = (ChoicePage)tester.startPage(new ChoicePage(Arrays.asList(books)));
		formTester = tester.newFormTester("choiceForm");
	}

	/**
	 * @throws Exception
	 */
	public void testSingleChoice() throws Exception
	{
		assertSame(books[1], choicePage.dropDownChoice);
		assertSame(books[3], choicePage.listChoice);
		assertSame(books[2], choicePage.radioChoice);
		assertSame(null, choicePage.radioGroup);
		formTester.select("dropDownChoice", 0);
		formTester.select("listChoice", 2);
		formTester.select("radioChoice", 1);
		formTester.select("radioGroup", 3);
		formTester.submit();
		assertSame(books[0], choicePage.dropDownChoice);
		assertSame(books[2], choicePage.listChoice);
		assertSame(books[1], choicePage.radioChoice);
		assertSame(books[3], choicePage.radioGroup);
	}

	/**
	 * @throws Exception
	 */
	public void testSingleChoice_toggle() throws Exception
	{
		assertSame(books[1], choicePage.dropDownChoice);
		assertSame(null, choicePage.radioGroup);
		formTester.select("dropDownChoice", 0);
		formTester.select("dropDownChoice", 1);// toggle to 1
		formTester.select("radioGroup", 3);
		formTester.select("radioGroup", 2);// toggle to 2
		formTester.submit();
		assertSame(books[1], choicePage.dropDownChoice);
		assertSame(books[2], choicePage.radioGroup);
	}

	/**
	 * @throws Exception
	 */
	public void testSingleChoiceComponentNotAllowSelectMuliple() throws Exception
	{
		try
		{
			formTester.selectMultiple("dropDownChoice", new int[] { 0 });
			throw new RuntimeException("WicketRuntimeException expected");
		}
		catch (WicketRuntimeException expected)
		{
		}

		try
		{
			formTester.selectMultiple("radioGroup", new int[] { 2, 1 });
			throw new RuntimeException("WicketRuntimeException expected");
		}
		catch (WicketRuntimeException expected)
		{
		}
	}

	/**
	 * @throws Exception
	 */
	public void testSelectMultiple() throws Exception
	{
		assertBooksEquals(new Book[0], choicePage.listMultipleChoice);
		assertBooksEquals(new Book[0], choicePage.checkBoxMultipleChoice);
		assertBooksEquals(new Book[0], choicePage.checkGroup);
		formTester.selectMultiple("listMultipleChoice", new int[] { 0, 3 });
		formTester.selectMultiple("checkBoxMultipleChoice", new int[] { 1, 0, 3 });
		formTester.selectMultiple("checkGroup", new int[] { 0, 1, 2, 3 });
		formTester.submit();

		assertBooksEquals(new Book[] { books[0], books[3] }, choicePage.listMultipleChoice);
		assertBooksEquals(new Book[] { books[0], books[1], books[3] },
			choicePage.checkBoxMultipleChoice);
		assertBooksEquals(books, choicePage.checkGroup);
	}

	/**
	 * @throws Exception
	 */
	public void testMultipleChoiceComponent_cumulate() throws Exception
	{
		assertBooksEquals(new Book[0], choicePage.listMultipleChoice);
		assertBooksEquals(new Book[0], choicePage.checkGroup);
		formTester.select("listMultipleChoice", 0);
		formTester.selectMultiple("listMultipleChoice", new int[] { 0, 3 });
		formTester.selectMultiple("listMultipleChoice", new int[] { 1 });

		formTester.selectMultiple("checkGroup", new int[] { 2 });
		formTester.selectMultiple("checkGroup", new int[] { 2, 3 });
		formTester.select("checkGroup", 0);
		formTester.submit();

		assertBooksEquals(new Book[] { books[0], books[1], books[3] },
			choicePage.listMultipleChoice);
		assertBooksEquals(new Book[] { books[0], books[2], books[3] }, choicePage.checkGroup);
	}

	private void assertBooksEquals(Book[] expectBooks, List actualBooks)
	{
		assertEquals(expectBooks.length, actualBooks.size());
		assertTrue(Arrays.asList(expectBooks).containsAll(actualBooks));
	}

	/**
	 * @throws Exception
	 */
	public void testMultipleButtonSubmit() throws Exception
	{
		formTester.submit();
		assertFalse(choicePage.anotherButtonPressed);

		formTester = tester.newFormTester("choiceForm");
		formTester.submit("anotherButton");
		assertTrue(choicePage.anotherButtonPressed);
	}

	/**
	 * Tests proper initialization.
	 */
	public void testInitialValues()
	{
		assertInitialValues();
		formTester.submit();
		assertInitialValues();
	}

	private void assertInitialValues()
	{
		assertSame(books[1], choicePage.dropDownChoice);
		assertSame(books[3], choicePage.listChoice);
		assertSame(books[2], choicePage.radioChoice);
		assertEquals(true, choicePage.checkBox);
		assertBooksEquals(new Book[] { books[2], books[1] }, choicePage.initialListMultipleChoice);
		assertBooksEquals(new Book[] { books[3], books[0] },
			choicePage.initialCheckBoxMultipleChoice);
		assertBooksEquals(new Book[] { books[3], books[2] }, choicePage.initialCheckGroup);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16133.java