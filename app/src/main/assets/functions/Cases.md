## Cases

``` 
Cases(list, pattern)
```
> returns the elements of `list` that match `pattern`.

```
Cases(list, pattern, ls)
```
> returns the elements matching at levelspec `ls`.

### Examples
```
>> Cases({a, 1, 2.5, \"string\"}, _Integer|_Real)
{1,2.5}

>> Cases(_Complex)[{1, 2I, 3, 4-I, 5}]
{I*2,4-I}
 
>> Cases(1, 2)
{}
 
>> Cases(f(1, 2), 2)
{2}
 
>> Cases(f(f(1, 2), f(2)), 2)
{}
 
>> Cases(f(f(1, 2), f(2)), 2, 2)
{2,2}
 
>> Cases(f(f(1, 2), f(2), 2), 2, Infinity)
{2,2,2}
 
>> Cases({1, f(2), f(3, 3, 3), 4, f(5, 5)}, f(x__) :> Plus(x))
{2,9,10}
 
>> Cases({1, f(2), f(3, 3, 3), 4, f(5, 5)}, f(x__) -> Plus(x))
{2, 3, 3, 3, 5, 5}
```