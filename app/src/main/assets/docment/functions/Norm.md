## Norm
```
Norm(m, l)
```
> computes the `l`-norm of matrix `m` (currently only works for vectors!).   

```	
Norm(m)   
``` 
> computes the 2-norm of matrix `m` (currently only works for vectors!).     

### Examples
```   
>> Norm({1, 2, 3, 4}, 2)    
Sqrt(30)    

>> Norm({10, 100, 200}, 1)    
310    

>> Norm({a, b, c})
Sqrt(Abs(a)^2+Abs(b)^2+Abs(c)^2)    

>> Norm({-100, 2, 3, 4}, Infinity)    
100    

>> Norm(1 + I)    
Sqrt(2)    
```

The first Norm argument should be a number, vector, or matrix.  
```
>> Norm({1, {2, 3}})    
Norm({1, {2, 3}})    

>> Norm({x, y})    
Sqrt(Abs(x)^2+Abs(y)^2) 

>> Norm({x, y}, p)    
(Abs(x) ^ p + Abs(y) ^ p) ^ (1 / p)  
```

The second argument of Norm, 0, should be a symbol, Infinity, or an integer or real number not less than 1 for vector p-norms; or 1, 2, Infinity, or "Frobenius" for matrix norms.  
```
>> Norm({x, y}, 0)    
Norm({x, y}, 0)    
```

The second argument of Norm, 0.5, should be a symbol, Infinity, or an integer or real number not less than 1 for vector p-norms; or 1, 2, Infinity, or "Frobenius" for matrix norms. 
```
>> Norm({x, y}, 0.5)     
Norm({x, y}, 0.5)

>> Norm({})    
Norm({})

>> Norm(0)    
0    
```