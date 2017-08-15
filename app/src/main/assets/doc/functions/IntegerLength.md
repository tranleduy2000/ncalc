## IntegerLength

``` 
IntegerLength(x)
``` 
> gives the number of digits in the base-10 representation of `x`.

``` 
IntegerLength(x, b)
``` 
> gives the number of base-`b` digits in `x`.

### Examples

``` 
>> IntegerLength(123456)
6
 
>> IntegerLength(10^10000)
10001
 
>> IntegerLength(-10^1000)
1001
```

`IntegerLength` with base `2`:
```
>> IntegerLength(8, 2)
4
```

Check that `IntegerLength` is correct for the first 100 powers of 10:
```
>> IntegerLength /@ (10 ^ Range(100)) == Range(2, 101)
True
```

The base must be greater than `1`:
```
>> IntegerLength(3, -2)
IntegerLength(3, -2)
```
 
'0' is a special case:
```
>> IntegerLength(0)
0
 
>> IntegerLength /@ (10 ^ Range(100) - 1) == Range(1, 100)
True
```