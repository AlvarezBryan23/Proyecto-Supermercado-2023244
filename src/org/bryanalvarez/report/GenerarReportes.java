package org.bryanalvarez.report;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.bryanalvarez.DB.Conection;

/**
 *
 * @author USUARIO
 */
public class GenerarReportes {
    public static void mostrarReportes(String nombreReporte, String titulo, Map parametro){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
            try{
                JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);
                JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametro, Conection.getInstance().getConexion());
                JasperViewer visor = new JasperViewer(reporteImpreso, false);
                visor.setTitle(titulo);
                visor.setVisible(true);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
}

/*
Interface Map
        HashMap es uno de los objetos que implementa un conjunto de key-value.
        Tiene un constructor sin parametros new HasMap() y su finalidad suele referirse para
        agrupar información en un unico objeto.

        Tiene cierta similitud con la coleccion de objetos ArrayList pero con la diferencia que estos 
        no tienen un orden

        Has hace referencia a una tecnica de organizacion de archivos el hasing (abierto-cerrado) en la que 
        se almacena el registro de una direccion que es generada por una funcion se aplica a la llave del registro 
        dentro de memoria fisica.

        En Java HasMap posee un espacio de memoria y cuando se aguarda un objeto en dicho espacio 
        se determina su direccion, aplicando un funcion a la llave que le indiquemos.
        Cada objeto se identifica algun identificador apropiado
*/