## Erf

```
Erf(z)
```

> returns the error function of `z`.
 
### Examples
`Erf(z)` is an odd function:
```  
>> Erf(-x)
-Erf(x)
 
>> Erf(1.0)
0.8427007929497151
 
>> Erf(0)
0
 
>> {Erf(0, x), Erf(x, 0)}
{Erf(x),-Erf(x)}
```