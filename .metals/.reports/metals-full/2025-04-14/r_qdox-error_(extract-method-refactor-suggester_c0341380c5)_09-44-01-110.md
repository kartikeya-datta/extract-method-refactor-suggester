error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13658.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13658.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13658.java
text:
```scala
private static final L@@ogger logger = LoggingManager.getLoggerForClass();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.gui.action;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.Searchable;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ComponentUtil;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 
 */
public class SearchTreeDialog extends JDialog implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4436834972710248247L;

	private Logger logger = LoggingManager.getLoggerForClass();

    private JButton searchButton;
    
	private JLabeledTextField searchTF;
	
	private JCheckBox isRegexpCB;

	private JCheckBox isCaseSensitiveCB;

	private JButton cancelButton;

	/**
	 * Store last search
	 */
	private transient String lastSearch = null;

	/**
	 * Hide Window on ESC
	 */
	private transient ActionListener enterActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			doSearch(actionEvent);
		}	
	};
	
	/**
	 * Do search on Enter
	 */
	private transient ActionListener escapeActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			setVisible(false);
		}	
	};
	
	public SearchTreeDialog() {
        super((JFrame) null, JMeterUtils.getResString("search_tree_title"), true); //$NON-NLS-1$
        init();
    }

    private void init() {
        this.getContentPane().setLayout(new BorderLayout(10,10));

        searchTF = new JLabeledTextField(JMeterUtils.getResString("search_text_field"), 20); //$NON-NLS-1$
        if(!StringUtils.isEmpty(lastSearch)) {
        	searchTF.setText(lastSearch);
        }
        isRegexpCB = new JCheckBox(JMeterUtils.getResString("search_text_chkbox_regexp"), false); //$NON-NLS-1$
        isCaseSensitiveCB = new JCheckBox(JMeterUtils.getResString("search_text_chkbox_case"), false); //$NON-NLS-1$
        Font font = new Font("SansSerif", Font.PLAIN, 10); // reduce font
        isRegexpCB.setFont(font);
        isCaseSensitiveCB.setFont(font);

        JPanel searchCriterionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchCriterionPanel.add(isCaseSensitiveCB);
        searchCriterionPanel.add(isRegexpCB);
        
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(7, 3, 3, 3));
        searchPanel.add(searchTF, BorderLayout.NORTH);
        searchPanel.add(searchCriterionPanel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        searchButton = new JButton(JMeterUtils.getResString("search")); //$NON-NLS-1$
        searchButton.addActionListener(this);
        cancelButton = new JButton(JMeterUtils.getResString("cancel")); //$NON-NLS-1$
        cancelButton.addActionListener(this);
        buttonsPanel.add(searchButton);
        buttonsPanel.add(cancelButton);
        searchPanel.add(buttonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(searchPanel);
        searchPanel.registerKeyboardAction(enterActionListener, KeyStrokes.ENTER, JComponent.WHEN_IN_FOCUSED_WINDOW);
        searchPanel.registerKeyboardAction(escapeActionListener, KeyStrokes.ESC, JComponent.WHEN_IN_FOCUSED_WINDOW);
    	searchTF.requestFocusInWindow();

        this.pack();
        ComponentUtil.centerComponentInWindow(this);
    }

    /**
     * Do search
     * @param e {@link ActionEvent}
     */
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource()==cancelButton) {
    		this.setVisible(false);
    		return;
    	} 
    	doSearch(e);
    }

	/**
	 * @param e {@link ActionEvent}
	 */
	private void doSearch(ActionEvent e) {
		String wordToSearch = searchTF.getText();
    	if(StringUtils.isEmpty(wordToSearch)) {
            return;
        } else {
        	this.lastSearch = wordToSearch;
        }
    	
    	// reset previous result
    	ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.SEARCH_RESET));
        // do search
    	Searcher searcher = null; 
    	if(isRegexpCB.isSelected()) {
    		searcher = new RegexpSearcher(isCaseSensitiveCB.isSelected(), searchTF.getText());
    	} else {
    		searcher = new RawTextSearcher(isCaseSensitiveCB.isSelected(), searchTF.getText());
    	}
        GuiPackage guiPackage = GuiPackage.getInstance();
        JMeterTreeModel jMeterTreeModel = guiPackage.getTreeModel();
        Set<JMeterTreeNode> nodes = new HashSet<JMeterTreeNode>();
        for (JMeterTreeNode jMeterTreeNode : jMeterTreeModel.getNodesOfType(Searchable.class)) {
            try {
                if (jMeterTreeNode.getUserObject() instanceof Searchable){
                    Searchable searchable = (Searchable) jMeterTreeNode.getUserObject();
                    List<JMeterTreeNode> matchingNodes = jMeterTreeNode.getPathToThreadGroup();
                    List<String> searchableTokens = searchable.getSearchableTokens();
                    boolean result = searcher.search(searchableTokens);
                    if(result) {
                        nodes.addAll(matchingNodes);
                    }
                }
            } catch (Exception ex) {
                logger.error("Error occured searching for word:"+ wordToSearch, ex);
            }
        }
        for (Iterator<JMeterTreeNode> iterator = nodes.iterator(); iterator.hasNext();) {
            JMeterTreeNode jMeterTreeNode = iterator.next();
            jMeterTreeNode.setMarkedBySearch(true);
        }
        GuiPackage.getInstance().getMainFrame().repaint();
        this.setVisible(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13658.java