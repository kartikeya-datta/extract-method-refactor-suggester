error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4165.java
text:
```scala
X@@MLWriter xmlWriter = new XMLWriter(writer, null/*use the workspace line delimiter*/);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.util.Messages;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Internal model element to represent a user library and code to serialize / deserialize.
 */
public class UserLibrary {

	private static final String CURRENT_VERSION= "1"; //$NON-NLS-1$

	private static final String TAG_VERSION= "version"; //$NON-NLS-1$
	private static final String TAG_USERLIBRARY= "userlibrary"; //$NON-NLS-1$
	private static final String TAG_SOURCEATTACHMENT= "sourceattachment"; //$NON-NLS-1$
	private static final String TAG_SOURCEATTACHMENTROOT= "sourceattachmentroot"; //$NON-NLS-1$
	private static final String TAG_PATH= "path"; //$NON-NLS-1$
	private static final String TAG_ARCHIVE= "archive"; //$NON-NLS-1$
	private static final String TAG_SYSTEMLIBRARY= "systemlibrary"; //$NON-NLS-1$
	
	private boolean isSystemLibrary;
	private IClasspathEntry[] entries;

	public UserLibrary(IClasspathEntry[] entries, boolean isSystemLibrary) {
		Assert.isNotNull(entries);
		this.entries= entries;
		this.isSystemLibrary= isSystemLibrary;
	}
	
	public IClasspathEntry[] getEntries() {
		return this.entries;
	}
	
	public boolean isSystemLibrary() {
		return this.isSystemLibrary;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == getClass()) {
			UserLibrary other= (UserLibrary) obj;
			if (this.entries.length == other.entries.length && this.isSystemLibrary == other.isSystemLibrary) {
				for (int i= 0; i < this.entries.length; i++) {
					if (!this.entries[i].equals(other.entries[i])) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashCode= 0;
		if (this.isSystemLibrary) {
			hashCode++;
		}
		for (int i= 0; i < this.entries.length; i++) {
			hashCode= hashCode * 17 + this.entries.hashCode();
		}
		return hashCode;
	}
	
	/* package */  String serialize() throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(s, "UTF8"); //$NON-NLS-1$
		XMLWriter xmlWriter = new XMLWriter(writer);
		
		HashMap library = new HashMap();
		library.put(TAG_VERSION, String.valueOf(CURRENT_VERSION));
		library.put(TAG_SYSTEMLIBRARY, String.valueOf(this.isSystemLibrary));
		xmlWriter.printTag(TAG_USERLIBRARY, library, true, true, false);
		
		for (int i = 0; i < this.entries.length; ++i) {
			ClasspathEntry cpEntry = (ClasspathEntry) this.entries[i];
		
			HashMap archive = new HashMap();
			archive.put(TAG_PATH, cpEntry.getPath().toString());
			IPath sourceAttach= cpEntry.getSourceAttachmentPath();
			if (sourceAttach != null)
				archive.put(TAG_SOURCEATTACHMENT, sourceAttach);
			IPath sourceAttachRoot= cpEntry.getSourceAttachmentRootPath();
			if (sourceAttachRoot != null)
				archive.put(TAG_SOURCEATTACHMENTROOT, sourceAttachRoot);				

			boolean hasExtraAttributes = cpEntry.extraAttributes != null && cpEntry.extraAttributes.length != 0;
			boolean hasRestrictions = cpEntry.getAccessRuleSet() != null; // access rule set is null if no access rules
			xmlWriter.printTag(TAG_ARCHIVE, archive, true, true, !(hasExtraAttributes || hasRestrictions));

			// write extra attributes if necessary
			if (hasExtraAttributes) {
				cpEntry.encodeExtraAttributes(xmlWriter, true, true);
			}

			// write extra attributes and restriction if necessary
			if (hasRestrictions) {
				cpEntry.encodeAccessRules(xmlWriter, true, true);
			}

			// write archive end tag if necessary
			if (hasExtraAttributes || hasRestrictions) {
				xmlWriter.endTag(TAG_ARCHIVE, true);
			}
		}	
		xmlWriter.endTag(TAG_USERLIBRARY, true);
		writer.flush();
		writer.close();
		return s.toString("UTF8");//$NON-NLS-1$
	}
	
	/* package */ static UserLibrary createFromString(Reader reader) throws IOException {
		Element cpElement;
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			cpElement = parser.parse(new InputSource(reader)).getDocumentElement();
		} catch (SAXException e) {
			throw new IOException(Messages.file_badFormat); 
		} catch (ParserConfigurationException e) {
			throw new IOException(Messages.file_badFormat); 
		} finally {
			reader.close();
		}
		
		if (!cpElement.getNodeName().equalsIgnoreCase(TAG_USERLIBRARY)) { //$NON-NLS-1$
			throw new IOException(Messages.file_badFormat); 
		}
		// String version= cpElement.getAttribute(TAG_VERSION);
		// in case we update the format: add code to read older versions
		
		boolean isSystem= Boolean.valueOf(cpElement.getAttribute(TAG_SYSTEMLIBRARY)).booleanValue();
		
		NodeList list= cpElement.getChildNodes();
		int length = list.getLength();
		
		ArrayList res= new ArrayList(length);
		for (int i = 0; i < length; ++i) {
			Node node = list.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element= (Element) node;
				if (element.getNodeName().equals(TAG_ARCHIVE)) {
					String path = element.getAttribute(TAG_PATH);
					IPath sourceAttach= element.hasAttribute(TAG_SOURCEATTACHMENT) ? new Path(element.getAttribute(TAG_SOURCEATTACHMENT)) : null;
					IPath sourceAttachRoot= element.hasAttribute(TAG_SOURCEATTACHMENTROOT) ? new Path(element.getAttribute(TAG_SOURCEATTACHMENTROOT)) : null;
					IClasspathAttribute[] extraAttributes = ClasspathEntry.decodeExtraAttributes(element);
					IAccessRule[] accessRules = ClasspathEntry.decodeAccessRules(element);
					IClasspathEntry entry = JavaCore.newLibraryEntry(new Path(path), sourceAttach, sourceAttachRoot, accessRules, extraAttributes, false/*not exported*/);
					res.add(entry);
				}
			}
		}
		
		IClasspathEntry[] entries= (IClasspathEntry[]) res.toArray(new IClasspathEntry[res.size()]);
		
		return new UserLibrary(entries, isSystem);
	}
	
	public String toString() {
		if (this.entries == null)
			return "null"; //$NON-NLS-1$
		StringBuffer buffer = new StringBuffer();
		int length = this.entries.length;
		for (int i=0; i<length; i++) {
			buffer.append(this.entries[i].toString()+'\n');
		}
		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4165.java