error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3113.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3113.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3113.java
text:
```scala
a@@ttributes.put("columba.host", "");

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
package org.columba.mail.message;

import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.Attributes;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.parser.HeaderParser;

import java.awt.Color;


/**
 * Represents a RFC822-compliant header
 * <p>
 * Every headerfield is saved in {@HeaderList}.
 * <p>
 * Generally every headerfield is a string,
 * but for optimization reasons some items
 * are going to change to for example a Date class
 * <p>
 * These items are saved in {@link Attributes} to separate
 * them clearly from general RFC822 headerfields.
 *
 * <p>
 * @see CachedHeaderfields
 *
 * @author tstich, fdietz
 */
public class ColumbaHeader {
    protected Header header;
    protected Attributes attributes;
    protected Flags flags;

    public ColumbaHeader(ColumbaHeader header) {
        this.header = header.getHeader();
        this.attributes = header.getAttributes();
        this.flags = header.getFlags();
    }

    public ColumbaHeader() {
        this(new Header());
    }

    public ColumbaHeader(Header header) {
        this.header = header;
        flags = new Flags();
        attributes = new Attributes();

        BasicHeader basicHeader = new BasicHeader(header);

        attributes.put("columba.alreadyfetched", Boolean.FALSE);
        attributes.put("columba.spam", Boolean.FALSE);

        attributes.put("columba.priority",
            new Integer(basicHeader.getPriority()));

        Address from = basicHeader.getFrom();

        if (from != null) {
            attributes.put("columba.from", from);
        } else {
            attributes.put("columba.from", new Address(""));
        }

        attributes.put("columba.host", new String());
        attributes.put("columba.date", basicHeader.getDate());

        String subject = basicHeader.getSubject();

        if (subject != null) {
            attributes.put("columba.subject", subject);
        } else {
            attributes.put("columba.subject", "");
        }

        attributes.put("columba.attachment", Boolean.FALSE);
        attributes.put("columba.size", new Integer(0));

        // message colour should be black as default
        attributes.put("columba.color", new Color(0, 0, 0));

        // use default account 
        attributes.put("columba.accountuid", new Integer(0));
    }

    public Object clone() {
        ColumbaHeader clone = new ColumbaHeader();
        clone.attributes = (Attributes) this.attributes.clone();
        clone.flags = (Flags) this.flags.clone();
        clone.header = (Header) this.header.clone();

        return clone;
    }

    public void copyColumbaKeys(ColumbaHeader header) {
        header.flags = (Flags) flags.clone();
        header.attributes = (Attributes) attributes.clone();
    }

    /* (non-Javadoc)
     * @see org.columba.mail.message.HeaderInterface#count()
     */
    public int count() {
        return attributes.count() + header.count() + 5;
    }

    /* (non-Javadoc)
     * @see org.columba.mail.message.HeaderInterface#getFlags()
     */
    public Flags getFlags() {
        return flags;
    }

    /**
     * Note: Don't use this method anymore when accessing
     * attributes like "columba.size", use getAttribute() instead
     *
     */
    public Object get(String s) {
        if (s.startsWith("columba.flags.")) {
            String flag = s.substring("columba.flags.".length());

            if (flag.equals("seen")) {
                return Boolean.valueOf(flags.get(Flags.SEEN));
            } else if (flag.equals("recent")) {
                return Boolean.valueOf(flags.get(Flags.RECENT));
            } else if (flag.equals("answered")) {
                return Boolean.valueOf(flags.get(Flags.ANSWERED));
            } else if (flag.equals("draft")) {
                return Boolean.valueOf(flags.get(Flags.DRAFT));
            } else if (flag.equals("flagged")) {
                return Boolean.valueOf(flags.get(Flags.FLAGGED));
            } else if (flag.equals("expunged")) {
                return Boolean.valueOf(flags.get(Flags.EXPUNGED));
            }
        }

        if (s.startsWith("columba.")) {
            return attributes.get(s);
        }

        return header.get(HeaderParser.normalizeKey(s));
    }

    /* (non-Javadoc)
     * @see org.columba.mail.message.HeaderInterface#set(java.lang.String, java.lang.Object)
     */
    public void set(String s, Object o) {
        if (s.startsWith("columba.flags")) {
            String flag = s.substring("columba.flags.".length());
            boolean value = ((Boolean) o).booleanValue();

            if (flag.equals("seen")) {
                flags.set(Flags.SEEN, value);

                return;
            }

            if (flag.equals("recent")) {
                flags.set(Flags.RECENT, value);

                return;
            }

            if (flag.equals("answered")) {
                flags.set(Flags.ANSWERED, value);

                return;
            }

            if (flag.equals("expunged")) {
                flags.set(Flags.EXPUNGED, value);

                return;
            }

            if (flag.equals("draft")) {
                flags.set(Flags.DRAFT, value);

                return;
            }

            if (flag.equals("flagged")) {
                flags.set(Flags.FLAGGED, value);
            }
        }

        if (s.startsWith("columba.")) {
            attributes.put(s, o);
        } else {
            header.set(HeaderParser.normalizeKey(s), (String) o);
        }
    }

    /**
     * @return
     */
    public Header getHeader() {
        return header;
    }

    /**
     * @return
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * @param flags
     */
    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    /**
     * @param header
     */
    public void setHeader(Header header) {
        this.header = header;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3113.java