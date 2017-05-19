## NHoldAll

```
NHoldAll
```

> is an attribute that protects all arguments of a function from numeric evaluation.
    
### Examples
```	
>> N(f(2, 3))    
f(2.0,3.0)   
 
>> SetAttributes(f, NHoldAll)    
>> N(f(2, 3))    
f(2,3)    
```