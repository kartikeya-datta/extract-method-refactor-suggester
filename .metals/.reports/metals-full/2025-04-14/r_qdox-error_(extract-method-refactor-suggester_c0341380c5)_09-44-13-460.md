error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2249.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2249.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2249.java
text:
```scala
i@@f (((ColumbaHeader) child.getHeader()).getFlags().getSeen()==false) {

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
package org.columba.mail.gui.table.model;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.columba.mail.message.ColumbaHeader;

/**
 * Threaded model using Message-Id:, In-Reply-To: and References: headers to
 * create a tree structure of discussions.
 * <p>
 * The algorithm works this way:
 * <ul>
 * <li>save every header in hashmap, use Message-Id: as key, MessageNode as
 * value</li>
 * <li>for each header, check if In-Reply-To:, or References: points to a
 * matching Message-Id:. If match was found, add header as child</li>
 * </ul>
 * <p>
 * 
 * @author fdietz
 */
public class TableModelThreadedView extends TreeTableModelDecorator {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger
            .getLogger("org.columba.mail.gui.table.model");

    private boolean enabled;

    private HashMap hashtable;

    private int idCount = 0;

    private Collator collator;

    public TableModelThreadedView(TreeTableModelInterface tableModel) {
        super(tableModel);

        enabled = false;

        collator = Collator.getInstance();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        enabled = b;
    }

    /**
     * Parse References: header value and save every found Message-Id: in array.
     * <p>
     * TODO: cleanup tokenizer, this could be much faster using regexp
     * 
     * @param references
     *            References: header value
     * 
     * @return array containing Message-Id: header values
     */
    public static String[] parseReferences(String references) {

        StringTokenizer tk = new StringTokenizer(references, ">");
        String[] list = new String[tk.countTokens()];
        int i = 0;

        while (tk.hasMoreTokens()) {
            String str = (String) tk.nextToken();
            str = str.trim();
            str = str + ">";
            list[i++] = str;

        }

        return list;
    }
    
    protected boolean add(MessageNode node, MessageNode rootNode) {
        ColumbaHeader header = node.getHeader();
        String references = (String) header.get("References");
        String inReply = (String) header.get("In-Reply-To");

        if (inReply != null) {
            inReply = inReply.trim();

            if (hashtable.containsKey(inReply)) {

                MessageNode parent = (MessageNode) hashtable.get(inReply);
                parent.add(node);

                return true;
            }
        } else if (references != null) {
            references = references.trim();

            String[] referenceList = parseReferences(references);
            int count = referenceList.length;

            if (count >= 1) {
                // the last element is the direct parent
                MessageNode parent = (MessageNode) hashtable
                        .get(referenceList[referenceList.length - 1].trim());
                if (parent != null) {
                    parent.add(node);
                    return true;
                }
            }
        }

        return false;
    }

    // create tree structure
    protected void thread(MessageNode rootNode) {
        idCount = 0;

        if (rootNode == null) { return; }

        // save every MessageNode in hashmap for later reference
        createHashmap(rootNode);

        // for each element in the message-header-reference or in-reply-to
        // headerfield: - find a container whose message-id matches and add
        // message, otherwise create empty container        
        Iterator it = hashtable.keySet().iterator();
        while (it.hasNext()) {
        	String key = (String) it.next();
        	
        	MessageNode node = (MessageNode) hashtable.get(key);
        	add(node, rootNode);
        }

        // go through whole tree and sort the siblings after date
        sort(rootNode);
    }
    
    private String getMessageID(MessageNode node) {
    	ColumbaHeader header = node.getHeader();

        String id = (String) header.get("Message-ID");

        if (id == null) {
            id = (String) header.get("Message-Id");
        }

        // if no Message-Id: available create bogus
        if (id == null) {
            id = new String("<bogus-id:" + (idCount++) + ">");
            header.set("Message-ID", id);
        }

        id = id.trim();

        return id;
    }

    /**
     * Save every MessageNode in HashMap for later reference.
     * <p>
     * Message-Id: is key, MessageNode is value
     * 
     * @param rootNode
     *            root node
     */
    private void createHashmap(MessageNode rootNode) {
        hashtable = new HashMap(rootNode.getChildCount());

        // save every message-id in hashtable for later reference
        for (Enumeration enum = rootNode.children(); enum.hasMoreElements();) {
            MessageNode node = (MessageNode) enum.nextElement();
            String id = getMessageID(node);

           
            hashtable.put(id, node);

        }
    }

    /**
     * 
     * sort all children after date
     * 
     * @param node
     *            root MessageNode
     */
    protected void sort(MessageNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            MessageNode child = (MessageNode) node.getChildAt(i);

            //if ( ( child.isLeaf() == false ) && ( !child.getParent().equals(
            // node ) ) )
            if (!child.isLeaf()) {
                // has children
                List v = child.getVector();
                Collections.sort(v, new MessageHeaderComparator(getRealModel()
                        .getColumnNumber("Date"), true));

                // check if there are messages marked as recent
                //  -> in case underline parent node
                boolean contains = containsRecentChildren(child);
                child.setHasRecentChildren(contains);
            }
        }
    }

    protected boolean containsRecentChildren(MessageNode parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            MessageNode child = (MessageNode) parent.getChildAt(i);

            if (((ColumbaHeader) child.getHeader()).getFlags().getRecent()) {
                // recent found
                LOG.info("found recent message");

                return true;
            } else {
                containsRecentChildren(child);
            }
        }

        return false;
    }

    /**
     * implements TableModelModifier
     *  
     */

    /*
     * (non-Javadoc)
     * 
     * @see org.columba.mail.gui.table.model.TableModelModifier#update()
     */
    public void update() {
        super.update();

        if (isEnabled()) {
            thread(getRootNode());
        }
    }

    class MessageHeaderComparator implements Comparator {

        protected int column;

        protected boolean ascending;

        public MessageHeaderComparator(int sortCol, boolean sortAsc) {
            column = sortCol;
            ascending = sortAsc;
        }

        public int compare(Object o1, Object o2) {

            MessageNode node1 = (MessageNode) o1;
            MessageNode node2 = (MessageNode) o2;

            ColumbaHeader header1 = node1.getHeader();
            ColumbaHeader header2 = node2.getHeader();

            if ((header1 == null) || (header2 == null)) { return 0; }

            int result = 0;

            String columnName = getRealModel().getColumnName(column);

            if (columnName.equals("Date")) {
                Date d1 = (Date) header1.get("columba.date");
                Date d2 = (Date) header2.get("columba.date");

                if ((d1 == null) || (d2 == null)) {
                    result = 0;
                } else {
                    result = d1.compareTo(d2);
                }
            } else {
                Object item1 = header1.get(columnName);
                Object item2 = header2.get(columnName);

                if ((item1 != null) && (item2 == null)) {
                    result = 1;
                } else if ((item1 == null) && (item2 != null)) {
                    result = -1;
                } else if ((item1 == null) && (item2 == null)) {
                    result = 0;
                } else if (item1 instanceof String) {
                    result = collator.compare((String) item1, (String) item2);
                }
            }

            if (!ascending) {
                result = -result;
            }

            return result;
        }

        public boolean equals(Object obj) {
            if (obj instanceof MessageHeaderComparator) {
                MessageHeaderComparator compObj = (MessageHeaderComparator) obj;

                return (compObj.column == column)
                        && (compObj.ascending == ascending);
            }

            return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2249.java