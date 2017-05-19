## Distribute  

```
Distribute(f(x1, x2, x3,...))
```

> distributes `f` over `Plus` appearing in any of the `xi`.
 
See:  
* [Wikipedia - Distributive property](http://en.wikipedia.org/wiki/Distributive_property)
 
### Examples
```
>> Distribute((a+b)*(x+y+z))
a*x+a*y+a*z+b*x+b*y+B*z
```