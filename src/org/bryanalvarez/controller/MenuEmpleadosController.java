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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.bryanalvarez.DB.Conection;
import org.bryanalvarez.bean.CargoEmpleado;
import org.bryanalvarez.bean.Empleados;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuEmpleadosController  implements Initializable {
private Main escenarioPrincipal;
private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
private operaciones tipoDeOperaciones = operaciones.NINGUNO;
private ObservableList<Empleados> listarEmpleados;
private ObservableList<CargoEmpleado> listarCargo;

@FXML private TextField txtcodigoEm;
@FXML private TextField txtnombreEm;
@FXML private TextField txtapellidoEm;
@FXML private TextField txtsueldoEm;
@FXML private TextField txtdireccionEm;
@FXML private TextField txturnoEm;
@FXML private TableView tblEmpleados;

@FXML private ComboBox cmbCodigoCE;

@FXML private TableColumn colCodigoE;
@FXML private TableColumn colnombreE;
@FXML private TableColumn colapellidoE;
@FXML private TableColumn colsueldoE;
@FXML private TableColumn coldireccionE;
@FXML private TableColumn colturnoE;
@FXML private TableColumn colCodigoCE;

@FXML private Button btnAgregar;
@FXML private Button btnEliminar;
@FXML private Button btnEditar;
@FXML private Button btnReporte;
@FXML private Button btnRegresar;

@FXML private ImageView imgAgregarEmpleado;
@FXML private ImageView imgEliminarEmpleado;
@FXML private ImageView imgEditarEmpleado;
@FXML private ImageView imgReporteEmpleado;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        
    }   
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colCodigoE.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colnombreE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colapellidoE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
        colsueldoE.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        coldireccionE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccion"));
        colturnoE.setCellValueFactory(new PropertyValueFactory<Empleados, String>("turno"));
        colCodigoCE.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoCargoEmpleado"));
    }
    
    public void seleccionarDatos(){
      txtcodigoEm.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
      txtnombreEm.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado()));
      txtapellidoEm.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado()));
      txtsueldoEm.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
      txtdireccionEm.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccion()));
      txturnoEm.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTurno()));
      cmbCodigoCE.getSelectionModel().select(buscarCargo(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
    }
  
    public CargoEmpleado  buscarCargo(int codigoCargoEmpleado){
        CargoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
            procedimiento.setInt(1, codigoCargoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new CargoEmpleado(registro.getInt("codigoCargoEmpleado"),
                                                                    registro.getString("nombreCargo"),
                                                                    registro.getString("descripcionCargo")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                                                        resultado.getString("nombreEmpleado"),
                                                        resultado.getString("apellidoEmpleado"),
                                                        resultado.getDouble("sueldo"),
                                                        resultado.getString("direccion"),
                                                        resultado.getString("turno"),
                                                        resultado.getInt("codigoCargoEmpleado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarEmpleados  = FXCollections.observableArrayList(lista);
    }  
    
    public void desactivarControles(){
       txtcodigoEm.setEditable(false);
       txtnombreEm.setEditable(false);
       txtapellidoEm.setEditable(false);
       txtsueldoEm.setEditable(false);
       txtdireccionEm.setEditable(false);
       txturnoEm.setEditable(false);
    }
    
    public void activarControles(){
       txtcodigoEm.setEditable(true);
       txtnombreEm.setEditable(true);
       txtapellidoEm.setEditable(true);
       txtsueldoEm.setEditable(true);
       txtdireccionEm.setEditable(true);
       txturnoEm.setEditable(true);  
    }
    
    public void limpiarControles(){
        txtcodigoEm.clear();
       txtnombreEm.clear();
       txtapellidoEm.clear();
       txtsueldoEm.clear();
       txtdireccionEm.clear();
       txturnoEm.clear();
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