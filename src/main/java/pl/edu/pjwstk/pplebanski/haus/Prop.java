package pl.edu.pjwstk.pplebanski.haus;

import java.util.ArrayList;

// wszelkie sprzety w pokoju
class Prop extends InjectibleEntity{
	String name;
	Location parent = null;
	String[] position = null;
	
	public void setParent( Location parent ){
		this.parent = parent;
	}
	
	Prop( String n, String[] position ){
		name = n;
		//this.parent = parent;
		this.position = position;
	}
	
	void injectEntity(){
		String posList = Haus.join(position, ",");
		System.out.println("% Prop: "+this.name);
		Haus.assertCond( "prop("+this.parent.name+","+this.name+",["+ posList +"])" );
	}
	
	public String toString(){
		return "Prop: " + this.name;
	}
}

class Clutter extends Prop {

	Clutter(String n, String[] position) {
		super(n, position);
	}
	
	void injectEntity(){
		super.injectEntity();
		Haus.assertCond( "clutter("+this.name+")" );
	}
	
}

class Door extends Prop {

	public Door(String n, String[] position) {
		super(n, position);
	}
	
	void injectEntity(){
		super.injectEntity();
		Haus.assertCond( "door("+this.name+")" );
	}
}

class Furniture extends Prop {

	ArrayList<Clutter> contents = new ArrayList<Clutter>(); 

	public void setParent( Location parent ){
		super.setParent( parent );
		if( !contents.isEmpty() ){
			for( Clutter c : contents ){
				c.setParent(parent);
			}
		}
		
	}	
	
	public void addStuff( Clutter c ){
		c.setParent( this.parent );
		c.position = new String[]{"onTop("+this.name+")"};
		contents.add(c);
	};
	
	Furniture(String n, String[] position) {
		super(n, position);
	}
	
	void injectEntity(){
		super.injectEntity();
		Haus.assertCond( "furniture("+this.name+")" );
		
		if( !contents.isEmpty() ){
			for( Clutter c : contents ){
				c.inject();
			}
		}
	}
	
}

class Window extends Prop {

	public Window(String n, String[] position) {
		super(n, position);
	}
	
	void injectEntity(){
		super.injectEntity();
		Haus.assertCond( "window("+this.name+")" );
	}
}