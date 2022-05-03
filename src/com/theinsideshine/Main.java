package com.theinsideshine;

/*
https://foro.elhacker.net/java/algoritmo_en_java_solucionado-t184597.0.html

Los mensajes alemanes utilizan el juego de 29 carácteres siguientes,
 codificados a partir de 0(los tres últimos son el espacio, el punto y la coma).

A B C D E F G H I J K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z     .  ,
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28

La máquina enigma tiene  una  rueda, esta en la posicion A y se avanza 1,
pasara a B; si avanza 29 pasos, volvera a la posicion A.

Tras traducirlo al español, lo primero que hacen  los alemanes para codificar un mensaje
 es elegir una secuencia especial de 5 letras, que hara de semilla.
 Colocan esta secuencia al principio y el mensaje inmediatamente a continuacion.

EJEMPLO:

Supongase el mensaje MATAR A LOS HUMANOS

Para codificarlos, Enigma podia enteponder una semilla cualquiera, por ejemplo RUDER preparando el mensaje asi:

RUDERMATAR A LOS HUMANOS

Se multiplican y suman los codigos de RUDER (que son 17,20,3,4,17); por tanto tenemeros:
(17x20x3x4x17 + 17+20+3+4+17), y ese (69421 en este caso) es el numero de pasos
que avanzará la rueda de la M. Eso nos lleva a la H. Por tanto, el mensaje pasa a ser:

RUDERHATAR A LOS HUMANOS

La misma operacion se haria para la A, con los codigos de las letras UDERH.
 Y así sucesivamente. Eso nos llevaría al mensaje codificado:

RUDERHRML.EMG,AKYRMRNGNU

 */

import java.util.ArrayList;

public class Main {

    public static class Codex {
        private ArrayList table;

        int posMax = 29;

        Codex() {
            table = new ArrayList(posMax);
            makeTable();
        }

        void makeTable() {

            for (int i = 0; i < posMax - 3; i++)
                table.add((char) (65 + i));

            table.add((char) (32));
            table.add((char) (46));
            table.add((char) (44));
        }

        int calculatePosNew(int i, int posMov, int posOld, boolean isEncode) {

            int posNew = 0;

            if (isEncode) {
                /*
                   Se fija si al moverse posMov veces desde posOld  hacia la izquierda se sale de la table o no.
                 */

                if ((posMov + posOld) > (posMax - 1)) {
                    posNew = posMov - (posMax - posOld);
                } else {
                    posNew = posOld + posMov;
                }

            } else {
                 /*
                   Se fija si al moverse posMov veces desde posOld  hacia la derecha se sale de la table o no.
                 */

                if ((posOld - posMov) < 0) {
                    posNew = (posMax) - (posMov - posOld);
                } else {
                    posNew = posOld - posMov;
                }
            }

            return posNew;
        }

        int calculatePosMov(int i, StringBuilder data, boolean isEncode) {

            int posMov = 0;
            int mul = 1, add = 0;

            /*
               Calcula la cantidad de veces que va a mover.
             */

            if (isEncode) {

                for (int j = i - 5; j < i; j++) {
                    char c = data.charAt(j);
                    int pos = table.indexOf(c);
                    mul *= pos;
                    add += pos;
                }

            } else {

                for (int j = i - 5; j < i; j++) {
                    char c = data.charAt(j);
                    int pos = table.indexOf(c);
                    mul *= pos;
                    add += pos;
                }

            }
            posMov = add + mul;
            posMov %= 29;
            mul = 1;
            add = 0;

            return posMov;
        }



        public String muxCode(String data, boolean isEncode) {

            StringBuilder dataIn = new StringBuilder(data);
            StringBuilder dataOut = new StringBuilder("");
            int posMov = 0;
            int posNew = 0;
            int posOld = 0;

            if (isEncode) {

                 /*
                    Recorre dataIn de izquierda a derecha , la semilla termina en i = 4.
                */

                for (int i = 5; i < dataIn.length(); i++) {

                    posMov = calculatePosMov(i, dataIn, isEncode);
                    posOld = table.indexOf(dataIn.charAt(i));
                    posNew = calculatePosNew(i, posMov, posOld, isEncode);
                    dataOut.append(table.get(posNew));

                    /*
                     * El caracter codificado es parte de la siguiente codificacion, se inserta en el dataIn.
                     */

                    dataIn.setCharAt(i, (Character) table.get(posNew));
                }
                System.out.println("Cadena sin codificar: " + data);

                return dataOut.toString();

            }else {

                /*
                    Recorre dataIn de derecha a izquierda, la semilla termina en i = 4.
                 */

                for (int i = dataIn.length() - 1; i >= 5; i--) {

                    posMov = calculatePosMov(i, dataIn, isEncode);
                    posOld = table.indexOf(dataIn.charAt(i));
                    posNew = calculatePosNew(i, posMov, posOld, isEncode);
                    dataOut.append(table.get(posNew));

                }
                System.out.println("Cadena codificada: " + dataIn);

                /*
                  El sentido de la decodificacion construye el string de derecha a izquierda.
                 */

                return dataOut.reverse().toString();
            }


        }

    }


    static public void main(String[] args) {
        Codex eni = new Codex();
        String encodeData ="";
        String decodeData ="";

        encodeData = eni.muxCode("RUDERMATAR A LOS HUMANOS",true);
        System.out.println("Cadena  codificada: " + encodeData);
        System.out.println("Solucion enunciado:  HRML.EMG,AKYRMRNGNU");

        decodeData = eni.muxCode("RUDER"+encodeData,false);
        System.out.println("Cadena  decodificada: " + decodeData);
    }
}

