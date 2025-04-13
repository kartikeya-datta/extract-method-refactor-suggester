error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2093.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2093.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2093.java
text:
```scala
r@@eturn (b1[i] & 0xFF) - (b2[i] & 0xFF);

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS.security;

import java.io.*;
import java.math.*;
import java.util.*;
import java.security.*;
import java.security.interfaces.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import org.xbill.DNS.*;
import org.xbill.DNS.utils.*;

/**
 * A class that verifies DNS data using digital signatures contained in DNSSEC
 * SIG records.  DNSSECVerifier stores a set of trusted keys.  Each specific
 * verification references a cache where additional secure keys may be found.
 * @see Verifier
 * @see DNSSEC
 *
 * @author Brian Wellington
 */

public class DNSSECVerifier implements Verifier {

public class ByteArrayComparator implements Comparator {
	public int
	compare(Object o1, Object o2) throws ClassCastException {
		byte [] b1 = (byte []) o1;
		byte [] b2 = (byte []) o2;
		for (int i = 0; i < b1.length && i < b2.length; i++)
			if (b1[i] != b2[i])
				return b1[i] - b2[i];
		return b1.length - b2.length;
	}
}

private Hashtable trustedKeys;

/** Creates a new DNSSECVerifier */
public
DNSSECVerifier() {
	trustedKeys = new Hashtable();
}

/** Adds the specified key to the set of trusted keys */
public synchronized void
addTrustedKey(KEYRecord key) {
	Name name = key.getName();
	Vector list = (Vector) trustedKeys.get(name);
	if (list == null)
		trustedKeys.put(name, list = new Vector());
	list.addElement(key);
}

/** Adds the specified key to the set of trusted keys */
public void
addTrustedKey(Name name, PublicKey key) {
	KEYRecord keyrec;
	keyrec = KEYConverter.buildRecord(name, DClass.IN, 0, 0,
					  KEYRecord.PROTOCOL_DNSSEC, key);
	if (keyrec != null)
		addTrustedKey(keyrec);
}

private PublicKey
findMatchingKey(Enumeration e, int algorithm, int footprint) {
	while (e.hasMoreElements()) {
		KEYRecord keyrec = (KEYRecord) e.nextElement();
		if (keyrec.getAlgorithm() == algorithm &&
		    keyrec.getFootprint() == footprint)
			return KEYConverter.parseRecord(keyrec);
	}
	return null;
}

private synchronized PublicKey
findTrustedKey(Name name, int algorithm, int footprint) {
	Vector list = (Vector) trustedKeys.get(name);
	if (list == null)
		return null;
	return findMatchingKey(list.elements(), algorithm, footprint);
}

private PublicKey
findCachedKey(Cache cache, Name name, int algorithm, int footprint) {
	RRset [] keysets = cache.findAnyRecords(name, Type.KEY, DClass.ANY);
	if (keysets == null)
		return null;
	RRset keys = keysets[0];
	if (keys.getSecurity() < DNSSEC.Secure)
		return null;
	return findMatchingKey(keys.rrs(), algorithm, footprint);
}

private PublicKey
findKey(Cache cache, Name name, int algorithm, int footprint) {
	PublicKey key = findTrustedKey(name, algorithm, footprint);
	if (key == null && cache != null)
		return findCachedKey(cache, name, algorithm, footprint);
	return key;
}

private byte
verifySIG(RRset set, SIGRecord sigrec, Cache cache) {
	PublicKey key = findKey(cache, sigrec.getSigner(),
				sigrec.getAlgorithm(), sigrec.getFootprint());
	if (key == null)
		return DNSSEC.Insecure;

	DataByteOutputStream out = new DataByteOutputStream();

	Date now = new Date();
	if (now.compareTo(sigrec.getExpire()) > 0 ||
	    now.compareTo(sigrec.getTimeSigned()) < 0)
	{
		System.err.println("Outside of validity period");
		return DNSSEC.Failed;
	}
	try {
		out.writeShort(sigrec.getTypeCovered());
		out.writeByte(sigrec.getAlgorithm());
		out.writeByte(sigrec.getLabels());
		out.writeInt(sigrec.getOrigTTL());
		out.writeInt((int) (sigrec.getExpire().getTime() / 1000));
		out.writeInt((int) (sigrec.getTimeSigned().getTime() / 1000));
		out.writeShort(sigrec.getFootprint());
		sigrec.getSigner().toWireCanonical(out);
		Enumeration e = set.rrs();
		int size = set.size();
		byte [][] records = new byte[size][];
		while (e.hasMoreElements()) {
			Record rec = (Record) e.nextElement();
			if (rec.getName().labels() > sigrec.getLabels()) {
				Name name = rec.getName();
				Name wild = name.wild(name.labels() -
						      sigrec.getLabels());
				rec = rec.withName(wild);
			}
			records[--size] = rec.toWireCanonical();
		}
		Arrays.sort(records, new ByteArrayComparator());
		for (int i = 0; i < records.length; i++)
			out.write(records[i]);
	}
	catch (IOException ioe) {
	}
	byte [] data = out.toByteArray();

	byte [] sig;
	String algString;

	switch (sigrec.getAlgorithm()) {
		case DNSSEC.RSA:
			sig = sigrec.getSignature();
			algString = "RSA";
			break;
		case DNSSEC.DSA:
			sig = DSASignature.create(sigrec);
			algString = "DSA";
			break;
		default:
			return DNSSEC.Failed;
        }

	try {
		Signature s = Signature.getInstance(algString);
		s.initVerify(key);
		s.update(data);
		return s.verify(sig) ? DNSSEC.Secure : DNSSEC.Failed;
	}
	catch (GeneralSecurityException e) {
		if (Options.check("verboseexceptions"))
			System.err.println("Signing data: " + e);
		return DNSSEC.Failed;
	}
}

/**
 * Attempts to verify an RRset.  This does not modify the set.
 * @param set The RRset to verify
 * @param cache The Cache where obtained secure keys are found (may be null)
 * @return The new security status of the set
 * @see RRset
 */
public byte
verify(RRset set, Cache cache) {
	Enumeration sigs = set.sigs();
	if (Options.check("verbosesec"))
		System.out.print("Verifying " + set.getName() + "/" +
				 Type.string(set.getType()) + ": ");
	if (!sigs.hasMoreElements()) {
		if (Options.check("verbosesec"))
			System.out.println("Insecure");
		return DNSSEC.Insecure;
	}
	while (sigs.hasMoreElements()) {
		SIGRecord sigrec = (SIGRecord) sigs.nextElement();
		if (verifySIG(set, sigrec, cache) == DNSSEC.Secure) {
			if (Options.check("verbosesec"))
				System.out.println("Secure");
			return DNSSEC.Secure;
		}
	}
	if (Options.check("verbosesec"))
		System.out.println("Failed");
	return DNSSEC.Failed;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2093.java