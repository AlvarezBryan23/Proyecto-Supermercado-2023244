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
import org.bryanalvarez.bean.Clientes;
import org.bryanalvarez.system.Main;


/**
 * FXML Controller class
 *
 * @author USUARIO
 */
public class MenuClientesController implements Initializable {
private Main escenarioPrincipal;
private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
private operaciones tipoDeOperaciones = operaciones.NINGUNO;
private ObservableList<Clientes> listarClientes;


@FXML private TextField txtCorreoC;
@FXML private TextField txtDireccionC;
@FXML private TextField txtCodigoCliente;
@FXML private TextField txtTelefonoC;
@FXML private TextField NITcliente;
@FXML private TextField NombreC;
@FXML private TextField ApellidoC;
@FXML private TableView tblClientes;

@FXML private TableColumn colCodigoC;
@FXML private TableColumn NITC;
@FXML private TableColumn colNombreC;
@FXML private TableColumn colApellidoC;
@FXML private TableColumn colDireccionC;
@FXML private TableColumn colTelefonoC;
@FXML private TableColumn colCorreoC;   

@FXML private Button btnAgregar;
@FXML private Button btnEliminar;
@FXML private Button btnEditar;
@FXML private Button btnReporte;
@FXML private Button btnRegresar;
                
@FXML private ImageView imgAgregarClientes;
@FXML private ImageView imgEliminarClientes;
@FXML private ImageView imgEditar;
@FXML private ImageView imgReportes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }    

    public void cargarDatos(){
        tblClientes.setItems(getClientes());
        colCodigoC.setCellValueFactory (new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
        NITC.setCellValueFactory (new PropertyValueFactory<Clientes, String>("NITcliente"));
        colNombreC.setCellValueFactory (new PropertyValueFactory<Clientes, String>("nombreCliente"));
        colApellidoC.setCellValueFactory (new PropertyValueFactory<Clientes, String>("apellidoCliente"));
        colDireccionC.setCellValueFactory (new PropertyValueFactory<Clientes, String>("direccionCliente")); 
        colTelefonoC.setCellValueFactory (new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colCorreoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoCliente"));
        
    }
    
    public void seleccionarDatos(){
        txtCodigoCliente.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        NITcliente.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNITcliente()));
        NombreC.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente()));
        ApellidoC.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente()));
        txtDireccionC.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente()));
        txtTelefonoC.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente()));
        txtCorreoC.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente()));
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
    
    public void agregar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgAgregarClientes.setImage(new Image("org/bryanalvarez/Images/Guardar.png"));
                imgEliminarClientes.setImage(new Image("org/bryanalvarez/Images/Cancelar.png"));
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
                imgAgregarClientes.setImage(new Image("org/bryanalvarez/Images/AgregarClientes.png"));
                imgEliminarClientes.setImage(new Image("org/bryanalvarez/Images/EliminarClientes.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
        }
    }
    public void guardar(){
        Clientes registro = new Clientes();
        registro.setCodigoCliente (Integer.parseInt(txtCodigoCliente.getText()));
        registro.setNITcliente (NITcliente.getText());
        registro.setNombreCliente (NombreC.getText());
        registro.setApellidoCliente (ApellidoC.getText());
        registro.setTelefonoCliente (txtTelefonoC.getText());
        registro.setDireccionCliente (txtDireccionC.getText());
        registro.setCorreoCliente (txtCorreoC.getText());
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarClientes(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString (4, registro.getNITcliente());
            procedimiento.setString(2, registro.getNombreCliente());
            procedimiento.setString (3, registro.getApellidoCliente());
            procedimiento.setString(5, registro.getTelefonoCliente()); 
            procedimiento.setString(6, registro.getDireccionCliente());
            procedimiento.setString (7, registro.getCorreoCliente()); 
            procedimiento.execute();
            listarClientes.add(registro);
            
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
              imgAgregarClientes.setImage(new Image("org/bryanalvarez/Images/AgregarClientes.png"));
              imgEliminarClientes.setImage(new Image("org/bryanalvarez/Images/EliminarClientes.png"));
              tipoDeOperaciones = operaciones.NINGUNO;
              break;
            default:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si Elimina el registro", "Eliminar Clientes", 
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listarClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
                            
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
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgReportes.setImage(new Image("/org/bryanalvarez/Images/Cancelar.png"));
                    imgEditar.setImage(new Image("/org/bryanalvarez/Images/Editar.png"));
                    txtCodigoCliente.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento a editar");
                break;
                
            case ACTUALIZAR:  
                desactivarControles();
                   Actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnAgregar.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgReportes.setImage(new Image("/org/bryanalvarez/Images/ReportesClientes.png"));
                    imgEditar.setImage(new Image("/org/bryanalvarez/Images/EditarClientes.png"));
                    limpiarControles();
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
    
    public void Actualizar(){
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)}");
            Clientes registro = (Clientes)tblClientes.getSelectionModel().getSelectedItem();
            registro.setNITcliente(NITcliente.getText());
            registro.setNombreCliente(NombreC.getText());
            registro.setApellidoCliente(ApellidoC.getText());
            registro.setDireccionCliente(txtDireccionC.getText());
            registro.setTelefonoCliente(txtTelefonoC.getText());
            registro.setCorreoCliente(txtCorreoC.getText());
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNITcliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
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
                imgEditar.setImage(new Image ("/org/bryanalvarez/Images/EditarClientes.png"));
                imgReportes.setImage(new Image ("/org/bryanalvarez/Images/ReportesClientes.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;    
        }
    }
    
    public void desactivarControles(){
         txtCodigoCliente.setEditable(false);
         NITcliente.setEditable(false);
         NombreC.setEditable(false);
        ApellidoC.setEditable(false);
        txtDireccionC.setEditable(false);
       txtTelefonoC.setEditable(false);
        txtCorreoC.setEditable(false);     
    }
    
    public void activarControles(){
         txtCodigoCliente.setEditable(true);
         NITcliente.setEditable(true);
         NombreC.setEditable(true);
        ApellidoC.setEditable(true);
        txtDireccionC.setEditable(true);
       txtTelefonoC.setEditable(true);
        txtCorreoC.setEditable(true);
    }
    
    public void limpiarControles(){
       txtCodigoCliente.clear();
       NITcliente.clear();
       NombreC.clear();
       ApellidoC.clear();
       txtDireccionC.clear();
       txtTelefonoC.clear();
        txtCorreoC.clear();
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