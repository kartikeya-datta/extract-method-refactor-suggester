error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9797.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9797.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9797.java
text:
```scala
(@@byte)0, ExtendedFlags.DO);

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

import java.io.*;
import org.xbill.DNS.*;

/** @author Brian Wellington &lt;bwelling@xbill.org&gt; */

public class dig {

static Name name = null;
static int type = Type.A, dclass = DClass.IN;

static void
usage() {
	System.out.println("Usage: dig [@server] name [<type>] [<class>] " +
			   "[options]");
	System.exit(0);
}

static void
doQuery(Message response, long ms) throws IOException {
	System.out.println("; java dig 0.0");
	System.out.println(response);
	System.out.println(";; Query time: " + ms + " ms");
}

static void
doAXFR(Message response) throws IOException {
	System.out.println("; java dig 0.0 <> " + name + " axfr");
	if (response.isSigned()) {
		System.out.print(";; TSIG ");
		if (response.isVerified())
			System.out.println("ok");
		else
			System.out.println("failed");
	}

	if (response.getRcode() != Rcode.NOERROR) {
		System.out.println(response);
		return;
	}

	Record [] records = response.getSectionArray(Section.ANSWER);
	for (int i = 0; i < records.length; i++)
		System.out.println(records[i]);

	System.out.print(";; done (");
	System.out.print(response.getHeader().getCount(Section.ANSWER));
	System.out.print(" records, ");
	System.out.print(response.getHeader().getCount(Section.ADDITIONAL));
	System.out.println(" additional)");
}

public static void
main(String argv[]) throws IOException {
	String server;
	int arg;
	Message query, response;
	Record rec;
	Record opt = null;
	Resolver res = null;
	boolean printQuery = false;
	long startTime, endTime;

	if (argv.length < 1) {
		usage();
	}

	try {
		arg = 0;
		if (argv[arg].startsWith("@")) {
			server = argv[arg++].substring(1);
			res = new SimpleResolver(server);
		}
		else
			res = new ExtendedResolver();

		String nameString = argv[arg++];
		if (nameString.equals("-x")) {
			name = ReverseMap.fromAddress(argv[arg++]);
			type = Type.PTR;
			dclass = DClass.IN;
		}
		else {
			name = Name.fromString(nameString, Name.root);
			type = Type.value(argv[arg]);
			if (type < 0)
				type = Type.A;
			else
				arg++;

			dclass = DClass.value(argv[arg]);
			if (dclass < 0)
				dclass = DClass.IN;
			else
				arg++;
		}

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
				String key;
				if (argv[arg].length() > 2)
					key = argv[arg].substring(2);
				else
					key = argv[++arg];
				int index = key.indexOf('/');
				if (index < 0)
					res.setTSIGKey(key);
				else
					res.setTSIGKey(key.substring(0, index),
						       key.substring(index+1));
				break;

			    case 't':
				res.setTCP(true);
				break;

			    case 'i':
				res.setIgnoreTruncation(true);
				break;

			    case 'e':
				String ednsStr;
				int edns;
				if (argv[arg].length() > 2)
					ednsStr = argv[arg].substring(2);
				else
					ednsStr = argv[++arg];
				edns = Integer.parseInt(ednsStr);
				if (edns < 0 || edns > 1) {
					System.out.println("Unsupported " +
							   "EDNS level: " +
							   edns);
					return;
				}
				res.setEDNS(edns);
				break;

			    case 'd':
			    	opt = new OPTRecord((short)1280, (byte)0,
						    (byte)0, Flags.DO);
				break;

			    case 'q':
			    	printQuery = true;
				break;

			    default:
				System.out.print("Invalid option: ");
				System.out.println(argv[arg]);
			}
			arg++;
		}

	}
	catch (ArrayIndexOutOfBoundsException e) {
		if (name == null)
			usage();
	}

	rec = Record.newRecord(name, type, dclass);
	query = Message.newQuery(rec);
	if (opt != null)
		query.addRecord(opt, Section.ADDITIONAL);
	if (printQuery)
		System.out.println(query);
	startTime = System.currentTimeMillis();
	response = res.send(query);
	endTime = System.currentTimeMillis();

	if (type == Type.AXFR)
		doAXFR(response);
	else
		doQuery(response, endTime - startTime);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9797.java