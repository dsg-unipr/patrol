%list_players([id,id2,id3,id4]).
%list_conquered_planets([4,6,3,3]).
%total_planets(20).
%calcolo massimo
max([X],X).
max([X|Xs],X):- max(Xs,Y), X >=Y.
max([X|Xs],N):- max(Xs,N), N > X.
%calcolo posizione
position(E,[E|_],1).
position(E,[_|C],P):-position(E,C,P1),P is P1+1.
%calcolo ripetizioni di un numero in una lista
occurrence(_X,[],0).
occurrence(X,[X|R],N):-occurrence(X,R,N1), N is N1 +1,!.
occurrence(X,[_Y|R],N):-occurrence(X,R,N).
%calcolo somma di una lista
sum_list(0,[]). 
sum_list(S,[H|T]):-sum_list(N,T),S is H+N.

total_conquered_planets(S):-list_conquered_planets(L),sum_list(S,L).

all_planets_conquered:-total_conquered_planets(S),total_planets(T),T=S.

pos_winner(P):-list_conquered_planets(L),max(L,M),position(M,L,P).

winner(W):-list_players(L),pos_winner(P),element(P,L,W).

onewinner:-list_conquered_planets(L),max(L,M),occurrence(M,L,O),O=1.

gameover(W):-all_planets_conquered,onewinner,winner(W).