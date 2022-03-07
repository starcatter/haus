:- dynamic location/1.
:- dynamic room/1.
:- dynamic balcony/1.

:- dynamic passage/2.
:- dynamic door/2.
:- dynamic window/2.

:- dynamic prop/3.

:- dynamic cat/1.
:- dynamic gramma/1.

cat(outside).
gramma(westBalcony).

/*
prop(anteroom,coatHanger,[southWall,leftOf(door(anteroom,outside))]).
contents(coatHanger,[winterCoat,umbrella,carKeys]).
*/

contains(X,Y) :-
              contents(X,Z),
              member(Y,Z).

/*
furniture(anteroom, shoeRack, [southWall, leftOf(shoeRack)]).

furniture(livingroom, sofa, [northWall, center]).
furniture(livingroom, tv, [southWall, center]).
*/

path(A,B,Path) :-
       travel(A,B,[A],Q),
       reverse(Q,Path).

travel(A,B,P,[B|P]) :-
       passage(A,B).
	   
travel(A,B,Visited,Path) :-
       passage(A,C),
       C \== B,
       \+member(C,Visited),
       travel(C,B,[C|Visited],Path).

pathDoor(A,B,Path) :-
       travelDoor(A,B,[A],Q),
%	   write('pathing from ['),
%	   write(A),write('] to ['),
%	   write(B),write('] at ['),
%	   write(Q),write(']'), nl,
       reverse(Q,Path).

travelDoor(A,B,P,[B|[X|P]]) :-
       passageVia(A,B,X), door(X).
%	   write('travel end: from ['),
%	   write(A),write('] to ['),
%	   write(B),write('] at ['),
%	   write(B),write('] via '),write(P), nl.
	   
travelDoor(A,B,Visited,Path) :-
       passageVia(A,C,X), door(X),
       C \== B,
       \+member(X,Visited),
       \+member(C,Visited),
%	   write('travel from ['),
%	   write(A),write('] to ['),
%	   write(B),write('] at ['),
%	   write(C),write('] via '),write(Visited), nl,
       travelDoor(C,B,[C|[X|Visited]],Path).

% path wo gramma found
pathVia(A,B,Path) :-
		passage(B,C),
		\+gramma(C),
		travelVia(A,B,[A],Q),
		reverse(Q,Path).

% path found
pathVia(A,B,Path) :-
		travelVia(A,B,[A],Q),
		reverse(Q,Path).

%path via props
travelVia(A,B,P,[B|[X|P]]) :-
       passageVia(A,B,X).

travelVia(A,B,Visited,Path) :-
       passageVia(A,C,X),
	   \+gramma(C),
       C \== B,
       \+member(X,Visited),
       \+member(C,Visited),
       travelVia(C,B,[C|[X|Visited]],Path).

% direct passages
travelVia(A,B,P,[B|P]) :-
       \+passageVia(A,B,_), passage(A,B).

travelVia(A,B,Visited,Path) :-
       \+passageVia(A,C,_), passage(A,C),
       C \== B,
       \+member(C,Visited),
       travelVia(C,B,[C|Visited],Path).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% path found
pathAlt(A,B,Path) :-
       travelAlt(A,B,[A],Q),
       reverse(Q,Path).

%path via props
travelAlt(A,B,P,[B|[X|P]]) :-
       passageVia(A,B,X), \+door(X).

travelAlt(A,B,Visited,Path) :-
       passageVia(A,C,X), \+door(X),
       C \== B,
       \+member(X,Visited),
       \+member(C,Visited),
       travelAlt(C,B,[C|[X|Visited]],Path).

% direct passages
travelAlt(A,B,P,[B|P]) :-
       \+passageVia(A,B,X), passage(A,B).

travelAlt(A,B,Visited,Path) :-
       \+passageVia(A,C,_), passage(A,C),
       C \== B,
       \+member(C,Visited),
       travelAlt(C,B,[C|Visited],Path).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%

take_path([]).

take_path([Current|PathLeft]):-
  traverse(Current, PathLeft),
  nl,
  take_path(PathLeft).
  
take_path(From,To) :-
  path(From,To,X),
  take_path(X).
  
  


traverse(From,[]) :-
                     write('Arrived at '),
                     write(From),
                     nl.
                     
traverse(From,[To|_]) :-
                         traverse(From,To).
                         
traverse(From,To) :-
  %passage(From,To),
  write('Taking door'),
  write(' from '),
  write(From),
  write(' to '),
  write(To).

 
dropClutter(Furniture) :-
	furniture(Furniture),
	prop(Room,Furniture,_),	% there's some furniture in the room
	room(Room),				% we're in a room

	prop(Room,Clutter,Location),
	clutter(Clutter),		% we're trying to drop some clutter
	member(onTop(Furniture),Location),	%clutter is on top of the furniture
	% preconditions end
	write('The cat jumps on the '),write(Furniture),write('!'),nl,
	retract( cat(Room) ),
	assert( cat(Furniture) ),
	!,
	dropClutter(Clutter).
  
