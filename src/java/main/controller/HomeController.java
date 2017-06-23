package controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import dto.Item;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import listener.SplitBillListener;
import util.SpliBillConstant;
import util.SplitBillContextUtil;
import util.SplitBillUtil;

public class HomeController implements Initializable {
	@FXML
	TabPane tabpane;
	@FXML
	Button addMember;
	@FXML
	Button reset;
	@FXML
	Button removeMember;
	@FXML
	Button splitBill;
	@FXML
	ListView<String> memberList;
	@FXML
	private MenuItem export;
	@FXML
	private MenuItem edit;
	@FXML
	private MenuItem delete;
	@FXML
	private TextField name;
	@FXML
	private DatePicker date;
	@FXML
	private TextField total;

	public static Map<String, SplitBillPopupController> tabIdMap = new HashMap<String, SplitBillPopupController>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		removeMember.disableProperty().bind(Bindings.isEmpty(memberList.getSelectionModel().getSelectedItems()));
		splitBill.disableProperty().bind(Bindings.isEmpty(memberList.getItems()));
		SplitBillListener listener = new SplitBillListener();
		listener.setList(memberList.getItems());
		listener.setTabpane(tabpane);
		listener.setDate(date);
		listener.setName(name);
		splitBill.setOnAction(listener);
		export.setOnAction(event -> export());
	}

	public void delete(ActionEvent event) {
		SplitBillPopupController splitBillPopupController = getCurrentTabController();
		TableView<Item> splitTable = splitBillPopupController.getSplitTable();
		ObservableList<Item> items = splitTable.getItems();
		Item selectedItem = splitTable.getSelectionModel().getSelectedItem();
		Item itemTotal = splitBillPopupController.getTotalItem();
		if (selectedItem != null && !selectedItem.equals(itemTotal)) {
			if (items.size() == 2) {
				items.removeAll(items);
				splitBillPopupController.setTotalItem(null);
			} else {
				items.remove(selectedItem);
				items.remove(itemTotal);
				itemTotal = Item.getTotalItemAfterDelete(itemTotal, selectedItem);
				items.add(itemTotal);
			}
		}
	}

	public void edit(ActionEvent event) {
		SplitBillPopupController splitBillPopupController = getCurrentTabController();
		TableView<Item> splitTable = splitBillPopupController.getSplitTable();
		ObservableList<Item> items = splitTable.getItems();
		Item selectedItem = splitTable.getSelectionModel().getSelectedItem();
		int selectedIndex = splitTable.getSelectionModel().getSelectedIndex();
		Item itemTotal = splitBillPopupController.getTotalItem();
		if (selectedItem != null && !selectedItem.equals(itemTotal)) {
			splitBillPopupController.getItemName().setText(selectedItem.getItemName());
			splitBillPopupController.getActualTotal().setText(String.valueOf(selectedItem.getActualTotal()));
			if (items.size() == 2) {
				items.removeAll(items);
				splitBillPopupController.setTotalItem(null);
			} else {
				items.remove(selectedItem);
				items.remove(itemTotal);
				itemTotal = Item.getTotalItemAfterDelete(itemTotal, selectedItem);
				items.add(itemTotal);
			}
			splitBillPopupController.getTitlePaneA().setExpanded(false);
			splitBillPopupController.getTitlePaneB().setExpanded(true);
			splitBillPopupController.setEditIndex(selectedIndex);
			splitBillPopupController.getActionButton().setText(SpliBillConstant.EDIT);
		}
	}

	private SplitBillPopupController getCurrentTabController() {
		Tab selectedItem = tabpane.getSelectionModel().getSelectedItem();
		String id = selectedItem.getText();
		SplitBillPopupController splitBillPopupController = (SplitBillPopupController) SplitBillContextUtil.getApplicationcontext().get(id);
		return splitBillPopupController;
	}

	private void export() {
		Tab selectedItem = tabpane.getSelectionModel().getSelectedItem();
		String id = selectedItem.getText();
		SplitBillPopupController splitBillPopupController = (SplitBillPopupController) SplitBillContextUtil.getApplicationcontext().get(id);
		TableView<Item> splitTable = splitBillPopupController.getSplitTable();
		ObservableList<TableColumn<Item, ?>> columns = splitTable.getColumns();
		ObservableList<Item> items = splitTable.getItems();
		try {
			SplitBillUtil.export(columns, items, id);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addShortCut(KeyEvent event) {
		KeyCombination add = new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_DOWN);
		KeyCombination remove = new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN);
		KeyCombination resetCombi = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);

		if (tabpane.getSelectionModel().getSelectedIndex()==0) {
			if (add.match(event)) {
				addMember();
			} else if (remove.match(event) && !removeMember.isDisable()) {
				removeMember();
			} else if (resetCombi.match(event) || KeyCode.ESCAPE.equals(event.getCode())) {
				reset.fire();
			} else if (!splitBill.isDisable() && KeyCode.ENTER.equals(event.getCode())) {
				splitBill.fire();
			} 
		}
	}

	public void reset(ActionEvent event) {
		name.setText(null);
		date.setValue(null);
		total.setText(null);
		memberList.getItems().removeAll(memberList.getItems());
	}

	public void addMember(ActionEvent event) {
		addMember();
	}

	private void addMember() {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane root = loader.load(getClass().getResource("/addMember.fxml").openStream());
			AddMemberController addMemberController = (AddMemberController) loader.getController();
			addMemberController.setMemberList(memberList);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Add Member Dialog");
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeMember(ActionEvent event) {
		removeMember();
	}

	private void removeMember() {
		memberList.getItems().remove(memberList.getSelectionModel().getSelectedIndex());
	}

}
