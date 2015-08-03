position(E,[E|_],1).
position(E,[_|C],P):-position(E,C,P1),P is P1+1.

exploration:- enough_exploration_money, verify_exploration_resources.

conquest:- enough_conquest_money, verify_conquest_resources.

enough_exploration_money:-current_money(C),required_exploration_money(R), C >=R.

enough_conquest_money:-current_money(C),required_conquest_money(R), C >=R.

verify_exploration_resources:- required_exploration_resources(R), length(R,L),verify_exp_element(L). 

verify_exp_element(0).
verify_exp_element(P):-nl,write(P),required_exploration_resources(R),required_exploration_qresources(Q),current_resources(Rris),current_qresources(Qris),element(P,R,E),element(P,Q,Eq),member(E,Rris),position(E,Rris,Pos),element(Pos,Qris,Rq),Rq>=Eq,P1 is P-1,verify_exp_element(P1).

verify_conquest_resources:-required_conquest_resources(R),length(R,L),verify_conquest_element(L).
verify_conquest_element(0).
verify_conquest_element(P):-nl,write(P),required_conquest_resources(R),required_conquest_qresources(Q),current_resources(Rris),current_qresources(Qris),element(P,R,E),element(P,Q,Eq),member(E,Rris),position(E,Rris,Pos),element(Pos,Qris,Rq),Rq>=Eq,P1 is P-1,verify_conquest_element(P1).

fase(F):-(conquest, F is 3,!);(exploration,F is 2,!);(F is 1,!).

radius(R):-fase(F), ((F =:=1,R is 0.3,!);(F =:=2,R is 0.7,!);(F=:=3, R is 1,!)).
probability_attack(P):-fase(F), ((F =:=1,P is 50,!);(F =:=2,P is 70,!);(F=:=3, P is 100,!)).
probability_movement(P):-fase(F),((F=:=1,P is 30);(F=:=2,P is 70,!);(F=:=3,P is 100,!)).
probability_buy(P):-fase(F),((F=:=1,P is 90);(F=:=2,P is 60,!);(F=:=3,P is 30,!)).