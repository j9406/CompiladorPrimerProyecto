/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.uach.test.compiladores.proyecto1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import mx.uach.compiladores.proyecto1.AnalizadorSintactico;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ixxakk
 */
public class AnalizadorSintacticoTest {
    
    private ByteArrayOutputStream salida;
    private InputStream entrada;
    
    
    
    @Before
    public void setUp(){
        this.entrada= System.in;
        this.salida= new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida));
    }
    
    @After
    public void treaDown(){
        System.setIn(entrada);
        System.setOut(null);
    }

     @Test
   public void verVariable(){
       String exp="<entero>;";
       this.setData(exp);
       AnalizadorSintactico analizador = new 
            AnalizadorSintactico();
       assertEquals(true, analizador.iniciarAnalisis(exp));
   }
   
   @Test(expected = Error.class)
   public void verVariableFail(){
       String exp="<entero;";
       this.setData(exp);
       AnalizadorSintactico analizador = new
           AnalizadorSintactico();
       analizador.iniciarAnalisis(exp);
   }
   
   @Test
   public void definicionCorrecta(){
       String exp="<entero> ::=";
       this.setData(exp);
       AnalizadorSintactico analizador = new 
            AnalizadorSintactico();
       analizador.iniciarAnalisis(exp);
       assertEquals(true, analizador.iniciarAnalisis(exp));

   }
   
   @Test(expected = Error.class)
   public void definicionCorrectaFail(){
       String exp="<entero> ::";
       this.setData(exp);
       AnalizadorSintactico analizador= new AnalizadorSintactico();
       analizador.iniciarAnalisis(exp);
   }
   
   @Test
   public void variable(){
       String exp="<entero> ()";
       this.setData(exp);
       AnalizadorSintactico analizador = new 
            AnalizadorSintactico();
       assertEquals(true, analizador.iniciarAnalisis(exp));
   }
   
   @Test(expected = Error.class)
   public void variableFaile(){
       String exp="<entero> /";
       this.setData(exp);
       AnalizadorSintactico analizador = new
           AnalizadorSintactico();
       analizador.iniciarAnalisis(exp);
   
}
   @Test
   public void variableIniciandoConNumero(){
       String exp="<entero90>";
       this.setData(exp);
       AnalizadorSintactico analizador = new 
            AnalizadorSintactico();
       assertEquals(true, analizador.iniciarAnalisis(exp));
   }
   
   @Test(expected = Error.class)
   public void variableIniciandoConNumeroFail(){
       String exp="<90entero> ";
       this.setData(exp);
       AnalizadorSintactico analizador = new
           AnalizadorSintactico();
       analizador.iniciarAnalisis(exp);
   
}
   private void setData(String exp) {
        System.setIn(new ByteArrayInputStream(
                String.format("%s", exp).getBytes()));
    }
}