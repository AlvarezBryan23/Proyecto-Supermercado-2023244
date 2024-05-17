package org.bryanalvarez.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.bryanalvarez.system.Main;

/**
 *Herencia Multiple concepto, Interfazes. POO
 * 
 */
public class MenuPrincipalController implements Initializable {
    private Main escenarioPrincipal;

    @FXML MenuItem btnMenuClientes;
    @FXML MenuItem btnMenuProgramador;
    @FXML MenuItem btnMenuProveedores;
    @FXML MenuItem btnMenuEmpleados;
    @FXML MenuItem btnMenuAyuda;
    @FXML MenuItem btnCompras;
    @FXML MenuItem btnCargoEmpleado;
    @FXML MenuItem btnTipoProducto;
    @FXML MenuItem btnProductos;
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    @FXML
    public void clicClientes(ActionEvent event)throws IOException{
        if(event.getSource() == btnMenuClientes){
            escenarioPrincipal.menuClienteView();
        }
    }
     @FXML
    public void clicPro(ActionEvent event)throws IOException{
        if(event.getSource() == btnMenuProgramador){
            escenarioPrincipal.MenuProgramadorView();
        }
    }
    @FXML
    public void cliclProve(ActionEvent event) throws IOException{
        if(event.getSource() == btnMenuProveedores){
            escenarioPrincipal.MenuProveedoresView();
        }
    }
    @FXML
    public void clicEmple(ActionEvent event)throws IOException{
          if(event.getSource() == btnMenuEmpleados){
            escenarioPrincipal.MenuEmpleadosView();
         }
    }
    @FXML
    public void cliclAyu(ActionEvent event)throws IOException{
         if(event.getSource() == btnMenuAyuda){
            escenarioPrincipal.MenuAyudaView();
         }
    }
      @FXML
    public void cliclCom(ActionEvent event)throws IOException{
         if(event.getSource() == btnCompras){
            escenarioPrincipal.MenuComprasView();
         }
    }
      @FXML
    public void cliclCargo(ActionEvent event)throws IOException{
         if(event.getSource() == btnCargoEmpleado){
            escenarioPrincipal.MenuCargoEmpleadoView();
         }
    }
     @FXML
    public void cliclTipo(ActionEvent event)throws IOException{
         if(event.getSource() == btnTipoProducto){
            escenarioPrincipal.MenuTipoProductoView();
         }
    }
     @FXML
    public void cliclProduc(ActionEvent event)throws IOException{
         if(event.getSource() == btnProductos){
            escenarioPrincipal.MenuProductosView();
         }
    }
}