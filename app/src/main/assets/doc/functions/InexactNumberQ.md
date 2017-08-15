## InexactNumberQ
```
InexactNumberQ(expr)
```
> returns `True` if `expr` is not an exact number, and `False` otherwise.

### Examples
```
>> InexactNumberQ(a)
False
 
>> InexactNumberQ(3.0)
True
 
>> InexactNumberQ(2/3)
False
```

`InexactNumberQ` can be applied to complex numbers:
```
>> InexactNumberQ(4.0+I)    
True
```
