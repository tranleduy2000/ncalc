## EuclideanDistance
```
EuclideanDistance(u, v)
```

> returns the euclidean distance between `u` and `v`.

### Examples
```
>> EuclideanDistance({-1, -1}, {1, 1})
2*Sqrt(2)

>> EuclideanDistance({a, b}, {c, d})
Sqrt(Abs(a-c)^2+Abs(b-d)^2) 
```