## D

``` 
D(f, x)
``` 
> gives the partial derivative of `f` with respect to `x`. 


``` 
D(f, x, y, ...)
``` 
> differentiates successively with respect to `x`, `y`, etc. 

``` 
D(f, {x,n})
```  
> gives the multiple derivative of order `n`.  
  
``` 
D(f, {{x1, x2, ...}})
``` 
> gives the vector derivative of `f` with respect to `x1`, `x2`, etc.
		
**Note**: the upper case identifier `D` is different from the lower case identifier `d`.
 
### Examples
First-order derivative of a polynomial:    
```
>> D(x^3 + x^2, x)   
2*x+3*x^2  
```

Second-order derivative: 
```   
>> D(x^3 + x^2, {x, 2})    
2+6*x  
```

Trigonometric derivatives:   
``` 
>> D(Sin(Cos(x)), x)    
-Cos(Cos(x))*Sin(x) 
 
>> D(Sin(x), {x, 2})    
-Sin(x)    
 
>> D(Cos(t), {t, 2})    
-Cos(t)    
```

Unknown variables are treated as constant: 
```   
>> D(y, x)    
0    
 
>> D(x, x)    
1    
 
>> D(x + y, x)    
1    
```

Derivatives of unknown functions are represented using 'Derivative':    
```
>> D(f(x), x)    
f'(x)    
 
>> D(f(x, x), x)    
Derivative(0,1)[f][x,x]+Derivative(1,0)[f][x,x]   
```

Chain rule:    
```
>> D(f(2*x+1, 2*y, x+y)    
2*Derivative(1,0,0)[f][1+2*x,2*y,x+y]+Derivative(0,0,1)[f][1+2*x,2*y,x+y]    
 
>> D(f(x^2, x, 2*y), {x,2}, y) // Expand    
2*Derivative(0,2,1)[f][x^2,x,2*y]+4*Derivative(1,0,1)[f][x^2,x,2*y]+8*x*Derivative(
1,1,1)[f][x^2,x,2*y]+8*x^2*Derivative(2,0,1)[f][x^2,x,2*y] 
```

Compute the gradient vector of a function:   
``` 
>> D(x ^ 3 * Cos(y), {{x, y}})   
{3*x^2*Cos(y),-x^3*Sin(y)}  
```

Hesse matrix:    
```
>> D(Sin(x) * Cos(y), {{x,y}, 2})    
{{-Cos(y)*Sin(x),-Cos(x)*Sin(y)},{-Cos(x)*Sin(y),-Cos(y)*Sin(x)}}  
 
>> D(2/3*Cos(x) - 1/3*x*Cos(x)*Sin(x) ^ 2,x)//Expand    
1/3*x*Sin(x)^3-1/3*Sin(x)^2*Cos(x)-2/3*Sin(x)-2/3*x*Cos(x)^2*Sin(x)
 
>> D(f(#1), {#1,2})    
f''(#1)   
 
>> D((#1&)(t),{t,4})    
0    
 
>> Attributes(f) = {HoldAll}; Apart(f''(x + x))    
f''(2*x)  
 
>> Attributes(f) = {}; Apart(f''(x + x))    
f''(2*x)  
  
>> D({#^2}, #)
{2*#1}
```