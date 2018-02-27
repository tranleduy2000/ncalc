## Ordering
```
Ordering(list)
```

> calculate the permutation list of the elements in the sorted `list`.

```
Ordering(list, n)
```

> calculate the first `n` indexes of the  permutation list of the elements in the sorted `list`.

```
Ordering(list, -n)
```

> calculate the last `n` indexes of the  permutation list of the elements in the sorted `list`.


```
Ordering(list, n, head)
```

> calculate the first `n` indexes of the  permutation list of the elements in the sorted `list` using comparator operation `head`.

### Examples
```
>> Ordering({1,3,4,2,5,9,6})
{1,4,2,3,5,7,6}

>> Ordering({1,3,4,2,5,9,6}, All, Greater)
{6,7,5,3,2,4,1}
```