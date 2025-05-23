error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16046.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16046.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16046.java
text:
```scala
private static final S@@tring PORTAL_URL = "http://portal.28msec.com/";

/*******************************************************************************
 * Copyright (c) 2008, 2009 28msec Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gabriel Petrovay (28msec) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xquery.set.internal.ui.wizards;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.wst.xquery.set.core.SETProjectConfig;
import org.eclipse.wst.xquery.set.core.SETProjectConfigUtil;
import org.eclipse.wst.xquery.set.launching.deploy.DeployInfo;
import org.eclipse.wst.xquery.set.launching.deploy.DeployManager;

public class SETDeployDataWizardPage extends WizardPage {

    private static final String DESCRIPTION = "Deploy project data in the Sausalito Cloud Infrastructure";

    private void isvalid() {
        String appName = fApplicationNameText.getText();
        if (appName.equals("")) {
            setErrorMessage("Provide an application name");
            setPageComplete(false);
            return;
        }
        if (!appName.matches("[\\p{Lower}\\p{Digit}\\-]+")) {
            setErrorMessage("Application name can only contain lower case letters, numbers, or hyphen (-)");
            setPageComplete(false);
            return;
        }
        if (appName.startsWith("-") || appName.endsWith("-") || appName.contains("--")) {
            setErrorMessage("The hyphen can not be used as first or last character, or more than once in a row");
            setPageComplete(false);
            return;
        }

        setErrorMessage(null);
        setPageComplete(true);
        return;

    }

    private class WidgetListener extends SelectionAdapter implements ModifyListener {

        public void modifyText(ModifyEvent e) {
            if (e.widget == fApplicationNameText) {
                isvalid();
            }
        }

        public void widgetSelected(SelectionEvent e) {
            if (e.widget == fShowCharsCheckButton) {
                if (fShowCharsCheckButton.getSelection()) {
                    fPasswordText.setEchoChar('\0');
                } else {
                    fPasswordText.setEchoChar((char)9679);
                }
            } else if (e.widget == fPortalLink) {
                openBrowser();
            }
            isvalid();
        }
    }

    private Link fPortalLink;
    private Text fApplicationNameText;
    private Text fUsernameText;
    private Text fPasswordText;
    private Button fRememberCheckButton;
    private Button fShowCharsCheckButton;

    private WidgetListener fListener = new WidgetListener();

    private IScriptProject fProject;
    private SETProjectConfig fConfig;

    protected SETDeployDataWizardPage(IScriptProject project) {
        super("Deploy Sausalito Project");
        fProject = project;
        fConfig = SETProjectConfigUtil.readProjectConfig(fProject.getProject());

        setTitle("Deploy Sausalito Project Data");
        setDescription(DESCRIPTION);
        setPageComplete(false);
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        setControl(composite);

        createDescriptionLabels(composite);
        createAppNameText(composite);
        SWTFactory.createVerticalSpacer(composite, 5);
        createCredentialsGroup(composite);

        initializePageFields();
        fApplicationNameText.setFocus();
        fApplicationNameText.setSelection(0, fApplicationNameText.getText().length());
    }

    private void initializePageFields() {
        DeployInfo info = DeployManager.getInstance().getCachedDeployInfo(fProject);
        if (info != null) {
            fRememberCheckButton.setSelection(true);
            fApplicationNameText.setText(info.getApplicationName());
            fUsernameText.setText(info.getUserName());
            fPasswordText.setText(info.getPassword());
        }
    }

    private static final String PORTAL_URL = "http://28msec.dyndns.org:8080/";

    private void createDescriptionLabels(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);

        fPortalLink = new Link(composite, SWT.NONE);

        fPortalLink
                .setText("Pressing finish will deploy the selected project data in the Sausalito cloud infrastructure.\n"
                        + "Make sure you already have an application created for this deployment in your\n"
                        + "<A HREF=\""
                        + PORTAL_URL
                        + "\">28msec Portal</A> account. The name of the application must be provided in the field below.");
        fPortalLink.addSelectionListener(fListener);

        SWTFactory.createVerticalSpacer(composite, 5);
    }

    private void createAppNameText(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        SWTFactory.createLabel(composite, "Project name:", 1);
        SWTFactory.createLabel(composite, fProject.getElementName(), 1);

        SWTFactory.createLabel(composite, "Application name:", 1);
        fApplicationNameText = SWTFactory.createText(composite, SWT.BORDER, 1, "");
        fApplicationNameText.addModifyListener(fListener);
    }

    private void createCredentialsGroup(Composite composite) {
        Group group = SWTFactory.createGroup(composite, "Credentials", 2, 1, GridData.FILL_HORIZONTAL);
        createUsernameText(group);
        createPasswortText(group);

        createShowCharsCheckButton(group);
        createRememberCheckButton(group);
    }

    void createUsernameText(Composite parent) {
        SWTFactory.createLabel(parent, "Username:", 1);
        fUsernameText = SWTFactory.createText(parent, SWT.BORDER, 1, "");
    }

    void createPasswortText(Composite parent) {
        SWTFactory.createLabel(parent, "Password:", 1);
        fPasswordText = SWTFactory.createText(parent, SWT.BORDER | SWT.PASSWORD, 1, "");
    }

    void createShowCharsCheckButton(Composite parent) {
        SWTFactory.createLabel(parent, "", 1);
        fShowCharsCheckButton = SWTFactory.createCheckButton(parent, "Show password", null, false, 1);
        fShowCharsCheckButton.addSelectionListener(fListener);
    }

    void createRememberCheckButton(Composite parent) {
        SWTFactory.createLabel(parent, "", 1);
        fRememberCheckButton = SWTFactory.createCheckButton(parent, "Remember deployment data for this project", null,
                false, 1);
        fRememberCheckButton.addSelectionListener(fListener);
    }

    public DeployInfo getDeployInfo() {
        return new DeployInfo(fProject, fConfig, fApplicationNameText.getText(), fUsernameText.getText(), fPasswordText
                .getText(), DeployInfo.DeployType.DATA, null);
    }

    public boolean cacheCredentials() {
        return fRememberCheckButton.getSelection();
    }

    private void openBrowser() {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                try {
                    String urlStr = PORTAL_URL;
                    URL url = new URL(urlStr);
                    IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
                    browser.openURL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16046.java