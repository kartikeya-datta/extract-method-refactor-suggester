error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5201.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5201.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5201.java
text:
```scala
b@@uffer.append(e.nextElement());

package junit.samples.money;

import java.util.*;

/**
 * A MoneyBag defers exchange rate conversions. For example adding 
 * 12 Swiss Francs to 14 US Dollars is represented as a bag 
 * containing the two Monies 12 CHF and 14 USD. Adding another
 * 10 Swiss francs gives a bag with 22 CHF and 14 USD. Due to 
 * the deferred exchange rate conversion we can later value a 
 * MoneyBag with different exchange rates.
 *
 * A MoneyBag is represented as a list of Monies and provides 
 * different constructors to create a MoneyBag. 
 */ 
class MoneyBag implements IMoney {
	private Vector fMonies= new Vector(5);

	static IMoney create(IMoney m1, IMoney m2) {
		MoneyBag result= new MoneyBag();
		m1.appendTo(result);
		m2.appendTo(result);
		return result.simplify();
	}
	public IMoney add(IMoney m) {
		return m.addMoneyBag(this);
	}
	public IMoney addMoney(Money m) { 
		return MoneyBag.create(m, this);
	}
	public IMoney addMoneyBag(MoneyBag s) {
		return MoneyBag.create(s, this);
	}
	void appendBag(MoneyBag aBag) {
		for (Enumeration e= aBag.fMonies.elements(); e.hasMoreElements(); )
			appendMoney((Money)e.nextElement());
	}
	void appendMoney(Money aMoney) {
		if (aMoney.isZero()) return;
		IMoney old= findMoney(aMoney.currency());
		if (old == null) {
			fMonies.addElement(aMoney);
			return;
		}
		fMonies.removeElement(old);
		IMoney sum= old.add(aMoney);
		if (sum.isZero()) 
			return;
		fMonies.addElement(sum);
	}
	public boolean equals(Object anObject) {
		if (isZero())
			if (anObject instanceof IMoney)
				return ((IMoney)anObject).isZero();

		if (anObject instanceof MoneyBag) {
			MoneyBag aMoneyBag= (MoneyBag)anObject;
			if (aMoneyBag.fMonies.size() != fMonies.size())
				return false;

		    for (Enumeration e= fMonies.elements(); e.hasMoreElements(); ) {
		        Money m= (Money) e.nextElement();
				if (!aMoneyBag.contains(m))
					return false;
			}
			return true;
		}
		return false;
	}
	private Money findMoney(String currency) {
		for (Enumeration e= fMonies.elements(); e.hasMoreElements(); ) {
			Money m= (Money) e.nextElement();
			if (m.currency().equals(currency))
				return m;
		}
		return null;
	}
	private boolean contains(Money m) {
		Money found= findMoney(m.currency());
		if (found == null) return false;
		return found.amount() == m.amount();
	}
	public int hashCode() {
		int hash= 0;
	    for (Enumeration e= fMonies.elements(); e.hasMoreElements(); ) {
	        Object m= e.nextElement();
			hash^= m.hashCode();
		}
	    return hash;
	}
	public boolean isZero() {
		return fMonies.size() == 0;
	}
	public IMoney multiply(int factor) {
		MoneyBag result= new MoneyBag();
		if (factor != 0) {
			for (Enumeration e= fMonies.elements(); e.hasMoreElements(); ) {
				Money m= (Money) e.nextElement();
				result.appendMoney((Money)m.multiply(factor));
			}
		}
		return result;
	}
	public IMoney negate() {
		MoneyBag result= new MoneyBag();
	    for (Enumeration e= fMonies.elements(); e.hasMoreElements(); ) {
	        Money m= (Money) e.nextElement();
	        result.appendMoney((Money)m.negate());
		}
		return result;
	}
	private IMoney simplify() {
		if (fMonies.size() == 1)
			return (IMoney)fMonies.elements().nextElement();
		return this;
	}
	public IMoney subtract(IMoney m) {
		return add(m.negate());
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		for (Enumeration e= fMonies.elements(); e.hasMoreElements(); )
		    buffer.append((Money) e.nextElement());
		buffer.append("}");
		return buffer.toString();
	}
	public void appendTo(MoneyBag m) {
		m.appendBag(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5201.java