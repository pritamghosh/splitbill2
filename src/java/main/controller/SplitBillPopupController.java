package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.CalculatorMain;
import dto.Item;
import dto.Items;
import dto.MemberAmount;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import util.SpliBillConstant;
import util.SplitBillUtil;

public class SplitBillPopupController implements Initializable, Runnable {

	@FXML
	private TableView<MemberAmount> memberTable;
	@FXML
	private TableView<Item> splitTable;
	@FXML
	private TextField itemName;
	@FXML
	private TextField actualTotal;
	@FXML
	private TextField calculatedTotal;
	@FXML
	private TableColumn<MemberAmount, Double> memberAmount;
	@FXML
	private TableColumn<MemberAmount, String> memberName;
	@FXML
	private TableColumn<Item, String> itemCol;
	/*
	 * @FXML private Label calcIcon;
	 */
	@FXML
	private VBox splitVBox;
	@FXML
	private TitledPane titlePaneA;
	@FXML
	private TitledPane titlePaneB;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private Button calculateButton;
	@FXML
	private Button actionButton;
	private int editIndex;

	public synchronized void setEditIndex(int editIndex) {
		this.editIndex = editIndex;
	}

	public synchronized TitledPane getTitlePaneA() {
		return titlePaneA;
	}

	public synchronized TitledPane getTitlePaneB() {
		return titlePaneB;
	}

	private Items items = new Items();

	private Item totalItem = null;

	public synchronized TextField getItemName() {
		return itemName;
	}

	public synchronized TextField getActualTotal() {
		return actualTotal;
	}

	public synchronized TableView<Item> getSplitTable() {
		return splitTable;
	}

	public synchronized Item getTotalItem() {
		return totalItem;
	}

	public synchronized void setTotalItem(Item totalItem) {
		this.totalItem = totalItem;
	}

	public synchronized Button getActionButton() {
		return actionButton;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	//	calculateButton.disableProperty().bind(Bindings.isEmpty(memberTable.getSelectionModel().getSelectedItems()));
	//	actionButton.disableProperty().bind(Bindings.isEmpty(memberTable.getSelectionModel().getSelectedItems()));
		splitVBox.setVgrow(titlePaneA, Priority.ALWAYS);
		splitVBox.setVgrow(titlePaneB, Priority.ALWAYS);
		memberTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// calcIcon.setGraphic(new ImageView(new
		// Image(getClass().getResourceAsStream("/oa9Srn1.png"))));
		// calcIcon.setOnMouseClicked(event -> initializeCalcIcon());
		calculatedTotal.setEditable(false);
		actionButton.setDisable(true);
		calculateButton.setDisable(true);
		titlePaneA.setExpanded(false);
		registerListner();
	}

	private void initializeCalcIcon() {

		Thread th = new Thread();
		th.start();
	}

	public void initialize(ObservableList<String> list) {
		initializeMemberTable(list);
		initializeSplitTable(list);
	}

	private void initializeSplitTable(ObservableList<String> columns) {
		for (int i = 0; i < columns.size(); i++) {
			String colName = columns.get(i);
			TableColumn<Item, String> col = new TableColumn(colName);
			splitTable.getColumns().add(col);
			col.setSortable(false);
			col.setCellValueFactory((final CellDataFeatures<Item, String> param) -> {
				CellDataFeatures<Item, String> item = param;
				return getColValue(item.getValue(), colName);
			});
		}
		TableColumn<Item, Double> actualTotal = new TableColumn(SpliBillConstant.ACTUAL_TOTAL);
		TableColumn<Item, Double> calculatedTotal = new TableColumn(SpliBillConstant.CALCULATED_TOTAL);
		splitTable.getColumns().add(actualTotal);
		splitTable.getColumns().add(calculatedTotal);
		calculatedTotal.setSortable(false);
		actualTotal.setSortable(false);
		itemCol.setSortable(false);
		itemCol.setCellValueFactory(new PropertyValueFactory<Item, String>("itemName"));
		actualTotal.setCellValueFactory(new PropertyValueFactory<Item, Double>("actualTotal"));
		calculatedTotal.setCellValueFactory(new PropertyValueFactory<Item, Double>("calculatedtotal"));
		splitTable.setItems(items.getItemList());
	}

