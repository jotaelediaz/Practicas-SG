package SistemaSolar;

import GUI.Ventana;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.Canvas3D;

/**
 *
 * @author JotaEle & Maximetinu
 */
public class SistemaSolar {
    
    public static void  main(String[] args) {
        
        // Creamos los dos lienzos de la clase Canvas3D cogiendo la configuracion gráfica del sistema
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
	Canvas3D canvas2 = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        
        // Elegimos el tamaño deseado
        canvas.setSize(700,500);
	canvas2.setSize(300,200);
        
        //Creamos nuestra escena y la asociamos a los canvas creados
        Escena miEscena = new Escena(canvas, canvas2);
        
        // Se crea la GUI de las dos ventanas. Asociamos a la primera ventana el canvas principal y a la segunda, el canvas2.
        Ventana ventana = new Ventana(canvas);
        Ventana ventana2 = new Ventana (canvas2);
        
        //Movemos un poco la segunda ventana para que no se superpongan
        ventana2.setLocation(705,0);
        
        // Se muestran las ventanas de la aplicación
        ventana.showWindow();
        ventana2.showWindow();
        
    }
}
