error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8858.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8858.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8858.java
text:
```scala
public A@@ccountWizard(boolean b) throws Exception{

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
//$Log: AccountWizard.java,v $
package org.columba.mail.gui.config.accountwizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.columba.core.gui.util.ImageLoader;
import org.columba.core.gui.util.wizard.DefaultWizardDialog;
import org.columba.core.gui.util.wizard.DefaultWizardPanel;
import org.columba.core.gui.util.wizard.WizardPanelSequence;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.IdentityItem;
import org.columba.mail.config.ImapItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.config.PopItem;
import org.columba.mail.config.SmtpItem;
import org.columba.mail.folder.imap.IMAPRootFolder;
import org.columba.mail.gui.config.account.AccountDialog;
import org.columba.mail.util.MailResourceLoader;

public class AccountWizard
	extends DefaultWizardDialog
	implements ActionListener {
	private WelcomePanel welcomePanel;
	private IdentityPanel identityPanel;
	private OutgoingServerPanel outgoingServerPanel;
	private IncomingServerPanel incomingServerPanel;
	private AdvancedPanel advancedPanel;
	private FinishPanel finishPanel;

	protected AccountItem item;

	private boolean add;

	protected WizardPanelSequence sequence;

	public AccountWizard(boolean b) {
		super();
		add = b;

		DefaultWizardPanel p = getSequence().getFirstPanel();
		init(p);

		updateWindow(p);
	}

	public WizardPanelSequence getSequence() {
		if (sequence == null) {
			sequence = new WizardPanelSequence();

			if (add == false) {
					welcomePanel = new WelcomePanel(dialog, this, MailResourceLoader.getString("dialog", "accountwizard", "welcome"), //$NON-NLS-1$
		MailResourceLoader.getString("dialog", "accountwizard", "initial_welcome_screen"), //$NON-NLS-1$
	ImageLoader.getSmallImageIcon("stock_preferences.png"));

				sequence.addPanel(welcomePanel);
			}
				identityPanel = new IdentityPanel(dialog, this, MailResourceLoader.getString("dialog", "accountwizard", "account_wizard"), //$NON-NLS-1$
		MailResourceLoader.getString("dialog", "accountwizard", "specify_your_identity_information"), //$NON-NLS-1$
	ImageLoader.getSmallImageIcon("stock_preferences.png"));
			sequence.addPanel(identityPanel);

				incomingServerPanel = new IncomingServerPanel(dialog, this, MailResourceLoader.getString("dialog", "accountwizard", "account_wizard"), //$NON-NLS-1$
		MailResourceLoader.getString("dialog", "accountwizard", "incoming_server_properties"), //$NON-NLS-1$
	ImageLoader.getSmallImageIcon("stock_preferences.png"));
			sequence.addPanel(incomingServerPanel);
				outgoingServerPanel = new OutgoingServerPanel(dialog, this, MailResourceLoader.getString("dialog", "accountwizard", "account_wizard"), //$NON-NLS-1$
		MailResourceLoader.getString("dialog", "accountwizard", "outgoing_server_properties"), //$NON-NLS-1$
	ImageLoader.getSmallImageIcon("stock_preferences.png"), true);
			sequence.addPanel(outgoingServerPanel);
				advancedPanel = new AdvancedPanel(dialog, this, MailResourceLoader.getString("dialog", "accountwizard", "account_wizard"), //$NON-NLS-1$
		MailResourceLoader.getString("dialog", "accountwizard", "advanced_options"), //$NON-NLS-1$
	ImageLoader.getSmallImageIcon("stock_preferences.png"));
			sequence.addPanel(advancedPanel);

			if (add == false) {
					finishPanel = new FinishPanel(dialog, this, MailResourceLoader.getString("dialog", "accountwizard", "account_wizard"), //$NON-NLS-1$
		MailResourceLoader.getString("dialog", "accountwizard", "finished_information_gathering"), //$NON-NLS-1$
	ImageLoader.getSmallImageIcon("stock_preferences.png"));

				sequence.addPanel(finishPanel);
			}
		}

		return sequence;
	}

	public void actionPerformed(ActionEvent e) {

		String action = e.getActionCommand();

		ColumbaLogger.log.info("action=" + action);

		if (action.equals("ACCOUNT")) {
			dialog.setVisible(false);

			finish();
			AccountDialog dialog = new AccountDialog(item);
		}

	}

	public void finish() {

		if (incomingServerPanel.isPopAccount())
			item = MailConfig.getAccountList().addEmptyAccount("pop3");
		else
			item = MailConfig.getAccountList().addEmptyAccount("imap");

		//System.out.println("name: "+ identityDialog.getAccountName() );

		IdentityItem identity = item.getIdentityItem();

		identity.set("name", identityPanel.getName());
		identity.set("address", identityPanel.getAddress());

		String accountname = advancedPanel.getAccountName();
		item.setName(accountname);

		if (incomingServerPanel.isPopAccount()) {
			PopItem pop = item.getPopItem();

			pop.set("host", incomingServerPanel.getHost());
			pop.set("user", incomingServerPanel.getLogin());

			MainInterface.popServerCollection.add(item);

			

		} else {
			ImapItem imap = item.getImapItem();

			imap.set("host", incomingServerPanel.getHost());
			imap.set("user", incomingServerPanel.getLogin());

			IMAPRootFolder parentFolder = new IMAPRootFolder(item);

			MainInterface.treeModel.nodeStructureChanged(
				parentFolder.getParent());

			try {
				parentFolder.addFolder("INBOX");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		SmtpItem smtp = item.getSmtpItem();

		smtp.set("host", outgoingServerPanel.getHost());

		//return item;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8858.java