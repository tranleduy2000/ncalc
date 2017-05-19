## Ceiling

```
Ceiling(expr)
```

> gives the first integer greater than or equal `expr`. 

```
Ceiling(expr, a)
```

> gives the first multiple of `a` greater than or equal to `expr`. 

### Examples

```
>> Ceiling(1/3)
1
 
>> Ceiling(-1/3)
0

>> Ceiling(1.2)    
2    
 
>> Ceiling(3/2)    
2    
```

For complex `expr`, take the ceiling of real and imaginary parts.   
``` 
>> Ceiling(1.3 + 0.7*I)    
2+I    

>> Ceiling(10.4, -1)    
10    
  
>> Ceiling(-10.4, -1)    
-11
```