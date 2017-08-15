## NMaximize
```
NMaximize(maximize_function, constraints, variables_list)
```

> the `NMaximize` function provides an implementation of [George Dantzig's simplex algorithm](http://en.wikipedia.org/wiki/Simplex_algorithm) for solving linear optimization problems with linear equality and inequality constraints and implicit non-negative variables.

See:  
* [Wikipedia - Linear programming](http://en.wikipedia.org/wiki/Linear_programming)
 
See also: [LinearProgramming](LinearProgramming.md), [NMinimize](NMinimize.md)

### Examples	
```
>> NMaximize({-2*x+y-5, x+2*y<=6 && 3*x + 2*y <= 12 }, {x, y})
{-2.0,{x->0.0,y->3.0}}
```

solves the linear problem:
```
Maximize -2x + y - 5
```

with the constraints:
```
  x  + 2y <=  6
  3x + 2y <= 12
        x >= 0
		y >= 0
```