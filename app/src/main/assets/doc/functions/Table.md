## Table

```
Table(expr, {i, n})
```
> evaluates `expr` with `i` ranging from `1` to `n`, returning a list of the results.

```
Table(expr, {i, start, stop, step})
```
> evaluates `expr` with `i` ranging from `start` to `stop`, incrementing by `step`.

```
Table(expr, {i, {e1, e2, ..., ei}})
```
> evaluates `expr` with `i` taking on the values `e1, e2, ..., ei`.

### Examples
```
>> Table(x!, {x, 8})
{1,2,6,24,120,720,5040,40320}

>> Table(x, {4})
{x,x,x,x}
 
>> n=0
>> Table(n= n + 1, {5})
{1,2,3,4,5}
 
>> Table(i, {i, 4})
{1,2,3,4}
 
>> Table(i, {i, 2, 5})
{2,3,4,5}
 
>> Table(i, {i, 2, 6, 2})
{2,4,6}
 
>> Table(i, {i, Pi, 2*Pi, Pi / 2})
{Pi,3/2*Pi,2*Pi} 
 
>> Table(x^2, {x, {a, b, c}})
{a^2,b^2,c^2} 
```

`Table` supports multi-dimensional tables:
```
>> Table({i, j}, {i, {a, b}}, {j, 1, 2})
{{{a,1},{a,2}},{{b,1},{b,2}}} 
 
>> Table(x, {x,0,1/3})
{0}
 
>> Table(x, {x, -0.2, 3.9})
{-0.2,0.8,1.8,2.8,3.8} 
```