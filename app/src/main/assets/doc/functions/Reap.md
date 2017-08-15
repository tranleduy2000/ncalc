## Reap

```
Reap(expr)
```

> gives the result of evaluating `expr`, together with all values sown during this evaluation. Values sown with different tags are given in different lists.

### Examples  
```
>> Reap(Sow(3); Sow(1))
{1,{{3,1}}}
```
