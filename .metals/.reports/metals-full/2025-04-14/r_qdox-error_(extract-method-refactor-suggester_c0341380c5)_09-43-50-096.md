error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2870.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2870.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2870.java
text:
```scala
c@@olors = new float[value.colors.length];

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.graphics.g2d;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.BitSet;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.MathUtils;

// BOZO - Javadoc.
// BOZO - Add a duplicate emitter button.

public class ParticleEmitter {
	static private final int UPDATE_SCALE = 1 << 0;
	static private final int UPDATE_ANGLE = 1 << 1;
	static private final int UPDATE_ROTATION = 1 << 2;
	static private final int UPDATE_VELOCITY = 1 << 3;
	static private final int UPDATE_WIND = 1 << 4;
	static private final int UPDATE_GRAVITY = 1 << 5;
	static private final int UPDATE_TINT = 1 << 6;

	private RangedNumericValue delayValue = new RangedNumericValue();
	private ScaledNumericValue lifeOffsetValue = new ScaledNumericValue();
	private RangedNumericValue durationValue = new RangedNumericValue();
	private ScaledNumericValue lifeValue = new ScaledNumericValue();
	private ScaledNumericValue emissionValue = new ScaledNumericValue();
	private ScaledNumericValue scaleValue = new ScaledNumericValue();
	private ScaledNumericValue rotationValue = new ScaledNumericValue();
	private ScaledNumericValue velocityValue = new ScaledNumericValue();
	private ScaledNumericValue angleValue = new ScaledNumericValue();
	private ScaledNumericValue windValue = new ScaledNumericValue();
	private ScaledNumericValue gravityValue = new ScaledNumericValue();
	private ScaledNumericValue transparencyValue = new ScaledNumericValue();
	private GradientColorValue tintValue = new GradientColorValue();
	private RangedNumericValue xOffsetValue = new ScaledNumericValue();
	private RangedNumericValue yOffsetValue = new ScaledNumericValue();
	private ScaledNumericValue spawnWidthValue = new ScaledNumericValue();
	private ScaledNumericValue spawnHeightValue = new ScaledNumericValue();
	private SpawnShapeValue spawnShapeValue = new SpawnShapeValue();

	private Sprite sprite;
	private Particle[] particles;
	private int minParticleCount, maxParticleCount = 4;
	private float x, y;
	private String name;
	private String imagePath;
	private int activeCount;
	private BitSet active;
	private boolean firstUpdate;
	private boolean flipX, flipY;
	private int updateFlags;
	private boolean allowCompletion;

	private int emission, emissionDiff, emissionDelta;
	private int lifeOffset, lifeOffsetDiff;
	private int life, lifeDiff;
	private int spawnWidth, spawnWidthDiff;
	private int spawnHeight, spawnHeightDiff;
	public float duration = 1, durationTimer;
	private float delay, delayTimer;

	private boolean attached;
	private boolean continuous;
	private boolean aligned;
	private boolean behind;
	private boolean additive = true;

	public ParticleEmitter () {
		initialize();
	}

	public ParticleEmitter (BufferedReader reader) throws IOException {
		initialize();
		load(reader);
	}

	public ParticleEmitter (ParticleEmitter emitter) {
		sprite = emitter.sprite;
		setMaxParticleCount(emitter.maxParticleCount);
		minParticleCount = emitter.minParticleCount;
		delayValue.load(emitter.delayValue);
		durationValue.load(emitter.durationValue);
		emissionValue.load(emitter.emissionValue);
		lifeValue.load(emitter.lifeValue);
		lifeOffsetValue.load(emitter.lifeOffsetValue);
		scaleValue.load(emitter.scaleValue);
		rotationValue.load(emitter.rotationValue);
		velocityValue.load(emitter.velocityValue);
		angleValue.load(emitter.angleValue);
		windValue.load(emitter.windValue);
		gravityValue.load(emitter.gravityValue);
		transparencyValue.load(emitter.transparencyValue);
		tintValue.load(emitter.tintValue);
		xOffsetValue.load(emitter.xOffsetValue);
		yOffsetValue.load(emitter.yOffsetValue);
		spawnWidthValue.load(emitter.spawnWidthValue);
		spawnHeightValue.load(emitter.spawnHeightValue);
		spawnShapeValue.load(emitter.spawnShapeValue);
		attached = emitter.attached;
		continuous = emitter.continuous;
		aligned = emitter.aligned;
		behind = emitter.behind;
		additive = emitter.additive;
	}

	private void initialize () {
		durationValue.setAlwaysActive(true);
		emissionValue.setAlwaysActive(true);
		lifeValue.setAlwaysActive(true);
		scaleValue.setAlwaysActive(true);
		transparencyValue.setAlwaysActive(true);
		spawnShapeValue.setAlwaysActive(true);
		spawnWidthValue.setAlwaysActive(true);
		spawnHeightValue.setAlwaysActive(true);
	}

	public void setMaxParticleCount (int maxParticleCount) {
		this.maxParticleCount = maxParticleCount;
		active = new BitSet(maxParticleCount);
		activeCount = 0;
		particles = new Particle[maxParticleCount];
	}

	public void addParticle () {
		int activeCount = this.activeCount;
		if (activeCount == maxParticleCount) return;
		BitSet active = this.active;
		int index = active.nextClearBit(0);
		activateParticle(index);
		active.set(index);
		this.activeCount = activeCount + 1;
	}

	public void addParticles (int count) {
		count = Math.min(count, maxParticleCount - activeCount);
		if (count == 0) return;
		BitSet active = this.active;
		for (int i = 0; i < count; i++) {
			int index = active.nextClearBit(0);
			activateParticle(index);
			active.set(index);
		}
		this.activeCount += count;
	}

	public void draw (SpriteBatch spriteBatch, float delta) {
		delta = Math.min(delta, 0.250f);
		int deltaMillis = (int)(delta * 1000);

		if (additive) spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		BitSet active = this.active;
		int activeCount = this.activeCount;
		int index = 0;
		while (true) {
			index = active.nextSetBit(index);
			if (index == -1) break;
			if (updateParticle(index, delta, deltaMillis))
				particles[index].draw(spriteBatch);
			else {
				active.clear(index);
				activeCount--;
			}
			index++;
		}
		this.activeCount = activeCount;

		if (additive) spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		if (delayTimer < delay) {
			delayTimer += deltaMillis;
			return;
		}

		if (firstUpdate) {
			firstUpdate = false;
			addParticle();
		}

		if (durationTimer < duration)
			durationTimer += deltaMillis;
		else {
			if (!continuous || allowCompletion) return;
			restart();
		}

		emissionDelta += deltaMillis;
		float emissionTime = emission + emissionDiff * emissionValue.getScale(durationTimer / (float)duration);
		if (emissionTime > 0) {
			emissionTime = 1000 / emissionTime;
			if (emissionDelta >= emissionTime) {
				int emitCount = (int)(emissionDelta / emissionTime);
				emitCount = Math.min(emitCount, maxParticleCount - activeCount);
				emissionDelta -= emitCount * emissionTime;
				emissionDelta %= emissionTime;
				addParticles(emitCount);
			}
		}
		if (activeCount < minParticleCount) addParticles(minParticleCount - activeCount);
	}

	public void start () {
		firstUpdate = true;
		allowCompletion = false;
		restart();
	}

	public void reset () {
		emissionDelta = 0;
		durationTimer = 0;
		start();
	}

	private void restart () {
		delay = delayValue.active ? delayValue.newLowValue() : 0;
		delayTimer = 0;

		durationTimer -= duration;
		duration = durationValue.newLowValue();

		emission = (int)emissionValue.newLowValue();
		emissionDiff = (int)emissionValue.newHighValue();
		if (!emissionValue.isRelative()) emissionDiff -= emission;

		life = (int)lifeValue.newLowValue();
		lifeDiff = (int)lifeValue.newHighValue();
		if (!lifeValue.isRelative()) lifeDiff -= life;

		lifeOffset = lifeOffsetValue.active ? (int)lifeOffsetValue.newLowValue() : 0;
		lifeOffsetDiff = (int)lifeOffsetValue.newHighValue();
		if (!lifeOffsetValue.isRelative()) lifeOffsetDiff -= lifeOffset;

		spawnWidth = (int)spawnWidthValue.newLowValue();
		spawnWidthDiff = (int)spawnWidthValue.newHighValue();
		if (!spawnWidthValue.isRelative()) spawnWidthDiff -= spawnWidth;

		spawnHeight = (int)spawnHeightValue.newLowValue();
		spawnHeightDiff = (int)spawnHeightValue.newHighValue();
		if (!spawnHeightValue.isRelative()) spawnHeightDiff -= spawnHeight;

		updateFlags = 0;
		if (angleValue.active && angleValue.timeline.length > 1) updateFlags |= UPDATE_ANGLE;
		if (velocityValue.active && velocityValue.active) updateFlags |= UPDATE_VELOCITY;
		if (scaleValue.timeline.length > 1) updateFlags |= UPDATE_SCALE;
		if (rotationValue.active && rotationValue.timeline.length > 1) updateFlags |= UPDATE_ROTATION;
		if (windValue.active) updateFlags |= UPDATE_WIND;
		if (gravityValue.active) updateFlags |= UPDATE_GRAVITY;
		if (tintValue.timeline.length > 1) updateFlags |= UPDATE_TINT;
	}

	private void activateParticle (int index) {
		Particle particle = particles[index];
		if (particle == null) {
			particles[index] = particle = new Particle(sprite);
			particle.flip(flipX, flipY);
		}

		float percent = durationTimer / (float)duration;
		int updateFlags = this.updateFlags;

		float offsetTime = lifeOffset + lifeOffsetDiff * lifeOffsetValue.getScale(percent);
		particle.life = particle.currentLife = life + (int)(lifeDiff * lifeValue.getScale(percent));

		if (velocityValue.active) {
			particle.velocity = velocityValue.newLowValue();
			particle.velocityDiff = velocityValue.newHighValue();
			if (!velocityValue.isRelative()) particle.velocityDiff -= particle.velocity;
		}

		particle.angle = angleValue.newLowValue();
		particle.angleDiff = angleValue.newHighValue();
		if (!angleValue.isRelative()) particle.angleDiff -= particle.angle;
		float angle = 0;
		if ((updateFlags & UPDATE_ANGLE) == 0) {
			angle = particle.angle + particle.angleDiff * angleValue.getScale(0);
			particle.angle = angle;
			particle.angleCos = MathUtils.cosDeg(angle);
			particle.angleSin = MathUtils.sinDeg(angle);
		}

		float spriteWidth = sprite.getWidth();
		particle.scale = scaleValue.newLowValue() / spriteWidth;
		particle.scaleDiff = scaleValue.newHighValue() / spriteWidth;
		if (!scaleValue.isRelative()) particle.scaleDiff -= particle.scale;
		if ((updateFlags & UPDATE_SCALE) == 0) particle.setScale(particle.scale + particle.scaleDiff * scaleValue.getScale(0));

		if (rotationValue.active) {
			particle.rotation = rotationValue.newLowValue();
			particle.rotationDiff = rotationValue.newHighValue();
			if (!rotationValue.isRelative()) particle.rotationDiff -= particle.rotation;
			if ((updateFlags & UPDATE_ROTATION) == 0) {
				float rotation = particle.rotation + particle.rotationDiff * rotationValue.getScale(0);
				if (aligned) rotation += angle;
				particle.setRotation(rotation);
			}
		}

		if (windValue.active) {
			particle.wind = windValue.newLowValue();
			particle.windDiff = windValue.newHighValue();
			if (!windValue.isRelative()) particle.windDiff -= particle.wind;
		}

		if (gravityValue.active) {
			particle.gravity = gravityValue.newLowValue();
			particle.gravityDiff = gravityValue.newHighValue();
			if (!gravityValue.isRelative()) particle.gravityDiff -= particle.gravity;
		}

		if ((updateFlags & UPDATE_TINT) == 0) {
			float[] color = particle.tint;
			if (color == null) particle.tint = color = new float[3];
			float[] temp = tintValue.getColor(0);
			color[0] = temp[0];
			color[1] = temp[1];
			color[2] = temp[2];
		}

		particle.transparency = transparencyValue.newLowValue();
		particle.transparencyDiff = transparencyValue.newHighValue() - particle.transparency;

		// Spawn.
		float x = this.x;
		if (xOffsetValue.active) x += (int)xOffsetValue.newLowValue();
		float y = this.y;
		if (yOffsetValue.active) y += (int)yOffsetValue.newLowValue();
		switch (spawnShapeValue.shape) {
		case square: {
			int width = spawnWidth + (int)(spawnWidthDiff * spawnWidthValue.getScale(percent));
			int height = spawnHeight + (int)(spawnHeightDiff * spawnHeightValue.getScale(percent));
			x += MathUtils.random(width) - width / 2;
			y += MathUtils.random(height) - height / 2;
			break;
		}
		case ellipse: {
			int width = spawnWidth + (int)(spawnWidthDiff * spawnWidthValue.getScale(percent));
			int height = spawnHeight + (int)(spawnHeightDiff * spawnHeightValue.getScale(percent));
			int radiusX = width / 2;
			int radiusY = height / 2;
			if (radiusX == 0 || radiusY == 0) break;
			float scaleY = radiusX / (float)radiusY;
			if (spawnShapeValue.edges) {
				float spawnAngle;
				switch (spawnShapeValue.side) {
				case top:
					spawnAngle = -MathUtils.random(179f);
					break;
				case bottom:
					spawnAngle = MathUtils.random(179f);
					break;
				default:
					spawnAngle = MathUtils.random(360f);
					break;
				}
				x += MathUtils.cosDeg(spawnAngle) * radiusX;
				y += MathUtils.sinDeg(spawnAngle) * radiusX / scaleY;
			} else {
				int radius2 = radiusX * radiusX;
				while (true) {
					int px = MathUtils.random(width) - radiusX;
					int py = MathUtils.random(width) - radiusX;
					if (px * px + py * py <= radius2) {
						x += px;
						y += py / scaleY;
						break;
					}
				}
			}
			break;
		}
		case line: {
			int width = spawnWidth + (int)(spawnWidthDiff * spawnWidthValue.getScale(percent));
			int height = spawnHeight + (int)(spawnHeightDiff * spawnHeightValue.getScale(percent));
			if (width != 0) {
				float lineX = width * MathUtils.random();
				x += lineX;
				y += lineX * (height / (float)width);
			} else
				y += height * MathUtils.random();
			break;
		}
		}

		float spriteHeight = sprite.getHeight();
		particle.setBounds(x - spriteWidth / 2, y - spriteHeight / 2, spriteWidth, spriteHeight);
	}

	private boolean updateParticle (int index, float delta, int deltaMillis) {
		Particle particle = particles[index];
		int life = particle.currentLife - deltaMillis;
		if (life <= 0) return false;
		particle.currentLife = life;

		float percent = 1 - particle.currentLife / (float)particle.life;
		int updateFlags = this.updateFlags;

		if ((updateFlags & UPDATE_SCALE) != 0)
			particle.setScale(particle.scale + particle.scaleDiff * scaleValue.getScale(percent));

		if ((updateFlags & UPDATE_VELOCITY) != 0) {
			float velocity = (particle.velocity + particle.velocityDiff * velocityValue.getScale(percent)) * delta;

			float velocityX, velocityY;
			if ((updateFlags & UPDATE_ANGLE) != 0) {
				float angle = particle.angle + particle.angleDiff * angleValue.getScale(percent);
				velocityX = velocity * MathUtils.cosDeg(angle);
				velocityY = velocity * MathUtils.sinDeg(angle);
				if ((updateFlags & UPDATE_ROTATION) != 0) {
					float rotation = particle.rotation + particle.rotationDiff * rotationValue.getScale(percent);
					if (aligned) rotation += angle;
					particle.setRotation(rotation);
				}
			} else {
				velocityX = velocity * particle.angleCos;
				velocityY = velocity * particle.angleSin;
				if (aligned || (updateFlags & UPDATE_ROTATION) != 0) {
					float rotation = particle.rotation + particle.rotationDiff * rotationValue.getScale(percent);
					if (aligned) rotation += particle.angle;
					particle.setRotation(rotation);
				}
			}

			if ((updateFlags & UPDATE_WIND) != 0)
				velocityX += (particle.wind + particle.windDiff * windValue.getScale(percent)) * delta;

			if ((updateFlags & UPDATE_GRAVITY) != 0)
				velocityY += (particle.gravity + particle.gravityDiff * gravityValue.getScale(percent)) * delta;

			particle.translate(velocityX, velocityY);
		} else {
			if ((updateFlags & UPDATE_ROTATION) != 0)
				particle.setRotation(particle.rotation + particle.rotationDiff * rotationValue.getScale(percent));
		}

		float[] color;
		if ((updateFlags & UPDATE_TINT) != 0)
			color = tintValue.getColor(percent);
		else
			color = particle.tint;
		particle.setColor(color[0], color[1], color[2],
			particle.transparency + particle.transparencyDiff * transparencyValue.getScale(percent));

		return true;
	}

	public void setPosition (float x, float y) {
		if (attached) {
			float xAmount = x - this.x;
			float yAmount = y - this.y;
			BitSet active = this.active;
			int index = 0;
			while (true) {
				index = active.nextSetBit(index);
				if (index == -1) break;
				Particle particle = particles[index];
				particle.translate(xAmount, yAmount);
				index++;
			}
		}
		this.x = x;
		this.y = y;
	}

	public void setSprite (Sprite sprite) {
		this.sprite = sprite;
		if (sprite == null) return;
		float originX = sprite.getOriginX();
		float originY = sprite.getOriginY();
		Texture texture = sprite.getTexture();
		for (int i = 0, n = particles.length; i < n; i++) {
			Particle particle = particles[i];
			if (particle == null) break;
			particle.setTexture(texture);
			particle.setOrigin(originX, originY);
		}
	}

	/**
	 * Ignores the {@link #setContinuous(boolean) continuous} setting until the emitter is started again. This allows the emitter
	 * to stop smoothly.
	 */
	public void allowCompletion () {
		allowCompletion = true;
		durationTimer = duration;
	}

	public Sprite getSprite () {
		return sprite;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public ScaledNumericValue getLife () {
		return lifeValue;
	}

	public ScaledNumericValue getScale () {
		return scaleValue;
	}

	public ScaledNumericValue getRotation () {
		return rotationValue;
	}

	public GradientColorValue getTint () {
		return tintValue;
	}

	public ScaledNumericValue getVelocity () {
		return velocityValue;
	}

	public ScaledNumericValue getWind () {
		return windValue;
	}

	public ScaledNumericValue getGravity () {
		return gravityValue;
	}

	public ScaledNumericValue getAngle () {
		return angleValue;
	}

	public ScaledNumericValue getEmission () {
		return emissionValue;
	}

	public ScaledNumericValue getTransparency () {
		return transparencyValue;
	}

	public RangedNumericValue getDuration () {
		return durationValue;
	}

	public RangedNumericValue getDelay () {
		return delayValue;
	}

	public ScaledNumericValue getLifeOffset () {
		return lifeOffsetValue;
	}

	public RangedNumericValue getXOffsetValue () {
		return xOffsetValue;
	}

	public RangedNumericValue getYOffsetValue () {
		return yOffsetValue;
	}

	public ScaledNumericValue getSpawnWidth () {
		return spawnWidthValue;
	}

	public ScaledNumericValue getSpawnHeight () {
		return spawnHeightValue;
	}

	public SpawnShapeValue getSpawnShape () {
		return spawnShapeValue;
	}

	public boolean isAttached () {
		return attached;
	}

	public void setAttached (boolean attached) {
		this.attached = attached;
	}

	public boolean isContinuous () {
		return continuous;
	}

	public void setContinuous (boolean continuous) {
		this.continuous = continuous;
	}

	public boolean isAligned () {
		return aligned;
	}

	public void setAligned (boolean aligned) {
		this.aligned = aligned;
	}

	public boolean isAdditive () {
		return additive;
	}

	public void setAdditive (boolean additive) {
		this.additive = additive;
	}

	public boolean isBehind () {
		return behind;
	}

	public void setBehind (boolean behind) {
		this.behind = behind;
	}

	public int getMinParticleCount () {
		return minParticleCount;
	}

	public void setMinParticleCount (int minParticleCount) {
		this.minParticleCount = minParticleCount;
	}

	public int getMaxParticleCount () {
		return maxParticleCount;
	}

	public boolean isComplete () {
		if (delayTimer < delay) return false;
		return durationTimer >= duration && activeCount == 0;
	}

	public float getPercentComplete () {
		if (delayTimer < delay) return 0;
		return Math.min(1, durationTimer / (float)duration);
	}

	public float getX () {
		return x;
	}

	public float getY () {
		return y;
	}

	public int getActiveCount () {
		return activeCount;
	}

	public int getDrawCount () {
		return active.length();
	}

	public String getImagePath () {
		return imagePath;
	}

	public void setImagePath (String imagePath) {
		this.imagePath = imagePath;
	}

	public void setFlip (boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
		if (particles == null) return;
		for (int i = 0, n = particles.length; i < n; i++) {
			Particle particle = particles[i];
			if (particle != null) particle.flip(flipX, flipY);
		}
	}

	public void save (Writer output) throws IOException {
		output.write(name + "\n");
		output.write("- Delay -\n");
		delayValue.save(output);
		output.write("- Duration - \n");
		durationValue.save(output);
		output.write("- Count - \n");
		output.write("min: " + minParticleCount + "\n");
		output.write("max: " + maxParticleCount + "\n");
		output.write("- Emission - \n");
		emissionValue.save(output);
		output.write("- Life - \n");
		lifeValue.save(output);
		output.write("- Life Offset - \n");
		lifeOffsetValue.save(output);
		output.write("- X Offset - \n");
		xOffsetValue.save(output);
		output.write("- Y Offset - \n");
		yOffsetValue.save(output);
		output.write("- Spawn Shape - \n");
		spawnShapeValue.save(output);
		output.write("- Spawn Width - \n");
		spawnWidthValue.save(output);
		output.write("- Spawn Height - \n");
		spawnHeightValue.save(output);
		output.write("- Scale - \n");
		scaleValue.save(output);
		output.write("- Velocity - \n");
		velocityValue.save(output);
		output.write("- Angle - \n");
		angleValue.save(output);
		output.write("- Rotation - \n");
		rotationValue.save(output);
		output.write("- Wind - \n");
		windValue.save(output);
		output.write("- Gravity - \n");
		gravityValue.save(output);
		output.write("- Tint - \n");
		tintValue.save(output);
		output.write("- Transparency - \n");
		transparencyValue.save(output);
		output.write("- Options - \n");
		output.write("attached: " + attached + "\n");
		output.write("continuous: " + continuous + "\n");
		output.write("aligned: " + aligned + "\n");
		output.write("additive: " + additive + "\n");
		output.write("behind: " + behind + "\n");
	}

	public void load (BufferedReader reader) throws IOException {
		try {
			name = readString(reader, "name");
			reader.readLine();
			delayValue.load(reader);
			reader.readLine();
			durationValue.load(reader);
			reader.readLine();
			setMinParticleCount(readInt(reader, "minParticleCount"));
			setMaxParticleCount(readInt(reader, "maxParticleCount"));
			reader.readLine();
			emissionValue.load(reader);
			reader.readLine();
			lifeValue.load(reader);
			reader.readLine();
			lifeOffsetValue.load(reader);
			reader.readLine();
			xOffsetValue.load(reader);
			reader.readLine();
			yOffsetValue.load(reader);
			reader.readLine();
			spawnShapeValue.load(reader);
			reader.readLine();
			spawnWidthValue.load(reader);
			reader.readLine();
			spawnHeightValue.load(reader);
			reader.readLine();
			scaleValue.load(reader);
			reader.readLine();
			velocityValue.load(reader);
			reader.readLine();
			angleValue.load(reader);
			reader.readLine();
			rotationValue.load(reader);
			reader.readLine();
			windValue.load(reader);
			reader.readLine();
			gravityValue.load(reader);
			reader.readLine();
			tintValue.load(reader);
			reader.readLine();
			transparencyValue.load(reader);
			reader.readLine();
			attached = readBoolean(reader, "attached");
			continuous = readBoolean(reader, "continuous");
			aligned = readBoolean(reader, "aligned");
			additive = readBoolean(reader, "additive");
			behind = readBoolean(reader, "behind");
		} catch (RuntimeException ex) {
			if (name == null) throw ex;
			throw new RuntimeException("Error parsing emitter: " + name, ex);
		}
	}

	static String readString (BufferedReader reader, String name) throws IOException {
		String line = reader.readLine();
		if (line == null) throw new IOException("Missing value: " + name);
		return line.substring(line.indexOf(":") + 1).trim();
	}

	static boolean readBoolean (BufferedReader reader, String name) throws IOException {
		return Boolean.parseBoolean(readString(reader, name));
	}

	static int readInt (BufferedReader reader, String name) throws IOException {
		return Integer.parseInt(readString(reader, name));
	}

	static float readFloat (BufferedReader reader, String name) throws IOException {
		return Float.parseFloat(readString(reader, name));
	}

	static class Particle extends Sprite {
		int life, currentLife;
		float scale, scaleDiff;
		float rotation, rotationDiff;
		float velocity, velocityDiff;
		float angle, angleDiff;
		float angleCos, angleSin;
		float transparency, transparencyDiff;
		float wind, windDiff;
		float gravity, gravityDiff;
		float[] tint;

		public Particle (Sprite sprite) {
			super(sprite);
		}
	}

	static public class ParticleValue {
		boolean active;
		boolean alwaysActive;

		public void setAlwaysActive (boolean alwaysActive) {
			this.alwaysActive = alwaysActive;
		}

		public boolean isAlwaysActive () {
			return alwaysActive;
		}

		public boolean isActive () {
			return active;
		}

		public void setActive (boolean active) {
			this.active = active;
		}

		public void save (Writer output) throws IOException {
			if (!alwaysActive)
				output.write("active: " + active + "\n");
			else
				active = true;
		}

		public void load (BufferedReader reader) throws IOException {
			if (!alwaysActive)
				active = readBoolean(reader, "active");
			else
				active = true;
		}

		public void load (ParticleValue value) {
			active = value.active;
			alwaysActive = value.alwaysActive;
		}
	}

	static public class NumericValue extends ParticleValue {
		private float value;

		public float getValue () {
			return value;
		}

		public void setValue (float value) {
			this.value = value;
		}

		public void save (Writer output) throws IOException {
			super.save(output);
			if (!active) return;
			output.write("value: " + value + "\n");
		}

		public void load (BufferedReader reader) throws IOException {
			super.load(reader);
			if (!active) return;
			value = readFloat(reader, "value");
		}

		public void load (NumericValue value) {
			super.load(value);
			this.value = value.value;
		}
	}

	static public class RangedNumericValue extends ParticleValue {
		private float lowMin, lowMax;

		public float newLowValue () {
			return lowMin + (lowMax - lowMin) * MathUtils.random();
		}

		public void setLow (float value) {
			lowMin = value;
			lowMax = value;
		}

		public void setLow (float min, float max) {
			lowMin = min;
			lowMax = max;
		}

		public float getLowMin () {
			return lowMin;
		}

		public void setLowMin (float lowMin) {
			this.lowMin = lowMin;
		}

		public float getLowMax () {
			return lowMax;
		}

		public void setLowMax (float lowMax) {
			this.lowMax = lowMax;
		}

		public void save (Writer output) throws IOException {
			super.save(output);
			if (!active) return;
			output.write("lowMin: " + lowMin + "\n");
			output.write("lowMax: " + lowMax + "\n");
		}

		public void load (BufferedReader reader) throws IOException {
			super.load(reader);
			if (!active) return;
			lowMin = readFloat(reader, "lowMin");
			lowMax = readFloat(reader, "lowMax");
		}

		public void load (RangedNumericValue value) {
			super.load(value);
			lowMax = value.lowMax;
			lowMin = value.lowMin;
		}
	}

	static public class ScaledNumericValue extends RangedNumericValue {
		private float[] scaling = {1};
		float[] timeline = {0};
		private float highMin, highMax;
		private boolean relative;

		public float newHighValue () {
			return highMin + (highMax - highMin) * MathUtils.random();
		}

		public void setHigh (float value) {
			highMin = value;
			highMax = value;
		}

		public void setHigh (float min, float max) {
			highMin = min;
			highMax = max;
		}

		public float getHighMin () {
			return highMin;
		}

		public void setHighMin (float highMin) {
			this.highMin = highMin;
		}

		public float getHighMax () {
			return highMax;
		}

		public void setHighMax (float highMax) {
			this.highMax = highMax;
		}

		public float[] getScaling () {
			return scaling;
		}

		public void setScaling (float[] values) {
			this.scaling = values;
		}

		public float[] getTimeline () {
			return timeline;
		}

		public void setTimeline (float[] timeline) {
			this.timeline = timeline;
		}

		public boolean isRelative () {
			return relative;
		}

		public void setRelative (boolean relative) {
			this.relative = relative;
		}

		public float getScale (float percent) {
			int endIndex = -1;
			float[] timeline = this.timeline;
			int n = timeline.length;
			for (int i = 1; i < n; i++) {
				float t = timeline[i];
				if (t > percent) {
					endIndex = i;
					break;
				}
			}
			if (endIndex == -1) return scaling[n - 1];
			float[] scaling = this.scaling;
			int startIndex = endIndex - 1;
			float startValue = scaling[startIndex];
			float startTime = timeline[startIndex];
			return startValue + (scaling[endIndex] - startValue) * ((percent - startTime) / (timeline[endIndex] - startTime));
		}

		public void save (Writer output) throws IOException {
			super.save(output);
			if (!active) return;
			output.write("highMin: " + highMin + "\n");
			output.write("highMax: " + highMax + "\n");
			output.write("relative: " + relative + "\n");
			output.write("scalingCount: " + scaling.length + "\n");
			for (int i = 0; i < scaling.length; i++)
				output.write("scaling" + i + ": " + scaling[i] + "\n");
			output.write("timelineCount: " + timeline.length + "\n");
			for (int i = 0; i < timeline.length; i++)
				output.write("timeline" + i + ": " + timeline[i] + "\n");
		}

		public void load (BufferedReader reader) throws IOException {
			super.load(reader);
			if (!active) return;
			highMin = readFloat(reader, "highMin");
			highMax = readFloat(reader, "highMax");
			relative = readBoolean(reader, "relative");
			scaling = new float[readInt(reader, "scalingCount")];
			for (int i = 0; i < scaling.length; i++)
				scaling[i] = readFloat(reader, "scaling" + i);
			timeline = new float[readInt(reader, "timelineCount")];
			for (int i = 0; i < timeline.length; i++)
				timeline[i] = readFloat(reader, "timeline" + i);
		}

		public void load (ScaledNumericValue value) {
			super.load(value);
			highMax = value.highMax;
			highMin = value.highMin;
			scaling = new float[value.scaling.length];
			System.arraycopy(value.scaling, 0, scaling, 0, scaling.length);
			timeline = new float[value.timeline.length];
			System.arraycopy(value.timeline, 0, timeline, 0, timeline.length);
			relative = value.relative;
		}
	}

	static public class GradientColorValue extends ParticleValue {
		static private float[] temp = new float[4];

		private float[] colors = {1, 1, 1};
		float[] timeline = {0};

		public GradientColorValue () {
			alwaysActive = true;
		}

		public float[] getTimeline () {
			return timeline;
		}

		public void setTimeline (float[] timeline) {
			this.timeline = timeline;
		}

		public float[] getColors () {
			return colors;
		}

		public void setColors (float[] colors) {
			this.colors = colors;
		}

		public float[] getColor (float percent) {
			int startIndex = 0, endIndex = -1;
			float[] timeline = this.timeline;
			int n = timeline.length;
			for (int i = 1; i < n; i++) {
				float t = timeline[i];
				if (t > percent) {
					endIndex = i;
					break;
				}
				startIndex = i;
			}
			float startTime = timeline[startIndex];
			startIndex *= 3;
			float r1 = colors[startIndex];
			float g1 = colors[startIndex + 1];
			float b1 = colors[startIndex + 2];
			if (endIndex == -1) {
				temp[0] = r1;
				temp[1] = g1;
				temp[2] = b1;
				return temp;
			}
			float factor = (percent - startTime) / (timeline[endIndex] - startTime);
			endIndex *= 3;
			temp[0] = r1 + (colors[endIndex] - r1) * factor;
			temp[1] = g1 + (colors[endIndex + 1] - g1) * factor;
			temp[2] = b1 + (colors[endIndex + 2] - b1) * factor;
			return temp;
		}

		public void save (Writer output) throws IOException {
			super.save(output);
			if (!active) return;
			output.write("colorsCount: " + colors.length + "\n");
			for (int i = 0; i < colors.length; i++)
				output.write("colors" + i + ": " + colors[i] + "\n");
			output.write("timelineCount: " + timeline.length + "\n");
			for (int i = 0; i < timeline.length; i++)
				output.write("timeline" + i + ": " + timeline[i] + "\n");
		}

		public void load (BufferedReader reader) throws IOException {
			super.load(reader);
			if (!active) return;
			colors = new float[readInt(reader, "colorsCount")];
			for (int i = 0; i < colors.length; i++)
				colors[i] = readFloat(reader, "colors" + i);
			timeline = new float[readInt(reader, "timelineCount")];
			for (int i = 0; i < timeline.length; i++)
				timeline[i] = readFloat(reader, "timeline" + i);
		}

		public void load (GradientColorValue value) {
			super.load(value);
			colors = new float[3];
			System.arraycopy(value.colors, 0, colors, 0, colors.length);
			timeline = new float[value.timeline.length];
			System.arraycopy(value.timeline, 0, timeline, 0, timeline.length);
		}
	}

	static public class SpawnShapeValue extends ParticleValue {
		SpawnShape shape = SpawnShape.point;
		boolean edges;
		SpawnEllipseSide side = SpawnEllipseSide.both;

		public SpawnShape getShape () {
			return shape;
		}

		public void setShape (SpawnShape shape) {
			this.shape = shape;
		}

		public boolean isEdges () {
			return edges;
		}

		public void setEdges (boolean edges) {
			this.edges = edges;
		}

		public SpawnEllipseSide getSide () {
			return side;
		}

		public void setSide (SpawnEllipseSide side) {
			this.side = side;
		}

		public void save (Writer output) throws IOException {
			super.save(output);
			if (!active) return;
			output.write("shape: " + shape + "\n");
			if (shape == SpawnShape.ellipse) {
				output.write("edges: " + edges + "\n");
				output.write("side: " + side + "\n");
			}
		}

		public void load (BufferedReader reader) throws IOException {
			super.load(reader);
			if (!active) return;
			shape = SpawnShape.valueOf(readString(reader, "shape"));
			if (shape == SpawnShape.ellipse) {
				edges = readBoolean(reader, "edges");
				side = SpawnEllipseSide.valueOf(readString(reader, "side"));
			}
		}

		public void load (SpawnShapeValue value) {
			super.load(value);
			shape = value.shape;
			edges = value.edges;
			side = value.side;
		}
	}

	static public enum SpawnShape {
		point, line, square, ellipse
	}

	static public enum SpawnEllipseSide {
		both, top, bottom
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2870.java