error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9917.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9917.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9917.java
text:
```scala
private S@@tring compact(String message) {

package org.junit;

/**
 * Thrown when an {@link org.junit.Assert#assertEquals(Object, Object) assertEquals(String, String)} fails. Create and throw
 * a <code>ComparisonFailure</code> manually if you want to show users the difference between two complex 
 * strings.
 * 
 * Inspired by a patch from Alex Chaffee (alex@purpletech.com)
 */
public class ComparisonFailure extends AssertionError {	
	/** 
	 * The maximum length for fExpected and fActual. If it is exceeded, the strings should be shortened. 
	 * @see ComparisonCompactor
	 */
	private static final int MAX_CONTEXT_LENGTH= 20;
	private static final long serialVersionUID= 1L;
	
	private String fExpected;
	private String fActual;

	/**
	 * Constructs a comparison failure.
	 * @param message the identifying message or null
	 * @param expected the expected string value
	 * @param actual the actual string value
	 */
	public ComparisonFailure (String message, String expected, String actual) {
		super (message);
		fExpected= expected;
		fActual= actual;
	}
	
	/**
	 * Returns "..." in place of common prefix and "..." in
	 * place of common suffix between expected and actual.
	 * 
	 * @see Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return new ComparisonCompactor(MAX_CONTEXT_LENGTH, fExpected, fActual).compact(super.getMessage());
	}
	
	/**
	 * Returns the actual string value
	 * @return the actual string value
	 */
	public String getActual() {
		return fActual;
	}
	/**
	 * Returns the expected string value
	 * @return the expected string value
	 */
	public String getExpected() {
		return fExpected;
	}
	
	private static class ComparisonCompactor {
		private static final String ELLIPSIS= "...";
		private static final String DELTA_END= "]";
		private static final String DELTA_START= "[";
		
		/**
		 * The maximum length for <code>expected</code> and <code>actual</code>. When <code>contextLength</code> 
		 * is exceeded, the Strings are shortened
		 */
		private int fContextLength;
		private String fExpected;
		private String fActual;
		private int fPrefix;
		private int fSuffix;

		/**
		 * @param contextLength the maximum length for <code>expected</code> and <code>actual</code>. When contextLength 
		 * is exceeded, the Strings are shortened
		 * @param expected the expected string value
		 * @param actual the actual string value
		 */
		public ComparisonCompactor(int contextLength, String expected, String actual) {
			fContextLength= contextLength;
			fExpected= expected;
			fActual= actual;
		}

		public String compact(String message) {
			if (fExpected == null || fActual == null || areStringsEqual())
				return Assert.format(message, fExpected, fActual);

			findCommonPrefix();
			findCommonSuffix();
			String expected= compactString(fExpected);
			String actual= compactString(fActual);
			return Assert.format(message, expected, actual);
		}

		private String compactString(String source) {
			String result= DELTA_START + source.substring(fPrefix, source.length() - fSuffix + 1) + DELTA_END;
			if (fPrefix > 0)
				result= computeCommonPrefix() + result;
			if (fSuffix > 0)
				result= result + computeCommonSuffix();
			return result;
		}

		private void findCommonPrefix() {
			fPrefix= 0;
			int end= Math.min(fExpected.length(), fActual.length());
			for (; fPrefix < end; fPrefix++) {
				if (fExpected.charAt(fPrefix) != fActual.charAt(fPrefix))
					break;
			}
		}

		private void findCommonSuffix() {
			int expectedSuffix= fExpected.length() - 1;
			int actualSuffix= fActual.length() - 1;
			for (; actualSuffix >= fPrefix && expectedSuffix >= fPrefix; actualSuffix--, expectedSuffix--) {
				if (fExpected.charAt(expectedSuffix) != fActual.charAt(actualSuffix))
					break;
			}
			fSuffix=  fExpected.length() - expectedSuffix;
		}

		private String computeCommonPrefix() {
			return (fPrefix > fContextLength ? ELLIPSIS : "") + fExpected.substring(Math.max(0, fPrefix - fContextLength), fPrefix);
		}

		private String computeCommonSuffix() {
			int end= Math.min(fExpected.length() - fSuffix + 1 + fContextLength, fExpected.length());
			return fExpected.substring(fExpected.length() - fSuffix + 1, end) + (fExpected.length() - fSuffix + 1 < fExpected.length() - fContextLength ? ELLIPSIS : "");
		}

		private boolean areStringsEqual() {
			return fExpected.equals(fActual);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9917.java