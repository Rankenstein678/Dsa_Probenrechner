package com.rankenstein.dsahelper.logic;

public class ChanceLogic {
    /**
     * @param e1  Eigenschaft 1
     * @param e2  Eigenschaft 2
     * @param e3  Eigenschaft 3
     * @param taw Talentwert
     * @param mod Modifikator (Erleichterung/Erschwernis)
     * @return [0]:Chance, [1]:durchschnittliche TaP*
     */
    public static double[] calculateChance(int e1, int e2, int e3, int taw, int mod) {
        int passed = 0; //Erfolgreiche Proben
        int tap = 0; //Gesamt TaP*

        final int tawMod = taw - mod; //Talentwert inklusive Modifikator

        for (int w1 = 1; w1 <= 20; w1++) {
            int t1 = tawMod; //bearbeitbarer TaW Zwischenstand
            int erg1 = tawMod < 0 ? w1 - tawMod : w1; //Erhöhe das Würfelergebnis bei negativem Talentwert, sonst setze mit taw gleich

            //Gleicht Differenz zwischen Würfelergebnis und Eigenschaft mit TaW aus und verringert TaW dementsprechend
            if (erg1 > e1 && t1 > 0) {
                if (erg1 - t1 <= e1) {
                    if (erg1 - e1 < t1) {
                        int unmodifiedValue = erg1;
                        erg1 -= erg1 - e1;
                        t1 -= unmodifiedValue - e1;
                    } else {
                        erg1 -= t1;
                        t1 = 0;
                    }
                }
            }

            for (int w2 = 1; w2 <= 20; w2++) {
                int t2 = t1; //bearbeitbarer TaW Zwischenstand
                int erg2 = tawMod < 0 ? w2 - tawMod : w2; //Erhöhe das Würfelergebnis bei negativem Talentwert, sonst setze mit taw gleich

                //Gleicht Differenz zwischen Würfelergebnis und Eigenschaft mit TaW aus
                if (erg2 > e2 && t2 > 0) {
                    if (erg2 - t2 <= e2) {
                        if (erg2 - e2 < t2) {
                            int unmodifiedValue = erg2;
                            erg2 -= erg2 - e2;
                            t2 -= unmodifiedValue - e3;
                        } else {
                            erg2 -= t2;
                            t2 = 0;
                        }
                    }
                }

                for (int w3 = 1; w3 <= 20; w3++) {
                    int t3 = t2; //bearbeitbarer TaW Zwischenstand
                    int erg3 = tawMod < 0 ? w3 - tawMod : w3; //Erhöhe das Würfelergebnis bei negativem Talentwert, sonst setze mit taw gleich

                    //Gleicht Differenz zwischen Würfelergebnis und Eigenschaft mit TaW aus
                    if (erg3 > e3 && t3 > 0) {
                        if (erg3 - t3 <= e3) {
                            if (erg3 - e3 < t3) {
                                int unmodifiedValue = erg3;
                                erg3 -= erg3 - e3;
                                t3 -= unmodifiedValue - e3;
                            } else {
                                erg3 -= t3;
                                t3 = 0;
                            }
                        }
                    }

                    //Auswertung
                    if ((w1 == 1 && w2 == 1) || (w1 == 1 && w3 == 1) || (w2 == 1 && w3 == 1)) { //Doppel 1 = Erfolg
                        passed++;
                        tap += taw;
                        continue;
                    }
                    if ((w1 == 20 && w2 == 20) || (w1 == 20 && w3 == 20) || (w2 == 20 && w3 == 20)) { //Doppel 20 = Misserfolg
                        continue;
                    }
                    if (e1 >= erg1 && e2 >= erg2 && e3 >= erg3) { //normale Probe
                        passed++;
                        tap += Math.min(t3, taw); //Falls TaP*>TaW addiere TaW Punkte
                    }

                }
            }
        }
        double chance = passed / 8000.0;
        double avrgTaP = tap * 1.0/ passed; //durchschnittliche TaP* werden nur aus den erfolgreichen Proben berechnet
        return new double[]{chance, avrgTaP};
    }
}
