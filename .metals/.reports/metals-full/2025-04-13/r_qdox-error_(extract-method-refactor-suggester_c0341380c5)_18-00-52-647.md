error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2059.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2059.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2059.java
text:
```scala
C@@olumbaLogger.log.info("to-headerfield:" + s);

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
package org.columba.mail.gui.composer;

import org.columba.addressbook.folder.HeaderItem;
import org.columba.addressbook.parser.AddressParser;
import org.columba.addressbook.parser.ListParser;

import org.columba.core.logging.ColumbaLogger;

import org.columba.mail.config.AccountItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.message.ColumbaMessage;

import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.StreamableMimePart;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;


/**
 * @author frd
 *
 * Model for message composer dialog
 *
 */
public class ComposerModel {
    ColumbaMessage message;
    AccountItem accountItem;
    String bodytext;
    String charsetName;
    List attachments;
    List toList;
    List ccList;
    List bccList;
    boolean signMessage;
    boolean encryptMessage;

    /**
     * Flag indicating whether this model holds a html
     * message (true) or plain text (false)
     */
    private boolean isHtmlMessage;

    /**
     * Create a new model with an empty plain text message
     * (default behaviour)
     */
    public ComposerModel() {
        this(null, false); // default ~ plain text
    }

    /**
     * Creates a new model with a plain text message
     * @param message        Initial message to hold in the model
     */
    public ComposerModel(ColumbaMessage message) {
        this(message, false);
    }

    /**
     * Creates a new model with an empty message
     *
     * @param html        True for a html message, false for plain text
     */
    public ComposerModel(boolean html) {
        this(null, html);
    }

    /**
     * Creates a new model
     *
     * @param message        Initial message to hold in the model
     * @param html                True for a html message, false for plain text
     */
    public ComposerModel(ColumbaMessage message, boolean html) {
        // set message
        if (message == null) {
            this.message = new ColumbaMessage();
        } else {
            this.message = message;
        }

        // set whether the model should handle html or plain text
        isHtmlMessage = html;

        // more initialization		
        toList = new Vector();
        ccList = new Vector();
        bccList = new Vector();
        attachments = new Vector();
        charsetName = "auto";
    }

    public void setTo(String s) {
        ColumbaLogger.log.debug("to-headerfield:" + s);

        if (s == null) {
            return;
        }

        if (s.length() == 0) {
            return;
        }

        /*
        int index = s.indexOf(",");
        if (index != -1) {
                String to = s;
                Vector v = ListParser.parseString(to);

                for (int i = 0; i < v.size(); i++) {
                        System.out.println("model add:" + v.get(i));
                        HeaderItem item = new HeaderItem(HeaderItem.CONTACT);
                        item.add("displayname", (String) v.get(i));
                        item.add("field", "To");
                        getToList().add(item);
                }
        } else {
                HeaderItem item = new HeaderItem(HeaderItem.CONTACT);
                item.add("displayname", s);
                item.add("field", "To");
                getToList().add(item);
        }
        */
        List v = ListParser.parseString(s);

        for (Iterator it = v.iterator(); it.hasNext();) {
            String str = (String) it.next();

            //		for (int i = 0; i < v.size(); i++) {
            //			String str = (String) v.get(i);
            HeaderItem item = new HeaderItem(HeaderItem.CONTACT);
            item.add("displayname", AddressParser.getDisplayname(str));
            item.add("email;internet", AddressParser.getAddress(str));
            item.add("field", "To");

            getToList().add(item);
        }
    }

    public void setHeaderField(String key, String value) {
        message.getHeader().set(key, value);
    }

    public void setHeader(Header header) {
        message.setHeader(header);
    }

    public String getHeaderField(String key) {
        return (String) message.getHeader().get(key);
    }

    public void setToList(List v) {
        this.toList = v;
    }

    public void setCcList(List v) {
        this.ccList = v;
    }

    public void setBccList(List v) {
        this.bccList = v;
    }

    public List getToList() {
        return toList;
    }

    public List getCcList() {
        return ccList;
    }

    public List getBccList() {
        return bccList;
    }

    public void setAccountItem(AccountItem item) {
        this.accountItem = item;
    }

    public AccountItem getAccountItem() {
        if (accountItem == null) {
            return MailConfig.getAccountList().get(0);
        } else {
            return accountItem;
        }
    }

    public void setMessage(ColumbaMessage message) {
        this.message = message;
    }

    public ColumbaMessage getMessage() {
        return message;
    }

    public String getHeader(String key) {
        return (String) message.getHeader().get(key);
    }

    public void addMimePart(StreamableMimePart mp) {
        attachments.add(mp);

        //notifyListeners();
    }

    public void setBodyText(String str) {
        this.bodytext = str;

        //notifyListeners();
    }

    public String getSignature() {
        return "signature";
    }

    public String getBodyText() {
        return bodytext;
    }

    public String getSubject() {
        return (String) message.getHeader().get("Subject");
    }

    public void setSubject(String s) {
        message.getHeader().set("Subject", s);
    }

    public List getAttachments() {
        return attachments;
    }

    public void setAccountItem(String host, String address) {
        setAccountItem(MailConfig.getAccountList().hostGetAccount(host, address));
    }

    /**
     * Returns the charsetName.
     * @return String
     */
    public String getCharsetName() {
        if (charsetName.equals("auto")) {
            charsetName = System.getProperty("file.encoding");
        }

        return charsetName;
    }

    /**
     * Sets the charsetName.
     * @param charsetName The charsetName to set
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * Returns the signMessage.
     * @return boolean
     */
    public boolean isSignMessage() {
        return signMessage;
    }

    /**
     * Sets the signMessage.
     * @param signMessage The signMessage to set
     */
    public void setSignMessage(boolean signMessage) {
        this.signMessage = signMessage;
    }

    /**
     * Returns the encryptMessage.
     * @return boolean
     */
    public boolean isEncryptMessage() {
        return encryptMessage;
    }

    /**
     * Sets the encryptMessage.
     * @param encryptMessage The encryptMessage to set
     */
    public void setEncryptMessage(boolean encryptMessage) {
        this.encryptMessage = encryptMessage;
    }

    public String getPriority() {
        if (message.getHeader().get("X-Priority") == null) {
            return "Normal";
        } else {
            return (String) message.getHeader().get("X-Priority");
        }
    }

    public void setPriority(String s) {
        message.getHeader().set("X-Priority", s);
    }

    /**
     * Returns whether the model holds a html message or plain text
     * @return        True for html, false for text
     */
    public boolean isHtml() {
        return isHtmlMessage;
    }

    /**
     * Sets whether the model holds a html message or plain text
     * @param        html        True for html, false for text
     */
    public void setHtml(boolean html) {
        isHtmlMessage = html;
    }

    /*
    public FrameMediator createInstance(String id) {
            return new ComposerController(id, this);
    }
    */
    public List getRCPTVector() {
        List output = new Vector();

        List v = ListParser.parseVector(getToList());
        output.addAll(AddressParser.normalizeRCPTVector(v));
        v = ListParser.parseVector(getCcList());
        output.addAll(AddressParser.normalizeRCPTVector(v));
        v = ListParser.parseVector(getBccList());
        output.addAll(AddressParser.normalizeRCPTVector(v));

        return output;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2059.java