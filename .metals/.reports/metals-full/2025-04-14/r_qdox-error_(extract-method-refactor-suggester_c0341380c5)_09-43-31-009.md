error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1550.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1550.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1550.java
text:
```scala
i@@f (flags.getDeleted()) {

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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.table.plugins;

import org.columba.core.gui.util.ImageLoader;

import org.columba.mail.gui.table.model.MessageNode;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.util.MailResourceLoader;

import org.columba.ristretto.message.Flags;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class StatusRenderer extends DefaultLabelRenderer {

    boolean bool;

    ImageIcon image1;

    ImageIcon image2;

    ImageIcon image4;

    ImageIcon image3;

    ImageIcon image5;

    ImageIcon image6;

    ImageIcon image7;

    public StatusRenderer() {
        super();

        setHorizontalAlignment(SwingConstants.CENTER);

        //setOpaque(true);
        image1 = ImageLoader.getSmallImageIcon("reply_small.png");
        image2 = ImageLoader.getSmallImageIcon("mail-new.png");
        image3 = ImageLoader.getSmallImageIcon("stock_delete-16.png");
        image4 = ImageLoader.getSmallImageIcon("mark-as-important-16.png");
        image5 = ImageLoader.getSmallImageIcon("mail-read.png");
        image6 = ImageLoader.getSmallImageIcon("mail-new.png");
        image7 = ImageLoader.getSmallImageIcon("drafts-16.png");
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);

        if (value == null) {
            setIcon(null);

            return this;
        }

        if (value instanceof String) {
            System.out
                    .println("statusrenderer-> instanceof String not expected");

            return this;
        }

        Flags flags = ((ColumbaHeader) ((MessageNode) value).getHeader())
                .getFlags();

        if (flags.getExpunged()) {
            setIcon(image3);

            setToolTipText(MailResourceLoader.getString("header", "column",
                    "expunged"));
        } else if (flags.getAnswered()) {
            setIcon(image1);
            setToolTipText(MailResourceLoader.getString("header", "column",
                    "answered"));
        } else if (flags.getDraft()) {
            setIcon(image7);
            setToolTipText(MailResourceLoader.getString("header", "column",
                    "draft"));
        } else if (!flags.getSeen()) {
            setIcon(image6);
            setToolTipText(MailResourceLoader.getString("header", "column",
                    "unread"));
        } else if (flags.getSeen()) {
            setIcon(image5);
            setToolTipText(MailResourceLoader.getString("header", "column",
                    "read"));
        }  else {
            setIcon(null);
        }

        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1550.java