position(E,[E|_],1).
position(E,[_|C],P):-position(E,C,P1),P is P1+1.
res_cost(C):-resource(X,C,_,_).
exist_resource:-resource(X,_,_,_),resource_req(X).
res_requirements(L):-resource(X,_,L,_),resource_req(X).
res_quantities(Q):-resource(X,_,_,Q),resource_req(X).
enough_money:-current_money(Q),res_cost(P),Q>=P.
verify_element(0).
verify_element(P):-res_requirements(L),res_quantities(Q),current_resources(Lris),current_resourcesQ(Qris),element(P,L,E),element(P,Q,Eq),member(E,Lris),position(E,Lris,Pos),element(Pos,Qris,Rq),Rq>=Eq,P1 is P-1,verify_element(P1).
verifyreq:-res_requirements(L),length(L,Length),verify_element(Length).
buy_resource:-exist_resource,enough_money,verifyreq,!.