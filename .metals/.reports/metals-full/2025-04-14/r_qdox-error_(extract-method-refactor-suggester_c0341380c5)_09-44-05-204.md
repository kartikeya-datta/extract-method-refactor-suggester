error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10203.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10203.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10203.java
text:
```scala
public static final S@@tring DEFAULT_SERVER_ID = "ecftcp://localhost:3282/server";

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

package org.eclipse.ecf.example.collab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.core.ISharedObjectContainer;
import org.eclipse.ecf.core.SharedObjectContainerFactory;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.example.collab.share.EclipseCollabSharedObject;
import org.eclipse.ecf.example.collab.share.SharedObjectEventListener;
import org.eclipse.ecf.example.collab.share.TreeItem;
import org.eclipse.ecf.example.collab.share.User;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Client {
    private static final int CONTAINER_DISPOSE = 1000;
    public static final String JOIN_TIME_FORMAT = "hh:mm:ss a z";
    public static final String GENERIC_CONTAINER_CLIENT_NAME = "org.eclipse.ecf.provider.generic.Client";
    public static final String GENERIC_CONTAINER_SERVER_NAME = "org.eclipse.ecf.provider.generic.Server";
    public static final String DEFAULT_SERVER_ID = "ecftcp://localhost:3282//server";
    public static final String COLLAB_SHARED_OBJECT_ID = "chat";
    public static final String FILE_DIRECTORY = "received_files";
    public static final String USERNAME = System.getProperty("user.name");
    public static final String ECFDIRECTORY = "ECF_" + FILE_DIRECTORY + "/";
    static ISharedObjectContainer client = null;
    static EclipseCollabSharedObject sharedObject = null;
    static ID defaultGroupID = null;
    static ID groupID = null;
    static ID sharedObjectID = null;

    public Client() throws Exception {
        defaultGroupID = IDFactory.makeStringID(DEFAULT_SERVER_ID);
    }

    protected User getUserData(String containerType, ID clientID, String usernick, String proj) {
        Vector topElements = new Vector();
        String contType = containerType.substring(containerType.lastIndexOf(".")+1);
        topElements.add(new TreeItem("Project", proj));
        SimpleDateFormat sdf = new SimpleDateFormat(JOIN_TIME_FORMAT);
        topElements.add(new TreeItem("Join Time",sdf.format(new Date())));
        topElements.add(new TreeItem("Container Type",contType));
        return new User(clientID, usernick, topElements);
    }

    protected String getSharedFileDirectoryForProject(IProject proj) {
        String eclipseDir = Platform.getLocation().lastSegment();
        if (proj == null)
            return eclipseDir + "/" + ECFDIRECTORY;
        else return FILE_DIRECTORY;
    }

    protected IProject getFirstProjectFromWorkspace() throws Exception {
        IWorkspace ws = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot wr = ws.getRoot();
        IProject[] projects = wr.getProjects();
        if (projects == null)
            return null;
        return projects[0];
    }

    protected void makeAndAddSharedObject(ISharedObjectContainer client,
            IProject proj, User user, String fileDir) throws Exception {
        IWorkbenchWindow ww = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow();
        sharedObject = new EclipseCollabSharedObject(proj, ww,
                user, fileDir);
        sharedObject.setListener(new SharedObjectEventListener() {
            public void memberRemoved(ID member) {
                if (member.equals(groupID)) {
                    disposeClient();
                }
            }
            public void memberAdded(ID member) {}
            public void otherActivated(ID other) {}
            public void otherDeactivated(ID other) {}
            public void windowClosing() {
                disposeClient();
            }
        });
        ID newID = IDFactory.makeStringID(COLLAB_SHARED_OBJECT_ID);
        client.getSharedObjectManager().addSharedObject(newID, sharedObject,
                new HashMap(), null);
    }

    protected void addObjectToClient(ISharedObjectContainer client,
            String username, IProject proj) throws Exception {
        IProject project = (proj == null) ? getFirstProjectFromWorkspace()
                : proj;
        String fileDir = getSharedFileDirectoryForProject(project);
        String projName = (project == null) ? "<workspace>" : project.getName();
        User user = getUserData(client.getClass().getName(),client.getConfig().getID(),
                (username == null) ? USERNAME : username, projName);
        makeAndAddSharedObject(client, project, user, fileDir);
    }

    public synchronized boolean isConnected() {
        return (client != null);
    }

    public synchronized void createAndConnectClient(String type, ID gID, String username,
            Object data, IProject proj) throws Exception {
        String containerType = (type==null)?GENERIC_CONTAINER_CLIENT_NAME:type;
        client = SharedObjectContainerFactory
                .makeSharedObjectContainer(containerType);
        if (gID == null) {
            groupID = defaultGroupID;
        } else {
            groupID = gID;
        }
        addObjectToClient(client, username, proj);
        if (groupID == null)
            client.joinGroup(defaultGroupID, data);
        else
            client.joinGroup(groupID, data);
    }

    public synchronized void disposeClient() {
        if (isConnected()) {
            try {
                if (sharedObject != null) sharedObject.destroySelf();
            } catch (Exception e) {}
            sharedObject = null;
        }
    }
    
    public synchronized ISharedObjectContainer getContainer() {
        return client;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10203.java