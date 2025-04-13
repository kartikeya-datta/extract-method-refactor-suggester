error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4873.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4873.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4873.java
text:
```scala
m@@Priority = Priority.toPriority(aAtts.getValue("level"));

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */
package org.apache.log4j.chainsaw;

import java.util.StringTokenizer;
import org.apache.log4j.Priority;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A content handler for document containing Log4J events logged using the
 * XMLLayout class. It will create events and add them to a supplied model.
 *
 * @author <a href="mailto:oliver@puppycrawl.com">Oliver Burn</a>
 * @version 1.0
 */
class XMLFileHandler
    extends DefaultHandler
{
    /** represents the event tag **/
    private static final String TAG_EVENT = "log4j:event";
    /** represents the message tag **/
    private static final String TAG_MESSAGE = "log4j:message";
    /** represents the ndc tag **/
    private static final String TAG_NDC = "log4j:NDC";
    /** represents the throwable tag **/
    private static final String TAG_THROWABLE = "log4j:throwable";
    /** represents the location info tag **/
    private static final String TAG_LOCATION_INFO = "log4j:locationInfo";

    /** where to put the events **/
    private final MyTableModel mModel;
    /** the number of events in the document **/
    private int mNumEvents;
    /** the current element being parsed **/
    private String mCurrentElement;

    /** the time of the event **/
    private long mTimeStamp;
    /** the priority of the event **/
    private Priority mPriority;
    /** the category of the event **/
    private String mCategoryName;
    /** the NDC for the event **/
    private String mNDC;
    /** the thread for the event **/
    private String mThreadName;
    /** the msg for the event **/
    private String mMessage;
    /** the throwable details the event **/
    private String[] mThrowableStrRep;
    /** the location details for the event **/
    private String mLocationDetails;


    /**
     * Creates a new <code>XMLFileHandler</code> instance.
     *
     * @param aModel where to add the events
     */
    XMLFileHandler(MyTableModel aModel) {
        mModel = aModel;
    }

    /** @see DefaultHandler **/
    public void startDocument()
        throws SAXException
    {
        mNumEvents = 0;
    }

    /** @see DefaultHandler **/
    public void characters(char[] aChars, int aStart, int aLength) {
        if (mCurrentElement == TAG_NDC) {
            mNDC = new String(aChars, aStart, aLength);
        } else if (mCurrentElement == TAG_MESSAGE) {
            mMessage = new String(aChars, aStart, aLength);
        } else if (mCurrentElement == TAG_THROWABLE) {
            final StringTokenizer st =
                new StringTokenizer(new String(aChars, aStart, aLength), "\t");
            mThrowableStrRep = new String[st.countTokens()];
            if (mThrowableStrRep.length > 0) {
                mThrowableStrRep[0] = st.nextToken();
                for (int i = 1; i < mThrowableStrRep.length; i++) {
                    mThrowableStrRep[i] = "\t" + st.nextToken();
                }
            }
        }
    }

    /** @see DefaultHandler **/
    public void endElement(String aNamespaceURI,
                           String aLocalName,
                           String aQName)
    {
        if (TAG_EVENT.equals(aQName)) {
            addEvent();
            resetData();
        } else if (mCurrentElement != TAG_EVENT) {
            mCurrentElement = TAG_EVENT; // hack - but only thing I care about
        }
    }

    /** @see DefaultHandler **/
    public void startElement(String aNamespaceURI,
                             String aLocalName,
                             String aQName,
                             Attributes aAtts)
    {
        if (TAG_EVENT.equals(aQName)) {
            mThreadName = aAtts.getValue("thread");
            mTimeStamp = Long.parseLong(aAtts.getValue("timestamp"));
            mCategoryName = aAtts.getValue("category");
            mPriority = Priority.toPriority(aAtts.getValue("priority"));
        } else if (TAG_LOCATION_INFO.equals(aQName)) {
            mLocationDetails = aAtts.getValue("class") + "."
                + aAtts.getValue("method")
                + "(" + aAtts.getValue("file") + ":" + aAtts.getValue("line")
                + ")";
        } else if (TAG_NDC.equals(aQName)) {
            mCurrentElement = TAG_NDC;
        } else if (TAG_MESSAGE.equals(aQName)) {
            mCurrentElement = TAG_MESSAGE;
        } else if (TAG_THROWABLE.equals(aQName)) {
            mCurrentElement = TAG_THROWABLE;
        }
    }

    /** @return the number of events in the document **/
    int getNumEvents() {
        return mNumEvents;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////

    /** Add an event to the model **/
    private void addEvent() {
        mModel.addEvent(new EventDetails(mTimeStamp,
                                         mPriority,
                                         mCategoryName,
                                         mNDC,
                                         mThreadName,
                                         mMessage,
                                         mThrowableStrRep,
                                         mLocationDetails));
        mNumEvents++;
    }

    /** Reset the data for an event **/
    private void resetData() {
        mTimeStamp = 0;
        mPriority = null;
        mCategoryName = null;
        mNDC = null;
        mThreadName = null;
        mMessage = null;
        mThrowableStrRep = null;
        mLocationDetails = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4873.java