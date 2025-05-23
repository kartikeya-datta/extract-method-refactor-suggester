error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14144.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14144.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14144.java
text:
```scala
E@@Object o = r.getContents().get(0);

/*******************************************************************************
 * Copyright (c) 2005 - 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package org.eclipse.xtend.typesystem.xsd;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLOptions;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.GenericXMLResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLOptionsImpl;
import org.eclipse.xpand2.output.FileHandle;
import org.eclipse.xpand2.output.PostProcessor;
import org.eclipse.xtend.typesystem.xsd.util.Msg;
import org.eclipse.xtend.typesystem.xsd.util.XSDLog;

public class XMLBeautifier implements PostProcessor {

	protected XMLMixedContentFormater formater = createFormatter();

	protected String[] fileExtensions = new String[] { ".xml", ".xsl", ".xsd",
			".wsdd", ".wsdl" };

	protected Log log = XSDLog.getLog(getClass());

	protected Map<String, Object> loadOptions = getDefaultLoadOptions();

	protected Map<String, Object> saveOptions = getDefaultSaveOptions();

	protected URIConverter uriConverter;

	public void addLoadOption(OptionsEntry entry) {
		loadOptions.put(entry.getKey(), entry.getValue());
	}

	public void addSaveOption(OptionsEntry entry) {
		saveOptions.put(entry.getKey(), entry.getValue());
	}

	public void afterClose(FileHandle impl) {
	}

	public void beforeWriteAndClose(FileHandle impl) {
		String fn = impl.getTargetFile().getAbsolutePath();
		boolean isXML = isXmlFile(fn);
		URI u = URI.createFileURI(fn);

		if (log.isInfoEnabled()) {
			if (isXML)
				log.info(Msg.create("Beautifying ").uri(u));
			else
				log.info(Msg.create("Ignoring ").uri(u).txt(
						" since the file extension does "
								+ "not match the filter."));
		}
		if (!isXML)
			return;

		ByteArrayInputStream is = new ByteArrayInputStream(impl.getBuffer()
				.toString().trim().getBytes());

		ResourceSet rs = new ResourceSetImpl();
		if (uriConverter != null)
			rs.setURIConverter(uriConverter);
		Resource r = new GenericXMLResourceFactoryImpl().createResource(u);
		rs.getResources().add(r);

		try {
			r.load(is, loadOptions);
			EObject o = (EObject) r.getContents().get(0);

			formater.beautifyMixedContent(0, o);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			r.save(os, saveOptions);
			impl.setBuffer(os.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected XMLMixedContentFormater createFormatter() {
		return new XMLMixedContentFormater();
	}

	protected Map<String, Object> getDefaultLoadOptions() {
		final String EXT = "http://xml.org/sax/features/external-parameter-entities";
		final String VAL = "http://xml.org/sax/features/validation";
		final String LOAD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

		HashMap<String, Object> feat = new HashMap<String, Object>();
		feat.put(EXT, Boolean.FALSE);
		feat.put(VAL, Boolean.FALSE);
		feat.put(LOAD, Boolean.FALSE);

		XMLOptions xopt = new XMLOptionsImpl();
		xopt.setProcessAnyXML(true);
		xopt.setProcessSchemaLocations(false);

		HashMap<String, Object> opt = new HashMap<String, Object>();
		opt.put(XMLResource.OPTION_PARSER_FEATURES, feat);
		opt.put(XMLResource.OPTION_XML_OPTIONS, xopt);
		return opt;
	}

	protected Map<String, Object> getDefaultSaveOptions() {
		HashMap<String, Object> opt = new HashMap<String, Object>();
		opt.put(XMLResource.OPTION_SAVE_DOCTYPE, Boolean.TRUE);
		opt.put(XMLResource.OPTION_FORMATTED, Boolean.TRUE);
		opt.put(XMLResource.OPTION_LINE_WIDTH, formater.getMaxLineWidth());
		return opt;
	}

	public boolean isXmlFile(final String absolutePath) {
		for (int i = 0; i < fileExtensions.length; i++)
			if (absolutePath.endsWith(fileExtensions[i].trim()))
				return true;
		return false;
	}

	public void setFileExtensions(final String[] fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public void setFormatComments(boolean formatComments) {
		formater.setFormatComments(formatComments);
	}

	// public void setIndetationString(String indetationString) {
	// formater.setIndetationString(indetationString);
	// }
	//
	// public void setIndetationWidth(int indetationWidth) {
	// formater.setIndetationWidth(indetationWidth);
	// }

	public void setMaxLineWidth(int maxLineWidth) {
		formater.setMaxLineWidth(maxLineWidth);
	}

	public void setUriConverter(URIConverter converter) {
		uriConverter = converter;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14144.java