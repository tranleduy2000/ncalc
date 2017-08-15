## Derivative

```
Derivative(n)[f]
```
> represents the `n`-th derivative of the function `f`.   

```
Derivative(n1, n2, n3,...)[f]
```
> represents a multivariate derivative.

### Examples
``` 
>> Derivative(1)[Sin]    
Cos(#1)&    
 
>> Derivative(3)[Sin]    
-Cos(#1)&   
 
>> Derivative(2)[# ^ 3&]    
6*(#1&)    
``` 

`Derivative` can be entered using `'`:   
```  
>> Sin'(x)    
Cos(x)    
 
>> (# ^ 4&)''    
12*(#1^2&)   
 
>> f'(x) // FullForm    
"Derivative(1)[f][x]"  
``` 

The `0`th derivative of any expression is the expression itself: 
```    
>> Derivative(0,0,0)[a+b+c]    
a+b+c    
``` 

Unknown derivatives:    
``` 
>> Derivative(2, 1)[h]    
Derivative(2,1)[h]   
 
>> Derivative(2, 0, 1, 0)[h(g)]    
Derivative(2,0,1,0)[h(g)] 
``` 
