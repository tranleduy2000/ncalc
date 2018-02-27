## Level
``` 
Level(expr, levelspec)
```
> gives a list of all sub-expressions of `expr` at the level(s) specified by `levelspec`.

Level uses standard level specifications:

``` 
n
```
> levels `1` through `n`

```
Infinity
```
> all levels from level `1`


```
{n}
```
> level `n` only

```
{m, n}
```
> levels `m` through `n`

Level 0 corresponds to the whole expression.
A negative level `-n` consists of parts with depth `n`.

### Examples

Level `-1` is the set of atoms in an expression:

```
>> Level(a + b ^ 3 * f(2 x ^ 2), {-1})
{a,b,3,2,x,2}
 
>> Level({{{{a}}}}, 3)
{{a},{{a}},{{{a}}}} 
 
>> Level({{{{a}}}}, -4)
{{{{a}}}}
 
>> Level({{{{a}}}}, -5)
{}
 
>> Level(h0(h1(h2(h3(a)))), {0, -1})
{a,h3(a),h2(h3(a)),h1(h2(h3(a))),h0(h1(h2(h3(a))))} 
```

Use the option `Heads -> True` to include heads:

```
>> Level({{{{a}}}}, 3, Heads -> True)
{List,List,List,{a},{{a}},{{{a}}}} 
 
>> Level(x^2 + y^3, 3, Heads -> True)
{Plus,Power,x,2,x^2,Power,y,3,y^3} 
 
>> Level(a ^ 2 + 2 * b, {-1}, Heads -> True)
{Plus,Power,a,2,Times,2,b} 
 
>> Level(f(g(h))[x], {-1}, Heads -> True)
{f,g,h,x}
 
>> Level(f(g(h))[x], {-2, -1}, Heads -> True)
{f,g,h,g(h),x,f(g(h))[x]} 
```