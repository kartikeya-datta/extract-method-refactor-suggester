error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5499.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5499.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5499.java
text:
```scala
static private P@@attern indexPattern = Pattern.compile("(.+)_(\\d+)$");


package com.badlogic.gdx.tools.imagepacker;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Rect;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.badlogic.gdx.utils.Array;

public class ImageProcessor {
	static private final BufferedImage emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
	static private Pattern indexPattern = Pattern.compile("(.+)(\\d+)$");

	private String rootPath;
	private final Settings settings;
	private final HashMap<String, Rect> crcs = new HashMap();
	private final Array<Rect> rects = new Array();

	public ImageProcessor (File rootDir, Settings settings) {
		this.settings = settings;

		rootPath = rootDir.getAbsolutePath().replace('\\', '/');
		if (!rootPath.endsWith("/")) rootPath += "/";
	}

	public void addImage (File file) {
		BufferedImage image;
		try {
			image = ImageIO.read(file);
		} catch (IOException ex) {
			throw new RuntimeException("Error reading image: " + file, ex);
		}
		if (image == null) throw new RuntimeException("Unable to read image: " + file);

		// Strip root dir off front of image path.
		String name = file.getAbsolutePath().replace('\\', '/');
		if (!name.startsWith(rootPath)) throw new RuntimeException("Path '" + name + "' does not start with root: " + rootPath);
		name = name.substring(rootPath.length());

		// Strip extension.
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex != -1) name = name.substring(0, dotIndex);

		Rect rect = null;

		// Strip ".9" from file name, read ninepatch split pixels, and strip ninepatch split pixels.
		int[] splits = null;
		if (name.endsWith(".9")) {
			name = name.substring(0, name.length() - 2);
			splits = getSplits(image, name);
			// Strip split pixels.
			BufferedImage newImage = new BufferedImage(image.getWidth() - 2, image.getHeight() - 2, BufferedImage.TYPE_4BYTE_ABGR);
			newImage.getGraphics().drawImage(image, 0, 0, newImage.getWidth(), newImage.getHeight(), 1, 1, image.getWidth() - 1,
				image.getHeight() - 1, null);
			image = newImage;
			// Ninepatches won't be rotated or whitespace stripped.
			rect = new Rect(image, 0, 0, image.getWidth(), image.getHeight());
			rect.splits = splits;
			rect.canRotate = false;
		}

		// Strip digits off end of name and use as index.
		Matcher matcher = indexPattern.matcher(name);
		int index = -1;
		if (matcher.matches()) {
			name = matcher.group(1);
			index = Integer.parseInt(matcher.group(2));
		}

		if (rect == null) {
			rect = createRect(image);
			if (rect == null) {
				System.out.println("Ignoring blank input image: " + name);
				return;
			}
		}

		rect.name = name;
		rect.index = index;

		if (settings.alias) {
			String crc = hash(rect.image);
			Rect existing = crcs.get(crc);
			if (existing != null) {
				existing.aliases.add(rect);
				return;
			}
			crcs.put(crc, rect);
		}

		rects.add(rect);
	}

	public Array<Rect> getImages () {
		return rects;
	}

	/** Strips whitespace and returns the rect, or null if the image should be ignored. */
	private Rect createRect (BufferedImage source) {
		WritableRaster alphaRaster = source.getAlphaRaster();
		if (alphaRaster == null || (!settings.stripWhitespaceX && !settings.stripWhitespaceY))
			return new Rect(source, 0, 0, source.getWidth(), source.getHeight());
		final byte[] a = new byte[1];
		int top = 0;
		int bottom = source.getHeight();
		if (settings.stripWhitespaceX) {
			outer:
			for (int y = 0; y < source.getHeight(); y++) {
				for (int x = 0; x < source.getWidth(); x++) {
					alphaRaster.getDataElements(x, y, a);
					int alpha = a[0];
					if (alpha < 0) alpha += 256;
					if (alpha > settings.alphaThreshold) break outer;
				}
				top++;
			}
			outer:
			for (int y = source.getHeight(); --y >= top;) {
				for (int x = 0; x < source.getWidth(); x++) {
					alphaRaster.getDataElements(x, y, a);
					int alpha = a[0];
					if (alpha < 0) alpha += 256;
					if (alpha > settings.alphaThreshold) break outer;
				}
				bottom--;
			}
		}
		int left = 0;
		int right = source.getWidth();
		if (settings.stripWhitespaceY) {
			outer:
			for (int x = 0; x < source.getWidth(); x++) {
				for (int y = top; y < bottom; y++) {
					alphaRaster.getDataElements(x, y, a);
					int alpha = a[0];
					if (alpha < 0) alpha += 256;
					if (alpha > settings.alphaThreshold) break outer;
				}
				left++;
			}
			outer:
			for (int x = source.getWidth(); --x >= left;) {
				for (int y = top; y < bottom; y++) {
					alphaRaster.getDataElements(x, y, a);
					int alpha = a[0];
					if (alpha < 0) alpha += 256;
					if (alpha > settings.alphaThreshold) break outer;
				}
				right--;
			}
		}
		int newWidth = right - left;
		int newHeight = bottom - top;
		if (newWidth <= 0 || newHeight <= 0) {
			if (settings.ignoreBlankImages)
				return null;
			else
				return new Rect(emptyImage, 0, 0, 1, 1);
		}
		return new Rect(source, left, top, newWidth, newHeight);
	}

	/** Returns the splits, or null if the image had no splits or the splits were only a single region. Splits are an int[4] that
	 * has startX, endX, startY, endY. */
	private int[] getSplits (BufferedImage image, String name) {
		WritableRaster raster = image.getRaster();
		int[] rgba = new int[4];

		int startX = 1;
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			raster.getPixel(x, 0, rgba);
			if (rgba[3] == 0) continue;
			if (rgba[0] != 0 || rgba[1] != 0 || rgba[2] != 0)
				throw new RuntimeException("Unknown ninepatch splits pixel:" + x + ",0: " + name);
			startX = x;
			break;
		}
		int endX;
		for (endX = startX; endX < raster.getWidth() - 1; endX++) {
			raster.getPixel(endX, 0, rgba);
			if (rgba[3] == 0) break;
			if (rgba[0] != 0 || rgba[1] != 0 || rgba[2] != 0)
				throw new RuntimeException("Unknown ninepatch splits pixel " + endX + ",0: " + name);
		}
		for (int x = endX + 1; x < raster.getWidth() - 1; x++) {
			raster.getPixel(x, 0, rgba);
			if (rgba[3] != 0) throw new RuntimeException("Unknown ninepatch splits pixel " + x + ",0: " + name);
		}

		int startY = 1;
		for (int y = 1; y < raster.getHeight() - 1; y++) {
			raster.getPixel(0, y, rgba);
			if (rgba[3] == 0) continue;
			if (rgba[0] != 0 || rgba[1] != 0 || rgba[2] != 0)
				throw new RuntimeException("Unknown ninepatch splits pixel: 0," + y + ": " + name);
			startY = y;
			break;
		}
		int endY;
		for (endY = startY; endY < raster.getHeight() - 1; endY++) {
			raster.getPixel(0, endY, rgba);
			if (rgba[3] == 0) break;
			if (rgba[0] != 0 || rgba[1] != 0 || rgba[2] != 0)
				throw new RuntimeException("Unknown ninepatch splits pixel 0," + endY + ": " + name);
		}
		for (int y = endY + 1; y < raster.getHeight() - 1; y++) {
			raster.getPixel(0, y, rgba);
			if (rgba[3] != 0) throw new RuntimeException("Unknown ninepatch splits pixel 0," + y + ": " + name);
		}

		// No splits, or all splits.
		if (startX == 1 && endX == 1 && startY == 1 && endY == 1) return null;

		return new int[] {startX - 1, endX - 1, startY - 1, endY - 1};
	}

	static private String hash (BufferedImage image) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");
			WritableRaster raster = image.getRaster();
			int width = image.getWidth();
			final byte[] pixels = new byte[4 * width];
			for (int y = 0; y < image.getHeight(); y++) {
				raster.getDataElements(0, y, width, 1, pixels);
				digest.update(pixels);
			}
			return new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5499.java