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
import org.bryanalvarez.bean.Compras;
import org.bryanalvarez.system.Main;

/**
 *
 * @author Usuario
 */
public class MenuComprasController implements Initializable{
private Main escenarioPrincipal;
private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
private operaciones tipoDeOperaciones = operaciones.NINGUNO;
private ObservableList<Compras> listarCompras;


@FXML private TextField txtnumeroDoc;
@FXML private TextField txtfechaDoc;
@FXML private TextField txtdescripcion;
@FXML private TextField txttotalDocumento;
@FXML private TableView tblCompras;

@FXML private TableColumn colnumeroDoc;
@FXML private TableColumn colfechaDoc;
@FXML private TableColumn coldescripcion;
@FXML private TableColumn coltotalDoc;

@FXML private Button btnAgregar;
@FXML private Button btnEliminar;
@FXML private Button btnEditar;
@FXML private Button btnReporte;
@FXML private Button btnRegresar;
                
@FXML private ImageView imgAgregarCompra;
@FXML private ImageView imgEliminarCompra;
@FXML private ImageView imgEditarCompra;
@FXML private ImageView imgReporteCompra;

  @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
}
    
    public void cargarDatos(){
        tblCompras.setItems(getCompras());
        colnumeroDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colfechaDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        coldescripcion.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
        coltotalDoc.setCellValueFactory(new PropertyValueFactory<Compras, Double>("totalDocumento"));
    }
    
    public void seleccionarDatos(){
        txtnumeroDoc.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtfechaDoc.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getFechaDocumento()));
        txtdescripcion.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion()));
        txttotalDocumento.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getTotalDocumento()));
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
                 imgAgregarCompra.setImage(new Image("org/bryanalvarez/Images/Guardar.png"));
                 imgEliminarCompra.setImage(new Image("org/bryanalvarez/Images/Cancelar.png"));
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
                imgAgregarCompra.setImage(new Image("org/bryanalvarez/Images/AgregarCompra.png"));
                imgEliminarCompra.setImage(new Image("org/bryanalvarez/Images/EliminarCompra.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Compras registro = new Compras();
        registro.setNumeroDocumento(Integer.parseInt(txtnumeroDoc.getText()));
        registro.setFechaDocumento(txtfechaDoc.getText());
        registro.setDescripcion(txtdescripcion.getText());
        registro.setTotalDocumento(Double.parseDouble(txttotalDocumento.getText()));
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_AgregarCompras(?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getTotalDocumento());
            procedimiento.execute();
            listarCompras.add(registro);
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
                 imgAgregarCompra.setImage(new Image("org/bryanalvarez/Images/AgregarCompra.png"));
                 imgEliminarCompra.setImage(new Image("org/bryanalvarez/Images/EliminarCompra.png"));
                 tipoDeOperaciones = operaciones.NINGUNO;
                 break;
            default:
                if(tblCompras.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmacion de eliminar esta compra", "Eliminar Compra",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EliminarCompras(?)}");
                            procedimiento.setInt(1,((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listarCompras.remove(tblCompras.getSelectionModel().getSelectedItem());
                            
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
                if(tblCompras.getSelectionModel().getSelectedItem() != null){
                  activarControles();
                  btnEditar.setText("Actualizar");
                  btnReporte.setText("Cancelar");
                  btnAgregar.setDisable(true);
                  btnEliminar.setDisable(true);
                  imgEditarCompra.setImage(new Image("/org/bryanalvarez/Images/Editar.png"));
                  imgReporteCompra.setImage(new Image("/org/bryanalvarez/Images/Cancelar.png"));
                 txtnumeroDoc.setEditable(false);
                 tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un dato a editar");
                break;
                
            case ACTUALIZAR:
                desactivarControles();
                Actualizar();
                  btnEditar.setText("Editar");
                  btnReporte.setText("Reporte");
                  btnAgregar.setDisable(false);
                  btnEliminar.setDisable(false); 
                   imgEditarCompra.setImage(new Image("/org/bryanalvarez/Images/EditarCompra.png"));
                  imgReporteCompra.setImage(new Image("/org/bryanalvarez/Images/ReporteCompra.png"));
                  limpiarControles();
                  tipoDeOperaciones = operaciones.NINGUNO;
                  cargarDatos();
                  break;
        }
    }
    
    public void Actualizar(){
        try{
            PreparedStatement procedimiento = Conection.getInstance().getConexion().prepareCall("{call sp_EditarCompras(?, ?, ?, ?)}");
            Compras registro = (Compras)tblCompras.getSelectionModel().getSelectedItem();
            registro.setFechaDocumento(txtfechaDoc.getText());
            registro.setDescripcion(txtdescripcion.getText());
            registro.setTotalDocumento(Double.parseDouble(txttotalDocumento.getText()));
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getTotalDocumento());
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
                 imgEditarCompra.setImage(new Image("/org/bryanalvarez/Images/EditarCompra.png"));
                 imgReporteCompra.setImage(new Image("/org/bryanalvarez/Images/ReporteCompra.png"));
                 tipoDeOperaciones = operaciones.NINGUNO;
                 break;
        }
    }
    
    public void desactivarControles(){
        txtnumeroDoc.setEditable(false);
        txtfechaDoc.setEditable(false);
       txtdescripcion.setEditable(false);
       txttotalDocumento.setEditable(false);
    }

      public void activarControles(){
         txtnumeroDoc.setEditable(true);
         txtfechaDoc.setEditable(true);
         txtdescripcion.setEditable(true);
        txttotalDocumento.setEditable(true);
      }
    
       public void limpiarControles(){
         txtnumeroDoc.clear();
         txtfechaDoc.clear();
         txtdescripcion.clear();
         txttotalDocumento.clear();
       }
      
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
     public void regresar (ActionEvent event){
        if (event.getSource() == btnRegresar){
        escenarioPrincipal.MenuPrincipalView();
        }
    } 
}