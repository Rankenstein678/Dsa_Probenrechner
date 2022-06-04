package com.rankenstein.dsahelper.logic;

public class ChanceLogic {
    /**
     * @param eig1  Eigenschaft 1
     * @param eig2  Eigenschaft 2
     * @param eig3  Eigenschaft 3
     * @param taw Talentwert
     * @param mod Modifikator (Erleichterung/Erschwernis)
     * @return [0]:Chance, [1]:durchschnittliche TaP*
     */
    public static double[] calculateChance(int eig1, int eig2, int eig3, int taw, int mod) {
        int passed = 0; //Erfolgreiche Proben
        int sumTaP = 0; //Gesamt TaP*

        final int tap = taw - mod; //Talentwert inklusive Modifikator

        for (int w1 = 1; w1 <= 20; w1++) {
            int erg1 = tap < 0 ? w1 - tap : w1; //Erhöhe das Würfelergebnis bei negativem Talentwert (doppelte Negierung)
            int tap1 = tap; //bearbeitbarer TaW Zwischenstand

            //Gleicht Differenz zwischen Würfelergebnis und Eigenschaft mit TaW aus und verringert TaP dementsprechend
            if (erg1 > eig1 && tap1 > 0) {
                if (erg1 - tap1 <= eig1) {
                    if (erg1 - eig1 < tap1) {
                        int unmodifiedValue = erg1;
                        erg1 -= erg1 - eig1;
                        tap1 -= unmodifiedValue - eig1;
                    } else {
                        erg1 -= tap1;
                        tap1 = 0;
                    }
                }
            }

            for (int w2 = 1; w2 <= 20; w2++) {
                int erg2 = tap < 0 ? w2 - tap : w2; //Erhöhe das Würfelergebnis bei negativem Talentwert (doppelte Negierung)
                int tap2 = tap1; //bearbeitbarer TaW Zwischenstand

                //Gleicht Differenz zwischen Würfelergebnis und Eigenschaft mit TaP aus
                if (erg2 > eig2 && tap2 > 0) {
                    if (erg2 - tap2 <= eig2) {
                        if (erg2 - eig2 < tap2) {
                            int unmodifiedValue = erg2;
                            erg2 -= erg2 - eig2;
                            tap2 -= unmodifiedValue - eig2;
                        } else {
                            erg2 -= tap2;
                            tap2 = 0;
                        }
                    }
                }

                for (int w3 = 1; w3 <= 20; w3++) {
                    int erg3 = tap < 0 ? w3 - tap : w3; //Erhöhe das Würfelergebnis bei negativem Talentwert (doppelte Negierung)
                    int tap3 = tap2; //bearbeitbarer TaW Zwischenstand

                    //Gleicht Differenz zwischen Würfelergebnis und Eigenschaft mit TaP aus
                    if (erg3 > eig3 && tap3 > 0) {
                        if (erg3 - tap3 <= eig3) {
                            if (erg3 - eig3 < tap3) {
                                int unmodifiedValue = erg3;
                                erg3 -= erg3 - eig3;
                                tap3 -= unmodifiedValue - eig3;
                            } else {
                                erg3 -= tap3;
                                tap3 = 0;
                            }
                        }
                    }

                    //Auswertung
                    if ((w1 == 1 && w2 == 1) || (w1 == 1 && w3 == 1) || (w2 == 1 && w3 == 1)) { //Doppel 1 = Erfolg
                        passed++;
                        sumTaP += taw;
                        continue;
                    }
                    if ((w1 == 20 && w2 == 20) || (w1 == 20 && w3 == 20) || (w2 == 20 && w3 == 20)) { //Doppel 20 = Misserfolg
                        continue;
                    }
                    if (eig1 >= erg1 && eig2 >= erg2 && eig3 >= erg3) { //normale Probe
                        passed++;
                        sumTaP += Math.min(tap3, taw); //Falls TaP*>TaW addiere TaW Punkte
                    }

                }
            }
        }
        double chance = passed / 8000.0;
        double avrgTaP = sumTaP * 1.0 / passed; //durchschnittliche TaP* werden nur aus den erfolgreichen Proben berechnet
        return new double[]{chance, avrgTaP};
    }
}
