error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4460.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4460.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4460.java
text:
```scala
t@@ypeComboBox.addItem("IMAP");

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

package org.columba.mail.gui.config.accountwizard;

import java.lang.reflect.Method;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.javaprog.ui.wizard.AbstractStep;
import net.javaprog.ui.wizard.DataModel;
import net.javaprog.ui.wizard.DefaultDataLookup;

import org.columba.core.gui.util.MultiLineLabel;
import org.columba.core.gui.util.wizard.WizardTextField;
import org.columba.mail.util.MailResourceLoader;

class IncomingServerStep extends AbstractStep {
        protected DataModel data;
	protected JTextField loginTextField;
	protected JTextField hostTextField;
	protected JLabel addressLabel;
	protected JComboBox typeComboBox;

	public IncomingServerStep(DataModel data) {
		super(MailResourceLoader.getString(
                                    "dialog",
                                    "accountwizard",
                                    "incomingserver"),
                      MailResourceLoader.getString(
                                    "dialog",
                                    "accountwizard",
                                    "incomingserver_description"));
                this.data = data;
                setCanGoNext(false);
        }
		
        protected JComponent createComponent() {
		JComponent component = new JPanel();
                component.setLayout(new BoxLayout(component, BoxLayout.Y_AXIS));
                component.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
                component.add(new MultiLineLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "accountwizard",
                                    "incomingserver_text")));
                component.add(Box.createVerticalStrut(40));
                WizardTextField middlePanel = new WizardTextField();
                JLabel nameLabel = new JLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "accountwizard",
                                    "login")); //$NON-NLS-1$
                nameLabel.setDisplayedMnemonic(MailResourceLoader.getMnemonic(
                                    "dialog",
                                    "accountwizard",
                                    "login")); //$NON-NLS-1$
                middlePanel.addLabel(nameLabel);
                loginTextField = new JTextField();
                Method method = null;
                try {
                        method = loginTextField.getClass().getMethod("getText", null);
                } catch (NoSuchMethodException nsme) {}
                data.registerDataLookup("IncomingServer.login", new DefaultDataLookup(loginTextField, method, null));
                DocumentListener fieldListener = new DocumentListener() {
                        public void removeUpdate(DocumentEvent e) {
                                checkFields();
                        }
                        
                        public void insertUpdate(DocumentEvent e) {
                                checkFields();
                        }
                        
                        protected void checkFields() {
                                setCanGoNext(loginTextField.getDocument().getLength() > 0
                                           && hostTextField.getDocument().getLength() > 0);
                        }
                        
                        public void changedUpdate(DocumentEvent e) {}
                };
                loginTextField.getDocument().addDocumentListener(fieldListener);
                nameLabel.setLabelFor(loginTextField);
                middlePanel.addTextField(loginTextField);
                JLabel exampleLabel = new JLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "accountwizard",
                                    "example") + "billgates");
                middlePanel.addExample(exampleLabel);
                addressLabel = new JLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "accountwizard",
                                    "host")); //$NON-NLS-1$
                addressLabel.setDisplayedMnemonic(MailResourceLoader.getMnemonic(
                                    "dialog",
                                    "accountwizard",
                                    "host")); //$NON-NLS-1$)
                middlePanel.addLabel(addressLabel);
                hostTextField = new JTextField();
                data.registerDataLookup("IncomingServer.host", new DefaultDataLookup(hostTextField, method, null));
                hostTextField.getDocument().addDocumentListener(fieldListener);
                addressLabel.setLabelFor(hostTextField);
                middlePanel.addTextField(hostTextField);
                JLabel addressExampleLabel = new JLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "accountwizard",
                                    "example") + "mail.microsoft.com");
                middlePanel.addExample(addressExampleLabel);

                JLabel typeLabel = new JLabel(MailResourceLoader.getString(
                                    "dialog",
                                    "accountwizard",
                                    "type")); //$NON-NLS-1$
                typeLabel.setDisplayedMnemonic(MailResourceLoader.getMnemonic(
                                    "dialog",
                                    "accountwizard",
                                    "type"));
                middlePanel.addLabel(typeLabel);
                typeComboBox = new JComboBox();
                typeLabel.setLabelFor(typeComboBox);
                typeComboBox.addItem("POP3");
                typeComboBox.addItem("IMAP4");
                try {
                        method = typeComboBox.getClass().getMethod("getSelectedItem", null);
                } catch (NoSuchMethodException nsme) {}
                data.registerDataLookup("IncomingServer.type", new DefaultDataLookup(typeComboBox, method, null));
                middlePanel.addTextField(typeComboBox);
                middlePanel.addEmptyExample();
                component.add(middlePanel);
                return component;
        }
        
        public void prepareRendering() {}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4460.java