error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1074.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1074.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1074.java
text:
```scala
e@@.notifyObservers();

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

package org.columba.mail.config;

import java.io.File;
import java.util.Observer;

import org.columba.core.xml.XmlElement;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.parser.ParserException;

/**
 * Encapsulates an identity used with an account.
 */
public class Identity {
	
    private static final String SIGNATURE_FILE = "signature_file";
	private static final String ORGANISATION = "organisation";
	private static final String REPLY_ADDRESS = "reply_address";
	private static final String NAME = "name";
	private static final String ADDRESS = "address";
	
	protected XmlElement e;
    protected Address address;
    protected Address replyToAddress;
    
    public Identity(XmlElement e) throws ParserException {
        this.e = e;
        String value = e.getAttribute(Identity.ADDRESS);
        if (value != null && value.length() > 0) {
            address = Address.parse(e.getAttribute(Identity.ADDRESS));
            address.setDisplayName(e.getAttribute(Identity.NAME));
        }
        value = e.getAttribute(Identity.REPLY_ADDRESS);
        if (value != null && value.length() > 0) {
            replyToAddress = Address.parse(value);
        }
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
        e.addAttribute(Identity.NAME, address.getDisplayName());
        e.addAttribute(Identity.ADDRESS, address.getMailAddress());
    }
    
    public Address getReplyToAddress() {
        return replyToAddress;
    }
    
    public void setReplyToAddress(Address address) {
        replyToAddress = address;
        if (address != null) {
            e.addAttribute(Identity.REPLY_ADDRESS, address.getMailAddress());
        } else {
            e.getAttributes().remove(Identity.REPLY_ADDRESS);
        }
    }
    
    public String getOrganisation() {
        return e.getAttribute(Identity.ORGANISATION);
    }
    
    public void setOrganisation(String organisation) {
        if (organisation != null) {
            e.addAttribute(Identity.ORGANISATION, organisation);
        } else {
            e.getAttributes().remove(Identity.ORGANISATION);
        }
    }
    
    /**
     * Returns the signature that should be attached to outgoing mails.
     */
    public File getSignature() {
        String path = e.getAttribute(Identity.SIGNATURE_FILE);
        if (path != null) {
            File signature = new File(path);
            if (signature.exists() && signature.isFile()) {
                return signature;
            }
        }
        return null;
    }
    
    /**
     * Sets the signature to be attached to outgoing mails.
     */
    public void setSignature(File signature) {
        if (signature != null && signature.exists() && signature.isFile()) {
            e.addAttribute(Identity.SIGNATURE_FILE, signature.getPath());
        } else {
            e.getAttributes().remove(Identity.SIGNATURE_FILE);
        }
        
        e.notifyObservers(signature);
    }
    
    public void addObserver(Observer observer) {
    	e.addObserver(observer);
    }

	public void removeObserver(Observer observer) {
		e.deleteObserver(observer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1074.java