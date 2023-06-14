import raytracing.Scene;
import raytracing.Disp;
import raytracing.Image;

import java.io.IOException;
import java.net.SocketException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {

        public static void main(String[] args) {

                try {

                        if (args.length != 8 && args.length != 9) {
                                System.out.println("Usage: java Client <ip du serveur> <port de l'annuaire> <fichier> <largeur de l'image> <hauteur de l'image> <nombre de noeuds> <nombre de lignes> <nombre de colonnes> {<bypass>}");
                                System.exit(1);
                        } else {
                                String ipServeur = args[0];
                                int portAnnuaire = Integer.parseInt(args[1]);
                                String fichier = args[2];
                                int largeur = Integer.parseInt(args[3]);
                                int hauteur = Integer.parseInt(args[4]);
                                int nbNoeuds = Integer.parseInt(args[5]);
                                int nbLignes = Integer.parseInt(args[6]);

                                if (hauteur % nbLignes != 0) {
                                        System.out.println("Le nombre de lignes doit etre un diviseur de la hauteur de l'image");
                                        System.exit(1);
                                }

                                int nbColonnes = Integer.parseInt(args[7]);

                                if (largeur % nbColonnes != 0) {
                                        System.out.println("Le nombre de colonnes doit etre un diviseur de la largeur de l'image");
                                        System.exit(1);
                                }

                                boolean bypass = false;
                                if (args.length == 9) {
                                        bypass = Boolean.parseBoolean(args[8]);
                                }


                                Registry reg = LocateRegistry.getRegistry(ipServeur, portAnnuaire);

                                ServiceServeurCentral serveurCentral = (ServiceServeurCentral) reg.lookup("Raytracing");

                                ArrayList<ServiceNoeudCalcul> noeudCalculs = serveurCentral.demandeCalcul(nbNoeuds, bypass);

                                int largeurCase = largeur / nbColonnes;
                                int hauteurCase = hauteur / nbLignes;

                                Disp disp = new Disp("Raytracer", largeur, hauteur);

                                // Initialisation d'une scène depuis le modèle
                                Scene scene = new Scene(fichier, largeur, hauteur);

                                int ligne = 0;
                                int colonne = 0;

                                while (ligne < nbLignes) {

                                        ServiceNoeudCalcul noeudCalcul = noeudCalculs.get((ligne*nbColonnes + colonne) % noeudCalculs.size());

                                        final int tempColonne = colonne;
                                        final int tempLigne = ligne;

                                        new Thread(() -> {
                                                try {
                                                        Image image = noeudCalcul.calculerImage(scene, tempColonne * largeurCase, tempLigne * hauteurCase, largeurCase, hauteurCase);
                                                        disp.setImage(image, tempColonne * largeurCase, tempLigne * hauteurCase);
                                                } catch (Exception e) {
                                                        noeudCalculs.remove(noeudCalcul);
                                                }
                                        }).start();

                                        colonne++;
                                        if (colonne == nbColonnes) {
                                                colonne = 0;
                                                ligne++;
                                        }
                                }

                                for (ServiceNoeudCalcul noeudCalcul : noeudCalculs) {
                                        serveurCentral.libereNoeud(noeudCalcul);
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
