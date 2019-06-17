## InverseCDF

```
InverseCDF(dist, q)
```

> returns the inverse cumulative distribution for the distribution `dist` as a function of `q` 


```
InverseCDF(dist, {x1, x2, ...})
```

> returns the inverse CDF evaluated at `{x1, x2, ...}` 

```
InverseCDF(dist)
```

> returns the inverse CDF as a pure function.


### Examples

 
```   
>> InverseCDF[NormalDistribution[0, 1], x]
ConditionalExpression(-Sqrt(2)*InverseErfc(2*#1),0<=#1<=1)&
```