## Subdivide

```
Subdivide(n)
```

> returns a list with `n+1` entries obtained by subdividing the range `0` to `1`.

```
Subdivide(to, n)
```

> returns a list with `n+1` entries obtained by subdividing the range `0` to `to`.

```
Subdivide(from, to, n)
```

> returns a list with `n+1` entries obtained by subdividing the range `from` to `to`.

### Examples

```
>> Subdivide(5)
{0,1/5,2/5,3/5,4/5,1}

>> Subdivide(10, 4)
{0,5/2,5,15/2,10}

>> Subdivide(-1, -4, 3)
{-1,-2,-3,-4}

>> Subdivide({10,5}, {5,15}, 4)
{{10,5},{35/4,15/2},{15/2,10},{25/4,25/2},{5,15}}
```