error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/971.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/971.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,46]

error in qdox parser
file content:
```java
offset: 46
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/971.java
text:
```scala
{"button.add-operation", "Äîáàâèòü îïåðàöèþ" }@@,

// Copyright (c) 1996-2001 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.i18n;
import java.util.*;
import org.argouml.util.*;
import javax.swing.*;
import java.awt.event.*;

/** Russian Resource bundle for internationalization of menu
 *
 *   @author Alexey Aphanasyev (Alexey@tigris.org)
 *   @see org.argouml.i18n.MenuResourceBundle
 */
public class MenuResourceBundle_ru extends ListResourceBundle {


   static final Object[][] _contents = {
        {"New", "Íîâûé ïðîåêò" },  
        {"Open Project...", "Îòêðûòü ïðîåêò..." },  
        {"Save Project", "Ñîõðàíèòü ïðîåêò" },  
        {"Load model from DB", "Çàãðóçèòü ìîäåëü èç ÁÄ" },  
        {"Store model to DB", "Ñîõðàíèòü ìîäåëü â ÁÄ" },  
        {"Save Project As...", "Ñîõðàíèòü ïðîåêò êàê..." },  
        {"Import", "Èìïîðòèðîâàòü" },
        {"Import sources...", "Èìïîðòèðîâàòü èñõîäíûé êîä..." },
        {"Print...", "Ïå÷àòàòü..." },  
        {"Save GIF...", "Ñîõðàíèòü êàê GIF..." },  
        {"Save Graphics...", "Ñîõðàíèòü äèàãðàìû êàê ãðàôèêó..." },  
	{"Save Configuration", "Ñîõðàíèòü êîíôèãóðàöèþ"},
        {"Exit", "Âûõîä" },  
        {"Undo", "Îòìåíèòü" },  
        {"Redo", "Ïîâòîðèòü" },  
        {"Cut", "Âûðåçàòü" },  
        {"Copy", "Êîïèðîâàòü" },  
        {"Paste", "Âñòàâèòü" },  
	{"Settings...", "Óñòàíîâêè..."},
        {"Remove From Diagram", "Óäàëèòü èç äèàãðàììû" },  
        {"Delete From Model", "Óäàëèòü èç ìîäåëè" },  
        {"Empty Trash", "Óíè÷òîæèòü ìóñîð" },  
        {"Navigate Back", "Ïðîäâèíóòüñÿ âïåðåä" },  
        {"Navigate Forward", "Âåðíóòüñÿ íàçàä" },  
        {"NavConfig", "Êîíôèãóðèðîâàíèå Íàâèãàöèè" },  
        {"Find...", "Íàéòè..." },  
        {"Goto Diagram...", "Ïåðåéòè ê äèàãðàìå..." },  
        {"Next Editing Tab", "Ñëåäóþùàÿ çàêëàäêà ðåäàêòèðîâàíèÿ" },  
        {"Next Details Tab", "Ñëåäóþùàÿ çàêëàäêà äåòàëåé" },  
        {"Buttons on Selection", "Buttons on Selection" },  
        {"Create Multiple...", "Create Multiple..." },  
        {"Add Top-Level Package", "Äîáàâèòü ïàêåò âåðõíåãî óðîâíÿ" },  
        {"ClassDiagram", "Äèàãðàììà Êëàññîâ" },  
        {"UseCaseDiagram", "Äèàãðàììà Âàðèàíòîâ èñïîëüçîâàíèÿ" },  
        {"StateDiagram", "Äèàãðàììà ñîñòîÿíèé" },  
        {"ActivityDiagram", "Äèàãðàììà äåÿòåëüíîñòè" },  
        {"CollaborationDiagram", "Äèàãðàììà êîîïåðàöèé" },  
        {"DeploymentDiagram", "Äèàãðàììà ðàçâåðòûâàíèÿ" },  
        {"SequenceDiagram", "Äèàãðàììà ïîñëåäîâàòåëüíîñòè" },  
        {"button.add-attribute", "Äîáàâèòü àòðèáóò" },  
        {"Add Operation", "Äîáàâèòü îïåðàöèþ" },  
        {"Add Message", "Äîáàâèòü ñîîáùåíèå" },  
        {"Add Internal Transition", "Äîáàâèòü âíóòðåííèé ïåðåõîä" },  
        {"Generate Selected Classes", "Ñãåíåðèðîâàòü âûáðàííûå êëàññû..." },  
        {"Generate All Classes", "Ñãåíåðèðîâàòü âñå êëàññû..." },  
        {"Toggle Auto-Critique", "Âêëþ÷èòü àâòîêðèòèêó" },  
        {"Design Issues...", "Ñïîðíûå ìîìåíòû ïðîåêòèðîâàíèÿ..." },  
        {"Design Goals...", "Öåëè ïðîåêòèðîâàíèÿ..." },  
        {"Browse Critics...", "Ïðîñìîòð êðèòè÷åñêèõ çàìå÷àíèé..." },  
        {"Toggle Flat View", "Âêëþ÷èòü ïëîñêèé âèä" },  
        {"New To Do Item...", "Íîâîå çàäàíèå..." },  
        {"Resolve Item...", "Ðåøèòü çàäàíèå..." },  
        {"Send Email To Expert...", "Ïîñëàòü ïèñüìî ýêñïåðòó..." },  
        {"More Info...", "Äîï. èíôîðìàöèÿ..." },  
        {"Snooze Critic", "Óñûïèòü êðèòèêó" },  
        {"About Argo/UML", "Êîðîòêî îá ArgoUML..." },  
        {"Properties", "Ñâîéñòâà" },  
        {"1", "1" },  
        {"0..1", "0..1" },  
        {"0..*", "0..*" },  
        {"1..*", "1..*" },  
        {"aggregate", "àãðåãàò" },  
        {"composite", "êîìïîçèòíûé" },  
        {"none", "ïóñòî" },  
        {"Show Attribute Compartment", "Ïîêàçàòü ðàçäåë àòðèáóòîâ" },  
        {"Hide Attribute Compartment", "Ñïðÿòàòü ðàçäåë àòðèáóòîâ" },  
        {"Show Operation Compartment", "ïîêàçàòü ðàçäåë îïåðàöèé" },  
        {"Hide Operation Compartment", "Ñïðÿòàòü ðàçäåë îïåðàöèé" },  
        {"Show All Compartments", "Ïîêàçàòü âñå ðàçäåëû" },  
        {"Hide All Compartments", "Ñïðÿòàòü âñå ðàçäåëû" },  
        {"File", "Ôàéë" },  
        {"Mnemonic_File", "F" },  
        {"Mnemonic_New", "N" },  
        {"Mnemonic_Open", "O" },  
        {"Mnemonic_Save", "S" },  
        {"Mnemonic_SaveAs", "A" },  
        {"Mnemonic_Print", "P" },  
        {"Mnemonic_SaveGraphics", "G" },  
        {"Mnemonic_Exit", "X" },  
        {"Edit", "Ðåäàêòèðîâàòü" },  
        {"Mnemonic_Edit", "E" },  
        {"Select", "Âûáðàòü" },  
        {"Mnemonic_Cut", "X" },  
        {"Mnemonic_Copy", "C" },  
        {"Mnemonic_Paste", "V" },  
        {"Mnemonic_RemoveFromDiagram", "R" },  
        {"Mnemonic_DeleteFromModel", "D" },  
        {"View", "Ïðîñìîòð" },  
        {"Zoom", "Ìàñøòàá" },  
        {"Mnemonic_View", "V" },  
        {"Editor Tabs", "Çàêëàäêè ðåäàêòîðà" },  
        {"Details Tabs", "Çàêëàäêè äåòàëåé" },  
        {"Create", "Ñîçäàòü" },  
        {"Mnemonic_Create", "C" },  
        {"Diagrams", "Äèàãðàììû" },  
        {"Create Diagram", "Ñîçäàòü äèàãðàììó" },  
        {"Arrange", "Ðàññòàâèòü" },  
        {"Mnemonic_Arrange", "A" },  
        {"Align", "Âûðîâíÿòü" },  
        {"Distribute", "Ðàñïðåäåëèòü" },  
        {"Reorder", "Ðåîðãàíèçîâàòü" },  
        {"Nudge", "Ïîäòîëêíóòü" },  
        {"Layout", "Êîìïîíîâêà" },  
        {"Generation", "Ãåíåðèðîâàíèå" },  
        {"Mnemonic_Generate", "G" },  
        {"Critique", "Êðèòèêà" },  
        {"Mnemonic_Critique", "R" },  
        {"Help", "Ïîìîùü" },  
        {"Mnemonic_Help", "H" },  
        {"Tools", "Èíñòðóìåíòû" },  
        {"Automatic", "Àâòîìàòè÷åñêè" },  
        {"Incremental", "Èíêðåìåíòíî" },  
        {"As Diagram", "Êàê äèàãðàììà" },  
        {"As Table", "Êàê òàáëèöà" },  
        {"As Metrics", "Êàê ìåòðèêà" },  
        {"ToDoItem", "Ñäåëàòü" },  
        {"Javadocs", "Javadocs" },  
        {"Source", "Èñõîäíûé êîä" },  
        {"Constraints", "Îãðàíè÷åíèÿ" },  
        {"TaggedValues", "Èìåíîâàííûå çíà÷åíèÿ" },  
        {"Checklist", "Checklist" },  
        {"History", "Èñòîðèÿ" },  

        { "Shortcut_New", KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK) },
        { "Shortcut_Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK) },
        { "Shortcut_Save", KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK) },
        { "Shortcut_Print", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK) },
        { "Shortcut_Select_All", KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK) },
        { "Shortcut_Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK) },
        { "Shortcut_Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK) },
        { "Shortcut_Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK) },
        { "Shortcut_Remove_From_Diagram", KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK) },
        { "Shortcut_Find", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0) },
        { "Shortcut_Generate_All", KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0) },
        { "Shortcut_Exit", KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK) },
        { "Shortcut_Delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)}

   };

     public Object[][] getContents() {
        return _contents;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/971.java