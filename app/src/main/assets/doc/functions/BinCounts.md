## BinCounts

```
BinCounts(list, width-of-bin)
```

or 

```
BinCounts(list, {min, max, width-of-bin} )
```

> count the number of elements, if `list`, is divided into successive bins with width `width-of-bin`.

### Examples

```
>> BinCounts({1,2,3,4,5},5) 
{4,1}

>> BinCounts({1,2,3,4,5},10) 
{5}
```