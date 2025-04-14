error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6859.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6859.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6859.java
text:
```scala
s@@et(VCARD.N_ADDITIONALNAMES,names[1]);

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
package org.columba.addressbook.model;

import org.jdom.CDATA;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Wraps a xml tree offering convenience methods to get and set information from
 * elements and child elements easily.
 * <p>
 * Note, that missing xml elements are created automatically.
 * <p>
 * Use the static tags defined in {@link VCARD}to change the contact data.
 * <p>
 * The top-level xml tag is "vcard".
 * 
 * @see VCARD
 * @author fdietz
 */
public class Contact {

	private Document doc;

	private Element root;

	private Object uid;

	/**
	 * Default constructor
	 * 
	 * @param contact
	 *            top-level xml node
	 */
	public Contact(Document doc, Object uid) throws WrongFileFormatException{
		this.doc = doc;
		this.uid = uid;
		this.root = doc.getRootElement();

		if (!root.getName().equals("vcard")) {
			// wrong xml-format
			throw new WrongFileFormatException(
					"Root element should be <vcard>!");
		}

	}

	/**
	 * Default constructor
	 */
	public Contact() {

		doc = new Document();
		root = new Element("vcard");
		root.setAttribute("version", "1.0");
		doc.addContent(root);
	}

	/**
	 * Get top-level xml element.
	 * 
	 * @return get top-level xml element
	 */
	public Element getRootElement() {
		return root;
	}

	/**
	 * Set CDATA value in xml element.
	 * 
	 * @param key
	 *            selected xml element
	 * @param value
	 *            CDATA text
	 */
	public void formatSet(String key, String value) {
		Element child = root.getChild(key);
		if (child == null) {
			child = new Element(key);
			root.addContent(child);
		}

		if (child.getContentSize() == 0)
			child.addContent(new CDATA(value));
		else
			child.setContent(0, new CDATA(value));
	}

	/**
	 * Set CDATA value in xml element.
	 * 
	 * @param key
	 *            selected xml element
	 * @param value
	 *            CDATA text
	 */
	public void formatSet(String key, String prefix, String value) {
		Element child = root.getChild(key);
		if (child == null) {
			child = new Element(key);
			root.addContent(child);
		}
		Element prefixchild = child.getChild(prefix);
		if (prefixchild == null) {
			prefixchild = new Element(prefix);
			child.addContent(prefixchild);
		}

		if (prefixchild.getContentSize() == 0)
			prefixchild.addContent(new CDATA(value));
		else
			prefixchild.setContent(0, new CDATA(value));
	}

	/**
	 * Set textual value of xml element.
	 * 
	 * @param key
	 *            selected xml element
	 * @param value
	 *            text value
	 */
	public void set(String key, String value) {
		Element child = root.getChild(key);
		if (child == null) {
			child = new Element(key);
			root.addContent(child);
		}
		child.setText(value);
	}

	/**
	 * Set textual value of xml child element.
	 * 
	 * @param key
	 *            selectd xml element
	 * @param prefix
	 *            child with element name prefix
	 * @param value
	 *            text value
	 */
	public void set(String key, String prefix, String value) {
		Element child = root.getChild(key);
		if (child == null) {
			child = new Element(key);
			root.addContent(child);
		}
		Element prefixchild = child.getChild(prefix);
		if (prefixchild == null) {
			prefixchild = new Element(prefix);
			child.addContent(prefixchild);
		}
		prefixchild.setText(value);
	}

	/**
	 * Get textual value of xml element.
	 * 
	 * @param key
	 *            selected xml element
	 * @return text value
	 */
	public String get(String key) {
		Element child = root.getChild(key);
		if (child == null) {
			child = new Element(key);
			root.addContent(child);
		}
		return child.getTextNormalize();
	}

	/**
	 * Get CDATA value of xml element.
	 * 
	 * @param key
	 *            selected xml element
	 * 
	 * @return CDATA value
	 */
	public String formatGet(String key) {
		Element child = root.getChild(key);
		if (child == null) {
			child = new Element(key);
			root.addContent(child);
		}

		if (child.getContentSize() == 0)
			child.addContent(new CDATA(""));

		Content cdata = child.getContent(0);

		return cdata.getValue();
	}

	/**
	 * Get textual data of xml child element.
	 * 
	 * @param key
	 *            selected xml element
	 * @param prefix
	 *            name of child element
	 * @return text data
	 */
	public String get(String key, String prefix) {
		Element child = root.getChild(key);
		if (child == null) {
			child = new Element(key);
			root.addContent(child);
		}
		Element prefixchild = child.getChild(prefix);
		if (prefixchild == null) {
			prefixchild = new Element(prefix);
			child.addContent(prefixchild);
		}

		return prefixchild.getTextNormalize();
	}

	/**
	 * @return Returns the doc.
	 */
	public Document getDocument() {
		return doc;
	}

	/**
	 * @return Returns the uid.
	 */
	public Object getUid() {
		return uid;
	}
	
	public void fillFullName(String fullName)
	{
			String[] names = tryBreakName(fullName);
			set(VCARD.N_GIVEN,names[0]);
			set(VCARD.N_MIDDLE,names[1]);
			set(VCARD.N_FAMILY,names[2]);
	}


	/*
	 * this method tries to break the display name into something meaningful
	 * to put under Given Name, Family Name and Middle Name.
	 * It'll miss prefix and suffix!
	 * */
	private static String[] tryBreakName(String displayName)
	{
	  String[] names = new String[]{"","",""};
	  int firstName = -1;
	  if ((firstName = displayName.indexOf(' ')) > 0)
	  	names[0] = displayName.substring(0,firstName);  
		else
		  return names; //exit immediately, nothing more to do
	  
	  int lastName = -1;
	  if ((lastName = displayName.lastIndexOf(' ')) >= firstName)
	    names[2] = displayName.substring(lastName+1);
		else
		  return names; //exit immediately, nothing more to do
	  
	  if (lastName > firstName)
	    names[1] = displayName.substring(firstName,lastName).trim();
	  
	  return names;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6859.java