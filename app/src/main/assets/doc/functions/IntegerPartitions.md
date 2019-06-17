## IntegerPartitions

```
IntegerPartitions(n)
```

> returns all partitions of the integer `n`.
 
```
IntegerPartitions(n, k)
```

> lists the possible ways to partition `n` into smaller integers, using up to `k` elements.

### Examples

``` 
>> IntegerPartitions(3)
{{3},{2,1},{1,1,1}}

>> IntegerPartitions(10,2)
{{10},{9,1},{8,2},{7,3},{6,4},{5,5}}
```