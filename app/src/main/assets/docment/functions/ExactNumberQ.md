## ExactNumberQ
```
ExactNumberQ(expr)
```
> returns `True` if `expr` is an exact number, and `False` otherwise.

### Examples
```
>> ExactNumberQ(10)
True
 
>> ExactNumberQ(4.0)
False
 
>> ExactNumberQ(n)
False
 
>> ExactNumberQ(1+I)    
True

>> ExactNumberQ(1 + 1. * I)
False    
```