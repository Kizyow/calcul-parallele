import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServeurCentral implements ServiceServeurCentral{

    private ArrayList<ServiceNoeudCalcul> noeudsLibres;

    private ArrayList<ServiceNoeudCalcul> noeudsOccupes;

    public ServeurCentral() {
        this.noeudsLibres = new ArrayList<ServiceNoeudCalcul>();
        this.noeudsOccupes = new ArrayList<ServiceNoeudCalcul>();
    }

    public void enregistrerNoeud(ServiceNoeudCalcul noeud) {
        this.noeudsLibres.add(noeud);
        System.out.println("Noeud enregistré");
        System.out.println("Noeuds disponibles : " + this.noeudsLibres.size());
        System.out.println("Noeuds occupés : " + this.noeudsOccupes.size());
    }

    public ArrayList<ServiceNoeudCalcul> demandeCalcul(int nombre, boolean bypass) {

        for (int i = 0; i < this.noeudsLibres.size(); i++) {
            try {
                ServiceNoeudCalcul noeud = this.noeudsLibres.get(i);
                noeud.isAlive();
            } catch (ConnectException e) {
                noeudsLibres.remove(i);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        ArrayList<ServiceNoeudCalcul> noeuds = new ArrayList<ServiceNoeudCalcul>();

        if (nombre > this.noeudsLibres.size()) {
            if (bypass) {
                nombre = this.noeudsLibres.size();
            } else {
                throw new RuntimeException("Pas assez de noeuds disponibles");
            }
        }

        for (int i = nombre-1; i >= 0; i--) {
            ServiceNoeudCalcul noeud = this.noeudsLibres.get(i);
            this.noeudsLibres.remove(noeud);
            this.noeudsOccupes.add(noeud);
            noeuds.add(noeud);
        }

        System.out.println("Noeuds disponibles : " + this.noeudsLibres.size());
        System.out.println("Noeuds occupés : " + this.noeudsOccupes.size());

        return noeuds;
    }

    public void libereNoeud(ServiceNoeudCalcul noeud) {
        if (this.noeudsOccupes.contains(noeud)) {
            this.noeudsOccupes.remove(noeud);
            this.noeudsLibres.add(noeud);

            System.out.println("Noeuds disponibles : " + this.noeudsLibres.size());
            System.out.println("Noeuds occupés : " + this.noeudsOccupes.size());
        } else {
            throw new RuntimeException("Noeud non occupé");
        }
    }
}
