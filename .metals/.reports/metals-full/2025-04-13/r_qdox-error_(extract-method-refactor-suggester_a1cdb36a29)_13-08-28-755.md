error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/705.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/705.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/705.java
text:
```scala
D@@NDFigures df = (DNDFigures)DNDHelper.processReceivedData(DNDFiguresTransferable.DNDFiguresFlavor, dsde.getDragSourceContext().getTransferable());

/*
 * JHDDragSource.java
 *
 * Created on January 28, 2003, 4:49 PM
 */

package CH.ifa.draw.contrib.dnd;

import CH.ifa.draw.framework.*;
import CH.ifa.draw.standard.DeleteFromDrawingVisitor;
import CH.ifa.draw.util.Undoable;
import java.awt.Component;
import java.awt.dnd.*;
import javax.swing.JComponent;

/**
 *
 * @author  Administrator
 */
public class JHDDragSourceListener implements java.awt.dnd.DragSourceListener {
	private Undoable sourceUndoable;
	private Boolean autoscrollState;
	private DrawingEditor editor;
	
	/** Creates a new instance of JHDDragSource */
	public JHDDragSourceListener(DrawingEditor editor, DrawingView view) {
		this.editor = editor;
	}
//	protected DrawingView view(){
//		return dv;
//	}
	protected DrawingEditor editor(){
		return editor;
	}
	/**
	 * This method is invoked to signify that the Drag and Drop operation is complete.
	 * This is the last method called in the process.
	 */
	public void dragDropEnd(java.awt.dnd.DragSourceDropEvent dsde) {
		DrawingView view = (DrawingView) dsde.getDragSourceContext().getComponent();
		log("DragSourceDropEvent-dragDropEnd");
		if (dsde.getDropSuccess() == true) {
			if (dsde.getDropAction() == DnDConstants.ACTION_MOVE) {
                log("DragSourceDropEvent-ACTION_MOVE");
				//get the flavor in order of ease of use here.
				setSourceUndoActivity(  createSourceUndoActivity( view ) );
				DNDFigures df = (DNDFigures)DNDHelper.ProcessReceivedData(DNDFiguresTransferable.DNDFiguresFlavor, dsde.getDragSourceContext().getTransferable());
				getSourceUndoActivity().setAffectedFigures( df.getFigures() );

				//all this visitation needs to be hidden in a view method.
				DeleteFromDrawingVisitor deleteVisitor = new DeleteFromDrawingVisitor(view.drawing());
				FigureEnumeration fe = getSourceUndoActivity().getAffectedFigures();
				while (fe.hasNextFigure()) {
					fe.nextFigure().visit(deleteVisitor);
				}
				view.clearSelection();
				view.checkDamage();

				editor().getUndoManager().pushUndo( getSourceUndoActivity() );
				editor().getUndoManager().clearRedos();
				// update menus
				editor().figureSelectionChanged( view );
			}
			else if (dsde.getDropAction() == DnDConstants.ACTION_COPY) {
				log("DragSourceDropEvent-ACTION_COPY");
			}
		}

		if (autoscrollState != null) {
			Component c = dsde.getDragSourceContext().getComponent();
			if (JComponent.class.isInstance( c )) {
				JComponent jc = (JComponent)c;
				jc.setAutoscrolls(autoscrollState.booleanValue());
				autoscrollState= null;
			}
		}
	}
	/**
	 * Called as the hotspot enters a platform dependent drop site.
	 */
	public void dragEnter(DragSourceDragEvent dsde) {
		log("DragSourceDragEvent-dragEnter");
		if (autoscrollState == null) {
			Component c = dsde.getDragSourceContext().getComponent();
			if (JComponent.class.isInstance( c )) {
				JComponent jc = (JComponent)c;
				autoscrollState= new Boolean(jc.getAutoscrolls());
				jc.setAutoscrolls(false);//why turn it off???
			}
		}
	}
	/**
	 * Called as the hotspot exits a platform dependent drop site.
	 */
	public void dragExit(java.awt.dnd.DragSourceEvent dse) {
	}
	/**
	 * Called as the hotspot moves over a platform dependent drop site.
	 */
	public void dragOver(DragSourceDragEvent dsde) {
		//log("DragSourceDragEvent-dragOver");
	}
	/**
	 * Called when the user has modified the drop gesture.
	 */
	public void dropActionChanged(DragSourceDragEvent dsde) {
		log("DragSourceDragEvent-dropActionChanged");
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Factory method for undo activity
	 */
	protected Undoable createSourceUndoActivity(DrawingView drawingView) {
		return new RemoveUndoActivity( drawingView );
	}
	protected void setSourceUndoActivity(Undoable undoable){
		sourceUndoable = undoable;
	}
	protected Undoable getSourceUndoActivity(){
		return sourceUndoable;
	}
	public static class RemoveUndoActivity extends CH.ifa.draw.util.UndoableAdapter {
		private boolean undone = false;
		public RemoveUndoActivity(DrawingView view) {
			super( view );
			log("RemoveUndoActivity created " + view);
			setUndoable(true);
			setRedoable(true);
		}

		public boolean undo() {
			if (isUndoable()) {
				if(getAffectedFigures().hasNextFigure()) {
					log("RemoveUndoActivity undo");
					getDrawingView().clearSelection();
					setAffectedFigures( getDrawingView().insertFigures(getAffectedFigures(), 0, 0,false));
					undone = true;
					return true;
				}
			}
			return false;
		}

		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (isRedoable()) {
				log("RemoveUndoActivity redo");
				DeleteFromDrawingVisitor deleteVisitor = new DeleteFromDrawingVisitor( getDrawingView().drawing());
				FigureEnumeration fe = getAffectedFigures();
				while (fe.hasNextFigure()) {
					fe.nextFigure().visit(deleteVisitor); //orphans figures
				}
				getDrawingView().clearSelection();
				setAffectedFigures( deleteVisitor.getDeletedFigures() );
				undone = false;
				return true;
			}
			return false;
		}
		/**
		 * Since this is a delete activity, figures can only be released if the
		 * action has not been undone.
		 */
		public void release() {
			if(undone == false){//we have figures that used to be in the drawing, but were not adding back
				FigureEnumeration fe = getAffectedFigures();
				while (fe.hasNextFigure()) {
					Figure f = fe.nextFigure();
					getDrawingView().drawing().remove(f);
					f.release();
				}
			}
			setAffectedFigures(CH.ifa.draw.standard.FigureEnumerator.getEmptyEnumeration());
		}		
	}
	
	
	
	
	
	
	
	private static void log(String message){
		//System.out.println("JHDDragSourceListener: " + message);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/705.java