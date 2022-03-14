import java.util.*;

public class Jeton {
    static final Scanner input = new Scanner(System.in);
    static final Random rand = new Random();
    public static String[] state;
    static final int NCASES = 21;
    static final int NLIGNES = 6;
    static final String[] COULEURS = { "B", "R" };
    static final String TRAIT = "___";
    static final int NBJETONS = 20;
    static List <Integer> liste = new ArrayList<Integer>(); //utilisé par 1e IA
    public static int pos; // utilisée par 2e iaRouge
    public static int cpt = 0; // cpt pour 2e iaRouge
    public static int ligne = 1; // utilisée par 2e iaRouge
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    static boolean estOui(char reponse) {
        return "yYoO".indexOf(reponse) != -1;
    }

    public static void main(String[] args) {
        boolean newDeal;
        int scoreBleus = 0;
        int scoreRouges = 0;
        int nbTours = 0;

        int n = Integer.parseInt(args[0]);

        do {
            System.out.println("Jouer seul ? ");
            char reponse = input.next().charAt(0);
            boolean single = estOui(reponse);

            Collections.addAll(liste, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 
                                12, 13, 14, 15, 16, 17, 18, 19, 20);
            initJeu();
            afficheJeu(state);
            int val = 1;
            for (int i = 0; i < NBJETONS; i++) {
                do {
                    if (single && nbTours == 1)
                        pos = iaRouge(1);
                    else {
                        /*
                        System.out.print("saisir un numéro entre 0 et 20 : ");
                        pos = input.nextInt();
                        */
                        pos = iaRouge(2);
                        
                    }
                } while (!jouer(COULEURS[nbTours], val, pos));
                cpt = 0; 
                System.out.println(COULEURS[nbTours] + " joue son jeton #" +
                        val + " en : " + pos);
                if (nbTours == 1) {
                    nbTours = 0;
                    val += 1;
                } else
                    nbTours = 1;
                afficheJeu(state);
            }
            System.out.println("la case vide est la " + getIdVide() + " ème.");
            int sumR = sommeVoisins("R");
            int sumB = sommeVoisins("B");
            if (sumB < sumR) {
                scoreBleus++;
                System.out.println(ANSI_BLUE + "Les bleus gagnent leur " +
                        scoreBleus + " victoire par " + sumB + " à " + sumR +
                        ANSI_RESET);
                System.out.println("Total de partie : "
                + (scoreBleus+scoreRouges) + " ; score : "
                + scoreRouges + " pour les rouges à " + scoreBleus
                + " pour les bleus.");
            }
            else if (sumB == sumR) {
                System.out.println("Égalité : " + sumB + " partout !");
                System.out.println("Total de partie : "
                + (scoreBleus+scoreRouges) + " ; score : "
                + scoreRouges + " pour les rouges à " + scoreBleus
                + " pour les bleus.");
            }
            else {
                scoreRouges++;
                System.out.println(ANSI_RED + "Les rouges gagnent leur " +
                        scoreRouges + " victoire par " + sumR + " à " + sumB +
                        ANSI_RESET);
                System.out.println("Total de partie : "
                + (scoreBleus+scoreRouges) + " ; score : "
                + scoreRouges + " pour les rouges à " + scoreBleus
                + " pour les bleus.");
            }

            System.out.println("Nouvelle partie ? ");
            reponse = input.next().charAt(0);
            newDeal = estOui(reponse);
        } while (newDeal);

        System.out.println("Bye Bye !");
        System.exit(0);
    }

    /**
     * Initialise le jeu avec un double/triple underscore à chaque case
     * signifiant 'case vide'
     */
    public static void initJeu() {
        state = new String[NCASES];
        for (int i = 0; i < state.length; i++) {
            state[i] = TRAIT;
        }
    }

    /**
     * Affiche le plateau de jeu en mode texte
     */
    public static void afficheJeu(String[] state) {
        int cpt = 0;
        for (int i = 0; i < NLIGNES; i++) {
            System.out.print(cpt + ":\t");
            for (int j = 0; j < NLIGNES - i; j++)
                System.out.print("  ");
            for (int k = 0; k <= i; k++) {
                if (state[cpt].charAt(0) == COULEURS[1].charAt(0))
                    System.out.print(ANSI_RED + state[cpt] + ANSI_RESET + " ");
                else if (state[cpt].charAt(0) == COULEURS[0].charAt(0))
                    System.out.print(ANSI_BLUE + state[cpt] + ANSI_RESET + " ");
                else
                    System.out.print(state[cpt] + " ");
                if (state[cpt].length() < 3) {
                    System.out.print(" ");
                }
                cpt = cpt + 1;
            } System.out.println("\n");
        }
    }

