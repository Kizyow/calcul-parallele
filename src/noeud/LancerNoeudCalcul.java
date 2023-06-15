import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerNoeudCalcul {

    public static void main(String[] args) {
        try {

            if (args.length != 2) {
                System.out.println("Usage: java LancerServeurCentral <ip du serveur> <port de l'annuaire>");
                System.exit(1);
            } else {
                String ipServeur = args[0];
                int portAnnuaire = Integer.parseInt(args[1]);


                Registry reg = LocateRegistry.getRegistry(portAnnuaire);
                ServiceServeurCentral serveurCentral = (ServiceServeurCentral) reg.lookup("Raytracing");


                NoeudCalcul noeudCalcul = new NoeudCalcul();

                int Un_port = 0;
                ServiceNoeudCalcul service = (ServiceNoeudCalcul) UnicastRemoteObject.exportObject(noeudCalcul, Un_port);

                serveurCentral.enregistrerNoeud(noeudCalcul);
            }

        } catch (RemoteException e) {
            System.out.println(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

}
