public synchronized void setParameters(Collection parameters) throws InvalidVariableException {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.functions;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;

/**
 * Function to get a JMeter property, or a default. Does not offer the option to
 * store the value, as it is just as easy to refetch it. This is a
 * specialisation of the __property() function to make it simpler to use for
 * ThreadGroup GUI etc. The name is also shorter.
 * 
 * Parameters: - property name - default value (optional; defaults to "1")
 * 
 * Usage:
 * 
 * Define the property in jmeter.properties, or on the command-line: java ...
 * -Jpropname=value
 * 
 * Retrieve the value in the appropriate GUI by using the string:
 * ${__P(propname)} $(__P(propname,default)}
 * 
 * Returns: - the property value, but if not found - the default value, but if
 * not present - "1" (suitable for use in ThreadGroup GUI)
 * 
 */
public class Property2 extends AbstractFunction implements Serializable {

	private static final List desc = new LinkedList();

	private static final String KEY = "__P"; //$NON-NLS-1$

	// Number of parameters expected - used to reject invalid calls
	private static final int MIN_PARAMETER_COUNT = 1;

	private static final int MAX_PARAMETER_COUNT = 2;
	static {
		desc.add(JMeterUtils.getResString("property_name_param")); //$NON-NLS-1$
		desc.add(JMeterUtils.getResString("property_default_param")); //$NON-NLS-1$
	}

	private Object[] values;

	public Property2() {
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String propertyName = ((CompoundVariable) values[0]).execute();

		String propertyDefault = "1"; //$NON-NLS-1$
		if (values.length > 1) { // We have a default
			propertyDefault = ((CompoundVariable) values[1]).execute();
		}

		String propertyValue = JMeterUtils.getPropDefault(propertyName, propertyDefault);

		return propertyValue;

	}

	public void setParameters(Collection parameters) throws InvalidVariableException {

		values = parameters.toArray();

		if ((values.length < MIN_PARAMETER_COUNT) || (values.length > MAX_PARAMETER_COUNT)) {
			throw new InvalidVariableException("Parameter Count not between " + MIN_PARAMETER_COUNT + " & "
					+ MAX_PARAMETER_COUNT);
		}

	}

	public String getReferenceKey() {
		return KEY;
	}

	public List getArgumentDesc() {
		return desc;
	}

}
 No newline at end of file