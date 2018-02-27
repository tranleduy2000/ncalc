## RotateRight

```
RotateRight(list)
```

> rotates the items of `list` by one item to the right.
 
```
RotateRight(list, n)
```

> rotates the items of `list` by `n` items to the right.

### Examples
```
>> RotateRight({1, 2, 3})
{3,1,2}

>> RotateRight(Range(10), 3)
{8,9,10,1,2,3,4,5,6,7}

>> RotateRight(x(a, b, c), 2)
x(b,c,a)
```