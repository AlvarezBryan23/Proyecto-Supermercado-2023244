package org.bryanalvarez.system;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bryanalvarez.controller.MenuAyudaController;
import org.bryanalvarez.controller.MenuCargoEmpleadoController;
import org.bryanalvarez.controller.MenuClientesController;
import org.bryanalvarez.controller.MenuComprasController;
import org.bryanalvarez.controller.MenuEmpleadosController;
import org.bryanalvarez.controller.MenuPrincipalController;
import org.bryanalvarez.controller.MenuProductosController;
import org.bryanalvarez.controller.MenuProgramadorController;
import org.bryanalvarez.controller.MenuProveedoresController;
import org.bryanalvarez.controller.MenuTipoProductoController;

/**
 *Documentacion
 * Nombre: Bryan Alvarez
 * Fecha de Creacion: 11/04
 * Fecha de Modificaciones:
 * @author informatica
 */

public class Main extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String  URLVIEW = "/org/bryanalvarez/view/";
      
    /*FXMLLoader, Parent, el separador es / */
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
            this.escenarioPrincipal = escenarioPrincipal;
            this.escenarioPrincipal.setTitle("Galactic Supermarket");   
            escenarioPrincipal.getIcons().add(new Image(Main.class.getResourceAsStream("/org/bryanalvarez/Images/Designer.png")));
            MenuPrincipalView();
            
           //Parent root  = FXMLLoader.load(getClass().getResource("/org/bryanalvarez/view/MenuPrincipalView.fxml"));    
          //Scene escena = new Scene(root);
         //escenarioPrincipal.setScene(escena);
            escenarioPrincipal.show();
    }
  
    public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws Exception {
     Initializable resultado;
     FXMLLoader loader = new FXMLLoader();
     
     InputStream file = Main.class.getResourceAsStream(URLVIEW + fxmlName);
     loader.setBuilderFactory(new JavaFXBuilderFactory());
     loader.setLocation(Main.class.getResource( URLVIEW + fxmlName));
     
         escena = new Scene((AnchorPane)loader.load(file), width, heigth);
     escenarioPrincipal.setScene(escena);
     escenarioPrincipal.sizeToScene();
     resultado = (Initializable)loader.getController();
            
    return resultado;        
    }   
    
    public void MenuPrincipalView(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 467, 467);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void menuClienteView(){
        try{
            MenuClientesController menuClientes = (MenuClientesController)cambiarEscena("MenuPrincipalClientes.fxml", 946, 544);
            menuClientes.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void MenuProgramadorView(){
           try{
            MenuProgramadorController menuProgramador = (MenuProgramadorController)cambiarEscena("MenuProgramador.fxml", 711, 398);
            menuProgramador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void MenuProveedoresView(){
        try{
             MenuProveedoresController menuProveedores = (MenuProveedoresController)cambiarEscena("MenuProveedores.fxml", 969, 544);
            menuProveedores.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        public void MenuEmpleadosView(){
        try{
             MenuEmpleadosController menuEmpleados = (MenuEmpleadosController)cambiarEscena("MenuEmpleados.fxml", 981, 551);
             menuEmpleados.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        public void MenuAyudaView(){
         try{
             MenuAyudaController menuAyuda = (MenuAyudaController)cambiarEscena("MenuAyuda.fxml", 752, 424);
             menuAyuda.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
         }
     }
         public void MenuComprasView(){
         try{
             MenuComprasController menuCompra = (MenuComprasController)cambiarEscena("MenuCompras.fxml", 900, 500);
             menuCompra.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
   }
       public void MenuCargoEmpleadoView(){
         try{
             MenuCargoEmpleadoController menuCargo = (MenuCargoEmpleadoController)cambiarEscena("MenuCargoEmpleado.fxml", 889, 512);
              menuCargo.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
   }      
         public void MenuTipoProductoView(){
         try{
             MenuTipoProductoController menuTipo = (MenuTipoProductoController)cambiarEscena("MenuTipoProducto.fxml", 900, 505);
              menuTipo.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
   }      
         public void MenuProductosView(){
         try{
             MenuProductosController menuPro = (MenuProductosController)cambiarEscena("MenuProductos.fxml", 999, 563);
              menuPro.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
      }
}