error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5608.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5608.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5608.java
text:
```scala
s@@etBorder(BorderFactory.createEmptyBorder(1,1,1,1));

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
package org.columba.mail.gui.message;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkListener;

import org.columba.mail.gui.attachment.AttachmentView;
import org.columba.mail.message.ColumbaHeader;


public class MessageView extends JScrollPane {
    public static final int VIEWER_HTML = 1;
    public static final int VIEWER_SIMPLE = 0;

    //private HtmlViewer debug;
    protected MouseListener listener;

    //protected MessageController messageViewer;
    protected int active;

    //HyperlinkTextViewer viewer;
    //JList list;
    protected JPanel panel;
    protected HeaderViewer hv;
    protected BodyTextViewer bodyTextViewer;
    protected MessageController messageController;
    protected SecurityIndicator pgp;

    public MessageView(MessageController controller,
        AttachmentView attachmentView) {
        super();
        this.messageController = controller;

        getViewport().setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel = new MessagePanel();
        //panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.setLayout(new BorderLayout());

        setViewportView(panel);

        active = VIEWER_SIMPLE;

        hv = new HeaderViewer();
        panel.add(hv, BorderLayout.NORTH);

        bodyTextViewer = new BodyTextViewer();
        panel.add(bodyTextViewer, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());

        pgp = new SecurityIndicator();
        bottom.add(pgp, BorderLayout.NORTH);
        bottom.add(attachmentView, BorderLayout.CENTER);

        panel.add(bottom, BorderLayout.SOUTH);
    }

    public void addHyperlinkListener(HyperlinkListener l) {
        hv.addHyperlinkListener(l);
        bodyTextViewer.addHyperlinkListener(l);
    }

    public void addMouseListener(MouseListener l) {
        hv.addMouseListener(l);
        bodyTextViewer.addMouseListener(l);
    }

    public void setDoc(ColumbaHeader header, String str, boolean html,
        boolean hasAttachments) throws Exception {
        if (header != null) {
            hv.setHeader(header, hasAttachments);
        }

        bodyTextViewer.setBodyText(str, html);
    }

    public void setDoc(ColumbaHeader header, InputStream in, boolean html,
        boolean hasAttachments) throws Exception {
        StringBuffer text = new StringBuffer();
        int next = in.read();

        while (next != -1) {
            text.append((char) next);
            next = in.read();
        }

        setDoc(header, text.toString(), html, hasAttachments);
    }

    /**
     * @return
     */
    public SecurityIndicator getPgp() {
        return pgp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5608.java