package SistemaSolar;

import GUI.VentanaControl;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

/**
 *
 * @author JotaEle & Maximetinu
 */
public class Escena {

    private final Fondo miFondo;
    private final LuzAmbiental luzEscena;
    private final Estrella sol;
    private final Nave nave;

    private final float unidadAstronomica;
    private final long vRotTierra, vTrasTierra;
    private Camara camaraPerspectiva, camaraLuna, camaraNave;
    Canvas3D canvas1, canvas2;
    
    
    // Constructor que crea nuestra escena del Sistema Solar (Con todos sus planetas y satélites) y la asigna a un Canvas3D. También crea la vista secundaria y la asigna al canvas2
    Escena(Canvas3D canvas, Canvas3D canvasSecundario) {
        
        //Creamos el BG raíz, del que cuelgan los demás.
        BranchGroup raiz = new BranchGroup();
        
        //Creamos el fondo del sistema solar y lo añadimos al BG raíz
        this.miFondo = new Fondo("img/milkyway.png");
        raiz.addChild(miFondo);
        
        // Creamos la luz ambiental del fondo y la añadimos al BG raíz
        this.luzEscena = new LuzAmbiental();
        raiz.addChild(luzEscena);
	
        //Definimos los parámetros
	this.unidadAstronomica = 3f; // NOTA CIENTÍFICA: 1 ua = Distancia Tierra-Sol  :D
	this.vRotTierra = 20000;
	this.vTrasTierra = 40000;
        
        canvas1 = canvas;
	canvas2 = canvasSecundario;	
        
        // --- SOL --- //
        
        //El sol colgará de la raíz, todos los demás planetas colgarán del Sol (Serán "satélites" de él)
        this.sol = new Estrella("img/sol.jpg", (unidadAstronomica*9.3e-3f)*25, 7.25f, vRotTierra*3);
        raiz.addChild(sol); 
       
	
        // --- MERCURIO --- //
        
        CuerpoCeleste mercurio = new CuerpoCeleste("img/mercury.png", (unidadAstronomica*1.63e-5f)*1e3f, unidadAstronomica*0.387f, 0, vRotTierra, (long)(vTrasTierra / 2.2f));
        sol.addSatelite(mercurio);

        
        // --- VENUS --- //
        
        CuerpoCeleste venus = new CuerpoCeleste("img/venus.png", (unidadAstronomica*4.05e-5f)*1e3f, unidadAstronomica*0.723f, 177, vRotTierra, (long) (vTrasTierra / 1.6f));
        sol.addSatelite(venus);
        
        
        // --- TIERRA --- //
        
        CuerpoCeleste tierra = new CuerpoCeleste("img/earthmap.png", (unidadAstronomica*4.05e-5f)*1e3f, unidadAstronomica, 23.4f, vRotTierra, vTrasTierra);
        tierra.addNubes("img/nubes.png");
        sol.addSatelite(tierra);
        
            CuerpoCeleste luna = new CuerpoCeleste("img/moon.png", (unidadAstronomica*1.16e-5f)*1e3f, (unidadAstronomica*2.57e-3f)*35f, 1.54f, vRotTierra, vTrasTierra);
            tierra.addSatelite(luna);                
        
        // --- MARTE --- //
        
        CuerpoCeleste marte = new CuerpoCeleste("img/mars.png", (unidadAstronomica*2.27e-5f)*1e3f, unidadAstronomica*1.52f, 25.2f, vRotTierra, (long) (vTrasTierra / 0.6f));
        sol.addSatelite(marte);
        
            CuerpoCeleste fobos = new CuerpoCeleste("img/phobos.png", (unidadAstronomica*1.34e-7f)*1e5f, (unidadAstronomica*6.27e-5f)*2e3f, 0, vRotTierra, vTrasTierra);
            marte.addSatelite(fobos);
            
            CuerpoCeleste deimos = new CuerpoCeleste("img/deimos.png", (unidadAstronomica*8.42e-8f)*1e5f, (unidadAstronomica*1.57e-4f)*1.5e3f, 0, vRotTierra, vTrasTierra);
            marte.addSatelite(deimos);
            
            
        // --- JÚPITER --- //

        CuerpoCeleste jupiter = new CuerpoCeleste("img/jupiter.png", (unidadAstronomica*4.78e-4f)*7e2f, (unidadAstronomica*5.2f)/2, 3.12f, vRotTierra, (long) (vTrasTierra / 0.4f));
        sol.addSatelite(jupiter);
            
            CuerpoCeleste io = new CuerpoCeleste("img/io.png", (unidadAstronomica*1.22e-5f)*1e3f, (unidadAstronomica*2.82e-3f)*110, 0f, vRotTierra, (long) (vTrasTierra * 0.4f));
            jupiter.addSatelite(io);
            
            CuerpoCeleste europa = new CuerpoCeleste("img/europa.png", (unidadAstronomica*1.04e-5f)*1e3f, (unidadAstronomica*4.48e-3f)*90, 0.1f, vRotTierra, vTrasTierra);
            jupiter.addSatelite(europa);
            
            CuerpoCeleste ganimedes = new CuerpoCeleste("img/ganymede.png", (unidadAstronomica*1.76e-5f)*1e3f, (unidadAstronomica*7.15e-3f)*70, 0.165f, vRotTierra, vTrasTierra);
            jupiter.addSatelite(ganimedes);
            
            CuerpoCeleste calisto = new CuerpoCeleste("img/callisto.png", (unidadAstronomica*1.61e-5f)*1e3f, (unidadAstronomica*1.26e-2f)*50, 0f, vRotTierra, vTrasTierra);
            jupiter.addSatelite(calisto);
        
   
        // --- SATURNO --- //

        CuerpoCeleste saturno = new CuerpoCeleste("img/saturn.png", (unidadAstronomica*4.03e-4f)*5e2f, (unidadAstronomica*9.54f)/2.5f, 26.7f, vRotTierra, (long) (vTrasTierra / 0.3f));
        sol.addSatelite(saturno);
        saturno.addAnillo("img/anillo.png");
        
            CuerpoCeleste titan = new CuerpoCeleste("img/titan.png", (unidadAstronomica*1.72e-5f)*1e3f, (unidadAstronomica*8.17e-3f)*65, 0f, vRotTierra, (long) (vTrasTierra * 0.4f));
            saturno.addSatelite(titan);

            
        // --- URANO --- //
        
        CuerpoCeleste urano = new CuerpoCeleste("img/uranus.png", (unidadAstronomica*1.71e-4f)*5e2f, (unidadAstronomica*19.2f)/3.5f, 97.8f, vRotTierra, (long) (vTrasTierra / 0.2f));
        sol.addSatelite(urano);
        
            CuerpoCeleste titania = new CuerpoCeleste("img/titania.png", (unidadAstronomica*5.27e-6f)*1e3f, (unidadAstronomica*2.91e-3f)*60, 0f, vRotTierra, (long) (vTrasTierra * 0.4f));
            urano.addSatelite(titania);
            
            CuerpoCeleste oberon = new CuerpoCeleste("img/oberon.png", (unidadAstronomica*5.09e-6f)*1e3f, (unidadAstronomica*3.9e-3f)*60, 0f, vRotTierra, vTrasTierra);  
            urano.addSatelite(oberon);

            
        // --- NEPTUNO --- //
        
        CuerpoCeleste neptuno = new CuerpoCeleste("img/neptune.png", (unidadAstronomica*1.66e-4f)*5e2f, (unidadAstronomica*30.1f)/4, 28.3f, vRotTierra, (long) (vTrasTierra / 0.1f));
        sol.addSatelite(neptuno);
        
            CuerpoCeleste triton = new CuerpoCeleste("img/triton.png", (unidadAstronomica*9.05e-6f)*1e3f, (unidadAstronomica*2.37e-3f)*60, 0f, vRotTierra, (long) (vTrasTierra * 0.2f));
            neptuno.addSatelite(triton);

        
            
        //  ------------------- NAVE ------------------ //
        
        Point3f[] posiciones_nave = new Point3f[]{
            new Point3f(10f,1f,-10f), 
            new Point3f(10f,-3f,-5f),
            new Point3f(11f,2f,0f), 
            new Point3f(10f,4f,3f),
            new Point3f(9f,2f,5f), 
            new Point3f(6f,-1f,2f),
            new Point3f(8f,0f,-4f), 
            new Point3f(10f,1f,-10f)
	};
	this.nave = new Nave("models/E-TIE-I/E-TIE-I.obj", 20000, posiciones_nave);
	raiz.addChild(this.nave);

        
        // PICK ROTACIÓN
        
        PickRotar controladorRotacion = new PickRotar(canvas, raiz);
	controladorRotacion.setSchedulingBounds(new BoundingSphere (new Point3d (0.0, 0.0, 0.0), 1000000.0));
	raiz.addChild(controladorRotacion);
	
        
        
        // Creamos el SimpleUniverse y un Locale, asignamos el Locale al SUniverse
        
	SimpleUniverse SUniverse = new SimpleUniverse();
	SUniverse.getViewer().setVisible(false);
	SUniverse.getCanvas().setVisible(false);
	Locale locale = new Locale(SUniverse);


        // ----- CÁMARAS -----
        
        
        // -- Cam Locale Perspectiva --
	camaraPerspectiva = new Camara(canvas, new Point3d (30,30,30), new Point3d (0,0,0), new Vector3d (0,1,0)); // Creamos la cámara en perspectiva del Sistema Solar. Colocamos la cámara algo alejada para tener una mejor visión
        locale.addBranchGraph(camaraPerspectiva); // Colgamos la cámara del Locale para que enfoque al Sistema Solar en global
        
        // -- Cam Nave -- Activada por VentanaControl
        camaraNave = new Camara(canvasSecundario, new Point3d (10,10,10), new Point3d (0,0,0), new Vector3d (0,1,0)); // Creamos una cámara para la Nave (Irá en el canvas secundario). Colocamos la cámara algo alejada para tener una mejor visión
        nave.addCamara(camaraNave); // La colgamos de la Nave.
               
        // -- Cam Luna -- 
        camaraLuna = new Camara(canvasSecundario, new Point3d (1,0,0), new Point3d (0,0,0), new Vector3d (0,1,0)); // Creamos una cámara para la Luna (Irá en el canvas secundario). Colocamos la cámara algo alejada para tener una mejor visión
        luna.addCamara(camaraLuna); // La colgamos de la Luna.
        
        VentanaControl ventControl = new VentanaControl(this);
        ventControl.setLocation(705,0);
        ventControl.showWindow();
        
		
	//Compilamos la raiz (OJO: antes de que esté viva) para optimizarla y la colgamos del universo creado.
        raiz.compile();			
        locale.addBranchGraph(raiz);
       
		
    }
    
    public void ActivarCamLuna() {
        camaraLuna.activarCamara();
    }
    
        
    public void ActivarCamNave() {
        camaraNave.activarCamara();
    }
    
    public void closeApp (int code) {
	System.exit (code);
    }
    
}
