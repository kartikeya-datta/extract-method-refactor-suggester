error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9545.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9545.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9545.java
text:
```scala
protected S@@tring patternString = "";

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

import org.columba.mail.message.ColumbaHeader;

import org.columba.ristretto.message.Flags;


/**
 * @author fdietz
 *
 * Adds basic filter capabilities to the TableModel
 *
 * Items which can be filtered are:
 *  - unseen flag
 *  - answered flag
 *  - flagged flag
 *  - expunged flag
 *  - has attachment
 *  - String in Subject or Sender
 *
 */
public class BasicTableModelFilter extends TreeTableModelDecorator {
    protected boolean newFlag = false;

    //protected boolean oldFlag = true;
    protected boolean answeredFlag = false;
    protected boolean flaggedFlag = false;
    protected boolean expungedFlag = false;
    protected boolean attachmentFlag = false;

    //protected String patternItem = new String("subject");
    protected String patternString = new String();
    protected boolean dataFiltering = false;

    public BasicTableModelFilter(TreeTableModelInterface tableModel) {
        super(tableModel);
    }

    /************** filter view *********************/
    public void setDataFiltering(boolean b) throws Exception {
        dataFiltering = b;
    }

    public boolean isEnabled() {
        return dataFiltering;
    }

    public void setNewFlag(boolean b) throws Exception {
        newFlag = b;
    }

    public boolean getNewFlag() {
        return newFlag;
    }

    public void setAnsweredFlag(boolean b) throws Exception {
        answeredFlag = b;
    }

    public boolean getAnsweredFlag() {
        return answeredFlag;
    }

    public void setFlaggedFlag(boolean b) throws Exception {
        flaggedFlag = b;
    }

    public boolean getFlaggedFlag() {
        return flaggedFlag;
    }

    public void setExpungedFlag(boolean b) throws Exception {
        expungedFlag = b;
    }

    public boolean getExpungedFlag() {
        return expungedFlag;
    }

    public void setAttachmentFlag(boolean b) throws Exception {
        attachmentFlag = b;
    }

    public boolean getAttachmentFlag() {
        return attachmentFlag;
    }

    public void setPatternString(String s) throws Exception {
        patternString = s;
    }

    public String getPatternString() {
        return patternString;
    }

    protected boolean testString(ColumbaHeader header) {
        String subject = (String) header.get("Subject");

        if (subject != null) {
            String pattern = getPatternString().toLowerCase();

            if (subject.toLowerCase().indexOf(pattern.toLowerCase()) != -1) {
                return true;
            }
        }

        String from = (String) header.get("From");

        if (from != null) {
            String pattern = getPatternString().toLowerCase();

            if (from.toLowerCase().indexOf(pattern.toLowerCase()) != -1) {
                return true;
            }
        }

        return false;
    }

    public boolean addItem(ColumbaHeader header) {
        boolean result = true;
        boolean result2 = false;

        //boolean result3 = true;
        boolean flags1 = false;
        boolean flags2 = false;

        Flags flags = ((ColumbaHeader) header).getFlags();

        if (flags == null) {
            System.out.println("flags is null");

            return false;
        }

        if (getNewFlag()) {
            if (flags.getSeen()) {
                result = false;
            }
        }

        if (getAnsweredFlag()) {
            if (!flags.getAnswered()) {
                result = false;
            }
        }

        if (getFlaggedFlag()) {
            if (!flags.getFlagged()) {
                result = false;
            }
        }

        if (getExpungedFlag()) {
            if (!flags.getExpunged()) {
                result = false;
            }
        }

        if (getAttachmentFlag()) {
            Boolean attach = (Boolean) header.get("columba.attachment");
            boolean attachment = attach.booleanValue();

            if (!attachment) {
                result = false;
            }
        }

        if (!(getPatternString().equals(""))) {
            flags2 = true;
            result2 = testString(header);
        } else {
            result2 = true;
        }

        if (result2) {
            if (result) {
                return true;
            }
        }

        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9545.java