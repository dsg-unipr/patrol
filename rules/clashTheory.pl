winner(ID1):-player1(ID1,Q1),player2(ID2,Q2),(Q1>Q2).
winner(ID2):-player1(ID1,Q1),player2(ID2,Q2),(Q1<Q2).
draw:-player1(_,Q1),player2(_,Q2),Q1=Q2.

