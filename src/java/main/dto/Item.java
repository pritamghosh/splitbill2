package dto;

import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Item implements Cloneable {
	private SimpleStringProperty itemName;
	private ObservableList<MemberAmount> memberAmountList = FXCollections.observableArrayList();
	private SimpleDoubleProperty actualTotal;
	private SimpleDoubleProperty calculatedtotal;

	public synchronized String getItemName() {
		return itemName.get();
	}

	public synchronized void setItemName(String itemName) {
		this.itemName = new SimpleStringProperty(itemName.toUpperCase());
	}

	public synchronized List<MemberAmount> getMemberAmountList() {
		return memberAmountList;
	}

	public synchronized double getActualTotal() {
		return actualTotal.get();
	}

	public synchronized void setActualTotal(double actualTotal) {
		this.actualTotal = new SimpleDoubleProperty(actualTotal);
	}

	public synchronized double getCalculatedtotal() {
		return calculatedtotal.get();
	}

	public synchronized void setCalculatedtotal(double calculatedtotal) {
		this.calculatedtotal = new SimpleDoubleProperty(calculatedtotal);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@SuppressWarnings("null")
	public static Item getTotalItemAfterDelete(Item itemTotal, Item itemNew) {
		if (itemTotal != null) {
			itemTotal.setActualTotal(itemTotal.getActualTotal() - itemNew.getActualTotal());
			double calculatedtotalVar = 0d;
			for (MemberAmount memberAmount : itemTotal.getMemberAmountList()) {
				for (MemberAmount memberAmountNew : itemNew.getMemberAmountList()) {
					if (memberAmount.equals(memberAmountNew)) {
						memberAmount.setAmount(memberAmount.getAmount() - memberAmountNew.getAmount());
						calculatedtotalVar += memberAmount.getAmount();
						break;
					}
				}
			}
			itemTotal.setCalculatedtotal(calculatedtotalVar);
		}
		return itemTotal;
	}
	public static Item getTotalItem(Item itemTotal, Item itemNew, ObservableList<MemberAmount> memberListNew) {
		if (itemTotal == null) {
			itemTotal = new Item();
			itemTotal.setItemName("Total");
			itemTotal.setActualTotal(0.0);
			for (MemberAmount memberAmountNew : memberListNew) {
				MemberAmount memberAmount = new MemberAmount();
				memberAmount.setAmount(0.0);
				memberAmount.setMemberName(memberAmountNew.getMemberName());
				itemTotal.getMemberAmountList().add(memberAmount);
			}

		}
		itemTotal.setActualTotal(itemTotal.getActualTotal() + itemNew.getActualTotal());
		double calculatedtotalVar = 0d;
		for (MemberAmount memberAmount : itemTotal.getMemberAmountList()) {
			for (MemberAmount memberAmountNew : itemNew.getMemberAmountList()) {
				if (memberAmount.equals(memberAmountNew)) {
					memberAmount.setAmount(memberAmount.getAmount() + memberAmountNew.getAmount());
					calculatedtotalVar+=memberAmount.getAmount();
					break;
				}
			}
		}
		itemTotal.setCalculatedtotal(calculatedtotalVar);
		return itemTotal;
	}
}
