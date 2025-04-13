error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2624.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2624.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2624.java
text:
```scala
r@@eturn new Status(IStatus.ERROR,ClientPlugin.PLUGIN_ID,IStatus.OK,"Could not connect to "+targetID.getName()+".  "+e.getMessage(),e);

/****************************************************************************
* Copyright (c) 2004 Composent, Inc. and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Composent, Inc. - initial API and implementation
*****************************************************************************/

package org.eclipse.ecf.example.collab.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.example.collab.Client;
import org.eclipse.ecf.example.collab.ClientPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class ClientConnectAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
    
    public static final String DEFAULT_SERVER_ID = "ecftcp://localhost:3282/server";

    public void setData(Object data) {
        this.data = data;
    }
    public void setProject(IProject project) {
        this.project = project;
        if (project==null) {
            projectName = "<workspace>";
        } else {
            projectName = project.getName();
        }
    }
    public void setTargetID(ID targetID) {
        this.targetID = targetID;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setContainerType(String type) {
        this.containerType = type;
    }
    protected String containerType = null;
    protected ID targetID = null;
    protected String username = null;
    protected Object data = null;
    protected IProject project = null;
    protected String projectName = null;
    protected Client client = null;
    
	public ClientConnectAction() {
        try {
            targetID = IDFactory.makeStringID(DEFAULT_SERVER_ID);
            client = new Client();
        } catch (Exception e) {
            throw new RuntimeException("Exception in ClientConnectAction()",e);
        }
	}
    
    public ClientConnectAction(ID targetID, String username, Object data, IProject project) {
        this.targetID = targetID;
        this.username = username;
        this.data = data;
        setProject(project);
    }
	public class ClientConnectJob extends Job {
        public ClientConnectJob(String name) {
            super(name);
        }
        public IStatus run(IProgressMonitor pm) {
            try {
                Status okStatus = new Status(IStatus.OK,ClientPlugin.PLUGIN_ID,IStatus.OK,"Connected",null);
                // Make sure we're disconnected
                Client.ClientEntry entry = client.isConnected(project,containerType);
                if (entry != null) {
                    Display.getDefault().syncExec(new Runnable() {
                        public void run() {
                            Display.getDefault().beep();
                            MessageDialog.openInformation(
                                    null,
                                    "Already connected",
                                    "Already connected for provider "+((containerType==null)?Client.GENERIC_CONTAINER_CLIENT_NAME:containerType));
                        }
                    });
                    return okStatus;
                }
                // Actually create and connect client
                client.createAndConnectClient(containerType,targetID,username,data,project);
                return okStatus;
            } catch (Exception e) {
                return new Status(IStatus.ERROR,ClientPlugin.PLUGIN_ID,IStatus.OK,"Could not connect to group '"+targetID.getName()+"'",e);
            }
        }        
    }
	public void run(IAction action) {
        ClientConnectJob clientConnect = new ClientConnectJob("Collaboration for '"+projectName+"'");
        clientConnect.schedule();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void dispose() {

	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2624.java