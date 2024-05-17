package org.bryanalvarez.controller;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.bryanalvarez.DB.Conection;
import org.bryanalvarez.bean.TipoProducto;
import org.bryanalvarez.system.Main;

/**
 *
 * @author Usuario
 */
public class MenuTipoProductoController  implements Initializable{
private Main escenarioPrincipal;
private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
private operaciones tipoDeOperaciones = operaciones.NINGUNO;
private ObservableList<TipoProducto> listarTipoProducto;

@FXML private TextField txtcodigoTP;
@FXML private TextField txtdescripcionTP;

@FXML private TableColumn colcodigoTipoProducto;
@FXML private TableColumn descripcion;

@FXML private Button btnAgregar;
@FXML private Button btnEliminar;
@FXML private Button btnEditar;
@FXML private Button btnReporte;
@FXML private Button btnRegresar;
@FXML private TableView tblTipoProducto;
                
@FXML private ImageView imgAgregarProducto;
@FXML private ImageView imgEliminarProducto;
@FXML private ImageView imgEditarProducto;
@FXML private ImageView imgReporteProducto;
    
   @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public void cargarDatos(){
        tblTipoProducto.setItems(getTipoProducto());
        colcodigoTipoProducto.setCellValueFactory(new PropertyValueFactory<TipoProducto, Integer>("codigoTipoProducto"));
        descripcion.setCellValueFactory(new PropertyValueFactory<TipoProducto, String>("descripcion"));
    }
    
    public void seleccionarDatos(){
        txtcodigoTP.setText(String.valueOf(((TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txtdescripcionTP.setText(String.valueOf(((TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem()).getDescripcion()));
    }
    
    public ObservableList<TipoProducto> getTipoProducto(){
         ArrayList<TipoProducto> lista = new ArrayList<>();
         try{
             PreparedStatement procedimientos = Conection.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
             ResultSet resultado = procedimientos.executeQuery();
             while(resultado.next()){
                 lista.add(new TipoProducto(resultado.getInt("codigoTipoProducto"),
                                                            resultado.getString("descripcion")
                 ));
             
             }
         }catch(Exception e){
             e.printStackTrace();
         }
         return listarTipoProducto = FXCollections.observableArrayList(lista);
    }
    
    public void agregar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
            activarControles();
            btnAgregar.setText("Guardar");
            btnEliminar.setText("Cancelar");
            btnEditar.setDisable(true);
            btnReporte.setDisable(true);
            imgAgregarProducto.setImage(new Image("org/bryanalvarez/Images/Guardar.png"));
            imgEliminarProducto.setImage(new Image("org/bryanalvarez/Images/Cancelar.png"));
            tipoDeOperaciones = operaciones.ACTUALIZAR;
            break;
            
            case ACTUALIZAR:
            guardar();
            desactivarControles();
            limpiarControles();
            btnAgregar.setText("Agregar");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
            imgAgregarProducto.setImage(new Image("org/bryanalvarez/Images/AgregarProducto.png"));
            imgEliminarProducto.setImage(new Image("org/bryanalvarez/Images/EliminarProducto.png"));
            tipoDeOperaciones = operaciones.NINGUNO;
            break;
        }
    }
    
    public void guardar(){
        TipoProducto registro = new TipoProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtcodigoTP.getText()));
        registro.setDescripcion(txtdescripcionTP.getText());
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarTipoProducto(?, ?)}");
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listarTipoProducto.add(registro);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
             desactivarControles();
              limpiarControles();
              btnAgregar.setText("Agregar");
              btnEliminar.setText("Eliminar");
              btnEditar.setDisable(false);
              btnReporte.setDisable(false);
              imgAgregarProducto.setImage(new Image("org/bryanalvarez/Images/AgregarProducto.png"));
              imgEliminarProducto.setImage(new Image("org/bryanalvarez/Images/EliminarProducto.png"));  
              tipoDeOperaciones = operaciones.NINGUNO;
              break;
            default: 
                if(tblTipoProducto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmacion para la eliminacion", "Eliminar el Tipo de Producto",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarTipoProducto(?)}");
                            procedimiento.setInt(1, ((TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listarTipoProducto.remove(tblTipoProducto.getSelectionModel().getSelectedItem());
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTipoProducto.getSelectionModel().getSelectedItem() != null){
                  activarControles();
                  btnEditar.setText("Actualizar");
                  btnReporte.setText("Cancelar");
                  btnAgregar.setDisable(true);
                  btnEliminar.setDisable(true);
                  imgEditarProducto.setImage(new Image("/org/bryanalvarez/Images/Editar.png"));
                  imgReporteProducto.setImage(new Image("/org/bryanalvarez/Images/Cancelar.png"));
                  txtcodigoTP.setEditable(false);
                  tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                break;
                
            case ACTUALIZAR:
                   desactivarControles();
                   Actualizar();
                   btnEditar.setText("Editar");
                   btnReporte.setText("Reporte");
                   btnAgregar.setDisable(false);
                   btnEliminar.setDisable(false);
                   imgEditarProducto.setImage(new Image("/org/bryanalvarez/Images/EditarProducto.png"));
                   imgReporteProducto.setImage(new Image("/org/bryanalvarez/Images/ReporteProducto.png")); 
                   limpiarControles();
                   tipoDeOperaciones = operaciones.NINGUNO;
                   cargarDatos();
                  break;
        }
    }
    
    public void Actualizar(){
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarTipoProducto(?, ?)}");
            TipoProducto registro = (TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtdescripcionTP.getText());
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditarProducto.setImage(new Image("/org/bryanalvarez/Images/EditarProducto.png"));
                imgReporteProducto.setImage(new Image("/org/bryanalvarez/Images/ReporteProducto.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void desactivarControles(){
        txtcodigoTP.setEditable(false);
        txtdescripcionTP.setEditable(false);
    }
    
    public void activarControles(){
        txtcodigoTP.setEditable(true);
        txtdescripcionTP.setEditable(true);
    }
    
     public void limpiarControles(){
        txtcodigoTP.clear();
        txtdescripcionTP.clear();
     }
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }    
  @FXML
  public void regresar (ActionEvent event){
        if (event.getSource() == btnRegresar){
        escenarioPrincipal.MenuPrincipalView();
        }
    } 
}