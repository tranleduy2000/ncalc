## Piecewise

```  
Piecewise({{expr1, cond1}, ...})
```   
> represents a piecewise function. 

```  
Piecewise({{expr1, cond1}, ...}, expr)
```   
> represents a piecewise function with default `expr`.

See:

* [Wikipedia - Piecewise](http://en.wikipedia.org/wiki/Piecewise)

### Examples

```   
>> Piecewise({{-x, x<0}, {x, x>=0}})/.{{x->-3}, {x->-1/3}, {x->0}, {x->1/2}, {x->5}}
{3,1/3,0,1/2,5}
```  

Heaviside function
```
>> Piecewise({{0, x <= 0}}, 1)    
Piecewise({{0, x <= 0}}, 1)         
```

Piecewise defaults to `0`, if no other case is matching.    
```
>> Piecewise({{1, False}})    
0    
 	
>> Piecewise({{0 ^ 0, False}}, -1)    
-1    
```