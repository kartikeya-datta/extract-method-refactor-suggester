error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4187.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4187.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4187.java
text:
```scala
J@@AXWSUIMessages.ANNOTATIONS_VIEW_NO_SUITABLE_LIBRARY_FOUND, "JAX-WS")); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.ui.widgets;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.FileEditorInput;

public class ClasspathComposite extends Composite {
    
    private String JRE_PREF_PAGE_ID = "org.eclipse.jdt.debug.ui.preferences.VMPreferencePage"; //$NON-NLS-1$
    private String PROP_ID = "org.eclipse.jdt.ui.propertyPages.BuildPathsPropertyPage"; //$NON-NLS-1$
    private Object DATA_REVEAL_ENTRY = "select_classpath_entry"; //$NON-NLS-1$

    private Label informationLabel;
    
    public ClasspathComposite(Composite parent, int style) {
        super(parent, style);
        addControls();
    }
    
    public void addControls() {
        GridLayout gridLayout = new GridLayout();
        this.setLayout(gridLayout);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        this.setLayoutData(gridData);
        this.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        informationLabel = new Label(this, SWT.NONE);
        informationLabel.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        informationLabel.setText(JAXWSUIMessages.bind(
                JAXWSUIMessages.ANNOTATIONS_VIEW_NO_SUITABLE_LIBRARY_FOUND, "JAX-WS"));
        Link link = new Link(this, SWT.NONE);
        link.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        link.setText(JAXWSUIMessages.CONFIGURE_JAVA_1_6_LIBRARY);
        link.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                if (selectionEvent.text.equals("1")) { //$NON-NLS-1$
                    PreferencesUtil.createPreferenceDialogOn(getShell(), JRE_PREF_PAGE_ID,
                            new String[] { JRE_PREF_PAGE_ID }, null).open();
                } else {
                    Map<Object, IClasspathEntry> data = new HashMap<Object, IClasspathEntry>();
                    data.put(DATA_REVEAL_ENTRY, JavaRuntime.getDefaultJREContainerEntry());
                    PreferencesUtil.createPropertyDialogOn(getShell(), getProject(), PROP_ID,
                            new String[] { PROP_ID }, data).open();
                }
            }
        });
        //TODO update the labels to provide information for supported annotation libraries that are missing
        //on the selected projects classpath.
        //Label otherLibrariesLabel  = new Label(this, SWT.SHADOW_IN);
        //otherLibrariesLabel.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        //otherLibrariesLabel.setText("Annotation Libraries currently supported: " + AnnotationsManager.getAnnotationCategories());
        //otherLibrariesLabel.setText(JAXWSUIMessages.ANNOTATIONS_VIEW_OTHER_ANNOTATION_LIBRARIES_USE);

    }

    private IProject getProject() {
        return ((FileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor().getEditorInput()).getFile().getProject();
    }
    
    
    public void updateLibraryLabel(String libraryName) {
        informationLabel.setText(JAXWSUIMessages.bind(
                JAXWSUIMessages.ANNOTATIONS_VIEW_NO_SUITABLE_LIBRARY_FOUND, libraryName));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4187.java