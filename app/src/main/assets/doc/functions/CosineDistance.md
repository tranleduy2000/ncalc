## CosineDistance
```
CosineDistance(u, v)
```
> returns the cosine distance between `u` and `v`.
  
### Examples
```
>> N(CosineDistance({7, 9}, {71, 89}))
7.596457213221441E-5
 
>> CosineDistance({a, b}, {c, d})
1-(a*c+b*d)/(Sqrt(Abs(a)^2+Abs(b)^2)*Sqrt(Abs(c)^2+Abs(d)^2))  
```