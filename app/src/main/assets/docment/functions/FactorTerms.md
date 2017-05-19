## FactorTerms 

```
FactorTerms(poly)
``` 

> pulls out any overall numerical factor in `poly`.

### Examples
```  
>> FactorTerms(3+3/4*x^3+12/17*x^2, x)
3/68*(17*x^3+16*x^2+68)
```