error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/311.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/311.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/311.java
text:
```scala
.@@showView("org.eclipse.ecf.ui.view.rosterview");

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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.presence.IAccountManager;
import org.eclipse.ecf.presence.IIMMessageEvent;
import org.eclipse.ecf.presence.IIMMessageListener;
import org.eclipse.ecf.presence.IPresence;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.IPresenceListener;
import org.eclipse.ecf.presence.IPresenceSender;
import org.eclipse.ecf.presence.Presence;
import org.eclipse.ecf.presence.im.IChatManager;
import org.eclipse.ecf.presence.im.IChatMessageEvent;
import org.eclipse.ecf.presence.im.IChatMessageSender;
import org.eclipse.ecf.presence.im.ITypingMessageEvent;
import org.eclipse.ecf.presence.im.ITypingMessageSender;
import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.eclipse.ecf.presence.roster.IRosterSubscriptionListener;
import org.eclipse.ecf.presence.roster.IRosterSubscriptionSender;
import org.eclipse.ecf.ui.dialogs.ReceiveAuthorizeRequestDialog;
import org.eclipse.ecf.ui.views.ILocalInputHandler;
import org.eclipse.ecf.ui.views.RosterView;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class PresenceContainerUI {

	protected static final int SEND_ERRORCODE = 2001;

	protected RosterView rosterView = null;

	protected IPresenceSender presenceSender = null;

	protected IAccountManager accountManager = null;

	protected IRosterSubscriptionSender rosterSubscriptionSender = null;

	protected IPresenceContainerAdapter pc = null;

	protected ISharedObjectContainer soContainer = null;

	protected org.eclipse.ecf.core.user.User localUser = null;

	protected ID groupID = null;

	protected IContainer container;

	protected IChatManager chatManager;

	protected IChatMessageSender chatMessageSender;

	protected ITypingMessageSender typingMessageSender;

	public PresenceContainerUI(IPresenceContainerAdapter pc) {
		this.pc = pc;
		this.presenceSender = pc.getRosterManager().getPresenceSender();
		this.rosterSubscriptionSender = pc.getRosterManager()
				.getRosterSubscriptionSender();
		this.accountManager = pc.getAccountManager();
		this.chatManager = pc.getChatManager();
		this.chatMessageSender = this.chatManager.getChatMessageSender();
		this.typingMessageSender = this.chatManager.getTypingMessageSender();
	}

	protected void setup(final IContainer container, final ID localUser,
			final String nick) {
		this.container = container;
		this.soContainer = (ISharedObjectContainer) this.container
				.getAdapter(ISharedObjectContainer.class);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					IWorkbenchWindow ww = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();
					IWorkbenchPage wp = ww.getActivePage();

					IViewPart view = wp
							.showView("org.eclipse.ecf.example.collab.ui.CollabRosterView");
					rosterView = (RosterView) view;

					// MultiRosterViewTesting
					// MultiRosterView mrv = (MultiRosterView)
					// wp.showView("org.eclipse.ecf.presence.ui.MultiRosterView");
					// mrv.addContainer(PresenceUI.this.container);

					String nickname = null;
					if (nick != null) {
						nickname = nick;
					} else {
						String name = localUser.getName();
						nickname = name.substring(0, name.indexOf("@"));
					}
					PresenceContainerUI.this.localUser = new org.eclipse.ecf.core.user.User(
							localUser, nickname);

				} catch (Exception e) {
					ClientPlugin.getDefault().getLog().log(
							new Status(IStatus.ERROR, ClientPlugin.PLUGIN_ID,
									SEND_ERRORCODE,
									"Exception showing presence view", e));
				}
			}
		});

		chatManager.addMessageListener(new IIMMessageListener() {
			public void handleMessageEvent(
					final IIMMessageEvent chatMessageEvent) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						if (chatMessageEvent instanceof IChatMessageEvent) {
							rosterView.handleMessageEvent(chatMessageEvent);
						} else if (chatMessageEvent instanceof ITypingMessageEvent) {
							rosterView.handleTyping(chatMessageEvent
									.getFromID());
						}
					}
				});
			}
		});

		container.addListener(new IContainerListener() {
			public void handleEvent(IContainerEvent event) {
				if (event instanceof IContainerConnectedEvent) {
					IContainerConnectedEvent cce = (IContainerConnectedEvent) event;
					final ID joinedContainer = cce.getTargetID();
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							ILocalInputHandler handler = new ILocalInputHandler() {
								public void inputText(ID userID, String text) {
									try {
										if (chatMessageSender != null)
											chatMessageSender.sendChatMessage(
													userID, text);
									} catch (ECFException e) {
										ClientPlugin
												.getDefault()
												.getLog()
												.log(
														new Status(
																IStatus.ERROR,
																ClientPlugin
																		.getDefault()
																		.getBundle()
																		.getSymbolicName(),
																SEND_ERRORCODE,
																"Error in sendMessage",
																e));
									}
								}

								public void startTyping(ID userID) {
									try {
										if (typingMessageSender != null)
											typingMessageSender
													.sendTypingMessage(userID,
															true, "");
									} catch (ECFException e) {
										ClientPlugin
												.getDefault()
												.getLog()
												.log(
														new Status(
																IStatus.ERROR,
																ClientPlugin
																		.getDefault()
																		.getBundle()
																		.getSymbolicName(),
																SEND_ERRORCODE,
																"Error in startTyping",
																e));
									}
								}

								public void disconnect() {
									container.disconnect();
								}

								public void updatePresence(ID userID,
										IPresence presence) {
									try {
										if (presenceSender != null)
											presenceSender.sendPresenceUpdate(
													userID, presence);
									} catch (ECFException e) {
										ClientPlugin
												.getDefault()
												.getLog()
												.log(
														new Status(
																IStatus.ERROR,
																ClientPlugin
																		.getDefault()
																		.getBundle()
																		.getSymbolicName(),
																SEND_ERRORCODE,
																"Error in sendPresenceUpdate",
																e));
									}
								}

								public void sendRosterAdd(String user,
										String name, String[] groups) {
									// Send roster add
									try {
										rosterSubscriptionSender.sendRosterAdd(
												user, name, groups);
									} catch (ECFException e) {
										ClientPlugin
												.getDefault()
												.getLog()
												.log(
														new Status(
																IStatus.ERROR,
																ClientPlugin
																		.getDefault()
																		.getBundle()
																		.getSymbolicName(),
																SEND_ERRORCODE,
																"Error in sendRosterAdd",
																e));
									}
								}

								public void sendRosterRemove(ID userID) {
									try {
										if (rosterSubscriptionSender != null)
											rosterSubscriptionSender
													.sendRosterRemove(userID);
									} catch (ECFException e) {
										ClientPlugin
												.getDefault()
												.getLog()
												.log(
														new Status(
																IStatus.ERROR,
																ClientPlugin
																		.getDefault()
																		.getBundle()
																		.getSymbolicName(),
																SEND_ERRORCODE,
																"Error in sendRosterRemove",
																e));
									}
								}
							};
							PresenceContainerUI.this.groupID = joinedContainer;
							rosterView.addAccount(joinedContainer,
									PresenceContainerUI.this.localUser,
									handler, container, pc, soContainer);
						}
					});

				} else if (event instanceof IContainerDisconnectedEvent) {
					final IContainerDisconnectedEvent de = (IContainerDisconnectedEvent) event;
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							rosterView.accountDisconnected(de.getTargetID());
						}
					});
				}
			}
		});

		pc.getRosterManager().addPresenceListener(new IPresenceListener() {

			public void handleRosterEntryAdd(final IRosterEntry entry) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						rosterView.handleRosterEntryAdd(
								PresenceContainerUI.this.groupID, entry);
					}
				});
			}

			public void handlePresence(final ID fromID, final IPresence presence) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						rosterView.handlePresence(
								PresenceContainerUI.this.groupID, fromID,
								presence);
					}
				});
			}

			public void handleRosterEntryUpdate(final IRosterEntry entry) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						rosterView.handleRosterEntryUpdate(
								PresenceContainerUI.this.groupID, entry);
					}
				});
			}

			public void handleRosterEntryRemove(final IRosterEntry entry) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						rosterView.handleRosterEntryRemove(
								PresenceContainerUI.this.groupID, entry);
					}
				});
			}

		});

		pc.getRosterManager().addRosterSubscriptionListener(
				new IRosterSubscriptionListener() {

					public void handleSubscribeRequest(final ID fromID) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								try {
									IWorkbenchWindow ww = PlatformUI
											.getWorkbench()
											.getActiveWorkbenchWindow();
									ReceiveAuthorizeRequestDialog authRequest = new ReceiveAuthorizeRequestDialog(
											ww.getShell(), fromID.getName(),
											localUser.getName());
									authRequest.setBlockOnOpen(true);
									authRequest.open();
									int res = authRequest.getButtonPressed();
									if (res == ReceiveAuthorizeRequestDialog.AUTHORIZE_AND_ADD) {
										if (presenceSender != null) {
											presenceSender
													.sendPresenceUpdate(
															fromID,
															new Presence(
																	IPresence.Type.SUBSCRIBED));
											if (rosterView != null)
												rosterView.sendRosterAdd(
														localUser, fromID
																.getName(),
														null);
										}
									} else if (res == ReceiveAuthorizeRequestDialog.AUTHORIZE_ID) {
										if (presenceSender != null) {
											presenceSender
													.sendPresenceUpdate(
															fromID,
															new Presence(
																	IPresence.Type.SUBSCRIBED));
										}
									} else if (res == ReceiveAuthorizeRequestDialog.REFUSE_ID) {
										// do nothing
									} else {
										// do nothing
									}
								} catch (Exception e) {
									ClientPlugin
											.getDefault()
											.getLog()
											.log(
													new Status(
															IStatus.ERROR,
															ClientPlugin.PLUGIN_ID,
															SEND_ERRORCODE,
															"Exception showing authorization dialog",
															e));
								}
							}
						});
					}

					public void handleSubscribed(ID fromID) {
						// System.out.println("subscribed from "+fromID);
					}

					public void handleUnsubscribed(ID fromID) {
						// System.out.println("unsubscribed from "+fromID);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/311.java