## PossibleZeroQ

```
PossibleZeroQ(expr)
```

> maps a (possible) zero `expr` to `True` and returns `False` otherwise.

 
### Examples

```
>> PossibleZeroQ((E + Pi)^2 - E^2 - Pi^2 - 2*E*Pi)
True

>> PossibleZeroQ(Sqrt(x^2) - x)
False
```