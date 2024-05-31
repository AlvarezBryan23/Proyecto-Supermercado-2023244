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
import org.bryanalvarez.bean.Proveedores;
import org.bryanalvarez.bean.TelefonoProveedor;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuTelefonoProveedorController  implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<TelefonoProveedor> listarTelefonoProveedor;
    private ObservableList<Proveedores> listarProveedores;
    
    @FXML private TextField txtCodigoTelefonoProveedor;
    @FXML private TextField txtNumeroP;
    @FXML private TextField txtNumeroS;
    @FXML private TextField txtObservaciones;
    @FXML private TableView tblTelefonoProveedor;
    
    @FXML private ComboBox cmbCodigoProveedor;
    
    @FXML private TableColumn colCodigoTelefonoProveedor;
    @FXML private TableColumn colNumeroPrin;
    @FXML private TableColumn colNumeroSecu;
    @FXML private TableColumn colObservaciones;
    @FXML private TableColumn colCodigoProve;
    
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnRegresar;
    
       @Override
    public void initialize(URL url, ResourceBundle rb) {
         cargarDatos();
         cmbCodigoProveedor.setItems(getProveedores());
    }
    
    public void cargarDatos(){
       tblTelefonoProveedor.setItems(getTelefonoProveedor());
       colCodigoTelefonoProveedor.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, Integer>("codigoTelefonoProveedor"));
       colNumeroPrin.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("numeroPrincipal"));
       colNumeroSecu.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("numeroSecundario"));
       colObservaciones.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("observaciones"));
       colCodigoProve.setCellValueFactory(new  PropertyValueFactory<TelefonoProveedor, Integer>("codigoProveedor"));
    }
    
    public void seleccionarDatos(){
        txtCodigoTelefonoProveedor.setText(String.valueOf(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
        txtNumeroP.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroPrincipal());
        txtNumeroS.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroSecundario());
        txtObservaciones.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getObservaciones());
        cmbCodigoProveedor.getSelectionModel().select(buscarProveedores(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }
    
    public Proveedores buscarProveedores(int codigoProveedor){
        Proveedores resultado = null;
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_BuscarProveedores(?)}");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
                                                                registro.getString("NITproveedor"),
                                                                registro.getString("nombreProveedor"),
                                                                registro.getString("apellidoProveedor"),
                                                                registro.getString("direccionProveedor"),
                                                                registro.getString("razonSocial"),
                                                                registro.getString("contactoPrincipal"),
                                                                registro.getString("paginaWeb")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<TelefonoProveedor> getTelefonoProveedor(){
        ArrayList<TelefonoProveedor> lista = new ArrayList<TelefonoProveedor>();
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_ListarTelefonoProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoProveedor(resultado.getInt("codigoTelefonoProveedor"),
                                                                    resultado.getString("numeroPrincipal"),
                                                                    resultado.getString("numeroSecundario"),
                                                                    resultado.getString("observaciones"),
                                                                    resultado.getInt("codigoProveedor")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarTelefonoProveedor = FXCollections.observableArrayList(lista);
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
            TelefonoProveedor registro = new TelefonoProveedor();
            registro.setCodigoTelefonoProveedor(Integer.parseInt(txtCodigoTelefonoProveedor.getText()));
            registro.setNumeroPrincipal(txtNumeroP.getText());
            registro.setNumeroSecundario(txtNumeroS.getText());
            registro.setObservaciones(txtObservaciones.getText());
            registro.setCodigoProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            try{
                PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarTelefonoProveedor(?, ?, ?, ?, ?)}");
                procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
                procedimiento.setString(2, registro.getNumeroPrincipal());
                procedimiento.setString(3, registro.getNumeroSecundario());
                procedimiento.setString(4, registro.getObservaciones());
                procedimiento.setInt(5, registro.getCodigoProveedor());
                procedimiento.execute();
                listarTelefonoProveedor.add(registro);
                
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
                    if(tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null){
                        int respuesta  = JOptionPane.showConfirmDialog(null, "Confirmar la eliminacion", "Eliminar", 
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_NO_OPTION){
                            try{
                                PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarTelefonoProveedor(?)}");
                                procedimiento.setInt(1, ((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
                                procedimiento.execute();
                                listarTelefonoProveedor.remove(tblTelefonoProveedor.getSelectionModel().getSelectedItem());
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
            }
        }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null){
                  activarControles();
                  btnEditar.setText("Actualizar");
                  btnReporte.setText("Cancelar");
                  btnAgregar.setDisable(true);
                  btnEliminar.setDisable(true);
                  txtCodigoTelefonoProveedor.setEditable(false);
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
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarTelefonoProveedor(?, ?, ?, ?, ?)}");
            TelefonoProveedor registro = (TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem();
            registro.setNumeroPrincipal(txtNumeroP.getText());
            registro.setNumeroSecundario(txtNumeroS.getText());
            registro.setObservaciones(txtObservaciones.getText());
            registro.setCodigoProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getNumeroPrincipal());
            procedimiento.setString(3, registro.getNumeroSecundario());
            procedimiento.setString(4, registro.getObservaciones());
            procedimiento.setInt(5, registro.getCodigoProveedor());
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
        txtCodigoTelefonoProveedor.setEditable(false);
        txtNumeroP.setEditable(false);
        txtNumeroS.setEditable(false);
        txtObservaciones.setEditable(false);
        cmbCodigoProveedor.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoTelefonoProveedor.setEditable(true);
        txtNumeroP.setEditable(true);
        txtNumeroS.setEditable(true);
        txtObservaciones.setEditable(true);
        cmbCodigoProveedor.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoTelefonoProveedor.clear();
        txtNumeroP.clear();
        txtNumeroS.clear();
        txtObservaciones.clear();
        cmbCodigoProveedor.getSelectionModel().getSelectedItem();
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