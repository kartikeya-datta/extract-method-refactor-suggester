error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14774.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14774.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14774.java
text:
```scala
i@@f (model.getDefaultRuntimeVersion().compareTo(CXFCorePlugin.CXF_VERSION_2_1) >= 0) {

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
package org.eclipse.jst.ws.internal.cxf.creation.ui.widgets;

import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.WSDL2JavaWidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

@SuppressWarnings("restriction")
public class WSDL2JavaDefaultsConfigWidget extends SimpleWidgetDataContributor {
    private WSDL2JavaDataModel model;
    
    public WSDL2JavaDefaultsConfigWidget() {
    }
    
    public void setWSDL2JavaDataModel(WSDL2JavaDataModel model) {
        this.model = model;
    }
    
    @Override
    public WidgetDataEvents addControls(final Composite parent, final Listener statusListener) {
        final Composite mainComposite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        mainComposite.setLayout(gridLayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        mainComposite.setLayoutData(gridData);

        Group wsdl2javaGroup = new Group(mainComposite, SWT.SHADOW_IN);
        wsdl2javaGroup.setText(CXFCreationUIMessages.WSDL2JAVA_GROUP_LABEL);
        GridLayout wsdl2javalayout = new GridLayout(1, true);
        wsdl2javaGroup.setLayout(wsdl2javalayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        wsdl2javaGroup.setLayoutData(gridData);

        WSDL2JavaWidgetFactory.createGenerateServerButton(wsdl2javaGroup, model);
        WSDL2JavaWidgetFactory.createGenerateImplementationButton(wsdl2javaGroup, model);

        WSDL2JavaWidgetFactory.createDefaultValuesButton(wsdl2javaGroup, model);

        WSDL2JavaWidgetFactory.createProcessSOAPHeadersButton(wsdl2javaGroup, model);

        WSDL2JavaWidgetFactory.createNamespacePackageMappingButton(wsdl2javaGroup, model);

        WSDL2JavaWidgetFactory.createExcludesNamespaceMappingButton(wsdl2javaGroup, model);

        if (CXFModelUtils.isAutoNameResolutionPermitted()) {
            WSDL2JavaWidgetFactory.createAutoNameResolutionButton(wsdl2javaGroup, model);
        }

        if (model.getCxfRuntimeVersion().compareTo(CXFCorePlugin.CXF_VERSION_2_1) >= 0) {
            WSDL2JavaWidgetFactory.createNoAddressBindingButton(wsdl2javaGroup, model);
        }
        
        Group xjcArgGroup = new Group(mainComposite, SWT.SHADOW_IN);
        xjcArgGroup.setText(CXFCreationUIMessages.WSDL2JAVA_XJC_ARG_GROUP_LABEL);
        GridLayout xjcArgLayout = new GridLayout(1, true);
        xjcArgGroup.setLayout(xjcArgLayout);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        xjcArgGroup.setLayoutData(gridData);

        Table xjcArgsTable = WSDL2JavaWidgetFactory.createXJCArgTable(xjcArgGroup, model);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 3;
        gridData.verticalSpan = 6;
        xjcArgsTable.setLayoutData(gridData);

        WSDL2JavaWidgetFactory.createXJCDefaultValuesTableItem(xjcArgsTable, model);
        WSDL2JavaWidgetFactory.createXJCToStringTableItem(xjcArgsTable, model);
        WSDL2JavaWidgetFactory.createXJCToStringMultiLineTableItem(xjcArgsTable, model);
        WSDL2JavaWidgetFactory.createXJCToStringSimpleTableItem(xjcArgsTable, model);
        WSDL2JavaWidgetFactory.createXJCLocatorTableItem(xjcArgsTable, model);
        WSDL2JavaWidgetFactory.createXJCSyncMethodsTableItem(xjcArgsTable, model);
        WSDL2JavaWidgetFactory.createXJCMarkGeneratedTableItem(xjcArgsTable, model);

        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14774.java