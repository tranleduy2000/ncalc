## RealNumberQ
```
RealNumberQ(expr)
```
> returns `True` if `expr` is an explicit number with no imaginary component.

### Examples
```
>> RealNumberQ[10]
 = True
 
>> RealNumberQ[4.0]
 = True
 
>> RealNumberQ[1+I]
 = False
 
>> RealNumberQ[0 * I]
 = True
 
>> RealNumberQ[0.0 * I]
 = False
```

