## Constant

```
Constant
```

> is an attribute that indicates that a symbol is a constant.
		
### Examples
```	  
Mathematical constants like `E` have attribute `Constant`:
```    
>> Attributes(E)    
{Constant}
``` 

Constant symbols cannot be used as variables in `Solve` and related functions.

E is a constant symbol and not a valid variable.  
```
>> Solve(x + E == 0, E)     
Solve(E+x==0,E)  
```