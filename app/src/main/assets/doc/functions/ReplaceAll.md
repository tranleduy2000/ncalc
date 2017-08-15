## ReplaceAll

```
ReplaceAll(expr, i -> new)
```
or
```
expr /. i -> new
```
> replaces all `i` in `expr` with `new`.

```
ReplaceAll(expr, {i1 -> new1, i2 -> new2, ... } )
```
or
```
expr /. {i1 -> new1, i2 -> new2, ... }
```
> replaces all `i`s in `expr` with `new`s.
 
## Examples
```
>> f(a) + f(b) /. f(x_) -> x^2
a^2+b^2
```
