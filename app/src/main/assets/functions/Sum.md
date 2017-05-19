## Sum

```
Sum(expr, {i, imin, imax})
```

> evaluates the discrete sum of `expr` with `i` ranging from `imin` to `imax`.

```
Sum(expr, {i, imin, imax, di})
```

> `i` ranges from `imin` to `imax` in steps of `di`.

```
Sum(expr, {i, imin, imax}, {j, jmin, jmax}, ...)
```
>> evaluates `expr` as a multiple sum, with `{i, ...}, {j, ...}, ...` being in outermost-to-innermost order.
		
### Examples
```
>> Sum(k, {k, 1, 10})    
55    
```

Double sum:   
``` 
>> Sum(i * j, {i, 1, 10}, {j, 1, 10})    
3025    
```

Symbolic sums are evaluated: 
```  
>> Sum(k, {k, 1, n})    
1/2*n*(1+n)

>> Sum(k, {k, n, 2*n})  
3/2*n*(1+n)

>> Sum(k, {k, I, I + 1})    
1+I*2   

>> Sum(1 / k ^ 2, {k, 1, n})    
HarmonicNumber(n, 2)    
```

Verify algebraic identities:    
```
>> Simplify(Sum(x ^ 2, {x, 1, y}) - y * (y + 1) * (2 * y + 1) / 6)   
0     
```
 
Infinite sums:    
```
>> Sum(1 / 2 ^ i, {i, 1, Infinity})    
1    
  
>> Sum(1 / k ^ 2, {k, 1, Infinity})    
Pi^2/6   

>> Sum(x^k*Sum(y^l,{l,0,4}),{k,0,4}))    
1+y+y^2+y^3+y^4+x*(1+y+y^2+y^3+y^4)+(1+y+y^2+y^3+y^4)*x^2+(1+y+y^2+y^3+y^4)*x^3+(1+y+y^2+y^3+y^4)*x^4  

>> Sum(2^(-i), {i, 1, Infinity})    
1    
 
>> Sum(i / Log(i), {i, 1, Infinity})    
Sum(i/Log(i),{i,1,Infinity})    

>> Sum(Cos(Pi i), {i, 1, Infinity})    
Sum(Cos(i*Pi),{i,1,Infinity})  
```