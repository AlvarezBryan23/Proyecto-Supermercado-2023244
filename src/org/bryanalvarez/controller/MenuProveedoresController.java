package org.bryanalvarez.controller;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.bryanalvarez.bean.Proveedores;
import org.bryanalvarez.report.GenerarReportes;
import org.bryanalvarez.system.Main;

/**
 *
 * @author informatica
 */
public class MenuProveedoresController implements Initializable {
private Main escenarioPrincipal;
private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
private operaciones tipoDeOperaciones = operaciones.NINGUNO;
private ObservableList<Proveedores> listarProveedores;

@FXML private TextField txtCodigoP; 
@FXML private TextField NITproveedores; 
@FXML private TextField txtNombrep; 
@FXML private TextField txtApellidop; 
@FXML private TextField txtDireccionP; 
@FXML private TextField txtRazonS;  
@FXML private TextField txtContactoP; 
@FXML private TextField txtPaginaW; 
@FXML private TableView tblProveedores;

@FXML private TableColumn colCodigoP;
@FXML private TableColumn colNITpro;
@FXML private TableColumn colNombrep;
@FXML private TableColumn colApellidop;
@FXML private TableColumn colDireccionP;
@FXML private TableColumn colContactoP;
@FXML private TableColumn colRazonS; 
@FXML private TableColumn colPaginaW; 

@FXML private Button btnAgregar;
@FXML private Button btnEliminar;
@FXML private Button btnEditar;
@FXML private Button btnReporte;
@FXML private Button btnRegresar;

@FXML private ImageView imgAgregarProveedores;
@FXML private ImageView imgEliminarProveedores;
@FXML private ImageView imgEditarProveedores;
@FXML private ImageView imgReportesProveedores;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         cargarDatos();
    }
    
    public void cargarDatos(){
        tblProveedores.setItems(getProveedores());
        colCodigoP.setCellValueFactory(new PropertyValueFactory <Proveedores, Integer>("codigoProveedor"));
        colNITpro.setCellValueFactory(new PropertyValueFactory <Proveedores, String>("NITproveedor"));
        colNombrep.setCellValueFactory(new PropertyValueFactory <Proveedores, String>("nombreProveedor"));
        colApellidop.setCellValueFactory(new PropertyValueFactory <Proveedores, String>("apellidoProveedor"));
        colDireccionP.setCellValueFactory(new PropertyValueFactory <Proveedores, String>("direccionProveedor"));
        colRazonS.setCellValueFactory(new PropertyValueFactory <Proveedores, String>("razonSocial"));
        colContactoP.setCellValueFactory(new PropertyValueFactory <Proveedores, String>("contactoPrincipal"));
        colPaginaW.setCellValueFactory(new PropertyValueFactory <Proveedores, String>("paginaWeb"));
    }

    public void seleccionarDatos(){
        txtCodigoP.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        NITproveedores.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNITproveedor()));
        txtNombrep.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNombreProveedor()));
        txtApellidop.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getApellidoProveedor()));
        txtDireccionP.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor()));
        txtRazonS.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial()));
        txtContactoP.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal()));
        txtPaginaW.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb()));
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
                imgAgregarProveedores.setImage(new Image("org/bryanalvarez/Images/Guardar.png"));
                imgEliminarProveedores.setImage(new Image("org/bryanalvarez/Images/Cancelar.png"));
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
                imgAgregarProveedores.setImage(new Image("org/bryanalvarez/Images/AgregarProveedores.png"));
                imgEliminarProveedores.setImage(new Image("org/bryanalvarez/Images/EliminarProveedores.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Proveedores loading = new Proveedores();
        loading.setCodigoProveedor(Integer.parseInt(txtCodigoP.getText()));
        loading.setNITproveedor(NITproveedores.getText());
        loading.setNombreProveedor(txtNombrep.getText());
        loading.setApellidoProveedor(txtApellidop.getText());
        loading.setDireccionProveedor(txtDireccionP.getText());
        loading.setRazonSocial(txtRazonS.getText());
        loading.setContactoPrincipal(txtContactoP.getText());
        loading.setPaginaWeb(txtPaginaW.getText());
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, loading.getCodigoProveedor());
            procedimiento.setString(2, loading.getNITproveedor());
            procedimiento.setString(3, loading.getNombreProveedor());
            procedimiento.setString(4, loading.getApellidoProveedor());
            procedimiento.setString(5, loading.getDireccionProveedor());
            procedimiento.setString(6, loading.getRazonSocial());
            procedimiento.setString(7, loading.getContactoPrincipal());
            procedimiento.setString(8, loading.getPaginaWeb());
            procedimiento.execute();
            listarProveedores.add(loading);
            
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
              imgAgregarProveedores.setImage(new Image("org/bryanalvarez/Images/AgregarProveedores.png"));
              imgEliminarProveedores.setImage(new Image("org/bryanalvarez/Images/EliminarProveedores.png"));
              tipoDeOperaciones = operaciones.NINGUNO;
              break;
            default:
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmacion de la Eliminacion", "Eliminar Proveedor", 
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarProveedores(?)}");
                            procedimiento.setInt(1, ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listarProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                            
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
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                     activarControles();
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     btnAgregar.setDisable(true);
                     btnEliminar.setDisable(true);
                     imgEditarProveedores.setImage(new Image("/org/bryanalvarez/Images/Editar.png"));
                     imgReportesProveedores.setImage(new Image("/org/bryanalvarez/Images/Cancelar.png"));
                     txtCodigoP.setEditable(false);
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
                imgEditarProveedores.setImage(new Image("/org/bryanalvarez/Images/EditarProveedores.png"));
                imgReportesProveedores.setImage(new Image("/org/bryanalvarez/Images/ReporteProveedores.png"));
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
      }  
  }
    public void Actualizar(){
        try{
            PreparedStatement procedimiento = Conection .getInstance().getConexion().prepareCall("{call sp_EditarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
            Proveedores registro = (Proveedores)tblProveedores.getSelectionModel().getSelectedItem();
            registro.setNITproveedor(NITproveedores.getText());
            registro.setNombreProveedor(txtNombrep.getText());
            registro.setApellidoProveedor(txtApellidop.getText());
            registro.setDireccionProveedor(txtDireccionP.getText());
            registro.setRazonSocial(txtRazonS.getText());
            registro.setContactoPrincipal(txtContactoP.getText());
            registro.setPaginaWeb(txtPaginaW.getText());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITproveedor());
            procedimiento.setString(3, registro.getNombreProveedor());
            procedimiento.setString(4, registro.getApellidoProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                 imprimirReporte();
                break;
            case ACTUALIZAR:
            desactivarControles();
            limpiarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnAgregar.setDisable(false);
            btnEliminar.setDisable(false);
            imgEditarProveedores.setImage(new Image("/org/bryanalvarez/Images/EditarProveedores.png"));
            imgReportesProveedores.setImage(new Image("/org/bryanalvarez/Images/ReporteProveedores.png"));
            tipoDeOperaciones = operaciones.NINGUNO;
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoProveedor", null);
        GenerarReportes.mostrarReportes("ReporteProveedores.jasper", "Reporte de Proveedores", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoP.setEditable(false);
        NITproveedores.setEditable(false);
        txtNombrep.setEditable(false);
        txtApellidop.setEditable(false);
        txtDireccionP.setEditable(false);
        txtRazonS.setEditable(false);
        txtContactoP.setEditable(false);
       txtPaginaW.setEditable(false);
    }
    
    public void activarControles(){
         txtCodigoP.setEditable(true);
        NITproveedores.setEditable(true);
        txtNombrep.setEditable(true);
        txtApellidop.setEditable(true);
        txtDireccionP.setEditable(true);
        txtRazonS.setEditable(true);
        txtContactoP.setEditable(true);
       txtPaginaW.setEditable(true);
    }
    
    public void limpiarControles(){
         txtCodigoP.clear();
        NITproveedores.clear();
        txtNombrep.clear();
        txtApellidop.clear();
        txtDireccionP.clear();
        txtRazonS.clear();
        txtContactoP.clear();
        txtPaginaW.clear();   
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