/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.CoppiaOffenseType;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<CoppiaOffenseType> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {

    	txtResult.clear();
    	
    	if (this.boxArco.getValue()==null) {
    		txtResult.appendText("ERRORE : Selezionare archi \n");
    		return; 
    	}
    	
    	List<String> percorso= this.model.percorso(this.boxArco.getValue());
    	for (String s : percorso) {
    		txtResult.appendText(s+"\n");
    	}
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	if (this.boxCategoria.getValue()== null || this.boxMese.getValue()==null) {
    		txtResult.appendText("ERRORE : Selezionare categoria / mese \n");
    		return; 
    	}
    	
    	
    	boolean creato= this.model.creaGrafo(this.boxCategoria.getValue(), this.boxMese.getValue());
    	if(creato==true) {
    	txtResult.appendText("Grafo creato con "+this.model.nVertex()+" vertex and "+
    	this.model.nArchi()+" edges \n ");
    	List<CoppiaOffenseType> archi= this.model.getEdges(); 
    	for (CoppiaOffenseType c : archi) {
    		txtResult.appendText(c+"\n");
    	}
    	//pulisco e riempio tendina 
    	this.boxArco.getItems().removeAll(this.boxArco.getItems()); 
    	this.boxArco.getItems().addAll(this.model.getEdges()); 
    	}
    	else txtResult.appendText("Non esistono reati appartenenti alla categoria in quel mese \n");

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(this.model.getAllOffenseCategories()); 
    	this.boxMese.getItems().addAll(this.model.getMonths()); 
    }
}
