error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1566.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1566.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1566.java
text:
```scala
public v@@oid updateSelectedGUI() throws Exception {

package org.columba.mail.pop3;

import java.util.Vector;

import org.columba.core.command.Command;
import org.columba.core.command.CompoundCommand;
import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.Worker;
import org.columba.core.gui.FrameController;
import org.columba.core.logging.ColumbaLogger;
import org.columba.main.MainInterface;
import org.columba.mail.command.POP3CommandReference;
import org.columba.mail.filter.Filter;
import org.columba.mail.filter.FilterList;
import org.columba.mail.folder.Folder;
import org.columba.mail.gui.frame.MailFrameController;
import org.columba.mail.gui.table.TableChangedEvent;
import org.columba.mail.message.HeaderInterface;
import org.columba.mail.message.Message;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FetchNewMessagesCommand extends Command {

	POP3Server server;

	/**
	 * Constructor for FetchNewMessages.
	 * @param frameController
	 * @param references
	 */
	public FetchNewMessagesCommand(
		FrameController frameController,
		DefaultCommandReference[] references) {
		super(frameController, references);
		
		POP3CommandReference[] r =
			(POP3CommandReference[]) getReferences(FIRST_EXECUTION);

		server = r[0].getServer();
	}

	/**
	 * @see org.columba.core.command.Command#updateGUI()
	 */
	public void updateGUI() throws Exception {
	}

	/**
	 * @see org.columba.core.command.Command#execute(Worker)
	 */
	public void execute(Worker worker) throws Exception {
		POP3CommandReference[] r =
			(POP3CommandReference[]) getReferences(FIRST_EXECUTION);

		server = r[0].getServer();

		Vector newUIDList = fetchUIDList();
		
		Vector messageSizeList = fetchMessageSizes();
		

		Vector newMessagesUIDList = synchronize( newUIDList);
		

		downloadNewMessage( newUIDList, messageSizeList, newMessagesUIDList, worker );
	

		logout();
		
		

	}
	
	public void downloadNewMessage( Vector newUIDList, Vector messageSizeList, Vector newMessagesUIDList, Worker worker ) throws Exception
	{
		ColumbaLogger.log.info(
			"need to fetch " + newMessagesUIDList.size() + " messages.");	
			
		for (int i = 0; i < newMessagesUIDList.size(); i++) {
			Object serverUID = newMessagesUIDList.get(i);

			ColumbaLogger.log.info("fetch message with UID=" + serverUID);

			//int index = ( (Integer) result.get(serverUID) ).intValue();
			int index = newUIDList.indexOf(serverUID);
			ColumbaLogger.log.info(
				"vector index=" + index + " server index=" + (index + 1));

			int size = Integer.parseInt((String) messageSizeList.get(index));
			size = Math.round(size / 1024);

			if ( server.getAccountItem().getPopItem().isLimit() )
			{
				// check if message isn't too big to download
				int maxSize = Integer.parseInt(server.getAccountItem().getPopItem().getLimit());
				
				// if message-size is bigger skip download of this message
				if ( size > maxSize ) 
				{
					ColumbaLogger.log.info(
					"skipping download of message, too big");
					continue;
				}
			}
			// server message numbers start with 1
			// whereas Vector numbers start with 0
			//  -> always increase fetch number
			Message message = server.getMessage(index + 1, serverUID);
			message.getHeader().set("columba.size", new Integer(size));

			//System.out.println("message:\n" + message.getSource());

			// get inbox-folder from pop3-server preferences
			Folder inboxFolder = server.getFolder();
			Object uid = inboxFolder.addMessage(message, worker);
			Object[] uids = new Object[1];
			uids[0] = uid;
			
			HeaderInterface[] headerList = new HeaderInterface[1];
			headerList[0] = message.getHeader();
			headerList[0].set("columba.uid",uid);
			
			TableChangedEvent ev = new TableChangedEvent( TableChangedEvent.ADD, inboxFolder, headerList);
		 
			((MailFrameController)frameController).tableController.tableChanged(ev);
		
			

			FilterList list = inboxFolder.getFilterList();
			for (int j = 0; j < list.count(); j++) {
				Filter filter = list.get(j);

				Object[] result =
					inboxFolder.searchMessages(filter, uids, worker);
				if (result.length != 0) {
					CompoundCommand command =
						filter.getCommand(frameController, inboxFolder, result);

					MainInterface.processor.addOp(command);
				}
				
			}
			

		}
	}
	
	public Vector synchronize( Vector newUIDList ) throws Exception
	{
		ColumbaLogger.log.info(
			"synchronize local UID-list with remote UID-list");
		// synchronize local UID-list with server 		
		Vector newMessagesUIDList = server.synchronize(newUIDList);
		
		
		return newMessagesUIDList;
	}
	
	public Vector fetchMessageSizes() throws Exception
	{
		// fetch message-size list 		
		Vector messageSizeList = server.getMessageSizeList();
		ColumbaLogger.log.info(
			"fetched message-size-list capacity=" + messageSizeList.size());
		return messageSizeList;
		
	}
	
	public Vector fetchUIDList() throws Exception
	{
		// fetch UID list 		
		Vector newUIDList = server.getUIDList();
		ColumbaLogger.log.info(
			"fetched UID-list capacity=" + newUIDList.size());
			
		return newUIDList;
	}
	

	public void logout() throws Exception
	{
		server.logout();

		ColumbaLogger.log.info("logout");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1566.java