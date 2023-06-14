import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServeurCentral {
    public static void main (String args[]) throws RemoteException {

        try {

            if (args.length != 1) {
                System.out.println("Usage: java LancerServeurCentral <port de l'annuaire>");
                System.exit(1);
            } else {
                int portAnnuaire = Integer.parseInt(args[0]);

                ServeurCentral serveurCentral = new ServeurCentral();

                int Un_port = 0;
                ServiceServeurCentral service = (ServiceServeurCentral) UnicastRemoteObject.exportObject(serveurCentral, Un_port);

                Registry reg = LocateRegistry.createRegistry(portAnnuaire);
                reg.rebind("Raytracing", service);
            }


        } catch (RemoteException e) {
            System.out.println(e);
        }
    }
}
