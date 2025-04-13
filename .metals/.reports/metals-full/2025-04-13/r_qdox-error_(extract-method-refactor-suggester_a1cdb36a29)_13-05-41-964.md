error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2724.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2724.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2724.java
text:
```scala
c@@olumbaHeader.set("columba.uid", "");

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
package org.columba.mail.message;

import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.Message;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;
import org.columba.ristretto.message.io.CharSequenceSource;
import org.columba.ristretto.message.io.Source;


/**
 * Adds Columba-specific features to the default {@link Message}
 * object found in the Ristretto API.
 * <p>
 *
 *
 * @author fdietz, tstich
 */
public class ColumbaMessage {
    protected ColumbaHeader columbaHeader;
    protected Flags flags;
    protected Message message;
    protected MimePart bodyPart;

    public ColumbaMessage() {
        this(new ColumbaHeader());
    }

    public ColumbaMessage(ColumbaHeader header) {
        columbaHeader = header;
        message = new Message();

        flags = columbaHeader.getFlags();
    }

    public ColumbaMessage(Message m) {
        columbaHeader = new ColumbaHeader(m.getHeader());
        message = m;

        flags = columbaHeader.getFlags();
    }

    public ColumbaMessage(Header header) {
        columbaHeader = new ColumbaHeader(header);
        message = new Message();
        message.setHeader(header);

        flags = columbaHeader.getFlags();
    }

    public ColumbaMessage(ColumbaHeader h, Message m) {
        columbaHeader = h;
        flags = columbaHeader.getFlags();

        columbaHeader.setHeader(m.getHeader());
        message = m;
    }

    public String getStringSource() {
        return getSource().toString();
    }

    public void setStringSource(String s) {
        message.setSource(new CharSequenceSource(s));
    }

    public void setBodyPart(MimePart body) {
        bodyPart = body;
    }

    public void setUID(Object o) {
        if (o != null) {
            columbaHeader.set("columba.uid", o);
        } else {
            columbaHeader.set("columba.uid", new String(""));
        }

        //uid = o;
    }

    public Object getUID() {
        return getHeader().get("columba.uid");
    }

    public MimeTree getMimePartTree() {
        return message.getMimePartTree();
    }

    public void setMimePartTree(MimeTree ac) {
        message.setMimePartTree(ac);
    }

    public void freeMemory() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.ristretto.message.Message#getHeader()
     */
    public ColumbaHeader getHeader() {
        return columbaHeader;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.ristretto.message.Message#setHeader(org.columba.ristretto.message.Header)
     */
    public void setHeader(ColumbaHeader h) {
        columbaHeader = h;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.ristretto.message.Message#getBodyPart()
     */
    public MimePart getBodyPart() {
        return bodyPart;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.ristretto.message.Message#getMimePart(int)
     */
    public MimePart getMimePart(int number) {
        return message.getMimePart(number);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.ristretto.message.Message#getMimePartCount()
     */
    public int getMimePartCount() {
        return message.getMimePartCount();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.ristretto.message.Message#getSource()
     */
    public Source getSource() {
        return message.getSource();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.ristretto.message.Message#setHeader(org.columba.ristretto.message.Header)
     */
    public void setHeader(Header h) {
        message.setHeader(h);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.ristretto.message.Message#setSource(org.columba.ristretto.message.io.Source)
     */
    public void setSource(Source source) {
        message.setSource(source);
    }

    /**
     * @return
     */
    public Flags getFlags() {
        return flags;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2724.java