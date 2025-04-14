error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8604.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8604.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8604.java
text:
```scala
r@@rToWire(DataByteOutputStream out, Compression c) {

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import java.text.*;
import org.xbill.DNS.utils.*;

/**
 * Location - describes the physical location of hosts, networks, subnets.
 *
 * @author Brian Wellington
 */

public class LOCRecord extends Record {

private static LOCRecord member = new LOCRecord();

private long size, hPrecision, vPrecision;
private int latitude, longitude, altitude;

private
LOCRecord() {}

private
LOCRecord(Name name, short dclass, int ttl) {
	super(name, Type.LOC, dclass, ttl);
}

static LOCRecord
getMember() {
	return member;
}

/**
 * Creates an LOC Record from the given data
 * @param latitude The latitude of the center of the sphere
 * @param longitude The longitude of the center of the sphere
 * @param altitude The altitude of the center of the sphere, in m
 * @param size The diameter of a sphere enclosing the described entity, in m.
 * @param hPrecision The horizontal precision of the data, in m.
 * @param vPrecision The vertical precision of the data, in m.
*/
public
LOCRecord(Name name, short dclass, int ttl, double latitude, double longitude,
	  double altitude, double size, double hPrecision, double vPrecision)
throws IOException
{
	this(name, dclass, ttl);
	this.latitude = (int)(latitude * 3600 * 1000 + (1 << 31));
	this.longitude = (int)(longitude * 3600 * 1000 + (1 << 31));
	this.altitude = (int)((altitude + 100000) * 100);
	this.size = (long)(size * 100);
	this.hPrecision = (long)(hPrecision * 100);
	this.vPrecision = (long)(vPrecision * 100);
}

Record
rrFromWire(Name name, short type, short dclass, int ttl, int length,
	   DataByteInputStream in)
throws IOException
{
	LOCRecord rec = new LOCRecord(name, dclass, ttl);
	if (in == null)
		return rec;
	int version, temp;

	version = in.readByte();
	if (version != 0)
		throw new WireParseException("Invalid LOC version");

	rec.size = parseLOCformat(in.readUnsignedByte());
	rec.hPrecision = parseLOCformat(in.readUnsignedByte());
	rec.vPrecision = parseLOCformat(in.readUnsignedByte());
	rec.latitude = in.readInt();
	rec.longitude = in.readInt();
	rec.altitude = in.readInt();
	return rec;
}

Record
rdataFromString(Name name, short dclass, int ttl, MyStringTokenizer st,
		Name origin)
throws TextParseException
{
	LOCRecord rec = new LOCRecord(name, dclass, ttl);

	String s = null;
	int deg, min;
	double sec;

	/* Latitude */
	deg = min = 0;
	sec = 0.0;
	try {
		s = st.nextToken();
		deg = Integer.parseInt(s);
		s = st.nextToken();
		min = Integer.parseInt(s);
		s = st.nextToken();
		sec = new Double(s).doubleValue();
		s = st.nextToken();
	}
	catch (NumberFormatException e) {
	}
	if (!s.equalsIgnoreCase("S") && !s.equalsIgnoreCase("N"))
		throw new TextParseException("Invalid LOC latitude");
	rec.latitude = (int) (1000 * (sec + 60 * (min + 60 * deg)));
	if (s.equalsIgnoreCase("S"))
		rec.latitude = -rec.latitude;
	rec.latitude += (1 << 31);
	
	/* Longitude */
	deg = min = 0;
	sec = 0.0;
	try {
		s = st.nextToken();
		deg = Integer.parseInt(s);
		s = st.nextToken();
		min = Integer.parseInt(s);
		s = st.nextToken();
		sec = new Double(s).doubleValue();
		s = st.nextToken();
	}
	catch (NumberFormatException e) {
	}
	if (!s.equalsIgnoreCase("W") && !s.equalsIgnoreCase("E"))
		throw new TextParseException("Invalid LOC longitude");
	rec.longitude = (int) (1000 * (sec + 60 * (min + 60 * deg)));
	if (s.equalsIgnoreCase("W"))
		rec.longitude = -rec.longitude;
	rec.longitude += (1 << 31);

	/* Altitude */
	if (!st.hasMoreTokens())
		return rec;
	s = st.nextToken();
	if (s.length() > 1 && s.charAt(s.length() - 1) == 'm')
		s = s.substring(0, s.length() - 1);
	try {
		rec.altitude = (int)((new Double(s).doubleValue() + 100000) *
				     100);
	}
	catch (NumberFormatException e) {
		throw new TextParseException("Invalid LOC altitude");
	}
	
	/* Size */
	if (!st.hasMoreTokens())
		return rec;
	s = st.nextToken();
	if (s.length() > 1 && s.charAt(s.length() - 1) == 'm')
		s = s.substring(0, s.length() - 1);
	try {
		rec.size = (int) (100 * new Double(s).doubleValue());
	}
	catch (NumberFormatException e) {
		throw new TextParseException("Invalid LOC size");
	}
	
	/* Horizontal precision */
	if (!st.hasMoreTokens())
		return rec;
	s = st.nextToken();
	if (s.length() > 1 && s.charAt(s.length() - 1) == 'm')
		s = s.substring(0, s.length() - 1);
	try {
		rec.hPrecision = (int) (100 * new Double(s).doubleValue());
	}
	catch (NumberFormatException e) {
		throw new TextParseException("Invalid LOC horizontal " +
					     "precision");
	}
	
	/* Vertical precision */
	if (!st.hasMoreTokens())
		return rec;
	s = st.nextToken();
	if (s.length() > 1 && s.charAt(s.length() - 1) == 'm')
		s = s.substring(0, s.length() - 1);
	try {
		rec.vPrecision = (int) (100 * new Double(s).doubleValue());
	}
	catch (NumberFormatException e) {
		throw new TextParseException("Invalid LOC vertical precision");
	}
	return rec;
}

/** Convert to a String */
public String
rdataToString() {
	StringBuffer sb = new StringBuffer();
	if (latitude != 0 || longitude != 0 || altitude != 0) {
		long temp;
		char direction;
		NumberFormat nf = new DecimalFormat();
		nf.setMaximumFractionDigits(3);
		nf.setGroupingUsed(false);

		/* Latitude */
		temp = (latitude & 0xFFFFFFFF) - (1 << 31);
		if (temp < 0) {
			temp = -temp;
			direction = 'S';
		}
		else
			direction = 'N';

		sb.append(temp / (3600 * 1000)); /* degrees */
		temp = temp % (3600 * 1000);
		sb.append(" ");

		sb.append(temp / (60 * 1000)); /* minutes */
		temp = temp % (60 * 1000);
		sb.append(" ");

		sb.append(nf.format((double)temp / 1000)); /* seconds */
		sb.append(" ");

		sb.append(direction);
		sb.append(" ");

		/* Latitude */
		temp = (longitude & 0xFFFFFFFF) - (1 << 31);
		if (temp < 0) {
			temp = -temp;
			direction = 'W';
		}
		else
			direction = 'E';

		sb.append(temp / (3600 * 1000)); /* degrees */
		temp = temp % (3600 * 1000);
		sb.append(" ");

		sb.append(temp / (60 * 1000)); /* minutes */
		temp = temp % (60 * 1000);
		sb.append(" ");

		sb.append(nf.format((double)temp / 1000)); /* seconds */
		sb.append(" ");

		sb.append(direction);
		sb.append(" ");

		nf.setMaximumFractionDigits(2);

		/* Altitude */
		sb.append(nf.format((double)(altitude - 10000000)/100));
		sb.append("m ");

		/* Size */
		sb.append(nf.format((double)size/100));
		sb.append("m ");

		/* Horizontal precision */
		sb.append(nf.format((double)hPrecision/100));
		sb.append("m ");

		/* Vertical precision */
		sb.append(nf.format((double)vPrecision/100));
		sb.append("m");
	}
	return sb.toString();
}

/** Returns the latitude */
public double
getLatitude() {  
	return (double)(latitude - (1<<31)) / (3600 * 1000);
}       

/** Returns the longitude */
public double
getLongitude() {  
	return (double)(longitude - (1<<31)) / (3600 * 1000);
}       

/** Returns the altitude */
public double
getAltitude() {  
	return (double)(altitude - 10000000)/100;
}       

/** Returns the diameter of the enclosing sphere */
public double
getSize() {  
	return (double)size / 100;
}       

/** Returns the horizontal precision */
public double
getHPrecision() {  
	return (double)hPrecision / 100;
}       

/** Returns the horizontal precision */
public double
getVPrecision() {  
	return (double)vPrecision / 100;
}       

void
rrToWire(DataByteOutputStream out, Compression c) throws IOException {
	if (latitude == 0 && longitude == 0 && altitude == 0)
		return;

	out.writeByte(0); /* version */
	out.writeByte(toLOCformat(size));
	out.writeByte(toLOCformat(hPrecision));
	out.writeByte(toLOCformat(vPrecision));
	out.writeInt(latitude);
	out.writeInt(longitude);
	out.writeInt(altitude);
}

private static long
parseLOCformat(int b) throws WireParseException {
	long out = b >> 4;
	int exp = b & 0xF;
	if (out > 9 || exp > 9)
		throw new WireParseException("Invalid LOC Encoding");
	while (exp-- > 0)
		out *= 10;
	return (out);
}

private byte
toLOCformat(long l) {
	byte exp = 0;
	while (l > 9) {
		exp++;
		l = (l + 5) / 10;
	}
	return (byte)((l << 4) + exp);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8604.java