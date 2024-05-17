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
import org.bryanalvarez.DB.Conection;
import org.bryanalvarez.bean.Productos;
import org.bryanalvarez.bean.Proveedores;
import org.bryanalvarez.bean.TipoProducto;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuProductosController implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Productos> listarProductos;
    private ObservableList<Proveedores> listarProveedores;
    private ObservableList<TipoProducto> listarTipoProducto;
    
    @FXML private TextField txtCodigoProduc;
    @FXML private TextField txtDescripcionP;
    @FXML private TextField txtPrecioUP;
    @FXML private TextField txtPrecioDP;
    @FXML private TextField txtPrecioMP;
    @FXML private TextField txtExistencia;
    @FXML private TableView tblProductos;
    
    @FXML private ComboBox cmbCodigoTipoP;
    @FXML private ComboBox cmbCodProv;
    
    @FXML private TableColumn colCodigoPro;
    @FXML private TableColumn colDescripcionP;
    @FXML private TableColumn colPrecioUP;
    @FXML private TableColumn colPrecioDP;
    @FXML private TableColumn colPrecioMP;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn codigoTP;
    @FXML private TableColumn codigoPro;

    
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnRegresar;
    
   @Override
    public void initialize(URL url, ResourceBundle rb) {
      cargarDatos();   
      cmbCodigoTipoP.setItems(getTipoProducto());
      cmbCodProv.setItems(getProveedores());
    }   
    
    public void cargarDatos(){
        tblProductos.setItems(getProductos());
       colCodigoPro.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
       colDescripcionP.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
       colPrecioUP.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
       colPrecioDP.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
       colPrecioMP.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
       colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
       codigoTP.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoTipoProducto"));
       codigoPro.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
     
    }
    

    
    public void seleccionarDatos(){
        txtCodigoProduc.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
        txtDescripcionP.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtPrecioUP.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioDP.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtPrecioMP.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
        txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
         cmbCodigoTipoP.getSelectionModel().select(buscarTipoP(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
    }
    
        public TipoProducto buscarTipoP(int codigoTipoProducto){
        TipoProducto resultado = null;
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_BuscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoProducto(registro.getInt("codigoTipoProducto"),
                                                                    registro.getString("descripcion")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    
        
        return resultado;
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
    
        public ObservableList<Proveedores> getProveedores(){
        ArrayList<Proveedores> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                  lista.add(new Proveedores (resultado.getInt("codigoProveedor"),
                                                              resultado.getString("NITproveedor"),
                                                              resultado.getString("nombreProveedor"),
                                                              resultado.getString("apellidoProveedor"),
                                                              resultado.getString("direccionProveedor"),
                                                              resultado.getString("razonSocial"),
                                                              resultado.getString("contactoPrincipal"),
                                                              resultado.getString("paginaWeb")
                  ));
            }
        }catch(Exception e){
          e.printStackTrace();
        }
        return listarProveedores = FXCollections.observableArrayList(lista);
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
            Productos registro = new Productos();
            registro.setCodigoProducto(txtCodigoProduc.getText());
            registro.setDescripcionProducto(txtDescripcionP.getText());
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUP.getText()));
            registro.setPrecioDocena(Double.parseDouble(txtPrecioDP.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioMP.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            registro.setCodigoProveedor(((Proveedores)cmbCodProv.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setCodigoTipoProducto(((TipoProducto)cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
            try{
                PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?, ?, ?, ?, ?, ?, ?, ?)}");
                procedimiento.setString(1, registro.getCodigoProducto());
                procedimiento.setString(2, registro.getDescripcionProducto());
                procedimiento.setDouble(3, registro.getPrecioUnitario());
                procedimiento.setDouble(4, registro.getPrecioDocena());
                procedimiento.setDouble(5, registro.getPrecioMayor());
                procedimiento.setInt(6, registro.getExistencia());
                procedimiento.setInt(7, registro.getCodigoTipoProducto());
                procedimiento.setInt(8, registro.getCodigoProveedor());
                procedimiento.execute();
                listarProductos.add(registro);
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void desactivarControles(){
        txtCodigoProduc.setEditable(false);
        txtDescripcionP.setEditable(false);
        txtPrecioUP.setEditable(false);
        txtPrecioDP.setEditable(false);
        txtPrecioMP.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoTipoP.setDisable(true);
        cmbCodProv.setDisable(true); 
    }
    
    public void activarControles(){
        txtCodigoProduc.setEditable(true);
        txtDescripcionP.setEditable(true);
        txtPrecioUP.setEditable(true);
        txtPrecioDP.setEditable(true);
        txtPrecioMP.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoP.setDisable(false);
        cmbCodProv.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoProduc.clear();
        txtDescripcionP.clear();
        txtPrecioUP.clear();
        txtPrecioDP.clear();
        txtPrecioMP.clear();
        txtExistencia.clear();
        cmbCodigoTipoP.getSelectionModel().getSelectedItem();
        cmbCodProv.getSelectionModel().getSelectedItem();
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