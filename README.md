# Graph-Matching
Identifying users across social networks

Code written in Augustin Chaintreau's COMS 6998 Social Networks at Columbia University, Fall 2014.

Task 1:
Given two graphs G1, G2, outputs all (u,v) pairs that predict u and v to represent the same person, where u \in G1 and v \in G2

Algorithm: Modification from this paper: http://www.vldb.org/pvldb/vol7/p377-korula.pdf


Task 2:
Given check in data (includes time and place) N1, N2, outputs all (u,v) pairs that predict u and v to represent the same person, where u \in user ids of N1 and v \in user ids of N2.

I implemented a time independent multinomiali model. For Erdos random graphs generated from real check in data, I empirically found an optimal parameter alpha* to be 0.00005, which gave 94% accuracy. See paper http://www.cs.bham.ac.uk/~musolesm/papers/cosn14.pdf