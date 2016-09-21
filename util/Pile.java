package util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author disavinr
 */
public class Pile<T> implements Serializable {

	ArrayList<T> pile;

	public Pile() {
		pile = new ArrayList<T>();
	}

	/**
	 * Ajouter un élément dans la pile
	 * @param elem 
	 */
	public void add(T elem) {
		pile.add(0, elem);
	}

	/**
	 * Enlève le sommet de la pile et le retourne
	 * @return le sommet de la pile
	 */
	public T pop() {
		T elem = pile.get(0);
		pile.remove(0);

		return elem;
	}

	/**
	 * Teste si la pile pile est vide ou non
	 * @return vrai si la pile est vide
	 */
	public boolean isEmpty() {
		return pile.isEmpty();
	}

	/**
	 * Retourne le sommet de la pile
	 * @return 
	 */
	public T pic() {
		return pile.get(0);
	}

	/**
	 * Retourne la valeur à l'indice demandé
	 * @return 
	 */
	public T get(int indice) {
		return pile.get(indice);
	}

	public int size() {
		return pile.size();
	}

	@Override
	public String toString() {
		String str = "Pile{pile=\n";
		for (T t : pile) {
			str += t + "\n";
		}
		str += "}";
		return str;
	}
	
	public void clear() {
		pile.clear();
	}
}
