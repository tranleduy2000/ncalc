## Cancel
```
Cancel(expr)
```

> cancels out common factors in numerators and denominators.

### Examples
```
>> Cancel(x / x ^ 2)
1/x
```

'Cancel' threads over sums:
```
>> Cancel(x / x ^ 2 + y / y ^ 2)
1/x+1/y
 
>> Cancel(f(x) / x + x * f(x) / x ^ 2)
(2*f(x))/x
```