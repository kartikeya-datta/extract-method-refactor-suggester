error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9413.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9413.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9413.java
text:
```scala
i@@f (Character.isJavaIdentifierPart(c)) res += c;

package uci.ui;

import java.awt.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import java.beans.*;

public class ToolBar extends JToolBar {


  public ToolBar() {
    setFloatable(false);
  }
  
  /**
   * Add a new JButton which dispatches the action.
   *
   * @param a the Action object to add as a new menu item
   */
  public JButton add(Action a) {
    String name = (String) a.getValue(Action.NAME);
    Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
    return add(a, name, icon);
  }
  
  public JButton add(Action a, String name, String iconResourceStr) {
    Icon icon = loadIconResource(imageName(iconResourceStr), name);
    //System.out.println(icon);
    return add(a, name, icon);
  }
  
  public JButton add(Action a, String name, Icon icon) {
    JButton b = new JButton(icon);
    b.setToolTipText(name);
    b.setEnabled(a.isEnabled());
    b.addActionListener(a);
    add(b);
    PropertyChangeListener actionPropertyChangeListener = 
      createActionChangeListener(b);
    a.addPropertyChangeListener(actionPropertyChangeListener);
    // needs-more-work: should buttons appear stuck down while action executes?
    return b;
  }

  public JToggleButton addToggle(Action a) {
    String name = (String) a.getValue(Action.NAME);
    Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
    return addToggle(a, name, icon);
  }
  
  public JToggleButton addToggle(Action a, String name, String iconResourceStr) {
    Icon icon = loadIconResource(imageName(iconResourceStr), name);
    //System.out.println(icon);
    return addToggle(a, name, icon);
  }
  
  public JToggleButton addToggle(Action a, String name, Icon icon) {
    JToggleButton b = new JToggleButton(icon);
    b.setToolTipText(name);
    b.setEnabled(a.isEnabled());
    b.addActionListener(a);
    add(b);
    PropertyChangeListener actionPropertyChangeListener = 
      createActionToggleListener(b);
    a.addPropertyChangeListener(actionPropertyChangeListener);
    // needs-more-work: should buttons appear stuck down while action executes?
    return b;
  }

  public JToggleButton addToggle(Action a, String name,
				 String upRes, String downRes) {
    ImageIcon upIcon = loadIconResource(imageName(upRes), name);
    ImageIcon downIcon = loadIconResource(imageName(downRes), name);
    JToggleButton b = new JToggleButton(upIcon);
    b.setToolTipText(name);
    b.setEnabled(a.isEnabled());
    b.addActionListener(a);
    b.setPressedIcon(downIcon);
    b.setMargin(new Insets(0,0,0,0));
    add(b);
    PropertyChangeListener actionPropertyChangeListener = 
      createActionToggleListener(b);
    a.addPropertyChangeListener(actionPropertyChangeListener);
    // needs-more-work: should buttons appear stuck down while action executes?
    return b;
  }


  
  public ButtonGroup addRadioGroup(String name1, ImageIcon oneUp,
				      ImageIcon oneDown,
				      String name2, ImageIcon twoUp,
				      ImageIcon twoDown) {
    JRadioButton b1 = new JRadioButton(oneUp, true);
    b1.setSelectedIcon(oneDown);
    b1.setToolTipText(name1);
    b1.setMargin(new Insets(0,0,0,0));
    b1.getAccessibleContext().setAccessibleName(name1);

    JRadioButton b2 = new JRadioButton(twoUp, false);
    b2.setSelectedIcon(twoDown);
    b2.setToolTipText(name2);
    b2.setMargin(new Insets(0,0,0,0));
    b2.getAccessibleContext().setAccessibleName(name2);

    add(b1);
    add(b2);

    //     JPanel p = new JPanel();
    //     p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    //     p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    //     p.add(b1);
    //     p.add(b2);
    //     add(p);
    
    ButtonGroup bg = new ButtonGroup();
    bg.add(b1);
    bg.add(b2);
    return bg;
  }

  protected PropertyChangeListener createActionToggleListener(JToggleButton b) {
	return new ActionToggleChangedListener(b);
    }

  private class ActionToggleChangedListener implements PropertyChangeListener {
        JToggleButton button;
        
        ActionToggleChangedListener(JToggleButton b) {
            super();
            this.button = b;
        }
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(Action.NAME)) {
                String text = (String) e.getNewValue();
                button.setText(text);
                button.repaint();
            } else if (propertyName.equals("enabled")) {
                Boolean enabledState = (Boolean) e.getNewValue();
                button.setEnabled(enabledState.booleanValue());
                button.repaint();
            } else if (e.getPropertyName().equals(Action.SMALL_ICON)) {
                Icon icon = (Icon) e.getNewValue();
                button.setIcon(icon);
                button.invalidate();
                button.repaint();
            } 
        }
    }


  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = ToolBar.class.getResource(imgName);
      //System.out.println(imgName);
      //System.out.println(imgURL);
      return new ImageIcon(imgURL, desc);
    }
    catch (Exception ex) {
      System.out.println("Exception in loadIconResource");
      ex.printStackTrace();
      return new ImageIcon(desc);
    }
  }

  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
  }


  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }

  
} /* end class ToolBar */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9413.java