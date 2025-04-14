error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6809.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6809.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6809.java
text:
```scala
public A@@ctionOpenProject() { super ("action.open-project"); }

// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.*;
import org.argouml.cognitive.Designer;
import org.argouml.ui.*;
import org.argouml.xml.argo.*;
import org.argouml.util.*;
import org.argouml.util.osdep.*;

import org.tigris.gef.base.*;

import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.text.MessageFormat;

import javax.swing.*;

/**
 * Action that loads the project.
 * This will throw away the project that we were working with up to this 
 * point so some extra caution.
 *
 * @see ActionSaveProject
 * @stereotype singleton
 */
public class ActionOpenProject extends UMLAction {
    
    protected static Category cat = Category.getInstance(org.argouml.uml.ui.ActionOpenProject.class);
  
  ////////////////////////////////////////////////////////////////
  // static variables
  
  public static ActionOpenProject SINGLETON = new ActionOpenProject();
  
  public static final String separator = System.getProperty ("file.separator");
  
  ////////////////////////////////////////////////////////////////
  // constructors
  
  public ActionOpenProject() { super ("Open Project..."); }
  
  ////////////////////////////////////////////////////////////////
  // main methods
  
    /** Performs the action.
     *
     * @param e an event
     */
  public void actionPerformed (ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = ProjectManager.getManager().getCurrentProject();
    
    if (p != null && p.needsSave()) {
      String t = MessageFormat.format (
          Argo.localize ("Actions", "template.open_project.save_changes_to"),
          new Object[] {p.getName()}
        );

      int response = JOptionPane.showConfirmDialog (
          pb,
          t,
          t,
          JOptionPane.YES_NO_CANCEL_OPTION
        );
      
      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION) {
        boolean safe = false;
        
        if (ActionSaveProject.SINGLETON.shouldBeEnabled()) {
          safe = ActionSaveProject.SINGLETON.trySave (true);
        }
        if (!safe) {
          safe = ActionSaveProjectAs.SINGLETON.trySave (false);
        }
        if (!safe) return;
      }
    }
    
    try {
      // next line does give user.home back but this is not compliant with how 
      // the project.url works and therefore open and save project as give 
      // different starting directories.
      // String directory = Globals.getLastDirectory();
      JFileChooser chooser = null;
      if (p != null && p.getURL() != null) {
          File file = new File(p.getURL().getFile());
          if (file.getParentFile() != null)
            chooser = OsUtil.getFileChooser(file.getParent());
      } else
        chooser = OsUtil.getFileChooser();
      
      // JFileChooser chooser = OsUtil.getFileChooser (directory);
      
      if (chooser == null) chooser = OsUtil.getFileChooser();
      
      chooser.setDialogTitle (Argo.localize ("Actions", "text.open_project.chooser_title"));
      SuffixFilter filter = FileFilters.CompressedFileFilter;
      chooser.addChoosableFileFilter (filter);
      chooser.addChoosableFileFilter (FileFilters.UncompressedFileFilter);
      chooser.addChoosableFileFilter (FileFilters.XMIFilter);
      chooser.setFileFilter (filter);
      
      int retval = chooser.showOpenDialog (pb);
      if (retval == 0) {
        File theFile = chooser.getSelectedFile();
        if (theFile != null) {
          String path = theFile.getParent();
          Globals.setLastDirectory (path);
          URL url = theFile.toURL();
          if(url != null) {
          	// 2002-07-18
        	// Jaap Branderhorst
        	// changed the loading of the projectfiles to solve hanging 
        	// of argouml if a project is corrupted. Issue 913
        	// made it possible to return to the old project if loading went wrong
        	Project oldProject = ProjectManager.getManager().getCurrentProject();
            // This is actually a hack! Some diagram types
            // (like the state diagrams) access the current
            // diagram to get some info. This might cause 
            // problems if there's another state diagram
            // active, so I remove the current project, before
            // loading the new one.
            // 2002-07-18
        	// Jaap Branderhorst
        	// changed the loading of the projectfiles to solve hanging 
        	// of argouml if a project is corrupted. Issue 913
        	// old code:
      
            // pb.setProject(Project.makeEmptyProject());
            
            // new code:
            Designer.disableCritiquing();
            Designer.clearCritiquing();

            // 2002-07-18
        	// Jaap Branderhorst
        	// changed the loading of the projectfiles to solve hanging 
        	// of argouml if a project is corrupted. Issue 913
        	// try catch block added
            try {
            	p = Project.loadProject (url);
            	ProjectManager.getManager().setCurrentProject(p);
            	pb.showStatus (MessageFormat.format (
                Argo.localize ("Actions", "template.open_project.status_read"),
                	new Object[] {url.toString()}
              	));
            }
            catch (java.io.FileNotFoundException ex) {
               JOptionPane.showMessageDialog(pb,
    				"Could not load the project " + url.toString() + "\n" +
    				"Exception message:\n" + ex,
   					"Error",
    				JOptionPane.ERROR_MESSAGE);
               // restore old state of the project browser
               ProjectManager.getManager().setCurrentProject(oldProject);
               Designer.enableCritiquing();
	       return;
            }
            catch (IOException io) {
                // now we have to handle the case of a corrupted XMI file
                JOptionPane.showMessageDialog(pb,
                    "Could not load the project " + url.toString() + "\n" +
                    "Project file probably corrupted.\n" +
                    "Please file a bug report at argouml.tigris.org including" +
                    " the corrupted project file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ProjectManager.getManager().setCurrentProject(oldProject);
                Designer.enableCritiquing();
		return;
            }
            catch (Exception ex) {
            	// now show some errorpane
            	JOptionPane.showMessageDialog(pb,
    				"Could not load the project " + url.toString() + "\n" +
    				"Project file probably corrupted.\n" +
                    "Exception message:\n" + ex,
   					"Error",
    				JOptionPane.ERROR_MESSAGE);

            	// lets restore the state of the projectbrowser
            	ProjectManager.getManager().setCurrentProject(oldProject);
                Designer.enableCritiquing();
		return;
            }
	    if (ArgoParser.SINGLETON.getLastLoadStatus() != true) {
            	JOptionPane.
		    showMessageDialog(pb,
				      "Problem in loading the project " 
				      + url.toString()
				      + "\n" 
				      + "Project file probably corrupt from "
				      + "an earlier version or ArgoUML.\n"
				      + "Error message:\n" 
				      + ArgoParser.SINGLETON.getLastLoadMessage()
				      + "\n"
				      + "Since the project was incorrectly "
				      + "saved some things might be missing "
				      + "from before you saved it.\n"
				      + "These things cannot be restored. "
				      + "You can continue working with what "
				      + "was actually loaded.\n",
				      "Error",
				      JOptionPane.ERROR_MESSAGE);
	    }
            Designer.enableCritiquing();
          }
          
          return;
        }
      }
    }
    catch (IOException ignore) {
        cat.error("got an IOException in ActionOpenProject", ignore);
    }
  }
} /* end class ActionOpenProject */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6809.java