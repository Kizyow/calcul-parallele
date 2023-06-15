import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServiceServeurCentral extends Remote {

    void enregistrerNoeud(ServiceNoeudCalcul noeud) throws RemoteException;

    ServiceNoeudCalcul demandeCalcul() throws RemoteException;

    void supprimerNoeud(ServiceNoeudCalcul noeud) throws RemoteException;

    boolean isAlive() throws RemoteException;

}
