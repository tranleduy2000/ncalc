## Xor

```
Xor(arg1, arg2, ...)
```

> Logical XOR (exclusive OR) function. Returns `True` if an odd number of the arguments are `True` and the rest are `False`. Returns `False` if an even number of the arguments are `True` and the rest are `False`.

See: [Wikipedia: Exclusive or](https://en.wikipedia.org/wiki/Exclusive_or)
	
### Examples

```
>> Xor(False, True)
True
>> Xor(True, True)
False
```

If an expression does not evaluate to `True` or `False`, `Xor` returns a result in symbolic form:

```
>> Xor(a, False, b)
Xor(a,b)
>> Xor()
False
>> Xor(a)
a
>> Xor(False)
False
>> Xor(True)
True
>> Xor(a, b)
Xor(a,b)
```