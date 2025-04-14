error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4969.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4969.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4969.java
text:
```scala
I@@Type implType = JDTUtils.findType(model.getProjectName(), selectImplementationCombo.getText());

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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIPlugin;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.Java2WSWidgetFactory;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

@SuppressWarnings("restriction")
public class Java2WSInterfaceConfigWidget extends SimpleWidgetDataContributor {
    private IStatus IMPL_SELECTION_STATUS = Status.OK_STATUS;

    private Java2WSDataModel model;
    private IType startingPointType;

    private Combo selectImplementationCombo;
    
    public Java2WSInterfaceConfigWidget() {
    }

    public void setJava2WSDataModel(Java2WSDataModel model) {
        this.model = model;
    }

    public void setJavaStartingPointType(IType startingPointType) {
        this.startingPointType = startingPointType;
    }

    @Override
    public WidgetDataEvents addControls(Composite parent, final Listener statusListener) {
        final Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        composite.setLayout(gridLayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        composite.setLayoutData(gridData);
        
        Java2WSWidgetFactory.createSelectImplementationLabel(composite);
        
        selectImplementationCombo = Java2WSWidgetFactory
            .createSelectImplementationCombo(composite, model, startingPointType);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        selectImplementationCombo.setLayoutData(gridData);
        
        selectImplementationCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                validateImplementationSelection();
                statusListener.handleEvent(null);
            }
        });
        
        selectImplementationCombo.addModifyListener(new ModifyListener() {

        	public void modifyText(ModifyEvent event) {
            	validateImplementationSelection();
                statusListener.handleEvent(null);
            }
        });
        
        Button browseImplButton = Java2WSWidgetFactory.createBrowseButton(composite);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        browseImplButton.setLayoutData(gridData);
        
        browseImplButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                
                ElementTreeSelectionDialog selectionDialog = Java2WSWidgetFactory.createElementTreeSelectionDialog(
                        composite.getShell(), CXFCreationUIMessages.JAVA2WS_SELECT_IMPL_DIALOG_TITLE,
                        CXFCreationUIMessages.JAVA2WS_SELECT_IMPL_DIALOG_DESCRIPTION,
                        JDTUtils.getJavaProject(model.getProjectName()), IJavaSearchConstants.CLASS);

                int returnCode = selectionDialog.open();
                if (returnCode == Window.OK) {
                    ICompilationUnit selectedCompilationUnit = (ICompilationUnit) selectionDialog.getFirstResult();
                    String selectedImplementation = selectedCompilationUnit.findPrimaryType().getFullyQualifiedName();
                    List<String> impls = Arrays.asList(selectImplementationCombo.getItems());
                    if (!impls.contains(selectedImplementation)) {
                    	selectImplementationCombo.add(selectedImplementation);
                    }
                    selectImplementationCombo.setText(selectedImplementation);
                }
            }
        });
        return this;
    }

    @Override
    public IStatus getStatus() {
        return IMPL_SELECTION_STATUS;
    }
    
	private void validateImplementationSelection() {
        IType implType = JDTUtils.getType(model.getProjectName(), selectImplementationCombo.getText());
        if (implType != null) {
            try {
                IMethod[] seiMethods = startingPointType.getMethods();
                for (IMethod seiMethod : seiMethods) {
                    IMethod[] implMethod = implType.findMethods(seiMethod);
                    if (implMethod == null) {
                    	IMPL_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
                    	    CXFCreationUIMessages.bind(CXFCreationUIMessages.WEBSERVICE_ENPOINTINTERFACE_MUST_IMPLEMENT,
                                    getImplementsMessage(startingPointType, seiMethod)));
                        break;
                    }
                    IMPL_SELECTION_STATUS = Status.OK_STATUS;
                }
                model.setServiceEndpointInterfaceName(startingPointType.getFullyQualifiedName());
                model.setFullyQualifiedJavaClassName(selectImplementationCombo.getText());
            } catch (JavaModelException jme) {
                CXFCreationUIPlugin.log(jme.getStatus());
            }
        } else {
        	IMPL_SELECTION_STATUS = new Status(IStatus.ERROR, CXFCreationUIPlugin.PLUGIN_ID,
        	    CXFCreationUIMessages.bind(CXFCreationUIMessages.WEBSERVICE_IMPLEMENTATION_NOT_FOUND,
                    		selectImplementationCombo.getText()));   
        }        
	}

    private String getImplementsMessage(IType seiType, IMethod seiMethod) {
        StringBuilder message = new StringBuilder(seiType.getElementName());
        message.append("."); //$NON-NLS-1$
        message.append(seiMethod.getElementName());
        message.append("("); //$NON-NLS-1$
        String[] parameterTypes = seiMethod.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            String parameterType = Signature.toString(parameterTypes[i]);
            message.append(parameterType);
            if (i < parameterTypes.length - 1) {
                message.append(", "); //$NON-NLS-1$
            }
        }
        message.append(")"); //$NON-NLS-1$
        return message.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4969.java