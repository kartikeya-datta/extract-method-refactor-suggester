error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3524.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3524.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3524.java
text:
```scala
e@@xpandButton.setText("�");

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tools.particleeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;

class ScaledNumericPanel extends EditorPanel {
	final ScaledNumericValue value;
	Slider lowMinSlider, lowMaxSlider;
	Slider highMinSlider, highMaxSlider;
	JCheckBox relativeCheckBox;
	Chart chart;
	JPanel formPanel;
	JButton expandButton;
	JButton lowRangeButton;
	JButton highRangeButton;

	public ScaledNumericPanel (final ScaledNumericValue value, String chartTitle, String name, String description) {
		super(value, name, description);
		this.value = value;

		initializeComponents(chartTitle);

		lowMinSlider.setValue(value.getLowMin());
		lowMaxSlider.setValue(value.getLowMax());
		highMinSlider.setValue(value.getHighMin());
		highMaxSlider.setValue(value.getHighMax());
		chart.setValues(value.getTimeline(), value.getScaling());
		relativeCheckBox.setSelected(value.isRelative());

		lowMinSlider.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent event) {
				value.setLowMin((Float)lowMinSlider.getValue());
				if (!lowMaxSlider.isVisible()) value.setLowMax((Float)lowMinSlider.getValue());
			}
		});
		lowMaxSlider.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent event) {
				value.setLowMax((Float)lowMaxSlider.getValue());
			}
		});
		highMinSlider.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent event) {
				value.setHighMin((Float)highMinSlider.getValue());
				if (!highMaxSlider.isVisible()) value.setHighMax((Float)highMinSlider.getValue());
			}
		});
		highMaxSlider.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent event) {
				value.setHighMax((Float)highMaxSlider.getValue());
			}
		});

		relativeCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				value.setRelative(relativeCheckBox.isSelected());
			}
		});

		lowRangeButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				boolean visible = !lowMaxSlider.isVisible();
				lowMaxSlider.setVisible(visible);
				lowRangeButton.setText(visible ? "<" : ">");
				GridBagLayout layout = (GridBagLayout)formPanel.getLayout();
				GridBagConstraints constraints = layout.getConstraints(lowRangeButton);
				constraints.gridx = visible ? 5 : 4;
				layout.setConstraints(lowRangeButton, constraints);
				Slider slider = visible ? lowMaxSlider : lowMinSlider;
				value.setLowMax((Float)slider.getValue());
			}
		});

		highRangeButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				boolean visible = !highMaxSlider.isVisible();
				highMaxSlider.setVisible(visible);
				highRangeButton.setText(visible ? "<" : ">");
				GridBagLayout layout = (GridBagLayout)formPanel.getLayout();
				GridBagConstraints constraints = layout.getConstraints(highRangeButton);
				constraints.gridx = visible ? 5 : 4;
				layout.setConstraints(highRangeButton, constraints);
				Slider slider = visible ? highMaxSlider : highMinSlider;
				value.setHighMax((Float)slider.getValue());
			}
		});

		expandButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				chart.setExpanded(!chart.isExpanded());
				boolean expanded = chart.isExpanded();
				GridBagLayout layout = (GridBagLayout)getContentPanel().getLayout();
				GridBagConstraints chartConstraints = layout.getConstraints(chart);
				GridBagConstraints expandButtonConstraints = layout.getConstraints(expandButton);
				if (expanded) {
					chart.setPreferredSize(new Dimension(150, 200));
					expandButton.setText("-");
					chartConstraints.weightx = 1;
					expandButtonConstraints.weightx = 0;
				} else {
					chart.setPreferredSize(new Dimension(150, 30));
					expandButton.setText("+");
					chartConstraints.weightx = 0;
					expandButtonConstraints.weightx = 1;
				}
				layout.setConstraints(chart, chartConstraints);
				layout.setConstraints(expandButton, expandButtonConstraints);
				relativeCheckBox.setVisible(!expanded);
				formPanel.setVisible(!expanded);
				chart.revalidate();
			}
		});

		if (value.getLowMin() == value.getLowMax()) lowRangeButton.doClick(0);
		if (value.getHighMin() == value.getHighMax()) highRangeButton.doClick(0);
	}

	public JPanel getFormPanel () {
		return formPanel;
	}

	private void initializeComponents (String chartTitle) {
		JPanel contentPanel = getContentPanel();
		{
			formPanel = new JPanel(new GridBagLayout());
			contentPanel.add(formPanel, new GridBagConstraints(5, 5, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 6), 0, 0));
			{
				JLabel label = new JLabel("High:");
				formPanel.add(label, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 6), 0, 0));
			}
			{
				highMinSlider = new Slider(0, -99999, 99999, 1f, -400, 400);
				formPanel.add(highMinSlider, new GridBagConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				highMaxSlider = new Slider(0, -99999, 99999, 1f, -400, 400);
				formPanel.add(highMaxSlider, new GridBagConstraints(4, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
			}
			{
				highRangeButton = new JButton("<");
				highRangeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
				formPanel.add(highRangeButton, new GridBagConstraints(5, 1, 1, 1, 0.0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));
			}
			{
				JLabel label = new JLabel("Low:");
				formPanel.add(label, new GridBagConstraints(2, 2, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 6), 0, 0));
			}
			{
				lowMinSlider = new Slider(0, -99999, 99999, 1f, -400, 400);
				formPanel.add(lowMinSlider, new GridBagConstraints(3, 2, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				lowMaxSlider = new Slider(0, -99999, 99999, 1f, -400, 400);
				formPanel.add(lowMaxSlider, new GridBagConstraints(4, 2, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
			}
			{
				lowRangeButton = new JButton("<");
				lowRangeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
				formPanel.add(lowRangeButton, new GridBagConstraints(5, 2, 1, 1, 0.0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));
			}
		}
		{
			chart = new Chart(chartTitle) {
				public void pointsChanged () {
					value.setTimeline(chart.getValuesX());
					value.setScaling(chart.getValuesY());
				}
			};
			contentPanel.add(chart, new GridBagConstraints(6, 5, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
			chart.setPreferredSize(new Dimension(150, 30));
		}
		{
			expandButton = new JButton("+");
			contentPanel.add(expandButton, new GridBagConstraints(7, 5, 1, 1, 1, 0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			expandButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
		}
		{
			relativeCheckBox = new JCheckBox("Relative");
			contentPanel.add(relativeCheckBox, new GridBagConstraints(7, 5, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3524.java