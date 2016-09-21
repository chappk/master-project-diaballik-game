/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moteur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicolas
 */
public class JoueurMonteCarloLasVegasIA extends JoueurIA {

    private class PaireNombreSomme {

        int nombre;
        int somme;

        PaireNombreSomme() {
            nombre = 0;
            somme = 0;
        }

        float getMoyenne() {
            return (float) somme / nombre;
        }

        void ajouter(int n) {
            nombre++;
            somme += n;
        }
    }

    public JoueurMonteCarloLasVegasIA(short couleur) {
        super("Carlos Le Poulpe", couleur);
    }
    
    @Override
    public Coup choisirCoup(Plateau plateau) {
        return choisirCoup(plateau, 11, 50000, 4);
    }

    protected Coup choisirCoup(Plateau plateau, final int profondeur, final int nbCoups, int nbThreads) {
        Thread[] threads = new Thread[nbThreads];

        final float[] meilleursScores = new float[nbThreads];
        final Coup[] meilleursCoups = new Coup[nbThreads];

        final Plateau[] plateaux = new Plateau[nbThreads];

        final short couleurAdversaire = adversaire.getCouleur();

        for (int t = 0; t < nbThreads; t++) {
            plateaux[t] = plateau.clone();
            final int T = t;
            final HashMap<Coup, PaireNombreSomme> moyennes = new HashMap<Coup, PaireNombreSomme>();
            threads[t] = new Thread(new Runnable() {

                @Override
                public void run() {
                    Plateau plateau = plateaux[T];
                    int val;

                    Coup[] coups = new Coup[profondeur];

                    for (int i = 0; i < nbCoups; i++) {
                        int j;
                        for (j = 0; j < profondeur; j++) {
                            if (j % 2 == 0) {
                                coups[j] = coupAleatoire(plateau);
                            } else {
                                coups[j] = adversaire.coupAleatoire(plateau);
                            }

                            if (j == 0) {
                                coups[j].setPositionPions(plateau.getPions(couleur));
                            }

                            if (j == profondeur - 1) {
                                val = evaluation(plateau);
                                PaireNombreSomme pns = moyennes.get(coups[0]);
                                if (pns == null) {
                                    pns = new PaireNombreSomme();
                                    moyennes.put(coups[0], pns);
                                }
                                pns.ajouter(val);
                            } else if (j % 2 == 0) {
                                if (plateau.balleAuBut(couleur)) {
                                    j++;
                                    val = INFINI;
                                    PaireNombreSomme pns = moyennes.get(coups[0]);
                                    if (pns == null) {
                                        pns = new PaireNombreSomme();
                                        moyennes.put(coups[0], pns);
                                    }
                                    pns.ajouter(val);
                                    break;
                                }
                            } else {
                                if (plateau.balleAuBut(couleurAdversaire)) {
                                    j++;
                                    val = -INFINI;
                                    PaireNombreSomme pns = moyennes.get(coups[0]);
                                    if (pns == null) {
                                        pns = new PaireNombreSomme();
                                        moyennes.put(coups[0], pns);
                                    }
                                    pns.ajouter(val);
                                    break;
                                }
                            }
                        }

                        for (int k = j - 1; k >= 0; k--) {
                            coups[k].annuler(plateau);
                        }
                    }

                    float meilleurScore = -INFINI;
                    Coup meilleurCoup = new Coup();

                    for (Map.Entry<Coup, PaireNombreSomme> entry : moyennes.entrySet()) {
                        float moyenne = entry.getValue().getMoyenne();
                        if (moyenne > meilleurScore) {
                            meilleurScore = moyenne;
                            meilleurCoup.copier(entry.getKey());
                        }
                    }

                    meilleursScores[T] = meilleurScore;
                    meilleursCoups[T] = meilleurCoup;
                }
            });
        }

        for (int t = 0; t < nbThreads; t++) {
            threads[t].start();
        }

        for (int t = 0; t < nbThreads; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(JoueurMonteCarloLasVegasIA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Coup meilleurCoup = meilleursCoups[0];
        float meilleurScore = meilleursScores[0];

        for (int t = 1; t < nbThreads; t++) {
            if (meilleursScores[t] > meilleurScore) {
                meilleurScore = meilleursScores[t];
                meilleurCoup = meilleursCoups[t];
            }
        }

        meilleurCoup.convertir(plateau);
        
		return meilleurCoup;
    }

    private int evaluation(Plateau plateau) {
        int eval = 0;

        short couleurAd = getAdversaire().getCouleur();

        if (plateau.balleAuBut(couleurAd)) {
            eval = -INFINI;
        } else if (plateau.balleAuBut(couleur)) {
            eval = INFINI;
        } else if (plateau.coupsFinaux(couleurAd) > 0) {
            eval = -INFINI / 2;
        } else if (plateau.coupsFinaux(couleur) > 0) {
            eval = INFINI / 2;
        }

        for (Pion pion : plateau.getPions(couleur)) {
            eval += pion.getDistanceDepart();
        }

        ArrayList<Passe> passes = plateau.passesPossibles(couleur);
		int evalPasse = 0;
		int passesGagnantes = 0;
        for (Passe passe : passes) {
            int d = passe.getPion().getDistanceDepart();
            evalPasse += d;
            if (d == 6) {
                passesGagnantes++;
            }
        }
		eval += Math.pow(evalPasse, passesGagnantes);

        eval += Math.pow(plateau.getBallon(couleur).getDistanceDepart(), 2);
        return eval;
    }
}
