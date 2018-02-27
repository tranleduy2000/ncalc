## FullForm

```
FullForm(expression) 
``` 

> shows the internal representation of the given `expression`.

### Examples

FullForm shows the difference in the internal expression representation:
```  
>> FullForm(x(x+1))
"x(Plus(1, x))"

>> FullForm(x*(x+1))
"Times(x, Plus(1, x))"
```