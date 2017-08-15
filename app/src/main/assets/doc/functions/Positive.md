## Positive

```
Positive(x)  
```

> returns `True` if `x` is a positive real number.
	
### Examples
 
```
>> Positive(1)
True
```

`Positive` returns `False` if `x` is zero or a complex number:
```
>> Positive(0)
False

>> Positive(1 + 2 I)
False

>> Positive(Pi)
True

>> Positive(x)
Positive(x)

>> Positive(Sin({11, 14}))
{False, True}
``` 
 
