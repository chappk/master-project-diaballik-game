package moteur;


import java.awt.Point;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dodelien
 */
public enum Direction {

    AUCUN(0), NORD(1), OUEST(2), SUD(3), EST(4);
    private int i;
    private Point vecteur;

    Direction(int i) {
        this.i = i;
        vecteur = new Point(0, 0);
        switch (i) {
            case 1:
                vecteur.y--;
                break;
            case 2:
                vecteur.x--;
                break;
            case 3:
                vecteur.y++;
                break;
            case 4:
                vecteur.x++;
                break;
        }
    }

    public Direction oppose() {
        switch (this) {
            case NORD:
                return SUD;
            case EST:
                return OUEST;
            case SUD:
                return NORD;
            case OUEST:
                return EST;
            default:
                return null;
        }
    }

    public Point getVecteur() {
        return vecteur;
    }

    @Override
    public String toString() {
        return "Direction{" + "i=" + i + ", vecteur=" + vecteur + '}';
    }
	
	public static Direction getDirection(Point p1, Point p2){
		int x = p2.x - p1.x;
		int y = p2.y - p1.y;
		if(x==1){
			return EST;
		}
		else if(x==-1){
			return OUEST;
		}
		else if(y==1){
			return SUD;
		}
		else if (y==-1){
			return NORD;
		}
		else{
			return AUCUN;
		}
		
	}
}
