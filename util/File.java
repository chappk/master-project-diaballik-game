package util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author disavinr
 */
public class File<T> implements Serializable{
    
    ArrayList<T> file;
    
    public File(){
        file = new ArrayList<T>();
    }
    
    /**
     * Ajouter un élément dans la pile
     * @param elem 
     */
    public void add(T elem){
        file.add(elem);
    }
    
    /**
     * Enlève le sommet de la pile et le retourne
     * @return le sommet de la pile
     */
    public T pop(){
        T elem = file.get(0);
        file.remove(0);
        
        return elem;
    }
    
    /**
     * Teste si la pile pile est vide ou non
     * @return vrai si la pile est vide
     */
    public boolean isEmpty(){
        return file.isEmpty();
    }
    
    /**
     * Retourne le sommet de la pile
     * @return 
     */
    public T pic(){
        return file.get(0);
    }
	
	public void clear() {
		file.clear();
	}
}
