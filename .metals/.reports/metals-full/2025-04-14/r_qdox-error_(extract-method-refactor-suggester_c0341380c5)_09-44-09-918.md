error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7422.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7422.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7422.java
text:
```scala
S@@tring[] uids = mediator.getTable().getUids();

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//Portions created by Celso Pinto are Copyright (C) 2004.
//
//All Rights Reserved.

package org.columba.addressbook.gui.table;

import java.awt.event.MouseEvent;

import org.columba.addressbook.folder.AbstractFolder;
import org.columba.addressbook.folder.GroupFolder;
import org.columba.addressbook.folder.IContactStorage;
import org.columba.addressbook.gui.dialog.contact.ContactEditorDialog;
import org.columba.addressbook.gui.frame.AddressbookFrameMediator;
import org.columba.addressbook.model.ContactModel;
import org.columba.addressbook.model.IContactModel;
import org.columba.core.gui.base.DoubleClickListener;
import org.columba.core.gui.dialog.ErrorDialog;
import org.columba.core.logging.Logging;


/**
 * @author Celso Pinto &lt;cpinto@yimports.com&gt;
 */
public class TableMouseListener extends DoubleClickListener
{
	private TableController controller = null;
	private AddressbookFrameMediator mediator = null;
	public TableMouseListener(TableController tableController)
	{
		controller = tableController;  
		mediator = controller.getMediator();
	}

  public void doubleClick(MouseEvent e)
  {
	  /*
	   * does exactly the same thing as EditPropertiesAction when contact
	   * table is focused
	   * */
	  if (e.getButton()==MouseEvent.BUTTON1 &&
	      e.getClickCount() > 1)
	  {
	    
			// get selected contact/group card
      Object[] uids = mediator.getTable().getUids();

      // get selected folder
      IContactStorage folder = (IContactStorage) mediator.getTree()
                                                       .getSelectedFolder();

      if (uids.length == 0)
        return;

      // FIXME: ugly cast to ContactModel
      IContactModel card = null;
      try
      {
        card = (IContactModel) folder.get(uids[0]);
      }
      catch (Exception ex)
      {

        if (Logging.DEBUG)
          ex.printStackTrace();

        ErrorDialog.createDialog(ex.getMessage(), ex);
      }

      ContactEditorDialog dialog = new ContactEditorDialog(mediator.getView().getFrame(),
                                               (ContactModel) card);

      if (dialog.getResult())
      {

        try
        {
          // modify card properties in folder
          folder.modify(uids[0], card);
        }
        catch (Exception e1)
        {
          if (Logging.DEBUG)
            e1.printStackTrace();

          ErrorDialog.createDialog(e1.getMessage(), e1);
        }

        if (folder instanceof GroupFolder)
          //					 re-select folder
          mediator.getTree().setSelectedFolder((AbstractFolder) folder);

			}
	  	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7422.java