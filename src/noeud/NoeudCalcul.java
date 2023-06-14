
import raytracing.Image;
import raytracing.Scene;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

public class NoeudCalcul implements ServiceNoeudCalcul {

    private ServiceServeurCentral serveurCentral;

    NoeudCalcul(ServiceServeurCentral serveurCentral) {
        this.serveurCentral = serveurCentral;
    }

    public Image calculerImage(Scene scene, int x, int y, int width, int height) throws RemoteException {
        try {
            return scene.compute(x, y, width, height);
        } catch (Exception e) {
            this.serveurCentral.libereNoeud(this);
            return null;
        }
    }

    public boolean isAlive() throws RemoteException {
        return true;
    }

}
