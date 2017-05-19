## Part
```  
Part(expr, i)
```  
or
```  
expr[[i]]
```  
> returns part `i` of `expr`. 

Extract an element from a list:
```  
>> A = {a, b, c, d}
>> A[[3]]
c
```  

Negative indices count from the end:
```  
>> {a, b, c}[[-2]]
b
```   

`Part` can be applied on any expression, not necessarily lists.
```  
>> (a + b + c)[[2]]
b
```  

`expr[[0]]` gives the head of `expr`:
```  
>> (a + b + c)[[0]]
Plus
```  

Parts of nested lists:
```  
>> M = {{a, b}, {c, d}}
>> M[[1, 2]]
b
```  

You can use `Span` to specify a range of parts:
```  
>> {1, 2, 3, 4}[[2;;4]]
{2,3,4}
 
>> {1, 2, 3, 4}[[2;;-1]]
{2,3,4}
```  

A list of parts extracts elements at certain indices:
```  
>> {a, b, c, d}[[{1, 3, 3}]]
{a,c,c}
```  
 
Get a certain column of a matrix:
```  
>> B = {{a, b, c}, {d, e, f}, {g, h, i}}
>> B[[;;, 2]]
{b, e, h}
```  

Extract a submatrix of 1st and 3rd row and the two last columns:
```  
>> B = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}
>> B[[{1, 3}, -2;;-1]]
{{2,3},{8,9}}
```  

Further examples:
```  
>> (a+b+c+d)[[-1;;-2]]
0
```

Part specification is longer than depth of object.
```
>> x[[2]] 
x[[2]]
```  

Assignments to parts are possible:
```  
>> B[[;;, 2]] = {10, 11, 12}
{10, 11, 12}
 
>> B
{{1, 10, 3}, {4, 11, 6}, {7, 12, 9}}
 
>> B[[;;, 3]] = 13
13
 
>> B
{{1, 10, 13}, {4, 11, 13}, {7, 12, 13}}
 
>> B[[1;;-2]] = t
>> B
{t,t,{7,12,13}}
 
>> F = Table(i*j*k, {i, 1, 3}, {j, 1, 3}, {k, 1, 3})
>> F[[;; All, 2 ;; 3, 2]] = t
>> F
{{{1,2,3},{2,t,6},{3,t,9}},{{2,4,6},{4,t,12},{6,t,18}},{{3,6,9},{6,t,18},{9,t,27}}} 
 
>> F[[;; All, 1 ;; 2, 3 ;; 3]] = k
>> F
{{{1,2,k},{2,t,k},{3,t,9}},{{2,4,k},{4,t,k},{6,t,18}},{{3,6,k},{6,t,k},{9,t,27}}}
```  

Of course, part specifications have precedence over most arithmetic operations:
```  
>> A[[1]] + B[[2]] + C[[3]] // Hold // FullForm
"Hold(Plus(Plus(Part(A, 1), Part(B, 2)), Part(C, 3)))"
 
>> a = {2,3,4}; i = 1; a[[i]] = 0; a
{0, 3, 4}
```   

Negative step
```  
>> {1,2,3,4,5}[[3;;1;;-1]]
{3,2,1}
 
>> {1, 2, 3, 4, 5}[[;; ;; -1]]       
{5, 4, 3, 2, 1}
 
>> Range(11)[[-3 ;; 2 ;; -2]]
{9,7,5,3}
 
>> Range(11)[[-3 ;; -7 ;; -3]]
{9,6}
 
>> Range(11)[[7 ;; -7;; -2]]
{7,5}
```  

Cannot take positions `1` through `3` in `{1, 2, 3, 4}`.
```
>> {1, 2, 3, 4}[[1;;3;;-1]]
{1,2,3,4}[[1;;3;;-1]]
```

Cannot take positions `3` through `1` in `{1, 2, 3, 4}`.
```
>> {1, 2, 3, 4}[[3;;1]]
{1,2,3,4}[[3;;1]]
```