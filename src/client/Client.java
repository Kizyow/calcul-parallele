import raytracing.Scene;
import raytracing.Disp;
import raytracing.Image;

import java.io.IOException;
import java.net.SocketException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {

        public static void main(String[] args) {

                try {

                        if (args.length != 6) {
                                System.out.println("Usage: java Client <ip du serveur> <port de l'annuaire> <fichier> <largeur de l'image> <hauteur de l'image> <taille du decoupage>");
                                System.exit(1);
                        } else {
                                String ipServeur = args[0];
                                int portAnnuaire = Integer.parseInt(args[1]);
                                String fichier = args[2];
                                int largeur = Integer.parseInt(args[3]);
                                int hauteur = Integer.parseInt(args[4]);
                                int taille = Integer.parseInt(args[5]);

                                if (taille > 512) {
                                        taille = 512;
                                        System.out.println("La taille du decoupage est trop grande, elle a été fixée à 512");
                                }

                                Registry reg = LocateRegistry.getRegistry(ipServeur, portAnnuaire);

                                ServiceServeurCentral serveurCentral = (ServiceServeurCentral) reg.lookup("Raytracing");

                                Disp disp = new Disp("Raytracer", largeur, hauteur);

                                // Initialisation d'une scène depuis le modèle
                                Scene scene = new Scene(fichier, largeur, hauteur);

                                int ligne = 0;
                                int colonne = 0;
                                final int tailleDecoupage = taille;

                                while (ligne < tailleDecoupage) {

                                        final int tempColonne = colonne;
                                        final int tempLigne = ligne;

                                        new Thread(() -> {
                                                boolean fin = false;
                                                ServiceNoeudCalcul noeudCalcul = null;
                                                while (!fin) {
                                                        try {
                                                                noeudCalcul = serveurCentral.demandeCalcul();

                                                                if (noeudCalcul == null) {
                                                                        try {
                                                                                Thread.sleep(1000);
                                                                        } catch (InterruptedException e) {
                                                                                System.out.println("Erreur lors de l'attente d'un noeud de calcul");
                                                                        }
                                                                } else {
                                                                        Image image = noeudCalcul.calculerImage(scene, tempColonne * largeur / tailleDecoupage, tempLigne * hauteur / tailleDecoupage, (int) Math.ceil((double) largeur / (double) tailleDecoupage), (int) Math.ceil((double) hauteur / (double) tailleDecoupage));
                                                                        disp.setImage(image, tempColonne * largeur / tailleDecoupage, tempLigne * hauteur / tailleDecoupage);
                                                                        fin = true;
                                                                }

                                                        } catch (ConnectException e) {
                                                                try {
                                                                        serveurCentral.supprimerNoeud(noeudCalcul);
                                                                } catch (RemoteException e1) {
                                                                        try {
                                                                                serveurCentral.isAlive();
                                                                                System.out.println("Erreur lors de la suppression d'un noeud de calcul");
                                                                        } catch (RemoteException e2) {
                                                                                System.out.println("Serveur central non disponible. Le client va s'arreter");
                                                                                System.exit(1);
                                                                        }
                                                                }
                                                        } catch (RemoteException e) {
                                                                try {
                                                                        serveurCentral.isAlive();
                                                                        System.out.println("Erreur lors de la recuperation d'un noeud de calcul");
                                                                } catch (RemoteException e2) {
                                                                        System.out.println("Serveur central non disponible. Le client va s'arreter");
                                                                        System.exit(1);
                                                                }
                                                        }
                                                }
                                        }).start();

                                        colonne++;
                                        if (colonne == tailleDecoupage) {
                                                colonne = 0;
                                                ligne++;
                                        }
                                }

                        }

                } catch (IOException e) {
                        System.out.println("Connexion à la reference distante echouee");
                } catch (NotBoundException e) {
                        System.out.println("Service non trouve");
                } catch (RuntimeException e) {
                        System.out.println(e);
                }

        }
}
