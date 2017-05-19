## PowerMod
```
PowerMod(x, y, m)
```

> computes `x^y` modulo `m`.

	
### Examples
```
>> PowerMod(2, 10000000, 3)
1
>> PowerMod(3, -2, 10)
9
```

0 is not invertible modulo 2.
```
>> PowerMod(0, -1, 2)
PowerMod(0, -1, 2)
```
The argument 0 should be nonzero.
```
>> PowerMod(5, 2, 0)
 PowerMod(5, 2, 0)
```