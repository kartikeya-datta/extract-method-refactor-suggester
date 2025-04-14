error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7889.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7889.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7889.java
text:
```scala
C@@olumbaLogger.log.severe("could not find popserver");

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
package org.columba.mail.pop3;

import org.columba.core.logging.ColumbaLogger;

import org.columba.mail.config.AccountItem;
import org.columba.mail.config.AccountList;
import org.columba.mail.config.MailConfig;
import org.columba.mail.config.PopItem;
import org.columba.mail.pop3.event.ModelChangeListener;
import org.columba.mail.pop3.event.ModelChangedEvent;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;


public class POP3ServerCollection //implements ActionListener
 {
    private List serverList;
    private POP3Server popServer;
    private List listeners;

    public POP3ServerCollection() {
        serverList = new Vector();
        listeners = new Vector();

        AccountList list = MailConfig.getAccountList();

        for (int i = 0; i < list.count(); i++) {
            AccountItem accountItem = list.get(i);

            if (accountItem.isPopAccount()) {
                add(accountItem);
            }
        }
    }

    public ListIterator getServerIterator() {
        return serverList.listIterator();
    }

    public POP3ServerController[] getList() {
        POP3ServerController[] list = new POP3ServerController[count()];

        ((Vector) serverList).copyInto(list);

        return list;
    }

    public void add(AccountItem item) {
        POP3ServerController server = new POP3ServerController(item);
        serverList.add(server);

        notifyListeners(new ModelChangedEvent(ModelChangedEvent.ADDED, server));
    }

    public POP3ServerController uidGet(int uid) {
        int index = getIndex(uid);

        if (index != -1) {
            return get(index);
        } else {
            return null;
        }
    }

    public POP3ServerController get(int index) {
        return (POP3ServerController) serverList.get(index);
    }

    public int count() {
        return serverList.size();
    }

    public void removePopServer(int uid) {
        int index = getIndex(uid);
        POP3ServerController server;

        if (index == -1) {
            ColumbaLogger.log.error("could not find popserver");

            return;
        } else {
            server = (POP3ServerController) serverList.remove(index);
        }

        notifyListeners(new ModelChangedEvent(ModelChangedEvent.REMOVED));
    }

    public int getIndex(int uid) {
        POP3ServerController c;
        int number;
        PopItem item;

        for (int i = 0; i < count(); i++) {
            c = get(i);
            number = c.getUid();

            if (number == uid) {
                return i;
            }
        }

        return -1;
    }

    public void saveAll() {
        POP3ServerController c;

        for (int i = 0; i < count(); i++) {
            c = get(i);

            try {
                c.getServer().save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public POP3Server getSelected() {
        return popServer;
    }

    public void addModelListener(ModelChangeListener l) {
        listeners.add(l);
    }

    private void notifyListeners(ModelChangedEvent e) {
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            ((ModelChangeListener) it.next()).modelChanged(e);

            // for (int i = 0; i < listeners.size(); i++) {
            // ((ModelChangeListener) listeners.get(i)).modelChanged(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7889.java