error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8240.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8240.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8240.java
text:
```scala
S@@ystem.out.println(";; done (" + response.getNumBytes() + " bytes)");

import java.io.*;
import java.util.*;

public class dig {

static dnsName name = null;
static short type = dns.A, _class = dns.IN;

static void usage() {
	System.out.println("Usage: dig [@server] name [<type>] [<class>]");
	System.exit(0);
}

static void doQuery(dnsMessage query, dnsResolver res) throws IOException {
	dnsMessage response;

	System.out.println("; java dig 0.0");

	response = res.send(query);

	System.out.print(";; ->>HEADER<<- ");
	System.out.print("opcode: ");
	System.out.print(dns.opcodeString(response.getHeader().getOpcode()));
	System.out.print(", status: ");
	System.out.print(dns.rcodeString(response.getHeader().getRcode()));
	System.out.println(", id: " + response.getHeader().getID());

	
	System.out.print(";; flags: " + response.getHeader().printFlags());
	System.out.print("; ");
	for (int i = 0; i < 4; i++) {
		System.out.print(dns.sectionString(i));
		System.out.print(": ");
		System.out.print(response.getHeader().getCount(i));
		System.out.print(" ");
	}
	System.out.println();

	if (response.getHeader().getCount(dns.QUESTION) > 0) {
		int i = dns.QUESTION;
		System.out.println(";; " + dns.longSectionString(i) + ":");
		Enumeration e = response.getSection(i).elements();
		while (e.hasMoreElements()) {
			dnsRecord rec = (dnsRecord) e.nextElement();
			System.out.print(";;\t");
			System.out.print(rec.rname);
			System.out.print(", type = ");
			System.out.print(dns.typeString(rec.rtype));
			System.out.print(", class = ");
			System.out.println(dns.classString(rec.rclass));
		}
		System.out.println();
	}

	for (int i = 1; i < 4; i++) {
		if (response.getHeader().getCount(i) == 0)
			continue;

		System.out.println(";; " + dns.longSectionString(i) + ":");
		Enumeration e = response.getSection(i).elements();
		while (e.hasMoreElements()) {
			System.out.println(e.nextElement());
		}
		System.out.println();
	}
	System.out.println(";; done");
}

static void doAXFR(dnsMessage query, dnsResolver res) throws IOException {
	dnsMessage response;

	System.out.println("; java dig 0.0 <> " + name + " axfr");

	response = res.sendAXFR(query);

	Enumeration e = response.getSection(dns.ANSWER).elements();
	while (e.hasMoreElements())
		System.out.println(e.nextElement());

	System.out.println(";; done");
}

public static void main(String argv[]) throws IOException {
	String server;
	int arg;
	dnsMessage query = new dnsMessage();
	dnsRecord rec;
	dnsResolver res = null;

	query.getHeader().setRandomID();
	query.getHeader().setFlag(dns.RD);
	query.getHeader().setOpcode(dns.QUERY);

	if (argv.length < 1) {
		usage();
	}

	try {
		arg = 0;
		if (argv[arg].startsWith("@")) {
			server = argv[arg++].substring(1);
			res = new dnsResolver(server);
		}
		else
			res = new dnsResolver("localhost");

		name = new dnsName(argv[arg++]);

		type = dns.typeValue(argv[arg]);
		if (type < 0)
			type = dns.A;
		else
			arg++;

		_class = dns.classValue(argv[arg]);
		if (_class < 0)
			_class = dns.IN;
		else
			arg++;

		while (argv[arg].startsWith("-") && argv[arg].length() > 1) {
			switch (argv[arg].charAt(1)) {
			    case 'p':
				String portStr;
				int port;
				if (argv[arg].length() > 2)
					portStr = argv[arg].substring(2);
				else
					portStr = argv[++arg];
				port = Integer.parseInt(portStr);
				if (port < 0 || port > 65536) {
					System.out.println("Invalid port");
					return;
				}
				res.setPort(port);
				break;

			    case 'k':
				String keyStr;
				if (argv[arg].length() > 2)
					keyStr = argv[arg].substring(2);
				else
					keyStr = argv[++arg];
				byte [] key = keyStr.getBytes();
				res.setTSIGKey(key);
				break;

			    default:
				System.out.print("Invalid option");
				System.out.println(argv[arg]);
			}
			arg++;
		}

	}
	catch (ArrayIndexOutOfBoundsException e) {
		if (name == null)
			usage();
	}

	rec = dnsRecord.newRecord(name, type, _class);
	query.addRecord(dns.QUESTION, rec);

	if (type == dns.AXFR)
		doAXFR(query, res);
	else
		doQuery(query, res);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8240.java