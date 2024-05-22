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
import org.bryanalvarez.bean.Compras;
import org.bryanalvarez.bean.DetalleCompra;
import org.bryanalvarez.bean.Productos;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuDetalleCompraController implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<DetalleCompra> listarDetalleCompra;
    private ObservableList<Productos> listarProductos;
    private ObservableList<Compras> listarCompras; 
    
    @FXML private TextField txtcodigoDC;
    @FXML private TextField txtcostoUni;
    @FXML private TextField txtcantidad;
    @FXML private TableView tblDetalleCompra;
    
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbNumeroDocumento;
    
    @FXML private TableColumn colcodigoDC;
    @FXML private TableColumn colcostoUni;
    @FXML private TableColumn colcantidad;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNumeroDocumento;

    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnRegresar;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCodigoProducto.setItems(getProductos());
        cmbNumeroDocumento.setItems(getCompras());
    }
    
   public void cargarDatos(){
       tblDetalleCompra.setItems(getDetalleCompra());
       colcodigoDC.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("codigoDetalleCompra"));
       colcostoUni.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("costoUnitario"));
       colcantidad.setCellValueFactory(new  PropertyValueFactory<DetalleCompra, Integer>("cantidad"));
       colCodigoProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("codigoProducto"));
       colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("numeroDocumento"));
    }
    
    public void seleccionarDatos(){
        txtcodigoDC.setText(String.valueOf(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
        txtcostoUni.setText(String.valueOf(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCostoUnitario()));
        txtcantidad.setText(String.valueOf(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbCodigoProducto.getSelectionModel().select(buscarProductos(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    }
    
    public Productos buscarProductos(String codigoProducto){
        Productos resultado = null;
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
            procedimiento.setString(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Productos(registro.getString("codigoProducto"),
                                                            registro.getString("descripcionProducto"),
                                                            registro.getDouble("precioUnitario"),
                                                            registro.getDouble("precioDocena"),
                                                            registro.getDouble("precioMayor"),
                                                            registro.getInt("existencia"),
                                                            registro.getInt("codigoTipoProducto"),
                                                            registro.getInt("codigoProveedor")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<DetalleCompra> getDetalleCompra(){
        ArrayList<DetalleCompra> lista = new  ArrayList<DetalleCompra>();
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_ListarDetalleCompra()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleCompra(resultado.getInt("codigoDetalleCompra"),
                                                        resultado.getDouble("costoUnitario"),
                                                        resultado.getInt("cantidad"),
                                                        resultado.getString("codigoProducto"),
                                                        resultado.getInt("numeroDocumento")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarDetalleCompra = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Compras> getCompras(){
        ArrayList<Compras> lista = new ArrayList<>();
        try{
            PreparedStatement procedimientos = Conection.getInstance().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimientos.executeQuery();
            while(resultado.next()){
                lista.add(new Compras (resultado.getInt("numeroDocumento"),
                                                       resultado.getString("fechaDocumento"),
                                                       resultado.getString("descripcion"),
                                                       resultado.getDouble("totalDocumento")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarCompras = FXCollections.observableArrayList(lista);
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
       DetalleCompra registro = new DetalleCompra(); 
       registro.setCodigoDetalleCompra(Integer.parseInt(txtcodigoDC.getText()));
       registro.setCostoUnitario(Double.parseDouble(txtcostoUni.getText()));
       registro.setCantidad(Integer.parseInt(txtcodigoDC.getText()));
       registro.setCodigoProducto(((Productos)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
       registro.setNumeroDocumento(((Compras)cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());
       try{
           PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleCompra(?, ?, ?, ?, ?)}");
           procedimiento.setInt(1, registro.getCodigoDetalleCompra());
           procedimiento.setDouble(2, registro.getCostoUnitario());
           procedimiento.setInt(3, registro.getCantidad());
           procedimiento.setString(4, registro.getCodigoProducto());
           procedimiento.setInt(5, registro.getNumeroDocumento());
           procedimiento.execute();
           listarDetalleCompra.add(registro);
           
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
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmacion de eliminacion", "Eliminar Compra", 
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleCompra(?)}");
                            procedimiento.setInt(1, ((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
                            procedimiento.execute();
                            listarDetalleCompra.remove(tblDetalleCompra.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento para eliminar");
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() != null){
                   activarControles();
                  btnEditar.setText("Actualizar");
                  btnReporte.setText("Cancelar");
                  btnAgregar.setDisable(true);
                  btnEliminar.setDisable(true);
                  txtcodigoDC.setEditable(false);
                  tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
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
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarDetalleCompra(?, ?, ?, ?, ?)}");
            DetalleCompra registro = (DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem();
            registro.setCostoUnitario(Double.parseDouble(txtcostoUni.getText()));
            registro.setCantidad(Integer.parseInt(txtcantidad.getText()));
            registro.setCodigoProducto(((Productos)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setNumeroDocumento(((Compras)cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setString(4, registro.getCodigoProducto());
            procedimiento.setInt(5, registro.getNumeroDocumento());
            procedimiento.execute();
            
        }catch(Exception e){
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

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
     public void desactivarControles(){
         txtcodigoDC.setEditable(false);
         txtcostoUni.setEditable(false);
         txtcantidad.setEditable(false);
         cmbCodigoProducto.setDisable(true);
         cmbNumeroDocumento.setDisable(true);
     }
     
     public void activarControles(){
         txtcodigoDC.setEditable(true);
         txtcostoUni.setEditable(true);
         txtcantidad.setEditable(true);
         cmbCodigoProducto.setDisable(false);
         cmbNumeroDocumento.setDisable(false);
     } 
     
     public void limpiarControles(){
         txtcodigoDC.clear();
         txtcostoUni.clear();
         txtcantidad.clear();
         cmbCodigoProducto.getSelectionModel().getSelectedItem();
         cmbNumeroDocumento.getSelectionModel().getSelectedItem();
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