	private SimpleStringProperty getColValue(Item item, String colName) {
		List<MemberAmount> memberAmountList = item.getMemberAmountList();
		for (MemberAmount memberAmount : memberAmountList) {
			if (memberAmount != null && colName.equalsIgnoreCase(memberAmount.getMemberName())) {
				return new SimpleStringProperty(String.valueOf(memberAmount.getAmount()));
			}
		}
		return null;
	}

	private void initializeMemberTable(ObservableList<String> list) {
		ObservableList<MemberAmount> memberList = FXCollections.observableArrayList();
		memberTable.setEditable(true);
		for (String name : list) {
			MemberAmount memberAmount = new MemberAmount(name);
			memberList.add(memberAmount);
		}
		memberAmount.setCellValueFactory(new PropertyValueFactory<MemberAmount, Double>("amount"));
		memberName.setCellValueFactory(new PropertyValueFactory<MemberAmount, String>("memberName"));
		memberTable.setItems(memberList);
		memberTable.getSelectionModel().select(0);
	}

	public void calculate(ActionEvent event) {
		calculate();
		memberTable.refresh();

	}

	private Item calculate() {
		Item item = new Item();
		item.setItemName(itemName.getText());
		ObservableList<MemberAmount> members = memberTable.getSelectionModel().getSelectedItems();
		ObservableList<MemberAmount> membersAll = memberTable.getItems();
		double calculatedTotalAmount = 0;
		double actualTotalAmount = Double.parseDouble(actualTotal.getText());
		double perHeadAmount = actualTotalAmount / members.size();
		for (MemberAmount member : members) {
			member.setAmount(perHeadAmount);
			calculatedTotalAmount += perHeadAmount;
		}
		for (MemberAmount memberAmount : membersAll) {
			if (!members.contains(memberAmount)) {

			}
		}
		item.getMemberAmountList().addAll(members);
		item.getMemberAmountList().addAll(membersAll);
		item.setCalculatedtotal(calculatedTotalAmount);
		item.setActualTotal(actualTotalAmount);
		calculatedTotal.setText(String.valueOf(calculatedTotalAmount));
		return item;
	}

	public void selectAll() {
		memberTable.getSelectionModel().selectAll();
	}

	public void addRow(ActionEvent event) throws CloneNotSupportedException {
		ObservableList<Item> itemList = items.getItemList();
		itemList.remove(totalItem);
		ObservableList<MemberAmount> memberListNew = FXCollections.observableArrayList();
		ObservableList<MemberAmount> memberList = memberTable.getItems();
		Item itemNew = calculate();
		if (SpliBillConstant.EDIT.equalsIgnoreCase(actionButton.getText())) {
			itemList.add(editIndex,itemNew);
			actionButton.setText(SpliBillConstant.ADD);
		} else {
			itemList.add(itemNew);
		}
		totalItem = Item.getTotalItem(totalItem, itemNew, memberList);
		itemList.add(totalItem);
		for (MemberAmount memberAmount : memberList) {
			MemberAmount memberAmountNew = (MemberAmount) memberAmount.clone();
			memberAmountNew.setAmount(0d);
			memberListNew.add(memberAmountNew);
		}
		memberTable.setItems(memberListNew);
		memberTable.refresh();
		splitTable.refresh();
	}

	private void registerListner() {
		itemName.focusedProperty().addListener((ov, oldValue, newValue) -> {
			enableActionButton();
		});
		itemName.textProperty().addListener((ov, oldValue, newValue) -> {
			enableActionButton();
			itemName.setText(newValue.toUpperCase());
		});

		actualTotal.focusedProperty().addListener((ov, oldValue, newValue) -> {
			enableActionButton();
		});
		actualTotal.textProperty().addListener((ov, oldValue, newValue) -> {
			SplitBillUtil.setNumericTrue(actualTotal, newValue);
			enableActionButton();
		});
	}

	public void enableActionButton() {
		try {
			if ((actualTotal.getText() != null && !actualTotal.getText().trim().isEmpty())
					&& (itemName.getText() != null && !itemName.getText().trim().isEmpty())) {
				Double.parseDouble(actualTotal.getText().trim());
				actionButton.setDisable(false);
				calculateButton.setDisable(false);
			} else {
				actionButton.setDisable(true);
				calculateButton.setDisable(true);
			}
		} catch (NumberFormatException ex) {
			actionButton.setDisable(true);
			calculateButton.setDisable(true);
		}

	}

	@Override
	public void run() {
		String[] args = null;
		CalculatorMain.main(args);
	}

}
