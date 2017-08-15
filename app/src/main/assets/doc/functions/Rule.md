## Rule

```
Rule(x, y)

x -> y
```
> represents a rule replacing `x` with `y`.

### Examples

``` 
>> a+b+c /. c->d
a+b+d

>> {x,x^2,y} /. x->3
{3,9,y}
``` 

Rule called with 3 arguments; 2 arguments are expected.
```
>> a /. Rule(1, 2, 3) -> t 
a
```