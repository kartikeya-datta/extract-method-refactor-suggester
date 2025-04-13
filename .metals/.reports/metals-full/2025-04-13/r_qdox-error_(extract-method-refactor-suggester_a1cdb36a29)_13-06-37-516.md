error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5189.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5189.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[5,1]

error in qdox parser
file content:
```java
offset: 120
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5189.java
text:
```scala
static class SimpleDSAParams implements DSAParams {

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

p@@ackage org.xbill.DNS.security;

import java.math.*;
import java.security.interfaces.*;

/**
 * A stub implementation of a DSA (Digital Signature Algorithm) public key
 *
 * @author Brian Wellington
 */

class DSAPubKey implements DSAPublicKey {

class SimpleDSAParams implements DSAParams {
	private BigInteger P, Q, G;

	public
	SimpleDSAParams(BigInteger p, BigInteger q, BigInteger g) {
		P = p;
		Q = q;
		G = g;
	}

	public BigInteger
	getP() {
		return P;
	}
		
	public BigInteger
	getQ() {
		return Q;
	}
		
	public BigInteger
	getG() {
		return G;
	}
}

private DSAParams params;
private BigInteger Y;

/** Create a DSA public key from its parts */
public
DSAPubKey(BigInteger p, BigInteger q, BigInteger g, BigInteger y) {
	params = (DSAParams) new SimpleDSAParams(p, q, g);
	Y = y;
}

/** Obtain the public value of a DSA public key */
public BigInteger
getY() {
	return Y;
}

/** Obtain the parameters of a DSA public key */
public DSAParams
getParams() {
	return params;
}

/** Obtain the algorithm of a DSA public key */
public String
getAlgorithm() {
	return "DSA";
}

/** Obtain the format of a DSA public key (unimplemented) */
public String
getFormat() {
	return null;
}

/** Obtain the encoded representation of a DSA public key (unimplemented) */
public byte []
getEncoded() {
	return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5189.java