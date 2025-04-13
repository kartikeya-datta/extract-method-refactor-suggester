error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2740.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2740.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2740.java
text:
```scala
public L@@ocalHeaderCache(LocalFolder folder) {

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
package org.columba.mail.folder.headercache;

import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.core.util.ListTools;

import org.columba.mail.folder.DataStorageInterface;
import org.columba.mail.folder.LocalFolder;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.HeaderList;
import org.columba.mail.util.MailResourceLoader;

import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.MessageFolderInfo;
import org.columba.ristretto.message.io.Source;
import org.columba.ristretto.parser.HeaderParser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;


/**
 * Implementation of a local headercache facility, which is also able to resync
 * itself with the {@DataStorageInterface}.
 *
 * @author fdietz
 */
public class LocalHeaderCache extends AbstractFolderHeaderCache {
    private final int WEEK = 1000 * 60 * 60 * 24 * 7;
    private boolean configurationChanged;

    public LocalHeaderCache(CachedFolder folder) {
        super(folder);

        configurationChanged = false;
    }

    public HeaderList getHeaderList() throws Exception {
        boolean needToRelease = false;

        // if there exists a ".header" cache-file
        //  try to load the cache
        if (!isHeaderCacheLoaded()) {
            if (headerFile.exists()) {
                try {
                    load();

                    if (needToSync(headerList.count())) {
                        sync();
                    }
                } catch (Exception e) {
                    sync();
                }
            } else {
                sync();
            }

            setHeaderCacheLoaded(true);
        }

        return headerList;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.columba.mail.folder.headercache.AbstractHeaderCache#needToSync(int)
     */
    public boolean needToSync(int capacity) {
        int mcount = ((LocalFolder) folder).getDataStorageInstance()
                      .getMessageCount();

        if (capacity != mcount) {
            return true;
        }

        return false;
    }

    /**
     * @param worker
     * @throws Exception
     */
    public void load() throws Exception {
        ColumbaLogger.log.fine("loading header-cache=" + headerFile);

        try {
            reader = new ObjectReader(headerFile);
        } catch (Exception e) {
            if (MainInterface.DEBUG) {
                e.printStackTrace();
            }
        }

        int capacity = ((Integer) reader.readObject()).intValue();
        ColumbaLogger.log.fine("capacity=" + capacity);

        boolean needToRelease = false;

        int additionalHeaderfieldsCount = ((Integer) reader.readObject()).intValue();

        if (additionalHeaderfieldsCount != 0) {
            // user-defined headerfields found
            // -> read all keys from file
            for (int i = 0; i < additionalHeaderfieldsCount; i++) {
                additionalHeaderfields.add((String) reader.readObject());
            }
        }

        String[] userDefinedHeaders = CachedHeaderfields.getUserDefinedHeaderfields();

        if ((userDefinedHeaders != null) &&
                (userDefinedHeaders.length >= additionalHeaderfieldsCount)) {
            configurationChanged = true;
        }

        headerList = new HeaderList(capacity);

        //System.out.println("Number of Messages : " + capacity);
        if (getObservable() != null) {
            getObservable().setMessage(folder.getName() + ": " +
                MailResourceLoader.getString("statusbar", "message",
                    "load_headers"));
            getObservable().setMax(capacity);
            getObservable().resetCurrent(); // setCurrent(0)
        }

        int nextUid = -1;

        // exists/unread/recent should be set to 0
        folder.setMessageFolderInfo(new MessageFolderInfo());

        for (int i = 0; i < capacity; i++) {
            if ((getObservable() != null) && ((i % 100) == 0)) {
                getObservable().setCurrent(i);
            }

            ColumbaHeader h = createHeaderInstance();

            loadHeader(h);

            headerList.add(h, (Integer) h.get("columba.uid"));

            if (h.getFlags().getRecent()) {
                folder.getMessageFolderInfo().incRecent();
            }

            if (h.getFlags().getSeen() == false) {
                folder.getMessageFolderInfo().incUnseen();
            }

            folder.getMessageFolderInfo().incExists();

            int aktUid = ((Integer) h.get("columba.uid")).intValue();

            if (nextUid < aktUid) {
                nextUid = aktUid;
            }
        }

        /*
        // Check if the count of the
        if (needToSync(capacity)) {
            ColumbaLogger.log.fine(
            "need to recreateHeaderList() because capacity is not matching");

            throw new FolderInconsistentException();
        }
        */
        nextUid++;
        ColumbaLogger.log.info("next UID for new messages =" + nextUid);
        ((LocalFolder) folder).setNextMessageUid(nextUid);

        reader.close();

        if (configurationChanged) {
            // headerfield cache configuration changed
            // -> try to properly fill the cache again
            reorganizeCache();
        }

        // we are done
        if (getObservable() != null) {
            getObservable().clearMessageWithDelay();
            getObservable().resetCurrent();
        }
    }

    /**
     * @param worker
     * @throws Exception
     */
    public void save() throws Exception {
        // we didn't load any header to save
        if (!isHeaderCacheLoaded()) {
            return;
        }

        ColumbaLogger.log.fine("saving header-cache=" + headerFile);

        // this has to called only if the uid becomes higher than Integer
        // allows
        //cleanUpIndex();
        try {
            writer = new ObjectWriter(headerFile);
        } catch (Exception e) {
            if (MainInterface.DEBUG) {
                e.printStackTrace();
            }
        }

        // write total number of headers to file
        int count = headerList.count();
        ColumbaLogger.log.fine("capacity=" + count);
        writer.writeObject(new Integer(count));

        // write keys of user specified headerfields in file
        // -> this allows a much more failsafe handling, when
        // -> users add/remove headerfields from the cache
        String[] userDefinedHeaderFields = CachedHeaderfields.getUserDefinedHeaderfields();

        if (userDefinedHeaderFields != null) {
            // write number of additional headerfields to file
            writer.writeObject(new Integer(userDefinedHeaderFields.length));

            // write keys to file
            for (int i = 0; i < userDefinedHeaderFields.length; i++) {
                writer.writeObject(userDefinedHeaderFields[i]);
            }
        } else {
            // no additionally headerfields
            writer.writeObject(new Integer(0));
        }

        ColumbaHeader h;

        //Message message;
        for (Enumeration e = headerList.keys(); e.hasMoreElements();) {
            Object uid = e.nextElement();

            h = (ColumbaHeader) headerList.get(uid);

            saveHeader(h);
        }

        writer.close();
    }

    /**
     * @param worker
     * @throws Exception
     */
    public void sync() throws Exception {
        if (getObservable() != null) {
            getObservable().setMessage(folder.getName() +
                ": Syncing headercache...");
        }

        DataStorageInterface ds = ((LocalFolder) folder).getDataStorageInstance();

        Object[] uids = ds.getMessageUids();

        HeaderList oldHeaderList = headerList;

        headerList = new HeaderList(uids.length);

        Date today = Calendar.getInstance().getTime();

        // parse all message files to recreate the header cache
        ColumbaHeader header = null;
        MessageFolderInfo messageFolderInfo = folder.getMessageFolderInfo();
        messageFolderInfo.setExists(0);
        messageFolderInfo.setRecent(0);
        messageFolderInfo.setUnseen(0);

        folder.setChanged(true);

        if (getObservable() != null) {
            getObservable().setMax(uids.length);
        }

        for (int i = 0; i < uids.length; i++) {
            if ((oldHeaderList != null) && oldHeaderList.containsKey(uids[i])) {
                header = oldHeaderList.get(uids[i]);
                headerList.add(header, uids[i]);
            } else {
                try {
                    Source source = ds.getMessageSource(uids[i]);

                    if (source.length() == 0) {
                        ds.removeMessage(uids[i]);

                        continue;
                    }

                    header = new ColumbaHeader(HeaderParser.parse(source));

                    header = CachedHeaderfields.stripHeaders(header);

                    if (isOlderThanOneWeek(today,
                                ((Date) header.getAttributes().get("columba.date")))) {
                        header.getFlags().set(Flags.SEEN);
                    }

                    int size = source.length() >> 10; // Size in KB
                    header.set("columba.size", new Integer(size));

                    //	set the attachment flag
                    String contentType = (String) header.get("Content-Type");

                    header.set("columba.attachment", header.hasAttachments());

                    header.set("columba.uid", uids[i]);

                    headerList.add(header, uids[i]);

                    source = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ColumbaLogger.log.severe("Error syncing HeaderCache :" +
                        ex.getLocalizedMessage());
                }
            }

            if (header.get("columba.flags.recent").equals(Boolean.TRUE)) {
                messageFolderInfo.incRecent();
            }

            if (header.get("columba.flags.seen").equals(Boolean.FALSE)) {
                messageFolderInfo.incUnseen();
            }

            header = null;

            messageFolderInfo.incExists();

            ((LocalFolder) folder).setNextMessageUid(((Integer) uids[uids.length -
                1]).intValue() + 1);

            if ((getObservable() != null) && ((i % 100) == 0)) {
                getObservable().setCurrent(i);
            }
        }

        // we are done
        if (getObservable() != null) {
            getObservable().resetCurrent();
        }
    }

    protected void loadHeader(ColumbaHeader h) throws Exception {
        h.set("columba.uid", reader.readObject());

        super.loadHeader(h);
    }

    protected void saveHeader(ColumbaHeader h) throws Exception {
        writer.writeObject(h.get("columba.uid"));

        super.saveHeader(h);
    }

    public boolean isOlderThanOneWeek(Date arg0, Date arg1) {
        return (arg0.getTime() - WEEK) > arg1.getTime();
    }

    /**
     * Method tries to fill the headercache with proper values.
     * <p>
     * This is needed after the user changed the headerfield caching setup.
     *
     */
    protected void reorganizeCache() throws Exception {
        List list = new LinkedList(Arrays.asList(
                    CachedHeaderfields.getUserDefinedHeaderfields()));
        ListTools.substract(list, additionalHeaderfields);

        if (list.size() == 0) {
            return;
        }

        JOptionPane.showMessageDialog(null,
            "<html></body><p>Columba recognized that you just changed the headerfield caching setup. This makes it necessary to reorganize the cache and will take a bit longer than generally.</p></body></html>");

        DataStorageInterface ds = ((LocalFolder) folder).getDataStorageInstance();

        Object[] uids = ds.getMessageUids();
        Header helper;
        ColumbaHeader header;

        for (int i = 0; i < uids.length; i++) {
            header = (ColumbaHeader) headerList.get(uids[i]);

            Source source = ds.getMessageSource(uids[i]);

            if (source.length() == 0) {
                continue;
            }

            helper = HeaderParser.parse(source);
            source.close();

            Iterator it = list.iterator();

            while (it.hasNext()) {
                String h = (String) it.next();
                header.set(h, helper.get(h));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2740.java