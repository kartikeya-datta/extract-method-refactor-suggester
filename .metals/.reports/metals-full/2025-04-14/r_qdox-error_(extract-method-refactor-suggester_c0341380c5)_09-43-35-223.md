error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7434.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7434.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7434.java
text:
```scala
S@@tring message = "";

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
package org.columba.mail.gui.composer.util;

import org.columba.core.config.Config;
import org.columba.core.io.TempFileStore;

import org.columba.mail.gui.composer.AbstractEditorController;
import org.columba.mail.gui.mimetype.MimeTypeViewer;
import org.columba.mail.util.MailResourceLoader;

import org.columba.ristretto.message.MimeHeader;

import java.awt.Font;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JOptionPane;


public class ExternalEditor {
    String Cmd;

    public ExternalEditor() {
    }
     // END public ExternalEditor()

    public ExternalEditor(String EditorCommand) {
    }
     // END public ExternalEditor(String EditorCommand)

    public boolean startExternalEditor(AbstractEditorController EditCtrl) {
        /*
         * *20030906, karlpeder* Method signature changed to take
         * an AbstractEditorController (instead of an TextEditorView) as
         * parameter since the view is no longer directly available
         */
        MimeHeader myHeader = new MimeHeader("text", "plain");
        MimeTypeViewer viewer = new MimeTypeViewer();
        File tmpFile = TempFileStore.createTempFileWithSuffix("extern_edit");
        FileWriter FO;
        FileReader FI;

        try {
            FO = new FileWriter(tmpFile);
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Error: Cannot write to temp file needed " +
                "for external editor.");

            return false;
        }

        try {
            //String M = EditView.getText();
            String M = EditCtrl.getViewText();

            if (M != null) {
                FO.write(M);
            }

            FO.close();
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Error: Cannot write to temp file needed " +
                "for external editor.");

            return false;
        }

        //Font OldFont = EditView.getFont();
        Font OldFont = EditCtrl.getViewFont();

        System.out.println("Setting Font to REALLY BIG!!! :-)");

        /*
        // Why doesn't this work???
        EditView.setFont(
                new Font(Config.getOptionsConfig().getThemeItem().getTextFontName(), Font.BOLD, 30));
        */
        Font font = Config.getOptionsConfig().getGuiItem().getTextFont();
        font = font.deriveFont(30);

        //EditView.setFont(font);
        EditCtrl.setViewFont(font);

        //EditView.setText(
        EditCtrl.setViewText(MailResourceLoader.getString("menu", "composer",
                "extern_editor_using_msg"));

        Process child = viewer.open(myHeader, tmpFile);

        if (child == null) {
            return false;
        }

        try {
            // Wait for external editor to quit
            child.waitFor();
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null,
                "Error: External editor exited " + "abnormally.");

            return false;
        }

        //EditView.setFont(OldFont);
        EditCtrl.setViewFont(OldFont);

        try {
            FI = new FileReader(tmpFile);
        } catch (java.io.FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                "Error: Cannot read from temp file used " +
                "by external editor.");

            return false;
        }

        //      int i = FI.available();
        char[] buf = new char[1000];
        int i;
        String message = new String("");

        try {
            while ((i = FI.read(buf)) >= 0) {
                //System.out.println( "*>"+String.copyValueOf(buf)+"<*");
                message += new String(buf, 0, i);

                //System.out.println( "-->"+Message+"<--");
            }

            FI.close();
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Error: Cannot read from temp file used " +
                "by external editor.");

            return false;
        }

        //System.out.println( "++>"+Message+"<++");
        //System.out.println( Message.length());
        //EditView.setText(message);
        EditCtrl.setViewText(message);

        return true;
    }
     // END public boolean startExternalEditor()
}
 // END public class ExternalEditor
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7434.java