    /**
     * Place un jeton sur le plateau, si possible.
     * 
     * @param couleur couleur du jeton : COULEURS[0] ou COULEURS[1]
     * @param val     valeur faciale du jeton
     * @param pos     position (indice) de l'emplacement où placer le jeton
     * @return true si le jeton a pu être posé, false sinon.
     */
    public static boolean jouer(String couleur, int val, int pos) {
        if (state[pos].equals(TRAIT)) {
            state[pos] = couleur + String.valueOf(val);
            return true;
        } else
            return false;
    }

    /**
     * Retourne l'indice de la case débutant la ligne idLigne
     * 
     * @param idLigne indice de la ligne. La première ligne est la ligne #0.
     * @return l'indice de la case la plus à gauche de la ligne
     */
    public static int idDebutLigne(int idLigne) {
        int idDebutLigne = 0;
        for (int i = 0; i < idLigne; i++) {
            idDebutLigne += i;
        } return idDebutLigne;
    }

    /**
     * Retourne l'indice de la case terminant la ligne idLigne
     * 
     * @param idLigne indice de la ligne. La première ligne est la ligne #0.
     * @return l'indice de la case la plus à droite de la ligne
     */
    public static int idFinLigne(int idLigne) {
        return idDebutLigne(idLigne) + idLigne - 1;
    }

    /**
     * Renvoie la position du jeton manquant
     * 
     * @return l'indice de la case non occupee
     */
    public static int getIdVide() {
        int idVide = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i].equals(TRAIT))
                idVide = i;
        }
        return idVide;
    }

    /**
     * determine l'identifiant de la ligne d'une case
     * 
     * @param idCase la case dont on souhaite connaitre la ligne
     * @return l'identifiant de la case
     */
    public static int getIdLigne(int idCase) {
        int idLigne = 1;
        for (int i = 1; i <= idCase; i++) {
            if (i == 1 || i == 3 || i == 6 || i == 10 || i == 15)
                idLigne += 1;
        }
        return idLigne;
    }

    /**
     * fait la somme des poids des voisins de couleur col
     * (6 voisins au maximum)
     * 
     * @param col couleur des voisins consideres
     * @return somme des poids
     */
    public static int sommeVoisins(String col) {
        int sumCol = 0;
        int idV = getIdVide();
        int idLCaseV = getIdLigne(idV);
        for (int i = 0; i < NCASES; i++) {
            if (state[i].charAt(0) == col.charAt(0)) {
                // caseinfGauche et caseinfDroite dont case 0
                if ((idLCaseV < NLIGNES) && ((i == idV + idLCaseV) ||
                        (i == idV + (idLCaseV + 1))))
                    sumCol += Integer.parseInt(state[i].substring(1));
                // casesupGauche;casesupDroite;casecoteGauche et case coteDroite
                if (1 < idLCaseV && idV != idDebutLigne(idLCaseV) &&
                        idV != idFinLigne(idLCaseV) &&
                        ((i == idV - idLCaseV) || (i == idV - (idLCaseV - 1)) ||
                                (i == idV - 1) || (i == idV + 1)))
                    sumCol += Integer.parseInt(state[i].substring(1));
                // casesupDroite et casecoteDroite pour idV = un début de ligne
                if (1 < idLCaseV && idV == idDebutLigne(idLCaseV) &&
                        ((i == idV - (idLCaseV - 1)) || (i == idV + 1)))
                    sumCol += Integer.parseInt(state[i].substring(1));
                // casesupGauche et coteGauche pour idV = un fin de ligne
                if (1 < idLCaseV && idV == idFinLigne(idLCaseV) &&
                        ((i == idV - idLCaseV) || (i == idV - 1)))
                    sumCol += Integer.parseInt(state[i].substring(1));
            }
        } return sumCol;
    }

    /**
     * Renvoie le prochain coup à jouer pour les rouges
     * Algo naïf = la première case dispo
     * @return id de la case
     */
    public static int iaRouge(int quelle_IA) {
        int numCase = 0;
        int var;
        // première intéligence artificielle
        if(quelle_IA==1){
            var = rand.nextInt(liste.size());
            numCase = liste.get(var);
            liste.remove(var);
        }
        else if (quelle_IA==2){
            switch (cpt){
                case 0: {
                    numCase = idDebutLigne(getIdLigne(pos));
                    cpt++;
                    break;
                }
                case 1: {
                    numCase = idFinLigne(getIdLigne(pos));
                    cpt++;
                    break;
                }
                case 2: {
                    numCase = idDebutLigne(ligne);
                    ligne += 1;
                    if (ligne > NLIGNES) {
                        cpt++;
                        ligne = 1;
                    }
                    break;
                }
                case 3: {
                    numCase = idFinLigne(ligne);
                    ligne += 1;
                    if (ligne > NLIGNES) {
                        cpt++;
                        ligne = 1;
                    }
                    break;
                }
                case 4: {
                    var = rand.nextInt(liste.size());
                    numCase = liste.get(var);
                    liste.remove(var);                    
                    break;
                }
            }
        } return numCase;
    }
}
