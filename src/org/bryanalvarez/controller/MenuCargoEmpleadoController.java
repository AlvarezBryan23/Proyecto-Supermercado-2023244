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
import org.bryanalvarez.bean.CargoEmpleado;
import org.bryanalvarez.system.Main;

/**
 *
 * @author Usuario
 */
public class MenuCargoEmpleadoController implements Initializable{
 private Main escenarioPrincipal;
private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
private operaciones tipoDeOperaciones = operaciones.NINGUNO;
private ObservableList<CargoEmpleado> listarCargo;

@FXML private TextField txtcodigoCE;
@FXML private TextField txtnombreCE;
@FXML private TextField txtdescripcionCE;
@FXML private TableView tblCargo;

@FXML private TableColumn colcodigoCargo;
@FXML private TableColumn colnombreCargo;
@FXML private TableColumn coldescripcionCargo;   

@FXML private Button btnAgregar;
@FXML private Button btnEliminar;
@FXML private Button btnEditar;
@FXML private Button btnReporte;
@FXML private Button btnRegresar;
                
@FXML private ImageView imgAgregarCargo;
@FXML private ImageView imgEliminarCargo;
@FXML private ImageView imgEditarCargo;
@FXML private ImageView imgReporteCargo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }  
    
    public void cargarDatos(){
        tblCargo.setItems(getCargo());
        colcodigoCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, Integer>("codigoCargoEmpleado"));
        colnombreCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("nombreCargo"));
        coldescripcionCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("descripcionCargo"));
    }
    
    public void SeleccionarDatos(){
        txtcodigoCE.setText(String.valueOf(((CargoEmpleado)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtnombreCE.setText(String.valueOf(((CargoEmpleado)tblCargo.getSelectionModel().getSelectedItem()).getNombreCargo()));
        txtdescripcionCE.setText(String.valueOf(((CargoEmpleado)tblCargo.getSelectionModel().getSelectedItem()).getDescripcionCargo()));
    }
    
        public ObservableList<CargoEmpleado> getCargo(){
             ArrayList<CargoEmpleado> lista = new ArrayList<>();
             try{
                 PreparedStatement procedimientos = Conection.getInstance().getConexion().prepareCall("{call sp_ListarCargo()}");   
                 ResultSet resultado = procedimientos.executeQuery();
                 while(resultado.next()){
                     lista.add(new CargoEmpleado(resultado.getInt("codigoCargoEmpleado"),
                                                                      resultado.getString("nombreCargo"),
                                                                      resultado.getString("descripcionCargo")
                     ));
                 
                 }
             }catch(Exception e){
                 e.printStackTrace();
             }
             return listarCargo = FXCollections.observableArrayList(lista);
        }
        
        public void agregar(){
            switch(tipoDeOperaciones){
                case NINGUNO:
                  activarControles();
                  btnAgregar.setText("Guardar");
                  btnEliminar.setText("Cancelar");
                  btnEditar.setDisable(true);
                  btnReporte.setDisable(true);
                  imgAgregarCargo.setImage(new Image("org/bryanalvarez/Images/Guardar.png"));
                  imgEliminarCargo.setImage(new Image("org/bryanalvarez/Images/Cancelar.png"));   
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
                  imgAgregarCargo.setImage(new Image("org/bryanalvarez/Images/AgregarCargo.png"));
                  imgEliminarCargo.setImage(new Image("org/bryanalvarez/Images/EliminarCargo.png"));
                  tipoDeOperaciones = operaciones.NINGUNO;
                  break;
            }
        }
        
        public void guardar(){
            CargoEmpleado registro = new CargoEmpleado();
            registro.setCodigoCargoEmpleado(Integer.parseInt(txtcodigoCE.getText()));
            registro.setNombreCargo(txtnombreCE.getText());
            registro.setDescripcionCargo(txtdescripcionCE.getText());
            try{
                PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarCargo(?, ?, ?)}");
                procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
                procedimiento.setString(2, registro.getNombreCargo());
                procedimiento.setString(3, registro.getDescripcionCargo());
                procedimiento.execute();
                listarCargo.add(registro);
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
                  imgAgregarCargo.setImage(new Image("org/bryanalvarez/Images/AgregarCargo.png"));
                  imgEliminarCargo.setImage(new Image("org/bryanalvarez/Images/EliminarCargo.png"));
                  tipoDeOperaciones = operaciones.NINGUNO;
                  break;
                default:
                    if(tblCargo.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null, "Confirmacion para la eliminacion", "Eliminar el cargo",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_NO_OPTION){
                            try{
                                PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarCargo(?)}");
                                procedimiento.setInt(1, ((CargoEmpleado)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                                procedimiento.execute();
                                listarCargo.remove(tblCargo.getSelectionModel().getSelectedItem());
                                
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un dato");
            }
        }
        
        public void editar(){
            switch(tipoDeOperaciones){
                case NINGUNO:
                if(tblCargo.getSelectionModel().getSelectedItem() != null){
                  activarControles();
                  btnEditar.setText("Actualizar");
                  btnReporte.setText("Cancelar");
                  btnAgregar.setDisable(true);
                  btnEliminar.setDisable(true);
                  imgEditarCargo.setImage(new Image("/org/bryanalvarez/Images/Editar.png"));
                  imgReporteCargo.setImage(new Image("/org/bryanalvarez/Images/Cancelar.png"));
                 txtcodigoCE.setEditable(false);
                 tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un dato");
                break; 
                
                case ACTUALIZAR:
                  desactivarControles();
                  Actualizar();
                  btnEditar.setText("Editar");
                  btnReporte.setText("Reporte");
                  btnAgregar.setDisable(false);
                  btnEliminar.setDisable(false);
                  imgEditarCargo.setImage(new Image("/org/bryanalvarez/Images/EditarCargo.png"));
                  imgReporteCargo.setImage(new Image("/org/bryanalvarez/Images/ReporteCargo.png"));
                  limpiarControles();
                  tipoDeOperaciones = operaciones.NINGUNO;
                  cargarDatos();
                  break;
            }
        }
        
        public void Actualizar(){
            try{
                PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarCargo(?, ?, ?)}");
                CargoEmpleado registro = (CargoEmpleado)tblCargo.getSelectionModel().getSelectedItem();
                registro.setNombreCargo(txtnombreCE.getText());
                registro.setDescripcionCargo(txtdescripcionCE.getText());
                procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
                procedimiento.setString(2, registro.getNombreCargo());
                procedimiento.setString(3, registro.getDescripcionCargo());
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
                 imgEditarCargo.setImage(new Image("/org/bryanalvarez/Images/EditarCargo.png"));
                 imgReporteCargo.setImage(new Image("/org/bryanalvarez/Images/ReporteCargo.png"));
                 tipoDeOperaciones = operaciones.NINGUNO;
                 break;
            }
        }
        
    public void desactivarControles(){
        txtcodigoCE.setEditable(false);
        txtnombreCE.setEditable(false);
        txtdescripcionCE.setEditable(false);
    }
    
    public void activarControles(){
        txtcodigoCE.setEditable(true);
        txtnombreCE.setEditable(true);
        txtdescripcionCE.setEditable(true);
    }
    
      public void limpiarControles(){
       txtcodigoCE.clear();
       txtnombreCE.clear();
       txtdescripcionCE.clear();
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