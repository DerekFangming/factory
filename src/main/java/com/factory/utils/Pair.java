package com.factory.utils;

/**
 * <code>Pair</code> provides a convenient means for pairing
 * two objects of any type.
 */
public class Pair < F, S > { // implements Serializable

	static final long serialVersionUID = 1L;

	private F first;
	private S second;

	/**
	 * Constructor for a pair of Objects.
	 *  
	 * @param f First object in the pair.
	 * @param s Second object in the pair.
	 */
	public Pair(F f, S s) {
		first = f;
		second = s;
	}

	/**
	 * Returns the first object in the pair.
	 * @return The first object.
	 */
	public F getFirst() {
		return first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	/**
	 * Returns the second object in the pair.
	 * @return The second object.
	 */
	public S getSecond() {
		return second;
	}

	public void setSecond(S second) {
		this.second = second;
	}

}
