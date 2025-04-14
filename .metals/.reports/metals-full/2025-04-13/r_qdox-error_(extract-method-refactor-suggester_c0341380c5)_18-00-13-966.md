error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4003.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4003.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4003.java
text:
```scala
d@@ouble zoomPercentage = _slider.getValue() / 100d;

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.swingext.PopupButton;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

/**
 * A button that can be used to change the zoom magnification of the current
 * diagram. When the user presses the button, a popup is displayed which
 * contains a vertical slider representing the range of zoom magnifications.
 * Dragging the slider changes the zoom magnification for the current diagram.
 * 
 * @author Jeremy Jones
 */
public class ZoomSliderButton extends PopupButton {
    
    /**
     * Used for loading the zoom icon from the Zoom Reset action.
     */
    private static final String RESOURCE_NAME = "Zoom Reset";
    
    /**
     * The localization bundle.
     */
    private static final String BUNDLE = "Cognitive";
    
    /**
     * Font used for the slider tick labels and for the current magnification
     * value label.
     */
    private static final Font   LABEL_FONT = new Font("Dialog", Font.PLAIN, 10);
    
    /**
     * The minimum zoom magnification slider value.
     */
    private static final int    MINIMUM_ZOOM = 0;

    /**
     * The maximum zoom magnification slider value.
     */
    private static final int    MAXIMUM_ZOOM = 500;
    
    /**
     * The preferred height of the slider component.
     */    
    private static final int    SLIDER_HEIGHT = 250;
    
    /**
     * The slider component.
     */
    private JSlider             _slider = null;
    
    /**
     * The text field which shows the current zoom magnification value.
     */
    private JTextField          _currentValue = null;

    /**
     * Constructs a new ZoomSliderButton.
     */
    public ZoomSliderButton() {
        super();

        Icon icon = ResourceLoaderWrapper.getResourceLoaderWrapper().
            lookupIconResource(Translator.getImageBinding(RESOURCE_NAME),
            Translator.localize(RESOURCE_NAME));
                
        setIcon(icon);
        setToolTipText(Argo.localize(BUNDLE, "button.zoom"));
    }

    /**
     * Creates the slider popup component.
     */
    private void createPopupComponent() {
        _slider = new JSlider(
            JSlider.VERTICAL, 
            MINIMUM_ZOOM, 
            MAXIMUM_ZOOM, 
            MINIMUM_ZOOM);
        _slider.setInverted(true);
        _slider.setMajorTickSpacing(MAXIMUM_ZOOM / 10);
        _slider.setMinorTickSpacing(MAXIMUM_ZOOM / 20);
        _slider.setPaintTicks(true);
        _slider.setPaintTrack(true);
        int sliderBaseWidth = _slider.getPreferredSize().width;
        _slider.setPaintLabels(true);
        
        for (Enumeration enum = _slider.getLabelTable().elements(); 
            enum.hasMoreElements();) {
            ((Component) enum.nextElement()).setFont(LABEL_FONT);
        }
        
        _slider.setToolTipText(Argo.localize(BUNDLE, 
            "button.zoom.slider-tooltip"));   
        
        _slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                handleSliderValueChange();
            }            
        });
        
        int labelWidth = _slider.getFontMetrics(LABEL_FONT).stringWidth(
            String.valueOf(MAXIMUM_ZOOM)) + 6;
        
        _slider.setPreferredSize(new Dimension(
            sliderBaseWidth + labelWidth, SLIDER_HEIGHT));
        
        _currentValue = new JTextField(5);
        _currentValue.setHorizontalAlignment(JLabel.CENTER);
        _currentValue.setFont(LABEL_FONT);
        _currentValue.setToolTipText(Argo.localize(
            BUNDLE, "button.zoom.current-zoom-magnification"));
        updateCurrentValueLabel();
        _currentValue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleTextEntry();
            }
        });
        _currentValue.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                handleTextEntry();
            }
        });
        
        JPanel currentValuePanel = new JPanel(new FlowLayout(
            FlowLayout.CENTER, 0, 0));
        currentValuePanel.add(_currentValue);
        
        JPanel zoomPanel = new JPanel(new BorderLayout(0, 0));
        zoomPanel.add(_slider, BorderLayout.CENTER);
        zoomPanel.add(currentValuePanel, BorderLayout.NORTH);
                
        setPopupComponent(zoomPanel);
    }
    
    /**
     * Update the slider value every time the popup is shown.
     */
    protected void showPopup() {
        if (_slider == null) {
            createPopupComponent();
        }
        
        Editor ed = Globals.curEditor();
        if (ed != null) {
            _slider.setValue((int) (ed.getScale() * 100d));
        } 
        
        super.showPopup();
    }

    /**
     * Called when the slider value changes.
     */
    private void handleSliderValueChange() {
        updateCurrentValueLabel();
                
        //if (!source.getValueIsAdjusting()) {
        double zoomPercentage = (double) _slider.getValue() / 100d;
                
        Editor ed = Globals.curEditor();
        if (ed == null || zoomPercentage <= 0.0) {
            return;
        } 
                
        if (zoomPercentage != ed.getScale()) {
            ed.setScale(zoomPercentage);
            ed.damageAll();
        }
        //}
    }

    /**
     * Called when the text field value changes.
     */
    private void handleTextEntry() {
        String value = _currentValue.getText();
        if (value.endsWith("%")) {
            value = value.substring(0, value.length() - 1);
        }
        try {
            int newZoom = Integer.parseInt(value);
            if (newZoom < MINIMUM_ZOOM || newZoom > MAXIMUM_ZOOM) {
                throw new NumberFormatException();
            }
            _slider.setValue(newZoom);
        }
        catch (NumberFormatException ex) {
            updateCurrentValueLabel();
        }
    }

    /**
     * Sets the current value label's text to the current slider value.
     */
    private void updateCurrentValueLabel() {
        _currentValue.setText(String.valueOf(_slider.getValue()) + '%');
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4003.java