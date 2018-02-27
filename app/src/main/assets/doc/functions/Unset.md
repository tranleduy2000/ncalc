## Unset

```
Unset(expr)
```

or

```
expr =.
```
> removes any definitions belonging to the left-hand-side `expr`.

### Examples
 
```
>> a = 2
2

>> a =.

>> a
a
```

Unsetting an already unset or never defined variable will not change anything:

```
>> a =.

>> b =.
```

`Unset` can unset particular function values. It will print a message if no corresponding rule is found.

```
>> f[x_) =.
Assignment not found for: f(x_)

>> f(x_) := x ^ 2

>> f(3)
9

>> f(x_) =.

>> f(3)
f(3)
```