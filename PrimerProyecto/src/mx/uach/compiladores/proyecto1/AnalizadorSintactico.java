package mx.uach.compiladores.proyecto1;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.util.StringTokenizer;

/**
 * Clase principal de analizador sintactico predicitivo
 *
 * <ul>
 * <li>Prog -> ConjProds;</li>
 * <li>ConjProds -> ConjProds | Prod</li>
 * <li>Prod -> Variable DEF expr;</li>
 * <li>Expr -> Expr ALT Term |Term</li>
 * <li>Term -> Term & Factor |Factor</li>
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
    private static final int TERMINAL=800;
    //Operadores
    private static final int CONCATENACION = '&';
    private static final int ALTERNACION = '|';
    private static final int CERR_IZQ = '{';
    private static final int CERR_DER = '}';
    private static final int OPC_IZQ = '[';
    private static final int OPC_DER = ']';
    private static final int PAR_IZQ='(';
    private static final int PAR_DER=')';
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
        
        //this.currentToken = lex();
        prod();
        if (!(this.currentToken.getToken() == FIN_ARCH)) {
            System.out.println(String.format("\nResultado: %s "
                    + "El programa se corrio correctamente", this.salida));
        
    }
    }
/**
 * revisa si prog es un conjunto de productos.
 * @return 
 */       

    public boolean prog(){
        return conjProds();
    }
/**
 * para evitar la recursividad a la izquierda se separo en dos subconjuntos del 
 * conjunto principal.
 * @return 
 */
    
    private boolean conjProds() {
        char token;
        if(prod()){
            return conjProds2();
        }else{
            return false;
        }
    }
/**
 * revisa que priductos se a variable, definicion y expresion asi como su secuencia y que termine en ;.
 * @return 
 */
    private boolean prod() {
        int token;
        token = currentToken.siguiente();
        if(token == VARIABLE){
            token = currentToken.siguiente();
            if(token==DEFINICION){
                if(expr()){
                    this.salida = String.format("%s%s", this.salida, (char) DEFINICION);
                    token=currentToken.siguiente();
                    return token==FIN_SENT;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean conjProds2() {
        int token = currentToken.siguiente();
        if(token == 'f'){
            return true;
        }else{
            currentToken.atras();
            return conjProds();
        }
    }

    private boolean expr() {
        return (term() && expr2());
    }

    private boolean term() {
        return (factor()&&term2());
    }

    private boolean expr2() {
        int token;
        token = currentToken.siguiente();
        if(token == ALTERNACION){
            if(expr()){
                this.salida = String.format("%s%s", this.salida, (char) ALTERNACION);
                return true;
            }else{
                return false;
            }
        }else{
            currentToken.atras();
            return true;
        }
    }

    private boolean term2() {
        int token = currentToken.siguiente();
        if(token==CONCATENACION){
            if(term()){
                this.salida = String.format("%s%s", this.salida, (char) CONCATENACION);
                return true;
            }else{
                return false;
            }
        }else{
            currentToken.atras();
            return true;
        }
    }

    private boolean factor() {
        int token = currentToken.siguiente();
        if(token==CERR_IZQ){
            if(expr()){
                token = currentToken.siguiente();
                return token == CERR_DER;
            }else{
                return false;
            }
        }else{
            if(token==OPC_IZQ){
                if(expr()){
                    token = currentToken.siguiente();
                    return token == OPC_DER;
                }else{
                    return false;
                }                
            }else{
                currentToken.atras();
                return prim();
            }
        }
    }
/**
 * revisa que primario sea expresion entre parentesis o variable o terminal.
 * @return 
 */
    private boolean prim() {
        int token=currentToken.siguiente();
        if(token==PAR_IZQ){
            if(expr()){
                token = currentToken.siguiente();
                return token==PAR_DER;
            }else{
                return false;
            }
        }else{
            if(token==VARIABLE || token==TERMINAL){
                System.out.print(token);
                return true;
            }else{
                return false;
            }
        }
    }
    
    public boolean iniciarAnalisis(String exp) {
                AnalizadorSintactico analizador = new AnalizadorSintactico();
        if (exp == "") {
            throw new Error("No hay tokens");
        } else {
            this.getTokenizer(exp);
            while (analizador.getTokenizer(exp).hasMoreTokens()) {
         Token t = analizador.lex("");
         System.out.println(t);
         }
        }
        return true;
    }
    
    private StringTokenizer getTokenizer(String codigoFuente) {
        if (this.tokenaizer == null) {
            String alfabeto = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s",
                    (char) FIN_SENT,
                    (char) FIN_ARCH,
                    (char) CONCATENACION,
                    (char) ALTERNACION,
                    (char) CERR_IZQ,
                    (char) CERR_DER,
                    (char) PAR_IZQ,
                    (char) PAR_DER,
                    (char) OPC_IZQ,
                    (char) OPC_DER,
                    (char) SUMA,
                    (char) RESTA,
                    (char) ESP);
            this.tokenaizer = new StringTokenizer(codigoFuente.trim(), alfabeto, true);

        }
        return this.tokenaizer;
    }

    private Token lex(String codigofuente) {
        Token token = null;
        String currentToken = this.getTokenizer(codigofuente).nextToken();
        if (esVariable(currentToken)) {
            token = new Token(this.renglon, VARIABLE, currentToken);
        } else if (esDefinicion(currentToken)) {
            token = new Token(this.renglon, DEFINICION, currentToken);
        } else if(esTerminal(currentToken)){
            token = new Token(this.renglon, TERMINAL,currentToken);
        }else{
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
                case PAR_DER:
                    token = new Token(this.renglon, PAR_DER,
                            String.format("%s", (char) simpleToken));
                    break;
                case PAR_IZQ:
                    token = new Token(this.renglon, PAR_IZQ,
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

    private static Boolean esTerminal(String textoRevisar) {
        Boolean esterminal = true;
        String caracter=null;
        
        for (int i = 0; i < textoRevisar.length(); i++) {
            esterminal = esterminal && (textoRevisar.charAt(i)==39
                         || textoRevisar.charAt(i)=='+'
                         || textoRevisar.charAt(i)=='-'
                         || textoRevisar.charAt(i)==39);
                    
            }

        

        return esterminal;
    }
    public static void main(String[] args) {
        AnalizadorSintactico analizador = new AnalizadorSintactico();
        //analizador.gettokenizer("<v> ::=|<v>;<v> ::='-'|<v>&'+';");
        //analizador.paser();
        //System.out.println(analizador.prog());
         
         while (analizador.getTokenizer("<Entero> ::={{['+'|'-']&<variable>&(['+'|'-']&{<variable2>})}&{['+'|'-']&<variable3>}}").hasMoreTokens()) {
         Token t = analizador.lex("");
         System.out.println(t);
         }
    }
}
