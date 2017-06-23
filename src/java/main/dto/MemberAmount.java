package dto;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemberAmount implements Cloneable{
	private SimpleStringProperty memberName;
	private SimpleDoubleProperty amount;
	

	public synchronized String getMemberName() {
		return memberName==null?"":memberName.get();
	}

	public synchronized void setMemberName(String memberName) {
		this.memberName = new SimpleStringProperty(memberName.toUpperCase());
	}

	public synchronized Double getAmount() {
		return amount==null?0:amount.get();
	}

	public synchronized void setAmount(Double amount) {
		this.amount = new SimpleDoubleProperty(amount);
	}

	public MemberAmount(String memberName) {
		super();
		this.memberName = new SimpleStringProperty(memberName);
		
	}

	public MemberAmount() {
		// TODO Auto-generated constructor stub
	}
	
@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((memberName == null) ? 0 : memberName.get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberAmount other = (MemberAmount) obj;
		if (memberName == null) {
			if (other.memberName != null)
				return false;
		} else if (!memberName.get().equalsIgnoreCase(other.memberName.get()))
			return false;
		return true;
	}

@Override
public Object clone() throws CloneNotSupportedException {
	// TODO Auto-generated method stub
	return super.clone();
}
}
