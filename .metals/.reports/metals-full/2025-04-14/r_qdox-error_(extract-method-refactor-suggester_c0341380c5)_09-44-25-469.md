error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/449.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/449.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/449.java
text:
```scala
r@@eturn new String[] {};

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

import org.columba.core.xml.XmlElement;

import org.columba.mail.config.MailConfig;
import org.columba.mail.message.ColumbaHeader;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 *
 *
 * Holds a collection of all cached headerfields, which Columba needs to be
 * able to quickly show the message summary, etc. to the user.
 *
 * @author fdietz
 */
public class CachedHeaderfields {
    protected static XmlElement headercache;

    // internally used headerfields
    // these are all boolean values, which are saved using
    // a single int value
    public static String[] INTERNAL_COMPRESSED_HEADERFIELDS = {
        
        // message flags
        "columba.flags.seen", "columba.flags.recent", "columba.flags.answered",
        "columba.flags.flagged", "columba.flags.expunged", "columba.flags.draft",
        

        //	true, if message has attachments, false otherwise
        "columba.attachment", 
        //	true/false
        "columba.spam"
    };

    // this internally used headerfields can be of every basic
    // type, including String, Integer, Boolean, Date, etc.
    public static String[] INTERNAL_HEADERFIELDS = {
        
        // priority as integer value
        "columba.priority",
        

        // short from, containing only name of person
        "columba.from",
        

        // host from which this message was downloaded
        "columba.host",
        

        // date
        "columba.date",
        

        // size of message
        "columba.size",
        

        // properly decoded subject
        "columba.subject",
        

        // message color
        "columba.color",
        

        // account ID
        "account.uid"
    };

    // these are cached by default
    // -> options.xml: /options/headercache
    // -> attribute: additional
    // -> whitespace separated list of additionally
    // -> to be cached headerfields
    // -----> only for power-users who want to tweak their search speed
    public static String[] DEFAULT_HEADERFIELDS = {
        "Subject", "From", "To", "Cc", "Date", "Message-Id", "In-Reply-To",
        "References", "Content-Type"
    };
    public static String[] POP3_HEADERFIELDS = {
        "Subject", "From", "columba.date", "columba.size",
        

        // POP3 message UID
        "columba.pop3uid",
        

        // was this message already fetched from the server?
        "columba.alreadyfetched"
    };

    public CachedHeaderfields() {
        //		see if we have to cache additional headerfields
        // which are added by the user
        XmlElement options = MailConfig.get("options").getElement("/options");
        headercache = options.getElement("headercache");

        if (headercache == null) {
            // create xml-node
            headercache = new XmlElement("headercache");
            options.addElement(headercache);
        }
    }

    /**
     *
     * create new header which only contains headerfields needed by Columba
     * (meaning they also get cached)
     *
     * @param h
     * @return
     */
    public static ColumbaHeader stripHeaders(ColumbaHeader h) {
        //return h;
        ColumbaHeader strippedHeader = new ColumbaHeader();

        //		copy all internally used headerfields
        for (int i = 0; i < DEFAULT_HEADERFIELDS.length; i++) {
            strippedHeader.set(DEFAULT_HEADERFIELDS[i],
                h.get(DEFAULT_HEADERFIELDS[i]));
        }

        for (int i = 0; i < INTERNAL_HEADERFIELDS.length; i++) {
            strippedHeader.set(INTERNAL_HEADERFIELDS[i],
                h.get(INTERNAL_HEADERFIELDS[i]));
        }

        for (int i = 0; i < INTERNAL_COMPRESSED_HEADERFIELDS.length; i++) {
            strippedHeader.set(INTERNAL_COMPRESSED_HEADERFIELDS[i],
                h.get(INTERNAL_COMPRESSED_HEADERFIELDS[i]));
        }

        // copy all user defined headerfields
        String[] userList = (String[]) getUserDefinedHeaderfieldArray();

        if (userList != null) {
            for (int i = 0; i < userList.length; i++) {
                String s = (String) userList[i];
                Object item = h.get(s);

                strippedHeader.set(s, item);
            }
        }

        return strippedHeader;
    }

    /**
     * @return array containing all user defined headerfields
     */
    public static String[] getUserDefinedHeaderfieldArray() {
        String additionalHeaderfields = headercache.getAttribute("headerfields");

        if ((additionalHeaderfields != null) &&
                (additionalHeaderfields.length() > 0)) {
            List list = new Vector();
            StringTokenizer tok = new StringTokenizer(additionalHeaderfields,
                    " ");

            while (tok.hasMoreTokens()) {
                String s = (String) tok.nextToken();
                list.add(s);
            }

            String[] stringList = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                stringList[i] = (String) list.get(i);
            }

            return stringList;
        }

        return null;
    }

    /**
     * @return array containing default + user-defined headerfields
     */
    public static String[] getCachedHeaderfieldArray() {
        List list = new Vector(Arrays.asList(DEFAULT_HEADERFIELDS));

        String[] userList = getUserDefinedHeaderfieldArray();

        if (userList != null) {
            list.addAll(Arrays.asList(userList));
        }

        String[] stringList = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            stringList[i] = (String) list.get(i);
        }

        return stringList;
    }

    public static String[] getDefaultHeaderfields() {
        return DEFAULT_HEADERFIELDS;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/449.java