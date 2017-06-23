package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AddMemberController implements Initializable{
	@FXML Button add;
	@FXML TextField memberName;

	@FXML ListView<String> memberList;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		add.setDisable(true);

		
		registerListner();		
	}
	
	private void registerListner() {
		memberName.focusedProperty().addListener((ov, oldValue, newValue) -> {
			if(memberName.getText().trim().isEmpty()){
				add.setDisable(true);
			}
			else{
				add.setDisable(false);
			}
		});		
		memberName.textProperty().addListener((ov, oldValue, newValue) -> {
			if(memberName.getText().trim().isEmpty()){
				add.setDisable(true);
			}
			else{
				add.setDisable(false);
			}
		});		
	}

	public synchronized ListView<String> getMemberList() {
		return memberList;
	}

	public synchronized void setMemberList(ListView<String> memberList) {
		this.memberList = memberList;
	}

	public void addMemberToList(ActionEvent event){
		addMemberToList();
	}

	private void addMemberToList() {
		memberList.getItems().add(memberName.getText().toUpperCase());
		closeWindow();
	}

	private void closeWindow() {
		Stage currentStage = (Stage)  add.getScene().getWindow();
		currentStage.close();
	}

	public void defaultKeyAction(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER&&!add.isDisable()) {
			addMemberToList();
		}
		else if (event.getCode() == KeyCode.ESCAPE){
			closeWindow();
		}
	}
}
