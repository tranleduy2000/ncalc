## Normalize

```
Normalize(v)
```

> calculates the normalized vector `v` as `v/Norm(v)`.

```
Normalize(z)
```

> calculates the normalized complex number `z`.

```
Normalize(v, f)
```

> calculates the normalized vector `v` and use the function `f` as the norm. Default value is the predefined function `Norm`.

See: 
* [Wikipedia - Unit vector](https://en.wikipedia.org/wiki/Unit_vector)

### Examples
```
>> Normalize({1, 1, 1, 1})
{1/2,1/2,1/2,1/2}

>> Normalize(1 + I)
(1+I)/Sqrt(2) 

>> Normalize(0)
0

>> Normalize({0})
{0}

>> Normalize({})
{}

>> Normalize({x,y}, f)
{x/f({x,y}),y/f({x,y})}
```