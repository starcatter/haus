package pl.edu.pjwstk.pplebanski.haus;

import java.util.ArrayList;

class Location extends InjectibleEntity{
	String name;
	ArrayList<Prop> props = new ArrayList<Prop>();
	ArrayList<Passage> exits = new ArrayList<Passage>();
	
	Location( String n ){
		name = n;
	}
	
	void addProp( Prop p ){
		p.setParent ( this );
		props.add(p);
	}
	
	void addExit( Passage p ){
		exits.add(p);
	}
	
	void injectProps(){
		System.out.println("\n% Location props: "+this.name);
		for( Prop p : props ){
			p.inject();
		}
	}
	
	void injectExits(){
		System.out.println("\n% Location exits: "+this.name);
		for( Passage p : exits ){
			p.inject();
		}
	}
	
	@Override
	void injectEntity() {
		// TODO Auto-generated method stub
		System.out.println("% Location: "+this.name);
		Haus.assertCond( "location("+this.name+")" );
	}
	
	public String toString(){
		return "Location: " + this.name;
	}	
}

class Balcony extends Location {

	Balcony(String n) {
		super(n);
	}

	void injectEntity(){
		super.injectEntity();
		Haus.assertCond( "balcony("+this.name+")" );
	}
	
}

class Room extends Location {

	Room(String n) {
		super(n);
	}

	void injectEntity(){
		super.injectEntity();
		Haus.assertCond( "room("+this.name+")" );
	}
	
}