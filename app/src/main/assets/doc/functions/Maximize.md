## Maximize

```
Maximize(unary-function, variable) 
```

> returns the maximum of the unary function for the given `variable`.
	
See:
* [Wikipedia - Derivative test](https://en.wikipedia.org/wiki/Derivative_test)
	
### Examples
 
```
>> Maximize(-x^4-7*x^3+2*x^2 - 42,x) 
{-42-7*(-21/8-Sqrt(505)/8)^3+2*(21/8+Sqrt(505)/8)^2-(21/8+Sqrt(505)/8)^4,{x->-21/8-Sqrt(505)/8}}
```
				
Print a message if no maximum can be found

```
>> Maximize(x^4+7*x^3-2*x^2 + 42, x) 
{Infinity,{x->-Infinity}}
```

### Related terms 
[Minimize](Minimize.md) 