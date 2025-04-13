error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2276.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2276.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2276.java
text:
```scala
r@@eturn uid1.toString()+","+uid2.toString();

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
package org.columba.mail.imap.parser;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MessageSet {

	String messageSetString;
	/**
	 * Constructor for MessageSet.
	 */
	public MessageSet( Object[] uids) {
		messageSetString = parse( uids );		
		
	}
	
	public String getString()
	{
		return messageSetString;
	}
	
	public String parse(Object[] uids) {
		String messageSet = new String();
		StringBuffer messageSetList = new StringBuffer();
		//Message message = null;
		//Integer lastPos;
		int lastPos = 0;
		int newPos = 0;
		char lastChar = 'p';
		
		if ( uids.length == 1 ) 
		{
			Integer uid = new Integer(Integer.parseInt((String) uids[0]));
			return uid.toString();
		} 
		else if ( uids.length == 2 )
		{
			Integer uid1 = new Integer(Integer.parseInt((String) uids[0]));
			Integer uid2 = new Integer(Integer.parseInt((String) uids[1]));
			
			return uid1.toString()+":"+uid2.toString();
		}

		for (int i = 0; i < uids.length; i++) {
			//System.out.println("parsing="+uids[i]);
			
			Integer uid = new Integer(Integer.parseInt((String) uids[i]));

			if (i == 0) {

				lastPos = (uid).intValue();
				messageSetList.append(lastPos);
				//System.out.println("append_0: "+lastPos);

			} else if (i == 1) {
				newPos = (uid).intValue();
				if (lastPos + 1 == newPos) {
					//messageSetList.append("-");
					messageSetList.append(":");
					lastChar = '-';
					lastPos = (uid).intValue();

					System.out.println("i==1");

					//System.out.println("append_1a: "+ message.getUID());
				} else {
					lastPos = (uid).intValue();
					messageSetList.append("," + lastPos);
					lastChar = 'p';
					//System.out.println("append_1b: "+  message.getUID());
				}
			} else if (i == uids.length - 1) {
				if (lastChar == '-') {
					lastPos = (uid).intValue();
					messageSetList.append(lastPos);
					//System.out.println("append_last_a: "+ message.getUID());
				} else {
					lastPos = (uid).intValue();
					messageSetList.append("," + lastPos);
					//System.out.println("append_last_b: "+ message.getUID());
				}

			} else {
				newPos = (uid).intValue();

				if (lastChar == '-') {
					if (lastPos + 1 == newPos) {
						lastPos = (uid).intValue();
						//System.out.println("append_between_aa: "+ message.getUID());
					} else {
						messageSetList.append(lastPos);
						lastPos = (uid).intValue();
						messageSetList.append("," + lastPos);
						lastChar = 'p';
						//System.out.println("append_between_ab: "+  message.getUID());
					}
				} else {
					if (lastPos + 1 == newPos) {
						messageSetList.append(":");

						lastChar = '-';
						lastPos = (uid).intValue();

						//System.out.println("append :");
						//System.out.println("append_between_ba: "+  message.getUID());
					} else {
						lastPos = (uid).intValue();
						messageSetList.append("," + lastPos);

						//System.out.println("append_between_bb: "+  message.getUID());
					}

				}

			}

		}

		//System.out.println("messageSet: "+ messageSetList );
		
		
		return messageSetList.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2276.java