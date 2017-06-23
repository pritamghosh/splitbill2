package listener;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import controller.HomeController;
import controller.SplitBillPopupController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.SpliBillConstant;
import util.SplitBillContextUtil;

public class SplitBillListener implements EventHandler<ActionEvent>,Runnable{
	@FXML private TabPane tabpane;
	private ObservableList<String> list;
	@FXML
	private TextField name;
	@FXML
	private DatePicker date;
	
	
	
	public synchronized void setName(TextField name) {
		this.name = name;
	}

	public synchronized void setDate(DatePicker date) {
		this.date = date;
	}

	public synchronized void setTabpane(TabPane tabpane) {
		this.tabpane = tabpane;
	}

	public synchronized void setList(ObservableList<String> list) {
		this.list = list;
	}

	private String constructTabId() {
		String nameTxt = name.getText();
		LocalDate dateVal = date.getValue();
		StringBuilder idBuilder = new StringBuilder();
		if (nameTxt != null && !nameTxt.toString().trim().isEmpty()) {
			idBuilder.append(nameTxt.trim());
		}
		if (dateVal != null && !dateVal.toString().trim().isEmpty()) {
			if (idBuilder.length()!= 0) {
				idBuilder.append(SpliBillConstant.EMPTY_SPACE);
			}
			idBuilder.append(dateVal.toString().trim());
		}
		if (idBuilder.length() == 0) {
			return SpliBillConstant.NEW;
		}
		return idBuilder.toString();
	}
	@Override
	public void handle(ActionEvent event) {
		Thread thread = new Thread(this);
		thread.run();
	}

	@Override
	public void run() {
		Pane root;
		try {
			FXMLLoader loader = new FXMLLoader();
			root = loader.load(getClass().getResource("/SplitBillPopupLayout.fxml").openStream());
			SplitBillPopupController controller = (SplitBillPopupController) loader.getController();
			controller.initialize(list);
			String tabId = constructTabId();
			String tabId2 = tabId;
			int i = 2;
			while (SplitBillContextUtil.getApplicationcontext().containsKey(tabId2)) {
				tabId2 = tabId + "(" + i++ + ")";
			}
			Tab newTab = new Tab(tabId2, root);
			tabpane.getTabs().add(newTab);
			newTab.setClosable(true);
			tabpane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
			tabpane.getSelectionModel().select(newTab);
			SplitBillContextUtil.getApplicationcontext().put(tabId2, controller);
			newTab.setOnCloseRequest(e -> {
				SplitBillContextUtil.getApplicationcontext().remove(tabpane.getSelectionModel().getSelectedItem().getText());
				tabpane.getTabs().remove(newTab);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
