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
import javax.swing.JOptionPane;
import org.bryanalvarez.DB.Conection;
import org.bryanalvarez.bean.Clientes;
import org.bryanalvarez.bean.Empleados;
import org.bryanalvarez.bean.Factura;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuFacturaController implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Factura> listarFactura;
    private ObservableList<Clientes> listarClientes;
    private ObservableList<Empleados> listarEmpleados;
    
    @FXML private TextField txtnumeroFac;
    @FXML private TextField txtEstado;
    @FXML private TextField txtTotalFactura;
    @FXML private TextField txtFechaFactura;
    @FXML private TableView tblFactura;
    
    @FXML private ComboBox cmbCodigoCliente;
    @FXML private ComboBox cmbCodigoEmpleado;
    
    @FXML private TableColumn colNumeroFactura;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colTotalFactura;
    @FXML private TableColumn colFechaFactura;
    @FXML private TableColumn colCodigoCliente;
    @FXML private TableColumn colCodigoEmpleado;

    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnRegresar;
    
    
      @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCodigoCliente.setItems(getClientes());
        cmbCodigoEmpleado.setItems(getEmpleados());
    }

    public void cargarDatos(){
       tblFactura.setItems(getFactura());
       colNumeroFactura.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("numeroFactura"));
       colEstado.setCellValueFactory(new PropertyValueFactory<Factura, String>("estado"));
       colTotalFactura.setCellValueFactory(new PropertyValueFactory<Factura, Double>("totalFactura"));
       colFechaFactura.setCellValueFactory(new PropertyValueFactory<Factura, String>("fechaFactura"));
       colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("codigoCliente"));
       colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("codigoEmpleado"));
       
       
    }
    
    public void seleccionarDatos(){
        txtnumeroFac.setText(String.valueOf(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        txtEstado.setText(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getEstado());
        txtTotalFactura.setText(String.valueOf(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getTotalFactura()));
        txtFechaFactura.setText(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getFechaFactura());
        cmbCodigoCliente.getSelectionModel().select(buscarClientes(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getCodigoCliente()));
    }
    
    public Clientes buscarClientes(int codigoCliente){
        Clientes resultado = null;
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_BuscarClientes(?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Clientes(registro.getInt("codigoCliente"),
                                                        registro.getString("NITcliente"),
                                                        registro.getString("nombreCliente"),
                                                        registro.getString("apellidoCliente"),
                                                        registro.getString("direccionCliente"),
                                                        registro.getString("telefonoCliente"),
                                                        registro.getString("correoCliente")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Factura> getFactura(){
        ArrayList<Factura> lista = new ArrayList<Factura>();
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_ListarFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Factura(resultado.getInt("numeroFactura"),
                                                    resultado.getString("estado"),
                                                    resultado.getDouble("totalFactura"),
                                                    resultado.getString("fechaFactura"),
                                                    resultado.getInt("codigoCliente"),
                                                    resultado.getInt("codigoEmpleado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarFactura = FXCollections.observableArrayList(lista);
    }
    
        public ObservableList<Clientes> getClientes(){
        ArrayList<Clientes> lista = new ArrayList<>();
        try{
            PreparedStatement procedimientos = Conection.getInstance().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = procedimientos.executeQuery();
            while(resultado.next()){
                lista.add(new Clientes (resultado.getInt("codigoCliente"),
                                                    resultado.getString("NITcliente"),
                                                    resultado.getString("nombreCliente"),
                                                    resultado.getString("apellidoCliente"),
                                                    resultado.getString("direccionCliente"),
                                                    resultado.getString("telefonoCliente"),
                                                    resultado.getString("correoCliente")
                                                    
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
     return  listarClientes = FXCollections.observableArrayList(lista);
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
        
        public void agregar(){
            switch(tipoDeOperaciones){
                  case NINGUNO:
                  activarControles();  
                  btnAgregar.setText("Guardar");
                  btnEliminar.setText("Cancelar");
                  btnEditar.setDisable(true);
                  btnReporte.setDisable(true);
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
                  tipoDeOperaciones = operaciones.NINGUNO;
                  cargarDatos();
                  break;
            }
        }
        
        public void guardar(){
            Factura registro = new Factura();
            registro.setNumeroFactura(Integer.parseInt(txtnumeroFac.getText()));
            registro.setEstado(txtEstado.getText());
            registro.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
            registro.setFechaFactura(txtFechaFactura.getText());
            registro.setCodigoCliente(((Clientes)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            registro.setCodigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            try{
                PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarFactura(?, ?, ?, ?, ?, ?)}");
                procedimiento.setInt(1, registro.getNumeroFactura());
                procedimiento.setString(2, registro.getEstado());
                procedimiento.setDouble(3, registro.getTotalFactura());
                procedimiento.setString(4, registro.getFechaFactura());
                procedimiento.setInt(5, registro.getCodigoCliente());
                procedimiento.setInt(6, registro.getCodigoEmpleado());
                procedimiento.execute();
                listarFactura.add(registro);
                
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
                  tipoDeOperaciones = operaciones.NINGUNO;
                  break;
                default:
                    if(tblFactura.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminacion", "Eliminar Factura", 
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_NO_OPTION){
                            try{
                                PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarFactura(?)}");
                                procedimiento.setInt(1, ((Factura)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
                                procedimiento.execute();
                                listarFactura.remove(tblFactura.getSelectionModel().getSelectedItem());
                                
                            }catch(Exception e){
                                e.printStackTrace();
                           }
                       }
                  }else
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un dato");
            }
        }
         public Main getEscenarioPrincipal() {
             return escenarioPrincipal;
    }
         
         public void editar(){
             switch(tipoDeOperaciones){
                 case NINGUNO:
                     if(tblFactura.getSelectionModel().getSelectedItem() != null){
                         btnEditar.setText("Actualizar");
                         btnReporte.setText("Cancelar");
                         btnAgregar.setDisable(true);
                         btnEliminar.setDisable(true);
                         txtnumeroFac.setEditable(false);
                         tipoDeOperaciones = operaciones.ACTUALIZAR;
                     }else
                         JOptionPane.showMessageDialog(null, "Debe seleccionar un dato");
                     break;
                     
                 case ACTUALIZAR:
                      desactivarControles();
                     Actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnAgregar.setDisable(false);
                    btnEliminar.setDisable(false);
                    limpiarControles();
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    break;
             }
         }
         
         public void Actualizar(){
             try{
                 PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarFactura(?, ?, ?, ?, ?, ?)}");
                 Factura registro = (Factura)tblFactura.getSelectionModel().getSelectedItem();
                 registro.setEstado(txtEstado.getText());
                 registro.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
                 registro.setFechaFactura(txtFechaFactura.getText());
                 registro.setCodigoCliente(((Clientes)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
                 registro.setCodigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                 procedimiento.setInt(1, registro.getNumeroFactura());
                 procedimiento.setString(2, registro.getEstado());
                 procedimiento.setDouble(3, registro.getTotalFactura());
                 procedimiento.setString(4, registro.getFechaFactura());
                 procedimiento.setInt(5, registro.getCodigoCliente());
                 procedimiento.setInt(6, registro.getCodigoEmpleado());
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
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
             }
         }
        
         
         
        public void desactivarControles(){
             txtnumeroFac.setEditable(false);
             txtEstado.setEditable(false);
             txtTotalFactura.setEditable(false);
            txtFechaFactura.setEditable(false);
            cmbCodigoCliente.setDisable(true);
            cmbCodigoEmpleado.setDisable(true); 
    }
    
        public void activarControles(){
             txtnumeroFac.setEditable(true);
             txtEstado.setEditable(true);
             txtTotalFactura.setEditable(true);
            txtFechaFactura.setEditable(true);
            cmbCodigoCliente.setDisable(false);
            cmbCodigoEmpleado.setDisable(false); 
     
    }
        
        public void limpiarControles(){
              txtnumeroFac.clear();
             txtEstado.clear();
             txtTotalFactura.clear();
            txtFechaFactura.clear();
            cmbCodigoCliente.getSelectionModel().getSelectedItem();
            cmbCodigoEmpleado.getSelectionModel().getSelectedItem();
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