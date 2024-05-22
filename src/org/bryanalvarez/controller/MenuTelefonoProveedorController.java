package org.bryanalvarez.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.bryanalvarez.bean.Proveedores;
import org.bryanalvarez.bean.TelefonoProveedor;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuTelefonoProveedorController  implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<TelefonoProveedor> listarTelefonoProveedor;
    private ObservableList<Proveedores> listarProveedores;
    
       @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}