dropClutter(Clutter) :-
	prop(Room,Clutter,Location),
	room(Room),				% we're in a room
	clutter(Clutter),		% we're trying to drop some clutter
	prop(Room,Furniture,_),	% there's some furniture in the room
	furniture(Furniture),
	member(onTop(Furniture),Location),	%clutter is on top of the furniture
	% preconditions end
	write('The cat knocks the '),write(Clutter),write(' to the floor!'),nl,
	retract( prop(Room,Clutter,Location) ),	% clutter no longer on top
	assertz( prop(Room,Clutter,[floor,nextTo(Furniture)]) ). %drop it to the floor
  
pickupClutter(Clutter) :-
	prop(Room,Clutter,Location),
	room(Room),				% we're in a room
	clutter(Clutter),		% we're trying to drop some clutter
	prop(Room,Furniture,_),	% there's some furniture in the room
	furniture(Furniture),
	member(nextTo(Furniture),Location),	%clutter is on top of the furniture
	% preconditions end
	write('Gramma picks up the '),write(Clutter),write(' and places it on the '),write(Furniture),nl,
	retract( prop(Room,Clutter,Location) ),	% clutter no longer on top
	assertz( prop(Room,Clutter,[floor,onTop(Furniture)]) ). %drop it to the floor

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
	
catAct :-
	cat(flushed),
	write('The cat swims against the current!'),nl,
	retract( cat(flushed) ),	
	assertz( cat(swimming) ),
	!. 
	
catAct :-
	cat(swimming),
	write('The cat climbs out through the kitchen sink!'),nl,
	retract( cat(swimming) ),	
	assertz( cat(kitchenSink) ),!.  
		
catAct :-
	cat(thrown),
	write('The cat lands gracefully on four paws!'),nl,
	retract( cat(thrown) ),	
	assertz( cat(outside) ),
	!. 
	
catAct :-
	cat(Furniture),
	furniture(Furniture),
	prop(Room,Furniture,_),	% there's some furniture in the room
	room(Room),				% we're in a room

	prop(Room,Clutter,Location),
	clutter(Clutter),		% we're trying to drop some clutter
	member(onTop(Furniture),Location),	%clutter is on top of the furniture
	% preconditions end
	!,
	dropClutter(Clutter), 
	catAct.

catAct :-
	cat(Furniture),
	furniture(Furniture),
	prop(Room,Furniture,_),	% there's some furniture in the room
	room(Room),				% we're in a room
	retract( cat(Furniture) ),
	assert( cat(Room) ),
	write('The cat jumps down from the '),write(Furniture),write('!'),nl,
	!,
	catAct.
	
% cat in room with something to drop
catAct :-
	cat(Room),
	\+gramma(Room),
	location(Room),	%cat is in some Location 
	prop(Room,Furniture,_), 
	furniture(Furniture),	%room contains furniture
	prop(Room,Clutter,Location),
	clutter(Clutter),		%room contains some clutter
	member(onTop(Furniture),Location),	%clutter is on top of furniture
	% preconditions end
	!,
	dropClutter(Furniture).
	%catAct.

% cat pathing to room	
catAct :-	
	cat(Room), 
	\+gramma(Room),
	location(Room),	%cat is in some Location 
	pathVia(Room,NextRoom,Path),
	\+gramma(NextRoom),
	prop(NextRoom,Furniture,_), 
	furniture(Furniture),	%room contains furniture
	prop(NextRoom,Clutter,Location),
	clutter(Clutter),		%room contains some clutter
	member(onTop(Furniture),Location),	%clutter is on top of furniture	
	%%%
	!,	
	catAdvance(Path),
	!.

	/*
% cat pathing to room	
catAct :-	
	cat(Room), 
	gramma(Room), %alt pathing with gramma in room
	location(Room),	%cat is in some Location 
	pathAlt(Room,NextRoom,Path),
	prop(NextRoom,Furniture,_), 
	furniture(Furniture),	%room contains furniture
	prop(NextRoom,Clutter,Location),
	clutter(Clutter),		%room contains some clutter
	member(onTop(Furniture),Location),	%clutter is on top of furniture	
	%%%
	!,	
	catAdvance(Path),
	!.
*/


catAdvance(Path) :-
	cat(Room), 
	location(Room),	%cat is in some room
%%%
	nth0(0,Path,Current),
	location(Current),
	%write('Cat at '),write(Current),nl,
%%%
	nth0(1,Path,Next),
	location(Next),
	%write('Cat heading to '),write(Next),nl,
%%%	
	last(Path,Target),
%%%
	write('Cat advances to '),write(Next),write(' on the way to '),write(Target),nl,
	retract( cat(Room) ),
	assert( cat(Next) ).
		
catAdvance(Path) :-
	cat(Room), 
	location(Room),	%cat is in some room 
%%%
	nth0(0,Path,Current),
	location(Current),
	%write('Cat at '),write(Current),nl,
%%%
	nth0(1,Path,Next),
	\+location(Next),
	%write('Cat heading to '),write(Next),nl,
%%%
	nth0(2,Path,Dest),
	location(Dest),
	%write('Cat heading to '),write(Dest),nl,
