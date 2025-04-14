error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1731.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1731.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1731.java
text:
```scala
A@@bstractMessageFolder child = (AbstractMessageFolder) folder.getChildAt(0);

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
package org.columba.mail.filter;

import org.columba.core.config.DefaultItem;
import org.columba.core.xml.XmlElement;
import org.columba.mail.folder.virtual.VirtualFolder;


public class SearchItem extends DefaultItem {
    /*
private AdapterNode vFolderNode;
private VirtualFolder vFolder;
private Filter filter;
private AdapterNode searchNode;
*/
    public SearchItem(XmlElement root) {
        super(root);

        /*
this.vFolderNode = node;
this.vFolder = vFolder;

vFolder.setSearchFilter(this);

if (node != null)
        parseNode();
*/
    }

    /*
public VirtualFolder getFolder() {
        return vFolder;
}

protected void parseNode() {
        searchNode = vFolderNode.getChild("search");
        AdapterNode filterNode = searchNode.getChild("filter");

        filter = new Filter(filterNode);

}

public Filter getFilter() {
        return filter;
}

public int getUid() {
        AdapterNode uidNode = searchNode.getChild("uid");
        String uidStr = getTextValue(uidNode);
        Integer iStr = new Integer(uidStr);
        int uid = iStr.intValue();

        return uid;
}

public void setUid(int i) {
        Integer uid = new Integer(i);

        AdapterNode uidNode = searchNode.getChild("uid");

        setTextValue(uidNode, uid.toString());

}

public void setInclude(String s) {
        AdapterNode includeNode = searchNode.getChild("include");
        setTextValue(includeNode, s);
}

public boolean isInclude() {
        AdapterNode includeNode = searchNode.getChild("include");
        String include = getTextValue(includeNode);

        if (include.equals("true"))
                return true;
        else
                return false;
}
*/
    public void addSearchToHistory(VirtualFolder folder) {
        if (folder.getUid() == 106) {
            addSearchToHistory();
        }
    }

    public void addSearchToHistory() {
        /*
//System.out.println("selectedfolder:"+ MainInterface.treeViewer.getSelected().getName());
VirtualFolder folder =
        (VirtualFolder) MainInterface.treeModel.getFolder(106);

if (folder.getChildCount() >= 10)
{
        MessageFolder child = (MessageFolder) folder.getChildAt(0);
        child.removeFromParent();
}

String name = "search result";
VirtualFolder vFolder2 =
        (VirtualFolder) MainInterface.treeModel.addVirtualFolder(folder, name);
Search s = vFolder2.getSearchFilter();
s.setUid(getUid());
s.setInclude((new Boolean(isInclude())).toString());
s.getFilter().getFilterRule().removeAll();
s.getFilter().getFilterRule().setCondition(
        getFilter().getFilterRule().getCondition());
for (int i = 0; i < getFilter().getFilterRule().count(); i++)
{
        FilterCriteria c = getFilter().getFilterRule().getCriteria(i);
        s.getFilter().getFilterRule().addEmptyCriteria();
        FilterCriteria newc = s.getFilter().getFilterRule().getCriteria(i);
        newc.setCriteria(c.getCriteria());
        newc.setHeaderItem(c.getHeaderItem());
        newc.setPattern(c.getPattern());
        newc.setType(c.getType());

        if (i == 0)
        {
                // lets find a good name for our new vfolder

                StringBuffer buf = new StringBuffer();

                if (newc.getType().equalsIgnoreCase("flags"))
                {
                        System.out.println("flags found");

                        buf.append(newc.getType());
                        buf.append(" (");
                        buf.append(newc.getCriteria());
                        buf.append(" ");
                        buf.append(newc.getPattern());
                        buf.append(")");
                }
                else if (newc.getType().equalsIgnoreCase("custom headerfield"))
                {

                        buf.append(newc.getHeaderItem());
                        buf.append(" (");
                        buf.append(newc.getCriteria());
                        buf.append(" ");
                        buf.append(newc.getPattern());
                        buf.append(")");
                }
                else
                {
                        buf.append(newc.getType());
                        buf.append(" (");
                        buf.append(newc.getCriteria());
                        buf.append(" ");
                        buf.append(newc.getPattern());
                        buf.append(")");

                }
                System.out.println("newname:" + buf);

                vFolder2.setName(buf.toString());
                TreeNodeEvent updateEvent2 = new TreeNodeEvent(vFolder2, TreeNodeEvent.UPDATE);
                MainInterface.crossbar.fireTreeNodeChanged(updateEvent2);
        }

}
*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1731.java