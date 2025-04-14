error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7587.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7587.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7587.java
text:
```scala
b@@uildRecord(Name name, int dclass, int ttl, int flags, int proto,

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS.security;

import java.io.*;
import java.math.*;
import java.security.*;
import java.security.interfaces.*;
import javax.crypto.interfaces.*;
import org.xbill.DNS.*;
import org.xbill.DNS.utils.*;

/**
 * Routines to convert between a DNS KEY record and a Java PublicKey.
 *
 * @author Brian Wellington
 */

public class KEYConverter {

private static final BigInteger DHPRIME768 = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A63A3620FFFFFFFFFFFFFFFF", 16);
private static final BigInteger DHPRIME1024 = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE65381FFFFFFFFFFFFFFFF", 16);
private static final BigInteger TWO = new BigInteger("2", 16);

static int
BigIntegerLength(BigInteger i) {
	byte [] b = i.toByteArray();
	return (b[0] == 0 ? b.length - 1 : b.length);
}

static RSAPublicKey
parseRSA(DataByteInputStream in) throws IOException {
	int exponentLength = in.readUnsignedByte();
	if (exponentLength == 0)
		exponentLength = in.readUnsignedShort();
	BigInteger exponent = in.readBigInteger(exponentLength);

	int modulusLength = in.available();
	BigInteger modulus = in.readBigInteger(modulusLength);

	RSAPublicKey rsa = new RSAPubKey(modulus, exponent);
	return rsa;
}

static DHPublicKey
parseDH(DataByteInputStream in) throws IOException {
	int special = 0;
	int pLength = in.readUnsignedShort();
	if (pLength < 16 && pLength != 1 && pLength != 2)
		return null;
	BigInteger p;
	if (pLength == 1 || pLength == 2) {
		if (pLength == 1)
			special = in.readUnsignedByte();
		else
			special = in.readUnsignedShort();
		if (special != 1 && special != 2)
			return null;
		if (special == 1)
			p = DHPRIME768;
		else
			p = DHPRIME1024;
	}
	else
		p = in.readBigInteger(pLength);

	int gLength = in.readUnsignedShort();
	BigInteger g;
	if (gLength == 0) {
		if (special != 0)
			g = TWO;
		else
			return null;
	}
	else
		g = in.readBigInteger(gLength);

	int yLength = in.readUnsignedShort();
	BigInteger y = in.readBigInteger(yLength);

	return new DHPubKey(p, g, y);
}

static DSAPublicKey
parseDSA(DataByteInputStream in) throws IOException {
	byte t = in.readByte();

	BigInteger q = in.readBigInteger(20);
	BigInteger p = in.readBigInteger(64 + t*8);
	BigInteger g = in.readBigInteger(64 + t*8);
	BigInteger y = in.readBigInteger(64 + t*8);

	DSAPublicKey dsa = new DSAPubKey(p, q, g, y);
	return dsa;
}

/** Converts a KEY record into a PublicKey */
public static PublicKey
parseRecord(KEYRecord r) {
	byte alg = r.getAlgorithm();
	byte [] data = r.getKey();
	DataByteInputStream dbs = new DataByteInputStream(data); 
	try {
		switch (alg) {
			case DNSSEC.RSAMD5:
			case DNSSEC.RSASHA1:
				return parseRSA(dbs);
			case DNSSEC.DH:
				return parseDH(dbs);
			case DNSSEC.DSA:
				return parseDSA(dbs);
			default:
				return null;
		}
	}
	catch (IOException e) {
		if (Options.check("verboseexceptions"))
			System.err.println(e);
		return null;
	}
}

static byte []
buildRSA(RSAPublicKey key) {
	DataByteOutputStream out = new DataByteOutputStream();
	BigInteger exponent = key.getPublicExponent();
	BigInteger modulus = key.getModulus();
	int exponentLength = BigIntegerLength(exponent);

	if (exponentLength < 256)
		out.writeByte(exponentLength);
	else {
		out.writeByte(0);
		out.writeShort(exponentLength);
	}
	out.writeBigInteger(exponent);
	out.writeBigInteger(modulus);

	return out.toByteArray();
}

static byte []
buildDH(DHPublicKey key) {
	DataByteOutputStream out = new DataByteOutputStream();
	BigInteger p = key.getParams().getP();
	BigInteger g = key.getParams().getG();
	BigInteger y = key.getY();

	int pLength, gLength, yLength;
	if (g.equals(TWO) && (p.equals(DHPRIME768) || p.equals(DHPRIME1024))) {
		pLength = 1;
		gLength = 0;
	}
	else {
		pLength = BigIntegerLength(p);
		gLength = BigIntegerLength(g);
	}
	yLength = BigIntegerLength(y);

	out.writeShort(pLength);
	if (pLength == 1) {
		if (p.bitLength() == 768)
			out.writeByte((byte)1);
		else
			out.writeByte((byte)2);
	}
	else
		out.writeBigInteger(p);
	out.writeShort(gLength);
	if (gLength > 0)
		out.writeBigInteger(g);
	out.writeShort(yLength);
	out.writeBigInteger(y);

	return out.toByteArray();
}

static byte []
buildDSA(DSAPublicKey key) {
	DataByteOutputStream out = new DataByteOutputStream();
	BigInteger q = key.getParams().getQ();
	BigInteger p = key.getParams().getP();
	BigInteger g = key.getParams().getG();
	BigInteger y = key.getY();
	int t = (p.toByteArray().length - 64) / 8;

	out.writeByte(t);
	out.writeBigInteger(q);
	out.writeBigInteger(p);
	out.writeBigInteger(g);
	out.writeBigInteger(y);

	return out.toByteArray();
}

/** Builds a KEY record from a PublicKey */
public static KEYRecord
buildRecord(Name name, short dclass, int ttl, int flags, int proto,
	    PublicKey key)
{
	byte [] data;
	byte alg;

	if (key instanceof RSAPublicKey) {
		alg = DNSSEC.RSAMD5;
		data = buildRSA((RSAPublicKey) key);
	}
	else if (key instanceof DHPublicKey) {
		alg = DNSSEC.DH;
		data = buildDH((DHPublicKey) key);
	}
	else if (key instanceof DSAPublicKey) {
		alg = DNSSEC.DSA;
		data = buildDSA((DSAPublicKey) key);
	}
	else
		return null;

	if (data == null)
		return null;

	return new KEYRecord(name, dclass, ttl, flags, proto, alg, data);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7587.java