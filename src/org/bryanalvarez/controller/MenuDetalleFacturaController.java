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
import org.bryanalvarez.bean.DetalleFactura;
import org.bryanalvarez.bean.Factura;
import org.bryanalvarez.bean.Productos;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuDetalleFacturaController implements Initializable{
     private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<DetalleFactura> listarDetalleFactura;
    private ObservableList<Factura> listarFactura;
    private ObservableList<Productos> listarProductos;
    
    @FXML private TextField txtCodigoDF;
    @FXML private TextField txtprecioUni;
    @FXML private TextField txtcantidad;
    @FXML private TableView tblDetalleFactura;
    
    @FXML private ComboBox cmbNumeroFactura;
    @FXML private ComboBox cmbCodigoProducto;
    
    @FXML private TableColumn colcodigoDF;
    @FXML private TableColumn colprecioU;
    @FXML private TableColumn colcantidad;
    @FXML private TableColumn colNumeroF;
    @FXML private TableColumn colCodigoProducto;

    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnRegresar;
    
   @Override
    public void initialize(URL url, ResourceBundle rb) {
         cargarDatos();
         cmbNumeroFactura.setItems(getFactura());
         cmbCodigoProducto.setItems(getProductos());
    }
    
    public void cargarDatos(){
        tblDetalleFactura.setItems( getDetalleFactura());
       colcodigoDF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("codigoDetalleFactura"));
       colprecioU.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Double>("precioUnitario"));
       colcantidad.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("cantidad"));
       colNumeroF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("cantidad"));
       colCodigoProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("codigoProducto"));
       
    }
    
    public void seleccionarDatos(){
        txtCodigoDF.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura()));
        txtprecioUni.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtcantidad.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCantidad()));
       cmbNumeroFactura.getSelectionModel().select(buscarFactura(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
    }
    
    public Factura buscarFactura(int numeroFactura){
        Factura resultado = null;
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_BuscarFactura(?)}");
            procedimiento.setInt(1, numeroFactura);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Factura(registro.getInt("codigoDetalleFactura"),
                                                        registro.getString("estado"),
                                                        registro.getDouble("totalFactura"),
                                                        registro.getString("fechaFactura"),
                                                        registro.getInt("codigoCliente"),
                                                        registro.getInt("codigoEmpleado")
                );
            
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<DetalleFactura> getDetalleFactura(){
        ArrayList<DetalleFactura> lista = new ArrayList<DetalleFactura>();
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_ListarDetalleFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleFactura(resultado.getInt("codigoDetalleFactura"),
                                                              resultado.getDouble("precioUnitario"),
                                                              resultado.getInt("cantidad"),
                                                              resultado.getInt("numeroFactura"),
                                                              resultado.getString("codigoProducto")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarDetalleFactura = FXCollections.observableArrayList(lista);
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
    
       public ObservableList<Productos> getProductos(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos (resultado.getString("codigoProducto"),
                                                        resultado.getString("descripcionProducto"),
                                                        resultado.getDouble("precioUnitario"),
                                                        resultado.getDouble("precioDocena"),
                                                        resultado.getDouble("precioMayor"),
                                                        resultado.getInt("existencia"),
                                                        resultado.getInt("codigoTipoProducto"),
                                                        resultado.getInt("codigoProveedor")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarProductos = FXCollections.observableArrayList(lista);
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
           DetalleFactura registro = new DetalleFactura();
           registro.setCodigoDetalleFactura(Integer.parseInt(txtCodigoDF.getText()));
           registro.setPrecioUnitario(Double.parseDouble(txtprecioUni.getText()));
           registro.setCantidad(Integer.parseInt(txtcantidad.getText()));
           registro.setNumeroFactura(((Factura)cmbNumeroFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
           registro.setCodigoProducto(((Productos)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
           try{
               PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleFactura(?, ?, ?, ?, ?)}");
               procedimiento.setInt(1, registro.getCodigoDetalleFactura());
               procedimiento.setDouble(2, registro.getPrecioUnitario());
               procedimiento.setInt(3, registro.getCantidad());
               procedimiento.setInt(4, registro.getNumeroFactura());
               procedimiento.setString(5, registro.getCodigoProducto());
               procedimiento.execute();
               listarDetalleFactura.add(registro);
               
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
                   if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null){
                       int respuesta = JOptionPane.showConfirmDialog(null, "Confirme si quiere eliminar el dato", "Eliminar dato", 
                               JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                      if(respuesta == JOptionPane.YES_NO_OPTION){
                          try{
                              PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleFactura(?)}");
                              procedimiento.setInt(1, ((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                              procedimiento.execute();
                              listarDetalleFactura.remove(tblDetalleFactura.getSelectionModel().getSelectedItem());
                              
                          }catch(Exception e){
                              e.printStackTrace();
                          }
                      }
                   }else
                       JOptionPane.showMessageDialog(null, "Debe seleccionar un dato para eliminar");
           }
       }
       
         public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
         
         public void editar(){
             switch(tipoDeOperaciones){
                 case NINGUNO:
                     if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null){
                           activarControles();
                           btnEditar.setText("Actualizar");
                           btnReporte.setText("Cancelar");
                           btnAgregar.setDisable(true);
                           btnEliminar.setDisable(true);
                           txtCodigoDF.setEditable(false);
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
                 PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarDetalleFactura(?, ?, ?, ?, ?)}");
                 DetalleFactura registro = (DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem();
                 registro.setPrecioUnitario(Double.parseDouble(txtprecioUni.getText()));
                 registro.setCantidad(Integer.parseInt(txtcantidad.getText()));
                 registro.setNumeroFactura(((Factura)cmbNumeroFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
                 registro.setCodigoProducto(((Productos)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
                 procedimiento.setInt(1, registro.getCodigoDetalleFactura());
                 procedimiento.setDouble(2, registro.getPrecioUnitario());
                 procedimiento.setInt(3, registro.getCantidad());
                 procedimiento.setInt(4, registro.getNumeroFactura());
                 procedimiento.setString(5, registro.getCodigoProducto());
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
         txtCodigoDF.setEditable(false);
        txtprecioUni.setEditable(false);
        txtcantidad.setEditable(false);
        cmbNumeroFactura.setDisable(true);
        cmbCodigoProducto.setDisable(true); 
       }
       
       public void activarControles(){
         txtCodigoDF.setEditable(true);
        txtprecioUni.setEditable(true);
        txtcantidad.setEditable(true);
        cmbNumeroFactura.setDisable(false);
        cmbCodigoProducto.setDisable(false); 
       }
       
         public void limpiarControles(){
         txtCodigoDF.clear();
        txtprecioUni.clear();
        txtcantidad.clear();
        cmbNumeroFactura.getSelectionModel().getSelectedItem();
        cmbCodigoProducto.getSelectionModel().getSelectedItem();
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