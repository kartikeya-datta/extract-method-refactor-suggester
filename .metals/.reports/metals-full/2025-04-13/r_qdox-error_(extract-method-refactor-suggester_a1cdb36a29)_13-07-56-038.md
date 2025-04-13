error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1821.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1821.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1821.java
text:
```scala
n@@ode = (Node) e.nextNode();

// $Header$
/*
 * ====================================================================
 * Copyright 2002-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

// The developers of JMeter and Apache are greatful to the developers
// of HTMLParser for giving Apache Software Foundation a non-exclusive
// license. The performance benefits of HTMLParser are clear and the
// users of JMeter will benefit from the hard work the HTMLParser
// team. For detailed information about HTMLParser, the project is
// hosted on sourceforge at http://htmlparser.sourceforge.net/.
//
// HTMLParser was originally created by Somik Raha in 2000. Since then
// a healthy community of users has formed and helped refine the
// design so that it is able to tackle the difficult task of parsing
// dirty HTML. Derrick Oswald is the current lead developer and was kind
// enough to assist JMeter.
package org.htmlparser.tags;

import org.htmlparser.Node;
import org.htmlparser.tags.data.CompositeTagData;
import org.htmlparser.tags.data.LinkData;
import org.htmlparser.tags.data.TagData;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.visitors.NodeVisitor;

/**
 * Identifies a link tag
 */
public class LinkTag extends CompositeTag {
	public static final String LINK_TAG_FILTER = "-l";

	/**
	 * The URL where the link points to
	 */
	protected String link;

	/**
	 * The text of of the link element
	 */
	protected String linkText;

	/**
	 * The accesskey existing inside this link.
	 */
	protected String accessKey;

	private boolean mailLink;

	private boolean javascriptLink;

	/**
	 * Constructor creates an HTMLLinkNode object, which basically stores the
	 * location where the link points to, and the text it contains.
	 * <p>
	 * In order to get the contents of the link tag, use the method linkData(),
	 * which returns an enumeration of nodes encapsulated within the link.
	 * <p>
	 * The following code will get all the images inside a link tag.
	 * 
	 * <pre>
	 * Node node;
	 * ImageTag imageTag;
	 * for (Enumeration e = linkTag.linkData(); e.hasMoreElements();) {
	 * 	node = (Node) e.nextElement();
	 * 	if (node instanceof ImageTag) {
	 * 		imageTag = (ImageTag) node;
	 * 		// Process imageTag
	 * 	}
	 * }
	 * </pre>
	 * 
	 * There is another mechanism available that allows for uniform extraction
	 * of images. You could do this to get all images from a web page :
	 * 
	 * <pre>
	 * Node node;
	 * Vector imageCollectionVector = new Vector();
	 * for (NodeIterator e = parser.elements(); e.hasMoreNode();) {
	 * 	node = e.nextHTMLNode();
	 * 	node.collectInto(imageCollectionVector, ImageTag.IMAGE_FILTER);
	 * }
	 * </pre>
	 * 
	 * The link tag processes all its contents in collectInto().
	 * 
	 * @param tagData
	 *            The data relating to the tag.
	 * @param compositeTagData
	 *            The data regarding the composite structure of the tag.
	 * @param linkData
	 *            The data specific to the link tag.
	 * @see #linkData()
	 */
	public LinkTag(TagData tagData, CompositeTagData compositeTagData, LinkData linkData) {
		super(tagData, compositeTagData);
		this.link = linkData.getLink();
		this.linkText = linkData.getLinkText();
		this.accessKey = linkData.getAccessKey();
		this.mailLink = linkData.isMailLink();
		this.javascriptLink = linkData.isJavascriptLink();
	}

	/**
	 * Returns the accesskey element if any inside this link tag
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * Returns the url as a string, to which this link points
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Returns the text contained inside this link tag
	 */
	public String getLinkText() {
		return linkText;
	}

	/**
	 * Return the text contained in this linkinode Kaarle Kaila 23.10.2001
	 */
	public String getText() {
		return toHtml();
	}

	/**
	 * Is this a mail address
	 * 
	 * @return boolean true/false
	 */
	public boolean isMailLink() {
		return mailLink;
	}

	/**
	 * Tests if the link is javascript
	 * 
	 * @return flag indicating if the link is a javascript code
	 */
	public boolean isJavascriptLink() {
		return javascriptLink;
	}

	/**
	 * Tests if the link is an FTP link.
	 * 
	 * @return flag indicating if this link is an FTP link
	 */
	public boolean isFTPLink() {
		return link.indexOf("ftp://") == 0;
	}

	/**
	 * Tests if the link is an HTTP link.
	 * 
	 * @return flag indicating if this link is an HTTP link
	 */
	public boolean isHTTPLink() {
		return (!isFTPLink() && !isHTTPSLink() && !isJavascriptLink() && !isMailLink());
	}

	/**
	 * Tests if the link is an HTTPS link.
	 * 
	 * @return flag indicating if this link is an HTTPS link
	 */
	public boolean isHTTPSLink() {
		return link.indexOf("https://") == 0;
	}

	/**
	 * Tests if the link is an HTTP link or one of its variations (HTTPS, etc.).
	 * 
	 * @return flag indicating if this link is an HTTP link or one of its
	 *         variations (HTTPS, etc.)
	 */
	public boolean isHTTPLikeLink() {
		return isHTTPLink() || isHTTPSLink();
	}

	/**
	 * Insert the method's description here. Creation date: (8/3/2001 1:49:31
	 * AM)
	 * 
	 * @param newMailLink
	 *            boolean
	 */
	public void setMailLink(boolean newMailLink) {
		mailLink = newMailLink;
	}

	/**
	 * Set the link as a javascript link.
	 * 
	 * @param newJavascriptLink
	 *            flag indicating if the link is a javascript code
	 */
	public void setJavascriptLink(boolean newJavascriptLink) {
		javascriptLink = newJavascriptLink;
	}

	/**
	 * Print the contents of this Link Node
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Link to : " + link + "; titled : " + linkText + "; begins at : " + elementBegin() + "; ends at : "
				+ elementEnd() + ", AccessKey=");
		if (accessKey == null)
			sb.append("null\n");
		else
			sb.append(accessKey + "\n");
		if (children() != null) {
			sb.append("  " + "LinkData\n");
			sb.append("  " + "--------\n");

			Node node;
			int i = 0;
			for (SimpleNodeIterator e = children(); e.hasMoreNodes();) {
				node = e.nextNode();
				sb.append("   " + (i++) + " ");
				sb.append(node.toString() + "\n");
			}
		}
		sb.append("  " + "*** END of LinkData ***\n");
		return sb.toString();
	}

	public void setLink(String link) {
		this.link = link;
		attributes.put("HREF", link);
	}

	/**
	 * This method returns an enumeration of data that it contains
	 * 
	 * @return Enumeration
	 * @deprecated Use children() instead.
	 */
	public SimpleNodeIterator linkData() {
		return children();
	}

	public void accept(NodeVisitor visitor) {
		visitor.visitLinkTag(this);
		super.accept(visitor);
	}

	public void removeChild(int i) {
		childTags.remove(i);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1821.java