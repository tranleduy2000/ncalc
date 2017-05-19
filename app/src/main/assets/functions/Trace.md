## Trace

```
Trace(expr)
```

> return the evaluation steps which are used to get the result.
 
### Examples
```
>> Trace(D(Sin(x),x))
{{Cos(#1)&[x],Cos(x)},D(x,x)*Cos(x),{D(x,x),1},1*Cos(x),Cos(x)}
```
