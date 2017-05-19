## Floor

```
Floor(expr)
```

> gives the smallest integer less than or equal `expr`. 

```
Floor(expr, a)
```

> gives the smallest multiple of `a` less than or equal to `expr`. 

### Examples

```
>> Floor(1/3)
0

>> Floor(-1/3)
-1

>> Floor(10.4)    
10    
 
>> Floor(10/3)    
3    
 
>> Floor(10)    
10    
 
>> Floor(21, 2)    
20    
 
>> Floor(2.6, 0.5)    
2.5    
 
>> Floor(-10.4)    
-11    
```

For complex `expr`, take the floor of real an imaginary parts.    
```
>> Floor(1.5 + 2.7*I)    
1+I*2   
```

For negative `a`, the smallest multiple of `a` greater than or equal to `expr` is returned.    
```
>> Floor(10.4, -1)    
11    
 
>> Floor(-10.4, -1)    
-10    
```