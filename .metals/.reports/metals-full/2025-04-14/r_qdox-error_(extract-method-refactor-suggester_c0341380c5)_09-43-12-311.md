error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4427.java
text:
```scala
D@@ate date = DateParser.parseString((String) h.get("Date"));

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.columba.mail.parser;

import java.util.Date;
import java.util.TimeZone;

import org.columba.mail.coder.EncodedWordDecoder;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.Message;
import org.columba.mail.message.MimeHeader;
import org.columba.mail.message.MimePart;
import org.columba.mail.message.MimePartTree;

public class Rfc822Parser extends AbstractParser
{

	// Definition of Constants

	private static final int RETURN_PATH = 0;
	private static final int RECEIVED = 1;
	private static final int FROM = 2;
	private static final int SENDER = 3;
	private static final int REPLY_TO = 4;
	private static final int RESENT_FROM = 5;
	private static final int RESENT_SENDER = 6;
	private static final int RESENT_REPLY_TO = 7;
	private static final int DATE = 8;
	private static final int RESENT_DATE = 9;
	private static final int TO = 10;
	private static final int RESENT_TO = 11;
	private static final int CC = 12;
	private static final int RESENT_CC = 13;
	private static final int BCC = 14;
	private static final int RESENT_BCC = 15;
	private static final int MESSAGE_ID = 16;
	private static final int RESENT_MESSAGE_ID = 17;
	private static final int IN_REPLY_TO = 18;
	private static final int REFERENCES = 19;
	private static final int KEYWORDS = 20;
	private static final int SUBJECT = 21;
	private static final int COMMENTS = 22;
	private static final int ENCRYPTED = 23;
	private static final int Mime_VERSION = 24;
	private static final int X_ADDITIONAL = 25;

	// Global Variables
	private String[] input;
	private EncodedWordDecoder decoder;
	private ColumbaHeader parsedHeader;
	private boolean mime;
	private String mimeVer;

	// Constructor

	// Public Methods
	public Rfc822Parser()
	{

	}

	// the boolean parseHeader argument:
	// if true the header gets parsed, otherwise not
	//
	// action can be 0 or 1:
	// 0 means: just parse bodystructure and headers
	// 1 means: if encrypted or signed use pgp to read message
	public Message parse(
		String input,
		boolean parseHeader,
		ColumbaHeader header,
		int action)
	{

		//System.out.println("------------------------------Parsing RFC822");

		Message output = new Message();
		MimePartTree mimeParts ;
		this.input = divideMessage(input);

		decoder = new EncodedWordDecoder();

		if (parseHeader)
		{
			ColumbaHeader tempHeader = parseHeaderString(this.input[0]);
			//System.out.println( tempHeader.get( "mime-version" ));
			tempHeader.copyColumbaKeys(header);
			output.setHeader(tempHeader);
		}
		else
		{
			output.setHeader(header);
		}

		String mimeValue = (String) output.getHeader().get("mime-version");
		//System.out.println( mimeValue );

		if (mimeValue != null)
		{
			mime = true;
			
			  //Commented for more robustness with MIME-Version
			/*
			        if ( mimeValue.equals("1.0") )
			{
			    mimeVer = new String("1.0");
			    mime=true;
			}
			        else
			{
			
			    mime=false;
			}
			*/
			
		}
		else
		{
			//System.out.println("---------------------------NOT Parsing MIME");
			mime = false;
		}

		if (mime)
		{
			MimeParser mimeParser = new MimeParser(header, action);

			//System.out.println("------------------------------Parsing MIME");

			mimeParts = new MimePartTree( mimeParser.parse(input) );
		}
		else
		{
			MimeHeader helpHeader = new MimeHeader();

			mimeParts = new MimePartTree( new MimePart(helpHeader, this.input[1]));
		}

		output.setMimePartTree(mimeParts);

		return output;
	}

	// Private Methods

	// Method that returns the Code for the Rfc822 Header-Entity

	private int getCode(String par)
	{
		if (par.equals("Return-Path"))
			return RETURN_PATH;
		if (par.equals("Received"))
			return RECEIVED;
		if (par.equals("From"))
			return FROM;
		if (par.equals("Sender"))
			return SENDER;
		if (par.equals("Reply-To"))
			return REPLY_TO;
		if (par.equals("Resent-From"))
			return RESENT_FROM;
		if (par.equals("Resent-Sender"))
			return RESENT_SENDER;
		if (par.equals("Resent-Reply-To"))
			return RESENT_REPLY_TO;
		if (par.equals("Date"))
			return DATE;
		if (par.equals("Resent-Date"))
			return RESENT_DATE;
		if (par.equals("To"))
			return TO;
		if (par.equals("Resent-To"))
			return RESENT_TO;
		if (par.equals("cc"))
			return CC;
		if (par.equals("Resent-cc"))
			return RESENT_CC;
		if (par.equals("bcc"))
			return BCC;
		if (par.equals("Resent-bcc"))
			return RESENT_BCC;
		if (par.equals("Message-ID"))
			return MESSAGE_ID;
		if (par.equals("Resent-Message-ID"))
			return RESENT_MESSAGE_ID;
		if (par.equals("In-Reply-To"))
			return IN_REPLY_TO;
		if (par.equals("References"))
			return REFERENCES;
		if (par.equals("Keywords"))
			return KEYWORDS;
		if (par.equals("Subject"))
			return SUBJECT;
		if (par.equals("Comments"))
			return COMMENTS;
		if (par.equals("Encrypted"))
			return ENCRYPTED;

		if (par.equals("Mime-Version"))
			return Mime_VERSION;

		if (par.startsWith("x"))
			return X_ADDITIONAL;
		if (par.startsWith("X"))
			return X_ADDITIONAL;

		return -1;
	}

	// This Method is the Heart of the class as it parses the Header

	public ColumbaHeader parseHeader(String message)
	{
		if (message == null)
		{

		}
		String[] divided = divideMessage(message);
		return parseHeaderString(divided[0]);
	}

	private ColumbaHeader parseHeaderString(String input) //, ColumbaHeader output )
	{
		String header;
		ColumbaHeader output = new ColumbaHeader();
		String line;
		StringBuffer received;

		//        if ( output == null ) output = new ColumbaHeader();

		// Check if Parameter is separated Header or whole Message
		if (decoder == null)
			decoder = new EncodedWordDecoder();

		HeaderTokenizer tokenizer = new HeaderTokenizer(input);

		line = tokenizer.nextLine();

		while (line != null)
		{
			if (line.indexOf(':') != -1)
			{

				String key = line.substring(0, line.indexOf(':'));
				
				// TODO fix toLowerCase problems
				//key = key.toLowerCase();

				output.set(key, decoder.decode(line.substring(line.indexOf(':') + 1).trim()));
			}
			line = tokenizer.nextLine();
		}

		return output;
	}
	
	public void addColumbaHeaderFields(ColumbaHeader h) {
		long OneDay = 24 * 60 * 60 * 1000;
		TimeZone localTimeZone = TimeZone.getDefault();

		h.set("columba.flags.recent", new Boolean(false));
		//m.setHost( item.getHost() );
		h.set("columba.host", new String(""));

		Date date = DateParser.parseString((String) h.get("date"));
		//System.out.println("date1: "+ h.get("date") );
		//m.setDate( date );
		h.set("columba.date", date);
		Date date2 = (Date) h.get("columba.date");

		long t = date.getTime();
		long datediff =
			((System.currentTimeMillis()
				+ localTimeZone.getRawOffset()
				- ((t + localTimeZone.getRawOffset()) / OneDay) * OneDay)
				/ OneDay);
		if (datediff > 1) {
			h.set("columba.flags.seen", new Boolean(true));
		}

		//System.out.println("date2: "+ h.get("columba.date") );

		String shortFrom = (String) h.get("From");
		if (shortFrom != null) {
			if (shortFrom.indexOf("<") != -1) {
				shortFrom = shortFrom.substring(0, shortFrom.indexOf("<"));
				if (shortFrom.length() > 0) {
					if (shortFrom.startsWith("\"") && shortFrom.length() > 1)
						shortFrom =
							shortFrom.substring(1, shortFrom.length() - 1);
					if (shortFrom.endsWith("\"") && shortFrom.length() > 1)
						shortFrom =
							shortFrom.substring(0, shortFrom.length() - 1);
				} else {
					// example: <test@test.de>
					// remove the "<" and the ">"

					shortFrom = (String) h.get("From");

					if (shortFrom.startsWith("<"))
						shortFrom =
							shortFrom.substring(1, shortFrom.length() - 1);
					if (shortFrom.endsWith(">"))
						shortFrom =
							shortFrom.substring(0, shortFrom.length() - 1);
				}

			} 
			/*else
				shortFrom = shortFrom;
			*/
			//m.setShortFrom( shortFrom );
			h.set("columba.from", shortFrom);
		} else {
			//m.setShortFrom("");
			h.set("columba.from", new String(""));
		}

		String priority = (String) h.get("X-Priority");
		if (priority != null) {
			//m.setPriority( prio );
			try {
				Integer p = new Integer(priority);
				h.set("columba.priority", p);
			} catch (Exception ex) {
				h.set("columba.priority", new Integer(3));
			}

		} else {
			//m.setPriority( 3 );
			h.set("columba.priority", new Integer(3));
		}

		String attachment = (String) h.get("Content-Type");
		if (attachment != null) {
			attachment = attachment.toLowerCase();

			if (attachment.indexOf("multipart") != -1) {
				//m.setAttachment(true);
				h.set("columba.attachment", new Boolean(true));
			} else {
				h.set("columba.attachment", new Boolean(false));
				//m.setAttachment(false);
			}
		} else {
			h.set("columba.attachment", new Boolean(false));
			//m.setAttachment(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4427.java