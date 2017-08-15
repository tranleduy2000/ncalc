## AtomQ
``` 
AtomQ(x)
```
 
> is true if `x` is an atom (an object such as a number or string, which cannot be divided into subexpressions using 'Part').

### Examples
``` 
>> AtomQ(x)
True
 
>> AtomQ(1.2)
True
 
>> AtomQ(2 + I)
True
 
>> AtomQ(2 / 3)
True
 
>> AtomQ(x + y)
False
``` 