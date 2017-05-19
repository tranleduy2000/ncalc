## InverseErf

```
InverseErf(z)
```

> returns the inverse error function of `z`.
 
### Examples
`InverseErf(z)` is an odd function:
```  
>> InverseErf /@ {-1, 0, 1}    
{-Infinity, 0, Infinity}     
``` 

'InverseErf($z$)' only returns numeric values for '-1 <= $z$ <= 1':    
``` 
>> InverseErf /@ {0.9, 1.0, 1.1}    
{1.1630871536766743,Infinity,InverseErf(1.1)} 
```