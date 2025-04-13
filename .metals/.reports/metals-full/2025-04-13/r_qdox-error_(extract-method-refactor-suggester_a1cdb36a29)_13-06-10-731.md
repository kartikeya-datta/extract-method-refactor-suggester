error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4276.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4276.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4276.java
text:
```scala
w@@hile (tokenizer.hasMoreTokens()) {

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
package com.badlogic.gdx.graphics.g2d.tiled;

import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;

/**
 * Contains an atlas of tiles by tile id for use with {@link TileMapRenderer}
 * @author David Fraska
 */
public class TileAtlas implements Disposable {

    protected IntMap<TextureRegion> regionsMap = new IntMap<TextureRegion>();
    protected final HashSet<Texture> textures = new HashSet<Texture>(1);

    /**
     * Protected constructor to allow different implementations
     */
    protected TileAtlas() {
    }

    /**
     * Creates a TileAtlas for use with {@link TileMapRenderer}. Run the map through TiledMapPacker to create the files required.
     * @param map The tiled map
     * @param inputDir The directory containing all the files created by TiledMapPacker
     * */
    public TileAtlas(TiledMap map, FileHandle inputDir) {
        // TODO: Create a constructor that doesn't take a tmx map, 
        for (TileSet set : map.tileSets) {
            FileHandle packfile = getRelativeFileHandle(inputDir, removeExtension(set.imageName) + " packfile");
            TextureAtlas textureAtlas = new TextureAtlas(packfile, packfile.parent(), false);
            List<AtlasRegion> atlasRegions = textureAtlas.findRegions(removeExtension(removePath(set.imageName)));

            for (AtlasRegion reg : atlasRegions) {
                regionsMap.put(reg.index + set.firstgid, reg);
                if (!textures.contains(reg.getTexture())) {
                    textures.add(reg.getTexture());
                }
            }
        }
    }

    /**
     * Gets an {@link TextureRegion} for a tile id
     * @param id tile id
     * @return the {@link TextureRegion}
     */
    public TextureRegion getRegion(int id) {
        return regionsMap.get(id);
    }

    /**
     * Releases all resources associated with this TileAtlas instance. This releases all the textures backing all AtlasRegions,
     * which should no longer be used after calling dispose.
     */
    @Override
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }

    private static String removeExtension(String s) {
        int extensionIndex = s.lastIndexOf(".");
        if (extensionIndex == -1) {
            return s;
        }

        return s.substring(0, extensionIndex);
    }

    private static String removePath(String s) {
        String temp;

        int index = s.lastIndexOf('\\');
        if (index != -1) {
            temp = s.substring(index + 1);
        } else {
            temp = s;
        }

        index = temp.lastIndexOf('/');
        if (index != -1) {
            return s.substring(index + 1);
        } else {
            return s;
        }
    }

    private static FileHandle getRelativeFileHandle(FileHandle path, String relativePath) {
        if (relativePath.trim().length() == 0) {
            return path;
        }

        FileHandle child = path;

        StringTokenizer tokenizer = new StringTokenizer(relativePath, "\\/");
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if (token.equals("..")) {
                child = child.parent();
            } else {
                child = child.child(token);
            }
        }

        return child;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4276.java