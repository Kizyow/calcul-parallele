import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServeurCentral implements ServiceServeurCentral{

    private ArrayList<ServiceNoeudCalcul> noeuds;
    private int noeudCourant;

    public ServeurCentral() {
        this.noeuds = new ArrayList<ServiceNoeudCalcul>();
    }

    public void enregistrerNoeud(ServiceNoeudCalcul noeud) {
        this.noeuds.add(noeud);
        System.out.println("Noeud enregistré");
    }

    public synchronized void supprimerNoeud(ServiceNoeudCalcul noeud) {
        if (this.noeuds.remove(noeud)) {
            System.out.println("Noeud supprimé");
        }
    }

    public synchronized ServiceNoeudCalcul demandeCalcul() {
        if (this.noeudCourant >= this.noeuds.size()) {
            this.noeudCourant = 0;
        }
        if (this.noeuds.size() == 0) {
            return null;
        }

        ServiceNoeudCalcul noeud = this.noeuds.get(this.noeudCourant);
        this.noeudCourant++;
        return noeud;
    }

    public boolean isAlive() throws RemoteException {
        return true;
    }

}
