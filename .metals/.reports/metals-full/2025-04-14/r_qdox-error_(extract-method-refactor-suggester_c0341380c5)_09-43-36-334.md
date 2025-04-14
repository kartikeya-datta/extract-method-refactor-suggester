error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5137.java
text:
```scala
@Override public O@@bject getValue() throws CouldNotGenerateValueException {

package org.junit.tests.experimental.theories.extendingwithstubs;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.hamcrest.BaseDescription;
import org.hamcrest.Description;
import org.junit.Assume.AssumptionViolatedException;

public class Guesser<T> extends ReguessableValue {
	static class GuessMap extends HashMap<MethodCall, Object> implements
			InvocationHandler {
		private static final long serialVersionUID = 1L;

		public GuessMap(GuessMap guesses) {
			super(guesses);
		}

		public GuessMap() {}

		GuessMap replaceGuess(Object oldValue, Object newValue) {
			GuessMap newGuesses = new GuessMap(this);
			for (Entry<MethodCall, Object> entry : newGuesses.entrySet()) {
				if (entry.getValue().equals(oldValue))
					entry.setValue(newValue);
			}
			return newGuesses;
		}

		protected Object generateGuess(Class<?> returnType) {
			if (returnType.equals(String.class))
				return "GUESS" + new Random().nextInt();
			if (returnType.equals(Integer.class)
 returnType.equals(int.class))
				return new Random().nextInt();
			return null;
		}

		Object getGuess(MethodCall call) {
			if (!containsKey(call))
				put(call, generateGuess(call.getReturnType()));
			return get(call);
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			return getGuess(new MethodCall(method, args));
		}
	}

	private final GuessMap guesses;
	private final Class<T> type;

	public Guesser(Class<T> type) {
		this(type, new GuessMap());
	}

	public Guesser(Class<T> type, GuessMap guesses) {
		this.type = type;
		this.guesses = guesses;
	}

	@SuppressWarnings("unchecked") public T getProxy() {
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[] { getType() }, guesses);
	}

	@Override public List<ReguessableValue> reguesses(
			AssumptionViolatedException e) {
		final ArrayList<ReguessableValue> returnThis = new ArrayList<ReguessableValue>();
		e.describeTo(new BaseDescription() {
			@Override protected void append(char arg0) {}

			boolean expectedSeen = false;
			Object expected = null;

			@SuppressWarnings("unchecked") @Override public Description appendValue(
					Object value) {
				noteValue(value);
				return super.appendValue(value);
			}

			private void noteValue(Object value) {
				if (!expectedSeen) {
					expected = value;
					expectedSeen = true;
					return;
				}

				GuessMap newGuesses = guesses.replaceGuess(expected, value);
				returnThis.add(new Guesser<T>(getType(), newGuesses));
			}
		});
		return returnThis;
	}

	@Override public Object getValue(Object test) throws CouldNotGenerateValueException {
		return getProxy();
	}

	public Class<T> getType() {
		return type;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5137.java