## PseudoInverse
```
PseudoInverse(matrix)
```

> computes the Moore-Penrose pseudoinverse of the `matrix`. If `matrix` is invertible, the pseudoinverse equals the inverse.

See:
* [Wikipedia: Moore-Penrose pseudoinverse](https://en.wikipedia.org/wiki/Moore%E2%80%93Penrose_pseudoinverse)
 
### Examples

```
>> PseudoInverse({{1, 2}, {2, 3}, {3, 4}})
{{1.0000000000000002,2.000000000000001},
 {1.9999999999999976,2.999999999999996},
 {3.000000000000001,4.0}}
>> PseudoInverse({{1, 2, 0}, {2, 3, 0}, {3, 4, 1}})
{{-2.999999999999998,1.9999999999999967,4.440892098500626E-16},
 {1.999999999999999,-0.9999999999999982,-2.7755575615628914E-16},
 {0.9999999999999999,-1.9999999999999991,1.0}}
>> PseudoInverse({{1.0, 2.5}, {2.5, 1.0}}) 
{{-0.19047619047619038,0.47619047619047616},
 {0.47619047619047616,-0.1904761904761904}}
```

Argument {1, {2}} at position 1 is not a non-empty rectangular matrix.
``` 
>> PseudoInverse({1, {2}})
PseudoInverse({1, {2}})
```