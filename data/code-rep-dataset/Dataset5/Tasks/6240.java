package wicket.threadtest.apps.app1;

/*
 * $Id: ContactGenerator.java 5394 2006-04-16 06:36:52 -0700 (Sun, 16 Apr 2006) jdonnerstag $ $Revision:
 * 3056 $ $Date: 2006-04-16 06:36:52 -0700 (Sun, 16 Apr 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.threadtest.apps;

import java.util.Collection;

/**
 * generates random contacts
 * 
 * @author igor
 * 
 */
public class ContactGenerator {
	private static ContactGenerator instance = new ContactGenerator();

	private static long nextId = 1;

	/**
	 * @return static instance of generator
	 */
	public static ContactGenerator getInstance() {
		return instance;
	}

	private String[] firstNames = { "Jacob", "Emily", "Michael", "Sarah", "Matthew", "Brianna", "Nicholas", "Samantha",
			"Christopher", "Hailey", "Abner", "Abby", "Joshua", "Douglas", "Jack", "Keith", "Gerald", "Samuel",
			"Willie", "Larry", "Jose", "Timothy", "Sandra", "Kathleen", "Pamela", "Virginia", "Debra", "Maria", "Linda" };

	private String[] lastNames = { "Smiith", "Johnson", "Williams", "Jones", "Brown", "Donahue", "Bailey", "Rose",
			"Allen", "Black", "Davis", "Clark", "Hall", "Lee", "Baker", "Gonzalez", "Nelson", "Moore", "Wilson",
			"Graham", "Fisher", "Cruz", "Ortiz", "Gomez", "Murray" };

	private ContactGenerator() {

	}

	/**
	 * generates a new contact
	 * 
	 * @return generated contact
	 */
	public Contact generate() {
		Contact contact = new Contact(randomString(firstNames), randomString(lastNames));
		contact.setId(generateId());
		contact.setHomePhone(generatePhoneNumber());
		contact.setCellPhone(generatePhoneNumber());
		return contact;
	}

	/**
	 * generats <code>count</code> number contacts and puts them into
	 * <code>collection</code> collection
	 * 
	 * @param collection
	 * @param count
	 */
	public void generate(Collection<Contact> collection, int count) {
		for (int i = 0; i < count; i++) {
			collection.add(generate());
		}
	}

	/**
	 * @return unique id
	 */
	public synchronized long generateId() {
		return nextId++;
	}

	private String generatePhoneNumber() {
		return new StringBuffer().append(rint(2, 9)).append(rint(0, 9)).append(rint(0, 9)).append("-555-").append(
				rint(1, 9)).append(rint(0, 9)).append(rint(0, 9)).append(rint(0, 9)).toString();
	}

	private String randomString(String[] choices) {
		return choices[rint(0, choices.length)];
	}

	private int rint(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

}