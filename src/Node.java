/**
 * Node class represents each element in the list.
 * Instead of memory pointers, it uses array indices for links.
 */
public class Node {
	/** Value stored in the node */
	private String value;
	/** Index of previous node (-1 if none) */
	private int prevIndex;
	/** Index of next node (-1 if none) */
	private int nextIndex;

	public Node() {
		this.nextIndex = this.prevIndex = -1;
	}
	public Node(String value, int nextIndex, int prevIndex) {
		this.value = value;
		this.nextIndex = nextIndex;
		this.prevIndex = prevIndex;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getPrevIndex() {
		return prevIndex;
	}
	public void setPrevIndex(int prevIndex) {
		this.prevIndex = prevIndex;
	}

	public int getNextIndex() {
		return nextIndex;
	}
	public void setNextIndex(int nextIndex) {
		this.nextIndex = nextIndex;
	}

	public String getValue() {
		return value;
	}
}
