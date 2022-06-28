/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.ReviewArchi;
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

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnMiglioramento"
    private Button btnMiglioramento; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doRiempiLocali(ActionEvent event) {
    	this.cmbLocale.getItems().clear();
    	String citta = this.cmbCitta.getValue();
    	if(citta != null) {
    		cmbLocale.getItems().addAll(model.getBusinessByCity(citta));
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Business b = cmbLocale.getValue();
    	if(b==null) {
    		txtResult.setText("Selezionare un locale");
    		return;
    	}
    	
    	String msg = model.creaGrafo(b);
    	txtResult.appendText(msg+"\n\n");
    	
    	List<ReviewArchi> reviewsMaxArchi = model.getReviewMaxArchiOut();
    	if(reviewsMaxArchi.isEmpty()) {
    		txtResult.appendText("Nessuna soluzione");
    		return;
    	}
    	
    	for(ReviewArchi r : reviewsMaxArchi) {
    		txtResult.appendText(r.getReview()+"\t # ARCHI USCENTI: "+r.getArchiOut()+"\n");
    	}
    }

    @FXML
    void doTrovaMiglioramento(ActionEvent event) {
    	txtResult.clear();
    	List<Review> lista = model.trovaMiglioramento();
    	if(lista==null) {
    		txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	
    	txtResult.appendText("RISULTATO:\n\n");
    	for(Review r : lista) {
    		txtResult.appendText(r+"\n");
    	}
    	
    	txtResult.appendText("\nNUMERO DI GIORNI TOTALE: "+model.getNumGiorni());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMiglioramento != null : "fx:id=\"btnMiglioramento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbCitta.getItems().addAll(model.getCities());
    }
}
