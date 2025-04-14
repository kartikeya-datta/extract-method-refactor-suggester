error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[257,2]

error in qdox parser
file content:
```java
offset: 6817
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1283.java
text:
```scala
import org.apache.jorphan.collections.Data;

package org.apache.jmeter.gui.util;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.jorphan.collections.Data;
/**
 * @author mstover
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class PowerTableModel extends DefaultTableModel {
	Data model = new Data();
	Class[] columnClasses;

	public PowerTableModel(String[] headers, Class[] cc) {
		model.setHeaders(headers);
		columnClasses = cc;
	}
	
	public PowerTableModel()
	{
	}

	public void setRowValues(int row, Object[] values) {
		model.setCurrentPos(row);
		for (int i = 0; i < values.length; i++) {
			model.addColumnValue(model.getHeaders()[i], values[i]);
		}
	}

	public Data getData() {
		return model;
	}
	
	public void addNewColumn(String colName,Class colClass)
	{
		model.addHeader(colName);
		Class[] newClasses = new Class[columnClasses.length+1];
		System.arraycopy(columnClasses,0,newClasses,0,columnClasses.length);
		newClasses[newClasses.length-1] = colClass;
		columnClasses = newClasses;
		Object defaultValue = createDefaultValue(columnClasses.length-1);
		model.setColumnData(colName,defaultValue);
		this.fireTableStructureChanged();
	}

	/****************************************
		 * Description of the Method
		 *
		 *@param row  Description of Parameter
		 ***************************************/
	public void removeRow(int row) {
		if (model.size() > row) {
			model.removeRow(row);
		}
	}
	
	public void removeColumn(int col)
	{
		model.removeColumn(col);
		this.fireTableStructureChanged();
	}
	
	public void setColumnData(int col,List data)
	{
		model.setColumnData(col,data);
	}
	
	
	public List getColumnData(String colName)
	{
		return model.getColumnAsObjectArray(colName);
	}
	
	public void clearData()
	{
		String[] headers = model.getHeaders();
		model = new Data();
		model.setHeaders(headers);
		this.fireTableDataChanged();
	}

	public void addRow(Object data[]) {
		model.setCurrentPos(model.size());
		for (int i = 0; i < data.length; i++) {
			model.addColumnValue(model.getHeaders()[i], data[i]);
		}
	}

	/****************************************
	 ***************************************/
	public void addNewRow() {		
		addRow(createDefaultRow());
	}
	
	private Object[] createDefaultRow()
	{
		Object[] rowData = new Object[getColumnCount()];
		for(int i = 0;i < rowData.length;i++)
		{
			rowData[i] = createDefaultValue(i);
		}
		return rowData;
	}
	
	public Object[] getRowData(int row)
	{
		Object[] rowData = new Object[getColumnCount()];
		for(int i = 0;i < rowData.length;i++)
		{
			rowData[i] = model.getColumnValue(i,row);
		}
		return rowData;
	}
	
	private Object createDefaultValue(int i)
	{
		Class colClass = getColumnClass(i);
		try {
			return colClass.newInstance();
		} catch(Exception e) {
			try {
				Constructor constr = colClass.getConstructor(new Class[]{String.class});
				return constr.newInstance(new Object[]{""});
			} catch(Exception err) {
			} 
			try {
				Constructor constr = colClass.getConstructor(new Class[]{Integer.TYPE});
				return constr.newInstance(new Object[]{new Integer(0)});
			} catch(Exception err) {
			} 
			try {
				Constructor constr = colClass.getConstructor(new Class[]{Long.TYPE});
				return constr.newInstance(new Object[]{new Long(0L)});
			} catch(Exception err) {
			}
			try {
				Constructor constr = colClass.getConstructor(new Class[]{Boolean.TYPE});
				return constr.newInstance(new Object[]{new Boolean(false)});
			} catch(Exception err) {
			}
			try {
				Constructor constr = colClass.getConstructor(new Class[]{Float.TYPE});
				return constr.newInstance(new Object[]{new Float(0F)});
			} catch(Exception err) {
			}
			try {
				Constructor constr = colClass.getConstructor(new Class[]{Double.TYPE});
				return constr.newInstance(new Object[]{new Double(0D)});
			} catch(Exception err) {
			}
			try {
				Constructor constr = colClass.getConstructor(new Class[]{Character.TYPE});
				return constr.newInstance(new Object[]{new Character(' ')});
			} catch(Exception err) {
			}
			try {
				Constructor constr = colClass.getConstructor(new Class[]{Byte.TYPE});
				return constr.newInstance(new Object[]{new Byte(Byte.MIN_VALUE)});
			} catch(Exception err) {
			}
			try {
				Constructor constr = colClass.getConstructor(new Class[]{Short.TYPE});
				return constr.newInstance(new Object[]{new Short(Short.MIN_VALUE)});
			} catch(Exception err) {
			}
		}
		return "";		
	}

	/****************************************
	 * required by table model interface
	 *
	 *@return   The RowCount value
	 ***************************************/
	public int getRowCount() {
		if (model == null) {
			return 0;
		}
		return model.size();
	}

	/****************************************
	 * required by table model interface
	 *
	 *@return   The ColumnCount value
	 ***************************************/
	public int getColumnCount() {
		return model.getHeaders().length;
	}

	/****************************************
	 * required by table model interface
	 *
	 *@param column  Description of Parameter
	 *@return        The ColumnName value
	 ***************************************/
	public String getColumnName(int column) {
		return model.getHeaders()[column];
	}

	/****************************************
	 * !ToDoo (Method description)
	 *
	 *@param row     !ToDo (Parameter description)
	 *@param column  !ToDo (Parameter description)
	 *@return        !ToDo (Return description)
	 ***************************************/
	public boolean isCellEditable(int row, int column) {
		// all table cells are editable
		return true;
	}

	/****************************************
	 * !ToDoo (Method description)
	 *
	 *@param column  !ToDo (Parameter description)
	 *@return        !ToDo (Return description)
	 ***************************************/
	public Class getColumnClass(int column) {
		return columnClasses[column];
	}

	/****************************************
	 * required by table model interface
	 *
	 *@param row     Description of Parameter
	 *@param column  Description of Parameter
	 *@return        The ValueAt value
	 ***************************************/
	public Object getValueAt(int row, int column) {
		return model.getColumnValue(column, row);
	}

	/****************************************
	 * Sets the ValueAt attribute of the Arguments object
	 *
	 *@param value   The new ValueAt value
	 *@param row     The new ValueAt value
	 *@param column  !ToDo (Parameter description)
	 ***************************************/
	public void setValueAt(Object value, int row, int column) {
		if(row < model.size())
		{
			model.setCurrentPos(row);
			model.addColumnValue(model.getHeaders()[column], value);
		}
	}

}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1283.java