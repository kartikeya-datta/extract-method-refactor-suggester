error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15585.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15585.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15585.java
text:
```scala
r@@equestFocusInWindow();

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

package org.apache.jmeter.gui.util;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextField;

/**
 * This is Date mask control. Using this control we can pop up our date in the
 * text field. And this control is Devloped basically for JDK1.3 and lower
 * version support. This control is similer to JSpinner control this is
 * available in JDK1.4 and above only.
 * <p>
 * This will set the date "yyyy/MM/dd HH:mm:ss" in this format only.
 * </p>
 *
 */
public class JDateField extends JTextField {

    private static final long serialVersionUID = 240L;

    // Datefields are not thread-safe
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // $NON-NLS-1$

    /*
     * The following array must agree with dateFormat
     *
     * It is used to translate the positions in the buffer to the values used by
     * the Calendar class for the field id.
     *
     * Current format: MM/DD/YYYY HH:MM:SS 01234567890123456789 ^buffer
     * positions
     */
    private static final int fieldPositions[] = {
            Calendar.YEAR, // Y
            Calendar.YEAR, // Y
            Calendar.YEAR, // Y
            Calendar.YEAR, // Y
            Calendar.YEAR, // sp
            Calendar.MONTH, // M
            Calendar.MONTH, // M
            Calendar.MONTH, // /
            Calendar.DAY_OF_MONTH, // D
            Calendar.DAY_OF_MONTH, // D
            Calendar.DAY_OF_MONTH, // /
            Calendar.HOUR_OF_DAY, // H
            Calendar.HOUR_OF_DAY, // H
            Calendar.HOUR_OF_DAY, // :
            Calendar.MINUTE, // M
            Calendar.MINUTE, // M
            Calendar.MINUTE, // :
            Calendar.SECOND, // S
            Calendar.SECOND, // S
            Calendar.SECOND // end
    };

    /**
     * Create a DateField with the specified date.
     */
    public JDateField(Date date) {
        super(20);
        this.addKeyListener(new KeyFocus());
        this.addFocusListener(new FocusClass());
        String myString = dateFormat.format(date);
        setText(myString);
    }

    // Dummy constructor to allo JUnit tests to work
    public JDateField() {
        this(new Date());
    }

    /**
     * Set the date to the Date mask control.
     */
    public void setDate(Date date) {
        setText(dateFormat.format(date));
    }

    /**
     * Get the date from the Date mask control.
     */
    public Date getDate() {
        try {
            return dateFormat.parse(getText());
        } catch (ParseException e) {
            return new Date();
        } catch (Exception e) {
            // DateFormat.parse has some bugs (up to JDK 1.4.2) by which it
            // throws unchecked exceptions. E.g. see:
            // http://developer.java.sun.com/developer/bugParade/bugs/4699765.html
            //
            // To avoid problems with such situations, we'll catch all
            // exceptions here and act just as for ParseException above:
            return new Date();
        }
    }

    /*
     * Convert position in buffer to Calendar type Assumes that pos >=0 (which
     * is true for getCaretPosition())
     */
    private static int posToField(int pos) {
        if (pos >= fieldPositions.length) { // if beyond the end
            pos = fieldPositions.length - 1; // then set to the end
        }
        return fieldPositions[pos];
    }

    /**
     * Converts a date/time to a calendar using the defined format
     */
    private Calendar parseDate(String datetime) {
        Calendar c = Calendar.getInstance();
        try {
            Date dat = dateFormat.parse(datetime);
            c.setTime(dat);
        } catch (ParseException e) {
            // Do nothing; the current time will be returned
        }
        return c;
    }

    /*
     * Update the current field. The addend is only expected to be +1/-1, but
     * other values will work. N.B. the roll() method only supports changes by a
     * single unit - up or down
     */
    private void update(int addend, boolean shifted) {
        Calendar c = parseDate(getText());
        int pos = getCaretPosition();
        int field = posToField(pos);
        if (shifted) {
            c.roll(field, true);
        } else {
            c.add(field, addend);
        }
        String newDate = dateFormat.format(c.getTime());
        setText(newDate);
        if (pos > newDate.length()) {
            pos = newDate.length();
        }
        setCaretPosition(pos);// Restore position

    }

    class KeyFocus extends KeyAdapter {
        KeyFocus() {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                update(1, e.isShiftDown());
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                update(-1, e.isShiftDown());
            }
        }
    }

    class FocusClass implements FocusListener {
        FocusClass() {
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            try {
                dateFormat.parse(getText());
            } catch (ParseException e1) {
                requestFocus();
            }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15585.java