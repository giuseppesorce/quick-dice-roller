package ohm.quickdice.entity;

import ohm.quickdice.R;
import ohm.quickdice.control.GraphicManager;

import android.content.Context;

public class RollModifier {

	protected String title;
	protected String description;
	protected int value;
	protected int resourceIndex;
	protected DiceBag parent;
	
	public RollModifier(Context context, int value) {
		if (value < 0) {
			//Penalty
			this.title = String.format(context.getString(R.string.msgMalusTitle), value);
			this.description = String.format(context.getString(R.string.msgMalusMessage), value);
			this.value = value;
			this.resourceIndex = GraphicManager.INDEX_DICE_ICON_MALUS;
		} else {
			//Bonus
			this.title = String.format(context.getString(R.string.msgBonusTitle), value);
			this.description = String.format(context.getString(R.string.msgBonusMessage), value);
			this.value = value;
			this.resourceIndex = GraphicManager.INDEX_DICE_ICON_BONUS;
		}
	}
	
	public RollModifier(String name, String description, int value, int resourceIndex) {
		this.title = name;
		this.description = description;
		this.value = value;
		this.resourceIndex = resourceIndex;
	}
	
	public String getName() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getValue() {
		return value;
	}

	public String getValueString() {
		if (value < 0)
			return Integer.toString(value);
		else
			return "+" + Integer.toString(value);
	}
	
	public int getResourceIndex() {
		return resourceIndex;
	}
	
	public DiceBag getParent() {
		return parent;
	}

	protected void setParent(DiceBag parent) {
		this.parent = parent;
	}

	public boolean isChanged() {
		return parent == null ? false : parent.isChanged();
	}

	protected void setChanged() {
		if (parent != null) {
			parent.setChanged();
		}
	}
}
