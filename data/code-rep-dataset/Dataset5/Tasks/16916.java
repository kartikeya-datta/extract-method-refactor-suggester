class GuiceInjectorHolder implements IClusterable

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.guice;

import org.apache.wicket.IClusterable;
import org.apache.wicket.MetaDataKey;

import com.google.inject.Injector;

/**
 * This is a holder for the Injector. The reason we need a holder
 * is that metadata only supports storing serializable objects but
 * Injector is not. The holder acts as a serializable wrapper for
 * the context. Notice that although holder implements IClusterable it
 * really is not because it has a reference to non-serializable context -
 * but this is ok because metadata objects in application are never
 * serialized.
 */
public class GuiceInjectorHolder implements IClusterable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Metadata key used to store Injector holder in application's
	 * metadata
	 */
	static MetaDataKey INJECTOR_KEY = new MetaDataKey(
			GuiceInjectorHolder.class) {

		private static final long serialVersionUID = 1L;

	};
	
	private final Injector injector;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	GuiceInjectorHolder(Injector injector) {
		this.injector = injector;
	}

	/**
	 * @return the context
	 */
	Injector getInjector() {
		return injector;
	}
}