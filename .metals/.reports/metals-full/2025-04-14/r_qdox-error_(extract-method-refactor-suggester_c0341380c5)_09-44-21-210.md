error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7133.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7133.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 47
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7133.java
text:
```scala
public class VorbisDecoder extends Decoder {

p@@ackage com.badlogic.gdx.audio.io;
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


import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.SharedLibraryLoader;

/** A {@link Decoder} implementation that decodes OGG Vorbis files using tremor
 * @author mzechner */
public class VorbisDecoder implements Decoder {
	static {
		new SharedLibraryLoader().load("gdx-audio");
	}
	
	/** address of native OggFileHandle structure **/
	private final long handle;

	/** Opens the given file for ogg decoding. Throws an IllegalArugmentException in case the file could not be opened.
	 * 
	 * @param file external or absolute {@link FileHandle}
	 */
	public VorbisDecoder (FileHandle file) {
		if(file.type() != FileType.External && file.type() != FileType.Absolute)
			throw new IllegalArgumentException("File must be absolute or external!");
		handle = openFile(file.file().getAbsolutePath());
		if (handle == 0)
			throw new IllegalArgumentException("couldn't open file '" + file + "'");
	}

	@Override
	public void dispose () {
		closeFile(handle);
	}

	@Override
	public float getLength () {
		return getLength(handle);
	}

	@Override
	public int getChannels () {
		return getNumChannels(handle);
	}

	@Override
	public int getRate () {
		return getRate(handle);
	}

	@Override
	public int readSamples (short[] samples, int offset, int numSamples) {
		return readSamples(handle, samples, offset, numSamples);
	}

	@Override
	public int skipSamples (int numSamples) {
		return skipSamples(handle, numSamples);
	}

	/*JNI
	#include <ogg.h>
	#include <ivorbiscodec.h>
	#include <ivorbisfile.h>
	#include <stdlib.h>
	#include <stdio.h>
	
	struct OggFile
	{
		OggVorbis_File* ogg;
		int channels;
		int rate;
		float length;
		int bitstream;
	};
	
	static char buffer[10000];
	 */
	
	private static native long openFile (String filename); /*
		OggVorbis_File* ogg = new OggVorbis_File();
		FILE* file = fopen(filename, "rb" );
	
		if( file == 0 )
		{
			delete ogg;
			return 0;
		}
	
		if( ov_open( file, ogg, NULL, 0 ) != 0 )
		{
			fclose( file );
			delete ogg;
			return 0;
		}
	
		vorbis_info *info = ov_info( ogg, -1 );
		int channels = info->channels;
		int rate = info->rate;
		float length = (float)ov_time_total(ogg, -1 ) / 1000.0f;
	
		OggFile* oggFile = new OggFile();
		oggFile->ogg = ogg;
		oggFile->channels = channels;
		oggFile->rate = rate;
		oggFile->length = length;
	
		return (jlong)oggFile;	
	*/

	private static native int getNumChannels (long handle); /*
		OggFile* file = (OggFile*)handle;
		return file->channels;
	*/

	private static native int getRate (long handle); /*
		OggFile* file = (OggFile*)handle;
		return file->rate;
	*/

	private static native float getLength (long handle); /*
		OggFile* file = (OggFile*)handle;
		return file->length;
	*/

	private static native int readSamples (long handle, short[] samples, int offset, int numSamples); /*
		OggFile* file = (OggFile*)handle;
		int toRead = 2 * numSamples;
		int read = 0;
	
		samples += offset;
	
		while( read != toRead )
		{
			int ret = ov_read( file->ogg, (char*)samples + read, toRead - read, &file->bitstream );
			if( ret == OV_HOLE )
				continue;
			if( ret == OV_EBADLINK || ret == OV_EINVAL || ret == 0 )
				return read / 2;
			read+=ret;
		}
		return read / 2;
	*/

	private static native int skipSamples (long handle, int numSamples); /*
		OggFile* file = (OggFile*)handle;
		int toRead = 2 * numSamples;
		int read = 0;
	
		while( read != toRead )
		{
			int ret = ov_read( file->ogg, buffer, (toRead - read)>10000?10000:(toRead-read), &file->bitstream );
			if( ret == OV_HOLE )
				continue;
			if( ret == OV_EBADLINK || ret == OV_EINVAL || ret == 0 )
				return read / 2;
			read+=ret;
		}
	
		return read / 2;
	*/

	private static native void closeFile (long handle); /*
		OggFile* file = (OggFile*)handle;
		ov_clear(file->ogg);
		free(file->ogg);
		free(file);
	*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7133.java