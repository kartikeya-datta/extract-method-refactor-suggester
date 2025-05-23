error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2988.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2988.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2988.java
text:
```scala
e@@mitter.setImagePath("particle.png");


package com.badlogic.gdx.graphics.particles;

import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;

class EffectPanel extends JPanel {
	ParticleEditor editor;
	JTable emitterTable;
	DefaultTableModel emitterTableModel;
	int editIndex;
	String lastDir;

	public EffectPanel (ParticleEditor editor) {
		this.editor = editor;
		initializeComponents();
	}

	public ParticleEmitter newEmitter (String name, boolean select) {
		final ParticleEmitter emitter = new ParticleEmitter();

		emitter.getDuration().setLow(3000, 3000);

		emitter.getEmission().setHigh(10, 10);

		emitter.getLife().setHigh(1000, 1000);

		emitter.getScale().setHigh(32, 32);

		emitter.getRotation().setLow(1, 360);
		emitter.getRotation().setHigh(180, 180);
		emitter.getRotation().setTimeline(new float[] {0, 1});
		emitter.getRotation().setScaling(new float[] {0, 1});
		emitter.getRotation().setRelative(true);

		emitter.getAngle().setHigh(1, 360);
		emitter.getAngle().setActive(true);

		emitter.getVelocity().setHigh(80, 80);
		emitter.getVelocity().setActive(true);

		emitter.getTransparency().setHigh(1, 1);
		emitter.getTransparency().setTimeline(new float[] {0, 0.2f, 0.8f, 1});
		emitter.getTransparency().setScaling(new float[] {0, 1, 1, 0});

		emitter.setFlip(false, true);
		emitter.setMaxParticleCount(15);
		emitter.setImagePath("data/particle.png");

		ArrayList<ParticleEmitter> emitters = editor.effect.getEmitters();
		if (emitters.isEmpty())
			emitter.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		else {
			ParticleEmitter p = emitters.get(0);
			emitter.setPosition(p.getX(), p.getY());
		}
		emitters.add(emitter);

		emitterTableModel.addRow(new Object[] {name, true});
		if (select) {
			editor.reloadRows();
			int row = emitterTableModel.getRowCount() - 1;
			emitterTable.getSelectionModel().setSelectionInterval(row, row);
		}
		return emitter;
	}

	void emitterSelected () {
		int row = emitterTable.getSelectedRow();
		if (row == -1) {
			row = editIndex;
			emitterTable.getSelectionModel().setSelectionInterval(row, row);
		}
		if (row == editIndex) return;
		editIndex = row;
		editor.reloadRows();
	}

	void openEffect () {
		FileDialog dialog = new FileDialog(editor, "Open Effect", FileDialog.LOAD);
		if (lastDir != null) dialog.setDirectory(lastDir);
		dialog.setVisible(true);
		final String file = dialog.getFile();
		final String dir = dialog.getDirectory();
		if (dir == null || file == null || file.trim().length() == 0) return;
		lastDir = dir;
		ParticleEffect effect = new ParticleEffect();
		try {
			effect.loadEmitters(Gdx.files.absolute(new File(dir, file).getAbsolutePath()));
			editor.effect = effect;
			emitterTableModel.getDataVector().removeAllElements();
			editor.particleData.clear();
		} catch (Exception ex) {
			System.out.println("Error loading effect: " + new File(dir, file).getAbsolutePath());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(editor, "Error opening effect.");
			return;
		}
		for (ParticleEmitter emitter : effect.getEmitters()) {
			emitter.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
			emitterTableModel.addRow(new Object[] {emitter.getName(), true});
		}
		editIndex = 0;
		emitterTable.getSelectionModel().setSelectionInterval(editIndex, editIndex);
		editor.reloadRows();
	}

	void saveEffect () {
		FileDialog dialog = new FileDialog(editor, "Save Effect", FileDialog.SAVE);
		if (lastDir != null) dialog.setDirectory(lastDir);
		dialog.setVisible(true);
		String file = dialog.getFile();
		String dir = dialog.getDirectory();
		if (dir == null || file == null || file.trim().length() == 0) return;
		lastDir = dir;
		int index = 0;
		for (ParticleEmitter emitter : editor.effect.getEmitters())
			emitter.setName((String)emitterTableModel.getValueAt(index++, 0));
		try {
			editor.effect.save(new File(dir, file));
		} catch (Exception ex) {
			System.out.println("Error saving effect: " + new File(dir, file).getAbsolutePath());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(editor, "Error saving effect.");
		}
	}

	void deleteEmitter () {
		if (editor.effect.getEmitters().size() == 1) return;
		int row = emitterTable.getSelectedRow();
		if (row == -1) return;
		if (row <= editIndex) {
			int oldEditIndex = editIndex;
			editIndex = Math.max(0, editIndex - 1);
			if (oldEditIndex == row) editor.reloadRows();
		}
		editor.effect.getEmitters().remove(row);
		emitterTableModel.removeRow(row);
		emitterTable.getSelectionModel().setSelectionInterval(editIndex, editIndex);
	}

	void move (int direction) {
		if (direction < 0 && editIndex == 0) return;
		ArrayList<ParticleEmitter> emitters = editor.effect.getEmitters();
		if (direction > 0 && editIndex == emitters.size() - 1) return;
		int insertIndex = editIndex + direction;
		Object name = emitterTableModel.getValueAt(editIndex, 0);
		emitterTableModel.removeRow(editIndex);
		ParticleEmitter emitter = emitters.remove(editIndex);
		emitterTableModel.insertRow(insertIndex, new Object[] {name});
		emitters.add(insertIndex, emitter);
		editIndex = insertIndex;
		emitterTable.getSelectionModel().setSelectionInterval(editIndex, editIndex);
	}

	void emitterChecked (int index, boolean checked) {
		editor.setEnabled(editor.effect.getEmitters().get(index), checked);
		editor.effect.start();
	}

	private void initializeComponents () {
		setLayout(new GridBagLayout());
		{
			JPanel sideButtons = new JPanel(new GridBagLayout());
			add(sideButtons, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
			{
				JButton newButton = new JButton("New");
				sideButtons.add(newButton, new GridBagConstraints(0, -1, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0), 0, 0));
				newButton.addActionListener(new ActionListener() {
					public void actionPerformed (ActionEvent event) {
						newEmitter("Untitled", true);
					}
				});
			}
			{
				JButton deleteButton = new JButton("Delete");
				sideButtons.add(deleteButton, new GridBagConstraints(0, -1, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0), 0, 0));
				deleteButton.addActionListener(new ActionListener() {
					public void actionPerformed (ActionEvent event) {
						deleteEmitter();
					}
				});
			}
			{
				sideButtons.add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, -1, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0), 0, 0));
			}
			{
				JButton saveButton = new JButton("Save");
				sideButtons.add(saveButton, new GridBagConstraints(0, -1, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0), 0, 0));
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed (ActionEvent event) {
						saveEffect();
					}
				});
			}
			{
				JButton openButton = new JButton("Open");
				sideButtons.add(openButton, new GridBagConstraints(0, -1, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0), 0, 0));
				openButton.addActionListener(new ActionListener() {
					public void actionPerformed (ActionEvent event) {
						openEffect();
					}
				});
			}
			{
				JButton upButton = new JButton("Up");
				sideButtons.add(upButton, new GridBagConstraints(0, -1, 1, 1, 0, 1, GridBagConstraints.SOUTH,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0), 0, 0));
				upButton.addActionListener(new ActionListener() {
					public void actionPerformed (ActionEvent event) {
						move(-1);
					}
				});
			}
			{
				JButton downButton = new JButton("Down");
				sideButtons.add(downButton, new GridBagConstraints(0, -1, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				downButton.addActionListener(new ActionListener() {
					public void actionPerformed (ActionEvent event) {
						move(1);
					}
				});
			}
		}
		{
			JScrollPane scroll = new JScrollPane();
			add(scroll, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
				0, 0, 6), 0, 0));
			{
				emitterTable = new JTable() {
					public Class getColumnClass (int column) {
						return column == 1 ? Boolean.class : super.getColumnClass(column);
					}
				};
				emitterTable.getTableHeader().setReorderingAllowed(false);
				emitterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scroll.setViewportView(emitterTable);
				emitterTableModel = new DefaultTableModel(new String[0][0], new String[] {"Emitter", ""});
				emitterTable.setModel(emitterTableModel);
				emitterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					public void valueChanged (ListSelectionEvent event) {
						if (event.getValueIsAdjusting()) return;
						emitterSelected();
					}
				});
				emitterTableModel.addTableModelListener(new TableModelListener() {
					public void tableChanged (TableModelEvent event) {
						if (event.getColumn() != 1) return;
						emitterChecked(event.getFirstRow(), (Boolean)emitterTable.getValueAt(event.getFirstRow(), 1));
					}
				});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2988.java