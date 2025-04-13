error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14638.java
text:
```scala
m@@odel.setJavaSourceFolder(JDTUtils.getJavaProjectSourceDirectoryPath(model.getProjectName()).toOSString());

/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.consumption.core.commands;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.wsdl.Definition;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.cxf.consumption.core.CXFConsumptionCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.context.WSDL2JavaPersistentContext;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.jst.ws.jaxws.core.utils.WSDLUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * Loads the <code>WSDL2JavaDataModel</code> with the persisted defaults 
 * from the preferences and with the initial runtime information such as the
 * starting point WSDL URL.
 * 
 */
public class WSDL2JavaClientDefaultingCommand extends AbstractDataModelOperation {
    private WSDL2JavaDataModel model;
    private String projectName;
    private String inputURL;

    /**
     * Constructs a WSDL2JavaClientDefaultingCommand object.
     * @param model the <code>WSDL2JavaDataModel</code> used to pass information
     *              between commands.
     */
    public WSDL2JavaClientDefaultingCommand(WSDL2JavaDataModel model, String projectName, String inputURL) {
        this.model = model;
        this.projectName = projectName;
        this.inputURL = inputURL;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
    	IStatus status = Status.OK_STATUS;
        WSDL2JavaPersistentContext context = CXFCorePlugin.getDefault().getWSDL2JavaContext();
        model.setCxfRuntimeVersion(context.getCxfRuntimeVersion());
        model.setCxfRuntimeEdition(context.getCxfRuntimeEdition());
        model.setProjectName(projectName);

        model.setIncludedNamespaces(new HashMap<String, String>());
        model.setExcludedNamespaces(new HashMap<String, String>());

        // XJC
        model.setXjcUseDefaultValues(context.isXjcUseDefaultValues());
        model.setXjcToString(context.isXjcToString());
        model.setXjcToStringMultiLine(context.isXjcToStringMultiLine());
        model.setXjcToStringSimple(context.isXjcToStringSimple());
        model.setXjcLocator(context.isXjcLocator());
        model.setXjcSyncMethods(context.isXjcSyncMethods());
        model.setXjcMarkGenerated(context.isXjcMarkGenerated());

        model.setValidate(context.isValidate());
        model.setProcessSOAPHeaders(context.isProcessSOAPHeaders());
        model.setLoadDefaultExcludesNamepsaceMapping(context.isLoadDefaultExcludesNamepsaceMapping());
        model.setLoadDefaultNamespacePackageNameMapping(context.isLoadDefaultNamespacePackageNameMapping());
        model.setUseDefaultValues(context.isUseDefaultValues());
        model.setNoAddressBinding(context.isNoAddressBinding());
        model.setAutoNameResolution(context.isAutoNameResolution());
        
        model.setJavaSourceFolder(JDTUtils.getJavaProjectSourceDirectoryPath(model.getProjectName()));

    	try {
    		URL wsdlUrl = new URL(inputURL);
			model.setWsdlURL(wsdlUrl);
			
			Definition definition = WSDLUtils.readWSDL(model.getWsdlURL());
        	if (definition != null) {
        		String targetNamespace = definition.getTargetNamespace();
        		String packageName = WSDLUtils.getPackageNameFromNamespace(targetNamespace);
        		model.setTargetNamespace(targetNamespace);
        		model.getIncludedNamespaces().put(targetNamespace, packageName);

                String wsdlLocation = WSDLUtils.getWSDLLocation(definition);
                if (wsdlLocation != null) {
                    model.setWsdlLocation(wsdlLocation);
                }
        		
        		model.setWsdlDefinition(definition);
        	}

		} catch (MalformedURLException murle) {
		    status = new Status(IStatus.ERROR, CXFConsumptionCorePlugin.PLUGIN_ID, 
		            murle.getLocalizedMessage());
			CXFConsumptionCorePlugin.log(status);
		} catch (IOException ioe) {
		    status = new Status(IStatus.ERROR, CXFConsumptionCorePlugin.PLUGIN_ID, 
		    		ioe.getLocalizedMessage());
			CXFConsumptionCorePlugin.log(status);
		}
        return status;
    }
    
    public WSDL2JavaDataModel getWSDL2JavaDataModel() {
        return model;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14638.java