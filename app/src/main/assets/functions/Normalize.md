## Normalize
```
Normalize(v)
```
> calculates the normalized vector `v`.

```
Normalize(z)
```
> calculates the normalized complex number `z`.

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
```