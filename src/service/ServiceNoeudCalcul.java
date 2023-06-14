import raytracing.Image;
import raytracing.Scene;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceNoeudCalcul extends Remote {

    Image calculerImage(Scene scene, int x, int y, int width, int height) throws RemoteException;

    boolean isAlive() throws RemoteException;

}
