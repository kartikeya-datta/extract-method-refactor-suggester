error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7569.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7569.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7569.java
text:
```scala
u@@sedIds.add(layer.tiles[y][x]);

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tiledmappacker;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TileSet;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;
import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;

/** Packs a Tiled Map, adding some properties to improve the speed of the {@link TileMapRenderer}. Also runs the texture packer on
 * the tiles for use with a {@link TileAtlas}
 * @author David Fraska */
public class TiledMapPacker {

	private TexturePacker packer;
	private TiledMap map;

	// private File outputDir;
	private ArrayList<String> processedTileSets = new ArrayList<String>();

	private ArrayList<Integer> blendedTiles = new ArrayList<Integer>();

	static private class TmxFilter implements FilenameFilter {

		public TmxFilter () {
		}

		@Override
		public boolean accept (File dir, String name) {
			if (name.endsWith(".tmx")) return true;

			return false;
		}

	}

	TiledMapPackerSettings settings;
	
	public TiledMapPacker() {
		this(new TiledMapPackerSettings());
	}
	
	public TiledMapPacker(TiledMapPackerSettings settings) {
		this.settings = settings;
	}
	
	/** Typically, you should run the {@link TiledMapPacker#main(String[])} method instead of this method. Packs a directory of
	 * Tiled Maps, adding properties to improve the speed of the {@link TileMapRenderer}. Also runs the texture packer on the tile
	 * sets for use with a {@link TileAtlas}
	 * @param inputDir the input directory containing the tmx files (and tile sets, relative to the path listed in the tmx file)
	 * @param outputDir The output directory for the tmx files, should be empty before running. WARNING: Use caution if you have a
	 *           "../" in the path of your tile sets! The output for these tile sets will be relative to the output directory. For
	 *           example, if your output directory is "C:\mydir\maps" and you have a tileset with the path "../tileset.png", the
	 *           tileset will be output to "C:\mydir\" and the maps will be in "C:\mydir\maps".
	 * @param settings the settings used in the TexturePacker */
	public void processMap (File inputDir, File outputDir, Settings settings) throws IOException {
		FileHandle inputDirHandle = new FileHandle(inputDir.getAbsolutePath());
		File[] files = inputDir.listFiles(new TmxFilter());

		for (File file : files) {
			map = TiledLoader.createMap(new FileHandle(file.getAbsolutePath()));

			IntArray usedIds = null;
			if (this.settings.stripUnusedTiles) {
				usedIds = new IntArray(map.layers.size() * map.height * map.width);
				for (TiledLayer layer : map.layers) {
					for (int y = 0; y < layer.tiles.length; ++y) {
						for (int x = 0; x < layer.tiles[y].length; ++x) {
							usedIds.add(layer.tiles[y][x] & ~0xE0000000);
						}
					}
				}
			}			
			
			for (TileSet set : map.tileSets) {
				if (!processedTileSets.contains(set.imageName)) {
					processedTileSets.add(set.imageName);
					packTileSet(set, usedIds, inputDirHandle, outputDir, settings);
				}
			}

			writeUpdatedTMX(outputDir, map.tmxFile);
		}
	}

	private void packTileSet (TileSet set, IntArray usedIds, FileHandle inputDirHandle, File outputDir, Settings settings) throws IOException {
		BufferedImage tile;
		Vector2 tileLocation;
		TileSetLayout packerTileSet;
		Graphics g;

		packer = new TexturePacker(settings);

		TileSetLayout layout = new TileSetLayout(set, inputDirHandle);

		for (int gid = layout.firstgid, i = 0; i < layout.numTiles; gid++, i++) {
			if (usedIds != null && !usedIds.contains(gid)) {
				System.out.println("Stripped Id: " + gid);
				continue;
			}
				
			
			tileLocation = layout.getLocation(gid);
			tile = new BufferedImage(layout.tileWidth, layout.tileHeight, BufferedImage.TYPE_4BYTE_ABGR);

			g = tile.createGraphics();
			g.drawImage(layout.image, 0, 0, layout.tileWidth, layout.tileHeight, (int)tileLocation.x, (int)tileLocation.y,
				(int)tileLocation.x + layout.tileWidth, (int)tileLocation.y + layout.tileHeight, null);

			if (isBlended(tile)) setBlended(gid);

			packer.addImage(tile, removeExtension(removePath(set.imageName)) + "_" + i);
		}

		File outputFile = getRelativeFile(outputDir, removeExtension(set.imageName) + " packfile");
		outputFile.getParentFile().mkdirs();
		packer.process(outputFile.getParentFile(), outputFile, removeExtension(removePath(set.imageName)));
	}

	private static String removeExtension (String s) {
		int extensionIndex = s.lastIndexOf(".");
		if (extensionIndex == -1) return s;

		return s.substring(0, extensionIndex);
	}

	private static String removePath (String s) {
		String temp;

		int index = s.lastIndexOf('\\');
		if (index != -1)
			temp = s.substring(index + 1);
		else
			temp = s;

		index = temp.lastIndexOf('/');
		if (index != -1)
			return s.substring(index + 1);
		else
			return s;
	}

	private static File getRelativeFile (File path, String relativePath) {
		if (relativePath.trim().length() == 0) return path;

		File child = path;

		StringTokenizer tokenizer = new StringTokenizer(relativePath, "\\/");
		while (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken();
			if (token.equals(".."))
				child = child.getParentFile();
			else {
				child = new File(child, token);
			}
		}

		return child;
	}

	private void setBlended (int tileNum) {
		blendedTiles.add(tileNum);
	}

	private void writeUpdatedTMX (File outputDir, FileHandle tmxFileHandle) throws IOException {
		Document doc;
		DocumentBuilder docBuilder;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(tmxFileHandle.read());

			Node map = doc.getFirstChild();
			while (map.getNodeType() != Node.ELEMENT_NODE || map.getNodeName() != "map") {
				if ((map = map.getNextSibling()) == null) {
					throw new GdxRuntimeException("Couldn't find map node!");
				}
			}

			setProperty(doc, map, "blended tiles", toCSV(blendedTiles));

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			outputDir.mkdirs();
			StreamResult result = new StreamResult(new File(outputDir, tmxFileHandle.name()));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			throw new RuntimeException("ParserConfigurationException: " + e.getMessage());
		} catch (SAXException e) {
			throw new RuntimeException("SAXException: " + e.getMessage());
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("TransformerConfigurationException: " + e.getMessage());
		} catch (TransformerException e) {
			throw new RuntimeException("TransformerException: " + e.getMessage());
		}
	}

	private static void setProperty (Document doc, Node parent, String name, String value) {
		Node properties = getFirstChildNodeByName(parent, "properties");
		Node property = getFirstChildByNameAttrValue(properties, "property", "name", name);

		NamedNodeMap attributes = property.getAttributes();
		Node valueNode = attributes.getNamedItem("value");
		if (valueNode == null) {
			valueNode = doc.createAttribute("value");
			valueNode.setNodeValue(value);
			attributes.setNamedItem(valueNode);
		} else {
			valueNode.setNodeValue(value);
		}
	}

	private static String toCSV (ArrayList<Integer> values) {
		String temp = "";
		for (int i = 0; i < values.size() - 1; i++) {
			temp += values.get(i) + ",";
		}
		if (values.size() > 0) temp += values.get(values.size() - 1);
		return temp;
	}

	/** If the child node doesn't exist, it is created. */
	private static Node getFirstChildNodeByName (Node parent, String child) {
		NodeList childNodes = parent.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeName().equals(child)) {
				return childNodes.item(i);
			}
		}

		Node newNode = parent.getOwnerDocument().createElement(child);

		if (childNodes.item(0) != null)
			return parent.insertBefore(newNode, childNodes.item(0));
		else
			return parent.appendChild(newNode);
	}

	/** If the child node or attribute doesn't exist, it is created. Usage example: Node property =
	 * getFirstChildByAttrValue(properties, "property", "name", "blended tiles"); */
	private static Node getFirstChildByNameAttrValue (Node node, String childName, String attr, String value) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeName().equals(childName)) {
				NamedNodeMap attributes = childNodes.item(i).getAttributes();
				Node attribute = attributes.getNamedItem(attr);
				if (attribute.getNodeValue().equals(value)) return childNodes.item(i);
			}
		}

		Node newNode = node.getOwnerDocument().createElement(childName);
		NamedNodeMap attributes = newNode.getAttributes();

		Attr nodeAttr = node.getOwnerDocument().createAttribute(attr);
		nodeAttr.setNodeValue(value);
		attributes.setNamedItem(nodeAttr);

		if (childNodes.item(0) != null) {
			return node.insertBefore(newNode, childNodes.item(0));
		} else {
			return node.appendChild(newNode);
		}
	}

	private static boolean isBlended (BufferedImage tile) {
		int[] rgbArray = new int[tile.getWidth() * tile.getHeight()];
		tile.getRGB(0, 0, tile.getWidth(), tile.getHeight(), rgbArray, 0, tile.getWidth());
		for (int i = 0; i < tile.getWidth() * tile.getHeight(); i++) {
			if (((rgbArray[i] >> 24) & 0xff) != 255) {
				return true;
			}
		}
		return false;
	}

	private TileSetLayout getTileSetLayout (int tileNum, FileHandle inputDirHandle) throws IOException {
		int firstgid = 0;
		int lastgid;

		for (TileSet set : map.tileSets) {
			TileSetLayout layout = new TileSetLayout(set, inputDirHandle);
			firstgid = set.firstgid;
			lastgid = firstgid + layout.numTiles - 1;
			if (tileNum >= firstgid && tileNum <= lastgid) {
				return layout;
			}
		}

		return null;
	}
	
	/** Processes a directory of Tile Maps, compressing each tile set contained in any map once.
	 * @param args args[0]: the input directory containing the tmx files (and tile sets, relative to the path listed in the tmx
	 *           file). args[1]: The output directory for the tmx files, should be empty before running. WARNING: Use caution if
	 *           you have a "../" in the path of your tile sets! The output for these tile sets will be relative to the output
	 *           directory. For example, if your output directory is "C:\mydir\output" and you have a tileset with the path
	 *           "../tileset.png", the tileset will be output to "C:\mydir\" and the maps will be in "C:\mydir\output".
	 *           args[2]: --strip-unused (optional, include to let the TiledMapPacker remove tiles which are not used. */
	public static void main (String[] args) {
		File inputDir = null;
		File outputDir = null;

		Settings texturePackerSettings = new Settings();
		
		texturePackerSettings.padding = 2;
		texturePackerSettings.duplicatePadding = true;

		TiledMapPackerSettings packerSettings = new TiledMapPackerSettings();
		
		switch (args.length) {
			case 3: {
				inputDir = new File(args[0]);
				outputDir = new File(args[1]);
				if ("--strip-unused".equals(args[2])) {
					packerSettings.stripUnusedTiles = true;
				}
				break;
			}
			case 2: {
				inputDir = new File(args[0]);
				outputDir = new File(args[1]);
				break;
			}
			case 1: {
				inputDir = new File(args[0]);
				outputDir = new File(inputDir, "output/");
				break;
			}
			default: {
				System.out.println("Usage: INPUTDIR [OUTPUTDIR] [--strip-unused]");
				System.exit(0);
			}
		}
		
		TiledMapPacker packer = new TiledMapPacker(packerSettings);

		if (!inputDir.exists()) {
			throw new RuntimeException("Input directory does not exist");
		}

		try {
			packer.processMap(inputDir, outputDir, texturePackerSettings);
		} catch (IOException e) {
			throw new RuntimeException("Error processing map: " + e.getMessage());
		}
		
	}
	
	public static class TiledMapPackerSettings {
		public boolean stripUnusedTiles = false;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7569.java