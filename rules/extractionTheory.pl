remaining_resources(N):-current_resources(A),accumulation_value(B),N is A- B.
depleted_resources:-remaining_resources(X),X=<0.
stop_extraction:-depleted_resources.
is_infinite:- infinite_resources(yes).