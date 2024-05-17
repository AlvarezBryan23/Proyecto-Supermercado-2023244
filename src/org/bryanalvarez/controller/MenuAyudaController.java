package org.bryanalvarez.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuAyudaController  implements  Initializable{
    private Main escenarioPrincipal;
    @FXML private Button btnRegresar;
    
       @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
        public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    public void regresar(ActionEvent event){
        if(event.getSource() == btnRegresar){
           escenarioPrincipal.MenuPrincipalView();
        }
    }
}