package pl.edu.pjwstk.pplebanski.haus;

//przejscie miedzy lokacjami
class Passage extends InjectibleEntity{
	
	Location source = null;
	Location target = null;
	
	Prop entryProp = null;
	Prop exitProp = null;
	
	Passage(Location source, Location target) {
		this.source = source;
		this.target = target;
		
		source.addExit(this);
		target.addExit(this);
	}
		
	void injectEntity(){
		Haus.assertCond( "passage("+this.source.name+", "+this.target.name+")" );
		Haus.assertCond( "passage("+this.target.name+", "+this.source.name+")" );
	}
	
	public String toString(){
		return "Passage: " + this.source.name + " <==> " + this.target.name;
	}		
}

class DoorPassage extends PropPassage {

	DoorPassage(String n, Location source, Location target, String[] entryPosition, String[] exitPosition) {
		super(n, source, target, entryPosition, exitPosition);
	}
	
	void createProps(String[] entryPosition, String[] exitPosition){
		entryProp = new Door(propName,entryPosition);		
		exitProp = new Door(propName,exitPosition);	
	}
	
}

//przejscie polaczone z obiektem ktory da sie umiescic np. drzwiami
abstract class PropPassage extends Passage {

	String propName = null;
	
	Prop entryProp = null;
	Prop exitProp = null;
	
	abstract void createProps(String[] entryPosition, String[] exitPosition);
	
	PropPassage(String propName, Location source, Location target, String[] entryPosition, String[] exitPosition) {
		super(source, target);
		this.propName = propName;
		
		createProps(entryPosition, exitPosition);
		
		Haus.assertCond( "passageVia("+this.source.name+","+this.target.name+","+propName+")" );
		Haus.assertCond( "passageVia("+this.target.name+","+this.source.name+","+propName+")" );
		
		source.addProp(entryProp);
		target.addProp(exitProp);
	}
	
	void injectEntity(){
		super.injectEntity();
		entryProp.inject();
		exitProp.inject();
	}
	
}

class WindowPassage extends PropPassage {

	WindowPassage(String n, Location source, Location target, String[] entryPosition, String[] exitPosition) {
		super(n, source, target, entryPosition, exitPosition);
	}
	
	void createProps(String[] entryPosition, String[] exitPosition){
		entryProp = new Window(propName,entryPosition);		
		exitProp = new Window(propName,exitPosition);	
	}
	
}