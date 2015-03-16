/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.uach.compiladores.proyecto1;

/**
 *
 * @author javiercruz
 */
public class Token {
    
    private Integer renglon;
    private int token;
    private String lexema;

    public Token(Integer renglon, int token, String lexema) {
        this.renglon = renglon;
        this.token = token;
        this.lexema = lexema;
    }

   
    public Integer getLinea() {
        return renglon;
    }

    
    public void setLinea(Integer linea) {
        this.renglon = linea;
    }

    
    public int getToken() {
        return token;
    }

    
    public void setToken(int token) {
        this.token = token;
    }

    
    public String getLexema() {
        return lexema;
    }

    
    public void setLexema(String lexema) {
        this.lexema = lexema;
    }
    
    @Override
    public String toString(){     
        return String.format("%s --- %s --- %s", 
                this.renglon, this.token, this.lexema);
    }
    
    
}
