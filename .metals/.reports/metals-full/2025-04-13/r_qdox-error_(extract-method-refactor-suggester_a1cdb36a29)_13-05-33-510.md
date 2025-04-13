error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5123.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5123.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,55]

error in qdox parser
file content:
```java
offset: 55
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5123.java
text:
```scala
{"button.add-operation", "A\u00f1adir operaci\u00f3n" }@@,

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

/**
 *
 * Provides strings for spanish localization of the interface.
 *
 * See source of UMLCognitiveResourceBundle_es for some notes about
 *
 * the translation.
 *
 *
 *
 * This is hardcoded. This class should be
 * modified to use something similar to the about
 * box. Ideally this class would call on the
 * xml file for all the languages and allow the users
 * to specify which language through a GUI
 * dialog.
 * @author Curt Arnold, Alejandro Ramirez
 * @since 0.9
 * @see java.util.ResourceBundle
 * @see org.argouml.uml.cognitive.UMLCognitiveResourceBundle_es
 */
public class MenuResourceBundle_es extends ListResourceBundle {

   static final Object[][] _contents = {
        {"New", "Nuevo" },
        {"Open Project...", "Abrir proyecto..." },
        {"Save Project", "Guardar proyecto" },
        {"Load model from DB", "Cargar modelo de la BD" },
        {"Store model to DB", "Guardar modelo en la BD" },
        {"Save Project As...", "Guardar proyecto como..." },
        {"Import sources...", "Importar fuentes..." },
        {"Import", "Importar" },
        {"Print...", "Imprimir..." },
        {"Save GIF...", "Guardar GIF..." },
        {"Save Graphics...", "Guardar gr\u00e1ficos..." },
        {"Save Configuration", "Guardar configuracion"},
        {"Exit", "Salir" },
        {"Undo", "Deshacer" },
        {"Redo", "Rehacer" },
        {"Cut", "Cortar" },
        {"Copy", "Copiar" },
        {"Paste", "Pegar" },
        {"Settings...", "Propiedades..."},
        {"Remove From Diagram", "Quitar del diagrama" },
        {"Delete From Model", "Borrar del modelo" },
        {"Empty Trash", "Eliminar basura" },
        {"Navigate Back", "Navegar atras" },
        {"Navigate Forward", "Navegar adelante" },
        {"NavConfig", "NavConfig" },
        {"Find...", "Buscar..." },
        {"Goto Diagram...", "Ir a diagrama..." },
        {"Next Editing Tab", "Siguiente solapa de edici\u00f3n" },
        {"Next Details Tab", "Siguiente solapa de detalle" },
        {"Buttons on Selection", "Botones en la selecci\u00f3n" },
        {"Create Multiple...", "Crear multiple..." },
        {"Add Top-Level Package", "A\u00f1adir paquete de nivel superior" },
        {"ClassDiagram", "Diagrama de clase" },
        {"UseCaseDiagram", "Diagrama de casos" },
        {"StateDiagram", "Diagrama de estado" },
        {"ActivityDiagram", "Diagrama de actividades" },
        {"CollaborationDiagram", "Diagrama de colaboracion" },
        {"DeploymentDiagram", "Diagrama de Instalaci\u00f3n" },
        {"SequenceDiagram", "Diagrama de secuencia" },
        {"button.add-attribute", "A\u00f1adir atributo" },
        {"Add Operation", "A\u00f1adir operaci\u00f3n" },
        {"Add Message", "A\u00f1adir mensaje" },
        {"Add Internal Transition", "A\u00f1adir transici\u00f3n interna" },
        {"Generate Selected Classes", "Generar clases seleccionadas..." },
        {"Generate All Classes", "Generar todas las clases..." },
        {"Toggle Auto-Critique", "(Des)activar Auto-Criticos" },
        {"Design Issues...", "Temas de dise\u00f1o..." },
        {"Design Goals...", "Objetivos de dise\u00f1o..." },
        {"Browse Critics...", "Ojear criticos..." },
        {"Toggle Flat View", "(Des)activar vista plana" },
        {"New To Do Item...", "Nueva tarea pendiente..." },
        {"Resolve Item...", "Resolver tarea..." },
        {"Send Email To Expert...", "Enviar email al experto..." },
        {"More Info...", "M\u00e1s informaci\u00f3n..." },
        {"Snooze Critic", "Dormir critico" },
        {"About Argo/UML", "Acerca de ArgoUML..." },
        {"Properties", "Propiedades" },
        {"1", "1" },
        {"0..1", "0..1" },
        {"0..*", "0..*" },
        {"1..*", "1..*" },
        {"aggregate", "agregaci\u00f3n" },
        {"composite", "composici\u00f3n" },
        {"none", "ninguno" },
        {"Show Attribute Compartment", "Muestra compartimento atributo" },
        {"Hide Attribute Compartment", "Oculta compartimento atributo" },
        {"Show Operation Compartment", "Muestra compartimento operaci\u00f3n" },
        {"Hide Operation Compartment", "Oculta compartimento operaci\u00f3n" },
        {"Show All Compartments", "Muestra ambos compartimentos" },
        {"Hide All Compartments", "Oculta ambos compartimentos" },
        {"File", "Archivo" },
        {"Mnemonic_File", "F" },
        {"Mnemonic_New", "N" },
        {"Mnemonic_Open", "O" },
        {"Mnemonic_Save", "S" },
        {"Mnemonic_SaveAs", "A" },
        {"Mnemonic_Print", "P" },
        {"Mnemonic_SaveGraphics", "G" },
        {"Mnemonic_Exit", "X" },
        {"Edit", "Editar" },
        {"Mnemonic_Edit", "E" },
        {"Select", "Seleccionar" },
        {"Mnemonic_Cut", "X" },
        {"Mnemonic_Copy", "C" },
        {"Mnemonic_Paste", "V" },
        {"Mnemonic_RemoveFromDiagram", "R" },
        {"Mnemonic_DeleteFromModel", "D" },
        {"View", "Ver" },
        {"Mnemonic_View", "V" },
        {"Editor Tabs", "Solapas de edici\u00f3n" },
        {"Details Tabs", "Solapas de detalle" },
        {"Create", "Crear" },
        {"Mnemonic_Create", "C" },
        {"Diagrams", "Diagramas" },
        {"Create Diagram", "Crear diagrama" },
        {"Arrange", "Arreglar" },
        {"Mnemonic_Arrange", "A" },
        {"Align", "Alinear" },
        {"Distribute", "Distribuir" },
        {"Reorder", "Reordenar" },
        {"Nudge", "Mover" },
        {"Generation", "Generaci\u00f3n" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "Critico" },
        {"Mnemonic_Critique", "R" },
        {"Help", "Ayuda" },
        {"Mnemonic_Help", "H" },
        {"Automatic", "Automatico" },
        {"As Diagram", "Como diagrama" },
        {"As Table", "Como tabla" },
        {"As Metrics", "Como m\u00e9trica" },
        {"ToDoItem", "Tarea pendiente" },
        {"Javadocs", "Javadocs" },
        {"Source", "C\u00f3digo fuente" },
        {"Constraints", "Restricciones" },
        {"TaggedValues", "Valores etiquetados" },
        {"Checklist", "Lista de comprobaci\u00f3n" },
        {"History", "Historia" },

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5123.java