position(E,[E|_],1).
position(E,[_|C],P):-position(E,C,P1),P is P1+1.
verify(0,Pos):- Pos is 0,!.
verify(P,Pos):-(verId(P),verPos(P),Pos is P,!);(P1 is P-1, verify(P1,Pos)).
attack(Pos):-type(T),length(T,L),verify(L,Pos).
verPos(P):-type(T),element(P,T,Type),restypes(Tp),member(Type,Tp),position(Type,Tp,Pt),verProb(Pt).
verId(P):-myID(Id),owner(O),element(P,O,I),Id\== I,I\== null.
verProb(Pt):-random_prob(Rp),probabilities_attack(Pa),element(Pt,Pa,E),Rp<E.
posX(X):-attack(Pos),posx(LX),element(Pos,LX,X).
posY(Y):-attack(Pos),posy(LY),element(Pos,LY,Y).

%regole per la difesa
defense(Pos):-type(T),length(T,L),verifyD(L,Pos).
verifyD(0,Pos):-Pos is 0,!.
verifyD(P,Pos):-(verId(P),verPosD(P),Pos is P,!);(P1 is P-1,verifyD(P1,Pos)).
verPosD(P):-type(T),element(P,T,Type),restypes(Tp),member(Type,Tp),position(Type,Tp,Pt),verProbD(Pt).
verProbD(Pt):-random_probD(Rp),probabilities_defense(Pd),element(Pt,Pd,E),Rp<E.
posDX(X):-defense(Pos),posx(LX),element(Pos,LX,X).
posDY(Y):-defense(Pos),posy(LY),element(Pos,LY,Y).