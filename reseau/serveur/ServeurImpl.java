package reseau.serveur;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import moteur.Pion;
import reseau.Client;
import reseau.Partie;
import reseau.Serveur;

/**
 *
 * @author vernagaa
 */
class ServeurImpl extends UnicastRemoteObject implements Serveur, Runnable {

	private final HashMap<Integer, PartieImpl> parties;
	private final HashMap<Integer, Client> clients;
	private final HashMap<Integer, String> pseudos;
	private final HashMap<Integer, PartieImpl> partie_client;
	private int cptPartie;
	private int cptClient;

	ServeurImpl() throws RemoteException {
		parties = new HashMap<Integer, PartieImpl>();
		clients = new HashMap<Integer, Client>();
		pseudos = new HashMap<Integer, String>();
		partie_client = new HashMap<Integer, PartieImpl>();
		cptPartie = 1;
		cptClient = 1;
	}

	@Override
	public void run() {
		while(true) {
		ArrayList<Integer> perdus = new ArrayList<Integer>();
			synchronized (clients) {
				for (Map.Entry<Integer, Client> val : clients.entrySet()) {
					try {
						val.getValue().ping();
					} catch (RemoteException ex) {
						System.out.println("PERDU : " + getPseudo(val.getKey()) + "(" + val.getKey() + ")");
						perdus.add(val.getKey());
					}
				}
				// suppression des clients perdus
				supprimerClients(perdus);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(ServeurImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public int creerPartie(int idClient, String nomPartie, boolean variante) {
		nomPartie = nomPartie.trim();
		
		if(nomPartie.isEmpty())
			nomPartie = getPseudo(idClient);
		
		synchronized (parties) {
			try {
				if (nomPartie == null || nomPartieExiste(nomPartie))
					return -1;

				int idPartie = cptPartie++;

				PartieImpl p = new PartieImpl(idPartie, nomPartie, variante, this);
				Client c = getClient(idClient);

				p.lierJoueur(new JoueurServ(c, idClient, Pion.BLANC));
				parties.put(idPartie, p);

				System.out.println("CRÉATION : partie " + idPartie + " par " + getPseudo(idClient) + "(" + idClient + ")");
				c.recevoirInformation("Vous avez créé la partie " + idPartie);
				c.setPartie(p);
				partie_client.put(idClient, p);
				p.initialiser();
				return idPartie;
			} catch (RemoteException ex) {
				Logger.getLogger(ServeurImpl.class.getName()).log(Level.SEVERE, null, ex);
				return -1;
			}
		}
	}
	
	@Override
	public int rejoindrePartie(int idClient, int idPartie) {
		synchronized (parties) {
			PartieImpl p = getPartie(idPartie);
			if(p == null) // partie non trouvée
				return PARTIE_NON_TROUVE;
			
			if(partie_client.get(idClient) == p) // client déjà associé à cette partie
				return PARTIE_DEJA_CONNECTE;
			
			Client c = getClient(idClient);
			try {
				JoueurServ j = new JoueurServ(c, idClient, Pion.NOIR);

				if (p.lierJoueur(j)) {
					c.setPartie(p);
					partie_client.put(idClient, p);
					System.out.println("AJOUT : " + getPseudo(idClient) + "(" + idClient + ")" + " ajouté à la partie " + idPartie);
					c.recevoirInformation("Vous avez été ajouté a la partie " + idPartie);
					p.demarrer();
					return PARTIE_OK;
				} else {
					c.recevoirInformation("Partie " + idPartie + " pleine");
					return PARTIE_PLEINE;
				}
			} catch (RemoteException ex) {
				Logger.getLogger(ServeurImpl.class.getName()).log(Level.SEVERE, null, ex);
				return PARTIE_ERREUR;
			}
		}
	}

	@Override
	public void envoyerMessageGeneral(String message, int idEmetteur) {
//		ArrayList<Integer> perdus = new ArrayList<Integer>();
		synchronized (clients) {
			for (Map.Entry<Integer, Client> val : clients.entrySet()) {
				try {
					val.getValue().recevoirMessage(message, getPseudo(idEmetteur));
					System.out.println("MESSAGE : " + getPseudo(idEmetteur) + "(" + idEmetteur + ")" + " => " + getPseudo(val.getKey()) + "(" + val.getKey() + ")" + " : \"" + message + "\"");
				} catch (RemoteException ex) {
					// destinataire perdu
					System.out.println("MESSAGE (échec) : " + getPseudo(idEmetteur) + "(" + idEmetteur + ")" + " => " + getPseudo(val.getKey()) + "(" + val.getKey() + ")" + " : \"" + message + "\"");
//					perdus.add(val.getKey());
				}
			}
			// suppression des clients perdus
//			supprimerClients(perdus);
		}
	}

	@Override
	public boolean envoyerMessagePrive(String message, int idEmetteur, String pseudoDestinataire) {
		boolean echec = false;
		synchronized (clients) {
			int idDestinataire = getIdPseudo(pseudoDestinataire);
			if (idDestinataire > 0) {
				try {
					getClient(idDestinataire).recevoirMessagePrive(message, getPseudo(idEmetteur));
					System.out.println("MP : " + getPseudo(idEmetteur) + "(" + idEmetteur + ")" + " => " + getPseudo(idDestinataire) + "(" + idDestinataire + ")" + " : \"" + message + "\"");
				} catch (RemoteException ex) {
					// destinataire perdu
					System.out.println("MP (échec) : " + getPseudo(idEmetteur) + "(" + idEmetteur + ")" + " => " + getPseudo(idDestinataire) + "(" + idDestinataire + ")" + " : \"" + message + "\"");
					echec = true;
				} catch (NullPointerException ex) {
					// destinataire inexistant/supprimé
					System.out.println("MP (échec) : " + getPseudo(idEmetteur) + "(" + idEmetteur + ")" + " => " + getPseudo(idDestinataire) + "(" + idDestinataire + ")" + " : \"" + message + "\"");
					echec = true;
				}

				if (echec)
					return false;
				else { // cas normal: accusé
					try {
						getClient(idEmetteur).recevoirMessagePriveAccuse(message, pseudoDestinataire);
					} catch (RemoteException ex) {
					}
				}
				return true;
			}
			else
				return false;
		}
	}

	@Override
	public int connexionClient(Client c) {
		try {
			String pseudo = c.getPseudo();
			synchronized (clients) {
				if (pseudoValide(pseudo)) {
					if (!pseudoExiste(pseudo)) {
						int id = cptClient++;
						c.setIdClient(id);
						clients.put(id, c);
						pseudos.put(id, pseudo);
						System.out.println("CONNEXION : " + getPseudo(id) + " => " + id);
						informerClients(pseudo + " vient de se connecter");
						return id;
					}
					else
					{
						return PSEUDO_EXISTANT;
					}
				} else {
					return PSEUDO_INVALIDE;
				}
			}
		} catch (RemoteException ex) {
			Logger.getLogger(ServeurImpl.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
	}

	@Override
	public HashMap<Integer, String> getPseudos() {
		synchronized (clients) {
			return pseudos;
		}
	}

	@Override
	public HashMap<Integer, Partie> getPartiesDispo() {
		HashMap<Integer, Partie> res = new HashMap<Integer, Partie>();
		synchronized (parties) {
			for (Map.Entry<Integer, PartieImpl> val : parties.entrySet())
				if (val.getValue().getJoueur(Pion.NOIR) == null)
					res.put(val.getKey(), val.getValue());
		}
		return res;
	}

	@Override
	public HashMap<Integer, String> getNomPartiesDispo() {
		HashMap<Integer, String> res = new HashMap<Integer, String>();
		synchronized (parties) {
			for (Map.Entry<Integer, PartieImpl> val : parties.entrySet()) {
				if (val.getValue().getJoueur(Pion.NOIR) == null)
					res.put(val.getKey(), val.getValue().getNom());
			}
		}
		return res;
	}

	void supprimerPartie(PartieImpl partie) {
		synchronized (parties) {
			try {
				// Arrêt du thread du RMI
				UnicastRemoteObject.unexportObject(partie, true);
			} catch (NoSuchObjectException ex) {
			}
			JoueurServ joueur = partie.getJoueur(Pion.BLANC);
			if(joueur != null)
				partie_client.remove(joueur.getId());
			
			joueur = partie.getJoueur(Pion.NOIR);
			if(joueur != null)
				partie_client.remove(joueur.getId());
			
			partie.demarrer();
			parties.remove(partie.getId());
		}
	}

	private PartieImpl getPartie(int idPartie) {
		return parties.get(idPartie);
	}

	private Client getClient(int idClient) {
		return clients.get(idClient);
	}

	private String getPseudo(int idClient) {
		return pseudos.get(idClient);
	}

	private int getIdPseudo(String pseudo) {
		for (Map.Entry<Integer, String> val : pseudos.entrySet()) {
			if (pseudo.equals(val.getValue()))
				return val.getKey();
		}
		return -1;
	}
	
	private void supprimerClients(ArrayList<Integer> idClients) {
		for (int id : idClients) {
			System.out.println("SUPPRESSION : " + getPseudo(id)+"("+id+")");
			clients.remove(id);
			PartieImpl partie = partie_client.get(id);
			if(partie != null) {
				partie.joueurPerdu(id);
				supprimerPartie(partie);
			}
		}
		
		for (int id : idClients) {
			informerClients(getPseudo(id) + " s'est déconnecté");
			pseudos.remove(id);
		}
	}
	
	private boolean pseudoValide(String pseudo) {
		//TODO plus de contraintes de pseudo (blacklist ?)
		return pseudo.length() > 0
				&& pseudo.length() <= 20
				&& !pseudo.contains(" ");
	}

	private boolean pseudoExiste(String pseudo) {
		return pseudos.containsValue(pseudo);
	}

	private boolean nomPartieExiste(String nom) {
		for(Map.Entry<Integer, PartieImpl> val : parties.entrySet()) {
				if(val.getValue().getNom().equals(nom))
					return true;
		}
		return false;
	}
	
	private void informerClients(String info) {
//		ArrayList<Integer> perdus = new ArrayList<Integer>();
		synchronized (clients) {
			for (Map.Entry<Integer, Client> val : clients.entrySet()) {
				try {
					val.getValue().recevoirInformation(info);
					System.out.println("INFORMATION : " + getPseudo(val.getKey()) + "(" + val.getKey() + ")" + " <= " + "\"" + info + "\"");
				} catch (RemoteException ex) {
					// destinataire perdu
					System.out.println("INFORMATION (échec) : " + getPseudo(val.getKey()) + "(" + val.getKey() + ")" + " <= " + "\"" + info + "\"");
//					perdus.add(val.getKey());
				}
			}
			// suppression des clients perdus
//			supprimerClients(perdus);
		}
	}

}
