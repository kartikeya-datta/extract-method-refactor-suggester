error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2959.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2959.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2959.java
text:
```scala
f@@or (JsonValue value = indices.child(); value != null; value = value.next(), j++) {

package com.badlogic.gdx.graphics.g3d.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.data.ModelAnimation;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMesh;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMeshPart;
import com.badlogic.gdx.graphics.g3d.model.data.ModelNode;
import com.badlogic.gdx.graphics.g3d.model.data.ModelNodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.data.ModelNodeKeyframe;
import com.badlogic.gdx.graphics.g3d.model.data.ModelNodePart;
import com.badlogic.gdx.graphics.g3d.model.data.ModelTexture;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BaseJsonReader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.UBJsonReader;

public class G3dModelLoader extends ModelLoader<AssetLoaderParameters<Model>> {
	public static final short VERSION_HI = 0;
	public static final short VERSION_LO = 1;
	protected final BaseJsonReader reader;
	
	public G3dModelLoader(final BaseJsonReader reader) {
		this(reader, null);
	}
	
	public G3dModelLoader(BaseJsonReader reader, FileHandleResolver resolver) {
		super(resolver);
		this.reader = reader;
	}
	
	@Override
	protected ModelData loadModelData (FileHandle fileHandle, AssetLoaderParameters<Model> parameters) {
		return parseModel(fileHandle);
	}
	
	public Model load (FileHandle handle) {
		ModelData modelData = parseModel(handle);
		return new Model(modelData);
	}

	public ModelData parseModel (FileHandle handle) {
		JsonValue json = reader.parse(handle);
		ModelData model = new ModelData();
		JsonValue version = json.require("version");
		model.version[0] = (short)version.getInt(0);
		model.version[1] = (short)version.getInt(1);
		if (model.version[0] != VERSION_HI || model.version[1] != VERSION_LO)
			throw new GdxRuntimeException("Model version not supported");

		model.id = json.getString("id", "");
		parseMeshes(model, json);
		parseMaterials(model, json, handle.parent().path());
		parseNodes(model, json);
		parseAnimations(model, json);
		return model;
	}

	
	private void parseMeshes (ModelData model, JsonValue json) {
		JsonValue meshes = json.require("meshes");
		
		model.meshes.ensureCapacity(meshes.size());
		for (JsonValue mesh = meshes.child(); mesh != null; mesh = mesh.next()) {
			ModelMesh jsonMesh = new ModelMesh();
			
			String id = mesh.getString("id", "");
			jsonMesh.id = id;
			
			JsonValue attributes = mesh.require("attributes");
			jsonMesh.attributes = parseAttributes(attributes);
			
			JsonValue vertices = mesh.require("vertices");
			float[] verts = new float[vertices.size()];
			int j = 0;
			for (JsonValue value = vertices.child(); value != null; value = value.next(), j++) {
				verts[j] = value.asFloat();
			}
			jsonMesh.vertices = verts;
			
			JsonValue meshParts = mesh.require("parts");
			Array<ModelMeshPart> parts = new Array<ModelMeshPart>();
			for (JsonValue meshPart = meshParts.child(); meshPart != null; meshPart = meshPart.next()) {
				ModelMeshPart jsonPart = new ModelMeshPart();
				String partId = meshPart.getString("id", null);
				if(id == null) {
					throw new GdxRuntimeException("Not id given for mesh part");
				}
				for(ModelMeshPart other: parts) {
					if(other.id.equals(partId)) {
						throw new GdxRuntimeException("Mesh part with id '" + partId + "' already in defined");
					}
				}
				jsonPart.id = partId;
				
				String type = meshPart.getString("type", null);
				if(type == null) {
					throw new GdxRuntimeException("No primitive type given for mesh part '" + partId + "'");
				}
				jsonPart.primitiveType = parseType(type);
				
				JsonValue indices = meshPart.require("indices");
				short[] partIndices = new short[indices.size()];
				int k = 0;
				for (JsonValue value = indices.child(); value != null; value = value.next(), k++) {
					partIndices[k] = (short)indices.getInt(k);
				}
				jsonPart.indices = partIndices;
				parts.add(jsonPart);
			}
			jsonMesh.parts = parts.toArray(ModelMeshPart.class);
			model.meshes.add(jsonMesh);
		}
	}
	
	private int parseType (String type) {
		if(type.equals("TRIANGLES")) {
			return GL10.GL_TRIANGLES;
		} else if(type.equals("LINES")) {
			return GL10.GL_LINES;
		} else if(type.equals("POINTS")) {
			return GL10.GL_POINTS;
		} else if(type.equals("TRIANGLE_STRIP")) {
			return GL10.GL_TRIANGLE_STRIP;
		} else if(type.equals("LINE_STRIP")) {
			return GL10.GL_LINE_STRIP;
		} else { 
			throw new GdxRuntimeException("Unknown primitive type '" + type + "', should be one of triangle, trianglestrip, line, linestrip, lineloop or point");
		}
	}

	private VertexAttribute[] parseAttributes (JsonValue attributes) {
		Array<VertexAttribute> vertexAttributes = new Array<VertexAttribute>();
		int unit = 0;
		int blendWeightCount = 0;
		for (JsonValue value = attributes.child(); value != null; value = value.next()) {
			String attribute = value.asString();
			String attr = (String)attribute;
			if(attr.equals("POSITION")) {
				vertexAttributes.add(VertexAttribute.Position());
			} else if(attr.equals("NORMAL")) {
				vertexAttributes.add(VertexAttribute.Normal());
			} else if(attr.equals("COLOR")) {
				vertexAttributes.add(VertexAttribute.ColorUnpacked());
			} else if(attr.equals("COLORPACKED")) {
				vertexAttributes.add(VertexAttribute.Color());
			} else if(attr.equals("TANGENT")) {
				vertexAttributes.add(VertexAttribute.Tangent());
			} else if(attr.equals("BINORMAL")) {
				vertexAttributes.add(VertexAttribute.Binormal());
			} else if(attr.startsWith("TEXCOORD")) {
				vertexAttributes.add(VertexAttribute.TexCoords(unit++));
			} else if(attr.startsWith("BLENDWEIGHT")) {
				vertexAttributes.add(VertexAttribute.BoneWeight(blendWeightCount++));
			} else {
				throw new GdxRuntimeException("Unknown vertex attribute '" + attr + "', should be one of position, normal, uv, tangent or binormal");
			}
		}
		return vertexAttributes.toArray(VertexAttribute.class);
	}

	private void parseMaterials (ModelData model, JsonValue json, String materialDir) {
		JsonValue materials = json.get("materials");
		if(materials == null) {
			// we should probably create some default material in this case
		}
		else {
			model.materials.ensureCapacity(materials.size());
			for (JsonValue material = materials.child(); material != null; material = material.next()) {
				ModelMaterial jsonMaterial = new ModelMaterial();
				
				String id = material.getString("id", null);
				if(id == null)
					throw new GdxRuntimeException("Material needs an id.");

				jsonMaterial.id = id;
							
				// Read material colors
				jsonMaterial.diffuse = parseColor(material.get("diffuse"), Color.WHITE);
				jsonMaterial.ambient = parseColor(material.get("ambient"), Color.BLACK);
				jsonMaterial.emissive = parseColor(material.get("emissive"), Color.BLACK);
				
			   // Read specular
				jsonMaterial.specular = parseColor(material.get("specular"), Color.BLACK);
				// Read shininess
				float shininess = material.getFloat("shininess", 0.0f);
				
				// Read textures
				JsonValue textures = material.get("textures");
				if(textures != null){
					for (JsonValue texture = textures.child(); texture != null; texture = texture.next()) {
						ModelTexture jsonTexture = new ModelTexture();
						
						String textureId = texture.getString("id", null);
						if(textureId == null)
							throw new GdxRuntimeException("Texture has no id.");
						jsonTexture.id = textureId;
						
						String fileName = texture.getString("filename", null);
						if(fileName == null)
							throw new GdxRuntimeException("Texture needs filename.");
						jsonTexture.fileName = materialDir + "/" + fileName;
						
						jsonTexture.uvTranslation = readVector2(texture.get("uvTranslation"), 0f, 0f);
						jsonTexture.uvScaling = readVector2(texture.get("uvScaling"), 1f, 1f);
						
						String textureType = texture.getString("type", null);
						if(textureType == null)
							throw new GdxRuntimeException("Texture needs type.");
						
						jsonTexture.usage = parseTextureUsage(textureType);
						
						if(jsonMaterial.textures == null)
							jsonMaterial.textures = new Array<ModelTexture>();
						jsonMaterial.textures.add(jsonTexture);
					}
				}

				model.materials.add(jsonMaterial);
			}
		}
	}
	
	private int parseTextureUsage(final String value) {
		if (value.equalsIgnoreCase("AMBIENT"))
			return ModelTexture.USAGE_AMBIENT;
		else if (value.equalsIgnoreCase("BUMP"))
			return ModelTexture.USAGE_BUMP;
		else if (value.equalsIgnoreCase("DIFFUSE"))
			return ModelTexture.USAGE_DIFFUSE;
		else if (value.equalsIgnoreCase("EMISSIVE"))
			return ModelTexture.USAGE_EMISSIVE;
		else if (value.equalsIgnoreCase("NONE"))
			return ModelTexture.USAGE_NONE;
		else if (value.equalsIgnoreCase("NORMAL"))
			return ModelTexture.USAGE_NORMAL;
		else if (value.equalsIgnoreCase("REFLECTION"))
			return ModelTexture.USAGE_REFLECTION;
		else if (value.equalsIgnoreCase("SHININESS"))
			return ModelTexture.USAGE_SHININESS;
		else if (value.equalsIgnoreCase("SPECULAR"))
			return ModelTexture.USAGE_SPECULAR;
		else if (value.equalsIgnoreCase("TRANSPARENCY"))
			return ModelTexture.USAGE_TRANSPARENCY;
		return ModelTexture.USAGE_UNKNOWN;
	}

	private Color parseColor (JsonValue colorArray, Color defaultColor) {
		if(colorArray == null) {
			return defaultColor;
		}
		else if(colorArray.size() == 3)
			return new Color(colorArray.getFloat(0), colorArray.getFloat(1), colorArray.getFloat(2), 1.0f);
		else
			throw new GdxRuntimeException("Expected Color values <> than three.");
	}

	private Vector2 readVector2 (JsonValue vectorArray, float x, float y) {
		if(vectorArray == null)
			return new Vector2(x, y);
		else if(vectorArray.size() == 2)
			return new Vector2(vectorArray.getFloat(0), vectorArray.getFloat(1));
		else
			throw new GdxRuntimeException("Expected Vector2 values <> than two.");
	}

	private Array<ModelNode> parseNodes (ModelData model, JsonValue json) {
		JsonValue nodes = json.get("nodes");
		if(nodes == null) {
			throw new GdxRuntimeException("At least one node is required.");
		}
		
		model.nodes.ensureCapacity(nodes.size());
		for (JsonValue node = nodes.child(); node != null; node = node.next()) {
			model.nodes.add(parseNodesRecursively(node));
		}
		return model.nodes;
	}
	
	private ModelNode parseNodesRecursively(JsonValue json){
		ModelNode jsonNode = new ModelNode();
		
		String id = json.getString("id", null);
		if(id == null)
			throw new GdxRuntimeException("Node id missing.");
		jsonNode.id = id;
		
		JsonValue translation = json.get("translation");
		if (translation != null && translation.size() != 3)
			throw new GdxRuntimeException("Node translation incomplete");
		jsonNode.translation = translation == null ? null : new Vector3(translation.getFloat(0), translation.getFloat(1), translation.getFloat(2));
		
		JsonValue rotation = json.get("rotation");
		if(rotation != null && rotation.size() != 4)
			throw new GdxRuntimeException("Node rotation incomplete");
		jsonNode.rotation = rotation == null ? null : new Quaternion(rotation.getFloat(0), rotation.getFloat(1), rotation.getFloat(2), rotation.getFloat(3));
		
		JsonValue scale = json.get("scale");
		if(scale != null && scale.size() != 3)
			throw new GdxRuntimeException("Node scale incomplete");
		jsonNode.scale = scale == null ? null : new Vector3(scale.getFloat(0), scale.getFloat(1), scale.getFloat(2));
		
		String meshId = json.getString("mesh", null);
		if(meshId != null)
			jsonNode.meshId = meshId;
		
		JsonValue materials = json.get("parts");
		if(materials != null){
			jsonNode.parts = new ModelNodePart[materials.size()];
			int i = 0;
			for (JsonValue material = materials.child(); material != null; material = material.next(), i++) {
				ModelNodePart nodePart = new ModelNodePart();
				
				String meshPartId = material.getString("meshpartid", null);
				String materialId = material.getString("materialid", null);
				if(meshPartId == null || materialId == null){
					throw new GdxRuntimeException("Node "+id+" part is missing meshPartId or materialId");
				}
				nodePart.materialId = materialId;
				nodePart.meshPartId = meshPartId;
				
				JsonValue bones = material.get("bones");
				if (bones != null) {
					nodePart.bones = new String[bones.size()];
					int j = 0;
					for (JsonValue bone = bones.child(); bone != null; bone = bone.next(), j++)
						nodePart.bones[j] = bone.asString(); 
				}
				
				jsonNode.parts[i] = nodePart;
			}
		}
		
		JsonValue children = json.get("children");
		if(children != null){
			jsonNode.children = new ModelNode[children.size()];

			int i = 0;
			for (JsonValue child = children.child(); child != null; child = child.next(), i++) {
				jsonNode.children[i] = parseNodesRecursively(child);
			}
		}
		
		return jsonNode;
	}
	
	private void parseAnimations (ModelData model, JsonValue json) {
		JsonValue animations = json.get("animations");
		if(animations == null)
			return;
		
		model.animations.ensureCapacity(animations.size());
		
		for (JsonValue anim = animations.child(); anim != null; anim = anim.next()) {
			JsonValue nodes = anim.get("bones");
			if (nodes == null)
				continue;
			ModelAnimation animation = new ModelAnimation();
			model.animations.add(animation);
			animation.nodeAnimations.ensureCapacity(nodes.size());
			animation.id = anim.getString("id");
			for (JsonValue node = nodes.child(); node != null; node = node.next()) {
				JsonValue keyframes = node.get("keyframes");

				ModelNodeAnimation nodeAnim = new ModelNodeAnimation();
				animation.nodeAnimations.add(nodeAnim);
				nodeAnim.nodeId = node.getString("boneId");
				nodeAnim.keyframes.ensureCapacity(keyframes.size());

				for (JsonValue keyframe = keyframes.child(); keyframe != null; keyframe = keyframe.next()) {
					ModelNodeKeyframe kf = new ModelNodeKeyframe();
					nodeAnim.keyframes.add(kf);
					kf.keytime = keyframe.getFloat("keytime") / 1000.f;
					JsonValue translation = keyframe.get("translation");
					if (translation != null && translation.size() == 3)
						kf.translation = new Vector3(translation.getFloat(0), translation.getFloat(1), translation.getFloat(2));
					JsonValue rotation = keyframe.get("rotation");
					if (rotation != null && rotation.size() == 4)
						kf.rotation = new Quaternion(rotation.getFloat(0), rotation.getFloat(1), rotation.getFloat(2), rotation.getFloat(3));
					JsonValue scale = keyframe.get("scale");
					if (scale != null && scale.size() == 3)
						kf.scale = new Vector3(scale.getFloat(0), scale.getFloat(1), scale.getFloat(2));
				}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2959.java