error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7967.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7967.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7967.java
text:
```scala
E@@lement properties = element.getChildByName("Properties");

package com.badlogic.gdx.maps.tiled;

import java.io.IOException;
import java.util.StringTokenizer;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.ImageResolver.AssetManagerImageResolver;
import com.badlogic.gdx.maps.ImageResolver.DirectImageResolver;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class TideMapLoader extends SynchronousAssetLoader<TiledMap, TideMapLoader.Parameters> {

	public static class Parameters extends AssetLoaderParameters<TiledMap> {
		
	}
	
	private XmlReader xml = new XmlReader();
	private Element root;
	
	public TideMapLoader() {
		super(new InternalFileHandleResolver());
	}
	
	public TideMapLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public TiledMap load (String fileName) {
		try {
			FileHandle tideFile = resolve(fileName);
			root = xml.parse(tideFile);
			ObjectMap<String, Texture> textures = new ObjectMap<String, Texture>();
			for(FileHandle textureFile: loadTileSheets(root, tideFile)) {
				textures.put(textureFile.path(), new Texture(textureFile));
			}
			DirectImageResolver imageResolver = new DirectImageResolver(textures);
			TiledMap map = loadMap(root, tideFile, imageResolver);
			map.setOwnedTextures(textures.values().toArray());
			return map;
		} catch(IOException e) {
			throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
		}

	}
	
	@Override
	public TiledMap load (AssetManager assetManager, String fileName, Parameters parameter) {
		FileHandle tideFile = resolve(fileName);
		try {
			return loadMap(root, tideFile, new AssetManagerImageResolver(assetManager));
		} catch (Exception e) {
			throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
		}
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, Parameters parameter) {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		try {
			FileHandle tmxFile = resolve(fileName);
			root = xml.parse(tmxFile);
			for(FileHandle image: loadTileSheets(root, tmxFile)) {
				dependencies.add(new AssetDescriptor(image.path(), Texture.class));
			}
			return dependencies;
		} catch (IOException e) {
			throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
		}
	}
	
	/**
	 * Loads the map data, given the XML root element and an {@link ImageResolver} used
	 * to return the tileset Textures
	 * @param root the XML root element 
	 * @param tmxFile the Filehandle of the tmx file
	 * @param imageResolver the {@link ImageResolver}
	 * @return the {@link TiledMap}
	 */
	private TiledMap loadMap(Element root, FileHandle tmxFile, ImageResolver imageResolver) {
		TiledMap map = new TiledMap();
		Element properties = root.getChildByName("properties");
		if (properties != null) {
			loadProperties(map.getProperties(), properties);
		}
		Element tilesheets = root.getChildByName("TileSheets");
		for (Element tilesheet : tilesheets.getChildrenByName("TileSheet")) {
			loadTileSheet(map, tilesheet, tmxFile, imageResolver);
		}
		Element layers = root.getChildByName("Layers");
		for (Element layer : layers.getChildrenByName("Layer")) {
			loadLayer(map, layer);
		}
		return map;
	}
	
	/**
	 * Loads the tilesets
	 * @param root the root XML element
	 * @return a list of filenames for images containing tiles
	 * @throws IOException 
	 */
	private Array<FileHandle> loadTileSheets(Element root, FileHandle tideFile) throws IOException {
		Array<FileHandle> images = new Array<FileHandle>();
		Element tilesheets = root.getChildByName("TileSheets");
		for (Element tileset : tilesheets.getChildrenByName("TileSheet")) {
			Element imageSource = tileset.getChildByName("ImageSource");
			FileHandle image = getRelativeFileHandle(tideFile, imageSource.getText());
			images.add(image);
		}
		return images;
	}
	
	private void loadTileSheet(TiledMap map, Element element, FileHandle tideFile, ImageResolver imageResolver) {
		if (element.getName().equals("TileSheet")) {
			String id = element.getAttribute("Id");
			String description = element.getChildByName("Description").getText();
			String imageSource = element.getChildByName("ImageSource").getText();
			
			Element alignment = element.getChildByName("Alignment");
			String sheetSize = alignment.getAttribute("SheetSize");
			String tileSize = alignment.getAttribute("TileSize");
			String margin = alignment.getAttribute("Margin");
			String spacing = alignment.getAttribute("Spacing");
			
			String[] sheetSizeParts = sheetSize.split(" x ");
			int sheetSizeX = Integer.parseInt(sheetSizeParts[0]);
			int sheetSizeY = Integer.parseInt(sheetSizeParts[1]);
			
			String[] tileSizeParts = tileSize.split(" x ");
			int tileSizeX = Integer.parseInt(tileSizeParts[0]);
			int tileSizeY = Integer.parseInt(tileSizeParts[1]);
			
			String[] marginParts = margin.split(" x ");
			int marginX = Integer.parseInt(marginParts[0]);
			int marginY = Integer.parseInt(marginParts[1]);

			String[] spacingParts = margin.split(" x ");
			int spacingX = Integer.parseInt(spacingParts[0]);
			int spacingY = Integer.parseInt(spacingParts[1]);

			FileHandle image = getRelativeFileHandle(tideFile, imageSource);
			TextureRegion texture = imageResolver.getImage(image.path());
			
			// TODO: Actually load the tilesheet
			// Need to make global ids as Tide doesn't have global ids.
			TiledMapTileSets tilesets = map.getTileSets();
			int firstgid = 1;
			for (TiledMapTileSet tileset : tilesets) {
				firstgid += tileset.size();
			}
			
			TiledMapTileSet tileset = new TiledMapTileSet();
			tileset.setName(id);
			tileset.getProperties().put("firstgid", firstgid);
			int gid = firstgid;
			
			int stopWidth = texture.getRegionWidth() - tileSizeX;
			int stopHeight = texture.getRegionHeight() - tileSizeY;
			
			for (int y = marginY; y <= stopHeight; y += tileSizeY + spacingY) {
				for (int x = marginX; x <= stopWidth; x += tileSizeX + spacingX) {
					TiledMapTile tile = new StaticTiledMapTile(new TextureRegion(texture, x, y, tileSizeX, tileSizeY));
					tile.setId(gid);
					tileset.putTile(gid++, tile);
				}
			}
			
			Element properties = element.getChildByName("Proeprties");
			if (properties != null) {
				loadProperties(tileset.getProperties(), properties);
			}
			
			tilesets.addTileSet(tileset);
		}
	}
	
	private void loadLayer(TiledMap map, Element element) {
		if (element.getName().equals("Layer")) {
			String id = element.getAttribute("Id");
			String visible = element.getAttribute("Visible");
			
			Element dimensions = element.getChildByName("Dimensions");
			String layerSize = dimensions.getAttribute("LayerSize");
			String tileSize = dimensions.getAttribute("TileSize");
			
			String[] layerSizeParts = layerSize.split(" x ");
			int layerSizeX = Integer.parseInt(layerSizeParts[0]);
			int layerSizeY = Integer.parseInt(layerSizeParts[1]);
			
			String[] tileSizeParts = tileSize.split(" x ");
			int tileSizeX = Integer.parseInt(tileSizeParts[0]);
			int tileSizeY = Integer.parseInt(tileSizeParts[1]);
			
			TiledMapTileLayer layer = new TiledMapTileLayer(layerSizeX, layerSizeY, tileSizeX, tileSizeY);
			Element tileArray = element.getChildByName("TileArray");
			Array<Element> rows = tileArray.getChildrenByName("Row");
			TiledMapTileSets tilesets = map.getTileSets();
			TiledMapTileSet currentTileSet = null;
			int firstgid = 0;
			int x, y;
			for (int row = 0, rowCount = rows.size; row < rowCount; row++) {
				Element currentRow = rows.get(row);
				y = rowCount -1 - row;
				x = 0;
				for (int child = 0, childCount = currentRow.getChildCount(); child < childCount; child++) {
					Element currentChild = currentRow.getChild(child);
					String name = currentChild.getName();
					if (name.equals("TileSheet")) {
						currentTileSet = tilesets.getTileSet(currentChild.getAttribute("Ref"));
						firstgid = currentTileSet.getProperties().get("firstgid", Integer.class);
					} else if (name.equals("Null")) {
						x += currentChild.getIntAttribute("Count");
					} else if (name.equals("Static")) {
						Cell cell = new Cell();
						cell.setTile(currentTileSet.getTile(firstgid + currentChild.getIntAttribute("Index")));
						layer.setCell(x++, y, cell);
					} else if (name.equals("Animated")) {
						// Create an AnimatedTile
						int interval = currentChild.getInt("Interval");
						Element frames = currentChild.getChildByName("Frames");
						Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>();
						for (int frameChild = 0, frameChildCount = frames.getChildCount(); frameChild < frameChildCount; frameChild++) {
							Element frame = frames.getChild(frameChild);
							String frameName = frame.getName();
							if (frameName.equals("TileSheet")) {
								currentTileSet = tilesets.getTileSet(frame.getAttribute("Ref"));
								firstgid = currentTileSet.getProperties().get("firstgid", Integer.class);
							} else if (frameName.equals("Static")) {
								frameTiles.add((StaticTiledMapTile) currentTileSet.getTile(firstgid + frame.getIntAttribute("Index")));
							}
						}
						Cell cell = new Cell();
						cell.setTile(new AnimatedTiledMapTile(interval / 1000f, frameTiles));
						layer.setCell(x++, y, cell); //TODO: Reuse existing animated tiles
					}
				}
			}
			map.getLayers().add(layer);
		}
	}
	
	private void loadProperties(MapProperties properties, Element element) {
		if (element.getName().equals("Properties")) {
			for (Element property : element.getChildrenByName("Property")) {
				String key = property.getAttribute("Key", null);
				String type = property.getAttribute("Type", null);
				String value = property.getText();
				
				if (type.equals("Int32")) {
					
				} else if (type.equals("String")) {
					
				} else if (type.equals("Boolean")) {
					
				} else {
					properties.put(key, value);					
				}
			}
		}
	}
	
	private static FileHandle getRelativeFileHandle(FileHandle file, String path) {
		StringTokenizer tokenizer = new StringTokenizer(path, "\\/");
		FileHandle result = file.parent();
		while (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken();
			if (token.equals(".."))
				result = result.parent();
			else {
				result = result.child(token);
			}
		}
		return result;		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7967.java