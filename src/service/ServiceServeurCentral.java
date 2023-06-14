import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServiceServeurCentral extends Remote {

    void enregistrerNoeud(ServiceNoeudCalcul noeud) throws RemoteException;

    ArrayList<ServiceNoeudCalcul> demandeCalcul(int nombre, boolean bypass) throws RemoteException;

    void libereNoeud(ServiceNoeudCalcul noeud) throws RemoteException;
}
