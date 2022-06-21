import java.util.List;
import java.util.ArrayList;

public class MyLinkedHashMap<K, V> {
	private Entry<K, V>[] table;
	private static int initialCapacity = 16;
	private int size = 0;
	private static double loadFactor = 0.75;
	private Entry<K, V> header;
	private Entry<K, V> last;
	Methods ms = new Methods();

	static class Entry<K, V> {
		K key;
		V value;
		Entry<K, V> next;
		Entry<K, V> before, after;

		public Entry(K key, V value, Entry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}

	public MyLinkedHashMap(int len, double load) {
		initialCapacity = len;
		loadFactor = load;
		table = new Entry[initialCapacity];
	}

	public MyLinkedHashMap() {
		this(initialCapacity, loadFactor);
	}

	private void expand() {
		Entry<K, V>[] newTable = new Entry[2 * initialCapacity];
		rehash(newTable);
	}

	private void rehash(Entry<K, V>[] newTable) {
		List<Entry<K, V>> list = new ArrayList<Entry<K, V>>();

		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) {
				continue;
			}
			findEntryByNext(table[i], list);
			if (list.size() > 0) {
				size = 0;
				initialCapacity = 2 * initialCapacity;

				table = newTable;
				for (Entry<K, V> entry : list) {
					if (entry.next != null) {
						entry.next = null;
					}
					set(entry.key, entry.value);
				}
			}
		}
	}

	public int size() {
		return size;
	}

	private void findEntryByNext(Entry<K, V> entry, List<Entry<K, V>> list) {
		if (entry != null && entry.next != null) {
			list.add(entry);
			findEntryByNext(entry.next, list);
		} else {
			list.add(entry);
		}
	}

	public ArrayList<K> Keyset() {
		ArrayList<K> keys = new ArrayList<>();
		Entry<K, V> currentEntry = header;
		while (currentEntry != null) {
			keys.add(currentEntry.key);
			currentEntry = currentEntry.after;
		}
		return keys;
	}

	public ArrayList<K> Valueset() {
		ArrayList<K> values = new ArrayList<>();
		Entry<K, V> currentEntry = header;
		while (currentEntry != null) {
			values.add((K) currentEntry.value);
			currentEntry = currentEntry.after;
		}
		return values;
	}

	public void set(K newKey, V data) {

		if (newKey == null)
			return;

		int hash = hash(newKey);

		Entry<K, V> newEntry = new Entry<K, V>(newKey, data, null);
		maintainOrderAfterInsert(newEntry);
		if (table[hash] == null) {
			table[hash] = newEntry;
			size++;
		} else {
			Entry<K, V> previous = null;
			Entry<K, V> current = table[hash];
			while (current != null) {
				if (current.key.equals(newKey)) {
					if (previous == null) {
						newEntry.next = current.next;
						table[hash] = newEntry;
						size++;
						return;
					} else {
						newEntry.next = current.next;
						previous.next = newEntry;
						return;
					}
				}
				previous = current;
				current = current.next;
			}
			previous.next = newEntry;
		}
		if (size == initialCapacity - 1) {
			expand();
		}
	}

	private void maintainOrderAfterInsert(Entry<K, V> newEntry) {

		if (header == null) {
			header = newEntry;
			last = newEntry;
			return;
		}

		if (header.key.equals(newEntry.key)) {
			deleteFirst();
			insertFirst(newEntry);
			return;
		}

		if (last.key.equals(newEntry.key)) {
			deleteLast();
			insertLast(newEntry);
			return;
		}

		Entry<K, V> beforeDeleteEntry = deleteSpecificEntry(newEntry);
		if (beforeDeleteEntry == null) {
			insertLast(newEntry);
		} else {
			insertAfter(beforeDeleteEntry, newEntry);
		}

	}

	private void maintainOrderAfterDeletion(Entry<K, V> deleteEntry) {

		if (header.key.equals(deleteEntry.key)) {
			deleteFirst();
			return;
		}

		if (last.key.equals(deleteEntry.key)) {
			deleteLast();
			return;
		}

		deleteSpecificEntry(deleteEntry);

	}

	private void insertAfter(Entry<K, V> beforeDeleteEntry, Entry<K, V> newEntry) {
		Entry<K, V> current = header;
		while (current != beforeDeleteEntry) {
			current = current.after; // move to next node.
		}

		newEntry.after = beforeDeleteEntry.after;
		beforeDeleteEntry.after.before = newEntry;
		newEntry.before = beforeDeleteEntry;
		beforeDeleteEntry.after = newEntry;

	}

	private void deleteFirst() {

		if (header == last) {
			header = last = null;
			return;
		}
		header = header.after;
		header.before = null;

	}

	private void insertFirst(Entry<K, V> newEntry) {

		if (header == null) {
			header = newEntry;
			last = newEntry;
			return;
		}

		newEntry.after = header;
		header.before = newEntry;
		header = newEntry;

	}

	private void insertLast(Entry<K, V> newEntry) {

		if (header == null) {
			header = newEntry;
			last = newEntry;
			return;
		}
		last.after = newEntry;
		newEntry.before = last;
		last = newEntry;

	}

	private void deleteLast() {

		if (header == last) {
			header = last = null;
			return;
		}

		last = last.before;
		last.after = null;
	}

	private Entry<K, V> deleteSpecificEntry(Entry<K, V> newEntry) {

		Entry<K, V> current = header;
		while (!current.key.equals(newEntry.key)) {
			if (current.after == null) {
				return null;
			}
			current = current.after;
		}

		Entry<K, V> beforeDeleteEntry = current.before;
		current.before.after = current.after;
		current.after.before = current.before;
		return beforeDeleteEntry;
	}

	public V get(K key) {
		int hash = hash(key);
		if (table[hash] == null) {
			return null;
		} else {
			Entry<K, V> temp = table[hash];
			while (temp != null) {
				if (temp.key.equals(key)) {
					return temp.value;
				}
				temp = temp.after;
			}
			return null;
		}
	}

	public boolean unset(K deleteKey) {

		int hash = hash(deleteKey);

		if (table[hash] == null) {
			return false;
		} else {
			Entry<K, V> previous = null;
			Entry<K, V> current = table[hash];

			while (current != null) {
				if (current.key.equals(deleteKey)) {
					maintainOrderAfterDeletion(current);
					if (previous == null) {
						table[hash] = table[hash].after;
						size--;
						return true;
					} else {
						previous.after = current.after;
						size--;
						return true;
					}
				}
				previous = current;
				current = current.next;
			}
			return false;
		}
	}

	public void update(K oldKey, V data) {
		int count = 0;
		for (int i = 0; i < initialCapacity; i++) {
			if (table[i] != null) {
				Entry<K, V> insert = table[i];
				if (ms.equals((String) oldKey, (String) insert.key)) {
					count++;
					insert.value = data;
					insert = insert.next;
				}
			}
		}
		if (count == 0) {
			System.out.println("No variable named " + oldKey);
		}
	}

	public void count(V data) {
		int count = 0;
		if (header != null) {
			Entry<K, V> current = header;
			while (current != null) {
				if (data.equals(current.value)) {
					count++;
				}
				current = current.after;
			}
		}
		if (count == 0) {
			System.out.println("null");
		} else {
			System.out.println(count);
		}
	}

	public void display() {

		Entry<K, V> currentEntry = header;
		while (currentEntry != null) {
			System.out.println(currentEntry.key + "=" + currentEntry.value + " ");
			currentEntry = currentEntry.after;
		}

	}
	public void flushall() {
		for(int i=0;i<initialCapacity;i++) {
			table[i]=null;
			header=null;
			last = null;
		}
	}
	public void lrange(String start1, String end1) {
		Integer start = Integer.parseInt(start1);
		Integer end = Integer.parseInt(end1);
		Entry<K, V> currentHeader = header;
		for(int i=0;i<start;i++) {
			if(currentHeader==null) {
				break;
			}
			currentHeader=header.after;
		}
		for(int j=0;j<=end;j++) {
			if(currentHeader==null) {
				break;
			}
			System.out.println(currentHeader.key + "=" + currentHeader.value + " ");
			currentHeader = currentHeader.after;
		}
	}
	private int hash(K key) {
		return (key.hashCode()) % initialCapacity;
	}

}