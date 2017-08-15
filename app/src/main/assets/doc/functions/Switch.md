## Switch

```
Switch(expr, pattern1, value1, pattern2, value2, ...)
```

> yields the first `value` for which `expr` matches the corresponding pattern.
 
### Examples
```
>> Switch(2, 1, x, 2, y, 3, z)
y

>> Switch(5, 1, x, 2, y)
Switch(5, 1, x, 2, y)

>> Switch(5, 1, x, 2, y, _, z)
z
```

Switch called with 2 arguments. Switch must be called with an odd number of arguments.
```
>> Switch(2, 1)
Switch(2, 1)
```

Switch called with 2 arguments. Switch must be called with an odd number of arguments.
```
>> a; Switch(b, b)
Switch(b, b)
```

Switch called with 2 arguments. Switch must be called with an odd number of arguments.
```
>> z = Switch(b, b);
>> z

Switch(b, b)
```
