## Cross
```
Cross(a, b)
```
> computes the vector cross product of `a` and `b`.

See:
* [Wikipedia: Cross product](https://en.wikipedia.org/wiki/Cross_product)

### Examples
``` 
>> Cross({x1, y1, z1}, {x2, y2, z2})
{-y2*z1+y1*z2,x2*z1-x1*z2,-x2*y1+x1*y2}
 
>> Cross({x, y})
{-y,x}
``` 

The arguments are expected to be vectors of equal length, and the number of arguments are expected to be 1 less than their length.
``` 
>> Cross({1, 2}, {3, 4, 5})
Cross({1, 2}, {3, 4, 5})
``` 