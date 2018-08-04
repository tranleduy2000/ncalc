## FindFit  

```
FindFit(list-of-data-points, function, parameters, variable)
```
 
> solve a least squares problem using the Levenberg-Marquardt algorithm.
   
See:  
* [Wikipedia - Levenberg–Marquardt algorithm](https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm) 
 
### Examples

```
>> FindFit({{15.2,8.9},{31.1,9.9},{38.6,10.3},{52.2,10.7},{75.4,11.4}}, a*Log(b*x), {a, b}, x)
{a->1.54503,b->20.28258}

>> FindFit({{1,1},{2,4},{3,9},{4,16}}, a+b*x+c*x^2, {a, b, c}, x)
{a->0.0,b->0.0,c->1.0}
```

The default initial guess in the following example for the parameters `{a,w,f}` is `{1.0, 1.0, 1.0}`.
These initial values give a bad result:

```
>> FindFit(Table({t, 3*Sin(3*t + 1)}, {t, -3, 3, 0.1}), a* Sin(w*t + f), {a,w,f}, t)
{a->0.6688,w->1.49588,f->3.74845}
```

The initial guess `{2.0, 1.0, 1.0}` gives a much better result:

```
>> FindFit(Table({t, 3*Sin(3*t + 1)}, {t, -3, 3, 0.1}), a* Sin(w*t + f), {{a, 2}, {w,1}, {f,1}}, t)
{a->3.0,w->3.0,f->1.0}
```

You can omit `1.0` in the parameter list because it's the default value:

```
>> FindFit(Table({t, 3*Sin(3*t + 1)}, {t, -3, 3, 0.1}), a* Sin(w*t + f), {{a, 2}, w, f}, t)
{a->3.0,w->3.0,f->1.0}
```