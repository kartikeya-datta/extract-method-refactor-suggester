error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/140.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/140.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/140.java
text:
```scala
@Override public v@@oid dispose () {


package com.badlogic.gdx.graphics.particles;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.desktop.LwjglApplication;
import com.badlogic.gdx.backends.desktop.LwjglCanvas;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ParticleEditor extends JFrame {
	LwjglCanvas lwjglCanvas;
	JPanel rowsPanel;
	EffectPanel effectPanel;
	private JSplitPane splitPane;

	ParticleEffect effect = new ParticleEffect();
	final HashMap<ParticleEmitter, ParticleData> particleData = new HashMap();

	public ParticleEditor () {
		super("GDX Particle Editor");

		lwjglCanvas = new LwjglCanvas(new Renderer(), false);
		addWindowListener(new WindowAdapter() {
			public void windowClosed (WindowEvent event) {
				System.exit(0);
				// Gdx.app.quit();
			}
		});

		initializeComponents();

		setSize(950, 950);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	void reloadRows () {
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				rowsPanel.removeAll();
				ParticleEmitter emitter = getEmitter();
				addRow(new ImagePanel(ParticleEditor.this));
				addRow(new RangedNumericPanel("Delay", emitter.getDelay()));
				addRow(new RangedNumericPanel("Duration", emitter.getDuration()));
				addRow(new CountPanel(ParticleEditor.this));
				addRow(new ScaledNumericPanel("Emission", "Duration", emitter.getEmission()));
				addRow(new ScaledNumericPanel("Life", "Duration", emitter.getLife()));
				addRow(new ScaledNumericPanel("Life Offset", "Duration", emitter.getLifeOffset()));
				addRow(new RangedNumericPanel("X Offset", emitter.getXOffsetValue()));
				addRow(new RangedNumericPanel("Y Offset", emitter.getYOffsetValue()));
				addRow(new SpawnPanel(emitter.getSpawnShape(), ParticleEditor.this));
				addRow(new ScaledNumericPanel("Spawn Width", "Duration", emitter.getSpawnWidth()));
				addRow(new ScaledNumericPanel("Spawn Height", "Duration", emitter.getSpawnHeight()));
				addRow(new ScaledNumericPanel("Size", "Life", emitter.getScale()));
				addRow(new ScaledNumericPanel("Velocity", "Life", emitter.getVelocity()));
				addRow(new ScaledNumericPanel("Angle", "Life", emitter.getAngle()));
				addRow(new ScaledNumericPanel("Rotation", "Life", emitter.getRotation()));
				addRow(new ScaledNumericPanel("Wind", "Life", emitter.getWind()));
				addRow(new ScaledNumericPanel("Gravity", "Life", emitter.getGravity()));
				addRow(new GradientPanel("Tint", emitter.getTint()));
				addRow(new PercentagePanel("Transparency", "Life", emitter.getTransparency()));
				addRow(new OptionsPanel(ParticleEditor.this));
				for (Component component : rowsPanel.getComponents())
					if (component instanceof EditorPanel) ((EditorPanel)component).update(ParticleEditor.this);
				rowsPanel.repaint();
			}
		});
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
			JPanel propertiesPanel = new JPanel(new GridBagLayout());
			splitPane.add(propertiesPanel, JSplitPane.RIGHT);
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
			spriteBatch = new SpriteBatch();

			font = new BitmapFont(Gdx.files.getFileHandle("data/default.fnt", FileType.Internal), Gdx.files.getFileHandle(
				"data/default.png", FileType.Internal), true);
			effectPanel.newEmitter("Untitled", true);
			// if (resources.openFile("/editor-bg.png") != null) bgImage = new Image(gl, "/editor-bg.png");
		}

		@Override public void resize (int width, int height) {
			Gdx.gl.glViewport(0, 0, width, height);
			spriteBatch.getProjectionMatrix().setToOrtho(0, width, height, 0, 0, 1);

			effect.setPosition(width / 2, height / 2);
		}

		public void render () {
			int viewWidth = Gdx.graphics.getWidth();
			int viewHeight = Gdx.graphics.getHeight();

			float delta = Gdx.graphics.getDeltaTime();

			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

			spriteBatch.begin();
			spriteBatch.enableBlending();
			spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

			if (bgImage != null) {
				bgImage.setPosition(viewWidth / 2 - bgImage.getWidth() / 2, viewHeight / 2 - bgImage.getHeight() / 2);
				bgImage.draw(spriteBatch);
			}

			activeCount = 0;
			for (ParticleEmitter emitter : effect.getEmitters()) {
				if (emitter.getTexture() == null && emitter.getImagePath() != null) loadImage(emitter);
				boolean enabled = isEnabled(emitter);
				if (enabled) {
					if (emitter.getTexture() != null) emitter.draw(spriteBatch, delta);
					activeCount += emitter.getActiveCount();
				}
			}
			if (effect.isComplete()) effect.start();

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

			font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 10, Color.WHITE);
			font.draw(spriteBatch, "Count: " + activeCount, 10, 30, Color.WHITE);
			font.draw(spriteBatch, "Max: " + lastMaxActive, 10, 50, Color.WHITE);
			font.draw(spriteBatch, (int)(getEmitter().getPercentComplete() * 100) + "%", 10, 70, Color.WHITE);

			spriteBatch.end();

			// gl.drawLine((int)(viewWidth * getCurrentParticles().getPercentComplete()), viewHeight - 1, viewWidth, viewHeight -
// 1);
			Gdx.input.processEvents(this);
		}

		private void loadImage (ParticleEmitter emitter) {
			final String imagePath = emitter.getImagePath();
			try {
				emitter.setTexture(Gdx.graphics.newTexture(Gdx.files.getFileHandle(imagePath, FileType.Absolute),
					TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge));
			} catch (GdxRuntimeException ex) {
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

		public boolean touchDown (int x, int y, int pointer) {
			effect.setPosition(x, y);
			return false;
		}

		public boolean touchUp (int x, int y, int pointer) {
			return false;
		}

		public boolean touchDragged (int x, int y, int pointer) {
			effect.setPosition(x, y);
			return false;
		}

		public void dispose () {
		}

		@Override public void destroy () {
			// TODO Auto-generated method stub

		}

		@Override public void pause () {
			// TODO Auto-generated method stub

		}

		@Override public void resume () {
			// TODO Auto-generated method stub

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
			}
		});
		new ParticleEditor();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/140.java