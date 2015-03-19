package mx.uach.compiladores.proyecto1;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
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
    private static final int PAR_DER = '(';
    private static final int PAR_IZQ = ')';
    private static final int OPC_IZQ = '[';
    private static final int OPC_DER = ']';
    private static final int SUMA = '+';
    private static final int RESTA = '-';
    private static final int ESP = ' ';

    //Variable
    private static final int VARIABLE = 600;

    private Integer renglon = 1;
    private StringTokenizer tokenaizer = null;
    private Token currentToken;
    private String salida;

    public void paser() {
        this.currentToken = lex();
        prod();
        if (!(this.currentToken.getToken() == FIN_ARCH)) {
            System.out.println(String.format("\nResultado: %s "
                    + "El programa se corrio correctamente", this.salida));
        }
    }

    public void prod() {
       
        if(this.currentToken.getToken()==DEFINICION)
        {
            this.currentToken = lex();
            term();
            this.currentToken = lex();
            this.salida = String.format("%s%s", this.salida,
                        this.currentToken.getLexema());
        }else if(this.currentToken.getToken()==VARIABLE)
        {
            this.currentToken = lex();
            term();
            this.currentToken = lex();
            this.salida = String.format("%s%s", this.salida,
                        this.currentToken.getLexema());   
        }else if(this.currentToken.getToken()==ESP)
        {
            this.currentToken = lex();
            term();
            this.currentToken = lex();
            this.salida = String.format("%s%s", this.salida,
                        this.currentToken.getLexema());   
        }
        
            

    }

    public void expr() {
        term();
            if (this.currentToken.getToken() == ALTERNACION) {
                this.currentToken = lex();
                term();
                this.currentToken = lex();
                this.salida = String.format("%s%s", this.salida,
                        (char) ALTERNACION);
            }

        
    }

    public void term() {

            if (this.currentToken.getToken() == CONCATENACION) {
                this.currentToken = lex();
                factor();
                this.currentToken = lex();
                this.salida = String.format("%s%s", this.salida,
                        (char) CONCATENACION);
            }
        
    }

    public void factor() {
        primario();
        
            if (this.currentToken.getToken() == OPC_DER) {
                this.currentToken = lex();
                expr();
                if (!(this.currentToken.getToken() == OPC_IZQ)) {
                    throw new Error(String.format("\nError de sintaxis se esperaba'%s'", (char) OPC_IZQ));
                }
            } else if (this.currentToken.getToken() == CERR_DER) {
                this.currentToken = lex();
                expr();
                if (!(this.currentToken.getToken() == CERR_IZQ)) {
                    throw new Error(String.format("\nError de sintaxis se esperaba'%s'", (char) CERR_IZQ));
                }
            }
        

    }

    public void primario() {
        
         term();
        if (this.currentToken.getToken() == VARIABLE) {
            this.currentToken = lex();
            this.salida = String.format("%s%s", this.salida,
                    this.currentToken.getLexema());

        } else if (this.currentToken.getToken() == PAR_DER) {
            this.currentToken = lex();
            expr();
            if (!(this.currentToken.getToken() == PAR_IZQ)) {
                throw new Error(String.format("\nError de sintaxis se esperaba'%s'", (char) PAR_IZQ));
            }
        }
    }

    private StringTokenizer gettokenizer(String Codigofuente) {
        if (this.tokenaizer == null) {
            String alfabeto = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s",
                    (char) FIN_SENT,
                    (char) FIN_ARCH,
                    (char) CONCATENACION,
                    (char) ALTERNACION,
                    (char) CERR_IZQ,
                    (char) CERR_DER,
                    (char) PAR_DER,
                    (char) PAR_IZQ,
                    (char) OPC_IZQ,
                    (char) OPC_DER,
                    (char) SUMA,
                    (char) RESTA,
                    (char) ESP);
            this.tokenaizer = new StringTokenizer(Codigofuente.trim(), alfabeto, true);

        }
        return this.tokenaizer;
    }

    private Token lex() {
        Token token = null;
        String currentToken = this.gettokenizer("").nextToken();
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
                case FIN_ARCH:
                    token = new Token(this.renglon, FIN_ARCH,
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
                case PAR_DER:
                    token = new Token(this.renglon, PAR_DER,
                            String.format("%s", (char) simpleToken));
                    break;
                case PAR_IZQ:
                    token = new Token(this.renglon, PAR_IZQ,
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
                case SUMA:
                    token = new Token(this.renglon, SUMA,
                            String.format("%s", (char) simpleToken));
                    break;
                case RESTA:
                    token = new Token(this.renglon, RESTA,
                            String.format("%s", (char) simpleToken));
                    break;
                case ESP:
                    token = new Token(this.renglon, ESP,
                            String.format("%s", (char) simpleToken));
                    break;
                default:
                    throw new Error("El caracter no esta dentro del alfabeto");
            }
        }
        return token;
    }

    /**
     * Con este metodo nos daremos cuenta si los tokens obtenido es una variable
     *
     * @param textoRevisar
     * @return
     */

    private static Boolean esVariable(String textoRevisar) {
        Boolean esnumero = true;
        for (int i = 0; i < textoRevisar.length(); i++) {
            
            esnumero = esnumero && textoRevisar.charAt(0) == '<';
            if (i == 1) {
                esnumero = esnumero && (Character.isAlphabetic(
                        textoRevisar.charAt(1)));
            } else if (i >= 2 && i < textoRevisar.length() - 2 && esnumero == true) {
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

    /**
     * Este metodo verifica que los tokens que hayamos tenido sea definicion
     *
     * @param textoRevisar
     * @return
     */

    private static Boolean esDefinicion(String textoRevisar) {
        Boolean esDefinicion = false;
        if (textoRevisar.equals("::=")) {
            esDefinicion = true;
        }
        return esDefinicion;
    }

    public static void main(String[] args) {
        AnalizadorSintactico analizador = new AnalizadorSintactico();
        analizador.gettokenizer("<Entero> ::={{[+|-]&<variable>&([+|-]&{<variable2>})}&{[+|-]&<variable3>}};");
        analizador.paser();
        /*
         while (analizador.gettokenizer("<Entero> ::={{[+|-]&<variable>&([+|-]&{<variable2>})}&{[+|-]&<variable3>}}").hasMoreTokens()) {
         Token t = analizador.lex("");
         System.out.println(t);
         }*/
    }
}
