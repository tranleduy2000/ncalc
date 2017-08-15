## BooleanQ

```
BooleanQ(expr) 
```

> returns `True` if `expr` is either `True` or `False`.

### Examples
 
```
>> BooleanQ(True)
True
>> BooleanQ(False)
True
>> BooleanQ(a)
False
>> BooleanQ(1 < 2)
True
>> BooleanQ("string")
False
>> BooleanQ(Together(x/y + y/x))
False
```