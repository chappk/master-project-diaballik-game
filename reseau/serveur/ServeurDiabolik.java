package reseau.serveur;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vernagaa
 */
public class ServeurDiabolik {

	static int port = 1099;
	static String ip = "127.0.0.1";
	static String service = "diaballik";

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws UnknownHostException {

		// Vérification des arguments
		if (args.length % 2 != 0) {
			show_usage();
			System.exit(1);
		}

		for (int i = 0; i <= args.length - 2; i += 2) {
			if (args[i].equals("-p"))
				port = Integer.parseInt(args[i + 1]);
			else if (args[i].equals("-s"))
				service = args[i + 1];
			else {
				show_usage();
				System.exit(1);
			}
		}

		try {
			ip = InetAddress.getLocalHost().getHostAddress();

			System.out.println("IP : " + ip);
			System.out.println("Port : " + port);
			System.out.println("Service : " + service);
			
			ServeurImpl diaballik = new ServeurImpl();

			System.setProperty("java.rmi.server.hostname", ip);
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind(service, diaballik);

			new Thread(diaballik, "Serveur Diaballik").start();

			System.out.println("Serveur lancé");
			System.out.println();
		} catch (Exception ex) {
			Logger.getLogger(ServeurDiabolik.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		}
	}

	/*
	 * affiche la syntaxe de la commande d'exécution
	 */
	private static void show_usage() {
		System.out.println("Utilisation : [-p <port>] [-s <service>]");
	}
}
