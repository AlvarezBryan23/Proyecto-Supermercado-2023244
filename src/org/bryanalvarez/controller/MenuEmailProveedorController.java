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
import org.bryanalvarez.bean.EmailProveedor;
import org.bryanalvarez.bean.Proveedores;
import org.bryanalvarez.system.Main;

/**
 *
 * @author USUARIO
 */
public class MenuEmailProveedorController implements Initializable{
    private Main escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<EmailProveedor> listarEmailProveedor;
    private ObservableList<Proveedores> listarProveedores;
    
    @FXML private TextField txtCodigoEP;
    @FXML private TextField txtEmailPro;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblEmailProveedor;
    
    @FXML private ComboBox cmbCodigoProveedor;
    
    @FXML private TableColumn colCodigoEmailPro;
    @FXML private TableColumn colEmailProveedor;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colCodigoProveedor;
    
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
        tblEmailProveedor.setItems(getEmailProveedor());
        colCodigoEmailPro.setCellValueFactory(new PropertyValueFactory<EmailProveedor, Integer>("codigoEmailProveedor"));
        colEmailProveedor.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("emailProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("descripcion"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<EmailProveedor, Integer>("codigoProveedor"));
    }
    
    public void seleccionarDatos(){
        txtCodigoEP.setText(String.valueOf(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor()));
        txtEmailPro.setText(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getEmailProveedor());
        txtDescripcion.setText(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbCodigoProveedor.getSelectionModel().select(buscarProveedores(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
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
    
    public ObservableList<EmailProveedor> getEmailProveedor(){
        ArrayList<EmailProveedor> lista = new ArrayList<EmailProveedor>();
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_ListarEmailProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new EmailProveedor(resultado.getInt("codigoEmailProveedor"),
                                                                resultado.getString("emailProveedor"),
                                                                resultado.getString("descripcion"),
                                                                resultado.getInt("codigoProveedor")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarEmailProveedor = FXCollections.observableArrayList(lista);
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
         EmailProveedor registro = new EmailProveedor();
         registro.setCodigoEmailProveedor(Integer.parseInt(txtCodigoEP.getText()));
         registro.setEmailProveedor(txtEmailPro.getText());
         registro.setDescripcion(txtDescripcion.getText());
         registro.setCodigoProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
         try{
             PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarEmailProveedor(?, ?, ?, ?)}");
             procedimiento.setInt(1, registro.getCodigoEmailProveedor());
             procedimiento.setString(2, registro.getEmailProveedor());
             procedimiento.setString(3, registro.getDescripcion());
             procedimiento.setInt(4, registro.getCodigoProveedor());
             procedimiento.execute();   
             listarEmailProveedor.add(registro);
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
                  if(tblEmailProveedor.getSelectionModel().getSelectedItem() != null){
                      int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar", "Seleccione uno", 
                              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                      if(respuesta == JOptionPane.YES_NO_OPTION){
                          try{
                              PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarEmailProveedor(?)}");
                              procedimiento.setInt(1, ((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
                              procedimiento.execute();
                              listarEmailProveedor.remove(tblEmailProveedor.getSelectionModel().getSelectedItem());
                          }catch(Exception e){
                              e.printStackTrace();
                          }
                      }
                  }else
                      JOptionPane.showMessageDialog(null, "Seleccione un dato");
         }
     }
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblEmailProveedor.getSelectionModel().getSelectedItem() != null){
                  activarControles();
                  btnEditar.setText("Actualizar");
                  btnReporte.setText("Cancelar");
                  btnAgregar.setDisable(true);
                  btnEliminar.setDisable(true);
                  txtCodigoEP.setEditable(false);
                  tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un dato para editar");
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
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarEmailProveedor(?, ?, ?, ?)}");
            EmailProveedor registro = (EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem();
            registro.setEmailProveedor(txtEmailPro.getText());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoEmailProveedor());
            procedimiento.setString(2, registro.getEmailProveedor());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setInt(4, registro.getCodigoProveedor());
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
        txtCodigoEP.setEditable(false);
        txtEmailPro.setEditable(false);
        txtDescripcion.setEditable(false);
        cmbCodigoProveedor.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoEP.setEditable(true);
        txtEmailPro.setEditable(true);
        txtDescripcion.setEditable(true);
        cmbCodigoProveedor.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoEP.clear();
        txtEmailPro.clear();
        txtDescripcion.clear();
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