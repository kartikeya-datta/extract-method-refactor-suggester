error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5857.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5857.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5857.java
text:
```scala
a@@ssertTextPresent("Bad luck. You failed to guess that the word was");

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.hangman;

import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.jwebunit.WebTestCase;
import nl.openedge.util.jetty.JettyDecorator;

/**
 * Testcase for the <code>Game</code> class.
 * 
 * @author Chris Turner
 * @version 1.0
 */
public class HangManTest extends WebTestCase
{

	/**
	 * Create the test case.
	 * 
	 * @param message
	 *           The test name
	 */
	public HangManTest(String message)
	{
		super(message);
	}

	/**
	 * Tests the hangman class directly for a winning game.
	 * 
	 * @throws Exception
	 */
	public void testHangmanWinGame() throws Exception
	{
		Game hangman = new Game();
		hangman.newGame(5, new WordGenerator(new String[] { "testing" }));
		
		Assert.assertEquals(5, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 'a', false);
		Assert.assertEquals(4, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		guess(hangman, 'a');
		Assert.assertEquals(4, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 't', true);
		Assert.assertEquals(4, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 'e', true);
		Assert.assertEquals(4, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 's', true);
		Assert.assertEquals(4, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 'i', true);
		Assert.assertEquals(4, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 'n', true);
		Assert.assertEquals(4, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 'g', true);
		Assert.assertEquals(4, hangman.getGuessesRemaining());
		Assert.assertTrue(hangman.isWon());
		Assert.assertFalse(hangman.isLost());
	}
	
	private Letter letter(Game hangman, char c)
	{
		for (Iterator iter = hangman.getLetters().iterator(); iter.hasNext();)
		{
			Letter letter = (Letter)iter.next();
			if (letter.asString().equalsIgnoreCase(Character.toString(c)))
			{
				return letter;
			}
		}		
		return null;
	}

	private boolean guess(Game hangman, char c)
	{
		return hangman.guess(letter(hangman, c));
	}
	
	/**
	 * Tests the hangman class directly for a lost game.
	 * 
	 * @throws Exception
	 */
	public void testHangmanLoseGame() throws Exception
	{
		Game hangman = new Game();
		hangman.newGame(2, new WordGenerator(new String[] { "foo" }));
		
		Assert.assertEquals(2, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 'z', false);
		Assert.assertEquals(1, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertFalse(hangman.isLost());

		doGuessTest(hangman, 'v', false);
		Assert.assertEquals(0, hangman.getGuessesRemaining());
		Assert.assertFalse(hangman.isWon());
		Assert.assertTrue(hangman.isLost());
	}

	/**
	 * Tests the webapplication for a successful match.
	 */
	public void testHangmanSuccessWebGame()
	{
		getTestContext().setBaseUrl("http://localhost:8098/wicket-examples");
		beginAt("/hangman?word=hangman");

		assertTitleEquals("Wicket Examples - hangman");
		assertLinkPresent("start");
		clickLink("start");
		assertTitleEquals("Wicket Examples - hangman");
		assertTextPresent("guesses remaining");

		assertElementPresent("guessesRemaining");
		assertTextInElement("guessesRemaining", "5");

		clickLink("letter_f");

		assertElementPresent("guessesRemaining");
		assertTextInElement("guessesRemaining", "4");

		clickLink("letter_h");
		assertElementPresent("guessesRemaining");
		assertTextInElement("guessesRemaining", "4");

		clickLink("letter_a");
		clickLink("letter_n");
		clickLink("letter_g");
		clickLink("letter_m");
		assertTextPresent("Congratulations! You guessed that the word was ");
	}

	/**
	 * Tests the webapplication for an unsuccessful match.
	 */
	public void testHangmanFailureWebGame()
	{
		getTestContext().setBaseUrl("http://localhost:8098/wicket-examples");
		beginAt("/hangman?word=hangman");

		assertTitleEquals("Wicket Examples - hangman");
		assertLinkPresent("start");
		clickLink("start");
		assertTitleEquals("Wicket Examples - hangman");
		assertTextPresent("guesses remaining");

		assertElementPresent("guessesRemaining");
		assertTextInElement("guessesRemaining", "5");

		clickLink("letter_f");

		assertElementPresent("guessesRemaining");
		assertTextInElement("guessesRemaining", "4");

		assertLinkNotPresent("letter_f");
		clickLink("letter_x");
		assertTextInElement("guessesRemaining", "3");

		clickLink("letter_e");
		assertTextInElement("guessesRemaining", "2");

		clickLink("letter_t");
		assertTextInElement("guessesRemaining", "1");

		clickLink("letter_v");
		assertTextPresent("Bad Luck! You failed to guess that the word was");
	}

	/**
	 * Performs a guess.
	 * @param hangman
	 * @param c
	 * @param expected
	 */
	private void doGuessTest(Game hangman, char c, boolean expected)
	{
		Assert.assertFalse(letter(hangman, c).isGuessed());
		Assert.assertEquals(expected, guess(hangman, c));
		Assert.assertTrue(letter(hangman, c).isGuessed());
	}

	/**
	 * Creates the testsuite.
	 * 
	 * @return the testsuite.
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(HangManTest.class);

		JettyDecorator deco = new JettyDecorator(suite);
		deco.setPort(8098);
		deco.setWebappContextRoot("src/webapp");
		deco.setContextPath("/wicket-examples");
		return deco;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5857.java