error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6717.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6717.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6717.java
text:
```scala
S@@IGRecord r = new SIGRecord(Name.root, DClass.ANY, 0,

package org.xbill.DNS.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.DSAKey;
import java.util.Date;
import org.xbill.DNS.*;
import org.xbill.DNS.utils.DataByteOutputStream;

/**
 * Creates SIG(0) transaction signatures.
 *
 * @author Pasi Eronen
 * @author Brian Wellington
 */

public class SIG0Signer {

/**
 * The default validity period for outgoing SIG(0) signed messages.
 * Can be overriden by the sig0validity option.
 */
private static final short VALIDITY = 300;
    
private int algorithm;
private PrivateKey privateKey;
private Name name;
private int footprint;

/** 
 * Creates a new SIG(0) signer object.
 * @param algorithm usually DNSSEC.RSAMD5, DNSSEC.DSA, or DNSSEC.RSASHA1
 * @param privateKey signing key (must match algorithm)
 * @param name the name of the key
 * @param keyFootprint the key tag
 */
public
SIG0Signer(int algorithm, PrivateKey privateKey, Name name, int keyFootprint) {
	this.algorithm = (byte) algorithm;
	this.privateKey = privateKey;
	this.name = name;
	this.footprint = keyFootprint;
}

/**
 * Creates a new SIG(0) signer object. This is the same as the
 * other constructor, except that the key tag is calculated automatically
 * from the given public key.
 */
public
SIG0Signer(int algorithm, PrivateKey privateKey, Name name,
	   PublicKey publicKey)
{
    this.algorithm = (byte) algorithm;
    this.privateKey = privateKey;
    this.name = name;
    KEYRecord keyRecord = KEYConverter.buildRecord(name, DClass.IN, 0,
						   KEYRecord.OWNER_USER,
						   KEYRecord.PROTOCOL_ANY,
						   publicKey);
    this.footprint = keyRecord.getFootprint();
}

/**
 * Appends a SIG(0) signature to the message.
 * @param m the message
 * @param old if this message is a response, the original message
 */
public void apply(Message m, byte old[])
throws IOException, SignatureException, InvalidKeyException,
       NoSuchAlgorithmException
{
	
	int validity = VALIDITY;
	if (Options.check("sig0validity")) {
		String s = Options.value("sig0validity");
		try {
			validity = Short.parseShort(s);
		}
		catch (NumberFormatException e) {
		}
        }
	
	long now = System.currentTimeMillis();
	Date timeSigned = new Date(now);
	Date timeExpires = new Date(now + validity * 1000);
	
	String algorithmName;
	if (algorithm == DNSSEC.DSA) {
		algorithmName = "SHA1withDSA";
	} else if (algorithm == DNSSEC.RSAMD5) {
		algorithmName = "MD5withRSA";
	} else if (algorithm == DNSSEC.RSASHA1) {
		algorithmName = "SHA1withRSA";
	} else {
		throw new NoSuchAlgorithmException("Unknown algorithm");
	}
	
	DataByteOutputStream out = new DataByteOutputStream();
	
	/*
	 * BIND 9.0 and 9.1.0 compute and verify SIG(0) records incorrectly.
	 * This will be fixed in a future version of BIND.
	 */
	if (Options.check("bind9sig0")) {
		if (old != null)
			out.write(old);
		out.write(m.toWire());
	}
	
	out.writeShort(0); // type covered
	out.writeByte(algorithm); // algorithm
	out.writeByte(0); // labels
	out.writeInt(0); // original TTL
	out.writeInt((int)(timeExpires.getTime() / 1000));
	out.writeInt((int)(timeSigned.getTime() / 1000));
	out.writeShort(footprint); // key tag
	name.toWireCanonical(out); // name
	
	if (!Options.check("bind9sig0")) {
		if (old != null)
			out.write(old);
		out.write(m.toWire());
	}
	
	byte[] outBytes = out.toByteArray();
	
	Signature signer = Signature.getInstance(algorithmName);
	signer.initSign(privateKey);
	signer.update(outBytes);
	byte[] signature = signer.sign();

	/*
	 * RSA signatures are already in correct format, but Java DSA
	 * routines use ASN.1; convert this to SIG format.
	 */
	if (algorithm == DNSSEC.DSA) {
		DSAKey dsakey = (DSAKey) privateKey;
		signature = DSASignature.create(dsakey.getParams(), signature);
	}
	
	SIGRecord r = new SIGRecord(new Name("."), DClass.ANY, 0,
				    0, algorithm,
				    0, timeExpires, timeSigned,
				    footprint,
				    name,
				    signature);
	m.addRecord(r, Section.ADDITIONAL);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6717.java