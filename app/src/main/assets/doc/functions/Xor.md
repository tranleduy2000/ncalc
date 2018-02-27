## Xor

```
Xor(expr1, expr2, ...)
```

> `Xor(expr1, expr2, ...)` evaluates each expression in turn, returning `True` as soon as not all expressions evaluate to the same value. If all expressions evaluate to the same value, `Xor` returns `False`.
	
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