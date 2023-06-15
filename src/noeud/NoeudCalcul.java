
import raytracing.Image;
import raytracing.Scene;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

public class NoeudCalcul implements ServiceNoeudCalcul {

    public Image calculerImage(Scene scene, int x, int y, int width, int height) throws RemoteException {
            return scene.compute(x, y, width, height);
    }

    public boolean isAlive() throws RemoteException {
        return true;
    }

}
