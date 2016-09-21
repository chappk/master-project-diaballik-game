package util;

/**
 *
 * @author vernagaa
 */
public class Couple<K,V> {

	private K key;
	private V value;

	public Couple(K val1, V val2) {
		this.key = val1;
		this.value = val2;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
	
}
