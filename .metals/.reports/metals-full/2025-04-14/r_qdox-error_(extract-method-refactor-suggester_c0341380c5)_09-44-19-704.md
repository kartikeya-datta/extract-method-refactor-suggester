error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14318.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14318.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14318.java
text:
```scala
public b@@oolean isValidSequence(String element, List<String> nodes,

/*******************************************************************************
 *Copyright (c) 2008 Standard for Technology in Automotive Retail  and others.
 *All rights reserved. This program and the accompanying materials
 *are made available under the terms of the Eclipse Public License v1.0
 *which accompanies this distribution, and is available at
 *http://www.eclipse.org/legal/epl-v10.html
 *
 *Contributors:
 *    David Carver (STAR) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.vex.core.internal.validator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDataType;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMGroup;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNodeList;
import org.eclipse.wst.xml.core.internal.contentmodel.ContentModelManager;
import org.eclipse.wst.xml.vex.core.internal.provisional.dom.Validator;

@SuppressWarnings("restriction")
public class WTPVEXValidator implements Validator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7632029717211069257L;
	private static CMDocument contentModel;

	public static CMDocument getContentModel() {
		return contentModel;
	}

	public static void setContentModel(CMDocument contentModel) {
		WTPVEXValidator.contentModel = contentModel;
	}

	private CMDocument attributeContentModel;

	private WTPVEXValidator() {
	}

	public static WTPVEXValidator create(URL url) throws IOException {
		ContentModelManager contentModelManager = ContentModelManager
				.getInstance();
		String resolved = url.toString();
		setContentModel(contentModelManager.createCMDocument(resolved, null));

		return new WTPVEXValidator();

	}

	public AttributeDefinition getAttributeDefinition(String element,
			String attribute) {
		attributeContentModel = getContentModel();
		CMElementDeclaration cmelement = getElementDeclaration(element);
		CMAttributeDeclaration attr = (CMAttributeDeclaration) cmelement
				.getAttributes().getNamedItem(attribute);
		AttributeDefinition vexAttr = createAttributeDefinition(attr);
		return vexAttr;
	}

	public AttributeDefinition[] getAttributeDefinitions(String element) {
		CMElementDeclaration cmelement = getElementDeclaration(element);
		AttributeDefinition[] attributeDefs = new AttributeDefinition[cmelement
				.getAttributes().getLength()];
		List<AttributeDefinition> attributeList = new ArrayList<AttributeDefinition>(
				cmelement.getAttributes().getLength());
		Iterator iter = cmelement.getAttributes().iterator();
		while (iter.hasNext()) {
			CMAttributeDeclaration attribute = (CMAttributeDeclaration) iter
					.next();
			AttributeDefinition vexAttr = createAttributeDefinition(attribute);
			attributeList.add(vexAttr);
		}

		attributeList.toArray(attributeDefs);
		return attributeDefs;
	}

	private CMElementDeclaration getElementDeclaration(String element) {
		CMElementDeclaration cmelement = (CMElementDeclaration) contentModel
				.getElements().getNamedItem(element);
		return cmelement;
	}

	private AttributeDefinition createAttributeDefinition(
			CMAttributeDeclaration attribute) {
		String defaultValue = attribute.getDefaultValue();
		String[] values = null;
		AttributeDefinition.Type type = null;
		if (attribute.getAttrType().equals(CMDataType.ENUM)) {
			type = AttributeDefinition.Type.ENUMERATION;
			values = attribute.getAttrType().getEnumeratedValues();
		} else if (attribute.getAttrType().equals(CMDataType.NOTATION)) {
			type = AttributeDefinition.Type.ENUMERATION;
			values = attribute.getAttrType().getEnumeratedValues();
		} else
			type = AttributeDefinition.Type.get(attribute.getAttrType()
					.getDataTypeName());
		boolean required = (attribute.getUsage() == CMAttributeDeclaration.REQUIRED);
		boolean fixed = (attribute.getUsage() == CMAttributeDeclaration.FIXED);
		AttributeDefinition vexAttr = new AttributeDefinition(attribute
				.getAttrName(), type, defaultValue, values, required, fixed);
		return vexAttr;
	}

	public Set<String> getValidItems(String element) {
		CMElementDeclaration elementDec = (CMElementDeclaration) contentModel
				.getElements().getNamedItem(element);
		List<CMNode> nodes = getAvailableContent(element, elementDec);
		Set<String> results = new HashSet<String>();
		Iterator iter = nodes.iterator();
		while (iter.hasNext()) {
			CMNode node = (CMNode) iter.next();
			if (node instanceof CMElementDeclaration) {
				CMElementDeclaration elem = (CMElementDeclaration) node;
				results.add(elem.getElementName());
			}
		}
		return results;
	}

	/**
	 * Returns a list of all CMNode 'meta data' that may be potentially added to
	 * the element.
	 */
	protected List<CMNode> getAvailableContent(String element, CMElementDeclaration ed) {
		List<CMNode> list = new ArrayList<CMNode>();
		if (ed.getContentType() == CMElementDeclaration.ELEMENT
 ed.getContentType() == CMElementDeclaration.MIXED) {
			CMGroup groupContent = (CMGroup) ed.getContent();
			list.addAll(getAllChildren(groupContent));
		}
		return list;
	}
	
	private List<CMNode> getAllChildren(CMGroup group) {
		List<CMNode> list = new ArrayList<CMNode>();
		CMNodeList nodeList = group.getChildNodes();
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			CMNode node = nodeList.item(i);
			if (node instanceof CMElementDeclaration) {
				String e = node.getNodeName();
				list.add(node);
			} else if (node instanceof CMGroup) {
				list.addAll(getAllChildren((CMGroup)node));
			}
		}
		return list;
	}

	public Set getValidRootElements() {
		Set<String> results = new HashSet<String>();

		Iterator iter = contentModel.getElements().iterator();
		while (iter.hasNext()) {
			CMElementDeclaration element = (CMElementDeclaration) iter.next();
			results.add(element.getElementName());
		}

		return results;
	}

	public boolean isValidSequence(String element, String[] nodes,
			boolean partial) {
		return true;
	}

	public boolean isValidSequence(String element, String[] seq1,
			String[] seq2, String[] seq3, boolean partial) {
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14318.java