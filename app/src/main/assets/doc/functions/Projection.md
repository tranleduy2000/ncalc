## Projection
```
Projection(vector1, vector2)
```
> Find the orthogonal projection of `vector1` onto another `vector2`.
 
```
Projection(vector1, vector2, ipf)
```
> Find the orthogonal projection of `vector1` onto another `vector2` using the inner product function `ipf`.

### Examples
``` 
>> Projection({5, I, 7}, {1, 1, 1})
{4+I*1/3,4+I*1/3,4+I*1/3}
``` 