%%%	
	last(Path,Target),
%%%
	write('Cat jumps through '),write(Next),write(' and advances to '),write(Dest),nl,%write(' on the way to '),write(Target),nl,
	retract( cat(Room) ),
	assert( cat(Dest) ).			
	
	
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% gramma in room with the cat
grammaAct :-
	gramma(Room), 
	location(Room),	%granny is in some Location
	cat(Room),
	passageVia(Room,outside,W),
	window(W),
	!,
	write('Gramma throws the cat outside through '), write(W), write('!'), nl,
	retract( cat(Room) ),
	assert( cat(thrown) ),
	!,
	grammaAct.			 

grammaAct :-
	gramma(Room), 
	location(Room),	%granny is in some Location
	prop(Room,Furniture,_),	% there's some furniture in the room
	furniture(Furniture),
	cat(Furniture),
	passageVia(Room,outside,W),
	window(W),
	!,
	write('Gramma throws the cat outside through '), write(W), write('!'), nl,
	retract( cat(Furniture) ),
	assert( cat(thrown) ),
	!,
	grammaAct.			
	 
% gramma in the guest bathroom w the cat
grammaAct :-
	gramma(Room), 
	location(Room),	%granny is in some Location
	Room = bathroom,
	cat(Room),
	!,
	write('Gramma flushes the cat down the toilet!'), nl,
	retract( cat(Room) ),
	assert( cat(flushed) ),
	!,
	grammaAct.

grammaAct :-
	gramma(Room), 
	location(Room),	%granny is in some Location
	Room = bathroom,
	prop(Room,Furniture,_),	% there's some furniture in the room
	furniture(Furniture),
	cat(Furniture),
	!,
	write('Gramma flushes the cat down the toilet!'), nl,
	retract( cat(Furniture) ),
	assert( cat(flushed) ),
	!,
	grammaAct.	


% gramma in room with something to pick up
grammaAct :-
	gramma(Room), 
	location(Room),	%cat is in some Location 
	prop(Room,Furniture,_), 
	furniture(Furniture),	%room contains furniture
	prop(Room,Clutter,Location),
	clutter(Clutter),		%room contains some clutter
	member(nextTo(Furniture),Location),	%clutter is on top of furniture
	% preconditions end
	!,
	pickupClutter(Clutter),
	grammaAct.

% gramma next to room with sth to pick up
grammaAct :-	
	gramma(Room), 
	location(Room),	%cat is in some Location 
	passage(Room,NextRoom),
	prop(NextRoom,Furniture,_), 
	furniture(Furniture),	%room contains furniture
	prop(NextRoom,Clutter,Location),
	clutter(Clutter),		%room contains some clutter
	member(nextTo(Furniture),Location),	%clutter is on top of furniture	
	%%%
	!,
	write('Gramma advances to '),write(NextRoom),nl,
	retract( gramma(Room) ),
	assert( gramma(NextRoom) ).
	grammaAct.

% gramma pathing to room	
grammaAct :-	
	gramma(Room), 
	location(Room),	%cat is in some Location 
	pathDoor(Room,NextRoom,Path),
	prop(NextRoom,Furniture,_), 
	furniture(Furniture),	%room contains furniture
	prop(NextRoom,Clutter,Location),
	clutter(Clutter),		%room contains some clutter
	member(nextTo(Furniture),Location),	%clutter is on top of furniture	
	%%%
	!,
	
%	write('Gramma is heading to '),write(NextRoom),nl,
%	write('Gramma advances to '),write(NextRoom),write(' looking for the cat!'),nl,
%	retract( gramma(Room) ),
%	assert( gramma(NextRoom) ),
	
	grammaAdvance(Path).
	
	grammaAct.

grammaAdvance(Path) :-
	gramma(Room), 
	location(Room),	%cat is in some room
%%%
	nth0(0,Path,Current),
	location(Current),
	%write('Gramma at '),write(Current),nl,
%%%
	nth0(1,Path,Next),
	location(Next),
	%write('Gramma heading to '),write(Next),nl,
%%%	
	last(Path,Target),
%%%
	write('Gramma advances to '),write(Next),write(' on the way to '),write(Target),nl,
	retract( gramma(Room) ),
	assert( gramma(Next) ).
		
grammaAdvance(Path) :-
	gramma(Room), 
	location(Room),	%cat is in some room 
	%write('Gramma at (2) '),write(Room),nl,
%%%
	nth0(0,Path,Current),
	location(Current),
	%write('Gramma at (2) '),write(Current),nl,
%%%
	nth0(1,Path,Next),
	\+location(Next),
	%write('Gramma heading through '),write(Next),nl,
%%%
	nth0(2,Path,Dest),
	location(Dest),
	%write('Gramma heading to '),write(Dest),nl,
%%%	
	last(Path,Target),
	%write('Gramma on the way to '),write(Target),nl,
%%%
	write('Gramma passes through '),write(Next),write(' and advances to '),write(Dest),write(' on the way to '),write(Target),nl,
	retract( gramma(Room) ),
	assert( gramma(Dest) ).			
	
grammaAdvance(Path) :-
	.