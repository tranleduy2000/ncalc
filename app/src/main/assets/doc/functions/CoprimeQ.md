## CoprimeQ
```
CoprimeQ(x, y)
```

> tests whether `x` and `y` are coprime by computing their greatest common divisor.

See:
* [Wikipedia - Coprime](http://en.wikipedia.org/wiki/Coprime)

### Examples
```
>> CoprimeQ(7, 9)
True
>> CoprimeQ(-4, 9)
True
>> CoprimeQ(12, 15)
False 
>> CoprimeQ(2, 3, 5)
True
>> CoprimeQ(2, 4, 5)
False
```