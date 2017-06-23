package dto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Items {
private ObservableList<Item> itemList = FXCollections.observableArrayList();

public synchronized ObservableList<Item> getItemList() {
	return itemList;
}

}
