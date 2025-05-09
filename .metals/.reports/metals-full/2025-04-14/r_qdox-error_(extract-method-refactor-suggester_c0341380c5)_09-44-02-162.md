error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10034.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10034.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10034.java
text:
```scala
i@@f (parameter != null && parameter.forceTextureFilters) {


package com.badlogic.gdx.maps.tiled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/** A TiledMap Loader which loads tiles from a TextureAtlas instead of separate images.
 * 
 * It requires a map-level property called 'atlas' with its value being the relative path to the TextureAtlas. The atlas must have
 * in it indexed regions named after the tilesets used in the map. The indexes shall be local to the tileset (not the global id).
 * Strip whitespace and rotation should not be used when creating the atlas.
 * 
 * @author Justin Shapcott
 * @author Manuel Bua */
public class AtlasTiledMapLoader extends
	AsynchronousAssetLoader<AtlasTiledMap, AtlasTiledMapLoader.AtlasTiledMapLoaderParameters> {

	public static class AtlasTiledMapLoaderParameters extends AssetLoaderParameters<AtlasTiledMap> {
		/** Whether to load the map for a y-up coordinate system */
		public boolean yUp = true;

		/** force texture filters? **/
		public boolean forceTextureFilters = false;

		/** The TextureFilter to use for minification, if forceTextureFilter is enabled **/
		public TextureFilter textureMinFilter = TextureFilter.Nearest;

		/** The TextureFilter to use for magnification, if forceTextureFilter is enabled **/
		public TextureFilter textureMagFilter = TextureFilter.Nearest;
	}

	protected static final int FLAG_FLIP_HORIZONTALLY = 0x80000000;
	protected static final int FLAG_FLIP_VERTICALLY = 0x40000000;
	protected static final int FLAG_FLIP_DIAGONALLY = 0x20000000;
	protected static final int MASK_CLEAR = 0xE0000000;

	protected XmlReader xml = new XmlReader();
	protected Element root;
	protected boolean yUp;

	protected int mapWidthInPixels;
	protected int mapHeightInPixels;

	protected AtlasTiledMap map;

	private interface AtlasResolver {

		public TextureAtlas getAtlas (String name);

		public static class DirectAtlasResolver implements AtlasResolver {

			private final ObjectMap<String, TextureAtlas> atlases;

			public DirectAtlasResolver (ObjectMap<String, TextureAtlas> atlases) {
				this.atlases = atlases;
			}

			@Override
			public TextureAtlas getAtlas (String name) {
				return atlases.get(name);
			}

		}

		public static class AssetManagerAtlasResolver implements AtlasResolver {
			private final AssetManager assetManager;

			public AssetManagerAtlasResolver (AssetManager assetManager) {
				this.assetManager = assetManager;
			}

			@Override
			public TextureAtlas getAtlas (String name) {
				return assetManager.get(name, TextureAtlas.class);
			}
		}
	}

	public AtlasTiledMapLoader () {
		super(new InternalFileHandleResolver());
	}

	public AtlasTiledMapLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public TiledMap load (String fileName) {
		return load(fileName, new AtlasTiledMapLoaderParameters());
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, AtlasTiledMapLoaderParameters parameter) {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		FileHandle tmxFile = resolve(fileName);
		try {
			root = xml.parse(tmxFile);

			Element properties = root.getChildByName("properties");
			if (properties != null) {
				for (Element property : properties.getChildrenByName("property")) {
					String name = property.getAttribute("name");
					String value = property.getAttribute("value");
					if (name.startsWith("atlas")) {
						FileHandle atlasHandle = getRelativeFileHandle(tmxFile, value);
						atlasHandle = resolve(atlasHandle.path());
						dependencies.add(new AssetDescriptor(atlasHandle.path(), TextureAtlas.class));
					}
				}
			}
		} catch (IOException e) {
			throw new GdxRuntimeException("Unable to parse .tmx file.");
		}
		return dependencies;
	}

	public AtlasTiledMap load (String fileName, AtlasTiledMapLoaderParameters parameter) {
		try {
			if (parameter != null) {
				yUp = parameter.yUp;
			} else {
				yUp = true;
			}

			FileHandle tmxFile = resolve(fileName);
			root = xml.parse(tmxFile);
			ObjectMap<String, TextureAtlas> atlases = new ObjectMap<String, TextureAtlas>();
			for (FileHandle atlasFile : loadAtlases(root, tmxFile)) {
				TextureAtlas atlas = new TextureAtlas(atlasFile);
				atlases.put(atlasFile.path(), atlas);
			}

			AtlasResolver.DirectAtlasResolver atlasResolver = new AtlasResolver.DirectAtlasResolver(atlases);
			AtlasTiledMap map = loadMap(root, tmxFile, atlasResolver, parameter);
			map.setOwnedAtlases(atlases.values().toArray());
			return map;
		} catch (IOException e) {
			throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
		}
	}

	protected Array<FileHandle> loadAtlases (Element root, FileHandle tmxFile) throws IOException {

		Element e = root.getChildByName("properties");
		Array<FileHandle> atlases = new Array<FileHandle>();

		if (e != null) {
			for (Element property : e.getChildrenByName("property")) {
				String name = property.getAttribute("name", null);
				String value = property.getAttribute("value", null);
				if (name.startsWith("atlas_")) {
					if (value == null) {
						value = property.getText();
					}
					FileHandle atlas = getRelativeFileHandle(tmxFile, value);
					atlases.add(atlas);
				}
			}
		}

		return atlases;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, AtlasTiledMapLoaderParameters parameter) {
		map = null;

		FileHandle tmxFile = resolve(fileName);
		if (parameter != null) {
			yUp = parameter.yUp;
		} else {
			yUp = true;
		}

		try {
			map = loadMap(root, tmxFile, new AtlasResolver.AssetManagerAtlasResolver(manager), parameter);
		} catch (Exception e) {
			throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
		}
	}

	@Override
	public AtlasTiledMap loadSync (AssetManager manager, String fileName, AtlasTiledMapLoaderParameters parameter) {
		return map;
	}

	protected AtlasTiledMap loadMap (Element root, FileHandle tmxFile, AtlasResolver resolver,
		AtlasTiledMapLoaderParameters parameter) {
		AtlasTiledMap map = new AtlasTiledMap();

		String mapOrientation = root.getAttribute("orientation", null);
		int mapWidth = root.getIntAttribute("width", 0);
		int mapHeight = root.getIntAttribute("height", 0);
		int tileWidth = root.getIntAttribute("tilewidth", 0);
		int tileHeight = root.getIntAttribute("tileheight", 0);
		String mapBackgroundColor = root.getAttribute("backgroundcolor", null);

		MapProperties mapProperties = map.getProperties();
		if (mapOrientation != null) {
			mapProperties.put("orientation", mapOrientation);
		}
		mapProperties.put("width", mapWidth);
		mapProperties.put("height", mapHeight);
		mapProperties.put("tilewidth", tileWidth);
		mapProperties.put("tileheight", tileHeight);
		if (mapBackgroundColor != null) {
			mapProperties.put("backgroundcolor", mapBackgroundColor);
		}
		mapWidthInPixels = mapWidth * tileWidth;
		mapHeightInPixels = mapHeight * tileHeight;

		for (int i = 0, j = root.getChildCount(); i < j; i++) {
			Element element = root.getChild(i);
			String elementName = element.getName();
			if (elementName.equals("properties")) {
				loadProperties(map.getProperties(), element);
			} else if (elementName.equals("tileset")) {
				loadTileset(map, element, tmxFile, resolver, parameter);
			} else if (elementName.equals("layer")) {
				loadTileLayer(map, element);
			} else if (elementName.equals("objectgroup")) {
				loadObjectGroup(map, element);
			}
		}
		return map;
	}

	protected void loadTileset (AtlasTiledMap map, Element element, FileHandle tmxFile, AtlasResolver resolver,
		AtlasTiledMapLoaderParameters parameter) {
		if (element.getName().equals("tileset")) {
			String name = element.get("name", null);
			int firstgid = element.getIntAttribute("firstgid", 1);
			int tilewidth = element.getIntAttribute("tilewidth", 0);
			int tileheight = element.getIntAttribute("tileheight", 0);
			int spacing = element.getIntAttribute("spacing", 0);
			int margin = element.getIntAttribute("margin", 0);
			String source = element.getAttribute("source", null);

			String imageSource = "";
			int imageWidth = 0, imageHeight = 0;

			FileHandle image = null;
			if (source != null) {
				FileHandle tsx = getRelativeFileHandle(tmxFile, source);
				try {
					element = xml.parse(tsx);
					name = element.get("name", null);
					tilewidth = element.getIntAttribute("tilewidth", 0);
					tileheight = element.getIntAttribute("tileheight", 0);
					spacing = element.getIntAttribute("spacing", 0);
					margin = element.getIntAttribute("margin", 0);
					imageSource = element.getChildByName("image").getAttribute("source");
					imageWidth = element.getChildByName("image").getIntAttribute("width", 0);
					imageHeight = element.getChildByName("image").getIntAttribute("height", 0);
				} catch (IOException e) {
					throw new GdxRuntimeException("Error parsing external tileset.");
				}
			} else {
				imageSource = element.getChildByName("image").getAttribute("source");
				imageWidth = element.getChildByName("image").getIntAttribute("width", 0);
				imageHeight = element.getChildByName("image").getIntAttribute("height", 0);
			}

			// get the TextureAtlas for this tileset
			TextureAtlas atlas = null;
			String regionsName = "";
			if (map.getProperties().containsKey("atlas_" + name)) {
				FileHandle atlasHandle = getRelativeFileHandle(tmxFile, map.getProperties().get("atlas_" + name, String.class));
				atlasHandle = resolve(atlasHandle.path());
				atlas = resolver.getAtlas(atlasHandle.path());
				regionsName = atlasHandle.nameWithoutExtension();

				if (parameter.forceTextureFilters) {
					for (Texture texture : atlas.getTextures()) {
						texture.setFilter(parameter.textureMinFilter, parameter.textureMagFilter);
					}
				}
			}

			TiledMapTileSet tileset = new TiledMapTileSet();
			MapProperties props = tileset.getProperties();
			tileset.setName(name);
			props.put("firstgid", firstgid);
			props.put("imagesource", imageSource);
			props.put("imagewidth", imageWidth);
			props.put("imageheight", imageHeight);
			props.put("tilewidth", tilewidth);
			props.put("tileheight", tileheight);
			props.put("margin", margin);
			props.put("spacing", spacing);

			Array<AtlasRegion> regions = atlas.findRegions(regionsName);
			for (AtlasRegion region : regions) {
				StaticTiledMapTile tile = new StaticTiledMapTile(region);
				// handle unused tile ids
				if (region != null) {
					if (!yUp) {
						region.flip(false, true);
					}

					int tileid = firstgid + region.index;
					tile.setId(tileid);
					tileset.putTile(tileid, tile);
				}
			}

			Array<Element> tileElements = element.getChildrenByName("tile");

			for (Element tileElement : tileElements) {
				int localtid = tileElement.getIntAttribute("id", 0);
				TiledMapTile tile = tileset.getTile(firstgid + localtid);
				if (tile != null) {
					Element properties = tileElement.getChildByName("properties");
					if (properties != null) {
						loadProperties(tile.getProperties(), properties);
					}
				}
			}

			Element properties = element.getChildByName("properties");
			if (properties != null) {
				loadProperties(tileset.getProperties(), properties);
			}
			map.getTileSets().addTileSet(tileset);
		}
	}

	protected void loadTileLayer (TiledMap map, Element element) {
		if (element.getName().equals("layer")) {
			String name = element.getAttribute("name", null);
			int width = element.getIntAttribute("width", 0);
			int height = element.getIntAttribute("height", 0);
			int tileWidth = element.getParent().getIntAttribute("tilewidth", 0);
			int tileHeight = element.getParent().getIntAttribute("tileheight", 0);
			boolean visible = element.getIntAttribute("visible", 1) == 1;
			float opacity = element.getFloatAttribute("opacity", 1.0f);
			TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
			layer.setVisible(visible);
			layer.setOpacity(opacity);
			layer.setName(name);

			TiledMapTileSets tilesets = map.getTileSets();

			Element data = element.getChildByName("data");
			String encoding = data.getAttribute("encoding", null);
			String compression = data.getAttribute("compression", null);
			if (encoding == null) { // no 'encoding' attribute means that the encoding is XML
				throw new GdxRuntimeException("Unsupported encoding (XML) for TMX Layer Data");
			}
			if (encoding.equals("csv")) {
				String[] array = data.getText().split(",");
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int id = (int)Long.parseLong(array[y * width + x].trim());

						final boolean flipHorizontally = ((id & FLAG_FLIP_HORIZONTALLY) != 0);
						final boolean flipVertically = ((id & FLAG_FLIP_VERTICALLY) != 0);
						final boolean flipDiagonally = ((id & FLAG_FLIP_DIAGONALLY) != 0);

						id = id & ~MASK_CLEAR;

						tilesets.getTile(id);
						TiledMapTile tile = tilesets.getTile(id);
						if (tile != null) {
							Cell cell = new Cell();
							if (flipDiagonally) {
								if (flipHorizontally && flipVertically) {
									cell.setFlipHorizontally(true);
									cell.setRotation(Cell.ROTATE_270);
								} else if (flipHorizontally) {
									cell.setRotation(Cell.ROTATE_270);
								} else if (flipVertically) {
									cell.setRotation(Cell.ROTATE_90);
								} else {
									cell.setFlipVertically(true);
									cell.setRotation(Cell.ROTATE_270);
								}
							} else {
								cell.setFlipHorizontally(flipHorizontally);
								cell.setFlipVertically(flipVertically);
							}
							cell.setTile(tile);
							layer.setCell(x, yUp ? height - 1 - y : y, cell);
						}
					}
				}
			} else {
				if (encoding.equals("base64")) {
					byte[] bytes = Base64Coder.decode(data.getText());
					if (compression == null) {
						int read = 0;
						for (int y = 0; y < height; y++) {
							for (int x = 0; x < width; x++) {

								int id = unsignedByteToInt(bytes[read++]) | unsignedByteToInt(bytes[read++]) << 8
 unsignedByteToInt(bytes[read++]) << 16 | unsignedByteToInt(bytes[read++]) << 24;

								final boolean flipHorizontally = ((id & FLAG_FLIP_HORIZONTALLY) != 0);
								final boolean flipVertically = ((id & FLAG_FLIP_VERTICALLY) != 0);
								final boolean flipDiagonally = ((id & FLAG_FLIP_DIAGONALLY) != 0);

								id = id & ~MASK_CLEAR;

								tilesets.getTile(id);
								TiledMapTile tile = tilesets.getTile(id);
								if (tile != null) {
									Cell cell = new Cell();
									if (flipDiagonally) {
										if (flipHorizontally && flipVertically) {
											cell.setFlipHorizontally(true);
											cell.setRotation(Cell.ROTATE_270);
										} else if (flipHorizontally) {
											cell.setRotation(Cell.ROTATE_270);
										} else if (flipVertically) {
											cell.setRotation(Cell.ROTATE_90);
										} else {
											cell.setFlipVertically(true);
											cell.setRotation(Cell.ROTATE_270);
										}
									} else {
										cell.setFlipHorizontally(flipHorizontally);
										cell.setFlipVertically(flipVertically);
									}
									cell.setTile(tile);
									layer.setCell(x, yUp ? height - 1 - y : y, cell);
								}
							}
						}
					} else if (compression.equals("gzip")) {
						GZIPInputStream GZIS = null;
						try {
							GZIS = new GZIPInputStream(new ByteArrayInputStream(bytes), bytes.length);
						} catch (IOException e) {
							throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e.getMessage());
						}

						byte[] temp = new byte[4];
						for (int y = 0; y < height; y++) {
							for (int x = 0; x < width; x++) {
								try {
									GZIS.read(temp, 0, 4);
									int id = unsignedByteToInt(temp[0]) | unsignedByteToInt(temp[1]) << 8
 unsignedByteToInt(temp[2]) << 16 | unsignedByteToInt(temp[3]) << 24;

									final boolean flipHorizontally = ((id & FLAG_FLIP_HORIZONTALLY) != 0);
									final boolean flipVertically = ((id & FLAG_FLIP_VERTICALLY) != 0);
									final boolean flipDiagonally = ((id & FLAG_FLIP_DIAGONALLY) != 0);

									id = id & ~MASK_CLEAR;

									tilesets.getTile(id);
									TiledMapTile tile = tilesets.getTile(id);
									if (tile != null) {
										Cell cell = new Cell();
										if (flipDiagonally) {
											if (flipHorizontally && flipVertically) {
												cell.setFlipHorizontally(true);
												cell.setRotation(Cell.ROTATE_270);
											} else if (flipHorizontally) {
												cell.setRotation(Cell.ROTATE_270);
											} else if (flipVertically) {
												cell.setRotation(Cell.ROTATE_90);
											} else {
												cell.setFlipVertically(true);
												cell.setRotation(Cell.ROTATE_270);
											}
										} else {
											cell.setFlipHorizontally(flipHorizontally);
											cell.setFlipVertically(flipVertically);
										}
										cell.setTile(tile);
										layer.setCell(x, yUp ? height - 1 - y : y, cell);
									}
								} catch (IOException e) {
									throw new GdxRuntimeException("Error Reading TMX Layer Data.", e);
								}
							}
						}
					} else if (compression.equals("zlib")) {
						Inflater zlib = new Inflater();

						byte[] temp = new byte[4];

						zlib.setInput(bytes, 0, bytes.length);

						for (int y = 0; y < height; y++) {
							for (int x = 0; x < width; x++) {
								try {
									zlib.inflate(temp, 0, 4);
									int id = unsignedByteToInt(temp[0]) | unsignedByteToInt(temp[1]) << 8
 unsignedByteToInt(temp[2]) << 16 | unsignedByteToInt(temp[3]) << 24;

									final boolean flipHorizontally = ((id & FLAG_FLIP_HORIZONTALLY) != 0);
									final boolean flipVertically = ((id & FLAG_FLIP_VERTICALLY) != 0);
									final boolean flipDiagonally = ((id & FLAG_FLIP_DIAGONALLY) != 0);

									id = id & ~MASK_CLEAR;

									tilesets.getTile(id);
									TiledMapTile tile = tilesets.getTile(id);
									if (tile != null) {
										Cell cell = new Cell();
										if (flipDiagonally) {
											if (flipHorizontally && flipVertically) {
												cell.setFlipHorizontally(true);
												cell.setRotation(Cell.ROTATE_270);
											} else if (flipHorizontally) {
												cell.setRotation(Cell.ROTATE_270);
											} else if (flipVertically) {
												cell.setRotation(Cell.ROTATE_90);
											} else {
												cell.setFlipVertically(true);
												cell.setRotation(Cell.ROTATE_270);
											}
										} else {
											cell.setFlipHorizontally(flipHorizontally);
											cell.setFlipVertically(flipVertically);
										}
										cell.setTile(tile);
										layer.setCell(x, yUp ? height - 1 - y : y, cell);
									}

								} catch (DataFormatException e) {
									throw new GdxRuntimeException("Error Reading TMX Layer Data.", e);
								}
							}
						}
					}
				} else {
					// any other value of 'encoding' is one we're not aware of, probably a feature of a future version of Tiled
					// or another editor
					throw new GdxRuntimeException("Unrecognised encoding (" + encoding + ") for TMX Layer Data");
				}
			}
			Element properties = element.getChildByName("properties");
			if (properties != null) {
				loadProperties(layer.getProperties(), properties);
			}
			map.getLayers().add(layer);
		}
	}

	protected void loadObjectGroup (TiledMap map, Element element) {
		if (element.getName().equals("objectgroup")) {
			String name = element.getAttribute("name", null);
			MapLayer layer = new MapLayer();
			layer.setName(name);
			Element properties = element.getChildByName("properties");
			if (properties != null) {
				loadProperties(layer.getProperties(), properties);
			}

			for (Element objectElement : element.getChildrenByName("object")) {
				loadObject(layer, objectElement);
			}

			map.getLayers().add(layer);
		}
	}

	protected void loadObject (MapLayer layer, Element element) {
		if (element.getName().equals("object")) {
			MapObject object = null;

			int x = element.getIntAttribute("x", 0);
			int y = (yUp ? mapHeightInPixels - element.getIntAttribute("y", 0) : element.getIntAttribute("y", 0));

			int width = element.getIntAttribute("width", 0);
			int height = element.getIntAttribute("height", 0);

			if (element.getChildCount() > 0) {
				Element child = null;
				if ((child = element.getChildByName("polygon")) != null) {
					String[] points = child.getAttribute("points").split(" ");
					float[] vertices = new float[points.length * 2];
					for (int i = 0; i < points.length; i++) {
						String[] point = points[i].split(",");
						vertices[i * 2] = Integer.parseInt(point[0]);
						vertices[i * 2 + 1] = Integer.parseInt(point[1]);
						if (yUp) {
							vertices[i * 2 + 1] *= -1;
						}
					}
					Polygon polygon = new Polygon(vertices);
					polygon.setPosition(x, y);
					object = new PolygonMapObject(polygon);
				} else if ((child = element.getChildByName("polyline")) != null) {
					String[] points = child.getAttribute("points").split(" ");
					float[] vertices = new float[points.length * 2];
					for (int i = 0; i < points.length; i++) {
						String[] point = points[i].split(",");
						vertices[i * 2] = Integer.parseInt(point[0]);
						vertices[i * 2 + 1] = Integer.parseInt(point[1]);
						if (yUp) {
							vertices[i * 2 + 1] *= -1;
						}
					}
					Polyline polyline = new Polyline(vertices);
					polyline.setPosition(x, y);
					object = new PolylineMapObject(polyline);
				} else if ((child = element.getChildByName("ellipse")) != null) {
					object = new EllipseMapObject(x, yUp ? y - height : y, width, height);
				}
			}
			if (object == null) {
				object = new RectangleMapObject(x, yUp ? y - height : y, width, height);
			}
			object.setName(element.getAttribute("name", null));
			String type = element.getAttribute("type", null);
			if (type != null) {
				object.getProperties().put("type", type);
			}
			object.getProperties().put("x", x);
			object.getProperties().put("y", yUp ? y - height : y);
			object.setVisible(element.getIntAttribute("visible", 1) == 1);
			Element properties = element.getChildByName("properties");
			if (properties != null) {
				loadProperties(object.getProperties(), properties);
			}
			layer.getObjects().add(object);
		}
	}

	protected void loadProperties (MapProperties properties, Element element) {
		if (element.getName().equals("properties")) {
			for (Element property : element.getChildrenByName("property")) {
				String name = property.getAttribute("name", null);
				String value = property.getAttribute("value", null);
				if (value == null) {
					value = property.getText();
				}
				properties.put(name, value);
			}
		}
	}

	public static FileHandle getRelativeFileHandle (FileHandle file, String path) {
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

	protected static int unsignedByteToInt (byte b) {
		return (int)b & 0xFF;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10034.java