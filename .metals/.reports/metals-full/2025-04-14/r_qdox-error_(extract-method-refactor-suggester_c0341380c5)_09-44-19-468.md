error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9691.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9691.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9691.java
text:
```scala
e@@ffectPanel.newExampleEmitter("Untitled", true);

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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.NumericValue;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ParticleEditor extends JFrame {
	LwjglCanvas lwjglCanvas;
	JPanel rowsPanel;
	JPanel editRowsPanel;
	EffectPanel effectPanel;
	private JSplitPane splitPane;
	OrthographicCamera worldCamera;
	OrthographicCamera textCamera;
	NumericValue pixelsPerMeter;
	NumericValue zoomLevel;
	NumericValue deltaMultiplier;

	float pixelsPerMeterPrev;
	float zoomLevelPrev;

	ParticleEffect effect = new ParticleEffect();
	final HashMap<ParticleEmitter, ParticleData> particleData = new HashMap();

	public ParticleEditor () {
		super("Particle Editor");

		lwjglCanvas = new LwjglCanvas(new Renderer(), false);
		addWindowListener(new WindowAdapter() {
			public void windowClosed (WindowEvent event) {
				System.exit(0);
				// Gdx.app.quit();
			}
		});

		initializeComponents();

		setSize(1000, 950);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	void reloadRows () {
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				editRowsPanel.removeAll();
				addEditorRow(new NumericPanel(pixelsPerMeter, "Pixels per meter", ""));
				addEditorRow(new NumericPanel(zoomLevel, "Zoom level", ""));
				addEditorRow(new NumericPanel(deltaMultiplier, "Delta multiplier", ""));

				rowsPanel.removeAll();
				ParticleEmitter emitter = getEmitter();
				addRow(new ImagePanel(ParticleEditor.this, "Image", ""));
				addRow(new CountPanel(ParticleEditor.this, "Count",
					"Min number of particles at all times, max number of particles allowed."));
				addRow(new RangedNumericPanel(emitter.getDelay(), "Delay",
					"Time from beginning of effect to emission start, in milliseconds."));
				addRow(new RangedNumericPanel(emitter.getDuration(), "Duration", "Time particles will be emitted, in milliseconds."));
				addRow(new ScaledNumericPanel(emitter.getEmission(), "Duration", "Emission",
					"Number of particles emitted per second."));
				addRow(new ScaledNumericPanel(emitter.getLife(), "Duration", "Life", "Time particles will live, in milliseconds."));
				addRow(new ScaledNumericPanel(emitter.getLifeOffset(), "Duration", "Life Offset",
					"Particle starting life consumed, in milliseconds."));
				addRow(new RangedNumericPanel(emitter.getXOffsetValue(), "X Offset",
					"Amount to offset a particle's starting X location, in world units."));
				addRow(new RangedNumericPanel(emitter.getYOffsetValue(), "Y Offset",
					"Amount to offset a particle's starting Y location, in world units."));
				addRow(new SpawnPanel(ParticleEditor.this, emitter.getSpawnShape(), "Spawn", "Shape used to spawn particles."));
				addRow(new ScaledNumericPanel(emitter.getSpawnWidth(), "Duration", "Spawn Width",
					"Width of the spawn shape, in world units."));
				addRow(new ScaledNumericPanel(emitter.getSpawnHeight(), "Duration", "Spawn Height",
					"Height of the spawn shape, in world units."));
				addRow(new ScaledNumericPanel(emitter.getScale(), "Life", "Size", "Particle size, in world units."));
				addRow(new ScaledNumericPanel(emitter.getVelocity(), "Life", "Velocity", "Particle speed, in world units per second."));
				addRow(new ScaledNumericPanel(emitter.getAngle(), "Life", "Angle", "Particle emission angle, in degrees."));
				addRow(new ScaledNumericPanel(emitter.getRotation(), "Life", "Rotation", "Particle rotation, in degrees."));
				addRow(new ScaledNumericPanel(emitter.getWind(), "Life", "Wind", "Wind strength, in world units per second."));
				addRow(new ScaledNumericPanel(emitter.getGravity(), "Life", "Gravity", "Gravity strength, in world units per second."));
				addRow(new GradientPanel(emitter.getTint(), "Tint", ""));
				addRow(new PercentagePanel(emitter.getTransparency(), "Life", "Transparency", ""));
				addRow(new OptionsPanel(ParticleEditor.this, "Options", ""));
				for (Component component : rowsPanel.getComponents())
					if (component instanceof EditorPanel) ((EditorPanel)component).update(ParticleEditor.this);
				rowsPanel.repaint();
			}
		});
	}

	void addEditorRow (JPanel row) {
		row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.black));
		editRowsPanel.add(row, new GridBagConstraints(0, -1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));
	}

	void addRow (JPanel row) {
		row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.black));
		rowsPanel.add(row, new GridBagConstraints(0, -1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0));
	}

	public void setVisible (String name, boolean visible) {
		for (Component component : rowsPanel.getComponents())
			if (component instanceof EditorPanel && ((EditorPanel)component).getName().equals(name)) component.setVisible(visible);
	}

	public ParticleEmitter getEmitter () {
		return effect.getEmitters().get(effectPanel.editIndex);
	}

	public ImageIcon getIcon (ParticleEmitter emitter) {
		ParticleData data = particleData.get(emitter);
		if (data == null) particleData.put(emitter, data = new ParticleData());
		String imagePath = emitter.getImagePath();
		if (data.icon == null && imagePath != null) {
			try {
				URL url;
				File file = new File(imagePath);
				if (file.exists())
					url = file.toURI().toURL();
				else {
					url = ParticleEditor.class.getResource(imagePath);
					if (url == null) return null;
				}
				data.icon = new ImageIcon(url);
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			}
		}
		return data.icon;
	}

	public void setIcon (ParticleEmitter emitters, ImageIcon icon) {
		ParticleData data = particleData.get(emitters);
		if (data == null) particleData.put(emitters, data = new ParticleData());
		data.icon = icon;
	}

	public void setEnabled (ParticleEmitter emitter, boolean enabled) {
		ParticleData data = particleData.get(emitter);
		if (data == null) particleData.put(emitter, data = new ParticleData());
		data.enabled = enabled;
		emitter.reset();
	}

	public boolean isEnabled (ParticleEmitter emitter) {
		ParticleData data = particleData.get(emitter);
		if (data == null) return true;
		return data.enabled;
	}

	private void initializeComponents () {
		// {
		// JMenuBar menuBar = new JMenuBar();
		// setJMenuBar(menuBar);
		// JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		// JMenu fileMenu = new JMenu("File");
		// menuBar.add(fileMenu);
		// }
		splitPane = new JSplitPane();
		splitPane.setUI(new BasicSplitPaneUI() {
			public void paint (Graphics g, JComponent jc) {
			}
		});
		splitPane.setDividerSize(4);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		{
			JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			rightSplit.setUI(new BasicSplitPaneUI() {
				public void paint (Graphics g, JComponent jc) {
				}
			});
			rightSplit.setDividerSize(4);
			splitPane.add(rightSplit, JSplitPane.RIGHT);

			{
				JPanel propertiesPanel = new JPanel(new GridBagLayout());
				rightSplit.add(propertiesPanel, JSplitPane.TOP);
				propertiesPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(3, 0, 6, 6), BorderFactory
					.createTitledBorder("Editor Properties")));
				{
					JScrollPane scroll = new JScrollPane();
					propertiesPanel.add(scroll, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
					{
						editRowsPanel = new JPanel(new GridBagLayout());
						scroll.setViewportView(editRowsPanel);
						scroll.getVerticalScrollBar().setUnitIncrement(70);
					}
				}
			}

			{
				JPanel propertiesPanel = new JPanel(new GridBagLayout());
				rightSplit.add(propertiesPanel, JSplitPane.BOTTOM);
				propertiesPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(3, 0, 6, 6), BorderFactory
					.createTitledBorder("Emitter Properties")));
				{
					JScrollPane scroll = new JScrollPane();
					propertiesPanel.add(scroll, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
					{
						rowsPanel = new JPanel(new GridBagLayout());
						scroll.setViewportView(rowsPanel);
						scroll.getVerticalScrollBar().setUnitIncrement(70);
					}
				}
			}
			rightSplit.setDividerLocation(200);

		}
		{
			JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			leftSplit.setUI(new BasicSplitPaneUI() {
				public void paint (Graphics g, JComponent jc) {
				}
			});
			leftSplit.setDividerSize(4);
			splitPane.add(leftSplit, JSplitPane.LEFT);
			{
				JPanel spacer = new JPanel(new BorderLayout());
				leftSplit.add(spacer, JSplitPane.TOP);
				spacer.add(lwjglCanvas.getCanvas());
				spacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
			}
			{
				JPanel emittersPanel = new JPanel(new BorderLayout());
				leftSplit.add(emittersPanel, JSplitPane.BOTTOM);
				emittersPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(0, 6, 6, 0), BorderFactory
					.createTitledBorder("Effect Emitters")));
				{
					effectPanel = new EffectPanel(this);
					emittersPanel.add(effectPanel);
				}
			}
			leftSplit.setDividerLocation(625);
		}
		splitPane.setDividerLocation(325);
	}

	class Renderer implements ApplicationListener, InputProcessor {
		private float maxActiveTimer;
		private int maxActive, lastMaxActive;
		private boolean mouseDown;
		private int activeCount;
		private int mouseX, mouseY;
		private BitmapFont font;
		private SpriteBatch spriteBatch;
		private Sprite bgImage; // BOZO - Add setting background image to UI.

		public void create () {
			if (spriteBatch != null) return;

			Texture.setEnforcePotImages(false);

			spriteBatch = new SpriteBatch();

			worldCamera = new OrthographicCamera();
			textCamera = new OrthographicCamera();

			pixelsPerMeter = new NumericValue();
			pixelsPerMeter.setValue(1.0f);
			pixelsPerMeter.setAlwaysActive(true);

			zoomLevel = new NumericValue();
			zoomLevel.setValue(1.0f);
			zoomLevel.setAlwaysActive(true);
			
			deltaMultiplier = new NumericValue();
			deltaMultiplier.setValue(1.0f);
			deltaMultiplier.setAlwaysActive(true);

			font = new BitmapFont(Gdx.files.getFileHandle("default.fnt", FileType.Internal), Gdx.files.getFileHandle("default.png",
				FileType.Internal), true);
			effectPanel.newEmitter("Untitled", true);
			// if (resources.openFile("/editor-bg.png") != null) bgImage = new Image(gl, "/editor-bg.png");
			Gdx.input.setInputProcessor(this);
		}

		@Override
		public void resize (int width, int height) {
			Gdx.gl.glViewport(0, 0, width, height);

			if (pixelsPerMeter.getValue() <= 0) {
				pixelsPerMeter.setValue(1);
			}
			worldCamera.setToOrtho(false, width / pixelsPerMeter.getValue(), height / pixelsPerMeter.getValue());
			worldCamera.update();

			textCamera.setToOrtho(true, width, height);
			textCamera.update();

			effect.setPosition(worldCamera.viewportWidth / 2, worldCamera.viewportHeight / 2);
		}

		public void render () {
			int viewWidth = Gdx.graphics.getWidth();
			int viewHeight = Gdx.graphics.getHeight();

			float delta = Math.max(0, Gdx.graphics.getDeltaTime() * deltaMultiplier.getValue());

			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

			if ((pixelsPerMeter.getValue() != pixelsPerMeterPrev) || (zoomLevel.getValue() != zoomLevelPrev)) {
				if (pixelsPerMeter.getValue() <= 0) {
					pixelsPerMeter.setValue(1);
				}

				worldCamera.setToOrtho(false, viewWidth / pixelsPerMeter.getValue(), viewHeight / pixelsPerMeter.getValue());
				worldCamera.zoom = zoomLevel.getValue();
				worldCamera.update();
				effect.setPosition(worldCamera.viewportWidth / 2, worldCamera.viewportHeight / 2);
				zoomLevelPrev = zoomLevel.getValue();
				pixelsPerMeterPrev = pixelsPerMeter.getValue();
			}
			
			spriteBatch.setProjectionMatrix(worldCamera.combined);

			spriteBatch.begin();
			spriteBatch.enableBlending();
			spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

			if (bgImage != null) {
				bgImage.setPosition(viewWidth / 2 - bgImage.getWidth() / 2, viewHeight / 2 - bgImage.getHeight() / 2);
				bgImage.draw(spriteBatch);
			}

			activeCount = 0;
			boolean complete = true;
			for (ParticleEmitter emitter : effect.getEmitters()) {
				if (emitter.getSprite() == null && emitter.getImagePath() != null) loadImage(emitter);
				boolean enabled = isEnabled(emitter);
				if (enabled) {
					if (emitter.getSprite() != null) emitter.draw(spriteBatch, delta);
					activeCount += emitter.getActiveCount();
					if (!emitter.isComplete()) complete = false;
				}
			}
			if (complete) effect.start();

			maxActive = Math.max(maxActive, activeCount);
			maxActiveTimer += delta;
			if (maxActiveTimer > 3) {
				maxActiveTimer = 0;
				lastMaxActive = maxActive;
				maxActive = 0;
			}

			if (mouseDown) {
				// gl.drawLine(mouseX - 6, mouseY, mouseX + 5, mouseY);
				// gl.drawLine(mouseX, mouseY - 5, mouseX, mouseY + 6);
			}

			spriteBatch.setProjectionMatrix(textCamera.combined);

			font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 15);
			font.draw(spriteBatch, "Count: " + activeCount, 5, 35);
			font.draw(spriteBatch, "Max: " + lastMaxActive, 5, 55);
			font.draw(spriteBatch, (int)(getEmitter().getPercentComplete() * 100) + "%", 5, 75);

			spriteBatch.end();

			// gl.drawLine((int)(viewWidth * getCurrentParticles().getPercentComplete()), viewHeight - 1, viewWidth, viewHeight -
			// 1);
		}

		private void loadImage (ParticleEmitter emitter) {
			final String imagePath = emitter.getImagePath();
			String imageName = new File(imagePath.replace('\\', '/')).getName();
			try {
				FileHandle file;
				if (imagePath.equals("particle.png"))
					file = Gdx.files.classpath(imagePath);
				else
					file = Gdx.files.absolute(imagePath);
				emitter.setSprite(new Sprite(new Texture(file)));
			} catch (GdxRuntimeException ex) {
				ex.printStackTrace();
				EventQueue.invokeLater(new Runnable() {
					public void run () {
						JOptionPane.showMessageDialog(ParticleEditor.this, "Error loading image:\n" + imagePath);
					}
				});
				emitter.setImagePath(null);
			}
		}

		public boolean keyDown (int keycode) {
			return false;
		}

		public boolean keyUp (int keycode) {
			return false;
		}

		public boolean keyTyped (char character) {
			return false;
		}

		public boolean touchDown (int x, int y, int pointer, int newParam) {
			Vector3 touchPoint = new Vector3(x, y, 0);
			worldCamera.unproject(touchPoint);
			effect.setPosition(touchPoint.x, touchPoint.y);
			return false;
		}

		public boolean touchUp (int x, int y, int pointer, int button) {
			return false;
		}

		public boolean touchDragged (int x, int y, int pointer) {
			Vector3 touchPoint = new Vector3(x, y, 0);
			worldCamera.unproject(touchPoint);
			effect.setPosition(touchPoint.x, touchPoint.y);
			return false;
		}

		@Override
		public void dispose () {
		}

		@Override
		public void pause () {
		}

		@Override
		public void resume () {
		}

		@Override
		public boolean mouseMoved (int x, int y) {
			return false;
		}

		@Override
		public boolean scrolled (int amount) {
			return false;
		}
	}

	static class ParticleData {
		public ImageIcon icon;
		public String imagePath;
		public boolean enabled = true;
	}

	public static void main (String[] args) {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (Throwable ignored) {
				}
				break;
			}
		}
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				new ParticleEditor();
			}
		});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9691.java