package pl.edu.pjwstk.pplebanski.haus;

import java.util.Map;

import org.jpl7.PrologException;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Util;

abstract class InjectibleEntity{
	public boolean injected = false;
	public boolean modified = false;
	
	abstract void injectEntity();
	
	void inject(){
		if( injected ) {
			System.out.println("% already injected: "+this.toString());
			return;
		}
		injectEntity();
		injected = true;
	}
}

public class Haus {

	public static String join(String r[],String d)
	{
	        if (r.length == 0) return "";
	        StringBuilder sb = new StringBuilder();
	        int i;
	        for(i=0;i<r.length-1;i++)
	            sb.append(r[i]+d);
	        return sb.toString()+r[i];
	}
	
	static boolean assertCond(String text){
		String queryText = "asserta( "+text+" )";
		Term t = Util.textToTerm( queryText );
		
		Query edq = new Query(t);
		
		try {
			if( edq.hasSolution() ){
				System.out.println( text+"." );	
				return true;
			} else {
				System.out.println( "FAIL" );
			}
		} catch (PrologException e) {
			System.out.println( e.getMessage());
		}	
		return false;
	}
	
	public Haus(){

		String t1 = "consult('haus.pl')";
		Query q1 = new Query(t1);
		System.out.println( t1 + " " + (q1.hasSolution() ? "." : " failed") );

		System.out.println( "loading data..." );
		try {
			loadData();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public static Location[] locs = null;
	
	void loadData() throws Exception {
		Location outside = new Location("outside");
		
		Room anteRoom = new Room("anteroom");
		Room kitchen = new Room("kitchen");
		Room bathroom = new Room("bathroom");
		Room livingroom = new Room("livingroom");
		Room secondBedroom = new Room("secondBedroom");
		Room masterBedroom = new Room("masterBedroom");

		Balcony westBalcony = new Balcony("westBalcony");
		Balcony northEastBalcony = new Balcony("northEastBalcony");
		Balcony southEastBalcony = new Balcony("southEastBalcony");

		locs = new Location[]{outside,anteRoom,kitchen,bathroom,livingroom,secondBedroom,masterBedroom,westBalcony,northEastBalcony,southEastBalcony};

		for( Location l : locs ){
			l.inject();
		}

		//												name					source			dest			src pos									dest pos
		Passage houseDoor = 		new DoorPassage( 	"houseDoor", 			anteRoom, 		outside, 		new String[]{"southWall","center"},		new String[]{"house","frontWall", "center"} );

		Passage livingroomDoor = 	new DoorPassage( 	"livingroomDoor", 		anteRoom, 		livingroom, 	new String[]{"northWall","rightCorner"},new String[]{"southWall","leftCorner"} );
		Passage kitchenDoor = 		new DoorPassage( 	"kitchenDoor", 			anteRoom, 		kitchen, 		new String[]{"westWall","rightCorner"},	new String[]{"westWall","rightCorner"} );
		Passage bathroomDoor = 		new DoorPassage( 	"bathroomDoor", 		anteRoom, 		bathroom, 		new String[]{"northWall","leftCorner"},	new String[]{"northWall","leftCorner"} );
		Passage secondBedroomDoor = new DoorPassage(	"secondBedroomDoor", 	anteRoom, 		secondBedroom,	new String[]{"westWall","rightCorner"},	new String[]{"westWall","rightCorner"} );

		Passage masterBedroomDoor = new DoorPassage( 	"masterBedroomDoor", 	livingroom, 	masterBedroom, new String[]{"westWall","rightCorner"},	new String[]{"eastWall","center"} );

		Passage anteroomWindow = 	new WindowPassage(	"anteroomWindow", 		anteRoom, 		outside, new String[]{"southWall","rightOf(houseDoor)"}, new String[]{"house","frontWall", "leftOf(houseDoor)"} );
		Passage livingroomWindow = 	new WindowPassage(	"livingroomWindow", 	livingroom, 	outside, new String[]{"southWall","rightOf(houseDoor)"}, new String[]{"house","frontWall", "leftOf(houseDoor)"} );
		Passage kitchenWindow = 	new WindowPassage(	"kitchenWindow", 		kitchen, 		outside, new String[]{"southWall","rightOf(houseDoor)"}, new String[]{"house","frontWall", "leftOf(houseDoor)"} );
		Passage mBedroomWindow = 	new WindowPassage(	"masterBedroomWindow", 	masterBedroom, 	outside, new String[]{"southWall","rightOf(houseDoor)"}, new String[]{"house","frontWall", "leftOf(houseDoor)"} );
		Passage sBedroomWindow = 	new WindowPassage(	"secondBedroomWindow", 	secondBedroom, 	outside, new String[]{"southWall","rightOf(houseDoor)"}, new String[]{"house","frontWall", "leftOf(houseDoor)"} );

		Passage westBalconyDoor 		= new DoorPassage( 	"westBalconyDoor", 	masterBedroom, 	westBalcony, new String[]{"westWall","rightCorner"},	new String[]{"eastWall","center"} );
		Passage northEastBalconyDoor 	= new DoorPassage( 	"northEastBalconyDoor", 	livingroom, 	northEastBalcony, new String[]{"westWall","rightCorner"},	new String[]{"eastWall","center"} );
		Passage southEastBalconyDoor 	= new DoorPassage( 	"southEastBalconyDoor", 	secondBedroom, 	southEastBalcony, new String[]{"westWall","rightCorner"},	new String[]{"eastWall","center"} );

		Passage westBalconyEntry 		= new Passage(westBalcony,outside);
		Passage northEastBalconyEntry 	= new Passage(northEastBalcony,outside);
		Passage southEastBalconyEntry 	= new Passage(southEastBalcony,outside);



		Furniture shoeRack	= new	Furniture( "shoeRack", new String[]{ "southWall","leftCorner", } );
		shoeRack.addStuff( new Clutter("winterBoots",null) );
		shoeRack.addStuff( new Clutter("summerShoes",null) );
		shoeRack.addStuff( new Clutter("highHeels",null) );
		shoeRack.addStuff( new Clutter("flipFlops",null) );
		anteRoom.addProp(shoeRack);

		Furniture clothesRack	= new	Furniture( "clothesRack", new String[]{ "southWall","rightCorner", } );
		clothesRack.addStuff( new Clutter("lightCoat",null) );
		clothesRack.addStuff( new Clutter("hat",null) );
		clothesRack.addStuff( new Clutter("doorKeys",null) );
		anteRoom.addProp(clothesRack);

		Furniture stove	= new	Furniture( "stove", new String[]{ "southWall","center", } );
		stove.addStuff( new Clutter("pan",null) );
		stove.addStuff( new Clutter("emptyPot",null) );
		stove.addStuff( new Clutter("soupPot",null) );
		kitchen.addProp(stove);

		Furniture kitchenSink	= new	Furniture( "kitchenSink", new String[]{ "southWall","center", } );
		kitchenSink.addStuff( new Clutter("detergent",null) );
		kitchenSink.addStuff( new Clutter("dirtyPlate",null) );
		kitchenSink.addStuff( new Clutter("laddle",null) );
		kitchen.addProp(kitchenSink);

		Furniture kingsizeBed	= new	Furniture( "kingsizeBed", new String[]{ "northWall","center", } );
		kingsizeBed.addStuff( new Clutter("largePillow",null) );
		kingsizeBed.addStuff( new Clutter("smallPillow",null) );
		kingsizeBed.addStuff( new Clutter("teddyBear",null) );
		masterBedroom.addProp(kingsizeBed);

		Furniture queenBed	= new	Furniture( "queenBed", new String[]{ "northWall","center", } );
		queenBed.addStuff( new Clutter("nicePillow",null) );
		queenBed.addStuff( new Clutter("duvet",null) );
		queenBed.addStuff( new Clutter("womensMagazine",null) );
		secondBedroom.addProp(queenBed);

		Furniture dresser	= new	Furniture( "dresser", new String[]{ "northWall","center", } );
		dresser.addStuff( new Clutter("handMirror",null) );
		dresser.addStuff( new Clutter("lipStick",null) );
		secondBedroom.addProp(dresser);

		Furniture sink	= new	Furniture( "sink", new String[]{ "northWall","leftCorner", } );
		sink.addStuff( new Clutter("toothbrush",null) );
		sink.addStuff( new Clutter("handSoap",null) );
		sink.addStuff( new Clutter("toothPaste",null) );
		bathroom.addProp(sink);

		Furniture bathTub	= new	Furniture( "bathTub", new String[]{ "northWall","rightCorner", } );
		bathTub.addStuff( new Clutter("shampooBottle",null) );
		bathTub.addStuff( new Clutter("bathSoap",null) );
		bathTub.addStuff( new Clutter("drainPlug",null) );
		bathroom.addProp(bathTub);

		Furniture toilet	= new	Furniture( "toilet", new String[]{ "northWall","rightCorner", } );
		toilet.addStuff( new Clutter("toiletPaper",null) );
		toilet.addStuff( new Clutter("plunger",null) );
		bathroom.addProp(toilet);
		
		Furniture tvStand	= new	Furniture( "tvStand", new String[]{ "northWall","center", } );
		tvStand.addStuff( new Clutter("hugePlasmaTV",null) );
		livingroom.addProp(tvStand);

		Furniture couch	= new	Furniture( "couch", new String[]{ "southWall","center", } );
		couch.addStuff( new Clutter("tvRemote",null) );
		couch.addStuff( new Clutter("couchPillow",null) );
		couch.addStuff( new Clutter("blanket",null) );
		livingroom.addProp(couch);

		
		for( Location l : locs ){
			l.injectProps();
			l.injectExits();
		}
	}
	
	public void listRooms() throws Exception {
		try{
			
			Query q = new Query("room(X)");		
			
			if( q.hasSolution() ){

				Map<String, Term>[] s = q.allSolutions();

				for ( int i=0 ; i<s.length ; i++ ) {
					String roomName = s[i].get("X").toString();
					System.out.println( "Room: "+ roomName );
					
					{
						Query q2 = null;

						// sprzety
						q2 = new Query("prop("+roomName+",X,Y)");		
						
						if( q2.hasSolution() ){
							System.out.println( "Props in "+roomName );
							Map<String, Term>[] s2 = q2.allSolutions();

							for ( int j=0 ; j<s2.length ; j++ ) {
								System.out.println( "\t-> "+s2[j].get("X") );
							}
						}

						// wyjscia
						q2 = new Query("passage("+roomName+",X)");		
						
						if( q2.hasSolution() ){
							System.out.println( "Exits from "+roomName );
							Map<String, Term>[] s2 = q2.allSolutions();

							for ( int j=0 ; j<s2.length ; j++ ) {
								System.out.println( "\t=> "+s2[j].get("X") );
							}
						}

					}
					
				}
			}
			
		} catch (Exception e){
			System.out.println( "listRooms Error..." );
		}
	}
	
	public void listPassages() throws Exception {
		try{
			
			Query q = new Query("passage(X,Y)");		
			
			if( q.hasSolution() ){
				
				Map<String, Term>[] s = q.allSolutions();

				for ( int i=0 ; i<s.length ; i++ ) {
					System.out.println( s[i].get("X") +" <==> "+ s[i].get("Y"));
				}
			}
			
		} catch (Exception e){
			System.out.println( "listRooms Error..." );
		}
	}
	
	public void activateCat(){
		try{
			
			Query q = new Query("catAct");
			q.oneSolution();

//			if( q.hasSolution() ){
//
//				Map<String, Term>[] s = q.allSolutions();
//
//				for ( int i=0 ; i<s.length ; i++ ) {
//					//System.out.println( s[i].get("X") +" <==> "+ s[i].get("Y"));
//					//System.out.println(s[i]);
//				}
//
//			}
			
		} catch (Exception e){
			System.out.println( "The cat has failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void activateGramma(){
		try{
			
			Query q = new Query("grammaAct");
			q.oneSolution();
			
//			if( q.hasSolution() ){
//
//				Map<String, Term>[] s = q.allSolutions();
//
//				for ( int i=0 ; i<s.length ; i++ ) {
////					System.out.println( s[i].get("X") +" <==> "+ s[i].get("Y"));
////					System.out.println(s[i]);
//				}
//
//			}
			
		} catch (Exception e){
			System.out.println( "The old lady has failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void findPath(Location src, Location dst) throws Exception {
		try{
			Query q;
			Map<String, Term> s;
			
			System.out.println("\npath: "+src.name+" => "+dst.name+"\n------\n");
			
			System.out.println( "Simple path:" );
			q = new Query("path("+src.name+","+dst.name+",Path)");		
			if( q.hasSolution() ){					
				s = q.oneSolution();
				System.out.println( s.get("Path") );
			} else {
				System.out.println( "Cant find path" );
			}

			System.out.println( "\npath via doors: " );
			q = new Query("pathDoor("+src.name+","+dst.name+",Path)");		
			if( q.hasSolution() ){					
				s = q.oneSolution();
				System.out.println( s.get("Path") );
			} else {
				System.out.println( "Cant find path" );
			}

			System.out.println( "\npath via anything: " );
			q = new Query("pathVia("+src.name+","+dst.name+",Path)");		
			if( q.hasSolution() ){					
				s = q.oneSolution();
				System.out.println( s.get("Path") );
			} else {
				System.out.println( "Cant find path" );
			}
			/*				
					Query q3 = new Query("pathDoor(outside,masterBedroom,Path)");		
					if( q3.hasSolution() ){

					Map<String, Term>[] s = q3.allSolutions();

					for ( int i=0 ; i<s.length ; i++ ) {
						System.out.println( s[i].get("Path") );
					}
				*/
				/*
				while( q3.hasMoreSolutions() ){
					try{
						Map<String, Term> s = q3.getSolution();				
						Object o = s.get("Path");
						if( o != null )
							System.out.println( o );
						else
							System.out.println( s );
					} catch (Exception e){
						e.printStackTrace();
						System.out.println( "Error: "+e.getMessage() );
						break;
					}
				}
				*/
		} catch (Exception e){
			System.out.println( "findPath Error..." );
		}
	}

}

