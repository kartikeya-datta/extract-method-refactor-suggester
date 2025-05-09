error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3705.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3705.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3705.java
text:
```scala
private static final C@@olor HILIT_COLOR = Color.LIGHT_GRAY;

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
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.visualizers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class SearchTextExtension implements ActionListener, DocumentListener {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String SEARCH_TEXT_COMMAND = "search_text"; // $NON-NLS-1$

    private static volatile int LAST_POSITION_DEFAULT = 0;

    private int lastPosition = LAST_POSITION_DEFAULT;

    private final static Color HILIT_COLOR = Color.LIGHT_GRAY;

    private Highlighter selection;

    private Highlighter.HighlightPainter painter;

    private JLabel label;

    private JButton findButton;

    private JTextField textToFindField;

    private  JCheckBox caseChkBox;

    private JCheckBox regexpChkBox;

    private String lastTextTofind;

    private boolean newSearch = false;

    private JEditorPane results;

    private JPanel searchPanel;


    public void init(JPanel resultsPane) {
    }

    public void setResults(JEditorPane results) {
        if (this.results != null) {
            newSearch = true;
            resetTextToFind();
        }
        this.results = results;
        // prepare highlighter to show text find with search command
        selection = new DefaultHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
        results.setHighlighter(selection);
    }

    /**
     * Launch find text engine on response text
     */
    private void executeAndShowTextFind() {
        String textToFind = textToFindField.getText();
        if (results != null && results.getText().length() > 0
                && textToFind.length() > 0) {

            // new search?
            if (lastTextTofind != null && !lastTextTofind.equals(textToFind)) {
                lastPosition = LAST_POSITION_DEFAULT;
            }

            if (log.isDebugEnabled()) {
                log.debug("lastPosition=" + lastPosition);
            }
            Matcher matcher = null;
            try {
                Pattern pattern = createPattern(textToFind);
                Document contentDoc = results.getDocument();
                String body = contentDoc.getText(lastPosition,
                        (contentDoc.getLength() - lastPosition));
                matcher = pattern.matcher(body);

                if ((matcher != null) && (matcher.find())) {
                    selection.removeAllHighlights();
                    selection.addHighlight(lastPosition + matcher.start(),
                            lastPosition + matcher.end(), painter);
                    results.setCaretPosition(lastPosition + matcher.end());

                    // save search position
                    lastPosition = lastPosition + matcher.end();
                    findButton.setText(JMeterUtils
                            .getResString("search_text_button_next"));// $NON-NLS-1$
                    lastTextTofind = textToFind;
                    newSearch = true;
                } else {
                    // Display not found message and reset search
                    JOptionPane.showMessageDialog(null, JMeterUtils
                            .getResString("search_text_msg_not_found"),// $NON-NLS-1$
                            JMeterUtils.getResString("search_text_title_not_found"), // $NON-NLS-1$
                            JOptionPane.INFORMATION_MESSAGE);
                    lastPosition = LAST_POSITION_DEFAULT;
                    findButton.setText(JMeterUtils
                            .getResString("search_text_button_find"));// $NON-NLS-1$
                    results.setCaretPosition(0);
                }
            } catch (BadLocationException ble) {
                log.error("Location exception in text find", ble);// $NON-NLS-1$
            }
        }
    }

    /**
     * Create the text find task pane
     *
     * @return Text find task pane
     */
    private JPanel createSearchTextPanel() {
        Font font = new Font("SansSerif", Font.PLAIN, 10);

        // Search field
        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        label = new JLabel(JMeterUtils.getResString("search_text_field")); // $NON-NLS-1$
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        searchPanel.add(label);
        textToFindField = new JTextField(); // $NON-NLS-1$
        searchPanel.add(textToFindField);
        searchPanel.add(Box.createRigidArea(new Dimension(5,0)));

        // add listener to intercept texttofind changes and reset search
        textToFindField.getDocument().addDocumentListener(this);

        // Buttons
        findButton = new JButton(JMeterUtils
                .getResString("search_text_button_find")); // $NON-NLS-1$
        findButton.setFont(font);
        findButton.setActionCommand(SEARCH_TEXT_COMMAND);
        findButton.addActionListener(this);
        searchPanel.add(findButton);

        // checkboxes
        caseChkBox = new JCheckBox(JMeterUtils
                .getResString("search_text_chkbox_case"), false); // $NON-NLS-1$
        caseChkBox.setFont(font);
        searchPanel.add(caseChkBox);
        regexpChkBox = new JCheckBox(JMeterUtils
                .getResString("search_text_chkbox_regexp"), false); // $NON-NLS-1$
        regexpChkBox.setFont(font);
        searchPanel.add(regexpChkBox);

        // when Enter is pressed, search start
        InputMap im = textToFindField
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke("ENTER"), SEARCH_TEXT_COMMAND);
        ActionMap am = textToFindField.getActionMap();
        am.put(SEARCH_TEXT_COMMAND, new EnterAction());

        // default not visible
        searchPanel.setVisible(true);
        return searchPanel;
    }

    JPanel createSearchTextExtensionPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(createSearchTextPanel());
        return pane;
    }

    /**
     * Display the response as text or as rendered HTML. Change the text on the
     * button appropriate to the current display.
     *
     * @param e
     *            the ActionEvent being processed
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Search text in response data
        if (SEARCH_TEXT_COMMAND.equals(command)) {
            executeAndShowTextFind();
        }
    }

    private class EnterAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent ev) {
            executeAndShowTextFind();
        }
    }

    // DocumentListener method
    public void changedUpdate(DocumentEvent e) {
        // do nothing
    }

    // DocumentListener method
    public void insertUpdate(DocumentEvent e) {
        resetTextToFind();
    }

    // DocumentListener method
    public void removeUpdate(DocumentEvent e) {
        resetTextToFind();
    }

    void resetTextToFind() {
        if (newSearch) {
            log.debug("reset pass");
            // Reset search
            lastPosition = LAST_POSITION_DEFAULT;
            lastTextTofind = null;
            findButton.setText(JMeterUtils
                    .getResString("search_text_button_find"));// $NON-NLS-1$
            selection.removeAllHighlights();
            results.setCaretPosition(0);
            newSearch = false;
        }
    }

    private Pattern createPattern(String textToFind) {
        // desactivate or not specials regexp char
        String textToFindQ = Pattern.quote(textToFind);
        if (regexpChkBox.isSelected()) {
            textToFindQ = textToFind;
        }
        Pattern pattern = null;
        if (caseChkBox.isSelected()) {
            pattern = Pattern.compile(textToFindQ);
        } else {
            pattern = Pattern.compile(textToFindQ, Pattern.CASE_INSENSITIVE);
        }
        return pattern;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3705.java