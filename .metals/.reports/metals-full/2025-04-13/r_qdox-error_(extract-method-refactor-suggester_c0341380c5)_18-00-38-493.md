error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2166.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2166.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2166.java
text:
```scala
I@@MAPResponse r = protocol.getResponse(null);

package org.columba.mail.imap.protocol;

import java.io.DataOutputStream;

import org.columba.mail.imap.IMAPResponse;

/**
 * @author freddy
 *
 * Helper class which writes <class>Arguments</class> to
 * a <class>DataOutputStream</class>
 * 
 * 
 * 
 *   Following a short paragraph of RFC2060 IMAP:
 * 
 *
 *   A string is in one of two forms: literal and quoted string.  The
 *   literal form is the general form of string.  The quoted string form
 *   is an alternative that avoids the overhead of processing a literal at
 *   the cost of limitations of characters that can be used in a quoted
 *   string.
 *
 *   A literal is a sequence of zero or more octets (including CR and LF),
 *   prefix-quoted with an octet count in the form of an open brace ("{"),
 *   the number of octets, close brace ("}"), and CRLF.  In the case of
 *   literals transmitted from server to client, the CRLF is immediately
 *   followed by the octet data.  In the case of literals transmitted from
 *   client to server, the client MUST wait to receive a command
 *   continuation request (described later in this document) before
 *   sending the octet data (and the remainder of the command).
 *
 *   A quoted string is a sequence of zero or more 7-bit characters,
 *   excluding CR and LF, with double quote (<">) characters at each end.
 *
 *   The empty string is represented as either "" (a quoted string with
 *   zero characters between double quotes) or as {0} followed by CRLF (a
 *   literal with an octet count of 0).
 *
 *      Note: Even if the octet count is 0, a client transmitting a
 *      literal MUST wait to receive a command continuation request.
 * 
 */
public class ArgumentWriter {

	DataOutputStream output;
	IMAPProtocol protocol;
	/**
	 * Constructor for ArgumentWriter.
	 */
	public ArgumentWriter(IMAPProtocol protocol) {

		this.protocol = protocol;

		this.output = protocol.getOutputStream();

	}

	public void write(Arguments args) throws Exception {
		if (args.count() == 0)
			return;

		for (int i = 0; i < args.count(); i++) {
			Object value = args.get(i);

			if (args.count() > 0)
				output.write(' ');

			if (value instanceof ByteString) {

				writeString(((ByteString) value).getBytes());
			} else if (value instanceof byte[]) {
				writeBytes((byte[]) value);
			}

		}
	}

	/**
	 * send String (converted to byte[]) and decide:
	 *  - do we have to send it as literal
	 *  - do we have to quote this string, because of 
	 *    special escape characters
	 * 
	 * @param data
	 * @throws Exception
	 */
	protected void writeString(byte[] data) throws Exception {
		int length = data.length;
		boolean needsQuoting = false;
		boolean hasEscapeCharacters = false;

		// If lengthgth is greater than 1024 bytes, send as literal
		if (length > 1024) {
			writeBytes(data);
			return;
		}

		// if length==0, send as quoted string
		if (length == 0)
			needsQuoting = true;

		byte b;

		// (1) 
		// we have to take a look at every byte to see
		// if its an escape character
		// 
		// if it has escape characters, we have to quote the string
		//
		//   args#            1    2        3
		// example: CLIENT: LOGIN user my%{passwo\rd
		//
		// the client-request #3 contains escape characters,
		// which are already used by the IMAP protocol
		//
		// we have to quote "my%$passwo/rd"
		//
		// (2) 
		// we have to search for NUL, CR or LF characters, meaning
		// that this is a complete string we want to send
		// to the SERVER.
		// If we don't send this as literal, SERVER thinks, when
		// seeing CR/LF, that the CLIENT request is finished

		for (int i = 0; i < length; i++) {
			b = data[i];

			// search for problem (2)
			if (b == '\0' || b == '\r' || b == '\n' || ((b & 0xff) > 0177)) {
				// NUL, CR or LF characters found
				//  -> send as literal
				writeBytes(data);
				return;
			}

			// search for problem (1)
			if (b == '*'
 b == '%'
 b == '('
 b == ')'
 b == '{'
 b == '"'
 b == '\\'
 ((b & 0xff) <= ' ')) {
				needsQuoting = true;
				if (b == '"' || b == '\\')
					hasEscapeCharacters = true;
			}
		}

		// begin quoted string
		if (needsQuoting)
			output.write('"');

		if (hasEscapeCharacters) {

			for (int i = 0; i < length; i++) {
				b = data[i];
				if (b == '"' || b == '\\')
					output.write('\\');
				output.write(b);
			}
		} else
			output.write(data);

		// end quoted string
		if (needsQuoting)
			output.write('"');
	}

	/**
	 * send byte[] as literal
	 * 
	 * 
	 * @param data
	 * @throws Exception
	 */
	protected void writeBytes(byte[] data) throws Exception {
		output.write('{');
		output.writeBytes(Integer.toString(data.length));
		output.writeBytes("}\r\n");
		output.flush();

		for (;;) {
			IMAPResponse r = protocol.getResponse();
			if (r.isCONTINUATION())
				break;
		}

		output.write(data);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2166.java