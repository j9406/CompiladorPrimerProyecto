package mx.uach.compiladores.proyecto1;

import java.util.StringTokenizer;

/**
 * Clase principal de analizador sintactico predicitivo
 *
 * <ul>
 * <li>Prog -> ConjProds</li>
 * <li>ConjProds -> ConjProds | Prod</li>
 * <li>Prod -> Variable DEF expr;</li>
 * <li>Expr -> Expr ALT Term |Term</li>
 * <li>Term -> Term & Factor</li>
 * <li>Factor -> {Expr}|[Expr]|Primario</li>
 * <li>Primario -> (Expr) |Variable|Terminal</li>
 * </ul>
 *
 * @author Javier Cruz Beltran
 * @since 11/03/2015
 * @version 1.0
 *
 */
public class AnalizadorSintactico {

    private static final int FIN_SENT = ';';
    private static final int FIN_ARCH = '.';
    private static final int DEFINICION = 700;

    //Operadores
    private static final int CONCATENACION = '&';
    private static final int ALTERNACION = '|';
    private static final int CERR_IZQ = '{';
    private static final int CERR_DER = '}';
    private static final int OPC_IZQ = '[';
    private static final int OPC_DER = ']';

    //Variable
    //private static final int INI_VAR='<';
    //private static final int FIN_VAR='>';
    private static final int VARIABLE = 600;

    private Integer renglon = 1;
    private StringTokenizer tokenaizer = null;
    private Token currentToken;

    private StringTokenizer gettokenizer(String Codigofuente) {
        if (this.tokenaizer == null) {
            String alfabeto = String.format("%s%s%s%s%s%s%s",
                    (char) FIN_SENT,
                    (char) CONCATENACION,
                    (char) ALTERNACION,
                    (char) CERR_IZQ,
                    (char) CERR_DER,
                    (char) OPC_IZQ,
                    (char) OPC_DER);
            this.tokenaizer = new StringTokenizer(Codigofuente.trim(), alfabeto, true);

        }
        return this.tokenaizer;
    }

    private Token lex(String codigfuente) {
        Token token = null;
        String currentToken = this.gettokenizer(codigfuente).nextToken();
        if (esVariable(currentToken)) {
            token = new Token(this.renglon, VARIABLE, currentToken);
        } else if (esDefinicion(currentToken)) {
            token = new Token(this.renglon, DEFINICION, currentToken);
        } else {
            int simpleToken = currentToken.charAt(0);
            switch (simpleToken) {
                case FIN_SENT:
                    token = new Token(this.renglon, FIN_SENT,
                            String.format("%s", (char) simpleToken));
                    break;
                case CONCATENACION:
                    token = new Token(this.renglon, CONCATENACION,
                            String.format("%s", (char) simpleToken));
                    break;
                case ALTERNACION:
                    token = new Token(this.renglon, ALTERNACION,
                            String.format("%s", (char) simpleToken));
                    break;
                case CERR_IZQ:
                    token = new Token(this.renglon, CERR_IZQ,
                            String.format("%s", (char) simpleToken));
                    break;
                case CERR_DER:
                    token = new Token(this.renglon, CERR_DER,
                            String.format("%s", (char) simpleToken));
                    break;
                case OPC_IZQ:
                    token = new Token(this.renglon, OPC_IZQ,
                            String.format("%s", (char) simpleToken));
                    break;
                case OPC_DER:
                    token = new Token(this.renglon, OPC_DER,
                            String.format("%s", (char) simpleToken));
                    break;
                default:
                    throw new Error("El caracter no esta dentro del alfabeto");
            }
        }
        return token;
    }

    private static Boolean esVariable(String textoRevisar) {
        Boolean esnumero = true;
        for (int i = 0; i < textoRevisar.length(); i++) {
            esnumero = esnumero && textoRevisar.charAt(0) == '<';
            if (i == 1) {
                System.out.println("valor de i= " + i);
                esnumero = esnumero && (Character.isAlphabetic(
                        textoRevisar.charAt(1)));
            } else if (i >= 2 && i < textoRevisar.length() - 2 && esnumero==true) {
                System.out.println("valor de i2= " + i);
                esnumero = esnumero && Character.isAlphabetic(
                        textoRevisar.charAt(i))
                        || Character.isDigit(textoRevisar.charAt(i));
            } else if (i == textoRevisar.length() - 1) {
                esnumero = esnumero && textoRevisar.charAt(
                        textoRevisar.length() - 1) == '>';
            }

        }

        return esnumero;
    }

    private static Boolean esDefinicion(String textoRevisar) {
        Boolean esDefinicion = true;
        for (int i = 0; i < 3; i++) {
            esDefinicion = esDefinicion && textoRevisar.charAt(0) == ':'
                    && textoRevisar.charAt(1) == ':'
                    && textoRevisar.charAt(2) == '=';
        }
        return esDefinicion;
    }

    public static void main(String[] args) {
        AnalizadorSintactico analizador = new AnalizadorSintactico();
        while (analizador.gettokenizer("&[<j9990>]&|::=").hasMoreTokens()) {
            Token t = analizador.lex("");
            System.out.println(t);
        }
    }
}
