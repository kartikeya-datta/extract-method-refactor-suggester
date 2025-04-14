error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11617.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11617.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11617.java
text:
```scala
public static final S@@tring ROOM_DELIMITER = ",";

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
package org.eclipse.ecf.presence.ui.chatroom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.IExceptionHandler;
import org.eclipse.ecf.internal.presence.ui.Activator;
import org.eclipse.ecf.internal.presence.ui.Messages;
import org.eclipse.ecf.presence.IIMMessageEvent;
import org.eclipse.ecf.presence.IIMMessageListener;
import org.eclipse.ecf.presence.chatroom.IChatRoomContainer;
import org.eclipse.ecf.presence.chatroom.IChatRoomInfo;
import org.eclipse.ecf.presence.chatroom.IChatRoomManager;
import org.eclipse.ecf.presence.chatroom.IChatRoomMessage;
import org.eclipse.ecf.presence.chatroom.IChatRoomMessageEvent;
import org.eclipse.ecf.ui.views.ChatRoomManagerView;
import org.eclipse.ecf.ui.views.IChatRoomViewCloseListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Chat room manager user interface.
 */
public class ChatRoomManagerUI {
	
	public static final String ROOM_DELIMITER = "&";
	
	private IContainer container;

	private IChatRoomManager manager;

	private boolean isContainerConnected = false;

	private boolean viewAlreadyActive = false;

	private IExceptionHandler exceptionHandler = null;

	private ChatRoomManagerView chatroomview = null;

	protected ID targetID = null;

	public ChatRoomManagerUI(IContainer container, IChatRoomManager manager) {
		this(container, manager, null);
	}

	public ChatRoomManagerUI(IContainer container, IChatRoomManager manager,
			IExceptionHandler exceptionHandler) {
		super();
		this.container = container;
		this.manager = manager;
		this.exceptionHandler = exceptionHandler;
	}

	public ID getTargetID() {
		return targetID;
	}

	private void setupNewView() throws Exception {
		IChatRoomInfo roomInfo = manager.getChatRoomInfo(null);
		Assert.isNotNull(roomInfo,
				Messages.ChatRoomManagerUI_EXCEPTION_NO_ROOT_CHAT_ROOM_MANAGER);
		IChatRoomContainer chatRoom = roomInfo.createChatRoomContainer();
		// initialize the chatroomview with the necessary
		// information
		chatroomview.initialize(new IChatRoomViewCloseListener() {
			public void chatRoomViewClosing(String secondaryID) {
				container.dispose();
			}
		}, chatRoom, manager, targetID);
		// Add listener for container, so that if the container is spontaneously
		// disconnected,
		// then we will be able to have the UI respond by making itself inactive
		container.addListener(new IContainerListener() {
			public void handleEvent(final IContainerEvent evt) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						if (evt instanceof IContainerDisconnectedEvent) {
							IContainerDisconnectedEvent cd = (IContainerDisconnectedEvent) evt;
							final ID departedContainerID = cd.getTargetID();
							ID connectedID = targetID;
							if (connectedID == null
 connectedID.equals(departedContainerID)) {
								chatroomview.disconnected();
								isContainerConnected = false;
							}
						} else if (evt instanceof IContainerConnectedEvent) {
							isContainerConnected = true;
							chatroomview.setEnabled(true);
							String [] channels = getRoomsForTarget();
							for (int i=0; i < channels.length; i++) {
								chatroomview.joinRoom(channels[i]);
							}
						}
					}
				});
			}
		});
		// Add listeners so that the new chat room gets
		// asynch notifications of various relevant chat room events
		chatRoom.addMessageListener(new IIMMessageListener() {
			public void handleMessageEvent(IIMMessageEvent messageEvent) {
				if (messageEvent instanceof IChatRoomMessageEvent) {
					IChatRoomMessage m = ((IChatRoomMessageEvent) messageEvent)
							.getChatRoomMessage();
					chatroomview.handleMessage(m.getFromID(), m.getMessage());
				}
			}
		});
	}

	/**
	 * Show a chat room manager UI for given targetID.  If a UI already
	 * exists that is connected to the given targetID, then it will be raised.
	 * and isContainerConnected
	 * connected to the given targetID then this will show the view associated
	 * with this targetID, and return <code>true</code>. The caller then
	 * <b>should not</b> connect the container, as there is already a container
	 * connected to the given target. If we are not already connected, then this
	 * method will return <code>false</code>, indicating that the caller
	 * should connect the new container to the given target ID.
	 * 
	 * @param targetID
	 */
	public void showForTarget(final ID targetID) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					ChatRoomManagerUI.this.targetID = targetID;
					chatroomview = getChatRoomManagerView();
					// If we're not already active, then setup new view
					if (!viewAlreadyActive) {
						setupNewView();
					} else if (isContainerConnected) {
						// If we are already active, and connected, then just
						// join room s
						String [] channels = getRoomsForTarget();
						for (int i=0; i < channels.length; i++) {
							chatroomview.joinRoom(channels[i]);
						}
						// We're already connected, so all we do is return
						return;
					}
				} catch (Exception e) {
					if (exceptionHandler != null)
						exceptionHandler.handleException(e);
					else
						Activator
								.getDefault()
								.getLog()
								.log(
										new Status(
												IStatus.ERROR,
												Activator.PLUGIN_ID,
												IStatus.ERROR,
												Messages.ChatRoomManagerUI_EXCEPTION_CHAT_ROOM_VIEW_INITIALIZATION
														+ targetID, e));
				}
			}

		});
	}

	public boolean isContainerConnected() {
		return isContainerConnected;
	}

	protected String getSecondaryViewID(ID targetID) {
		URI uri;
		try {
			uri = new URI(targetID.getName());
		} catch (URISyntaxException e) {
			return null;
		}
		// Get authority, host, and port to define view ID
		int port = uri.getPort();
		return uri.getAuthority() + ((port == -1) ? "" : ":" + port); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected ChatRoomManagerView getChatRoomManagerView()
			throws PartInitException {
		// Get view
		String secondaryViewID = getSecondaryViewID(targetID);
		IWorkbenchWindow ww = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWorkbenchPage wp = ww.getActivePage();
		ChatRoomManagerView view = null;
		if (secondaryViewID == null)
			view = (ChatRoomManagerView) wp.showView(org.eclipse.ecf.ui.views.ChatRoomManagerView.VIEW_ID);
		else {
			IViewReference viewRef = wp.findViewReference(
					org.eclipse.ecf.ui.views.ChatRoomManagerView.VIEW_ID, secondaryViewID);
			if (viewRef == null)
				view = (ChatRoomManagerView) wp.showView(
						org.eclipse.ecf.ui.views.ChatRoomManagerView.VIEW_ID, secondaryViewID,
						IWorkbenchPage.VIEW_ACTIVATE);
			else {
				// Old view with same secondaryViewID found, so use/restore it
				// rather than creating new view
				view = (ChatRoomManagerView) viewRef.getView(true);
			}
		}
		viewAlreadyActive = view.isEnabled();
		return view;
	}

	protected String modifyRoomNameForTarget(String roomName) {
		return roomName;
	}
	
	protected String[] getRoomsForTarget() {
		String initialRooms = null;
		try {
			URI targetURI = new URI(targetID.getName());
			initialRooms = targetURI.getPath();
		} catch (URISyntaxException e) {
		}
		if (initialRooms == null || initialRooms.equals("")) //$NON-NLS-1$
			return new String[0];
		while (initialRooms.charAt(0) == '/')
			initialRooms = initialRooms.substring(1);
		
		StringTokenizer st = new StringTokenizer(initialRooms,ROOM_DELIMITER);
		int tokenCount = st.countTokens();
		String [] roomsResult = new String[tokenCount];
		for(int i=0; i < tokenCount; i++) roomsResult[i] = modifyRoomNameForTarget(st.nextToken());
		return roomsResult;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11617.java